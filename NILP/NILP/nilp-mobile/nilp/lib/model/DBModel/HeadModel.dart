import 'dart:convert';

HeadModel headModelFromMap(String str) => HeadModel.fromMap(json.decode(str));

String headModelToMap(HeadModel data) => json.encode(data.toMap());

HeadModel headModelFromJson(String str) => HeadModel.fromJson(json.decode(str));

String headModelToJson(HeadModel data) => json.encode(data.toJson());


class HeadModel {
  HeadModel({
    this.name,
    this.address,
    this.markAs,
    this.age,
    this.mobile,
    this.gender,
    this.proofType,
    this.proofDetail,
    this.profession,
    this.qualification,
    this.socialCategoryId,
    this.fatherName,
    this.isOnlyHead,
    this.vtType,
    this.isdivyang,
  });

  String ? name;
  String ? address;
  String ? markAs;
  String ? age;
  String ? mobile;
  String ? gender;
  String ? proofType;
  String ? proofDetail;
  String ? profession;
  String ? qualification;
  String ? socialCategoryId;
  String ? fatherName;
  int ? isOnlyHead;
  String ? vtType;
  int? isdivyang;

  factory HeadModel.fromMap(Map<String, dynamic> json) => HeadModel(
    name: json["name"],
    address: json["address"],
    markAs: json["mark_as"],
    age: json["age"],
    mobile: json["mobile"],
    gender: json["gender"],
    proofType: json["proof_type"],
    proofDetail: json["proof_detail"],
    profession: json["profession"],
    qualification: json["qualification"],
    socialCategoryId: json["social_category_id"],
    fatherName: json["father_name"],
    isOnlyHead: json["is_only_head"],
    vtType: json["vt_type"],
    isdivyang: json["is_divyang"],
  );

  Map<String, dynamic> toMap() => {
    "name": name,
    "address": address,
    "mark_as": markAs,
    "age": age,
    "mobile": mobile,
    "gender": gender,
    "proof_type": proofType,
    "proof_detail": proofDetail,
    "profession": profession,
    "qualification": qualification,
    "social_category_id": socialCategoryId,
    "father_name": fatherName,
    "is_only_head": isOnlyHead,
    "vt_type": vtType,
    "is_divyang": isdivyang,
  };

  factory HeadModel.fromJson(Map<String, dynamic> json) => HeadModel(
    name: json["name"],
    address: json["address"],
    markAs: json["mark_as"],
    age: json["age"],
    mobile: json["mobile"],
    gender: json["gender"],
    proofType: json["proof_type"],
    proofDetail: json["proof_detail"],
    profession: json["profession"],
    qualification: json["qualification"],
    socialCategoryId: json["social_category_id"],
    fatherName: json["father_name"],
    isOnlyHead: json["is_only_head"],
    vtType: json["vt_type"],
    isdivyang: json["is_divyang"],
  );

  Map<String, dynamic> toJson() => {
    "name": name,
    "address": address,
    "mark_as": markAs,
    "age": age,
    "mobile": mobile,
    "gender": gender,
    "proof_type": proofType,
    "proof_detail": proofDetail,
    "profession": profession,
    "qualification": qualification,
    "social_category_id": socialCategoryId,
    "father_name": fatherName,
    "is_only_head": isOnlyHead,
    "vt_type": vtType,
    "is_divyang": isdivyang,
  };

}
