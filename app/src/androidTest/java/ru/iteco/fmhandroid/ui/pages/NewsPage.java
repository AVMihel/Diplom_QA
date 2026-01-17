package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsPage {

    // Элементы блока News на главной странице
    private final ViewInteraction allNewsTextView = onView(
            allOf(withId(R.id.all_news_text_view), withText("All news"), isDisplayed())
    );

    private final ViewInteraction allNewsTitleTextView = onView(
            allOf(withId(R.id.all_news_text_view), withText("ALL NEWS"), isDisplayed())
    );

    private final ViewInteraction expandNewsButton = onView(
            allOf(withId(R.id.expand_material_button), isDisplayed())
    );

    private final ViewInteraction sortNewsButton = onView(
            allOf(withId(R.id.sort_news_material_button), isDisplayed())
    );

    // Проверяет отображение кнопки "ALL NEWS" на главной странице
    public NewsPage checkAllNewsButtonIsDisplayed() {
        WaitUtils.waitForElement(allNewsTitleTextView, 3000);
        allNewsTitleTextView.check(matches(withText("ALL NEWS")));
        return this;
    }

    // Нажимает кнопку "ALL NEWS" для перехода к полному списку новостей
    public NewsPage clickAllNewsButton() {
        WaitUtils.waitForElement(allNewsTextView, 2000);
        allNewsTextView.perform(click());
        return this;
    }

    // Проверяет переход на экран списка новостей (по наличию кнопки сортировки)
    public NewsPage checkNewsListScreenIsDisplayed() {
        WaitUtils.waitForElement(sortNewsButton, 3000);
        sortNewsButton.check(matches(isDisplayed()));
        return this;
    }

    // Проверяет отображение кнопки сворачивания/разворачивания блока News
    public NewsPage checkExpandNewsButtonIsDisplayed() {
        WaitUtils.waitForElement(expandNewsButton, 2000);
        expandNewsButton.check(matches(isDisplayed()));
        return this;
    }

    // Нажимает кнопку сворачивания/разворачивания блока News
    public NewsPage clickExpandNewsButton() {
        WaitUtils.waitForElement(expandNewsButton, 2000);
        expandNewsButton.perform(click());
        return this;
    }

    // Проверяет, что кнопка "ALL NEWS" скрыта (после разворачивания блока)
    public NewsPage checkAllNewsButtonIsHidden() {
        try {
            WaitUtils.waitForElement(allNewsTitleTextView, 1000);
            throw new AssertionError("The 'ALL NEWS' button should be hidden after the block is expanded.");
        } catch (Exception e) {
            // Ожидаемое поведение - элемент не найден
            return this;
        }
    }

    // Проверяет, что кнопка "ALL NEWS" отображается (после сворачивания блока)
    public NewsPage checkAllNewsButtonIsVisible() {
        WaitUtils.waitForElement(allNewsTitleTextView, 2000);
        allNewsTitleTextView.check(matches(isDisplayed()));
        return this;
    }
}