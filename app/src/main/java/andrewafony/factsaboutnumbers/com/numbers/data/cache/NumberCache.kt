package andrewafony.factsaboutnumbers.com.numbers.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "numbers_table")
data class NumberCache(
    @PrimaryKey val number: String,
    val fact: String,
    val date: Long,
)