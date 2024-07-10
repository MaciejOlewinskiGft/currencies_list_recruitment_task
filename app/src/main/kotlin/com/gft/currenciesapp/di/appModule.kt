package com.gft.currenciesapp.di

import com.gft.currencies.di.currenciesDataModule
import com.gft.currencies.di.currenciesDomainModule
import com.gft.currencies.di.currenciesUiModule
import org.koin.dsl.module

val appModule = module {
    includes(
        currenciesDataModule,
        currenciesDomainModule,
        currenciesUiModule,
    )
}
