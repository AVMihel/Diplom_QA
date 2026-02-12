package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;

public class WaitUtils {

    private static final int DEFAULT_POLLING_INTERVAL = 50;

    @FunctionalInterface
    private interface Condition {
        boolean check();
    }

    public static void waitForElementWithId(int id, long timeout) {
        Allure.step("Ожидание элемента с ID: " + id + " (таймаут: " + timeout + " мс)");
        waitForCondition(() -> checkElement(withId(id)), timeout, "Element with ID: " + id);
    }

    public static void waitForElementWithText(String text, long timeout) {
        Allure.step("Ожидание элемента с текстом: '" + text + "' (таймаут: " + timeout + " мс)");
        waitForCondition(() -> checkElement(withText(text)), timeout, "Element with text: '" + text + "'");
    }

    public static void waitForElement(ViewInteraction element, long timeout) {
        Allure.step("Ожидание ViewInteraction элемента (таймаут: " + timeout + " мс)");
        waitForCondition(() -> checkElement(element), timeout, "ViewInteraction element");
    }

    public static boolean isElementDisplayedWithId(int id, long timeout) {
        Allure.step("Проверка отображения элемента с ID: " + id + " (таймаут: " + timeout + " мс)");
        return waitForCondition(() -> checkElement(withId(id)), timeout,
                "Element with ID: " + id, false);
    }

    public static boolean isElementDisplayedWithText(String text, long timeout) {
        Allure.step("Проверка отображения элемента с текстом: '" + text + "' (таймаут: " + timeout + " мс)");
        return waitForCondition(() -> checkElement(withText(text)), timeout,
                "Element with text: '" + text + "'", false);
    }

    public static void waitForMillis(long millis) {
        Allure.step("Ожидание " + millis + " мс");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void waitForAnyElement(int[] ids, long timeout) {
        StringBuilder idsStr = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            idsStr.append(ids[i]);
            if (i < ids.length - 1) idsStr.append(", ");
        }
        Allure.step("Ожидание любого из элементов с ID: [" + idsStr + "] (таймаут: " + timeout + " мс)");
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
            String errorMsg = "Timeout waiting for: " + description + " (" + timeout + " мс)";
            Allure.step("ОШИБКА: " + errorMsg);
            throw new RuntimeException(errorMsg);
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
                // Игнорируем, проверяем следующий элемент
            }
        }
        return false;
    }
}