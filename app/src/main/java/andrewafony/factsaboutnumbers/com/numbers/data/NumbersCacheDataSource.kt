package andrewafony.factsaboutnumbers.com.numbers.data

interface NumbersCacheDataSource: FetchNumber {

    suspend fun allNumbersFact(): List<NumberData>

    suspend fun contains(number: String): Boolean

    suspend fun saveNumberFact(numberData: NumberData)
}

interface FetchNumber {

    suspend fun number(number: String): NumberData
}