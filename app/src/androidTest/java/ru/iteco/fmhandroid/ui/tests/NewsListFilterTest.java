package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    @DisplayName("Отображение заглушки при пустом списке новостей")
    @Description("TC-NEWS-LIST-01: Отображение заглушки при пустом списке новостей")
    @Story("При отсутствии новостей отображается информационное сообщение")
    public void testEmptyNewsListPlaceholder() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Проверка, что список новостей пуст");
        boolean isNewsListEmpty = newsPage.isNewsListEmpty();
        boolean isNewsListDisplayed = newsPage.isNewsListDisplayed();

        if (isNewsListDisplayed) {
            fail("Test requires empty news list, but there are news items in the system. " +
                    "Please clear the news list before running this test.");
        }

        Allure.step("Шаг 3: Проверка отображения заглушки для пустого списка");
        assertTrue("BUG: Empty news list placeholder 'There is nothing here yet...' should be displayed",
                isNewsListEmpty);
    }

    @Test
    @DisplayName("Проверка видимости элементов управления в списке новостей")
    @Description("TC-NEWS-LIST-02: Проверка видимости элементов управления в списке новостей")
    @Story("На экране списка новостей должны быть кнопки управления")
    public void testNewsListControlsVisibility() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Проверка отображения элементов управления");
        boolean areControlsDisplayed = newsPage.areNewsControlsDisplayed();
        assertTrue("BUG: News list controls should be displayed", areControlsDisplayed);
    }

    @Test
    @DisplayName("Переход к фильтрации новостей")
    @Description("TC-NEWS-LIST-05: Переход к фильтрации новостей")
    @Story("Пользователь может открыть фильтр новостей")
    public void testNavigateToFilter() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Открытие фильтра новостей");
        newsPage.openNewsFilter();

        Allure.step("Шаг 3: Проверка отображения диалога фильтрации");
        boolean isFilterOpened = newsFilterPage.isFilterDialogDisplayed();
        assertTrue("BUG: News filter functionality should be accessible", isFilterOpened);

        Allure.step("Шаг 4: Отмена фильтрации");
        newsFilterPage.cancelFilter();

        Allure.step("Шаг 5: Проверка возврата к списку новостей");
        assertTrue("BUG: Should return to News after cancel",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Переход к созданию новости")
    @Description("TC-NEWS-LIST-06: Переход к созданию новости (Control Panel)")
    @Story("Пользователь может перейти к управлению новостями")
    public void testNavigateToControlPanelFromNewsList() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Переход в Control Panel");
        newsPage.clickEditNewsButton();

        Allure.step("Шаг 3: Проверка отображения Control Panel");
        assertTrue("BUG: Control Panel functionality should be accessible",
                controlPanelPage.isControlPanelDisplayed());
    }

    @Test
    @DisplayName("Отображение элементов экрана фильтрации")
    @Description("TC-NEWS-FILTER-01: Отображение элементов экрана фильтрации")
    @Story("Экран фильтрации должен содержать все необходимые элементы")
    public void testFilterElementsDisplay() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Открытие фильтра новостей");
        newsPage.openNewsFilter();

        Allure.step("Шаг 3: Проверка отображения всех элементов фильтра");
        boolean areElementsDisplayed = newsFilterPage.areAllFilterElementsDisplayed();
        assertTrue("BUG: News filter screen should display all required elements",
                areElementsDisplayed);

        Allure.step("Шаг 4: Отмена фильтрации");
        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }

    @Test
    @DisplayName("Работа выпадающего списка категорий")
    @Description("TC-NEWS-FILTER-02: Работа выпадающего списка категорий")
    @Story("Пользователь может выбирать категории из списка")
    public void testCategoryDropdown() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Открытие фильтра новостей");

        // Примечание: Проверку отображения всех 8 категорий реализовать не удалось
        // из-за технических ограничений взаимодействия с выпадающим списком через Espresso.
        // Тест проверяет базовую функциональность открытия списка категорий.

        newsPage.openNewsFilter();

        Allure.step("Шаг 3: Проверка функциональности выпадающего списка категорий");
        boolean isDropdownFunctional = newsFilterPage.checkCategoryDropdownFunctionality();
        assertTrue("BUG: Category dropdown field should be clickable to open category list",
                isDropdownFunctional);

        Allure.step("Шаг 4: Отмена фильтрации");
        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }

    @Test
    @DisplayName("Выбор даты через календарь")
    @Description("TC-NEWS-FILTER-03: Выбор даты через календарь")
    @Story("Пользователь может выбирать даты через календарь")
    public void testDatePickerInFilter() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Открытие фильтра новостей");
        newsPage.openNewsFilter();

        Allure.step("Шаг 3: Проверка функциональности выбора даты");
        boolean isDatePickerFunctional = newsFilterPage.checkDatePickerFunctionality();
        assertTrue("BUG: Date picker should allow date selection and display it in field",
                isDatePickerFunctional);

        Allure.step("Шаг 4: Отмена фильтрации");
        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }

    @Test
    @DisplayName("Отмена фильтрации")
    @Description("TC-NEWS-FILTER-05: Отмена фильтрации")
    @Story("Пользователь может отменить фильтрацию")
    public void testCancelFilter() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Открытие фильтра новостей");
        newsPage.openNewsFilter();

        Allure.step("Шаг 3: Отмена фильтрации");
        newsFilterPage.cancelFilter();

        Allure.step("Шаг 4: Проверка возврата к списку новостей");
        assertTrue("BUG: Filter cancellation functionality should work correctly",
                newsPage.isSortButtonDisplayed());
    }

    @Test
    @DisplayName("Валидация дат")
    @Description("TC-NEWS-FILTER-06: Валидация дат (конечная дата раньше начальной)")
    @Story("Система должна проверять корректность диапазона дат")
    public void testDateRangeValidation() {
        Allure.step("Шаг 1: Переход к списку всех новостей");
        newsPage.clickAllNewsButton();

        Allure.step("Шаг 2: Открытие фильтра новостей");
        newsPage.openNewsFilter();

        Allure.step("Шаг 3: Установка невалидных дат (начало позже окончания)");
        newsFilterPage.setInvalidDates();

        Allure.step("Шаг 4: Проверка отображения ошибки валидации");
        boolean errorDisplayed = newsFilterPage.isErrorDisplayed();
        assertTrue("BUG: Date range validation in news filter should work correctly", errorDisplayed);

        Allure.step("Шаг 5: Отмена фильтрации");
        newsFilterPage.cancelFilter();
        newsPage.isSortButtonDisplayed();
    }
}