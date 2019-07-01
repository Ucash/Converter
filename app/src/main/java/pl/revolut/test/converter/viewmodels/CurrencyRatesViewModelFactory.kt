package pl.revolut.test.converter.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.revolut.test.converter.model.ConverterDatabase
import pl.revolut.test.converter.repositories.CurrencyRepository
import pl.revolut.test.converter.repositories.RateRepository

class CurrencyRatesViewModelFactory private constructor(
    private val currencyRepository: CurrencyRepository,
    private val rateRepository: RateRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        CurrencyRatesViewModel(currencyRepository, rateRepository) as T

    companion object {
        fun create(context: Context) = with(ConverterDatabase.getInstance(context.applicationContext)) {
            CurrencyRatesViewModelFactory(
                CurrencyRepository.getInstance(this.currencyDao()),
                RateRepository.getInstance(this.rateDao())
            )
        }
    }
}