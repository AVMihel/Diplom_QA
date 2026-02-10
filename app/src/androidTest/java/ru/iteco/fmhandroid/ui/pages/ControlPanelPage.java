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
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import io.qameta.allure.kotlin.Step;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

@DisplayName("Страница Control Panel для управления новостями")
public class ControlPanelPage {

    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{2})\\.(\\d{2})\\.(\\d{4})");

    private static final int ADD_NEWS_BUTTON_ID = R.id.add_news_image_view;
    private static final int SORT_BUTTON_ID = R.id.sort_news_material_button;
    private static final int FILTER_BUTTON_ID = R.id.filter_news_material_button;
    private static final int NEWS_LIST_RECYCLER_ID = R.id.news_list_recycler_view;
    private static final int EDIT_NEWS_BUTTON_ID = R.id.edit_news_material_button;
    private static final int NEWS_CARD_ID = R.id.news_item_material_card_view;
    private static final int NEWS_TITLE_VIEW_ID = R.id.news_item_title_text_view;
    private static final int DELETE_NEWS_ICON_ID = R.id.delete_news_item_image_view;
    private static final int EDIT_NEWS_ICON_ID = R.id.edit_news_item_image_view;
    private static final int EXPAND_NEWS_ICON_ID = R.id.view_news_item_image_view;
    private static final int CREATION_DATE_ID = R.id.news_item_create_date_text_view;
    private static final int[] CONTROL_PANEL_IDS = {ADD_NEWS_BUTTON_ID, SORT_BUTTON_ID, NEWS_LIST_RECYCLER_ID};


    @Step("Навигация в Control Panel с главного экрана")
    public ControlPanelPage navigateToControlPanelFromMain(NavigationDrawerPage navigationDrawer, NewsPage newsPage) {
        navigationDrawer.openMenu().clickNewsMenuItem();
        assertTrue("News page should be displayed", newsPage.isSortButtonDisplayed());
        clickEditNewsButton();
        assertTrue("Control Panel should be displayed", isControlPanelDisplayed());
        return this;
    }
    @Step("Проверка отображения Control Panel")
    public void checkControlPanelIsDisplayed() {
        WaitUtils.waitForAnyElement(CONTROL_PANEL_IDS, LONG_DELAY);
    }

    @Step("Проверка отображения Control Panel (возвращает 'boolean')")
    public boolean isControlPanelDisplayed() {
        try {
            checkControlPanelIsDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Нажатие кнопки редактирования новостей")
    public void clickEditNewsButton() {
        ViewInteraction editButton = onView(allOf(withId(EDIT_NEWS_BUTTON_ID), isDisplayed()));
        WaitUtils.waitForElement(editButton, LONG_DELAY);
        editButton.perform(click());
    }

    @Step("Навигация к созданию новой новости")
    public CreateEditNewsPage navigateToCreateNews() {
        checkControlPanelIsDisplayed();
        ViewInteraction addButton = onView(allOf(withId(ADD_NEWS_BUTTON_ID), withContentDescription("Add news button"), isDisplayed()));
        WaitUtils.waitForElement(addButton, LONG_DELAY);
        addButton.perform(click());

        CreateEditNewsPage createPage = new CreateEditNewsPage();
        createPage.checkCreateScreenIsDisplayed();
        return createPage;
    }

    @Step("Навигация к редактированию новости")
    public CreateEditNewsPage navigateToEditNews() {
        checkControlPanelIsDisplayed();
        ViewInteraction recyclerView = onView(allOf(withId(NEWS_LIST_RECYCLER_ID), isDisplayed()));
        WaitUtils.waitForElement(recyclerView, LONG_DELAY);
        recyclerView.perform(actionOnItemAtPosition(0, new ClickEditButtonAction()));

        CreateEditNewsPage editPage = new CreateEditNewsPage();
        editPage.checkEditScreenIsDisplayed();
        return editPage;
    }

    @Step("Открыть фильтр новостей")
    public void openNewsFilter() {
        ViewInteraction filterButton = onView(allOf(withId(FILTER_BUTTON_ID), isDisplayed()));
        WaitUtils.waitForElement(filterButton, LONG_DELAY);
        filterButton.perform(click());
        WaitUtils.waitForElementWithId(R.id.filter_news_title_text_view, LONG_DELAY);
    }

    @Step("Клик по кнопке сортировки новостей")
    public void clickSortButton() {
        ViewInteraction sortButton = onView(allOf(withId(SORT_BUTTON_ID), isDisplayed()));
        WaitUtils.waitForElement(sortButton, LONG_DELAY);
        sortButton.perform(click());
    }

    @Step("Создание тестовой новости")
    public String createTestNews(String title, String category, String date, String time, String description) {
        CreateEditNewsPage createPage = navigateToCreateNews();
        WaitUtils.waitForElementWithId(R.id.news_item_title_text_input_edit_text, LONG_DELAY);

        createPage.fillTitle(title);
        WaitUtils.waitForMillis(SHORT_DELAY);
        createPage.selectCategorySimple(category);
        WaitUtils.waitForMillis(SHORT_DELAY);
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(replaceText(date));
        WaitUtils.waitForMillis(SHORT_DELAY);
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(replaceText(time));
        WaitUtils.waitForMillis(SHORT_DELAY);
        createPage.fillDescription(description);
        WaitUtils.waitForMillis(SHORT_DELAY);

        createPage.clickSaveButton();
        checkControlPanelIsDisplayed();

        boolean created = verifyNewsCreated(title);
        assertTrue("Failed to create news with title: " + title, created);
        return title;
    }

    @Step("Проверка создания новости")
    public boolean verifyNewsCreated(String title) {
        return verifyNewsCreatedWithTimeout(title, LONG_DELAY);
    }

    @Step("Создание новости для теста удаления")
    public String createNewsForDeletionTest() {
        String title = TestData.News.E2E.TEST_TITLE_PREFIX + System.currentTimeMillis();
        return createTestNewsWithCalendar(
                title,
                TestData.News.CATEGORY_ANNOUNCEMENT,
                1,
                TestData.News.DEFAULT_TIME,
                TestData.News.E2E.TEST_DELETION_DESCRIPTION
        );
    }

    @Step("Создание новости для теста дат")
    public String createNewsForDateTest(int year) {
        String title = TestData.News.E2E.DATE_TEST_TITLE_PREFIX + year + "_" + System.currentTimeMillis();
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
                TestData.News.E2E.DATE_TEST_DESCRIPTION_PREFIX + year
        );
    }

    @Step("Удаление новости по заголовку")
    public void deleteCreatedNewsByExactTitle(String title) {
        scrollToTop();
        WaitUtils.waitForMillis(1000);

        boolean found = false;
        for (int i = 0; i < 3; i++) {
            found = findNewsByTitleWithScroll(title);
            if (found) break;
            scrollToTop();
            WaitUtils.waitForMillis(1000);
        }

        if (!found) {
            throw new RuntimeException("News not found: " + title);
        }

        Matcher<View> cardMatcher = findCardWithTitle(title);
        ViewInteraction expandButton = onView(allOf(
                withId(EXPAND_NEWS_ICON_ID),
                withContentDescription("Expand news card button"),
                withParent(withParent(cardMatcher))
        ));

        WaitUtils.waitForElement(expandButton, LONG_DELAY);
        expandButton.perform(click());
        WaitUtils.waitForMillis(800);

        ViewInteraction deleteButton = onView(allOf(
                withId(DELETE_NEWS_ICON_ID),
                withContentDescription("News delete button"),
                withParent(withParent(cardMatcher))
        ));

        WaitUtils.waitForElement(deleteButton, LONG_DELAY);
        deleteButton.perform(click());
        WaitUtils.waitForMillis(500);
        performDeleteConfirmation();
    }

    @Step("Поиск новости в списке")
    public boolean findNewsByTitleWithScroll(String title) {
        try {
            if (WaitUtils.isElementDisplayedWithText(title, SHORT_DELAY)) {
                return true;
            }

            for (int i = 0; i < 3; i++) {
                onView(withId(NEWS_LIST_RECYCLER_ID)).perform(swipeUp());
                WaitUtils.waitForMillis(500);

                if (WaitUtils.isElementDisplayedWithText(title, SHORT_DELAY)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверка видимости новости")
    public boolean isNewsDisplayed(String title) {
        try {
            return WaitUtils.isElementDisplayedWithText(title, SHORT_DELAY) || findNewsByTitleWithScroll(title);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверка элементов управления новостями")
    public void checkNewsElementsExist() {
        checkControlPanelIsDisplayed();
        ViewInteraction recyclerView = onView(allOf(withId(NEWS_LIST_RECYCLER_ID), isDisplayed()));
        WaitUtils.waitForElement(recyclerView, LONG_DELAY);
        recyclerView.perform(scrollToPosition(0));

        Matcher<View> firstNewsCard = allOf(withId(NEWS_CARD_ID),
                childAtPosition(withId(NEWS_LIST_RECYCLER_ID), 0),
                isDisplayed());

        onView(allOf(withId(NEWS_TITLE_VIEW_ID), withParent(withParent(firstNewsCard))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(DELETE_NEWS_ICON_ID), withParent(withParent(firstNewsCard))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(EDIT_NEWS_ICON_ID), withParent(withParent(firstNewsCard))))
                .check(matches(isDisplayed()));
    }

    @Step("Проверка отображения элементов управления новостями (возвращает 'boolean')")
    public boolean areNewsElementsDisplayed() {
        try {
            checkNewsElementsExist();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Управление отображением описания")
    public void expandAndCollapseNewsDescription() {
        checkControlPanelIsDisplayed();
        ViewInteraction recyclerView = onView(allOf(withId(NEWS_LIST_RECYCLER_ID), isDisplayed()));
        WaitUtils.waitForElement(recyclerView, LONG_DELAY);
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        WaitUtils.waitForMillis(SHORT_DELAY);
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        WaitUtils.waitForMillis(SHORT_DELAY);
    }

    @Step("Проверка функциональности описания новости (возвращает 'boolean')")
    public boolean isNewsDescriptionFunctional() {
        try {
            expandAndCollapseNewsDescription();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Получение года из даты создания")
    public int getCreationDateYear(String newsTitle) {
        checkControlPanelIsDisplayed();
        boolean found = findNewsByTitleWithScroll(newsTitle);
        if (!found) {
            throw new RuntimeException("News not found: " + newsTitle);
        }

        Matcher<View> cardMatcher = findCardWithTitle(newsTitle);
        ViewInteraction creationDateView = onView(allOf(
                withId(CREATION_DATE_ID),
                withParent(withParent(cardMatcher)),
                isDisplayed()
        ));

        WaitUtils.waitForElement(creationDateView, LONG_DELAY);
        String dateText = getTextFromView(creationDateView);
        return extractYearFromDate(dateText);
    }

    @Step("Проверка отображения даты публикации (возвращает 'boolean')")
    public boolean isPublicationDateDisplayed(String title, String expectedDate) {
        try {
            if (!findNewsByTitleWithScroll(title)) {
                return false;
            }

            Matcher<View> cardMatcher = findCardWithTitle(title);
            ViewInteraction publicationDateView = onView(allOf(
                    withId(R.id.news_item_publication_date_text_view),
                    withParent(withParent(cardMatcher)),
                    isDisplayed()
            ));

            WaitUtils.waitForElement(publicationDateView, SHORT_DELAY);
            String actualDateText = getTextFromView(publicationDateView);

            if (actualDateText == null || actualDateText.isEmpty()) {
                return false;
            }

            String extractedDate = extractDateFromText(actualDateText);
            return extractedDate.equals(expectedDate);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Прокрутка к началу списка")
    public void scrollToTop() {
        try {
            onView(withId(NEWS_LIST_RECYCLER_ID)).perform(scrollToPosition(0));
            WaitUtils.waitForElementWithId(NEWS_TITLE_VIEW_ID, MEDIUM_DELAY);
        } catch (Exception e) {
        }
    }

    @Step("Создание новостей для проверки сортировки")
    public Map<String, String> createTestNewsForSortingTest() {
        Map<String, String> createdNews = new HashMap<>();

        String todayTitle = TestData.News.Sorting.generateUniqueTitle(TestData.News.Sorting.TODAY_NEWS_PREFIX);
        String todayDate = TestData.News.Sorting.getTodayDate();
        createTestNews(todayTitle, TestData.News.CATEGORY_ANNOUNCEMENT, todayDate,
                TestData.News.Sorting.TODAY_TIME, "Тест сортировки - сегодня");
        createdNews.put("todayTitle", todayTitle);
        createdNews.put("todayDate", todayDate);

        String tomorrowTitle = TestData.News.Sorting.generateUniqueTitle(TestData.News.Sorting.TOMORROW_NEWS_PREFIX);
        String tomorrowDate = TestData.News.Sorting.getTomorrowDate();
        createTestNews(tomorrowTitle, TestData.News.CATEGORY_ANNOUNCEMENT, tomorrowDate,
                TestData.News.Sorting.TOMORROW_TIME, "Тест сортировки - завтра");
        createdNews.put("tomorrowTitle", tomorrowTitle);
        createdNews.put("tomorrowDate", tomorrowDate);

        return createdNews;
    }

    @Step("Проверка видимости новости без прокрутки")
    public boolean isNewsVisibleWithoutScroll(String title) {
        try {
            onView(allOf(
                    withId(R.id.news_item_title_text_view),
                    withText(title),
                    isDisplayed()
            )).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверка пустого списка новостей (возвращает 'boolean')")
    public boolean isNewsListEmpty() {
        try {
            onView(withText("There is nothing here yet...")).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            try {
                onView(withId(NEWS_LIST_RECYCLER_ID)).check(matches(isDisplayed()));
                return false;
            } catch (Exception ex) {
                return true;
            }
        }
    }

    @Step("Проверка наличия неактивных новостей (возвращает 'boolean')")
    public boolean hasInactiveNews() {
        try {
            onView(withText("NOT ACTIVE")).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверка наличия активных новостей (возвращает 'boolean')")
    public boolean hasActiveNews() {
        try {
            onView(withText("ACTIVE")).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверка многострочного описания новости")
    public boolean isMultilineDescriptionDisplayed(String newsTitle, String expectedDescription) {
        try {
            boolean found = findNewsByTitleWithScroll(newsTitle);
            if (!found) {
                return false;
            }

            Matcher<View> cardMatcher = findCardWithTitle(newsTitle);
            ViewInteraction expandButton = onView(allOf(
                    withId(EXPAND_NEWS_ICON_ID),
                    withContentDescription("Expand news card button"),
                    withParent(withParent(cardMatcher))
            ));

            WaitUtils.waitForElement(expandButton, LONG_DELAY);
            expandButton.perform(click());
            WaitUtils.waitForMillis(800);

            ViewInteraction descriptionView = onView(allOf(
                    withId(R.id.news_item_description_text_view),
                    withParent(withParent(cardMatcher))
            ));

            String actualDescription = getTextFromView(descriptionView);

            if (actualDescription == null || actualDescription.isEmpty()) {
                return false;
            }

            String normalizedExpected = expectedDescription.replace("\n", " ").trim();
            String normalizedActual = actualDescription.replace("\n", " ").trim();

            return normalizedActual.contains(normalizedExpected.substring(0, Math.min(20, normalizedExpected.length())));

        } catch (Exception e) {
            return false;
        }
    }

    @Step("Создание тестовой новости с выбором даты через календарь")
    public String createTestNewsWithCalendar(String title, String category, int daysFromNow,
                                             String time, String description) {
        CreateEditNewsPage createPage = navigateToCreateNews();
        WaitUtils.waitForElementWithId(R.id.news_item_title_text_input_edit_text, LONG_DELAY);

        createPage.fillTitle(title);
        WaitUtils.waitForMillis(SHORT_DELAY);
        createPage.selectCategorySimple(category);
        WaitUtils.waitForMillis(SHORT_DELAY);

        if (daysFromNow == 0) {
            createPage.selectCurrentDate();
        } else if (daysFromNow > 0) {
            createPage.selectFutureDate(daysFromNow);
        } else {
            createPage.selectPastDate(Math.abs(daysFromNow));
        }

        WaitUtils.waitForMillis(SHORT_DELAY);

        createPage.selectTime(time);
        WaitUtils.waitForMillis(SHORT_DELAY);

        createPage.fillDescription(description);
        WaitUtils.waitForMillis(SHORT_DELAY);

        createPage.clickSaveButton();
        checkControlPanelIsDisplayed();

        boolean created = verifyNewsCreated(title);
        assertTrue("Failed to create news with title: " + title, created);
        return title;
    }

    // Вспомогательные методы

    private boolean verifyNewsCreatedWithTimeout(String title, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            if (isNewsDisplayed(title)) {
                return true;
            }
            WaitUtils.waitForMillis(MEDIUM_DELAY);
        }
        return false;
    }

    private void performDeleteConfirmation() {
        try {
            WaitUtils.waitForElementWithText("OK", LONG_DELAY);
            onView(withText("OK")).perform(click());
        } catch (Exception e) {
            try {
                onView(withText("ОК")).perform(click());
            } catch (Exception e2) {
                onView(withId(android.R.id.button1)).perform(click());
            }
        }
        WaitUtils.waitForElementWithId(NEWS_LIST_RECYCLER_ID, MEDIUM_DELAY);
    }

    private String getTextFromView(ViewInteraction viewInteraction) {
        String[] textHolder = new String[1];
        viewInteraction.perform(new GetTextAction(textHolder));
        return textHolder[0] != null ? textHolder[0] : "";
    }

    private int extractYearFromDate(String dateText) {
        java.util.regex.Matcher dateMatcher = DATE_PATTERN.matcher(dateText);
        assertTrue("Date does not match the format dd.MM.yyyy: " + dateText, dateMatcher.find());
        return Integer.parseInt(dateMatcher.group(3));
    }

    private Matcher<View> findCardWithTitle(String title) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("News card with title: " + title);
            }

            @Override
            protected boolean matchesSafely(View view) {
                if (view.getId() == NEWS_CARD_ID) {
                    return findTitleInHierarchy(view, title);
                }
                return false;
            }

            private boolean findTitleInHierarchy(View view, String title) {
                if (view instanceof ViewGroup) {
                    ViewGroup group = (ViewGroup) view;
                    for (int i = 0; i < group.getChildCount(); i++) {
                        View child = group.getChildAt(i);
                        if (child.getId() == NEWS_TITLE_VIEW_ID &&
                                child instanceof android.widget.TextView) {
                            String text = ((android.widget.TextView) child).getText().toString();
                            return text.equals(title);
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

    private String extractDateFromText(String dateText) {
        java.util.regex.Matcher matcher = DATE_PATTERN.matcher(dateText);
        if (matcher.find()) {
            return matcher.group();
        }
        return dateText;
    }

    // Внутренние классы

    private static class ClickEditButtonAction implements ViewAction {
        @Override
        public Matcher<View> getConstraints() {
            return isDisplayed();
        }

        @Override
        public String getDescription() {
            return "Click on edit button inside news card";
        }

        @Override
        public void perform(UiController uiController, View view) {
            View editButton = findViewWithId(view, EDIT_NEWS_ICON_ID);
            if (editButton != null && editButton.isShown()) {
                editButton.performClick();
            } else {
                view.performClick();
                uiController.loopMainThreadUntilIdle();

                editButton = findViewWithId(view, EDIT_NEWS_ICON_ID);
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
            return "Getting text from TextView";
        }

        @Override
        public void perform(UiController uiController, View view) {
            if (view instanceof android.widget.TextView) {
                textHolder[0] = ((android.widget.TextView) view).getText().toString();
            }
        }
    }

    // Matcher утилиты

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child element at position " + position);
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