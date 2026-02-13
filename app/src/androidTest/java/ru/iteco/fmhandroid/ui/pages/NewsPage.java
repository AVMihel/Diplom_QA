package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsPage {

    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;
    private static final int POLLING_DELAY = 50;

    // Текстовые константы
    private static final String ALL_NEWS_TEXT = "ALL NEWS";

    // ID элементов
    private static final int ALL_NEWS_BUTTON_ID = R.id.all_news_text_view;
    private static final int NEWS_BLOCK_CONTROL_BUTTON_ID = R.id.expand_material_button;
    private static final int SORT_BUTTON_ID = R.id.sort_news_material_button;

    // Проверка отображения кнопки 'ALL NEWS'
    public NewsPage checkAllNewsButtonIsDisplayed() {
        waitForElement(getAllNewsButton(), LONG_DELAY);
        return this;
    }

    // Проверка отображения кнопки 'ALL NEWS'
    public boolean isAllNewsButtonDisplayed() {
        return isElementDisplayedQuickly(getAllNewsButton(), SHORT_DELAY);
    }

    // Проверка, что кнопка 'ALL NEWS' скрыта
    public boolean isAllNewsButtonHidden() {
        return !isElementDisplayedQuickly(getAllNewsButton(), SHORT_DELAY);
    }

    // Проверка отображения кнопки сворачивания/разворачивания
    public boolean isExpandNewsButtonDisplayed() {
        return isElementDisplayedQuickly(getExpandNewsButton(), SHORT_DELAY);
    }

    // Клик по кнопке 'ALL NEWS'
    public NewsPage clickAllNewsButton() {
        waitForElement(getAllNewsButton(), LONG_DELAY);
        getAllNewsButton().perform(click());
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return this;
    }

    // Клик по кнопке сворачивания/разворачивания
    public NewsPage clickExpandNewsButton() {
        waitForElement(getExpandNewsButton(), LONG_DELAY);
        getExpandNewsButton().perform(click());
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return this;
    }

    // Проверка отображения кнопки сортировки
    public boolean isSortButtonDisplayed() {
        return isElementDisplayedQuickly(getSortButton(), SHORT_DELAY);
    }

    // Клик по кнопке 'Quotes' на экране News
    public NewsPage clickQuotesButtonOnNewsScreen() {
        ViewInteraction quotesButtonOnNews = onView(
                allOf(
                        withId(R.id.our_mission_image_button),
                        withContentDescription("Our Mission"),
                        isDisplayed()
                )
        );
        waitForElement(quotesButtonOnNews, LONG_DELAY);
        quotesButtonOnNews.perform(click());
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return this;
    }

    // Проверка пустого списка новостей
    public boolean isNewsListEmpty() {
        try {
            onView(withText("There is nothing here yet...")).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка отображения списка новостей
    public boolean isNewsListDisplayed() {
        try {
            onView(withId(R.id.news_list_recycler_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка отображения элементов управления новостями
    public boolean areNewsControlsDisplayed() {
        try {
            onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()));
            onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()));
            onView(withId(R.id.edit_news_material_button)).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Открытие фильтра новостей
    public boolean openNewsFilter() {
        try {
            ViewInteraction filterButton = onView(withId(R.id.filter_news_material_button));
            waitForElement(filterButton, LONG_DELAY);
            filterButton.perform(click());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Клик по кнопке редактирования новостей
    public NewsPage clickEditNewsButton() {
        ViewInteraction editButton = onView(withId(R.id.edit_news_material_button));
        waitForElement(editButton, LONG_DELAY);
        editButton.perform(click());
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return this;
    }

    // Вспомогательные методы

    private boolean isElementDisplayedQuickly(ViewInteraction view, long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                view.check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                WaitUtils.waitForMillis(POLLING_DELAY);
            }
        }
        return false;
    }

    private void waitForElement(ViewInteraction element, long timeout) {
        WaitUtils.waitForElement(element, timeout);
    }

    // Получение элементов интерфейса

    private ViewInteraction getAllNewsButton() {
        return onView(allOf(withId(ALL_NEWS_BUTTON_ID), withText(ALL_NEWS_TEXT), isDisplayed()));
    }

    private ViewInteraction getExpandNewsButton() {
        return onView(allOf(withId(NEWS_BLOCK_CONTROL_BUTTON_ID), isDisplayed()));
    }

    private ViewInteraction getSortButton() {
        return onView(allOf(withId(SORT_BUTTON_ID), isDisplayed()));
    }
}