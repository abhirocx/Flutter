import 'dart:convert';

RequestEditProfile requestEditProfileFromJson(String str) => RequestEditProfile.fromJson(json.decode(str));

String requestEditProfileToJson(RequestEditProfile data) => json.encode(data.toJson());

class RequestEditProfile {
  RequestEditProfile({
    required this.email,
    required this.currentEmail,
    required this.currentMobile,
    required this.mobile,
  });

  final String email;
  final String currentEmail;
  final String currentMobile;
  final String mobile;

  factory RequestEditProfile.fromJson(Map<String, dynamic> json) => RequestEditProfile(
    email: json["email"],
    currentEmail: json["current_email"],
    currentMobile: json["current_mobile"],
    mobile: json["mobile"],
  );

  Map<String, dynamic> toJson() => {
    "email": email,
    "current_email": currentEmail,
    "current_mobile": currentMobile,
    "mobile": mobile,
  };
}
