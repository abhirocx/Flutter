import 'dart:convert';

import 'package:expansion_tile_card/expansion_tile_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/model/response/ResponseEditVt.dart';
import 'package:nilp/model/response/ResponseTagging.dart';
import 'package:nilp/screens/manage_tag/manage_tag.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../../constants/Strings.dart';
import '../../../constants/UrlEndPoints.dart';
import '../../../constants/constants.dart';
import '../../../localization/language/languages.dart';
import '../../../model/request/RequestEditVt.dart';
import '../../../model/request/RequestRefreshToken.dart';
import '../../../model/response/ResponseEditVTLearner.dart';
import '../../../model/response/ResponseRefreshToken.dart';
import '../../../networking/AppSignatureUtil.dart';
import '../../../presenter/PresenterEditVT.dart';
import '../../../utility/Util.dart';
import '../../../utility/testEncryption.dart';
import '../../dashboard/dashboard.dart';
import '../../login/login.dart';
import '../../manage_record/local_manage_record.dart';
import 'editTag.dart';

class SearchEditTag extends StatefulWidget {
  const SearchEditTag({Key? key}) : super(key: key);

  @override
  _SearchEditTagState createState() => _SearchEditTagState();
}

class _SearchEditTagState extends State<SearchEditTag>
    implements ResponseContract {
  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
    String? login = perf.getString(LOGIN);
  }

  late PresenterEditTag presenterGetMemberDetails;
  ResponseEditVtLearner? responseEditVtLearner;
  List <Datum>? data;
  int flag = 0;
  TextEditingController txt_search = TextEditingController();

  @override
  void initState() {
    presenterGetMemberDetails = new PresenterEditTag(this);
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
                  MaterialPageRoute(builder: (context) => ManageTag()));
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
          child: GestureDetector(
            onTap: () => FocusManager.instance.primaryFocus?.unfocus(),
            child: SafeArea(
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
                                    // onChanged: onItemChanged,
                                    controller: txt_search,
                                    maxLines: 1,
                                    maxLength: 16,
                                    onEditingComplete: searchText,
                                    autofocus: false,
                                    textAlign: TextAlign.start,
                                    cursorColor: Colors.black,
                                    decoration: InputDecoration(
                                        counterText: "",
                                        border: InputBorder.none,
                                        fillColor: Colors.grey[100],
                                        focusColor: Colors.grey[100],
                                        hintMaxLines: 1,
                                        hintText: Languages.of(context)!
                                            .Search_MSG_EditTag,
                                        hintStyle: TextStyle(fontSize: 13)),
                                  ),
                                )),
                            Expanded(
                              flex: 2,
                              child: GestureDetector(
                                onTap: () {
                                  setState((){
                                   if (txt_search.text.trim() == "") {
                                     _showToast(Languages.of(context)!.Enter_text_to_search);
                                   }
                                   else {
                                     if (txt_search.text.length<=2) {
                                       _showToast(Languages.of(context)!.Please_enter_at_least_3_characters);
                                     }else{

                                       getMembeDetails(txt_search.text);

                                     }
                                   }
                                 });
                                  //getMembeDetails(txt_search.text);
                                },
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
                  Visibility(
                    visible: flag == 1 ? true : false,
                    child: Container(
                      height: 80,
                      width: double.infinity,
                      child: Padding(
                        padding: const EdgeInsets.fromLTRB(10, 5, 10, 0),
                        child: Card(
                          shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.only(
                                  topLeft: Radius.circular(10),
                                  topRight: Radius.circular(10))),
                          color: kFillaSurveyColor,
                          elevation: 8,
                          child: Container(
                            margin: EdgeInsets.only(
                              top: 5,
                              bottom: 5,
                            ),
                            child: Padding(
                              padding: EdgeInsets.fromLTRB(20, 3, 3, 3),
                              child: Center(
                                  child: Text(
                                Languages.of(context)!.Search_Result_Found,
                                style: kHeadingTextStyle,
                              )),
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  Visibility(
                    visible: getVisibilty(),
                    child: Expanded(
                      flex: 1,
                      child: ListView.builder(
                          itemCount:flag == 1?data!.length:0,
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
                                            decrypt(data![index].name.toString()),
                                            style: kInstructionHeading,
                                          ),
                                          subtitle: Text(
                                            decrypt(data![index].address.toString()),
                                            style: kManageProfileheadings,
                                          ),
                                          children: <Widget>[
                                            const Divider(
                                              thickness: 1.0,
                                              height: 1.0,
                                            ),
                                            Column(
                                              children: [
                                                Padding(
                                                  padding:
                                                      const EdgeInsets.all(8),
                                                  child: Row(
                                                    children: [
                                                      Expanded(
                                                        child: GestureDetector(
                                                          onTap: () {
                                                            sendDatatoTag(index);
                                                          },
                                                          child: Container(
                                                            height: 50,
                                                            width: 50,
                                                            decoration: BoxDecoration(
                                                                color:
                                                                    kDrawerSheetTextColor,
                                                                borderRadius:
                                                                    BorderRadius
                                                                        .circular(
                                                                            40)),
                                                            child: Center(
                                                              child: Padding(
                                                                padding:
                                                                    const EdgeInsets
                                                                            .all(
                                                                        4.0),
                                                                child: Text(
                                                                  Languages.of(
                                                                      context)!
                                                                      .Edit_tagging,
                                                                 // "EDIT TAGGING",
                                                                  textAlign:
                                                                      TextAlign
                                                                          .center,
                                                                  style: GoogleFonts
                                                                      .poppins(
                                                                    textStyle:
                                                                        kSurveySentSuccess,
                                                                  ),
                                                                ),
                                                              ),
                                                            ),
                                                          ),
                                                        ),
                                                      )
                                                    ],
                                                  ),
                                                )
                                              ],
                                            ),
                                          ])),
                                ],
                              ),
                            );
                          }),
                    ),
                  ),
                ],
              ),
            )),
          ),
        ));
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

  Future<ResponseEditVtLearner?> getMembeDetails(String value) async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    RequestEditVt requestSearchLearner = new RequestEditVt(proofDetail: value);
    String url = BASE_URL + GET_EDITMEMBER_LIST;
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), requestSearchLearner.toMap());
    Util util = Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      loader();
      return presenterGetMemberDetails.getLearnersLists(
          requestSearchLearner.toMap(),
          signature,
          timestamp.toString(),
          GET_EDITMEMBER_LIST,
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

    Navigator.pop(context);
    Fluttertoast.showToast(
        msg: utf8.decode(errorTxt.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
  }

  void sendDatatoTag(int index_val) {
    Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => EditTagging(data![index_val].id.toString(),
                data![index_val].name.toString(), data![index_val].address.toString())));

    // mem = new Member(
    //     id: data[index].id,
    //     fullname: data[index].fullname,
    //     mobile: data[index].mobile,
    //     email: data[index].email,
    //     address: data[index].address,
    //     markAs: data[index].markAs,
    //     age: data[index].age,
    //     gender: data[index].gender,
    //     proofType: data[index].proofType,
    //     proofDetail: data[index].proofDetail,
    //     socialCategoryId: data[index].socialCategoryId,
    //     fatherName: data[index].fatherName,
    //     profession: data[index].profession!,
    //     qualification: data[index].qualification,
    //     relation: 0,
    //     vt_type: data[index].vt_type,
    //     memberType: data[index].memberType);
    // Navigator.push(
    //     context,
    //     MaterialPageRoute(
    //         builder: (context) => EditTagging(
    //             data[index].fullname.toString(),
    //             data[index].address.toString(),
    //             data[index].members,
    //             data[index].id.toString(),
    //             mem)));
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

  searchText() {
    if (txt_search.text.trim() == "") {
      _showToast(Languages.of(context)!.Enter_text_to_search);
    }
    else if (txt_search.text.length<=2) {
      _showToast(Languages.of(context)!.Please_enter_at_least_3_characters);
    }
    else {
      getMembeDetails(txt_search.text);
    }
  }

  Future<bool> _onBackPressed() async {
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => DashBoard()));
    return await true;
  }

  @override
  void onResponseSuccess(ResponseEditVt responseDto) {
    // TODO: implement onResponseSuccess
  }

  @override
  void onResponseTagSuccess(ResponseTagging responseDto) {
    // TODO: implement onResponseTagSuccess
  }

  @override
  void onResponseSuccesses(ResponseEditVtLearner responseDto) {
    Navigator.pop(context);
    FocusManager.instance.primaryFocus?.unfocus();
    print("value..$responseDto");
    // if (responseDto.data![0].id != null) {
       setState(() {
         flag = 1;
       data = responseDto.data ;
      });
    // } else {
    //   Fluttertoast.showToast(
    //       msg: Languages.of(context)!.NO_RECORD_FOUND,
    //       toastLength: Toast.LENGTH_SHORT,
    //       gravity: ToastGravity.BOTTOM,
    //       timeInSecForIosWeb: 1,
    //       textColor: Colors.black,
    //       backgroundColor: Colors.white,
    //       fontSize: 16.0);
    // }
  }

  getVisibilty() {
    if (data != null) {
      // return data![index_val].id!.length == null ? false : true;
      return true;
    } else
      return false;
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

      getMembeDetails(txt_search.text);
    });
  }
}
