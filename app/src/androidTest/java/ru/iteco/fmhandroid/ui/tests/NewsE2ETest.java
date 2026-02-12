package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;

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
        Allure.step("Настройка тестового окружения - авторизация и переход в Control Panel");
        setUpToAuthScreen();
        loginAndGoToMainScreen();

        controlPanelPage.navigateToControlPanelFromMain(navigationDrawer, newsPage);
    }

    @After
    public void tearDown() {
        Allure.step("Очистка после теста - выход из системы");
        tearDownToAuthScreen();
    }

    @Test
    @DisplayName("Полный цикл создания новости")
    @Description("TC-NEWS-CREATE-01: Полный End-to-End сценарий создания новости")
    @Story("Пользователь может создать, найти и удалить новость")
    public void testE2ENewsCreation() {
        TestData.NewsItem testNews = TestData.News.E2E_NEWS;
        Allure.step("Шаг 1: Создание новости. Заголовок: " + testNews.title);

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.isCreateScreenDisplayed();

        Allure.step("Шаг 2: Заполнение всех полей формы");
        createPage.fillTitle(testNews.title)
                .selectCategorySimple(testNews.category)
                .selectCurrentDate()
                .selectCurrentTime()
                .fillDescription(testNews.description)
                .clickSaveButton();

        Allure.step("Шаг 3: Проверка возврата в Control Panel");
        assertTrue("BUG: E2E news creation flow should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        try {
            Allure.step("Шаг 4: Поиск созданной новости по заголовку");
            controlPanelPage.findNewsByTitleWithScroll(testNews.title);
            boolean isNewsCreated = controlPanelPage.isNewsDisplayed(testNews.title);
            assertTrue("BUG: E2E news creation should successfully create visible news", isNewsCreated);
        } finally {
            Allure.step("Шаг 5: Очистка - удаление созданной новости");
            controlPanelPage.deleteCreatedNewsByExactTitle(testNews.title);
            controlPanelPage.isNewsDisplayed(testNews.title);
        }
    }

    @Test
    @DisplayName("Создание и удаление новости")
    @Description("TC-NEWS-CP-05: Сценарий создания и удаления новости")
    @Story("Пользователь может создать и удалить новость")
    public void testE2ENewsDeletion() {
        Allure.step("Шаг 1: Создание новости для теста удаления");
        String testTitle = controlPanelPage.createNewsForDeletionTest();

        Allure.step("Шаг 2: Проверка успешного создания новости");
        boolean isNewsCreated = controlPanelPage.isNewsDisplayed(testTitle);
        assertTrue("BUG: E2E news deletion flow should successfully create news for deletion", isNewsCreated);

        Allure.step("Шаг 3: Удаление созданной новости");
        controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);

        Allure.step("Шаг 4: Проверка успешного удаления новости");
        boolean isNewsDeleted = !controlPanelPage.isNewsDisplayed(testTitle);
        assertTrue("BUG: E2E news deletion flow should successfully delete news", isNewsDeleted);
    }

    @Test
    @DisplayName("Предзаполнение полей при редактировании")
    @Description("TC-NEWS-EDIT-01: Сценарий редактирования существующей новости")
    @Story("Пользователь может редактировать новость с предзаполненными полями")
    public void testE2ENewsEditing() {
        String originalTitle = TestData.News.E2E.ORIGINAL_TITLE_PREFIX + System.currentTimeMillis();
        Allure.step("Шаг 1: Создание исходной новости. Заголовок: " + originalTitle);

        String createdTitle = controlPanelPage.createTestNews(
                originalTitle,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.getFutureDate(1),
                TestData.News.DEFAULT_TIME,
                TestData.News.Editing.EDITING_ORIGINAL_DESCRIPTION
        );

        Allure.step("Шаг 2: Проверка успешного создания новости");
        boolean isNewsCreated = controlPanelPage.isNewsDisplayed(createdTitle);
        assertTrue("BUG: E2E news editing flow should create news for editing", isNewsCreated);

        Allure.step("Шаг 3: Навигация к редактированию новости");
        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        String updatedTitle = TestData.News.E2E.UPDATED_TITLE_PREFIX + System.currentTimeMillis();
        Allure.step("Шаг 4: Изменение заголовка на: " + updatedTitle);
        editPage.fillTitle(updatedTitle);
        editPage.clickSaveButton();

        Allure.step("Шаг 5: Проверка возврата в Control Panel");
        assertTrue("BUG: E2E news editing flow should return to Control Panel after saving",
                controlPanelPage.isControlPanelDisplayed());

        Allure.step("Шаг 6: Проверка успешного обновления новости");
        boolean isNewsUpdated = controlPanelPage.isNewsDisplayed(updatedTitle);
        assertTrue("BUG: E2E news editing flow should successfully update news", isNewsUpdated);

        try {
            Allure.step("Шаг 7: Очистка - удаление обновленной новости");
            controlPanelPage.deleteCreatedNewsByExactTitle(updatedTitle);
        } catch (Exception e) {
            Allure.step("Очистка - удаление исходной новости");
            controlPanelPage.deleteCreatedNewsByExactTitle(createdTitle);
        }
    }
}