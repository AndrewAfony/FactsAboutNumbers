package andrewafony.factsaboutnumbers.com.numbers.presentation

import andrewafony.factsaboutnumbers.com.numbers.domain.NumberFact
import andrewafony.factsaboutnumbers.com.numbers.domain.NumberUiMapper
import org.junit.Assert.*
import org.junit.Test

class NumbersResultMapperTest : BaseTest() {

    @Test
    fun test_error() {
        val communications = TestNumbersCommunication()
        val mapper = NumbersResultMapper(communications, NumberUiMapper())

        mapper.map(emptyList(), "not empty message")
        assertEquals(UiState.ShowError("not empty message"), communications.stateCalledList[0])
    }

    @Test
    fun test_sucess_no_list() {
        val communications = TestNumbersCommunication()
        val mapper = NumbersResultMapper(communications, NumberUiMapper())

        mapper.map(emptyList(), "")
        assertEquals(0, communications.timesShowList)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)
    }

    @Test
    fun test_success_list() {
        val communications = TestNumbersCommunication()
        val mapper = NumbersResultMapper(communications, NumberUiMapper())

        mapper.map(listOf(NumberFact("4", "fact 4")), "")
        assertEquals(1, communications.timesShowList)
        assertEquals(true, communications.stateCalledList[0] is UiState.Success)
        assertEquals(NumberUi("4", "fact 4"), communications.numbersList[0])
    }
}