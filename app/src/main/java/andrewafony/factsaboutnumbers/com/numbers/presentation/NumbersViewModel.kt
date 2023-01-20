package andrewafony.factsaboutnumbers.com.numbers.presentation

import andrewafony.factsaboutnumbers.com.R
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersInteractor
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersResult
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NumbersViewModel(
    private val communications: NumbersCommunications,
    private val interactor: NumbersInteractor,
    private val manageResources: ManageResources,
    private val requestHandler: HandleNumbersRequest,
) : ViewModel(), ObserveNumbers, FetchNumbers {

    override fun observeProgress(owner: LifecycleOwner, observer: Observer<Boolean>) {
        communications.observeProgress(owner, observer)
    }

    override fun observeState(owner: LifecycleOwner, observer: Observer<UiState>) {
        communications.observeState(owner, observer)
    }

    override fun observeList(owner: LifecycleOwner, observer: Observer<List<NumberUi>>) {
        communications.observeList(owner, observer)
    }

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            requestHandler.handle(viewModelScope) { interactor.init() }
    }

    override fun fetchRandomNumberFact() {
        requestHandler.handle(viewModelScope) { interactor.factAboutRandomNumber() }
    }

    override fun fetchNumberFact(number: String) {
        if (number.isEmpty())
            communications.showState(UiState.Error(manageResources.string(R.string.empty_number_error_message)))
        else {
            requestHandler.handle(viewModelScope) { interactor.factAboutNumber(number) }
        }
    }
}

interface HandleNumbersRequest {

    fun handle(
        coroutineScope: CoroutineScope,
        block: suspend () -> NumbersResult,
    )

    class Base(
        private val communications: NumbersCommunications,
        private val dispatchers: DispatchersList,
        private val numbersResultMapper: NumbersResult.Mapper<Unit>,
    ) : HandleNumbersRequest {
        override fun handle(coroutineScope: CoroutineScope, block: suspend () -> NumbersResult) {
            communications.showProgress(true)
            coroutineScope.launch(dispatchers.io()) {
                val result = block.invoke()
                communications.showProgress(false)
                result.map(numbersResultMapper)
            }
        }
    }
}

interface FetchNumbers {

    fun init(isFirstRun: Boolean)

    fun fetchRandomNumberFact()

    fun fetchNumberFact(number: String)
}