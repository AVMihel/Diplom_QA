package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

public class WaitUtils {

    private static final int POLLING_DELAY = 50;

    // Базовый метод ожидания времени
    public static void waitForMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Ожидание отображения элемента по ID
    public static void waitForElementWithId(int elementId, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(elementId)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                waitForMillis(POLLING_DELAY);
            }
        }
        throw new AssertionError("Element with ID " + elementId + " not found within " + timeout + "ms");
    }

    // Ожидание отображения элемента по тексту
    public static void waitForElementWithText(String text, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                waitForMillis(POLLING_DELAY);
            }
        }
        throw new AssertionError("Element with text '" + text + "' not found within " + timeout + "ms");
    }

    // Ожидание отображения ViewInteraction элемента
    public static void waitForElement(ViewInteraction element, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                element.check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                waitForMillis(POLLING_DELAY);
            }
        }
        throw new AssertionError("Element not displayed within " + timeout + "ms");
    }

    // Ожидание появления любого элемента из массива ID
    public static void waitForAnyElement(int[] elementIds, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            for (int elementId : elementIds) {
                try {
                    onView(withId(elementId)).check(matches(isDisplayed()));
                    return;
                } catch (Exception e) {
                    // Игнорируем
                }
            }
            waitForMillis(POLLING_DELAY);
        }
        throw new AssertionError("None of the elements found within " + timeout + "ms");
    }

    // Проверка отображения элемента по тексту с таймаутом (без исключения)
    public static boolean isElementDisplayedWithText(String text, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                waitForMillis(POLLING_DELAY);
            }
        }
        return false;
    }

    // Проверка отображения элемента по ID с таймаутом (без исключения)
    public static boolean isElementDisplayedWithId(int elementId, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(elementId)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                waitForMillis(POLLING_DELAY);
            }
        }
        return false;
    }
}