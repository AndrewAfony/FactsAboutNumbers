package andrewafony.factsaboutnumbers.com.numbers.domain

import andrewafony.factsaboutnumbers.com.numbers.presentation.NumberUi

class NumberUiMapper(): NumberFact.Mapper<NumberUi> {
    override fun map(id: String, fact: String): NumberUi = NumberUi(id, fact)
}