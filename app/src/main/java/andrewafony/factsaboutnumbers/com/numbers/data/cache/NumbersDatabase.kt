package andrewafony.factsaboutnumbers.com.numbers.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NumberCache::class], version = 1)
abstract class NumbersDatabase: RoomDatabase() {

    abstract fun dao(): NumbersDao
}