import 'dart:convert';

ResponseEditProfile responseEditProfileFromJson(String str) =>
    ResponseEditProfile.fromJson(json.decode(str));

String responseEditProfileToJson(ResponseEditProfile data) =>
    json.encode(data.toJson());

class ResponseEditProfile {
  ResponseEditProfile({
    required this.data,
  });

  final Data data;

  factory ResponseEditProfile.fromJson(Map<String, dynamic> json) =>
      ResponseEditProfile(
        data: Data.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
        "data": data.toJson(),
      };
}

class Data {
  Data({
    required this.tsnId,
    required this.message,
    //required this.otp,
  });

  final String tsnId;
  final String message;
  // final int otp;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        tsnId: json["tsnId"],
        message: json["message"],
        //  otp: json["otp"],
      );

  Map<String, dynamic> toJson() => {
        "tsnId": tsnId,
        "message": message,
        // "otp": otp,
      };
}
