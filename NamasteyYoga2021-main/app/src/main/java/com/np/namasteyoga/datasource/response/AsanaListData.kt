package com.np.namasteyoga.datasource.response

data class AsanaListData(
    val id: Int? = null,
    val aasana_categories_id: Int? = null,
    val aasana_sub_categories_id: Int = 0,
    val aasana_name: String? = null,
    val aasana_description: String? = null,
    val assana_tag: String? = null,
    val assana_video_id: String? = null,
    val assana_video_duration: String? = null,
    val assana_benifits: String? = null,
    val assana_instruction: String? = null
)