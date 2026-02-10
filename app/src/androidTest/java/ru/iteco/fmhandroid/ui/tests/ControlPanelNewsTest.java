package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.util.Map;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;
import ru.iteco.fmhandroid.ui.pages.NewsFilterPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Управление новостями")
@Feature("Control Panel - Панель управления новостей")
@DisplayName("Тесты Control Panel для управления новостей")
public class ControlPanelNewsTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();
    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final NewsPage newsPage = new NewsPage();
    private final NewsFilterPage newsFilterPage = new NewsFilterPage();

    @Before
    public void setUp() {
        setUpToAuthScreen();
        loginAndGoToMainScreen();

        controlPanelPage.navigateToControlPanelFromMain(navigationDrawer, newsPage);
    }

    @After
    public void tearDown() {
        tearDownToAuthScreen();
    }

    @Test
    @DisplayName("Переход в 'Control Panel'")
    @Description("TC-NEWS-CP-01: Переход в 'Control Panel'")
    @Story("Пользователь может перейти в панель управления для редактирования новостей")
    public void testNavigateToControlPanel() {
        assertTrue("BUG: Control Panel screen should be accessible from News screen",
                controlPanelPage.isControlPanelDisplayed());
    }

    @Test
    @DisplayName("Проверка элементов управления у новости в Control Panel")
    @Description("TC-NEWS-CP-02: Проверка элементов управления у новости in Control Panel")
    @Story("Каждая карточка новости в Control Panel должна предоставлять интерфейс для управления")
    public void testDisplayNewsElementsInControlPanel() {
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()));
        controlPanelPage.scrollToTop();
        controlPanelPage.checkNewsElementsExist();

        assertTrue("BUG: News management elements should be displayed in Control Panel",
                controlPanelPage.areNewsElementsDisplayed());
    }

    @Test
    @DisplayName("Раскрытие/скрытие описания новости")
    @Description("TC-NEWS-CP-03: Раскрытие/скрытие описания новости")
    @Story("Пользователь может управлять отображением описания новости")
    public void testExpandCollapseNewsDescription() {
        controlPanelPage.expandAndCollapseNewsDescription();

        assertTrue("BUG: News description should be expandable/collapsible",
                controlPanelPage.isNewsDescriptionFunctional());
    }

    @Test
    @DisplayName("Переход к редактированию новости")
    @Description("TC-NEWS-CP-04: Переход к редактированию новости")
    @Story("Пользователь может редактировать новости через Control Panel")
    public void testNavigateToEditNews() {
        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();

        assertTrue("BUG: Edit news screen should be accessible from Control Panel",
                editPage.isEditScreenDisplayed());

        editPage.cancelWithConfirmation();
        assertTrue(controlPanelPage.isControlPanelDisplayed());
    }

    @Test
    @DisplayName("Проверка корректности отображения 'Publication date'")
    @Description("TC-NEWS-CP-06: Проверка корректности отображения 'Publication date'")
    @Story("Дата публикации должна корректно отображаться для каждой новости")
    public void testPublicationDateDisplay() {
        String testNewsTitle = controlPanelPage.createTestNews(
                "Test Publication Date " + System.currentTimeMillis(),
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.getFutureDate(1),
                TestData.News.DEFAULT_TIME,
                "Test description for publication date"
        );
        try {
            String expectedDate = TestData.News.getFutureDate(1);
            boolean isPublicationDateCorrect = controlPanelPage.isPublicationDateDisplayed(testNewsTitle, expectedDate);
            assertTrue("BUG: Publication date should be displayed correctly for created news",
                    isPublicationDateCorrect);
        } finally {
            controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
        }
    }

    @Test
    @DisplayName("Проверка корректности отображения 'Creation date'")
    @Description("TC-NEWS-CP-07: Проверка корректности отображения 'Creation date'")
    @Story("Дата создания должна содержать корректный год")
    public void testCreationDateDisplay() {
        int currentYear = LocalDate.now().getYear();
        String testNewsTitle = controlPanelPage.createNewsForDateTest(currentYear);
        try {
            int actualYear = controlPanelPage.getCreationDateYear(testNewsTitle);

            assertTrue("BUG: Creation date should display correct year",
                    actualYear == currentYear);
        } finally {
            controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
        }
    }

    @Test
    @DisplayName("Сортировка новостей по дате публикации")
    @Description("TC-NEWS-CP-08: Сортировка новостей по дате публикации")
    @Story("Пользователь может сортировать новости по дате публикации в прямом и обратном порядке")
    public void testSortNewsByPublicationDate() {

        // Примечание: Проверку конкретного порядка сортировки по датам публикации
        // двух новостей реализовать не удалось из-за сложности
        // определения точных позиций новостей в RecyclerView.
        // Тест проверяет базовую функциональность сортировки - изменение порядка
        // новостей при нажатии кнопки сортировки.

        Map<String, String> testNews = controlPanelPage.createTestNewsForSortingTest();
        String todayTitle = testNews.get("todayTitle");
        String tomorrowTitle = testNews.get("tomorrowTitle");
        try {
            controlPanelPage.scrollToTop();
            boolean todayVisible = controlPanelPage.isNewsVisibleWithoutScroll(todayTitle);
            boolean tomorrowVisible = controlPanelPage.isNewsVisibleWithoutScroll(tomorrowTitle);

            assertTrue("BUG: At least one test news should be visible without scrolling",
                    todayVisible || tomorrowVisible);

            String visibleFirst = todayVisible ? todayTitle : tomorrowTitle;
            String previousFirst = visibleFirst;
            boolean sortChangedSomething = false;
            for (int i = 1; i <= 2; i++) {
                controlPanelPage.clickSortButton();
                controlPanelPage.scrollToTop();
                boolean todayNowVisible = controlPanelPage.isNewsVisibleWithoutScroll(todayTitle);
                boolean tomorrowNowVisible = controlPanelPage.isNewsVisibleWithoutScroll(tomorrowTitle);
                String currentFirst = todayNowVisible ? todayTitle :
                        tomorrowNowVisible ? tomorrowTitle : "";
                if (!currentFirst.equals(previousFirst) && !currentFirst.isEmpty()) {
                    sortChangedSomething = true;
                }
                previousFirst = currentFirst;
            }
            assertTrue("BUG: Sort button should change news order",
                    sortChangedSomething);
        } finally {
            controlPanelPage.deleteCreatedNewsByExactTitle(todayTitle);
            controlPanelPage.deleteCreatedNewsByExactTitle(tomorrowTitle);
        }
    }

    @Test
    @DisplayName("Фильтрация с неактивным чекбоксом статуса")
    @Description("TC-NEWS-CP-09: Фильтрация с неактивным чекбоксом статуса")
    @Story("При снятых чекбоксах активных/неактивных новостей не должно быть результатов")
    public void testFilterWithNoStatusChecked() {
        controlPanelPage.openNewsFilter();
        assertTrue(newsFilterPage.isFilterDialogDisplayed());
        onView(withId(R.id.filter_news_active_material_check_box)).perform(click());
        onView(withId(R.id.filter_news_inactive_material_check_box)).perform(click());
        newsFilterPage.applyFilter();

        boolean isEmptyList = controlPanelPage.isNewsListEmpty();
        assertTrue("BUG: List should be empty when no status checkboxes are selected", isEmptyList);
    }

    @Test
    @DisplayName("Фильтрация только активных новостей")
    @Description("TC-NEWS-CP-10: Фильтрация только активных новостей")
    @Story("При фильтрации только активных новостей не должны отображаться неактивные")
    public void testFilterOnlyActiveNews() {
        controlPanelPage.openNewsFilter();
        assertTrue(newsFilterPage.isFilterDialogDisplayed());
        onView(withId(R.id.filter_news_inactive_material_check_box)).perform(click());
        newsFilterPage.applyFilter();

        boolean hasInactiveNews = controlPanelPage.hasInactiveNews();
        assertTrue("BUG: Inactive news should not be displayed when filtering only active news",
                !hasInactiveNews);
    }

    @Test
    @DisplayName("Фильтрация только неактивных новостей")
    @Description("TC-NEWS-CP-11: Фильтрация только неактивных новостей")
    @Story("При фильтрации только неактивных новостей не должны отображаться активные")
    public void testFilterOnlyInactiveNews() {
        controlPanelPage.openNewsFilter();
        assertTrue(newsFilterPage.isFilterDialogDisplayed());
        onView(withId(R.id.filter_news_active_material_check_box)).perform(click());
        newsFilterPage.applyFilter();

        boolean hasActiveNews = controlPanelPage.hasActiveNews();
        assertTrue("BUG: Active news should not be displayed when filtering only inactive news",
                !hasActiveNews);
    }
}