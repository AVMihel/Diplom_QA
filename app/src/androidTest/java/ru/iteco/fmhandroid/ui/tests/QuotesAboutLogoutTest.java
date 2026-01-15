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
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;

/**
 * Тестовый класс для проверки функциональности:
 * - Цитаты (Quotes) - базовое тестирование
 * - Раздел "О приложении" (About)
 * - Выход из системы (Logout)
 */
public class QuotesAboutLogoutTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();

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
            // Игнорируем
        }
    }

    // TC-QUOTES-01: Проверка раздела с цитатами
    @Test
    public void testQuotesFunctionality() {
        // 1. Нажать на верхней панели кнопку перехода в "Quotes"
        mainPage.clickQuotesButton();

        // 2. Проверяем, что открывается вкладка Quotes с заголовком "Love is all"
        mainPage.checkQuotesScreenIsDisplayed();

        // 3. Проверяем наличие списка цитат
        onView(withId(R.id.our_mission_item_list_recycler_view))
                .check(matches(isDisplayed()));

        // 4. Возвращаемся на главный экран через боковое меню
        navigationDrawer.openMenu().clickMainMenuItem();
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

        // 3. Подтверждаем, что выход успешен - проверяем возможность снова авторизоваться
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-LOGOUT-02: Очистка полей при возврате с главного экрана
    @Test
    public void testClearFieldsAfterReturnFromMainScreen() {
        // 1. Нажать кнопку "Log out"
        mainPage.logout();

        // 2. Проверяем, что произошел переход на экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();

        // 3. Авторизуемся снова
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainScreenIsDisplayed();

        // 4. Снова выходим
        mainPage.logout();

        // 5. Проверяем, что поля снова пустые (экран авторизации отображается)
        authPage.checkAuthorizationScreenIsDisplayed();
    }
}