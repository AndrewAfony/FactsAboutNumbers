package andrewafony.factsaboutnumbers.com.main.sl

import andrewafony.factsaboutnumbers.com.details.presentation.DetailsViewModel
import andrewafony.factsaboutnumbers.com.details.sl.NumberDetailsModule
import andrewafony.factsaboutnumbers.com.main.presentation.MainViewModel
import andrewafony.factsaboutnumbers.com.numbers.presentation.NumbersViewModel
import andrewafony.factsaboutnumbers.com.numbers.sl.NumbersModule
import androidx.lifecycle.ViewModel

interface DependencyContainer {

    fun <T : ViewModel> module(clazz: Class<T>): Module<*>

    class Error : DependencyContainer {
        override fun <T : ViewModel> module(clazz: Class<T>): Module<*> {
            throw IllegalStateException("No module found for $clazz")
        }
    }

    class Base(
        private val core: Core,
        private val dependencyContainer: DependencyContainer = Error()
    ) : DependencyContainer {

        override fun <T : ViewModel> module(clazz: Class<T>): Module<*> =
            when (clazz) {
                MainViewModel::class.java -> MainModule(core)
                NumbersViewModel.Base::class.java -> NumbersModule(core)
                DetailsViewModel::class.java -> NumberDetailsModule(core)
                else -> dependencyContainer.module(clazz)
            }
    }
}