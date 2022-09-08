package com.np.namasteyoga.datasource.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.np.namasteyoga.datasource.Response
import com.np.namasteyoga.utils.ConstUtility
import java.io.Serializable
import java.lang.reflect.Array.get

data class UserDetail (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("phone")
    @Expose
    var _phone: String? = null,

    @SerializedName("role_id")
    @Expose
    var roleId: Int? = null,

    @SerializedName("city_id")
    @Expose
    var cityId: Int? = null,

    @SerializedName("state_id")
    @Expose
    var stateId: Int? = null,
    @SerializedName("sitting_capacity")
    @Expose
    var sitting_capacity: Int? = null,

    @SerializedName("country_id")
    @Expose
    var countryId: Int? = null,

    @SerializedName("user_type")
    @Expose
    var userType: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("email")
    @Expose
    var _email: String? = null,

    @SerializedName("address")
    @Expose
    var address: String? = null,

    @SerializedName("zip")
    @Expose
    var zip: String? = null,

    @SerializedName("lat")
    @Expose
    var lat :Double? = 0.0,

    @SerializedName("lng")
    @Expose
    var lng:Double? = 0.0,

    @SerializedName("token")
    @Expose
    var token: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("city_name")
    @Expose
    var cityName: String? = null,

    @SerializedName("country_name")
    @Expose
    var countryName: String? = null,

    @SerializedName("state_name")
    @Expose
    var stateName: String? = null,

    @SerializedName("nearest")
    @Expose
    var nearest:Int? = 0,

    @SerializedName("nearest_distance")
    @Expose
    var nearest_distance: String? = null
):Response(){

    val phone:String?  get()= if (_phone!=null)ConstUtility.decrypt(_phone)else null
    val email:String? get()= if (_email!=null)ConstUtility.decrypt(_email)else null
}