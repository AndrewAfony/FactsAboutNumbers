package andrewafony.factsaboutnumbers.com.numbers.data

import andrewafony.factsaboutnumbers.com.numbers.domain.NoConnectionException
import andrewafony.factsaboutnumbers.com.numbers.domain.NumberFact
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class BaseNumbersRepositoryTest {

    private lateinit var repository: NumbersRepository
    private lateinit var cloudDataSource: TestNumbersCloudDataSource
    private lateinit var cacheDataSource: TestNumbersCacheDataSource

    @Before
    fun setUp() {
        cloudDataSource = TestNumbersCloudDataSource()
        cacheDataSource = TestNumbersCacheDataSource()
        val mapper = NumberDataToDomainMapper()
        repository = BaseNumbersRepository(
            cloudDataSource,
            cacheDataSource,
            mapper,
            HandleDataRequest.Base(cacheDataSource, mapper, HandleDomainError())
        )
    }

    @Test
    fun test_all_numbers() = runTest {
        cacheDataSource.replaceData(
            listOf(
                NumberData("4", "fact about 4"),
                NumberData("5", "fact about 5")
            )
        )

        val actual = repository.allNumbersFacts()
        val expected = listOf(
            NumberFact("4", "fact about 4"),
            NumberFact("5", "fact about 5")
        )

        actual.forEachIndexed { index, item ->
            assertEquals(expected[index], item)
        }
        assertEquals(1, cacheDataSource.allNumbersFactCallCount)
    }

    @Test
    fun test_number_fact_not_cached_success() = runTest {
        cloudDataSource.makeExpected(NumberData("1", "fact about 1"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.numberFact("1")
        val expected = NumberFact("1", "fact about 1")

        assertEquals(expected, actual)
        assertEquals(1, cloudDataSource.numberFactCalledCount)
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cacheDataSource.numberFactCalled.size) // get from cache method is not called
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount) //save to cache method is called only once
        assertEquals(NumberData("1", "fact about 1"), cacheDataSource.data[0]) // NumberFact is actually in data

//        assertEquals("10", cacheDataSource.numberFactCalled[0]) // check if argument is passed from repository method to cacheDataSource
    }

    @Test(expected = NoConnectionException::class)
    fun test_number_fact_not_cached_failure() = runTest{
        cloudDataSource.changeInternetConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.numberFact("10")
        assertEquals(1, cloudDataSource.numberFactCalledCount)
        assertEquals(false, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(0, cacheDataSource.numberFactCalled.size) // get from cache method is not called
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount) //save to cache method is called only once

    }

    @Test
    fun test_number_fact_cached() = runTest {
        cloudDataSource.changeInternetConnection(true)
        cloudDataSource.makeExpected(NumberData("10", "cloud 10"))
        cacheDataSource.replaceData(listOf(NumberData("10", "fact about 10")))

        val actual = repository.numberFact("10")
        val expected = NumberFact("10", "fact about 10")

        assertEquals(expected, actual)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(true, cacheDataSource.containsCalledList[0])
        assertEquals(1, cacheDataSource.containsCalledList.size)
        assertEquals(1, cacheDataSource.numberFactCalled.size)
        assertEquals("10", cacheDataSource.numberFactCalled[0])
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
    }

    @Test
    fun test_random_number_fact_not_cached_success() = runTest {
        cloudDataSource.makeExpected(NumberData("1", "fact about 1"))
        cacheDataSource.replaceData(emptyList())

        val actual = repository.randomNumberFact()
        val expected = NumberFact("1", "fact about 1")

        assertEquals(expected, actual)
        assertEquals(0, cloudDataSource.numberFactCalledCount)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
        assertEquals(NumberData("1", "fact about 1"), cacheDataSource.data[0])
    }


    @Test(expected = NoConnectionException::class)
    fun test_random_number_fact_not_cached_failure() = runTest {
        cloudDataSource.changeInternetConnection(false)
        cacheDataSource.replaceData(emptyList())

        repository.randomNumberFact()
        assertEquals(0, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)
        assertEquals(0, cacheDataSource.numberFactCalled.size) // get from cache method is not called
        assertEquals(0, cacheDataSource.saveNumberFactCalledCount) //save to cache method is called only once
    }

    @Test
    fun test_random_number_fact_cached() = runTest{
        cloudDataSource.changeInternetConnection(true)
        cloudDataSource.makeExpected(NumberData("10", "cloud 10"))
        cacheDataSource.replaceData(listOf(NumberData("10", "fact about 10")))

        val actual = repository.randomNumberFact()
        val expected = NumberFact("10", "cloud 10")

        assertEquals(expected, actual)
        assertEquals(1, cloudDataSource.randomNumberFactCalledCount)

         assertEquals(0, cacheDataSource.numberFactCalled.size)
        assertEquals(1, cacheDataSource.saveNumberFactCalledCount)
    }

}

private class TestNumbersCloudDataSource : NumbersCloudDataSource {

    private var isInternetConnection = true
    private var numberData = NumberData("", "")

    var numberFactCalledCount = 0
    var randomNumberFactCalledCount = 0

    fun changeInternetConnection(connection: Boolean) {
        isInternetConnection = connection
    }

    fun makeExpected(numberData: NumberData) {
        this.numberData = numberData
    }

    override suspend fun number(number: String): NumberData {
        numberFactCalledCount++
        return if (isInternetConnection) {
             numberData
        } else {
            throw UnknownHostException()
        }
    }

    override suspend fun randomNumber(): NumberData {
        randomNumberFactCalledCount++
        return if (isInternetConnection) {
            numberData
        } else {
            throw UnknownHostException()
        }
    }
}

private class TestNumbersCacheDataSource : NumbersCacheDataSource {

    var numberFactCalled = mutableListOf<String>()
    var allNumbersFactCallCount = 0
    var saveNumberFactCalledCount = 0
    var containsCalledList = mutableListOf<Boolean>()

    val data = mutableListOf<NumberData>()

    fun replaceData(newData: List<NumberData>) {
        data.clear()
        data.addAll(newData)
    }

    override suspend fun allNumbersFact(): List<NumberData> {
        allNumbersFactCallCount++
        return data
    }

    override suspend fun contains(number: String): Boolean {
        var result = data.find { it.matches(number) } != null
        containsCalledList.add(result)
        return result
    }

    override suspend fun number(number: String): NumberData {
        numberFactCalled.add(number)
        return data[0]
    }

    override suspend fun saveNumberFact(numberData: NumberData) {
        saveNumberFactCalledCount++
        data.add(numberData)
    }

}