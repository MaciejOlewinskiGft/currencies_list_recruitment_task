package com.gft.currencies.ui.screens.demooptions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gft.common.ui.OneTimeEvent
import com.gft.currencies.domain.usecase.ClearDataUseCase
import com.gft.currencies.domain.usecase.GetAllCryptoCurrenciesUseCase
import com.gft.currencies.domain.usecase.GetAllCurrenciesUseCase
import com.gft.currencies.domain.usecase.GetAllFiatCurrenciesUseCase
import com.gft.currencies.domain.usecase.InsertDataUseCase
import com.gft.currencies.ui.R
import com.gft.currencies.ui.common.models.CurrencyInfo
import com.gft.currencies.ui.common.models.cryptoCurrenciesToUi
import com.gft.currencies.ui.common.models.fiatCurrenciesToUi
import com.gft.currencies.ui.screens.demooptions.DemoOptionsNavigationEffect.NavigateToCurrencyList
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEffect.ShowToast
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnClearDataButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnInsertDataButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnShowAllCurrenciesButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnShowCryptoCurrenciesButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewEvent.OnShowFiatCurrenciesButtonClicked
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewModel.CurrencyTypeToFetch.ALL
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewModel.CurrencyTypeToFetch.CRYPTO
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewModel.CurrencyTypeToFetch.FIAT
import kotlinx.coroutines.CoroutineStart.UNDISPATCHED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val LAST_FETCHED_CURRENCY_TYPE = "DemoOptionsViewModel.lastFetchedCurrencyType"

class DemoOptionsViewModel(
    savedStateHandle: SavedStateHandle,
    private val clearData: ClearDataUseCase,
    private val insertData: InsertDataUseCase,
    private val getAllCryptoCurrencies: GetAllCryptoCurrenciesUseCase,
    private val getAllFiatCurrencies: GetAllFiatCurrenciesUseCase,
    private val getAllCurrencies: GetAllCurrenciesUseCase,
) : ViewModel() {

    // To handle activity kill scenario (all real-life scenarios which can be simulated by "Don't keep activities" developer option), we need to
    // remember which type of currencies were fetched last time (to re-fetch data on restore); here is an enum which defines all possible types:
    private enum class CurrencyTypeToFetch {
        CRYPTO,
        FIAT,
        ALL,
    }

    private val loadingInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val currentData: MutableStateFlow<List<CurrencyInfo>?> = MutableStateFlow(null)

    val viewStates: StateFlow<DemoOptionsViewState> = combine(
        flow = loadingInProgress,
        flow2 = currentData,
    ) { loading, data ->
        DemoOptionsViewState(
            isLoadingIndicatorVisible = loading,
            areButtonsEnabled = !loading,
            currenciesList = data,
        )
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = DemoOptionsViewState(
            isLoadingIndicatorVisible = true,
            areButtonsEnabled = false,
            currenciesList = null,
        ),
    )

    private val _viewEffects: MutableStateFlow<OneTimeEvent<DemoOptionsViewEffect>?> = MutableStateFlow(null)
    val viewEffects = _viewEffects.asStateFlow()

    private val _navigationEffects: MutableStateFlow<OneTimeEvent<DemoOptionsNavigationEffect>?> = MutableStateFlow(null)
    val navigationEffects = _navigationEffects.asStateFlow()

    // Reading type of currencies which were fetched last time. In case of first enter to the screen it will be null, but in case of activity kill
    // scenario it will return us information what was fetched last time before activity kill. We will use this info to re-fetch data.
    private val lastFetchedCurrencyType = savedStateHandle.getLiveData<CurrencyTypeToFetch?>(LAST_FETCHED_CURRENCY_TYPE, null)

    init {
        lastFetchedCurrencyType.value?.let { nonNullLastFetchedCurrencyType ->
            // If during init the lastFetchedCurrencyType is not null, it means that activity was recreated after kill and we need to re-fetch the data
            viewModelScope.launch(start = UNDISPATCHED) {
                readCurrenciesList(nonNullLastFetchedCurrencyType)
            }
        }
    }

    fun onEvent(event: DemoOptionsViewEvent) = when (event) {
        OnClearDataButtonClicked -> handleClearDataButtonClicked()
        OnInsertDataButtonClicked -> handleInsertDataButton()
        OnShowCryptoCurrenciesButtonClicked -> readCurrenciesListAndNavigateToCurrencyList(CRYPTO)
        OnShowFiatCurrenciesButtonClicked -> readCurrenciesListAndNavigateToCurrencyList(FIAT)
        OnShowAllCurrenciesButtonClicked -> readCurrenciesListAndNavigateToCurrencyList(ALL)
    }

    private fun handleClearDataButtonClicked() {
        viewModelScope.launch(start = UNDISPATCHED) {
            loadingInProgress.value = true
            currentData.value = null

            withContext(Dispatchers.IO) { clearData() }

            loadingInProgress.value = false

            _viewEffects.value = OneTimeEvent(ShowToast(R.string.demo_options_screen_data_cleared_confirmation_toast_message))
        }
    }

    private fun handleInsertDataButton() {
        viewModelScope.launch(start = UNDISPATCHED) {
            loadingInProgress.value = true

            try {
                withContext(Dispatchers.IO) { insertData() }

                _viewEffects.value = OneTimeEvent(ShowToast(R.string.demo_options_screen_data_inserted_confirmation_toast_message))
            } catch (e: Exception) {
                _viewEffects.value = OneTimeEvent(ShowToast(R.string.demo_options_screen_data_loading_error_toast_message))
            }

            loadingInProgress.value = false
        }
    }

    private fun readCurrenciesListAndNavigateToCurrencyList(currencyTypeToFetch: CurrencyTypeToFetch) {
        viewModelScope.launch(start = UNDISPATCHED) {
            readCurrenciesList(currencyTypeToFetch)

            _navigationEffects.value = OneTimeEvent(NavigateToCurrencyList)
        }
    }

    private suspend fun readCurrenciesList(currencyTypeToFetch: CurrencyTypeToFetch) {
        loadingInProgress.value = true
        currentData.value = emptyList()
        lastFetchedCurrencyType.value = currencyTypeToFetch

        val currenciesList = withContext(Dispatchers.IO) {
            when (currencyTypeToFetch) {
                CRYPTO -> getAllCryptoCurrencies().cryptoCurrenciesToUi()
                FIAT -> getAllFiatCurrencies().fiatCurrenciesToUi()
                ALL -> getAllCurrencies().let { allCurrenciesPair ->
                    allCurrenciesPair.first.cryptoCurrenciesToUi() + allCurrenciesPair.second.fiatCurrenciesToUi()
                }
            }
        }

        currentData.value = currenciesList
        loadingInProgress.value = false
    }
}
