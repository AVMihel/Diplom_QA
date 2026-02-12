package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;
import ru.iteco.fmhandroid.ui.pages.QuotesPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Дополнительные функции")
@Feature("Цитаты, информация о приложении, выход из системы")
@DisplayName("Тесты цитат, раздела 'О приложении' и выхода из системы")
public class QuotesAboutLogoutTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final QuotesPage quotesPage = new QuotesPage();

    @Before
    public void setUp() {
        Allure.step("Настройка тестового окружения - авторизация и переход на главный экран");
        setUpToAuthScreen();
        loginAndGoToMainScreen();
    }

    @After
    public void tearDown() {
        Allure.step("Очистка после теста - выход из системы");
        tearDownToAuthScreen();
    }

    @Test
    @DisplayName("Работа с цитатами")
    @Description("TC-QUOTES-01: Работа с цитатами")
    @Story("Пользователь может просматривать и управлять отображением цитат")
    public void testQuotesFunctionality() {
        Allure.step("Шаг 1: Переход на экран цитат");
        mainPage.clickQuotesButton();

        Allure.step("Шаг 2: Проверка отображения экрана цитат");
        assertTrue("BUG: Quotes screen should be displayed after clicking Quotes button",
                quotesPage.isQuotesScreenDisplayed());

        Allure.step("Шаг 3: Проверка отображения списка цитат");
        assertTrue("BUG: Quotes list should be displayed on Quotes screen",
                quotesPage.checkQuotesListIsDisplayed() != null);

        Allure.step("Шаг 4: Разворачивание цитаты на позиции 0");
        quotesPage.expandQuoteAtPosition(0);

        Allure.step("Шаг 5: Проверка отображения описания цитаты");
        assertTrue("BUG: Quote description should be displayed after expanding",
                quotesPage.isQuoteDescriptionDisplayed());

        Allure.step("Шаг 6: Сворачивание цитаты на позиции 0");
        quotesPage.expandQuoteAtPosition(0);

        Allure.step("Шаг 7: Проверка, что описание цитаты скрыто");
        assertTrue("BUG: Quote description should be hidden after collapsing",
                quotesPage.isQuoteDescriptionHidden());

        Allure.step("Шаг 8: Возврат на главный экран через меню");
        navigationDrawer.openMenu().clickMainMenuItem();

        Allure.step("Шаг 9: Проверка отображения главного экрана");
        assertTrue(mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Проверка раздела 'О приложении'")
    @Description("TC-ABOUT-01: Проверка раздела 'О приложении'")
    @Story("Пользователь может просматривать информацию о версии приложения")
    public void testAboutSection() {
        Allure.step("Шаг 1: Открытие бокового меню");
        navigationDrawer.openMenu();

        Allure.step("Шаг 2: Клик по пункту меню 'About'");
        navigationDrawer.clickAboutMenuItem();

        Allure.step("Шаг 3: Проверка отображения экрана 'О приложении'");
        assertTrue("BUG: User should be able to view app version information",
                mainPage.isAboutScreenDisplayed());

        Allure.step("Шаг 4: Нажатие кнопки 'Назад' на экране 'About'");
        navigationDrawer.clickAboutBackButton();

        Allure.step("Шаг 5: Проверка возврата на главный экран");
        assertTrue("BUG: After closing About screen user should return to Main screen",
                mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Выход из системы")
    @Description("TC-LOGOUT-01: Выход из системы")
    @Story("Пользователь может безопасно выйти из приложения")
    public void testLogout() {
        Allure.step("Шаг 1: Выполнение выхода из системы");
        mainPage.logout();

        Allure.step("Шаг 2: Проверка отображения экрана авторизации");
        authPage.isAuthorizationScreenDisplayed();

        Allure.step("Шаг 3: Проверка успешного выхода из системы");
        assertTrue("BUG: User should be able to safely logout from the app",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Очистка полей при возврате с главного экрана")
    @Description("TC-LOGOUT-02: Очистка полей при возврате с главного экрана")
    @Story("Поля авторизации должны очищаться после выхода из системы")
    public void testClearFieldsAfterReturnFromMainScreen() {
        Allure.step("Шаг 1: Выполнение выхода из системы");
        mainPage.logout();

        Allure.step("Шаг 2: Проверка отображения экрана авторизации");
        authPage.isAuthorizationScreenDisplayed();

        Allure.step("Шаг 3: Проверка, что все поля авторизации пусты");
        assertTrue("BUG: Authorization fields should be cleared after logout",
                authPage.areAllFieldsEmpty());
    }
}