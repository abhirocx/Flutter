import 'dart:convert';

ResponseErrorLogin responseErrorJFromJson(String str) =>
    ResponseErrorLogin.fromJson(json.decode(str));

String responseErrorJToJson(ResponseErrorLogin data) =>
    json.encode(data.toJson());

class ResponseErrorLogin {
  ResponseErrorLogin({
    required this.errors,
  });

  List<Error> errors;

  factory ResponseErrorLogin.fromJson(Map<String, dynamic> json) =>
      ResponseErrorLogin(
        errors: List<Error>.from(json["errors"].map((x) => Error.fromJson(x))),
      );

  Map<String, dynamic> toJson() => {
        "errors": List<dynamic>.from(errors.map((x) => x.toJson())),
      };
}

class Error {
  Error({
    required this.code,
    required this.message,
  });

  String code;
  String message;

  factory Error.fromJson(Map<String, dynamic> json) => Error(
        code: json["code"],
        message: json["message"],
      );

  Map<String, dynamic> toJson() => {
        "code": code,
        "message": message,
      };
}
