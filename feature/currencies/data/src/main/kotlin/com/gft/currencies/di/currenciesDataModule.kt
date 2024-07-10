package com.gft.currencies.di

import androidx.room.Room
import com.gft.currencies.data.local.LocalDataSource
import com.gft.currencies.data.local.db.CURRENCIES_DATABASE_NAME
import com.gft.currencies.data.local.db.CurrenciesDatabase
import com.gft.currencies.data.remote.RemoteDataSource
import com.gft.currencies.data.repository.DefaultCurrenciesRepository
import com.gft.currencies.domain.repository.CurrenciesRepository
import com.squareup.moshi.Moshi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val currenciesDataModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = CurrenciesDatabase::class.java,
            name = CURRENCIES_DATABASE_NAME,
        ).build()
    }

    factory { get<CurrenciesDatabase>().getCryptoCurrenciesDao() }
    factory { get<CurrenciesDatabase>().getFiatCurrenciesDao() }

    singleOf(::LocalDataSource)

    single {
        Moshi.Builder().build()
    }

    singleOf(::RemoteDataSource)

    single<CurrenciesRepository> {
        DefaultCurrenciesRepository(
            localDataSource = get(),
            remoteDataSource = get(),
        )
    }
}
