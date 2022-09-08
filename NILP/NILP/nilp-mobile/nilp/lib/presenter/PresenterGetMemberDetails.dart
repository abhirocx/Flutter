import 'package:nilp/model/response/ResponseMemberDetails.dart';

import '../constants/Strings.dart';
import '../model/response/ResponseErrorJ.dart';
import '../model/response/ResponseRefreshToken.dart';
import '../networking/ApiBaseHelper.dart';

abstract class ResponseContract {
  void onError(String errorTxt, String code);

  void onTokenExpire();

  void onResponseRefreshSuccess(ResponseRefreshToken responseDto) {}

  void onResponseMemberDetails(ResponseMemberDetails responseDto) {}
}

class PresenterGetMemberDetails {

  ResponseContract _view;

  PresenterGetMemberDetails(this._view);

  ApiBaseHelper _helper = ApiBaseHelper();

  Future<ResponseRefreshToken?> refreshToken(
      Map data, String signature, String timestamp, String url) async {
    try {
      final response =
      await _helper.post(url, _helper.header(timestamp, signature), data);

      if (!response.toString().contains("errors")) {
        ResponseRefreshToken responseDto =
        ResponseRefreshToken.fromJson(response);
        //print("response: $response");
        _view.onResponseRefreshSuccess(responseDto);
        return responseDto;
      } else {
        ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
        if (responseErrorJ.errors[0].extensions.code == TOKEN_EXPIRED)
          _view.onTokenExpire();
      }
    } on Exception catch (_) {
      _view.onError("Something went wrong", "");
    }
  }

}
