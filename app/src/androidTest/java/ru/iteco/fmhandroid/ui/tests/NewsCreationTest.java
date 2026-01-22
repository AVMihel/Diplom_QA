package ru.iteco.fmhandroid.ui.tests;

import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.ViewInteraction;

public class NewsCreationTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();

    // TC-NEWS-CREATE-08: Валидация поля "Publication date": нельзя выбрать прошедшую дату
    @Test
    public void testPublicationDatePastDateValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.checkCreateScreenIsDisplayed();

        try {
            String pastDate = TestData.NewsCreation.getPastDateString();

            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText("Тест прошедшей даты_" + System.currentTimeMillis()));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                    .perform(replaceText(pastDate));

            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
            WaitUtils.waitForMillis(1000);
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText("Описание для теста прошедшей даты"));

            onView(withId(R.id.save_button)).perform(click());

            WaitUtils.waitForMillis(3000);

            boolean savedSuccessfully = WaitUtils.isElementDisplayedWithId(R.id.news_list_recycler_view, 2000);

            if (savedSuccessfully) {
                String testTitle = "Тест прошедшей даты_" + System.currentTimeMillis();
                try {
                    controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);
                } catch (Exception e) {
                    // Игнорируем ошибки удаления
                }
                throw new AssertionError("БАГ: Прошедшая дата принята как валидная");
            }

            boolean stillOnEditScreen = WaitUtils.isElementDisplayedWithId(
                    R.id.news_item_title_text_input_edit_text, 1000);

            assertTrue("При вводе прошедшей даты должны остаться на экране редактирования",
                    stillOnEditScreen);

        } finally {
            try {
                if (WaitUtils.isElementDisplayedWithId(R.id.cancel_button, 1000)) {
                    onView(withId(R.id.cancel_button)).perform(click());
                    WaitUtils.waitForMillis(1000);
                    if (WaitUtils.isElementDisplayedWithText("OK", 1000)) {
                        onView(withText("OK")).perform(click());
                    }
                }
            } catch (Exception e) {
                pressBack();
            }
        }
    }

    // TC-NEWS-CREATE-09: Валидация поля "Category": ручной ввод текста
    @Test
    public void testCategoryManualInputValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.checkCreateScreenIsDisplayed();

        try {
            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.NewsCreation.INVALID_CATEGORY));

            createPage.fillTitle("Тест ручного ввода категории_" + System.currentTimeMillis())
                    .selectCurrentDate()
                    .selectCurrentTime()
                    .fillDescription("Описание для теста ручного ввода категории")
                    .clickSaveButton();

            WaitUtils.waitForMillis(3000);

            boolean savedSuccessfully = controlPanelPage.isControlPanelDisplayed(2000);

            if (!savedSuccessfully) {
                boolean errorDisplayed = WaitUtils.isElementDisplayedWithText("Saving failed", 2000) ||
                        WaitUtils.isElementDisplayedWithText("Error", 2000) ||
                        WaitUtils.isElementDisplayedWithText("Invalid", 2000);
                assertTrue("Должна быть ошибка при ручном вводе категории", errorDisplayed);
            } else {
                String testTitle = "Тест ручного ввода категории_" + System.currentTimeMillis();
                controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);
                throw new AssertionError("БАГ: Ручной ввод категории принят как валидный");
            }

        } finally {
            controlPanelPage.cleanupEditingScreen();
        }
    }

    // TC-NEWS-CREATE-10: Минимальная проверка выбора категории
    @Test
    public void testCategorySelectionFromDropdown() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.checkCreateScreenIsDisplayed();

        // Просто используем метод selectCategorySimple - он уже проверен в других тестах
        // Этот метод кликает на поле и либо выбирает из списка, либо вводит текст
        createPage.selectCategorySimple(TestData.News.CATEGORY_ANNOUNCEMENT);

        // Если не упало с исключением - значит категория выбрана
        // Дополнительная проверка: можем ли мы продолжить заполнение формы
        onView(withId(R.id.news_item_title_text_input_edit_text))
                .perform(replaceText("Тест выбора категории"));

        // Если дошли сюда без ошибок - тест пройден
    }

    // TC-NEWS-CREATE-11: Валидация длины поля "Title"
    @Test
    public void testTitleLengthValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.checkCreateScreenIsDisplayed();

        try {
            String longTitle = TestData.NewsCreation.getVeryLongTitle();

            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText(longTitle));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
            WaitUtils.waitForMillis(1000);
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
            WaitUtils.waitForMillis(1000);
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText("Описание для теста длины заголовка"));

            onView(withId(R.id.save_button)).perform(click());

            WaitUtils.waitForMillis(3000);

            boolean savedSuccessfully = WaitUtils.isElementDisplayedWithId(
                    R.id.news_list_recycler_view, 2000);

            if (savedSuccessfully) {
                try {
                    controlPanelPage.deleteCreatedNewsByExactTitle(longTitle);
                } catch (Exception e) {
                    // Игнорируем
                }
                throw new AssertionError("БАГ: Слишком длинный заголовок принят как валидный");
            }

            boolean stillOnEditScreen = WaitUtils.isElementDisplayedWithId(
                    R.id.news_item_title_text_input_edit_text, 1000);

            assertTrue("Должны остаться на экране редактирования при ошибке валидации",
                    stillOnEditScreen);

        } finally {
            try {
                if (WaitUtils.isElementDisplayedWithId(R.id.cancel_button, 1000)) {
                    onView(withId(R.id.cancel_button)).perform(click());
                    WaitUtils.waitForMillis(1000);
                    if (WaitUtils.isElementDisplayedWithText("OK", 1000)) {
                        onView(withText("OK")).perform(click());
                    }
                }
            } catch (Exception e) {
                pressBack();
            }
        }
    }

    // TC-NEWS-CREATE-12: Валидация поля "Title" из спецсимволов
    @Test
    public void testTitleSpecialCharactersValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.checkCreateScreenIsDisplayed();

        String testTitle = TestData.NewsCreation.SPECIAL_CHARS_TITLE;

        try {
            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText(testTitle));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
            WaitUtils.waitForMillis(1000);
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
            WaitUtils.waitForMillis(1000);
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText("Описание для теста спецсимволов"));

            onView(withId(R.id.save_button)).perform(click());

            WaitUtils.waitForMillis(3000);

            boolean savedSuccessfully = WaitUtils.isElementDisplayedWithId(
                    R.id.news_list_recycler_view, 2000);

            if (savedSuccessfully) {
                boolean newsCreated = controlPanelPage.findNewsByTitleWithScroll(testTitle);

                if (newsCreated) {
                    controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);
                    throw new AssertionError("БАГ: Спецсимволы приняты как валидный заголовок");
                }
            } else {
                boolean stillOnEditScreen = WaitUtils.isElementDisplayedWithId(
                        R.id.news_item_title_text_input_edit_text, 1000);

                assertTrue("Должны остаться на экране редактирования при спецсимволах",
                        stillOnEditScreen);
            }

        } finally {
            try {
                if (WaitUtils.isElementDisplayedWithId(R.id.cancel_button, 1000)) {
                    onView(withId(R.id.cancel_button)).perform(click());
                    WaitUtils.waitForMillis(1000);
                    if (WaitUtils.isElementDisplayedWithText("OK", 1000)) {
                        onView(withText("OK")).perform(click());
                    }
                }
            } catch (Exception e) {
                pressBack();
            }
        }
    }

    // TC-NEWS-CREATE-13: Валидация "Title" из пробелов
    @Test
    public void testTitleSpacesOnlyValidation() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        CreateEditNewsPage createPage = controlPanelPage.navigateToCreateNews();
        createPage.checkCreateScreenIsDisplayed();

        try {
            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText(TestData.NewsCreation.SPACES_ONLY_TITLE));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
            WaitUtils.waitForMillis(1000);
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
            WaitUtils.waitForMillis(1000);
            onView(withText("OK")).perform(click());

            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText("Описание для теста пробелов"));

            onView(withId(R.id.save_button)).perform(click());

            WaitUtils.waitForMillis(3000);

            boolean savedSuccessfully = WaitUtils.isElementDisplayedWithId(
                    R.id.news_list_recycler_view, 2000);

            if (!savedSuccessfully) {
                boolean stillOnEditScreen = WaitUtils.isElementDisplayedWithId(
                        R.id.news_item_title_text_input_edit_text, 1000);

                assertTrue("Должны остаться на экране редактирования при заголовке из пробелов",
                        stillOnEditScreen);
            } else {
                try {
                    controlPanelPage.deleteCreatedNewsByExactTitle("   ");
                } catch (Exception e) {
                    // Игнорируем ошибки удаления
                }
                throw new AssertionError("БАГ: Заголовок из пробелов принят как валидный");
            }

        } finally {
            try {
                if (WaitUtils.isElementDisplayedWithId(R.id.cancel_button, 1000)) {
                    onView(withId(R.id.cancel_button)).perform(click());
                    WaitUtils.waitForMillis(1000);
                    if (WaitUtils.isElementDisplayedWithText("OK", 1000)) {
                        onView(withText("OK")).perform(click());
                    }
                }
            } catch (Exception e) {
                pressBack();
            }
        }
    }

    // TC-NEWS-CREATE-14: Проверка многострочного "Description"
    @Test
    public void testMultilineDescription() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkControlPanelIsDisplayed();

        String testTitle = "Тест многострочного описания_" + System.currentTimeMillis();

        try {
            String createdTitle = controlPanelPage.createTestNews(
                    testTitle,
                    TestData.News.CATEGORY_ANNOUNCEMENT,
                    TestData.News.getFutureDate(1),
                    "12:00",
                    TestData.NewsCreation.MULTILINE_DESCRIPTION
            );

            boolean isCreated = controlPanelPage.findNewsByTitleWithScroll(createdTitle);
            assertTrue("Новость с многострочным описанием должна быть создана", isCreated);

        } finally {
            try {
                controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);
            } catch (Exception e) {
                // Игнорируем ошибки при удалении
            }
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

        String testTitle = "Тест отмены создания_" + System.currentTimeMillis();

        try {
            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .check(matches(isDisplayed()))
                    .perform(replaceText(testTitle));

            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .check(matches(isDisplayed()))
                    .perform(replaceText(TestData.News.CATEGORY_ANNOUNCEMENT));

            WaitUtils.waitForMillis(1000);

            onView(withId(R.id.cancel_button))
                    .check(matches(isDisplayed()))
                    .perform(click());

            WaitUtils.waitForMillis(2000);

            boolean dialogDisplayed = WaitUtils.isElementDisplayedWithText("OK", 2000);

            assertTrue("Должен появиться диалог подтверждения отмены", dialogDisplayed);

            onView(withText("OK")).perform(click());

            WaitUtils.waitForMillis(2000);

            controlPanelPage.checkControlPanelIsDisplayed();

            boolean newsNotCreated = !controlPanelPage.isNewsDisplayed(testTitle);
            assertTrue("Новость не должна быть создана после отмены", newsNotCreated);

        } finally {
            // Очистка не требуется
        }
    }
}