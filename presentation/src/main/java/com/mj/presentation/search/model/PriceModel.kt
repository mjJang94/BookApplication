package com.mj.presentation.search.model

import com.mj.domain.model.Price

data class PriceModel(
    val amount: Int,
    val currencyCode: String,
)

fun Price.toPresentation(): PriceModel = PriceModel(
    amount, currencyCode
)
