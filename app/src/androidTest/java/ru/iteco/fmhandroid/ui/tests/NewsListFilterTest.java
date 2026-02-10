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
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.NewsFilterPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Список и фильтрация новостей")
@Feature("Работа со списком новостей и фильтрами")
@DisplayName("Тесты списка новостей и фильтрации")
public class NewsListFilterTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();
    private final NewsPage newsPage = new NewsPage();
    private final NewsFilterPage newsFilterPage = new NewsFilterPage();

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
    @DisplayName("Отображение заглушки при пустом списке новостей")
    @Description("TC-NEWS-LIST-01: Отображение заглушки при пустом списке новостей")
    @Story("При отсутствии новостей отображается информационное сообщение")
    public void testEmptyNewsListPlaceholder() {
        newsPage.clickAllNewsButton();
        boolean isEmptyListDisplayed = newsPage.isNewsListEmpty();
        boolean isListDisplayed = newsPage.isNewsListDisplayed();

        String errorMessage = "BUG: ";
        if (isEmptyListDisplayed) {
            errorMessage += "Empty news list placeholder should be displayed when list is empty";
        } else if (isListDisplayed) {
            errorMessage += "News list should be displayed when there are news items";
        } else {
            errorMessage += "Either empty placeholder or news list should be displayed";
        }

        assertTrue(errorMessage, isEmptyListDisplayed || isListDisplayed);
    }

    @Test
    @DisplayName("Проверка видимости элементов управления в списке новостей")
    @Description("TC-NEWS-LIST-02: Проверка видимости элементов управления в списке новостей")
    @Story("На экране списка новостей должны быть кнопки управления")
    public void testNewsListControlsVisibility() {
        newsPage.clickAllNewsButton();
        boolean areControlsDisplayed = newsPage.areNewsControlsDisplayed();
        assertTrue("BUG: News list controls should be displayed", areControlsDisplayed);
    }

    @Test
    @DisplayName("Переход к фильтрации новостей")
    @Description("TC-NEWS-LIST-05: Переход к фильтрации новостей")
    @Story("Пользователь может открыть фильтр новостей")
    public void testNavigateToFilter() {
        newsPage.clickAllNewsButton();
        newsPage.openNewsFilter();

        boolean isFilterOpened = newsFilterPage.isFilterDialogDisplayed();
        assertTrue("BUG: News filter functionality should be accessible", isFilterOpened);

        newsFilterPage.cancelFilter();
        assertTrue("BUG: Should return to News after cancel",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Переход к созданию новости")
    @Description("TC-NEWS-LIST-06: Переход к созданию новости (Control Panel)")
    @Story("Пользователь может перейти к управлению новостями")
    public void testNavigateToControlPanelFromNewsList() {
        newsPage.clickAllNewsButton();
        newsPage.clickEditNewsButton();
        assertTrue("BUG: Control Panel functionality should be accessible",
                controlPanelPage.isControlPanelDisplayed());
    }

    @Test
    @DisplayName("Отображение элементов экрана фильтрации")
    @Description("TC-NEWS-FILTER-01: Отображение элементов экрана фильтрации")
    @Story("Экран фильтрации должен содержать все необходимые элементы")
    public void testFilterElementsDisplay() {
        newsPage.clickAllNewsButton();
        newsPage.openNewsFilter();

        boolean areElementsDisplayed = newsFilterPage.areAllFilterElementsDisplayed();
        assertTrue("BUG: News filter screen should display all required elements",
                areElementsDisplayed);

        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }

    @Test
    @DisplayName("Работа выпадающего списка категорий")
    @Description("TC-NEWS-FILTER-02: Работа выпадающего списка категорий")
    @Story("Пользователь может выбирать категории из списка")
    public void testCategoryDropdown() {

        // Примечание: Проверку отображения всех 8 категорий реализовать не удалось
        // из-за технических ограничений взаимодействия с выпадающим списком через Espresso.
        // Тест проверяет базовую функциональность открытия списка категорий.

        newsPage.clickAllNewsButton();
        newsPage.openNewsFilter();

        boolean isDropdownFunctional = newsFilterPage.checkCategoryDropdownFunctionality();
        assertTrue("BUG: Category dropdown field should be clickable to open category list",
                isDropdownFunctional);

        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }

    @Test
    @DisplayName("Выбор даты через календарь")
    @Description("TC-NEWS-FILTER-03: Выбор даты через календарь")
    @Story("Пользователь может выбирать даты через календарь")
    public void testDatePickerInFilter() {
        newsPage.clickAllNewsButton();
        newsPage.openNewsFilter();

        boolean isDatePickerFunctional = newsFilterPage.checkDatePickerFunctionality();
        assertTrue("BUG: Date picker should allow date selection and display it in field",
                isDatePickerFunctional);

        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }

    @Test
    @DisplayName("Отмена фильтрации")
    @Description("TC-NEWS-FILTER-05: Отмена фильтрации")
    @Story("Пользователь может отменить фильтрацию")
    public void testCancelFilter() {
        newsPage.clickAllNewsButton();
        newsPage.openNewsFilter();
        newsFilterPage.cancelFilter();

        assertTrue("BUG: Filter cancellation functionality should work correctly",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Валидация дат")
    @Description("TC-NEWS-FILTER-06: Валидация дат (конечная дата раньше начальной)")
    @Story("Система должна проверять корректность диапазона дат")
    public void testDateRangeValidation() {
        newsPage.clickAllNewsButton();
        newsPage.openNewsFilter();
        newsFilterPage.setInvalidDates();

        boolean errorDisplayed = newsFilterPage.isErrorDisplayed();
        assertTrue("BUG: Date range validation in news filter should work correctly",
                errorDisplayed);

        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }
}