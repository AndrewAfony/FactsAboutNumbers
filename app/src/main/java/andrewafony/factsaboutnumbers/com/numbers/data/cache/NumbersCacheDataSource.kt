package andrewafony.factsaboutnumbers.com.numbers.data.cache

import andrewafony.factsaboutnumbers.com.numbers.data.NumberData

interface NumbersCacheDataSource: FetchNumber {

    suspend fun allNumbers(): List<NumberData>

    suspend fun contains(number: String): Boolean

    suspend fun saveNumber(numberData: NumberData)
}

interface FetchNumber {

    suspend fun number(number: String): NumberData
}