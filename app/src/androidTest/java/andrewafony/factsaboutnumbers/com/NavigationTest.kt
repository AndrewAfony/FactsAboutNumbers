package andrewafony.factsaboutnumbers.com

import andrewafony.factsaboutnumbers.com.main.presentation.MainActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_details_navigation() {
        onView(withId(R.id.editText)).perform(typeText("10"), closeSoftKeyboard())
        onView(withId(R.id.buttonGetFact)).perform(click())

        onView(withId(R.id.titleTextView)).check(matches(withText("10")))

        // todo navigation

        pressBack()

        // todo check
    }
}