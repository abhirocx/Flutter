// To parse this JSON data, do
//
//     final responseEditVtLearner = responseEditVtLearnerFromMap(jsonString);

import 'dart:convert';

ResponseEditVtLearner responseEditVtLearnerFromMap(String str) => ResponseEditVtLearner.fromMap(json.decode(str));

String responseEditVtLearnerToMap(ResponseEditVtLearner data) => json.encode(data.toMap());

class ResponseEditVtLearner {
  ResponseEditVtLearner({
    this.data,
  });

  List<Datum> ?data;

  factory ResponseEditVtLearner.fromMap(Map<String, dynamic> json) => ResponseEditVtLearner(
    data: List<Datum>.from(json["data"].map((x) => Datum.fromMap(x))),
  );

  Map<String, dynamic> toMap() => {
    "data": List<dynamic>.from(data!.map((x) => x.toMap())),
  };
}

class Datum {
  Datum({
    this.id,
    this.fullname,
    this.name,
    // this.mobile,
    // this.email,
    this.address,
    // this.age,
    // this.gender,
    // this.proofType,
    // this.proofDetail,
    // this.socialCategoryId,
    // this.fatherName,
    // this.memberType,
    // this.roleCode,
    // this.markAs,
    // this.profession,
    // this.qualification,
    // this.vtType,
    // this.relation,
    // this.vt,
    // this.parent,
  });

  String? id;
  String? fullname;
  String? name;
  // String mobile;
  // dynamic email;
  dynamic address;
  // int age;
  // String gender;
  // int proofType;
  // String proofDetail;
  // int socialCategoryId;
  // String fatherName;
  // int memberType;
  // String roleCode;
  // String markAs;
  // int profession;
  // dynamic qualification;
  // dynamic vtType;
  // int relation;
  // String vt;
  // String parent;

  factory Datum.fromMap(Map<String, dynamic> json) => Datum(
    id: json["id"],
    fullname: json["fullname"],
    name: json["name"],
    // mobile: json["mobile"],
    // email: json["email"],
    address: json["address"],
    // age: json["age"],
    // gender: json["gender"],
    // proofType: json["proof_type"],
    // proofDetail: json["proof_detail"],
    // socialCategoryId: json["social_category_id"],
    // fatherName: json["father_name"],
    // memberType: json["member_type"],
    // roleCode: json["role_code"],
    // markAs: json["mark_as"],
    // profession: json["profession"],
    // qualification: json["qualification"],
    // vtType: json["vt_type"],
    // relation: json["relation"],
    // vt: json["vt"],
    // parent: json["parent"],
  );

  Map<String, dynamic> toMap() => {
    "id": id,
    "fullname": fullname,
    "name": name,
    // "mobile": mobile,
    // "email": email,
    "address": address,
    // "age": age,
    // "gender": gender,
    // "proof_type": proofType,
    // "proof_detail": proofDetail,
    // "social_category_id": socialCategoryId,
    // "father_name": fatherName,
    // "member_type": memberType,
    // "role_code": roleCode,
    // "mark_as": markAs,
    // "profession": profession,
    // "qualification": qualification,
    // "vt_type": vtType,
    // "relation": relation,
    // "vt": vt,
    // "parent": parent,
  };
}
