package com.gft.currencies.domain.repository

import com.gft.currencies.domain.models.CryptoCurrency
import com.gft.currencies.domain.models.FiatCurrency

interface CurrenciesRepository {

    suspend fun loadData()

    suspend fun clearData()

    suspend fun getAllCryptoCurrencies(): List<CryptoCurrency>

    suspend fun getAllFiatCurrencies(): List<FiatCurrency>
}
