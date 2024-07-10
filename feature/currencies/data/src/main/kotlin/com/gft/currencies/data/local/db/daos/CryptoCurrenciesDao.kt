package com.gft.currencies.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gft.currencies.data.local.db.CRYPTO_CURRENCIES_ENTITY_TABLE_NAME
import com.gft.currencies.data.local.db.entities.CryptoCurrencyLocalDto

@Dao
internal interface CryptoCurrenciesDao {

    @Query("SELECT * FROM $CRYPTO_CURRENCIES_ENTITY_TABLE_NAME")
    suspend fun readAll(): List<CryptoCurrencyLocalDto>

    @Insert
    suspend fun insertAll(cryptoCurrencies: List<CryptoCurrencyLocalDto>)

    @Query("DELETE FROM $CRYPTO_CURRENCIES_ENTITY_TABLE_NAME")
    suspend fun deleteAll()
}
