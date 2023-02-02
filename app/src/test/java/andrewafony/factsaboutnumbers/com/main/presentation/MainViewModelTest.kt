package andrewafony.factsaboutnumbers.com.main.presentation

import andrewafony.factsaboutnumbers.com.numbers.presentation.NumberUi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var navigationCommunication: TestNavigationCommunication
    private lateinit var detailsCommunication: TestDetailsCommunication
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        navigationCommunication = TestNavigationCommunication()
        detailsCommunication = TestDetailsCommunication()
        viewModel = MainViewModel(navigationCommunication, detailsCommunication)
    }

    @Test
    fun test_starting_navigation() {

        viewModel.init(true)

        assertEquals(1, navigationCommunication.count)
        assertTrue(navigationCommunication.strategy is NavigationStrategy.Replace)
        assertEquals(0, detailsCommunication.count)
        assertTrue(detailsCommunication.details.isBlank())

        viewModel.init(false)
        assertEquals(1, navigationCommunication.count)
    }

    @Test
    fun test_navigation_to_details() {

        viewModel.init(true)

        viewModel.showDetails(NumberUi("9", "fact about 9"))

        assertEquals(2, navigationCommunication.count)
        assertTrue(navigationCommunication.strategy is NavigationStrategy.Add)

        assertEquals(1, detailsCommunication.count)
        assertEquals("9 \n\n fact about 9", detailsCommunication.details)
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
    var count = 0

    override fun observe(owner: LifecycleOwner, observer: Observer<String>) = Unit

    override fun map(source: String) {
        count++
        details = source
    }
}