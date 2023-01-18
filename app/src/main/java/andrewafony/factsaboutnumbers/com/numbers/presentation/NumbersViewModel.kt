package andrewafony.factsaboutnumbers.com.numbers.presentation

import andrewafony.factsaboutnumbers.com.R
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersInteractor
import andrewafony.factsaboutnumbers.com.numbers.domain.NumbersResult
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class NumbersViewModel(
    private val dispatchers: DispatchersList,
    private val communications: NumbersCommunications,
    private val interactor: NumbersInteractor,
    private val numbersResultMapper: NumbersResultMapper,
    private val manageResources: ManageResources,
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
        if (isFirstRun) {
            communications.showProgress(true)
            viewModelScope.launch(dispatchers.io()) {
                val result = interactor.init()
                communications.showProgress(false)
                result.map(numbersResultMapper)
            }
        }
    }

    override fun fetchRandomNumberFact() {
        communications.showProgress(true)
        viewModelScope.launch(dispatchers.io()) {
            val result = interactor.factAboutRandomNumber()
            communications.showProgress(false)
            result.map(numbersResultMapper)
        }
    }

    override fun fetchNumberFact(number: String) {
        if (number.isEmpty())
            communications.showState(UiState.Error(manageResources.string(R.string.empty_number_error_message)))
        else {
            communications.showProgress(true)
            viewModelScope.launch(dispatchers.io()) {
                val result = interactor.factAboutNumber(number)
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