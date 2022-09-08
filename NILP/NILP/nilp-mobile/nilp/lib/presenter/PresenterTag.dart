import 'package:flutter/cupertino.dart';
import 'package:nilp/model/response/ResponseTagging.dart';
import 'package:nilp/model/response/ResponseVTList.dart';
import '../constants/Strings.dart';
import '../model/response/ResponseErrorJ.dart';
import '../model/response/ResponseRefreshToken.dart';
import '../networking/ApiBaseHelper.dart';

abstract class ResponseContract {
  void onResponseSuccess(ResponseVtList responseDto);
  void onError(String errorTxt, String code);

  void onTokenExpire();

  void onResponseRefreshSuccess(ResponseRefreshToken responseDto) {}

  void onResponseTagSuccess(ResponseTagging responseDto) {}
}

class PresenterTag {
  ResponseContract _view;

  PresenterTag(this._view);

  ApiBaseHelper _helper = ApiBaseHelper();

  Future<ResponseVtList?> getMembeDetails(
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
        ResponseVtList responseDto = ResponseVtList.fromMap(response);

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

        print("response: $response");
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
