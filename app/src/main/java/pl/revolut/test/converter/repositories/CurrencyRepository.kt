package pl.revolut.test.converter.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.revolut.test.converter.model.Currency
import pl.revolut.test.converter.model.CurrencyDao

class CurrencyRepository private constructor(private val dao: CurrencyDao) {

    fun getAll() = dao.getAll()

    suspend fun insertAll(vararg currencies: Currency) = withContext(Dispatchers.IO) {
        dao.insertAll(*currencies)
    }

    companion object {

        @Volatile
        private var instance: CurrencyRepository? = null

        fun getInstance(currencyDao: CurrencyDao) =
            instance ?: synchronized(this) {
                instance ?: CurrencyRepository(currencyDao).also { instance = it }
            }
    }
}