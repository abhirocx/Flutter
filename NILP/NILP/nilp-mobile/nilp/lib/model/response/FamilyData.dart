import 'dart:convert';

FamilyDetails familyDetailsFromJson(String str) => FamilyDetails.fromJson(json.decode(str));

String familyDetailsToJson(FamilyDetails data) => json.encode(data.toJson());

class FamilyDetails {
  FamilyDetails({
    required this.userid,

    required this.name,
    required this.address,
    required this.markAs,
    required this.age,
    required this.mobile,
    required this.gender,
    required this.proofType,
    required this.proofDetail,
    required this.socialCategoryId,
    required this.fatherName,
    required this.relation,
    required this.memberName,
    required this.memberAddress,
    required this.memberMarkAs,
    required this.memberAge,
    required this.memberMobile,
    required this.memberGender,
    required this.memberProofType,
    required this.memberProofDetail,
    required this.memberSocialCategoryId,
    required this.memberFatherName,
    required this.memberRelation,
    required this.vtType,
    required this.qualification,
    required this.memberVtType,
    required this.memberQualification,
  });

  final String userid;
  final String name;
  final String address;
  final String markAs;
  final int age;
  final String mobile;
  final String gender;
  final String proofType;
  final String proofDetail;
  final String socialCategoryId;
  final String fatherName;
  final String relation;
  final String memberName;
  final String memberAddress;
  final String memberMarkAs;
  final int memberAge;
  final String memberMobile;
  final String memberGender;
  final String memberProofType;
  final String memberProofDetail;
  final String memberSocialCategoryId;
  final String memberFatherName;
  final String memberRelation;
  final String vtType;
  final String qualification;
  final String memberVtType;
  final String memberQualification;

  factory FamilyDetails.fromJson(Map<String, dynamic> json) => FamilyDetails(
    userid: json["userid"],
    name: json["name"],
    address: json["address"],
    markAs: json["mark_as"],
    age: json["age"],
    mobile: json["mobile"],
    gender: json["gender"],
    proofType: json["proof_type"],
    proofDetail: json["proof_detail"],
    socialCategoryId: json["social_category_id"],
    fatherName: json["father_name"],
    relation: json["relation"],
    memberName: json["member_name"],
    memberAddress: json["member_address"],
    memberMarkAs: json["member_mark_as"],
    memberAge: json["member_age"],
    memberMobile: json["member_mobile"],
    memberGender: json["member_gender"],
    memberProofType: json["member_proof_type"],
    memberProofDetail: json["member_proof_detail"],
    memberSocialCategoryId: json["member_social_category_id"],
    memberFatherName: json["member_father_name"],
    memberRelation: json["member_relation"],
    vtType: json["vt_type"],
    qualification: json["qualification"],
    memberVtType: json["member_vt_type"],
    memberQualification: json["member_qualification"],
  );

  Map<String, dynamic> toJson() => {
    "userid": userid,
    "name": name,
    "address": address,
    "mark_as": markAs,
    "age": age,
    "mobile": mobile,
    "gender": gender,
    "proof_type": proofType,
    "proof_detail": proofDetail,
    "social_category_id": socialCategoryId,
    "father_name": fatherName,
    "relation": relation,
    "member_name": memberName,
    "member_address": memberAddress,
    "member_mark_as": memberMarkAs,
    "member_age": memberAge,
    "member_mobile": memberMobile,
    "member_gender": memberGender,
    "member_proof_type": memberProofType,
    "member_proof_detail": memberProofDetail,
    "member_social_category_id": memberSocialCategoryId,
    "member_father_name": memberFatherName,
    "member_relation": memberRelation,
    "vt_type": vtType,
    "qualification": qualification,
    "member_vt_type": memberVtType,
    "member_qualification": memberQualification,
  };
}
