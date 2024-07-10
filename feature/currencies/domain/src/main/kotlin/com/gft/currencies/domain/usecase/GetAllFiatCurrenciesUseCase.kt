package com.gft.currencies.domain.usecase

import com.gft.currencies.domain.repository.CurrenciesRepository

class GetAllFiatCurrenciesUseCase internal constructor(
    private val currenciesRepository: CurrenciesRepository,
){

    suspend operator fun invoke() = currenciesRepository.getAllFiatCurrencies()
}
