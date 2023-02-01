package andrewafony.factsaboutnumbers.com.main.presentation

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

interface NavigationStrategy {

    fun navigate(manager: FragmentManager, container: Int)

    class Replace(private val screen: Screen) : NavigationStrategy {

        override fun navigate(manager: FragmentManager, container: Int) = manager.commit {
            replace(container, screen.fragment, null)
        }
    }

    class Add (private val screen: Screen) : NavigationStrategy {

        override fun navigate(manager: FragmentManager, container: Int) = manager.commit {
            add(container, screen.fragment, null, screen.fragment.simpleName)
            addToBackStack(screen.fragment.simpleName)
        }
    }
}