import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/screens/dashboard/dashboard.dart';
import 'package:nilp/utility/testEncryption.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../Database/SquliteDataBaseHelper.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/DBModel/localHeadUpdateModel.dart';
import '../../model/FamilyInfo.dart';
import '../../presenter/PresenterAddMember.dart';
import '../../utility/Util.dart';
import '../manage_record/local_manage_record.dart';
import 'SurveyFamilyMember.dart';
import 'newLocalSurveyFamilyMemberAdd.dart';

class SurveyLocalFamilyHeadEdit extends StatefulWidget {
  SurveyLocalFamilyHeadEdit(
    var family_head_name,
    family_head_address1,
    family_head_mark_as,
    family_head_age,
    family_head_mobile_no,
    family_head_gender,
    family_head_proofType,
    family_head_proofDetail,
    family_head_social_caterory,
    family_head_profession,
    family_head_qualification,
    family_head_vt_type,
      family_head_isdivyang,
    family_head_member_type,
    family_head_id,
  ) {
    head_name = family_head_name;
    head_address = family_head_address1;
    head_mark_as = family_head_mark_as;
    head_age = family_head_age;
    head_mobile_no = family_head_mobile_no;

    head_genders = family_head_gender;
    head_proofType = family_head_proofType;
    head_proof_detail = family_head_proofDetail;
    head_social_caterorys = family_head_social_caterory;
    head_profession_type = family_head_profession;
    head_educationtype = family_head_qualification;
    head_vtTYpe = family_head_vt_type;
    head_divyang = family_head_isdivyang=="1"||family_head_isdivyang==1?true:false;
    head_member_TYpe = family_head_member_type;
    head_id = family_head_id;
  }
  @override
  _SurveyLocalFamilyHeadEditState createState() =>
      _SurveyLocalFamilyHeadEditState();
}

late PresenterAddMember presenterAddMember;

var head_proof_Identity_type,
    head_genders,
    head_mark_as,
    family_head_social_caterory,
    head_profession_type,
    head_educationtype,
    head_vtTYpe,
    family_head_education,
    head_social_caterorys,
    head_proof_detail,
    family_head_vt,
    head_member_TYpe;

var valueMap,head_is_divyang;
var store_head_markas_value;

class _SurveyLocalFamilyHeadEditState extends State<SurveyLocalFamilyHeadEdit>
    with SingleTickerProviderStateMixin {
  SquliteDatabaseHelper squlitedb = new SquliteDatabaseHelper();
  Util util = new Util();

  Map? jsonData;
  List? professional;
  List? education_type;
  List? vt;
  List? social_category;
  List? relationship;
  List? proofType;
  List? family_head_role;
  int? _selectedValueIndex;
  List<String> ?buttonText ;
  List<String> ?buttonTextDivyang;
  int? _selectedValueIndexDivyang;


  GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  TextEditingController family_head_name = TextEditingController();
  TextEditingController family_head_address = TextEditingController();
  TextEditingController family_head_mobile_no = TextEditingController();
  TextEditingController family_head_age = TextEditingController();
  TextEditingController family_head_proofDetail = TextEditingController();

  int flag = 0;

  @override
  void initState() {
    family_head_name.text = head_name.toString();
    family_head_address.text = head_address.toString();
    family_head_mobile_no.text = head_mobile_no.toString();
    family_head_age.text = head_age.toString();
    family_head_proofDetail.text = head_proof_detail.toString();
    head_gender = head_gender.toString();
    head_mark_as = head_mark_as.toString();
    family_head_social_caterory = head_social_caterorys.toString();
    _tabController.index = getTab();
    _selectedValueIndex = genderValue();
    _selectedValueIndexDivyang =  isdivyangValue();
    getDropdownValue();
    store_head_markas_value = head_mark_as;
    super.initState();
  }

  getDropdownValue() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? dprofessions = await pref.getString("dropdowns");
    setState(() {
      jsonData = jsonDecode(dprofessions!);
      valueMap = json.decode(dprofessions!);
      print(valueMap);
      professional = jsonData!['profession'];
      social_category = jsonData!['social_category'];
      relationship = jsonData!['relationship'];
      education_type = jsonData!['qualifications'];
      vt = jsonData!['vt_types'];
      proofType = jsonData!['proof_type'];
      family_head_role = jsonData!['family_head_role'];
    });
  }

  late final _tabController = TabController(length: 3, vsync: this);

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
        child: Form(
            key: _formKey,
            child: SafeArea(
                child: SingleChildScrollView(
                    child: Container(
              color: kHomeBgColor,
              child: Column(
                children: [
                  Wrap(
                    children: [
                      Padding(
                        padding: EdgeInsets.fromLTRB(20, 0, 20, 5),
                        child: Container(
                          margin: EdgeInsets.only(top: 20),
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
                                              .Family_Head_Details,
                                          style: kBodyTextStyle,
                                        ))),
                              ),
                              Column(
                                children: [
                                  Padding(
                                    padding: const EdgeInsets.all(12.0),
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
                                        enableInteractiveSelection: true,
                                        controller: family_head_name,
                                        style: const TextStyle(
                                            color: kRefreshTextColor),
                                        decoration: InputDecoration(
                                          labelStyle: TextStyle(
                                              color: kRefreshTextColor),
                                          labelText: Languages.of(context)!
                                              .Family_Head_Name,
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
                                  Padding(
                                    padding: const EdgeInsets.all(12.0),
                                    child: Container(
                                      child: TextFormField(
                                          keyboardType:
                                              TextInputType.streetAddress,
                                          autofocus: false,
                                          cursorColor: kSkipColor,
                                          enableInteractiveSelection: true,
                                          controller: family_head_address,
                                          style: const TextStyle(
                                              color: kRefreshTextColor),
                                          decoration: InputDecoration(
                                            labelStyle: TextStyle(
                                                color: kRefreshTextColor),
                                            labelText: Languages.of(context)!
                                                .House_Address,
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
                                          validator: (String? value1) {
                                            if (value1!.isEmpty) {
                                              return Languages.of(context)!
                                                  .Please_enter_your_address;
                                            }
                                          }),
                                    ),
                                  )
                                ],
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
                                  length: 3,
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
                                            head_mark_as = "0";
                                          });
                                        } else if (value == 1) {
                                          setState(() {
                                            head_mark_as = LEARNER;
                                          });
                                        } else if (value == 2) {
                                          setState(() {
                                            head_mark_as = VT;
                                          });
                                        } else {
                                          head_mark_as = "0";
                                        }
                                        print(head_mark_as);
                                      },
                                      isScrollable: false,
                                      unselectedLabelColor: kBottomSheetTexts,
                                      labelColor: kBottomSheetTexts,
                                      indicator: BoxDecoration(
                                          color: kDrawerSheetTextColor),
                                      tabs: [
                                        Tab(
                                          child:
                                              Text(Languages.of(context)!.None),
                                        ),
                                        Tab(
                                          child: Text(
                                            Languages.of(context)!.Learner,
                                          ),
                                        ),
                                        Tab(
                                          child: Text(Languages.of(context)!.VT),
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
                                          controller: family_head_age,
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
                                            } else if (head_mark_as == VT) {
                                              if (int.parse(value) < 12 ||
                                                  int.parse(value) > 100) {
                                                return Languages.of(context)!
                                                    .Please_enter_valid_age;
                                              }
                                            } else if ((head_mark_as ==
                                                    LEARNER) ||
                                                (head_mark_as == "0")) {
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
                                        enableInteractiveSelection: false,
                                        maxLength: 10,
                                        inputFormatters: <TextInputFormatter>[
                                          FilteringTextInputFormatter.deny(
                                              RegExp('[,.]')),
                                        ],
                                        controller: family_head_mobile_no,
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
                                          style: kDashBoardBoxBody))),
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









                              Padding(
                                  padding: EdgeInsets.all(12.0),
                                  child: Align(
                                      alignment: Alignment.centerLeft,
                                      child: Text(Languages.of(context)!.Is_Divyangjan +"?",
                                          style: kDashBoardBoxBody))),
                              Padding(
                                padding: const EdgeInsets.only(left: 8),
                                child: Row(
                                  mainAxisAlignment: MainAxisAlignment.start,
                                  children: [
                                    ...List.generate(
                                      buttonTextDivyang!.length,
                                          (index) => Padding(
                                        padding: const EdgeInsets.all(8.0),
                                        child: Center(
                                          child: buttonDivyang(
                                            index: index,
                                            text: buttonTextDivyang![index],
                                          ),
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








                              Padding(
                                padding:
                                    const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                child: DropdownButtonFormField(
                                  decoration: InputDecoration(
                                    labelText: Languages.of(context)!
                                        .Select_Identity_Type,
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
                                  value: int.parse(head_proofType),
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
                                    // print(value);
                                    head_proofType = value.toString();
                                    //print(head_proofType);
                                  },
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(12.0),
                                child: Container(
                                  child: TextFormField(
                                      autofocus: false,
                                      controller: family_head_proofDetail,
                                      cursorColor: kSkipColor,
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
                                  value: int.parse(family_head_social_caterory),
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
                                    family_head_social_caterory =
                                        value.toString();
                                    print(family_head_social_caterory);
                                  },
                                ),
                              ),
                              Padding(
                                padding: EdgeInsets.fromLTRB(1, 10, 1, 10),
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
                                  value: int.parse(head_profession_type),
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
                                    head_profession_type = value.toString();
                                    print(head_profession_type);
                                  },
                                ),
                              ),
                              Visibility(
                                visible: (head_mark_as == VT) ? true : false,
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
                                    value: head_educationtype == "null"
                                        ? int.parse("0")
                                        : int.parse(head_educationtype),
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
                                      head_educationtype = value.toString();
                                      print(head_educationtype);
                                    },
                                  ),
                                ),
                              ),
                              Visibility(
                                visible: (head_mark_as == VT) ? true : false,
                                child: Padding(
                                  padding:
                                      const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                  child: DropdownButtonFormField(
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
                                    value: head_vtTYpe == "null"
                                        ? int.parse("0")
                                        : int.parse(head_vtTYpe),
                                    isExpanded: true,
                                    validator: (value) => value == null
                                        ? Languages.of(context)!
                                            .Please_select_VT
                                        : null,
                                    items: vt?.map((map) {
                                      return DropdownMenuItem(
                                        child: Wrap(
                                          children: [
                                            Text(map[dropdownConvertLang()]),
                                          ],
                                        ),
                                        value: map['id'],
                                      );
                                    }).toList(),
                                    onChanged: (value) {
                                      head_vtTYpe = value.toString();
                                      print(head_vtTYpe);
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
                  Container(
                    margin: EdgeInsets.fromLTRB(20, 10, 20, 10),
                    child: Row(
                      children: [
                        Expanded(
                          flex: 5,
                          child: Padding(
                            padding: const EdgeInsets.all(10),
                            child: GestureDetector(
                              onTap: () {
                                Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) =>
                                            NewLocalSurveyFamilyMemberAdd(
                                                family_head_name.text,
                                                family_head_address.text,
                                                head_mark_as,
                                                family_head_age.text,
                                                family_head_mobile_no.text,
                                                head_genders,
                                                head_proofType,
                                                family_head_proofDetail.text,
                                                head_social_caterorys,
                                                head_profession_type,
                                                head_educationtype,
                                                head_vtTYpe,
                                                head_member_TYpe,
                                                head_divyang,
                                                head_id)));
                              },
                              child: Container(
                                height: 60,
                                decoration: BoxDecoration(
                                    color: kSendaSurveyColor,
                                    borderRadius: BorderRadius.circular(40)),
                                child: Center(
                                  child: Padding(
                                    padding: EdgeInsets.all(4.0),
                                    child: Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.center,
                                      children: [
                                        Text(
                                          Languages.of(context)!.ADD_MEMBER,
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
                        Expanded(
                          flex: 5,
                          child: GestureDetector(
                            onTap: () {
                              if (_formKey.currentState!.validate()) {
                                if ((head_educationtype == "null" ||
                                    head_educationtype == "0") &&
                                        head_mark_as == VT) {
                                  Fluttertoast.showToast(
                                    msg:
                                    Languages.of(context)!.Please_select_any_other_Education_type,
                                    toastLength: Toast.LENGTH_LONG,
                                    textColor: Colors.black,
                                    backgroundColor: Colors.white,
                                    fontSize: 18.0,
                                  );
                                } else {
                                  if ((head_vtTYpe == "null" ||
                                      head_vtTYpe == "0" )&&
                                          head_mark_as == VT) {
                                    Fluttertoast.showToast(
                                      msg:  Languages.of(context)!.Please_select_any_other_VT_type,
                                      toastLength: Toast.LENGTH_LONG,
                                      textColor: Colors.black,
                                      backgroundColor: Colors.white,
                                      fontSize: 18.0,
                                    );
                                  } else {
                                    if (validateIdType(
                                      int.parse(head_proofType),
                                      family_head_proofDetail.text.length,
                                      family_head_proofDetail.text.toString(),
                                    )) {
                                      if (store_head_markas_value ==
                                          head_mark_as) {
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
                                                    // tabcolor = 1;
                                                    Navigator.pop(context);
                                                    updateHead();
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
                                                  Languages.of(context)!.Do_you_want_to_change_the_role_to +
                                                      util.getRole(
                                                          head_mark_as)),
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
                                                    // tabcolor = 1;
                                                    Navigator.pop(context);
                                                    updateHead();
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
                                    } else {
                                      Fluttertoast.showToast(
                                          msg: Languages.of(context)!.Please_enter_valid_ID_Number,
                                          toastLength: Toast.LENGTH_SHORT,
                                          gravity: ToastGravity.BOTTOM,
                                          timeInSecForIosWeb: 1,
                                          textColor: Colors.black,
                                          backgroundColor: Colors.white,
                                          fontSize: 16.0);
                                    }
                                  }
                                }
                              }
                            },
                            child: Container(
                              height: 60,
                              decoration: BoxDecoration(
                                  color: kSendaSurveyColor,
                                  borderRadius: BorderRadius.circular(40)),
                              child: Center(
                                child: Padding(
                                  padding: const EdgeInsets.all(4.0),
                                  child: Text(
                                    Languages.of(context)!.UPDATE,
                                    textAlign: TextAlign.center,
                                    style: GoogleFonts.poppins(
                                      textStyle: kButtonTextStyle,
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ),
                        )
                      ],
                    ),
                  ),
                  SizedBox(
                    height: 20,
                  )
                ],
              ),
            )))),
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
            head_divyang = true;
          } else if (_selectedValueIndexDivyang == 1) {
            head_divyang = false;
          }
          print("div  $head_divyang ");
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
            head_genders = "m";
          } else if (_selectedValueIndex == 1) {
            head_genders = "f";
          } else if (_selectedValueIndex == 2) {
            head_genders = "t";
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

  int getTab() {
    if (head_mark_as == "0")
      return 0;
    else if (head_mark_as == LEARNER)
      return 1;
    else
      return 2;
  }

  genderValue() {
    if (head_genders == "m") {
      return 0;
    } else if (head_genders == "f") {
      return 1;
    } else {
      return 2;
    }
  }

  isdivyangValue() {
    if (head_divyang == true) {
      return 0;
    } else if (head_divyang == false) {
      return 1;
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

  List<Family_Head>? lstdata;
  getData() async {
    final db = await squlitedb;
    lstdata = await db.getUser();

    return lstdata;
  }

  String getValue(String mark) {
    if (mark == "0") {
      return "None";
    } else if (mark == "8ecf3868-03b7-4ea1-8520-e619d793ed7d") {
      return "VT";
    } else if (mark == "8ca80f73-afd2-4d3f-b41d-e5bea0254b98") {
      return "LEARNER";
    } else {
      return "None";
    }
  }

  void updateHead() async {
    LocalHeadUpdateModel request = new LocalHeadUpdateModel(
      name: encrypt(family_head_name.text.toString()).base64,
      address: encrypt(family_head_address.text.toString()).base64,
      mobile: encrypt(family_head_mobile_no.text.toString()).base64,
      age: (family_head_age.text),
      markAs: head_mark_as.toString(),
      gender: head_genders.toString(),
      proofDetail: family_head_proofDetail.text.toString(),
      proofType: head_proofType.toString(),
      socialCategoryId: family_head_social_caterory.toString(),
      profession: head_profession_type.toString(),
      qualification: head_educationtype.toString(),
      vtType: head_vtTYpe.toString(),
      fatherName: "",
      id: head_id,
      isOnlyHead: int.parse(head_member_TYpe),
      isdivyang: int.parse(head_divyang==true?"1":"0"),
    );

    final db = await squlitedb;
    int? res = await db.updateHeadData(request, head_id!);
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
