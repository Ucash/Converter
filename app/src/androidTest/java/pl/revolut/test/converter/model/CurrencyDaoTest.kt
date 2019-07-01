package pl.revolut.test.converter.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.revolut.test.converter.utils.getValue

class CurrencyDaoTest {
    private lateinit var database: ConverterDatabase
    private lateinit var currencyDao: CurrencyDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, ConverterDatabase::class.java).build()
        currencyDao = database.currencyDao()

        currencyDao.insertAll(*TEST_CURRENCIES)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getAllOrderedTest() {
        getValue(currencyDao.getAll()).let {
            assertEquals(2, it.size)
            assertEquals("PLN", it[0].code)
            assertEquals("EUR", it[1].code)
            assertTrue(it[0].ordinal < it[1].ordinal)
        }
    }

    @Test
    fun insertNewCurrencyTest() {
        currencyDao.insertAll(Currency("USD", "us", "us", 1))
        getValue(currencyDao.getAll()).let {
            assertEquals(3, it.size)
            assertEquals("PLN", it[0].code)
            assertEquals("USD", it[1].code)
            assertEquals("EUR", it[2].code)
            assertTrue(it[0].ordinal < it[1].ordinal)
            assertTrue(it[1].ordinal < it[2].ordinal)
        }
    }

    @Test
    fun rearrangeCurrenciesTest() {
        currencyDao.insertAll(TEST_CURRENCIES[0].copy(ordinal = 0), TEST_CURRENCIES[1].copy(ordinal = 2))
        getValue(currencyDao.getAll()).let {
            assertEquals(2, it.size)
            assertEquals("EUR", it[0].code)
            assertEquals("PLN", it[1].code)
            assertTrue(it[0].ordinal < it[1].ordinal)
        }
    }

    companion object {
        private val TEST_CURRENCIES = arrayOf(
            Currency("EUR", "eu", "eu", 2),
            Currency("PLN", "pl", "pl", 0)
        )

    }
}