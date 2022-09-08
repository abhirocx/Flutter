import 'dart:async';

import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:google_fonts/google_fonts.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../dashboard/dashboard.dart';


class Success extends StatefulWidget {

  @override
  _SuccessState createState() => _SuccessState();
}

int isFilledSurvey = 0;

class _SuccessState extends State<Success> {



  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) {
              return DashBoard();
            },
          ),
        );
        return false;
      },
      child: Scaffold(
        backgroundColor: kBg,
        body: Center(
          child: Container(
            width: MediaQuery.of(context).size.width,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                SvgPicture.asset(
                  "images/thankyou.svg",
                  width: 180,
                  height: 180,
                  alignment: Alignment.center,
                ),
                Container(
                  margin: const EdgeInsets.only(top: 20,),
                  child: Padding(
                    padding:EdgeInsets.fromLTRB(0, 0, 0,4),
                    child: Center(
                      child: Text(
                        Languages.of(context)!.CONGRATULATIONS,
                        style: kCongratulationsStyle,
                        textAlign: TextAlign.center,
                      ).tr(),
                    ),
                  ),
                ),
                Container(
                  margin: const EdgeInsets.only(left: 16, top: 0, right: 16),
                  child: Center(
                    child: Text(Languages.of(context)!
                        .Learner_is_tagged_successfully_with_VT,
                      //Languages.of(context)!.SURVEY_SENT_SUCCESSFULLY,
                      style: kSurveySentSuccess,
                      textAlign: TextAlign.center,
                      softWrap: true,
                    ).tr(),
                  ),
                ),
                SizedBox(height: 10,),
                GestureDetector(
                  onTap: (){
                    Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (context) =>
                                DashBoard()));
                  },
                  child: Container(
                    child: Padding(
                      padding: const EdgeInsets.all(10.0),
                      child: Text(Languages.of(context)!
                          .Go_to_dashboard,
                        style: kGoDashborad,
                        textAlign: TextAlign.center,
                        softWrap: true,
                      ),
                    ),
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.circular(10),
                        color: Colors.white,
                        boxShadow: const [
                          BoxShadow(color: kHomeRecordColor, spreadRadius: 2),
                        ],
                      )
                  ),
                ),
                // SizedBox(
                //   height: 10,
                // ),
                // Padding(
                //   padding: const EdgeInsets.only(left: 8, right: 8),
                //   child: Text(
                //     Languages.of(context)!.SUCCESS_SCREEN_BODY,
                //     textAlign: TextAlign.center,
                //     style: GoogleFonts.poppins(textStyle: kSucessScreeenBody),
                //   ).tr(),
                // ),
                // SizedBox(
                //   height: 26,
                // ),
                // Padding(
                //   padding: const EdgeInsets.only(left: 32, right: 32),
                //   child: Row(
                //     children: [
                //       Expanded(
                //           child: GestureDetector(
                //         onTap: () {
                //           Navigator.pushAndRemoveUntil<void>(
                //               context,
                //               MaterialPageRoute<void>(
                //                   builder: (BuildContext context) =>
                //                       DashBoard()),
                //               ModalRoute.withName('/'));
                //         },
                //         child: Container(
                //           height: 60,
                //           decoration: BoxDecoration(
                //               color: Colors.orangeAccent,
                //               borderRadius: BorderRadius.circular(10)),
                //           child: Center(
                //             child: Text(
                //               Languages.of(context)!.VIEW_DASHBOARD,
                //               style: GoogleFonts.poppins(
                //                   textStyle: kButtonTextStyle),
                //             ).tr(),
                //           ),
                //         ),
                //       ))
                //     ],
                //   ),
                // ),
                // SizedBox(
                //   height: 20,
                // ),
                // Container(
                //     child: Image(
                //   height: 60,
                //   width: 150,
                //   image: AssetImage("images/niclogo.png"),
                //   alignment: Alignment.center,
                // )),

                // image:AssetImage(
                //   "images/niclogo.png",
                // ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
