package com.mj.remote.model

import com.mj.data.model.SaleEntity
import com.mj.remote.RemoteMapper

data class SaleResponse(
    val saleability: String?,
    val buyLink: String?,
    val listPrice: PriceResponse?,
) : RemoteMapper<SaleEntity> {

    override fun toData(): SaleEntity {
        return SaleEntity(saleability = saleability.orEmpty(), buyLink = buyLink.orEmpty(), listPrice = (listPrice ?: PriceResponse(0, "")).toData()
        )
    }
}
