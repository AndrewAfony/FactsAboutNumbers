package andrewafony.factsaboutnumbers.com.main.presentation

import andrewafony.factsaboutnumbers.com.numbers.presentation.Communication
import andrewafony.factsaboutnumbers.com.numbers.presentation.NumberUi
import andrewafony.factsaboutnumbers.com.numbers.presentation.NumbersFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

class MainViewModel(
    private val navigationCommunication: NavigationCommunication.Mutable,
    private val detailsCommunication: DetailsCommunication
): ViewModel(), Init, Communication.Observe<NavigationStrategy>, ObserveDetails, ShowDetails {

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            navigationCommunication.map(NavigationStrategy.Replace(Screen.Main))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NavigationStrategy>) {
        navigationCommunication.observe(owner, observer)
    }

    override fun observeDetails(owner: LifecycleOwner, observer: Observer<String>) {
        detailsCommunication.observe(owner, observer)
    }

    override fun showDetails(item: NumberUi) {
        detailsCommunication.map(item.toString())
        navigationCommunication.map(NavigationStrategy.Add(Screen.Details))
    }
}

interface ObserveDetails {

    fun observeDetails(owner: LifecycleOwner, observer: Observer<String>)
}

interface ShowDetails {

    fun showDetails(item: NumberUi)
}