import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:nilp/screens/manage_tag/Tagging/searchTag.dart';
import '../../constants/constants.dart';
import '../../localization/language/languages.dart';
import '../dashboard/dashboard.dart';
import '../manage_record/local_manage_record.dart';
import '../survey/surveryFamilyHead.dart';
import 'EditTag/searchEditTag.dart';
class ManageTag extends StatefulWidget {

  @override
  _ManageTagState createState() => _ManageTagState();
}

class _ManageTagState extends State<ManageTag> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kHomeBgColor,
      appBar: AppBar(
        leading: IconButton(
          icon: const Icon(Icons.arrow_back_ios_outlined),
          onPressed: () {
            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) =>
                        DashBoard()));
          },
        ),
        title: Text(
          Languages.of(context)!.Manage_Tag,
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
                Navigator.push(context,
                    MaterialPageRoute(builder: (context) => LocalManageRecord()));
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
      body: SafeArea(
        child:Column(
          children: [
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
                                            SearchTag()));
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
                                    padding: const EdgeInsets.all(8.0),
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
                                                  Languages.of(context)!
                                                      .TAG,
                                                  style: kDashboardText,
                                                ),
                                              ),
                                              Spacer(),
                                              Icon(
                                                Icons.arrow_forward_ios,
                                                size: 30,
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
                                            SearchEditTag())
                                );
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
                                    padding: const EdgeInsets.all(8.0),
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
                                                  Languages.of(context)!.Edit_Tag,
                                                  // Languages.of(context)!
                                                  //     .SURVEY,
                                                  style: kDashboardText,
                                                ),
                                              ),
                                              Spacer(),
                                              Icon(
                                                Icons.arrow_forward_ios,
                                                size: 30,
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
                      ],
                    )),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
