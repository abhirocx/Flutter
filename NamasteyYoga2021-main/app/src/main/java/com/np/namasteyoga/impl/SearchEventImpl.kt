package com.np.namasteyoga.impl

import com.google.gson.Gson
import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.Event
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.SearchEventRepository
import com.np.namasteyoga.utils.ConstUtility
import io.reactivex.Single
import java.lang.Exception
import java.util.HashMap

class SearchEventImpl(private val restApi: RESTApi) : SearchEventRepository {
    override fun searchEventList(
        search: String,
        city: City
    ): Single<BaseResponseNamasteYoga<List<Event>>> {
        return try {
            val hashMap = HashMap<String, String>()
            hashMap["search"] = search
            hashMap["country"] = city.country_name
            hashMap["city"] = city.city
            hashMap["state"] = city.state_name
            hashMap["type"] = "event"
            val json = Gson().toJson(hashMap)
            restApi.searchEventList(
                search, city.country_name, city.city, city.state_name,
                ConstUtility.getHeaderCity(json)
            )
        } catch (e: Exception) {
            Single.error(Throwable(e))
        }
    }
}