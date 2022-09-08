import 'package:meta/meta.dart';
import 'dart:convert';

ResponseMemberDetails responseMemberDetailsFromMap(String str) => ResponseMemberDetails.fromMap(json.decode(str));

String responseMemberDetailsToMap(ResponseMemberDetails data) => json.encode(data.toMap());

class ResponseMemberDetails {
  ResponseMemberDetails({
    this.data,
  });

  Datum ?data;

  factory ResponseMemberDetails.fromMap(Map<String, dynamic> json) => ResponseMemberDetails(
    data: Datum.fromMap(json["data"]),
  );

  Map<String, dynamic> toMap() => {
    "data": data!.toMap(),
  };
}

class Datum {
  Datum({
    this.id,
    this.fullname,
    this.mobile,
    this.email,
    this.address,
    this.markAs,
    this.age,
    this.gender,
    this.proofType,
    this.proofDetail,
    this.socialCategoryId,
    this.fatherName,
    this.relation,
    this.parent,
    this.memberType,
    this.members,
  });

  String ?id;
  String ?fullname;
  String ?mobile;
  String ?email;
  String ?address;
  String ?markAs;
  int ?age;
  String ?gender;
  int ?proofType;
  String ?proofDetail;
  int ?socialCategoryId;
  String ?fatherName;
  int ?relation;
  String ?parent;
  int ?memberType;
  List<Datum> ?members;

  factory Datum.fromMap(Map<String, dynamic> json) => Datum(
    id: json["id"],
    fullname: json["fullname"],
    mobile: json["mobile"],
    email: json["email"],
    address: json["address"] == null ? null : json["address"],
    markAs: json["mark_as"],
    age: json["age"],
    gender: json["gender"] == null ? null : json["gender"],
    proofType: json["proof_type"],
    proofDetail: json["proof_detail"],
    socialCategoryId: json["social_category_id"],
    fatherName: json["father_name"] == null ? null : json["father_name"],
    relation: json["relation"],
    parent: json["parent"] == null ? null : json["parent"],
    memberType: json["member_type"],
    members: json["members"] == null ? null : List<Datum>.from(json["members"].map((x) => Datum.fromMap(x))),
  );

  Map<String, dynamic> toMap() => {
    "id": id,
    "fullname": fullname,
    "mobile": mobile,
    "email": email,
    "address": address == null ? null : address,
    "mark_as": markAs,
    "age": age,
    "gender": gender == null ? null : gender,
    "proof_type": proofType,
    "proof_detail": proofDetail,
    "social_category_id": socialCategoryId,
    "father_name": fatherName == null ? null : fatherName,
    "relation": relation,
    "parent": parent == null ? null : parent,
    "member_type": memberType,
    "members": members == null ? null : List<dynamic>.from(members!.map((x) => x.toMap())),
  };
}
