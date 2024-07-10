package com.gft.currencies.ui.screens.currencylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gft.currencies.ui.common.models.CurrencyInfo
import com.gft.currencies.ui.screens.currencylist.CurrencyListViewEvent.OnNewCurrencyListDataSource
import com.gft.currencies.ui.screens.currencylist.CurrencyListViewEvent.OnSearchQueryUpdated
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

internal class CurrencyListViewModel : ViewModel() {

    private val currentDataSource: MutableStateFlow<Flow<List<CurrencyInfo>>> = MutableStateFlow(flow { emit(emptyList()) })
    private val currentSearchQuery: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val viewStates = currentDataSource
        .flatMapLatest { dataSource -> dataSource }
        .combine(currentSearchQuery) { data, searchQuery ->
            val filteredCurrencyInfos = data.filterBySearchQuery(searchQuery)

            CurrencyListViewState(
                isCurrenciesListVisible = filteredCurrencyInfos.isNotEmpty(),
                currencyInfos = filteredCurrencyInfos,
                isNoDataPlaceholderVisible = data.isEmpty(),
                isNoSearchResultsPlaceholderVisible = data.isNotEmpty() && filteredCurrencyInfos.isEmpty(),
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CurrencyListViewState(
                isCurrenciesListVisible = false,
                currencyInfos = emptyList(),
                isNoDataPlaceholderVisible = true,
                isNoSearchResultsPlaceholderVisible = false,
            ),
        )

    fun onEvent(event: CurrencyListViewEvent) = when (event) {
        is OnNewCurrencyListDataSource -> handleNewCurrencyListDataSource(event.currencyListDataSource)
        is OnSearchQueryUpdated -> handleSearchQueryUpdated(event.searchQuery)
    }

    private fun handleNewCurrencyListDataSource(currencyListDataSource: Flow<List<CurrencyInfo>>) {
        currentDataSource.value = currencyListDataSource
    }

    private fun handleSearchQueryUpdated(searchQuery: String) {
        currentSearchQuery.value = searchQuery
    }

    private fun List<CurrencyInfo>.filterBySearchQuery(searchQuery: String) = if (searchQuery.isBlank()) {
        this
    } else {
        filter { currencyInfo ->
            currencyInfo.name.startsWith(searchQuery, ignoreCase = true)
                || currencyInfo.name.contains(" $searchQuery", ignoreCase = true)
                || currencyInfo.symbol.startsWith(searchQuery, ignoreCase = true)
        }
    }
}
