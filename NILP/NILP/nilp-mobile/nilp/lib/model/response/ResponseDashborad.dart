import 'package:meta/meta.dart';
import 'dart:convert';

ResponseDashborad responseDashboradFromJson(String str) => ResponseDashborad.fromJson(json.decode(str));

String responseDashboradToJson(ResponseDashborad data) => json.encode(data.toJson());

class ResponseDashborad {
  ResponseDashborad({
    required this.data,
  });

  final Data data;

  factory ResponseDashborad.fromJson(Map<String, dynamic> json) => ResponseDashborad(
    data: Data.fromJson(json["data"]),
  );

  Map<String, dynamic> toJson() => {
    "data": data.toJson(),
  };
}

class Data {
  Data({
    required this.learner,
    required this.taggedLearner,
    required this.untaggedLearner,
    required this.vt,
    required this.taggedVt,
    required this.untaggedVt,
  });

  final String learner;
  final String taggedLearner;
  final String untaggedLearner;
  final String vt;
  final String taggedVt;
  final String untaggedVt;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
    learner: json["learner"],
    taggedLearner: json["tagged_learner"],
    untaggedLearner: json["untagged_learner"],
    vt: json["vt"],
    taggedVt: json["tagged_vt"],
    untaggedVt: json["untagged_vt"],
  );

  Map<String, dynamic> toJson() => {
    "learner": learner,
    "tagged_learner": taggedLearner,
    "untagged_learner": untaggedLearner,
    "vt": vt,
    "tagged_vt": taggedVt,
    "untagged_vt": untaggedVt,
  };
}
