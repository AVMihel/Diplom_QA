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

    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;

    // Текстовые константы
    private static final String ALL_NEWS_TEXT = "ALL NEWS";
    private static final String ERROR_NAVIGATE_TO_NEWS = "Failed to navigate to 'News' section";

    // ID элементов
    private static final int ALL_NEWS_TEXT_VIEW_ID = R.id.all_news_text_view;
    private static final int EXPAND_BUTTON_ID = R.id.expand_material_button;
    private static final int SORT_BUTTON_ID = R.id.sort_news_material_button;

    // Проверка отображения кнопки "ALL NEWS"
    public NewsPage checkAllNewsButtonIsDisplayed() {
        WaitUtils.waitForElement(getAllNewsTextView(), LONG_DELAY);
        return this;
    }

    // Клик по кнопке "ALL NEWS"
    public NewsPage clickAllNewsButton() {
        WaitUtils.waitForElement(getAllNewsTextView(), LONG_DELAY);
        getAllNewsTextView().perform(click());
        return this;
    }

    // Проверка отображения экрана со списком новостей
    public NewsPage checkNewsListScreenIsDisplayed() {
        WaitUtils.waitForElement(getSortNewsButton(), LONG_DELAY);
        return this;
    }

    // Проверка отображения кнопки разворачивания блока новостей
    public NewsPage checkExpandNewsButtonIsDisplayed() {
        WaitUtils.waitForElement(getExpandNewsButton(), LONG_DELAY);
        return this;
    }

    // Клик по кнопке разворачивания блока новостей
    public NewsPage clickExpandNewsButton() {
        WaitUtils.waitForElement(getExpandNewsButton(), LONG_DELAY);
        getExpandNewsButton().perform(click());
        return this;
    }

    // Проверка скрытия кнопки "ALL NEWS" (после разворачивания блока)
    public NewsPage checkAllNewsButtonIsHidden() {
        try {
            WaitUtils.waitForElement(getAllNewsTextView(), MEDIUM_DELAY);
            throw new AssertionError("'ALL NEWS' button should be hidden after expanding the block");
        } catch (Exception e) {
            return this;
        }
    }

    // Проверка видимости кнопки "ALL NEWS"
    public NewsPage checkAllNewsButtonIsVisible() {
        WaitUtils.waitForElement(getAllNewsTextView(), LONG_DELAY);
        return this;
    }

    // Переход в раздел News с главного экрана
    public void navigateToNewsSection() {
        try {
            clickAllNewsButton();
            WaitUtils.waitForMillis(MEDIUM_DELAY);
        } catch (Exception e) {
            try {
                onView(withId(ALL_NEWS_TEXT_VIEW_ID))
                        .perform(click());
                WaitUtils.waitForMillis(MEDIUM_DELAY);
            } catch (Exception e2) {
                throw new RuntimeException(ERROR_NAVIGATE_TO_NEWS + e2.getMessage(), e2);
            }
        }

        checkNewsListScreenIsDisplayed();
    }

    // Вспомогательные методы для получения элементов интерфейса

    private ViewInteraction getAllNewsTextView() {
        return onView(
                allOf(withId(ALL_NEWS_TEXT_VIEW_ID), withText(ALL_NEWS_TEXT), isDisplayed())
        );
    }

    private ViewInteraction getExpandNewsButton() {
        return onView(
                allOf(withId(EXPAND_BUTTON_ID), isDisplayed())
        );
    }

    private ViewInteraction getSortNewsButton() {
        return onView(
                allOf(withId(SORT_BUTTON_ID), isDisplayed())
        );
    }
}