package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NavigationDrawerPage {

    // Открытие бокового меню
    public NavigationDrawerPage openMenu() {
        ViewInteraction menuButton = onView(
                allOf(withId(R.id.main_menu_image_button),
                        isDisplayed())
        );
        WaitUtils.waitForElement(menuButton, 2000);
        menuButton.perform(click());
        return this;
    }

    // Проверка отображения меню
    public NavigationDrawerPage checkMenuIsDisplayed() {
        WaitUtils.waitForElementWithText("Main", 1500);
        return this;
    }

    // Проверка отображения пункта меню "Main"
    public NavigationDrawerPage checkMainMenuItemIsDisplayed() {
        ViewInteraction mainItem = onView(
                allOf(withId(android.R.id.title), withText("Main"), isDisplayed())
        );
        WaitUtils.waitForElement(mainItem, 1500);
        return this;
    }

    // Проверка отображения пункта меню "News"
    public NavigationDrawerPage checkNewsMenuItemIsDisplayed() {
        ViewInteraction newsItem = onView(
                allOf(withId(android.R.id.title), withText("News"), isDisplayed())
        );
        WaitUtils.waitForElement(newsItem, 1500);
        return this;
    }

    // Проверка отображения пункта меню "About"
    public NavigationDrawerPage checkAboutMenuItemIsDisplayed() {
        ViewInteraction aboutItem = onView(
                allOf(withId(android.R.id.title), withText("About"), isDisplayed())
        );
        WaitUtils.waitForElement(aboutItem, 1500);
        return this;
    }

    // Клик по пункту меню "Main"
    public NavigationDrawerPage clickMainMenuItem() {
        ViewInteraction mainItem = onView(
                allOf(withId(android.R.id.title), withText("Main"), isDisplayed())
        );
        WaitUtils.waitForElement(mainItem, 1500);
        mainItem.perform(click());
        return this;
    }

    // Клик по пункту меню "News"
    public NavigationDrawerPage clickNewsMenuItem() {
        ViewInteraction newsItem = onView(
                allOf(withId(android.R.id.title), withText("News"), isDisplayed())
        );
        WaitUtils.waitForElement(newsItem, 1500);
        newsItem.perform(click());
        return this;
    }

    // Клик по пункту меню "About"
    public NavigationDrawerPage clickAboutMenuItem() {
        ViewInteraction aboutItem = onView(
                allOf(withId(android.R.id.title), withText("About"), isDisplayed())
        );
        WaitUtils.waitForElement(aboutItem, 1500);
        aboutItem.perform(click());
        return this;
    }
}