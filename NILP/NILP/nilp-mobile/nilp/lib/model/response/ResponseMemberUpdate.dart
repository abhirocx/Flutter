import 'dart:convert';

ResponseMemberUpdate responseMemberUpdateFromMap(String str) => ResponseMemberUpdate.fromMap(json.decode(str));

String responseMemberUpdateToMap(ResponseMemberUpdate data) => json.encode(data.toMap());
ResponseMemberUpdate responseMemberUpdateFromJson(String str) => ResponseMemberUpdate.fromJson(json.decode(str));

String responseMemberUpdateToJson(ResponseMemberUpdate data) => json.encode(data.toJson());
class ResponseMemberUpdate {
  ResponseMemberUpdate({
    this.data,
  });

  Data? data;

  factory ResponseMemberUpdate.fromMap(Map<String, dynamic> json) => ResponseMemberUpdate(
    data: Data.fromMap(json["data"]),
  );

  Map<String, dynamic> toMap() => {
    "data": data!.toMap(),
  };
  factory ResponseMemberUpdate.fromJson(Map<String, dynamic> json) => ResponseMemberUpdate(
    data: Data.fromJson(json["data"]),
  );

  Map<String, dynamic> toJson() => {
    "data": data!.toJson(),
  };

}

class Data {
  Data({
    this.status,
    this.message,
  });

  bool? status;
  String? message;

  factory Data.fromMap(Map<String, dynamic> json) => Data(
    status: json["status"],
    message: json["message"],
  );

  Map<String, dynamic> toMap() => {
    "status": status,
    "message": message,
  };
  factory Data.fromJson(Map<String, dynamic> json) => Data(
    status: json["status"],
    message: json["message"],
  );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
  };
}

