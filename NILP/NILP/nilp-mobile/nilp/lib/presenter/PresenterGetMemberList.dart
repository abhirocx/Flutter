import 'package:flutter/cupertino.dart';
import 'package:nilp/model/response/ResponseLearnerList.dart';
import 'package:nilp/model/response/ResponseSearchMember.dart';
import '../constants/Strings.dart';
import '../model/response/ResponseErrorJ.dart';
import '../model/response/ResponseRefreshToken.dart';
import '../networking/ApiBaseHelper.dart';

abstract class ResponseContract {
  void onResponseSuccess(ResponseSearchMember responseDto);
  // void onResponseLearnerList(ResponseLearnerList responseDto);
  void onError(String errorTxt, String code);

  void onTokenExpire();

  void onResponseRefreshSuccess(ResponseRefreshToken responseDto) {}

  void onResponseLSuccess(ResponseSearchMember responseDto) {}
}

class PresenterGetMemberList {
  ResponseContract _view;

  PresenterGetMemberList(this._view);

  ApiBaseHelper _helper = ApiBaseHelper();

  Future<ResponseSearchMember?> getMembeDetails(
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
        ResponseSearchMember responseDto =
            ResponseSearchMember.fromMap(response);

        _view.onResponseSuccess(responseDto);
        return responseDto;
      } else {
        ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
        if (responseErrorJ.errors[0].extensions.code == TOKEN_EXPIRED)
          _view.onTokenExpire();
        else
        _view.onError(
            responseErrorJ.errors[0].message, responseErrorJ.errors[0].message);
      }
    } on Exception catch (_) {
      _view.onError("Something went wrong", "");
    }
  }

  Future<ResponseSearchMember?> getLearnersList(
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
        ResponseSearchMember responseDto =
            ResponseSearchMember.fromMap(response);
        _view.onResponseLSuccess(responseDto);
        return responseDto;
      } else {
        ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
        if (responseErrorJ.errors[0].extensions.code == TOKEN_EXPIRED)
          _view.onTokenExpire();
        else
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
        _view.onResponseRefreshSuccess(responseDto);
        return responseDto;
      } else {
        ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
        if (responseErrorJ.errors[0].extensions.code == TOKEN_EXPIRED) {
          _view.onTokenExpire();
        }
      }
    } on Exception catch (_) {
      _view.onError("Something went wrong", "");
    }
  }



  // Future<ResponseSearchMember?> getLearnerList(
  //     Map data,
  //     String signature,
  //     String timestamp,
  //     String url,
  //     BuildContext context,
  //     String acesstoken) async {
  //   try {
  //     final Map<String, dynamic> response = await _helper.postwithToken(
  //         url, _helper.headerwithToken(timestamp, signature, acesstoken), data);
  //
  //     if (!response.containsKey("errors")) {
  //       ResponseLearnerList responseDto =
  //       ResponseSearchMember.fromJson(response);
  //
  //       _view.ResponseSearchMember(responseDto);
  //       return responseDto;
  //     } else {
  //       ResponseErrorJ responseErrorJ = ResponseErrorJ.fromJson(response);
  //       if (responseErrorJ.errors[0].extensions.code == TOKEN_EXPIRED)
  //         _view.onTokenExpire();
  //       else
  //         _view.onError(
  //             responseErrorJ.errors[0].message, responseErrorJ.errors[0].message);
  //     }
  //   } on Exception catch (_) {
  //     _view.onError("Something went wrong", "");
  //   }
  // }

}
