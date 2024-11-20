package andrewafony.factsaboutnumbers.com.numbers.data.cache

import andrewafony.factsaboutnumbers.com.numbers.data.NumberData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NumbersCacheDataSourceTest {

    @Test
    fun test_all_numbers_empty() = runTest {

        val dataSource = NumbersCacheDataSource.Base(dao = TestDao(), TestMapper(5))

        val actual = dataSource.allNumbers()
        val expected = emptyList<NumberCache>()
        assertEquals(expected, actual)
    }

    @Test
    fun test_all_numbers_not_empty() = runTest {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao = dao, TestMapper(5))

        dao.data.add(NumberCache("1", "fact1", 1))
        dao.data.add(NumberCache("2", "fact2", 2))

        val actualList = dataSource.allNumbers()
        val expectedList = listOf(NumberData("1", "fact1"), NumberData("2", "fact2"))

        actualList.forEachIndexed { index, actual ->
            assertEquals(expectedList[index], actual)
        }
    }

    @Test
    fun test_contains_true() = runTest {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao = dao, TestMapper(5))

        dao.data.add(NumberCache("6", "fact6", 6))

        val actual = dataSource.contains("6")
        assertTrue(actual)
    }

    @Test
    fun test_contains_false() = runTest {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao = dao, TestMapper(5))

        dao.data.add(NumberCache("7", "fact7", 7))

        val actual = dataSource.contains("6")
        assertFalse(actual)
    }

    @Test
    fun test_save() = runTest {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao = dao, TestMapper(8))

        dataSource.saveNumber(NumberData("8", "fact8"))
        assertEquals(NumberCache("8", "fact8", 8), dao.data[0])
    }

    @Test
    fun test_number_contains() = runTest {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao = dao, TestMapper(9))

        dao.data.add(NumberCache("10", "fact10", 10))

        val actual = dataSource.number("10")
        val expected = NumberData("10", "fact10")
        assertEquals(expected, actual)
    }

    @Test
    fun test_number_doesnt_exist() = runTest {
        val dao = TestDao()
        val dataSource = NumbersCacheDataSource.Base(dao = dao, TestMapper(9))

        dao.data.add(NumberCache("10", "fact10", 10))

        val actual = dataSource.number("32")
        val expected = NumberData("", "")
        assertEquals(expected, actual)
    }
}

private class TestDao : NumbersDao {

    val data = mutableListOf<NumberCache>()

    override suspend fun allNumbers(): List<NumberCache> {
        return data
    }

    override suspend fun insert(number: NumberCache) {
        data.add(number)
    }

    override suspend fun number(number: String): NumberCache =
        data.find { it.number == number } ?: NumberCache("", "", 0)

    override suspend fun numberExist(number: String): Boolean =
        data.find { it.number == number } != null
}

private class TestMapper(private val date: Long) : NumberData.Mapper<NumberCache> {
    override fun map(id: String, fact: String): NumberCache {
        return NumberCache(id, fact, date)
    }
}