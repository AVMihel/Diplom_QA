package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class MainPage {

    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;
    private static final int POLLING_DELAY = 50;

    // Текстовые константы
    private static final String ALL_NEWS_TEXT = "ALL NEWS";
    private static final String NEWS_TEXT = "News";
    private static final String LOG_OUT_TEXT = "Log out";
    private static final String VERSION_TEXT = "Version:";
    private static final String LOVE_IS_ALL_TEXT = "Love is all";

    // ID элементов
    private static final int LOGOUT_BUTTON_ID = R.id.authorization_image_button;
    private static final int ALL_NEWS_BUTTON_ID = R.id.all_news_text_view;
    private static final int NEWS_BLOCK_ID = R.id.container_list_news_include_on_fragment_main;
    private static final int QUOTES_BUTTON_ID = R.id.our_mission_image_button;
    private static final int ABOUT_VERSION_TITLE_ID = R.id.about_version_title_text_view;
    private static final int OUR_MISSION_TITLE_ID = R.id.our_mission_title_text_view;

    // Быстрая проверка отображения главного экрана
    public boolean isMainScreenDisplayedQuick(long timeout) {
        return isElementDisplayedQuickly(getAllNewsButtonOnMain(), timeout);
    }

    // Проверка отображения главного экрана
    public MainPage checkMainScreenIsDisplayed() {
        WaitUtils.waitForElement(getAllNewsButtonOnMain(), LONG_DELAY);
        return this;
    }

    // Проверка отображения экрана новостей
    public MainPage checkNewsScreenIsDisplayed() {
        WaitUtils.waitForElementWithText(NEWS_TEXT, LONG_DELAY);
        return this;
    }

    // Проверка отображения экрана "О приложении"
    public MainPage checkAboutScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(ABOUT_VERSION_TITLE_ID, LONG_DELAY);
        onView(withId(ABOUT_VERSION_TITLE_ID)).check(matches(withText(VERSION_TEXT)));
        return this;
    }

    // Проверка отображения экрана цитат
    public MainPage checkQuotesScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(OUR_MISSION_TITLE_ID, LONG_DELAY);
        onView(withId(OUR_MISSION_TITLE_ID)).check(matches(withText(LOVE_IS_ALL_TEXT)));
        return this;
    }

    // Проверка отображения блока новостей на главном экране
    public MainPage checkNewsBlockOnMainIsDisplayed() {
        WaitUtils.waitForElement(getNewsBlock(), LONG_DELAY);
        return this;
    }

    // Клик по кнопке цитат
    public MainPage clickQuotesButton() {
        WaitUtils.waitForElement(getQuotesButton(), LONG_DELAY);
        getQuotesButton().perform(click());
        return this;
    }

    // Выход из приложения
    public void logout() {
        WaitUtils.waitForElement(getLogoutButton(), LONG_DELAY);
        getLogoutButton().perform(click());
        WaitUtils.waitForElementWithText(LOG_OUT_TEXT, LONG_DELAY);
        onView(withText(LOG_OUT_TEXT)).perform(click());
    }

    // Попытка выхода из приложения (безопасный метод)
    public void tryToLogout() {
        try {
            if (isElementDisplayedQuickly(getLogoutButton(), LONG_DELAY)) {
                getLogoutButton().perform(click());
                WaitUtils.waitForMillis(MEDIUM_DELAY);
                if (isElementWithTextDisplayedQuickly(LOG_OUT_TEXT, LONG_DELAY)) {
                    onView(withText(LOG_OUT_TEXT)).perform(click());
                }
            }
        } catch (Exception e) {
            // Игнорируем ошибки при попытке выхода
        }
    }

    // Быстрая проверка отображения элемента
    private boolean isElementDisplayedQuickly(ViewInteraction view, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                view.check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                WaitUtils.waitForMillis(POLLING_DELAY);
            }
        }
        return false;
    }

    // Быстрая проверка отображения элемента с текстом
    private boolean isElementWithTextDisplayedQuickly(String text, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                WaitUtils.waitForMillis(POLLING_DELAY);
            }
        }
        return false;
    }

    // Получение элементов интерфейса
    private ViewInteraction getLogoutButton() {
        return onView(allOf(withId(LOGOUT_BUTTON_ID), isDisplayed()));
    }

    private ViewInteraction getAllNewsButtonOnMain() {
        return onView(allOf(withId(ALL_NEWS_BUTTON_ID), withText(ALL_NEWS_TEXT), isDisplayed()));
    }

    private ViewInteraction getNewsBlock() {
        return onView(allOf(withId(NEWS_BLOCK_ID), isDisplayed()));
    }

    private ViewInteraction getQuotesButton() {
        return onView(allOf(withId(QUOTES_BUTTON_ID), isDisplayed()));
    }
}