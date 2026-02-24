package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

public class WaitUtils {

    private static final int POLLING_DELAY = 100;

    private WaitUtils() {
    }

    // Внутренний метод для приостановки выполнения потока
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Ожидание в течение указанного количества миллисекунд
    public static void waitMillis(long millis) {
        sleep(millis);
    }

    // Проверка, отображается ли элемент по его ID в течение таймаута (возвращает boolean)
    public static boolean isElementDisplayedWithId(int viewId, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                sleep(POLLING_DELAY);
            }
        }
        return false;
    }

    // Проверка, отображается ли элемент по его тексту в течение таймаута (возвращает boolean)
    public static boolean isElementDisplayedWithText(String text, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                sleep(POLLING_DELAY);
            }
        }
        return false;
    }

    // Проверка, отображается ли переданный ViewInteraction элемент в течение таймаута (возвращает boolean)
    public static boolean isElementDisplayed(ViewInteraction element, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                element.check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                sleep(POLLING_DELAY);
            }
        }
        return false;
    }

    // Ожидание появления элемента по его ID в течение таймаута (выбрасывает ошибку, если не найден)
    public static void waitForElementWithId(int elementId, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(elementId)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                sleep(POLLING_DELAY);
            }
        }
        throw new AssertionError("Element with ID " + elementId + " not found within " + timeout + "ms");
    }

    // Ожидание появления элемента по его тексту в течение таймаута (выбрасывает ошибку, если не найден)
    public static void waitForElementWithText(String text, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                sleep(POLLING_DELAY);
            }
        }
        throw new AssertionError("Element with text '" + text + "' not found within " + timeout + "ms");
    }

    // Ожидание появления переданного ViewInteraction элемента в течение таймаута (выбрасывает ошибку, если не найден)
    public static void waitForElement(ViewInteraction element, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                element.check(matches(isDisplayed()));
                return;
            } catch (Exception e) {
                sleep(POLLING_DELAY);
            }
        }
        throw new AssertionError("Element not displayed within " + timeout + "ms");
    }
}