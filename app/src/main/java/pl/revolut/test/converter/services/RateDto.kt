package pl.revolut.test.converter.services

import pl.revolut.test.converter.model.Rate

data class RateDto (
    val base: String,
    val rates: Map<String, Double>
) {
    fun toEntities() = rates.map {
        Rate(from = base, to = it.key, rate = it.value)
    }
}