package com.np.namasteyoga.interfaces

import com.np.namasteyoga.comman.CommonInt
import com.np.namasteyoga.comman.CommonString
import com.np.namasteyoga.datasource.EmptyResponse
import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.pojo.*
import com.np.namasteyoga.datasource.request.*
import com.np.namasteyoga.datasource.response.*
import com.np.namasteyoga.datasource.response.UserListResponse
import com.np.namasteyoga.datasource.response.ayushSubCategoryModels.AyushSubCategoryList
import com.np.namasteyoga.utils.C
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RESTApi {


    @POST(C.API_GET_VERSION)
    fun getVersion(
        @Body getVersionRequest: GetVersionRequest,
        @HeaderMap header: Map<String, String>?
    ): Single<BaseResponse<VersionData>>

    @POST(C.API_LOGIN)
    fun login(
        @Body loginRequest: LoginRequest,
        @HeaderMap header: Map<String, String>?
    ): Single<BaseResponse<UserDetail>>

    @POST(C.API_FCM_TOKEN_UPDATE)
    fun updateDeviceToken(
        @Query("user_id") user_id: String,
        @Query("new_device_id") new_device_id: String,
        @Query("old_device_id") old_device_id: String,
        @Query("notificationSetting") notificationSetting: Int,
        @Query("deviceType") deviceType: String = CommonString.ANDROID
    ): Single<BaseResponseNamasteYoga<EmptyResponse>>

    @POST(C.API_FORGOT_PASSWORD)
    fun forgotPassword(
        @Body loginRequest: LoginRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<BaseResponse<EmptyResponse>>

    @POST(C.API_VERIFY_PASSWORD)
    fun verifyPassword(
        @Body loginRequest: LoginRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<BaseResponse<EmptyResponse>>

    @POST(C.API_REGISTER)
    fun register(
        @Body registerRequest: RegisterRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<RegisterResponse>

    @POST(C.API_OTP_VERIFICATION)
    fun otpVerify(
        @Body loginRequest: LoginRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<BaseResponse<EmptyResponse>>

    @POST(C.API_CHANGE_PASSWORD)
    fun changePassword(
        @Body changePasswordModel: ChangePasswordModel,
        @HeaderMap header: Map<String, String>?
    ):Single<BaseResponse<EmptyResponse>>


    @POST(C.API_USER_LIST)
    fun getUserList(
        @Body userListRequest: UserListRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<UserListResponse>

    @POST(C.API_CITY_SEARCH)
    fun getSearchUserList(
        @Body userListRequest: UserListRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<UserListResponse>

    @POST(C.API_ASANA_CATEGORY)
    fun getAsanaCategory(
        @Query("page") page: Int
    ):Single<BaseResponse<List<AsanaCategoryModel>>>

    @POST(C.API_LOGOUT)
    fun logout(
        @HeaderMap header: Map<String, String>?
    ):Single<BaseResponse<EmptyResponse>>


    @POST(C.API_SUSPEND_ACCOUNT)
    fun suspendAccount(
        @HeaderMap header: Map<String, String>?,
        @Body emptyResponse: EmptyResponse= EmptyResponse()
    ):Single<BaseResponse<EmptyResponse>>

    @POST(C.API_RESUME_ACCOUNT)
    fun resumeAccount(
        @Body loginRequest: LoginRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<BaseResponse<UserDetail>>

    @POST(C.API_UPDATE_PROFILE)
    fun updateAccount(
        @Body editRequest: EditRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<BaseResponse<UserDetail>>

    @POST(C.API_SOCIAL_LINKS)
    fun getSocialLinks(
        @Query("page") page: Int
    ):Single<BaseResponse<List<SocialLinkModel>>>

    @POST(C.API_EVENT_LIST)
    fun getEventList(
        @Body getEventRequest: GetEventRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<EventResponse>

    @POST(C.API_GET_FEEDBACK_LIST)
    fun getFeedbackList(
        @Query("page") page: Int,
        @HeaderMap header: Map<String, String>?
    ):Single<FeedbackResponseModel>

    @POST(C.API_SUBMIT_FEEDBACK)
    fun submitFeedback(
        @Body feedbackSubmitRequest: FeedbackSubmitRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<BaseResponseNamasteYoga<EmptyResponse>>

    @POST(C.API_INTERESTED)
    fun interested(@Query("events_id") events_id: String,
                   @HeaderMap header: Map<String, String>?):Single<BaseResponseNamasteYoga<EmptyResponse>>


    @POST(C.API_CITY_SEARCH)
    fun searchEventList(
        @Query("search") search: String,
        @Query("country") country: String,
        @Query("city") city: String,
        @Query("state") state: String,
        @HeaderMap header: Map<String, String>?,
        @Query("type") type: String="event"):Single<BaseResponseNamasteYoga<List<Event>>>

    @POST(C.API_AYUSH_CATEGORY)
    fun getAyushCategoryList(
        @Query("page") page: Int
    ):Single<BaseResponseNamasteYoga<List<AyushMerchandiseModel>>>

    @POST(C.API_CELEBRITY_TESTIMONIAL)
    fun getCelebrityTestimonialList(
        @Query("page") page: Int
    ):Single<BaseResponseNamasteYoga<List<CelebrityTestimonialModel>>>

    @POST(C.API_AYUSH_SUB_CATEGORY)
    fun getAyushSubCategoryList(
        @Query("page") page: Int,
        @Query("category_id") category_id: String
    ):Single<BaseResponseNamasteYoga<List<AyushSubCategoryList>>>

    @POST(C.API_AYUSH_PRODUCT_DETAILS)
    fun getAyushProductDetails(
        @Query("product_id") product_id: String
    ):Single<BaseResponseNamasteYoga<AyushSubCategoryList>>

    @POST(C.API_UPLOAD_MEDIA)
    @Multipart
    fun uploadMedia(
        @Part("events_id") events_id: RequestBody,
        @Part("videoId") videoId: RequestBody?,
        @Part file1: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?,
        @Part file3: MultipartBody.Part?,
        @Part file4: MultipartBody.Part?,
        @HeaderMap header: Map<String, String>?
    ): Single<BaseResponseNamasteYoga<EmptyResponse>>


    @POST(C.API_BHUVAN_COUNTRY_LIST)
    fun getBhuvanList(
        @Body bhuvanListRequest: BhuvanListRequest,
        @HeaderMap header: Map<String, String>?
    ):Single<BhuvanAppListResponseModel>


}