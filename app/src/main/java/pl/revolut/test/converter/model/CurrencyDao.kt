package pl.revolut.test.converter.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg currencies: Currency)

    @Query("SELECT * FROM currencies ORDER BY ordinal")
    fun getAll(): LiveData<List<Currency>>
}