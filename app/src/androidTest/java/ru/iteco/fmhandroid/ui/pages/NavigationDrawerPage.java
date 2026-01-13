package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.ui.utils.WaitUtils;

// Page Object для работы с боковым навигационным меню
public class NavigationDrawerPage {

    // Открывает боковое меню (кликает по кнопке "гамбургер")
    public NavigationDrawerPage openMenu() {
        ViewInteraction menuButton = onView(
                allOf(withId(ru.iteco.fmhandroid.R.id.main_menu_image_button),
                        isDisplayed())
        );
        WaitUtils.waitForElement(menuButton, 5000);
        menuButton.perform(click());
        return this;
    }

    // Проверяет, что меню открыто (по наличию текста "Main")
    public NavigationDrawerPage checkMenuIsDisplayed() {
        WaitUtils.waitForElementWithText("Main", 3000);
        return this;
    }

    // Проверяет отображение пункта меню "Main"
    public NavigationDrawerPage checkMainMenuItemIsDisplayed() {
        ViewInteraction mainItem = onView(
                allOf(withId(android.R.id.title), withText("Main"), isDisplayed())
        );
        WaitUtils.waitForElement(mainItem, 3000);
        return this;
    }

    // Проверяет отображение пункта меню "News"
    public NavigationDrawerPage checkNewsMenuItemIsDisplayed() {
        ViewInteraction newsItem = onView(
                allOf(withId(android.R.id.title), withText("News"), isDisplayed())
        );
        WaitUtils.waitForElement(newsItem, 3000);
        return this;
    }

    // Проверяет отображение пункта меню "About"
    public NavigationDrawerPage checkAboutMenuItemIsDisplayed() {
        ViewInteraction aboutItem = onView(
                allOf(withId(android.R.id.title), withText("About"), isDisplayed())
        );
        WaitUtils.waitForElement(aboutItem, 3000);
        return this;
    }

    // Кликает по пункту меню "Main" для перехода на главный экран
    public NavigationDrawerPage clickMainMenuItem() {
        ViewInteraction mainItem = onView(
                allOf(withId(android.R.id.title), withText("Main"), isDisplayed())
        );
        WaitUtils.waitForElement(mainItem, 3000);
        mainItem.perform(click());
        return this;
    }

    // Кликает по пункту меню "News" для перехода в раздел новостей
    public NavigationDrawerPage clickNewsMenuItem() {
        ViewInteraction newsItem = onView(
                allOf(withId(android.R.id.title), withText("News"), isDisplayed())
        );
        WaitUtils.waitForElement(newsItem, 3000);
        newsItem.perform(click());
        return this;
    }

    // Кликает по пункту меню "About" для перехода в раздел "О приложении"
    public NavigationDrawerPage clickAboutMenuItem() {
        ViewInteraction aboutItem = onView(
                allOf(withId(android.R.id.title), withText("About"), isDisplayed())
        );
        WaitUtils.waitForElement(aboutItem, 3000);
        aboutItem.perform(click());
        return this;
    }
}