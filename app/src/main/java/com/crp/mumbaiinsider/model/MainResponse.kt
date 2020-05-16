package com.crp.mumbaiinsider.model

data class MainResponse(
    val banners: List<Banner>?,
    val featured: List<Featured>?,
    val groups: List<String>?,
    val popular: List<Popular>?,
    val tags: List<String>?
)