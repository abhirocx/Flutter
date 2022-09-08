package com.np.namasteyoga.utils

import com.np.namasteyoga.BuildConfig
import com.np.namasteyoga.datasource.pojo.City


object C {
    const val TIME_OUT_TO_EXIT = "TIme_out"
    const val BACK_TO_ACTIVITY = "back_to_activity"
    const val API_TIME_OUT = 15000
    const val API_BASE_URL = ""

    const val BASE_URL = "https://yogalocator.inroad.in/"

    const val API_USER_LIST = API_BASE_URL + "api/v4/userList"
    const val API_CITY_LIST = API_BASE_URL + "api/v4/getUserByCity"
const val API_BHUVAN_COUNTRY_LIST = "https://bhuvan-app3.nrsc.gov.in/yoga/getYogaStats.php"


    const val DEBUG = true
    const val GOOGLE_URL = BuildConfig.GOOGLE_URL
    const val APPLICATION_ID = BuildConfig.APPLICATION_ID


    //    public static final String API_REGISTER = API_BASE_URL + "api/register";
//    const val API_REGISTER = API_BASE_URL + "api/v5/register"
    const val API_REGISTER = API_BASE_URL + "api/v6/register"
    const val API_LOGIN = API_BASE_URL + "api/v5/login"
    const val API_SUSPEND_ACCOUNT = API_BASE_URL + "api/v5/suspendAccount"
    const val API_RESUME_ACCOUNT = API_BASE_URL + "api/v5/ResumeOtpAccount"
    const val API_EVENT_LIST = API_BASE_URL + "api/v4/eventList"
    const val API_CREATE_EVENT = API_BASE_URL + "api/v4/addEvent"
    const val API_GET_VERSION = API_BASE_URL + "api/v4/getLatestVersion"
    const val API_CITY_SEARCH = API_BASE_URL + "api/v4/cityList"
    const val API_NOTIFY = API_BASE_URL + "api/v4/notifyMe"
    const val API_FORGOT_PASSWORD = API_BASE_URL + "api/v4/forgotPassword"
    const val API_VERIFY_PASSWORD = API_BASE_URL + "api/v4/verifyForgotPassword"
    const val API_MY_EVENT = API_BASE_URL + "api/v4/getMyEventList"
    const val API_UPDATE_EVENT = API_BASE_URL + "api/v4/editMyEvent"
//    const val API_UPDATE_PROFILE = API_BASE_URL + "api/v4/editMyProfile"
    const val API_UPDATE_PROFILE = API_BASE_URL + "api/v6/editMyProfile"
    const val API_CHANGE_PASSWORD = API_BASE_URL + "api/v4/changePassword"
    const val API_OTP_VERIFICATION = API_BASE_URL + "api/v4/otpVerification"
    const val API_RESEND_OTP = API_BASE_URL + "api/v4/resentRegisterOtp"
    const val API_ADD_RATING = API_BASE_URL + "api/v4/addRating"
//    const val API_LOGOUT = API_BASE_URL + "api/v4/apilogout"
    const val API_LOGOUT = API_BASE_URL + "api/v6/apilogout"
    const val API_ASANA_LIST = API_BASE_URL + "api/v6/GetAasanaList"
    const val API_POLL_LIST = API_BASE_URL + "api/v6/getPoll"
    const val API_POLL_VALIDATE = API_BASE_URL + "api/v6/validatePoll"
    const val API_POLL_SUBMIT = API_BASE_URL + "api/v6/submitPoll"

    const val API_QUIZ_LIST = API_BASE_URL + "api/v6/getQuiz"
    const val API_QUIZ_VALIDATE = API_BASE_URL + "api/v6/validateQuiz"
    const val API_QUIZ_SUBMIT = API_BASE_URL + "api/v6/submitQuiz"


    const val ASANA_THUMB_BASE_URL = "https://img.youtube.com/vi/"
    const val ASANA_THUMB_BASE_URL_END = "/0.jpg"
//    const val YOU_TUBE_SPIT = "?v="
    const val YOU_TUBE_SPIT = "v="


    /////////////////////////////////////////////////////////////////////////////////////////
    const val API_FCM_TOKEN_UPDATE = API_BASE_URL + "api/v6/deviceTokenUpdate"
    const val API_ASANA_CATEGORY = API_BASE_URL + "api/v6/GetSubCategoryList"
    const val API_SOCIAL_LINKS = API_BASE_URL + "api/v6/GetAllSocialLinks"
    const val API_GET_FEEDBACK_LIST = API_BASE_URL + "api/v6/GetFeedbackQuestionList"
    const val API_SUBMIT_FEEDBACK = API_BASE_URL + "api/v6/SubmitFeedback"
    const val API_AYUSH_CATEGORY = API_BASE_URL + "api/v6/aayushcategorylist"
    const val API_CELEBRITY_TESTIMONIAL = API_BASE_URL + "api/v6/getTestimonials"
    const val API_AYUSH_SUB_CATEGORY = API_BASE_URL + "api/v6/productbycategory"
    const val API_AYUSH_PRODUCT_DETAILS = API_BASE_URL + "api/v6/productdetails"
    const val API_INTERESTED = API_BASE_URL + "api/v5/addEventSubscribers"
    const val API_UPLOAD_MEDIA = API_BASE_URL + "api/v5/uploadEventImages"

    /////////////////////////////////////////////////////////////////////////////////////////


    const val NP_STATUS_SUCCESS = "NP001"
    const val NP_STATUS_FAIL = "NP002"
    const val NP_STATUS_OTHER = "NP007"//for 2
    val CW_STATUS_SUCCESS = "CW001"
    val NP_STATUS_NOT_KNOWN = "NP003"
    const val NP_INVALID_TOKEN = "NP009"
    const val NP_TOKEN_EXPIRED = "NP010"
    const val NP_TOKEN_NOT_FOUND = "NP011"


    const val TOKEN = "token"
    const val TOKEN_KEY = "Authorization"
    const val TOKEN_VALUE = "Bearer"
    const val CITY = "city"
    const val STATE = "state"
    const val COUNTRY = "country"
    const val ANDROID = "A"
    const val USER_DATA = "user_data"
    const val IS_FIRST_LAUNCH = "is_firstlaunch"
    const val EVENTDATA = "eventdata"
    var city: City? = null
    const val TAG_FRAGMENT_CREATE_EVENT = "create_event"
    const val TAG_FRAGMENT_DISCLAIMER = "disclaimer"
    const val TAG_FRAGMENT_EDIT_EVENT = "editEvent"
    const val DATE_FORMAT_EVENT = "dd-MMM-yyyy hh:mm a"
    const val DATE_FORMAT_EVENT_2 = "yyyy-MM-dd HH:mm:ss"
    const val DATE_FORMAT_EVENT2 = "dd-MMM-yyyy"
    const val SPLASH_LOADER_TIME: Long = 2000

    @JvmField
    var English = "english"

    @JvmField
    var Hindi = "hindi"

    @JvmField
    var language = "language"
    var PAST = "past"
    var CURRENT = "current"
    var UPCOMING = "upcoming"
}