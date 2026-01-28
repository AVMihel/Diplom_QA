package ru.iteco.fmhandroid.ui.tests;

import org.junit.Before;
import org.junit.Test;

import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

public class NewsMainTest extends BaseTest {

    private final NewsPage newsPage = new NewsPage();

    @Before
    public void setUpNewsTests() {
        ensureOnMainScreen();
    }

    // TC-NEWS-MAIN-01: Переход к списку всех новостей с главного экрана
    @Test
    public void testNavigationFromMainToNewsList() {
        mainPage.checkNewsBlockOnMainIsDisplayed();
        newsPage.checkAllNewsButtonIsDisplayed();
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();
    }

    // TC-NEWS-MAIN-02: Сворачивание/разворачивание блока новостей на главном экране
    @Test
    public void testCollapseExpandNewsBlockOnMainPage() {
        mainPage.checkNewsBlockOnMainIsDisplayed();
        newsPage.checkAllNewsButtonIsDisplayed()
                .checkExpandNewsButtonIsDisplayed();
        newsPage.clickExpandNewsButton();
        newsPage.checkAllNewsButtonIsHidden();
        newsPage.clickExpandNewsButton();
        newsPage.checkAllNewsButtonIsVisible();
    }
}