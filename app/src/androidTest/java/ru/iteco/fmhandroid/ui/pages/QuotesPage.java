package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class QuotesPage {

    private static final int LONG_DELAY = 1500;

    // Текстовые константы
    private static final String QUOTES_TITLE_TEXT = "Love is all";
    private static final String QUOTE_DESCRIPTION_HIDDEN_ERROR = "Quote description should be hidden";

    // ID элементов
    private static final int QUOTES_TITLE_ID = R.id.our_mission_title_text_view;
    private static final int QUOTES_RECYCLER_VIEW_ID = R.id.our_mission_item_list_recycler_view;
    private static final int QUOTE_DESCRIPTION_ID = R.id.our_mission_item_description_text_view;

    // Проверяет, что экран Quotes отображается
    public QuotesPage checkQuotesScreenIsDisplayed() {
        WaitUtils.waitForElement(getQuotesTitle(), LONG_DELAY);
        return this;
    }

    // Проверяет, что список цитат отображается
    public QuotesPage checkQuotesListIsDisplayed() {
        WaitUtils.waitForElement(getQuotesRecyclerView(), LONG_DELAY);
        return this;
    }

    // Кликает на стрелку разворачивания для цитаты по указанной позиции
    public QuotesPage expandQuoteAtPosition(int position) {
        checkQuotesListIsDisplayed();
        getQuotesRecyclerView().perform(
                RecyclerViewActions.actionOnItemAtPosition(position, click())
        );
        return this;
    }

    // Проверяет, что описание цитаты отображается (развернутое состояние)
    public QuotesPage checkQuoteDescriptionIsDisplayed() {
        WaitUtils.waitForElement(getQuoteDescriptionView(), LONG_DELAY);
        return this;
    }

    // Проверяет, что описание цитаты скрыто (свернутое состояние)
    public QuotesPage checkQuoteDescriptionIsHidden() {
        try {
            getQuoteDescriptionView().check(matches(isDisplayed()));
            throw new AssertionError(QUOTE_DESCRIPTION_HIDDEN_ERROR);
        } catch (Exception e) {
            // Ожидаемое поведение - элемент не отображается
        }
        return this;
    }

    // Возвращается на главный экран через боковое меню
    public void goBackToMainScreen() {
        NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
        navigationDrawer.openMenu().clickMainMenuItem();
    }

    // Вспомогательные методы для получения элементов интерфейса

    private ViewInteraction getQuotesTitle() {
        return onView(
                allOf(withId(QUOTES_TITLE_ID), withText(QUOTES_TITLE_TEXT))
        );
    }

    private ViewInteraction getQuotesRecyclerView() {
        return onView(
                allOf(withId(QUOTES_RECYCLER_VIEW_ID), isDisplayed())
        );
    }

    private ViewInteraction getQuoteDescriptionView() {
        return onView(
                allOf(withId(QUOTE_DESCRIPTION_ID), isDisplayed())
        );
    }
}