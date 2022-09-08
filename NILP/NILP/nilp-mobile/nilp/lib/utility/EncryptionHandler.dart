import 'dart:convert';
import 'dart:developer';
import 'dart:typed_data';
import 'package:pointycastle/block/aes_fast.dart';
import 'package:pointycastle/block/modes/cbc.dart';
import 'package:pointycastle/digests/sha1.dart';
import 'package:pointycastle/key_derivators/pbkdf2.dart';
import 'package:pointycastle/macs/hmac.dart';
import 'package:pointycastle/paddings/pkcs7.dart';
import 'package:pointycastle/pointycastle.dart';
import 'package:pointycastle/random/fortuna_random.dart';

const KEY_SIZE = 16;
const ITERATION_COUNT = 5;

class EncryptionHandler {
  static const CBC_MODE = 'AES';
  String SECRET_KEY = "QD32VdbRuMa0iI0q9q7cH6FIHGcNWGdEZOLyK669";
  String SALT = "KeheUber";
  Uint8List IV =
      Uint8List.fromList([0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
  String ALGO_TYPE = "PBKDF2WithHmacSHA256";
  String ENCRE_TYPE = "AES";
  String PADDING = "AES/CBC/PKCS5PADDING";
  int COUNT_ITERATION = 65536;
  int KEY_LENGTH = 256;

  static Uint8List deriveKey(dynamic password,
      {String salt = '0000000000000000',
      int iterationCount = ITERATION_COUNT,
      int derivedKeyLength = KEY_SIZE}) {
    if (password == null || password.isEmpty) {
      throw new ArgumentError('password must not be empty');
    }

    if (password is String) {
      password = createUint8ListFromString(password);
    }

    Uint8List saltBytes = createUint8ListFromString(salt);
    String hexSalt = formatBytesAsHexString(saltBytes);

    KeyDerivator keyDerivator =
        new PBKDF2KeyDerivator(new HMac(new SHA1Digest(), 64));

    Pbkdf2Parameters params =
        new Pbkdf2Parameters(saltBytes, iterationCount, derivedKeyLength);

    keyDerivator.init(params);

    return keyDerivator.process(password);
  }

  Uint8List createUint8ListFromHexString(String hex) {
    var result = new Uint8List(hex.length ~/ 2);
    for (var i = 0; i < hex.length; i += 2) {
      var num = hex.substring(i, i + 2);
      var byte = int.parse(num, radix: 16);
      result[i ~/ 2] = byte;
    }
    return result;
  }

  static String formatBytesAsHexString(Uint8List bytes) {
    var result = new StringBuffer();
    for (var i = 0; i < bytes.lengthInBytes; i++) {
      var part = bytes[i];
      result.write('${part < 16 ? '0' : ''}${part.toRadixString(16)}');
    }
    return result.toString();
  }

  static Uint8List pad(Uint8List src, int blockSize) {
    var pad = new PKCS7Padding();
    pad.init(null);

    int padLength = blockSize - (src.length % blockSize);
    var out = new Uint8List(src.length + padLength)..setAll(0, src);
    pad.addPadding(out, src.length);

    return out;
  }

  static Uint8List unpad(Uint8List src) {
    var pad = new PKCS7Padding();
    pad.init(null);

    int padLength = pad.padCount(src);
    int len = src.length - padLength;

    return new Uint8List(len)..setRange(0, len, src);
  }

  static String encrypt(String password, String plaintext,
      {String mode = CBC_MODE}) {
    Uint8List derivedKey = deriveKey(password);
    KeyParameter keyParam = new KeyParameter(derivedKey);
    BlockCipher aes = new AESFastEngine();

    var rnd = FortunaRandom();
    rnd.seed(keyParam);
    Uint8List iv = createUint8ListFromString("0000000000000000");

    BlockCipher cipher;
    ParametersWithIV params = new ParametersWithIV(keyParam, iv);
    cipher = new CBCBlockCipher(aes);
    cipher.init(true, params);

    Uint8List textBytes = createUint8ListFromString(plaintext);
    Uint8List paddedText = pad(textBytes, aes.blockSize);
    Uint8List cipherBytes = _processBlocks(cipher, paddedText);
    Uint8List cipherIvBytes = new Uint8List(cipherBytes.length + iv.length)
      ..setAll(0, iv)
      ..setAll(iv.length, cipherBytes);

    return base64.encode(cipherIvBytes);
  }

  static String decrypt(String password, String ciphertext) {
    log('Password: $password');
    Uint8List derivedKey = deriveKey(password);
    log('derivedKey: $derivedKey');
    KeyParameter keyParam = new KeyParameter(derivedKey);
    log('keyParam: $keyParam');
    BlockCipher aes = new AESFastEngine();

    Uint8List cipherIvBytes = base64.decode(ciphertext);
    log('cipherIvBytes: $cipherIvBytes');
    Uint8List iv = createUint8ListFromString("0000000000000000");
    // Uint8List iv = new Uint8List(aes.blockSize)
    //   ..setRange(0, aes.blockSize, cipherIvBytes);
    log('iv: $iv');
    BlockCipher cipher;
    ParametersWithIV params = new ParametersWithIV(keyParam, iv);
    log('params: $params');
    cipher = new CBCBlockCipher(aes);
    log('cipher: $cipher');
    cipher.init(false, params);

    int cipherLen = cipherIvBytes.length - aes.blockSize;
    Uint8List cipherBytes = new Uint8List(cipherLen)
      ..setRange(0, cipherLen, cipherIvBytes, aes.blockSize);
    Uint8List paddedText = _processBlocks(cipher, cipherBytes);
    log('cipher: $paddedText');
    Uint8List textBytes = paddedText;
    // Uint8List textBytes = unpad(paddedText);

    return new String.fromCharCodes(textBytes);
  }

  static Uint8List createUint8ListFromString(String s) {
    var ret = new Uint8List(s.length);
    for (var i = 0; i < s.length; i++) {
      ret[i] = s.codeUnitAt(i);
    }
    return ret;
  }

  static Uint8List _processBlocks(BlockCipher cipher, Uint8List inp) {
    var out = new Uint8List(inp.lengthInBytes);

    for (var offset = 0; offset < inp.lengthInBytes;) {
      var len = cipher.processBlock(inp, offset, out, offset);
      offset += len;
    }

    return out;
  }
}
