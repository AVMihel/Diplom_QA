package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsPage {

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

    // Проверка отображения кнопки "ALL NEWS"
    public NewsPage checkAllNewsButtonIsDisplayed() {
        WaitUtils.waitForElement(allNewsTitleTextView, 3000);
        return this;
    }

    // Клик по кнопке "All news" (на главном экране)
    public NewsPage clickAllNewsButton() {
        WaitUtils.waitForElement(allNewsTextView, 2000);
        allNewsTextView.perform(click());
        return this;
    }

    // Проверка отображения экрана со списком новостей
    public NewsPage checkNewsListScreenIsDisplayed() {
        WaitUtils.waitForElement(sortNewsButton, 3000);
        return this;
    }

    // Проверка отображения кнопки разворачивания блока новостей
    public NewsPage checkExpandNewsButtonIsDisplayed() {
        WaitUtils.waitForElement(expandNewsButton, 2000);
        return this;
    }

    // Клик по кнопке разворачивания блока новостей
    public NewsPage clickExpandNewsButton() {
        WaitUtils.waitForElement(expandNewsButton, 2000);
        expandNewsButton.perform(click());
        return this;
    }

    // Проверка скрытия кнопки "ALL NEWS" (после разворачивания блока)
    public NewsPage checkAllNewsButtonIsHidden() {
        try {
            WaitUtils.waitForElement(allNewsTitleTextView, 1000);
            throw new AssertionError("The 'ALL NEWS' button should be hidden after the block is expanded.");
        } catch (Exception e) {
            return this;
        }
    }

    // Проверка видимости кнопки "ALL NEWS"
    public NewsPage checkAllNewsButtonIsVisible() {
        WaitUtils.waitForElement(allNewsTitleTextView, 2000);
        return this;
    }
}