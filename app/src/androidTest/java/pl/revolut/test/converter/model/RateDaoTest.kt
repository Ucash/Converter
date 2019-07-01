package pl.revolut.test.converter.model

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.revolut.test.converter.utils.getValue

class RateDaoTest {
    private lateinit var database: ConverterDatabase
    private lateinit var rateDao: RateDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, ConverterDatabase::class.java).build()
        rateDao = database.rateDao()

        database.currencyDao().insertAll(*TEST_CURRENCIES)
        rateDao.insertAll(*TEST_RATES)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun updateRateTest() {
        rateDao.insertAll(Rate("EUR", "PLN", 1.0))
        getValue(rateDao.getAllForCurrency("EUR")).let {
            assertEquals(1, it.size)
            assertEquals("PLN", it[0].to)
            assertEquals(1.0, it[0].rate, Double.MIN_VALUE)
        }
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertRateForUnknownCurrencyTest() {
        rateDao.insertAll(Rate("EUR", "USD", 2.0))
    }


    companion object {
        private val TEST_CURRENCIES = arrayOf(
            Currency("EUR", "eu", "eu", 0),
            Currency("PLN", "pl", "pl", 1)
        )
        private val TEST_RATES = arrayOf(
            Rate("EUR", "PLN", 0.25),
            Rate("PLN", "EUR", 4.0)
        )
    }
}