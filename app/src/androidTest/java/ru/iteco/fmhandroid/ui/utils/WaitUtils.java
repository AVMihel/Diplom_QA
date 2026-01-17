package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.os.SystemClock;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;

public class WaitUtils {

    // Ожидает отображения элемента с указанным ID
    public static void waitForElementWithId(int id, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(ViewMatchers.withId(id)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        // Крайняя попытка
        try {
            onView(ViewMatchers.withId(id)).check(matches(isDisplayed()));
        } catch (Exception e) {
            throw new RuntimeException("Element with id " + id + " not found within " + timeout + "ms", e);
        }
    }

    // Ожидает отображения элемента
    public static void waitForElement(ViewInteraction view, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                view.check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        view.check(matches(isDisplayed()));
    }

    // Ожидает отображения элемента с указанным текстом
    public static void waitForElementWithText(String text, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(ViewMatchers.withText(text)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        onView(ViewMatchers.withText(text)).check(matches(isDisplayed()));
    }
}