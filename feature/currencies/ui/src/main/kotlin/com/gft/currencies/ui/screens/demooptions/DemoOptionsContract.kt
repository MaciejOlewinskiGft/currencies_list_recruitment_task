package com.gft.currencies.ui.screens.demooptions

import androidx.annotation.StringRes
import com.gft.currencies.ui.common.models.CurrencyInfo

data class DemoOptionsViewState(
    val isLoadingIndicatorVisible: Boolean,
    val areButtonsEnabled: Boolean,
    val currenciesList: List<CurrencyInfo>?,
)

sealed interface DemoOptionsViewEffect {
    data class ShowToast(@StringRes val messageResId: Int) : DemoOptionsViewEffect
}

sealed interface DemoOptionsNavigationEffect {
    data object NavigateToCurrencyList : DemoOptionsNavigationEffect
}

sealed interface DemoOptionsViewEvent {
    data object OnClearDataButtonClicked : DemoOptionsViewEvent
    data object OnInsertDataButtonClicked : DemoOptionsViewEvent
    data object OnShowCryptoCurrenciesButtonClicked : DemoOptionsViewEvent
    data object OnShowFiatCurrenciesButtonClicked : DemoOptionsViewEvent
    data object OnShowAllCurrenciesButtonClicked : DemoOptionsViewEvent
}
