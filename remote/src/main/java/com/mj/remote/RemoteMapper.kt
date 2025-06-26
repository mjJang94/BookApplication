package com.mj.remote

interface RemoteMapper<DataModel> {
    fun toData(): DataModel
}
