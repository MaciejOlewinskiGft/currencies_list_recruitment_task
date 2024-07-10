package com.gft.currencies.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gft.currencies.data.local.db.CRYPTO_CURRENCIES_ENTITY_ID_COLUMN_NAME
import com.gft.currencies.data.local.db.CRYPTO_CURRENCIES_ENTITY_NAME_COLUMN_NAME
import com.gft.currencies.data.local.db.CRYPTO_CURRENCIES_ENTITY_SYMBOL_COLUMN_NAME
import com.gft.currencies.data.local.db.CRYPTO_CURRENCIES_ENTITY_TABLE_NAME
import com.gft.currencies.domain.models.CryptoCurrency

@Entity(tableName = CRYPTO_CURRENCIES_ENTITY_TABLE_NAME)
internal data class CryptoCurrencyLocalDto(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = CRYPTO_CURRENCIES_ENTITY_ID_COLUMN_NAME)
    val id: String,

    @ColumnInfo(name = CRYPTO_CURRENCIES_ENTITY_NAME_COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = CRYPTO_CURRENCIES_ENTITY_SYMBOL_COLUMN_NAME)
    val symbol: String,
)

internal fun List<CryptoCurrencyLocalDto>.toDomain() = map { dto ->
    CryptoCurrency(
        id = dto.id,
        name = dto.name,
        symbol = dto.symbol,
    )
}

internal fun List<CryptoCurrency>.toLocal() = map { cryptoCurrency ->
    CryptoCurrencyLocalDto(
        id = cryptoCurrency.id,
        name = cryptoCurrency.name,
        symbol = cryptoCurrency.symbol,
    )
}
