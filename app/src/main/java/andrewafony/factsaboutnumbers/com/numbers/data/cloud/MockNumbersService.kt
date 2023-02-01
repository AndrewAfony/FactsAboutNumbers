package andrewafony.factsaboutnumbers.com.numbers.data.cloud

import retrofit2.Response

class MockNumbersService: NumbersService {

    override suspend fun fact(number: String): String = "fact about $number"

    override suspend fun random(): Response<String> {
        TODO()
    }
}