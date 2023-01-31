package andrewafony.factsaboutnumbers.com.main.sl

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
            if (clazz == NumbersViewModel::class.java) {
                NumbersModule(core)
            } else
                dependencyContainer.module(clazz)

    }
}