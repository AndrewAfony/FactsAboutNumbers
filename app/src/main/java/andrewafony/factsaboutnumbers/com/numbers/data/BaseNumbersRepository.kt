package andrewafony.factsaboutnumbers.com.numbers.data

import andrewafony.factsaboutnumbers.com.numbers.domain.NumberFact
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersRepository

class BaseNumbersRepository(
    private val cloudDataSource: NumbersCloudDataSource,
    private val cacheDataSource: NumbersCacheDataSource,
    private val mapperToDomain: NumberData.Mapper<NumberFact>,
    private val handleDataRequest: HandleDataRequest,
) : NumbersRepository {

    override suspend fun allNumbersFacts(): List<NumberFact> {
        val data = cacheDataSource.allNumbers()
        return data.map { it.map(mapperToDomain) }
    }

    override suspend fun numberFact(number: String): NumberFact = handleDataRequest.handle {
        val dataSource =
            (if (cacheDataSource.contains(number)) cacheDataSource else cloudDataSource)
        dataSource.number(number)
    }

    override suspend fun randomNumberFact(): NumberFact = handleDataRequest.handle {
        cloudDataSource.randomNumber()
    }

}

