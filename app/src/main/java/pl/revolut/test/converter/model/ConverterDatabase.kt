package pl.revolut.test.converter.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Currency::class, Rate::class], version = 1, exportSchema = false)
abstract class ConverterDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun rateDao(): RateDao

    companion object {

        @Volatile
        private var instance: ConverterDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, ConverterDatabase::class.java, "converter_db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch { addAllCurrencies(context) }
                    }
                })
                .build()


        private fun addAllCurrencies(context: Context) {
            getInstance(context.applicationContext).currencyDao().insertAll(
                Currency("EUR", "eur", "eu", 0),
                Currency("AUD", "aud", "au", 1),
                Currency("BGN", "bgn", "bg", 2),
                Currency("BRL", "brl", "br", 3),
                Currency("CAD", "cad", "ca", 4),
                Currency("CHF", "chf", "ch", 5),
                Currency("CNY", "cny", "cn", 6),
                Currency("CZK", "czk", "cz", 7),
                Currency("DKK", "dkk", "dk", 8),
                Currency("GBP", "gbp", "gb", 9),
                Currency("HKD", "hkd", "hk", 10),
                Currency("HRK", "hrk", "hr", 11),
                Currency("HUF", "huf", "hu", 12),
                Currency("IDR", "idr", "id", 13),
                Currency("ILS", "ils", "il", 14),
                Currency("INR", "inr", "ind", 15),
                Currency("ISK", "isk", "isk", 16),
                Currency("JPY", "jpy", "jp", 17),
                Currency("KRW", "krw", "kr", 18),
                Currency("MXN", "mxn", "mx", 19),
                Currency("MYR", "myr", "my", 20),
                Currency("NOK", "nok", "no", 21),
                Currency("NZD", "nzd", "nz", 22),
                Currency("PHP", "php", "ph", 23),
                Currency("PLN", "pln", "pl", 24),
                Currency("RON", "ron", "ro", 25),
                Currency("RUB", "rub", "ru", 26),
                Currency("SEK", "sek", "se", 27),
                Currency("SGD", "sgd", "sg", 28),
                Currency("THB", "thb", "th", 29),
                Currency("TRY", "try_lira", "tr", 30),
                Currency("USD", "usd", "us", 31),
                Currency("ZAR", "zar", "za", 32)
            )
        }
    }
}