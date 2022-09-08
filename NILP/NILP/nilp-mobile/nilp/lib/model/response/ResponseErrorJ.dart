import 'dart:convert';

ResponseErrorJ responseErrorJFromMap(String str) =>
    ResponseErrorJ.fromMap(json.decode(str));

String responseErrorJToMap(ResponseErrorJ data) => json.encode(data.toMap());

ResponseErrorJ responseErrorJFromJson(String str) =>
    ResponseErrorJ.fromJson(json.decode(str));

String responseErrorJToJson(ResponseErrorJ data) => json.encode(data.toJson());

class ResponseErrorJ {
  ResponseErrorJ({
    required this.errors,
  });

  List<Error> errors;

  factory ResponseErrorJ.fromMap(Map<String, dynamic> json) => ResponseErrorJ(
        errors: List<Error>.from(json["errors"].map((x) => Error.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "errors": List<dynamic>.from(errors.map((x) => x.toMap())),
      };
  factory ResponseErrorJ.fromJson(Map<String, dynamic> json) => ResponseErrorJ(
        errors: List<Error>.from(json["errors"].map((x) => Error.fromJson(x))),
      );

  Map<String, dynamic> toJson() => {
        "errors": List<dynamic>.from(errors.map((x) => x.toJson())),
      };
}

class Error {
  Error({
    required this.message,
    required this.extensions,
  });

  String message;
  Extensions extensions;

  factory Error.fromMap(Map<String, dynamic> json) => Error(
        message: json["message"],
        extensions: Extensions.fromMap(json["extensions"]),
      );

  Map<String, dynamic> toMap() => {
        "message": message,
        "extensions": extensions.toMap(),
      };

  factory Error.fromJson(Map<String, dynamic> json) => Error(
        message: json["message"],
        extensions: Extensions.fromJson(json["extensions"]),
      );

  Map<String, dynamic> toJson() => {
        "message": message,
        "extensions": extensions.toJson(),
      };
}

class Extensions {
  Extensions({
    required this.code,
  });

  String code;

  factory Extensions.fromMap(Map<String, dynamic> json) => Extensions(
        code: json["code"],
      );

  Map<String, dynamic> toMap() => {
        "code": code,
      };
  factory Extensions.fromJson(Map<String, dynamic> json) => Extensions(
        code: json["code"],
      );

  Map<String, dynamic> toJson() => {
        "code": code,
      };
}
