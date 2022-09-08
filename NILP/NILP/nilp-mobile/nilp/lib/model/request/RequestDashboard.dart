import 'dart:convert';

RequestDashboard requestDashboardFromMap(String str) =>
    RequestDashboard.fromMap(json.decode(str));

String requestDashboardToMap(RequestDashboard data) =>
    json.encode(data.toMap());

class RequestDashboard {
  RequestDashboard({
    this.userId,
  });

  String? userId;

  factory RequestDashboard.fromMap(Map<String, dynamic> json) =>
      RequestDashboard(
        userId: json["userID"],
      );

  Map<String, dynamic> toMap() => {
        "userID": userId,
      };

  factory RequestDashboard.fromJson(Map<String, dynamic> json) =>
      RequestDashboard(
        userId: json["userID"],
      );

  Map<String, dynamic> toJson() => {
        "userID": userId,
      };
}
