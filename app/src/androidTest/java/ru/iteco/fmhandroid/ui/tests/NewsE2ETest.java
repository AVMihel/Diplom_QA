package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;

public class NewsE2ETest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();

    // TC-NEWS-CREATE-01: Полный E2E сценарий создания новости
    @Test
    public void testE2ENewsCreation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        TestData.NewsItem testNews = TestData.News.E2E_NEWS;

        onView(allOf(withId(R.id.add_news_image_view)))
                .check(matches(isDisplayed()))
                .perform(click());

        CreateEditNewsPage createPage = new CreateEditNewsPage();

        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .perform(replaceText(testNews.category));

        onView(withId(R.id.news_item_title_text_input_edit_text))
                .perform(replaceText(testNews.title));

        createPage.selectCurrentDate();
        createPage.selectCurrentTime();

        onView(withId(R.id.news_item_description_text_input_edit_text))
                .perform(replaceText(testNews.description));

        onView(withId(R.id.save_button)).perform(click());

        controlPanelPage.checkControlPanelIsDisplayed();
        controlPanelPage.findNewsByTitleWithScroll(testNews.title);

        controlPanelPage.deleteCreatedNewsByExactTitle(testNews.title);
    }

    // TC-NEWS-CP-05: Удаление новости
    @Test
    public void testE2ENewsDeletion() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        String testTitle = controlPanelPage.createNewsForDeletionTest();

        try {
            controlPanelPage.findNewsByTitleWithScroll(testTitle);
        } finally {
            controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);
        }
    }

    // TC-NEWS-EDIT-01: Предзаполнение полей при редактировании
    @Test
    public void testE2ENewsEditing() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        String originalTitle = TestData.News.E2E.ORIGINAL_TITLE_PREFIX + System.currentTimeMillis();

        String createdTitle = controlPanelPage.createTestNews(
                originalTitle,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.E2E.DEFAULT_DATE,
                TestData.News.DEFAULT_TIME,
                TestData.News.Editing.EDITING_ORIGINAL_DESCRIPTION
        );

        try {
            controlPanelPage.findNewsByTitleWithScroll(createdTitle);

            CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
            editPage.checkEditScreenIsDisplayed();

            editPage.checkTitleFieldDisplayed();
            editPage.checkCategoryFieldDisplayed();

            String updatedTitle = TestData.News.E2E.UPDATED_TITLE_PREFIX + System.currentTimeMillis();
            editPage.fillTitle(updatedTitle);
            editPage.clickSaveButton();

            controlPanelPage.checkControlPanelIsDisplayed();
            controlPanelPage.findNewsByTitleWithScroll(updatedTitle);

            controlPanelPage.deleteCreatedNewsByExactTitle(updatedTitle);
        } catch (Exception e) {
            controlPanelPage.deleteCreatedNewsByExactTitle(createdTitle);
            throw e;
        }
    }
}