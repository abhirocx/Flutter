import 'dart:convert';

MemberModel memberModelFromJson(String str) =>
    MemberModel.fromJson(json.decode(str));

String memberModelToJson(MemberModel data) => json.encode(data.toJson());

MemberModel memberModelFromMap(String str) =>
    MemberModel.fromMap(json.decode(str));

String memberModelToMap(MemberModel data) => json.encode(data.toMap());

class MemberModel {
  MemberModel({
    this.memberName,
    this.memberAddress,
    this.head_id,
    this.memberMarkAs,
    this.memberAge,
    this.memberMobile,
    this.memberGender,
    this.memberProofType,
    this.memberProofDetail,
    this.memberSocialCategoryId,
    this.memberFatherName,
    this.memberRelation,
    this.memberVtType,
    this.memberProfession,
    this.memberDivyang,
    this.memberQualification,
  });

  String? memberName;
  int? head_id;

  String? memberAddress;
  String? memberMarkAs;
  String? memberAge;
  String? memberMobile;
  String? memberGender;
  String? memberProofType;
  String? memberProofDetail;
  String? memberSocialCategoryId;
  String? memberFatherName;
  String? memberRelation;
  String? memberVtType;
  String? memberProfession;
  int? memberDivyang;
  String? memberQualification;

  factory MemberModel.fromJson(Map<String, dynamic> json) => MemberModel(
        memberName: json["member_name"],
        head_id: json["head_id"],
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
        memberVtType: json["member_vt_type"],
        memberProfession: json["member_profession"],
    memberDivyang: json["member_divyang"],
        memberQualification: json["member_qualification"],
      );

  Map<String, dynamic> toJson() => {
        "member_name": memberName,
        "head_id": head_id,
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
        "member_vt_type": memberVtType,
        "member_profession": memberProfession,
        "member_divyang": memberDivyang,
        "member_qualification": memberQualification,
      };
  factory MemberModel.fromMap(Map<String, dynamic> json) => MemberModel(
        memberName: json["member_name"],
        head_id: json["head_id"],
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
        memberVtType: json["member_vt_type"],
        memberProfession: json["member_profession"],
    memberDivyang: json["member_divyang"],
        memberQualification: json["member_qualification"],
      );

  Map<String, dynamic> toMap() => {
        "member_name": memberName,
        "member_address": memberAddress,
        "head_id": head_id,
        "member_mark_as": memberMarkAs,
        "member_age": memberAge,
        "member_mobile": memberMobile,
        "member_gender": memberGender,
        "member_proof_type": memberProofType,
        "member_proof_detail": memberProofDetail,
        "member_social_category_id": memberSocialCategoryId,
        "member_father_name": memberFatherName,
        "member_relation": memberRelation,
        "member_vt_type": memberVtType,
        "member_profession": memberProfession,
        "member_divyang": memberDivyang,
        "member_qualification": memberQualification,
      };
}
