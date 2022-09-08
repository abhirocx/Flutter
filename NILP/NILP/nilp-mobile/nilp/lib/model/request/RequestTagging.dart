import 'dart:convert';

RequestTagging requestTaggingFromMap(String str) =>
    RequestTagging.fromMap(json.decode(str));

String requestTaggingToMap(RequestTagging data) => json.encode(data.toMap());

class RequestTagging {
  RequestTagging({
    required this.learner,
    required this.vt,
  });

  final List<String> learner;
  final String vt;

  factory RequestTagging.fromMap(Map<String, dynamic> json) => RequestTagging(
        learner: List<String>.from(json["learner"].map((x) => x)),
        vt: json["vt"],
      );

  Map<String, dynamic> toMap() => {
        "learner": List<dynamic>.from(learner.map((x) => x)),
        "vt": vt,
      };

  factory RequestTagging.fromJson(Map<String, dynamic> json) => RequestTagging(
        learner: List<String>.from(json["learner"].map((x) => x)),
        vt: json["vt"],
      );

  Map<String, dynamic> toJson() => {
        "learner": List<dynamic>.from(learner.map((x) => x)),
        "vt": vt,
      };
}
