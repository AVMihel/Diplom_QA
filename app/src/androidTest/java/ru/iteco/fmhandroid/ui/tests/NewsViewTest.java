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
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.NewsFilterPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Новости")
@Feature("Просмотр и фильтрация новостей")
@DisplayName("Тесты просмотра и фильтрации новостей")
public class NewsViewTest extends BaseTest {

    private NewsPage newsPage;
    private NewsFilterPage newsFilterPage;
    private ControlPanelPage controlPanelPage;

    @Before
    public void setUp() {
        ensureOnMainScreen();
        newsPage = new NewsPage();
        newsFilterPage = new NewsFilterPage();
        controlPanelPage = new ControlPanelPage();
    }

    @Test
    @DisplayName("Переход к списку всех новостей с главного экрана")
    @Description("TC-NEWS-MAIN-01: Проверка перехода к полному списку новостей с главного экрана")
    @Story("Пользователь может перейти к полному списку новостей через кнопку 'ALL NEWS'")
    public void testNavigationFromMainToNewsList() {
        assertTrue("Main screen should be displayed", mainPage.isMainScreenDisplayed());
        assertTrue("News block should be displayed on main screen", mainPage.isNewsBlockDisplayed());

        newsPage.checkAllNewsButtonIsDisplayed()
                .clickAllNewsButton();

        assertTrue("Failed to navigate to full news list via ALL NEWS button",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Сворачивание/разворачивание блока новостей на главном экране")
    @Description("TC-NEWS-MAIN-02: Проверка функционала сворачивания/разворачивания блока новостей")
    @Story("Пользователь может управлять отображением блока новостей на главном экране")
    public void testCollapseExpandNewsBlockOnMainPage() {
        ensureOnMainScreen();

        assertTrue("News block should be displayed", newsPage.isNewsBlockDisplayed());
        assertTrue("ALL NEWS button should be displayed initially",
                newsPage.isAllNewsButtonDisplayed());

        newsPage.clickExpandButton();

        assertTrue("ALL NEWS button should be hidden after collapse",
                newsPage.isAllNewsButtonHidden());

        newsPage.clickExpandButton();

        assertTrue("ALL NEWS button should be displayed again after expand",
                newsPage.isAllNewsButtonDisplayed());
    }

    @Test
    @DisplayName("Проверка видимости элементов управления в списке новостей")
    @Description("TC-NEWS-LIST-02: Проверка видимости элементов управления в списке новостей")
    @Story("На экране списка новостей должны быть кнопки управления")
    public void testNewsListControlsVisibility() {
        newsPage.clickAllNewsButton();
        assertTrue("News list controls should be displayed",
                newsPage.areNewsControlsDisplayed());
    }

    @Test
    @DisplayName("Переход к фильтрации новостей")
    @Description("TC-NEWS-LIST-05: Переход к фильтрации новостей")
    @Story("Пользователь может открыть фильтр новостей")
    public void testNavigateToFilter() {
        newsPage.clickAllNewsButton()
                .openNewsFilter();

        assertTrue("News filter dialog should be displayed",
                newsFilterPage.isFilterDialogDisplayed());

        newsFilterPage.cancelFilter();
        assertTrue("Should return to News after cancel",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Переход в панель управления (Control Panel)")
    @Description("TC-NEWS-LIST-06: Переход к созданию новости (Control Panel)")
    @Story("Пользователь может перейти к управлению новостями")
    public void testNavigateToControlPanelFromNewsList() {
        newsPage.clickAllNewsButton()
                .goToControlPanel();

        assertTrue("Control Panel should be accessible",
                controlPanelPage.isControlPanelDisplayed());
    }

    @Test
    @DisplayName("Отображение элементов экрана фильтрации")
    @Description("TC-NEWS-FILTER-01: Отображение элементов экрана фильтрации")
    @Story("Экран фильтрации должен содержать все необходимые элементы")
    public void testFilterElementsDisplay() {
        newsPage.clickAllNewsButton()
                .openNewsFilter();

        assertTrue("News filter screen should display all required elements",
                newsFilterPage.areAllFilterElementsDisplayed());

        newsFilterPage.cancelFilter();
        assertTrue("Should return to News", newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Работа выпадающего списка категорий")
    @Description("TC-NEWS-FILTER-02: Работа выпадающего списка категорий")
    @Story("Пользователь может выбирать категории из списка")
    public void testCategoryDropdown() {
        newsPage.clickAllNewsButton()
                .openNewsFilter();

        boolean isDropdownFunctional = newsFilterPage.checkCategoryDropdownFunctionality();
        assertTrue("Category dropdown field should be clickable to open category list",
                isDropdownFunctional);

        // Примечание: Проверку отображения всех 8 категорий реализовать не удалось
        // из-за технических ограничений взаимодействия с выпадающим списком через Espresso.
        // Тест проверяет базовую функциональность открытия списка категорий.

        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }

    @Test
    @DisplayName("Выбор даты через календарь")
    @Description("TC-NEWS-FILTER-03: Выбор даты через календарь")
    @Story("Пользователь может выбирать даты через календарь")
    public void testDatePickerInFilter() {
        newsPage.clickAllNewsButton()
                .openNewsFilter();

        assertTrue("Date picker should allow date selection",
                newsFilterPage.checkDatePickerFunctionality());

        newsFilterPage.cancelFilter();
        assertTrue("Should return to News", newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Отмена фильтрации")
    @Description("TC-NEWS-FILTER-05: Отмена фильтрации")
    @Story("Пользователь может отменить фильтрацию")
    public void testCancelFilter() {
        newsPage.clickAllNewsButton()
                .openNewsFilter();

        newsFilterPage.cancelFilter();

        assertTrue("Should return to News after cancel",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Валидация дат: конечная дата раньше начальной")
    @Description("TC-NEWS-FILTER-06: Валидация дат (конечная дата раньше начальной)")
    @Story("Система должна проверять корректность диапазона дат")
    public void testDateRangeValidation() {
        newsPage.clickAllNewsButton()
                .openNewsFilter();

        newsFilterPage.setInvalidDates()
                .applyFilter();

        assertTrue("Date range validation should show error message",
                newsFilterPage.isErrorDisplayed());

        newsFilterPage.cancelFilter();
        assertTrue("Should return to News", newsPage.isSortButtonDisplayed());
    }
}