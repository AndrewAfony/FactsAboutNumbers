package andrewafony.factsaboutnumbers.com.main.sl

import andrewafony.factsaboutnumbers.com.details.data.NumberFactDetails
import andrewafony.factsaboutnumbers.com.main.presentation.NavigationCommunication
import andrewafony.factsaboutnumbers.com.numbers.data.cache.CacheModule
import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumbersDatabase
import andrewafony.factsaboutnumbers.com.numbers.data.cloud.CloudModule
import andrewafony.factsaboutnumbers.com.numbers.presentation.DispatchersList
import andrewafony.factsaboutnumbers.com.numbers.presentation.ManageResources
import android.content.Context

interface Core : CloudModule, ManageResources, CacheModule, ProvideNavigation, ProvideNumberDetails {

    fun provideDispatchers(): DispatchersList

    class Base(
        private val provideInstances: ProvideInstances,
        context: Context
    ) : Core {

        private val numberDetails = NumberFactDetails.Base()

        private val navigationCommunication = NavigationCommunication.Base()

        private val manageResources = ManageResources.Base(context)

        private val dispatchers by lazy {
            DispatchersList.Base()
        }

        private val cloudModule by lazy {
            provideInstances.provideCloudModule()
        }

        private val cacheModule by lazy {
            provideInstances.provideCacheModule()
        }

        override fun <T> service(clazz: Class<T>): T = cloudModule.service(clazz)

        override fun provideDatabase(): NumbersDatabase = cacheModule.provideDatabase()

        override fun provideDispatchers(): DispatchersList = dispatchers

        override fun string(id: Int): String = manageResources.string(id)

        override fun provideNavigation() = navigationCommunication

        override fun provide(): NumberFactDetails.Mutable = numberDetails
    }
}

interface ProvideNavigation {

    fun provideNavigation(): NavigationCommunication.Mutable
}

interface ProvideNumberDetails {

    fun provide(): NumberFactDetails.Mutable
}