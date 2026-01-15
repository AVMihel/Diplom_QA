package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.os.SystemClock;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;

public class WaitUtils {

    // Ожидает отображения элемента в течение указанного времени
    public static void waitForElement(ViewInteraction view, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                view.check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(100);
            }
        }
        throw new RuntimeException("Element not found within " + timeout + "ms");
    }

    // Ожидает отображения элемента с указанным текстом
    public static void waitForElementWithText(String text, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(ViewMatchers.withText(text)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(100);
            }
        }
        throw new RuntimeException("Element with text '" + text + "' not found within " + timeout + "ms");
    }

    // Ожидает отображения элемента с указанным ID
    public static void waitForElementWithId(int id, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(ViewMatchers.withId(id)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(100);
            }
        }
        throw new RuntimeException("Element with id " + id + " not found within " + timeout + "ms");
    }

    // Проверяет отображение элемента без выброса исключения
    public static boolean isElementDisplayed(ViewInteraction view, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                view.check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                SystemClock.sleep(100);
            }
        }
        return false;
    }
}