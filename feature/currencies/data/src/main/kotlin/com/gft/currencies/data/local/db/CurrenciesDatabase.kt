package com.gft.currencies.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gft.currencies.data.local.db.daos.CryptoCurrenciesDao
import com.gft.currencies.data.local.db.daos.FiatCurrenciesDao
import com.gft.currencies.data.local.db.entities.CryptoCurrencyLocalDto
import com.gft.currencies.data.local.db.entities.FiatCurrencyLocalDto

private const val DATABASE_VERSION = 1

@Database(entities = [CryptoCurrencyLocalDto::class, FiatCurrencyLocalDto::class], version = DATABASE_VERSION)
internal abstract class CurrenciesDatabase : RoomDatabase() {

    abstract fun getCryptoCurrenciesDao(): CryptoCurrenciesDao

    abstract fun getFiatCurrenciesDao(): FiatCurrenciesDao
}
