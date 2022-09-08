package com.np.namasteyoga.repository

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.response.SocialLinkModel
import io.reactivex.Single


interface SocialLinkRepository {
    fun getSocialLinks(page:Int):Single<BaseResponse<List<SocialLinkModel>>>
}