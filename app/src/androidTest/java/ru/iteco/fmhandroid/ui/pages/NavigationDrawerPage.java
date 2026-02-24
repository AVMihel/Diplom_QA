package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NavigationDrawerPage {

    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int SHORT_TIMEOUT = 3000;

    // Текстовые константы
    private static final String MAIN_MENU_TEXT = "Main";
    private static final String NEWS_MENU_TEXT = "News";
    private static final String ABOUT_MENU_TEXT = "About";

    // ID элементов
    private static final int MENU_ITEM_ID = android.R.id.title;
    private static final int MAIN_MENU_BUTTON_ID = R.id.main_menu_image_button;
    private static final int ABOUT_BACK_BUTTON_ID = R.id.about_back_image_button;


    public NavigationDrawerPage openMenu() {
        Allure.step("Открыть боковое меню");
        WaitUtils.waitForElementWithId(MAIN_MENU_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(MAIN_MENU_BUTTON_ID)).perform(click());
        checkMenuIsDisplayed();
        return this;
    }

    public NavigationDrawerPage clickMainMenuItem() {
        Allure.step("Нажать пункт меню 'Main'");
        clickMenuItem(MAIN_MENU_TEXT);
        return this;
    }

    public NavigationDrawerPage clickNewsMenuItem() {
        Allure.step("Нажать пункт меню 'News'");
        clickMenuItem(NEWS_MENU_TEXT);
        return this;
    }

    public NavigationDrawerPage clickAboutMenuItem() {
        Allure.step("Нажать пункт меню 'About'");
        clickMenuItem(ABOUT_MENU_TEXT);
        return this;
    }

    public NavigationDrawerPage clickAboutBackButton() {
        Allure.step("Нажать кнопку 'Назад' на экране 'About'");
        WaitUtils.waitForElementWithId(ABOUT_BACK_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(ABOUT_BACK_BUTTON_ID)).perform(click());
        return this;
    }

    public NavigationDrawerPage checkMenuIsDisplayed() {
        Allure.step("Проверить, что боковое меню отображается");
        WaitUtils.waitForElementWithText(MAIN_MENU_TEXT, DEFAULT_TIMEOUT);
        return this;
    }

    public boolean isMainMenuItemActive() {
        Allure.step("Проверить активность пункта 'Main'");
        return isMenuItemActive(MAIN_MENU_TEXT);
    }

    public boolean isNewsMenuItemActive() {
        Allure.step("Проверить активность пункта 'News'");
        return isMenuItemActive(NEWS_MENU_TEXT);
    }

    public boolean isAboutMenuItemActive() {
        Allure.step("Проверить активность пункта 'About'");
        return isMenuItemActive(ABOUT_MENU_TEXT);
    }

    public NavigationDrawerPage checkMainMenuItemIsActive() {
        Allure.step("Проверить, что пункт 'Main' активен");
        if (!isMainMenuItemActive()) {
            throw new AssertionError("Main menu item should be active");
        }
        return this;
    }

    public NavigationDrawerPage checkMainMenuItemIsInactive() {
        Allure.step("Проверить, что пункт 'Main' неактивен");
        if (isMainMenuItemActive()) {
            throw new AssertionError("Main menu item should be inactive");
        }
        return this;
    }

    public NavigationDrawerPage checkNewsMenuItemIsActive() {
        Allure.step("Проверить, что пункт 'News' активен");
        if (!isNewsMenuItemActive()) {
            throw new AssertionError("News menu item should be active");
        }
        return this;
    }

    public NavigationDrawerPage checkNewsMenuItemIsInactive() {
        Allure.step("Проверить, что пункт 'News' неактивен");
        if (isNewsMenuItemActive()) {
            throw new AssertionError("News menu item should be inactive");
        }
        return this;
    }

    public NavigationDrawerPage checkAboutMenuItemIsActive() {
        Allure.step("Проверить, что пункт 'About' активен");
        if (!isAboutMenuItemActive()) {
            throw new AssertionError("About menu item should be active");
        }
        return this;
    }

    // Вспомогательные методы

    private boolean isMenuItemActive(String menuText) {
        try {
            ViewInteraction menuItem = onView(
                    allOf(withId(MENU_ITEM_ID), withText(menuText), isDisplayed())
            );
            if (WaitUtils.isElementDisplayed(menuItem, SHORT_TIMEOUT)) {
                menuItem.check(matches(isEnabled()));
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void clickMenuItem(String menuText) {
        WaitUtils.waitForElementWithText(menuText, DEFAULT_TIMEOUT);
        onView(allOf(withId(MENU_ITEM_ID), withText(menuText), isDisplayed()))
                .perform(click());
    }
}