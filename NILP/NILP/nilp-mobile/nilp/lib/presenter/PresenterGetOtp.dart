import 'package:flutter/src/widgets/framework.dart';
import 'package:nilp/model/response/ResponseDashborad.dart';
import 'package:nilp/model/response/ResponseDropdown.dart';
import 'package:nilp/model/response/ResponseLearnerList.dart';

import '../constants/Strings.dart';
import '../localization/language/languages.dart';
import '../model/response/ResponseErrorJ.dart';
import '../model/response/ResponseGetOTP.dart';
import '../model/response/ResponseGetRespondentType.dart';
import '../model/response/ResponseOTPLogin.dart';
import '../model/response/ResponseRefreshToken.dart';
import '../model/response/ResponseResendOtp.dart';
import '../networking/ApiBaseHelper.dart';

abstract class ResponseContract {
  void onResponseSuccess(ResponseGetRespondentType responseDto);

  void onError(String errorTxt, String code);

  void onTokenExpire();

  void onResponseSuccessFav();

  void onResponseRefreshSuccess(ResponseRefreshToken responseDto) {}

  void onResponseOTPSuccess(ResponseGetOtp responseDto) {}

  void onResponseMasterData(ResponseDropdown responseDto) {}

  void onResponseDashboard(ResponseDashborad responseDto) {}

  void onResponseOTPLogin(ResponseOtpLogin responseOtpLogin);

  void onResponseResendOTPSuccess(ResponseResendOtp responseResendOtp);
}

class PresenterGetOtp {
  ResponseContract _view;

  PresenterGetOtp(this._view);

  ApiBaseHelper _helper = ApiBaseHelper();

  Future<ResponseDashborad?> getDashboradData(
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
          ResponseDashborad responseDto = ResponseDashborad.fromJson(response);
          _view.onResponseDashboard(responseDto);
          return responseDto;
        } else {
          ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
          if (identical(responseErrorJ.errors[0].message, TOKEN_EXPIRED)) {
            _view.onTokenExpire();
          } else
            _view.onError(responseErrorJ.errors[0].message,
                responseErrorJ.errors[0].extensions.code);
          _view.onTokenExpire();
        }
      } else {
        _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
      }
    } on Exception catch (_, e) {
      print(e);
      _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
    }
  }

  Future<ResponseDropdown?> getMasterData(
      Map data,
      String signature,
      String timestamp,
      String url,
      String acetoken,
      BuildContext context) async {
    try {
      final Map<String, dynamic> response =
          await _helper.post(url, _helper.header(timestamp, signature), data);
      if (response != null) {
        if (!response.containsKey("errors")) {
          ResponseDropdown responseDto = ResponseDropdown.fromJson(response);
          _view.onResponseMasterData(responseDto);
          return responseDto;
        } else {
          ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
          if (identical(responseErrorJ.errors[0].message, TOKEN_EXPIRED)) {
          } else
            _view.onError(responseErrorJ.errors[0].message,
                responseErrorJ.errors[0].extensions.code);
        }
      } else {
        _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
      }
    } on Exception catch (_) {
      _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
    }
  }

  Future<ResponseGetOtp?> getOTP(Map data, String signature, String timestamp,
      String url, BuildContext context) async {
    try {
      final Map<String, dynamic> response =
          await _helper.post(url, _helper.header(timestamp, signature), data);

      if (response != null) {
        if (!response!.containsKey("errors")) {
          ResponseGetOtp responseDto = ResponseGetOtp.fromJson(response);
          _view.onResponseOTPSuccess(responseDto);
          return responseDto;
        } else {
          ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
          if (identical(responseErrorJ.errors[0].message, TOKEN_EXPIRED)) {
            // _view.onTokenExpire();
            _view.onError(responseErrorJ.errors[0].message,
                responseErrorJ.errors[0].extensions.code);
          } else
            _view.onError(responseErrorJ.errors[0].message,
                responseErrorJ.errors[0].extensions.code);
        }
      } else {
        _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
      }
    } on Exception catch (_) {
      _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
    }
  }

  Future<ResponseOtpLogin?> verifyOTP(
      Map<dynamic, dynamic> data,
      String signature,
      String timestamp,
      String url,
      BuildContext context) async {
    try {
      final Map<String, dynamic> response =
          await _helper.post(url, _helper.header(timestamp, signature), data);

      if (response != null) {
        if (!response.containsKey("errors")) {
          ResponseOtpLogin responseDto = ResponseOtpLogin.fromJson(response);
          _view.onResponseOTPLogin(responseDto);
          return responseDto;
        } else {
          ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
          if (identical(
              responseErrorJ.errors[0].extensions.code, TOKEN_EXPIRED)) {
            _view.onTokenExpire();
          } else
            _view.onError(responseErrorJ.errors[0].message,
                responseErrorJ.errors[0].extensions.code);
        }
      } else {
        _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
      }
    } on Exception catch (_) {
      _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
    }
  }

  resendOTP(Map data, String signature, String timestamp, String url,
      BuildContext context) async {
    try {
      final response =
          await _helper.post(url, _helper.header(timestamp, signature), data);
      if (response != null) {
        if (!response.toString().contains("errors")) {
          ResponseResendOtp responseDto = ResponseResendOtp.fromJson(response);
          _view.onResponseResendOTPSuccess(responseDto);
        } else {
          ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
          if (identical(
              responseErrorJ.errors[0].extensions.code, TOKEN_EXPIRED)) {
            // _view.onTokenExpire();
            _view.onError(responseErrorJ.errors[0].message,
                responseErrorJ.errors[0].extensions.code);
          } else
            _view.onError(responseErrorJ.errors[0].message,
                responseErrorJ.errors[0].extensions.code);
        }
      } else {
        _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
      }
    } on Exception catch (_) {
      _view.onError(Languages.of(context)!.SOMETHING_WENT_WRONG, "");
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
