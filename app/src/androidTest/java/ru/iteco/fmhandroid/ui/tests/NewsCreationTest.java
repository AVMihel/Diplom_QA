package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsCreationTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();

    // TC-NEWS-CREATE-08: Валидация поля "Publication date": нельзя выбрать прошедшую дату
    @Test
    public void testPublicationDatePastDateValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        try {
            CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
            createPage.checkCreateScreenIsDisplayed();

            String pastDate = TestData.NewsCreation.getPastDateString();

            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText(TestData.News.Validation.LENGTH_TEST_TITLE_PREFIX + System.currentTimeMillis()));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                    .perform(replaceText(pastDate));

            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText(TestData.News.Validation.LENGTH_TEST_DESCRIPTION));

            onView(withId(R.id.save_button)).perform(click());

            WaitUtils.waitForMillis(1000);

            if (!createPage.isStillOnEditScreen()) {
                throw new AssertionError("БАГ: Прошедшая дата принята как валидная");
            }

        } finally {
            pressBack();
        }
    }

    // TC-NEWS-CREATE-09: Валидация поля "Category": ручной ввод текста
    @Test
    public void testCategoryManualInputValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        try {
            CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
            createPage.checkCreateScreenIsDisplayed();

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.NewsCreation.INVALID_CATEGORY));

            createPage.fillTitle(TestData.News.Validation.MANUAL_CATEGORY_TEST_TITLE_PREFIX + System.currentTimeMillis())
                    .selectCurrentDate()
                    .selectCurrentTime()
                    .fillDescription(TestData.News.Validation.MANUAL_CATEGORY_TEST_DESCRIPTION)
                    .clickSaveButton();

            WaitUtils.waitForMillis(1000);

            if (!createPage.isValidationErrorDisplayed()) {
                throw new AssertionError("БАГ: Ручной ввод категории принят как валидный");
            }

        } finally {
            pressBack();
        }
    }

    // TC-NEWS-CREATE-10: Проверка выбора категории из выпадающего списка
    @Test
    public void testCategorySelectionFromDropdown() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        try {
            CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
            createPage.checkCreateScreenIsDisplayed();

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .check(matches(withText("")));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(click());

            createPage.selectCategorySimple(TestData.News.CATEGORY_ANNOUNCEMENT);

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .check(matches(withText(TestData.News.CATEGORY_ANNOUNCEMENT)));

        } finally {
            pressBack();
        }
    }

    // TC-NEWS-CREATE-11: Валидация длины поля "Title"
    @Test
    public void testTitleLengthValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        try {
            CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
            createPage.checkCreateScreenIsDisplayed();

            String longTitle = TestData.NewsCreation.getVeryLongTitle();

            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText(longTitle));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText(TestData.News.Validation.LENGTH_TEST_DESCRIPTION));

            onView(withId(R.id.save_button)).perform(click());

            WaitUtils.waitForMillis(1000);

            if (!createPage.isStillOnEditScreen()) {
                throw new AssertionError("БАГ: Слишком длинный заголовок принят как валидный");
            }

        } finally {
            pressBack();
        }
    }

    // TC-NEWS-CREATE-12: Валидация поля "Title" из спецсимволов
    @Test
    public void testTitleSpecialCharactersValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        try {
            CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
            createPage.checkCreateScreenIsDisplayed();

            String testTitle = TestData.NewsCreation.SPECIAL_CHARS_TITLE;

            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText(testTitle));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText(TestData.News.Validation.SPECIAL_CHARS_TEST_DESCRIPTION));

            onView(withId(R.id.save_button)).perform(click());

            WaitUtils.waitForMillis(1000);

            if (!createPage.isStillOnEditScreen()) {
                throw new AssertionError("БАГ: Спецсимволы приняты как валидный заголовок");
            }

        } finally {
            pressBack();
        }
    }

    // TC-NEWS-CREATE-13: Валидация "Title" из пробелов
    @Test
    public void testTitleSpacesOnlyValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        try {
            CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
            createPage.checkCreateScreenIsDisplayed();

            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText(TestData.NewsCreation.SPACES_ONLY_TITLE));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText(TestData.News.Validation.SPACES_TEST_DESCRIPTION));

            onView(withId(R.id.save_button)).perform(click());

            WaitUtils.waitForMillis(1000);

            if (!createPage.isStillOnEditScreen()) {
                throw new AssertionError("БАГ: Заголовок из пробелов принят как валидный");
            }

        } finally {
            pressBack();
        }
    }

    // TC-NEWS-CREATE-14: Проверка многострочного "Description"
    @Test
    public void testMultilineDescription() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        String testTitle = TestData.News.Validation.MULTILINE_TEST_TITLE_PREFIX + System.currentTimeMillis();

        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    TestData.News.DEFAULT_TIME,
                    TestData.NewsCreation.MULTILINE_DESCRIPTION
            );

            controlPanelPage.findNewsByTitleWithScroll(createdTitle);
        } finally {
            controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);
        }
    }

    // TC-NEWS-CREATE-15: Отмена создания новости
    @Test
    public void testCancelNewsCreation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.checkCreateScreenIsDisplayed();

        String testTitle = TestData.News.Validation.CANCEL_TEST_TITLE_PREFIX + System.currentTimeMillis();

        onView(withId(R.id.news_item_title_text_input_edit_text))
                .check(matches(isDisplayed()))
                .perform(replaceText(testTitle));

        onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                .check(matches(isDisplayed()))
                .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

        onView(withId(R.id.cancel_button))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText("OK")).perform(click());

        controlPanelPage.checkControlPanelIsDisplayed();
    }
}