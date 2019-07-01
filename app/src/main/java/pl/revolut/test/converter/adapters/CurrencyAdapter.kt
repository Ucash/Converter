package pl.revolut.test.converter.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.revolut.test.converter.databinding.ItemCurrencyBinding
import pl.revolut.test.converter.model.Currency
import pl.revolut.test.converter.utils.DecimalDigitsInputFilter
import pl.revolut.test.converter.viewmodels.CurrencyRatesViewModel

class CurrencyAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: CurrencyRatesViewModel,
    private val onCurrencySelection: (Currency) -> Unit
) : ListAdapter<Currency, CurrencyAdapter.ViewHolder>(CurrencyDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = getItem(position)
        holder.apply {
            bind(currency, lifecycleOwner, viewModel, View.OnClickListener {
                val list = currentList.toMutableList()
                list.remove(currency)
                list.add(0, currency)
                viewModel.setTrackedCurrency(currency)
                binding.amount.setText(binding.rate.text)
                submitList(list) { onCurrencySelection(currency) }
            })
        }
    }

    class ViewHolder(val binding: ItemCurrencyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Currency,
            lifecycleOwner: LifecycleOwner,
            viewModel: CurrencyRatesViewModel,
            listener: View.OnClickListener
        ) {
            binding.apply {
                itemClickListener = listener
                currency = item
                this.viewModel = viewModel
                this.lifecycleOwner = lifecycleOwner
                amount.addTextChangedListener(createTextWatcher(viewModel, item))
                amount.filters = arrayOf(DecimalDigitsInputFilter())
                if (item == viewModel.trackedCurrency.value) {
                    amount.setText(viewModel.amount.toString())
                }
            }
        }

        private fun createTextWatcher(viewModel: CurrencyRatesViewModel, currency: Currency) = object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (viewModel.trackedCurrency.value != currency) return
                viewModel.setAmount(s.toString().toDoubleOrNull() ?: 0.0)
            }

            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        }
    }
}

class CurrencyDiff : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = oldItem.code == newItem.code

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = oldItem == newItem
}
