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

    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 2000;

    // Текстовые константы
    private static final String QUOTES_TITLE_TEXT = "Love is all";

    // ID элементов
    private static final int QUOTES_TITLE_ID = R.id.our_mission_title_text_view;
    private static final int QUOTES_RECYCLER_VIEW_ID = R.id.our_mission_item_list_recycler_view;
    private static final int QUOTE_DESCRIPTION_ID = R.id.our_mission_item_description_text_view;

    public boolean isQuotesScreenDisplayed() {
        Allure.step("Проверка отображения экрана цитат");
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return isElementDisplayedQuickly(getQuotesTitle(), LONG_DELAY);
    }

    public QuotesPage checkQuotesListIsDisplayed() {
        Allure.step("Проверка отображения списка цитат");
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        waitForElement(getQuotesRecyclerView(), LONG_DELAY);
        return this;
    }

    public QuotesPage expandQuoteAtPosition(int position) {
        Allure.step("Развернуть/свернуть цитату по позиции " + position);
        checkQuotesListIsDisplayed();
        getQuotesRecyclerView().perform(
                RecyclerViewActions.actionOnItemAtPosition(position, click())
        );
        delay();
        return this;
    }

    public boolean isQuoteDescriptionDisplayed() {
        Allure.step("Проверка отображения описания цитаты");
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return isElementDisplayedQuickly(getQuoteDescriptionView(), LONG_DELAY);
    }

    public boolean isQuoteDescriptionHidden() {
        Allure.step("Проверка, что описание цитаты скрыто");
        WaitUtils.waitForMillis(MEDIUM_DELAY);
        return !isElementDisplayedQuickly(getQuoteDescriptionView(), SHORT_DELAY);
    }

    // Вспомогательные методы
    private void waitForElement(ViewInteraction element, long timeout) {
        WaitUtils.waitForElement(element, timeout);
    }

    private void delay() {
        WaitUtils.waitForMillis(SHORT_DELAY);
    }

    private boolean isElementDisplayedQuickly(ViewInteraction view, long timeout) {
        try {
            waitForElement(view, timeout);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Получение элементов
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