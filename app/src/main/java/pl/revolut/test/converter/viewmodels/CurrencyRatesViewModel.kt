package pl.revolut.test.converter.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import kotlinx.coroutines.*
import pl.revolut.test.converter.model.Currency
import pl.revolut.test.converter.repositories.CurrencyRepository
import pl.revolut.test.converter.repositories.RateRepository
import java.util.*

class CurrencyRatesViewModel(
    private val currencyRepository: CurrencyRepository,
    private val rateRepository: RateRepository
) : ViewModel(), CoroutineScope {
    private val viewModelJob = SupervisorJob()
    override val coroutineContext = Dispatchers.IO + viewModelJob

    private val exchange = MutableLiveData<Exchange>()

    val trackedCurrency = MutableLiveData<Currency>()
    val error = MutableLiveData<Boolean>(false)
    var amount: Double = 100.0
        private set

    val currencies = currencyRepository.getAll()

    val rates = exchange.switchMap { exchange ->
        rateRepository.getAllForCurrency(exchange.currency.code).map { rates ->
            rates.map {
                it.to to String.format(Locale.getDefault(), "%.2f", it.rate * amount)
            }.toMap()
        }
    }

    init {
        launch {
            while (isActive) {
                updateRatesForTrackedCurrency()
                delay(1000)
            }
        }
    }

    fun setTrackedCurrency(currency: Currency) {
        this.trackedCurrency.value = currency
        launch { updateRatesForTrackedCurrency() }
        with(exchange.value) {
            if (this == null) {
                exchange.value = Exchange(currency, amount)
            } else {
                exchange.value = Exchange(currency, amount)
            }
        }
    }

    fun setAmount(amount: Double) {
        this.amount = amount
        exchange.value?.let {
            exchange.value = Exchange(it.currency, amount)
        }
    }

    fun updateCurrencies(list: List<Currency>) = launch {
        currencyRepository.insertAll(*list.toTypedArray())
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private suspend fun updateRatesForTrackedCurrency() {
        trackedCurrency.value?.let {
            rateRepository.updateRatesRemotely(it.code).also {
                withContext(Dispatchers.Main) { error.value = !it }
            }
        }
    }
}

data class Exchange(val currency: Currency, val amount: Double)