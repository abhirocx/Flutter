class RequestGetOtp {
  RequestGetOtp({
    required this.mobile,
  });

  String mobile;

  factory RequestGetOtp.fromJson(Map<String, dynamic> json) => RequestGetOtp(
        mobile: json["mobile"],
      );

  Map<String, dynamic> toJson() => {
        "mobile": mobile,
      };

  Map toMap() {
    var map = new Map();
    map["mobile"] = mobile;

    return map;
  }
}
