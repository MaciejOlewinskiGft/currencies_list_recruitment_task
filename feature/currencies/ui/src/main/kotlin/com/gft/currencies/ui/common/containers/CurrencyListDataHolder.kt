package com.gft.currencies.ui.common.containers

import com.gft.currencies.ui.common.models.CurrencyInfo
import kotlinx.coroutines.flow.Flow

interface CurrencyListDataHolder {

    fun setCurrencyListDataSource(currencyListDataSource: Flow<List<CurrencyInfo>>)
}
