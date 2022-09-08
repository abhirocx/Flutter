class ResponseRefreshToken {
  ResponseRefreshToken({
    required this.data,
  });

  Datam data;

  factory ResponseRefreshToken.fromJson(Map<String, dynamic> json) =>
      ResponseRefreshToken(
        data: Datam.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
    "data": data.toJson(),
  };
}

class Datam {
  Datam({
    required this.accessToken,
    required this.expires,
    required this.refreshToken,
  });

  String accessToken;
  int expires;
  String refreshToken;

  factory Datam.fromJson(Map<String, dynamic> json) => Datam(
    accessToken: json["access_token"],
    expires: json["expires"],
    refreshToken: json["refresh_token"],
  );

  Map<String, dynamic> toJson() => {
    "access_token": accessToken,
    "expires": expires,
    "refresh_token": refreshToken,
  };
}
