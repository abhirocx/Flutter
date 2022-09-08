// To parse this JSON data, do
//
//     final requestVersionApi = requestVersionApiFromMap(jsonString);

import 'dart:convert';

RequestVersionApi requestVersionApiFromMap(String str) => RequestVersionApi.fromMap(json.decode(str));

String requestVersionApiToMap(RequestVersionApi data) => json.encode(data.toJson());

class RequestVersionApi {
  RequestVersionApi({
    this.userOs,
    this.installedVersion,
  });

  String ?userOs;
  String ?installedVersion;

  factory RequestVersionApi.fromJson(Map<String, dynamic> json) => RequestVersionApi(
    userOs: json["userOS"],
    installedVersion: json["installedVersion"],
  );

  Map<String, dynamic> toJson() => {
    "userOS": userOs,
    "installedVersion": installedVersion,
  };

  factory RequestVersionApi.fromMap(Map<String, dynamic> json) => RequestVersionApi(
    userOs: json["userOS"],
    installedVersion: json["installedVersion"],
  );

  Map<String, dynamic> toMap() => {
    "userOS": userOs,
    "installedVersion": installedVersion,
  };
}
