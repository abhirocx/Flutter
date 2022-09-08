// To parse this JSON data, do
//
//     final responseVersionApi = responseVersionApiFromMap(jsonString);

import 'dart:convert';

ResponseVersionApi responseVersionApiFromMap(String str) => ResponseVersionApi.fromMap(json.decode(str));

String responseVersionApiToMap(ResponseVersionApi data) => json.encode(data.toMap());

class ResponseVersionApi {
  ResponseVersionApi({
    this.data,
  });

  Data? data;

  factory ResponseVersionApi.fromMap(Map<String, dynamic> json) => ResponseVersionApi(
    data: Data.fromMap(json["data"]),
  );

  Map<String, dynamic> toMap() => {
    "data": data!.toMap(),
  };
}

class Data {
  Data({
    this.latestVersion,
    this.isCompulsory,
    this.updateAvailable,
  });

  String? latestVersion;
  bool? isCompulsory;
  bool? updateAvailable;

  factory Data.fromMap(Map<String, dynamic> json) => Data(
    latestVersion: json["latestVersion"],
    isCompulsory: json["isCompulsory"],
    updateAvailable: json["updateAvailable"],
  );

  Map<String, dynamic> toMap() => {
    "latestVersion": latestVersion,
    "isCompulsory": isCompulsory,
    "updateAvailable": updateAvailable,
  };
}
