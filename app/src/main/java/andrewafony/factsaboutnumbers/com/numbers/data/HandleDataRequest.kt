package andrewafony.factsaboutnumbers.com.numbers.data

import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumbersCacheDataSource
import andrewafony.factsaboutnumbers.com.numbers.domain.HandleError
import andrewafony.factsaboutnumbers.com.numbers.domain.NumberFact

interface HandleDataRequest {

    suspend fun handle(block: suspend () -> NumberData): NumberFact

    class Base(
        private val cacheDataSource: NumbersCacheDataSource,
        private val mapperToDomain: NumberData.Mapper<NumberFact>,
        private val handleError: HandleError<Exception>,
    ) : HandleDataRequest {

        override suspend fun handle(block: suspend () -> NumberData): NumberFact = try {
            val result = block.invoke()
            cacheDataSource.saveNumber(result)
            result.map(mapperToDomain)
        } catch (e: Exception) {
            throw handleError.handle(e)
        }
    }
}