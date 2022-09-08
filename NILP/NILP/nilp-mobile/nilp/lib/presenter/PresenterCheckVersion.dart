

import 'package:nilp/model/response/ResponseVersionAPI.dart';

import '../model/response/ResponseErrorJ.dart';
import '../networking/ApiBaseHelper.dart';

abstract class ResponseContract {
  void onResponseSuccess(ResponseVersionApi responseDto);

  void onError(String errorTxt, String code);
}

class PresenterCheckVersion {
  ResponseContract _view;

  PresenterCheckVersion(this._view);

  ApiBaseHelper _helper = ApiBaseHelper();

  Future<ResponseVersionApi?> postCheckVersion(Map<dynamic, dynamic> data,
      String signature, String timestamp, String url) async {
    //, String acesstoken
    try {
      final response = await _helper.post_api_version(
          url, _helper.header(timestamp, signature), data);
      //, acesstoken
      if (response != null) {
        print("responsevalues: $response");
        if (!response.toString().contains("error")) {
          ResponseVersionApi responseDto =
          ResponseVersionApi.fromMap(response);
          print("responsevaluea: $response");
          _view.onResponseSuccess(responseDto);
          return responseDto;
        } else {
          ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
          /*if (responseErrorJ.errors[0].code == TOKEN_EXPIRED) {
            _view.onTokenExpire();
          } else*/
          _view.onError(
              responseErrorJ.errors[0].message, responseErrorJ.errors[0].message);
        }
      } else {
        _view.onError("Something went wrong", "");
      }
    } on Exception catch (_) {
      _view.onError("Something went wrong", "");
    }
  }

}
