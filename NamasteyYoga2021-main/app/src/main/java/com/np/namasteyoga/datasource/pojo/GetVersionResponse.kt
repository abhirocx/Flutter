package com.np.namasteyoga.datasource.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.np.namasteyoga.datasource.response.VersionData
import java.io.Serializable

class GetVersionResponse : Serializable {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: VersionData? = null
}