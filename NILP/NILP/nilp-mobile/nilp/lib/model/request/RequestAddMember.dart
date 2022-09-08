import 'dart:convert';

RequestAddMember requestAddMemberFromMap(String str) =>
    RequestAddMember.fromMap(json.decode(str));

String requestAddMemberToMap(RequestAddMember data) =>
    json.encode(data.toMap());

class RequestAddMember {


  RequestAddMember({
    this.id,
    this.is_only_member,
    this.fullname,
    this.address,
    this.markAs,
    this.age,
    this.mobile,
    this.gender,
    this.proofType,
    this.proofDetail,
    this.socialCategoryId,
    this.fatherName,
    this.profession,
    //this.relation,
    this.memberId,
    this.memberFullname,
    this.memberAddress,
    this.memberMarkAs,
    this.memberAge,
    this.memberMobile,
    this.memberGender,
    this.memberProofType,
    this.memberProofDetail,
    this.memberSocialCategoryId,
    this.memberFatherName,
    this.memberRelation,
    this.vt_type,
    this.qualification,
    this.member_vt_type,
    this.member_qualification,
    this.member_profession,
    this.is_divyang,
    this.member_is_divyang,
  });

  String? id;
  bool? is_only_member;
  String? vt_type;
  String? qualification;
  String? fullname;
  String? address;
  String? markAs;
  String? age;
  String? mobile;
  String? gender;
  String? proofType;
  String? proofDetail;
  String? socialCategoryId;
  String? fatherName;
  String? profession;
  // String? relation;
  String? memberId;
  String? memberFullname;
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
  String? member_vt_type;
  String? member_qualification;
  String? member_profession;
  bool? is_divyang;
  bool? member_is_divyang;

  factory RequestAddMember.fromMap(Map<String, dynamic> json) =>
      RequestAddMember(
        id: json["id"],
        is_only_member: json["is_only_member"],
        fullname: json["fullname"],
        address: json["address"],
        markAs: json["mark_as"],
        age: json["age"],
        mobile: json["mobile"],
        gender: json["gender"],
        proofType: json["proof_type"],
        proofDetail: json["proof_detail"],
        socialCategoryId: json["social_category_id"],
        fatherName: json["father_name"],
        profession: json["profession"],
        memberId: json["member_id"],
        memberFullname: json["member_fullname"],
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
        vt_type: json["vt_type"],
        qualification: json["qualification"],
        member_vt_type: json["member_vt_type"],
        member_qualification: json["member_qualification"],
        member_profession: json["member_profession"],
        is_divyang: json["is_divyang"],
        member_is_divyang: json["member_is_divyang"],
      );

  Map<String, dynamic> toMap() => {
        "vt_type": vt_type,
        "is_only_member": is_only_member,
        "member_is_divyang": member_is_divyang,
        "is_divyang": is_divyang,
        "qualification": qualification,
        "member_vt_type": member_vt_type,
        "member_qualification": member_qualification,
        "id": id,
        "fullname": fullname,
        "address": address,
        "mark_as": markAs,
        "age": age,
        "mobile": mobile,
        "gender": gender,
        "profession": profession,
        "proof_type": proofType,
        "proof_detail": proofDetail,
        "social_category_id": socialCategoryId,
        "father_name": fatherName,
        "member_id": memberId,
        "member_fullname": memberFullname,
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
        "member_profession": member_profession,
      };
}
