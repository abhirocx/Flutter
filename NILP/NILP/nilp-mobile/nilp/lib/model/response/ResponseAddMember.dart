import 'dart:convert';

ResponseAddMember responseAddMemberFromMap(String str) =>
    ResponseAddMember.fromMap(json.decode(str));

String responseAddMemberToMap(ResponseAddMember data) =>
    json.encode(data.toMap());

class ResponseAddMember {
  ResponseAddMember({
    required this.data,
  });

  final Data data;

  factory ResponseAddMember.fromMap(Map<String, dynamic> json) =>
      ResponseAddMember(
        data: Data.fromMap(json["data"]),
      );

  Map<String, dynamic> toMap() => {
        "data": data.toMap(),
      };

  factory ResponseAddMember.fromJson(Map<String, dynamic> json) =>
      ResponseAddMember(
        data: Data.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
        "data": data.toJson(),
      };
}

class Data {
  Data({
    required this.updateStatus,
    required this.memberUpdateStatus,
    required this.updateErrorMessage,
    required this.updateSuccessMessage,
  });

  final bool updateStatus;
  final bool memberUpdateStatus;
  final String updateErrorMessage;
  final String updateSuccessMessage;

  factory Data.fromMap(Map<String, dynamic> json) => Data(
        updateStatus: json["update_status"],
        memberUpdateStatus: json["member_update_status"],
        updateErrorMessage: json["update_error_message"],
    updateSuccessMessage: json["update_success_message"],
      );

  Map<String, dynamic> toMap() => {
        "update_status": updateStatus,
        "member_update_status": memberUpdateStatus,
        "update_error_message": updateErrorMessage,
        "update_success_message": updateSuccessMessage,
      };

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        updateStatus: json["update_status"],
        memberUpdateStatus: json["member_update_status"],
        updateErrorMessage: json["update_error_message"],
    updateSuccessMessage: json["update_success_message"],
      );

  Map<String, dynamic> toJson() => {
        "update_status": updateStatus,
        "member_update_status": memberUpdateStatus,
        "update_error_message": updateErrorMessage,
        "update_success_message": updateSuccessMessage,
      };
}
