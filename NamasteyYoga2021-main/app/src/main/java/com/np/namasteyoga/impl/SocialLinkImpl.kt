package com.np.namasteyoga.impl

import com.np.namasteyoga.datasource.BaseResponse
import com.np.namasteyoga.datasource.response.SocialLinkModel
import com.np.namasteyoga.interfaces.RESTApi
import com.np.namasteyoga.repository.SocialLinkRepository
import io.reactivex.Single

class SocialLinkImpl(private val restApi: RESTApi):SocialLinkRepository {
    override fun getSocialLinks(page: Int): Single<BaseResponse<List<SocialLinkModel>>> = restApi.getSocialLinks(page)
}