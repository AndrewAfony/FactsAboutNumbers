package andrewafony.factsaboutnumbers.com.main

import andrewafony.factsaboutnumbers.com.BuildConfig
import andrewafony.factsaboutnumbers.com.main.sl.*
import andrewafony.factsaboutnumbers.com.numbers.domain.RandomNumberRepository
import andrewafony.factsaboutnumbers.com.random.ProvidePeriodicRepository
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import java.io.File

class NumbersApp : Application(), ProvideViewModel, ProvidePeriodicRepository {

    private lateinit var viewModelsFactory: ViewModelsFactory
    private lateinit var dependencyContainer: DependencyContainer.Base

    override fun onCreate() {
        super.onCreate()

        dependencyContainer = DependencyContainer.Base(
            Core.Base(
                if (BuildConfig.DEBUG) ProvideInstances.Mock(this) else ProvideInstances.Release(this),
                this
            )
        )

        viewModelsFactory = ViewModelsFactory(dependencyContainer)
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T {
        return ViewModelProvider(owner, viewModelsFactory)[clazz]
    }

    override fun providePeriodicRepository() = dependencyContainer.provide()

}