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
import ru.iteco.fmhandroid.ui.pages.NewsPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Новости")
@Feature("Работа с новостями на главном экране")
@DisplayName("Тесты новостей на главном экране")
public class NewsMainTest extends BaseTest {

    private final NewsPage newsPage = new NewsPage();

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
    @DisplayName("Переход к списку всех новостей с главного экрана")
    @Description("TC-NEWS-MAIN-01: Проверка перехода к полному списку новостей с главного экрана")
    @Story("Пользователь может перейти к полному списку новостей через кнопку 'ALL NEWS'")
    public void testNavigationFromMainToNewsList() {
        mainPage.isMainScreenDisplayed();
        mainPage.checkNewsBlockOnMainIsDisplayed();
        newsPage.checkAllNewsButtonIsDisplayed();
        newsPage.clickAllNewsButton();

        assertTrue("BUG: User should be able to navigate to full news list via ALL NEWS button",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Сворачивание/разворачивание блока новостей на главном экране")
    @Description("TC-NEWS-MAIN-02: Проверка функционала сворачивания/разворачивания блока новостей")
    @Story("Пользователь может управлять отображением блока новостей на главном экране")
    public void testCollapseExpandNewsBlockOnMainPage() {
        mainPage.isMainScreenDisplayed();
        newsPage.isAllNewsButtonDisplayed();
        newsPage.isExpandNewsButtonDisplayed();
        newsPage.clickExpandNewsButton();
        newsPage.isAllNewsButtonHidden();
        newsPage.clickExpandNewsButton();

        assertTrue("BUG: User should be able to control news block display (collapse/expand functionality)",
                newsPage.isAllNewsButtonDisplayed());
    }
}