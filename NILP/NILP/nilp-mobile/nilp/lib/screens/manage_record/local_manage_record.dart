import 'dart:convert';

import 'package:expansion_tile_card/expansion_tile_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/model/request/ResquestLocalMemberList.dart';
import 'package:nilp/model/response/ResponseGetRespondentType.dart';
import 'package:nilp/model/response/ResponseMemberUpdate.dart';
import 'package:nilp/screens/survey/surveyLocalFamilyHeadEdit.dart';
import 'package:nilp/screens/survey/surveyLocalFamilyMemberEdit.dart';
import 'package:nilp/utility/Util.dart';
import 'package:percent_indicator/linear_percent_indicator.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../Database/SquliteDataBaseHelper.dart';
import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/FamilyInfo.dart';
import '../../model/request/RequestRefreshToken.dart';
import '../../model/response/ResponseOfflineRecords.dart';
import '../../model/response/ResponseRefreshToken.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../presenter/PresenterOfflineUpload.dart';
import '../../utility/testEncryption.dart';
import '../dashboard/dashboard.dart';
import '../login/login.dart';

class LocalManageRecord extends StatefulWidget {
  const LocalManageRecord({Key? key}) : super(key: key);

  @override
  _LocalManageRecordState createState() => _LocalManageRecordState();
}

late PresenterOfflineUpload presenterOfflineUpload;

class _LocalManageRecordState extends State<LocalManageRecord>
    implements ResponseContract {
  SquliteDatabaseHelper squlitedb = new SquliteDatabaseHelper();
  Util util = new Util();
  var name;
  int flag = 0;
  List<Family_Head>? lstdata;
  Family_Head? data;

  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
    String? login = perf.getString(LOGIN);
  }

  @override
  void initState() {
    presenterOfflineUpload = new PresenterOfflineUpload(this);
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: kHomeBgColor,
        appBar: AppBar(
          leading: IconButton(
            icon: const Icon(Icons.arrow_back_ios_outlined),
            onPressed: () {
              Navigator.push(context,
                  MaterialPageRoute(builder: (context) => DashBoard()));
            },
          ),
          title: Text(
            Languages.of(context)!.OFFLINE_RECORDS,
            // Languages.of(context)!.MANAGE_RECORDS,
            style: TextStyle(fontSize: 18),
          ),
          backgroundColor: kFillaSurveyColor,
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.only(
                bottomRight: Radius.circular(30),
                bottomLeft: Radius.circular(30)),
          ),
          actions: [
            /*Padding(
              padding: const EdgeInsets.only(right: 8),
              child: IconButton(
                onPressed: () {},
                icon: Icon(
                  Icons.cloud_upload,
                  color: kDrawerSheetTextColor,
                  size: 40,
                ),
              ),
            ),*/
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
          onWillPop: _backpress,
          child: FutureBuilder(
              future: getData(),
              builder: (context, snapshot) {
                if (!snapshot.hasData) {
                  return Center(
                    child: new SizedBox(
                      height: 50.0,
                      width: 50.0,
                      child: new CircularProgressIndicator(
                        valueColor:
                            new AlwaysStoppedAnimation<Color>(Colors.red),
                        strokeWidth: 1.0,
                      ),
                    ),
                  );
                }
                if (lstdata!.length > 0)
                  return getView();
                else {
                  return Center(
                    child: Container(
                      child: Text(Languages.of(context)!.NO_OFFLINE_RECORD_FOUND),
                    ),
                  );
                }
              }),
        ));
  }

  Widget getView() {
    return SafeArea(
        child: Container(
      color: kHomeBgColor,
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(left: 10, right: 10),
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
                          child: TextField(
                            maxLines: 1,
                            autofocus: false,
                            textAlign: TextAlign.start,
                            cursorColor: Colors.black,
                            decoration: InputDecoration(
                                border: InputBorder.none,
                                hintMaxLines: 1,
                                hintText: Languages.of(context)!.Search_Family,
                                hintStyle: TextStyle(fontSize: 13)),
                          ),
                        )),
                    Expanded(
                      flex: 2,
                      child: Container(
                        child: const Padding(
                          padding: EdgeInsets.all(5.0),
                          child: Icon(
                            Icons.search,
                            color: kSendaSurveyColor,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
          Expanded(
            flex: 1,
            child: ListView.builder(
                itemCount: lstdata!.length,
                itemBuilder: (BuildContext context, int index) {
                  return GestureDetector(
                    behavior: HitTestBehavior.opaque,
                    onLongPress: () {},
                    child: ListView(
                      shrinkWrap: true,
                      physics: NeverScrollableScrollPhysics(),
                      children: <Widget>[
                        Padding(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 10,
                            vertical: 10,
                          ),
                          child: ExpansionTileCard(
                            baseColor: kBg,
                            expandedColor: kBg,
                            title: Text(
                              decrypt(lstdata![index].name.toString()),
                              style: kInstructionHeading,
                            ),
                            leading: GestureDetector(
                                onTap: () {
                                  getUpdloadSingleData(lstdata![index]);
                                },
                                child: Icon(
                                  Icons.upload_sharp,
                                  size: 40,
                                  color: kFillaSurveyColor,
                                )),
                            children: <Widget>[
                              Container(
                                height: 50,
                                width: MediaQuery.of(context).size.width,
                                decoration: BoxDecoration(
                                  color: kFillaSurveyColor,
                                ),
                                child: Row(
                                  children: [
                                    Container(
                                        margin: EdgeInsets.only(left: 20),
                                        child: Text(
                                          Languages.of(context)!
                                              .Family_Head_Details,
                                          style: kBodyTextStyle,
                                        )),
                                    Spacer(),
                                    GestureDetector(
                                      onTap: () {
                                        Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                                builder: (context) =>
                                                    SurveyLocalFamilyHeadEdit(
                                                      decrypt(lstdata![index]
                                                          .name
                                                          .toString()),
                                                      decrypt(lstdata![index]
                                                          .address
                                                          .toString()),
                                                      lstdata![index]
                                                          .markAs
                                                          .toString(),
                                                      lstdata![index]
                                                          .age
                                                          .toString(),
                                                      decrypt(lstdata![index]
                                                          .mobile
                                                          .toString()),
                                                      lstdata![index]
                                                          .gender
                                                          .toString(),
                                                      lstdata![index]
                                                          .proofType
                                                          .toString(),
                                                      lstdata![index]
                                                          .proofDetail
                                                          .toString(),
                                                      lstdata![index]
                                                          .socialCategoryId
                                                          .toString(),
                                                      lstdata![index]
                                                          .profession
                                                          .toString(),
                                                      lstdata![index]
                                                          .qualification
                                                          .toString(),
                                                      lstdata![index]
                                                          .vtType
                                                          .toString(),
                                                      lstdata![index]
                                                        .isdivyang.toString(),
                                                      lstdata![index]
                                                          .isOnlyHead
                                                          .toString(),
                                                      lstdata![index].id,

                                                      // data[index].fullname.toString(),
                                                      // data[index].address.toString(),
                                                      // data[index].mobile.toString(),
                                                      // data[index].id.toString(),
                                                      // data[index].age.toString(),
                                                      // data[index].markAs.toString(),
                                                      // data[index].gender.toString(),
                                                      // data[index].proofType.toString(),
                                                      // data[index].socialCategoryId.toString(),
                                                      // data[index].proofDetail.toString()
                                                    )));
                                      },
                                      child: Visibility(
                                        visible: true,
                                        child: Container(
                                            margin: EdgeInsets.only(right: 20),
                                            height: 35,
                                            width: 35,
                                            decoration: BoxDecoration(
                                                borderRadius:
                                                    BorderRadius.circular(20),
                                                color: kDrawerSheetTextColor,
                                                border: Border()),
                                            child: Padding(
                                              padding:
                                                  const EdgeInsets.all(5.0),
                                              child: Icon(Icons.edit, size: 20),
                                            )),
                                      ),
                                    )
                                  ],
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Column(
                                  children: [
                                    Align(
                                        alignment: Alignment.topLeft,
                                        child: Text(
                                          Languages.of(context)!
                                              .Family_Head_Name,
                                          style: kManageProfile,
                                        )),
                                    Align(
                                        alignment: Alignment.topLeft,
                                        child: Text(
                                          decrypt(
                                              lstdata![index].name.toString()),
                                          style: kManageProfileheading,
                                        )),
                                  ],
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Column(
                                  children: [
                                    Align(
                                        alignment: Alignment.topLeft,
                                        child: Text(
                                          Languages.of(context)!.House_Address,
                                          style: kManageProfile,
                                        )),
                                    Align(
                                        alignment: Alignment.topLeft,
                                        child: Text(
                                          decrypt(lstdata![index]
                                              .address
                                              .toString()),
                                          style: kManageProfileheading,
                                        )),
                                  ],
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Column(
                                  children: [
                                    Align(
                                        alignment: Alignment.topLeft,
                                        child: Text(
                                          Languages.of(context)!.Mark_As,
                                          style: kManageProfile,
                                        )),
                                    Align(
                                        alignment: Alignment.topLeft,
                                        child: Text(
                                          getValue(lstdata![index]
                                              .markAs
                                              .toString()),
                                          // lstdata[index].name.toString(),
                                          style: kManageProfileheading,
                                        )),
                                  ],
                                ),
                              ),
                              Visibility(
                                  visible: (lstdata![index]
                                                  .members![0]
                                                  .memberName ==
                                              null ||
                                          lstdata![index].members!.length == 0)
                                      ? false
                                      : true,
                                  child: ListView.builder(
                                      itemCount:
                                          lstdata![index].members!.length,
                                      shrinkWrap: true,
                                      physics: NeverScrollableScrollPhysics(),
                                      itemBuilder:
                                          (BuildContext context, int indexs) {
                                        // Util utils = new Util();
                                        return Column(
                                          children: [
                                            Container(
                                              height: 50,
                                              width: MediaQuery.of(context)
                                                  .size
                                                  .width,
                                              decoration: BoxDecoration(
                                                color: kFillaSurveyColor,
                                              ),
                                              child: Row(
                                                children: [
                                                  Container(
                                                      margin: EdgeInsets.only(
                                                          left: 20),
                                                      child: Text(
                                                        Languages.of(context)!.Family_Member,
                                                        style: kBodyTextStyle,
                                                      )),
                                                  Spacer(),
                                                  GestureDetector(
                                                    onTap: () {
                                                      Navigator.push(
                                                        context,
                                                        MaterialPageRoute(
                                                          builder: (context) {
                                                            return SurveyLocalFamilyMemberEdit(
                                                                lstdata![index]
                                                                    .id,
                                                                decrypt(lstdata![index]
                                                                    .name
                                                                    .toString()),
                                                                decrypt(lstdata![index]
                                                                    .address
                                                                    .toString()),
                                                                lstdata![index]
                                                                    .markAs
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .member_id
                                                                    .toString(),
                                                                decrypt(lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberName
                                                                    .toString()),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberRelation
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberMarkAs
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberFatherName
                                                                    .toString(),
                                                                decrypt(lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberMobile
                                                                    .toString()),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberAge
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberProofDetail
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberProfession
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberQualification
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberVtType
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberGender
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![indexs]
                                                                    .memberProofType
                                                                    .toString(),
                                                                lstdata![index]
                                                                    .members![indexs]
                                                                    .memberSocialCategoryId
                                                                    .toString(),
                                                            lstdata![index]
                                                                .members![
                                                            indexs]
                                                                .memberDivyang);
                                                          },
                                                        ),
                                                      );
                                                    },
                                                    child: Visibility(
                                                      visible: true,
                                                      child: Container(
                                                          margin:
                                                              EdgeInsets.only(
                                                                  right: 20),
                                                          height: 35,
                                                          width: 35,
                                                          decoration: BoxDecoration(
                                                              borderRadius:
                                                                  BorderRadius
                                                                      .circular(
                                                                          20),
                                                              color:
                                                                  kDrawerSheetTextColor,
                                                              border: Border()),
                                                          child: Padding(
                                                            padding:
                                                                const EdgeInsets
                                                                    .all(5.0),
                                                            child: Icon(
                                                                Icons.edit,
                                                                size: 20),
                                                          )),
                                                    ),
                                                  )
                                                ],
                                              ),
                                            ),
                                            Column(
                                              children: [
                                                Padding(
                                                  padding:
                                                      const EdgeInsets.all(8.0),
                                                  child: Column(
                                                    children: [
                                                      Align(
                                                          alignment:
                                                              Alignment.topLeft,
                                                          child: Text(
                                                            Languages.of(
                                                                    context)!
                                                                .Full_Name,
                                                            style:
                                                                kManageProfile,
                                                          )),
                                                      Align(
                                                          alignment:
                                                              Alignment.topLeft,
                                                          child: Text(
                                                            decrypt(lstdata![
                                                                    index]
                                                                .members![
                                                                    indexs]
                                                                .memberName
                                                                .toString()),
                                                            style:
                                                                kManageProfileheading,
                                                          )),
                                                    ],
                                                  ),
                                                ),
                                                Padding(
                                                  padding:
                                                      const EdgeInsets.all(8.0),
                                                  child: Column(
                                                    children: [
                                                      Align(
                                                          alignment:
                                                              Alignment.topLeft,
                                                          child: Text(
                                                            Languages.of(
                                                                    context)!
                                                                .Father_Name,
                                                            style:
                                                                kManageProfile,
                                                          )),
                                                      Align(
                                                          alignment:
                                                              Alignment.topLeft,
                                                          child: Text(
                                                            lstdata![index]
                                                                .members![
                                                                    indexs]
                                                                .memberFatherName
                                                                .toString(),
                                                            style:
                                                                kManageProfileheading,
                                                          )),
                                                    ],
                                                  ),
                                                ),
                                                Row(
                                                  mainAxisAlignment:
                                                      MainAxisAlignment.start,
                                                  children: [
                                                    Expanded(
                                                      flex: 1,
                                                      child: Padding(
                                                        padding:
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            Text(
                                                              Languages.of(
                                                                      context)!
                                                                  .Mark_As,
                                                              style:
                                                                  kManageProfile,
                                                            ),
                                                            Text(
                                                              lstdata![index]
                                                                          .members![
                                                                              indexs]
                                                                          .memberMarkAs
                                                                          .toString() ==
                                                                      LEARNER
                                                                  ? Languages.of(
                                                                          context)!
                                                                      .Learner
                                                                  : Languages.of(
                                                                          context)!
                                                                      .VT,
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
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            Text(
                                                              Languages.of(context)!.Select_Age,
                                                              style:
                                                                  kManageProfile,
                                                            ),
                                                            Text(
                                                              lstdata![index]
                                                                  .members![
                                                                      indexs]
                                                                  .memberAge
                                                                  .toString(),
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
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            Text(
                                                              Languages.of(
                                                                      context)!
                                                                  .Mobile_Number,
                                                              style:
                                                                  kManageProfile,
                                                            ),
                                                            Text(
                                                              decrypt(lstdata![
                                                                      index]
                                                                  .members![
                                                                      indexs]
                                                                  .memberMobile
                                                                  .toString()),
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
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            Text(
                                                              Languages.of(
                                                                      context)!
                                                                  .Gender,
                                                              style:
                                                                  kManageProfile,
                                                            ),
                                                            Text(
                                                              lstdata![index]
                                                                          .members![
                                                                              indexs]
                                                                          .memberGender
                                                                          .toString() ==
                                                                      "m"
                                                                  ? "Male"
                                                                  : "Female",
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
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            Text(
                                                              Languages.of(
                                                                      context)!
                                                                  .Proof_of_Identity_Type,
                                                              style:
                                                                  kManageProfile,
                                                            ),
                                                            Text(
                                                              util.getIdentityValue(
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberProofType
                                                                    .toString(),
                                                              ),
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
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            Text(
                                                              Languages.of(
                                                                      context)!
                                                                  .ID_Number,
                                                              style:
                                                                  kManageProfile,
                                                            ),
                                                            Text(
                                                              lstdata![index]
                                                                  .members![
                                                                      indexs]
                                                                  .memberProofDetail
                                                                  .toString(),
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
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            Text(
                                                              Languages.of(
                                                                      context)!
                                                                  .Social_Category,
                                                              style:
                                                                  kManageProfile,
                                                            ),
                                                            Text(
                                                              util.getSocialCategory(
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberSocialCategoryId
                                                                    .toString(),
                                                              ),
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
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          crossAxisAlignment:
                                                              CrossAxisAlignment
                                                                  .start,
                                                          children: [
                                                            Text(
                                                              Languages.of(
                                                                      context)!
                                                                  .Profession,
                                                              style:
                                                                  kManageProfile,
                                                            ),
                                                            Text(
                                                              util.getProfession(
                                                                lstdata![index]
                                                                    .members![
                                                                        indexs]
                                                                    .memberProfession
                                                                    .toString(),
                                                              ),
                                                              style:
                                                                  kManageProfileheading,
                                                            )
                                                          ],
                                                        ),
                                                      ),
                                                    )
                                                  ],
                                                ),
                                              ],
                                            ),
                                          ],
                                        );
                                      }))
                            ],
                          ),
                        ),
                      ],
                    ),
                  );
                }),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: GestureDetector(
              onTap: () {
                uploadRecord();
              },
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
                            Languages.of(context)!.UPLOAD_RECORDS,
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
                        ],
                      ),
                    ),
                  )
                ],
              ),
            ),
          ),
        ],
      ),
    ));
  }

  getData() async {
    final db = await squlitedb;
    lstdata = await db.getUser();
    return lstdata;
  }

  String getValue(String mark) {
    if (mark == "0") {
      if(Util.currentLocal=="hin"){
        return "कोई भी नहीं";
      }
      else{
        return "None";
      }
    } else if (mark == "8ecf3868-03b7-4ea1-8520-e619d793ed7d") {
      if(Util.currentLocal=="hin"){
        return "स्वयंसेवी";
      }
      else{
        return "VT";
      }
    } else if (mark == "8ca80f73-afd2-4d3f-b41d-e5bea0254b98") {
      if(Util.currentLocal=="hin"){
        return "शिक्षार्थी";
      }
      else{
        return "Learner";
      }
    } else {
      if(Util.currentLocal=="hin"){
        return "कोई भी नहीं";
      }
      else{
        return "None";
      }
    }
  }

  bool getVisibilty(int index) {
    var len = lstdata![index].members?.length ?? 0;
    if (len == 0)
      return false;
    else
      return true;
  }

  Future<bool> _backpress() async {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => DashBoard()));
    return await true;
  }

  void uploadRecord() async {
    Util util = new Util();
    bool isOnline = await util.hasInternet();
    if (isOnline) {
      int i = 1;
      for (var item in lstdata!) {
        await addMember(item!);
        i++;
      }
      if (flag == 0) {
        Navigator.pop(context);
        Navigator.push(
            context, MaterialPageRoute(builder: (context) => DashBoard()));
        Fluttertoast.showToast(
            msg: Languages.of(context)!.Records_uploaded_successfully,
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIosWeb: 1,
            textColor: Colors.black,
            backgroundColor: Colors.white,
            fontSize: 16.0);
      }
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

  Future<ResponseOfflineRecords?> addMember(Family_Head family_head) async {
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    List<Member> mem = [];
    ResquestLocalMemberList request;
    if (family_head.isOnlyHead == 0) {
      for (var m in family_head.members!) {
        Member members = new Member();
        members.memberId = m.member_id;
        members.memberFullname = m.memberName;
        members.memberMarkAs = m.memberMarkAs;
        members.memberAddress = m.memberAddress;
        members.memberAge = m.memberAge;
        members.memberMobile = m.memberMobile.toString();
        members.memberGender = m.memberGender;
        members.memberFatherName = m.memberFatherName;
        members.memberRelation = m.memberRelation;
        members.memberProofType = m.memberProofType;
        members.memberProofDetail = m.memberProofDetail;
        members.memberSocialCategoryId = m.memberSocialCategoryId;
        members.member_profession = m.memberProfession;
        members.memberQualification = m.memberQualification;
        members.memberVtType = m.memberVtType;
        members.memberDivyang = m.memberDivyang==0?false:true;
        mem.add(members);
      }

      request = new ResquestLocalMemberList(
          markAs: family_head.markAs.toString(),
          id: family_head.id,
          fullname: (family_head.name.toString()),
          address: (family_head.address.toString()),
          age: int.parse(family_head.age!),
          mobile: (family_head.mobile.toString()),
          gender: family_head.gender.toString(),
          proofDetail: family_head.proofDetail.toString(),
          proofType: family_head.proofType.toString(),
          socialCategoryId: family_head.socialCategoryId.toString(),
          vtType: family_head.vtType.toString(),
          qualification: family_head.qualification.toString(),
          profession: family_head.profession.toString(),
          isOnlyMember: family_head.isOnlyHead == 0 ? false : true,
          isdivyang: family_head.isdivyang == 0 ? false : true,
          members: mem);
    } else {
      Member members = new Member();
      members.memberId = null;
      members.memberFullname = null;
      members.memberMarkAs = null;
      members.memberAddress = null;
      members.memberAge = null;
      members.memberMobile = null;
      members.memberGender = null;
      members.memberFatherName = null;
      members.memberRelation = null;
      members.memberProofType = null;
      members.memberProofDetail = null;
      members.memberSocialCategoryId = null;
      members.member_profession = null;
      members.memberQualification = null;
      members.memberVtType = null;
      members.memberDivyang=null;
      mem.add(members);
      request = new ResquestLocalMemberList(
        markAs: family_head.markAs.toString(),
        id: family_head.id,
        fullname: (family_head.name.toString()),
        address: (family_head.address.toString()),
        age: int.parse(family_head.age!),
        mobile: (family_head.mobile.toString()),
        gender: family_head.gender.toString(),
        proofDetail: family_head.proofDetail.toString(),
        proofType: family_head.proofType.toString(),
        socialCategoryId: family_head.socialCategoryId.toString(),
        vtType: family_head.vtType.toString(),
        qualification: family_head.qualification.toString(),
        profession: family_head.profession.toString(),
        isOnlyMember: family_head.isOnlyHead == 0 ? false : true,
        isdivyang: family_head.isdivyang == 0 ? false : true,
        members: mem,
      );
    }
    String url = BASE_URL + MULTIPLE_MEMBER;
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), request.toJson());
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    Util util = new Util();
    bool isOnline = await util.hasInternet();
    if (isOnline) {
      loader();
      return presenterOfflineUpload.addMember(request.toJson(), signature,
          timestamp.toString(), MULTIPLE_MEMBER, context, acesstoken);
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

  Future<dynamic>? loader() {
    isLoader = true;
    return showDialog(
      context: context,
      barrierDismissible: false,
      builder: (context) => Center(
        child: LinearPercentIndicator(
          alignment: MainAxisAlignment.center,
          width: MediaQuery.of(context).size.width - 50,
          animation: true,
          lineHeight: 20.0,
          animationDuration: 2000,
          percent: 0.9,
          center: Text(Languages.of(context)!.Uploading_Records),
          linearStrokeCap: LinearStrokeCap.roundAll,
          progressColor: kDrawerSheetTextColor,
        ),
      ),
    );
  }

  @override
  void onError(String errorTxt, String code) {
    Navigator.pop(context);
    Fluttertoast.showToast(
        msg: errorTxt,
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
  void onResponseAddMember(ResponseOfflineRecords responseDto) async {
    flag = 0;
    if (responseDto.data!.status!) {
      final db = await squlitedb;
      if (responseDto.data!.member!.length > 0) {
        for (int i = 0; i <= responseDto.data!.member!.length - 1; i++) {
          if (responseDto.data!.member![0] != "null") {
            await db.deleteMemberUsers(int.parse(responseDto.data!.member![i]));
          } else {}
        }
      }

      await db.deleteHeadUsers(int.parse(responseDto.data!.head!));
      /* Navigator.pop(context);
      Navigator.push(
          context, MaterialPageRoute(builder: (context) => DashBoard()));
      Fluttertoast.showToast(
          msg: responseDto.data!.updateSuccessMessage!,
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);*/

      print(responseDto.data!.updateSuccessMessage!);
    } else {
      final db = await squlitedb;
      if (responseDto.data!.member!.length > 0) {
        for (int i = 0; i <= responseDto.data!.member!.length; i++) {
          await db.deleteMemberUsers(int.parse(responseDto.data!.member![i]));
        }
      }
      await db.deleteHeadUsers(int.parse(responseDto.data!.head!));

/*      Navigator.pop(context);
      Fluttertoast.showToast(
          msg: responseDto.data!.updateErrorMessage!,
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);*/
    }
  }

  @override
  void onResponseSuccess(ResponseGetRespondentType responseDto) {
    // TODO: implement onResponseSuccess
  }

  @override
  void onResponseSuccessFav() {
    // TODO: implement onResponseSuccessFav
  }

  @override
  void onResponseUpateMember(ResponseMemberUpdate responseDto1) {
    // TODO: implement onResponseUpateMember
  }

  void getUpdloadSingleData(uploadUserId) async {
    Util util = new Util();
    bool isOnline = await util.hasInternet();
    if (isOnline) {
      await addMember(uploadUserId!);
      if (flag == 0) {
        Navigator.pop(context);
        Fluttertoast.showToast(
            msg: Languages.of(context)!.Records_uploaded_successfully,
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIosWeb: 1,
            textColor: Colors.black,
            backgroundColor: Colors.white,
            fontSize: 16.0);
        Navigator.push(
            context, MaterialPageRoute(builder: (context) => DashBoard()));
      }
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
  void onTokenExpire() {
    setCredentialslogout();
    Fluttertoast.showToast(
        msg: utf8.decode(TOKEN_EXPIRED.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
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

    presenterOfflineUpload.refreshToken(requestRefreshToken.toMap(), signature,
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
      //  addMember(uploadUserId);
    });
  }

  @override
  void onResponseAlReadyAdd(ResponseOfflineRecords responseDto) {
    Navigator.pop(context);
    Fluttertoast.showToast(
        msg: utf8.decode(responseDto.data!.updateErrorMessage!.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    flag = 1;
  }
}
