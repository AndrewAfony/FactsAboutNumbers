package andrewafony.factsaboutnumbers.com.numbers

import org.junit.Assert.assertEquals
import org.junit.Test

class NumbersViewModelTest {

    /**
     * Initial test
     */
    @Test
    fun test_init_and_reinit() {
        val communication = TestNumbersCommunication()
        val interactor = TestNumbersInteractor()

        val viewModel = NumbersViewModel(communication, interactor)
        interactor.changeExpectedResult(NumbersResult.Success())

        viewModel.init(isFirstRun = true)

        assertEquals(1, communication.progressCalledList.size) // show progress bar count
        assertEquals(true, communication.progressCalledList[0]) // progress bar is shown

        assertEquals(2, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size) // ??????????
        assertEquals(UiState.Success(emptyList<NumberFact>()), communication.stateCalledList[0])

        assertEquals(0, communication.numbersList.size)
        assertEquals(0, communication.timesShowList)

        interactor.changeExpectedResult(NumbersResult.Failure())
        viewModel.fetchRandomNumberData()

        assertEquals(3, communication.progressCalledList.size)
        assertEquals(true, communication.progressCalledList[2])

        assertEquals(1, interactor.fetchAboutRandomNumberCalledList.size)

        assertEquals(4, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[3])

        assertEquals(2, communication.stateCalledList.size) // ??????????
        assertEquals(UiState.Error("No internet connection"), communication.stateCalledList[1])
        assertEquals(0, communication.timesShowList)

        viewModel.init(isFirstRun = false)

        assertEquals(4, communication.progressCalledList.size)
        assertEquals(2, communication.stateCalledList.size) // ??????????
        assertEquals(1, communication.timesShowList)

    }

    @Test
    fun test_fact_about_empty_number() {
        val communication = TestNumbersCommunication()
        val interactor = TestNumbersInteractor()

        val viewModel = NumbersViewModel(communication, interactor)

        viewModel.fetchFact("")

        assertEquals(0, interactor.fetchAboutNumberCalledList.size)

        assertEquals(0, communication.progressCalledList.size)

        assertEquals(1, communication.stateCalledList.size) // ??????????
        assertEquals(UiState.Error("Entered number is empty"), communication.stateCalledList[0])

        assertEquals(0, communication.timesShowList)
    }

    @Test
    fun test_fact_about_number() {
        val communication = TestNumbersCommunication()
        val interactor = TestNumbersInteractor()

        val viewModel = NumbersViewModel(communication, interactor)

        interactor.changeExpectedResult(NumbersResult.Success(listOf(NumberFact("45", "fact about 45"))))
        viewModel.fetchFact("45")

        assertEquals(1, communication.progressCalledList.size) // show progress bar count
        assertEquals(true, communication.progressCalledList[0]) // progress bar is shown

        assertEquals(1, interactor.fetchAboutNumberCalledList.size)
        assertEquals(NumberFact("45", "fact about 45"), interactor.fetchAboutNumberCalledList[0])

        assertEquals(2, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size) // ??????????
        assertEquals(UiState.Success(), communication.stateCalledList[0])

        assertEquals(1, communication.timesShowList)
        assertEquals(NumberUi("45", "fact about 45"), communication.numbersList[0])
    }
}

private class TestNumbersCommunication : NumbersCommunicaiton {

    val progressCalledList = mutableListOf<Boolean>()
    val stateCalledList = mutableListOf<Boolean>()
    var timesShowList = 0
    val numbersList = mutableListOf<NumberUI>()

    override fun showProgress(show: Boolean) {
        progressCalledList.add(show)
    }

    override fun showState(state: UiState) {
        stateCalledList.add(state)
    }

    override fun showList(list: List<NumberUi>) {
        timesShowList++
        numbersList.addAll(list)
    }
}

private class TestNumbersInteractor : NumbersInteractor {

    private var result: NumbersResult = NumbersResult.Success()

    val initCalledList = mutableListOf<NumbersResult>()
    val fetchAboutNumberCalledList = mutableListOf<NumbersResult>()
    val fetchAboutRandomNumberCalledList = mutableListOf<NumbersResult>()

    fun changeExpectedResult(newResult: NumbersResult) {
        result = newResult
    }

    override suspend fun init(): NumbersResult {
        initCalledList.add(result)
        return result
    }

    override suspend fun factAboutNumber(number: String): NumbersResult {
        fetchAboutNumberCalledList.add(result)
        return result
    }

    override suspend fun factAboutRandomNumber(): NumbersResult {
        fetchAboutRandomNumberCalledList.add(result)
        return result
    }

}