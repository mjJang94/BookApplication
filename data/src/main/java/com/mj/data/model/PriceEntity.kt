package com.mj.data.model

import com.mj.data.DataMapper
import com.mj.domain.model.Price

data class PriceEntity(
    val amount: Int,
    val currencyCode: String,
): DataMapper<Price?> {
    override fun toDomain(): Price = Price(amount, currencyCode)
}
