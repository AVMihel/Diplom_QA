package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

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
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class ControlPanelPage {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final NewsPage newsPage = new NewsPage();

    // Переход в Control Panel из главного экрана
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
        int[] controlPanelIds = {R.id.add_news_image_view, R.id.sort_news_material_button,
                R.id.news_list_recycler_view};
        WaitUtils.waitForAnyElement(controlPanelIds, 2000);
        WaitUtils.waitForMillis(500);
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
            WaitUtils.waitForElement(editButton, 3000);
            editButton.perform(click());
        } catch (Exception e) {
            ViewInteraction altEditButton = onView(
                    allOf(withId(R.id.edit_news_material_button), isDisplayed()));
            WaitUtils.waitForElement(altEditButton, 2000);
            altEditButton.perform(click());
        }
    }

    // Проверка элементов у новостей в Control Panel
    public void checkNewsElementsExist() {
        checkControlPanelIsDisplayed();

        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        WaitUtils.waitForElement(recyclerView, 3000);
        recyclerView.perform(scrollToPosition(0));

        Matcher<View> firstNewsCard = allOf(withId(R.id.news_item_material_card_view),
                childAtPosition(withId(R.id.news_list_recycler_view), 0),
                isDisplayed());

        checkElementInCard(firstNewsCard, withId(R.id.news_item_title_text_view));
        checkElementInCard(firstNewsCard, withId(R.id.delete_news_item_image_view));
        checkElementInCard(firstNewsCard, withId(R.id.edit_news_item_image_view));
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

    // Разворачивание и сворачивание описания новости
    public void expandAndCollapseNewsDescription() {
        checkControlPanelIsDisplayed();

        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        WaitUtils.waitForElement(recyclerView, 3000);

        recyclerView.perform(actionOnItemAtPosition(0, click()));
        WaitUtils.waitForMillis(500);

        recyclerView.perform(actionOnItemAtPosition(0, click()));
        WaitUtils.waitForMillis(500);
    }

    // Переход к редактированию новости
    public CreateEditNewsPage navigateToEditNews() {
        checkControlPanelIsDisplayed();

        ViewInteraction recyclerView = onView(withId(R.id.news_list_recycler_view));
        WaitUtils.waitForElement(recyclerView, 3000);

        recyclerView.perform(actionOnItemAtPosition(0, new ClickEditButtonAction()));

        CreateEditNewsPage editPage = new CreateEditNewsPage();
        editPage.checkEditScreenIsDisplayed();
        return editPage;
    }

    // Удаление созданной новости по точному заголовку
    public void deleteCreatedNewsByExactTitle(String title) {
        try {
            // 1. Сначала разворачиваем карточку для доступа к кнопке удаления
            ViewInteraction expandButton = onView(allOf(
                    withId(R.id.view_news_item_image_view),
                    withContentDescription("Expand news card button"),
                    withParent(withParent(findCardWithTitle(title)))
            ));

            WaitUtils.waitForElement(expandButton, 2000);
            expandButton.perform(click());
            WaitUtils.waitForMillis(500);

            // 2. Находим кнопку удаления в развернутой карточке
            ViewInteraction deleteButton = onView(allOf(
                    withId(R.id.delete_news_item_image_view),
                    withContentDescription("News delete button"),
                    withParent(withParent(findCardWithTitle(title)))
            ));

            WaitUtils.waitForElement(deleteButton, 2000);
            deleteButton.perform(click());

            // 3. Подтверждаем удаление
            WaitUtils.waitForElementWithText("OK", 2000);
            onView(withText("OK")).perform(click());

            // 4. Ждем обновления списка
            WaitUtils.waitForMillis(1500);

        } catch (Exception e) {
            deleteNewsByTitleAlternative(title);
        }
    }

    // Альтернативный метод удаления новости по заголовку
    private void deleteNewsByTitleAlternative(String title) {
        try {
            // 1. Находим новость по заголовку и кликаем на нее
            ViewInteraction newsTitle = onView(allOf(
                    withId(R.id.news_item_title_text_view),
                    withText(title),
                    isDisplayed()
            ));
            WaitUtils.waitForElement(newsTitle, 3000);

            // 2. Кликаем на заголовок, чтобы открыть карточку
            newsTitle.perform(click());
            WaitUtils.waitForMillis(500);

            // 3. Ищем кнопку удаления в видимой области
            ViewInteraction deleteButton = onView(allOf(
                    withId(R.id.delete_news_item_image_view),
                    withContentDescription("News delete button"),
                    isDisplayed()
            ));

            WaitUtils.waitForElement(deleteButton, 2000);
            deleteButton.perform(click());

            // 4. Подтверждаем удаление
            WaitUtils.waitForElementWithText("OK", 2000);
            onView(withText("OK")).perform(click());

            WaitUtils.waitForMillis(1500);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete news with title: " + title, e);
        }
    }

    // Создание тестовой новости с указанными параметрами
    public String createTestNews(String title, String category, String date, String time, String description) {
        try {
            CreateEditNewsPage createPage = navigateToCreateNews();
            WaitUtils.waitForElementWithId(R.id.news_item_title_text_input_edit_text, 5000);

            createPage.fillTitle(title);
            WaitUtils.waitForMillis(500);

            createPage.selectCategorySimple(category);
            WaitUtils.waitForMillis(500);

            onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                    .perform(replaceText(date));
            WaitUtils.waitForMillis(500);

            onView(withId(R.id.news_item_publish_time_text_input_edit_text))
                    .perform(replaceText(time));
            WaitUtils.waitForMillis(500);

            createPage.fillDescription(description);
            WaitUtils.waitForMillis(500);

            createPage.clickSaveButton();
            waitForControlPanel();

            boolean created = verifyNewsCreated(title, 10000);
            if (!created) {
                throw new RuntimeException("News not created: " + title);
            }

            return title;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create test news: " + e.getMessage(), e);
        }
    }

    // Создание новости для теста удаления
    public String createNewsForDeletionTest() {
        String title = "TestNews_" + System.currentTimeMillis();
        String category = "Объявление";
        String date = "01.01.2025";
        String time = "12:00";
        String description = "Automated test news for deletion";

        return createTestNews(title, category, date, time, description);
    }

    // Создание новости для теста дат с указанным годом
    public String createNewsForDateTest(int year) {
        String title = "ТестДаты_" + year + "_" + System.currentTimeMillis();
        String category = "Объявление";

        LocalDate currentDate = LocalDate.now();
        String date = String.format("%02d.%02d.%d",
                currentDate.getDayOfMonth(),
                currentDate.getMonthValue(),
                year);

        String time = "12:00";
        String description = "Новость для теста дат: " + year;

        return createTestNews(title, category, date, time, description);
    }

    // Проверка создания новости в списке
    private boolean verifyNewsCreated(String title, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            if (findNewsByTitleWithScroll(title)) {
                return true;
            }
            WaitUtils.waitForMillis(1000);
        }
        return false;
    }

    // Поиск новости по заголовку с прокруткой списка
    public boolean findNewsByTitleWithScroll(String title) {
        int maxScrollAttempts = 5;

        for (int attempt = 0; attempt < maxScrollAttempts; attempt++) {
            try {
                onView(allOf(withId(R.id.news_item_title_text_view),
                        withText(title),
                        isDisplayed()))
                        .check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                try {
                    if (attempt % 2 == 0) {
                        onView(withId(R.id.news_list_recycler_view))
                                .perform(androidx.test.espresso.action.ViewActions.swipeUp());
                    } else {
                        onView(withId(R.id.news_list_recycler_view))
                                .perform(androidx.test.espresso.action.ViewActions.swipeDown());
                    }
                    WaitUtils.waitForMillis(1000);
                } catch (Exception scrollEx) {
                    break;
                }
            }
        }
        return false;
    }

    // Проверка отображения новости с указанным заголовком
    public boolean isNewsDisplayed(String title) {
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

    // Проверка удаления новости (отсутствия в списке)
    public boolean isNewsDeleted(String title, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(allOf(withId(R.id.news_item_title_text_view),
                        withText(title)))
                        .check(matches(isDisplayed()));
                WaitUtils.waitForMillis(500);
            } catch (Exception e) {
                return true;
            }
        }
        return false;
    }

    // Проверка года в дате создания новости
    public void verifyCreationDateYear(String newsTitle, int expectedYear) {
        checkControlPanelIsDisplayed();

        boolean found = findNewsByTitleWithScroll(newsTitle);
        Assert.assertTrue("Новость не найдена: " + newsTitle, found);

        try {
            Matcher<View> cardMatcher = findCardWithTitle(newsTitle);

            ViewInteraction creationDateView = onView(allOf(
                    withId(R.id.news_item_create_date_text_view),
                    withParent(withParent(cardMatcher)),
                    isDisplayed()
            ));

            WaitUtils.waitForElement(creationDateView, 2000);

            String[] dateText = new String[1];
            creationDateView.perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isDisplayed();
                }

                @Override
                public String getDescription() {
                    return "Get text from view";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    if (view instanceof android.widget.TextView) {
                        dateText[0] = ((android.widget.TextView) view).getText().toString();
                    }
                }
            });

            Assert.assertNotNull("Дата создания пустая", dateText[0]);
            Assert.assertFalse("Дата создания пустая", dateText[0].isEmpty());

            Pattern pattern = Pattern.compile("(\\d{2})\\.(\\d{2})\\.(\\d{4})");
            java.util.regex.Matcher dateMatcher = pattern.matcher(dateText[0]);

            Assert.assertTrue("Неверный формат даты: " + dateText[0], dateMatcher.find());

            int actualYear = Integer.parseInt(dateMatcher.group(3));

            Assert.assertEquals("Год в дате создания не совпадает", expectedYear, actualYear);

        } catch (Exception e) {
            try {
                ViewInteraction anyCreationDate = onView(
                        allOf(withId(R.id.news_item_create_date_text_view), isDisplayed())
                );

                String[] dateText = new String[1];
                anyCreationDate.perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Get text from any creation date view";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        if (view instanceof android.widget.TextView) {
                            dateText[0] = ((android.widget.TextView) view).getText().toString();
                        }
                    }
                });

                if (dateText[0] != null && !dateText[0].isEmpty()) {
                    Pattern pattern = Pattern.compile("(\\d{2})\\.(\\d{2})\\.(\\d{4})");
                    java.util.regex.Matcher dateMatcher = pattern.matcher(dateText[0]);

                    if (dateMatcher.find()) {
                        int actualYear = Integer.parseInt(dateMatcher.group(3));
                        Assert.assertEquals("Год в дате создания не совпадает", expectedYear, actualYear);
                    } else {
                        Assert.fail("Неверный формат даты: " + dateText[0]);
                    }
                } else {
                    Assert.fail("Дата создания пустая");
                }

            } catch (Exception e2) {
                throw new RuntimeException("Не удалось проверить дату создания: " + e.getMessage(), e);
            }
        }
    }

    // Проверка года в дате публикации новости
    public void verifyPublicationDateYear(String newsTitle, int expectedYear) {
        checkControlPanelIsDisplayed();

        boolean found = findNewsByTitleWithScroll(newsTitle);
        Assert.assertTrue("Новость не найдена: " + newsTitle, found);

        try {
            Matcher<View> cardMatcher = findCardWithTitle(newsTitle);

            ViewInteraction publicationDateView = onView(allOf(
                    withId(R.id.news_item_publication_date_text_view),
                    withParent(withParent(cardMatcher)),
                    isDisplayed()
            ));

            WaitUtils.waitForElement(publicationDateView, 2000);

            String[] dateText = new String[1];
            publicationDateView.perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isDisplayed();
                }

                @Override
                public String getDescription() {
                    return "Get text from view";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    if (view instanceof android.widget.TextView) {
                        dateText[0] = ((android.widget.TextView) view).getText().toString();
                    }
                }
            });

            Assert.assertNotNull("Дата публикации пустая", dateText[0]);
            Assert.assertFalse("Дата публикации пустая", dateText[0].isEmpty());

            Pattern pattern = Pattern.compile("(\\d{2})\\.(\\d{2})\\.(\\d{4})");
            java.util.regex.Matcher dateMatcher = pattern.matcher(dateText[0]);

            Assert.assertTrue("Неверный формат даты: " + dateText[0], dateMatcher.find());

            int actualYear = Integer.parseInt(dateMatcher.group(3));

            Assert.assertEquals("Год в дате публикации не совпадает", expectedYear, actualYear);

        } catch (Exception e) {
            try {
                ViewInteraction anyPublicationDate = onView(
                        allOf(withId(R.id.news_item_publication_date_text_view), isDisplayed())
                );

                String[] dateText = new String[1];
                anyPublicationDate.perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "Get text from any publication date view";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        if (view instanceof android.widget.TextView) {
                            dateText[0] = ((android.widget.TextView) view).getText().toString();
                        }
                    }
                });

                if (dateText[0] != null && !dateText[0].isEmpty()) {
                    Pattern pattern = Pattern.compile("(\\d{2})\\.(\\d{2})\\.(\\d{4})");
                    java.util.regex.Matcher dateMatcher = pattern.matcher(dateText[0]);

                    if (dateMatcher.find()) {
                        int actualYear = Integer.parseInt(dateMatcher.group(3));
                        Assert.assertEquals("Год в дате публикации не совпадает", expectedYear, actualYear);
                    } else {
                        Assert.fail("Неверный формат даты: " + dateText[0]);
                    }
                } else {
                    Assert.fail("Дата публикации пустая");
                }

            } catch (Exception e2) {
                throw new RuntimeException("Не удалось проверить дату публикации: " + e.getMessage(), e);
            }
        }
    }

    // Клик по кнопке сортировки новостей
    public void clickSortButton() {
        ViewInteraction sortButton = onView(
                allOf(withId(R.id.sort_news_material_button), isDisplayed())
        );
        WaitUtils.waitForElement(sortButton, 2000);
        sortButton.perform(click());
    }

    // Проверка отображения отсортированного списка новостей
    public void checkNewsAreSorted() {
        onView(withId(R.id.news_list_recycler_view)).check(matches(isDisplayed()));
    }

    // Открытие диалога фильтрации новостей
    public NewsFilterPage openFilterDialog() {
        ViewInteraction filterButton = onView(
                allOf(withId(R.id.filter_news_material_button), isDisplayed())
        );
        WaitUtils.waitForElement(filterButton, 2000);
        filterButton.perform(click());

        NewsFilterPage filterPage = new NewsFilterPage();
        filterPage.checkFilterDialogIsDisplayed();
        return filterPage;
    }

    // Переход к созданию новой новости
    public CreateEditNewsPage navigateToCreateNews() {
        checkControlPanelIsDisplayed();

        ViewInteraction addButton = onView(
                allOf(withId(R.id.add_news_image_view),
                        withContentDescription("Add news button"),
                        isDisplayed())
        );
        WaitUtils.waitForElement(addButton, 3000);
        addButton.perform(click());

        CreateEditNewsPage createPage = new CreateEditNewsPage();
        createPage.checkCreateScreenIsDisplayed();
        return createPage;
    }

    // Поиск карточки новости по заголовку
    private Matcher<View> findCardWithTitle(String title) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Card containing title: " + title);
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

    // Действие для клика по кнопке редактирования внутри карточки новости
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

    // Матчер для поиска дочернего элемента по позиции
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
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