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

    // Быстрая проверка отображения главного экрана
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

    // Проверка отображения главного экрана
    public MainPage checkMainScreenIsDisplayed() {
        WaitUtils.waitForElement(allNewsButtonOnMain, 5000);
        return this;
    }

    // Проверка отображения экрана новостей
    public MainPage checkNewsScreenIsDisplayed() {
        WaitUtils.waitForElementWithText("News", 3000);
        return this;
    }

    // Проверка отображения экрана "О приложении"
    public MainPage checkAboutScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.about_version_title_text_view, 3000);
        onView(withId(R.id.about_version_title_text_view)).check(matches(withText("Version:")));
        return this;
    }

    // Проверка отображения экрана цитат
    public MainPage checkQuotesScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.our_mission_title_text_view, 3000);
        onView(withId(R.id.our_mission_title_text_view)).check(matches(withText("Love is all")));
        return this;
    }

    // Проверка отображения блока новостей на главном экране
    public MainPage checkNewsBlockOnMainIsDisplayed() {
        ViewInteraction newsBlock = onView(
                allOf(withId(R.id.container_list_news_include_on_fragment_main), isDisplayed())
        );
        WaitUtils.waitForElement(newsBlock, 2000);
        return this;
    }

    // Клик по кнопке цитат
    public MainPage clickQuotesButton() {
        ViewInteraction quotesButton = onView(
                allOf(withId(R.id.our_mission_image_button), isDisplayed())
        );
        WaitUtils.waitForElement(quotesButton, 2000);
        quotesButton.perform(click());
        return this;
    }

    // Выход из приложения
    public void logout() {
        WaitUtils.waitForElement(logoutButton, 2000);
        logoutButton.perform(click());
        WaitUtils.waitForElementWithText("Log out", 2000);
        onView(withText("Log out")).perform(click());
    }

    // Попытка выхода из приложения (безопасный метод)
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
            // Игнорируем
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

    // Быстрая проверка отображения элемента с текстом
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