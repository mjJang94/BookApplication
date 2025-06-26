package com.mj.remote.model

data class SummaryBookResponse(
    val kind: String,
    val totalItems: Int,
    val items: List<BookResponse>
)
