package com.gft.currencies.data.remote.models

import com.gft.currencies.domain.models.FiatCurrency
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class FiatCurrencyRemoteDto(
    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "symbol")
    val symbol: String,

    @Json(name = "code")
    val code: String,
)

internal fun List<FiatCurrencyRemoteDto>.toDomain() = map { dto ->
    FiatCurrency(
        id = dto.id,
        name = dto.name,
        symbol = dto.symbol,
        code = dto.code,
    )
}
