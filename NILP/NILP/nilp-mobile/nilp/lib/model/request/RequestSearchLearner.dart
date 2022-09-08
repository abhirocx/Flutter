import 'dart:convert';

RequestSearchLearner requestSearchLearnerFromJson(String str) =>
    RequestSearchLearner.fromJson(json.decode(str));

String requestSearchLearnerToJson(RequestSearchLearner data) =>
    json.encode(data.toJson());

class RequestSearchLearner {
  RequestSearchLearner({required this.search, required this.type});

  final String search;
  final String type;

  factory RequestSearchLearner.fromJson(Map<String, dynamic> json) =>
      RequestSearchLearner(search: json["search"], type: json["type"]);

  Map<String, dynamic> toJson() => {"search": search, "type": type};

  factory RequestSearchLearner.fromMap(Map<String, dynamic> json) =>
      RequestSearchLearner(search: json["search"], type: json["type"]);

  Map<String, dynamic> toMap() => {"search": search, "type": type};
}
