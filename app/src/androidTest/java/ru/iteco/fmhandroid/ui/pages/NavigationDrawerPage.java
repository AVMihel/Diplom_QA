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

import io.qameta.allure.kotlin.Step;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

@DisplayName("Страница бокового меню навигации")
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

    @Step("Открытие бокового меню")
    public NavigationDrawerPage openMenu() {
        ViewInteraction menuButton = onView(
                allOf(withId(MAIN_MENU_BUTTON_ID), isDisplayed())
        );
        waitForElement(menuButton, LONG_DELAY);
        menuButton.perform(click());
        return this;
    }

    @Step("Проверка отображения меню")
    public NavigationDrawerPage checkMenuIsDisplayed() {
        WaitUtils.waitForElementWithText(MAIN_MENU_TEXT, LONG_DELAY);
        return this;
    }

    @Step("Получение состояния пункта меню 'Main'")
    public boolean isMainMenuItemActive() {
        return isMenuItemActive(MAIN_MENU_TEXT);
    }

    @Step("Получение состояния пункта меню 'News'")
    public boolean isNewsMenuItemActive() {
        return isMenuItemActive(NEWS_MENU_TEXT);
    }

    @Step("Получение состояния пункта меню 'About'")
    public boolean isAboutMenuItemActive() {
        return isMenuItemActive(ABOUT_MENU_TEXT);
    }

    @Step("Клик по пункту меню 'Main'")
    public NavigationDrawerPage clickMainMenuItem() {
        clickMenuItem(MAIN_MENU_TEXT);
        return this;
    }

    @Step("Клик по пункту меню 'News'")
    public NavigationDrawerPage clickNewsMenuItem() {
        clickMenuItem(NEWS_MENU_TEXT);
        return this;
    }

    @Step("Клик по пункту меню 'About'")
    public NavigationDrawerPage clickAboutMenuItem() {
        clickMenuItem(ABOUT_MENU_TEXT);
        return this;
    }

    @Step("Нажатие кнопки 'Назад' на экране 'About'")
    public NavigationDrawerPage clickAboutBackButton() {
        waitForElementWithId(ABOUT_BACK_BUTTON_ID, LONG_DELAY);
        onView(withId(ABOUT_BACK_BUTTON_ID)).perform(click());
        return this;
    }

    @Step("Проверка активности пункта меню")
    private boolean isMenuItemActive(String menuText) {
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

    @Step("Клик по пункту меню: {menuText}")
    private void clickMenuItem(String menuText) {
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