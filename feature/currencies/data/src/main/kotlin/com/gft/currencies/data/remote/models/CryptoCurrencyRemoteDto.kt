package com.gft.currencies.data.remote.models

import com.gft.currencies.domain.models.CryptoCurrency
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class CryptoCurrencyRemoteDto(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "symbol")
    val symbol: String,
)

internal fun List<CryptoCurrencyRemoteDto>.toDomain() = map { dto ->
    CryptoCurrency(
        id = dto.id,
        name = dto.name,
        symbol = dto.symbol,
    )
}
