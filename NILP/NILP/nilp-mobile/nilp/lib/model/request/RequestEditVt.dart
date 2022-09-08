import 'dart:convert';

RequestEditVt requestEditVtFromMap(String str) => RequestEditVt.fromMap(json.decode(str));

String requestEditVtToMap(RequestEditVt data) => json.encode(data.toMap());

class RequestEditVt {
  RequestEditVt({
    this.proofDetail,
  });

  String ?proofDetail;

  factory RequestEditVt.fromMap(Map<String, dynamic> json) => RequestEditVt(
    proofDetail: json["proof_detail"],
  );

  Map<String, dynamic> toMap() => {
    "proof_detail": proofDetail,
  };
}
