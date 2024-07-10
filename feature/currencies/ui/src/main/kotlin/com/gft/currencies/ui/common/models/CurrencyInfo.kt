package com.gft.currencies.ui.common.models

import com.gft.currencies.domain.models.CryptoCurrency
import com.gft.currencies.domain.models.FiatCurrency

data class CurrencyInfo(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String?,
)

fun List<CryptoCurrency>.cryptoCurrenciesToUi() = map { cryptoCurrency ->
    CurrencyInfo(
        id = cryptoCurrency.id,
        name = cryptoCurrency.name,
        symbol = cryptoCurrency.symbol,
        code = null,
    )
}

fun List<FiatCurrency>.fiatCurrenciesToUi() = map { fiatCurrency ->
    CurrencyInfo(
        id = fiatCurrency.id,
        name = fiatCurrency.name,
        symbol = fiatCurrency.symbol,
        code = fiatCurrency.code,
    )
}
