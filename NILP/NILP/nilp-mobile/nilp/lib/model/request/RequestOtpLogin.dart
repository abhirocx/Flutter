class RequestOtpLogin {
  RequestOtpLogin({
    required this.otp,
    required this.tsnId,
  });

  String otp;
  String tsnId;

  factory RequestOtpLogin.fromJson(Map<String, dynamic> json) =>
      RequestOtpLogin(
        otp: json["otp"],
        tsnId: json["tsnId"],
      );

  Map toMap() {
    var map = new Map();

    map["otp"] = otp;
    map["tsnId"] = tsnId;

    return map;
  }
}
