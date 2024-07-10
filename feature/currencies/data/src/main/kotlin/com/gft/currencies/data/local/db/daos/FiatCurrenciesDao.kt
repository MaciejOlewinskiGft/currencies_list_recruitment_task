package com.gft.currencies.data.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gft.currencies.data.local.db.FIAT_CURRENCIES_ENTITY_TABLE_NAME
import com.gft.currencies.data.local.db.entities.FiatCurrencyLocalDto

@Dao
internal interface FiatCurrenciesDao {

    @Query("SELECT * FROM $FIAT_CURRENCIES_ENTITY_TABLE_NAME")
    suspend fun readAll(): List<FiatCurrencyLocalDto>

    @Insert
    suspend fun insertAll(fiatCurrencies: List<FiatCurrencyLocalDto>)

    @Query("DELETE FROM $FIAT_CURRENCIES_ENTITY_TABLE_NAME")
    suspend fun deleteAll()
}
