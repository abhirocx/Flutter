import 'dart:convert';

import 'package:expansion_tile_card/expansion_tile_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/model/response/ResponseEditVTLearner.dart';
import 'package:nilp/model/response/ResponseRefreshToken.dart';
import 'package:nilp/screens/manage_record/local_manage_record.dart';
import 'package:nilp/screens/manage_tag/EditTag/searchEditTag.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../../constants/Strings.dart';
import '../../../constants/UrlEndPoints.dart';
import '../../../constants/constants.dart';
import '../../../localization/language/languages.dart';
import '../../../model/request/ReequestVT.dart';
import '../../../model/request/RequestRefreshToken.dart';
import '../../../model/request/RequestTagging.dart';
import '../../../model/response/ResponseEditVt.dart';
import '../../../model/response/ResponseTagging.dart';
import '../../../networking/AppSignatureUtil.dart';
import '../../../presenter/PresenterEditVT.dart';
import '../../../utility/Util.dart';
import '../../../utility/testEncryption.dart';
import '../../dashboard/dashboard.dart';
import '../../login/login.dart';
import '../../success/success.dart';
import '../Tagging/searchTag.dart';

class EditTagging extends StatefulWidget {
  EditTagging(var family_head_ids, family_head_name, family_head_address) {
    head_id = family_head_ids;
    head_name = family_head_name;
    head_address = family_head_address;
  }

  @override
  _EditTaggingState createState() => _EditTaggingState();
}

var head_name, head_id, head_address;

class _EditTaggingState extends State<EditTagging> implements ResponseContract {
  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
    String? login = perf.getString(LOGIN);
  }

  late ResponseEditVt data;
  late PresenterEditTag presenterEditTag;
  late List<bool> _isChecked;
  String? valuemember;
  int flagoffline = 0;
  int flag = 0;

  @override
  void initState() {
    presenterEditTag = new PresenterEditTag(this);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: kHomeBgColor,
        appBar: AppBar(
          leading: IconButton(
            icon: const Icon(
              Icons.arrow_back_ios_outlined,
            ),
            onPressed: () {
              _onBackPressed();
            },
          ),
          title: Text(
               Languages.of(context)!.Edit_Tag,
          ),
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
                          builder: (context) => LocalManageRecord()));
                },
                icon: Icon(
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
                  Navigator.push(context,
                      MaterialPageRoute(builder: (context) => DashBoard()));
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
        body: WillPopScope(
          onWillPop: _onBackPressed,
          child: Container(
              child: FutureBuilder<ResponseEditVt?>(
                  future: getVTList(head_id),
                  builder: (context, snapshot) {
                    if (!snapshot.hasData) {
                      return Center(
                        child: CircularProgressIndicator(
                          valueColor: AlwaysStoppedAnimation<Color>(
                              kDrawerSheetTextColor),
                          strokeWidth: 7.0,
                        ),
                      );
                    } else {
                      return getView();
                    }
                  })),
        ));
  }

  Future<ResponseEditVt?> getVTList(String id) async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());

    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    ReequestVt requestSearchMember = new ReequestVt(id: id);

    String url = BASE_URL + GET_Edit_VT_List;

    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), requestSearchMember.toMap());
    Util util = new Util();
    bool isOnline = await util.hasInternet();
    if (flagoffline == 0) {
      if (isOnline) {
        return presenterEditTag.getEditVtList(
            requestSearchMember.toMap(),
            signature,
            timestamp.toString(),
            GET_Edit_VT_List,
            context,
            acesstoken);
      } else {
        Fluttertoast.showToast(
            msg: Languages.of(context)!.NO_INTERNET,
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIosWeb: 1,
            textColor: Colors.black,
            backgroundColor: Colors.white,
            fontSize: 16.0);
      }
    }
  }

  @override
  void onError(String errorTxt, String code) {
    Navigator.pop(context);
    Fluttertoast.showToast(
        msg: utf8.decode(errorTxt.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);

    if (code == "NP00REFRESH") {
      refreshToken();
    } else if (code == "") {
      refreshToken();
    } else if (code == "INVALID_CREDENTIALS") {
      setCredentialslogout();
      Navigator.pushAndRemoveUntil<void>(
          context,
          MaterialPageRoute<void>(builder: (BuildContext context) => Login()),
          ModalRoute.withName('/'));
    }
  }

  @override
  void onResponseSuccess(ResponseEditVt responseDto) {
    data = responseDto;

    List<FamilyVt>? familyvt = data.data?.familyVt![0].members;
    List<NeighbourVt>? neighbourvt;
    List<OtherVt>? othervt;

  //  List? other = data.data?.others;

    if (flag == 0) {
      for (int i = 0; i < data.data!.familyVt!.length; i++) {
        familyvt = data.data?.familyVt![i].members;
        familyvt!.forEach((element) {
          if (element.selected == true)
            valuemember = element.id;
        });
      }


      for (int i = 0; i < data.data!.neighbourVt!.length; i++) {
        neighbourvt = data.data?.neighbourVt![i].members;
        neighbourvt!.forEach((element) {
          if (element.selected == true)
            valuemember = element.id;
        });
      }


      for (int i = 0; i < data.data!.otherVt!.length; i++) {
        othervt = data.data?.otherVt![i].members;
        othervt!.forEach((element) {
          if (element.selected == true)
            valuemember = element.id;
        });
      }



      flag++;
      print("flag ${flag}");
    }

    // TODO: implement onResponseSuccess
  }

  Tag(List<String> learner, String vtID) async {
    loader();
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    RequestTagging requestTagging =
        new RequestTagging(learner: learner, vt: vtID);
    String url = BASE_URL + VT_TAGGING;
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), requestTagging.toMap());
    Util util = new Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      return presenterEditTag.vtTagging(requestTagging.toMap(), signature,
          timestamp.toString(), VT_TAGGING, context, acesstoken);
    } else {
      Fluttertoast.showToast(
          msg: Languages.of(context)!.NO_INTERNET,
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
    }
  }

  @override
  void onResponseTagSuccess(ResponseTagging responseDto) {
    // TODO: implement onResponseTagSuccess
    Navigator.pop(context);
    Navigator.push(context, MaterialPageRoute(builder: (context) => Success()));
  }

  Widget getView() {
    return SafeArea(
        child: Container(
      color: kHomeBgColor,
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(left: 10, right: 10),
            child: InkWell(
              onTap: () {
                Navigator.push(context,
                    MaterialPageRoute(builder: (context) => SearchTag()));
              },
              child: Card(
                margin: EdgeInsets.only(top: 20),
                shadowColor: Colors.grey[100],
                color: kBg,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10)),
                child: Container(
                  height: 50,
                  child: Row(
                    children: [
                      Expanded(
                          flex: 8,
                          child: Container(
                            margin: EdgeInsets.only(left: 20),
                            child: Text(
                              Languages.of(context)!
                                  .Search_Family_Head_Name_or_Mobile_No,
                              style: TextStyle(fontSize: 13),
                            ),
                          )),
                      const Expanded(
                        flex: 2,
                        child: Padding(
                          padding: EdgeInsets.all(5.0),
                          child: Icon(
                            Icons.search,
                            color: kSendaSurveyColor,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
          const SizedBox(
            height: 20,
          ),
          Row(
            children: [
              Container(
                width: MediaQuery.of(context).size.width / 2.1,
                height: MediaQuery.of(context).size.height / 1.5,
                color: kBg,
                child: Column(
                  children: [
                    Container(
                      height: 50,
                      width: double.infinity,
                      child: Card(
                        color: kFillaSurveyColor,
                        child: Container(
                          margin: const EdgeInsets.only(
                            top: 5,
                            bottom: 5,
                          ),
                          child: Padding(
                            padding: EdgeInsets.fromLTRB(3, 3, 3, 3),
                            child: Center(
                                child: Text(
                              Languages.of(context)!.Family_Member,
                              style: kDashboardText,
                            )),
                          ),
                        ),
                      ),
                    ),
                    Flexible(
                        child: ListView.builder(
                            itemCount: 1,
                            itemBuilder: (BuildContext context, int index) {
                              return Container(
                                color: kTagColor,
                                child: Padding(
                                  padding: const EdgeInsets.all(2.0),
                                  child: Column(
                                    children: [
                                      CheckboxListTile(
                                        title: Text(decrypt(head_name),
                                            style: kTagTextBlack),
                                        value: true,
                                        activeColor: kDrawerSheetTextColor,
                                        checkColor: kBottomSheetText,
                                        onChanged: (val) {
                                          setState(
                                            () {
                                              _isChecked[index] = true;
                                            },
                                          );
                                        },
                                      ),
                                      Divider(
                                        thickness: 1.0,
                                        height: 1.0,
                                      ),
                                    ],
                                  ),
                                ),
                              );
                            })),
                  ],
                ),
              ),
              Spacer(),
              Container(
                width: MediaQuery.of(context).size.width / 2.1,
                height: MediaQuery.of(context).size.height / 1.5,
                color: kBg,
                child: Column(
                  children: [
                    Container(
                      height: 50,
                      width: double.infinity,
                      child: Card(
                          color: kFillaSurveyColor,
                          child: Container(
                            margin: const EdgeInsets.only(
                              top: 5,
                              bottom: 5,
                            ),
                            child: Padding(
                              padding: EdgeInsets.fromLTRB(3, 0, 3, 0),
                              child: Center(
                                  child: Text(
                                    Languages.of(context)!.Voluntary_Teacher,
                                style: kDashboardText,
                              )),
                            ),
                          )),
                    ),
                    Flexible(
                      child: ListView(
                        physics: AlwaysScrollableScrollPhysics(),
                        children: <Widget>[
                          Container(
                            height: 30,
                            width: double.infinity,
                            child: Card(
                                color: kDrawerSheetTextColor,
                                child: Container(
                                  // margin: EdgeInsets.only(left: 10),
                                  child: Center(
                                      child: Text(
                                        Languages.of(context)!.Within_Family,
                                        style: kTagTextBlack,
                                      )),
                                )),
                          ),
                          ListView.builder(
                              itemCount: data.data!.familyVt!.length,
                              shrinkWrap: true,
                              physics: NeverScrollableScrollPhysics(),
                              itemBuilder: (BuildContext context, int index) {
                                return ExpansionTileCard(
                                  baseColor: kBg,
                                  expandedColor: kBg,
                                  title: Text(
                                    decrypt(data.data!.familyVt![index].name
                                        .toString()),
                                    style: kTagBody,
                                  ),
                                  subtitle: Text(
                                    decrypt(data.data!.familyVt![index].address
                                        .toString()),
                                    style: kTagSubHeading,
                                  ),
                                  children: <Widget>[
                                    const Divider(
                                      thickness: 1.0,
                                      height: 1.0,
                                    ),
                                    Container(
                                      color: kTagColor,
                                      child: ListView.builder(
                                          shrinkWrap: true,
                                          physics:
                                              NeverScrollableScrollPhysics(),
                                          itemCount: data.data!.familyVt![index]
                                              .members!.length,
                                          itemBuilder: (BuildContext context,
                                              int indexs) {
                                            return Column(
                                              children: [
                                                RadioListTile<String>(
                                                    value: data
                                                        .data!
                                                        .familyVt![index]
                                                        .members![indexs]
                                                        .id
                                                        .toString(),
                                                    groupValue: valuemember,
                                                    // groupValue: data.data!.familyVt![index].selected==true?data.data!.familyVt![index].members![indexs].id:valuemember,
                                                    //  groupValue:
                                                    //  data.data!.familyVt![index].selected==true?
                                                    //  data.data!.familyVt![index].id:
                                                    //  "",
                                                    activeColor:
                                                        kDrawerSheetTextColor,
                                                    selectedTileColor:
                                                        kSkipColor,
                                                    onChanged: (value) {
                                                      setState(() {
                                                        valuemember = value;
                                                      });
                                                    },
                                                    title: Text(
                                                        decrypt(data
                                                            .data!
                                                            .familyVt![index]
                                                            .members![indexs]
                                                            .name
                                                            .toString()),
                                                        style: kTagTextBlack)),
                                                Divider(
                                                  thickness: 1.0,
                                                  height: 1.0,
                                                ),
                                              ],
                                            );
                                          }),
                                    )
                                  ],
                                );
                              }),
                          Container(
                            height: 30,
                            width: double.infinity,
                            child: Card(
                                color: kDrawerSheetTextColor,
                                child: Container(
                                  child: Center(
                                      child: Text(
                                    Languages.of(context)!.Within_Neighbourhoad,
                                    style: kTagTextBlack,
                                  )),
                                )),
                          ),
                          ListView.builder(
                              itemCount: data.data!.neighbourVt!.length,
                              shrinkWrap: true,
                              physics: NeverScrollableScrollPhysics(),
                              itemBuilder: (BuildContext context, int index) {
                                return ExpansionTileCard(
                                  baseColor: kBg,
                                  expandedColor: kBg,
                                  title: Text(
                                    decrypt(data.data!.neighbourVt![index].name
                                        .toString()),
                                    style: kTagBody,
                                  ),
                                  subtitle: Text(
                                    decrypt(data
                                        .data!.neighbourVt![index].address
                                        .toString()),
                                    style: kTagSubHeading,
                                  ),
                                  children: <Widget>[
                                    const Divider(
                                      thickness: 1.0,
                                      height: 1.0,
                                    ),
                                    Container(
                                      color: kTagColor,
                                      child: ListView.builder(
                                          shrinkWrap: true,
                                          physics:
                                              NeverScrollableScrollPhysics(),
                                          itemCount: data
                                              .data!
                                              .neighbourVt![index]
                                              .members!
                                              .length,
                                          itemBuilder: (BuildContext context,
                                              int indexs) {
                                            return Column(
                                              children: [
                                                RadioListTile<String>(
                                                    value: data
                                                        .data!
                                                        .neighbourVt![index]
                                                        .members![indexs]
                                                        .id
                                                        .toString(),
                                                    groupValue: valuemember,
                                                    //  groupValue: data.data!.neighbourVt![index].selected==true?data.data!.neighbourVt![index].members![indexs].id:valuemember,
                                                    // groupValue: data
                                                    //     .data!
                                                    //     .neighbourVt![index].selected==true
                                                    //     ?data.data!.neighbourVt![index].id:"",
                                                    activeColor:
                                                        kDrawerSheetTextColor,
                                                    selectedTileColor:
                                                        kSkipColor,
                                                    onChanged: (value) {
                                                      setState(() {
                                                        valuemember = value;
                                                      });
                                                      print(
                                                          "radio1 $valuemember");
                                                    },
                                                    title: Text(
                                                        decrypt(data
                                                            .data!
                                                            .neighbourVt![index]
                                                            .members![indexs]
                                                            .name
                                                            .toString()),
                                                        style: kTagTextBlack)),
                                                Divider(
                                                  thickness: 1.0,
                                                  height: 1.0,
                                                ),
                                              ],
                                            );
                                          }),
                                    )
                                  ],
                                );
                              }),
                          // Container(
                          //   height: 30,
                          //   width: double.infinity,
                          //   child: Card(
                          //       color: kDrawerSheetTextColor,
                          //       child: Container(
                          //         child: Center(
                          //             child: Text(
                          //           Languages.of(context)!.Other,
                          //           style: kTagTextBlack,
                          //         )),
                          //       )),
                          // ),
                          // ListView.builder(
                          //     itemCount: data.data!.otherVt!.length,
                          //     shrinkWrap: true,
                          //     physics: NeverScrollableScrollPhysics(),
                          //     itemBuilder: (BuildContext context, int index) {
                          //       return ExpansionTileCard(
                          //         baseColor: kBg,
                          //         expandedColor: kBg,
                          //         title: Text(
                          //           decrypt(data.data!.otherVt![index].name
                          //               .toString()),
                          //           style: kTagBody,
                          //         ),
                          //         subtitle: Text(
                          //           decrypt(data
                          //               .data!.otherVt![index].address
                          //               .toString()),
                          //           style: kTagSubHeading,
                          //         ),
                          //         children: <Widget>[
                          //           const Divider(
                          //             thickness: 1.0,
                          //             height: 1.0,
                          //           ),
                          //           Container(
                          //             color: kTagColor,
                          //             child: ListView.builder(
                          //                 shrinkWrap: true,
                          //                 physics:
                          //                 NeverScrollableScrollPhysics(),
                          //                 itemCount: data
                          //                     .data!
                          //                     .otherVt![index]
                          //                     .members!
                          //                     .length,
                          //                 itemBuilder: (BuildContext context,
                          //                     int indexs) {
                          //                   return Column(
                          //                     children: [
                          //                       RadioListTile<String>(
                          //                           value: data
                          //                               .data!
                          //                               .otherVt![index]
                          //                               .members![indexs]
                          //                               .id
                          //                               .toString(),
                          //                           groupValue: valuemember,
                          //                           //  groupValue: data.data!.neighbourVt![index].selected==true?data.data!.neighbourVt![index].members![indexs].id:valuemember,
                          //                           // groupValue: data
                          //                           //     .data!
                          //                           //     .neighbourVt![index].selected==true
                          //                           //     ?data.data!.neighbourVt![index].id:"",
                          //                           activeColor:
                          //                           kDrawerSheetTextColor,
                          //                           selectedTileColor:
                          //                           kSkipColor,
                          //                           onChanged: (value) {
                          //                             setState(() {
                          //                               valuemember = value;
                          //                             });
                          //                             print(
                          //                                 "radio1 $valuemember");
                          //                           },
                          //                           title: Text(
                          //                               decrypt(data
                          //                                   .data!
                          //                                   .otherVt![index]
                          //                                   .members![indexs]
                          //                                   .name
                          //                                   .toString()),
                          //                               style: kTagTextBlack)),
                          //                       Divider(
                          //                         thickness: 1.0,
                          //                         height: 1.0,
                          //                       ),
                          //                     ],
                          //                   );
                          //                 }),
                          //           )
                          //         ],
                          //       );
                          //     }),
                        ],
                      ),
                    ),
                  ],
                ),
              )
            ],
          ),
          Row(
            children: [
              Expanded(
                flex: 7,
                child: GestureDetector(
                  onTap: () {
                    setState(() {
                      //   idvalue.clear();
                      valuemember = "";
                      // _isChecked =
                      // List<bool>.filled(family_member_list.length, false);
                    });
                  },
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(4, 4, 2, 4),
                    child: Container(
                      height: 40,
                      decoration: BoxDecoration(
                          color: kSendaSurveyColor,
                          borderRadius: BorderRadius.circular(40)),
                      child: Center(
                        child: Padding(
                          padding: EdgeInsets.all(4.0),
                          child: Text(
                            Languages.of(context)!.CLEAR,
                            textAlign: TextAlign.center,
                            style: GoogleFonts.poppins(
                              textStyle: kButtonTextStyle,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              ),
              Expanded(
                flex: 1,
                child: SizedBox(),
              ),
              Expanded(
                flex: 7,
                child: GestureDetector(
                  onTap: () {
                    //  idvalue = [];
                    // idlist.forEach((element) {
                    //   if (element.isNotEmpty) idvalue.add(element);
                    // });
                    // print("id $idvalue");

                    if (valuemember == "") {
                      Fluttertoast.showToast(
                          msg: Languages.of(context)!.Please_select_at_least_one_learner_and_VT,
                          toastLength: Toast.LENGTH_SHORT,
                          gravity: ToastGravity.BOTTOM,
                          timeInSecForIosWeb: 1,
                          textColor: Colors.black,
                          backgroundColor: Colors.white,
                          fontSize: 16.0);
                    } else {
                      flagoffline++;
                      List<String> lsth = [head_id.toString()];
                      Tag(lsth, valuemember!);
                    }
                  },
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(2, 4, 4, 4),
                    child: Container(
                      height: 40,
                      decoration: BoxDecoration(
                          color: kSendaSurveyColor,
                          borderRadius: BorderRadius.circular(40)),
                      child: Center(
                        child: Padding(
                          padding: const EdgeInsets.all(4.0),
                          child: Text(
                            Languages.of(context)!.TAG,
                            textAlign: TextAlign.center,
                            style: GoogleFonts.poppins(
                              textStyle: kButtonTextStyle,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              )
            ],
          ),
        ],
      ),
    ));
  }

  Future<dynamic>? loader() {
    isLoader = true;
    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => Center(
        child: SizedBox(
          height: 50,
          width: 50,
          child: CircularProgressIndicator(
            valueColor:
                new AlwaysStoppedAnimation<Color>(kDrawerSheetTextColor),
            strokeWidth: 7.0,
          ),
        ),
      ),
    );
  }

  Future<bool> _onBackPressed() async {
    return await showDialog(
        barrierDismissible: false,
        context: context,
        builder: (context) {
          return AlertDialog(
            content: Text(Languages.of(context)!.Survey_Dialog),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: Text(Languages.of(context)!.CANCEL),
              ),
              TextButton(
                onPressed: () async {
                  Navigator.pop(context);
                  Navigator.pushReplacement(context,
                      MaterialPageRoute(builder: (context) => SearchEditTag()));
                },
                child: Text(Languages.of(context)!.OK),
              )
            ],
          );
        });
  }

  @override
  void onResponseSuccesses(ResponseEditVtLearner responseDto) {
    // TODO: implement onResponseSuccesses
  }
  @override
  void onTokenExpire() {
    Fluttertoast.showToast(
        msg: Languages.of(context)!.Token_Expired,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    setCredentialslogout();
    Navigator.pushAndRemoveUntil<void>(
        context,
        MaterialPageRoute<void>(builder: (BuildContext context) => Login()),
        ModalRoute.withName('/'));
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

    presenterEditTag.refreshToken(requestRefreshToken.toMap(), signature,
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
      getVTList(head_id);
    });
  }
}
