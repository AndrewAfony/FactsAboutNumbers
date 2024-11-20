package andrewafony.factsaboutnumbers.com.main.presentation

import andrewafony.factsaboutnumbers.com.details.presentation.DetailsFragment
import andrewafony.factsaboutnumbers.com.numbers.presentation.NumbersFragment
import androidx.fragment.app.Fragment

sealed class Screen {

    abstract val fragment: Class<out Fragment>

    object Main : Screen() {
        override val fragment: Class<out Fragment>
            get() = NumbersFragment::class.java
    }

    object Details : Screen() {

        override val fragment: Class<out Fragment>
            get() = DetailsFragment::class.java

    }
}
