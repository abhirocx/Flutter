import 'dart:convert';

ResponseSearchLearner responseSearchMemberFromMap(String str) =>
    ResponseSearchLearner.fromMap(json.decode(str));

String responseSearchMemberToMap(ResponseSearchLearner data) =>
    json.encode(data.toMap());

class ResponseSearchLearner {
  ResponseSearchLearner({
    required this.data,
  });

  final List<Datum> data;

  factory ResponseSearchLearner.fromMap(Map<String, dynamic> json) =>
      ResponseSearchLearner(
        data: List<Datum>.from(json["data"].map((x) => Datum.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "data": List<dynamic>.from(data.map((x) => x.toMap())),
      };
}

class Datum {
  Datum({
    required this.id,
    required this.fullname,
    required this.mobile,
    required this.email,
    required this.address,
    required this.markAs,
    required this.age,
    required this.gender,
    required this.proofType,
    required this.proofDetail,
    required this.socialCategoryId,
    required this.fatherName,
    // required this.relation,
    // required this.parent,
    required this.memberType,
    required this.members,
  });

  final String id;
  final String fullname;
  final String mobile;
  final String email;
  final String address;
  final String? markAs;
  final int age;
  final String gender;
  final int proofType;
  final String proofDetail;
  final int socialCategoryId;
  final String fatherName;
  // final int relation;
  // final String parent;
  final int memberType;
  final List<Member> members;

  factory Datum.fromMap(Map<String, dynamic> json) => Datum(
        id: json["id"],
        fullname: json["fullname"],
        mobile: json["mobile"],
        email: json["email"],
        address: json["address"],
        markAs: json["mark_as"],
        age: json["age"],
        gender: json["gender"],
        proofType: json["proof_type"],
        proofDetail: json["proof_detail"],
        socialCategoryId: json["social_category_id"],
        fatherName: json["father_name"],
        // relation: json["relation"],
        // parent: json["parent"],
        memberType: json["member_type"],
        members:
            List<Member>.from(json["members"].map((x) => Member.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "id": id,
        "fullname": fullname,
        "mobile": mobile,
        "email": email,
        "address": address,
        "mark_as": markAs,
        "age": age,
        "gender": gender,
        "proof_type": proofType,
        "proof_detail": proofDetail,
        "social_category_id": socialCategoryId,
        "father_name": fatherName,
        // "relation": relation,
        // "parent": parent,
        "member_type": memberType,
        "members": List<dynamic>.from(members.map((x) => x.toMap())),
      };
}

class Member {
  Member({
    required this.id,
    required this.fullname,
    required this.mobile,
    required this.email,
    required this.address,
    required this.markAs,
    required this.age,
    required this.gender,
    required this.proofType,
    required this.proofDetail,
    required this.socialCategoryId,
    required this.fatherName,
    // required this.relation,
    // required this.parent,
    required this.memberType,
  });

  final String id;
  final String fullname;
  final String mobile;
  final String email;
  final String address;
  final String? markAs;
  final int age;
  final String gender;
  final int proofType;
  final String proofDetail;
  final int socialCategoryId;
  final String fatherName;
  // final int relation;
  // final String parent;
  final int memberType;

  factory Member.fromMap(Map<String, dynamic> json) => Member(
        id: json["id"],
        fullname: json["fullname"],
        mobile: json["mobile"],
        email: json["email"],
        address: json["address"],
        markAs: json["mark_as"],
        age: json["age"],
        gender: json["gender"],
        proofType: json["proof_type"],
        proofDetail: json["proof_detail"],
        socialCategoryId: json["social_category_id"],
        fatherName: json["father_name"],
        // relation: json["relation"],
        // parent: json["parent"],
        memberType: json["member_type"],
      );

  Map<String, dynamic> toMap() => {
        "id": id,
        "fullname": fullname,
        "mobile": mobile,
        "email": email,
        "address": address,
        "mark_as": markAs,
        "age": age,
        "gender": gender,
        "proof_type": proofType,
        "proof_detail": proofDetail,
        "social_category_id": socialCategoryId,
        "father_name": fatherName,
        // "relation": relation,
        // "parent": parent,
        "member_type": memberType,
      };
}
