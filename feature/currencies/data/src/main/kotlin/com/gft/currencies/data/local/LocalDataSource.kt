package com.gft.currencies.data.local

import androidx.room.withTransaction
import com.gft.currencies.data.local.db.CurrenciesDatabase
import com.gft.currencies.data.local.db.daos.CryptoCurrenciesDao
import com.gft.currencies.data.local.db.daos.FiatCurrenciesDao
import com.gft.currencies.data.local.db.entities.toDomain
import com.gft.currencies.data.local.db.entities.toLocal
import com.gft.currencies.domain.models.CryptoCurrency
import com.gft.currencies.domain.models.FiatCurrency

internal class LocalDataSource(
    private val currenciesDatabase: CurrenciesDatabase,
    private val cryptoCurrenciesDao: CryptoCurrenciesDao,
    private val fiatCurrenciesDao: FiatCurrenciesDao,
) {

    suspend fun saveAll(cryptoCurrencies: List<CryptoCurrency>, fiatCurrencies: List<FiatCurrency>) {
        currenciesDatabase.withTransaction {
            cryptoCurrenciesDao.insertAll(cryptoCurrencies.toLocal())
            fiatCurrenciesDao.insertAll(fiatCurrencies.toLocal())
        }
    }

    suspend fun deleteAll() {
        currenciesDatabase.withTransaction {
            cryptoCurrenciesDao.deleteAll()
            fiatCurrenciesDao.deleteAll()
        }
    }

    suspend fun getAllCryptoCurrencies() = cryptoCurrenciesDao.readAll().toDomain()

    suspend fun getAllFiatCurrencies() = fiatCurrenciesDao.readAll().toDomain()
}
