package com.mj.presentation.search.model

import com.mj.domain.model.Sale

data class SaleModel(
    val saleability: String,
    val buyLink: String,
    val listPrice: PriceModel,
)

fun Sale.toPresentation(): SaleModel = SaleModel(
    saleability,
    buyLink,
    listPrice.toPresentation()
)