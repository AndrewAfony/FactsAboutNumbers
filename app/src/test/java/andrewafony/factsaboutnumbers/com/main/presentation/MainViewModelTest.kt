package andrewafony.factsaboutnumbers.com.main.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.junit.Assert.*
import org.junit.Test

class MainViewModelTest {

    @Test
    fun test_starting_navigation() {
        val navigationCommunication = TestNavigationCommunication()
        val detailsCommunication = TestDetailsCommunication()
        val viewModel = MainViewModel(navigationCommunication, detailsCommunication)

        viewModel.init(true)

        assertEquals(1, navigationCommunication.count)
        assertTrue(navigationCommunication.strategy is NavigationStrategy.Replace)

        viewModel.init(false)
        assertEquals(1, navigationCommunication.count)
    }
}

private class TestNavigationCommunication : NavigationCommunication.Mutable {

    lateinit var strategy: NavigationStrategy
    var count = 0

    override fun map(source: NavigationStrategy) {
        count++
        strategy = source
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NavigationStrategy>) = Unit
}

private class TestDetailsCommunication : DetailsCommunication {

    var details = ""

    override fun observe(owner: LifecycleOwner, observer: Observer<String>) = Unit

    override fun map(source: String) {
        details = source
    }
}