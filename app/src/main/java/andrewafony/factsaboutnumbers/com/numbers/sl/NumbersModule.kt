package andrewafony.factsaboutnumbers.com.numbers.sl

import andrewafony.factsaboutnumbers.com.main.sl.Core
import andrewafony.factsaboutnumbers.com.main.sl.Module
import andrewafony.factsaboutnumbers.com.numbers.data.BaseNumbersRepository
import andrewafony.factsaboutnumbers.com.numbers.data.HandleDataRequest
import andrewafony.factsaboutnumbers.com.numbers.data.HandleDomainError
import andrewafony.factsaboutnumbers.com.numbers.data.NumberDataToDomainMapper
import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumberDataToCache
import andrewafony.factsaboutnumbers.com.numbers.data.cache.NumbersCacheDataSource
import andrewafony.factsaboutnumbers.com.numbers.data.cloud.NumbersCloudDataSource
import andrewafony.factsaboutnumbers.com.numbers.data.cloud.NumbersService
import andrewafony.factsaboutnumbers.com.numbers.domain.*
import andrewafony.factsaboutnumbers.com.numbers.presentation.*

class NumbersModule(
    private val core: Core,
    private val provideRepository: ProvideNumbersRepository,
) : Module<NumbersViewModel.Base> {

    override fun viewModel(): NumbersViewModel.Base {

        val repository = provideRepository.provide()

        val communication = NumbersCommunications.Base(
            ProgressCommunication.Base(),
            NumbersStateCommunication.Base(),
            NumbersListCommunication.Base()
        )

        return NumbersViewModel.Base(
            requestHandler = HandleNumbersRequest.Base(
                dispatchers = core.provideDispatchers(),
                communications = communication,
                numbersResultMapper = NumbersResultMapper(communication, NumberUiMapper())
            ),
            manageResources = core,
            communications = communication,
            interactor = NumbersInteractor.Base(
                repository = repository,
                handleRequest = HandleRequest.Base(
                    repository = repository,
                    handleError = HandleError.Base(core)
                )
            )
        )
    }
}

interface ProvideNumbersRepository {

    fun provide(): NumbersRepository

    class Base(private val core: Core) : ProvideNumbersRepository {

        override fun provide(): NumbersRepository {

            val cacheDataSource = NumbersCacheDataSource.Base(
                dao = core.provideDatabase().dao(),
                dataToCache = NumberDataToCache()
            )

            return BaseNumbersRepository(
                cloudDataSource = NumbersCloudDataSource.Base(core.service(NumbersService::class.java)),
                cacheDataSource = cacheDataSource,
                mapperToDomain = NumberDataToDomainMapper(),
                handleDataRequest = HandleDataRequest.Base(
                    cacheDataSource = cacheDataSource,
                    mapperToDomain = NumberDataToDomainMapper(),
                    handleError = HandleDomainError()
                )
            )
        }
    }
}