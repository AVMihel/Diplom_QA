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
        setUpToAuthScreen();
        loginAndGoToMainScreen();

        controlPanelPage.navigateToControlPanelFromMain(navigationDrawer, newsPage);

        testNewsTitle = "Тест_редактирование_" + System.currentTimeMillis();

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
                controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
                boolean isNewsDeleted = !controlPanelPage.isNewsDisplayed(testNewsTitle);
                assertTrue("Test news should be deleted", isNewsDeleted);
            } catch (Exception e) {
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

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(replaceText(newCategory));

        editPage.clickSaveButton();
        assertTrue("BUG: News category editing functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        boolean isNewsFound = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News category editing should preserve visibility of edited news", isNewsFound);

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

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        editPage.fillTitle(updatedTitle);
        editPage.clickSaveButton();

        assertTrue("BUG: News title editing functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        boolean isNewsFound = controlPanelPage.isNewsDisplayed(updatedTitle);
        assertTrue("BUG: News title editing should update news title correctly", isNewsFound);

        testNewsTitle = updatedTitle;

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
        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        onView(withId(R.id.switcher)).check(matches(isChecked()));

        onView(withId(R.id.switcher)).perform(click());
        onView(withId(R.id.switcher)).check(matches(not(isChecked())));

        editPage.clickSaveButton();
        assertTrue("BUG: News active status editing functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        boolean isNewsFound = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News active status editing should preserve visibility of edited news", isNewsFound);

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

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        editPage.fillDescription(newDescription);
        editPage.clickSaveButton();

        assertTrue("BUG: News description editing functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        boolean isNewsFound = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News description editing should preserve visibility of edited news", isNewsFound);

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

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.isEditScreenDisplayed();

        editPage.fillTitle(changedTitle);
        onView(withId(R.id.cancel_button)).perform(click());

        onView(withText("OK")).perform(click());
        assertTrue("BUG: News editing cancellation functionality should work correctly",
                controlPanelPage.isControlPanelDisplayed());

        boolean isOriginalNewsDisplayed = controlPanelPage.isNewsDisplayed(testNewsTitle);
        assertTrue("BUG: News editing cancellation should preserve original news", isOriginalNewsDisplayed);

        onView(allOf(
                withId(R.id.news_item_title_text_view),
                withText(testNewsTitle),
                isDisplayed()
        )).check(matches(isDisplayed()));
    }
}
