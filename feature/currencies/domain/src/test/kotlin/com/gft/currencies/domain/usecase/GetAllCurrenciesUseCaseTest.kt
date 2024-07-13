package com.gft.currencies.domain.usecase

import com.gft.currencies.domain.models.CryptoCurrency
import com.gft.currencies.domain.models.FiatCurrency
import com.gft.currencies.domain.repository.CurrenciesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class GetAllCurrenciesUseCaseTest {

    private val testCryptoCurrencies = listOf(
        getRandomCryptoCurrency(),
        getRandomCryptoCurrency(),
        getRandomCryptoCurrency(),
    )
    private val testFiatCurrencies = listOf(
        getRandomFiatCurrency(),
        getRandomFiatCurrency(),
        getRandomFiatCurrency(),
    )

    @MockK
    private lateinit var currenciesRepositoryMock: CurrenciesRepository

    private lateinit var testedObject: GetAllCurrenciesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        testedObject = GetAllCurrenciesUseCase(
            currenciesRepository = currenciesRepositoryMock,
        )
    }

    // FOR REVIEWER: Those 4 tests can be replaced by Junit5's parameterized test (Junit4 also have parameterized tests, but much less flexible)
    @Test
    fun `GIVEN repository has data for crypto and fiat currencies WHEN invoke is called THEN crypto and fiat currencies are returned`() = runTest {
        // GIVEN
        coEvery { currenciesRepositoryMock.getAllCryptoCurrencies() } returns testCryptoCurrencies
        coEvery { currenciesRepositoryMock.getAllFiatCurrencies() } returns testFiatCurrencies

        // WHEN
        val result = testedObject()

        // THEN
        assertEquals(testCryptoCurrencies to testFiatCurrencies, result)
    }

    @Test
    fun `GIVEN repository has data only for crypto currencies WHEN invoke is called THEN crypto currencies are returned`() = runTest {
        // GIVEN
        coEvery { currenciesRepositoryMock.getAllCryptoCurrencies() } returns testCryptoCurrencies
        coEvery { currenciesRepositoryMock.getAllFiatCurrencies() } returns emptyList()

        // WHEN
        val result = testedObject()

        // THEN
        assertEquals(testCryptoCurrencies to emptyList<FiatCurrency>(), result)
    }

    @Test
    fun `GIVEN repository has data only for fiat currencies WHEN invoke is called THEN fiat currencies are returned`() = runTest {
        // GIVEN
        coEvery { currenciesRepositoryMock.getAllCryptoCurrencies() } returns emptyList()
        coEvery { currenciesRepositoryMock.getAllFiatCurrencies() } returns testFiatCurrencies

        // WHEN
        val result = testedObject()

        // THEN
        assertEquals(emptyList<CryptoCurrency>() to testFiatCurrencies, result)
    }

    @Test
    fun `GIVEN repository has no data WHEN invoke is called THEN empty list is returned`() = runTest {
        // GIVEN
        coEvery { currenciesRepositoryMock.getAllCryptoCurrencies() } returns emptyList()
        coEvery { currenciesRepositoryMock.getAllFiatCurrencies() } returns emptyList()

        // WHEN
        val result = testedObject()

        // THEN
        assertEquals(emptyList<CryptoCurrency>() to emptyList<FiatCurrency>(), result)
    }

    private fun getRandomCryptoCurrency() = Random.nextInt().let { randomInt ->
        CryptoCurrency(
            id = "id$randomInt",
            name = "name$randomInt",
            symbol = "symbol$randomInt",
        )
    }

    private fun getRandomFiatCurrency() = Random.nextInt().let { randomInt ->
        FiatCurrency(
            id = "id$randomInt",
            name = "name$randomInt",
            symbol = "symbol$randomInt",
            code = "code$randomInt",
        )
    }
}
