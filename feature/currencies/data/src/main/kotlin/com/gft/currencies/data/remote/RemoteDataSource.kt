package com.gft.currencies.data.remote

import android.content.Context
import com.gft.currencies.data.remote.models.CryptoCurrencyRemoteDto
import com.gft.currencies.data.remote.models.FiatCurrencyRemoteDto
import com.gft.currencies.data.remote.models.toDomain
import com.gft.currencies.domain.models.CryptoCurrency
import com.gft.currencies.domain.models.FiatCurrency
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

private const val FAKE_REMOTE_CRYPTO_CURRENCIES_JSON_FILE_NAME = "crypto_currencies.json"
private const val FAKE_REMOTE_FIAT_CURRENCIES_JSON_FILE_NAME = "fiat_currencies.json"

internal class RemoteDataSource(
    private val context: Context,
    private val moshi: Moshi,
) {
    // TODO Czy to na pewno lazy robić? A może dostarczyć przez DI, jak samo Moshi?
    private val cryptoCurrencyRemoteDtosListAdapter by lazy {
        val dtosListType = Types.newParameterizedType(
            List::class.java,
            CryptoCurrencyRemoteDto::class.java,
        )

        moshi.adapter<List<CryptoCurrencyRemoteDto>>(dtosListType)
    }

    // TODO Czy to na pewno lazy robić? A może dostarczyć przez DI, jak samo Moshi?
    private val fiatCurrencyRemoteDtosListAdapter by lazy {
        val dtosListType = Types.newParameterizedType(
            List::class.java,
            FiatCurrencyRemoteDto::class.java,
        )

        moshi.adapter<List<FiatCurrencyRemoteDto>>(dtosListType)
    }

    fun fetchCryptoCurrencies(): List<CryptoCurrency> {
        val jsonString = context.assets.open(FAKE_REMOTE_CRYPTO_CURRENCIES_JSON_FILE_NAME).bufferedReader().use { bufferedReader ->
            bufferedReader.readText()
        }

        return cryptoCurrencyRemoteDtosListAdapter.fromJson(jsonString)?.toDomain() ?: emptyList()
    }

    fun fetchFiatCurrencies(): List<FiatCurrency> {
        val jsonString = context.assets.open(FAKE_REMOTE_FIAT_CURRENCIES_JSON_FILE_NAME).bufferedReader().use { bufferedReader ->
            bufferedReader.readText()
        }

        return fiatCurrencyRemoteDtosListAdapter.fromJson(jsonString)?.toDomain() ?: emptyList()
    }
}
