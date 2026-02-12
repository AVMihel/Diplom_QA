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
    @DisplayName("Основная навигация через боковое меню с главного экрана")
    @Description("TC-NAV-01: Основная навигация через боковое меню с главного экрана")
    @Story("Пользователь может перемещаться между разделами через боковое меню")
    public void testMainNavigationThroughSideMenu() {
        Allure.step("Шаг 1: Открытие меню и проверка его отображения");
        navigationDrawer.openMenu().checkMenuIsDisplayed();

        Allure.step("Шаг 2: Проверка состояния пунктов меню на главном экране");
        assertTrue("BUG: 'Main' menu item should be inactive on 'Main' screen",
                !navigationDrawer.isMainMenuItemActive());
        assertTrue("BUG: 'News' menu item should be active on 'Main' screen",
                navigationDrawer.isNewsMenuItemActive());
        assertTrue("BUG: 'About' menu item should be active on 'Main' screen",
                navigationDrawer.isAboutMenuItemActive());

        Allure.step("Шаг 3: Переход на экран новостей через меню");
        navigationDrawer.clickNewsMenuItem();
        assertTrue("BUG: Navigation to 'News' screen via side menu should work correctly",
                mainPage.isNewsScreenDisplayed());

        Allure.step("Шаг 4: Возврат на главный экран через меню");
        navigationDrawer.openMenu().clickMainMenuItem();
        assertTrue(mainPage.isMainScreenDisplayed());

        Allure.step("Шаг 5: Переход на экран 'About' через меню");
        navigationDrawer.openMenu().clickAboutMenuItem();
        assertTrue("BUG: Navigation to 'About' screen via side menu should work correctly",
                mainPage.isAboutScreenDisplayed());

        Allure.step("Шаг 6: Возврат на главный экран через кнопку 'Назад'");
        navigationDrawer.clickAboutBackButton();
        assertTrue(mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Проверка логики активности пунктов бокового меню в разделе 'News'")
    @Description("TC-NAV-02: Проверка логики активности пунктов бокового меню в разделе 'News'")
    @Story("Система правильно отображает активные/неактивные пункты меню")
    public void testMenuLogicInNewsSection() {
        Allure.step("Шаг 1: Переход на экран новостей");
        navigationDrawer.openMenu().clickNewsMenuItem();
        assertTrue("BUG: Navigation to 'News' screen via side menu should work correctly",
                mainPage.isNewsScreenDisplayed());

        Allure.step("Шаг 2: Проверка состояния пунктов меню на экране 'News'");
        navigationDrawer.openMenu().checkMenuIsDisplayed();
        assertTrue("BUG: 'Main' menu item should be active on 'News' screen",
                navigationDrawer.isMainMenuItemActive());
        assertTrue("BUG: 'News' menu item should be inactive on 'News' screen",
                !navigationDrawer.isNewsMenuItemActive());
        assertTrue("BUG: 'About' menu item should be active on 'News' screen",
                navigationDrawer.isAboutMenuItemActive());

        Allure.step("Шаг 3: Переход на главный экран из раздела 'News'");
        navigationDrawer.clickMainMenuItem();
        assertTrue("BUG: Navigation to 'Main' screen from 'News' section should work correctly",
                mainPage.isMainScreenDisplayed());

        Allure.step("Шаг 4: Возврат на экран 'News'");
        navigationDrawer.openMenu().clickNewsMenuItem();
        mainPage.isNewsScreenDisplayed();

        Allure.step("Шаг 5: Переход на экран 'About' из раздела 'News'");
        navigationDrawer.openMenu().clickAboutMenuItem();
        assertTrue("BUG: Navigation to 'About' screen from 'News' section should work correctly",
                mainPage.isAboutScreenDisplayed());

        Allure.step("Шаг 6: Возврат на экран 'News' через кнопку 'Назад'");
        navigationDrawer.clickAboutBackButton();
        mainPage.isNewsScreenDisplayed();

        Allure.step("Шаг 7: Возврат на главный экран");
        navigationDrawer.openMenu().clickMainMenuItem();
        assertTrue(mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Навигация через верхнюю панель")
    @Description("TC-NAV-03: Навигация через верхнюю панель")
    @Story("Пользователь может использовать верхнюю панель для навигации")
    public void testNavigationThroughTopPanel() {
        Allure.step("Шаг 1: Переход на экран цитат через верхнюю панель");
        mainPage.clickQuotesButton();
        assertTrue("BUG: Navigation to 'Quotes' screen via top panel should work correctly",
                quotesPage.isQuotesScreenDisplayed());

        Allure.step("Шаг 2: Выход из системы");
        mainPage.logout();
        assertTrue("BUG: Navigation to Authorization screen after logout should work correctly",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Проверка логики активности пунктов бокового меню в разделе 'Quotes'")
    @Description("TC-NAV-04: Проверка логики активности пунктов бокового меню в разделе 'Quotes'")
    @Story("Система правильно отображает активные пункты меню в разных разделах")
    public void testMenuLogicInQuotesSection() {
        Allure.step("Шаг 1: Переход на экран цитат");
        mainPage.clickQuotesButton();
        assertTrue("BUG: Navigation to 'Quotes' screen via top panel should work correctly",
                quotesPage.isQuotesScreenDisplayed());

        Allure.step("Шаг 2: Проверка состояния пунктов меню на экране 'Quotes'");
        navigationDrawer.openMenu().checkMenuIsDisplayed();
        assertTrue("BUG: 'Main' menu item should be active on 'Quotes' screen",
                navigationDrawer.isMainMenuItemActive());
        assertTrue("BUG: 'News' menu item should be active on 'Quotes' screen",
                navigationDrawer.isNewsMenuItemActive());
        assertTrue("BUG: 'About' menu item should be active on 'Quotes' screen",
                navigationDrawer.isAboutMenuItemActive());

        Allure.step("Шаг 3: Переход на главный экран из раздела 'Quotes'");
        navigationDrawer.clickMainMenuItem();
        assertTrue("BUG: Navigation to 'Main' screen from 'Quotes' section should work correctly",
                mainPage.isMainScreenDisplayed());

        Allure.step("Шаг 4: Возврат на экран 'Quotes'");
        mainPage.clickQuotesButton();
        quotesPage.isQuotesScreenDisplayed();

        Allure.step("Шаг 5: Переход на экран 'News' из раздела 'Quotes'");
        navigationDrawer.openMenu().clickNewsMenuItem();
        assertTrue("BUG: Navigation to 'News' screen from 'Quotes' section should work correctly",
                mainPage.isNewsScreenDisplayed());

        Allure.step("Шаг 6: Переход на экран 'Quotes' из раздела 'News'");
        newsPage.clickQuotesButtonOnNewsScreen();
        quotesPage.isQuotesScreenDisplayed();

        Allure.step("Шаг 7: Переход на экран 'About' из раздела 'Quotes'");
        navigationDrawer.openMenu().clickAboutMenuItem();
        assertTrue("BUG: Navigation to 'About' screen from Quotes section should work correctly",
                mainPage.isAboutScreenDisplayed());

        Allure.step("Шаг 8: Возврат на экран 'Quotes' через кнопку 'Назад'");
        navigationDrawer.clickAboutBackButton();
        quotesPage.isQuotesScreenDisplayed();

        Allure.step("Шаг 9: Возврат на главный экран");
        navigationDrawer.openMenu().clickMainMenuItem();
        assertTrue(mainPage.isMainScreenDisplayed());
    }
}