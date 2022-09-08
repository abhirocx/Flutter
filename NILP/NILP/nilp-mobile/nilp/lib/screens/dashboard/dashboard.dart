import 'package:expansion_tile_card/expansion_tile_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_svg/svg.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/model/response/ResponseDropdown.dart';
import 'package:nilp/model/response/ResponseGetOTP.dart';
import 'package:nilp/model/response/ResponseGetRespondentType.dart';
import 'package:nilp/model/response/ResponseOTPLogin.dart';
import 'package:nilp/model/response/ResponseRefreshToken.dart';
import 'package:nilp/model/response/ResponseResendOtp.dart';
import 'package:nilp/screens/manage_profile/manage_profile.dart';
import 'package:nilp/screens/survey/surveryFamilyHead.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../Database/SquliteDataBaseHelper.dart';
import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../localization/locale_constant.dart';
import '../../model/FamilyInfo.dart';
import '../../model/request/RequestDashboard.dart';
import '../../model/request/RequestRefreshToken.dart';
import '../../model/response/ResponseDashborad.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../presenter/PresenterGetOtp.dart';
import '../../utility/Util.dart';
import '../../utility/testEncryption.dart';
import '../home_screen/home.dart';
import '../login/login.dart';
import '../manage_record/local_manage_record.dart';
import '../manage_record/manage_record.dart';
import '../manage_tag/manage_tag.dart';

class DashBoard extends StatefulWidget {
  const DashBoard({Key? key}) : super(key: key);

  @override
  _DashBoardState createState() => _DashBoardState();
}

class _DashBoardState extends State<DashBoard> implements ResponseContract {
  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
    String? login = perf.getString(LOGIN);
  }

  SquliteDatabaseHelper squlitedb = new SquliteDatabaseHelper();
  bool isLoader = false;
  String name = '',
      id = '',
      school = "",
      block = '',
      cluster = '',
      district = "",
      role = '',
      villageWard = '',
      pincode = '',
      learnerDashboard = '',
      vtDashborad = '',
      taggedLearner = '',
      taggedVt = '',
      untaggedLearner = '',
      untaggedVt = '';
  String? state = "", designation = '';
  late PresenterGetOtp presenterGetOtp;
  List<Family_Head>? lstdata = [];

  getData() async {
    final db = await squlitedb;
    lstdata = await db.getUser();
    return lstdata;
  }

  @override
  void initState() {
    presenterGetOtp = new PresenterGetOtp(this);
    // setState(() {
    getDashboradData();
    super.initState();

//  });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: kHomeBgColor,
        appBar: AppBar(
          title: Text(
            Languages.of(context)!.Home,
           // textAlign: TextAlign.start,
          ),
          backgroundColor: kFillaSurveyColor,
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
                bottomRight: Radius.circular(30),
                bottomLeft: Radius.circular(30)),
          ),
        ),
        body: WillPopScope(
            onWillPop: _onBackPressed,
            child: SafeArea(
              child: SingleChildScrollView(
                child: Container(
                  color: kHomeBgColor,
                  child: Column(
                    children: [
                      Container(
                        child: ListView(
                          shrinkWrap: true,
                          physics: NeverScrollableScrollPhysics(),
                          children: <Widget>[
                            Padding(
                              padding: const EdgeInsets.symmetric(
                                horizontal: 10.0,
                                vertical: 10,
                              ),
                              child: ExpansionTileCard(
                                baseColor: kBg,
                                expandedColor: kBg,
                                title: Text(
                                  Languages.of(context)!.SCHOOL,
                                  style: kManageProfile,
                                ),
                                subtitle: Text(
                                  school,
                                  style: kDashboardTextBlack,
                                ),
                                children: <Widget>[
                                  const Divider(
                                    thickness: 1.0,
                                    height: 1.0,
                                  ),
                                  Column(
                                    children: [
                                      Row(
                                        children: [
                                          Expanded(
                                            flex: 1,
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Text(
                                                      Languages.of(context)!
                                                          .UDISE_CODE,
                                                      style: kManageProfile),
                                                  Text(
                                                    id,
                                                    style:
                                                        kManageProfileheading,
                                                  ),
                                                ],
                                              ),
                                            ),
                                          ),
                                          Expanded(
                                            flex: 1,
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Text(
                                                    Languages.of(context)!
                                                        .State,
                                                    style: kManageProfile,
                                                  ),
                                                  Text(
                                                    state!,
                                                    style:
                                                        kManageProfileheading,
                                                  ),
                                                ],
                                              ),
                                            ),
                                          )
                                        ],
                                      ),
                                      Row(
                                        mainAxisAlignment:
                                            MainAxisAlignment.start,
                                        children: [
                                          Expanded(
                                            flex: 1,
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Text(
                                                    Languages.of(context)!
                                                        .District,
                                                    style: kManageProfile,
                                                  ),
                                                  Text(
                                                    district,
                                                    style:
                                                        kManageProfileheading,
                                                  ),
                                                ],
                                              ),
                                            ),
                                          ),
                                          Expanded(
                                            flex: 1,
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Text(
                                                    Languages.of(context)!
                                                        .Block,
                                                    style: kManageProfile,
                                                  ),
                                                  Text(
                                                    block,
                                                    style:
                                                        kManageProfileheading,
                                                  ),
                                                ],
                                              ),
                                            ),
                                          )
                                        ],
                                      ),
                                      Row(
                                        children: [
                                          Expanded(
                                            flex: 1,
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Text(
                                                    Languages.of(context)!
                                                        .Cluster,
                                                    style: kManageProfile,
                                                  ),
                                                  Text(
                                                    cluster,
                                                    style:
                                                        kManageProfileheading,
                                                  )
                                                ],
                                              ),
                                            ),
                                          ),
                                          Expanded(
                                            flex: 1,
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Text(
                                                    Languages.of(context)!.Ward,
                                                    style: kManageProfile,
                                                  ),
                                                  Text(
                                                    villageWard,
                                                    style:
                                                        kManageProfileheading,
                                                  )
                                                ],
                                              ),
                                            ),
                                          )
                                        ],
                                      ),
                                      Align(
                                        alignment: Alignment.topLeft,
                                        child: Padding(
                                          padding: const EdgeInsets.all(8.0),
                                          child: Column(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.start,
                                            children: [
                                              Text(
                                                Languages.of(context)!.Pincode,
                                                style: kManageProfile,
                                              ),
                                              Text(
                                                pincode == "0" ? "--" : pincode,
                                                style: kManageProfileheading,
                                              )
                                            ],
                                          ),
                                        ),
                                      ),
                                    ],
                                  )
                                ],
                              ),
                            ),
                          ],
                        ),
                      ),
                      Padding(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 12.0,
                          ),
                          child: Align(
                              alignment: Alignment.topLeft,
                              child: Text(
                                Languages.of(context)!.Survey_Details,
                                style: kDashboardTextBlackS,
                              ))),
                      Padding(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 10.0,
                          vertical: 10,
                        ),
                        child: Container(
                          height: MediaQuery.of(context).size.height / 6,
                          width: MediaQuery.of(context).size.width,
                          decoration: BoxDecoration(
                            border: Border.all(
                                color: kFillaSurveyColor, // set border color
                                width: 3.0),
                            borderRadius: BorderRadius.circular(10.0),
                          ),
                          child: Column(
                            children: [
                              Container(
                                height: MediaQuery.of(context).size.height /
                                    16, //15
                                width: MediaQuery.of(context).size.width,
                                color: kFillaSurveyColor,
                                child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text(
                                      learnerDashboard,
                                      style: kDashboardTextS,
                                    ),
                                    Text(
                                      Languages.of(context)!.Non_Literate,
                                      style: kDashboardheading,
                                    )
                                  ],
                                ),
                              ),
                              Row(
                                children: [
                                  Container(
                                    width:
                                        MediaQuery.of(context).size.width / 2.2,
                                    height: MediaQuery.of(context).size.height /
                                        13, //12
                                    child: Column(
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      children: [
                                        Text(
                                          taggedLearner,
                                          style: kDashBoardBoxBodyS,
                                        ),
                                        Text(
                                          Languages.of(context)!.Tagged,
                                          style: kManageProfile,
                                        )
                                      ],
                                    ),
                                  ),
                                  Container(
                                    height:
                                        MediaQuery.of(context).size.height / 11,
                                    color: kFillaSurveyColor,
                                    width: 2,
                                  ),
                                  Container(
                                    width:
                                        MediaQuery.of(context).size.width / 2.3,
                                    height:
                                        MediaQuery.of(context).size.height / 12,
                                    child: Column(
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      children: [
                                        Text(
                                          untaggedLearner,
                                          style: kDashBoardBoxBodyS,
                                        ),
                                        Text(
                                          Languages.of(context)!.Untagged,
                                          style: kManageProfile,
                                        )
                                      ],
                                    ),
                                  )
                                ],
                              )
                            ],
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 10.0,
                          vertical: 10,
                        ),
                        child: Container(
                          height: MediaQuery.of(context).size.height / 6,
                          width: MediaQuery.of(context).size.width,
                          decoration: BoxDecoration(
                            border: Border.all(
                                color: kFillaSurveyColor, width: 3.0),
                            borderRadius: BorderRadius.circular(10.0),
                          ),
                          child: Column(
                            children: [
                              Container(
                                height: MediaQuery.of(context).size.height / 16,
                                width: MediaQuery.of(context).size.width,
                                color: kFillaSurveyColor,
                                child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Text(
                                      vtDashborad,
                                      style: kDashboardTextS,
                                    ),
                                    Text(
                                      Languages.of(context)!.Voluntary,
                                      style: kDashboardheading,
                                    )
                                  ],
                                ),
                              ),
                              Row(
                                children: [
                                  Container(
                                    width:
                                        MediaQuery.of(context).size.width / 2.2,
                                    height:
                                        MediaQuery.of(context).size.height / 13,
                                    child: Column(
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      children: [
                                        Text(
                                          taggedVt,
                                          style: kDashBoardBoxBodyS,
                                        ),
                                        Text(
                                          Languages.of(context)!.Tagged,
                                          style: kManageProfile,
                                        )
                                      ],
                                    ),
                                  ),
                                  Container(
                                    height:
                                        MediaQuery.of(context).size.height / 11,
                                    color: kFillaSurveyColor,
                                    width: 2,
                                  ),
                                  Container(
                                    width:
                                        MediaQuery.of(context).size.width / 2.3,
                                    height:
                                        MediaQuery.of(context).size.height / 14,
                                    child: Column(
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      children: [
                                        Text(
                                          untaggedVt,
                                          style: kDashBoardBoxBodyS,
                                        ),
                                        Text(
                                          Languages.of(context)!.Untagged,
                                          style: kManageProfile,
                                        )
                                      ],
                                    ),
                                  )
                                ],
                              )
                            ],
                          ),
                        ),
                      ),
                      Padding(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 12.0,
                          ),
                          child: Align(
                              alignment: Alignment.topLeft,
                              child: Text(
                                Languages.of(context)!.Quick_Actions,
                                style: kDashboardTextBlackS,
                              ))),
                      Wrap(
                        children: [
                          Container(
                              padding: EdgeInsets.only(top: 10, bottom: 15),
                              // height: MediaQuery.of(context).size.height/3.2,
                              alignment: Alignment.center,
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.start,
                                children: [
                                  Row(
                                    mainAxisAlignment:
                                        MainAxisAlignment.spaceEvenly,
                                    children: [
                                      GestureDetector(
                                        onTap: () {
                                          Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                  builder: (context) =>
                                                      SurveyFamilyHead()));
                                        },
                                        child: Container(
                                            height: 110,
                                            width: MediaQuery.of(context)
                                                    .size
                                                    .width /
                                                2.2,
                                            decoration: BoxDecoration(
                                              color: kHomeSurveyColor,
                                              borderRadius:
                                                  BorderRadius.circular(10.0),
                                            ),
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                //mainAxisAlignment: MainAxisAlignment.spaceAround,
                                                children: [
                                                  Expanded(
                                                    flex: 6,
                                                    child: SvgPicture.asset(
                                                      "images/survey.svg",
                                                      height: 60,
                                                      width: 60,
                                                    ),
                                                  ),
                                                  Expanded(
                                                    flex: 3,
                                                    child: Row(
                                                      children: [
                                                        Padding(
                                                          padding:
                                                              EdgeInsets.only(
                                                                  left: 8),
                                                          child: Text(
                                                            Languages.of(
                                                                    context)!
                                                                .SURVEY,
                                                            style: GoogleFonts
                                                                .montserrat(
                                                                    textStyle:
                                                                        kDashboardText),
                                                          ),
                                                        ),
                                                        Spacer(),
                                                        Icon(
                                                          Icons
                                                              .arrow_forward_ios,
                                                          size: 20,
                                                          color: kBg,
                                                        )
                                                      ],
                                                    ),
                                                  )
                                                ],
                                              ),
                                            )),
                                      ),
                                      GestureDetector(
                                        onTap: () {
                                          Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                  builder: (context) =>
                                                      ManageTag()));

                                          //SearchTag()));
                                        },
                                        child: Container(
                                            height: 110,
                                            width: MediaQuery.of(context)
                                                    .size
                                                    .width /
                                                2.2,
                                            decoration: BoxDecoration(
                                              color: kHomeTagColor,
                                              borderRadius:
                                                  BorderRadius.circular(10.0),
                                            ),
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Expanded(
                                                    flex: 6,
                                                    child: SvgPicture.asset(
                                                      "images/tag.svg",
                                                      height: 60,
                                                      width: 60,
                                                    ),
                                                  ),
                                                  Expanded(
                                                    flex: 3,
                                                    child: Row(
                                                      children: [
                                                        Padding(
                                                          padding:
                                                              EdgeInsets.only(
                                                                  left: 8),
                                                          child: Text(
                                                            Languages.of(
                                                                    context)!
                                                                .TAG,
                                                            style: GoogleFonts
                                                                .montserrat(
                                                              textStyle:
                                                                  kDashboardText,
                                                            ),
                                                          ),
                                                        ),
                                                        Spacer(),
                                                        Icon(
                                                          Icons
                                                              .arrow_forward_ios,
                                                          size: 20,
                                                          color: kBg,
                                                        )
                                                      ],
                                                    ),
                                                  )
                                                ],
                                              ),
                                            )),
                                      ),
                                    ],
                                  ),
                                  const SizedBox(
                                    height: 10,
                                  ),
                                  Row(
                                    mainAxisAlignment:
                                        MainAxisAlignment.spaceEvenly,
                                    children: [
                                      GestureDetector(
                                        onTap: () {
                                          Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                  builder: (context) =>
                                                      ManageRecord()));
                                        },
                                        child: Container(
                                            height: 110,
                                            width: MediaQuery.of(context)
                                                    .size
                                                    .width /
                                                2.2,
                                            decoration: BoxDecoration(
                                              color: kHomeRecordColor,
                                              borderRadius:
                                                  BorderRadius.circular(10.0),
                                            ),
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                //mainAxisAlignment: MainAxisAlignment.spaceAround,
                                                children: [
                                                  Expanded(
                                                    flex: 6,
                                                    child: SvgPicture.asset(
                                                      "images/manage_records.svg",
                                                      height: 60,
                                                      width: 60,
                                                    ),
                                                  ),
                                                  Expanded(
                                                    flex: 3,
                                                    child: Row(
                                                      children: [
                                                        Text(
                                                          Languages.of(context)!
                                                              .MANAGE_RECORDS,
                                                          style: GoogleFonts
                                                              .montserrat(
                                                            textStyle:
                                                                kDashboardText,
                                                          ),
                                                        ),
                                                        Spacer(),
                                                        Icon(
                                                          Icons
                                                              .arrow_forward_ios,
                                                          size: 20,
                                                          color: kBg,
                                                        )
                                                      ],
                                                    ),
                                                  )
                                                ],
                                              ),
                                            )),
                                      ),
                                      GestureDetector(
                                        onTap: () {
                                          Navigator.push(
                                              context,
                                              MaterialPageRoute(
                                                  builder: (context) =>
                                                      ManageProfile()
                                                  // Upload()
                                                  ));
                                        },
                                        child: Container(
                                            height: 110,
                                            width: MediaQuery.of(context)
                                                    .size
                                                    .width /
                                                2.2,
                                            decoration: BoxDecoration(
                                              color: kHomeProfileColor,
                                              borderRadius:
                                                  BorderRadius.circular(10.0),
                                            ),
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(8.0),
                                              child: Column(
                                                crossAxisAlignment:
                                                    CrossAxisAlignment.start,
                                                children: [
                                                  Expanded(
                                                    flex: 6,
                                                    child: SvgPicture.asset(
                                                      "images/manage_profile.svg",
                                                      height: 60,
                                                      width: 60,
                                                    ),
                                                  ),
                                                  Expanded(
                                                    flex: 3,
                                                    child: Row(
                                                      children: [
                                                        Text(
                                                          Languages.of(context)!
                                                              .MANAGE_PROFILE,
                                                          style: GoogleFonts
                                                              .montserrat(
                                                            textStyle:
                                                                kDashboardText,
                                                          ),
                                                        ),
                                                        Spacer(),
                                                        Icon(
                                                          Icons
                                                              .arrow_forward_ios,
                                                          size: 20,
                                                          color: kBg,
                                                        )
                                                      ],
                                                    ),
                                                  )
                                                ],
                                              ),
                                            )),
                                      )
                                    ],
                                  ),
                                ],
                              )),
                        ],
                      ),
                      FutureBuilder(
                          future: getData(),
                          builder: (context, snapshot) {
                            if (!snapshot.hasData) {
                              return Center(
                                child: new SizedBox(
                                  height: 50.0,
                                  width: 50.0,
                                  /*child: new CircularProgressIndicator(
                                    valueColor:
                                        new AlwaysStoppedAnimation<Color>(
                                            Colors.red),
                                    strokeWidth: 1.0,
                                  ),*/
                                ),
                              );
                            }
                            return getView();
                          })
                    ],
                  ),
                ),
              ),
            )),
        drawer: Drawer(
          child: Container(
            color: kFillaSurveyColor,
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                Container(
                  margin: EdgeInsets.only(top: 40, right: 20),
                  child: Align(
                    alignment: Alignment.topRight,
                    child: GestureDetector(
                      onTap: () {
                        Navigator.pop(context);
                      },
                      child: SvgPicture.asset(
                        "images/close.svg",
                        height: 30,
                      ),
                    ),
                  ),
                ),
                Container(
                  margin: const EdgeInsets.only(top: 60, bottom: 20),
                  child: Row(
                    children: [
                      Expanded(
                        flex: 2,
                        child: SvgPicture.asset(
                          "images/user.svg",
                          height: 40,
                        ),
                      ),
                      Expanded(
                        flex: 8,
                        child: Padding(
                          padding: const EdgeInsets.only(left: 20),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              Text(
                                name,
                                maxLines: 2,
                                overflow: TextOverflow.ellipsis,
                                style: GoogleFonts.domine(
                                  color: kDrawerSheetTextColor,
                                  fontSize: 20,
                                ),
                              ),
                              Text(
                                Languages.of(context)!.Surveyor,
                                style: GoogleFonts.montserrat(color: kBg),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                const Divider(
                  color: kDrawableDividerColor,
                  thickness: 1,
                ),
                ListTile(
                  leading: SvgPicture.asset(
                    "images/language.svg",
                  ),
                  title: Text(
                    Languages.of(context)!.CHANGE_LANGUAGE,
                    style: GoogleFonts.domine(color: kBg),
                  ),
                  onTap: () {
                    getLang();
                    _langBottomSheet(context);
                  },
                ),
                const Divider(
                  color: kDrawableDividerColor,
                  thickness: 1,
                ),
                ListTile(
                  leading: SvgPicture.asset(
                    "images/help.svg",
                  ),
                  title: Text(
                    Languages.of(context)!.HELP,
                    style: GoogleFonts.domine(color: kBg),
                  ),
                  onTap: () {
                    // Navigator.push(context,
                    //     MaterialPageRoute(builder: (context)=>Upload()));
                  },
                ),
                // const Divider(
                //   color: kDrawableDividerColor,
                //   thickness: 1,
                // ),
                // ListTile(
                //   leading: SvgPicture.asset(
                //     "images/privacy_policy.svg",
                //   ),
                //   title: Text(j
                //     Languages.of(context)!.PRIVACY_POLICY,
                //     style: GoogleFonts.domine(color: kBg),
                //   ),
                //   //trailing: Icon(Icons.email),
                //   onTap: () {
                //     // Navigator.push(context,
                //     //     MaterialPageRoute(builder: (context)=>Success()));
                //   },
                // ),
                const Divider(
                  color: kDrawableDividerColor,
                  thickness: 1,
                ),
                ListTile(
                  leading: SvgPicture.asset(
                    "images/logout.svg",
                  ),
                  title: Text(
                    Languages.of(context)!.LOGOUT,
                    style: GoogleFonts.domine(color: kBg),
                  ),
                  onTap: () {
                    _logoutDialogBox(context);
                  },
                ),
                const Divider(
                  color: kDrawableDividerColor,
                  thickness: 1,
                ),
              ],
            ),
          ),
        ));
  }

  Future<void> _langBottomSheet(context) async {
    Util util = new Util();
    bool isOnline = await util.hasInternet();

    List<String> list = [
      // "Assamese",
      "English",
      // "Gujarati ",
      "Hindi",
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
      // "asm",
      "en",
      // "guj",
      "hin"
      // "kan",
      // "mal",
      // "pan",
      // "tel"
    ];

// List <bool> liststyleValue =[alangu,elangu,glangu,hlangu,
//   knlangu, mllangu,plangu, tlangu];
    List<bool> liststyleValue = [elangu, hlangu];

    if (isOnline || !isOnline) {
      showModalBottomSheet(
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.vertical(
              top: Radius.circular(20),
            ),
          ),
          backgroundColor: kFillaSurveyColor,
          context: context,
          builder: (BuildContext bc) {
            return Column(mainAxisSize: MainAxisSize.min, children: [
              Container(
                width: 330,
                height: MediaQuery.of(context).size.height * 0.08,
                // height: MediaQuery.of(context).size.height * 0.1,
                color: kFillaSurveyColor,
                //height: MediaQuery.of(context).size.height * 0.08,
                // height: 80,
                alignment: Alignment.centerLeft,
                padding: const EdgeInsets.all(5.0),
                child: Container(
                  child: Text(
                    Languages.of(context)!.SELECT_LANGUAGE,
                    style: kLanguageoptionHeader,
                  ),
                ),
              ),
              Container(
                height: 1,
                color: kBottomDividerColor,
              ),
              Container(
                height: MediaQuery.of(context).size.height / 4.8 -
                    MediaQuery.of(context).size.height * 0.08,
                // height: MediaQuery.of(context).size.height / 2 -
                //     MediaQuery.of(context).size.height * 0.1,
                width: MediaQuery.of(context).size.width,
                color: kBg,
                child: ListView.builder(
                  itemCount: list.length,
                  itemBuilder: (BuildContext context, int index) {
                    return Column(
                      children: [
                        TextButton(
                          onPressed: () {
                            showDialog(
                                context: context,
                                builder: (context) {
                                  return AlertDialog(
                                    title: Text("Are you sure"),
                                    content: Text("Would you like to select"
                                        " ${list[index]} language"),
                                    actions: [
                                      TextButton(
                                        onPressed: () {
                                          Navigator.pop(context);
                                        },
                                        child: Text("NO"),
                                      ),
                                      TextButton(
                                        onPressed: () {
                                          Navigator.pop(context);
                                          changeLanguage(
                                              context, listvalue[index]);
                                          Util.currentLocal =
                                              currentLocalValue[index];
                                          Navigator.pop(context);
                                        },
                                        child: Text("YES"),
                                      )
                                    ],
                                  );
                                });
                          },
                          child: Align(
                              alignment: Alignment.centerLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(left: 20),
                                child: Text(
                                  list[index],
                                  style: liststyleValue[index]
                                      ? kSelectedLanguageoption
                                      : kLanguageoption,
                                ),

                                // child: Text(list[index].toString(),
                                //     style: kLanguageoption),
                              )),
                        ),
                      ],
                    );
                  },
                ),
              ),
              // Container(
              //   color: kBg,
              //   child: Padding(
              //     padding:
              //         const EdgeInsets.only(left: 32, right: 32, bottom: 3),
              //     child: Row(
              //       children: [
              //         Expanded(
              //           child: Container(
              //             height: 60,
              //             decoration: BoxDecoration(
              //                 color: kSendaSurveyColor,
              //                 borderRadius: BorderRadius.circular(40)),
              //             child: Center(
              //               child: Text(
              //                 Languages.of(context)!.SELECT,
              //                 style: GoogleFonts.poppins(
              //                     textStyle: kButtonTextStyle,
              //                     color: kBg,
              //                     letterSpacing: 1),
              //               ),
              //             ),
              //           ),
              //         )
              //       ],
              //     ),
              //   ),
              // )
            ]);
          });
    } else {}
  }

  void getLanguage() {}

  _logoutDialogBox(BuildContext context) {
    return showDialog(
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text(Languages.of(context)!.ARE_YOU_SURE),
            content: Text(Languages.of(context)!.DO_YOU_LOGOUT),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: Text(Languages.of(context)!.NO),
              ),
              TextButton(
                onPressed: () {
                  setCredentialslogout();
                  Navigator.pushAndRemoveUntil<void>(
                    context,
                    MaterialPageRoute<void>(
                        builder: (BuildContext context) => Otlas()),
                    ModalRoute.withName('/'),
                  );
                },
                child: Text(Languages.of(context)!.YES),
              )
            ],
          );
        });
  }

  Future<bool> _onBackPressed() async {
    return await showDialog(
          context: context,
          builder: (context) => Container(
            decoration: ShapeDecoration(
                shape: ContinuousRectangleBorder(
              borderRadius: BorderRadius.all(
                Radius.circular(30),
              ),
            )),
            child: AlertDialog(
              title: new Text(Languages.of(context)!.ARE_YOU_SURE),
              content: new Text(Languages.of(context)!.DO_YOU_WANT_TO_EXIT),
              actions: <Widget>[
                ElevatedButton(
                  onPressed: () => Navigator.of(context).pop(false),
                  child: Text(Languages.of(context)!.NO),
                ),
                SizedBox(height: 16),
                ElevatedButton(
                  onPressed: () => SystemNavigator.pop(),
                  child: Text(Languages.of(context)!.YES),
                ),
              ],
            ),
          ),
        ) ??
        false;
  }

  @override
  void onResponseSuccess(ResponseGetRespondentType responseDto) {
    // TODO: implement onResponseSuccess
  }

  @override
  void onResponseSuccessFav() {
    // TODO: implement onResponseSuccessFav
  }
  // String id='';
  // String designation='';
  // int school=0;
  // String block='';
  // String cluster='';
  // int district=0;
  // String role='';
  // int state=0;
  // String villageWard='';

  getValue() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    setState(() {
      String? firstname = perf.getString(FIRSTNAME);
      String? schools = perf.getString(SCHOOL);
      String? usidises = perf.getString(USIDISE);
      String? blocks = perf.getString(BLOCK);
      String? clusters = perf.getString(CLUSTER);
      String? states = perf.getString(STATE);
      String? districts = perf.getString(DISTRICT);
      String? villageWards = perf.getString(VILLAGEWARD);
      String? learner_dashboard = perf.getString(LEARNERS);
      String? vt_dashborad = perf.getString(VTT);
      String? tagged_learner = perf.getString(TAGGEDLEARNER);
      String? tagged_vt = perf.getString(TAGGEDVT);
      String? untagged_learner = perf.getString(UNTAGGEDLEARNER);
      String? untagged_vt = perf.getString(UNTAGGEDVT);
      String? pincodes = perf.getString(PINCODE);

      learnerDashboard = learner_dashboard.toString();
      vtDashborad = vt_dashborad.toString();
      taggedLearner = tagged_learner.toString();
      taggedVt = tagged_vt.toString();
      untaggedLearner = untagged_learner.toString();
      untaggedVt = untagged_vt.toString();
      var myInt = int.parse(taggedVt);
      var myInts = int.parse(untaggedVt);

      //  vtDashborad = (myInt + myInts).toString();

      name = decrypt(firstname.toString());
      school = schools.toString();
      state = states;
      pincode = pincodes.toString();
      district = districts.toString();
      id = usidises.toString();
      block = blocks.toString();
      cluster = clusters.toString();
      villageWard = villageWards.toString();
      //getData();
    });
  }

  @override
  void onError(String errorTxt, String code) {
    // TODO: implement onError
    if (code == "NP00REFRESH") {
      refreshToken();
    } else if (code == "") {
      refreshToken();
    } else if (code == "INVALID_CREDENTIALS") {
      setCredentialslogout();
      Navigator.pushAndRemoveUntil<void>(
          context,
          MaterialPageRoute<void>(builder: (BuildContext context) => Otlas()),
          ModalRoute.withName('/'));
    }
  }

  Future<ResponseDashborad?> getDashboradData() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());

    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();

    String url = BASE_URL + DASHBORAD_DATA;
    RequestDashboard requestDashboard = new RequestDashboard(userId: "userid");

    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), requestDashboard.toMap());
    Util util = new Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      return presenterGetOtp.getDashboradData(
          requestDashboard.toMap(),
          signature,
          timestamp.toString(),
          DASHBORAD_DATA,
          acesstoken!,
          context);
    } else {
/*      Fluttertoast.showToast(
          msg: Languages.of(context)!.NO_INTERNET,
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);*/

      setState(() {
        getValue();
      });
    }
  }

  @override
  Future<void> onResponseDashboard(responseDto) async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    setState(() {
      if (responseDto.data.learner != null)
        pref.setString(LEARNERS, responseDto.data.learner!);
      if (responseDto.data.taggedLearner != null)
        pref.setString(TAGGEDLEARNER, responseDto.data.taggedLearner!);
      if (responseDto.data.taggedVt != null)
        pref.setString(TAGGEDVT, responseDto.data.taggedVt!);
      if (responseDto.data.untaggedLearner != null)
        pref.setString(UNTAGGEDLEARNER, responseDto.data.untaggedLearner!);
      if (responseDto.data.untaggedVt != null)
        pref.setString(UNTAGGEDVT, responseDto.data.untaggedVt!);
      if (responseDto.data.vt! != null)
        pref.setString(VTT, responseDto.data.vt!);
    });
    await getValue();
  }

  @override
  void onResponseMasterData(ResponseDropdown responseDto) {
    // TODO: implement onResponseMasterData
  }

  @override
  void onResponseOTPLogin(ResponseOtpLogin responseOtpLogin) {
    // TODO: implement onResponseOTPLogin
  }

  @override
  void onResponseOTPSuccess(ResponseGetOtp responseDto) {
    // TODO: implement onResponseOTPSuccess
  }

  @override
  void onResponseResendOTPSuccess(ResponseResendOtp responseResendOtp) {
    // TODO: implement onResponseResendOTPSuccess
  }

  Future<dynamic>? loader() {
    isLoader = true;
    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => Center(
        child: CircularProgressIndicator(
          valueColor: new AlwaysStoppedAnimation<Color>(kFillaSurveyColor),
          strokeWidth: 1.0,
        ),
      ),
    );
  }

  getVisibilty() {
    if (lstdata == null)
      return false;
    else if (lstdata!.length <= 0)
      return false;
    else
      return true;
  }

  getView() {
    return Visibility(
      visible: getVisibilty(),
      child: GestureDetector(
        onTap: () {
          Navigator.push(context,
              MaterialPageRoute(builder: (context) => LocalManageRecord()));
        },
        child: Container(
          child: Padding(
            padding: const EdgeInsets.only(
              left: 10,
              right: 10,
              bottom: 20,
            ),
            child: Row(
              children: [
                Expanded(
                  child: Container(
                    height: 60,
                    decoration: BoxDecoration(
                        color: kSendaSurveyColor,
                        borderRadius: BorderRadius.circular(40)),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      children: [
                        SizedBox(
                          width: 40,
                        ),
                        Text(
                          Languages.of(context)!.OFFLINE_RECORDS,
                          style: GoogleFonts.poppins(
                              textStyle: kButtonTextStyle,
                              color: kBg,
                              letterSpacing: 0.3),
                        ),
                        Container(
                          width: 40,
                          height: 40,
                          child: Center(
                            child: Text(
                              lstdata!.length.toString(),
                              style: kUploadButtonTextStyle,
                              // textAlign: TextAlign.right,
                            ),
                          ),
                          decoration: BoxDecoration(
                              shape: BoxShape.circle, color: khtmlBgColor),
                        ),
                        /*  Container(
                          width: 60,
                          height: 60,
                          color: khtmlBgColor,
                          child: Text(
                            lstdata!.length.toString(),
                            style: GoogleFonts.poppins(
                                textStyle: kButtonTextStyle,
                                color: kBg,
                                letterSpacing: 0.3),
                            // textAlign: TextAlign.right,
                          ),
                          decoration: BoxDecoration(
                              shape: BoxShape.circle, color: khtmlBgColor),
                        ),*/
                      ],
                    ),
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }

  Future<void> refreshToken() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? refreshToken = pref.getString(REFRESHTOKEN);
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    int timestamp = DateTime.now().millisecondsSinceEpoch;

    String urlr = BASE_URL! + REFRESH_TOKEN!;
    RequestRefreshToken requestRefreshToken =
        new RequestRefreshToken(refreshToken: refreshToken);
    String signature = appSignatureUtil.generateSignature(
        urlr, "null", 0, timestamp.toString(), requestRefreshToken.toMap());

    presenterGetOtp.refreshToken(requestRefreshToken.toMap(), signature,
        timestamp.toString(), REFRESH_TOKEN);
  }

  @override
  Future<void> onResponseRefreshSuccess(
      ResponseRefreshToken responseDto) async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    pref.setString(LOGIN, "true");
    pref.setString(ACESSTOKEN, encrypt(responseDto.data.accessToken!).base64);
    pref.setString(REFRESHTOKEN, responseDto.data.refreshToken);

    setState(() {
      getDashboradData();
    });
  }

  @override
  void onTokenExpire() {
    setCredentialslogout();
    Fluttertoast.showToast(
        msg: "Token Expired",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    Navigator.pushAndRemoveUntil<void>(
        context,
        MaterialPageRoute<void>(builder: (BuildContext context) => Otlas()),
        ModalRoute.withName('/'));
  }
}


//128
