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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;

public class NewsEditingTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();
    private String testNewsTitle;

    @Before
    public void setUpTestNews() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        testNewsTitle = "Тест_редактирование_" + System.currentTimeMillis();

        testNewsTitle = controlPanelPage.createTestNews(
                testNewsTitle,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.getFutureDate(1),
                TestData.News.getTime(),
                TestData.News.Editing.ORIGINAL_DESCRIPTION
        );

        controlPanelPage.findNewsByTitleWithScroll(testNewsTitle);
    }

    @After
    public void cleanUpTestNews() {
        if (testNewsTitle != null) {
            try {
                controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
            } catch (Exception e) {
                testNewsTitle = null;
            }
        }
    }

    // TC-NEWS-EDIT-02: Изменение "Category" при редактировании
    @Test
    public void testEditCategory() {
        String newCategory = TestData.News.CATEGORY_BIRTHDAY;

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(replaceText(newCategory));

        editPage.clickSaveButton();

        controlPanelPage.checkControlPanelIsDisplayed();

        controlPanelPage.findNewsByTitleWithScroll(testNewsTitle);

        editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        onView(withText(newCategory)).check(matches(isDisplayed()));

        editPage.cancelWithConfirmation();

        controlPanelPage.checkControlPanelIsDisplayed();
        controlPanelPage.findNewsByTitleWithScroll(testNewsTitle);
    }

    // TC-NEWS-EDIT-03: Изменение "Title" при редактировании
    @Test
    public void testEditTitle() {
        String originalTitle = testNewsTitle;
        String updatedTitle = TestData.News.Editing.UPDATED_TITLE_PREFIX + System.currentTimeMillis();

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        editPage.fillTitle(updatedTitle);
        editPage.clickSaveButton();

        controlPanelPage.checkControlPanelIsDisplayed();

        controlPanelPage.findNewsByTitleWithScroll(updatedTitle);

        controlPanelPage.isNewsDisplayed(originalTitle);

        onView(allOf(
                withId(R.id.news_item_title_text_view),
                withText(updatedTitle),
                isDisplayed()
        )).check(matches(isDisplayed()));

        testNewsTitle = updatedTitle;
    }

    // TC-NEWS-EDIT-04: Изменение статуса "Active" при редактировании
    @Test
    public void testEditActiveStatus() {
        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        onView(withId(R.id.switcher)).check(matches(isChecked()));

        onView(withId(R.id.switcher)).perform(click());
        onView(withId(R.id.switcher)).check(matches(not(isChecked())));

        editPage.clickSaveButton();
        controlPanelPage.checkControlPanelIsDisplayed();
        controlPanelPage.findNewsByTitleWithScroll(testNewsTitle);

        editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        onView(withId(R.id.switcher)).check(matches(not(isChecked())));

        editPage.cancelWithConfirmation();
        controlPanelPage.checkControlPanelIsDisplayed();
        controlPanelPage.findNewsByTitleWithScroll(testNewsTitle);
    }

    // TC-NEWS-EDIT-05: Изменение "Description" при редактировании
    @Test
    public void testEditDescription() {
        String newDescription = TestData.News.Editing.UPDATED_DESCRIPTION_PREFIX + System.currentTimeMillis();

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        editPage.fillDescription(newDescription);
        editPage.clickSaveButton();

        controlPanelPage.checkControlPanelIsDisplayed();
        controlPanelPage.findNewsByTitleWithScroll(testNewsTitle);

        editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        onView(withId(R.id.news_item_description_text_input_edit_text))
                .check(matches(withText(newDescription)));

        editPage.cancelWithConfirmation();
        controlPanelPage.checkControlPanelIsDisplayed();
        controlPanelPage.findNewsByTitleWithScroll(testNewsTitle);
    }

    // TC-NEWS-EDIT-06: Отмена редактирования
    @Test
    public void testCancelEditing() {
        String originalTitle = testNewsTitle;
        String changedTitle = TestData.News.Editing.TITLE_PREFIX_FOR_CANCEL + System.currentTimeMillis();

        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();

        editPage.fillTitle(changedTitle);
        onView(withId(R.id.cancel_button)).perform(click());

        onView(withText("OK")).perform(click());
        controlPanelPage.checkControlPanelIsDisplayed();

        controlPanelPage.isNewsDisplayed(originalTitle);

        onView(allOf(
                withId(R.id.news_item_title_text_view),
                withText(originalTitle),
                isDisplayed()
        )).check(matches(isDisplayed()));
    }
}
