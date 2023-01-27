package andrewafony.factsaboutnumbers.com.numbers.data.cloud

import andrewafony.factsaboutnumbers.com.numbers.data.NumberData
import andrewafony.factsaboutnumbers.com.numbers.data.cache.FetchNumber

interface NumbersCloudDataSource : FetchNumber {

    suspend fun randomNumber(): NumberData

    class Base(private val service: NumbersService): NumbersCloudDataSource {

        override suspend fun number(number: String): NumberData {
            val fact = service.fact(number)
            return NumberData(id = number, fact = fact)
        }

        override suspend fun randomNumber(): NumberData {
            val response = service.random()
            val body = response.body() ?: throw IllegalStateException("Service unavailable")
            val headers = response.headers()
            headers[RANDOM_API_HEADER]?.let {
                return NumberData(it, body)
            }
            throw IllegalStateException("Service unavailable")
        }

        companion object {
            private const val RANDOM_API_HEADER = "X-Numbers-API-Number"
        }
    }
}
