package com.gft.currencies.domain.usecase

import com.gft.currencies.domain.repository.CurrenciesRepository

class GetAllCurrenciesUseCase internal constructor(
    private val currenciesRepository: CurrenciesRepository,
) {

    suspend operator fun invoke() = currenciesRepository.getAllCryptoCurrencies() to currenciesRepository.getAllFiatCurrencies()
}
