package pl.revolut.test.converter.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.revolut.test.converter.model.RateDao
import pl.revolut.test.converter.services.RateService
import java.io.IOException

class RateRepository private constructor(private val dao: RateDao, private val service: RateService) {

    fun getAllForCurrency(currency: String) = dao.getAllForCurrency(currency)

    suspend fun updateRatesRemotely(currency: String) = withContext(Dispatchers.IO) {
        //TODO: more gracefully exception handling (also in UI)
        try {
            val response = service.getRatesForCurrency(currency).execute()
            if (response.isSuccessful) {
                response.body()?.let { dao.insertAll(*it.toEntities().toTypedArray()) }
                true
            } else {
                false
            }
        } catch (e: IOException) {
            false
        }
    }

    companion object {

        @Volatile
        private var instance: RateRepository? = null

        fun getInstance(rateDao: RateDao) =
            instance ?: synchronized(this) {
                instance ?: RateRepository(rateDao, RateService.instance).also { instance = it }
            }
    }
}