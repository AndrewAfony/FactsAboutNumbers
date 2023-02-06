package andrewafony.factsaboutnumbers.com.numbers.domain

interface NumbersRepository: RandomNumberRepository {

    suspend fun allNumbersFacts(): List<NumberFact>

    suspend fun numberFact(number: String): NumberFact

}

interface RandomNumberRepository {

    suspend fun randomNumberFact(): NumberFact

}