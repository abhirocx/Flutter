import 'dart:convert';

ReequestVt reequestVtFromMap(String str) =>
    ReequestVt.fromMap(json.decode(str));

String reequestVtToMap(ReequestVt data) => json.encode(data.toMap());

class ReequestVt {
  ReequestVt({
    required this.id,
  });

  final String id;

  factory ReequestVt.fromMap(Map<String, dynamic> json) => ReequestVt(
        id: json["id"],
      );

  Map<String, dynamic> toMap() => {
        "id": id,
      };

  factory ReequestVt.fromJson(Map<String, dynamic> json) => ReequestVt(
        id: json["id"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
      };
}
