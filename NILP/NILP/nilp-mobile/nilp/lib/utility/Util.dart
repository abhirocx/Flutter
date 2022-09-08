import 'dart:convert' show utf8;
import 'dart:typed_data';

import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:cryptography/cryptography.dart';
import 'package:encrypt/encrypt.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../constants/Strings.dart';
import '../constants/UrlEndPoints.dart';

class Util {
  String SECRET_KEY = "37ZD3D89A64C115855DF9178B8R84c1x";

  // String SECRET_KEY = "70dd2d6d89e0712be2f6f00035424f8c";
  String SALT = "213A26DBB4A358C5";

  // String SALT = "KeheOla";

  // Uint8List IVV =
  //     Uint8List.fromList([0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);
  String ALGO_TYPE = "PBKDF2WithHmacSHA256";
  String ENCRE_TYPE = "AES";
  String PADDING = "AES/CBC/PKCS5PADDING";
  int COUNT_ITERATION = 65536;
  int KEY_LENGTH = 256;

  /////////////////////////////////////////

  Uint8List IVV =
      Uint8List.fromList([0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);

  List<int> ivarray = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

  static String currentLocal = "en";
  static int isHeadSaved = 0;
  static int headID = 0;

  Future<String> getLanguageCode() async {
    SharedPreferences _prefs = await SharedPreferences.getInstance();
    String code = _prefs.getString(prefSelectedLanguageCode)!;
    String headercode = "";
    if (code == "en")
      return "en";
    else if (code == "gu")
      return "guj";
    else if (code == "as")
      return "asm";
    // else if (code == "bo")
    //   return "bod";
    // else if (code == "doi")
    //   return "doi";
    else if (code == "ks")
      return "ks";
    else if (code == "kn")
      return "kan";
    else if (code == "mr")
      return "mar";
    else if (code == "ml")
      return "mal";
    else if (code == "pa")
      return "pan";
    else if (code == "te")
      return "tel";
    else if (code == "hi")
      return "hin";
    else
      return "en";
  }

  String getRole(String markas) {
    if (markas == LEARNER) {
      if(Util.currentLocal=='hin'){
        return " शिक्षार्थी";
      }else{
        return "Learner";
      }
    } else if (markas == VT) {
      if(Util.currentLocal=='hin'){
        return " स्वयंसेवी";
      }else{
        return "VT";
      }
    } else {
      if(Util.currentLocal=='hin'){
        return " कोई भी नहीं";
      }else{
        return "None";
      }
    }
  }

  Encrypted encrypt(String plainText) {
    final key1 = Key.fromUtf8(SECRET_KEY);
    final iv1 = IV.fromUtf8(SALT);
    final encrypter = Encrypter(AES(key1, mode: AESMode.cbc));
    //print("Dart Output…!!!");
    //print("IV: " + iv1.bytes.toString());
    //print("Key: " + key1.bytes.toString());
    final encrypted = encrypter.encrypt(plainText, iv: iv1);
    //print("Encrypted: " + encrypted.bytes.toString());
    //print("Base64: " + encrypted.base64);

    return encrypted;
  }

/*static Future<bool> hasNetwork() async {
    try {
      final result = await InternetAddress.lookup('https://www.google.com/');
      return result.isNotEmpty && result[0].rawAddress.isNotEmpty;
    } on SocketException catch (_) {
      return false;
    }
  }*/
  Future<bool> hasInternet() async {
    var connectivityResult = await (Connectivity().checkConnectivity());
    if (connectivityResult == ConnectivityResult.mobile) {
      return true;
    } else if (connectivityResult == ConnectivityResult.wifi) {
      return true;
    } else {
      return false;
    }
  }

  getProfession(String value) {
    if (value == "1") {
      return "Daily Wager";
    } else if (value == "2") {
      return "Contract Employee";
    } else if (value == "3") {
      return "Technician/Technical personnel";
    } else if (value == "4") {
      return "Office Worker";
    } else if (value == "5") {
      return "Agriculture/Animal Husbandry & Dairy";
    } else if (value == "6") {
      return "Vendors";
    } else if (value == "7") {
      return "Manufacturer";
    } else if (value == "8") {
      return "Construction";
    } else if (value == "9") {
      return "Service/IT";
    } else if (value == "10") {
      return "Garments/Fashion Industry";
    } else if (value == "11") {
      return "Any other Occupation";
    }else if (value == "12") {
      return "Self Employed";
    } else if (value == "13") {
      return "Student";
    }else if (value == "14") {
      return "NCC Volunteer";
    }else if (value == "15") {
      return "TTI Student";
    }else if (value == "16") {
      return "NYSK/ NSS Volunteer";
    }else if (value == "17") {
      return "Ex-service men";
    }else if (value == "18") {
      return "Retd. Govt. Employee";
    }else {
      return "Daily Wager";
    }
  }

  getMemberRelation(String value) {
    if (value == "1") {
      return "Father";
    } else if (value == "2") {
      return "Mother";
    } else if (value == "3") {
      return "Son";
    } else if (value == "4") {
      return "Daughter";
    } else if (value == "5") {
      return "Son-in-law";
    } else if (value == "6") {
      return "Daughter-in-law";
    } else if (value == "7") {
      return "Others";
    } else {
      return "Father";
    }
  }

  getSocialCategory(String value) {
    if (value == "1") {
      return "SC";
    } else if (value == "2") {
      return "ST";
    } else if (value == "3") {
      return "Minority";
    } else if (value == "4") {
      return "General";
    } else {
      return "General";
    }
  }

  getIdentityValue(String value) {
    if (value == "1") {
      return "Voter ID";
    } else if (value == "2") {
      return "Pan Card";
    } else if (value == "3") {
      return "Ration Card";
    } else if (value == "4") {
      return "Driving License";
    } else if (value == "5") {
      return "Passport";
    } else if (value == "6") {
      return "NPR Smart Card";
    } else if (value == "7") {
      return "Student Id Card";
    } else if (value == "8") {
      return "MGNREGA Job Card";
    }else if (value == "9") {
      return "Labor Card";
    }else if (value == "10") {
      return "Bank Passbook with photo";
    }else {
      return "Voter ID";
    }
  }

  getEducationType(String value) {
    if (value == "1") {
      return "Secondary";
    } else if (value == "2") {
      return "Higher secondary";
    } else if (value == "3") {
      return "Graduate";
    } else if (value == "4") {
      return "Post Graduate and above";
    } else if (value == "0") {
      return "Other";
    } else {
      return "Secondary";
    }
  }

  getDesignationType(String value) {
    if (value == "9864af5f-900b-4998-89b2-60f211dd85e5") {
      return "Principal";
    } else if (value == "b1ef4c0e-c75a-4009-bf30-b3aa23b792e1") {
      return "Teacher";
    }else if (value == "15f2a32c-8b7d-4cc1-b048-3d3ebeff97fc") {
      return  "Deputy Secretary";
    }else if (value ==  "008742d8-4617-4156-b77c-9813cbadadfc") {
      return "Director";
    }else if (value == "544efba5-84f0-4298-9138-01ba1deb517c") {
      return  "Joint Secretary";
    } else if (value ==  "6fe07605-f63f-4118-8b8b-fc8080981985") {
      return  "Under Secretary";
    }else {
      return "Teacher";
    }
  }

  getVT(String value) {
    if (value == "1") {
      return "School Student";
    } else if (value == "2") {
      return "College Student";
    } else if (value == "3") {
      return "NCTE Student";
    } else if (value == "4") {
      return "Volunteer of NSS";
    } else if (value == "5") {
      return "NYK";
    } else if (value == "6") {
      return "NCC";
    } else if (value == "0") {
      return "Others";
    } else {
      return "Others";
    }
  }
}

String getDate(String timestamp) {
  int timeInMillis = int.parse(timestamp);
  var date = DateTime.fromMillisecondsSinceEpoch(timeInMillis * 1000);
  var formattedDate = DateFormat.MMMd().format(date);
  //print(formattedDate);
  return formattedDate.toUpperCase();
}

bool validateGender(String gender, String relation) {
  if (gender == 'f') {
    if (relation == "2" ||
        relation == "6" ||
        relation == "4" ||
        relation == "7")
      return true;
    else
      return false;
  } else if (gender == 'm') {
    if (relation == "1" ||
        relation == "3" ||
        relation == "5" ||
        relation == "7")
      return true;
    else
      return false;
  } else
    return true;
}

logo(){
  if(Util.currentLocal=="hin"){
    return "images/new_splash_logo_hin.svg";
  }else{
    return "images/new_splash_logo.svg";

  }
}

bool validateIdType(int proofType, int proof_detail_length, var id_value) {
  if (proofType == 4) {
    if (proof_detail_length >= 6 && proof_detail_length <= 16)
      return true;
    else
      return false;
  } else if (proofType == 6) {
    if (proof_detail_length >= 12 && proof_detail_length <= 16)
      return true;
    else
      return false;
  } else if (proofType == 2) {
    if (proof_detail_length == 10) {
      String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
      // Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
      // Match matcher = pattern.matcher(id_value);

      // id_value.matches(regex);

      // String regex = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
      // Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
      // Matcher matcher = pattern.matcher(id_value);
      // bool isPAnNum;
      //  PANValidator panValidator = new PANValidator();
      bool result = id_value.contains(RegExp("[A-Z]{5}[0-9]{4}[A-Z]{1}"));
      if (result) {
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  } else if (proofType == 5) {
    if (proof_detail_length >= 8 && proof_detail_length <= 14)
      return true;
    else
      return false;
  } else if (proofType == 3) {
    if (proof_detail_length == 10)
      return true;
    else
      return false;
  } else if (proofType == 7) {
    if (proof_detail_length >= 4 && proof_detail_length <= 16)
      return true;
    else
      return false;
  } else if (proofType == 1) {
    if (proof_detail_length >= 10 && proof_detail_length <= 16)
      return true;
    else
      return false;
  } else
    return true;
}

Future<List<int>> getpkdf() async {
  final pbkdf2 = Pbkdf2(
    macAlgorithm: Hmac.sha256(),
    iterations: 65536,
    bits: 256,
  );

  List<int> bytes = utf8.encode("37HG3D86D64C115933DF9458B8E84c1b");
  final secretKey = SecretKey(bytes);
  // A random salt
  List<int> salt = utf8.encode("913Z26EBB9F356A5");

  // Calculate a hash that can be stored in the database
  final newSecretKey = await pbkdf2.deriveKey(
    secretKey: secretKey,
    nonce: salt,
  );
  final newSecretKeyBytes = await newSecretKey.extractBytes();

  return newSecretKeyBytes;
}
