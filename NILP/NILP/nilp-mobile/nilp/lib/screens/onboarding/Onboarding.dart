import 'package:flutter/material.dart';
// ignore: unused_import
import 'package:flutter/services.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:shared_preferences/shared_preferences.dart';


import '../../constants/Strings.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../utility/Util.dart';
import 'InstructionWebView.dart';
// import 'package:survey_app/screens/login/loginle_fonts.dart';

class OnBoardingPage extends StatefulWidget {
  @override
  _OnBoardingPageState createState() => _OnBoardingPageState();
}

class _OnBoardingPageState extends State<OnBoardingPage> {
  get curve => null;

  void setOnboarding() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(ONBOARDING, "true");
    String? onboarding = perf.getString(ONBOARDING);
    //print("onboardinge done: $onboarding");
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    // setOnboarding();
  }

  PageController pageController = PageController(initialPage: 0);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBg,
      body: SafeArea(
        child: PageView(
          controller: pageController,
          children: [
            Container(
              child: Column(
                children: [
                  Expanded(
                    flex: 1,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      children: [
                        Container(
                          height: 50,
                          width: 200,
                          child: Image.asset(getImage()),
                        ),
                        GestureDetector(
                          onTap: () {
                            setOnboarding();
                            Navigator.pushAndRemoveUntil<void>(
                              context,
                              MaterialPageRoute<void>(
                                  builder: (BuildContext context) =>
                                      InstructionWebView()),
                              ModalRoute.withName('/'),
                            );
                          },
                          child: Text(
                            Languages.of(context)!.SKIP,
                            style: kSkip,
                          ),
                        )
                      ],
                    ),
                  ),
                  Expanded(
                    flex: 3,
                    child: Container(
                      height: 300,
                      width: 300,
                      child: SvgPicture.asset(
                        'images/screen1.svg',
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 0,
                    child: Padding(
                      padding:
                          const EdgeInsets.only(left: 40, right: 40, bottom: 5),
                      child: Text(
                        Languages.of(context)!.ONBOARDING1_HEADING,
                        textAlign: TextAlign.center,
                        style: kOnboardingHeadingTextStyle,
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 0,
                    child: Center(
                      child: Padding(
                        padding:
                            const EdgeInsets.only(left: 35, right: 35, top: 5),
                        child: Container(
                          child: Text(
                            Languages.of(context)!.ONBOARDING1_BODY,
                            textAlign: TextAlign.center,
                            style: kOnboardingBodyTextStyle,
                          ),
                        ),
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: GestureDetector(
                      onTap: () {
                        pageController.animateToPage(1,
                            duration: Duration(milliseconds: 50),
                            curve: Curves.easeIn);
                      },
                      child: Container(
                        height: 50,
                        width: 50,
                        child: Image.asset('images/tap1.png'),
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: Text(
                      Languages.of(context)!.TAP_TO_CONTINUE,
                      style: kOnboardingTTCTextStyle,
                    ),
                  ),
                ],
              ),
            ),
            Container(
              child: Column(
                children: [
                  Expanded(
                    flex: 1,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      children: [
                        Container(
                          height: 50,
                          width: 200,
                          child: Image.asset(getImage()),
                        ),
                        GestureDetector(
                          onTap: () {
                            setOnboarding();
                            Navigator.pushAndRemoveUntil<void>(
                              context,
                              MaterialPageRoute<void>(
                                  builder: (BuildContext context) =>
                                      InstructionWebView()),
                              ModalRoute.withName('/'),
                            );
                          },
                          child: Text(
                            Languages.of(context)!.SKIP,
                            style: kSkip,
                          ),
                        )
                      ],
                    ),
                  ),
                  Expanded(
                    flex: 0,
                    child: Padding(
                      padding: const EdgeInsets.only(left: 40, right: 40),
                      child: Text(
                        Languages.of(context)!.ONBOARDING2_HEADING,
                        textAlign: TextAlign.center,
                        style: kOnboardingHeadingTextStyle,
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 0,
                    child: Center(
                      child: Padding(
                        padding:
                            const EdgeInsets.only(left: 35, right: 35, top: 5),
                        child: Container(
                          child: Text(
                            Languages.of(context)!.ONBOARDING2_BODY,
                            textAlign: TextAlign.center,
                            style: kOnboardingBodyTextStyle,
                          ),
                        ),
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 3,
                    child: Container(
                      height: 300,
                      width: 300,
                      child: SvgPicture.asset(
                        'images/screen2.svg',
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: GestureDetector(
                      onTap: () {
                        pageController.animateToPage(2,
                            duration: Duration(milliseconds: 100),
                            curve: Curves.easeIn);
                      },
                      child: Container(
                        height: 50,
                        width: 50,
                        child: Image.asset('images/tap2.png'),
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: Text(
                      Languages.of(context)!.TAP_TO_CONTINUE,
                      style: kOnboardingTTCTextStyle,
                    ),
                  ),
                ],
              ),
            ),
            Container(
              child: Column(
                children: [
                  Expanded(
                    flex: 1,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      children: [
                        Text(" "),
                        Container(
                          height: 50,
                          width: 200,
                          child: Image.asset(getImage()),
                        ),
                        Text(
                          " ",
                          style: TextStyle(
                            color: Color(0xFF333333),
                          ),
                        )
                      ],
                    ),
                  ),
                  Expanded(
                    flex: 0,
                    child: Padding(
                      padding: const EdgeInsets.only(left: 40, right: 40),
                      child: Text(
                        Languages.of(context)!.ONBOARDING3_HEADING,
                        textAlign: TextAlign.center,
                        style: kOnboardingHeadingTextStyle,
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 0,
                    child: Center(
                      child: Padding(
                        padding:
                            const EdgeInsets.only(left: 35, right: 35, top: 5),
                        child: Container(
                          child: Text(
                            Languages.of(context)!.ONBOARDING3_BODY,
                            textAlign: TextAlign.center,
                            style: kOnboardingBodyTextStyle,
                          ),
                        ),
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 3,
                    child: Container(
                      height: 300,
                      width: 300,
                      child: SvgPicture.asset(
                        'images/screen3.svg',
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: GestureDetector(
                      onTap: () {
                        setOnboarding();
                        Navigator.pushAndRemoveUntil<void>(
                          context,
                          MaterialPageRoute<void>(
                              builder: (BuildContext context) =>
                                  InstructionWebView()),
                          ModalRoute.withName('/'),
                        );
                      },
                      child: Container(
                        height: 50,
                        width: 50,
                        child: Image.asset('images/tap3.png'),
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: Text(
                      Languages.of(context)!.TAP_TO_CONTINUE,
                      style: kOnboardingTTCTextStyle,
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  String getImage() {
    if (Util.currentLocal == "en")
      return 'images/ncf.png';
    else
      return 'images/ncf_hin.png';
  }
}
