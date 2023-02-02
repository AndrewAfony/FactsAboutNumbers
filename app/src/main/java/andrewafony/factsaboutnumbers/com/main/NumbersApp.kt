package andrewafony.factsaboutnumbers.com.main

import andrewafony.factsaboutnumbers.com.BuildConfig
import andrewafony.factsaboutnumbers.com.main.sl.*
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class NumbersApp : Application(), ProvideViewModel {

    private lateinit var viewModelsFactory: ViewModelsFactory

    override fun onCreate() {
        super.onCreate()

        viewModelsFactory = ViewModelsFactory(
            DependencyContainer.Base(
                Core.Base(
                    if (BuildConfig.DEBUG) ProvideInstances.Mock(this) else ProvideInstances.Release(this),
                    this
                )
            )
        )
    }

    override fun <T : ViewModel> provideViewModel(clazz: Class<T>, owner: ViewModelStoreOwner): T {
        return ViewModelProvider(owner, viewModelsFactory)[clazz]
    }
}