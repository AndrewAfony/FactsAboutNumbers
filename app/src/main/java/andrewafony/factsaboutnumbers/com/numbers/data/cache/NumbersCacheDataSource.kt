package andrewafony.factsaboutnumbers.com.numbers.data.cache

import andrewafony.factsaboutnumbers.com.numbers.data.NumberData
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface NumbersCacheDataSource : FetchNumber {

    suspend fun allNumbers(): List<NumberData>

    suspend fun contains(number: String): Boolean

    suspend fun saveNumber(numberData: NumberData)

    class Base(
        private val dao: NumbersDao,
        private val dataToCache: NumberData.Mapper<NumberCache>,
    ) : NumbersCacheDataSource {

        private val mutex = Mutex()

        override suspend fun allNumbers(): List<NumberData> = mutex.withLock {
            val data = dao.allNumbers()
            return data.map { NumberData(it.number, it.fact) }
        }

        override suspend fun contains(number: String): Boolean = mutex.withLock {
            dao.numberExist(number)
        }

        override suspend fun saveNumber(numberData: NumberData) = mutex.withLock {
            dao.insert(numberData.map(dataToCache))
        }

        override suspend fun number(number: String): NumberData = mutex.withLock {
            val numberCache = dao.number(number)
            return NumberData(numberCache.number, numberCache.fact)
        }
    }
}

interface FetchNumber {

    suspend fun number(number: String): NumberData
}