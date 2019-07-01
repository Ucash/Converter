package pl.revolut.test.converter.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg rates: Rate)

    @Query("SELECT * FROM rates WHERE `from` = :currency")
    fun getAllForCurrency(currency: String): LiveData<List<Rate>>

}