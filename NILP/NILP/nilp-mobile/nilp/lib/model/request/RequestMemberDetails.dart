import 'dart:convert';

RequestMemberDetails requestMemberDetailsFromMap(String str) => RequestMemberDetails.fromMap(json.decode(str));

String requestMemberDetailsToMap(RequestMemberDetails data) => json.encode(data.toMap());

class RequestMemberDetails {
  RequestMemberDetails({
     this.data,
  });

  Data ?data;

  factory RequestMemberDetails.fromMap(Map<String, dynamic> json) => RequestMemberDetails(
    data: Data.fromMap(json["data"]),
  );

  Map<String, dynamic> toMap() => {
    "data": data!.toMap(),
  };
}

class Data {
  Data({
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
  });

  String ?id;
  String ?fullname;
  String ?address;
  String ?markAs;
  int ?age;
  String ?mobile;
  String ?gender;
  String ?proofType;
  String ?proofDetail;
  String ?socialCategoryId;
  String ?fatherName;
  String ?relation;
  String ?memberId;
  String ?memberFullname;
  String ?memberAddress;
  int ?memberMarkAs;
  int ?memberAge;
  String ?memberMobile;
  String ?memberGender;
  String ?memberProofType;
  String ?memberProofDetail;
  String ?memberSocialCategoryId;
  String ?memberFatherName;
  String ?memberRelation;

  factory Data.fromMap(Map<String, dynamic> json) => Data(
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
  };
}
