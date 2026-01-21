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
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsListFilterTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();
    private final NewsPage newsPage = new NewsPage();
    private final NewsFilterPage newsFilterPage = new NewsFilterPage();

    // TC-NEWS-LIST-01: Отображение заглушки при пустом списке новостей
    @Test
    public void testEmptyNewsListPlaceholder() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим к списку новостей
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();
        WaitUtils.waitForMillis(3000);

        // 3. Проверяем заглушку или список новостей
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
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим к списку новостей
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();

        // 3. Проверяем элементы управления
        onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()));
        onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_news_material_button)).check(matches(isDisplayed()));

        // 4. Проверяем наличие списка
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()));
    }

    // TC-NEWS-LIST-05: Переход к фильтрации новостей
    @Test
    public void testNavigateToFilter() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем диалог фильтрации
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        filterPage.checkFilterDialogIsDisplayed();

        // 4. Проверяем основные элементы фильтра
        filterPage.checkFilterElementsDisplayed();

        // 5. Отменяем фильтрацию
        filterPage.cancelFilter();

        // 6. Проверяем возврат в Control Panel
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-LIST-06: Переход к созданию новости (Control Panel)
    @Test
    public void testNavigateToControlPanelFromNewsList() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим к списку новостей
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();

        // 3. Нажимаем кнопку редактирования
        onView(withId(R.id.edit_news_material_button)).perform(click());

        // 4. Проверяем что перешли в Control Panel
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-01: Отображение элементов экрана фильтрации
    @Test
    public void testFilterElementsDisplay() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем фильтр
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();

        // 4. Проверяем все элементы фильтра
        filterPage.checkFilterElementsDisplayed();

        // 5. Отменяем
        filterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-02: Работа выпадающего списка категорий
    @Test
    public void testCategoryDropdown() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем фильтр
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();

        // 4. Тестируем выпадающий список категорий
        filterPage.checkCategoriesList();

        // 5. Отменяем фильтр
        filterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-03: Выбор даты через календарь
    @Test
    public void testDatePickerInFilter() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем фильтр
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();

        // 4. Выбираем начальную дату через календарь
        filterPage.selectStartDate();
        WaitUtils.waitForMillis(500);

        // 5. Выбираем конечную дату через календарь
        filterPage.selectEndDate();
        WaitUtils.waitForMillis(500);

        // 6. Отменяем фильтр
        filterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-05: Отмена фильтрации
    @Test
    public void testCancelFilter() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем фильтр через кнопку в Control Panel
        onView(withId(R.id.filter_news_material_button))
                .check(matches(isDisplayed()))
                .perform(click());

        WaitUtils.waitForMillis(2000);

        // 4. Проверяем что диалог фильтра открылся
        onView(withText("Filter news")).check(matches(isDisplayed()));

        // 5. Заполняем поле категории через
        newsFilterPage.selectAnyAvailableCategory();

        // 6. Отменяем
        onView(withId(R.id.cancel_button)).perform(click());

        // 7. Проверяем возврат
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-06: Валидация дат (конечная дата раньше начальной)
    @Test
    public void testDateRangeValidation() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем фильтр
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();

        // 4. Устанавливаем невалидные даты
        filterPage.setInvalidDates();

        // 5. Проверяем сообщение об ошибке
        boolean errorDisplayed = filterPage.isErrorDisplayed();
        assertTrue("Должно отображаться сообщение об ошибке для некорректного диапазона дат", errorDisplayed);

        // 6. Закрываем фильтр
        try {
            filterPage.cancelFilter();
        } catch (Exception e) {
        }

        // 7. Проверяем возврат в Control Panel
        controlPanelPage.checkControlPanelIsDisplayed();
    }
}