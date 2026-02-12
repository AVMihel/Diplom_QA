package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Создание новостей")
@Feature("Создание и валидация новостей")
@DisplayName("Тесты создания новостей в Control Panel")
public class NewsCreationTest extends BaseTest {

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
    @DisplayName("Валидация поля 'Publication date': нельзя выбрать прошедшую дату")
    @Description("TC-NEWS-CREATE-08: Валидация поля 'Publication date': нельзя выбрать прошедшую дату")
    @Story("Прошедшие даты должны быть отклонены")
    public void testPublicationDatePastDateValidation() {
        Allure.step("Шаг 1: Навигация к созданию новости");
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();

        Allure.step("Шаг 2: Проверка отображения экрана создания");
        createPage.isCreateScreenDisplayed();

        String testTitle = TestData.News.Validation.LENGTH_TEST_TITLE_PREFIX + System.currentTimeMillis();
        Allure.step("Шаг 3: Заполнение данных новости. Заголовок: " + testTitle);
        createPage.fillTitle(testTitle)
                .selectCategorySimple(TestData.News.CATEGORY_ANNOUNCEMENT);

        Allure.step("Шаг 4: Выбор прошедшей даты (1 день назад)");
        createPage.selectPastDate(1);

        Allure.step("Шаг 5: Выбор текущего времени и заполнение описания");
        createPage.selectCurrentTime()
                .fillDescription(TestData.News.Validation.LENGTH_TEST_DESCRIPTION)
                .clickSaveButton();

        Allure.step("Шаг 6: Проверка, что остались на экране редактирования");
        boolean isStillOnEditScreen = createPage.isStillOnEditScreen();
        assertTrue("BUG: Validation of past publication date should work correctly", isStillOnEditScreen);
    }

    @Test
    @DisplayName("Валидация поля 'Category': ручной ввод текста")
    @Description("TC-NEWS-CREATE-09: Валидация поля 'Category': ручной ввод текста")
    @Story("Ручной ввод должен быть отклонен")
    public void testCategoryManualInputValidation() {
        Allure.step("Шаг 1: Навигация к созданию новости");
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();

        Allure.step("Шаг 2: Проверка отображения экрана создания");
        createPage.isCreateScreenDisplayed();

        String testTitle = TestData.News.Validation.MANUAL_CATEGORY_TEST_TITLE_PREFIX + System.currentTimeMillis();
        Allure.step("Шаг 3: Заполнение заголовка: " + testTitle);
        createPage.fillTitle(testTitle);

        Allure.step("Шаг 4: Ручной ввод невалидной категории: " + TestData.NewsCreation.INVALID_CATEGORY);
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(replaceText(TestData.NewsCreation.INVALID_CATEGORY));

        Allure.step("Шаг 5: Выбор даты и времени публикации");
        createPage.selectCurrentDate()
                .selectCurrentTime()
                .fillDescription(TestData.News.Validation.MANUAL_CATEGORY_TEST_DESCRIPTION)
                .clickSaveButton();

        Allure.step("Шаг 6: Проверка отображения ошибки валидации");
        boolean isValidationErrorDisplayed = createPage.isValidationErrorDisplayed();
        assertTrue("BUG: Validation of manual category input should work correctly", isValidationErrorDisplayed);
    }

    @Test
    @DisplayName("Проверка выбора категории из выпадающего списка")
    @Description("TC-NEWS-CREATE-10: Проверка выбора категории из выпадающего списка")
    @Story("Категория должна выбираться из списка")
    public void testCategorySelectionFromDropdown() {
        Allure.step("Шаг 1: Навигация к созданию новости");
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();

        Allure.step("Шаг 2: Проверка отображения экрана создания");
        createPage.isCreateScreenDisplayed();

        Allure.step("Шаг 3: Выбор категории из списка: " + TestData.News.CATEGORY_ANNOUNCEMENT);
        createPage.selectCategorySimple(TestData.News.CATEGORY_ANNOUNCEMENT);

        Allure.step("Шаг 4: Проверка, что категория выбрана");
        boolean isCategorySelected = createPage.isCategoryFieldDisplayed();
        assertTrue("BUG: Category selection from dropdown should work correctly", isCategorySelected);
    }

    @Test
    @DisplayName("Валидация длины поля 'Title'")
    @Description("TC-NEWS-CREATE-11: Валидация длины поля 'Title'")
    @Story("Слишком длинный заголовок должен быть отклонен")
    public void testTitleLengthValidation() {
        Allure.step("Шаг 1: Навигация к созданию новости");
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();

        Allure.step("Шаг 2: Проверка отображения экрана создания");
        createPage.isCreateScreenDisplayed();

        String longTitle = TestData.NewsCreation.getVeryLongTitle();
        Allure.step("Шаг 3: Ввод слишком длинного заголовка (" + longTitle.length() + " символов)");
        createPage.fillTitle(longTitle)
                .selectCategorySimple(TestData.News.CATEGORY_ANNOUNCEMENT)
                .selectCurrentDate()
                .selectCurrentTime()
                .fillDescription(TestData.News.Validation.LENGTH_TEST_DESCRIPTION)
                .clickSaveButton();

        Allure.step("Шаг 4: Проверка, что остались на экране редактирования");
        boolean isStillOnEditScreen = createPage.isStillOnEditScreen();
        assertTrue("BUG: Validation of title length should work correctly", isStillOnEditScreen);
    }

    @Test
    @DisplayName("Валидация поля 'Title' из спецсимволов")
    @Description("TC-NEWS-CREATE-12: Валидация поля 'Title' из спецсимволов")
    @Story("Заголовок из спецсимволов должен быть отклонен")
    public void testTitleSpecialCharactersValidation() {
        Allure.step("Шаг 1: Навигация к созданию новости");
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();

        Allure.step("Шаг 2: Проверка отображения экрана создания");
        createPage.isCreateScreenDisplayed();

        Allure.step("Шаг 3: Ввод заголовка из спецсимволов: " + TestData.NewsCreation.SPECIAL_CHARS_TITLE);
        createPage.fillTitle(TestData.NewsCreation.SPECIAL_CHARS_TITLE)
                .selectCategorySimple(TestData.News.CATEGORY_ANNOUNCEMENT)
                .selectCurrentDate()
                .selectCurrentTime()
                .fillDescription(TestData.News.Validation.SPECIAL_CHARS_TEST_DESCRIPTION)
                .clickSaveButton();

        Allure.step("Шаг 4: Проверка, что остались на экране редактирования");
        boolean isStillOnEditScreen = createPage.isStillOnEditScreen();
        assertTrue("BUG: Validation of special characters in title should work correctly", isStillOnEditScreen);
    }

    @Test
    @DisplayName("Валидация 'Title' из пробелов")
    @Description("TC-NEWS-CREATE-13: Валидация 'Title' из пробелов")
    @Story("Заголовок из пробелов должен быть отклонен")
    public void testTitleSpacesOnlyValidation() {
        Allure.step("Шаг 1: Навигация к созданию новости");
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();

        Allure.step("Шаг 2: Проверка отображения экрана создания");
        createPage.isCreateScreenDisplayed();

        Allure.step("Шаг 3: Ввод заголовка, состоящего только из пробелов");
        createPage.fillTitle(TestData.NewsCreation.SPACES_ONLY_TITLE)
                .selectCategorySimple(TestData.News.CATEGORY_ANNOUNCEMENT)
                .selectCurrentDate()
                .selectCurrentTime()
                .fillDescription(TestData.News.Validation.SPACES_TEST_DESCRIPTION)
                .clickSaveButton();

        Allure.step("Шаг 4: Проверка, что остались на экране редактирования");
        boolean isStillOnEditScreen = createPage.isStillOnEditScreen();
        assertTrue("BUG: Validation of title with only spaces should work correctly", isStillOnEditScreen);
    }

    @Test
    @DisplayName("Проверка многострочного 'Description'")
    @Description("TC-NEWS-CREATE-14: Проверка многострочного 'Description'")
    @Story("Многострочное описание должно поддерживаться")
    public void testMultilineDescription() {
        String testTitle = TestData.News.Validation.MULTILINE_TEST_TITLE_PREFIX + System.currentTimeMillis();
        Allure.step("Шаг 1: Создание новости с многострочным описанием. Заголовок: " + testTitle);

        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    TestData.NewsCreation.MULTILINE_DESCRIPTION
            );

            Allure.step("Шаг 2: Проверка создания новости");
            boolean isNewsCreated = controlPanelPage.isNewsDisplayed(createdTitle);
            assertTrue("BUG: Creation of news with multiline description should work correctly", isNewsCreated);

            Allure.step("Шаг 3: Проверка сохранения многострочного описания");
            boolean isDescriptionPreserved = controlPanelPage.isMultilineDescriptionDisplayed(
                    createdTitle,
                    TestData.NewsCreation.MULTILINE_DESCRIPTION
            );
            assertTrue("BUG: Multiline description content should be preserved", isDescriptionPreserved);

        } finally {
            Allure.step("Очистка: Удаление тестовой новости");
            controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);
        }
    }

    @Test
    @DisplayName("Отмена создания новости")
    @Description("TC-NEWS-CREATE-15: Отмена создания новости")
    @Story("Пользователь может отменить создание новости")
    public void testCancelNewsCreation() {
        Allure.step("Шаг 1: Навигация к созданию новости");
        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();

        Allure.step("Шаг 2: Проверка отображения экрана создания");
        createPage.isCreateScreenDisplayed();

        Allure.step("Шаг 3: Отмена создания новости с подтверждением");
        createPage.cancelWithConfirmation();

        Allure.step("Шаг 4: Проверка возврата в Control Panel");
        boolean isControlPanelDisplayed = controlPanelPage.isControlPanelDisplayed();
        assertTrue("BUG: Cancel functionality should work correctly", isControlPanelDisplayed);
    }
}