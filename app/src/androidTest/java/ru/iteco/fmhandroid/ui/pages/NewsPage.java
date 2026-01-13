package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

// Page Object для работы с блоком новостей на главной странице
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
        WaitUtils.waitForElement(allNewsTitleTextView, 5000);
        allNewsTitleTextView.check(matches(withText("ALL NEWS")));
        return this;
    }

    // Нажимает кнопку "ALL NEWS" для перехода к полному списку новостей
    public NewsPage clickAllNewsButton() {
        WaitUtils.waitForElement(allNewsTextView, 5000);
        allNewsTextView.perform(click());
        return this;
    }

    // Проверяет переход на экран списка новостей (по наличию кнопки сортировки)
    public NewsPage checkNewsListScreenIsDisplayed() {
        WaitUtils.waitForElement(sortNewsButton, 5000);
        sortNewsButton.check(matches(isDisplayed()));
        return this;
    }

    // Проверяет отображение кнопки сворачивания/разворачивания блока News
    public NewsPage checkExpandNewsButtonIsDisplayed() {
        WaitUtils.waitForElement(expandNewsButton, 5000);
        expandNewsButton.check(matches(isDisplayed()));
        return this;
    }

    // Нажимает кнопку сворачивания/разворачивания блока News
    public NewsPage clickExpandNewsButton() {
        WaitUtils.waitForElement(expandNewsButton, 5000);
        expandNewsButton.perform(click());
        return this;
    }

    // Проверяет, что кнопка "ALL NEWS" скрыта (после разворачивания блока)
    public NewsPage checkAllNewsButtonIsHidden() {
        try {
            allNewsTitleTextView.check(matches(isDisplayed()));
            throw new AssertionError("Кнопка 'ALL NEWS' должна быть скрыта после разворачивания блока");
        } catch (Exception e) {
            // Ожидаемое поведение - элемент не найден
            return this;
        }
    }

    // Проверяет, что кнопка "ALL NEWS" отображается (после сворачивания блока)
    public NewsPage checkAllNewsButtonIsVisible() {
        WaitUtils.waitForElement(allNewsTitleTextView, 3000);
        allNewsTitleTextView.check(matches(isDisplayed()));
        return this;
    }

    // Вспомогательный метод для поиска дочерних элементов
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}