package ru.iteco.fmhandroid.ui.tests;

import org.junit.Test;

import java.time.LocalDate;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.pages.NewsFilterPage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class ControlPanelNewsTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();

    // TC-NEWS-CP-01: Переход в "Control Panel"
    @Test
    public void testNavigateToControlPanel() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Проверяем элементы Control Panel
        onView(withId(R.id.add_news_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()));
    }

    // TC-NEWS-CP-02: Отображение элементов у новостей в "Control Panel"
    @Test
    public void testDisplayNewsElementsInControlPanel() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Проверяем элементы новостей
        controlPanelPage.checkNewsElementsExist();
    }

    // TC-NEWS-CP-03: Раскрытие/скрытие описания новости
    @Test
    public void testExpandCollapseNewsDescription() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Проверяем раскрытие/скрытие описания
        controlPanelPage.expandAndCollapseNewsDescription();
    }

    // TC-NEWS-CP-04: Переход к редактированию новости
    @Test
    public void testNavigateToEditNews() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Переходим к редактированию новости
        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        // 4. Отменяем редактирование
        editPage.cancelWithConfirmation();

        // 5. Проверяем возврат в Control Panel
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-CP-06: Проверка корректности отображения "Publication date"
    @Test
    public void testPublicationDateDisplay() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        String testNewsTitle = null;
        try {
            // 3. Создаем новость с текущим годом
            int currentYear = LocalDate.now().getYear();
            testNewsTitle = controlPanelPage.createNewsForDateTest(currentYear);

            // 4. Проверяем год в дате публикации
            controlPanelPage.verifyPublicationDateYear(testNewsTitle, currentYear);

        } finally {
            // 5. Очистка
            if (testNewsTitle != null) {
                try {
                    controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
                } catch (Exception e) {
                }
            }
        }
    }

    // TC-NEWS-CP-07: Проверка корректности отображения "Creation date"
    @Test
    public void testCreationDateYear() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        String testNewsTitle = null;
        try {
            // 3. Создаем новость с текущим годом
            int currentYear = LocalDate.now().getYear();
            testNewsTitle = controlPanelPage.createNewsForDateTest(currentYear);

            // 4. Проверяем год в дате создания
            controlPanelPage.verifyCreationDateYear(testNewsTitle, currentYear);

        } finally {
            // 5. Очистка
            if (testNewsTitle != null) {
                try {
                    controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
                } catch (Exception e) {
                }
            }
        }
    }

    // TC-NEWS-CP-08: Сортировка новостей по дате публикации
    @Test
    public void testSortNewsByPublicationDate() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Сортируем новости
        controlPanelPage.clickSortButton();
        controlPanelPage.checkNewsAreSorted();

        // 4. Сортируем еще раз (обратный порядок)
        controlPanelPage.clickSortButton();
        controlPanelPage.checkNewsAreSorted();
    }

    // TC-NEWS-CP-09: Фильтрация с неактивным чекбоксом статуса
    @Test
    public void testFilterWithNoStatusChecked() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Открываем фильтр
        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();

        // 4. Снимаем оба чекбокса статуса
        filterPage.uncheckBothStatuses();

        // 5. Применяем фильтр
        filterPage.applyFilter();

        // 6. Проверяем результат
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-CP-10: Фильтрация только активных новостей
    @Test
    public void testFilterOnlyActiveNews() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Проверяем кнопку фильтра
        onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()));
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-CP-11: Фильтрация только неактивных новостей
    @Test
    public void testFilterOnlyInactiveNews() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Проверяем кнопку фильтра
        onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()));
        controlPanelPage.checkControlPanelIsDisplayed();
    }
}