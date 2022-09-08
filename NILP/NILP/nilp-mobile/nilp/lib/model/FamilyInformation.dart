import 'package:meta/meta.dart';
import 'dart:convert';

FamilyInformation familyInformationFromJson(String str) => FamilyInformation.fromJson(json.decode(str));

String familyInformationToJson(FamilyInformation data) => json.encode(data.toJson());

class FamilyInformation {
  FamilyInformation({
    required this.data,
  });

  List<Family_Heads> data;

  factory FamilyInformation.fromJson(Map<dynamic, dynamic> json) => FamilyInformation(
    data: List<Family_Heads>.from(json["data"].map((x) => Family_Heads.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "data": List<dynamic>.from(data.map((x) => x.toJson())),
  };
}

class Family_Heads {
  Family_Heads({
     this.name,
     this.address,
     this.mark_as,
     this.age,
     this.mobile,
     this.gender,
     this.proof_type,
     this.proof_detail,
     this.social_category_id,
     this.fatherName,
     this.relation,
     // this.member_id,
     this.member_name,
     this.member_address,
     this.member_mark_as,
     this.member_age,
     this.member_mobile,
     this.member_gender,
     this.member_proof_type,
     this.member_proof_detail,
     this.member_social_category_id,
     this.member_father_name,
     this.member_relation,
    this.vt_type,
    this.qualification,
    this.member_vt_type,
    this.member_qualification,
  });

  String? name;
  String? address;
  String? mark_as;
  String? age;
  String? vt_type;
  String? qualification;
  String? member_vt_type;
  String? member_qualification;
  String? mobile;
  String? gender;
  String? proof_type;
  String? proof_detail;
  String? social_category_id;
  String? fatherName;
  String? relation;
  String? member_name;
  String? member_address;
  String? member_mark_as;
  String? member_age;
  String? member_mobile;
  String? member_gender;
  String? member_proof_type;
  String? member_proof_detail;
  String? member_social_category_id;
  String? member_father_name;
  String? member_relation;

  factory Family_Heads.fromJson(Map<String, dynamic> json) => Family_Heads(
    name: json["name"],
    address: json["address"],
    mark_as: json["mark_as"],
    age: json ["age"],
    mobile: json["mobile"],
    gender: json["gender"],
    proof_type: json["proof_type"],
    proof_detail: json["proof_detail"],
    social_category_id: json["social_category_id"],
    fatherName: json["father_name"],
    relation: json["relation"],
    member_name: json["member_name"],
    member_address: json["member_address"],
    member_mark_as: json["member_mark_as"],
    member_age: json["member_age"],
    member_mobile: json["member_mobile"],
    member_gender: json["member_gender"],
    member_proof_type: json["member_proof_type"],
    member_proof_detail: json["member_proof_detail"],
    member_social_category_id: json["member_social_category_id"],
    member_father_name: json["member_father_name"],
    member_relation: json["member_relation"],
  );

  Map<String, dynamic> toJson() => {
    "name": name,
    "address": address,
    "mark_as": mark_as,
    "age": age,
    "mobile": mobile,
    "gender": gender,
    "proof_type": proof_type,
    "proof_detail": proof_detail,
    "social_category_id": social_category_id,
    "father_name": fatherName,
    "relation": relation,
    "member_name": member_name,
    "member_address": member_address,
    "member_mark_as": member_mark_as,
    "member_age": member_age,
    "member_mobile": member_mobile,
    "member_gender": member_gender,
    "member_proof_type": member_proof_type,
    "member_proof_detail": member_proof_detail,
    "member_social_category_id": member_social_category_id,
    "member_father_name": member_father_name,
    "member_relation": member_relation,
    "vt_type": vt_type,
    "qualification": qualification,
    "member_vt_type": member_vt_type,
    "member_qualification": member_qualification,
  };
}
