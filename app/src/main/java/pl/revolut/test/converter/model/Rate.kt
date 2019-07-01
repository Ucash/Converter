package pl.revolut.test.converter.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "rates",
    primaryKeys = ["from", "to"],
    indices = [
        Index(value = ["from"]),
        Index(value = ["to"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Currency::class,
            parentColumns = ["code"],
            childColumns = ["from"]
        ),
        ForeignKey(
            entity = Currency::class,
            parentColumns = ["code"],
            childColumns = ["to"]
        )
    ]
)
data class Rate(
    val from: String,
    val to: String,
    val rate: Double
)