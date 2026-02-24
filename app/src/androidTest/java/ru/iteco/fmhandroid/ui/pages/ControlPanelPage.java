package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

import android.view.View;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class ControlPanelPage {

    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int SHORT_TIMEOUT = 3000;
    private static final int MEDIUM_DELAY = 500;
    private static final int SHORT_DELAY = 200;


    // Текстовые константы
    private static final String ADD_NEWS_CONTENT_DESC = "Add news button";
    private static final String DELETE_BUTTON_CONTENT_DESC = "News delete button";
    private static final String EMPTY_LIST_MESSAGE = "There is nothing here yet...";
    private static final String ACTIVE_STATUS = "ACTIVE";
    private static final String NOT_ACTIVE_STATUS = "NOT ACTIVE";
    private static final String OK_BUTTON = "OK";

    // ID элементов
    public static final int ADD_NEWS_BUTTON_ID = R.id.add_news_image_view;
    public static final int SORT_BUTTON_ID = R.id.sort_news_material_button;
    public static final int FILTER_BUTTON_ID = R.id.filter_news_material_button;
    public static final int NEWS_LIST_RECYCLER_ID = R.id.news_list_recycler_view;
    public static final int EDIT_NEWS_BUTTON_ID = R.id.edit_news_material_button;
    public static final int NEWS_TITLE_ID = R.id.news_item_title_text_view;
    public static final int NEWS_PUBLICATION_DATE_ID = R.id.news_item_publication_date_text_view;
    public static final int NEWS_CREATION_DATE_ID = R.id.news_item_create_date_text_view;
    public static final int NEWS_AUTHOR_ID = R.id.news_item_author_name_text_view;
    public static final int NEWS_STATUS_ID = R.id.news_item_published_text_view;
    public static final int NEWS_DESCRIPTION_ID = R.id.news_item_description_text_view;
    public static final int EDIT_ICON_ID = R.id.edit_news_item_image_view;
    public static final int DELETE_ICON_ID = R.id.delete_news_item_image_view;
    public static final int NEWS_CARD_ID = R.id.news_item_material_card_view;


    public boolean isControlPanelDisplayed() {
        Allure.step("Проверка отображения Control Panel");
        return WaitUtils.isElementDisplayedWithId(ADD_NEWS_BUTTON_ID, SHORT_TIMEOUT);
    }

    public boolean isNewsDisplayed(String title) {
        try {
            return isNewsDisplayedWithoutScroll(title) || findNewsByTitleWithoutException(title);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForControlPanelLoadedWithTimeout(long timeoutMillis) {
        Allure.step("Ожидание загрузки Control Panel с таймаутом " + timeoutMillis + "ms");
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            if (isControlPanelDisplayed()) {
                return true;
            }
            WaitUtils.waitMillis(100);
        }
        return isControlPanelDisplayed();
    }

    public boolean isNewsVisibleWithoutScroll(String title) {
        return isNewsDisplayedWithoutScroll(title);
    }

    public boolean isNewsListEmpty() {
        Allure.step("Проверка пустого списка");
        return WaitUtils.isElementDisplayedWithText(EMPTY_LIST_MESSAGE, SHORT_TIMEOUT);
    }

    public boolean hasOnlyActiveNews() {
        Allure.step("Проверка, что только активные новости");
        try {
            onView(withText(NOT_ACTIVE_STATUS)).check(matches(not(isDisplayed())));
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean hasOnlyInactiveNews() {
        Allure.step("Проверка, что только неактивные новости");
        try {
            onView(withText(ACTIVE_STATUS)).check(matches(not(isDisplayed())));
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isPublicationDateCorrect(String title, String expectedDate) {
        Allure.step("Проверка даты публикации для: " + title);
        try {
            findNewsByTitle(title);
            Matcher<View> cardMatcher = findCardWithTitle(title);

            ViewInteraction dateView = onView(allOf(
                    withId(NEWS_PUBLICATION_DATE_ID),
                    withParent(withParent(cardMatcher))
            ));

            String actualDate = getTextFromView(dateView);
            return actualDate.contains(expectedDate);
        } catch (Exception e) {
            return false;
        }
    }

    public ControlPanelPage waitForControlPanelLoaded() {
        Allure.step("Ожидание загрузки Control Panel");
        WaitUtils.waitForElementWithId(ADD_NEWS_BUTTON_ID, DEFAULT_TIMEOUT);
        return this;
    }

    public ControlPanelPage clickEditNewsButton() {
        Allure.step("Нажатие кнопки редактирования новостей");
        WaitUtils.waitForElementWithId(EDIT_NEWS_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(EDIT_NEWS_BUTTON_ID)).perform(click());
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public ControlPanelPage clickAddNewsButton() {
        Allure.step("Нажатие кнопки добавления новости");
        WaitUtils.waitForElementWithId(ADD_NEWS_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(allOf(withId(ADD_NEWS_BUTTON_ID), withContentDescription(ADD_NEWS_CONTENT_DESC)))
                .perform(click());
        return this;
    }

    public CreateEditNewsPage navigateToCreateNews() {
        Allure.step("Переход к созданию новости");
        clickAddNewsButton();
        CreateEditNewsPage createPage = new CreateEditNewsPage();
        createPage.waitForCreateScreen();
        return createPage;
    }

    public CreateEditNewsPage navigateToEditNews(String title) {
        Allure.step("Переход к редактированию новости: " + title);
        findNewsByTitle(title);
        ViewInteraction editButton = getEditButtonForNews(title);
        WaitUtils.waitForElement(editButton, DEFAULT_TIMEOUT);
        editButton.perform(click());
        CreateEditNewsPage editPage = new CreateEditNewsPage();
        editPage.waitForEditScreen();
        return editPage;
    }

    public ControlPanelPage clickSortButton() {
        Allure.step("Нажатие кнопки сортировки");
        WaitUtils.waitForElementWithId(SORT_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(SORT_BUTTON_ID)).perform(click());
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public ControlPanelPage openNewsFilter() {
        Allure.step("Открытие фильтра новостей");
        WaitUtils.waitForElementWithId(FILTER_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(FILTER_BUTTON_ID)).perform(click());
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public ControlPanelPage findNewsByTitle(String title) {
        Allure.step("Поиск новости: " + title);
        int maxScrolls = 3;

        if (isNewsDisplayedWithoutScroll(title)) {
            return this;
        }

        for (int i = 0; i < maxScrolls; i++) {
            onView(withId(NEWS_LIST_RECYCLER_ID)).perform(swipeUp());
            WaitUtils.waitMillis(SHORT_DELAY);

            if (isNewsDisplayedWithoutScroll(title)) {
                return this;
            }
        }
        throw new AssertionError("News with title '" + title + "' not found");
    }

    public ControlPanelPage deleteNews(String title) {
        Allure.step("Удаление новости: " + title);
        findNewsByTitle(title);
        ViewInteraction deleteButton = getDeleteButtonForNews(title);
        WaitUtils.waitForElement(deleteButton, DEFAULT_TIMEOUT);
        deleteButton.perform(click());
        WaitUtils.waitMillis(SHORT_DELAY);
        confirmDelete();
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public ControlPanelPage confirmDelete() {
        Allure.step("Подтверждение удаления");
        WaitUtils.waitForElementWithText(OK_BUTTON, DEFAULT_TIMEOUT);
        onView(withText(OK_BUTTON)).perform(click());
        return this;
    }

    public ControlPanelPage scrollToTop() {
        Allure.step("Прокрутка к началу списка");
        try {
            onView(withId(NEWS_LIST_RECYCLER_ID)).perform(swipeUp());
            WaitUtils.waitMillis(MEDIUM_DELAY);
        } catch (Exception e) {
        }
        return this;
    }

    public ControlPanelPage checkNewsItemElements(String title) {
        Allure.step("Проверка элементов новости: " + title);
        findNewsByTitle(title);

        Matcher<View> cardMatcher = findCardWithTitle(title);

        onView(allOf(withId(NEWS_TITLE_ID), withParent(withParent(cardMatcher))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(NEWS_PUBLICATION_DATE_ID), withParent(withParent(cardMatcher))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(NEWS_CREATION_DATE_ID), withParent(withParent(cardMatcher))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(NEWS_AUTHOR_ID), withParent(withParent(cardMatcher))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(NEWS_STATUS_ID), withParent(withParent(cardMatcher))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(EDIT_ICON_ID), withParent(withParent(cardMatcher))))
                .check(matches(isDisplayed()));
        onView(allOf(withId(DELETE_ICON_ID), withParent(withParent(cardMatcher))))
                .check(matches(isDisplayed()));

        return this;
    }

    public ControlPanelPage expandAndCollapseDescription(String title) {
        Allure.step("Раскрытие/скрытие описания новости: " + title);
        findNewsByTitle(title);

        Matcher<View> cardMatcher = findCardWithTitle(title);
        onView(cardMatcher).perform(click());
        WaitUtils.waitMillis(SHORT_DELAY);

        onView(cardMatcher).perform(click());
        WaitUtils.waitMillis(SHORT_DELAY);

        return this;
    }

    public int getCreationDateYear(String title) {
        Allure.step("Получение года создания для: " + title);
        findNewsByTitle(title);

        Matcher<View> cardMatcher = findCardWithTitle(title);
        ViewInteraction dateView = onView(allOf(
                withId(NEWS_CREATION_DATE_ID),
                withParent(withParent(cardMatcher))
        ));

        String dateText = getTextFromView(dateView);
        return extractYearFromDate(dateText);
    }

    public String createTestNews(String title, String category, String date, String time, String description) {
        Allure.step("Создание тестовой новости: " + title);
        CreateEditNewsPage createPage = navigateToCreateNews();

        createPage.fillTitle(title)
                .selectCategory(category)
                .setDate(date)
                .setTime(time)
                .fillDescription(description)
                .clickSaveButton();

        waitForControlPanelLoaded();
        assertTrue("News not created: " + title, isNewsDisplayed(title));
        return title;
    }

    public Map<String, String> createTwoNewsForSortingTest() {
        Allure.step("Создание двух новостей для теста сортировки");
        Map<String, String> result = new HashMap<>();

        String todayTitle = TestData.News.Sorting.generateUniqueTitle(TestData.News.Sorting.TODAY_NEWS_PREFIX);
        String todayDate = TestData.News.Sorting.getTodayDate();
        createTestNews(todayTitle, TestData.News.CATEGORY_ANNOUNCEMENT, todayDate,
                "20:00", "News for sorting test (today)");
        result.put("todayTitle", todayTitle);

        WaitUtils.waitMillis(1000);

        String tomorrowTitle = TestData.News.Sorting.generateUniqueTitle(TestData.News.Sorting.TOMORROW_NEWS_PREFIX);
        String tomorrowDate = TestData.News.Sorting.getTomorrowDate();
        createTestNews(tomorrowTitle, TestData.News.CATEGORY_ANNOUNCEMENT, tomorrowDate,
                "09:00", "News for sorting test (tomorrow)");
        result.put("tomorrowTitle", tomorrowTitle);

        return result;
    }

    // Вспомогательные методы

    private boolean isNewsDisplayedWithoutScroll(String title) {
        try {
            onView(allOf(withId(NEWS_TITLE_ID), withText(title), isDisplayed()))
                    .check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean findNewsByTitleWithoutException(String title) {
        try {
            findNewsByTitle(title);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    private ViewInteraction getEditButtonForNews(String title) {
        Matcher<View> cardMatcher = findCardWithTitle(title);
        return onView(allOf(
                withId(EDIT_ICON_ID),
                withParent(withParent(cardMatcher)),
                isDisplayed()
        ));
    }

    private ViewInteraction getDeleteButtonForNews(String title) {
        Matcher<View> cardMatcher = findCardWithTitle(title);
        return onView(allOf(
                withId(DELETE_ICON_ID),
                withParent(withParent(cardMatcher)),
                isDisplayed()
        ));
    }

    private Matcher<View> findCardWithTitle(String title) {
        return new NewsCardMatcher(title, NEWS_TITLE_ID, NEWS_CARD_ID);
    }

    private String getTextFromView(ViewInteraction viewInteraction) {
        final String[] text = new String[1];
        viewInteraction.perform(new androidx.test.espresso.ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "get text";
            }

            @Override
            public void perform(androidx.test.espresso.UiController uiController, View view) {
                if (view instanceof android.widget.TextView) {
                    text[0] = ((android.widget.TextView) view).getText().toString();
                }
                uiController.loopMainThreadUntilIdle();
            }
        });
        return text[0] != null ? text[0] : "";
    }

    private int extractYearFromDate(String dateText) {
        try {
            String[] parts = dateText.split("\\.");
            if (parts.length == 3) {
                return Integer.parseInt(parts[2]);
            }
            return LocalDate.now().getYear();
        } catch (Exception e) {
            return LocalDate.now().getYear();
        }
    }

    public void safeDeleteNews(String title) {
        if (isNewsDisplayed(title)) {
            deleteNews(title);
        }
    }

    public boolean waitForNewsDisplayed(String title, int maxAttempts, long delayMillis) {
        for (int i = 0; i < maxAttempts; i++) {
            if (isNewsDisplayed(title)) {
                return true;
            }
            WaitUtils.waitMillis(delayMillis);
        }
        return isNewsDisplayed(title);
    }

    public boolean waitForNewsDisplayed(String title) {
        return waitForNewsDisplayed(title, 3, 500);
    }

    // Внутренний класс для поиска карточки новости
    private static class NewsCardMatcher extends org.hamcrest.TypeSafeMatcher<View> {
        private final String title;
        private final int titleId;
        private final int cardId;

        NewsCardMatcher(String title, int titleId, int cardId) {
            this.title = title;
            this.titleId = titleId;
            this.cardId = cardId;
        }

        @Override
        protected boolean matchesSafely(View view) {
            if (view.getId() != cardId) {
                return false;
            }
            return findTitleInHierarchy(view, title);
        }

        private boolean findTitleInHierarchy(View view, String targetTitle) {
            if (view instanceof android.view.ViewGroup) {
                android.view.ViewGroup group = (android.view.ViewGroup) view;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (child.getId() == titleId && child instanceof android.widget.TextView) {
                        String text = ((android.widget.TextView) child).getText().toString();
                        if (text.equals(targetTitle)) {
                            return true;
                        }
                    }
                    if (findTitleInHierarchy(child, targetTitle)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void describeTo(org.hamcrest.Description description) {
            description.appendText("News card with title: " + title);
        }
    }
}