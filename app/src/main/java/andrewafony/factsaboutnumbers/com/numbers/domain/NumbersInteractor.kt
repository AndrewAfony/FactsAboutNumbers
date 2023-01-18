package andrewafony.factsaboutnumbers.com.numbers.domain

import android.widget.FrameLayout

interface NumbersInteractor {

    suspend fun init(): NumbersResult

    suspend fun factAboutNumber(number: String): NumbersResult

    suspend fun factAboutRandomNumber(): NumbersResult
}