package andrewafony.factsaboutnumbers.com.main.sl

import andrewafony.factsaboutnumbers.com.numbers.data.cache.CacheModule
import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumbersDatabase
import andrewafony.factsaboutnumbers.com.numbers.data.cloud.CloudModule
import andrewafony.factsaboutnumbers.com.numbers.presentation.DispatchersList
import andrewafony.factsaboutnumbers.com.numbers.presentation.ManageResources
import android.content.Context

interface Core : CloudModule, ManageResources, CacheModule {

    fun provideDispatchers(): DispatchersList

    class Base(private val isRelease: Boolean, context: Context) : Core {

        private val manageResources = ManageResources.Base(context)

        private val dispatchers by lazy {
            DispatchersList.Base()
        }

        private val cloudModule by lazy {
            if (isRelease)
                CloudModule.Debug()
            else
                CloudModule.Release()
        }

        private val cacheModule by lazy {
            if (isRelease)
                CacheModule.Base(context)
            else
                CacheModule.Mock(context)
        }

        override fun <T> service(clazz: Class<T>): T = cloudModule.service(clazz)

        override fun provideDatabase(): NumbersDatabase = cacheModule.provideDatabase()

        override fun provideDispatchers(): DispatchersList = dispatchers

        override fun string(id: Int): String = manageResources.string(id)
    }
}