import 'dart:convert';

import 'package:encrypt/encrypt.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/svg.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/model/response/ResponseDashborad.dart';
import 'package:nilp/model/response/ResponseDropdown.dart';
import 'package:nilp/model/response/ResponseRefreshToken.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/request/RequestGetOTP.dart';
import '../../model/response/ResponseGetOTP.dart';
import '../../model/response/ResponseOTPLogin.dart';
import '../../model/response/ResponseResendOtp.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../presenter/PresenterGetOtp.dart';
import '../../utility/Util.dart';
import '../../utility/testEncryption.dart';
import '../direct_registration/DirectRegistration.dart';
import '../home_screen/home.dart';
import '../otp/otp.dart';

class Login extends StatefulWidget {
  const Login({key}) : super(key: key);

  @override
  _LoginState createState() => _LoginState();
}

bool network = false;

class _LoginState extends State<Login> implements ResponseContract {
  final GlobalKey<FormState> _key = GlobalKey();
  late String mobile;
  late PresenterGetOtp presenterGetOtp;
  late String phone;

  @override
  void initState() {
    super.initState();
    presenterGetOtp = PresenterGetOtp(this);
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: _onBackPressed,
      child: Scaffold(
        backgroundColor: kloginBackgrondColor,
        body: GestureDetector(
          onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
          child: Container(
            color: kBg,
            child: SafeArea(
              child: SingleChildScrollView(
                child: Column(
                  children: [
                    // SizedBox(
                    //   height: MediaQuery.of(context).size.height / 1.8,
                    //   child: Center(
                    //     child: SvgPicture.asset(
                    //       "images/login_img.svg",
                    //       height: 249,
                    //       width: 253,
                    //     ),
                    //   ),
                    // ),
                    SizedBox(
                      height: MediaQuery.of(context).size.height / 7.8,
                      child: Center(
                        child:Image.asset('images/splash.png',
                          height: 249,
                          width: 253,
                        ),
                      ),
                    ),
                    SizedBox(
                      height: MediaQuery.of(context).size.height / 2.3,
                      child: Center(
                        child:SvgPicture.asset(
                                logo(),
                              )
                      ),
                    ),
                    Align(
                      alignment: Alignment.bottomCenter,
                      child: Row(children: [
                        Expanded(
                            flex: 2,
                            child: Card(
                              color: kFillaSurveyColor,
                              margin: EdgeInsets.zero,
                              shape: const RoundedRectangleBorder(
                                borderRadius: BorderRadius.only(
                                    topLeft: Radius.circular(35),
                                    topRight: Radius.circular(35)),
                              ),
                              child: Padding(
                                  padding: const EdgeInsets.all(15),
                                  child: Column(
                                      mainAxisAlignment:
                                          MainAxisAlignment.start,
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
                                      children: [
                                        Padding(
                                          padding: const EdgeInsets.all(16.0),
                                          child: Text(
                                            Languages.of(context)!.SIGN_IN,
                                            style: kHeadingTextStyle,
                                          ),
                                        ),
                                        Form(
                                          key: _key,
                                          child: formUi(),
                                        ),
                                        Padding(
                                          padding: const EdgeInsets.only(
                                              left: 14, top: 12, right: 14),
                                          child: Row(
                                            children: [
                                              Expanded(
                                                child: GestureDetector(
                                                  onTap: () {
                                                    _sendToServer();
                                                  },
                                                  child: Container(
                                                    height: 60,
                                                    decoration: BoxDecoration(
                                                        color:
                                                            kSendaSurveyColor,
                                                        borderRadius:
                                                            BorderRadius
                                                                .circular(40)),
                                                    child: Center(
                                                      child: Padding(
                                                        padding:
                                                            const EdgeInsets
                                                                .all(4.0),
                                                        child: Text(
                                                          Languages.of(context)!
                                                              .OTP,
                                                          textAlign:
                                                              TextAlign.center,
                                                          style: GoogleFonts
                                                              .poppins(
                                                            textStyle:
                                                                kButtonTextStyle,
                                                          ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              )
                                            ],
                                          ),
                                        ),
                                        const SizedBox(
                                          height: 20,
                                        ),
                                         Visibility(
                                           visible: false,
                                           child: Padding(
                                            padding: EdgeInsets.all(16.0),
                                            child:
                                            Center(
                                              child:GestureDetector(
                                                onTap: (){
                                                  Navigator.push(context,
                                                      MaterialPageRoute(builder: (context) => DirectRegistration()));
                                                },
                                                child: Row(
                                                  mainAxisAlignment: MainAxisAlignment.center,
                                                  children: [
                                                    Icon(Icons.forward,color:kDrawerSheetTextColor,size: 20,),
                                                    Text(
                                                    "Direct Registration",
                                                    //Languages.of(context)!.SIGN_IN,
                                                    style: kDirectRegistrationStyle,
                                                    textAlign: TextAlign.center,
                                            ),
                                                  ],
                                                ),
                                              ),
                                        ),),
                                         ),
                                        const SizedBox(
                                          height: 120,
                                        )
                                      ])),
                            ))
                    //   ]),
                    // ),
                  ],
                ),
              ),]
            ),
          ),
        ),
      ))));
  }

  Widget formUi() {
    return Padding(
      padding: const EdgeInsets.all(16.0),
      child: TextFormField(
        keyboardType: TextInputType.number,
        autofocus: false,
        cursorColor: kBg,
        enableInteractiveSelection: true,
        maxLength: 10,
        style: const TextStyle(color: kBg),
        inputFormatters: <TextInputFormatter>[
          FilteringTextInputFormatter.allow(RegExp(r'[0-9]')),
        ],
        onSaved: (value) {
          mobile = value!;
        },
        onEditingComplete: _sendToServer,
        validator: (value) {
          String pattern = r'(^((\+91)?|91)?[6789][0-9]{9})';
          RegExp regExp = RegExp(pattern);
          if (value!.isEmpty) {
            return Languages.of(context)!.MOBILE_NUMBER_REQUIRED;
          } else if (!regExp.hasMatch(value)) {
            return Languages.of(context)!.ENTER_VALID_MOBILE;
          }
          return null;
        },
        decoration: InputDecoration(
          counterText: "",
          labelStyle: const TextStyle(color: kBg),
          labelText: Languages.of(context)!.ENTER_YOUR_MOBILE_NUMBER,
          focusedBorder: const UnderlineInputBorder(
            borderSide: BorderSide(color: kBg),
          ),
          enabledBorder: const UnderlineInputBorder(
            borderSide: BorderSide(color: kBg),
          ),
        ),
      ),
    );
  }

  _sendToServer() async {
    if (_key.currentState!.validate()) {
      _key.currentState!.save();
      phone = mobile;
      int timestamp = DateTime.now().millisecondsSinceEpoch;
      AppSignatureUtil appSignatureUtil = AppSignatureUtil();
      Encrypted encrypted = encrypt(mobile);
      RequestGetOtp request = RequestGetOtp(mobile: encrypted.base64);
      String url = BASE_URL + GENERATE_OTP;
      String signature = appSignatureUtil.generateSignature(
          url, "null", 0, timestamp.toString(), request);
      Util util = Util();
      bool isOnline = await util.hasInternet();
      if (isOnline) {
        getOTP(GENERATE_OTP, timestamp.toString(), signature, request);
      } else {
        Fluttertoast.showToast(
            msg: Languages.of(context)!.NO_INTERNET,
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIosWeb: 1,
            textColor: Colors.black,
            backgroundColor: Colors.white,
            fontSize: 16.0);
      }
    } else {
      setState(() {});
    }
  }

  getOTP(
      String url, String timestamp, String signature, RequestGetOtp request) {
    loader();
    return presenterGetOtp.getOTP(
        request.toMap(), signature, timestamp, url, context);
  }

  Future<dynamic>? loader() {
    isLoader = true;
    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => const Center(
        child: SizedBox(
          height: 50,
          width: 50,
          child: CircularProgressIndicator(
            valueColor: AlwaysStoppedAnimation<Color>(kDrawerSheetTextColor),
            strokeWidth: 7.0,
          ),
        ),
      ),
    );
  }

  @override
  void onError(String errorTxt, String code) {
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
    Fluttertoast.showToast(
       // msg:errorTxt.toString(),
         msg: utf8.decode(errorTxt.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
  }

  @override
  void onResponseSuccessFav() {
    // TODO: implement onResponseSuccessFav
  }

  @override
  Future<void> onResponseOTPSuccess(ResponseGetOtp responseDto) async {
    // Fluttertoast.showToast(
    //     msg: responseDto.data.otp.toString(),
    //     toastLength: Toast.LENGTH_LONG,
    //     gravity: ToastGravity.BOTTOM,
    //     timeInSecForIosWeb: 1,
    //     textColor: Colors.black,
    //     backgroundColor: Colors.white,
    //     fontSize: 16.0);
    final prefs = await SharedPreferences.getInstance();
    prefs.setString(TSNID, responseDto.data.tsnid);
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) {
          return Otp();
        },
      ),
    );
  }

  @override
  void onResponseOTPLogin(ResponseOtpLogin responseOtpLogin) {
    // TODO: implement onResponseOTPLogin
  }

  @override
  void onResponseResendOTPSuccess(ResponseResendOtp responseResendOtp) {
    // TODO: implement onResponseResendOTPSuccess
  }

  void getLoacale() async {
    // changeLanguage(context, "en");
  }

  @override
  void onResponseSuccess(responseDto) {
    // TODO: implement onResponseSuccess
  }

  @override
  void onResponseMasterData(ResponseDropdown responseDto) {
    // TODO: implement onResponseMasterData
  }

  Future<bool> _onBackPressed() async {
    Navigator.push(context,
        MaterialPageRoute(builder: (context) => Otlas()));
    // SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
    // // Navigator.pop(context);
    // SystemNavigator.pop();
    return false;
  }

  @override
  void onResponseDashboard(ResponseDashborad responseDto) {
    // TODO: implement onResponseDashboard
  }

  @override
  void onTokenExpire() {
    // TODO: implement onTokenExpire
  }

  @override
  void onResponseRefreshSuccess(ResponseRefreshToken responseDto) {
    // TODO: implement onResponseRefreshSuccess
  }

}
