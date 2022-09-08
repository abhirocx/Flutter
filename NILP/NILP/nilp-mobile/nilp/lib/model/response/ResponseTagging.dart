import 'dart:convert';

ResponseTagging responseTaggingFromJson(String str) =>
    ResponseTagging.fromJson(json.decode(str));

String responseTaggingToJson(ResponseTagging data) =>
    json.encode(data.toJson());

class ResponseTagging {
  ResponseTagging({
    required this.data,
  });

  final Data data;

  factory ResponseTagging.fromJson(Map<String, dynamic> json) =>
      ResponseTagging(
        data: Data.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
        "data": data.toJson(),
      };
  factory ResponseTagging.fromMap(Map<String, dynamic> json) => ResponseTagging(
        data: Data.fromMap(json["data"]),
      );

  Map<String, dynamic> toMap() => {
        "data": data.toMap(),
      };
}

class Data {
  Data({
    required this.status,
  });

  final bool status;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        status: json["status"],
      );

  Map<String, dynamic> toJson() => {
        "status": status,
      };

  factory Data.fromMap(Map<String, dynamic> json) => Data(
        status: json["status"],
      );

  Map<String, dynamic> toMap() => {
        "status": status,
      };
}


