import 'dart:convert';

FamilyInfo familyInfoFromMap(String str) =>
    FamilyInfo.fromMap(json.decode(str));

String familyInfoToMap(FamilyInfo data) => json.encode(data.toMap());
String familyInfoToJson(FamilyInfo data) => json.encode(data.toJson());

class FamilyInfo {
  FamilyInfo({
    required this.data,
  });

  List<Family_Head> data;

  factory FamilyInfo.fromMap(Map<String, dynamic> json) => FamilyInfo(
        data: List<Family_Head>.from(
            json["data"].map((x) => Family_Head.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "data": List<dynamic>.from(data.map((x) => x.toMap())),
      };

  factory FamilyInfo.fromJson(Map<String, dynamic> json) => FamilyInfo(
        data: List<Family_Head>.from(
            json["data"].map((x) => Family_Head.fromJson(x))),
      );

  Map<String, dynamic> toJson() => {
        "data": List<dynamic>.from(data.map((x) => x.toJson())),
      };
}

class Family_Head {
  Family_Head({
    this.name,
    this.id,
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
    this.members,
  });

  String? name;
  String? address;
  String? markAs;
  String? age;
  String? mobile;
  String? gender;
  String? proofType;
  String? proofDetail;
  String? profession;
  String? qualification;
  String? socialCategoryId;
  String? fatherName;
  int? isOnlyHead;
  int? id;
  int? isdivyang;
  String? vtType;

  List<Members>? members;

  factory Family_Head.fromMap(Map<String, dynamic> json) => Family_Head(
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
        id: json["id"],
        vtType: json["vt_type"],
        isdivyang: json["is_divyang"],
        members:
            List<Members>.from(json["members"].map((x) => Members.fromMap(x))),
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
        "id": id,
        "vt_type": vtType,
        "is_divyang": isdivyang,
        "members": members == null
            ? null
            : List<dynamic>.from(members!.map((x) => x.toMap())),
      };

  factory Family_Head.fromJson(Map<String, dynamic> json) => Family_Head(
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
        id: json["id"],
        vtType: json["vt_type"],
        isdivyang: json["is_divyang"],
        members:
            List<Members>.from(json["members"].map((x) => Members.fromJson(x))),
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
        "id": id,
        "vt_type": vtType,
        "is_divyang": isdivyang,
        "members": members == null
            ? null
            : List<dynamic>.from(members!.map((x) => x.toJson())),
      };
}

class Members {
  Members({
    this.memberName,
    this.memberAddress,
    this.head_id,
    this.member_id,
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
    this.memberQualification,
    this.memberDivyang,
  });

  String? memberName;
  int? head_id;
  int? member_id;
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
  String? memberQualification;
  int? memberDivyang;
  factory Members.fromMap(Map<String, dynamic> json) => Members(
        memberName: json["member_name"],
        head_id: json["head_id"],
        member_id: json["member_id"],
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
        memberQualification: json["member_qualification"],
        memberDivyang: json["member_divyang"],
      );

  Map<String, dynamic> toMap() => {
        "member_name": memberName,
        "member_address": memberAddress,
        "head_id": head_id,
        "member_id": member_id,
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
        "member_qualification": memberQualification,
        "member_divyang": memberDivyang,
      };

  factory Members.fromJson(Map<String, dynamic> json) => Members(
        memberName: json["member_name"],
        head_id: json["head_id"],
        member_id: json["member_id"],
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
        memberQualification: json["member_qualification"],
        memberDivyang: json["member_divyang"],
      );

  Map<String, dynamic> toJson() => {
        "member_name": memberName,
        "head_id": head_id,
        "member_id": member_id,
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
        "member_qualification": memberQualification,
        "member_divyang": memberDivyang,
      };
}
