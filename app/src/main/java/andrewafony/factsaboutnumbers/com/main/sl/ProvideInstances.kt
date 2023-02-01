package andrewafony.factsaboutnumbers.com.main.sl

import andrewafony.factsaboutnumbers.com.numbers.data.cache.CacheModule
import andrewafony.factsaboutnumbers.com.numbers.data.cloud.CloudModule
import android.content.Context

interface ProvideInstances {

    fun provideCloudModule(): CloudModule

    fun provideCacheModule(): CacheModule

    class Release(private val context: Context): ProvideInstances {
        override fun provideCloudModule(): CloudModule = CloudModule.Base()

        override fun provideCacheModule(): CacheModule = CacheModule.Base(context)
    }

    class Mock(private val context: Context): ProvideInstances {
        override fun provideCloudModule(): CloudModule = CloudModule.Mock()

        override fun provideCacheModule(): CacheModule = CacheModule.Mock(context)
    }
}