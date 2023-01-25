package andrewafony.factsaboutnumbers.com.numbers.data

interface NumbersCloudDataSource : FetchNumber {

    suspend fun randomNumber(): NumberData
}
