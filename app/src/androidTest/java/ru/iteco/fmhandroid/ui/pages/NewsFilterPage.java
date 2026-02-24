package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;

import android.widget.DatePicker;

import java.time.LocalDate;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsFilterPage {

    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int SHORT_TIMEOUT = 3000;
    private static final int MEDIUM_DELAY = 500;

    // Текстовые константы
    private static final String FILTER_TITLE = "Filter news";
    private static final String ERROR_WRONG_DATE = "Wrong date format";

    // ID элементов
    private static final int CATEGORY_FIELD_ID = R.id.news_item_category_text_auto_complete_text_view;
    private static final int START_DATE_FIELD_ID = R.id.news_item_publish_date_start_text_input_edit_text;
    private static final int END_DATE_FIELD_ID = R.id.news_item_publish_date_end_text_input_edit_text;
    private static final int FILTER_BUTTON_ID = R.id.filter_button;
    private static final int CANCEL_BUTTON_ID = R.id.cancel_button;
    private static final int ACTIVE_CHECKBOX_ID = R.id.filter_news_active_material_check_box;
    private static final int INACTIVE_CHECKBOX_ID = R.id.filter_news_inactive_material_check_box;


    public boolean isFilterDialogDisplayed() {
        Allure.step("Проверка отображения диалога фильтрации");
        return WaitUtils.isElementDisplayedWithText(FILTER_TITLE, SHORT_TIMEOUT);
    }

    public boolean areAllFilterElementsDisplayed() {
        Allure.step("Проверка отображения всех элементов фильтра");
        try {
            WaitUtils.waitMillis(MEDIUM_DELAY);
            onView(withText(FILTER_TITLE)).check(matches(isDisplayed()));
            onView(withId(CATEGORY_FIELD_ID)).check(matches(isDisplayed()));
            onView(withId(START_DATE_FIELD_ID)).check(matches(isDisplayed()));
            onView(withId(END_DATE_FIELD_ID)).check(matches(isDisplayed()));
            onView(withId(FILTER_BUTTON_ID)).check(matches(isDisplayed()));
            onView(withId(CANCEL_BUTTON_ID)).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkCategoryDropdownFunctionality() {
        Allure.step("Проверка функциональности выпадающего списка категорий");
        try {
            onView(withId(CATEGORY_FIELD_ID)).perform(click());
            WaitUtils.waitMillis(500);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkDatePickerFunctionality() {
        Allure.step("Проверка функциональности выбора даты");
        try {
            onView(withId(START_DATE_FIELD_ID)).perform(click());
            WaitUtils.waitForElementWithText("OK", DEFAULT_TIMEOUT);
            onView(withText("OK")).perform(click());
            WaitUtils.waitMillis(MEDIUM_DELAY);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorDisplayed() {
        Allure.step("Проверка отображения ошибки '" + ERROR_WRONG_DATE + "'");
        return WaitUtils.isElementDisplayedWithText(ERROR_WRONG_DATE, SHORT_TIMEOUT);
    }

    public NewsFilterPage selectFutureEndDateViaCalendar(int days) {
        Allure.step("Выбрать будущую дату окончания через календарь: +" + days + " дней");
        selectEndDateViaCalendar(days);
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public NewsFilterPage selectPastStartDateViaCalendar(int days) {
        Allure.step("Выбрать прошедшую дату начала через календарь: -" + days + " дней");
        selectStartDateViaCalendar(days);
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public NewsFilterPage applyFilter() {
        Allure.step("Применить фильтр");
        WaitUtils.waitForElementWithId(FILTER_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(FILTER_BUTTON_ID)).perform(click());
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public NewsFilterPage cancelFilter() {
        Allure.step("Отменить фильтрацию");
        WaitUtils.waitForElementWithId(CANCEL_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(CANCEL_BUTTON_ID)).perform(click());
        WaitUtils.waitMillis(2000);
        return this;
    }

    public NewsFilterPage setInvalidDates() {
        Allure.step("Установить невалидные даты (начало позже окончания)");
        selectPastStartDateViaCalendar(1);
        selectFutureEndDateViaCalendar(1);
        return this;
    }

    public NewsFilterPage checkOnlyActive() {
        Allure.step("Выбор только активных новостей");
        uncheckInactive();
        return this;
    }

    public NewsFilterPage checkOnlyInactive() {
        Allure.step("Выбор только неактивных новостей");
        uncheckActive();
        return this;
    }

    public NewsFilterPage uncheckActive() {
        Allure.step("Снятие чекбокса активных новостей");
        WaitUtils.waitForElementWithId(ACTIVE_CHECKBOX_ID, DEFAULT_TIMEOUT);
        onView(withId(ACTIVE_CHECKBOX_ID)).perform(click());
        return this;
    }

    public NewsFilterPage uncheckInactive() {
        Allure.step("Снятие чекбокса неактивных новостей");
        WaitUtils.waitForElementWithId(INACTIVE_CHECKBOX_ID, DEFAULT_TIMEOUT);
        onView(withId(INACTIVE_CHECKBOX_ID)).perform(click());
        return this;
    }

    // Вспомогательные методы

    private void selectStartDateViaCalendar(int days) {
        LocalDate date = LocalDate.now().minusDays(days);
        onView(withId(START_DATE_FIELD_ID)).perform(click());
        WaitUtils.waitMillis(500);
        onView(withClassName(is(DatePicker.class.getName())))
                .perform(setDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click());
        WaitUtils.waitMillis(300);
    }

    private void selectEndDateViaCalendar(int days) {
        LocalDate date = LocalDate.now().plusDays(days);
        onView(withId(END_DATE_FIELD_ID)).perform(click());
        WaitUtils.waitMillis(500);
        onView(withClassName(is(DatePicker.class.getName())))
                .perform(setDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click());
        WaitUtils.waitMillis(300);
    }
}