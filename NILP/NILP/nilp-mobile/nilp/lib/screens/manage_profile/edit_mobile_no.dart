import 'dart:async';
import 'dart:convert';

import 'package:encrypt/encrypt.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/presenter/PresenterEditProfile.dart';
import 'package:nilp/screens/manage_record/manage_record.dart';
import 'package:pin_code_fields/pin_code_fields.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/request/RequestEditProfile.dart';
import '../../model/request/RequestEditProfileOTP.dart';
import '../../model/request/RequestRefreshToken.dart';
import '../../model/response/ResponseEditProfile.dart';
import '../../model/response/ResponseEditProfileOtp.dart';
import '../../model/response/ResponseGetRespondentType.dart';
import '../../model/response/ResponseRefreshToken.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../utility/Util.dart';
import '../../utility/testEncryption.dart';
import '../dashboard/dashboard.dart';
import '../login/login.dart';
import '../manage_record/local_manage_record.dart';
import 'manage_profile.dart';

class EditManageProfile extends StatefulWidget {
  EditManageProfile(var flag, mobile, email) {
    ui_status = flag;
    old_mobile = mobile;
    old_email = email;
  }

  @override
  _EditManageProfileState createState() => _EditManageProfileState();
}

var old_mobile, old_email, ui_status, pinotp;
String _otp = "";
int generate_otp_flag = 0;
int verify_otp_flag = 0;
int resend_otp_flag = 0;

class _EditManageProfileState extends State<EditManageProfile>
    implements ResponseContract {
  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
  }

  TextEditingController mobile_number = TextEditingController();
  TextEditingController email_value = TextEditingController();
  TextEditingController textEditingController = TextEditingController();
  final GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  late PresenterEditProfile presenterEditProfile;
  final interval = const Duration(seconds: 1);

  final int timerMaxSeconds = 60;

  int currentSeconds = 0;

  String get timerText =>
      '${((timerMaxSeconds - currentSeconds) ~/ 60).toString().padLeft(2, '0')} Min ${((timerMaxSeconds - currentSeconds) % 60).toString().padLeft(2, '0')} Sec';

  startTimeout([int? milliseconds]) {
    var duration = interval;
    Timer.periodic(duration, (timer) {
      setState(() {
        currentSeconds = timer.tick;
        if (timer.tick >= timerMaxSeconds) {
          timer.cancel();
          resend_otp_flag = 1;
        }
      });
    });
  }

  @override
  void initState() {
    generate_otp_flag = 0;
    verify_otp_flag = 0;
    presenterEditProfile = PresenterEditProfile(this);
    mobile_number.text = old_mobile.toString();
    email_value.text = old_email.toString();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: kHomeBgColor,
        appBar: AppBar(
          leading: IconButton(
            icon: const Icon(Icons.arrow_back_ios_outlined),
            onPressed: () {
              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => const ManageProfile()));
            },
          ),
          title: Text(Languages.of(context)!.Edit_Profile),
          backgroundColor: kFillaSurveyColor,
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
                bottomRight: Radius.circular(30),
                bottomLeft: Radius.circular(30)),
          ),
          actions: [
            Padding(
              padding: const EdgeInsets.only(right: 8),
              child: IconButton(
                onPressed: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => const LocalManageRecord()));
                },
                icon: const Icon(
                  Icons.cloud_upload,
                  color: kDrawerSheetTextColor,
                  size: 40,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(right: 8),
              child: IconButton(
                onPressed: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => const ManageRecord()));
                },
                icon: const Icon(
                  Icons.home,
                  color: kDrawerSheetTextColor,
                  size: 40,
                ),
              ),
            ),
          ],
        ),
        body: Form(
          key: _formKey,
          child: SafeArea(
              child: WillPopScope(
            onWillPop: _onBackPressed,
            child: Container(
              color: kHomeBgColor,
              child: Padding(
                padding: const EdgeInsets.fromLTRB(20, 5, 20, 5),
                child: Wrap(
                  children: [
                    Container(
                      margin: const EdgeInsets.only(top: 20),
                      width: MediaQuery.of(context).size.width,
                      decoration: BoxDecoration(
                        color: kBg,
                        borderRadius: BorderRadius.circular(10.0),
                      ),
                      child: Column(
                        children: [
                          const SizedBox(
                            height: 20,
                          ),
                          Visibility(
                            visible: ui_status == 0 ? true : false,
                            child: Padding(
                              padding:
                                  const EdgeInsets.fromLTRB(20, 20, 20, 20),
                              child: TextFormField(
                                keyboardType: TextInputType.number,
                                autofocus: false,
                                cursorColor: kSkipColor,
                                readOnly: generate_otp_flag == 0 ? false : true,
                                maxLength: 10,
                                inputFormatters: <TextInputFormatter>[
                                  FilteringTextInputFormatter.deny(
                                      RegExp('[,.]')),
                                ],
                                controller: mobile_number,
                                style:
                                    const TextStyle(color: kRefreshTextColor),
                                decoration: InputDecoration(
                                  counterText: "",
                                  labelStyle:
                                      const TextStyle(color: kRefreshTextColor),
                                  labelText:
                                      Languages.of(context)!.Mobile_Number,
                                  focusedBorder: const UnderlineInputBorder(
                                    borderSide: BorderSide(color: kSkipColor),
                                  ),
                                  enabledBorder: const UnderlineInputBorder(
                                    borderSide: BorderSide(color: kSkipColor),
                                  ),
                                ),
                                validator: (value) {
                                  String pattern =
                                      r'(^((\+91)?|91)?[6789][0-9]{9})';
                                  RegExp regExp = RegExp(pattern);
                                  if (value!.isEmpty) {
                                    return Languages.of(context)!
                                        .Please_enter_your_mobile_no;
                                  } else if (!regExp.hasMatch(value)) {
                                    return Languages.of(context)!
                                        .ENTER_VALID_MOBILE;
                                  }
                                  return null;
                                },
                              ),
                            ),
                          ),
                          Visibility(
                            visible: ui_status == 1 ? true : false,
                            child: Padding(
                              padding:
                                  const EdgeInsets.fromLTRB(20, 20, 20, 20),
                              child: TextFormField(
                                enabled: true,
                                autofocus: false,
                                cursorColor: kSkipColor,
                                controller: email_value,
                                readOnly: generate_otp_flag == 0 ? false : true,
                                keyboardType: TextInputType.emailAddress,
                                autovalidateMode: AutovalidateMode.always,
                                style:
                                     TextStyle(color: kRefreshTextColor),
                                decoration:  InputDecoration(
                                  labelStyle:
                                      TextStyle(color: kRefreshTextColor),
                                  labelText: Languages.of(context)!.Enter_Email,
                                  focusedBorder: UnderlineInputBorder(
                                    borderSide: BorderSide(color: kSkipColor),
                                  ),
                                  enabledBorder: UnderlineInputBorder(
                                    borderSide: BorderSide(color: kSkipColor),
                                  ),
                                ),
                                validator: (input) => isEmail(email_value.text)
                                    ? null
                                    : Languages.of(context)!.Check_your_email,
                              ),
                            ),
                          ),

                          const SizedBox(
                            height: 30,
                          ),
                          Visibility(
                              visible: generate_otp_flag == 1 ? true : false,
                              child: Text(
                                   Languages.of(context)!.We_have_send_OTP_on_your +
                                      mobile_number.text,
                                  style: kDashboardTextBlackS)),
                          Visibility(
                              visible: generate_otp_flag == 1 ? true : false,
                              child: const SizedBox(
                                height: 20,
                              )),
                          Visibility(
                            visible: generate_otp_flag == 1 ? true : false,
                            child: Padding(
                              padding:
                                  const EdgeInsets.only(left: 20, right: 20),
                              child: PinCodeTextField(
                                key: pinotp,
                                controller: textEditingController,
                                autoFocus: false,
                                length: 6,
                                obscureText: false,
                                showCursor: false,
                                cursorColor: kFillaSurveyColor,
                                animationType: AnimationType.none,
                                enableActiveFill: true,
                                pinTheme: PinTheme(
                                  activeFillColor: kDashboardgreyBox,
                                  activeColor: kDashboardgreyBox,
                                  inactiveFillColor: kDashboardgreyBox,
                                  inactiveColor: kDashboardgreyBox,
                                  selectedFillColor: kDashboardgreyBox,
                                  selectedColor: kFillaSurveyColor,
                                  shape: PinCodeFieldShape.box,
                                  borderRadius: BorderRadius.circular(20),
                                  fieldHeight: 45,
                                  fieldWidth: 45,
                                ),
                                animationDuration:
                                    const Duration(milliseconds: 100),
                                keyboardType: TextInputType.number,
                                inputFormatters: <TextInputFormatter>[
                                  FilteringTextInputFormatter.allow(
                                      RegExp(r'[0-9]')),
                                ],
                                appContext: context,
                                onChanged: (String value) {},
                              ),
                            ),
                          ),
                          Visibility(
                            visible: verify_otp_flag == 0 ? true : false,
                            child: Padding(
                              padding: const EdgeInsets.all(12),
                              child: Row(
                                children: [
                                  Expanded(
                                    child: GestureDetector(
                                      onTap: () {
                                        email_value.text =
                                            email_value.text.trim();
                                        if (_formKey.currentState!.validate()) {
                                          _changeMobileRequest();
                                        }
                                      },
                                      child: Container(
                                        height: 60,
                                        decoration: BoxDecoration(
                                            color: kSendaSurveyColor,
                                            borderRadius:
                                                BorderRadius.circular(40)),
                                        child: Center(
                                          child: Padding(
                                            padding: const EdgeInsets.all(4.0),
                                            child: Text(
                                              Languages.of(context)!.send_OTP,
                                              textAlign: TextAlign.center,
                                              style: GoogleFonts.poppins(
                                                textStyle: kButtonTextStyle,
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
                          ),
                          // SizedBox(
                          //   height: 40,
                          // ),

                          Visibility(
                            visible:
                                generate_otp_flag == 1 && resend_otp_flag == 0
                                    ? true
                                    : false,
                            child: Padding(
                              padding: const EdgeInsets.all(12),
                              child: Row(
                                children: [
                                  Expanded(
                                    child: Center(
                                      child: Padding(
                                        padding: const EdgeInsets.all(4.0),
                                        child: Text(
                                           Languages.of(context)!.Request_again + timerText,
                                          textAlign: TextAlign.center,
                                          style: GoogleFonts.poppins(
                                            textStyle: kManageProfileheading,
                                          ),
                                        ),
                                      ),
                                    ),
                                  )
                                ],
                              ),
                            ),
                          ),

                          Visibility(
                            visible: resend_otp_flag == 1 ? true : false,
                            child: GestureDetector(
                              onTap: () {
                                _changeMobileRequest();
                              },
                              child: Padding(
                                padding: const EdgeInsets.all(12),
                                child: Row(
                                  children: [
                                    Expanded(
                                      child: Center(
                                        child: Padding(
                                          padding: const EdgeInsets.all(4.0),
                                          child: Text(
                                            "Didn't receive a code? Request again",
                                            textAlign: TextAlign.center,
                                            style: GoogleFonts.poppins(
                                              textStyle: kManageProfileheading,
                                            ),
                                          ),
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                              ),
                            ),
                          ),

                          Visibility(
                            visible: generate_otp_flag == 1 ? true : false,
                            child: Padding(
                              padding: const EdgeInsets.all(12),
                              child: Row(
                                children: [
                                  Expanded(
                                    child: GestureDetector(
                                      onTap: () {
                                        _otp = textEditingController.text;
                                        if (_otp.length == 6) {
                                          verify(_otp);
                                        } else {
                                          _showToast();
                                        }
                                      },
                                      child: Container(
                                        height: 60,
                                        decoration: BoxDecoration(
                                            color: kSendaSurveyColor,
                                            borderRadius:
                                                BorderRadius.circular(40)),
                                        child: Center(
                                          child: Padding(
                                            padding: const EdgeInsets.all(4.0),
                                            child: Text(
                                              Languages.of(context)!.UPDATE,
                                              textAlign: TextAlign.center,
                                              style: GoogleFonts.poppins(
                                                textStyle: kButtonTextStyle,
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
                          )
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          )),
        ));
  }

  _changeMobileRequest() async {
    resend_otp_flag = 0;
    String? new_email, new_mobile;
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = AppSignatureUtil();
    Encrypted encrypted_current_mobile_no_ = encrypt(old_mobile);
    Encrypted encrypted_current_email = encrypt(old_email);
    if (ui_status == 0) {
      Encrypted encrypted_mobile = encrypt(mobile_number.text);
      Encrypted encrypted_email = encrypt(old_email);
      new_email = encrypted_email.base64;
      new_mobile = encrypted_mobile.base64;
    } else {
      Encrypted encrypted_mobile = encrypt(old_mobile);
      Encrypted encrypted_email = encrypt(email_value.text.trim());
      new_email = encrypted_email.base64;
      new_mobile = encrypted_mobile.base64;
    }

    RequestEditProfile request = RequestEditProfile(
        currentMobile: encrypted_current_mobile_no_.base64,
        currentEmail: encrypted_current_email.base64,
        mobile: new_mobile,
        email: new_email.trim());

    String url = BASE_URL + GET_OTP_EDIT_PROFILE;
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), request);

    Util util = Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      loader();
      presenterEditProfile.editprofileGetOTP(request.toJson(), signature,
          timestamp.toString(), GET_OTP_EDIT_PROFILE, acesstoken, context);
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
  }

  Future<ResponseEditProfileOtp?> verify(String otp) async {
    String? new_email, new_mobile;
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? tsnId = pref.getString(TSNID).toString();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    if (ui_status == 0) {
      String? emails = encrypt(old_email).base64.toString(); //email_value
      String? mobiles = encrypt(mobile_number.text).base64.toString();
      new_email = emails;
      new_mobile = mobiles;
    } else {
      String? emails = encrypt(email_value.text.trim()).base64.toString();
      String? mobiles = encrypt(old_mobile).base64.toString();
      new_email = emails;
      new_mobile = mobiles;
    }

    ReqestEditProfileOtp request = ReqestEditProfileOtp(
        otp: int.parse(otp),
        tsnId: tsnId,
        email: new_email,
        mobile: new_mobile);
    AppSignatureUtil appSignatureUtil = AppSignatureUtil();
    String url = BASE_URL + VERIFY_OTP_EDIT_PROFILE;
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), request.toJson());

    Util util = Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      loader();
      return presenterEditProfile.verifyOTPEditProfile(
          request.toJson(),
          signature,
          timestamp.toString(),
          VERIFY_OTP_EDIT_PROFILE,
          acesstoken,
          context);
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
    return null;
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
    // TODO: implement onError
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
    Fluttertoast.showToast(
        msg: utf8.decode(errorTxt.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);

    if (code == "NP00REFRESH") {
      refreshToken();
    } else if (code == "") {
      refreshToken();
    } else if (code == "INVALID_CREDENTIALS") {
      setCredentialslogout();
      Navigator.pushAndRemoveUntil<void>(
          context,
          MaterialPageRoute<void>(
              builder: (BuildContext context) => const Login()),
          ModalRoute.withName('/'));
    }
  }

  @override
  void onResponseEditProfile(ResponseEditProfile responseDto) async {
    // TODO: implement onResponseEditProfile
    SharedPreferences pref = await SharedPreferences.getInstance();
    pref.setString(TSNID, responseDto.data.tsnId);
    generate_otp_flag = 1;
    verify_otp_flag = 1;
    startTimeout();
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
  }

  @override
  void onResponseSuccess(ResponseGetRespondentType responseDto) {
    // TODO: implement onResponseSuccess
  }

  @override
  void onResponseEditProfileOtp(ResponseEditProfileOtp responseDto1) async {
    // TODO: implement onResponseEditProfileOtp
    SharedPreferences pref = await SharedPreferences.getInstance();
    pref.setString(EMAIL, responseDto1.data.email.trim());
    pref.setString(LASTNAME, responseDto1.data.mobile);
    pref.setString(TSNID, responseDto1.data.tsnid);
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
    Fluttertoast.showToast(
        msg: utf8.decode(responseDto1.data.message.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    Navigator.pushReplacement(
        context, MaterialPageRoute(builder: (context) => const DashBoard()));
  }

  void _showToast() {
    Fluttertoast.showToast(
        msg: Languages.of(context)!.INVALID_OTP,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
  }

  Future<bool> _onBackPressed() async {
    Navigator.push(context,
        MaterialPageRoute(builder: (context) => const ManageProfile()));
    return true;
  }

  bool isEmail(String em) {
    String p = r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)'
        r'|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])'
        r'|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$';

    RegExp regExp = RegExp(p);
    return regExp.hasMatch(em);
  }

  @override
  void onTokenExpire() {
    setCredentialslogout();
    Fluttertoast.showToast(
        msg: Languages.of(context)!.Token_Expired,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    Navigator.pushAndRemoveUntil<void>(
        context,
        MaterialPageRoute<void>(
            builder: (BuildContext context) => const Login()),
        ModalRoute.withName('/'));
  }

  Future<void> refreshToken() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? refreshToken = pref.getString(REFRESHTOKEN);

    AppSignatureUtil appSignatureUtil = AppSignatureUtil();
    int timestamp = DateTime.now().millisecondsSinceEpoch;

    String urlr = BASE_URL + REFRESH_TOKEN;
    RequestRefreshToken requestRefreshToken =
        RequestRefreshToken(refreshToken: refreshToken);
    String signature = appSignatureUtil.generateSignature(
        urlr, "null", 0, timestamp.toString(), requestRefreshToken.toMap());

    presenterEditProfile.refreshToken(requestRefreshToken.toMap(), signature,
        timestamp.toString(), REFRESH_TOKEN);
  }

  @override
  Future<void> onResponseRefreshSuccess(
      ResponseRefreshToken responseDto) async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    pref.setString(LOGIN, "true");
    pref.setString(ACESSTOKEN, encrypt(responseDto.data.accessToken).base64);
    pref.setString(REFRESHTOKEN, responseDto.data.refreshToken);

    setState(() {
      _changeMobileRequest();
    });
  }
}
