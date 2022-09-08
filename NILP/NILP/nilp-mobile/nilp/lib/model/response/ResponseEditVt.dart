import 'dart:convert';

ResponseEditVt responseEditVtFromMap(String str) =>
    ResponseEditVt.fromMap(json.decode(str));

String responseEditVtToMap(ResponseEditVt data) => json.encode(data.toMap());

class ResponseEditVt {
  ResponseEditVt({
    this.data,
  });

  Data? data;

  factory ResponseEditVt.fromMap(Map<String, dynamic> json) => ResponseEditVt(
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
    this.otherVt,
  });

  List<FamilyVt>? familyVt;
  List<NeighbourVt>? neighbourVt;
  List<OtherVt>? otherVt;

  factory Data.fromMap(Map<String, dynamic> json) => Data(
        familyVt: List<FamilyVt>.from(
            json["family_vt"].map((x) => FamilyVt.fromMap(x))),
        neighbourVt: List<NeighbourVt>.from(
            json["neighbour_vt"].map((x) => NeighbourVt.fromMap(x))),
    otherVt: List<OtherVt>.from(
        json["others"].map((x) => OtherVt.fromMap(x))),
       // others: List<dynamic>.from(json["others"].map((x) => x)),
      );

  Map<String, dynamic> toMap() => {
        "family_vt": List<dynamic>.from(familyVt!.map((x) => x.toMap())),
        "neighbour_vt": List<dynamic>.from(neighbourVt!.map((x) => x.toMap())),
        "others": List<dynamic>.from(otherVt!.map((x) => x)),
      };
}

class FamilyVt {
  FamilyVt({
    this.id,
    this.fullname,
    this.name,
    this.address,
    this.selected,
    this.members,
  });

  String? id;
  String? fullname;
  String? name;
  String? address;
  bool? selected;
  List<FamilyVt>? members;

  factory FamilyVt.fromMap(Map<String, dynamic> json) => FamilyVt(
        id: json["id"],
        fullname: json["fullname"],
        name: json["name"],
        address: json["address"],
        selected: json["selected"],
        members: json["members"] == null
            ? null
            : List<FamilyVt>.from(
                json["members"].map((x) => FamilyVt.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "id": id,
        "fullname": fullname,
        "name": name,
        "address": address,
        "selected": selected,
        "members": members == null
            ? null
            : List<dynamic>.from(members!.map((x) => x.toMap())),
      };
}

class NeighbourVt {
  NeighbourVt({
    this.id,
    this.fullname,
    this.name,
    this.address,
    this.selected,
    this.members,
  });

  String? id;
  String? fullname;
  String? name;
  String? address;
  bool? selected;
  List<NeighbourVt>? members;

  factory NeighbourVt.fromMap(Map<String, dynamic> json) => NeighbourVt(
        id: json["id"],
        fullname: json["fullname"],
        name: json["name"],
        address: json["address"],
        selected: json["selected"],
        members: json["members"] == null
            ? null
            : List<NeighbourVt>.from(
                json["members"].map((x) => NeighbourVt.fromMap(x))),
      );

  Map<String, dynamic> toMap() => {
        "id": id,
        "fullname": fullname,
        "name": name,
        "address": address,
        "selected": selected,
        "members": members == null
            ? null
            : List<dynamic>.from(members!.map((x) => x.toMap())),
      };
}

class OtherVt {
  OtherVt({
    this.id,
    this.fullname,
    this.name,
    this.address,
    this.selected,
    this.members,
  });

  String? id;
  String? fullname;
  String? name;
  String? address;
  bool? selected;
  List<OtherVt>? members;

  factory OtherVt.fromMap(Map<String, dynamic> json) => OtherVt(
    id: json["id"],
    fullname: json["fullname"],
    name: json["name"],
    address: json["address"],
    selected: json["selected"],
    members: json["members"] == null
        ? null
        : List<OtherVt>.from(
        json["members"].map((x) => OtherVt.fromMap(x))),
  );

  Map<String, dynamic> toMap() => {
    "id": id,
    "fullname": fullname,
    "name": name,
    "address": address,
    "selected": selected,
    "members": members == null
        ? null
        : List<dynamic>.from(members!.map((x) => x.toMap())),
  };
}
enum Gender { M, F, T }

final genderValues = EnumValues({"f": Gender.F, "m": Gender.M, "t": Gender.T});

enum RoleCode { VT, LEARNER }

final roleCodeValues =
    EnumValues({"Learner": RoleCode.LEARNER, "VT": RoleCode.VT});

class EnumValues<T> {
  Map<String, T> map;
  Map<T, String>? reverseMap;

  EnumValues(this.map);

  Map<T, String>? get reverse {
    if (reverseMap == null) {
      reverseMap = map.map((k, v) => new MapEntry(v, k));
    }
    return reverseMap;
  }
}
