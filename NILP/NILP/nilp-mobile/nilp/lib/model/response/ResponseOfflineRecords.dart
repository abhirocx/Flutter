import 'dart:convert';

ResponseOfflineRecords responseOfflineRecordsFromJson(String str) =>
    ResponseOfflineRecords.fromJson(json.decode(str));

String responseOfflineRecordsToJson(ResponseOfflineRecords data) =>
    json.encode(data.toJson());

ResponseOfflineRecords responseOfflineRecordsFromMap(String str) =>
    ResponseOfflineRecords.fromMap(json.decode(str));

String responseOfflineRecordsToMap(ResponseOfflineRecords data) =>
    json.encode(data.toMap());

class ResponseOfflineRecords {
  ResponseOfflineRecords({
    this.data,
  });

  Data? data;

  factory ResponseOfflineRecords.fromJson(Map<String, dynamic> json) =>
      ResponseOfflineRecords(
        data: Data.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
        "data": data!.toJson(),
      };

  factory ResponseOfflineRecords.fromMap(Map<String, dynamic> json) =>
      ResponseOfflineRecords(
        data: Data.fromMap(json["data"]),
      );

  Map<String, dynamic> toMap() => {
        "data": data!.toMap(),
      };
}

class Data {
  Data({
    this.updateStatus,
    this.memberUpdateStatus,
    this.head,
    this.member,
    this.status,
    this.updateSuccessMessage,
    this.updateErrorMessage,
  });

  bool? updateStatus;
  bool? memberUpdateStatus;
  String? head;
  List<String>? member;
  bool? status;
  String? updateSuccessMessage;
  String? updateErrorMessage;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        updateStatus: json["update_status"],
        memberUpdateStatus: json["member_update_status"],
        head: json["head"],
        member: List<String>.from(json["member"].map((x) => x)),
        status: json["status"],
        updateSuccessMessage: json["update_success_message"],
        updateErrorMessage: json["update_error_message"],
      );

  Map<String, dynamic> toJson() => {
        "update_status": updateStatus,
        "member_update_status": memberUpdateStatus,
        "head": head,
        "member": List<dynamic>.from(member!.map((x) => x)),
        "status": status,
        "update_success_message": updateSuccessMessage,
        "update_error_message": updateErrorMessage,
      };
  factory Data.fromMap(Map<String, dynamic> json) => Data(
        updateStatus: json["update_status"],
        memberUpdateStatus: json["member_update_status"],
        head: json["head"],
        member: List<String>.from(json["member"].map((x) => x)),
        status: json["status"],
        updateSuccessMessage: json["update_success_message"],
        updateErrorMessage: json["update_error_message"],
      );

  Map<String, dynamic> toMap() => {
        "update_status": updateStatus,
        "member_update_status": memberUpdateStatus,
        "head": head,
        "member": List<dynamic>.from(member!.map((x) => x)),
        "status": status,
        "update_success_message": updateSuccessMessage,
        "update_error_message": updateErrorMessage,
      };
}
