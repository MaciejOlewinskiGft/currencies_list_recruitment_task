package com.gft.currencies.di

import com.gft.currencies.domain.usecase.ClearDataUseCase
import com.gft.currencies.domain.usecase.GetAllCryptoCurrenciesUseCase
import com.gft.currencies.domain.usecase.GetAllCurrenciesUseCase
import com.gft.currencies.domain.usecase.GetAllFiatCurrenciesUseCase
import com.gft.currencies.domain.usecase.InsertDataUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val currenciesDomainModule = module {
    factoryOf(::ClearDataUseCase)
    factoryOf(::InsertDataUseCase)
    factoryOf(::GetAllCryptoCurrenciesUseCase)
    factoryOf(::GetAllFiatCurrenciesUseCase)
    factoryOf(::GetAllCurrenciesUseCase)
}
