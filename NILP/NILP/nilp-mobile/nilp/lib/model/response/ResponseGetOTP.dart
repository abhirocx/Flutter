import 'dart:convert';

ResponseGetOtp responseGetOtpFromJson(String str) =>
    ResponseGetOtp.fromJson(json.decode(str));

String responseGetOtpToJson(ResponseGetOtp data) => json.encode(data.toJson());

class ResponseGetOtp {
  ResponseGetOtp({
    required this.data,
  });

  Data data;

  factory ResponseGetOtp.fromJson(Map<String, dynamic> json) => ResponseGetOtp(
        data: Data.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
        "data": data.toJson(),
      };
}

class Data {
  String tsnid;
   //int otp;


  Data({
    required this.tsnid,
     //required this.otp,
  });

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        tsnid: json["tsnId"],
        // otp: json["otp"],
      );

  Map<String, dynamic> toJson() => {
        "tsnId": tsnid,
        // "otp": otp,
      };
}
