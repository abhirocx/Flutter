class RequestLogin {
  RequestLogin({
    required this.mobile,
    required this.otp,
  });

  String mobile;
  String otp;

  factory RequestLogin.fromJson(Map<String, dynamic> json) => RequestLogin(
        mobile: json["mobile"],
        otp: json["otp"],
      );

  Map<String, dynamic> toJson() => {
        "mobile": mobile,
        "otp": otp,
      };

  Map toMap() {
    var map = new Map();
    map["mobile"] = mobile;
    map["otp"] = otp;

    return map;
  }
}
