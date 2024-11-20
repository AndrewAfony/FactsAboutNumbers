package andrewafony.factsaboutnumbers.com.numbers.presentation

import andrewafony.factsaboutnumbers.com.numbers.domain.NumberFact
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersResult

class NumbersResultMapper(
    private val communications: NumbersCommunications,
    private val mapper: NumberFact.Mapper<NumberUi>,
) : NumbersResult.Mapper<Unit> {

    override fun map(list: List<NumberFact>, errorMessage: String) {
        communications.showState(
            if (errorMessage.isEmpty()) {
                if (list.isNotEmpty())
                    communications.showList(list.map { it.map(mapper) })
                UiState.Success()
            } else
                UiState.ShowError(errorMessage)
        )
    }
}