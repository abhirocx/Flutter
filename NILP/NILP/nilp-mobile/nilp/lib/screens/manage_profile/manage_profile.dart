import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:nilp/model/response/ResponseEditProfile.dart';
import 'package:nilp/model/response/ResponseGetRespondentType.dart';
import 'package:nilp/model/response/ResponseRefreshToken.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../constants/Strings.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/response/ResponseEditProfileOtp.dart';
import '../../presenter/PresenterEditProfile.dart';
import '../../utility/testEncryption.dart';
import '../dashboard/dashboard.dart';
import '../manage_record/local_manage_record.dart';
import 'edit_mobile_no.dart';

class ManageProfile extends StatefulWidget {
  const ManageProfile({Key? key}) : super(key: key);
  @override
  _ManageProfileState createState() => _ManageProfileState();
}

class _ManageProfileState extends State<ManageProfile>
    implements ResponseContract {
  TextEditingController mobileNumber = TextEditingController();
  TextEditingController emailValue = TextEditingController();
  TextEditingController designationValue = TextEditingController();
  TextEditingController textEditingController = TextEditingController();

  Map? jsonData;
  List? professional;
  bool isLoader = false;
  int flag = 0;

  String name = '',
      id = '',
      school = '',
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
      untaggedVt = '',
      mobile = '',
      email = '',
      state = "",
      designation = '';

  late PresenterEditProfile presenterEditProfile;
  @override
  void initState() {
    presenterEditProfile = PresenterEditProfile(this);
    super.initState();
    getDropdownValue();
    getValue();
  }

  @override
  void dispose() {
    setState(() {
      mobileNumber.dispose();
      emailValue.dispose();
      designationValue.dispose();
      textEditingController.dispose();
    });
    super.dispose();
  }

  getDropdownValue() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? dprofessions = pref.getString("dropdowns");
    setState(() {
      jsonData = jsonDecode(dprofessions!);
      professional = jsonData!['designations'];
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: kHomeBgColor,

        //    AppBar............
        appBar: AppBar(
          leading: IconButton(
            icon: const Icon(Icons.arrow_back_ios_outlined),
            onPressed: () {
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => const DashBoard()));
            },
          ),
          title:  Text(Languages.of(context)!.MANAGE_PROFILE),
          backgroundColor: kFillaSurveyColor,
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
                bottomRight: Radius.circular(30),
                bottomLeft: Radius.circular(30)),
          ),
          actions: [
            Padding(
              padding: const EdgeInsets.only(right: 8),
              child: IconButton(
                onPressed: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => const LocalManageRecord()));
                },
                icon: const Icon(
                  Icons.cloud_upload,
                  color: kDrawerSheetTextColor,
                  size: 40,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.only(right: 8),
              child: IconButton(
                onPressed: () {
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => const DashBoard()));
                },
                icon: const Icon(
                  Icons.home,
                  color: kDrawerSheetTextColor,
                  size: 40,
                ),
              ),
            ),
          ],
        ),
        body: SafeArea(
            child: SingleChildScrollView(
                child: WillPopScope(
          onWillPop: _onBackPressed,
          child: Container(
            color: kHomeBgColor,
            child: Column(
              children: [
                //   Regional Details........
                Padding(
                  padding: const EdgeInsets.fromLTRB(20, 5, 20, 5),
                  child: Wrap(
                    children: [
                      Container(
                        margin: const EdgeInsets.only(top: 20),
                        width: MediaQuery.of(context).size.width,
                        decoration: BoxDecoration(
                          color: kBg,
                          borderRadius: BorderRadius.circular(10.0),
                        ),
                        child: Column(
                          children: [
                            Container(
                              height: 50,
                              width: MediaQuery.of(context).size.width,
                              decoration: const BoxDecoration(
                                color: kFillaSurveyColor,
                                borderRadius: BorderRadius.only(
                                    topLeft: Radius.circular(10),
                                    topRight: Radius.circular(10)),
                              ),
                              child: Align(
                                  alignment: Alignment.centerLeft,
                                  child: Container(
                                      margin: const EdgeInsets.only(left: 20),
                                      child: Text(
                                        Languages.of(context)!.Regional_Details,
                                        style: kBodyTextStyle,
                                      ))),
                            ),
                            Column(
                              children: [
                                Row(
                                  children: [
                                    Expanded(
                                      flex: 1,
                                      child: Padding(
                                        padding: const EdgeInsets.all(8.0),
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
                                              style: kManageProfileheading,
                                            ),
                                          ],
                                        ),
                                      ),
                                    ),
                                    Expanded(
                                      flex: 1,
                                      child: Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              Languages.of(context)!.State,
                                              style: kManageProfile,
                                            ),
                                            Text(
                                              state.toString(),
                                              style: kManageProfileheading,
                                            ),
                                          ],
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                                Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  children: [
                                    Expanded(
                                      flex: 1,
                                      child: Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              Languages.of(context)!.District,
                                              style: kManageProfile,
                                            ),
                                            Text(
                                              district,
                                              style: kManageProfileheading,
                                            ),
                                          ],
                                        ),
                                      ),
                                    ),
                                    Expanded(
                                      flex: 1,
                                      child: Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              Languages.of(context)!.Block,
                                              style: kManageProfile,
                                            ),
                                            Text(
                                              block,
                                              style: kManageProfileheading,
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
                                        padding: const EdgeInsets.all(8.0),
                                        child: Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                              Languages.of(context)!.Cluster,
                                              style: kManageProfile,
                                            ),
                                            Text(
                                              cluster,
                                              style: kManageProfileheading,
                                            )
                                          ],
                                        ),
                                      ),
                                    ),
                                    Expanded(
                                      flex: 1,
                                      child: Padding(
                                        padding: const EdgeInsets.all(8.0),
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
                                              style: kManageProfileheading,
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

                //   Personal Details........
                Wrap(
                  children: [
                    Padding(
                      padding: const EdgeInsets.fromLTRB(20, 5, 20, 30),
                      child: Container(
                        margin: const EdgeInsets.only(top: 20),
                        width: MediaQuery.of(context).size.width,
                        decoration: BoxDecoration(
                          color: kBg,
                          borderRadius: BorderRadius.circular(10.0),
                        ),
                        child: Column(
                          children: [
                            Container(
                              decoration: const BoxDecoration(
                                color: kFillaSurveyColor,
                                borderRadius: BorderRadius.only(
                                    topLeft: Radius.circular(10),
                                    topRight: Radius.circular(10)),
                              ),
                              height: 50,
                              width: MediaQuery.of(context).size.width,
                              //color: kFillaSurveyColor,
                              child: Align(
                                  alignment: Alignment.centerLeft,
                                  child: Container(
                                      margin: const EdgeInsets.only(left: 20),
                                      child: Text(
                                        Languages.of(context)!.Personal_Details,
                                        style: kBodyTextStyle,
                                      ))),
                            ),
                            Padding(
                              padding: const EdgeInsets.all(8.0),
                              child: Align(
                                  alignment: Alignment.centerLeft,
                                  child: Text(
                                      Languages.of(context)!.Surveyor_Name,
                                      style: kDashBoardBoxBody)),
                            ),
                            Padding(
                              padding: const EdgeInsets.fromLTRB(8, 4, 8, 8),
                              child: Align(
                                  alignment: Alignment.centerLeft,
                                  child:
                                      Text(name, style: kYourDashBoardStyle)),
                            ),
                            Form(
                                child: Column(
                              children: [
                                Row(
                                  children: [
                                    Expanded(
                                      flex: 7,
                                      child: Padding(
                                        padding: const EdgeInsets.fromLTRB(
                                            12, 12, 0, 12),
                                        child: TextFormField(
                                          enabled: false,
                                          controller: mobileNumber,
                                          style: const TextStyle(
                                              color: kRefreshTextColor),
                                          decoration: InputDecoration(
                                            //hintText: mobile,
                                            counterText: "",
                                            labelStyle: const TextStyle(
                                                color: kRefreshTextColor),
                                            labelText: Languages.of(context)!
                                                .Mobile_Number,
                                            focusedBorder:
                                                const UnderlineInputBorder(
                                              borderSide:
                                                  BorderSide(color: kSkipColor),
                                            ),
                                            enabledBorder:
                                                const UnderlineInputBorder(
                                              borderSide:
                                                  BorderSide(color: kSkipColor),
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                    GestureDetector(
                                      onTap: () {
                                        Navigator.pushReplacement(
                                            context,
                                            MaterialPageRoute(
                                                builder: (context) =>
                                                    EditManageProfile(flag = 0,
                                                        mobile, email)));
                                      },
                                      child: const Visibility(
                                        visible: true,
                                        child: Padding(
                                          padding: EdgeInsets.only(right: 12),
                                          child: Padding(
                                            padding: EdgeInsets.all(8.0),
                                            child: Icon(
                                              Icons.edit,
                                              size: 25,
                                              color: kSendaSurveyColor,
                                            ),
                                          ),
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                                Row(
                                  children: [
                                    Expanded(
                                      flex: 7,
                                      child: Padding(
                                        padding: const EdgeInsets.fromLTRB(
                                            12, 12, 0, 12),
                                        child: TextFormField(
                                          // keyboardType: TextInputType.emailAddress,
                                          enabled: false,
                                          controller: emailValue,
                                          decoration: InputDecoration(
                                            labelStyle: const TextStyle(
                                                color: kRefreshTextColor),
                                            labelText:
                                                Languages.of(context)!.Email_Id,
                                          ),
                                        ),
                                      ),
                                    ),
                                    GestureDetector(
                                      onTap: () {
                                        Navigator.pushReplacement(
                                            context,
                                            MaterialPageRoute(
                                                builder: (context) =>
                                                    EditManageProfile(flag = 1,
                                                        mobile, email)));
                                      },
                                      child: const Visibility(
                                        visible: true,
                                        child: Padding(
                                          padding: EdgeInsets.only(right: 12),
                                          child: Padding(
                                            padding: EdgeInsets.all(8.0),
                                            child: Icon(
                                              Icons.edit,
                                              size: 25,
                                              color: kSendaSurveyColor,
                                            ),
                                          ),
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                                Padding(
                                  padding: const EdgeInsets.all(12.0),
                                  child: TextFormField(
                                    enabled: false,
                                    autofocus: false,
                                    controller: designationValue,
                                    cursorColor: kBg,
                                    enableInteractiveSelection: true,
                                    maxLength: 10,
                                    style: const TextStyle(
                                        color: kRefreshTextColor),
                                    decoration: InputDecoration(
                                      counterText: "",
                                      labelStyle: const TextStyle(
                                          color: kRefreshTextColor),
                                      labelText: Languages.of(context)!
                                          .Designation_Type,
                                    ),
                                  ),
                                )
                              ],
                            )),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ))));
  }

  getValue() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    setState(() {
      String? firstname = perf.getString(FIRSTNAME);
      String? designations = perf.getString(DESIGNATION);
      String? mobiles = perf.getString(LASTNAME);
      String? emails = perf.getString(EMAIL);
      String? schools = perf.getString(SCHOOL);
      String? usidises = perf.getString(USIDISE);
      String? blocks = perf.getString(BLOCK);
      String? clusters = perf.getString(CLUSTER);
      String? states = perf.getString(STATE);
      String? districts = perf.getString(DISTRICT);
      String? villageWards = perf.getString(VILLAGEWARD);
      String? pincodes = perf.getString(PINCODE);
      name = decrypt(firstname.toString());
      designation = designations.toString();
      mobile = decrypt(mobiles.toString());
      email = decrypt(emails.toString());
      school = schools.toString();
      state = states.toString();
      district = districts.toString();
      id = usidises.toString();
      block = blocks.toString();
      cluster = clusters.toString();
      villageWard = villageWards.toString();
      pincode = pincodes.toString();
      mobileNumber.text = mobile.toString();
      emailValue.text = email;
      designationValue.text = designation;
    });
  }

  @override
  void onError(String errorTxt, String code) {
    // TODO: implement onError
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
    flag = 0;
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
  void onResponseEditProfile(ResponseEditProfile responseDto) async {
    // TODO: implement onResponseEditProfile
    SharedPreferences pref = await SharedPreferences.getInstance();
    pref.setString(TSNID, responseDto.data.tsnId);
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
  }

  @override
  void onResponseSuccess(ResponseGetRespondentType responseDto) {
    // TODO: implement onResponseSuccess
  }

  @override
  void onResponseEditProfileOtp(ResponseEditProfileOtp responseDto1) async {
    // TODO: implement onResponseEditProfileOtp
    SharedPreferences pref = await SharedPreferences.getInstance();
    pref.setString(EMAIL, responseDto1.data.email);
    pref.setString(LASTNAME, responseDto1.data.mobile);
    pref.setString(TSNID, responseDto1.data.tsnid);
    if (isLoader) {
      isLoader = false;
      Navigator.pop(context);
    }
    flag = 0;
    Fluttertoast.showToast(
        msg: responseDto1.data.message,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    Navigator.pushReplacement(
        context, MaterialPageRoute(builder: (context) => const DashBoard()));
  }

  Future<bool> _onBackPressed() async {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => const DashBoard()));
    return true;
  }

  @override
  void onResponseRefreshSuccess(ResponseRefreshToken responseDto) {
    // TODO: implement onResponseRefreshSuccess
  }

  @override
  void onTokenExpire() {
    // TODO: implement onTokenExpire
  }
}
