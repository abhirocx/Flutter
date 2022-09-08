import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/svg.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:launch_review/launch_review.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../localization/locale_constant.dart';
import '../../model/request/RequestVersionAPI.dart';
import '../../model/response/ResponseVersionAPI.dart';
import '../../networking/ApiBaseHelper.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../presenter/PresenterCheckVersion.dart';
import '../../utility/Util.dart';
import '../dashboard/dashboard.dart';
import '../home_screen/home.dart';
import '../login/login.dart';

class Splash extends StatefulWidget {
  const Splash({Key? key}) : super(key: key);

  @override
  _SplashState createState() => _SplashState();
}

class _SplashState extends State<Splash> with TickerProviderStateMixin implements ResponseContract {
  late AnimationController controller;
  final ApiBaseHelper httpRequests = ApiBaseHelper();
  bool value = false;
  late String version;
  late PresenterCheckVersion presenterCheckVersion;

  void setOnboarding() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(ONBOARDING, "true");
    //  String? onboarding = perf.getString(ONBOARDING);
  }

  @override
  void initState() {
    // TODO: implement initState
    PackageInfo.fromPlatform().then((PackageInfo packageInfo) {
      version = packageInfo.version;
      //getPackageInfo(version);
      print("Inside initState after getPackageInfo: $version");
      postCheckVersionApi(version);
      print("Inside initState after postCheckVersionApi: $version");
      presenterCheckVersion = new PresenterCheckVersion(this);
    });
    super.initState();
  //  _navigatetohome();
    controller = BottomSheet.createAnimationController(this);
    controller.duration = const Duration(milliseconds: 1000);
  }

  _navigatetohome() async {
    await Future.delayed(const Duration(milliseconds: 00), () {});

    SharedPreferences perf = await SharedPreferences.getInstance();
    String? onboarding = perf.getString(ONBOARDING);
    String? login = perf.getString(LOGIN);
    Util util = Util();
    bool isOnline = await util.hasInternet();
    if (isOnline || !isOnline) {
      if (onboarding == 'true') {
        if (login == 'true') {
          Navigator.pushReplacement(context,
              MaterialPageRoute(builder: (context) => const DashBoard()));
        } else {
          Navigator.pushReplacement(
              context, MaterialPageRoute(builder: (context) =>  Otlas()));
        }
      } else {
        _langBottomSheet(context);
      }
    } else {
      Fluttertoast.showToast(
          msg: Languages.of(context)!.NO_INTERNET,
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
      _navigatetohome();
    }
  }

  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
        systemNavigationBarColor: kFillaSurveyColor, // navigation bar color
        statusBarColor: kFillaSurveyColor,
        statusBarIconBrightness: Brightness.dark // status bar color
        ));
    return const SplashImage();
  }

  Future<void> _langBottomSheet(context) async {
    Util util = Util();
    bool isOnline = await util.hasInternet();
    int selectLang = 0;

    List<String> list = [
      //"Assamese",
      "English",
      // "Gujarati",
      "Hindi"
      // "Kannada",
      // "Malayalam",
      // "Punjabi",
      // "Telugu"
    ];

    List<String> listvalue = [
      //"as",
      "en",
      //"gu",
      "hi"
    ];
    //"kn", "ml", "pa", "te"];

    List<String> currentLocalValue = [
      //  "asm",
      "en",
      //  "guj",
      "hin"
    ];
    // "kan",
    // "mal",
    // "pan",
    // "tel"

    List<bool> liststyleValue = [
      //alangu,
      elangu,
      //glangu,
      hlangu
    ];
    //  knlangu, mllangu,plangu, tlangu];

    if (isOnline || !isOnline) {
      showModalBottomSheet(
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.vertical(
              top: Radius.circular(20),
            ),
          ),
          transitionAnimationController: controller,
          isDismissible: false,
          enableDrag: false,
          backgroundColor: kFillaSurveyColor,
          isScrollControlled: false,
          context: context,
          builder: (BuildContext bc) {
            return Container(
                color: kOTPFieldColour,
                child: Column(mainAxisSize: MainAxisSize.min, children: [
                  Container(
                    width: 330,
                    height: MediaQuery.of(context).size.height * 0.08,
                    color: kFillaSurveyColor,
                    alignment: Alignment.centerLeft,
                    padding: const EdgeInsets.all(5.0),
                    child: Text(
                      Languages.of(context)!.SELECT_LANGUAGE,
                      textAlign: TextAlign.center,
                      style: kInstructionHeadings,
                    ),
                  ),
                  Container(
                    height: 1,
                    color: kOTPFieldColour,
                  ),
                  Container(
                    height: MediaQuery.of(context).size.height / 4.8 -
                        MediaQuery.of(context).size.height * 0.08,
                    // height: MediaQuery.of(context).size.height / 2 -
                    //     MediaQuery.of(context).size.height * 0.1,
                    width: MediaQuery.of(context).size.width,
                    color: Colors.white,
                    child: ListView.builder(
                      itemCount: list.length,
                      itemBuilder: (BuildContext context, int index) {
                        return Column(
                          children: [
                            TextButton(
                              onPressed: () {
                                showDialog(
                                    barrierDismissible: false,
                                    context: context,
                                    builder: (context) {
                                      return AlertDialog(
                                        title: const Text("Are you sure"),
                                        content: Text(
                                            "Would you like to select ${list[index]} language"),
                                        actions: [
                                          TextButton(
                                            onPressed: () {
                                              Navigator.pop(context);
                                            },
                                            child: const Text("NO"),
                                          ),
                                          TextButton(
                                            onPressed: () {
                                              changeLanguage(
                                                  context, listvalue[index]);
                                              selectLang = 1;
                                              Util.currentLocal =
                                                  currentLocalValue[index];
                                              Navigator.pop(context);
                                              setOnboarding();
                                              Navigator.pushReplacement(
                                                  context,
                                                  MaterialPageRoute(
                                                      builder: (context) =>
                                                           Otlas()));
                                            },
                                            child: const Text("YES"),
                                          )
                                        ],
                                      );
                                    });
                              },
                              child: Align(
                                  alignment: Alignment.centerLeft,
                                  child: Padding(
                                    padding: const EdgeInsets.only(left: 30),
                                    child: Text(
                                      list[index].toString(),
                                      style: liststyleValue[index]
                                          ? kSelectedLanguageoption
                                          : kLanguageoption,
                                    ),
                                  )),
                            ),
                          ],
                        );
                      },
                    ),
                  ),
                ]));
          }).whenComplete(() {
        controller = BottomSheet.createAnimationController(this);
        controller.duration = const Duration(milliseconds: 1500);
        if (selectLang == 0) SystemNavigator.pop();
        //print('Hey there, I\'m calling after hide bottomSheet');
      });
    } else {}
  }

  void onError(String errorTxt, String code) {
    // TODO: implement onError
    Fluttertoast.showToast(
        msg: errorTxt.toString(),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
  }

  Future<ResponseVersionApi?> postCheckVersionApi(String version) async {
    String? id;
    if (Platform.isAndroid) {
      id ="android";
      print("Android Version: $version");
    }
    else if (Platform.isIOS) {
      id ="ios";
      print("iOS Version: $version");
    }
    print("url: $version");
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    RequestVersionApi requestCheckVersion =
    new RequestVersionApi(
        userOs: id.toString(),
        installedVersion:version.toString());


    String url = BASE_URL+Version_Api ;
    print("Inside postCheckVersionApi: $version");
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(),requestCheckVersion
    );
    Util util = new Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      return presenterCheckVersion.postCheckVersion(requestCheckVersion.toMap(),
          signature, timestamp.toString(), Version_Api);
    } else {
      Fluttertoast.showToast(
          msg: "NO INTERNET",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
    }
  }
  @override
  void onResponseSuccess(ResponseVersionApi responseDto) {
    // TODO: implement onResponseSuccess
    print(responseDto);
    print("values_amans ${responseDto.data!.updateAvailable}");
    if(responseDto.data!.updateAvailable==true){
      if(responseDto.data!.isCompulsory==true){
        print("values_aman ${responseDto.data!.updateAvailable}");
        showAlertDialog(context);
      }
      else{
        print("values_aman ${responseDto.data!.updateAvailable}");
        showAlertDialogs(context);
      }
    }
    else{
      print("values");
      _navigatetohome();
    }
  }


  showAlertDialog(BuildContext context) {
    Widget okButton = FlatButton(
      child: Text("OK"),
      onPressed: () {
        LaunchReview.launch(
          androidAppId: "com.np.nilp",
          iOSAppId: "1597449083",
        );
      },
    );

    // Create AlertDialog
    AlertDialog alert = AlertDialog(
      title: Text("App Update Available"),
      content: Text("Please update latest version app"),
      actions: [
        okButton,
      ],
    );

    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (BuildContext context) {
        return WillPopScope(
            onWillPop: _onBackPressed,
            child: alert);
      },
    );

  }

  showAlertDialogs(BuildContext context) {
    Widget okButton = FlatButton(
      child: Text("OK"),
      onPressed: () {
        LaunchReview.launch(
          androidAppId: "com.np.nilp",
          iOSAppId: "1597449083",
        );
      },
    );

    Widget cancelButton = FlatButton(
      child: Text("NOT NOW"),
      onPressed: () {
        Navigator.pop(context);
        _navigatetohome();
      },
    );

    // Create AlertDialog
    AlertDialog alert = AlertDialog(
      title: Text("App Update Available"),
      content: Text("Please update latest version app"),
      actions: [
        okButton,
        cancelButton
      ],
    );

    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (BuildContext context) {
        return WillPopScope(
            onWillPop:_onBackPressed,
            child: alert);
      },
    );

  }

  Future<bool> _onBackPressed() async {
    SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
    SystemNavigator.pop();
    return false;
  }


}

class SplashImage extends StatelessWidget {
  const SplashImage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () => _willPopCallback(context),
      child: Container(
          color: Colors.white,
          child: Column(
            children: [
              Expanded(
                  flex: 3,
                  child: Center(child: Image.asset('images/splash.png'))),
              Expanded(
                  flex: 4,
                  child: SvgPicture.asset(
                   logo(),
                  )
                ,
              ),
              Expanded(
                  flex: 6,
                  child: Container(
                      margin: EdgeInsets.zero,
                      child: Image.asset(
                        'images/splash_bottom_bg.png',
                        fit: BoxFit.fill,
                      )))
            ],
          )),
    );
  }

  Future<bool> _willPopCallback(BuildContext context) async {
    return true;
  }
}
