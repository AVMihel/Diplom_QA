package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class QuotesPage {

    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int SHORT_TIMEOUT = 3000;
    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;

    private static final String QUOTES_TITLE_TEXT = "Love is all";

    private static final int QUOTES_TITLE_ID = R.id.our_mission_title_text_view;
    private static final int QUOTES_RECYCLER_VIEW_ID = R.id.our_mission_item_list_recycler_view;
    private static final int QUOTE_DESCRIPTION_ID = R.id.our_mission_item_description_text_view;


    public boolean isQuotesScreenDisplayed() {
        Allure.step("Проверка отображения экрана цитат");
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return WaitUtils.isElementDisplayed(getQuotesTitle(), SHORT_TIMEOUT);
    }

    public boolean isQuoteDescriptionDisplayed() {
        Allure.step("Проверка отображения описания цитаты");
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return WaitUtils.isElementDisplayed(getQuoteDescriptionView(), SHORT_TIMEOUT);
    }

    public boolean isQuoteDescriptionHidden() {
        Allure.step("Проверка, что описание цитаты скрыто");
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return !WaitUtils.isElementDisplayed(getQuoteDescriptionView(), SHORT_TIMEOUT);
    }

    public QuotesPage checkQuotesListIsDisplayed() {
        Allure.step("Проверка отображения списка цитат");
        WaitUtils.waitMillis(MEDIUM_DELAY);
        WaitUtils.waitForElement(getQuotesRecyclerView(), DEFAULT_TIMEOUT);
        return this;
    }

    public QuotesPage expandQuoteAtPosition(int position) {
        Allure.step("Развернуть цитату на позиции " + position);
        checkQuotesListIsDisplayed();

        getQuotesRecyclerView().perform(
                RecyclerViewActions.actionOnItemAtPosition(position, click())
        );

        WaitUtils.waitMillis(SHORT_DELAY);
        return this;
    }

    public QuotesPage collapseQuoteAtPosition(int position) {
        Allure.step("Свернуть цитату на позиции " + position);

        getQuotesRecyclerView().perform(
                RecyclerViewActions.actionOnItemAtPosition(position, click())
        );

        WaitUtils.waitMillis(SHORT_DELAY);
        return this;
    }

    // Вспомогательные методы

    private ViewInteraction getQuotesTitle() {
        return onView(allOf(withId(QUOTES_TITLE_ID), withText(QUOTES_TITLE_TEXT)));
    }

    private ViewInteraction getQuotesRecyclerView() {
        return onView(allOf(withId(QUOTES_RECYCLER_VIEW_ID), isDisplayed()));
    }

    private ViewInteraction getQuoteDescriptionView() {
        return onView(allOf(withId(QUOTE_DESCRIPTION_ID), isDisplayed()));
    }
}