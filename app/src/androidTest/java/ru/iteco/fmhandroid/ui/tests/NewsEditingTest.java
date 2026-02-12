package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
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
@Epic("Редактирование новостей")
@Feature("Изменение параметров существующих новостей")
@DisplayName("Тесты редактирования новостей в Control Panel")
public class NewsEditingTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();
    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final NewsPage newsPage = new NewsPage();
    private String testNewsTitle;

    @Before
    public void setUp() {
        Allure.step("Настройка тестового окружения - авторизация и переход в Control Panel");
        setUpToAuthScreen();
        loginAndGoToMainScreen();

        controlPanelPage.navigateToControlPanelFromMain(navigationDrawer, newsPage);

        testNewsTitle = "Тест_редактирование_" + System.currentTimeMillis();
        Allure.step("Создание тестовой новости для редактирования. Заголовок: " + testNewsTitle);

        testNewsTitle = controlPanelPage.createTestNews(
                testNewsTitle,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.getFutureDate(1),
                TestData.News.getTime(),
                TestData.News.Editing.ORIGINAL_DESCRIPTION
        );

        boolean isNewsCreated = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News editing functionality requires test news creation to work correctly", isNewsCreated);
    }

    @After
    public void tearDown() {
        if (testNewsTitle != null) {
            try {
                Allure.step("Очистка - удаление тестовой новости: " + testNewsTitle);
                controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
                boolean isNewsDeleted = !controlPanelPage.isNewsDisplayed(testNewsTitle);
                assertTrue("Test news should be deleted", isNewsDeleted);
            } catch (Exception e) {
                // Игнорируем исключение
            }
        }
        tearDownToAuthScreen();
    }

    @Test
    @DisplayName("Изменение 'Category' при редактировании")
    @Description("TC-NEWS-EDIT-02: Изменение 'Category' при редактировании")
    @Story("Пользователь может изменить категорию существующей новости")
    public void testEditCategory() {
        String newCategory = TestData.News.CATEGORY_BIRTHDAY;
        Allure.step("Шаг 1: Изменение категории на: " + newCategory);

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        Allure.step("Шаг 2: Ввод новой категории");
        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(replaceText(newCategory));

        Allure.step("Шаг 3: Сохранение изменений");
        editPage.clickSaveButton();
        assertTrue("BUG: News category editing functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        Allure.step("Шаг 4: Проверка видимости отредактированной новости");
        boolean isNewsFound = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News category editing should preserve visibility of edited news", isNewsFound);

        Allure.step("Шаг 5: Проверка, что категория изменилась");
        editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        onView(withText(newCategory)).check(matches(isDisplayed()));

        editPage.cancelWithConfirmation();
        controlPanelPage.isControlPanelDisplayed();
    }

    @Test
    @DisplayName("Изменение 'Title' при редактировании")
    @Description("TC-NEWS-EDIT-03: Изменение 'Title' при редактировании")
    @Story("Пользователь может изменить заголовок существующей новости")
    public void testEditTitle() {
        String updatedTitle = TestData.News.Editing.UPDATED_TITLE_PREFIX + System.currentTimeMillis();
        Allure.step("Шаг 1: Изменение заголовка на: " + updatedTitle);

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        Allure.step("Шаг 2: Ввод нового заголовка");
        editPage.fillTitle(updatedTitle);

        Allure.step("Шаг 3: Сохранение изменений");
        editPage.clickSaveButton();

        Allure.step("Шаг 4: Проверка возврата в Control Panel");
        assertTrue("BUG: News title editing functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        Allure.step("Шаг 5: Проверка, что новость найдена по новому заголовку");
        boolean isNewsFound = controlPanelPage.isNewsDisplayed(updatedTitle);
        assertTrue("BUG: News title editing should update news title correctly", isNewsFound);

        testNewsTitle = updatedTitle;

        Allure.step("Шаг 6: Проверка отображения нового заголовка в списке");
        onView(allOf(
                withId(R.id.news_item_title_text_view),
                withText(updatedTitle),
                isDisplayed()
        )).check(matches(isDisplayed()));
    }

    @Test
    @DisplayName("Изменение статуса 'Active' при редактировании")
    @Description("TC-NEWS-EDIT-04: Изменение статуса 'Active' при редактировании")
    @Story("Пользователь может изменить активный статус новости")
    public void testEditActiveStatus() {
        Allure.step("Шаг 1: Навигация к редактированию новости");
        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        Allure.step("Шаг 2: Проверка, что статус 'Active' включен по умолчанию");
        onView(withId(R.id.switcher)).check(matches(isChecked()));

        Allure.step("Шаг 3: Отключение статуса 'Active'");
        onView(withId(R.id.switcher)).perform(click());
        onView(withId(R.id.switcher)).check(matches(not(isChecked())));

        Allure.step("Шаг 4: Сохранение изменений");
        editPage.clickSaveButton();
        assertTrue("BUG: News active status editing functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        Allure.step("Шаг 5: Проверка видимости отредактированной новости");
        boolean isNewsFound = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News active status editing should preserve visibility of edited news", isNewsFound);

        Allure.step("Шаг 6: Проверка, что статус изменился");
        editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        onView(withId(R.id.switcher)).check(matches(not(isChecked())));

        editPage.cancelWithConfirmation();
        controlPanelPage.isControlPanelDisplayed();
    }

    @Test
    @DisplayName("Изменение 'Description' при редактировании")
    @Description("TC-NEWS-EDIT-05: Изменение 'Description' при редактировании")
    @Story("Пользователь может изменить описание существующей новости")
    public void testEditDescription() {
        String newDescription = TestData.News.Editing.UPDATED_DESCRIPTION_PREFIX + System.currentTimeMillis();
        Allure.step("Шаг 1: Изменение описания новости");

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        Allure.step("Шаг 2: Ввод нового описания");
        editPage.fillDescription(newDescription);

        Allure.step("Шаг 3: Сохранение изменений");
        editPage.clickSaveButton();

        Allure.step("Шаг 4: Проверка возврата в Control Panel");
        assertTrue("BUG: News description editing functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        Allure.step("Шаг 5: Проверка видимости отредактированной новости");
        boolean isNewsFound = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News description editing should preserve visibility of edited news", isNewsFound);

        Allure.step("Шаг 6: Проверка, что описание изменилось");
        editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        onView(withId(R.id.news_item_description_text_input_edit_text))
                .check(matches(withText(newDescription)));

        editPage.cancelWithConfirmation();
        controlPanelPage.isControlPanelDisplayed();
    }

    @Test
    @DisplayName("Отмена редактирования")
    @Description("TC-NEWS-EDIT-06: Отмена редактирования")
    @Story("Пользователь может отменить редактирование новости")
    public void testCancelEditing() {
        String changedTitle = TestData.News.Editing.TITLE_PREFIX_FOR_CANCEL + System.currentTimeMillis();
        Allure.step("Шаг 1: Навигация к редактированию новости");

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        Allure.step("Шаг 2: Изменение заголовка на: " + changedTitle);
        editPage.fillTitle(changedTitle);

        Allure.step("Шаг 3: Нажатие кнопки отмены");
        onView(withId(R.id.cancel_button)).perform(click());

        Allure.step("Шаг 4: Подтверждение отмены в диалоге");
        onView(withText("OK")).perform(click());

        Allure.step("Шаг 5: Проверка возврата в Control Panel");
        assertTrue("BUG: News editing cancellation functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        Allure.step("Шаг 6: Проверка, что исходная новость не изменилась");
        boolean isOriginalNewsDisplayed = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News editing cancellation should preserve original news", isOriginalNewsDisplayed);

        onView(allOf(
                withId(R.id.news_item_title_text_view),
                withText(testNewsTitle),
                isDisplayed()
        )).check(matches(isDisplayed()));
    }
}
