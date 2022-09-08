import 'dart:convert';

ResponseGetRespondentType responseGetRespondentTypeFromJson(String str) =>
    ResponseGetRespondentType.fromJson(json.decode(str));

String responseGetRespondentTypeToJson(ResponseGetRespondentType data) =>
    json.encode(data.toJson());

class ResponseGetRespondentType {
  ResponseGetRespondentType({
    required this.data,
  });

  List<Datum_Respondent> data;

  factory ResponseGetRespondentType.fromJson(Map<String, dynamic> json) =>
      ResponseGetRespondentType(
        data: List<Datum_Respondent>.from(json["data"].map((x) => Datum_Respondent.fromJson(x))),
      );

  Map<String, dynamic> toJson() => {
        "data": List<dynamic>.from(data.map((x) => x.toJson())),
      };
}

class Datum_Respondent {
  Datum_Respondent({required this.respondentTypeId, required this.respondentType});

  String respondentType;
  int respondentTypeId;

  factory Datum_Respondent.fromJson(Map<String, dynamic> json) => Datum_Respondent(
        respondentTypeId: json["respondentTypeId"],
        respondentType: json["respondentType"],
      );

  Map<String, dynamic> toJson() => {
        "respondentTypeId": respondentTypeId,
        "respondentType": respondentType,
      };
}
