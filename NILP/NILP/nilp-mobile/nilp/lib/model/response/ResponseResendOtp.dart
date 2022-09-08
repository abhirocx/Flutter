import 'dart:convert';

ResponseResendOtp responseResendOtpFromJson(String str) =>
    ResponseResendOtp.fromJson(json.decode(str));

String responseResendOtpToJson(ResponseResendOtp data) =>
    json.encode(data.toJson());

class ResponseResendOtp {
  ResponseResendOtp({
    required this.data,
  });

  Data data;

  factory ResponseResendOtp.fromJson(Map<String, dynamic> json) =>
      ResponseResendOtp(
        data: Data.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
        "data": data.toJson(),
      };
}

class Data {
  Data({
    //  required this.otp,
    required this.tsnId,
    required this.statusCode,
    required this.statusDesc,
  });

  // int otp;
  String tsnId;
  String statusCode;
  String statusDesc;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        //  otp: json["otp"],
        tsnId: json["tsnId"],
        statusCode: json["statusCode"],
        statusDesc: json["statusDesc"],
      );

  Map<String, dynamic> toJson() => {
        //     "otp": otp,
        "tsnId": tsnId,
        "statusCode": statusCode,
        "statusDesc": statusDesc,
      };
}
