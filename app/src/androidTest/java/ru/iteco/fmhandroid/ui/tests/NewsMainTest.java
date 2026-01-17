package ru.iteco.fmhandroid.ui.tests;

import org.junit.Test;

import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

public class NewsMainTest extends BaseTest {

    private final NewsPage newsPage = new NewsPage();

    // TC-NEWS-MAIN-01: Навигация с главного экрана к списку новостей
    @Test
    public void testNavigationFromMainToNewsList() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Проверяем блок новостей на главной
        mainPage.checkNewsBlockOnMainIsDisplayed();

        // 3. Проверяем кнопку "ALL NEWS"
        newsPage.checkAllNewsButtonIsDisplayed();

        // 4. Нажимаем кнопку "ALL NEWS"
        newsPage.clickAllNewsButton();

        // 5. Проверяем переход к списку новостей
        newsPage.checkNewsListScreenIsDisplayed();
    }

    // TC-NEWS-MAIN-02: Сворачивание/разворачивание блока "News" на главной
    @Test
    public void testCollapseExpandNewsBlockOnMainPage() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Проверяем блок новостей на главной
        mainPage.checkNewsBlockOnMainIsDisplayed();

        // 3. Проверяем элементы управления блоком News
        newsPage.checkAllNewsButtonIsDisplayed()
                .checkExpandNewsButtonIsDisplayed();

        // 4. Сворачиваем блок новостей
        newsPage.clickExpandNewsButton();

        // 5. Проверяем, что кнопка "ALL NEWS" скрыта
        newsPage.checkAllNewsButtonIsHidden();

        // 6. Разворачиваем блок новостей
        newsPage.clickExpandNewsButton();

        // 7. Проверяем, что кнопка "ALL NEWS" отображается
        newsPage.checkAllNewsButtonIsVisible();
    }
}