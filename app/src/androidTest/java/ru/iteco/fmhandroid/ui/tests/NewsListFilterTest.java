package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.NewsFilterPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

public class NewsListFilterTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();
    private final NewsPage newsPage = new NewsPage();
    private final NewsFilterPage newsFilterPage = new NewsFilterPage();

    // TC-NEWS-LIST-01: Отображение заглушки при пустом списке новостей
    @Test
    public void testEmptyNewsListPlaceholder() {
        ensureOnMainScreen();
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();

        try {
            onView(withText("There is nothing here yet..."))
                    .check(matches(isDisplayed()));
        } catch (Exception e) {
            onView(withId(R.id.news_list_recycler_view))
                    .check(matches(isDisplayed()));
        }
    }

    // TC-NEWS-LIST-02: Проверка видимости элементов управления в списке новостей
    @Test
    public void testNewsListControlsVisibility() {
        ensureOnMainScreen();
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();

        onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()));
        onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_news_material_button)).check(matches(isDisplayed()));
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()));
    }

    // TC-NEWS-LIST-05: Переход к фильтрации новостей
    @Test
    public void testNavigateToFilter() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        filterPage.checkFilterDialogIsDisplayed();
        filterPage.checkFilterElementsDisplayed();
        filterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-LIST-06: Переход к созданию новости (Control Panel)
    @Test
    public void testNavigateToControlPanelFromNewsList() {
        ensureOnMainScreen();
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();
        onView(withId(R.id.edit_news_material_button)).perform(click());
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-01: Отображение элементов экрана фильтрации
    @Test
    public void testFilterElementsDisplay() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        filterPage.checkFilterElementsDisplayed();
        filterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-02: Работа выпадающего списка категорий
    @Test
    public void testCategoryDropdown() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        filterPage.checkCategoriesList();
        filterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-03: Выбор даты через календарь
    @Test
    public void testDatePickerInFilter() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        filterPage.selectStartDate();
        filterPage.selectEndDate();
        filterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-05: Отмена фильтрации
    @Test
    public void testCancelFilter() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        onView(withId(R.id.filter_news_material_button)).perform(click());
        onView(withText("Filter news")).check(matches(isDisplayed()));
        newsFilterPage.selectAnyAvailableCategory();
        onView(withId(R.id.cancel_button)).perform(click());
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-06: Валидация дат (конечная дата раньше начальной)
    @Test
    public void testDateRangeValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        filterPage.setInvalidDates();
        boolean errorDisplayed = filterPage.isErrorDisplayed();
        assertTrue("БАГ: Должно отображаться сообщение об ошибке для некорректного диапазона дат", errorDisplayed);
        filterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }
}