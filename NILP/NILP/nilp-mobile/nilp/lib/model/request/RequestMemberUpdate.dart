import 'dart:convert';

RequestMemberUpdate requestMemberUpdateFromMap(String str) => RequestMemberUpdate.fromMap(json.decode(str));

String requestMemberUpdateToMap(RequestMemberUpdate data) => json.encode(data.toMap());

class RequestMemberUpdate {
  RequestMemberUpdate({
    this.fullname,
    this.id,
    this.mobile,
    this.address,
    this.markAs,
    this.age,
    this.gender,
    this.proofType,
    this.proofDetail,
    this.profession,
    this.qualification,
    this.socialCategoryId,
    this.fatherName,
    this.relation,
    this.vtType,
    this.is_divyang,
  });

  String ?fullname;
  String ?mobile;
  String ?id;
  String ?address;
  String ?markAs;
  int ?age;
  String ?gender;
  String ?proofType;
  String ?proofDetail;
  String ?profession;
  String ?qualification;
  String ?socialCategoryId;
  String ?fatherName;
  String ?relation;
  String ?vtType;
  bool ?is_divyang;

  factory RequestMemberUpdate.fromMap(Map<String, dynamic> json) => RequestMemberUpdate(
    fullname: json["fullname"],
    id: json["id"],
    mobile: json["mobile"],
    address: json["address"],
    markAs: json["mark_as"],
    age: json["age"],
    gender: json["gender"],
    proofType: json["proof_type"],
    proofDetail: json["proof_detail"],
    profession: json["profession"],
    qualification: json["qualification"],
    socialCategoryId: json["social_category_id"],
    fatherName: json["father_name"],
    relation: json["relation"],
    vtType: json["vt_type"],
    is_divyang: json["is_divyang"],
  );

  Map<String, dynamic> toMap() => {
    "fullname": fullname,
    "id": id,
    "mobile": mobile,
    "address": address,
    "mark_as": markAs,
    "age": age,
    "gender": gender,
    "proof_type": proofType,
    "proof_detail": proofDetail,
    "profession": profession,
    "qualification": qualification,
    "social_category_id": socialCategoryId,
    "father_name": fatherName,
    "relation": relation,
    "vt_type": vtType,
    "is_divyang": is_divyang,
  };
}
