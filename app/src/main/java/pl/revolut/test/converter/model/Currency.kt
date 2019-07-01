package pl.revolut.test.converter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class Currency (
    @PrimaryKey val code: String,
    val nameRes: String,
    val flagRes: String,
    var ordinal: Int
)