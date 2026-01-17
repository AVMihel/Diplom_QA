package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;
import ru.iteco.fmhandroid.ui.pages.QuotesPage;

public class QuotesAboutLogoutTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final QuotesPage quotesPage = new QuotesPage();

    // TC-QUOTES-01: Полная проверка работы с цитатами
    @Test
    public void testQuotesFunctionality() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в раздел Quotes
        mainPage.clickQuotesButton();

        // 3. Проверяем экран Quotes
        quotesPage.checkQuotesScreenIsDisplayed();

        // 4. Проверяем список цитат
        quotesPage.checkQuotesListIsDisplayed();

        // 5. Разворачиваем первую цитату
        quotesPage.expandQuoteAtPosition(0);

        // 6. Проверяем описание цитаты
        quotesPage.checkQuoteDescriptionIsDisplayedAtPosition(0);

        // 7. Сворачиваем цитату
        quotesPage.expandQuoteAtPosition(0);

        // 8. Проверяем, что описание скрыто
        quotesPage.checkQuoteDescriptionIsHidden();

        // 9. Возвращаемся на главный экран
        quotesPage.goBackToMainScreen();

        // 10. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();
    }

    // Дополнительный тест: проверка разворачивания разных цитат
    @Test
    public void testMultipleQuotesExpansion() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в раздел Quotes
        mainPage.clickQuotesButton();

        // 3. Проверяем экран Quotes
        quotesPage.checkQuotesScreenIsDisplayed();

        // 4. Проверяем разворачивание/сворачивание нескольких цитат
        quotesPage.expandAndCollapseQuote(0); // Первая цитата
        quotesPage.expandAndCollapseQuote(1); // Вторая цитата
        quotesPage.expandAndCollapseQuote(2); // Третья цитата

        // 5. Возвращаемся на главный экран
        quotesPage.goBackToMainScreen();

        // 6. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-ABOUT-01: Проверка раздела "О приложении"
    @Test
    public void testAboutSection() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Открываем боковое меню
        navigationDrawer.openMenu();

        // 3. Переходим в раздел About
        navigationDrawer.clickAboutMenuItem();

        // 4. Проверяем экран About
        mainPage.checkAboutScreenIsDisplayed();

        // 5. Проверяем версию приложения
        onView(withId(R.id.about_version_title_text_view))
                .check(matches(withText("Version:")));
        onView(withId(R.id.about_version_value_text_view))
                .check(matches(isDisplayed()));

        // 6. Возвращаемся на главный экран
        onView(withId(R.id.about_back_image_button)).perform(click());

        // 7. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-LOGOUT-01: Выход из системы
    @Test
    public void testLogout() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Выполняем выход
        mainPage.logout();

        // 3. Проверяем переход на экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();
    }

    // TC-LOGOUT-02: Очистка полей при возврате с главного экрана
    @Test
    public void testClearFieldsAfterReturnFromMainScreen() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Выполняем выход
        mainPage.logout();

        // 3. Проверяем, что поля авторизации пустые
        authPage.checkAllFieldsAreEmpty();
    }
}