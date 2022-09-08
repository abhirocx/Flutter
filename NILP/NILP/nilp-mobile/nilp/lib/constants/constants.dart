import 'package:flutter/material.dart';

bool isLoader = false;
const kBodyTextColor = Color(0xFFA8A8A8);
const khtmlBgColor = Color(0xFFF9F9F9);
const kOTPFieldColour = Color(0x0f037a92);
const kSkipColor = Color(0xFFA5A5A5);
const kOnboardingBodyTextColor = Color(0xFF383838);
const kBg = Color(0xFFFFFFFF);
const kSendaSurveyColor = Color(0xFFF38332);
const kFillaSurveyColor = Color(0xFF288D85);
const kDashboardgreyBox = Color(0xFFF2F4F4);
const kRefreshTextColor = Color(0xFF131313);
const kSeperatorColor = Color(0xFFDEE6F8);
const kDrawableDividerColor = Color(0xff39afa6);
const kloginBackgrondColor = Color(0xfffff3d6);
//////////////////
const kBottomDividerColor = Color(0xFFA4A4A4);
const kBottomSheetText = Color(0xFF202020);
const kBottomSheetTexts = Color(0xff4A4A4A);
const kDrawerSheetTextColor = Color(0xFFFDBF44);
const kManageRecordColor = Color(0xFF26C0D8);
const kHomeSurveyColor = Color(0xFFF389B7);
const kHomeTagColor = Color(0xFFE27575);
const kHomeRecordColor = Color(0xFF8CC493);
const kHomeProfileColor = Color(0xFF68CFB8);
const kHomeBgColor = Color(0xFFF1F1F1);
const kTagColor = Color(0xffF9F9F9);
const kSadowColor = Color(0xFF7E7E7E);

// All TextStyles
const kButtonTextStyle = TextStyle(
  fontWeight: FontWeight.bold,
  fontSize: 16,
  color: Colors.white,
  fontFamily: 'Poppins',
);

const kDirectRegistrationStyle = TextStyle(
  fontWeight: FontWeight.w600,
  fontSize: 16,
  color: kDrawerSheetTextColor,
  fontFamily: 'Poppins',
);

const kUploadButtonTextStyle = TextStyle(
  fontWeight: FontWeight.bold,
  fontSize: 16,
  color: Colors.black,
  fontFamily: 'Poppins',
);

const kSkip = TextStyle(
  color: kSkipColor,
  fontFamily: 'Poppins',
);

const kHeadingTextStyle = TextStyle(
    fontWeight: FontWeight.bold,
    fontSize: 20,
    letterSpacing: 0.6,
    fontFamily: 'Poppins',
    color: kBg);

const kHeadingTextStyles = TextStyle(
    fontWeight: FontWeight.w700,
    fontSize: 15,
    fontFamily: 'Poppins',
    color: kBodyTextColor);


const kBodyTextStyle = TextStyle(
    fontWeight: FontWeight.bold,
    fontSize: 16,
    letterSpacing: 0.2,
    fontFamily: 'Poppins',
    color: kBg);
const kOnboardingHeadingTextStyle = TextStyle(
  fontWeight: FontWeight.bold,
  fontSize: 30,
  height: 1.2,
  fontFamily: 'Poppins',
);
const kOnboardingBodyTextStyle = TextStyle(
  fontSize: 12,
  color: kOnboardingBodyTextColor,
  fontFamily: 'Poppins',
);
const kOnboardingTTCTextStyle = TextStyle(
  fontSize: 12,
  color: kSkipColor,
  fontFamily: 'Poppins',
);

const kTagBody = TextStyle(
  color: Color(0xff4A4A4A),
  fontSize: 11,
  fontWeight: FontWeight.bold,
  fontFamily: 'Poppins',
);
const kTagSubHeading = TextStyle(
  color: Color(0xff4A4A4A),
  fontSize: 10,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
);

const kTagSubHead = TextStyle(
  color: Color(0xff131313),
  fontSize: 14,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
);
const kDashBoardBoxBody = TextStyle(
  color: Color(0xff4A4A4A),
  fontSize: 14,
  fontWeight: FontWeight.bold,
  fontFamily: 'Poppins',
);
const kDashBoardBoxBodyS = TextStyle(
  color: Color(0xff4A4A4A),
  fontSize: 16,
  fontWeight: FontWeight.bold,
  fontFamily: 'Poppins',
);
const kYourDashBoardStyle = TextStyle(
  fontSize: 18,
  fontWeight: FontWeight.bold,
  color: kFillaSurveyColor,
  fontFamily: 'Poppins',
);

const kDropdownTileStyle = TextStyle(
  fontSize: 14,
  fontFamily: 'Poppins',
  color: Color(0xFF1C1D29),
  fontWeight: FontWeight.w400,
);

const kCongratulationsStyle = TextStyle(
  fontSize: 28,
  fontFamily: 'Poppins',
  letterSpacing: 0.3,
  fontWeight: FontWeight.bold,
  //fontStyle: FontStyle.italic,
  color: Color(0xFFF38332),
);
const kSurveySentSuccess = TextStyle(
  fontSize: 15,
  letterSpacing: 0.5,
  fontWeight: FontWeight.bold,
  color: Color(0xFF202020),
  fontFamily: 'Poppins',
);

const kGoDashborad = TextStyle(
  fontSize: 15,
  letterSpacing: 0.5,
  fontWeight: FontWeight.bold,
  color: Color(0xFF8CC493),
  fontFamily: 'Poppins',
);

const kLanguageoption = TextStyle(
  color: Color(0xFF202020),
  fontSize: 12,
  letterSpacing: 1,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.bold,
);
const kLanguageoptionHeader = TextStyle(
  color: Color(0xFFFFFFFF),
  fontSize: 20,
  letterSpacing: 1,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.bold,
);

const kSelectedLanguageoption = TextStyle(
  color: kSendaSurveyColor,
  fontSize: 12,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w400,
);

const kManageProfileheadings = TextStyle(
  color: kBottomDividerColor,
  fontSize: 14,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);

const kInstructionHeading = TextStyle(
  color: kBottomSheetText,
  fontSize: 22,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);

const kInstructionHeadingss = TextStyle(
  color: kRefreshTextColor,
  fontSize: 16,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.bold,
);

const kInstructionHeadings = TextStyle(
  color: kBg,
  fontSize: 17,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);

const kManageProfileheading = TextStyle(
  color: kFillaSurveyColor,
  fontSize: 14,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);

const kManageProfile = TextStyle(
  color: Color(0xff4A4A4A),
  fontSize: 12,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  // fontWeight: FontWeight.w500,
);

const kDashboardText = TextStyle(
  color: kBg,
  fontSize: 12,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);

const kDashboardTextS = TextStyle(
  color: kBg,
  fontSize: 16,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);
const kTagTextBlack = TextStyle(
  color: Color(0xff4A4A4A),
  fontSize: 13,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);
const kDashboardTextBlack = TextStyle(
  color: Color(0xff000000),
  fontSize: 14,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);
const kDashboardTextBlackS = TextStyle(
  color: Color(0xff000000),
  fontSize: 14,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.bold,
);

const kDashboardheading = TextStyle(
  color: kBg,
  fontSize: 12,
  decoration: TextDecoration.none,
  fontFamily: 'Poppins',
  fontWeight: FontWeight.w500,
);
