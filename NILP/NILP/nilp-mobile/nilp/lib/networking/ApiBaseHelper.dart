import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../constants/Strings.dart';
import '../constants/UrlEndPoints.dart';
import '../utility/Util.dart';
import 'CustomException.dart';

class ApiBaseHelper {
  Map<String, String> header(String timestamp, String signature) {
    Map<String, String> map = {
      "Accept": "application/json",
      "accept-language": Util.currentLocal,
      "content-type": "application/json",
      "Signature": signature,
      "timestamp": timestamp
    };

    return map;
  }

  Map<String, String> headerwithToken(
      String timestamp, String signature, String token) {
    Map<String, String> map = {
      "Accept": "application/json",
      "accept-language": Util.currentLocal,
      "content-type": "application/json",
      "Signature": signature,
      "timestamp": timestamp,
      "Authorization": "Bearer $token",
    };
    return map;
  }

  Future<dynamic> get(String url, Map<String, String> header) async {
    var responseJson;
    try {
      final response = await http.get(Uri.parse(url), headers: header);
      responseJson = _returnResponse(response);
    } on SocketException {
      throw FetchDataException('No Internet connection');
    }
    return responseJson;
  }

  Future<dynamic> post(
      String url, Map<String, String> header, Map jsObject) async {
    var responseJson;
    try {
      final response = await http.post(Uri.parse(BASE_URL + url),
          headers: header, body: json.encode(jsObject));
      responseJson = _returnResponse(response);
    } on Exception catch (e) {
      print('never $e');
    } on SocketException {
      throw FetchDataException('N8o Internet connection');
    }
    return responseJson;
  }

  Future<dynamic> post_api_version(
      String url, Map<String, String> header, Map jsObject) async {
    var responseJson;
    try {
      final response = await http.post(
          Uri.parse(BASE_URL + url),
          headers: header,
          body: json.encode(jsObject));
      responseJson = _returnResponse(response);
    } on Exception catch (_) {
      print('never reached');
    } on SocketException {
      throw FetchDataException('No Internet connection');
    }
    return responseJson;
  }

  Future<dynamic> postwithToken(
      String url, Map<String, String> header, Map jsObject) async {
    var responseJson;
    try {
      final response = await http.post(Uri.parse(BASE_URL + url),
          headers: header, body: json.encode(jsObject));
      responseJson = _returnResponse(response);
    } on Exception catch (_) {
      // print('never reached');

      var responseJson = json.decode(
          '{{errors: [{message: "Something went wrong", extensions: {code: INVALID_PAYLOAD}}]}');
      // print(responseJson);
      responseJson = _returnResponse(responseJson);
    } on SocketException {
      throw FetchDataException('No Internet connection');
    }
    return responseJson;
  }

  Future<dynamic> postImage(
      String url, Map<String, String> header, Map jsObject) async {
    var responseJson;
    try {
      final response = await http.post(
          Uri.parse(BASE_URL + "local/api/picture-update.php"),
          headers: header,
          body: json.encode(jsObject));
      responseJson = _returnResponse(response);
    } on SocketException {
      throw FetchDataException('No Internet connection');
    }
    return responseJson;
  }

  Future<dynamic> postlogin(
      String url, Map<String, String> header, Map jsObject) async {
    var responseJson;
    try {
      final response = await http.post(Uri.parse(BASE_URL + url),
          headers: header, body: json.encode(jsObject));
      responseJson = _returnResponse(response);
    } on SocketException {
      throw FetchDataException('No Internet connection');
    }
    return responseJson;
  }

  dynamic _returnResponse(http.Response response) {
    print("verifyotp,$response");
    switch (response.statusCode) {
      case 200:
        var responseJson = json.decode(response.body.toString());
        print(responseJson);
        return responseJson;
      case 201:
        var responseJson = json.decode(response.body.toString());
        //  print(responseJson);
        return responseJson;
      case 400:
        throw BadRequestException(response.body.toString());
      case 401:
        //  var responseJson = json.decode(response.body.toString());
        // var resbodyBytes = {_Uint8List} size = 95ponseJson = json.decode(response.body.toString());
        var responseJson = json.decode(
            '{"errors": [{"message": "Something went wrong", "extensions": {"code": "INVALID_PAYLOAD"}}]}');
        print(responseJson);
        return responseJson;
      case 403:
        var responseJson = json.decode(
            '{{errors: [{message: "Something went wrong", extensions: {code: INVALID_PAYLOAD}}]}'); // json.decode('{"errors":[{"code": "NP00REFRESH","message": "Something went wrong"}]}');
        // print(responseJson);
        return responseJson;
      case 404:
        var responseJson = json.decode(
            '{{errors: [{message: "Something went wrong", extensions: {code: INVALID_PAYLOAD}}]}');
        return responseJson;
      case 502:
        var responseJson = json.decode(
            '{{errors: [{message: "Something went wrong", extensions: {code: INVALID_PAYLOAD}}]}');
        print(responseJson);
        return responseJson;
      case 503:
        // var responseJson = json.decode(response.body.toString());
        // print(responseJson);
        var responseJson = json.decode(
            '{{errors: [{message: "Something went wrong", extensions: {code: INVALID_PAYLOAD}}]}');
        return responseJson;
      case 500:
      default:
        throw FetchDataException(
            'Error occured while Communication with Server with StatusCode : ${response.statusCode}');
    }
  }
}

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
  else if (code == "bo")
    return "bod";
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
  else
    return "hin";
}

class Resource<T> {
  final String url;
  T Function(Response response) parse;

  Resource({required this.url, required this.parse});
}
