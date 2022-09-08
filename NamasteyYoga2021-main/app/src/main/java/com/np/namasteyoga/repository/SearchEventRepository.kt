package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponseNamasteYoga
import com.np.namasteyoga.datasource.pojo.City
import com.np.namasteyoga.datasource.pojo.Event
import io.reactivex.Single

interface SearchEventRepository {
    fun searchEventList(search: String,city: City): Single<BaseResponseNamasteYoga<List<Event>>>
}