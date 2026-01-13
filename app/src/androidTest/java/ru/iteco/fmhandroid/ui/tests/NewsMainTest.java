package ru.iteco.fmhandroid.ui.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

// Тестовый класс для проверки блока новостей на главной странице
public class NewsMainTest extends BaseTest {

    private final NewsPage newsPage = new NewsPage();

    @Before
    public void setUpTest() {
        // Выполняем авторизацию перед каждым тестом
        performLoginAndGoToMainScreen();

        // Дополнительная проверка - убедимся, что мы на главной странице
        mainPage.checkMainScreenIsDisplayed();
        mainPage.checkNewsBlockIsDisplayed();
    }

    @After
    public void tearDownTest() {
        // Выходим из системы после каждого теста для чистого состояния
        try {
            if (mainPage.isMainScreenDisplayed()) {
                mainPage.forceLogout();
                authPage.checkAuthorizationScreenIsDisplayed();
            }
        } catch (Exception e) {
            // Игнорируем ошибки выхода
        }
    }

    // TC-NEWS-MAIN-01: Навигация с главного экрана к списку новостей
    @Test
    public void testNavigationFromMainToNewsList() {
        // 1. Проверяем, что кнопка "ALL NEWS" отображается на главной странице
        newsPage.checkAllNewsButtonIsDisplayed();

        // 2. Нажимаем кнопку "ALL NEWS" для перехода к полному списку новостей
        newsPage.clickAllNewsButton();

        // 3. Проверяем переход на экран списка новостей (по наличию кнопки сортировки)
        newsPage.checkNewsListScreenIsDisplayed();
    }

    // TC-NEWS-MAIN-02: Сворачивание/разворачивание блока "News" на главной
    @Test
    public void testCollapseExpandNewsBlockOnMainPage() {
        // 1. Проверяем, что кнопка "ALL NEWS" и кнопка сворачивания отображаются
        newsPage.checkAllNewsButtonIsDisplayed()
                .checkExpandNewsButtonIsDisplayed();

        // 2. Нажимаем кнопку сворачивания (стрелка) - блок сворачивается
        newsPage.clickExpandNewsButton();

        // 3. Проверяем, что кнопка "ALL NEWS" скрылась после сворачивания
        newsPage.checkAllNewsButtonIsHidden();

        // 4. Снова нажимаем кнопку сворачивания (стрелка) - блок разворачивается
        newsPage.clickExpandNewsButton();

        // 5. Проверяем, что кнопка "ALL NEWS" снова отображается после разворачивания
        newsPage.checkAllNewsButtonIsVisible();
    }
}
