class RequestUser {
  RequestUser({
    required this.mobile,
  });

  String mobile;

  factory RequestUser.fromJson(Map<String, dynamic> json) => RequestUser(
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
