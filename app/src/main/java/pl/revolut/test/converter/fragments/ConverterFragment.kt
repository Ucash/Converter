package pl.revolut.test.converter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import pl.revolut.test.converter.R
import pl.revolut.test.converter.adapters.CurrencyAdapter
import pl.revolut.test.converter.databinding.FragmentConverterBinding
import pl.revolut.test.converter.model.Currency
import pl.revolut.test.converter.viewmodels.CurrencyRatesViewModel
import pl.revolut.test.converter.viewmodels.CurrencyRatesViewModelFactory

class ConverterFragment : Fragment() {

    private val viewModel: CurrencyRatesViewModel by viewModels { CurrencyRatesViewModelFactory.create(requireContext()) }

    private lateinit var adapter: CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentConverterBinding.inflate(inflater, container, false).apply {
            currencyRatesViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            adapter = CurrencyAdapter(viewLifecycleOwner, viewModel) {
                updateActionBar(it)
                (currenciesList.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
            }
            currenciesList.adapter = adapter
            viewModel.currencies.observe(viewLifecycleOwner) {
                if (it.isEmpty()) return@observe
                it.first().apply {
                    viewModel.setTrackedCurrency(this)
                    updateActionBar(this)
                }
                adapter.submitList(it)
            }
        }
        return binding.root
    }

    private fun updateActionBar(currency: Currency) {
        //TODO: Replace with sticky first element (maybe by ItemDecoration)
        requireActivity().title = getString(R.string.title, currency.code)
    }

    override fun onStop() {
        super.onStop()
        if (!::adapter.isInitialized) return
        adapter.currentList.let {
            it.forEachIndexed { index, currency -> currency.ordinal = index }
            viewModel.updateCurrencies(it)
        }
    }
}