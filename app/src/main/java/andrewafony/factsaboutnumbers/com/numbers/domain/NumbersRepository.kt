package andrewafony.factsaboutnumbers.com.numbers.domain

interface NumbersRepository {

    suspend fun allNumbersFacts(): List<NumberFact>

    suspend fun numberFact(number: String): NumberFact

    suspend fun randomNumberFact(): NumberFact
}
