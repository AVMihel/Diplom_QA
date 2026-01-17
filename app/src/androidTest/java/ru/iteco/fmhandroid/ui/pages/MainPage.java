package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.os.SystemClock;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class MainPage {

    private final ViewInteraction logoutButton = onView(
            allOf(withId(R.id.authorization_image_button), isDisplayed())
    );

    private final ViewInteraction allNewsButtonOnMain = onView(
            allOf(withId(R.id.all_news_text_view), withText("ALL NEWS"), isDisplayed())
    );

    // Быстрая проверка главного экрана
    public boolean isMainScreenDisplayedQuick(long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                allNewsButtonOnMain.check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        return false;
    }

    // Проверяет, что главный экран отображается
    public MainPage checkMainScreenIsDisplayed() {
        WaitUtils.waitForElement(allNewsButtonOnMain, 5000);
        return this;
    }

    // Проверяет, что экран News отображается
    public MainPage checkNewsScreenIsDisplayed() {
        WaitUtils.waitForElementWithText("News", 3000);
        return this;
    }

    // Проверяет, что экран About отображается
    public MainPage checkAboutScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.about_version_title_text_view, 3000);
        onView(withId(R.id.about_version_title_text_view)).check(matches(withText("Version:")));
        return this;
    }

    // Проверяет, что экран Quotes успешно открылся
    public MainPage checkQuotesScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.our_mission_title_text_view, 3000);
        onView(withId(R.id.our_mission_title_text_view)).check(matches(withText("Love is all")));
        return this;
    }

    // Проверяет, что блок новостей отображается на главной странице
    public MainPage checkNewsBlockOnMainIsDisplayed() {
        ViewInteraction newsBlock = onView(
                allOf(withId(R.id.container_list_news_include_on_fragment_main), isDisplayed())
        );
        WaitUtils.waitForElement(newsBlock, 2000);
        return this;
    }

    // Кликает по кнопке перехода в раздел Quotes
    public MainPage clickQuotesButton() {
        ViewInteraction quotesButton = onView(
                allOf(withId(R.id.our_mission_image_button), isDisplayed())
        );
        WaitUtils.waitForElement(quotesButton, 2000);
        quotesButton.perform(click());
        return this;
    }

    // Выполняет выход из системы (с проверкой ошибок)
    public void logout() {
        WaitUtils.waitForElement(logoutButton, 2000);
        logoutButton.perform(click());
        WaitUtils.waitForElementWithText("Log out", 2000);
        onView(withText("Log out")).perform(click());
    }

    // Безопасно пытается выйти из системы (без исключений)
    public void tryToLogout() {
        try {
            if (isElementDisplayedQuickly(logoutButton, 1500)) {
                logoutButton.perform(click());
                Thread.sleep(300);
                if (isElementWithTextDisplayedQuickly("Log out", 1500)) {
                    onView(withText("Log out")).perform(click());
                }
            }
        } catch (Exception e) {
        }
    }

    // Быстрая проверка отображения элемента
    private boolean isElementDisplayedQuickly(ViewInteraction view, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                view.check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        return false;
    }

    // Быстрая проверка отображения элемента по тексту
    private boolean isElementWithTextDisplayedQuickly(String text, long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(withText(text)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        return false;
    }
}