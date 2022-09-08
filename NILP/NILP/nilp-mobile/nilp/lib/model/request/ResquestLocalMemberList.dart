import 'dart:convert';

ResquestLocalMemberList resquestLocalMemberListFromJson(String str) =>
    ResquestLocalMemberList.fromJson(json.decode(str));

String? resquestLocalMemberListToJson(ResquestLocalMemberList data) =>
    json.encode(data.toJson());

ResquestLocalMemberList resquestLocalMemberListFromMap(String str) => ResquestLocalMemberList.fromMap(json.decode(str));

String resquestLocalMemberListToMap(ResquestLocalMemberList data) => json.encode(data.toMap());

class ResquestLocalMemberList {
  ResquestLocalMemberList({
    this.id,
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
    this.relation,
    this.vtType,
    this.qualification,
    this.isOnlyMember,
    this.profession,
    this.isdivyang,
    this.members,
  });

  int? id;
  String? fullname;
  String? address;
  String? markAs;
  int? age;
  String? mobile;
  String? gender;
  String? proofType;
  String? proofDetail;
  String? socialCategoryId;
  String? fatherName;
  String? relation;
  String? vtType;
  String? qualification;
  String? profession;
  bool? isdivyang;
  bool? isOnlyMember;
  List<Member>? members;

  factory ResquestLocalMemberList.fromJson(Map<String?, dynamic> json) =>
      ResquestLocalMemberList(
        id: json["id"],
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
        relation: json["relation"],
        vtType: json["vt_type"],
        profession: json["profession"],
        qualification: json["qualification"],
        isdivyang: json["is_divyang"],
        isOnlyMember: json["is_only_member"],
        members:
            List<Member>.from(json["members"].map((x) => Member.fromJson(x))),
      );

  Map<String?, dynamic> toJson() => {
        "id": id,
        "fullname": fullname,
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
        "vt_type": vtType,
        "profession": profession,
        "qualification": qualification,
        "is_divyang": isdivyang,
        "is_only_member": isOnlyMember,
        "members": List<dynamic>.from(members!.map((x) => x.toJson())),
      };


  factory ResquestLocalMemberList.fromMap(Map<String, dynamic> json) => ResquestLocalMemberList(
    id: json["id"],
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
    relation: json["relation"],
    vtType: json["vt_type"],
    qualification: json["qualification"],
    profession: json["profession"],
    isdivyang: json["is_divyang"],
    isOnlyMember: json["is_only_member"],
    members: List<Member>.from(json["members"].map((x) => Member.fromMap(x))),
  );

  Map<String, dynamic> toMap() => {
    "id": id,
    "fullname": fullname,
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
    "vt_type": vtType,
    "qualification": qualification,
    "profession": profession,
    "is_divyang": isdivyang,
    "is_only_member": isOnlyMember,
    "members": List<dynamic>.from(members!.map((x) => x.toMap())),
  };
}

class Member {
  Member({
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
    this.memberVtType,
    this.memberQualification,
    this.member_profession,
    this.memberDivyang,
  });

  int? memberId;
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
  String? memberVtType;
  String? member_profession;
  bool? memberDivyang;
  String? memberQualification;


  factory Member.fromMap(Map<String, dynamic> json) => Member(
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
    memberVtType: json["member_vt_type"],
    memberQualification: json["member_qualification"],
    member_profession: json["member_profession"],
    memberDivyang: json["member_is_divyang"],
  );

  Map<String, dynamic> toMap() => {
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
    "member_vt_type": memberVtType,
    "member_qualification": memberQualification,
    "member_profession": member_profession,
    "member_is_divyang": memberDivyang,
  };

  factory Member.fromJson(Map<String?, dynamic> json) => Member(
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
        memberVtType: json["member_vt_type"],
        member_profession: json["member_profession"],
        memberQualification: json["member_qualification"],
        memberDivyang: json["member_is_divyang"],
      );

  Map<String?, dynamic> toJson() => {
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
        "member_vt_type": memberVtType,
        "member_profession": member_profession,
        "member_qualification": memberQualification,
        "member_is_divyang": memberDivyang,
      };
}
