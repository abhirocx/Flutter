import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/svg.dart';
import 'package:nilp/screens/login/login.dart';
import '../../constants/constants.dart';
import 'package:google_fonts/google_fonts.dart';

import '../../localization/language/languages.dart';
import '../../utility/Util.dart';
import '../direct_registration/DirectRegistration.dart';


class Otlas extends StatefulWidget {

  @override
  _OtlasState createState() => _OtlasState();
}

class _OtlasState extends State<Otlas> {


  @override
  void initState() {
    super.initState();
  }

   int next_screen=4;

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: _onBackPressed,
      child: Scaffold(
        backgroundColor: kBg,
        body: GestureDetector(
          onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
          child: SafeArea(
            child: Column(
              children: [
             Expanded(
               flex: 4,
                 child: Column(
                  children: [
                 SizedBox(
                   height: MediaQuery.of(context).size.height /2.8,
                   child: Center(
                   // child:Image.asset('images/logo.png') ,
                       child:SvgPicture.asset(
                        logo(),
                       )
                   ),
                 ),
                  Text(
                   Languages.of(context)!.Please_select_anyone
                   ,style: kInstructionHeadingss,
                 ),
                 // Padding(
                 //   padding: const EdgeInsets.all(8.0),
                 //   child: const Text(
                 //     "Lorem Ipsum is simply dummy text of"
                 //     ,style: kHeadingTextStyles,
                 //   ),
                 // ),
                 // SizedBox(height: 30,),
               ],
             )),
             Expanded(
               flex:5,
               child:
               Column(
                 children: [
                   Expanded(
                     flex: 2,
                     child: Padding(
                       padding: const EdgeInsets.only(left: 10,right: 10),
                       child: Row(
                         children: [
                           GestureDetector(
                             onTap: () {
                               setState(() {
                                 next_screen=0;
                               });
                               next_screen=0;
                             },
                             child:Card(
                               shape: RoundedRectangleBorder(
                                 side:BorderSide(
                                   color: next_screen==0? Colors.red:Colors.white
                                 ),
                                 borderRadius: BorderRadius.circular(10.0),
                               ),
                               child: SizedBox(
                                 width: MediaQuery.of(context).size.width/2.3,
                                 height: 130,
                                 child: Column(
                                   mainAxisAlignment: MainAxisAlignment.center,
                                   children: [
                                     Expanded(
                                       flex: 6,
                                       child: SvgPicture.asset(
                                         "images/survey_login_img.svg",
                                         height: 60,
                                         width: 60,
                                       ),
                                     ),
                                     Expanded(
                                       flex: 2,
                                       child: Text(
                                         Languages.of(context)!.For_Survey,
                                         textAlign: TextAlign.center,
                                         style: GoogleFonts
                                             .montserrat(
                                           textStyle:
                                           kInstructionHeadingss,
                                         ),
                                       ),
                                     )
                                   ],
                                 ),
                               ),
                             ),
                           ),
                           Spacer(),
                           GestureDetector(
                             onTap: () {
                               setState(() {
                                 next_screen=1;
                               });
                             },
                             child: Card(
                               shape: RoundedRectangleBorder(
                                 side: BorderSide(
                                     color: next_screen==1? Colors.red:Colors.white
                                 ),
                                 borderRadius: BorderRadius.circular(10.0),
                               ),
                               child: SizedBox(
                                width: MediaQuery.of(context).size.width/2.3,
                                 height: 130,
                                 child: Column(
                                   mainAxisAlignment: MainAxisAlignment.center,
                                   children: [
                                     Expanded(
                                       flex: 6,
                                       child: SvgPicture.asset(
                                         "images/registeration.svg",
                                         height: 60,
                                         width: 60,
                                       ),
                                     ),
                                     Expanded(
                                       flex: 2,
                                       child: Text(
                                         Languages.of(context)!.Self_Registration,
                                         textAlign: TextAlign.center,
                                         style: GoogleFonts
                                             .montserrat(
                                           textStyle:
                                           kInstructionHeadingss,
                                         ),
                                       ),
                                     )
                                   ],
                                 ),
                               ),
                             ),
                           )
                         ],
                       ),
                     ),
                   ),
                   Expanded(
                     flex: 2,
                     child: Padding(
                       padding: const EdgeInsets.only(left: 10,right: 10),
                       child: Row(
                         children: [
                           GestureDetector(
                             onTap: () {
                               setState(() {
                                 next_screen=2;
                               });
                             },
                             child:Card(
                               shape: RoundedRectangleBorder(
                                 side: BorderSide(
                                     color: next_screen==2? Colors.red:Colors.white
                                 ),
                                 borderRadius: BorderRadius.circular(10.0),
                               ),
                               child: SizedBox(
                                 width: MediaQuery.of(context).size.width/2.3,
                                 height: 130,
                                 child: Column(
                                   mainAxisAlignment: MainAxisAlignment.center,
                                   children: [
                                     Expanded(
                                       flex: 6,
                                       child: SvgPicture.asset(
                                         "images/otlas.svg",
                                         height: 60,
                                         width: 60,
                                       ),
                                     ),
                                     Expanded(
                                       flex: 2,
                                       child: Text(
                                         Languages.of(context)!.Learning,
                                         textAlign: TextAlign.center,
                                         style: GoogleFonts
                                             .montserrat(
                                           textStyle:
                                           kInstructionHeadingss,
                                         ),
                                       ),
                                     )
                                   ],
                                 ),
                               ),
                             ),
                           ),
                           Spacer(),
                           GestureDetector(
                             onTap: () {
                               setState(() {
                                 next_screen=3;
                               });
                             },
                             child: Card(
                               shape: RoundedRectangleBorder(
                                 side: BorderSide(
                                     color: next_screen==3? Colors.red:Colors.white
                                 ),
                                 borderRadius: BorderRadius.circular(10.0),
                               ),
                               child: SizedBox(
                                 width: MediaQuery.of(context).size.width/2.3,
                                 height: 130,
                                 child: Column(
                                   mainAxisAlignment: MainAxisAlignment.center,
                                   children: [
                                     Expanded(
                                       flex: 6,
                                       child: SvgPicture.asset(
                                         "images/certificate.svg",
                                         height: 60,
                                         width: 60,
                                       ),
                                     ),
                                     Expanded(
                                       flex: 2,
                                       child: Text(
                                         Languages.of(context)!.Certificate,
                                         textAlign: TextAlign.center,
                                         style: GoogleFonts
                                             .montserrat(
                                           textStyle:
                                           kInstructionHeadingss,
                                         ),
                                       ),
                                     )
                                   ],
                                 ),
                               ),
                             ),
                           )
                         ],
                       ),
                     ),
                   ),
                   SizedBox(height: 100,),
                   Expanded(
                     flex: 1,
                     child: Padding(
                       padding: const EdgeInsets.only(
                           left: 14,bottom: 10, right: 14),
                       child: GestureDetector(
                         onTap: () {
                           onClick();
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
                                 Languages.of(context)!.Next,
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
                     ),
                   ),
                 ],
               ),
             )
              ],
            ),
          ),
        ),
      ),
    );
  }

  Future<bool> _onBackPressed() async {
    SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]);
    SystemNavigator.pop();
    return false;
  }

  void onClick() {
    if(next_screen==0){
      Navigator.push(
          context,
          MaterialPageRoute(
              builder: (context) =>
                  Login()
         ));
    }
    else if(next_screen==1){
      Navigator.push(
          context,
          MaterialPageRoute(
              builder: (context) =>
                  DirectRegistration()
            // Upload()
          ));
    }
   else if(next_screen==2){
      // Navigator.push(
      //     context,
      //     MaterialPageRoute(
      //         builder: (context) =>
      //             Login()
      //       // Upload()
      //     ));
    }
    else if(next_screen==3){
      // Navigator.push(
      //     context,
      //     MaterialPageRoute(
      //         builder: (context) =>
      //             Login()
      //       // Upload()
      //     ));
    }
  }

}
