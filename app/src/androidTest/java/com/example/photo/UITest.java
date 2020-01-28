package com.example.photo;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Rule;

import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class UITest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void TestFilter() {
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.etToDateTime)).perform(typeText("31/01/18"), closeSoftKeyboard());
        onView(withId(R.id.etFromDateTime)).perform(typeText("01/01/18"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        for (int i = 0; i <= 5; i++) {
            onView(withId(R.id.btnRight)).perform(click());
        }
    }
    @Test
    public void TestCaption() {
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("NoCaption"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.Caption)).check(matches(withText("NoCaption")));
        onView(withId(R.id.btnRight)).perform(click());
        onView(withId(R.id.btnLeft)).perform(click());
    }
}

