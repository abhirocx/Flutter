import 'dart:async';
import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/svg.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/model/request/RequestUser.dart';
import 'package:nilp/model/response/ResponseDropdown.dart';
import 'package:nilp/model/response/ResponseRefreshToken.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/request/RequestOtpLogin.dart';
import '../../model/request/RequestResendOTP.dart';
import '../../model/response/ResponseDashborad.dart';
import '../../model/response/ResponseGetOTP.dart';
import '../../model/response/ResponseOTPLogin.dart';
import '../../model/response/ResponseResendOtp.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../presenter/PresenterGetOtp.dart';
import '../../utility/Util.dart';
import '../../utility/testEncryption.dart';
import '../dashboard/dashboard.dart';

class Otp extends StatefulWidget {
  const Otp({Key? key}) : super(key: key);

  @override
  _OtpState createState() => _OtpState();
}

class _OtpState extends State<Otp> implements ResponseContract {
  String _otp = "";

  bool isLoader = false;
  var pinotp;
  late int _otpInt;
  late PresenterGetOtp presenterGetOtp;
  TextEditingController textEditingController=TextEditingController();
  late String _comingSms;



  int currentSeconds = 0;
  final int timerMaxSeconds = 60;
  int resend_otp_flag = 0;
  final interval = const Duration(seconds: 1);


  String get timerText =>
      '${((timerMaxSeconds - currentSeconds) ~/ 60).toString().padLeft(2, '0')} Min ${((timerMaxSeconds - currentSeconds) % 60).toString().padLeft(2, '0')} Sec';

  startTimeout([int? milliseconds]) {
    var duration = interval;
    Timer.periodic(duration, (timer) {
      setState(() {
        currentSeconds = timer.tick;
        if (timer.tick >= timerMaxSeconds) {
          timer.cancel();
          setState(() {
            resend_otp_flag = 0;
          });
        }
      });
    });
  }

  @override
  void initState() {
    super.initState();
    // textEditingController = TextEditingController();
    // textEditingController.addListener(() {
    //   setState(() {});
    // });
    presenterGetOtp = PresenterGetOtp(this);
//    textEditingController = TextEditingController();
   // initSmsListener();
  }

  // Future<void> initSmsListener() async {
  //   String? comingSms;
  //   try {
  //     comingSms = await AltSmsAutofill().listenForSms;
  //   } on PlatformException {
  //     comingSms = '';
  //   }
  //   setState(() {
  //     _comingSms = comingSms!;
  //     textEditingController.text=_comingSms[32]+_comingSms[33]+_comingSms[34]+_comingSms[35]+_comingSms[36]+_comingSms[37];
  //   });
  // }


  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: _onBackPressed,
      child: Scaffold(
        backgroundColor: kBg,
        body: GestureDetector(
          onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
          child: SingleChildScrollView(
            child: SafeArea(
              child: Column(
                children: [
                  // Padding(
                  //   padding: const EdgeInsets.only(left: 16, top: 16),
                  //   child: Row(
                  //     children: [
                  //       GestureDetector(
                  //         onTap: () {
                  //           Navigator.pop(context);
                  //         },
                  //         child: const SizedBox(
                  //           height: 25,
                  //           width: 25,
                  //           child: Image(
                  //             image: AssetImage("images/back.png"),
                  //           ),
                  //         ),
                  //       ),
                  //     ],
                  //   ),
                  // ),
                  // SizedBox(
                  //   height: 249,
                  //   width: 253,
                  //   child: SvgPicture.asset(
                  //     "images/login_img.svg",
                  //   ),
                  // ),
                  // const SizedBox(
                  //   height: 30,
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
                    height: MediaQuery.of(context).size.height / 2.9,
                    child: Center(
                      //child:Image.asset('images/logo.png') ,
                        child:SvgPicture.asset(
                          logo(),
                        )
                    ),
                  ),
                  SizedBox(
                    height: 30,
                  ),
                  Align(
                    alignment: Alignment.bottomCenter,
                    child: Row(children: [
                      Expanded(
                          flex: 1,
                          child: Card(
                            color: kFillaSurveyColor,
                            margin: EdgeInsets.zero,
                            shape: const RoundedRectangleBorder(
                              borderRadius: BorderRadius.only(
                                  topLeft: Radius.circular(35),
                                  topRight: Radius.circular(35)),
                            ),
                            child: Padding(
                                padding: const EdgeInsets.all(15.0),
                                child: Column(
                                    mainAxisAlignment: MainAxisAlignment.start,
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Padding(
                                        padding: const EdgeInsets.all(16.0),
                                        child: Align(
                                          alignment: Alignment.topLeft,
                                          child: Text(
                                            Languages.of(context)!
                                                .Enter_OTP_Code,
                                            style: GoogleFonts.poppins(
                                                textStyle: kHeadingTextStyle,
                                                color: kBg),
                                          ),
                                        ),
                                      ),
                                      Padding(
                                        padding: const EdgeInsets.only(
                                            left: 16.0, right: 16, top: 16),
                                        child: Text(
                                          //Languages.of(context)!.OTP_BODY,
                                          Languages.of(context)!.OTP_MSG,
                                          textAlign: TextAlign.left,
                                          style: GoogleFonts.poppins(
                                            textStyle: const TextStyle(
                                              color: kBg,
                                            ),
                                          ),
                                        ),
                                      ),
                                      const SizedBox(
                                        height: 20,
                                      ),
                                      Form(
                                        key: pinotp,
                                        child: formUi(),
                                      ),
                                      const SizedBox(
                                        height: 10,
                                      ),
                                      Row(
                                        mainAxisAlignment:
                                            MainAxisAlignment.center,
                                        crossAxisAlignment:
                                            CrossAxisAlignment.center,
                                        children: [
                                          Visibility(
                                            visible: resend_otp_flag == 1
                                                ? true
                                                : false,
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(4.0),
                                              child: Text(
                                                timerText,
                                                textAlign: TextAlign.center,
                                                style: GoogleFonts.poppins(
                                                    textStyle: const TextStyle(
                                                  color: kDrawerSheetTextColor,
                                                  fontSize: 16,
                                                  letterSpacing: 1,
                                                  fontFamily: 'Poppins',
                                                  fontWeight: FontWeight.bold,
                                                )),
                                              ),
                                            ),
                                          ),
                                          Visibility(
                                            visible: resend_otp_flag == 0
                                                ? true
                                                : false,
                                            child: GestureDetector(
                                              onTap: () => resendOTP(),
                                              child: Padding(
                                                padding: const EdgeInsets.only(
                                                    left: 16.0,
                                                    right: 16,
                                                    bottom: 10),
                                                child: Align(
                                                  alignment: Alignment.topRight,
                                                  child: Text(
                                                    Languages.of(context)!
                                                        .Resend_OTP,
                                                    style: GoogleFonts.poppins(
                                                        textStyle:
                                                            const TextStyle(
                                                      color:
                                                          kDrawerSheetTextColor,
                                                      fontSize: 16,
                                                      letterSpacing: 1,
                                                      fontFamily: 'Poppins',
                                                      fontWeight:
                                                          FontWeight.bold,
                                                    )),
                                                  ),
                                                ),
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                      const SizedBox(
                                        height: 10,
                                      ),
                                      Padding(
                                        padding: const EdgeInsets.only(
                                            left: 16, right: 16),
                                        child: Row(
                                          children: [
                                            Expanded(
                                              child: GestureDetector(
                                                onTap: () {
                                                  _otp = textEditingController
                                                      .text;
                                                  _otpInt = int.parse(_otp);
                                                  if (_otp.length == 6) {
                                                    verifyOtp(_otp);
                                                  } else {
                                                    _showToast();
                                                  }
                                                },
                                                child: Container(
                                                  height: 60,
                                                  decoration: BoxDecoration(
                                                      color: kSendaSurveyColor,
                                                      borderRadius:
                                                          BorderRadius.circular(
                                                              40)),
                                                  child: Center(
                                                    child: Text(
                                                      Languages.of(context)!
                                                          .VERIFY,
                                                      style:
                                                          GoogleFonts.poppins(
                                                        textStyle:
                                                            kButtonTextStyle,
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
                                        height: 120,
                                      ),
                                    ])),
                          ))
                    ]),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget formUi() {
    return Padding(
      padding: const EdgeInsets.only(left: 16.0, right: 16, top: 20),
      child: TextFormField(
        //
        controller: textEditingController, //
        keyboardType: TextInputType.number,
        autofocus: false,
        cursorColor: kBg,
        onEditingComplete: searchText,
        enableInteractiveSelection: true,
        maxLength: 10,
        style: const TextStyle(color: kBg),
        inputFormatters: [
          LengthLimitingTextInputFormatter(6), // for mobile
        ],
        validator: (value) {
          String pattern = r'(^([0-9]{6})';
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
          labelText: Languages.of(context)!.Enter_your_OTP,
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

  void setCredentials() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString("login", "true");
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

  Future<ResponseOtpLogin?> verifyOtp(String otp) async {
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = AppSignatureUtil();

    final prefs = await SharedPreferences.getInstance();
    // String? otp = prefs.getString(OTP).toString();
    String? tsnid = prefs.getString(TSNID).toString();
    RequestOtpLogin request = RequestOtpLogin(otp: otp, tsnId: tsnid);
    String url = BASE_URL + OTP_LOGIN;
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), request.toMap());

    Util util = Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      loader();
      return presenterGetOtp.verifyOTP(
          request.toMap(), signature, timestamp.toString(), OTP_LOGIN, context);
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

  @override
  void onError(String errorTxt, String code) {
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
    textEditingController.clear();
    Fluttertoast.showToast(
        msg: utf8.decode(errorTxt.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
  }

  @override
  void onResponseOTPSuccess(ResponseGetOtp responseDto) {
    // TODO: implement onResponseOTPSuccess
    // getDropdownValue();
  }

  @override
  void onResponseSuccessFav() {
    // TODO: implement onResponseSuccessFav
  }

  @override
  Future<void> onResponseOTPLogin(ResponseOtpLogin responseOtpLogin) async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    pref.setString(LOGIN, "true");
    pref.setString(
        ACESSTOKEN, encrypt(responseOtpLogin.data.accessToken).base64);
    pref.setString(REFRESHTOKEN, responseOtpLogin.data.refreshToken);
    pref.setString(FIRSTNAME, responseOtpLogin.data.user.fullName);
    pref.setString(DESIGNATION, responseOtpLogin.data.user.designation);
    pref.setString(SCHOOL, responseOtpLogin.data.user.schoolName);
    pref.setString(USIDISE, responseOtpLogin.data.user.schoolUdiseCode);
    pref.setString(STATE, responseOtpLogin.data.user.stateName);
    pref.setString(DISTRICT, responseOtpLogin.data.user.districtName);
    pref.setString(BLOCK, responseOtpLogin.data.user.blockName);
    pref.setString(CLUSTER, responseOtpLogin.data.user.clusterName);
    pref.setString(VILLAGEWARD, responseOtpLogin.data.user.villageWardName);
    pref.setString(LASTNAME, responseOtpLogin.data.user.mobile);
    pref.setString(EMAIL, responseOtpLogin.data.user.email);
    pref.setString(
        PINCODE, responseOtpLogin.data.user.schoolPincode.toString());
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }

    getDropdownValue();
    Navigator.pushAndRemoveUntil<void>(
      context,
      MaterialPageRoute<void>(
          builder: (BuildContext context) => const DashBoard()),
      ModalRoute.withName('/'),
    );
  }

  resendOTP() async {
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = AppSignatureUtil();

    SharedPreferences pref = await SharedPreferences.getInstance();
    String? tsnid = pref.getString(TSNID);
    loader();
    RequestResendOtp request = RequestResendOtp(tsnId: tsnid!);
    String url = BASE_URL + RESEND_OTP;
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), request);

    presenterGetOtp.resendOTP(
        request.toMap(), signature, timestamp.toString(), RESEND_OTP, context);
  }

  @override
  Future<void> onResponseResendOTPSuccess(
      ResponseResendOtp responseResendOtp) async {
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
      textEditingController.clear();
    }
    setState(() {
      resend_otp_flag = 1;
    });
    startTimeout();

    final pref = await SharedPreferences.getInstance();
    pref.setString(TSNID, responseResendOtp.data.tsnId);
    // Fluttertoast.showToast(
    //     msg: responseResendOtp.data.otp.toString(),
    //     toastLength: Toast.LENGTH_LONG,
    //     gravity: ToastGravity.BOTTOM,
    //     timeInSecForIosWeb: 1,
    //     textColor: Colors.black,
    //     backgroundColor: Colors.white,
    //     fontSize: 16.0);
  }

  @override
  void onResponseSuccess(responseDto) {
    // TODO: implement onResponseSuccess
  }

  Future<ResponseDropdown?> getDropdownValue() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = AppSignatureUtil();

    String url = BASE_URL + DROPDOWN_VALUE;
    RequestUser requestUser = RequestUser(mobile: "q53672828");

    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), requestUser.toMap());
    Util util = Util();
    bool isOnline = await util.hasInternet();
    if (isOnline) {
      return presenterGetOtp.getMasterData(requestUser.toMap(), signature,
          timestamp.toString(), DROPDOWN_VALUE, acesstoken, context);
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

  @override
  Future<void> onResponseMasterData(ResponseDropdown responseDto) async {
    var json = jsonEncode(responseDto.data!.toJson());
    SharedPreferences pref = await SharedPreferences.getInstance();
    try {
      String jsonString = json;
      await pref.setString("dropdowns", jsonString);
    } catch (e) {}
  }

  Future<bool> _onBackPressed() async {
    SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
    SystemNavigator.pop();
    return false;
  }

  @override
  void onResponseDashboard(ResponseDashborad responseDto) async {
    // TODO: implement onResponseDashboard
    SharedPreferences pref = await SharedPreferences.getInstance();
    setState(() {
      pref.setString(LEARNERS, responseDto.data.learner);
      pref.setString(TAGGEDLEARNER, responseDto.data.taggedLearner);
      pref.setString(TAGGEDVT, responseDto.data.taggedVt);
      pref.setString(UNTAGGEDLEARNER, responseDto.data.untaggedLearner);
      pref.setString(UNTAGGEDVT, responseDto.data.untaggedVt);
      pref.setString(VTT, responseDto.data.vt);
    });
  }

  searchText() {
    _otp = textEditingController.text;
    _otpInt = int.parse(_otp);
    if (_otp.length == 6) {
      verifyOtp(_otp);
    } else {
      _showToast();
    }
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
