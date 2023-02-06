package andrewafony.factsaboutnumbers.com.main.sl

import andrewafony.factsaboutnumbers.com.details.presentation.DetailsViewModel
import andrewafony.factsaboutnumbers.com.details.sl.NumberDetailsModule
import andrewafony.factsaboutnumbers.com.main.presentation.MainViewModel
import andrewafony.factsaboutnumbers.com.numbers.data.BaseNumbersRepository
import andrewafony.factsaboutnumbers.com.numbers.data.HandleDataRequest
import andrewafony.factsaboutnumbers.com.numbers.data.HandleDomainError
import andrewafony.factsaboutnumbers.com.numbers.data.NumberDataToDomainMapper
import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumberDataToCache
import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumbersCacheDataSource
import andrewafony.factsaboutnumbers.com.numbers.data.cloud.NumbersCloudDataSource
import andrewafony.factsaboutnumbers.com.numbers.data.cloud.NumbersService
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersRepository
import andrewafony.factsaboutnumbers.com.numbers.presentation.NumbersViewModel
import andrewafony.factsaboutnumbers.com.numbers.sl.NumbersModule
import andrewafony.factsaboutnumbers.com.numbers.sl.ProvideNumbersRepository
import andrewafony.factsaboutnumbers.com.random.WorkManagerWrapper
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
    ) : DependencyContainer, ProvideNumbersRepository {

        private val repository: NumbersRepository by lazy {
            ProvideNumbersRepository.Base(core).provide()
        }

        override fun <T : ViewModel> module(clazz: Class<T>): Module<*> =
            when (clazz) {
                MainViewModel::class.java -> MainModule(core, core.provideWorkManager())
                NumbersViewModel.Base::class.java -> NumbersModule(core, this)
                DetailsViewModel::class.java -> NumberDetailsModule(core)
                else -> dependencyContainer.module(clazz)
            }

        override fun provide(): NumbersRepository {
            return repository
        }
    }
}