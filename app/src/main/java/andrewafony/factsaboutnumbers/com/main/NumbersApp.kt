package andrewafony.factsaboutnumbers.com.main

import andrewafony.factsaboutnumbers.com.BuildConfig
import andrewafony.factsaboutnumbers.com.numbers.data.CloudModule
import android.app.Application

class NumbersApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val cloudModule = if (BuildConfig.DEBUG)
            CloudModule.Debug()
        else
            CloudModule.Release()
    }
}