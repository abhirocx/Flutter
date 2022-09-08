// To parse this JSON data, do
//
//     final responseDropdown = responseDropdownFromJson(jsonString);

import 'dart:convert';

ResponseDropdown responseDropdownFromJson(String str) => ResponseDropdown.fromJson(json.decode(str));

String responseDropdownToJson(ResponseDropdown data) => json.encode(data.toJson());

class ResponseDropdown {
  ResponseDropdown({
    this.data,
  });

  Data ?data;

  factory ResponseDropdown.fromJson(Map<String, dynamic> json) => ResponseDropdown(
    data: Data.fromJson(json["data"]),
  );

  Map<String, dynamic> toJson() => {
    "data": data!.toJson(),
  };
}

class Data {
  Data({
    this.profession,
    this.relationship,
    this.proofType,
    this.socialCategory,
    this.familyHeadRole,
    this.vtTypes,
    this.qualifications,
    this.designations,
  });

  List<Profession> ?profession;
  List<Relationship> ?relationship;
  List<ProofType> ?proofType;
  List<SocialCategory> ?socialCategory;
  List<FamilyHeadRole> ?familyHeadRole;
  List<VtTypes> ?vtTypes;
  List<Qualification> ?qualifications;
  List<Designation> ?designations;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
    profession: List<Profession>.from(json["profession"].map((x) => Profession.fromJson(x))),
    relationship: List<Relationship>.from(json["relationship"].map((x) => Relationship.fromJson(x))),
    proofType: List<ProofType>.from(json["proof_type"].map((x) => ProofType.fromJson(x))),
    socialCategory: List<SocialCategory>.from(json["social_category"].map((x) => SocialCategory.fromJson(x))),
    familyHeadRole: List<FamilyHeadRole>.from(json["family_head_role"].map((x) => FamilyHeadRole.fromJson(x))),
    vtTypes: List<VtTypes>.from(json["vt_types"].map((x) => VtTypes.fromJson(x))),
    qualifications: List<Qualification>.from(json["qualifications"].map((x) => Qualification.fromJson(x))),
    designations: List<Designation>.from(json["designations"].map((x) => Designation.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "profession": List<dynamic>.from(profession!.map((x) => x.toJson())),
    "relationship": List<dynamic>.from(relationship!.map((x) => x.toJson())),
    "proof_type": List<dynamic>.from(proofType!.map((x) => x.toJson())),
    "social_category": List<dynamic>.from(socialCategory!.map((x) => x.toJson())),
    "family_head_role": List<dynamic>.from(familyHeadRole!.map((x) => x.toJson())),
    "vt_types": List<dynamic>.from(vtTypes!.map((x) => x.toJson())),
    "qualifications": List<dynamic>.from(qualifications!.map((x) => x.toJson())),
    "designations": List<dynamic>.from(designations!.map((x) => x.toJson())),
  };
}


class Profession {
  Profession({
    this.id,
    this.professionName,
    this.en,
    this.hin,
    this.name,
  });

  int ?id;
  String ?professionName;
  String ?en;
  String ?hin;
  String ?name;

  factory Profession.fromJson(Map<String, dynamic> json) => Profession(
    id: json["id"],
    professionName: json["profession"] == null ? null : json["profession"],
    en: json["en"],
    hin: json["hin"],
    name: json["name"] == null ? null : json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "profession": professionName == null ? null : professionName,
    "en": en,
    "hin": hin,
    "name": name == null ? null : name,
  };
}


class Relationship {
  Relationship({
    this.id,
    this.relationship,
    this.en,
    this.hin,
    this.name,
    this.gender,
  });

  int ?id;
  String ?relationship;
  String ?en;
  String ?hin;
  String ?name;
  String ?gender;

  factory Relationship.fromJson(Map<String, dynamic> json) => Relationship(
    id: json["id"],
    relationship: json["relationship"] == null ? null : json["relationship"],
    en: json["en"],
    hin: json["hin"],
    name: json["name"] == null ? null : json["name"],
    gender: json["gender"] == null ? null : json["gender"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "relationship": relationship == null ? null : relationship,
    "en": en,
    "hin": hin,
    "name": name == null ? null : name,
    "gender": gender == null ? null : gender,
  };
}

class ProofType {
  ProofType({
    this.id,
    this.proofType,
    this.en,
    this.hin,
    this.name,
  });

  int ?id;
  String ?proofType;
  String ?en;
  String ?hin;
  String ?name;

  factory ProofType.fromJson(Map<String, dynamic> json) => ProofType(
    id: json["id"],
    proofType: json["proof_type"] == null ? null : json["proof_type"],
    en: json["en"],
    hin: json["hin"],
    name: json["name"] == null ? null : json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "proof_type": proofType == null ? null : proofType,
    "en": en,
    "hin": hin,
    "name": name == null ? null : name,
  };
}

class SocialCategory {
  SocialCategory({
    this.id,
    this.socialCategory,
    this.en,
    this.hin,
    this.name,
  });

  int ?id;
  String ?socialCategory;
  String ?en;
  String ?hin;
  String ?name;

  factory SocialCategory.fromJson(Map<String, dynamic> json) => SocialCategory(
    id: json["id"],
    socialCategory: json["social_category"] == null ? null : json["social_category"],
    en: json["en"],
    hin: json["hin"],
    name: json["name"] == null ? null : json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "social_category": socialCategory == null ? null : socialCategory,
    "en": en,
    "hin": hin,
    "name": name == null ? null : name,
  };
}

class VtTypes {
  VtTypes({
    this.id,
    this.vtTypes,
    this.en,
    this.hin,
    this.name,
  });

  int ?id;
  String ?vtTypes;
  String ?en;
  String ?hin;
  String ?name;

  factory VtTypes.fromJson(Map<String, dynamic> json) => VtTypes(
    id: json["id"],
    vtTypes: json["vt_types"] == null ? null : json["vt_types"],
    en: json["en"],
    hin: json["hin"],
    name: json["name"] == null ? null : json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "vt_types": vtTypes == null ? null : vtTypes,
    "en": en,
    "hin": hin,
    "name": name == null ? null : name,
  };
}

class Qualification {
  Qualification({
    this.id,
    this.qualifications,
    this.en,
    this.hin,
    this.name,
  });

  int ?id;
  String ?qualifications;
  String ?en;
  String ?hin;
  String ?name;

  factory Qualification.fromJson(Map<String, dynamic> json) => Qualification(
    id: json["id"],
    qualifications: json["qualifications"] == null ? null : json["qualifications"],
    en: json["en"],
    hin: json["hin"],
    name: json["name"] == null ? null : json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "qualifications": qualifications == null ? null : qualifications,
    "en": en,
    "hin": hin,
    "name": name == null ? null : name,
  };
}


class FamilyHeadRole {
  FamilyHeadRole({
    this.id,
    this.familyHeadRole,
    this.en,
    this.hin,
    this.name,
  });

  String ?id;
  String ?familyHeadRole;
  String ?en;
  String ?hin;
  String ?name;

  factory FamilyHeadRole.fromJson(Map<String, dynamic> json) => FamilyHeadRole(
    id: json["id"],
    familyHeadRole: json["family_head_role"] == null ? null : json["family_head_role"],
    en: json["en"],
    hin: json["hin"],
    name: json["name"] == null ? null : json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "family_head_role": familyHeadRole == null ? null : familyHeadRole,
    "en": en,
    "hin": hin,
    "name": name == null ? null : name,
  };
}


class Designation {
  Designation({
    this.id,
    this.designations,
    this.en,
    this.hin,
    this.name,
  });

  String ?id;
  String ?designations;
  String ?en;
  String ?hin;
  String ?name;

  factory Designation.fromJson(Map<String, dynamic> json) => Designation(
    id: json["id"],
    designations: json["designations"] == null ? null : json["designations"],
    en: json["en"],
    hin: json["hin"],
    name: json["name"] == null ? null : json["name"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "designations": designations == null ? null : designations,
    "en": en,
    "hin": hin,
    "name": name == null ? null : name,
  };
}

