class RequestRefreshToken {
  RequestRefreshToken({
    required this.refreshToken,
  });

  String? refreshToken;

  factory RequestRefreshToken.fromJson(Map<String, dynamic> json) =>
      RequestRefreshToken(
        refreshToken: json["refresh_token"],
      );

  Map<String, dynamic> toJson() => {
    "refresh_token": refreshToken,
  };

  Map toMap() {
    var map = new Map();
    map["refresh_token"] = refreshToken;

    return map;
  }
}
