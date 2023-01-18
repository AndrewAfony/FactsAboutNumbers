package andrewafony.factsaboutnumbers.com.numbers.presentation

import andrewafony.factsaboutnumbers.com.numbers.domain.NumberFact
import andrewafony.factsaboutnumbers.com.numbers.domain.NumberUiMapper
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersInteractor
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersResult
import andrewafony.factsaboutnumbers.com.numbers.presentation.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NumbersViewModelTest : BaseTest() {

    private lateinit var communication: TestNumbersCommunication
    private lateinit var interactor: TestNumbersInteractor
    private lateinit var viewModel: NumbersViewModel
    private lateinit var manageResources: TestManageResources

    @Before
    fun init() {
        communication = TestNumbersCommunication()
        interactor = TestNumbersInteractor()
        manageResources = TestManageResources()
        viewModel = NumbersViewModel(
            TestDispatchersList(),
            communication,
            interactor,
            NumbersResultMapper(communication, NumberUiMapper()),
            manageResources
        )
    }

//    @After
//    fun tearDown() {
//        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
//        mainThreadSurrogate.close()
//    }

    /**
     * Initial test
     */
    @Test
    fun test_init_and_reinit() = runTest {

        manageResources.string = "No internet connection"

        interactor.changeExpectedResult(NumbersResult.Success())

        viewModel.init(isFirstRun = true)

        assertEquals(true, communication.progressCalledList[0]) // progress bar is shown
        assertEquals(2, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size)
        assertEquals(true, communication.stateCalledList[0] is UiState.Success)

        assertEquals(0, communication.numbersList.size)
        assertEquals(0, communication.timesShowList)

        interactor.changeExpectedResult(NumbersResult.Failure("No internet connection"))
        viewModel.fetchRandomNumberFact()

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
        assertEquals(0, communication.timesShowList)

    }

    @Test
    fun test_fact_about_empty_number() = runTest {

        manageResources.string = "Entered number is empty"

        viewModel.fetchNumberFact("")

        assertEquals(0, interactor.fetchAboutNumberCalledList.size)

        assertEquals(0, communication.progressCalledList.size)

        assertEquals(1, communication.stateCalledList.size) // ??????????
        assertEquals(UiState.Error("Entered number is empty"), communication.stateCalledList[0])

        assertEquals(0, communication.timesShowList)
    }

    @Test
    fun test_fact_about_number() = runTest {
        interactor.changeExpectedResult(NumbersResult.Success(listOf(NumberFact("45",
            "fact about 45"))))
        viewModel.fetchNumberFact("45")

        assertEquals(true, communication.progressCalledList[0]) // progress bar is shown

        assertEquals(1, interactor.fetchAboutNumberCalledList.size)
        assertEquals(NumbersResult.Success(listOf(NumberFact("45", "fact about 45"))), interactor.fetchAboutNumberCalledList[0])

        assertEquals(2, communication.progressCalledList.size)
        assertEquals(false, communication.progressCalledList[1])

        assertEquals(1, communication.stateCalledList.size) // ??????????
        assertEquals(true, communication.stateCalledList[0] is UiState.Success)

        assertEquals(1, communication.timesShowList)
        assertEquals(NumberUi("45", "fact about 45"), communication.numbersList[0])
    }
}

private class TestManageResources : ManageResources {

    var string = ""

    override fun string(id: Int): String {
        return string
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

private class TestDispatchersList(
    private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
) : DispatchersList {

    override fun io(): CoroutineDispatcher = dispatcher
    override fun ui(): CoroutineDispatcher = dispatcher
}