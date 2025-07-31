package com.mj.data.model

import com.mj.data.DataMapper
import com.mj.data.toDomainModel
import com.mj.domain.model.Sale

data class SaleEntity(
    val saleability: String,
    val buyLink: String,
    val listPrice: PriceEntity,
): DataMapper<Sale?> {
    override fun toDomain(): Sale = Sale(saleability, buyLink, listPrice.toDomainModel())
}
