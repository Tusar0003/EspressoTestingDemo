/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.espressotestingdemo

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.screenshot.Screenshot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


const val EMPTY = ""
const val USERNAME = "Espresso"
const val INVALID_USERNAME = "Username"
const val EMPTY_MESSAGE = "Username is empty!"
const val INVALID_MESSAGE = "Username is invalid!"

/**
 * The kotlin equivalent to the ChangeTextBehaviorTest, that
 * showcases simple view matchers and actions like [ViewMatchers.withId],
 * [ViewActions.click] and [ViewActions.typeText], and ActivityScenarioRule
 *
 *
 * Note that there is no need to tell Espresso that a view is in a different [Activity].
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()
//    @get:Rule var newActivityScenarioRule = activityScenarioRule<NewActivity>()

    private var monitor: Instrumentation.ActivityMonitor = getInstrumentation()
        .addMonitor(NewActivity::class.java.name, null, false)

    @Before
    fun setup() {}

    @Test
    fun userNameIsNotEmptyTest() {
        onView(
            withId(R.id.continue_button)
        ).perform(
            click()
        )

        onView(
            withId(R.id.error_text_view)
        ).check(
            matches(
                withText(EMPTY_MESSAGE)
            )
        )
    }

    @Test
    fun inValidUserNameTest() {
        onView(
            withId(R.id.user_name_edit_text)
        ).perform(
            typeText(INVALID_USERNAME),
            closeSoftKeyboard()
        )

        onView(
            withId(R.id.continue_button)
        ).perform(
            click()
        )

//        onView(
//            withId(R.id.error_text_view)
//        ).check(
//            matches(
//                withText(INVALID_MESSAGE)
//            )
//        )

        val errorTextView: ViewInteraction = onView(
            withId(R.id.error_text_view)
        )
        val errorMessage = getText(errorTextView)

        assertEquals(
            errorMessage,
            INVALID_MESSAGE
        )
    }

    @Test
    fun validUserNameTest() {
        onView(
            withId(R.id.user_name_edit_text)
        ).perform(
            typeText(USERNAME),
            closeSoftKeyboard()
        )

        onView(
            withId(R.id.continue_button)
        ).perform(
            click()
        )

//        Thread.sleep(1000)

        val newActivity: Activity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000)
        assertNotNull(newActivity)

//        onView(
//            withId(R.id.error_text_view)
//        ).check(
//            matches(
//                isDisplayed()
//            )
//        )
    }

    @Test
    fun changeText_newActivity() {
        // Type text and then press the button.
        onView(withId(R.id.user_name_edit_text)).perform(
            typeText(USERNAME),
            closeSoftKeyboard()
        )

        val simpleEditText: ViewInteraction = onView(withId(R.id.user_name_edit_text))
        val simpleString = getText(simpleEditText)

        onView(withId(R.id.continue_button)).perform(click())
//        Thread.sleep(2000)

//        val newActivity: Activity = getInstrumentation()
//            .waitForMonitorWithTimeout(monitor, 5000)
//        assertNotNull(newActivity);

        onView(withId(R.id.display_text_view)).perform(
            setTextInTextView(simpleString)
        )

        // This view is in a different Activity, no need to tell Espresso.
        onView(withId(R.id.display_text_view)).check(
            matches(withText(USERNAME))
        )
    }

    private fun getText(matcher: ViewInteraction): String {
        var text = String()
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }

    private fun setTextInTextView(value: String): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return CoreMatchers.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(TextView::class.java)
                )
            }

            override fun perform(uiController: UiController, view: View) {
                (view as TextView).text = value
            }

            override fun getDescription(): String {
                return "replace text"
            }
        }
    }
}