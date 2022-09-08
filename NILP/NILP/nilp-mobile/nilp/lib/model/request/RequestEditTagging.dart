import 'dart:convert';

RequestEditTagging requestTaggingFromMap(String str) =>
    RequestEditTagging.fromMap(json.decode(str));

String requestTaggingToMap(RequestEditTagging data) => json.encode(data.toMap());

class RequestEditTagging {
  RequestEditTagging({
    required this.learner,
    required this.vt,
  });

  final String learner;
  final String vt;

  factory RequestEditTagging.fromMap(Map<String, dynamic> json) => RequestEditTagging(
        learner:json["learner"],
        vt: json["vt"],
      );

  Map<String, dynamic> toMap() => {
        "learner": learner,
        "vt": vt,
      };

  factory RequestEditTagging.fromJson(Map<String, dynamic> json) => RequestEditTagging(
        learner:"learner",
        vt: json["vt"],
      );

  Map<String, dynamic> toJson() => {
        "learner":learner,
        "vt": vt,
      };
}
