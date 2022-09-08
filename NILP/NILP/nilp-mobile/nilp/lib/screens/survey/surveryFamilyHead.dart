import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:nilp/Database/SquliteDataBaseHelper.dart';
import 'package:nilp/model/response/ResponseGetRespondentType.dart';
import 'package:nilp/model/response/ResponseMemberUpdate.dart';
import 'package:nilp/screens/dashboard/dashboard.dart';
import 'package:nilp/screens/manage_record/local_manage_record.dart';
import 'package:nilp/screens/manage_record/manage_record.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../utility/testEncryption.dart';
import '../../constants/Strings.dart';
import '../../constants/UrlEndPoints.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../../model/DBModel/HeadModel.dart';
import '../../model/request/RequestAddMember.dart';
import '../../model/request/RequestRefreshToken.dart';
import '../../model/response/ResponseAddMember.dart';
import '../../model/response/ResponseRefreshToken.dart';
import '../../networking/AppSignatureUtil.dart';
import '../../presenter/PresenterAddMember.dart';
import '../../utility/Util.dart';
import '../../utility/testEncryption.dart';
import '../login/login.dart';
import 'SurveyFamilyMember.dart';

class SurveyFamilyHead extends StatefulWidget {
  @override
  _SurveyFamilyHeadState createState() => _SurveyFamilyHeadState();
}

class _SurveyFamilyHeadState extends State<SurveyFamilyHead>
    implements ResponseContract {
  Map? jsonData;
  List? professional;
  List? education_type;
  List? vt;
  var flag = 0;
  List? social_category;
  List? relationship;
  List? proofType;
  List? family_head_role;
  int? _selectedValueIndex;
  int? _selectedValueIndexDivyang;
  List<String> ?buttonText;
  List<String> ?buttonTextDivyang;
  SquliteDatabaseHelper squlitedb = new SquliteDatabaseHelper();
  GlobalKey<FormState> _formKey = GlobalKey<FormState>();

  TextEditingController family_head_name = TextEditingController();
  TextEditingController family_head_address = TextEditingController();
  TextEditingController head_mobile_no = TextEditingController();
  TextEditingController head_age = TextEditingController();
  TextEditingController head_id = TextEditingController();
  String head_mark_as = "0";
  late PresenterAddMember presenterAddOnlyHead;

  var head_proof_Identity_type,
      head_gender,
      head_divyang,
      head_social_caterory,
      head_profession,
      head_educationtype,
      head_vtTYpe,
      valueMap;
  bool checkBoxValue = false;
  int ui_flag = 0;

  void setCredentialslogout() async {
    SharedPreferences perf = await SharedPreferences.getInstance();
    perf.setString(LOGIN, "false");
    String? login = perf.getString(LOGIN);
  }

  @override
  initState() {
    presenterAddOnlyHead = new PresenterAddMember(this);
    getDropdownValue();
    super.initState();
  }

  getDropdownValue() async {
    SharedPreferences pref = await SharedPreferences.getInstance();
    String? dprofessions = await pref.getString("dropdowns");
    jsonData = jsonDecode(dprofessions!);
    valueMap = json.decode(dprofessions!);
    professional = jsonData!['profession'];
    education_type = jsonData!['qualifications'];
    vt = jsonData!['vt_types'];
    social_category = jsonData!['social_category'];
    relationship = jsonData!['relationship'];
    proofType = jsonData!['proof_type'];
    family_head_role = jsonData!['family_head_role'];
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
                  Padding(
                    padding: const EdgeInsets.only(left: 20, right: 20),
                    child: InkWell(
                      onTap: () {
                        navigations();
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
                                      Languages.of(context)!.Search_Survey,
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
                                        maxLength: 100,
                                        enableInteractiveSelection: true,
                                        controller: family_head_name,
                                        style: const TextStyle(
                                            color: kRefreshTextColor),
                                        decoration: InputDecoration(
                                          labelStyle: TextStyle(
                                              color: kRefreshTextColor),
                                          labelText: Languages.of(context)!
                                              .Family_Head_Name,
                                          counterText: "",
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
                                          } else if (value!.length > 100)
                                            return Languages.of(context)!.Name_length_should_be_less_than_100_characters;
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
                                          // textCapitalization:
                                          //     TextCapitalization.sentences,
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
                                          maxLength: 3,
                                          inputFormatters: <TextInputFormatter>[
                                            FilteringTextInputFormatter.deny(
                                                RegExp('[,.]')),
                                          ],
                                          controller: head_age,
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
                                        controller: head_mobile_no,
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
                                  )
                                ],
                              ),
                              Padding(
                                  padding: EdgeInsets.all(12.0),
                                  child: Align(
                                      alignment: Alignment.centerLeft,
                                      child: Text(Languages.of(context)!.Gender,
                                          style: kDashBoardBoxBody))),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  ...List.generate(
                                   buttonText!.length,
                                    (index) => Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Center(
                                        child: button(
                                          index: index,
                                        text: buttonText![index],
                                        ),
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
                              /////////////////   start    /////////////////////////////


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
                                            text:buttonTextDivyang![index],
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
                              ////////////////////////////////////////////////////


                              Padding(
                                padding:
                                    const EdgeInsets.fromLTRB(1, 10, 1, 10),
                                child: DropdownButtonFormField(
                                  autofocus: false,
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
                                    head_proof_Identity_type = value;
                                    print(head_proof_Identity_type);
                                  },
                                ),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(12.0),
                                child: Container(
                                  child: TextFormField(
                                      autofocus: false,
                                      controller: head_id,
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
                                        String pattern = r'^[a-zA-Z0-9]';
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
                                  validator: (value) => value == null
                                      ? Languages.of(context)!
                                          .Please_select_social_category
                                      : null,
                                  items: social_category?.map((map) {
                                    return DropdownMenuItem(
                                      child:Text(map[dropdownConvertLang()]),
                                      value: map['id'],
                                    );
                                  }).toList(),
                                  onChanged: (value) {
                                    head_social_caterory = value;
                                    print(head_social_caterory);
                                  },
                                ),
                              ),
                              Padding(
                                padding: EdgeInsets.fromLTRB(1, 10, 1, 10),
                                child: DropdownButtonFormField(
                                  decoration: InputDecoration(
                                    labelText:
                                        Languages.of(context)!.Profession_Type,
                                    labelStyle: TextStyle(
                                      color: kRefreshTextColor,
                                    ),
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
                                    head_profession = value;
                                    print(head_profession);
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
                                      head_educationtype = value;
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
                                      head_vtTYpe = value;
                                      print(head_vtTYpe);
                                    },
                                  ),
                                ),
                              ),
                              Row(
                                children: <Widget>[
                                  new Checkbox(
                                      value: checkBoxValue,
                                      activeColor: kSendaSurveyColor,
                                      onChanged: (bool? newValue) {
                                        setState(() {
                                          checkBoxValue = newValue!;
                                          print(newValue);
                                        });
                                      }),
                                  Text(
                                    Languages.of(context)!.Add_head_of_family_only,
                                    style: kDropdownTileStyle,
                                  )
                                ],
                              )
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                  Visibility(
                    visible: checkBoxValue == false ? true : false,
                    child: Container(
                      margin: EdgeInsets.fromLTRB(20, 10, 20, 10),
                      child: Row(
                        children: [
                          Expanded(
                            child: GestureDetector(
                              onTap: () {
                                setState(() {
                                  flag = 1;
                                });
                                family_head_address.text =
                                    family_head_address.text.trim();
                                if (_formKey.currentState!.validate() &&
                                    _selectedValueIndex != null&&_selectedValueIndexDivyang !=null) {
                                  if (validateIdType(
                                      head_proof_Identity_type,
                                      head_id.text.length,
                                      head_id.text.toString())) {
                                    if (head_educationtype == 0 &&
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
                                      if (head_vtTYpe == 0 &&
                                          head_mark_as == VT) {
                                        Fluttertoast.showToast(
                                          msg:
                                          Languages.of(context)!.Please_select_any_other_VT_type,
                                          textColor: Colors.black,
                                          backgroundColor: Colors.white,
                                          toastLength: Toast.LENGTH_LONG,
                                          fontSize: 18.0,
                                        );
                                      } else {
                                        Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                                builder: (context) =>
                                                    SurveyFamilyMember(
                                                      family_head_name.text
                                                          .trim(),
                                                      family_head_address.text
                                                          .trim(),
                                                      head_mobile_no.text
                                                          .trim(),
                                                      head_id.text.trim(),
                                                      head_age.text,
                                                      head_mark_as,
                                                      head_gender,
                                                      head_divyang,
                                                      head_proof_Identity_type,
                                                      head_social_caterory,
                                                      head_profession,
                                                      head_educationtype,
                                                      head_vtTYpe,
                                                      checkBoxValue,
                                                      ui_flag,
                                                    )));
                                      }
                                    }
                                  } else {
                                    Fluttertoast.showToast(
                                        msg: Languages.of(context)!.Please_enter_valid_id,
                                        toastLength: Toast.LENGTH_SHORT,
                                        gravity: ToastGravity.BOTTOM,
                                        timeInSecForIosWeb: 1,
                                        textColor: Colors.black,
                                        backgroundColor: Colors.white,
                                        fontSize: 16.0);
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
                          )
                        ],
                      ),
                    ),
                  ),
                  Visibility(
                    visible: checkBoxValue == true ? true : false,
                    child: Container(
                      margin: EdgeInsets.fromLTRB(20, 10, 20, 10),
                      child: GestureDetector(
                        onTap: () {
                          setState(() {
                            flag = 1;
                          });
                          family_head_address.text =
                              family_head_address.text.trim();
                          if (_formKey.currentState!.validate() &&
                              _selectedValueIndex != null && _selectedValueIndexDivyang!=null) {
                            if (validateIdType(head_proof_Identity_type,
                                head_id.text.length, head_id.text.toString())) {
                              if (head_educationtype == 0 &&
                                  head_mark_as == VT) {
                                Fluttertoast.showToast(
                                  msg: Languages.of(context)!.Please_select_any_other_Education_type,
                                  toastLength: Toast.LENGTH_LONG,
                                  textColor: Colors.black,
                                  backgroundColor: Colors.white,
                                  fontSize: 18.0,
                                );
                              } else {
                                if (head_vtTYpe == 0 && head_mark_as == VT) {
                                  Fluttertoast.showToast(
                                    msg: Languages.of(context)!.Please_select_any_other_VT_type,
                                    toastLength: Toast.LENGTH_LONG,
                                    textColor: Colors.black,
                                    backgroundColor: Colors.white,
                                    fontSize: 18.0,
                                  );
                                } else {
                                  addOnlyHead();
                                }
                              }
                            } else {
                              Fluttertoast.showToast(
                                  msg: Languages.of(context)!.Proof_Identity_Type_not_valid,
                                  toastLength: Toast.LENGTH_SHORT,
                                  gravity: ToastGravity.BOTTOM,
                                  timeInSecForIosWeb: 1,
                                  textColor: Colors.black,
                                  backgroundColor: Colors.white,
                                  fontSize: 16.0);
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
                                Languages.of(context)!.Add_Details,
                                // Languages.of(context)!.ADD_MEMBER,
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
                  SizedBox(
                    height: 20,
                  )
                ],
              ),
            ))),
          ),
        ));
  }

  Widget _customPopup(BuildContext context, String? item, bool isSelected) {
    return Container(
      height: 50,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
              height: 49,
              alignment: Alignment.centerLeft,
              margin: EdgeInsets.fromLTRB(15, 0, 0, 0),
              child: Text(
                item!,
                style: kDropdownTileStyle,
              )),
          Container(
            height: 1,
            color: kSeperatorColor,
          )
        ],
      ),
    );
  }

  Widget button({required String text, required int index}) {
    return InkWell(
      splashColor: Colors.cyanAccent,
      onTap: () {
        setState(() {
          _selectedValueIndex = index;
          if (_selectedValueIndex == 0) {
            head_gender = "m";
          } else if (_selectedValueIndex == 1) {
            head_gender = "f";
          } else if (_selectedValueIndex == 2) {
            head_gender = "t";
          }
          print(head_gender);
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
  Future<void> getValue() async {
    await getDropdownValue();
  }

  Future<ResponseAddMember?> addOnlyHead() async {
    int timestamp = DateTime.now().millisecondsSinceEpoch;
    AppSignatureUtil appSignatureUtil = new AppSignatureUtil();
    RequestAddMember request = new RequestAddMember(
      fullname: encrypt(family_head_name.text.trim().toString()).base64,
      address: encrypt(family_head_address.text.trim().toString()).base64,
      age: head_age.text,
      markAs: head_mark_as.toString(),
      mobile: encrypt(head_mobile_no.text.trim().toString()).base64,
      gender: head_gender.toString(),
      proofDetail: head_id.text.toString(),
      proofType: head_proof_Identity_type.toString(),
      socialCategoryId: head_social_caterory.toString(),
      profession: head_profession.toString(),
      vt_type: head_vtTYpe.toString(),
      qualification: head_educationtype.toString(),
      is_only_member: checkBoxValue,
      is_divyang: head_divyang,
    );
    String url = BASE_URL + ADD_MEMBER;
    String signature = appSignatureUtil.generateSignature(
        url, "null", 0, timestamp.toString(), request.toMap());
    SharedPreferences pref = await SharedPreferences.getInstance();

    String? acesstoken = decrypt(pref.getString(ACESSTOKEN).toString());
    Util util = new Util();
    bool isOnline = await util.hasInternet();

    if (isOnline) {
      loader();
      return presenterAddOnlyHead.addOnlySingleMember(request.toMap(),
          signature, timestamp.toString(), ADD_MEMBER, context, acesstoken);
    } else {
      saveHeadDetailstoDB();
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

  @override
  void onResponseAddMember(ResponseAddMember responseDto) {
    // TODO: implement onResponseAddMember
    if(responseDto.data.updateErrorMessage!=""){
      Navigator.pop(context);
      Fluttertoast.showToast(
          msg: utf8.decode(responseDto.data.updateErrorMessage.codeUnits),
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.BOTTOM,
          timeInSecForIosWeb: 1,
          textColor: Colors.black,
          backgroundColor: Colors.white,
          fontSize: 16.0);
    }else{
      Navigator.pop(context);
      Navigator.push(
          context, MaterialPageRoute(builder: (context) => DashBoard()));
      Fluttertoast.showToast(
          msg: utf8.decode(responseDto.data.updateSuccessMessage.codeUnits),
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

  @override
  void onResponseUpateMember(ResponseMemberUpdate responseDto1) {
    // TODO: implement onResponseUpateMember
  }
  void saveHeadDetailstoDB() async {
    if (Util.isHeadSaved == 0) {
      HeadModel headmodel = new HeadModel(
        name: encrypt(family_head_name.text.toString()).base64,
        address: encrypt(family_head_address.text.toString()).base64,
        markAs: head_mark_as.toString(),
        age: head_age.text.toString(),
        mobile: encrypt(head_mobile_no.text.toString()).base64,
        gender: head_gender.toString(),
        proofDetail: head_id.text.toString(),
        proofType: head_proof_Identity_type.toString(),
        socialCategoryId: head_social_caterory.toString(),
        qualification: head_educationtype.toString(),
        fatherName: encrypt(family_head_name.text.toString()).base64,
        vtType: head_vtTYpe.toString(),
        profession: head_profession!.toString(),
        isOnlyHead: checkBoxValue ? 1 : 0,
        isdivyang: head_divyang==true?1:0,
      );
      final db = await squlitedb;
      int? id = await db.saveHead(headmodel);
      // Util.isHeadSaved = 0;
      // Util.headID = id!;
    }
    // Navigator.pop(context);
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => DashBoard()));
    Fluttertoast.showToast(
        msg: Languages.of(context)!.Record_saved_locally,
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.BOTTOM,
        timeInSecForIosWeb: 1,
        textColor: Colors.black,
        backgroundColor: Colors.white,
        fontSize: 16.0);
  }

  Future<void> navigations() async {
    Util util = Util();
    bool isOnline = await util.hasInternet() as bool;
    if (isOnline) {
      Navigator.push(
          context, MaterialPageRoute(builder: (context) => ManageRecord()));
    } else {
      Navigator.push(context,
          MaterialPageRoute(builder: (context) => LocalManageRecord()));
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
                  Navigator.push(context,
                      MaterialPageRoute(builder: (context) => DashBoard()));
                },
                child: Text(Languages.of(context)!.OK,maxLines: 1,),
              )
            ],
          );
        });
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
      addOnlyHead();
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
