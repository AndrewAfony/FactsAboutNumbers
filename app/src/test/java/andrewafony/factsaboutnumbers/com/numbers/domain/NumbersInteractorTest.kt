package andrewafony.factsaboutnumbers.com.numbers.domain

import andrewafony.factsaboutnumbers.com.numbers.presentation.ManageResources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NumbersInteractorTest {

    private lateinit var repository: TestNumbersRepository
    private lateinit var interactor: NumbersInteractor
    private lateinit var manageResources: TestManageResources

    @Before
    fun setup() {
        repository = TestNumbersRepository()
        manageResources = TestManageResources()

        interactor = NumbersInteractor.Base(
            repository,
            HandleRequest.Base(repository, HandleError.Base(manageResources))
        )
    }

    /**
     * Each method of interactor returns NumbersResult which can be Success of Failure
     */
    @Test
    fun test_init_success() = runTest {

        repository.changeExpectedList(listOf(NumberFact("6", "fact about 6")))

        val actual = interactor.init()
        val exptected = NumbersResult.Success(listOf(NumberFact("6", "fact about 6")))

        assertEquals(exptected, actual)
        assertEquals(1, repository.allNumbersFactsCallCount)
    }

    @Test
    fun test_fact_about_number_success() = runTest {
        repository.changeExpectedNumberFact(NumberFact("7", "fact about 7"))

        val actual = interactor.factAboutNumber("7")
        val expected = NumbersResult.Success(listOf(NumberFact("7", "fact about 7")))

        assertEquals(expected, actual)
        assertEquals("7", repository.numberFactCallList[0])
        assertEquals(1, repository.numberFactCallList.size)
    }

    @Test
    fun test_fact_about_number_error() = runTest {
        repository.numberFactError(true)
        manageResources.changeExpected("No internet connection")

        val actual = interactor.factAboutNumber("7")
        val expected = NumbersResult.Failure("No internet connection")

        assertEquals(expected, actual)
        assertEquals("7", repository.numberFactCallList[0])
        assertEquals(1, repository.numberFactCallList.size)
    }

    @Test
    fun test_fact_random_about_number_success() = runTest {
        repository.changeExpectedRandomNumberFact(NumberFact("7", "fact about 7"))

        val actual = interactor.factAboutRandomNumber()
        val expected = NumbersResult.Success(listOf(NumberFact("7", "fact about 7")))

        assertEquals(expected, actual)
        assertEquals(1, repository.numberRandomFactCallList.size)
    }

    @Test
    fun test_fact_random_about_number_error() = runTest {
        repository.randomNumberFactError(true)
        manageResources.changeExpected("No internet connection")

        val actual = interactor.factAboutRandomNumber()
        val expected = NumbersResult.Failure("No internet connection")

        assertEquals(expected, actual)
        assertEquals(1, repository.numberRandomFactCallList.size)
    }
}

private class TestNumbersRepository : NumbersRepository {

    private val allNumbersFacts = mutableListOf<NumberFact>()
    private var numberFact = NumberFact("", "")
    private var numberFactError = false

    var allNumbersFactsCallCount = 0
    val numberFactCallList = mutableListOf<String>()
    val numberRandomFactCallList = mutableListOf<String>()

    fun changeExpectedList(list: List<NumberFact>) {
        allNumbersFacts.clear()
        allNumbersFacts.addAll(list)
    }

    fun changeExpectedNumberFact(numberFact: NumberFact) {
        this.numberFact = numberFact
    }

    fun changeExpectedRandomNumberFact(numberFact: NumberFact) {
        this.numberFact = numberFact
    }

    fun numberFactError(error: Boolean) {
        numberFactError = error
    }

    fun randomNumberFactError(error: Boolean) {
        numberFactError = error
    }

    override suspend fun allNumbersFacts(): List<NumberFact> {
        allNumbersFactsCallCount++
        return allNumbersFacts
    }

    override suspend fun numberFact(number: String): NumberFact {
        numberFactCallList.add(number)
        allNumbersFacts.add(numberFact)
        if (numberFactError)
            throw NoConnectionException()
        return numberFact
    }

    override suspend fun randomNumberFact(): NumberFact {
        numberRandomFactCallList.add("random number")
        allNumbersFacts.add(numberFact)
        if (numberFactError)
            throw NoConnectionException()
        return numberFact
    }

}


private class TestManageResources : ManageResources {

    private var value: String = ""

    fun changeExpected(newValue: String) {
        value = newValue
    }

    override fun string(id: Int): String = value
}