package com.gft.currencies.data.repository

import com.gft.currencies.data.local.LocalDataSource
import com.gft.currencies.data.remote.RemoteDataSource
import com.gft.currencies.domain.models.CryptoCurrency
import com.gft.currencies.domain.models.FiatCurrency
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

class DefaultCurrenciesRepositoryTest {

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
    private lateinit var localDataSourceMock: LocalDataSource

    @MockK
    private lateinit var remoteDataSourceMock: RemoteDataSource

    private lateinit var testedObject: DefaultCurrenciesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        testedObject = DefaultCurrenciesRepository(
            localDataSource = localDataSourceMock,
            remoteDataSource = remoteDataSourceMock,
        )
    }

    @Test
    fun `GIVEN remote data source has data WHEN loadData is called THEN data from remote data source is saved to local data source`() = runTest {
        // GIVEN
        coEvery { remoteDataSourceMock.fetchCryptoCurrencies() } returns testCryptoCurrencies
        coEvery { remoteDataSourceMock.fetchFiatCurrencies() } returns testFiatCurrencies
        coEvery { localDataSourceMock.saveAll(any(), any()) } just Runs

        // WHEN
        testedObject.loadData()

        // THEN
        coVerify { localDataSourceMock.saveAll(testCryptoCurrencies, testFiatCurrencies) }
    }

    @Test
    fun `GIVEN remote data source is empty WHEN loadData is called THEN empty data is saved to local data source`() = runTest {
        // GIVEN
        coEvery { remoteDataSourceMock.fetchCryptoCurrencies() } returns emptyList()
        coEvery { remoteDataSourceMock.fetchFiatCurrencies() } returns emptyList()
        coEvery { localDataSourceMock.saveAll(any(), any()) } just Runs

        // WHEN
        testedObject.loadData()

        // THEN
        coVerify { localDataSourceMock.saveAll(emptyList(), emptyList()) }
    }

    @Test
    fun `WHEN clearData is called THEN local data source is cleared`() = runTest {
        coEvery { localDataSourceMock.deleteAll() } just Runs

        // WHEN
        testedObject.clearData()

        // THEN
        coVerify { localDataSourceMock.deleteAll() }
    }

    @Test
    fun `GIVEN local data source is empty WHEN getAllCryptoCurrencies is called THEN empty list is returned`() = runTest {
        // GIVEN
        coEvery { localDataSourceMock.getAllCryptoCurrencies() } returns emptyList()

        // WHEN
        val result = testedObject.getAllCryptoCurrencies()

        // THEN
        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN local data source is empty WHEN getAllFiatCurrencies is called THEN empty list is returned`() = runTest {
        // GIVEN
        coEvery { localDataSourceMock.getAllFiatCurrencies() } returns emptyList()

        // WHEN
        val result = testedObject.getAllFiatCurrencies()

        // THEN
        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN local data source has data WHEN getAllCryptoCurrencies is called THEN data from local source is returned`() = runTest {
        // GIVEN
        coEvery { localDataSourceMock.getAllCryptoCurrencies() } returns testCryptoCurrencies

        // WHEN
        val result = testedObject.getAllCryptoCurrencies()

        // THEN
        assertEquals(testCryptoCurrencies, result)
    }

    @Test
    fun `GIVEN local data source has data WHEN getAllFiatCurrencies is called THEN data from local source is returned`() = runTest {
        // GIVEN
        coEvery { localDataSourceMock.getAllFiatCurrencies() } returns testFiatCurrencies

        // WHEN
        val result = testedObject.getAllFiatCurrencies()

        // THEN
        assertEquals(testFiatCurrencies, result)
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
