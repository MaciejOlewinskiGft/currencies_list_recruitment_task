package com.gft.currencies.data.repository

import com.gft.currencies.data.local.LocalDataSource
import com.gft.currencies.data.remote.RemoteDataSource
import com.gft.currencies.domain.repository.CurrenciesRepository

internal class DefaultCurrenciesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) : CurrenciesRepository {

    override suspend fun loadData() {
        localDataSource.saveAll(
            cryptoCurrencies = remoteDataSource.fetchCryptoCurrencies(),
            fiatCurrencies = remoteDataSource.fetchFiatCurrencies(),
        )
    }

    override suspend fun clearData() {
        localDataSource.deleteAll()
    }

    override suspend fun getAllCryptoCurrencies() = localDataSource.getAllCryptoCurrencies()

    override suspend fun getAllFiatCurrencies() = localDataSource.getAllFiatCurrencies()
}
