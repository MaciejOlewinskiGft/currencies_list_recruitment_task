package com.gft.currencies.di

import com.gft.currencies.ui.screens.currencylist.CurrencyListViewModel
import com.gft.currencies.ui.screens.demooptions.DemoOptionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val currenciesUiModule = module {
    viewModelOf(::CurrencyListViewModel)
    viewModelOf(::DemoOptionsViewModel)
}
