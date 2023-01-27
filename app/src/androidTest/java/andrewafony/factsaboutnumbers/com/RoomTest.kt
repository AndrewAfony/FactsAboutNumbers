package andrewafony.factsaboutnumbers.com

import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumberCache
import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumbersDao
import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumbersDatabase
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoomTest {

    private lateinit var db: NumbersDatabase
    private lateinit var dao: NumbersDao

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, NumbersDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.dao()
    }

    @After
    @Throws(IOException::class)
    fun clear() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun test_add_and_check() = runBlocking {
        val number = NumberCache("42", "fact about 42", 12)
        dao.insert(number)
        val actualList = dao.allNumbers()
        assertEquals(number, actualList[0])
        val actual = dao.number("42")
        assertEquals(number, actual)
    }

    @Test
    @Throws(Exception::class)
    fun test_add_two_times() = runBlocking {
        val number = NumberCache("42", "fact about 42", 12)
        dao.insert(number)
        val actualList = dao.allNumbers()
        assertEquals(number, actualList[0])
        val new = NumberCache("42", "fact about 42", 13)
        dao.insert(new)
        val newActualList = dao.allNumbers()
        assertEquals(1, newActualList.size)
        assertEquals(new, newActualList[0])
    }

}