package com.example.photo;
import org.junit.Test;
import static org.junit.Assert.*;
import com.example.photo.MainActivity;
import com.example.photo.SearchActivity;

import org.junit.Rule;

import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class UITests {
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

    public void TestCaption() {
        onView(withId(R.id.btnFilter)).perform(click());
        onView(withId(R.id.etFromDateTime)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.etToDateTime)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.etKeywords)).perform(typeText("caption"), closeSoftKeyboard());
        onView(withId(R.id.go)).perform(click());
        onView(withId(R.id.Caption)).check(matches(withText("caption")));
        onView(withId(R.id.btnRight)).perform(click());
        onView(withId(R.id.btnLeft)).perform(click());
    }
}

