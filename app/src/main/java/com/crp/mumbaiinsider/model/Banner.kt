package com.crp.mumbaiinsider.model

data class Banner(
    val _id: String?,
    val category_id: CategoryId?,
    val display_details: DisplayDetails?,
    val group_id: GroupId?,
    val is_internal: Boolean?,
    val map_link: String?,
    val name: String?,
    val priority: Int?,
    val type: String?,
    val vertical_cover_image: String?
)