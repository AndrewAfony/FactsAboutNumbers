package andrewafony.factsaboutnumbers.com.numbers.data.cloud

import okhttp3.Headers
import retrofit2.Response

class MockNumbersService: NumbersService {

    private var count = 0

    override suspend fun fact(number: String): String = "fact about $number"

    override suspend fun random(): Response<String> {
        count++
        return Response.success(
            "fact about $count",
            Headers.headersOf("X-Numbers-API-Number $count") //todo tests
        )
    }
}