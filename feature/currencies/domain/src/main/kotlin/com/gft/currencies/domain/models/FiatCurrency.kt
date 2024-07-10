package com.gft.currencies.domain.models

data class FiatCurrency(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String,
)
