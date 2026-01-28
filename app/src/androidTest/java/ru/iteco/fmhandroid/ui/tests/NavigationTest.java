package ru.iteco.fmhandroid.ui.tests;

import org.junit.Before;
import org.junit.Test;

import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;

public class NavigationTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();

    @Before
    public void setUpNavigation() {
        ensureOnMainScreen();
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-NAV-01: Основная навигация через боковое меню с главного экрана
    @Test
    public void testMainNavigationThroughSideMenu() {
        navigationDrawer.openMenu()
                .checkMainMenuItemIsActive()
                .checkNewsMenuItemIsActive()
                .checkAboutMenuItemIsActive();

        navigationDrawer.clickNewsMenuItem();
        mainPage.checkNewsScreenIsDisplayed();

        navigationDrawer.openMenu().clickMainMenuItem();
        mainPage.checkMainScreenIsDisplayed();

        navigationDrawer.openMenu().clickAboutMenuItem();
        mainPage.checkAboutScreenIsDisplayed();

        navigationDrawer.clickAboutBackButton();
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-NAV-02: Проверка логики активности пунктов бокового меню в разделе "News"
    @Test
    public void testMenuLogicInNewsSection() {
        navigationDrawer.openMenu().clickNewsMenuItem();
        mainPage.checkNewsScreenIsDisplayed();

        navigationDrawer.openMenu()
                .checkMainMenuItemIsActive();

        StringBuilder bugs = new StringBuilder();

        try {
            navigationDrawer.checkNewsMenuItemIsInactive();
        } catch (AssertionError e) {
            bugs.append("Пункт 'News' должен быть неактивен на этом экране. ");
        }

        try {
            navigationDrawer.checkAboutMenuItemIsActive();
        } catch (AssertionError e) {
            bugs.append("Пункт 'About' должен быть активен. ");
        }

        if (bugs.length() > 0) {
            throw new AssertionError("БАГ: " + bugs.toString().trim());
        }
    }

    // TC-NAV-03: Навигация через верхнюю панель
    @Test
    public void testNavigationThroughTopPanel() {
        mainPage.clickQuotesButton();
        mainPage.checkQuotesScreenIsDisplayed();

        navigationDrawer.openMenu()
                .checkMainMenuItemIsActive()
                .checkNewsMenuItemIsActive()
                .checkAboutMenuItemIsActive()
                .clickMainMenuItem();

        mainPage.checkMainScreenIsDisplayed();
        mainPage.logout();
        authPage.checkAuthorizationScreenIsDisplayed();
    }

    // TC-NAV-04: Проверка логики активности пунктов бокового меню в разделе "Quotes"
    @Test
    public void testMenuLogicInQuotesSection() {
        mainPage.clickQuotesButton();
        mainPage.checkQuotesScreenIsDisplayed();

        navigationDrawer.openMenu()
                .checkMenuIsDisplayed()
                .checkMainMenuItemIsActive()
                .checkNewsMenuItemIsActive()
                .checkAboutMenuItemIsActive()
                .clickMainMenuItem();

        mainPage.checkMainScreenIsDisplayed();
    }
}