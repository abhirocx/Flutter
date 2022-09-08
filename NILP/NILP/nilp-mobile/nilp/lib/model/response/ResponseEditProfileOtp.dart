import 'dart:convert';

ResponseEditProfileOtp responseEditProfileOtpFromJson(String str) =>
    ResponseEditProfileOtp.fromJson(json.decode(str));

String responseEditProfileOtpToJson(ResponseEditProfileOtp data) =>
    json.encode(data.toJson());

ResponseEditProfileOtp responseEditProfileOtpFromMap(String str) =>
    ResponseEditProfileOtp.fromMap(json.decode(str));

String responseEditProfileOtpToMap(ResponseEditProfileOtp data) =>
    json.encode(data!.toMap());

class ResponseEditProfileOtp {
  ResponseEditProfileOtp({
    required this.data,
  });

  Data data;

  factory ResponseEditProfileOtp.fromJson(Map<String, dynamic> json) =>
      ResponseEditProfileOtp(
        data: Data.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
        "data": data.toJson(),
      };

  factory ResponseEditProfileOtp.fromMap(Map<String, dynamic> json) =>
      ResponseEditProfileOtp(
        data: Data.fromMap(json["data"]),
      );

  Map<String, dynamic> toMap() => {
        "data": data.toMap(),
      };
}

class Data {
  Data({
    required this.id,
    required this.email,
    required this.mobile,
    required this.tsnid,
    required this.message,
  });

  String id;
  String email;
  String mobile;
  String tsnid;
  String message;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        id: json["id"],
        email: json["email"],
        mobile: json["mobile"],
        tsnid: json["tsnid"],
        message: json["message"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "email": email,
        "mobile": mobile,
        "tsnid": tsnid,
        "message": message,
      };
  factory Data.fromMap(Map<String, dynamic> json) => Data(
        id: json["id"],
        email: json["email"],
        mobile: json["mobile"],
        tsnid: json["tsnid"],
        message: json["message"],
      );

  Map<String, dynamic> toMap() => {
        "id": id,
        "email": email,
        "mobile": mobile,
        "tsnid": tsnid,
        "message": message,
      };
}
