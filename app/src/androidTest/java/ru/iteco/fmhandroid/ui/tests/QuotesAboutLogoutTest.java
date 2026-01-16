package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;
import ru.iteco.fmhandroid.ui.pages.QuotesPage;

/**
 * Тестовый класс для проверки функциональности:
 * - Цитаты (Quotes)
 * - Раздел "О приложении" (About)
 * - Выход из системы (Logout)
 */
public class QuotesAboutLogoutTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final QuotesPage quotesPage = new QuotesPage();

    @Before
    public void setUpTest() {
        performLoginAndGoToMainScreen();
        mainPage.checkMainScreenIsDisplayed();
    }

    @After
    public void tearDownTest() {
        try {
            if (mainPage.isMainScreenDisplayed()) {
                mainPage.forceLogout();
            }
        } catch (Exception e) {
        }
    }

    // TC-QUOTES-01: Полная проверка работы с цитатами
    @Test
    public void testQuotesFunctionality() {
        // 1. Нажать на верхней панели кнопку перехода в "Quotes"
        mainPage.clickQuotesButton();

        // 2. Проверяем, что открывается вкладка Quotes с заголовком "Love is all"
        quotesPage.checkQuotesScreenIsDisplayed();

        // 3. Проверяем наличие списка цитат
        quotesPage.checkQuotesListIsDisplayed();

        // 4. Нажать стрелку у одной из цитат (первой в списке)
        quotesPage.expandQuoteAtPosition(0);

        // 5. Проверяем, что под цитатой раскрывается отзыв пользователя
        quotesPage.checkQuoteDescriptionIsDisplayedAtPosition(0);

        // 6. Еще раз нажать стрелку у той же цитаты
        quotesPage.expandQuoteAtPosition(0);

        // 7. Проверяем, что отзыв скрывается
        quotesPage.checkQuoteDescriptionIsHidden();

        // 8. Возвращаемся на главный экран через боковое меню
        quotesPage.goBackToMainScreen();
        mainPage.checkMainScreenIsDisplayed();
    }

    // Дополнительный тест: проверка разворачивания разных цитат
    @Test
    public void testMultipleQuotesExpansion() {
        // Переходим в Quotes
        mainPage.clickQuotesButton();
        quotesPage.checkQuotesScreenIsDisplayed();

        // Проверяем несколько цитат
        quotesPage.expandAndCollapseQuote(0); // Первая цитата
        quotesPage.expandAndCollapseQuote(1); // Вторая цитата
        quotesPage.expandAndCollapseQuote(2); // Третья цитата

        // Возвращаемся на главный
        quotesPage.goBackToMainScreen();
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-ABOUT-01: Проверка раздела "О приложении"
    @Test
    public void testAboutSection() {
        // 1. Открыть боковое меню
        navigationDrawer.openMenu();

        // 2. Нажать пункт "About"
        navigationDrawer.clickAboutMenuItem();

        // 3. Проверяем, что открывается раздел "About"
        mainPage.checkAboutScreenIsDisplayed();

        // 4. Проверяем отображение версии приложения
        onView(withId(R.id.about_version_title_text_view))
                .check(matches(withText("Version:")));
        onView(withId(R.id.about_version_value_text_view))
                .check(matches(isDisplayed()));

        // 5. Возвращаемся на главный экран
        onView(withId(R.id.about_back_image_button)).perform(click());
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-LOGOUT-01: Выход из системы
    @Test
    public void testLogout() {
        // 1. Нажать кнопку "Log out" на верхней панели
        mainPage.logout();

        // 2. Проверяем переход на экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();
    }

    // TC-LOGOUT-02: Очистка полей при возврате с главного экрана
    @Test
    public void testClearFieldsAfterReturnFromMainScreen() {
        // 1. Нажать кнопку "Log out"
        mainPage.logout();

        // 2. Проверяем, что произошел переход на экран авторизации
        // И проверяем, что поля Login и Password пустые
        authPage.checkAllFieldsAreEmpty();
    }
}