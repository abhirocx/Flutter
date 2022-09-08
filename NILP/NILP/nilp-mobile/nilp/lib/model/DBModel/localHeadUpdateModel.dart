import 'dart:convert';

LocalHeadUpdateModel LocalHeadUpdateModelFromMap(String str) => LocalHeadUpdateModel.fromMap(json.decode(str));

String LocalHeadUpdateModelToMap(LocalHeadUpdateModel data) => json.encode(data.toMap());

LocalHeadUpdateModel LocalHeadUpdateModelFromJson(String str) => LocalHeadUpdateModel.fromJson(json.decode(str));

String LocalHeadUpdateModelToJson(LocalHeadUpdateModel data) => json.encode(data.toJson());


class LocalHeadUpdateModel {
  LocalHeadUpdateModel({
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
    this.isdivyang,
    this.vtType,
    this.id,

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
  int ? isdivyang;
  String ? vtType;
  int? id;

  factory LocalHeadUpdateModel.fromMap(Map<String, dynamic> json) => LocalHeadUpdateModel(
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
    isdivyang: json["is_divyang"],
    vtType: json["vt_type"],
    id: json["_id"],
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
    "is_divyang": isdivyang,
    "vt_type": vtType,
    "_id": id,
  };

  factory LocalHeadUpdateModel.fromJson(Map<String, dynamic> json) => LocalHeadUpdateModel(
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
    isdivyang: json["is_divyang"],
    vtType: json["vt_type"],
    id: json["_id"],
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
    "is_divyang": isdivyang,
    "vt_type": vtType,
    "_id": id,
  };

}
