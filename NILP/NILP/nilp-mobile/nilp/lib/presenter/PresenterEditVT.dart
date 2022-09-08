import 'package:flutter/cupertino.dart';
import 'package:nilp/model/response/ResponseTagging.dart';
import '../constants/Strings.dart';
import '../model/response/ResponseEditVTLearner.dart';
import '../model/response/ResponseEditVt.dart';
import '../model/response/ResponseErrorJ.dart';
import '../model/response/ResponseRefreshToken.dart';
import '../networking/ApiBaseHelper.dart';

abstract class ResponseContract {
  void onResponseSuccess(ResponseEditVt responseDto);
  void onResponseSuccesses(ResponseEditVtLearner responseDto);

  void onTokenExpire();

  void onResponseRefreshSuccess(ResponseRefreshToken responseDto) {}

  void onResponseTagSuccess(ResponseTagging responseDto) {}
  void onError(String errorTxt, String code);
}

class PresenterEditTag {
  ResponseContract _view;

  PresenterEditTag(this._view);

  ApiBaseHelper _helper = ApiBaseHelper();

  Future<ResponseEditVt?> getEditVtList(
      Map data,
      String signature,
      String timestamp,
      String url,
      BuildContext context,
      String acesstoken) async {
    try {
      final  Map<String, dynamic> response = await _helper.postwithToken(
          url, _helper.headerwithToken(timestamp, signature, acesstoken), data);

      if (!response.containsKey("error")) {
        ResponseEditVt responseDto = ResponseEditVt.fromMap(response);

        print("response: $response");
        _view.onResponseSuccess(responseDto);
        return responseDto;
      } else {
        ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
        // if (responseErrorJ.errors[0].code == TOKEN_EXPIRED)
        //   _view.onTokenExpire();
        // else
        _view.onError(
            responseErrorJ.errors[0].message, responseErrorJ.errors[0].message);
      }
    } on Exception catch (_) {
      _view.onError("Something went wrong", "");
    }
  }

  Future<ResponseTagging?> vtTagging(
      Map data,
      String signature,
      String timestamp,
      String url,
      BuildContext context,
      String acesstoken) async {
    try {
      final response = await _helper.postwithToken(
          url, _helper.headerwithToken(timestamp, signature, acesstoken), data);

      if (!response.toString().contains("errors")) {
        ResponseTagging responseDto = ResponseTagging.fromMap(response);
        _view.onResponseTagSuccess(responseDto);
        return responseDto;
      } else {
        ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
        // if (responseErrorJ.errors[0].code == TOKEN_EXPIRED)
        //   _view.onTokenExpire();
        // else
        _view.onError(
            responseErrorJ.errors[0].message, responseErrorJ.errors[0].message);
      }
    } on Exception catch (_) {
      _view.onError("Something went wrong", "");
    }
  }


  Future<ResponseEditVtLearner?> getLearnersLists(
      Map data,
      String signature,
      String timestamp,
      String url,
      BuildContext context,
      String acesstoken) async {
    try {
      final Map<String, dynamic> response = await _helper.postwithToken(
          url, _helper.headerwithToken(timestamp, signature, acesstoken), data);

      if (!response.containsKey("errors")) {
        ResponseEditVtLearner responseDto =
        ResponseEditVtLearner.fromMap(response);

        print("response: $response");
        _view.onResponseSuccesses(responseDto);
        return responseDto;
      } else {
        ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
        // if (responseErrorJ.errors[0].code == TOKEN_EXPIRED)
        //   _view.onTokenExpire();
        // else
        _view.onError(
            responseErrorJ.errors[0].message, responseErrorJ.errors[0].message);
      }
    } on Exception catch (_) {
      _view.onError("Something went wrong", "");
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
        //print("response: $response");
        _view.onResponseRefreshSuccess(responseDto);
        return responseDto;
      } else {
        ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
        if (responseErrorJ.errors[0].extensions.code == TOKEN_EXPIRED)
          _view.onTokenExpire();
        // _view.onTokenExpire();
      }
    } on Exception catch (_) {
      _view.onError("Something went wrong", "");
    }
  }
}
