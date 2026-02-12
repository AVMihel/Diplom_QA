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

import io.qameta.allure.kotlin.Allure;
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

    public NewsPage checkAllNewsButtonIsDisplayed() {
        Allure.step("Проверка отображения кнопки 'ALL NEWS'");
        waitForElement(getAllNewsButton(), LONG_DELAY);
        return this;
    }

    public boolean isAllNewsButtonDisplayed() {
        Allure.step("Проверка отображения кнопки 'ALL NEWS'");
        return isElementDisplayedQuickly(getAllNewsButton(), SHORT_DELAY);
    }

    public boolean isAllNewsButtonHidden() {
        Allure.step("Проверка, что кнопка 'ALL NEWS' скрыта");
        return !isElementDisplayedQuickly(getAllNewsButton(), SHORT_DELAY);
    }

    public boolean isExpandNewsButtonDisplayed() {
        Allure.step("Проверка отображения кнопки сворачивания/разворачивания");
        return isElementDisplayedQuickly(getExpandNewsButton(), SHORT_DELAY);
    }

    public NewsPage clickAllNewsButton() {
        Allure.step("Клик по кнопке 'ALL NEWS'");
        waitForElement(getAllNewsButton(), LONG_DELAY);
        getAllNewsButton().perform(click());
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return this;
    }

    public NewsPage clickExpandNewsButton() {
        Allure.step("Клик по кнопке сворачивания/разворачивания");
        waitForElement(getExpandNewsButton(), LONG_DELAY);
        getExpandNewsButton().perform(click());
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return this;
    }

    public boolean isSortButtonDisplayed() {
        Allure.step("Проверка отображения кнопки сортировки");
        return isElementDisplayedQuickly(getSortButton(), SHORT_DELAY);
    }

    public NewsPage clickQuotesButtonOnNewsScreen() {
        Allure.step("Клик по кнопке 'Quotes' на экране News");
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

    public boolean isNewsListEmpty() {
        Allure.step("Проверка пустого списка новостей");
        try {
            onView(withText("There is nothing here yet...")).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNewsListDisplayed() {
        Allure.step("Проверка отображения списка новостей");
        try {
            onView(withId(R.id.news_list_recycler_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areNewsControlsDisplayed() {
        Allure.step("Проверка отображения элементов управления новостями");
        try {
            onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()));
            onView(withId(R.id.filter_news_material_button)).check(matches(isDisplayed()));
            onView(withId(R.id.edit_news_material_button)).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean openNewsFilter() {
        Allure.step("Открытие фильтра новостей");
        try {
            ViewInteraction filterButton = onView(withId(R.id.filter_news_material_button));
            waitForElement(filterButton, LONG_DELAY);
            filterButton.perform(click());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public NewsPage clickEditNewsButton() {
        Allure.step("Клик по кнопке редактирования новостей");
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