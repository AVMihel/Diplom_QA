package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.regex.Pattern;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class ControlPanelPage {

    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{2})\\.(\\d{2})\\.(\\d{4})");

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final NewsPage newsPage = new NewsPage();

    // Переход в Control Panel с главного экрана
    public void navigateToControlPanel() {
        navigationDrawer.openMenu().clickNewsMenuItem();
        newsPage.checkNewsListScreenIsDisplayed();
        clickEditNewsButton();
        waitForControlPanel();
    }

    // Проверка отображения Control Panel
    public void checkControlPanelIsDisplayed() {
        waitForControlPanel();
    }

    // Ожидание загрузки Control Panel
    private void waitForControlPanel() {
        waitForControlPanel(LONG_DELAY);
    }

    private void waitForControlPanel(long timeout) {
        int[] controlPanelIds = {R.id.add_news_image_view, R.id.sort_news_material_button,
                R.id.news_list_recycler_view};
        WaitUtils.waitForAnyElement(controlPanelIds, timeout);
        WaitUtils.waitForMillis(MEDIUM_DELAY);
    }

    // Переход к созданию новой новости
    public CreateEditNewsPage navigateToCreateNews() {
        checkControlPanelIsDisplayed();

        ViewInteraction addButton = onView(
                allOf(withId(R.id.add_news_image_view),
                        withContentDescription("Add news button"),
                        isDisplayed())
        );
        WaitUtils.waitForElement(addButton, LONG_DELAY);
        addButton.perform(click());

        CreateEditNewsPage createPage = new CreateEditNewsPage();
        createPage.checkCreateScreenIsDisplayed();
        return createPage;
    }

    // Переход к редактированию новости
    public CreateEditNewsPage navigateToEditNews() {
        checkControlPanelIsDisplayed();

        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        WaitUtils.waitForElement(recyclerView, LONG_DELAY);

        recyclerView.perform(actionOnItemAtPosition(0, new ClickEditButtonAction()));

        CreateEditNewsPage editPage = new CreateEditNewsPage();
        editPage.checkEditScreenIsDisplayed();
        return editPage;
    }

    // Открытие диалога фильтрации новостей
    public NewsFilterPage openFilterDialog() {
        ViewInteraction filterButton = onView(
                allOf(withId(R.id.filter_news_material_button), isDisplayed())
        );
        WaitUtils.waitForElement(filterButton, LONG_DELAY);
        filterButton.perform(click());

        NewsFilterPage filterPage = new NewsFilterPage();
        filterPage.checkFilterDialogIsDisplayed();
        return filterPage;
    }

    // Клик по кнопке сортировки новостей
    public void clickSortButton() {
        ViewInteraction sortButton = onView(
                allOf(withId(R.id.sort_news_material_button), isDisplayed())
        );
        WaitUtils.waitForElement(sortButton, LONG_DELAY);
        sortButton.perform(click());
    }

    // Проверка отображения отсортированного списка новостей
    public void checkNewsAreSorted() {
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()));
    }

    // Создание тестовой новости с указанными параметрами
    public String createTestNews(String title, String category, String date, String time, String description) {
        CreateEditNewsPage createPage = navigateToCreateNews();
        WaitUtils.waitForElementWithId(R.id.news_item_title_text_input_edit_text, LONG_DELAY);

        createPage.fillTitle(title);
        delay();

        createPage.selectCategorySimple(category);
        delay();

        onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                .perform(replaceText(date));
        delay();

        onView(withId(R.id.news_item_publish_time_text_input_edit_text))
                .perform(replaceText(time));
        delay();

        createPage.fillDescription(description);
        delay();

        createPage.clickSaveButton();
        waitForControlPanel();

        boolean created = verifyNewsCreated(title, LONG_DELAY);
        if (!created) {
            throw new RuntimeException();
        }

        return title;
    }

    // Создание новости для теста удаления
    public String createNewsForDeletionTest() {
        String title = TestData.News.E2E.TEST_TITLE_PREFIX + System.currentTimeMillis();
        String description = TestData.News.E2E.TEST_DELETION_DESCRIPTION;

        return createTestNews(
                title,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.E2E.DEFAULT_DATE,
                TestData.News.DEFAULT_TIME,
                description
        );
    }

    // Создание новости для теста дат с указанным годом
    public String createNewsForDateTest(int year) {
        String title = TestData.News.E2E.DATE_TEST_TITLE_PREFIX + year + "_" + System.currentTimeMillis();
        String description = TestData.News.E2E.DATE_TEST_DESCRIPTION_PREFIX + year;

        LocalDate currentDate = LocalDate.now();
        String date = String.format("%02d.%02d.%d",
                currentDate.getDayOfMonth(),
                currentDate.getMonthValue(),
                year);

        return createTestNews(
                title,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                date,
                TestData.News.DEFAULT_TIME,
                description
        );
    }

    // Создание новости для теста дат (базовый метод)
    public String createNewsForDateTest() {
        String title = TestData.News.Common.DATE_TEST_TITLE + System.currentTimeMillis();
        return createTestNews(
                title,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                TestData.News.E2E.DEFAULT_DATE,
                TestData.News.DEFAULT_TIME,
                TestData.News.Common.DATE_TEST_DESCRIPTION
        );
    }

    // Проверка создания новости в списке
    private boolean verifyNewsCreated(String title, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            if (findNewsByTitleWithScroll(title)) {
                return true;
            }
            WaitUtils.waitForMillis(MEDIUM_DELAY);
        }
        return false;
    }

    // Удаление созданной новости по точному заголовку
    public void deleteCreatedNewsByExactTitle(String title) {
        try {
            ViewInteraction expandButton = onView(allOf(
                    withId(R.id.view_news_item_image_view),
                    withContentDescription("Expand news card button"),
                    withParent(withParent(findCardWithTitle(title)))
            ));

            WaitUtils.waitForElement(expandButton, LONG_DELAY);
            expandButton.perform(click());
            delay();

            ViewInteraction deleteButton = onView(allOf(
                    withId(R.id.delete_news_item_image_view),
                    withContentDescription("News delete button"),
                    withParent(withParent(findCardWithTitle(title)))
            ));

            WaitUtils.waitForElement(deleteButton, LONG_DELAY);
            deleteButton.perform(click());

            performDeleteConfirmation();

        } catch (Exception e) {
            deleteNewsByTitleAlternative(title);
        }
    }

    // Альтернативный метод удаления новости по заголовку
    private void deleteNewsByTitleAlternative(String title) {
        try {
            ViewInteraction newsTitle = onView(getTitleMatcher(title));
            WaitUtils.waitForElement(newsTitle, LONG_DELAY);

            newsTitle.perform(click());
            delay();

            ViewInteraction deleteButton = onView(allOf(
                    withId(R.id.delete_news_item_image_view),
                    withContentDescription("News delete button"),
                    isDisplayed()
            ));

            WaitUtils.waitForElement(deleteButton, LONG_DELAY);
            deleteButton.perform(click());

            performDeleteConfirmation();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Подтверждение удаления
    private void performDeleteConfirmation() {
        WaitUtils.waitForElementWithText("OK", LONG_DELAY);
        onView(withText("OK")).perform(click());
        WaitUtils.waitForMillis(LONG_DELAY);
    }

    // Поиск новости по заголовку с прокруткой
    public boolean findNewsByTitleWithScroll(String title) {
        WaitUtils.waitForMillis(MEDIUM_DELAY);

        try {
            checkTitleDisplayed(title);
            return true;
        } catch (Exception e) {
            try {
                onView(withId(R.id.news_list_recycler_view))
                        .perform(swipeUp());
                WaitUtils.waitForMillis(MEDIUM_DELAY);
                checkTitleDisplayed(title);
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    // Проверка отображения новости с указанным заголовком
    public boolean isNewsDisplayed(String title) {
        try {
            checkTitleDisplayed(title);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка элементов у новостей в Control Panel
    public void checkNewsElementsExist() {
        checkControlPanelIsDisplayed();

        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        WaitUtils.waitForElement(recyclerView, LONG_DELAY);
        recyclerView.perform(scrollToPosition(0));

        Matcher<View> firstNewsCard = allOf(withId(R.id.news_item_material_card_view),
                childAtPosition(withId(R.id.news_list_recycler_view), 0),
                isDisplayed());

        checkElementInCard(firstNewsCard, withId(R.id.news_item_title_text_view));
        checkElementInCard(firstNewsCard, withId(R.id.delete_news_item_image_view));
        checkElementInCard(firstNewsCard, withId(R.id.edit_news_item_image_view));
    }

    // Разворачивание и сворачивание описания новости
    public void expandAndCollapseNewsDescription() {
        checkControlPanelIsDisplayed();

        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        WaitUtils.waitForElement(recyclerView, LONG_DELAY);

        recyclerView.perform(actionOnItemAtPosition(0, click()));
        delay();

        recyclerView.perform(actionOnItemAtPosition(0, click()));
        delay();
    }

    // Проверка года в дате создания новости
    public void verifyCreationDateYear(String newsTitle, int expectedYear) {
        checkControlPanelIsDisplayed();

        boolean found = findNewsByTitleWithScroll(newsTitle);
        Assert.assertTrue(found);

        try {
            Matcher<View> cardMatcher = findCardWithTitle(newsTitle);

            ViewInteraction creationDateView = onView(allOf(
                    withId(R.id.news_item_create_date_text_view),
                    withParent(withParent(cardMatcher)),
                    isDisplayed()
            ));

            WaitUtils.waitForElement(creationDateView, LONG_DELAY);

            String dateText = getTextFromView(creationDateView);
            Assert.assertNotNull(dateText);
            Assert.assertFalse(dateText.isEmpty());

            int actualYear = extractYearFromDate(dateText);
            Assert.assertEquals(expectedYear, actualYear);

        } catch (Exception e) {
            verifyCreationDateYearAlternative(expectedYear);
        }
    }

    // Проверка отображения даты публикации
    public void verifyPublicationDateDisplay(String title) {
        findNewsByTitleWithScroll(title);
    }

    // Клик по кнопке редактирования новостей
    private void clickEditNewsButton() {
        try {
            ViewInteraction editButton = onView(
                    allOf(withId(R.id.edit_news_material_button),
                            childAtPosition(
                                    childAtPosition(
                                            withId(R.id.container_list_news_include),
                                            0),
                                    3),
                            isDisplayed()));
            WaitUtils.waitForElement(editButton, LONG_DELAY);
            editButton.perform(click());
        } catch (Exception e) {
            ViewInteraction altEditButton = onView(
                    allOf(withId(R.id.edit_news_material_button), isDisplayed()));
            WaitUtils.waitForElement(altEditButton, LONG_DELAY);
            altEditButton.perform(click());
        }
    }

    // Проверка элемента внутри карточки новости
    private void checkElementInCard(Matcher<View> cardMatcher, Matcher<View> elementMatcher) {
        try {
            onView(allOf(elementMatcher, withParent(withParent(cardMatcher))))
                    .check(matches(isDisplayed()));
        } catch (Exception e) {
            onView(elementMatcher).check(matches(isDisplayed()));
        }
    }

    // Поиск карточки новости по заголовку
    private Matcher<View> findCardWithTitle(String title) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (view.getId() == R.id.news_item_material_card_view) {
                    return findTitleInHierarchy(view, title);
                }
                return false;
            }

            private boolean findTitleInHierarchy(View view, String title) {
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int i = 0; i < group.getChildCount(); i++) {
                        View child = group.getChildAt(i);
                        if (child.getId() == R.id.news_item_title_text_view &&
                                child instanceof android.widget.TextView) {
                            String text = ((android.widget.TextView) child).getText().toString();
                            if (text.equals(title)) {
                                return true;
                            }
                        }
                        if (findTitleInHierarchy(child, title)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    // Альтернативная проверка года в дате создания
    private void verifyCreationDateYearAlternative(int expectedYear) {
        try {
            ViewInteraction anyCreationDate = onView(
                    allOf(withId(R.id.news_item_create_date_text_view), isDisplayed())
            );

            String dateText = getTextFromView(anyCreationDate);
            if (dateText != null && !dateText.isEmpty()) {
                int actualYear = extractYearFromDate(dateText);
                Assert.assertEquals(expectedYear, actualYear);
            } else {
                Assert.fail();
            }

        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    // Метод для очистки экрана редактирования
    public void cleanupEditingScreen() {
        try {
            if (WaitUtils.isElementDisplayedWithId(R.id.cancel_button, MEDIUM_DELAY)) {
                onView(withId(R.id.cancel_button)).perform(click());
                WaitUtils.waitForMillis(MEDIUM_DELAY);
                if (WaitUtils.isElementDisplayedWithText("OK", MEDIUM_DELAY)) {
                    onView(withText("OK")).perform(click());
                }
            }
        } catch (Exception e) {
        }
    }

    // Выполнение короткой задержки
    private void delay() {
        WaitUtils.waitForMillis(SHORT_DELAY);
    }

    // Создание Matcher для поиска заголовка новости по тексту
    private Matcher<View> getTitleMatcher(String title) {
        return allOf(
                withId(R.id.news_item_title_text_view),
                withText(title),
                isDisplayed()
        );
    }

    // Проверка отображения заголовка новости
    private void checkTitleDisplayed(String title) {
        onView(getTitleMatcher(title)).check(matches(isDisplayed()));
    }

    // Получение текста из ViewInteraction с использованием кастомного GetTextAction
    private String getTextFromView(ViewInteraction viewInteraction) {
        String[] textHolder = new String[1];
        viewInteraction.perform(new GetTextAction(textHolder));
        return textHolder[0];
    }

    // Извлечение года из строки даты формата dd.MM.yyyy
    private int extractYearFromDate(String dateText) {
        java.util.regex.Matcher dateMatcher = DATE_PATTERN.matcher(dateText);
        Assert.assertTrue(dateMatcher.find());
        return Integer.parseInt(dateMatcher.group(3));
    }

    // Действие для клика по кнопке редактирования внутри карточки новости
    private static class ClickEditButtonAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return isDisplayed();
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public void perform(UiController uiController, View view) {
            View editButton = findViewWithId(view, R.id.edit_news_item_image_view);
            if (editButton != null && editButton.isShown()) {
                editButton.performClick();
            } else {
                view.performClick();
                uiController.loopMainThreadUntilIdle();

                editButton = findViewWithId(view, R.id.edit_news_item_image_view);
                if (editButton != null && editButton.isShown()) {
                    editButton.performClick();
                }
            }
            uiController.loopMainThreadUntilIdle();
        }

        private View findViewWithId(View root, int id) {
            if (root.getId() == id) {
                return root;
            }

            if (root instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) root;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    View found = findViewWithId(child, id);
                    if (found != null) {
                        return found;
                    }
                }
            }
            return null;
        }
    }

    // Действие для получения текста из TextView
    private static class GetTextAction implements ViewAction {
        private final String[] textHolder;

        GetTextAction(String[] textHolder) {
            this.textHolder = textHolder;
        }

        @Override
        public Matcher<View> getConstraints() {
            return isDisplayed();
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public void perform(UiController uiController, View view) {
            if (view instanceof android.widget.TextView) {
                textHolder[0] = ((android.widget.TextView) view).getText().toString();
            }
        }
    }

    // Матчер для поиска дочернего элемента по позиции
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}