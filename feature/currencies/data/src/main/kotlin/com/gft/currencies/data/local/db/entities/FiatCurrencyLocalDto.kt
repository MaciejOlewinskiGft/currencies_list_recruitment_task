package com.gft.currencies.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gft.currencies.data.local.db.FIAT_CURRENCIES_ENTITY_CODE_COLUMN_NAME
import com.gft.currencies.data.local.db.FIAT_CURRENCIES_ENTITY_ID_COLUMN_NAME
import com.gft.currencies.data.local.db.FIAT_CURRENCIES_ENTITY_NAME_COLUMN_NAME
import com.gft.currencies.data.local.db.FIAT_CURRENCIES_ENTITY_SYMBOL_COLUMN_NAME
import com.gft.currencies.data.local.db.FIAT_CURRENCIES_ENTITY_TABLE_NAME
import com.gft.currencies.domain.models.FiatCurrency

@Entity(tableName = FIAT_CURRENCIES_ENTITY_TABLE_NAME)
internal data class FiatCurrencyLocalDto(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = FIAT_CURRENCIES_ENTITY_ID_COLUMN_NAME)
    val id: String,

    @ColumnInfo(name = FIAT_CURRENCIES_ENTITY_NAME_COLUMN_NAME)
    val name: String,

    @ColumnInfo(name = FIAT_CURRENCIES_ENTITY_SYMBOL_COLUMN_NAME)
    val symbol: String,

    @ColumnInfo(name = FIAT_CURRENCIES_ENTITY_CODE_COLUMN_NAME)
    val code: String,
)

internal fun List<FiatCurrencyLocalDto>.toDomain() = map { dto ->
    FiatCurrency(
        id = dto.id,
        name = dto.name,
        symbol = dto.symbol,
        code = dto.code,
    )
}

internal fun List<FiatCurrency>.toLocal() = map { fiatCurrency ->
    FiatCurrencyLocalDto(
        id = fiatCurrency.id,
        name = fiatCurrency.name,
        symbol = fiatCurrency.symbol,
        code = fiatCurrency.code,
    )
}
