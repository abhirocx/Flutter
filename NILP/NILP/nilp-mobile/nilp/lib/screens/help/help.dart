import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';

class Help extends StatefulWidget {

  @override
  _HelpState createState() => _HelpState();
}

class _HelpState extends State<Help> {
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(const SystemUiOverlayStyle(
        systemNavigationBarColor: kFillaSurveyColor, // navigation bar color
        statusBarColor: kFillaSurveyColor,
        statusBarIconBrightness: Brightness.light // status bar color
    ));

    return Scaffold(
      body: SafeArea(
        child:SingleChildScrollView(
          child: Container(
            color: kHomeBgColor,
            child: Column(
              children: [
                Row(children: [
                  Expanded(
                      flex: 2,
                      child: Card(
                        margin: EdgeInsets.zero,
                        color: kFillaSurveyColor,
                        shape: const RoundedRectangleBorder(
                          borderRadius: BorderRadius.only(
                              bottomRight: Radius.circular(35),
                              bottomLeft: Radius.circular(35)),
                        ),
                        child: Padding(
                            padding: const EdgeInsets.all(15.0),
                            child: Column(children: [
                              Padding(
                                padding: const EdgeInsets.symmetric(
                                    horizontal: 10, vertical: 1),
                                child: Row(
                                  children: [
                                    const Center(
                                      child: Icon(
                                        Icons.menu,color: kBg,
                                        size: 30,
                                      ),
                                    ),
                                    Expanded(
                                        flex: 7,
                                        child: Container(
                                          margin: EdgeInsets.only(left: 30),
                                          child: TextField(
                                            maxLines: 1,
                                            textAlign: TextAlign.left,
                                            decoration: InputDecoration(
                                              border: InputBorder.none,
                                              hintText: Languages.of(context)!.HELP,
                                              hintStyle: const TextStyle(
                                                  fontSize: 20,
                                                  fontWeight: FontWeight.w600,
                                                  color: kBg,
                                              ),
                                            ),

                                          ),
                                        )),
                                  ],
                                ),
                              ),
                            ])),
                      ))
                ]),






              ],
            ),
          ),
        ),

      ),
    );
  }
}
