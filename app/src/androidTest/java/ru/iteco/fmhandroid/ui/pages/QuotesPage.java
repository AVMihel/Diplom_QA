package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class QuotesPage {

    private final ViewInteraction quotesTitle = onView(
            allOf(withId(R.id.our_mission_title_text_view), withText("Love is all"))
    );

    private final ViewInteraction quotesRecyclerView = onView(
            allOf(withId(R.id.our_mission_item_list_recycler_view))
    );

    // Проверяет, что экран Quotes отображается
    public QuotesPage checkQuotesScreenIsDisplayed() {
        WaitUtils.waitForElement(quotesTitle, 4000);
        return this;
    }

    // Проверяет, что список цитат отображается
    public QuotesPage checkQuotesListIsDisplayed() {
        WaitUtils.waitForElement(quotesRecyclerView, 3000);
        return this;
    }

    // Кликает на стрелку разворачивания для цитаты по указанной позиции
    public QuotesPage expandQuoteAtPosition(int position) {
        checkQuotesListIsDisplayed();

        // Кликаем на карточку цитаты по позиции
        quotesRecyclerView.perform(
                RecyclerViewActions.actionOnItemAtPosition(position, click())
        );

        return this;
    }

    // Проверяет, что описание цитаты отображается (развернутое состояние)
    public QuotesPage checkQuoteDescriptionIsDisplayedAtPosition(int position) {
        // После клика ждем появления описания
        ViewInteraction descriptionView = onView(
                allOf(
                        withId(R.id.our_mission_item_description_text_view),
                        isDisplayed()
                )
        );

        WaitUtils.waitForElement(descriptionView, 1500);
        return this;
    }

    // Проверяет, что описание цитаты скрыто (свернутое состояние)
    public QuotesPage checkQuoteDescriptionIsHidden() {
        try {
            onView(withId(R.id.our_mission_item_description_text_view)).check(matches(isDisplayed()));
            throw new AssertionError("Quote description should be hidden");
        } catch (Exception e) {
            // Ожидаемое поведение - элемент не найден
        }
        return this;
    }

    // Выполняет полный цикл: развернуть и свернуть цитату
    public QuotesPage expandAndCollapseQuote(int position) {
        // 1. Разворачиваем цитату
        expandQuoteAtPosition(position);

        // 2. Проверяем, что описание отображается
        checkQuoteDescriptionIsDisplayedAtPosition(position);

        // 3. Снова кликаем, чтобы свернуть
        expandQuoteAtPosition(position);

        // 4. Проверяем, что описание скрыто
        checkQuoteDescriptionIsHidden();

        return this;
    }

    // Возвращается на главный экран через боковое меню
    public void goBackToMainScreen() {
        NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
        navigationDrawer.openMenu().clickMainMenuItem();
    }
}