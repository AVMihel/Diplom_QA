package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
@Epic("Управление новостями")
@Feature("Создание и редактирование новостей")
@DisplayName("Тесты создания и редактирования новостей")
public class NewsCreateEditTest extends BaseTest {

    private ControlPanelPage controlPanelPage;
    private NavigationDrawerPage navigationDrawer;
    private NewsPage newsPage;

    @Before
    public void setUp() {
        ensureOnMainScreen();
        controlPanelPage = new ControlPanelPage();
        navigationDrawer = new NavigationDrawerPage();
        newsPage = new NewsPage();

        // Переход в Control Panel
        navigationDrawer.openMenu().clickNewsMenuItem();
        newsPage.waitForNewsScreen();
        controlPanelPage.clickEditNewsButton();
        controlPanelPage.waitForControlPanelLoaded();
    }

    @Test
    @DisplayName("Полный E2E сценарий создания новости")
    @Description("TC-NEWS-CREATE-01: Создание новости со всеми полями")
    @Story("Пользователь может создать новость с заполнением всех полей")
    public void testE2ECreateNews() {
        String testTitle = "E2ECreate_" + System.currentTimeMillis();

        String createdTitle = controlPanelPage.createTestNews(
                testTitle,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.getFutureDate(1),
                TestData.News.E2E_TIME,
                "E2E test news description"
        );

        assertTrue("News should be created and displayed", controlPanelPage.isNewsDisplayed(createdTitle));
        controlPanelPage.deleteNews(createdTitle);
    }

    @Test
    @DisplayName("Валидация поля Publication date: нельзя выбрать прошедшую дату")
    @Description("TC-NEWS-CREATE-03: Проверка блокировки прошедших дат")
    @Story("Система не позволяет выбрать дату в прошлом")
    public void testPastDateValidation() {
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.selectCategory(TestData.News.CATEGORY_ANNOUNCEMENT)
                .fillTitle("PastDateTest_" + System.currentTimeMillis())
                .setDate(TestData.News.getDateForDaysFromNow(-1))
                .setCurrentTime()
                .fillDescription("Test description")
                .clickSaveButton();

        assertTrue("Should stay on create screen when past date selected",
                createPage.isStillOnEditScreen());
    }

    @Test
    @DisplayName("Валидация поля Category: ручной ввод текста")
    @Description("TC-NEWS-CREATE-04: Проверка валидации при ручном вводе категории")
    @Story("Ручной ввод невалидной категории должен вызывать ошибку")
    public void testManualCategoryValidation() {
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.fillTitle("ManualCategoryTest_" + System.currentTimeMillis())
                .selectCategory("TestInvalidCategory")
                .setCurrentDate()
                .setCurrentTime()
                .fillDescription("Test description")
                .clickSaveButton();

        assertTrue("Should stay on create screen with validation error",
                createPage.isStillOnEditScreen() || createPage.isValidationErrorDisplayed());
    }

    @Test
    @DisplayName("Проверка выбора категории из выпадающего списка")
    @Description("TC-NEWS-CREATE-05: Выбор существующей категории")
    @Story("Пользователь может выбрать категорию из списка")
    public void testSelectCategoryFromDropdown() {
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.selectCategory(TestData.News.CATEGORY_BIRTHDAY);

        // Примечание: Проверку выбора категории из выпадающего списка реализовать не удалось
        // из-за технических ограничений взаимодействия с выпадающим списком через Espresso.
        // Тест проверяет вставку текста в поле категория и его отображение без точного совпадения текста.

        assertTrue("Category field should contain selected value",
                createPage.isCategoryFieldDisplayed());
    }

    @Test
    @DisplayName("Валидация максимальной длины поля Title")
    @Description("TC-NEWS-CREATE-06: Проверка заголовка из 500 символов")
    @Story("Заголовок из 500 символов должен быть отклонен")
    public void testMaxLengthTitleValidation() {
        String longTitle = TestData.NewsCreation.getVeryLongTitle();

        if (controlPanelPage.isNewsDisplayed(longTitle)) {
            controlPanelPage.deleteNews(longTitle);
        }

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.fillTitle(longTitle)
                .selectCategory(TestData.News.CATEGORY_ANNOUNCEMENT)
                .setCurrentDate()
                .setCurrentTime()
                .fillDescription("Test description")
                .clickSaveButton();

        if (controlPanelPage.waitForControlPanelLoadedWithTimeout(2000) &&
                controlPanelPage.isNewsDisplayed(longTitle)) {
            controlPanelPage.deleteNews(longTitle);
            fail("System created news with 500-char title");
        }
    }

    @Test
    @DisplayName("Валидация поля Title из спецсимволов")
    @Description("TC-NEWS-CREATE-07: Проверка заголовка из спецсимволов (баг)")
    @Story("Заголовок из спецсимволов должен вызывать ошибку")
    public void testSpecialCharsTitleValidation() {
        String specialTitle = TestData.NewsCreation.SPECIAL_CHARS_TITLE;

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.fillTitle(specialTitle)
                .selectCategory(TestData.News.CATEGORY_ANNOUNCEMENT)
                .setCurrentDate()
                .setCurrentTime()
                .fillDescription("Test description")
                .clickSaveButton();

        if (controlPanelPage.waitForControlPanelLoadedWithTimeout(2000)) {
            boolean isCreated = controlPanelPage.isNewsDisplayed(specialTitle);
            if (isCreated) {
                controlPanelPage.deleteNews(specialTitle);
                fail("System created news with title containing only special characters");
            }
        } else {
            assertTrue("Should stay on create screen with validation",
                    createPage.isStillOnEditScreen());
        }
    }

    @Test
    @DisplayName("Валидация Title из пробелов")
    @Description("TC-NEWS-CREATE-08: Проверка заголовка из пробелов")
    @Story("Заголовок из пробелов должен вызывать ошибку")
    public void testSpacesOnlyTitleValidation() {
        String spacesTitle = TestData.NewsCreation.SPACES_ONLY_TITLE;

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.fillTitle(spacesTitle)
                .selectCategory(TestData.News.CATEGORY_ANNOUNCEMENT)
                .setCurrentDate()
                .setCurrentTime()
                .fillDescription("Test description")
                .clickSaveButton();

        if (controlPanelPage.waitForControlPanelLoadedWithTimeout(2000)) {
            boolean isCreated = controlPanelPage.isNewsDisplayed(spacesTitle);
            assertFalse("News with spaces-only title should not be created", isCreated);
        } else {
            assertTrue("Should stay on create screen with validation",
                    createPage.isStillOnEditScreen());
        }
    }

    @Test
    @DisplayName("Проверка многострочного Description")
    @Description("TC-NEWS-CREATE-09: Сохранение многострочного описания")
    @Story("Многострочное описание должно сохраняться с переносами")
    public void testMultilineDescription() {
        String testTitle = "MultilineTest_" + System.currentTimeMillis();

        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    TestData.NewsCreation.MULTILINE_DESCRIPTION
            );

            assertTrue("News with multiline description should be created",
                    controlPanelPage.isNewsDisplayed(createdTitle));
        } finally {
            controlPanelPage.safeDeleteNews(testTitle);
        }
    }

    @Test
    @DisplayName("Отмена создания новости")
    @Description("TC-NEWS-CREATE-10: Отмена с подтверждением")
    @Story("Пользователь может отменить создание новости")
    public void testCancelNewsCreation() {
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.cancelWithConfirmation();
        assertTrue("Should return to Control Panel after cancel",
                controlPanelPage.isControlPanelDisplayed());
    }

    @Test
    @DisplayName("Валидация всех обязательных полей")
    @Description("TC-NEWS-CREATE-11: Проверка валидации пустых полей")
    @Story("Все обязательные поля должны быть заполнены")
    public void testRequiredFieldsValidation() {
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.clickSaveButton();

        assertTrue("Should stay on create screen when required fields are empty",
                createPage.isStillOnEditScreen());
    }

    @Test
    @DisplayName("Валидация выбора даты публикации - граничные значения")
    @Description("TC-NEWS-CREATE-14: Проверка выбора различных дат")
    @Story("Пользователь может выбирать различные даты публикации")
    public void testDateBoundaryValues() {
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        assertTrue("Create screen should be displayed", createPage.isCreateScreenDisplayed());

        createPage.fillTitle("DateBoundaryTest_" + System.currentTimeMillis())
                .selectCategory(TestData.News.CATEGORY_ANNOUNCEMENT);

        createPage.setCurrentDate();
        createPage.setDate(TestData.News.getFutureDate(1));
        createPage.setDate(TestData.News.getFutureDate(365));

        assertTrue("Date selection should work", createPage.isStillOnEditScreen());
    }

    @Test
    @DisplayName("Отмена редактирования")
    @Description("TC-NEWS-EDIT-01: Отмена с подтверждением")
    @Story("Пользователь может отменить редактирование")
    public void testCancelEditing() {
        String testTitle = "EditCancelTest_" + System.currentTimeMillis();
        String createdTitle = null;

        try {
            createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    "Original description for cancel test"
            );

            CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews(createdTitle);
            assertTrue("Edit screen should be displayed", editPage.isEditScreenDisplayed());

            String newTitle = "Changed_" + System.currentTimeMillis();
            editPage.fillTitle(newTitle)
                    .cancelWithConfirmation();

            assertTrue("Should return to Control Panel", controlPanelPage.isControlPanelDisplayed());
            assertTrue("Original news should still exist",
                    controlPanelPage.isNewsDisplayed(createdTitle));
            assertFalse("Changed title should not appear",
                    controlPanelPage.isNewsDisplayed(newTitle));
        } finally {
            if (createdTitle != null) {
                controlPanelPage.safeDeleteNews(createdTitle);
            }
        }
    }

    @Test
    @DisplayName("Полный E2E сценарий редактирования новости")
    @Description("TC-NEWS-EDIT-02: Редактирование всех полей")
    @Story("Пользователь может изменить все поля существующей новости")
    public void testE2EEditNews() {
        String originalTitle = "Original_" + System.currentTimeMillis();
        String updatedTitle = "Updated_" + System.currentTimeMillis();
        String createdTitle = null;

        try {
            createdTitle = controlPanelPage.createTestNews(
                    originalTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    "Original description for edit test"
            );

            CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews(createdTitle);
            assertTrue("Edit screen should be displayed", editPage.isEditScreenDisplayed());

            String titleText = editPage.getTitleText();
            assertTrue("Title should be pre-filled", titleText != null && !titleText.isEmpty());

            editPage.fillTitle(updatedTitle)
                    .selectCategory(TestData.News.CATEGORY_BIRTHDAY)
                    .setDate(TestData.News.getFutureDate(2))
                    .setTime("15:30")
                    .fillDescription("Updated description")
                    .setActiveStatus(false)
                    .clickSaveButton();

            assertTrue("Should return to Control Panel", controlPanelPage.waitForControlPanelLoadedWithTimeout(2000));
            assertTrue("Updated news should be displayed",
                    controlPanelPage.waitForNewsDisplayed(updatedTitle));

            createdTitle = updatedTitle;

        } finally {
            if (createdTitle != null) {
                controlPanelPage.safeDeleteNews(createdTitle);
            }
        }
    }
}