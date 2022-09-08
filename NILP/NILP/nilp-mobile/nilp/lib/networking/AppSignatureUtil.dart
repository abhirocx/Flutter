import 'dart:convert';

import 'package:crypto/crypto.dart';

class AppSignatureUtil {
  static String preShareKeyForSignature =
      "QD32VdbRuMa0iI0q9q7cH6FIHGcNWGdEZOLyK669";
  static String signatureAlgorithm = "HmacSHA256";

  late String signature;

  String generateSignature(
      String url, String param, int recordId, String timestamp, Object inObj) {
    if (((url != null) && (!("" == url))) && url.contains("/")) {
      List<String> strArr = url.split("/");
      strArr.add("null");
      String methodName = strArr[4];
      String inServiceParam = strArr[5];
      String inClientParam = strArr[6];
      String data;

      String detail = param + timestamp;

      String hashcode = detail.hashCode.toString();
      // detail = (detail + hashcode);
      detail = (detail + getHashCode(detail));
      //print("detail:$detail");
      if (recordId != null && recordId > 0) {
        detail = detail + recordId.toString();
      }

      String jsonTutorial = jsonEncode(inObj);
      detail = detail + jsonTutorial;

      // print("detail:$detail");
      // print("inOBJ:$inObj");

      data = methodName + timestamp + inServiceParam + inClientParam + detail;
      //print("detail:$data");
      signature = calculateHMAC(data.toString());
    } else {
      //throw new IllegalArgumentException("URL cannot be null or empty.");

    }

    // print("signature:$signature");
    return signature;
  }

  String calculateHMAC(String data) {
    // print("hmacdata:$data");

    var key = utf8.encode(preShareKeyForSignature);
    var bytes = utf8.encode(data);

    var hmacSha256 = Hmac(sha256, key); // HMAC-SHA256
    var digest = hmacSha256.convert(bytes);

    String base64Mac = base64.encode(digest.bytes);

    // print("hmac:$base64Mac");
    return base64Mac.toString();
  }

  static bool isNotBlank(String str) {
    return (str != null) && (!("" == str));
  }

  String getHashCode(String data) {
    int hash = 0;
    // print("data:$data");
    int char;
    if (data.length == 0) return "";
    for (int i = 0; i < data.length; i++) {
      char = data.codeUnitAt(i);
      hash = (((hash) << 5) - hash) + char;
    }
    hash = getSignedInt(hash);
    // print("hashcode :$hash");
    return hash.toString();
  }

  int getSignedInt(int val) {
    return val.toSigned(32);
  }
}
