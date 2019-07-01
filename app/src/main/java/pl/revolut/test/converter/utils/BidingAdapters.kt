package pl.revolut.test.converter.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import pl.revolut.test.converter.model.Currency

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

@Suppress("DEPRECATION")
@BindingAdapter("iconResourceName")
fun ImageView.setIcon(iconResourceName: String) {
    this.setImageDrawable(context.run {
        resources.getDrawable(resources.getIdentifier(iconResourceName, "drawable", packageName))
    })
}

@BindingAdapter("stringResourceName")
fun TextView.setStringValue(stringResourceName: String) {
    this.text = context.run {
        resources.getString(resources.getIdentifier(stringResourceName, "string", packageName))
    }
}

@BindingAdapter(value = ["visibleIfSame1", "visibleIfSame2"])
fun TextView.visibleIfSame(currency1: Currency, currency2: Currency) {
    this.isVisible = currency1 == currency2
}

@BindingAdapter(value = ["visibleIfDifferent1", "visibleIfDifferent2"])
fun TextView.visibleIfDifferent(currency1: Currency, currency2: Currency) {
    this.isVisible = currency1 != currency2
}