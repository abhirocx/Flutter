import 'dart:convert';

RequestResendOtp requestResendOtpFromJson(String str) =>
    RequestResendOtp.fromJson(json.decode(str));

String requestResendOtpToJson(RequestResendOtp data) =>
    json.encode(data.toJson());

class RequestResendOtp {
  RequestResendOtp({
    required this.tsnId,
  });

  String tsnId;

  factory RequestResendOtp.fromJson(Map<String, dynamic> json) =>
      RequestResendOtp(
        tsnId: json["tsnId"],
      );

  Map<String, dynamic> toJson() => {
        "tsnId": tsnId,
      };

  factory RequestResendOtp.fromMap(Map<String, dynamic> json) =>
      RequestResendOtp(
        tsnId: json["tsnId"],
      );

  Map<String, dynamic> toMap() => {
        "tsnId": tsnId,
      };
}
