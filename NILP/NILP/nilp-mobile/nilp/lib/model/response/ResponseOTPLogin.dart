import 'package:meta/meta.dart';
import 'dart:convert';

ResponseOtpLogin responseOtpLoginFromJson(String str) => ResponseOtpLogin.fromJson(json.decode(str));

String responseOtpLoginToJson(ResponseOtpLogin data) => json.encode(data.toJson());

class ResponseOtpLogin {
  ResponseOtpLogin({
   required this.data,
  });

  final Data data;

  factory ResponseOtpLogin.fromJson(Map<String, dynamic> json) => ResponseOtpLogin(
    data: Data.fromJson(json["data"]),
  );

  Map<String, dynamic> toJson() => {
    "data": data.toJson(),
  };
}

class Data {
  Data({
   required this.accessToken,
   required this.refreshToken,
   required this.expires,
   required this.user,
  });

  final String accessToken;
  final String refreshToken;
  final int expires;
  final User user;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
    accessToken: json["access_token"],
    refreshToken: json["refresh_token"],
    expires: json["expires"],
    user: User.fromJson(json["user"]),
  );

  Map<String, dynamic> toJson() => {
    "access_token": accessToken,
    "refresh_token": refreshToken,
    "expires": expires,
    "user": user.toJson(),
  };
}

class User {
  User({
   required this.id,
   required this.fullName,
   required this.email,
   required this.mobile,
   required this.designation,
   required this.designationId,
   required this.blockId,
   required this.blockCode,
   required this.blockName,
   required this.districtId,
   required this.districtCode,
   required this.districtName,
   required this.stateId,
   required this.stateCode,
   required this.stateName,
   required this.clusterId,
   required this.clusterName,
   required this.villageWardId,
   required this.udiseVillageWardCode,
   required this.villageWardName,
   required this.schoolId,
   required this.schoolName,
   required this.schoolAddress,
   required this.schoolPincode,
   required this.schoolUdiseCode,
  });

  final String id;
  final String fullName;
  final String email;
  final String mobile;
  final String designation;
  final String designationId;
  final int blockId;
  final String blockCode;
  final String blockName;
  final int districtId;
  final String districtCode;
  final String districtName;
  final int stateId;
  final String stateCode;
  final String stateName;
  final int clusterId;
  final String clusterName;
  final int villageWardId;
  final String udiseVillageWardCode;
  final String villageWardName;
  final int schoolId;
  final String schoolName;
  final String schoolAddress;
  final int schoolPincode;
  final String schoolUdiseCode;

  factory User.fromJson(Map<String, dynamic> json) => User(
    id: json["id"],
    fullName: json["full_name"],
    email: json["email"],
    mobile: json["mobile"],
    designation: json["designation"],
    designationId: json["designation_id"],
    blockId: json["block_id"],
    blockCode: json["block_code"],
    blockName: json["block_name"],
    districtId: json["district_id"],
    districtCode: json["district_code"],
    districtName: json["district_name"],
    stateId: json["state_id"],
    stateCode: json["state_code"],
    stateName: json["state_name"],
    clusterId: json["cluster_id"],
    clusterName: json["cluster_name"],
    villageWardId: json["village_ward_id"],
    udiseVillageWardCode: json["udise_village_ward_code"],
    villageWardName: json["village_ward_name"],
    schoolId: json["school_id"],
    schoolName: json["school_name"],
    schoolAddress: json["school_address"],
    schoolPincode: json["school_pincode"],
    schoolUdiseCode: json["school_udise_code"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "full_name": fullName,
    "email": email,
    "mobile": mobile,
    "designation": designation,
    "designation_id": designationId,
    "block_id": blockId,
    "block_code": blockCode,
    "block_name": blockName,
    "district_id": districtId,
    "district_code": districtCode,
    "district_name": districtName,
    "state_id": stateId,
    "state_code": stateCode,
    "state_name": stateName,
    "cluster_id": clusterId,
    "cluster_name": clusterName,
    "village_ward_id": villageWardId,
    "udise_village_ward_code": udiseVillageWardCode,
    "village_ward_name": villageWardName,
    "school_id": schoolId,
    "school_name": schoolName,
    "school_address": schoolAddress,
    "school_pincode": schoolPincode,
    "school_udise_code": schoolUdiseCode,
  };
}

class Role {
  Role({
   required this.id,
   required this.role,
   required this.state,
   required this.district,
   required this.block,
   required this.cluster,
   required this.school,
   required this.villageWard,
   required this.roleCode,
   required this.sort,
  });

  final String id;
  final String role;
  final int state;
  final int district;
  final String block;
  final String cluster;
  final int school;
  final dynamic villageWard;
  final String roleCode;
  final dynamic sort;

  factory Role.fromJson(Map<String, dynamic> json) => Role(
    id: json["id"],
    role: json["role"],
    state: json["state"],
    district: json["district"],
    block: json["block"],
    cluster: json["cluster"],
    school: json["school"],
    villageWard: json["village_ward"],
    roleCode: json["role_code"],
    sort: json["sort"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "role": role,
    "state": state,
    "district": district,
    "block": block,
    "cluster": cluster,
    "school": school,
    "village_ward": villageWard,
    "role_code": roleCode,
    "sort": sort,
  };
}
