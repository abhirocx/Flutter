import 'dart:async';
import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';

import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../utility/Util.dart';


class InstructionWebView_d extends StatefulWidget {
  @override
  InstructionWebView_dState createState() => InstructionWebView_dState();
}

String URL = "";

class InstructionWebView_dState extends State<InstructionWebView_d> {
  @override
  void initState() {
    super.initState();
    // SystemChrome.setEnabledSystemUIOverlays([]);

    // Enable hybrid composition.
    //if (Platform.isAndroid) WebView.platform = SurfaceAndroidWebView();
    /*Locale _locale = Locale('en', '');
    getLocale().then((locale) {
      setState(() {
        _locale = locale;
        if (_locale.languageCode == 'en') {
          URL = "https://www.google.com/";
        } else {
          URL = "https://hello.com/en/index.html";
        }
      });
    });*/
  }

  InAppWebViewGroupOptions options = InAppWebViewGroupOptions(
      crossPlatform: InAppWebViewOptions(
        useShouldOverrideUrlLoading: true,
        mediaPlaybackRequiresUserGesture: false,
      ),
      android: AndroidInAppWebViewOptions(
        useHybridComposition: true,
      ),
      ios: IOSInAppWebViewOptions(
        allowsInlineMediaPlayback: true,
      ));

  @override
  Widget build(BuildContext context) {
    Codec<String, String> stringToBase64 = utf8.fuse(base64);
    getURL();

    return SafeArea(
      child: WillPopScope(onWillPop: _onBackPressed, child: _portraitMode()),
    );
  }

  Widget _portraitMode() {
    return Container(
      color: khtmlBgColor,
      //  height: 100,
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: Column(
            // verticalDirection: VerticalDirection.down,
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                height: MediaQuery.of(context).size.height /12,
                child: Row(
                  children: [
                    Container(
                      margin: EdgeInsets.all(10),
                      height: 25,
                      width: 25,
                      alignment: Alignment.topRight,
                      child: GestureDetector(
                        child: Image.asset('images/cross.png'),
                        onTap: () {
                          SystemChrome.setPreferredOrientations(
                              [DeviceOrientation.portraitUp]);

                          Navigator.pop(context);
                        },
                      ),
                    ),
                    Container(
                      margin: EdgeInsets.fromLTRB(20, 0, 0, 0),
                      child: Text(
                        Languages.of(context)!.INSTRUCTIONS,
                        textAlign: TextAlign.center,
                        style: kInstructionHeading,
                      ),
                    ),
                  ],
                ),
              ),
              Container(
                height: MediaQuery.of(context).size.height- MediaQuery.of(context).size.height /6.64,
                child: InAppWebView(
                  initialOptions: options,
                  initialUrlRequest: URLRequest(url: Uri.parse(URL)),
                ),
              ),
            ]),
      ),
    );
  }

  @protected
  @mustCallSuper
  Future<void> dispose() async {
    await SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
  }

  Future<bool> _onBackPressed() async {
    SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
    Navigator.pop(context);
    //SystemNavigator.pop();
    return false;
  }

  Future<String> convertString(Future<String> scormUrl) async =>
      scormUrl.then((value) {
        return value;
      });

  List <String> currentLocalValue =["asm","en","guj","hin",
    "kan", "mal","pan", "tel"];

  void getURL() {
    if (Util.currentLocal == 'en') {
      URL = ENGLISH_INSTRUCTION;
    }
    else if (Util.currentLocal == 'guj') {
      URL = GUJ_INSTRUCTION;
    }
    else if (Util.currentLocal == 'pan') {
      URL = PAN_INSTRUCTION;
    }
    else if (Util.currentLocal == 'asm') {
      URL = ASM_INSTRUCTION;
    }
    else if (Util.currentLocal == 'kan') {
      URL = KAN_INSTRUCTION;
    }
    else if (Util.currentLocal == 'mar') {
      URL = MAR_INSTRUCTION;
    }
    else if (Util.currentLocal == 'mal') {
      URL = MAL_INSTRUCTION;
    }
    else if (Util.currentLocal == 'tel') {
      URL = TEL_INSTRUCTION;
    }
    else if (Util.currentLocal == 'hin') {
      URL = HINDI_INSTRUCTION;
    }
    else {
      URL = ENGLISH_INSTRUCTION;
    }
  }
}
