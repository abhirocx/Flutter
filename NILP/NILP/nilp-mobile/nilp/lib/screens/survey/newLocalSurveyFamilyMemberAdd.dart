import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/Database/SquliteDataBaseHelper.dart';
import 'package:nilp/model/response/ResponseAddMember.dart';
import 'package:nilp/model/response/ResponseGetRespondentType.dart';
import 'package:nilp/model/response/ResponseMemberUpdate.dart';
import 'package:nilp/presenter/PresenterAddMember.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/DBModel/MemberModel.dart';
import '../../model/DBModel/localHeadUpdateModel.dart';
import '../../model/request/RequestRefreshToken.dart';
import '../../model/response/ResponseRefreshToken.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../utility/Util.dart';
import '../../utility/testEncryption.dart';
import '../dashboard/dashboard.dart';
import '../login/login.dart';
import '../manage_record/local_manage_record.dart';

class NewLocalSurveyFamilyMemberAdd extends StatefulWidget {
  NewLocalSurveyFamilyMemberAdd(
    var family_head_names,
    family_head_addresss,
    family_head_markass,
    family_head_age,
    family_head_mobile,
    family_head_gender,
    family_head_proof_type,
    family_head_proof_id,
    family_head_social_category,
    family_head_profession,
    family_head_education,
    family_head_vt_type,
    head_member_TYpe,
      head_divyang_TYpe,
    head_id,
  ) {
    head_names = family_head_names;
    head_addresss = family_head_addresss;
    head_mark_ass = family_head_markass;
    head_ages = family_head_age;
    head_mobile_nos = family_head_mobile;
    head_genders = family_head_gender;
    head_proofTypes = family_head_proof_type;
    head_ids = family_head_proof_id;
    head_social_caterorys = family_head_social_category;
    head_professions = family_head_profession;
    head_eduTypes = family_head_education;
    head_VTtypes = family_head_vt_type;
    only_head = head_member_TYpe;
    headisDivyang = head_divyang_TYpe;
    head_id_value = head_id;
  }

  @override
  _NewLocalSurveyFamilyMemberAddState createState() =>
      _NewLocalSurveyFamilyMemberAddState();
}

late PresenterAddMember presenterAddMember;
var head_names,
    head_addresss,
    head_mobile_nos,
    head_ids,
    head_ages,
    head_mark_ass,
    head_professions,
    head_genders,
    head_proofTypes,
    head_eduTypes,
    head_VTtypes,
    head_social_caterorys,
    check_only_Heads,
    only_head,
    head_id_value;

var family_member_proof_Identity_type,
    family_member_gender,
    family_member_social_caterory,
    family_member_age,
    family_member_profession,
    family_member_relation;
String family_member_mark_as = LEARNER;
int? age;
var familydata,headisDivyang;

class _NewLocalSurveyFamilyMemberAddState
    extends State<NewLocalSurveyFamilyMemberAdd> implements ResponseContract {
  Map? jsonData;
  List<String>? Age;
  List? professional;
  List? social_category;
  var flag = 0;
  var divyang;
  List? relationship;
  List? education_type;
  List? vt;
  List? proofType;
  List? family_head_role;
  var valueMap;
  var mark_as;
  var gender, member_educationtype, member_vtTYpe;
  String member_mark_as = "";

  TextEditingController family_member_name = TextEditingController();
  TextEditingController family_member_father = TextEditingController();
  TextEditingController family_member_mobile_no = TextEditingController();
  TextEditingController family_member_id_no = TextEditingController();
  TextEditingController family_member_age = TextEditingController();
  var family_member_profession;
  int? _selectedValueIndex;
  List<String> ?buttonText;
  int?   _selectedValueIndexDivyang;
  List<String> ?buttonTextDivyang ;


  List<Map> slist = [];
  SquliteDatabaseHelper squlitedb = new SquliteDatabaseHelper();

  GlobalKey<FormState> _formKey1 = GlobalKey<FormState>();

  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
    String? login = perf.getString(LOGIN);
  }

  @override
  void initState() {
    getDropdownValue();
    presenterAddMember = new PresenterAddMember(this);
    super.initState();
  }

  getDropdownValue() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? dprofessions = await pref.getString("dropdowns");
    setState(() {
      jsonData = jsonDecode(dprofessions!);
      valueMap = json.decode(dprofessions!);
      print(valueMap);
      education_type = jsonData!['qualifications'];
      vt = jsonData!['vt_types'];
      professional = jsonData!['profession'];
      social_category = jsonData!['social_category'];
      relationship = jsonData!['relationship'];
      proofType = jsonData!['proof_type'];
      family_head_role = jsonData!['family_head_role'];
    });
  }

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
              Navigator.of(context).pop();
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
                                //color: kFillaSurveyColor,
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
                                    Text(head_names!,
                                        style: kManageProfileheading),
                                    Text(
                                      Languages.of(context)!.House_Address,
                                      style: kManageProfile,
                                    ),
                                    Text(
                                      head_addresss!,
                                      style: kManageProfileheading,
                                    ),
                                    Text(
                                      Languages.of(context)!.Mark_As,
                                      style: kManageProfile,
                                    ),
                                    Text(
                                      //"Learner",
                                      showHeadMarkasValue(),
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
                                      inputFormatters: <TextInputFormatter>[
                                        FilteringTextInputFormatter.deny(RegExp(r'[0-9]')),
                                      ],
                                      // inputFormatters: <TextInputFormatter>[
                                      //   FilteringTextInputFormatter.allow(
                                      //       RegExp(r'[a-zA-Z]+|\s')),
                                      // ],
                                      // textCapitalization:
                                      //     TextCapitalization.sentences,
                                      controller: family_member_name,
                                      cursorColor: kSkipColor,
                                      maxLength: 100,
                                      // enableInteractiveSelection: true,
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
                                        } else if (value!.length > 100) {
                                          return "Name length should be less than 100 characters ";
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
                                      cursorColor: kSkipColor,
                                      inputFormatters: <TextInputFormatter>[
                                        FilteringTextInputFormatter.deny(RegExp(r'[0-9]')),
                                      ],
                                      // inputFormatters: <TextInputFormatter>[
                                      //   FilteringTextInputFormatter.allow(
                                      //       RegExp(r'[a-zA-Z]+|\s')),
                                      // ],
                                      // textCapitalization:
                                      //     TextCapitalization.sentences,
                                      controller: family_member_father,
                                      enableInteractiveSelection: true,
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
                                        errorStyle:
                                            TextStyle(color: Colors.red),
                                      ),
                                      validator: (String? value) {
                                        if (value!.length > 100)
                                          return "Name length should less than 100 characters ";
                                      }),
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
                                  validator: (value) => value == null
                                      ? Languages.of(context)!
                                          .Please_select_relation_to_head
                                      : null,
                                  items: relationship?.map((map) {
                                    return DropdownMenuItem(
                                      child: Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    print(value);
                                    family_member_relation = value;
                                    print(family_member_relation);
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
                                      onTap: (value) {
                                        print(value);
                                        if (value == 0) {
                                          setState(() {
                                            family_member_mark_as = LEARNER;
                                          });
                                        } else if (value == 1) {
                                          setState(() {
                                            family_member_mark_as = VT;
                                          });
                                        } else {
                                          setState(() {
                                            family_member_mark_as = "0";
                                          });
                                        }
                                        print(family_member_mark_as);
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
                                            } else if (family_member_mark_as ==
                                                VT) {
                                              if (int.parse(value) < 12 ||
                                                  int.parse(value) > 100) {
                                                return Languages.of(context)!
                                                    .Please_enter_valid_age;
                                              }
                                            } else if ((family_member_mark_as ==
                                                LEARNER)) {
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
                                mainAxisAlignment: MainAxisAlignment.center,
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
                                (_selectedValueIndexDivyang == null && flag == 1),
                                child: Padding(
                                  padding: const EdgeInsets.all(10.0),
                                  child: Align(
                                      alignment: Alignment.topLeft,
                                      child: Text(Languages.of(context)!.Please_select_divyang_type,
                                        // Languages.of(context)!
                                        //     .Please_select_Gender,
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
                                  validator: (value) => value == null
                                      ? Languages.of(context)!
                                          .Please_select_identity_type
                                      : null,
                                  items: proofType?.map((map) {
                                    return DropdownMenuItem(
                                      child: Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    print(value);
                                    family_member_proof_Identity_type = value;
                                    print(family_member_proof_Identity_type);
                                  },
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(12.0),
                                child: Container(
                                  child: TextFormField(
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
                                  validator: (value) => value == null
                                      ? Languages.of(context)!
                                          .Please_select_social_category
                                      : null,
                                  items: social_category?.map((map) {
                                    return DropdownMenuItem(
                                      child: Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    family_member_social_caterory = value;
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
                                  validator: (value) => value == null
                                      ? Languages.of(context)!
                                          .Please_select_profession_type
                                      : null,
                                  items: professional?.map((map) {
                                    return DropdownMenuItem(
                                      child: Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    family_member_profession = value;
                                    print(family_member_profession);
                                  },
                                ),
                              ),
                              Visibility(
                                visible: (family_member_mark_as == VT)
                                    ? true
                                    : false,
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
                                      member_educationtype = value;
                                      print(family_member_social_caterory);
                                    },
                                  ),
                                ),
                              ),
                              Visibility(
                                visible: (family_member_mark_as == VT)
                                    ? true
                                    : false,
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
                                      member_vtTYpe = value;
                                      print(member_vtTYpe);
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
                      Visibility(
                        visible: true,
                        child: Expanded(
                          flex: 5,
                          child: GestureDetector(
                            onTap: () {
                              setState(() {
                                flag = 1;
                              });
                              if (_formKey1.currentState!.validate()) {
                                if (validateGender(gender.toString(),
                                    family_member_relation.toString())) {
                                  if (validateIdType(
                                    family_member_proof_Identity_type,
                                    family_member_id_no.text.length,
                                    family_member_id_no.text.toString(),
                                  )) {
                                    if (member_educationtype == 0 &&
                                        family_member_mark_as == VT) {
                                      Fluttertoast.showToast(
                                        msg:
                                            "Please select any other Education type",
                                        toastLength: Toast.LENGTH_LONG,
                                        textColor: Colors.black,
                                        backgroundColor: Colors.white,
                                        fontSize: 18.0,
                                      );
                                    } else {
                                      if (member_vtTYpe == 0 &&
                                          family_member_mark_as == VT) {
                                        Fluttertoast.showToast(
                                          msg: "Please select any other VT type ",
                                          toastLength: Toast.LENGTH_LONG,
                                          textColor: Colors.black,
                                          backgroundColor: Colors.white,
                                          fontSize: 18.0,
                                        );
                                      } else {
                                        if(_selectedValueIndexDivyang==null){
                                          Fluttertoast.showToast(
                                            msg: "Please select divyang type",
                                            toastLength: Toast.LENGTH_LONG,
                                            textColor: Colors.black,
                                            backgroundColor: Colors.white,
                                            fontSize: 18.0,
                                          );
                                        }
                                        else{
                                          AddMoreMember();
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
                            child: Padding(
                              padding:
                                  const EdgeInsets.fromLTRB(20, 10, 20, 10),
                              child: Container(
                                height: 60,
                                decoration: BoxDecoration(
                                    color: kSendaSurveyColor,
                                    borderRadius: BorderRadius.circular(40)),
                                child: Center(
                                  child: Padding(
                                    padding: const EdgeInsets.all(4.0),
                                    child: Text(
                                      Languages.of(context)!.ADD_MEMBER,
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
                      )
                    ],
                  ),
                ],
              ),
            )),
          )),
        ));
  }

  void AddMoreMember() async {
    LocalHeadUpdateModel request = new LocalHeadUpdateModel(
      name: encrypt(head_names.toString()).base64,
      address: encrypt(head_addresss.toString()).base64,
      mobile: encrypt(head_mobile_nos.toString()).base64,
      age: (head_ages),
      markAs: head_mark_ass.toString(),
      gender: head_genders.toString(),
      proofDetail: head_ids.toString(),
      proofType: head_proofTypes.toString(),
      socialCategoryId: head_social_caterorys.toString(),
      profession: head_professions.toString(),
      qualification: head_eduTypes.toString(),
      vtType: head_VTtypes.toString(),
      fatherName: "",
      id: head_id_value,
      isdivyang:headisDivyang==false?0:1,
      isOnlyHead: 0,
    );
    final db = await squlitedb;
    int? res = await db.updateHeadData(request, head_id_value!);
    saveMemberDetailstoDB(head_id_value);
  }

  void saveMemberDetailstoDB(int id) async {
    MemberModel member_model = new MemberModel(
      memberName: encrypt(family_member_name.text.toString()).base64,
      head_id: id,
      memberAddress: encrypt(head_addresss.toString()).base64,
      memberMarkAs: family_member_mark_as.toString(),
      memberAge: family_member_age.text.toString(),
      memberMobile: encrypt(family_member_mobile_no.text.toString()).base64,
      memberGender: gender.toString(),
      memberProofDetail: family_member_id_no.text.toString(),
      memberProofType: family_member_proof_Identity_type.toString(),
      memberSocialCategoryId: family_member_social_caterory.toString(),
      memberQualification: member_educationtype.toString(),
      memberFatherName: family_member_father.text == ""
          ? ""
          : family_member_father.text.toString(),
      memberVtType: member_vtTYpe.toString(),
      memberProfession: family_member_profession.toString(),
      memberRelation: family_member_relation.toString(),
      memberDivyang: divyang==true?1:0,
    );
    final db = await squlitedb;
    await db.saveMember(member_model, id);
    Fluttertoast.showToast(
        msg: Languages.of(context)!.Member_added_Sccessfully,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => LocalManageRecord()));
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
            divyang = true;
          } else if (_selectedValueIndexDivyang == 1) {
            divyang = false;
          }
          print("div  $divyang ");
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
            gender = "m";
          } else if (_selectedValueIndex == 1) {
            gender = "f";
          } else if (_selectedValueIndex == 2) {
            gender = "t";
          }
          print(gender);
          print(_selectedValueIndex);
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
  void onResponseAddMember(ResponseAddMember responseDto) {
    if (responseDto.data.memberUpdateStatus || responseDto.data.updateStatus) {
      Navigator.pop(context);
      Fluttertoast.showToast(
          msg: utf8.decode(responseDto.data.updateErrorMessage.codeUnits),
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
      // showDialog(
      //     barrierDismissible: false,
      //     context: context,
      //     builder: (context) {
      //       return AlertDialog(
      //         title: Text(Languages.of(context)!.Thank_you),
      //         content: Text(
      //             Languages.of(context)!.Do_you_want_to_upload_more_member),
      //         actions: [
      //           TextButton(
      //             onPressed: () {
      //               Navigator.pop(context);
      //               Util.isHeadSaved = 0;
      //               Navigator.pushReplacement(
      //                   context,
      //                   MaterialPageRoute(
      //                       builder: (context) => DashBoard()));
      //             },
      //             child: Text(Languages.of(context)!.NO),
      //           ),
      //           TextButton(
      //             onPressed: () {
      //               // Navigator.pushReplacement(
      //               //     context,
      //               //     MaterialPageRoute(
      //               //         builder: (context) => NewLocalSurveyFamilyMemberAdd(
      //               //           head_name,
      //               //           head_address,
      //               //           head_mobile_no,
      //               //           head_id,
      //               //           head_age,
      //               //           // age,
      //               //           head_mark_as,
      //               //           head_gender,
      //               //           head_proofType,
      //               //           head_social_caterory,
      //               //           head_profession,
      //               //           head_eduType,
      //               //           head_VTtype,
      //               //           check_only_Head,
      //               //         )));
      //             },
      //             child: Text(Languages.of(context)!.YES),
      //           )
      //         ],
      //       );
      //     });
    } else {
      Navigator.pop(context);
      Fluttertoast.showToast(
          msg: utf8.decode(responseDto.data.updateErrorMessage.codeUnits),
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
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

  showHeadMarkasValue() {
    if (head_mark_ass == LEARNER) {
      return "LEARNER";
    } else if (head_mark_ass == VT) {
      return "VT";
    } else {
      return "NONE";
    }
  }

  Future<bool> _onBackPressed() async {
    return await showDialog(
        barrierDismissible: false,
        context: context,
        builder: (context) {
          return AlertDialog(
            // title: Text( Languages.of(context)!
            //     .TAGGED_Successfully),
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
                  /*  Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(
                          builder: (context) => SurveyFamilyHead()));*/
                  Navigator.pop(context);
                  Util util = new Util();
                  bool isOnline = await util.hasInternet();
                  if (isOnline) {
                    Navigator.pop(context);
                  } else {
                    Navigator.push(context,
                        MaterialPageRoute(builder: (context) => DashBoard()));
                  }
                },
                child: Text(Languages.of(context)!.OK,maxLines: 1,),
              )
            ],
          );
        });
  }

  @override
  void onResponseUpateMember(ResponseMemberUpdate responseDto1) {
    // TODO: implement onResponseUpateMember
  }
  void showNextMemberDailog() {
    showDialog(
        barrierDismissible: false,
        context: context,
        builder: (context) {
          return AlertDialog(
            title: Text(Languages.of(context)!.Thank_you),
            content:
                Text(Languages.of(context)!.Do_you_want_to_upload_more_member),
            actions: [
              TextButton(
                onPressed: () {
                  Util.isHeadSaved = 0;
                  Navigator.pop(context);
                  Navigator.pushReplacement(context,
                      MaterialPageRoute(builder: (context) => DashBoard()));
                },
                child: Text(Languages.of(context)!.NO),
              ),
              TextButton(
                onPressed: () {
                  // Navigator.pushReplacement(
                  //     context,
                  //     MaterialPageRoute(
                  //         builder: (context) => NewLocalSurveyFamilyMemberAdd(
                  //           head_name,
                  //           head_address,
                  //           head_mobile_no,
                  //           head_id,
                  //           head_age,
                  //           // age,
                  //           head_mark_as,
                  //           head_gender,
                  //           head_proofType,
                  //           head_social_caterory,
                  //           head_profession,
                  //           head_eduType,
                  //           head_VTtype,
                  //           check_only_Head,
                  //         )));
                },
                child: Text(Languages.of(context)!.YES),
              )
            ],
          );
        });
  }

  @override
  void onTokenExpire() {
    setCredentialslogout();
    Navigator.pushAndRemoveUntil<void>(
        context,
        MaterialPageRoute<void>(builder: (BuildContext context) => Login()),
        ModalRoute.withName('/'));
    print("token expire -- DASHBOARD");
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
      // getDashboradData();
    });
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
