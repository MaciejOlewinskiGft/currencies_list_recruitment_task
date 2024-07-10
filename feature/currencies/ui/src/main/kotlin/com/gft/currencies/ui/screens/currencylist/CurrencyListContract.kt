package com.gft.currencies.ui.screens.currencylist

import com.gft.currencies.ui.common.models.CurrencyInfo
import kotlinx.coroutines.flow.Flow

internal data class CurrencyListViewState(
    val isCurrenciesListVisible: Boolean,
    val currencyInfos: List<CurrencyInfo>,
    val isNoDataPlaceholderVisible: Boolean,
    val isNoSearchResultsPlaceholderVisible: Boolean,
)

internal sealed interface CurrencyListViewEvent {
    data class OnNewCurrencyListDataSource(val currencyListDataSource: Flow<List<CurrencyInfo>>) : CurrencyListViewEvent
    data class OnSearchQueryUpdated(val searchQuery: String) : CurrencyListViewEvent
}
