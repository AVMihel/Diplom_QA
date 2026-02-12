package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NavigationDrawerPage {

    private static final int LONG_DELAY = 1500;

    // Текстовые константы
    private static final String MAIN_MENU_TEXT = "Main";
    private static final String NEWS_MENU_TEXT = "News";
    private static final String ABOUT_MENU_TEXT = "About";

    // ID элементов
    private static final int MENU_ITEM_ID = android.R.id.title;
    private static final int MAIN_MENU_BUTTON_ID = R.id.main_menu_image_button;
    private static final int ABOUT_BACK_BUTTON_ID = R.id.about_back_image_button;

    public NavigationDrawerPage openMenu() {
        Allure.step("Открытие бокового меню");
        ViewInteraction menuButton = onView(
                allOf(withId(MAIN_MENU_BUTTON_ID), isDisplayed())
        );
        waitForElement(menuButton, LONG_DELAY);
        menuButton.perform(click());
        return this;
    }

    public NavigationDrawerPage checkMenuIsDisplayed() {
        Allure.step("Проверка отображения меню");
        WaitUtils.waitForElementWithText(MAIN_MENU_TEXT, LONG_DELAY);
        return this;
    }

    public boolean isMainMenuItemActive() {
        Allure.step("Получение состояния пункта меню 'Main'");
        return isMenuItemActive(MAIN_MENU_TEXT);
    }

    public boolean isNewsMenuItemActive() {
        Allure.step("Получение состояния пункта меню 'News'");
        return isMenuItemActive(NEWS_MENU_TEXT);
    }

    public boolean isAboutMenuItemActive() {
        Allure.step("Получение состояния пункта меню 'About'");
        return isMenuItemActive(ABOUT_MENU_TEXT);
    }

    public NavigationDrawerPage clickMainMenuItem() {
        Allure.step("Клик по пункту меню 'Main'");
        clickMenuItem(MAIN_MENU_TEXT);
        return this;
    }

    public NavigationDrawerPage clickNewsMenuItem() {
        Allure.step("Клик по пункту меню 'News'");
        clickMenuItem(NEWS_MENU_TEXT);
        return this;
    }

    public NavigationDrawerPage clickAboutMenuItem() {
        Allure.step("Клик по пункту меню 'About'");
        clickMenuItem(ABOUT_MENU_TEXT);
        return this;
    }

    public NavigationDrawerPage clickAboutBackButton() {
        Allure.step("Нажатие кнопки 'Назад' на экране 'About'");
        waitForElementWithId(ABOUT_BACK_BUTTON_ID, LONG_DELAY);
        onView(withId(ABOUT_BACK_BUTTON_ID)).perform(click());
        return this;
    }

    private boolean isMenuItemActive(String menuText) {
        Allure.step("Проверка активности пункта меню: " + menuText);
        ViewInteraction menuItem = onView(
                allOf(withId(MENU_ITEM_ID), withText(menuText), isDisplayed())
        );
        waitForElement(menuItem, LONG_DELAY);

        try {
            menuItem.check(matches(isEnabled()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void clickMenuItem(String menuText) {
        Allure.step("Клик по пункту меню: " + menuText);
        ViewInteraction menuItem = onView(
                allOf(withId(MENU_ITEM_ID), withText(menuText), isDisplayed())
        );
        waitForElement(menuItem, LONG_DELAY);
        menuItem.perform(click());
    }

    // Вспомогательные методы

    private void waitForElement(ViewInteraction element, long timeout) {
        WaitUtils.waitForElement(element, timeout);
    }

    private void waitForElementWithId(int elementId, long timeout) {
        WaitUtils.waitForElementWithId(elementId, timeout);
    }
}