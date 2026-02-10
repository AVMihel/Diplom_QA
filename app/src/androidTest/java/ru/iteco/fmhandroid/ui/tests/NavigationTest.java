package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
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
@DisplayName("Тесты навигации в мобильном приложении")
public class NavigationTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final NewsPage newsPage = new NewsPage();
    private final QuotesPage quotesPage = new QuotesPage();

    @Before
    public void setUp() {
        setUpToAuthScreen();
        loginAndGoToMainScreen();
    }

    @After
    public void tearDown() {
        tearDownToAuthScreen();
    }

    @Test
    @DisplayName("Основная навигация через боковое меню с главного экрана")
    @Description("TC-NAV-01: Основная навигация через боковое меню с главного экрана")
    @Story("Пользователь может перемещаться между разделами через боковое меню")
    public void testMainNavigationThroughSideMenu() {
        navigationDrawer.openMenu().checkMenuIsDisplayed();
        assertTrue("BUG: 'Main' menu item should be inactive on Main screen",
                !navigationDrawer.isMainMenuItemActive());
        assertTrue("BUG: 'News' menu item should be active on Main screen",
                navigationDrawer.isNewsMenuItemActive());
        assertTrue("BUG: 'About' menu item should be active on Main screen",
                navigationDrawer.isAboutMenuItemActive());

        navigationDrawer.clickNewsMenuItem();
        assertTrue("BUG: Navigation to News screen via side menu should work correctly",
                mainPage.isNewsScreenDisplayed());

        navigationDrawer.openMenu().clickMainMenuItem();
        assertTrue(mainPage.isMainScreenDisplayed());

        navigationDrawer.openMenu().clickAboutMenuItem();
        assertTrue("BUG: Navigation to About screen via side menu should work correctly",
                mainPage.isAboutScreenDisplayed());

        navigationDrawer.clickAboutBackButton();
        assertTrue(mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Проверка логики активности пунктов бокового меню в разделе 'News'")
    @Description("TC-NAV-02: Проверка логики активности пунктов бокового меню в разделе 'News'")
    @Story("Система правильно отображает активные/неактивные пункты меню")
    public void testMenuLogicInNewsSection() {
        navigationDrawer.openMenu().clickNewsMenuItem();
        assertTrue("BUG: Navigation to News screen via side menu should work correctly",
                mainPage.isNewsScreenDisplayed());

        navigationDrawer.openMenu().checkMenuIsDisplayed();
        assertTrue("BUG: 'Main' menu item should be active on News screen",
                navigationDrawer.isMainMenuItemActive());
        assertTrue("BUG: 'News' menu item should be inactive on News screen",
                !navigationDrawer.isNewsMenuItemActive());
        assertTrue("BUG: 'About' menu item should be active on News screen",
                navigationDrawer.isAboutMenuItemActive());

        navigationDrawer.clickMainMenuItem();
        assertTrue("BUG: Navigation to Main screen from News section should work correctly",
                mainPage.isMainScreenDisplayed());

        navigationDrawer.openMenu().clickNewsMenuItem();
        mainPage.isNewsScreenDisplayed();

        navigationDrawer.openMenu().clickAboutMenuItem();
        assertTrue("BUG: Navigation to About screen from News section should work correctly",
                mainPage.isAboutScreenDisplayed());

        navigationDrawer.clickAboutBackButton();
        mainPage.isNewsScreenDisplayed();

        navigationDrawer.openMenu().clickMainMenuItem();
        assertTrue(mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Навигация через верхнюю панель")
    @Description("TC-NAV-03: Навигация через верхнюю панель")
    @Story("Пользователь может использовать верхнюю панель для навигации")
    public void testNavigationThroughTopPanel() {
        mainPage.clickQuotesButton();
        assertTrue("BUG: Navigation to Quotes screen via top panel should work correctly",
                quotesPage.isQuotesScreenDisplayed());

        mainPage.logout();
        assertTrue("BUG: Navigation to Authorization screen after logout should work correctly",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Проверка логики активности пунктов бокового меню в разделе 'Quotes'")
    @Description("TC-NAV-04: Проверка логики активности пунктов бокового меню в разделе 'Quotes'")
    @Story("Система правильно отображает активные пункты меню в разных разделах")
    public void testMenuLogicInQuotesSection() {
        mainPage.clickQuotesButton();
        assertTrue("BUG: Navigation to Quotes screen via top panel should work correctly",
                quotesPage.isQuotesScreenDisplayed());

        navigationDrawer.openMenu().checkMenuIsDisplayed();
        assertTrue("BUG: 'Main' menu item should be active on Quotes screen",
                navigationDrawer.isMainMenuItemActive());
        assertTrue("BUG: 'News' menu item should be active on Quotes screen",
                navigationDrawer.isNewsMenuItemActive());
        assertTrue("BUG: 'About' menu item should be active on Quotes screen",
                navigationDrawer.isAboutMenuItemActive());

        navigationDrawer.clickMainMenuItem();
        assertTrue("BUG: Navigation to Main screen from Quotes section should work correctly",
                mainPage.isMainScreenDisplayed());

        mainPage.clickQuotesButton();
        quotesPage.isQuotesScreenDisplayed();

        navigationDrawer.openMenu().clickNewsMenuItem();
        assertTrue("BUG: Navigation to News screen from Quotes section should work correctly",
                mainPage.isNewsScreenDisplayed());

        newsPage.clickQuotesButtonOnNewsScreen();
        quotesPage.isQuotesScreenDisplayed();

        navigationDrawer.openMenu().clickAboutMenuItem();
        assertTrue("BUG: Navigation to About screen from Quotes section should work correctly",
                mainPage.isAboutScreenDisplayed());

        navigationDrawer.clickAboutBackButton();
        quotesPage.isQuotesScreenDisplayed();

        navigationDrawer.openMenu().clickMainMenuItem();
        assertTrue(mainPage.isMainScreenDisplayed());
    }
}