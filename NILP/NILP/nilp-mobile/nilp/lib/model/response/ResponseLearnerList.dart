// To parse this JSON data, do
//
//     final responseLearnerList = responseLearnerListFromJson(jsonString);

import 'dart:convert';

ResponseLearnerList responseLearnerListFromJson(String str) => ResponseLearnerList.fromJson(json.decode(str));

String responseLearnerListToJson(ResponseLearnerList data) => json.encode(data.toJson());

class ResponseLearnerList {
  ResponseLearnerList({
    this.data,
    this.meta,
  });

  List<Datum> ?data;
  Meta ?meta;

  factory ResponseLearnerList.fromJson(Map<String, dynamic> json) => ResponseLearnerList(
    data: List<Datum>.from(json["data"].map((x) => Datum.fromJson(x))),
    meta: Meta.fromJson(json["meta"]),
  );

  Map<String, dynamic> toJson() => {
    "data": List<dynamic>.from(data!.map((x) => x.toJson())),
    "meta": meta!.toJson(),
  };
}

class Datum {
  Datum({
    this.id,
    this.fullname,
    this.name,
    this.mobile,
    this.email,
    this.address,
    this.age,
    this.gender,
    this.proofType,
    this.proofDetail,
    this.socialCategoryId,
    this.fatherName,
    this.memberType,
    this.roleCode,
    this.markAs,
    this.profession,
    this.qualification,
    this.vtType,
    this.relation,
    this.vt,
    this.isDivyang,
    this.members,
  });

  String ?id;
  String ?fullname;
  String ?name;
  String ?mobile;
  String ?email;
  String ?address;
  int ?age;
  String ?gender;
  int ?proofType;
  String ?proofDetail;
  int ?socialCategoryId;
  String ?fatherName;
  int ?memberType;
  String ?roleCode;
  String ?markAs;
  int ?profession;
  int ?qualification;
  int ?vtType;
  int ?relation;
  String ?vt;
  bool ?isDivyang;
  List<Datum> ?members;

  factory Datum.fromJson(Map<String, dynamic> json) => Datum(
    id: json["id"],
    fullname: json["fullname"],
    name: json["name"],
    mobile: json["mobile"],
    email: json["email"],
    address: json["address"],
    age: json["age"],
    gender: json["gender"],
    proofType: json["proof_type"],
    proofDetail: json["proof_detail"],
    socialCategoryId: json["social_category_id"],
    fatherName: json["father_name"],
    memberType: json["member_type"],
    roleCode: json["role_code"],
    markAs: json["mark_as"],
    profession: json["profession"],
    qualification: json["qualification"],
    vtType: json["vt_type"],
    relation: json["relation"],
    vt: json["vt"],
    isDivyang: json["is_divyang"],
    members: json["members"] == null ? null : List<Datum>.from(json["members"].map((x) => Datum.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "fullname": fullname,
    "name": name,
    "mobile": mobile,
    "email": email,
    "address": address,
    "age": age,
    "gender": gender,
    "proof_type": proofType,
    "proof_detail": proofDetail,
    "social_category_id": socialCategoryId,
    "father_name": fatherName,
    "member_type": memberType,
    "role_code": roleCode,
    "mark_as": markAs,
    "profession": profession,
    "qualification": qualification,
    "vt_type": vtType,
    "relation": relation,
    "vt": vt,
    "is_divyang": isDivyang,
    "members": members == null ? null : List<dynamic>.from(members!.map((x) => x.toJson())),
  };
}

class Meta {
  Meta({
    this.totalCount,
    this.totalPage,
  });

  int ?totalCount;
  int ?totalPage;

  factory Meta.fromJson(Map<String, dynamic> json) => Meta(
    totalCount: json["total_count"],
    totalPage: json["total_page"],
  );

  Map<String, dynamic> toJson() => {
    "total_count": totalCount,
    "total_page": totalPage,
  };
}
