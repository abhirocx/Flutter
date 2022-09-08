import 'dart:async';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:nilp/localization/language/languages.dart';
import '../../constants/constants.dart';
import 'package:flutter_inappwebview/flutter_inappwebview.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/gestures.dart';



class DirectRegistration extends StatefulWidget {
  const DirectRegistration({Key? key}) : super(key: key);

  @override
  _DirectRegistrationState createState() => _DirectRegistrationState();
}

class _DirectRegistrationState extends State<DirectRegistration>{

  String _url = 'https://staging-nilp.inroad.in/nilp/#/register-mobile';
  bool isLoading=true;
  InAppWebViewController? webViewController;

  @override
  void initState() {
    getlink();
    super.initState();
  }

  getlink() async{
     _url="https://staging-nilp.inroad.in/nilp/#/register-mobile";
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: _onBackPressed,
      child: Scaffold(
        //backgroundColor: kHomeBgColor,
        appBar:
        PreferredSize(
          preferredSize: Size.fromHeight(60.0),
        child:AppBar(
          title:  Text(
            Languages.of(context)!.Self_Registration,
          ),
          backgroundColor: kFillaSurveyColor,
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
                bottomRight: Radius.circular(30),
                bottomLeft: Radius.circular(30)),
          ),
        ),),
      body:Container(
        height: MediaQuery.of(context).size.height - 60,
        child: InAppWebView(
          gestureRecognizers: Set()
            ..add(
              Factory<VerticalDragGestureRecognizer>(
                    () {
                  print(_url);
                  return VerticalDragGestureRecognizer();
                },
              ),
            ),
          initialUrlRequest: URLRequest(
            url: Uri.parse(_url),
          ),
          onWebViewCreated: (controller) {
            webViewController = controller;
          },
          initialOptions: InAppWebViewGroupOptions(
            crossPlatform: InAppWebViewOptions(
              supportZoom: true,
            ),
          ),
        ),
      ),
       ),
    );
  }

  Future<bool> _onBackPressed() async {
    Navigator.pop(context);
    return false;
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
}
