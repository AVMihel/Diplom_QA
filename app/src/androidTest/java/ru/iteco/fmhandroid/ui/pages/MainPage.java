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

// Page Object для работы с главным экраном приложения
public class MainPage {

    // Элементы верхней панели
    private final ViewInteraction quotesButton = onView(
            allOf(withId(R.id.our_mission_image_button), isDisplayed())
    );

    private final ViewInteraction logoutButton = onView(
            allOf(withId(R.id.authorization_image_button), isDisplayed())
    );

    private final ViewInteraction menuButton = onView(
            allOf(withId(R.id.main_menu_image_button), isDisplayed())
    );

    // Проверяет отображение главного экрана по наличию кнопки меню
    public MainPage checkMainScreenIsDisplayed() {
        WaitUtils.waitForElement(menuButton, 10000);
        return this;
    }

    // Проверяет отображение экрана News по тексту заголовка
    public MainPage checkNewsScreenIsDisplayed() {
        WaitUtils.waitForElementWithText("News", 10000);
        return this;
    }

    // Проверяет отображение экрана About по тексту "Version:"
    public MainPage checkAboutScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.about_version_title_text_view, 10000);
        onView(withId(R.id.about_version_title_text_view)).check(matches(withText("Version:")));
        return this;
    }

    // Проверяет отображение экрана Quotes по заголовку "Love is all"
    public MainPage checkQuotesScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.our_mission_title_text_view, 10000);
        onView(withId(R.id.our_mission_title_text_view)).check(matches(withText("Love is all")));
        return this;
    }

    // Кликает по кнопке перехода в раздел Quotes (иконка Our Mission)
    public void clickQuotesButton() {
        WaitUtils.waitForElement(quotesButton, 5000);
        quotesButton.perform(click());
    }

    // Выполняет выход из системы через меню авторизации
    public void logout() {
        clickAuthorizationMenuButton();
        WaitUtils.waitForElementWithText("Log out", 3000);
        onView(withText("Log out")).perform(click());
    }

    // Кликает по кнопке меню авторизации (вспомогательный метод для logout)
    private void clickAuthorizationMenuButton() {
        WaitUtils.waitForElement(logoutButton, 5000);
        logoutButton.perform(click());
    }

    // Проверяет успешную авторизацию (пользователь на главном экране)
    public void checkSuccessfulAuthorization() {
        checkMainScreenIsDisplayed();
    }

    // Проверяет отображение главного экрана (без выброса исключения)
    public boolean isMainScreenDisplayed() {
        try {
            checkMainScreenIsDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Выполняет принудительный выход из системы (с обработкой ошибок)
    public void forceLogout() {
        try {
            logout();
        } catch (Exception e) {
            System.out.println("Normal logout failed, trying force logout: " + e.getMessage());
            try {
                onView(allOf(withId(R.id.authorization_image_button), isDisplayed()))
                        .perform(click());
                Thread.sleep(500);
                onView(withText("Log out")).perform(click());
                Thread.sleep(1000);
                System.out.println("Force logout successful");
            } catch (Exception ex) {
                System.out.println("Force logout also failed: " + ex.getMessage());
            }
        }
    }

    // Проверяет, что блок новостей отображается на главной странице
    public MainPage checkNewsBlockIsDisplayed() {
        ViewInteraction newsBlock = onView(
                allOf(withId(R.id.container_list_news_include_on_fragment_main), isDisplayed())
        );
        WaitUtils.waitForElement(newsBlock, 5000);
        return this;
    }
}