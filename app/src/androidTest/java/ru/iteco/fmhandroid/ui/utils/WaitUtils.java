package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

public class WaitUtils {

    private static final int DEFAULT_POLLING_INTERVAL = 50;

    @FunctionalInterface
    private interface Condition {
        boolean check();
    }

    // Ожидание элемента по ID
    public static void waitForElementWithId(int id, long timeout) {
        waitForCondition(() -> checkElement(withId(id)), timeout, "Element with ID: " + id);
    }

    // Ожидание элемента по тексту
    public static void waitForElementWithText(String text, long timeout) {
        waitForCondition(() -> checkElement(withText(text)), timeout, "Element with text: '" + text + "'");
    }

    // Ожидание элемента (ViewInteraction)
    public static void waitForElement(ViewInteraction element, long timeout) {
        waitForCondition(() -> checkElement(element), timeout, "ViewInteraction element");
    }

    // Проверка отображения элемента по ID (возвращает 'boolean')
    public static boolean isElementDisplayedWithId(int id, long timeout) {
        return waitForCondition(() -> checkElement(withId(id)), timeout,
                "Element with ID: " + id, false);
    }

    // Проверка отображения элемента по тексту (возвращает 'boolean')
    public static boolean isElementDisplayedWithText(String text, long timeout) {
        return waitForCondition(() -> checkElement(withText(text)), timeout,
                "Element with text: '" + text + "'", false);
    }

    // Ожидание без условий (просто задержка)
    public static void waitForMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Ожидание появления любого из указанных элементов
    public static void waitForAnyElement(int[] ids, long timeout) {
        waitForCondition(() -> checkAnyElement(ids), timeout,
                "Any of " + ids.length + " elements");
    }

    // Базовый метод ожидания условия (с выбрасыванием исключения при таймауте)
    private static void waitForCondition(Condition condition, long timeout, String description) {
        waitForCondition(condition, timeout, description, true);
    }

    // Базовый метод ожидания условия
    private static boolean waitForCondition(Condition condition, long timeout,
                                            String description, boolean throwOnTimeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            if (condition.check()) {
                return true;
            }
            waitForMillis(DEFAULT_POLLING_INTERVAL);
        }
        if (condition.check()) {
            return true;
        }
        if (throwOnTimeout) {
            throw new RuntimeException("Timeout waiting for: " + description);
        }
        return false;
    }

    // Вспомогательные методы

    private static boolean checkElement(org.hamcrest.Matcher<android.view.View> matcher) {
        try {
            onView(matcher).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkElement(ViewInteraction element) {
        try {
            element.check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean checkAnyElement(int[] ids) {
        for (int id : ids) {
            try {
                onView(withId(id)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }
}