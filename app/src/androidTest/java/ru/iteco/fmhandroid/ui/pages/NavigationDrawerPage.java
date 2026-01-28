package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

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
    private static final int ABOUT_CONTAINER_ID = R.id.container_custom_app_bar_include_on_fragment_about;

    // Открытие бокового меню
    public NavigationDrawerPage openMenu() {
        ViewInteraction menuButton = onView(
                allOf(withId(MAIN_MENU_BUTTON_ID), isDisplayed())
        );
        WaitUtils.waitForElement(menuButton, LONG_DELAY);
        menuButton.perform(click());
        return this;
    }

    // Проверка отображения меню
    public NavigationDrawerPage checkMenuIsDisplayed() {
        WaitUtils.waitForElementWithText(MAIN_MENU_TEXT, LONG_DELAY);
        return this;
    }

    // Проверка, что пункт меню "Main" отображается и активен
    public NavigationDrawerPage checkMainMenuItemIsActive() {
        checkMenuItem(MAIN_MENU_TEXT, true, "'Main' menu item should be active");
        return this;
    }

    // Проверка, что пункт меню "News" отображается и активен
    public NavigationDrawerPage checkNewsMenuItemIsActive() {
        checkMenuItem(NEWS_MENU_TEXT, true, "'News' menu item should be active");
        return this;
    }

    // Проверка, что пункт меню "News" отображается и неактивен
    public NavigationDrawerPage checkNewsMenuItemIsInactive() {
        checkMenuItem(NEWS_MENU_TEXT, false, "'News' menu item should be inactive on this screen");
        return this;
    }

    // Проверка, что пункт меню "About" отображается и активен
    public NavigationDrawerPage checkAboutMenuItemIsActive() {
        checkMenuItem(ABOUT_MENU_TEXT, true, "'About' menu item should be active");
        return this;
    }

    // Клик по пункту меню "Main"
    public NavigationDrawerPage clickMainMenuItem() {
        clickMenuItem(MAIN_MENU_TEXT);
        return this;
    }

    // Клик по пункту меню "News"
    public NavigationDrawerPage clickNewsMenuItem() {
        clickMenuItem(NEWS_MENU_TEXT);
        return this;
    }

    // Клик по пункту меню "About"
    public NavigationDrawerPage clickAboutMenuItem() {
        clickMenuItem(ABOUT_MENU_TEXT);
        return this;
    }

    // Нажатие кнопки "Назад" на экране "About"
    public NavigationDrawerPage clickAboutBackButton() {
        ViewInteraction backButton = onView(
                allOf(withId(ABOUT_BACK_BUTTON_ID),
                        childAtPosition(
                                allOf(withId(ABOUT_CONTAINER_ID),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                1),
                        isDisplayed())
        );
        backButton.perform(click());
        return this;
    }

    // Универсальная проверка пункта меню
    private void checkMenuItem(String menuText, boolean shouldBeActive, String errorMessage) {
        try {
            ViewInteraction menuItem = onView(
                    allOf(withId(MENU_ITEM_ID), withText(menuText), isDisplayed())
            );
            WaitUtils.waitForElement(menuItem, LONG_DELAY);

            if (shouldBeActive) {
                menuItem.check(matches(isEnabled()));
            } else {
                menuItem.check(matches(isNotEnabled()));
            }
        } catch (AssertionError e) {
            throw new AssertionError(errorMessage);
        }
    }

    // Универсальный клик по пункту меню
    private void clickMenuItem(String menuText) {
        ViewInteraction menuItem = onView(
                allOf(withId(MENU_ITEM_ID), withText(menuText), isDisplayed())
        );
        WaitUtils.waitForElement(menuItem, LONG_DELAY);
        menuItem.perform(click());
    }

    // Поиск дочернего элемента по позиции в иерархии View
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}