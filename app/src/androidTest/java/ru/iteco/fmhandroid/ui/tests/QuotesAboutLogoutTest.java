package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
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

    private NavigationDrawerPage navigationDrawer;
    private QuotesPage quotesPage;

    @Before
    public void setUp() {
        ensureOnMainScreen();
        navigationDrawer = new NavigationDrawerPage();
        quotesPage = new QuotesPage();
    }

    @Test
    @DisplayName("Работа с цитатами")
    @Description("TC-QUOTES-01: Работа с цитатами")
    @Story("Пользователь может просматривать и управлять отображением цитат")
    public void testQuotesFunctionality() {
        mainPage.clickQuotesButton();
        assertTrue("Failed to navigate to Quotes section", quotesPage.isQuotesScreenDisplayed());

        quotesPage.checkQuotesListIsDisplayed();

        quotesPage.expandQuoteAtPosition(0);
        assertTrue("Quote description should be displayed after expanding",
                quotesPage.isQuoteDescriptionDisplayed());

        quotesPage.collapseQuoteAtPosition(0);
        assertTrue("Quote description should be hidden after collapsing",
                quotesPage.isQuoteDescriptionHidden());

        navigationDrawer.openMenu().clickMainMenuItem();
        assertTrue("Failed to return to Main screen", mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Проверка раздела 'О приложении'")
    @Description("TC-ABOUT-01: Проверка раздела 'О приложении'")
    @Story("Пользователь может просматривать информацию о версии приложения")
    public void testAboutSection() {
        navigationDrawer.openMenu()
                .clickAboutMenuItem();

        assertTrue("About screen should be displayed", mainPage.isAboutScreenDisplayed());

        navigationDrawer.clickAboutBackButton();
        assertTrue("Should return to Main screen after closing About", mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Выход из системы")
    @Description("TC-LOGOUT-01: Выход из системы")
    @Story("Пользователь может безопасно выйти из приложения")
    public void testLogout() {
        mainPage.logout();
        assertTrue("Should navigate to authorization screen after logout",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Очистка полей при возврате с главного экрана")
    @Description("TC-LOGOUT-02: Очистка полей при возврате с главного экрана")
    @Story("Поля авторизации должны очищаться после выхода из системы")
    public void testClearFieldsAfterReturnFromMainScreen() {
        mainPage.logout();
        assertTrue("Should navigate to authorization screen after logout",
                authPage.isAuthorizationScreenDisplayed());

        assertTrue("Authorization fields should be empty after logout",
                authPage.areAllFieldsEmpty());
    }
}