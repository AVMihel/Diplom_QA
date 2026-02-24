package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
@DisplayName("Тесты Control Panel")
public class ControlPanelNewsTest extends BaseTest {

    private ControlPanelPage controlPanelPage;
    private NavigationDrawerPage navigationDrawer;
    private NewsPage newsPage;
    private NewsFilterPage newsFilterPage;

    @Before
    public void setUp() {
        ensureOnMainScreen();
        controlPanelPage = new ControlPanelPage();
        navigationDrawer = new NavigationDrawerPage();
        newsPage = new NewsPage();
        newsFilterPage = new NewsFilterPage();

        // Переход в Control Panel
        navigationDrawer.openMenu().clickNewsMenuItem();
        newsPage.waitForNewsScreen();
        controlPanelPage.clickEditNewsButton();
        controlPanelPage.waitForControlPanelLoaded();
    }

    @Test
    @DisplayName("Отображение элементов у новостей в Control Panel")
    @Description("TC-NEWS-CP-01: Проверка наличия всех элементов управления у новости")
    @Story("Каждая карточка новости содержит все необходимые элементы")
    public void testNewsElementsDisplay() {
        String testTitle = "TestNews_" + System.currentTimeMillis();
        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    "Test description"
            );

            controlPanelPage.checkNewsItemElements(createdTitle);
        } finally {
            controlPanelPage.safeDeleteNews(testTitle);
        }
    }

    @Test
    @DisplayName("Раскрытие/скрытие описания новости")
    @Description("TC-NEWS-CP-02: Проверка функционала раскрытия описания")
    @Story("Пользователь может раскрывать и скрывать описание новости")
    public void testExpandCollapseDescription() {
        String testTitle = "ExpandTest_" + System.currentTimeMillis();
        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    "Test description for expand/collapse"
            );

            controlPanelPage.expandAndCollapseDescription(createdTitle);
        } finally {
            controlPanelPage.safeDeleteNews(testTitle);
        }
    }

    @Test
    @DisplayName("Переход к редактированию новости")
    @Description("TC-NEWS-CP-03: Проверка перехода на экран редактирования")
    @Story("Пользователь может перейти к редактированию новости")
    public void testNavigateToEdit() {
        String testTitle = "EditNavTest_" + System.currentTimeMillis();
        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    "Test description for edit navigation"
            );

            CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews(createdTitle);
            assertTrue("Edit screen should be displayed", editPage.isEditScreenDisplayed());

            editPage.cancelWithConfirmation();
            assertTrue("Should return to Control Panel", controlPanelPage.isControlPanelDisplayed());
        } finally {
            controlPanelPage.safeDeleteNews(testTitle);
        }
    }

    @Test
    @DisplayName("Полный E2E сценарий удаления новости")
    @Description("TC-NEWS-CP-04: Создание и удаление новости")
    @Story("Пользователь может создать и удалить новость")
    public void testCreateAndDeleteNews() {
        String testTitle = "DeleteTest_" + System.currentTimeMillis();

        String createdTitle = controlPanelPage.createTestNews(
                testTitle,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.getFutureDate(1),
                TestData.News.DEFAULT_TIME,
                "Test description for deletion"
        );

        assertTrue("News should be created", controlPanelPage.isNewsDisplayed(createdTitle));

        controlPanelPage.deleteNews(createdTitle);
        assertFalse("News should be deleted", controlPanelPage.isNewsDisplayed(createdTitle));
    }

    @Test
    @DisplayName("Проверка корректности отображения Publication date")
    @Description("TC-NEWS-CP-05: Дата публикации отображается корректно")
    @Story("Дата публикации новости отображается в правильном формате")
    public void testPublicationDateDisplay() {
        String expectedDate = TestData.News.getFutureDate(1);
        String testTitle = "PubDateTest_" + System.currentTimeMillis();

        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    expectedDate,
                    TestData.News.DEFAULT_TIME,
                    "Test for publication date"
            );

            assertTrue("Publication date should be displayed correctly",
                    controlPanelPage.isPublicationDateCorrect(createdTitle, expectedDate));
        } finally {
            controlPanelPage.safeDeleteNews(testTitle);
        }
    }

    @Test
    @DisplayName("Проверка корректности отображения Creation date")
    @Description("TC-NEWS-CP-06: Дата создания отображается корректно")
    @Story("Дата создания новости должна содержать правильный год")
    public void testCreationDateDisplay() {
        int currentYear = LocalDate.now().getYear();
        String testTitle = "CreationDateTest_" + System.currentTimeMillis();

        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    "Test for creation date"
            );

            int actualYear = controlPanelPage.getCreationDateYear(createdTitle);
            assertTrue("Creation date should contain correct year. Expected: " + currentYear +
                            ", Actual: " + actualYear,
                    actualYear == currentYear);
        } finally {
            controlPanelPage.safeDeleteNews(testTitle);
        }
    }

    @Test
    @DisplayName("Сортировка новостей по дате публикации")
    @Description("TC-NEWS-CP-07: Сортировка меняет порядок новостей")
    @Story("Пользователь может сортировать новости по дате")
    public void testSortNews() {
        Map<String, String> testNews = controlPanelPage.createTwoNewsForSortingTest();
        String todayTitle = testNews.get("todayTitle");
        String tomorrowTitle = testNews.get("tomorrowTitle");

        try {
            controlPanelPage.scrollToTop();

            boolean todayFirstBefore = controlPanelPage.isNewsVisibleWithoutScroll(todayTitle);
            boolean tomorrowFirstBefore = controlPanelPage.isNewsVisibleWithoutScroll(tomorrowTitle);

            controlPanelPage.clickSortButton();
            controlPanelPage.scrollToTop();

            boolean todayFirstAfter = controlPanelPage.isNewsVisibleWithoutScroll(todayTitle);
            boolean tomorrowFirstAfter = controlPanelPage.isNewsVisibleWithoutScroll(tomorrowTitle);

            assertTrue("Sort button should change news order",
                    (todayFirstBefore != todayFirstAfter) || (tomorrowFirstBefore != tomorrowFirstAfter));
        } finally {
            controlPanelPage.safeDeleteNews(todayTitle);
            controlPanelPage.safeDeleteNews(tomorrowTitle);
        }
    }

    @Test
    @DisplayName("Фильтрация с неактивным чекбоксом статуса")
    @Description("TC-NEWS-CP-08: Пустой список при снятых чекбоксах (баг)")
    @Story("При отсутствии выбранных статусов не должно быть результатов")
    public void testFilterWithNoStatus() {
        controlPanelPage.openNewsFilter();
        assertTrue("Filter dialog should be displayed", newsFilterPage.isFilterDialogDisplayed());

        newsFilterPage.uncheckActive()
                .uncheckInactive()
                .applyFilter();

        assertTrue("News list should be empty when no status selected",
                controlPanelPage.isNewsListEmpty());
    }

    @Test
    @DisplayName("Фильтрация только активных новостей")
    @Description("TC-NEWS-CP-09: Отображаются только активные новости")
    @Story("При фильтре по активным новостям неактивные не отображаются")
    public void testFilterOnlyActive() {
        controlPanelPage.openNewsFilter();
        assertTrue("Filter dialog should be displayed", newsFilterPage.isFilterDialogDisplayed());

        newsFilterPage.checkOnlyActive()
                .applyFilter();

        assertTrue("Only active news should be displayed",
                controlPanelPage.hasOnlyActiveNews());
    }

    @Test
    @DisplayName("Фильтрация только неактивных новостей")
    @Description("TC-NEWS-CP-10: Отображаются только неактивные новости")
    @Story("При фильтре по неактивным новостям активные не отображаются")
    public void testFilterOnlyInactive() {
        controlPanelPage.openNewsFilter();
        assertTrue("Filter dialog should be displayed", newsFilterPage.isFilterDialogDisplayed());

        newsFilterPage.checkOnlyInactive()
                .applyFilter();

        assertTrue("Only inactive news should be displayed",
                controlPanelPage.hasOnlyInactiveNews());
    }
}