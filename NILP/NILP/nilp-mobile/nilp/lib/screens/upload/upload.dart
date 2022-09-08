import 'dart:async';

import 'package:easy_localization/easy_localization.dart';
import 'package:flutter/material.dart';
import 'package:flutter_animation_progress_bar/flutter_animation_progress_bar.dart';

import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../dashboard/dashboard.dart';

class Upload extends StatefulWidget {
  const Upload({Key? key}) : super(key: key);

  @override
  _UploadState createState() => _UploadState();
}

int isFilledSurvey = 0;

class _UploadState extends State<Upload> {
  final int splashDuration = 9;
  startTime() async {
    return Timer(Duration(seconds: splashDuration), () async {
      Navigator.push(
          context, MaterialPageRoute(builder: (context) => const DashBoard()));
    });
  }

  @override
  void initState() {
    init();
    super.initState();
  }

  void init() async {
    startTime();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        return false;
      },
      child: Scaffold(
        backgroundColor: kBg,
        body: Center(
          child: SizedBox(
            width: MediaQuery.of(context).size.width,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Center(
                  child: Text(
                    Languages.of(context)!.Please_wait,
                    style: const TextStyle(
                        fontSize: 26,
                        fontWeight: FontWeight.bold,
                        color: kFillaSurveyColor),
                  ).tr(),
                ),
                Center(
                  child: Text(
                    Languages.of(context)!.survey_records_uploading,
                    style: const TextStyle(
                        fontSize: 26,
                        fontWeight: FontWeight.bold,
                        color: kFillaSurveyColor),
                  ).tr(),
                ),
                const SizedBox(
                  height: 10,
                ),
                Align(
                  alignment: Alignment.topLeft,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Container(
                      margin: const EdgeInsets.only(left: 25),
                      child: Text(
                        Languages.of(context)!.Uploading,
                        style: kSurveySentSuccess,
                        softWrap: true,
                      ).tr(),
                    ),
                  ),
                ),
                Center(
                    child: Padding(
                  padding: const EdgeInsets.all(2.0),
                  child: Container(
                    decoration: BoxDecoration(
                      border: Border.all(
                        color: kSendaSurveyColor,
                        width: 3.0,
                      ),
                    ),
                    margin: const EdgeInsets.only(left: 20, right: 20),
                    child: Padding(
                      padding: const EdgeInsets.all(2.0),
                      child: FAProgressBar(
                        currentValue: 100,
                        displayText: '%',
                        size: 20,
                        progressColor: kSendaSurveyColor,
                        animatedDuration: const Duration(milliseconds: 8000),
                      ),
                    ),
                  ),
                )),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
