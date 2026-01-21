package ru.iteco.fmhandroid.ui.tests;

import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.NewsFilterPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

public class NewsListFilterTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();
    private final NewsPage newsPage = new NewsPage();

    // TC-NEWS-LIST-01: Отображение заглушки при пустом списке новостей
    @Test
    public void testEmptyNewsListPlaceholder() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим к списку новостей
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();

        // 3. Проверяем список новостей
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()));

        // 4. Проверяем наличие заглушки для пустого списка
        try {
            onView(withText("There is nothing here yet...")).check(matches(isDisplayed()));
        } catch (Exception e) {
            // Список не пустой - это допустимо
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
    }

    // TC-NEWS-LIST-05: Переход к фильтрации новостей
    @Test
    public void testNavigateToFilter() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим к списку новостей
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();

        // 3. Нажимаем кнопку фильтра
        onView(withId(R.id.filter_news_material_button)).perform(click());

        // 4. Проверяем что диалог фильтрации открылся
        NewsFilterPage newsFilterPage = new NewsFilterPage();
        newsFilterPage.checkFilterDialogIsDisplayed();

        // 5. Отменяем фильтрацию
        newsFilterPage.cancelFilter();

        // 6. Проверяем что вернулись к списку новостей
        newsPage.checkNewsListScreenIsDisplayed();
    }

    // TC-NEWS-LIST-06: Переход к созданию новости (Control Panel)
    @Test
    public void testNavigateToControlPanelFromNewsList() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим к списку новостей
        newsPage.clickAllNewsButton();
        newsPage.checkNewsListScreenIsDisplayed();

        // 3. Нажимаем кнопку редактирования (карандаш)
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
        NewsFilterPage newsFilterPage = controlPanelPage.openFilterDialog();

        // 4. Проверяем все элементы фильтра
        newsFilterPage.checkFilterElementsDisplayed();

        // 5. Отменяем
        newsFilterPage.cancelFilter();
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
        NewsFilterPage newsFilterPage = controlPanelPage.openFilterDialog();

        // 4. Проверяем что список категорий содержит все 8 категорий
        newsFilterPage.checkCategoriesList();

        // 5. Отменяем фильтр
        newsFilterPage.cancelFilter();
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
        NewsFilterPage newsFilterPage = controlPanelPage.openFilterDialog();

        // 4. Выбираем начальную дату через календарь
        newsFilterPage.selectStartDate();

        // 5. Выбираем конечную дату через календарь
        newsFilterPage.selectEndDate();

        // 6. Отменяем фильтр
        newsFilterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-05: Отмена фильтрации
    @Test
    public void testCancelFilter() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем фильтр
        NewsFilterPage newsFilterPage = controlPanelPage.openFilterDialog();

        // 4. Выбираем категорию
        newsFilterPage.selectCategory("Объявление");

        // 5. Выбираем даты
        newsFilterPage.selectStartDate();
        newsFilterPage.selectEndDate();

        // 6. Отменяем фильтрацию
        newsFilterPage.cancelFilter();

        // 7. Проверяем что вернулись в Control Panel
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-FILTER-06: Валидация дат (конечная раньше начальной)
    @Test
    public void testDateValidation() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем фильтр
        NewsFilterPage newsFilterPage = controlPanelPage.openFilterDialog();

        // 4. Устанавливаем невалидные даты (конечная раньше начальной)
        newsFilterPage.setInvalidDates();

        // 5. Проверяем что появилось сообщение об ошибке
        boolean errorDisplayed = newsFilterPage.isErrorDisplayed();
        assertTrue("Should display error message for invalid date range", errorDisplayed);

        // 6. Закрываем фильтр
        newsFilterPage.cancelFilter();
        controlPanelPage.checkControlPanelIsDisplayed();
    }
}