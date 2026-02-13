package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.NewsFilterUtils;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsFilterPage {

    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;

    // Выбрать будущую дату окончания через календарь
    public void selectFutureEndDateViaCalendar(int days) {
        NewsFilterUtils.selectFutureEndDateViaCalendar(days);
    }

    // Выбрать прошедшую дату начала через календарь
    public void selectPastStartDateViaCalendar(int days) {
        NewsFilterUtils.selectPastStartDateViaCalendar(days);
    }

    // Применить фильтр
    public void applyFilter() {
        ViewInteraction applyButton = onView(withId(R.id.filter_button));
        WaitUtils.waitForElement(applyButton, LONG_DELAY);
        applyButton.perform(click());
        WaitUtils.waitForMillis(SHORT_DELAY);
    }

    // Отменить фильтрацию
    public void cancelFilter() {
        ViewInteraction cancelButton = onView(withId(R.id.cancel_button));
        WaitUtils.waitForElement(cancelButton, LONG_DELAY);
        cancelButton.perform(click());
        WaitUtils.waitForMillis(SHORT_DELAY);
    }

    // Проверка отображения диалога фильтрации
    public boolean isFilterDialogDisplayed() {
        try {
            onView(withText("Filter news")).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка отображения всех элементов фильтра
    public boolean areAllFilterElementsDisplayed() {
        try {
            WaitUtils.waitForMillis(MEDIUM_DELAY);
            onView(withText("Filter news")).check(matches(isDisplayed()));
            onView(withId(R.id.news_item_category_text_auto_complete_text_view)).check(matches(isDisplayed()));
            onView(withId(R.id.news_item_publish_date_start_text_input_edit_text)).check(matches(isDisplayed()));
            onView(withId(R.id.news_item_publish_date_end_text_input_edit_text)).check(matches(isDisplayed()));
            onView(withId(R.id.filter_button)).check(matches(isDisplayed()));
            onView(withId(R.id.cancel_button)).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка функциональности выпадающего списка категорий
    public boolean checkCategoryDropdownFunctionality() {
        try {
            onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка функциональности выбора даты
    public boolean checkDatePickerFunctionality() {
        try {
            onView(withId(R.id.news_item_publish_date_start_text_input_edit_text)).perform(click());
            WaitUtils.waitForElementWithText("OK", LONG_DELAY);

            onView(withText("OK")).perform(click());
            WaitUtils.waitForMillis(MEDIUM_DELAY);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Установить невалидные даты (начало раньше окончания)
    public void setInvalidDates() {
        try {
            selectPastStartDateViaCalendar(1);
            WaitUtils.waitForMillis(MEDIUM_DELAY);
            selectFutureEndDateViaCalendar(1);
            WaitUtils.waitForMillis(MEDIUM_DELAY);
        } catch (Exception e) {
            // Игнорируем исключение
        }
    }

    // Проверка отображения ошибки 'Wrong date format'
    public boolean isErrorDisplayed() {
        try {
            onView(withText("Wrong date format")).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}