import 'dart:convert';

RequestSearchMember requestSearchMemberFromJson(String str) =>
    RequestSearchMember.fromJson(json.decode(str));

String requestSearchMemberToJson(RequestSearchMember data) =>
    json.encode(data.toJson());

class RequestSearchMember {
  RequestSearchMember({
    required this.search,
    required this.page,
    required this.size,
  });

  final String search;
  final int page;
  final int size;

  factory RequestSearchMember.fromJson(Map<String, dynamic> json) =>
      RequestSearchMember(
        search: json["search"],
        page: json["page"],
        size: json["size"],
      );

  Map<String, dynamic> toJson() => {
        "search": search,
        "page": page,
        "size": size,
      };

  factory RequestSearchMember.fromMap(Map<String, dynamic> json) =>
      RequestSearchMember(
        search: json["search"],
        page: json["page"],
        size: json["size"],
      );

  Map<String, dynamic> toMap() => {
        "search": search,
        "page": page,
        "size": size,
      };
}
