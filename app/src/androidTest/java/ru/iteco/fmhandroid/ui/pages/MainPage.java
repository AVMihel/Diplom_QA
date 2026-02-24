package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class MainPage {

    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int SHORT_TIMEOUT = 3000;

    // Текстовые константы
    private static final String ALL_NEWS_TEXT = "ALL NEWS";
    private static final String NEWS_TEXT = "News";
    private static final String LOG_OUT_TEXT = "Log out";
    private static final String VERSION_TEXT = "Version:";

    // ID элементов
    private static final int LOGOUT_BUTTON_ID = R.id.authorization_image_button;
    private static final int ALL_NEWS_BUTTON_ID = R.id.all_news_text_view;
    private static final int NEWS_BLOCK_ID = R.id.container_list_news_include_on_fragment_main;
    private static final int QUOTES_BUTTON_ID = R.id.our_mission_image_button;
    private static final int ABOUT_VERSION_TITLE_ID = R.id.about_version_title_text_view;


    public boolean isMainScreenDisplayed() {
        Allure.step("Проверка отображения главного экрана");
        return WaitUtils.isElementDisplayedWithId(ALL_NEWS_BUTTON_ID, SHORT_TIMEOUT);
    }

    public boolean isNewsScreenDisplayed() {
        Allure.step("Проверка отображения экрана новостей");
        return WaitUtils.isElementDisplayedWithText(NEWS_TEXT, SHORT_TIMEOUT);
    }

    public boolean isAboutScreenDisplayed() {
        Allure.step("Проверка отображения экрана 'О приложении'");
        if (WaitUtils.isElementDisplayedWithId(ABOUT_VERSION_TITLE_ID, SHORT_TIMEOUT)) {
            onView(withId(ABOUT_VERSION_TITLE_ID)).check(matches(withText(VERSION_TEXT)));
            return true;
        }
        return false;
    }

    public boolean isNewsBlockDisplayed() {
        Allure.step("Проверка отображения блока новостей на главном экране");
        return WaitUtils.isElementDisplayedWithId(NEWS_BLOCK_ID, SHORT_TIMEOUT);
    }

    public MainPage clickQuotesButton() {
        Allure.step("Нажать кнопку перехода к цитатам на верхней панели");
        WaitUtils.waitForElementWithId(QUOTES_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(QUOTES_BUTTON_ID)).perform(click());
        return this;
    }

    public MainPage clickLogoutButton() {
        Allure.step("Нажать кнопку 'Log out' на верхней панели");
        WaitUtils.waitForElementWithId(LOGOUT_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(LOGOUT_BUTTON_ID)).perform(click());
        return this;
    }

    public MainPage confirmLogout() {
        Allure.step("Подтвердить выход в диалоговом окне");
        WaitUtils.waitForElementWithText(LOG_OUT_TEXT, DEFAULT_TIMEOUT);
        onView(withText(LOG_OUT_TEXT)).perform(click());
        return this;
    }

    public void logout() {
        Allure.step("Выполнить полный выход из системы");
        clickLogoutButton();
        confirmLogout();
    }

    public void tryToLogout() {
        try {
            if (WaitUtils.isElementDisplayedWithId(LOGOUT_BUTTON_ID, SHORT_TIMEOUT)) {
                onView(withId(LOGOUT_BUTTON_ID)).perform(click());
                WaitUtils.waitMillis(500);
                if (WaitUtils.isElementDisplayedWithText(LOG_OUT_TEXT, SHORT_TIMEOUT)) {
                    onView(withText(LOG_OUT_TEXT)).perform(click());
                }
            }
        } catch (Exception e) {
        }
    }
}