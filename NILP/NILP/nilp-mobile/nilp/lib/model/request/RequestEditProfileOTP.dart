import 'dart:convert';

ReqestEditProfileOtp reqestEditProfileOtpFromJson(String str) =>
    ReqestEditProfileOtp.fromJson(json.decode(str));

String reqestEditProfileOtpToJson(ReqestEditProfileOtp data) =>
    json.encode(data.toJson());

class ReqestEditProfileOtp {
  ReqestEditProfileOtp({
    this.otp,
    this.tsnId,
    this.email,
    this.mobile,
  });

  int? otp;
  String? tsnId;
  String? email;
  String? mobile;

  factory ReqestEditProfileOtp.fromJson(Map<String, dynamic> json) =>
      ReqestEditProfileOtp(
        otp: json["otp"],
        tsnId: json["tsnId"],
        email: json["email"],
        mobile: json["mobile"],
      );

  Map<String, dynamic> toJson() => {
        "otp": otp,
        "tsnId": tsnId,
        "email": email,
        "mobile": mobile,
      };
}
