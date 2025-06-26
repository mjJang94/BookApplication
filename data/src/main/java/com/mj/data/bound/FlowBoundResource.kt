package com.mj.data.bound

import com.mj.data_resource.DataResource
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector

class FlowBoundResource<DomainType, DataType>(dataAction: suspend () -> DataType) :
    FlowBaseBoundResource<DomainType, DataType>(dataAction) {

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<DataResource<DomainType>>) {
        collector.emit(DataResource.loading<DomainType>() as DataResource<DomainType>)
        fetchFromSource(collector)
    }

}
