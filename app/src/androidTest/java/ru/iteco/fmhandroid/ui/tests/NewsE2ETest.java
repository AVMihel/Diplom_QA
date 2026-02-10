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
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("End-to-End тесты")
@Feature("Полные сценарии работы с новостей")
@DisplayName("E2E тесты создания, редактирования и удаления новостей")
public class NewsE2ETest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();
    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final NewsPage newsPage = new NewsPage();

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
    @DisplayName("Полный цикл создания новости")
    @Description("TC-NEWS-CREATE-01: Полный End-to-End сценарий создания новости")
    @Story("Пользователь может создать, найти и удалить новость")
    public void testE2ENewsCreation() {
        TestData.NewsItem testNews = TestData.News.E2E_NEWS;

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.isCreateScreenDisplayed();

        createPage.fillTitle(testNews.title)
                .selectCategorySimple(testNews.category)
                .selectCurrentDate()
                .selectCurrentTime()
                .fillDescription(testNews.description)
                .clickSaveButton();

        assertTrue("BUG: E2E news creation flow should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        try {
            controlPanelPage.findNewsByTitleWithScroll(testNews.title);
            boolean isNewsCreated = controlPanelPage.isNewsDisplayed(testNews.title);
            assertTrue("BUG: E2E news creation should successfully create visible news", isNewsCreated);
        } finally {
            controlPanelPage.deleteCreatedNewsByExactTitle(testNews.title);
            controlPanelPage.isNewsDisplayed(testNews.title);
        }
    }

    @Test
    @DisplayName("Создание и удаление новости")
    @Description("TC-NEWS-CP-05: Сценарий создания и удаления новости")
    @Story("Пользователь может создать и удалить новость")
    public void testE2ENewsDeletion() {
        String testTitle = controlPanelPage.createNewsForDeletionTest();

        boolean isNewsCreated = controlPanelPage.isNewsDisplayed(testTitle);
        assertTrue("BUG: E2E news deletion flow should successfully create news for deletion", isNewsCreated);

        controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);

        boolean isNewsDeleted = !controlPanelPage.isNewsDisplayed(testTitle);
        assertTrue("BUG: E2E news deletion flow should successfully delete news", isNewsDeleted);
    }

    @Test
    @DisplayName("Предзаполнение полей при редактировании")
    @Description("TC-NEWS-EDIT-01: Сценарий редактирования существующей новости")
    @Story("Пользователь может редактировать новость с предзаполненными полями")
    public void testE2ENewsEditing() {
        String originalTitle = TestData.News.E2E.ORIGINAL_TITLE_PREFIX + System.currentTimeMillis();

        String createdTitle = controlPanelPage.createTestNews(
                originalTitle,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.getFutureDate(1),
                TestData.News.DEFAULT_TIME,
                TestData.News.Editing.EDITING_ORIGINAL_DESCRIPTION
        );

        boolean isNewsCreated = controlPanelPage.isNewsDisplayed(createdTitle);
        assertTrue("BUG: E2E news editing flow should create news for editing", isNewsCreated);

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        String updatedTitle = TestData.News.E2E.UPDATED_TITLE_PREFIX + System.currentTimeMillis();

        editPage.fillTitle(updatedTitle);
        editPage.clickSaveButton();

        assertTrue("BUG: E2E news editing flow should return to Control Panel after saving",
                controlPanelPage.isControlPanelDisplayed());

        boolean isNewsUpdated = controlPanelPage.isNewsDisplayed(updatedTitle);
        assertTrue("BUG: E2E news editing flow should successfully update news", isNewsUpdated);

        try {
            controlPanelPage.deleteCreatedNewsByExactTitle(updatedTitle);
        } catch (Exception e) {
            controlPanelPage.deleteCreatedNewsByExactTitle(createdTitle);
        }
    }
}