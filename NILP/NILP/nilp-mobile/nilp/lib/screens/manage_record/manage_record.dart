import 'package:expansion_tile_card/expansion_tile_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:nilp/model/request/RequestSearchMember.dart';
import 'package:nilp/model/response/ResponseRefreshToken.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/request/RequestLearnerList.dart';
import '../../model/request/RequestRefreshToken.dart';
import '../../model/response/ResponseSearchMember.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../presenter/PresenterGetMemberList.dart';
import '../../utility/Util.dart';
import '../../utility/testEncryption.dart';
import '../dashboard/dashboard.dart';
import '../login/login.dart';
import '../survey/surveyFamilyHeadEdit.dart';
import '../survey/surveyFamilyMemberEdit.dart';
import 'local_manage_record.dart';

class ManageRecord extends StatefulWidget {
  const ManageRecord({Key? key}) : super(key: key);

  @override
  _ManageRecordState createState() => _ManageRecordState();
}

class _ManageRecordState extends State<ManageRecord>
    implements ResponseContract {
  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
  }

  List<Map> slist = [];
  List<Datum> data = [];
  late PresenterGetMemberList presenterGetMemberDetails;
  late List<Map> memberList;
  late String newMemberList;
  int ?flag;
  TextEditingController txt_search = TextEditingController();

  getLocalData() async {}

  @override
  void initState() {
    presenterGetMemberDetails = new PresenterGetMemberList(this);
      getLernerList();
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
          title: Text(Languages.of(context)!.MANAGE_RECORDS,
              style: TextStyle(fontSize: 18)),
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
        body: GestureDetector(
          onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
          child: SafeArea(
              child: WillPopScope(
            onWillPop: _onBackPressed,
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
                                    onChanged: onItemChanged,
                                    maxLines: 1,
                                    onEditingComplete: searchRecord,
                                    controller: txt_search,
                                    autofocus: false,
                                    textAlign: TextAlign.start,
                                    cursorColor: Colors.black,
                                    decoration: InputDecoration(
                                        border: InputBorder.none,
                                        hintText: Languages.of(context)!
                                            .Search_Family,
                                        hintStyle: TextStyle(fontSize: 13)),
                                  ),
                                )),
                            Expanded(
                              flex: 2,
                              child: GestureDetector(
                                onTap: () {
                                  if (txt_search.text.trim() == "") {
                                    _showToast(Languages.of(context)!.Enter_text_to_search);
                                  } else {
                                    if (txt_search.text.length<=2) {
                                      _showToast(Languages.of(context)!.Please_enter_at_least_3_characters);
                                    }else{
                                      getMembeDetails(txt_search.text.trim());
                                    }
                                  }
                                },
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
                            ),
                          ],
                        ),
                      ),
                    ),
                  ),
                  Expanded(
                    flex: 1,
                    child: ListView.builder(
                        itemCount:data.length,
                        itemBuilder: (BuildContext context, int index) {
                          return GestureDetector(
                            behavior: HitTestBehavior.opaque,
                            onLongPress: () {},
                            child: ListView(
                              shrinkWrap: true,
                              physics: NeverScrollableScrollPhysics(),
                              children: [
                                Padding(
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: 10,
                                    vertical: 10,
                                  ),
                                  child: ExpansionTileCard(
                                    baseColor: kBg,
                                    expandedColor: kBg,
                                    title: Text(
                                      decrypt(data[index].fullname.toString()),
                                      style: kInstructionHeading,
                                    ),
                                    subtitle: Visibility(
                                      visible: getVisibilty(index),
                                      child: Text(
                                        data[index].members!.length.toString() +" "+
                                            Languages.of(context)!.Members,
                                        style: kManageProfileheading,
                                      ),
                                    ),
                                    children: [
                                      Container(
                                        height: 50,
                                        width:
                                            MediaQuery.of(context).size.width,
                                        decoration: BoxDecoration(
                                          color: kFillaSurveyColor,
                                        ),
                                        child: Row(
                                          children: [
                                            Container(
                                                margin:
                                                    EdgeInsets.only(left: 20),
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
                                                            SurveyFamilyHeadEdit(
                                                              data[index]
                                                                  .fullname
                                                                  .toString(),
                                                              data[index]
                                                                  .address
                                                                  .toString(),
                                                              data[index]
                                                                  .mobile
                                                                  .toString(),
                                                              data[index]
                                                                  .id
                                                                  .toString(),
                                                              data[index]
                                                                  .age
                                                                  .toString(),
                                                              data[index]
                                                                  .markAs
                                                                  .toString(),
                                                              data[index]
                                                                  .gender
                                                                  .toString(),
                                                              data[index]
                                                                  .proofType
                                                                  .toString(),
                                                              data[index]
                                                                  .socialCategoryId
                                                                  .toString(),
                                                              data[index]
                                                                  .profession
                                                                  .toString(),
                                                              data[index]
                                                                  .proofDetail
                                                                  .toString(),
                                                              data[index]
                                                                  .qualification
                                                                  .toString(),
                                                              data[index]
                                                                  .memberType
                                                                  .toString(),
                                                              data[index]
                                                                  .vt_type
                                                                  .toString(),
                                                              data[index]
                                                                  .is_divyang,
                                                            )));
                                              },
                                              child: Visibility(
                                                visible: true,
                                                child: Container(
                                                    margin: EdgeInsets.only(
                                                        right: 20),
                                                    height: 35,
                                                    width: 35,
                                                    decoration: BoxDecoration(
                                                        borderRadius:
                                                            BorderRadius
                                                                .circular(20),
                                                        color:
                                                            kDrawerSheetTextColor,
                                                        border: Border()),
                                                    child: Padding(
                                                      padding:
                                                          const EdgeInsets.all(
                                                              5.0),
                                                      child: Icon(Icons.edit,
                                                          size: 20),
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
                                                  decrypt(data[index]
                                                      .fullname
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
                                                  Languages.of(context)!
                                                      .House_Address,
                                                  style: kManageProfile,
                                                )),
                                            Align(
                                                alignment: Alignment.topLeft,
                                                child: Text(
                                                  setAddress(data[index]
                                                      .address
                                                      .toString()),
                                                  // decrypt(data[index]
                                                  //     .address
                                                  //     .toString()),
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
                                                  getValue(data[index]
                                                      .markAs
                                                      .toString()),
                                                  style: kManageProfileheading,
                                                )),
                                          ],
                                        ),
                                      ),
                                      Visibility(
                                        visible: (data[index].members.isEmpty ||
                                                data[index].members.length == 0)
                                            ? false
                                            : true,
                                        child: ListView.builder(
                                            itemCount:
                                                data[index].members.length,
                                            shrinkWrap: true,
                                            physics:
                                                NeverScrollableScrollPhysics(),
                                            itemBuilder: (BuildContext context,
                                                int indexs) {
                                              Util utils = new Util();
                                              return Column(
                                                children: [
                                                  Container(
                                                    height: 50,
                                                    width:
                                                        MediaQuery.of(context)
                                                            .size
                                                            .width,
                                                    decoration: BoxDecoration(
                                                      color: kFillaSurveyColor,
                                                    ),
                                                    child: Row(
                                                      children: [
                                                        Container(
                                                            margin:
                                                                EdgeInsets.only(
                                                                    left: 20),
                                                            child: Text(
                                                              Languages.of(context)!.Family_Member,
                                                              style:
                                                                  kBodyTextStyle,
                                                            )),
                                                        Spacer(),
                                                        GestureDetector(
                                                          onTap: () {
                                                            Navigator.push(
                                                                context,
                                                                MaterialPageRoute(
                                                                    builder:
                                                                        (context) =>
                                                                            SurveyFamilyMemberEdit(
                                                                              data[index].id.toString(),
                                                                              data[index].fullname.toString(),
                                                                              data[index].address.toString(),
                                                                              data[index].markAs.toString(),
                                                                              data[index].members[indexs].id.toString(),
                                                                              data[index].members[indexs].fullname.toString(),
                                                                              data[index].members[indexs].relation,
                                                                              data[index].members[indexs].markAs.toString(),
                                                                              data[index].members[indexs].fatherName.toString(),
                                                                              data[index].members[indexs].mobile.toString(),
                                                                              data[index].members[indexs].age.toString(),
                                                                              data[index].members[indexs].proofDetail.toString(),
                                                                              data[index].members[indexs].profession,
                                                                              data[index].members[indexs].qualification,
                                                                              data[index].members[indexs].vt_type,
                                                                              data[index].members[indexs].gender.toString(),
                                                                              data[index].members[indexs].proofType,
                                                                              data[index].members[indexs].socialCategoryId,
                                                                              data[index].members[indexs].is_divyang,
                                                                            )));
                                                          },
                                                          child: Visibility(
                                                            visible: true,
                                                            child: Container(
                                                                margin: EdgeInsets
                                                                    .only(
                                                                        right:
                                                                            20),
                                                                height: 35,
                                                                width: 35,
                                                                decoration: BoxDecoration(
                                                                    borderRadius:
                                                                        BorderRadius.circular(
                                                                            20),
                                                                    color:
                                                                        kDrawerSheetTextColor,
                                                                    border:
                                                                        Border()),
                                                                child: Padding(
                                                                  padding:
                                                                      const EdgeInsets
                                                                              .all(
                                                                          5.0),
                                                                  child: Icon(
                                                                      Icons
                                                                          .edit,
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
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          children: [
                                                            Align(
                                                                alignment:
                                                                    Alignment
                                                                        .topLeft,
                                                                child: Text(
                                                                  Languages.of(
                                                                          context)!
                                                                      .Full_Name,
                                                                  style:
                                                                      kManageProfile,
                                                                )),
                                                            Align(
                                                                alignment:
                                                                    Alignment
                                                                        .topLeft,
                                                                child: Text(
                                                                  decrypt(data[
                                                                          index]
                                                                      .members[
                                                                          indexs]
                                                                      .fullname
                                                                      .toString()),
                                                                  style:
                                                                      kManageProfileheading,
                                                                )),
                                                          ],
                                                        ),
                                                      ),
                                                      Padding(
                                                        padding:
                                                            const EdgeInsets
                                                                .all(8.0),
                                                        child: Column(
                                                          children: [
                                                            Align(
                                                                alignment:
                                                                    Alignment
                                                                        .topLeft,
                                                                child: Text(
                                                                  Languages.of(
                                                                          context)!
                                                                      .Father_Name,
                                                                  style:
                                                                      kManageProfile,
                                                                )),
                                                            Align(
                                                                alignment:
                                                                    Alignment
                                                                        .topLeft,
                                                                child: Text(
                                                                  data[index]
                                                                      .members[
                                                                          indexs]
                                                                      .fatherName
                                                                      .toString(),
                                                                  style:
                                                                      kManageProfileheading,
                                                                )),
                                                          ],
                                                        ),
                                                      ),
                                                      Row(
                                                        mainAxisAlignment:
                                                            MainAxisAlignment
                                                                .start,
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
                                                                    data[index].members[indexs].markAs ==
                                                                            LEARNER
                                                                        ? Languages.of(context)!
                                                                            .Learner
                                                                        : Languages.of(context)!
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
                                                                    data[index]
                                                                        .members[
                                                                            indexs]
                                                                        .age
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
                                                            MainAxisAlignment
                                                                .start,
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
                                                                    decrypt(data[
                                                                            index]
                                                                        .members[
                                                                            indexs]
                                                                        .mobile
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
                                                                    getMemberGender(data[
                                                                            index]
                                                                        .members[
                                                                            indexs]
                                                                        .gender
                                                                        .toString()),
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
                                                            MainAxisAlignment
                                                                .start,
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
                                                                    utils.getIdentityValue(data[
                                                                            index]
                                                                        .members[
                                                                            indexs]
                                                                        .proofType
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
                                                                        .ID_Number,
                                                                    style:
                                                                        kManageProfile,
                                                                  ),
                                                                  Text(
                                                                    data[index]
                                                                        .members[
                                                                            indexs]
                                                                        .proofDetail
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
                                                                    utils
                                                                        .getSocialCategory(
                                                                      data[index]
                                                                          .members[
                                                                              indexs]
                                                                          .socialCategoryId
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
                                                                    utils
                                                                        .getProfession(
                                                                      data[index]
                                                                          .members[
                                                                              indexs]
                                                                          .profession
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
                                            }),
                                      )
                                    ],
                                  ),
                                ),
                              ],
                            ),
                          );
                        }),

                    //                       const Expanded(flex: 2,
                    //                         child: Card(
                    //                           shape: RoundedRectangleBorder(
                    //                               borderRadius: BorderRadius.all(Radius.circular(50)),
                    //                               side: BorderSide(width: 3, color: kSendaSurveyColor)),
                    //                           //color: kFillaSurveyColor,
                    //                           child:Padding(
                    //                             padding: EdgeInsets.all(8.0),
                    //                             child: Icon(Icons.upload_rounded,color: kSendaSurveyColor,
                    //                               size: 25,),
                    //                           ),
                    //
                    //                         ),
                    //                       ),
                  ),
                ],
              ),
            ),
          )),
        ));
  }

  Future<ResponseSearchMember?> getMembeDetails(String value) async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    RequestSearchMember requestSearchMember =
        new RequestSearchMember(search: value.trim(),page: 1,size: 5);

    String url = BASE_URL + GET_MEMBER_DETAILS;

    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), requestSearchMember.toMap());
    Util util = new Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      loader();
      return presenterGetMemberDetails.getMembeDetails(
          requestSearchMember.toMap(),
          signature,
          timestamp.toString(),
          GET_MEMBER_DETAILS,
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
          MaterialPageRoute<void>(builder: (BuildContext context) => Login()),
          ModalRoute.withName('/'));
    }
  }

  void _showToast(String msg) {
    Fluttertoast.showToast(
        msg: msg,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
  }

  @override
  void onResponseSuccess(ResponseSearchMember responseDto) {
    Navigator.pop(context);
    data.clear();
    if (responseDto.data.length > 0) {
      setState(() {
        data = responseDto.data;
      });
      FocusManager.instance.primaryFocus?.unfocus();
    } else {
      setState(() {
        data.clear();
      });
      Fluttertoast.showToast(
          msg: Languages.of(context)!.NO_RECORD_FOUND,
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
    }
    // TODO: implement onResponseSuccess
  }

  void onItemChanged(String value) {
    // getMembeDetails(value);
  }

  String getValue(String mark) {
    if (mark == "0") {
      if(Util.currentLocal=="hin"){
        return "कोई भी नहीं";
      }else{
        return "None";
      }
    } else if (mark == VT) {
      if(Util.currentLocal=="hin"){
        return "स्वयंसेवी";
      }else{
        return "VT";
      }
    } else if (mark == LEARNER) {
      if(Util.currentLocal=="hin"){
        return "शिक्षार्थी";
      }else{
        return "Learner";
      }
    } else {
      return "None";
    }
  }

  String getMemberGender(String gender) {
    if (gender == "m") {
      return "Male";
    }
    if (gender == "f") {
      return "Female";
    } else {
      return "Transgender";
    }
  }

  @override
  void onResponseLSuccess(ResponseSearchMember responseDto) {
    // TODO: implement onResponseLSuccess
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

  getVisibilty(int index) {
    if (data![index].members == null ||
        !data![index].members.isEmpty ||
        data![index].members.length > 0) {
      return true;
    } else {
      return false;
    }
  }

  String setAddress(String add) {
    if (add == "")
      return "";
    else
      return decrypt(add.toString());
  }

  searchRecord() {
    if (txt_search.text.trim() == "") {
      _showToast(Languages.of(context)!.Enter_text_to_search);
    }
    else if (txt_search.text.length<=2) {
      _showToast(Languages.of(context)!.Please_enter_at_least_3_characters);
    }
    else {
      flag==1;
      getMembeDetails(txt_search.text);
    }
  }

  void getUpdloadSingleData() async {
    Fluttertoast.showToast(
        msg: Languages.of(context)!.NO_RECORD_FOUND,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
  }

  Future<bool> _onBackPressed() async {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => DashBoard()));
    return await true;
  }

  @override
  void onTokenExpire() {
    setCredentialslogout();
    Fluttertoast.showToast(
        msg: Languages.of(context)!.Token_Expired,
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

    presenterGetMemberDetails.refreshToken(requestRefreshToken.toMap(),
        signature, timestamp.toString(), REFRESH_TOKEN);
  }

  @override
  Future<void> onResponseRefreshSuccess(
      ResponseRefreshToken responseDto) async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    pref.setString(LOGIN, "true");
    pref.setString(ACESSTOKEN, encrypt(responseDto.data.accessToken!).base64);
    pref.setString(REFRESHTOKEN, responseDto.data.refreshToken);

    setState(() {
      getMembeDetails(txt_search.text.trim());
    });
  }

  Future<ResponseSearchMember?> getLernerList() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    RequestLearnerList requestSearchMember =
    new RequestLearnerList(search: "",type: "",page: 1,size:200);

    String url = BASE_URL + GET_MEMBER_DETAILS;

    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), requestSearchMember.toJson());
    Util util = new Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      loader();
      return presenterGetMemberDetails.getMembeDetails(
          requestSearchMember.toJson(),
          signature,
          timestamp.toString(),
          GET_MEMBER_DETAILS,
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
