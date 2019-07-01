package pl.revolut.test.converter.utils

import android.text.InputFilter
import android.text.Spanned

class DecimalDigitsInputFilter : InputFilter {
    override fun filter(
        source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int
    ): CharSequence? {
        val builder = StringBuilder(dest).apply {
            replace(dstart, dend, source.subSequence(start, end).toString())
        }
        return if (!builder.toString().matches(("(([1-9]{1})([0-9]+)?)?(\\.[0-9]{0,2})?").toRegex())) {
            if (source.isEmpty()) dest.subSequence(dstart, dend) else ""
        } else null
    }
}