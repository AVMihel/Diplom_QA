package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

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

    private final ViewInteraction filterNewsButton = onView(
            allOf(withId(R.id.filter_news_material_button), isDisplayed())
    );

    private final ViewInteraction newsListRecyclerView = onView(
            allOf(withId(R.id.news_list_recycler_view), isDisplayed())
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

    // НОВЫЙ МЕТОД: Переход в раздел News с главного экрана
    public void navigateToNewsSection() {
        try {
            // Пробуем через кнопку "All news" на главном экране
            clickAllNewsButton();
            WaitUtils.waitForMillis(1000);
        } catch (Exception e) {
            // Если не сработало, пробуем альтернативный путь
            try {
                // Ищем и кликаем по любой видимой кнопке All news
                onView(withId(R.id.all_news_text_view))
                        .perform(click());
                WaitUtils.waitForMillis(1000);
            } catch (Exception e2) {
                throw new RuntimeException("Failed to navigate to News section: " + e2.getMessage(), e2);
            }
        }

        // Проверяем что перешли успешно
        checkNewsListScreenIsDisplayed();
    }

    // Дополнительный метод: Проверка отображения списка новостей
    public boolean isNewsListDisplayed() {
        return WaitUtils.isElementDisplayedWithId(R.id.news_list_recycler_view, 2000);
    }

    // Дополнительный метод: Проверка отображения кнопки фильтра
    public boolean isFilterButtonDisplayed() {
        return WaitUtils.isElementDisplayedWithId(R.id.filter_news_material_button, 2000);
    }

    // Дополнительный метод: Клик по кнопке сортировки
    public NewsPage clickSortButton() {
        WaitUtils.waitForElement(sortNewsButton, 2000);
        sortNewsButton.perform(click());
        return this;
    }

    // Дополнительный метод: Клик по кнопке фильтра
    public NewsPage clickFilterButton() {
        WaitUtils.waitForElement(filterNewsButton, 2000);
        filterNewsButton.perform(click());
        return this;
    }
}