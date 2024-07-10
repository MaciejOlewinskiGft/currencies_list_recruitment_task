package com.gft.currencies.domain.usecase

import com.gft.currencies.domain.repository.CurrenciesRepository

class GetAllCryptoCurrenciesUseCase internal constructor(
    private val currenciesRepository: CurrenciesRepository,
){

    suspend operator fun invoke() = currenciesRepository.getAllCryptoCurrencies()
}
