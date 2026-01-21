package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

import android.os.SystemClock;

import androidx.test.espresso.ViewInteraction;

public class WaitUtils {

    // Ожидание элемента по ID
    public static void waitForElementWithId(int id, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(withId(id)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        onView(withId(id)).check(matches(isDisplayed()));
    }

    // Ожидание элемента по тексту
    public static void waitForElementWithText(String text, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        onView(withText(text)).check(matches(isDisplayed()));
    }

    // Ожидание элемента (ViewInteraction)
    public static void waitForElement(ViewInteraction element, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                element.check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        element.check(matches(isDisplayed()));
    }

    // Проверка отображения элемента по ID (возвращает boolean)
    public static boolean isElementDisplayedWithId(int id, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(withId(id)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        return false;
    }

    // Проверка отображения элемента по тексту (возвращает boolean)
    public static boolean isElementDisplayedWithText(String text, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        return false;
    }

    // Ожидание исчезновения элемента по ID
    public static void waitForElementToDisappear(int id, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(withId(id)).check(matches(isDisplayed()));
                SystemClock.sleep(50);
            } catch (Exception e) {
                return;
            }
        }
    }

    // Ожидание исчезновения элемента по тексту
    public static void waitForTextToDisappear(String text, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                SystemClock.sleep(50);
            } catch (Exception e) {
                return;
            }
        }
    }

    // Ожидание без условий (просто задержка)
    public static void waitForMillis(long millis) {
        SystemClock.sleep(millis);
    }

    // Альтернативное имя для обратной совместимости
    public static void waitFor(long millis) {
        SystemClock.sleep(millis);
    }

    // Ожидание появления любого из указанных элементов
    public static void waitForAnyElement(int[] ids, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            for (int id : ids) {
                try {
                    onView(withId(id)).check(matches(isDisplayed()));
                    return;
                } catch (Exception e) {
                    // Продолжаем проверку других элементов
                }
            }
            SystemClock.sleep(50);
        }
        throw new RuntimeException("None of the specified elements found within timeout");
    }

    // Ожидание элемента с определенным ID и текстом
    public static void waitForElementWithIdAndText(int id, String text, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(allOf(withId(id), withText(text))).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        throw new RuntimeException("Element with id " + id + " and text '" + text + "' not found");
    }
}