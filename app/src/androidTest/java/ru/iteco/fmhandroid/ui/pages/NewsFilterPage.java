package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.NewsFilterUtils;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsFilterPage {

    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;

    public void selectFutureEndDateViaCalendar(int days) {
        Allure.step("Выбрать будущую дату окончания через календарь (через " + days + " дней)");
        NewsFilterUtils.selectFutureEndDateViaCalendar(days);
    }

    public void selectPastStartDateViaCalendar(int days) {
        Allure.step("Выбрать прошедшую дату начала через календарь (за " + days + " дней до сегодня)");
        NewsFilterUtils.selectPastStartDateViaCalendar(days);
    }

    public void applyFilter() {
        Allure.step("Применить фильтр");
        ViewInteraction applyButton = onView(withId(R.id.filter_button));
        WaitUtils.waitForElement(applyButton, LONG_DELAY);
        applyButton.perform(click());
        WaitUtils.waitForMillis(SHORT_DELAY);
    }

    public void cancelFilter() {
        Allure.step("Отменить фильтрацию");
        ViewInteraction cancelButton = onView(withId(R.id.cancel_button));
        WaitUtils.waitForElement(cancelButton, LONG_DELAY);
        cancelButton.perform(click());
        WaitUtils.waitForMillis(SHORT_DELAY);
    }

    public boolean isFilterDialogDisplayed() {
        Allure.step("Проверка отображения диалога фильтрации");
        try {
            onView(withText("Filter news")).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areAllFilterElementsDisplayed() {
        Allure.step("Проверка отображения всех элементов фильтра");
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

    public boolean checkCategoryDropdownFunctionality() {
        Allure.step("Проверка функциональности выпадающего списка категорий");
        try {
            onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkDatePickerFunctionality() {
        Allure.step("Проверка функциональности выбора даты");
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

    public void setInvalidDates() {
        Allure.step("Установить невалидные даты (начало раньше окончания)");
        try {
            selectFutureEndDateViaCalendar(1);
            selectPastStartDateViaCalendar(1);
        } catch (Exception e) {
            // Игнорируем исключение
        }
    }

    public boolean isErrorDisplayed() {
        Allure.step("Проверка отображения ошибки 'Wrong date format'");
        try {
            WaitUtils.waitForElementWithText("Wrong date format", LONG_DELAY);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}