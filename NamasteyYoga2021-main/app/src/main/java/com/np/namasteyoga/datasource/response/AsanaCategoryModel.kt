package com.np.namasteyoga.datasource.response

data class AsanaCategoryModel(
    val category_description: String? = null,
    val category_image_path: String? = null,
    val category_name: String? = null,
    val id: Int? = null,
    val sub_category_data: List<SubCategoryData>? = null
)