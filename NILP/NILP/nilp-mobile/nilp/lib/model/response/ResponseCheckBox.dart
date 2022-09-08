import 'package:meta/meta.dart';
import 'dart:convert';

ResponseCheckBox responseCheckBoxFromJson(String str) => ResponseCheckBox.fromJson(json.decode(str));

String responseCheckBoxToJson(ResponseCheckBox data) => json.encode(data.toJson());

class ResponseCheckBox {
  ResponseCheckBox({
    required this.checked,
    required this.id,
  });

  late final bool checked;
  String id;

  factory ResponseCheckBox.fromJson(Map<String, dynamic> json) => ResponseCheckBox(
    checked: json["checked"],
    id: json["id"],
  );

  Map<String, dynamic> toJson() => {
    "checked": checked,
    "id": id,
  };
}