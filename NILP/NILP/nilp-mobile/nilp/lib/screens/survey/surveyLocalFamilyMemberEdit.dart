import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/Database/SquliteDataBaseHelper.dart';
import 'package:nilp/model/FamilyInfo.dart';
import 'package:nilp/model/response/ResponseAddMember.dart';
import 'package:nilp/model/response/ResponseGetRespondentType.dart';
import 'package:nilp/presenter/PresenterAddMember.dart';
import 'package:nilp/screens/survey/surveryFamilyHead.dart';
import 'package:nilp/screens/survey/surveyFamilyMemberEdit.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/request/RequestMemberUpdate.dart';
import '../../model/request/RequestRefreshToken.dart';
import '../../model/response/ResponseMemberUpdate.dart';
import '../../model/response/ResponseRefreshToken.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../utility/Util.dart';
import '../../utility/testEncryption.dart';
import '../dashboard/dashboard.dart';
import '../login/login.dart';
import '../manage_record/local_manage_record.dart';
import '../manage_record/manage_record.dart';

class SurveyLocalFamilyMemberEdit extends StatefulWidget {
  SurveyLocalFamilyMemberEdit(
    id,
    family_head_name,
    family_head_address,
    family_head_markas,
    member_id,
    member_name,
    member_ralateion,
    member_mark_as,
    member_father_name,
    member_mobile_no,
    member_age,
    member_proof_detail,
    member_profession,
    member_qualification,
    member_vt,
    member_gender,
    member_prooftype,
    member_social_category,
      member_isdivyang,

      ) {
    h_id = id;
    h_name = family_head_name;
    h_address = family_head_address;
    h_markas = family_head_markas;
    m_name = member_name;
    m_father_name = member_father_name;
    m_mobile_no = member_mobile_no;
    m_age = member_age;
    m_id = member_id;
    m_mark_as = member_mark_as;
    m_gender = member_gender;
    m_proofType = member_prooftype;
    m_vt_type = member_vt;
    m_social_caterory = member_social_category;
    m_qualification = member_qualification;
    m_profession = member_profession;
    m_relation = member_ralateion;
    m_proofdetail = member_proof_detail;
    m_is_divyang = member_isdivyang==1?true:false;

  }

  @override
  _SurveyLocalFamilyMemberEditState createState() =>
      _SurveyLocalFamilyMemberEditState();
}

late PresenterAddMember presenterAddMember;
var m_name,
    h_id,
    h_name,
    h_address,
    h_markas,
    m_father_name,
    //head_address,
    m_mobile_no,
    m_id,
    m_age,
    m_mark_as,
    m_profession,
    m_gender,
    m_proofType,
    m_social_caterory,
    m_vt_type,
    m_relation,
    m_qualification,
    m_proofdetail;

var family_member_proof_Identity_type,
    family_member_gender,
    family_member_mark_as,
    family_member_social_caterory,
    family_member_age,
    family_member_profession,
    family_member_relation;

int? age;
int? flag = 0;
var familydata;

class _SurveyLocalFamilyMemberEditState
    extends State<SurveyLocalFamilyMemberEdit>
    with SingleTickerProviderStateMixin
    implements ResponseContract {
  Map? jsonData;
  List<String>? Age;
  List? professional;
  List? social_category;
  List? relationship;
  List? proofType;
  List? education_type;
  List? vt;
  List? family_head_role;
  var valueMap;
  var mark_as;
  var divyang;
  var store_head_markas_value;
  var gender, member_educationtype, member_vtTYpe;

  TextEditingController family_member_name = TextEditingController();
  TextEditingController family_member_father = TextEditingController();
  TextEditingController family_member_mobile_no = TextEditingController();
  TextEditingController family_member_id_no = TextEditingController();
  TextEditingController family_member_age = TextEditingController();
  var family_member_profession;
  int? _selectedValueIndex;
  int? _selectedValueIndexDivyang;

  List<String> ?buttonText;
  List<String> ?buttonTextDivyang;

  Util util = new Util();

  List<Map> slist = [];
  SquliteDatabaseHelper squlitedb = new SquliteDatabaseHelper();

  GlobalKey<FormState> _formKey1 = GlobalKey<FormState>();

  @override
  void initState() {
    family_member_name.text = (m_name);
    family_member_father.text = m_father_name.toString();
    family_member_mobile_no.text = (m_mobile_no);
    family_member_id_no.text = (m_proofdetail);
    family_member_age.text = (m_age);
    family_member_social_caterory = m_social_caterory;
    _tabController.index = getTab();
    getDropdownValue();
    _selectedValueIndex = genderValue();
    _selectedValueIndexDivyang = divyangValue();
    store_head_markas_value = m_mark_as;
    presenterAddMember = new PresenterAddMember(this);
    super.initState();
  }

  getDropdownValue() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? dprofessions = await pref.getString("dropdowns");
    setState(() {
      jsonData = jsonDecode(dprofessions!);
      valueMap = json.decode(dprofessions!);
      education_type = jsonData!['qualifications'];
      vt = jsonData!['vt_types'];
      professional = jsonData!['profession'];
      social_category = jsonData!['social_category'];
      relationship = jsonData!['relationship'];
      proofType = jsonData!['proof_type'];
      family_head_role = jsonData!['family_head_role'];
    });
  }

  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
    String? login = perf.getString(LOGIN);
  }

  late final _tabController = TabController(length: 2, vsync: this);

  @override
  Widget build(BuildContext context) {
    buttonTextDivyang = [Languages.of(context)!.YES, Languages.of(context)!.NO];
    buttonText = [Languages.of(context)!.Male, Languages.of(context)!.Female, Languages.of(context)!.Transgender];
    return Scaffold(
        backgroundColor: kHomeBgColor,
        appBar: AppBar(
          leading: IconButton(
            icon: new Icon(Icons.arrow_back_ios_outlined),
            onPressed: () {
              _onBackPressed();
            },
          ),
          title: Text(
            Languages.of(context)!.Survey,
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
                icon: Icon(
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
          child: SafeArea(
              child: Form(
            key: _formKey1,
            child: SingleChildScrollView(
                child: Container(
              color: kHomeBgColor,
              child: Column(
                children: [
                  Padding(
                    padding: EdgeInsets.fromLTRB(20, 20, 20, 5),
                    child: Wrap(
                      children: [
                        Container(
                          width: MediaQuery.of(context).size.width,
                          decoration: BoxDecoration(
                            color: kBg,
                            borderRadius: BorderRadius.circular(10.0),
                          ),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Container(
                                height: 50,
                                width: MediaQuery.of(context).size.width,
                                decoration: BoxDecoration(
                                  color: kFillaSurveyColor,
                                  borderRadius: BorderRadius.only(
                                      topLeft: Radius.circular(10),
                                      topRight: Radius.circular(10)),
                                ),
                                child: Row(
                                  children: [
                                    Align(
                                        alignment: Alignment.centerLeft,
                                        child: Container(
                                            margin:
                                                const EdgeInsets.only(left: 20),
                                            child: Text(
                                              Languages.of(context)!
                                                  .Family_Head_Detail,
                                              style: kBodyTextStyle,
                                            ))),
                                    const Spacer(),
                                    Visibility(
                                      visible: false,
                                      child: const Padding(
                                        padding: EdgeInsets.all(5.0),
                                        child: Card(
                                          shape: RoundedRectangleBorder(
                                            borderRadius: BorderRadius.all(
                                                Radius.circular(60)),
                                          ),
                                          color: kDrawerSheetTextColor,
                                          child: Padding(
                                            padding: EdgeInsets.all(8.0),
                                            child: Icon(
                                              Icons.edit,
                                              color: kBg,
                                              size: 20,
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(8.0),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                        Languages.of(context)!.Family_Head_Name,
                                        style: kManageProfile),
                                    Text((h_name),
                                        style: kManageProfileheading),
                                    Text(
                                      Languages.of(context)!.House_Address,
                                      style: kManageProfile,
                                    ),
                                    Text(
                                      (h_address),
                                      style: kManageProfileheading,
                                    ),
                                    Text(
                                      Languages.of(context)!.Mark_As,
                                      style: kManageProfile,
                                    ),
                                    Text(
                                      head_markas(h_markas),
                                  //    h_markas == LEARNER ? "LEARNER" : "VT",
                                      style: kManageProfileheading,
                                    ),
                                  ],
                                ),
                              )
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                  Wrap(
                    children: [
                      Padding(
                        padding: EdgeInsets.fromLTRB(20, 5, 20, 5),
                        child: Container(
                          margin: EdgeInsets.only(top: 20),
                          //height: MediaQuery.of(context).size.height,
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
                                decoration: BoxDecoration(
                                  color: kFillaSurveyColor,
                                  borderRadius: BorderRadius.only(
                                      topLeft: Radius.circular(10),
                                      topRight: Radius.circular(10)),
                                ),
                                child: Align(
                                    alignment: Alignment.centerLeft,
                                    child: Container(
                                        margin: EdgeInsets.only(left: 20),
                                        child: Text(
                                          Languages.of(context)!
                                              .Family_Member_Details,
                                          style: kBodyTextStyle,
                                        ))),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(12.0),
                                child: Container(
                                  child: TextFormField(
                                      keyboardType: TextInputType.name,
                                      autofocus: false,
                                      maxLength: 100,
                                      controller: family_member_name,
                                      inputFormatters: <TextInputFormatter>[
                                        FilteringTextInputFormatter.deny(RegExp(r'[0-9]')),
                                      ],
                                      // inputFormatters: <TextInputFormatter>[
                                      //   FilteringTextInputFormatter.allow(
                                      //       RegExp(r'[a-zA-Z]+|\s')),
                                      // ],
                                      // textCapitalization:
                                      //     TextCapitalization.sentences,
                                      cursorColor: kSkipColor,
                                      style: const TextStyle(
                                          color: kRefreshTextColor),
                                      decoration: InputDecoration(
                                        counterText: "",
                                        labelStyle:
                                            TextStyle(color: kRefreshTextColor),
                                        labelText:
                                            Languages.of(context)!.Full_Name,
                                        focusedBorder: UnderlineInputBorder(
                                          borderSide:
                                              BorderSide(color: kSkipColor),
                                        ),
                                        enabledBorder: UnderlineInputBorder(
                                          borderSide:
                                              BorderSide(color: kSkipColor),
                                        ),
                                        errorStyle:
                                            TextStyle(color: Colors.red),
                                      ),
                                      validator: (String? value) {
                                        if (value!.isEmpty) {
                                          return Languages.of(context)!
                                              .Please_enter_your_name;
                                        }
                                      }),
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(12.0),
                                child: Container(
                                  child: TextFormField(
                                    keyboardType: TextInputType.name,
                                    autofocus: false,
                                    inputFormatters: <TextInputFormatter>[
                                      FilteringTextInputFormatter.deny(RegExp(r'[0-9]')),
                                    ],
                                    // inputFormatters: <TextInputFormatter>[
                                    //   FilteringTextInputFormatter.allow(
                                    //       RegExp(r'[a-zA-Z]+|\s')),
                                    // ],
                                    // textCapitalization:
                                    //     TextCapitalization.sentences,
                                    cursorColor: kSkipColor,
                                    controller: family_member_father,
                                    style: const TextStyle(
                                        color: kRefreshTextColor),
                                    decoration: InputDecoration(
                                      counterText: "",
                                      labelStyle:
                                          TextStyle(color: kRefreshTextColor),
                                      labelText:
                                          Languages.of(context)!.Father_Name,
                                      focusedBorder: UnderlineInputBorder(
                                        borderSide:
                                            BorderSide(color: kSkipColor),
                                      ),
                                      enabledBorder: UnderlineInputBorder(
                                        borderSide:
                                            BorderSide(color: kSkipColor),
                                      ),
                                      errorStyle: TextStyle(color: Colors.red),
                                    ),
                                  ),
                                ),
                              ),
                              Padding(
                                padding:
                                    const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                child: DropdownButtonFormField(
                                  decoration: InputDecoration(
                                    labelText:
                                        Languages.of(context)!.Select_Relation,
                                    labelStyle:
                                        TextStyle(color: kRefreshTextColor),
                                    focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: kSkipColor),
                                    ),
                                    hintStyle: TextStyle(color: Colors.black),
                                    filled: true,
                                    fillColor: Colors.white,
                                    errorStyle: TextStyle(color: Colors.red),
                                  ),
                                  value: int.parse(m_relation),
                                  validator: (value) => value == null
                                      ? 'Please select your relation to head'
                                      : null,
                                  items: relationship?.map((map) {
                                    return DropdownMenuItem(
                                      child: Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    m_relation = value.toString();
                                  },
                                ),
                              ),
                              Padding(
                                padding: EdgeInsets.all(12.0),
                                child: Align(
                                    alignment: Alignment.centerLeft,
                                    child: Text(Languages.of(context)!.Mark_As,
                                        style: kDashBoardBoxBody)),
                              ),
                              Padding(
                                padding: EdgeInsets.all(8),
                                child: DefaultTabController(
                                  length: 2,
                                  child: Container(
                                    alignment: Alignment.center,
                                    height: 40,
                                    decoration: BoxDecoration(
                                      color: Colors.white,
                                      border: Border.all(
                                          color: kDrawerSheetTextColor,
                                          width: 2.0,
                                          style: BorderStyle.solid),
                                      borderRadius: BorderRadius.circular(10),
                                    ),
                                    child: TabBar(
                                      controller: _tabController,
                                      onTap: (value) {
                                        print(value);
                                        if (value == 0) {
                                          setState(() {
                                            m_mark_as = LEARNER;
                                          });
                                        } else {
                                          setState(() {
                                            m_mark_as = VT;
                                          });
                                        }
                                      },
                                      isScrollable: false,
                                      unselectedLabelColor: kBottomSheetTexts,
                                      labelColor: kBottomSheetTexts,
                                      indicator: BoxDecoration(
                                          color: kDrawerSheetTextColor),
                                      tabs: [
                                        Tab(
                                          child: Text(
                                            Languages.of(context)!.Learner,
                                          ),
                                        ),
                                        Tab(
                                          child: Text(Languages.of(context)!
                                              .Volunteer_Trainer),
                                        ),
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                              Row(
                                children: [
                                  Expanded(
                                    flex: 4,
                                    child: Padding(
                                      padding: const EdgeInsets.all(12.0),
                                      child: TextFormField(
                                          keyboardType: TextInputType.number,
                                          autofocus: false,
                                          cursorColor: kSkipColor,
                                          enableInteractiveSelection: true,
                                          maxLength: 2,
                                          inputFormatters: <TextInputFormatter>[
                                            FilteringTextInputFormatter.deny(
                                                RegExp('[,.]')),
                                          ],
                                          controller: family_member_age,
                                          style: const TextStyle(
                                              color: kRefreshTextColor),
                                          decoration: InputDecoration(
                                            counterText: "",
                                            labelStyle: TextStyle(
                                                color: kRefreshTextColor),
                                            labelText: Languages.of(context)!
                                                .Select_Age,
                                            focusedBorder: UnderlineInputBorder(
                                              borderSide:
                                                  BorderSide(color: kSkipColor),
                                            ),
                                            enabledBorder: UnderlineInputBorder(
                                              borderSide:
                                                  BorderSide(color: kSkipColor),
                                            ),
                                            errorStyle:
                                                TextStyle(color: Colors.red),
                                          ),
                                          validator: (String? value) {
                                            if (value!.isEmpty) {
                                              return Languages.of(context)!
                                                  .Enter_age;
                                            } else if (m_mark_as == VT) {
                                              if (int.parse(value) < 12 ||
                                                  int.parse(value) > 100) {
                                                return Languages.of(context)!
                                                    .Please_enter_valid_age;
                                              }
                                            } else if ((m_mark_as == LEARNER)) {
                                              if (int.parse(value) < 15 ||
                                                  int.parse(value) > 100) {
                                                return Languages.of(context)!
                                                    .Please_enter_valid_age;
                                              }
                                            }
                                          }),
                                    ),
                                  ),
                                  Expanded(
                                    flex: 6,
                                    child: Padding(
                                      padding: const EdgeInsets.all(12.0),
                                      child: TextFormField(
                                        keyboardType: TextInputType.number,
                                        autofocus: false,
                                        cursorColor: kSkipColor,
                                        enableInteractiveSelection: true,
                                        maxLength: 10,
                                        inputFormatters: <TextInputFormatter>[
                                          FilteringTextInputFormatter.deny(
                                              RegExp('[,.]')),
                                        ],
                                        controller: family_member_mobile_no,
                                        style: const TextStyle(
                                            color: kRefreshTextColor),
                                        decoration: InputDecoration(
                                          counterText: "",
                                          labelStyle: TextStyle(
                                              color: kRefreshTextColor),
                                          labelText: Languages.of(context)!
                                              .Mobile_Number,
                                          focusedBorder: UnderlineInputBorder(
                                            borderSide:
                                                BorderSide(color: kSkipColor),
                                          ),
                                          enabledBorder: UnderlineInputBorder(
                                            borderSide:
                                                BorderSide(color: kSkipColor),
                                          ),
                                          errorStyle:
                                              TextStyle(color: Colors.red),
                                        ),
                                        validator: (value) {
                                          String pattern =
                                              r'(^((\+91)?|91)?[6789][0-9]{9})';
                                          RegExp regExp = new RegExp(pattern);
                                          if (value!.length == 0) {
                                            return Languages.of(context)!
                                                .Please_enter_your_mobile_no;
                                          } else if (!regExp.hasMatch(value)) {
                                            return Languages.of(context)!
                                                .ENTER_VALID_MOBILE;
                                          }
                                        },
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                              Padding(
                                padding: EdgeInsets.all(12.0),
                                child: Align(
                                    alignment: Alignment.centerLeft,
                                    child: Text(Languages.of(context)!.Gender,
                                        style: kDashBoardBoxBody)),
                              ),
                              Row(
                                children: [
                                  ...List.generate(
                                    buttonText!.length,
                                    (index) => Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: button(
                                        index: index,
                                        text: buttonText![index],
                                      ),
                                    ),
                                  )
                                ],
                              ),
                              Visibility(
                                visible:
                                    (_selectedValueIndex == null && flag == 1),
                                child: Padding(
                                  padding: const EdgeInsets.all(10.0),
                                  child: Align(
                                      alignment: Alignment.topLeft,
                                      child: Text(
                                        Languages.of(context)!
                                            .Please_select_Gender,
                                        style: TextStyle(
                                          color: Colors.red,
                                          fontSize: 12,
                                        ),
                                      )),
                                ),
                              ),



                              /////////////////////////////////////////////////////////////////////////////
                              Padding(
                                padding: EdgeInsets.all(12.0),
                                child: Align(
                                    alignment: Alignment.centerLeft,
                                    child: Text(Languages.of(context)!.Is_Divyangjan +"?",
                                        style: kDashBoardBoxBody)),
                              ),
                              Padding(
                                padding: const EdgeInsets.only(left: 8),
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  children: [
                                    ...List.generate(
                                      buttonTextDivyang!.length,
                                          (index) => Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: buttonDivyang(
                                          index: index,
                                          text: buttonTextDivyang![index],
                                        ),
                                      ),
                                    )
                                  ],
                                ),
                              ),
                              Visibility(
                                visible:
                                (_selectedValueIndex == null && flag == 1),
                                child: Padding(
                                  padding: const EdgeInsets.all(10.0),
                                  child: Align(
                                      alignment: Alignment.topLeft,
                                      child: Text(
                                        Languages.of(context)!
                                            .Please_select_Gender,
                                        style: TextStyle(
                                          color: Colors.red,
                                          fontSize: 12,
                                        ),
                                      )),
                                ),
                              ),

                              /////////////////////////////////////////////////////////////////


                              Padding(
                                padding:
                                    const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                child: DropdownButtonFormField(
                                  decoration: InputDecoration(
                                    labelText: Languages.of(context)!
                                        .Select_Identity_Type,
                                    hintStyle: TextStyle(color: Colors.black),
                                    labelStyle:
                                        TextStyle(color: kRefreshTextColor),
                                    focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: kSkipColor),
                                    ),
                                    filled: true,
                                    fillColor: Colors.white,
                                    errorStyle: TextStyle(color: Colors.red),
                                  ),
                                  value: int.parse(m_proofType),
                                  validator: (value) => value == null
                                      ? 'Please select your identity type'
                                      : null,
                                  items: proofType?.map((map) {
                                    return DropdownMenuItem(
                                      child: Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    print(value);
                                    m_proofType = value.toString();
                                  },
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(12.0),
                                child: Container(
                                  child: TextFormField(
                                      // keyboardType: TextInputType.number,
                                      autofocus: false,
                                      controller: family_member_id_no,
                                      cursorColor: kSkipColor,
                                      enableInteractiveSelection: true,
                                      maxLength: 16,
                                      inputFormatters: <TextInputFormatter>[
                                        FilteringTextInputFormatter.allow(
                                            RegExp("[0-9a-zA-Z]")),
                                      ],
                                      style: const TextStyle(
                                          color: kRefreshTextColor),
                                      decoration: InputDecoration(
                                        counterText: "",
                                        labelStyle:
                                            TextStyle(color: kRefreshTextColor),
                                        labelText:
                                            Languages.of(context)!.ID_Number,
                                        focusedBorder: UnderlineInputBorder(
                                          borderSide:
                                              BorderSide(color: kSkipColor),
                                        ),
                                        enabledBorder: UnderlineInputBorder(
                                          borderSide:
                                              BorderSide(color: kSkipColor),
                                        ),
                                        errorStyle:
                                            TextStyle(color: Colors.red),
                                      ),
                                      validator: (String? value) {
                                        if (value!.isEmpty) {
                                          return Languages.of(context)!
                                              .Please_enter_your_Id_NO;
                                        } else if (value.length < 4) {
                                          return Languages.of(context)!
                                              .Please_enter_valid_id;
                                        }
                                      }),
                                ),
                              ),
                              Padding(
                                padding:
                                    const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                child: DropdownButtonFormField(
                                  decoration: InputDecoration(
                                    labelText:
                                        Languages.of(context)!.Select_Category,
                                    hintStyle: TextStyle(color: Colors.black),
                                    labelStyle:
                                        TextStyle(color: kRefreshTextColor),
                                    focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: kSkipColor),
                                    ),
                                    filled: true,
                                    fillColor: Colors.white,
                                    errorStyle: TextStyle(color: Colors.red),
                                  ),
                                  value: int.parse(m_social_caterory),
                                  validator: (value) => value == null
                                      ? 'Please select your social category'
                                      : null,
                                  items: social_category?.map((map) {
                                    return DropdownMenuItem(
                                      child: Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    m_social_caterory = value.toString();
                                    print(family_member_social_caterory);
                                  },
                                ),
                              ),
                              Padding(
                                padding:
                                    const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                child: DropdownButtonFormField(
                                  decoration: InputDecoration(
                                    labelText:
                                        Languages.of(context)!.Profession_Type,
                                    labelStyle:
                                        TextStyle(color: kRefreshTextColor),
                                    focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: kSkipColor),
                                    ),
                                    hintStyle: TextStyle(color: Colors.black),
                                    filled: true,
                                    fillColor: Colors.white,
                                    errorStyle: TextStyle(color: Colors.red),
                                  ),
                                  value: int.parse(m_profession),
                                  validator: (value) => value == null
                                      ? 'Please select your profession type'
                                      : null,
                                  items: professional?.map((map) {
                                    return DropdownMenuItem(
                                      child: Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    m_profession = value.toString();
                                  },
                                ),
                              ),
                              Visibility(
                                visible: (m_mark_as == VT) ? true : false,
                                child: Padding(
                                  padding:
                                      const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                  child: DropdownButtonFormField(
                                    decoration: InputDecoration(
                                      labelText:
                                          Languages.of(context)!.Education_Type,
                                      labelStyle:
                                          TextStyle(color: kRefreshTextColor),
                                      focusedBorder: UnderlineInputBorder(
                                        borderSide:
                                            BorderSide(color: kSkipColor),
                                      ),
                                      hintStyle: TextStyle(color: Colors.black),
                                      filled: true,
                                      fillColor: Colors.white,
                                      errorStyle: TextStyle(color: Colors.red),
                                    ),
                                    value: m_qualification == null
                                        ||m_qualification == "null"
                                        ? int.parse("0")
                                        : int.parse(m_qualification),
                                    validator: (value) => value == null
                                        ? Languages.of(context)!
                                            .Please_select_education_type
                                        : null,
                                    items: education_type?.map((map) {
                                      return DropdownMenuItem(
                                        child: Text(map[dropdownConvertLang()]),
                                        value: map['id'],
                                      );
                                    }).toList(),
                                    onChanged: (value) {
                                      m_qualification = value.toString();
                                    },
                                  ),
                                ),
                              ),
                              Visibility(
                                visible: (m_mark_as == VT) ? true : false,
                                child: Padding(
                                  padding:
                                      const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                  child: DropdownButtonFormField(
                                    isExpanded: true,
                                    decoration: InputDecoration(
                                      labelText: Languages.of(context)!
                                          .Voluntary_Traniner_Type,
                                      labelStyle:
                                          TextStyle(color: kRefreshTextColor),
                                      focusedBorder: UnderlineInputBorder(
                                        borderSide:
                                            BorderSide(color: kSkipColor),
                                      ),
                                      hintStyle: TextStyle(color: Colors.black),
                                      filled: true,
                                      fillColor: Colors.white,
                                      errorStyle: TextStyle(color: Colors.red),
                                    ),
                                    value: m_vt_type == null
                                        ||m_vt_type == "null"
                                        ? int.parse("0")
                                        : int.parse(m_vt_type),
                                    validator: (value) => value == null
                                        ? Languages.of(context)!
                                            .Please_select_VT
                                        : null,
                                    items: vt?.map((map) {
                                      return DropdownMenuItem(
                                        child: Wrap(
                                          children: [
                                            Text(
                                              map[dropdownConvertLang()],
                                              // overflow: TextOverflow.ellipsis,
                                            ),
                                          ],
                                        ),
                                        value: map['id'],
                                      );
                                    }).toList(),
                                    onChanged: (value) {
                                      m_vt_type = value.toString();
                                    },
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                  Row(
                    children: [
                      Expanded(
                        flex: 5,
                        child: Padding(
                          padding: const EdgeInsets.all(10),
                          child: GestureDetector(
                            onTap: () {
                              setState(() {
                                flag = 1;
                              });
                              if (_formKey1.currentState!.validate()) {
                                if (validateGender(m_gender.toString(),
                                    m_relation.toString())) {
                                  if (validateIdType(
                                    int.parse(m_proofType),
                                    family_member_id_no.text.length,
                                    family_member_id_no.text.toString(),
                                  )) {
                                    if (m_qualification == "0" &&
                                        m_mark_as == VT) {
                                      Fluttertoast.showToast(
                                        msg:
                                            "Please select any other Education type",
                                        toastLength: Toast.LENGTH_LONG,
                                        textColor: Colors.black,
                                        backgroundColor: Colors.white,
                                        fontSize: 18.0,
                                      );
                                    } else {
                                      if (m_vt_type == "0" && m_mark_as == VT) {
                                        Fluttertoast.showToast(
                                          msg:
                                              "Please select any other VT type",
                                          toastLength: Toast.LENGTH_LONG,
                                          textColor: Colors.black,
                                          backgroundColor: Colors.white,
                                          fontSize: 18.0,
                                        );
                                      } else {
                                        if (store_head_markas_value ==
                                            m_mark_as) {
                                          showDialog(
                                            context: context,
                                            builder: (context) => Container(
                                              decoration: ShapeDecoration(
                                                  shape:
                                                      ContinuousRectangleBorder(
                                                borderRadius: BorderRadius.all(
                                                  Radius.circular(30),
                                                ),
                                              )),
                                              child: AlertDialog(
                                                title: new Text(
                                                    Languages.of(context)!
                                                        .ARE_YOU_SURE),
                                                content: new Text(
                                                    Languages.of(context)!.Do_you_want_to_change),
                                                actions: <Widget>[
                                                  ElevatedButton(
                                                    onPressed: () =>
                                                        Navigator.of(context)
                                                            .pop(false),
                                                    child: Text(
                                                        Languages.of(context)!
                                                            .NO),
                                                  ),
                                                  SizedBox(height: 16),
                                                  ElevatedButton(
                                                    onPressed: () {
                                                      Navigator.pop(context);
                                                      updateMember();
                                                    },
                                                    child: Text(
                                                        Languages.of(context)!
                                                            .YES),
                                                  ),
                                                ],
                                              ),
                                            ),
                                          );
                                        } else {
                                          showDialog(
                                            context: context,
                                            builder: (context) => Container(
                                              decoration: ShapeDecoration(
                                                  shape:
                                                      ContinuousRectangleBorder(
                                                borderRadius: BorderRadius.all(
                                                  Radius.circular(30),
                                                ),
                                              )),
                                              child: AlertDialog(
                                                title: new Text(
                                                    Languages.of(context)!
                                                        .ARE_YOU_SURE),
                                                content: new Text(
                                                    Languages.of(context)!.Do_you_want_to_change +
                                                        util.getRole(
                                                            m_mark_as)),
                                                actions: <Widget>[
                                                  ElevatedButton(
                                                    onPressed: () =>
                                                        Navigator.of(context)
                                                            .pop(false),
                                                    child: Text(
                                                        Languages.of(context)!
                                                            .NO),
                                                  ),
                                                  SizedBox(height: 16),
                                                  ElevatedButton(
                                                    onPressed: () {
                                                      Navigator.pop(context);
                                                      updateMember();
                                                    },
                                                    child: Text(
                                                        Languages.of(context)!
                                                            .YES),
                                                  ),
                                                ],
                                              ),
                                            ),
                                          );
                                        }
                                      }
                                    }
                                  } else {
                                    Fluttertoast.showToast(
                                        msg: "Please enter valid ID Number",
                                        toastLength: Toast.LENGTH_SHORT,
                                        gravity: ToastGravity.BOTTOM,
                                        timeInSecForIosWeb: 1,
                                        textColor: Colors.black,
                                        backgroundColor: Colors.white,
                                        fontSize: 16.0);
                                  }
                                } else {
                                  Fluttertoast.showToast(
                                      msg: "Gender not valid",
                                      toastLength: Toast.LENGTH_SHORT,
                                      gravity: ToastGravity.BOTTOM,
                                      timeInSecForIosWeb: 1,
                                      textColor: Colors.black,
                                      backgroundColor: Colors.white,
                                      fontSize: 16.0);
                                }
                              }
                            },

                            /*   onTap: () {
                              updateMember();
                            },*/
                            child: Container(
                              height: 60,
                              decoration: BoxDecoration(
                                  color: kSendaSurveyColor,
                                  borderRadius: BorderRadius.circular(40)),
                              child: Center(
                                child: Padding(
                                  padding: EdgeInsets.all(4.0),
                                  child: Row(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Padding(
                                        padding: const EdgeInsets.all(4.0),
                                        child: Icon(
                                          Icons.upload_rounded,
                                          size: 30,
                                          color: kBg,
                                        ),
                                      ),
                                      Text(
                                        Languages.of(context)!.UPDATE,
                                        textAlign: TextAlign.center,
                                        style: GoogleFonts.poppins(
                                          textStyle: kButtonTextStyle,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            )),
          )),
        ));
  }

  Future<ResponseMemberUpdate?> addMember() async {
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    RequestMemberUpdate request = new RequestMemberUpdate(
      id: m_id.toString(),
      fullname: encrypt(family_member_name.text.toString()).base64,
      fatherName: family_member_father.text == ""
          ? ""
          : family_member_father.text.toString(),
      mobile: encrypt(family_member_mobile_no.text.toString()).base64,
      age: int.parse(family_member_age.text),
      markAs: m_mark_as.toString(),
      gender: m_gender.toString(),
      proofDetail: family_member_id_no.text.toString(),
      proofType: m_proofType.toString(),
      socialCategoryId: m_social_caterory.toString(),
      profession: m_profession.toString(),
      qualification: m_qualification.toString(),
      vtType: m_vt_type.toString(),
      relation: m_relation.toString(),
    );
    String url = BASE_URL + MEMBER_UPDATE;
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), request.toMap());
    SharedPreferences pref = await SharedPreferences.getInstance();

    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    Util util = new Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      loader();
      return presenterAddMember.updateMember(request.toMap(), signature,
          timestamp.toString(), MEMBER_UPDATE, context, acesstoken);
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


  Widget buttonDivyang({required String text, required int index}) {
    return InkWell(
      splashColor: Colors.cyanAccent,
      onTap: () {
        setState(() {
          _selectedValueIndexDivyang = index;
          if (_selectedValueIndexDivyang == 0) {
            m_is_divyang = true;
          } else if (_selectedValueIndexDivyang == 1) {
            m_is_divyang = false;
          }
          print("div  $m_is_divyang ");
          print(_selectedValueIndexDivyang);
        });
      },
      child: Container(
          width: MediaQuery.of(context).size.width / 4,
          padding: const EdgeInsets.fromLTRB(3, 8, 3, 8),
          child: Center(
            child: Text(
              text,
              style: TextStyle(
                color: index == _selectedValueIndexDivyang
                    ? kBottomSheetTexts
                    : kBottomSheetTexts,
              ),
            ),
          ),
          decoration: index == _selectedValueIndexDivyang
              ? BoxDecoration(
              color: kDrawerSheetTextColor,
              borderRadius: BorderRadius.circular(10),
              boxShadow: const [
                BoxShadow(color: kDrawerSheetTextColor, spreadRadius: 2)
              ])
              : BoxDecoration(
            borderRadius: BorderRadius.circular(10),
            color: Colors.white,
            boxShadow: const [
              BoxShadow(color: kDrawerSheetTextColor, spreadRadius: 2),
            ],
          )),
    );
  }

  Widget button({required String text, required int index}) {
    return InkWell(
      splashColor: Colors.cyanAccent,
      onTap: () {
        setState(() {
          _selectedValueIndex = index;
          if (_selectedValueIndex == 0) {
            m_gender = "m";
          } else if (_selectedValueIndex == 1) {
            m_gender = "f";
          } else if (_selectedValueIndex == 2) {
            m_gender = "t";
          }
        });
      },
      child: Container(
          width: MediaQuery.of(context).size.width / 4,
          padding: const EdgeInsets.fromLTRB(3, 8, 3, 8),
          child: Center(
            child: Text(
              text,
              style: TextStyle(
                color: index == _selectedValueIndex
                    ? kBottomSheetTexts
                    : kBottomSheetTexts,
              ),
            ),
          ),
          decoration: index == _selectedValueIndex
              ? BoxDecoration(
                  color: kDrawerSheetTextColor,
                  borderRadius: BorderRadius.circular(10),
                  boxShadow: const [
                      BoxShadow(color: kDrawerSheetTextColor, spreadRadius: 2)
                    ])
              : BoxDecoration(
                  borderRadius: BorderRadius.circular(10),
                  color: Colors.white,
                  boxShadow: const [
                    BoxShadow(color: kDrawerSheetTextColor, spreadRadius: 2),
                  ],
                )),
    );
  }

  @override
  void onResponseAddMember(ResponseAddMember responseDto) {
    // TODO: implement onResponseAddMember
    Navigator.pop(context);
    Fluttertoast.showToast(
        msg: utf8.decode(responseDto.data.updateErrorMessage.codeUnits),
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    showDialog(
        barrierDismissible: false,
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text("Thank you"),
            content: Text("Do you want to upload more members ?"),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                  Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(
                          builder: (context) => SurveyFamilyHead()));
                },
                child: Text("NO"),
              ),
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: Text("YES"),
              )
            ],
          );
        });
  }

  @override
  void onResponseSuccess(ResponseGetRespondentType responseDto) {
    // TODO: implement onResponseSuccess
  }

  @override
  void onError(String errorTxt, String code) {
    Navigator.pop(context);
    // Navigator.pushReplacement(
    //     context, MaterialPageRoute(builder: (context) => ManageRecord()));
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
  void onResponseSuccessFav() {
    // TODO: implement onResponseSuccessFav
  }

  showHeadMarkasValue() {
    if (m_mark_as == LEARNER) {
      return "LEARNER";
    } else if (m_mark_as == VT) {
      return "VT";
    }
  }

  @override
  void onResponseUpateMember(ResponseMemberUpdate responseDto1) {
    // TODO: implement onResponseUpateMember
    if (responseDto1.data!.status!) {
      Navigator.pop(context);
      Navigator.pushReplacement(
          context, MaterialPageRoute(builder: (context) => ManageRecord()));
      Fluttertoast.showToast(
          msg: utf8.decode(responseDto1.data!.message!.codeUnits),
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
    } else {
      Navigator.pop(context);
      Fluttertoast.showToast(
          msg:utf8.decode( responseDto1.data!.message!.codeUnits),
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
    }
  }

  int getTab() {
    if (m_mark_as == LEARNER)
      return 0;
    else
      return 1;
  }


  divyangValue() {
    if (m_is_divyang == true) {
      return 0;
    } else if (m_is_divyang == false) {
      return 1;
    }
  }
  //

  genderValue() {
    if (m_gender == "m") {
      return 0;
    } else if (m_gender == "f") {
      return 1;
    } else {
      return 2;
    }
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
                child: Text(Languages.of(context)!.CANCEL,maxLines: 1,),
              ),
              TextButton(
                onPressed: () async {
                  Navigator.pop(context);
                  Navigator.push(
                      context,
                      MaterialPageRoute(
                          builder: (context) => LocalManageRecord()));
                },
                child: Text(Languages.of(context)!.OK,maxLines: 1,),
              )
            ],
          );
        });
  }

  void updateMember() async {
    Members request = new Members(
        memberName: encrypt(family_member_name.text.toString()).base64,
        memberFatherName: family_member_father.text == ""
            ? ""
            : family_member_father.text.toString(),
        memberMobile: encrypt(family_member_mobile_no.text.toString()).base64,
        memberAge: (family_member_age.text),
        memberMarkAs: m_mark_as.toString(),
        memberGender: m_gender.toString(),
        memberProofDetail: family_member_id_no.text.toString(),
        memberProofType: m_proofType.toString(),
        memberSocialCategoryId: m_social_caterory.toString(),
        memberProfession: m_profession.toString(),
        memberQualification: m_qualification.toString(),
        memberVtType: m_vt_type.toString(),
        memberRelation: m_relation.toString(),
        memberAddress: "",
        member_id: int.parse(m_id),
        memberDivyang: m_is_divyang==true?1:0,
        head_id: h_id);

    final db = await squlitedb;
    int? res = await db.updateMemberData(request, int.parse(m_id!));
    Fluttertoast.showToast(
        msg: Languages.of(context)!.Member_added_Sccessfully,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => DashBoard()));
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

    presenterAddMember.refreshToken(requestRefreshToken.toMap(), signature,
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
      addMember();
    });
  }

  head_markas(String value){
    if(value=="0"){
      return "None";
    }
    else if(value==LEARNER){
      return "Learner";
    }
    else
      return "VT";
  }

  dropdownConvertLang(){
    if(Util.currentLocal=="en"){
      return 'en';
    }
    else if ( Util.currentLocal=="hin"){
      return 'hin';
    }
    else{
      return 'en';
    }
  }
}
