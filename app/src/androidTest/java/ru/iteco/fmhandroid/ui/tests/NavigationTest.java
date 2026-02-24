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
import ru.iteco.fmhandroid.ui.pages.NewsPage;
import ru.iteco.fmhandroid.ui.pages.QuotesPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Навигация")
@Feature("Навигация по приложению")
@DisplayName("Тесты навигации")
public class NavigationTest extends BaseTest {

    private NavigationDrawerPage navigationDrawer;
    private NewsPage newsPage;
    private QuotesPage quotesPage;

    @Before
    public void setUp() {
        ensureOnMainScreen();
        navigationDrawer = new NavigationDrawerPage();
        newsPage = new NewsPage();
        quotesPage = new QuotesPage();
    }

    @Test
    @DisplayName("Основная навигация через боковое меню с главного экрана")
    @Description("TC-NAV-01: Проверка переходов через боковое меню и состояний пунктов")
    @Story("Пользователь может перемещаться между разделами через боковое меню")
    public void testMainNavigationThroughSideMenu() {
        navigationDrawer.openMenu()
                .checkMenuIsDisplayed()
                .checkMainMenuItemIsInactive()
                .checkNewsMenuItemIsActive()
                .checkAboutMenuItemIsActive()
                .clickNewsMenuItem();

        assertTrue("Failed to navigate to News section", mainPage.isNewsScreenDisplayed());

        navigationDrawer.openMenu()
                .clickMainMenuItem();
        assertTrue("Failed to return to Main section", mainPage.isMainScreenDisplayed());

        navigationDrawer.openMenu()
                .clickAboutMenuItem();
        assertTrue("Failed to navigate to About section", mainPage.isAboutScreenDisplayed());

        navigationDrawer.clickAboutBackButton();
        assertTrue("Failed to return to Main via back button", mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Проверка логики активности пунктов бокового меню в разделе 'News'")
    @Description("TC-NAV-02: Проверка состояний пунктов меню на экране News (баг)")
    @Story("Система правильно отображает активные/неактивные пункты меню")
    public void testMenuLogicInNewsSection() {
        navigationDrawer.openMenu()
                .clickNewsMenuItem();
        assertTrue("Failed to navigate to News section", mainPage.isNewsScreenDisplayed());

        navigationDrawer.openMenu()
                .checkMenuIsDisplayed()
                .checkMainMenuItemIsActive()
                .checkNewsMenuItemIsInactive()
                .checkAboutMenuItemIsActive();

        navigationDrawer.clickMainMenuItem();
        assertTrue("Failed to navigate to Main from News", mainPage.isMainScreenDisplayed());

        navigationDrawer.openMenu()
                .clickNewsMenuItem();
        assertTrue("Failed to return to News", mainPage.isNewsScreenDisplayed());

        navigationDrawer.openMenu()
                .clickAboutMenuItem();
        assertTrue("Failed to navigate to About from News", mainPage.isAboutScreenDisplayed());

        navigationDrawer.clickAboutBackButton();
        assertTrue("Failed to return to News via back button", mainPage.isNewsScreenDisplayed());

        navigationDrawer.openMenu()
                .clickMainMenuItem();
        assertTrue("Failed to return to Main", mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Навигация через верхнюю панель")
    @Description("TC-NAV-03: Проверка кнопок на верхней панели (Quotes, Logout)")
    @Story("Пользователь может использовать верхнюю панель для навигации")
    public void testNavigationThroughTopPanel() {
        mainPage.clickQuotesButton();
        assertTrue("Failed to navigate to Quotes section", quotesPage.isQuotesScreenDisplayed());

        mainPage.logout();
        assertTrue("Failed to logout to authorization screen", authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Проверка логики активности пунктов бокового меню в разделе 'Quotes'")
    @Description("TC-NAV-04: Проверка состояний пунктов меню на экране Quotes")
    @Story("Система правильно отображает активные пункты меню в разных разделах")
    public void testMenuLogicInQuotesSection() {
        mainPage.clickQuotesButton();
        assertTrue("Failed to navigate to Quotes section", quotesPage.isQuotesScreenDisplayed());

        navigationDrawer.openMenu()
                .checkMenuIsDisplayed()
                .checkMainMenuItemIsActive()
                .checkNewsMenuItemIsActive()
                .checkAboutMenuItemIsActive();

        navigationDrawer.clickMainMenuItem();
        assertTrue("Failed to navigate to Main from Quotes", mainPage.isMainScreenDisplayed());

        mainPage.clickQuotesButton();
        assertTrue("Failed to return to Quotes", quotesPage.isQuotesScreenDisplayed());

        navigationDrawer.openMenu()
                .clickNewsMenuItem();
        assertTrue("Failed to navigate to News from Quotes", mainPage.isNewsScreenDisplayed());

        newsPage.clickQuotesButton();
        assertTrue("Failed to return to Quotes from News", quotesPage.isQuotesScreenDisplayed());

        navigationDrawer.openMenu()
                .clickAboutMenuItem();
        assertTrue("Failed to navigate to About from Quotes", mainPage.isAboutScreenDisplayed());

        navigationDrawer.clickAboutBackButton();
        assertTrue("Failed to return to Quotes via back button", quotesPage.isQuotesScreenDisplayed());

        navigationDrawer.openMenu()
                .clickMainMenuItem();
        assertTrue("Failed to return to Main", mainPage.isMainScreenDisplayed());
    }
}