package andrewafony.factsaboutnumbers.com.numbers.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NumbersDao {

    @Query("SELECT * FROM numbers_table ORDER BY date ASC")
    suspend fun allNumbers(): List<NumberCache> // todo remake to flow

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(number: NumberCache)

    @Query("SELECT * FROM numbers_table WHERE number = :number")
    suspend fun number(number: String): NumberCache

    @Query("SELECT EXISTS(SELECT * FROM numbers_table WHERE number = :number)")
    suspend fun numberExist(number: String): Boolean

}