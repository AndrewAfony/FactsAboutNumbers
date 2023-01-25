package andrewafony.factsaboutnumbers.com.numbers.data

import andrewafony.factsaboutnumbers.com.numbers.domain.NumberFact

class NumberDataToDomainMapper: NumberData.Mapper<NumberFact> {

    override fun map(id: String, fact: String): NumberFact = NumberFact(id, fact)
}