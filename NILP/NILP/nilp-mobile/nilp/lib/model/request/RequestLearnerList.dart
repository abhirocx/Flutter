// To parse this JSON data, do
//
//     final requestLearnerList = requestLearnerListFromJson(jsonString);

import 'dart:convert';

RequestLearnerList requestLearnerListFromJson(String str) => RequestLearnerList.fromJson(json.decode(str));

String requestLearnerListToJson(RequestLearnerList data) => json.encode(data.toJson());

class RequestLearnerList {
  RequestLearnerList({
    this.search,
    this.type,
    this.page,
    this.size,
  });

  String ?search;
  String ?type;
  int ?page;
  int ?size;

  factory RequestLearnerList.fromJson(Map<String, dynamic> json) => RequestLearnerList(
    search: json["search"],
    type: json["type"],
    page: json["page"],
    size: json["size"],
  );

  Map<String, dynamic> toJson() => {
    "search": search,
    "type": type,
    "page": page,
    "size": size,
  };
}
