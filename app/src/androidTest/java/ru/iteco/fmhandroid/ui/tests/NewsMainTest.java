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
import ru.iteco.fmhandroid.ui.pages.NewsPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Новости")
@Feature("Работа с новостями на главном экране")
@DisplayName("Тесты новостей на главном экране")
public class NewsMainTest extends BaseTest {

    private final NewsPage newsPage = new NewsPage();

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
    @DisplayName("Переход к списку всех новостей с главного экрана")
    @Description("TC-NEWS-MAIN-01: Проверка перехода к полному списку новостей с главного экрана")
    @Story("Пользователь может перейти к полному списку новостей через кнопку 'ALL NEWS'")
    public void testNavigationFromMainToNewsList() {
        Allure.step("Шаг 1: Проверка отображения главного экрана");
        assertTrue("BUG: Main screen should be displayed",
                mainPage.isMainScreenDisplayed());

        Allure.step("Шаг 2: Проверка отображения блока новостей на главном экране");
        assertTrue("BUG: News block should be displayed on main screen",
                mainPage.checkNewsBlockOnMainIsDisplayed());

        Allure.step("Шаг 3: Проверка отображения кнопки 'ALL NEWS'");
        newsPage.checkAllNewsButtonIsDisplayed();

        Allure.step("Шаг 4: Клик по кнопке 'ALL NEWS'");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 5: Проверка перехода на экран со списком всех новостей");
        assertTrue("BUG: User should be able to navigate to full news list via ALL NEWS button",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Сворачивание/разворачивание блока новостей на главном экране")
    @Description("TC-NEWS-MAIN-02: Проверка функционала сворачивания/разворачивания блока новостей")
    @Story("Пользователь может управлять отображением блока новостей на главном экране")
    public void testCollapseExpandNewsBlockOnMainPage() {
        Allure.step("Шаг 1: Проверка отображения главного экрана");
        assertTrue("BUG: Main screen should be displayed",
                mainPage.isMainScreenDisplayed());

        Allure.step("Шаг 2: Проверка отображения кнопки 'ALL NEWS'");
        assertTrue("BUG: ALL NEWS button should be displayed on main screen",
                newsPage.isAllNewsButtonDisplayed());

        Allure.step("Шаг 3: Проверка отображения кнопки сворачивания/разворачивания");
        assertTrue("BUG: Expand/collapse button should be displayed",
                newsPage.isExpandNewsButtonDisplayed());

        Allure.step("Шаг 4: Сворачивание блока новостей");
        newsPage.clickExpandNewsButton();

        Allure.step("Шаг 5: Проверка, что кнопка 'ALL NEWS' скрыта");
        assertTrue("BUG: ALL NEWS button should be hidden after collapse",
                newsPage.isAllNewsButtonHidden());

        Allure.step("Шаг 6: Разворачивание блока новостей");
        newsPage.clickExpandNewsButton();

        Allure.step("Шаг 7: Проверка, что кнопка 'ALL NEWS' снова отображается");
        assertTrue("BUG: User should be able to control news block display (collapse/expand functionality)",
                newsPage.isAllNewsButtonDisplayed());
    }
}