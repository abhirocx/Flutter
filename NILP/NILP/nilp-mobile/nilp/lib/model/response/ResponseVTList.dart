import 'dart:convert';

ResponseVtList responseVtListFromJson(String str) =>
    ResponseVtList.fromJson(json.decode(str));

String responseVtListToJson(ResponseVtList data) => json.encode(data.toJson());

class ResponseVtList {
  ResponseVtList({
    this.data,
  });

  Data? data;

  factory ResponseVtList.fromJson(Map<String, dynamic> json) => ResponseVtList(
        data: Data.fromJson(json["data"]),
      );

  Map<String, dynamic> toJson() => {
        "data": data!.toJson(),
      };

  factory ResponseVtList.fromMap(Map<String, dynamic> json) => ResponseVtList(
        data: Data.fromMap(json["data"]),
      );

  Map<String, dynamic> toMap() => {
        "data": data!.toMap(),
      };
}

class Data {
  Data({
    this.familyVt,
    this.neighbourVt,
    this.others,
  });

  List<FamilyVt>? familyVt;
  List<FamilyVt>? neighbourVt;
  List<FamilyVt>? others;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
        familyVt: List<FamilyVt>.from(
            json["family_vt"].map((x) => FamilyVt.fromJson(x))),
        neighbourVt: List<FamilyVt>.from(
            json["neighbour_vt"].map((x) => FamilyVt.fromJson(x))),
        others: List<FamilyVt>.from(
            json["others"].map((x) => FamilyVt.fromJson(x))),
      );

  Map<String, dynamic> toJson() => {
        "family_vt": List<dynamic>.from(familyVt!.map((x) => x.toJson())),
        "neighbour_vt": List<dynamic>.from(neighbourVt!.map((x) => x.toJson())),
        "others": List<dynamic>.from(others!.map((x) => x.toJson())),
      };

  factory Data.fromMap(Map<String, dynamic> json) => Data(
        familyVt: List<FamilyVt>.from(
            json["family_vt"].map((x) => FamilyVt.fromMap(x))),
        neighbourVt: List<FamilyVt>.from(
            json["neighbour_vt"].map((x) => FamilyVt.fromMap(x))),
        others:
            List<FamilyVt>.from(json["others"].map((x) => FamilyVt.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "family_vt": List<dynamic>.from(familyVt!.map((x) => x.toMap())),
        "neighbour_vt": List<dynamic>.from(neighbourVt!.map((x) => x.toMap())),
        "others": List<dynamic>.from(others!.map((x) => x.toMap())),
      };
}

class FamilyVt {
  FamilyVt({
    this.id,
    this.name,
    // this.mobile,
    // this.email,
    this.address,
    // this.memberType,
    // this.roleCode,
    this.members,
  });

  String? id;
  String? name;
  // String ?mobile;
  // String ?email;
  String? address;
  // int ?memberType;
  // String ?roleCode;
  List<MembersVT>? members;

  factory FamilyVt.fromJson(Map<String, dynamic> json) => FamilyVt(
        id: json["id"],
        name: json["name"],
        // mobile: json["mobile"],
        // email: json["email"],
        address: json["address"],
        // memberType: json["member_type"],
        // roleCode: json["role_code"],
        members: json["members"] == null
            ? null
            : List<MembersVT>.from(
                json["members"].map((x) => MembersVT.fromJson(x))),
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "name": name,
        // "mobile": mobile,
        // "email": email,
        "address": address,
        // "member_type": memberType,
        // "role_code": roleCode,
        "members": members == null
            ? null
            : List<dynamic>.from(members!.map((x) => x.toJson())),
      };

  factory FamilyVt.fromMap(Map<String, dynamic> json) => FamilyVt(
        id: json["id"],
        name: json["name"],
        // mobile: json["mobile"],
        // email: json["email"],
        address: json["address"],
        // memberType: json["member_type"],
        // roleCode: json["role_code"],
        members: json["members"] == null
            ? null
            : List<MembersVT>.from(
                json["members"].map((x) => MembersVT.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "id": id,
        "name": name,
        // "mobile": mobile,
        // "email": email,
        "address": address,
        // "member_type": memberType,
        //"role_code": roleCode,
        "members": members == null
            ? null
            : List<dynamic>.from(members!.map((x) => x.toMap())),
      };
}

///////////////////////////////////////////////

class MembersVT {
  MembersVT({
    this.id,
    this.name,
    // this.mobile,
    // this.email,
    this.address,
    // this.memberType,
    // this.roleCode,
  });

  String? id;
  String? name;
  // String ?mobile;
  // String ?email;
  String? address;
  // int ?memberType;
  // String ?roleCode;

  factory MembersVT.fromJson(Map<String, dynamic> json) => MembersVT(
        id: json["id"],
        name: json["name"],
        // mobile: json["mobile"],
        // email: json["email"],
        address: json["address"],
        // memberType: json["member_type"],
        // roleCode: json["role_code"],
      );

  Map<String, dynamic> toJson() => {
        "id": id,
        "name": name,
        // "mobile": mobile,
        // "email": email,
        "address": address,
        // "member_type": memberType,
        // "role_code": roleCode,
      };

  factory MembersVT.fromMap(Map<String, dynamic> json) => MembersVT(
        id: json["id"],
        name: json["name"],
        // mobile: json["mobile"],
        // email: json["email"],
        address: json["address"],
        // memberType: json["member_type"],
        // roleCode: json["role_code"],
      );

  Map<String, dynamic> toMap() => {
        "id": id,
        "name": name,
        // "mobile": mobile,
        // "email": email,
        "address": address,
        // "member_type": memberType,
        // "role_code": roleCode,
      };
}

