import 'package:flutter/src/widgets/framework.dart';
import 'package:nilp/model/response/ResponseEditProfile.dart';
import '../constants/Strings.dart';
import '../localization/language/languages.dart';
import '../model/response/ResponseEditProfileOtp.dart';
import '../model/response/ResponseErrorJ.dart';
import '../model/response/ResponseGetRespondentType.dart';
import '../model/response/ResponseRefreshToken.dart';
import '../networking/ApiBaseHelper.dart';

abstract class ResponseContract {
  void onResponseSuccess(ResponseGetRespondentType responseDto);

  void onError(String errorTxt, String code);

  void onTokenExpire();

  void onResponseRefreshSuccess(ResponseRefreshToken responseDto) {}

  void onResponseEditProfileOtp(ResponseEditProfileOtp responseDto) {}

  void onResponseEditProfile(ResponseEditProfile responseDto) {}
}

class PresenterEditProfile {
  ResponseContract _view;

  PresenterEditProfile(this._view);

  ApiBaseHelper _helper = ApiBaseHelper();

  Future<ResponseEditProfile?> editprofileGetOTP(
      Map data,
      String signature,
      String timestamp,
      String url,
      String acesstoken,
      BuildContext context) async {
    try {
      final Map<String, dynamic> response = await _helper.postwithToken(
          url, _helper.headerwithToken(timestamp, signature, acesstoken), data);
      if (response != null) {
        if (!response.containsKey("errors")) {
          ResponseEditProfile responseDto =
              ResponseEditProfile.fromJson(response);
          _view.onResponseEditProfile(responseDto);
          return responseDto;
        } else {
          ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
          if (identical(responseErrorJ.errors[0].message, TOKEN_EXPIRED)) {
             _view.onTokenExpire();
          } else
            _view.onError(responseErrorJ.errors[0].message,responseErrorJ.errors[0].extensions.code);
        }
      } else {
        _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG,"");
      }
    } on Exception catch (_) {
      _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG,"");
    }
  }

  Future<ResponseEditProfileOtp?> verifyOTPEditProfile(
      Map data,
      String signature,
      String timestamp,
      String url,
      String acesstoken,
      BuildContext context) async {
    try {
      final Map<String, dynamic> response = await _helper.post(
          url, _helper.headerwithToken(timestamp, signature, acesstoken), data);

      if (response != null) {
        if (!response.containsKey("errors")) {
          ResponseEditProfileOtp responseDto =
              ResponseEditProfileOtp.fromJson(response);
          _view.onResponseEditProfileOtp(responseDto);
          return responseDto;
        } else {
          ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
          if (identical(responseErrorJ.errors[0].extensions.code, TOKEN_EXPIRED)) {
             _view.onTokenExpire();
          } else
          _view.onError(responseErrorJ.errors[0].message,responseErrorJ.errors[0].extensions.code);
        }
      } else {
        _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG,"");
      }
    } on Exception catch (_) {
      _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG,"");
    }
  }

  Future<ResponseRefreshToken?> refreshToken(
      Map data, String signature, String timestamp, String url) async {
    try {
      final response =
      await _helper.post(url, _helper.header(timestamp, signature), data);

      if (!response.toString().contains("errors")) {
        ResponseRefreshToken responseDto =
        ResponseRefreshToken.fromJson(response);
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
