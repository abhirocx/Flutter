package com.np.namasteyoga.datasource.response

import com.np.namasteyoga.datasource.Response

data class VersionData(
    var version: String? = null,
    var type: String? = null,
    var description: String? = null,
    var updatedAt: String? = null,
    var createdAt: String? = null,
    var id: Int? = null
):Response()