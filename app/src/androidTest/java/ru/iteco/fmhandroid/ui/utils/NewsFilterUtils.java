package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

import android.widget.DatePicker;

import java.time.LocalDate;

import ru.iteco.fmhandroid.R;

public class NewsFilterUtils {

    // Выбрать дату начала через календарь фильтра
    public static void selectStartDateViaCalendar(int year, int month, int day) {
        onView(withId(R.id.news_item_publish_date_start_text_input_edit_text)).perform(click());
        WaitUtils.waitForMillis(500);
        onView(withClassName(is(DatePicker.class.getName()))).perform(setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
        WaitUtils.waitForMillis(300);
    }

    // Выбрать дату окончания через календарь фильтра
    public static void selectEndDateViaCalendar(int year, int month, int day) {
        onView(withId(R.id.news_item_publish_date_end_text_input_edit_text)).perform(click());
        WaitUtils.waitForMillis(500);
        onView(withClassName(is(DatePicker.class.getName()))).perform(setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
        WaitUtils.waitForMillis(300);
    }

    // Выбрать будущую дату как дату окончания через календарь
    public static void selectFutureEndDateViaCalendar(int days) {
        LocalDate futureDate = LocalDate.now().plusDays(days);
        selectEndDateViaCalendar(futureDate.getYear(), futureDate.getMonthValue(), futureDate.getDayOfMonth());
    }

    // Выбрать прошедшую дату как дату начала через календарь
    public static void selectPastStartDateViaCalendar(int days) {
        LocalDate pastDate = LocalDate.now().minusDays(days);
        selectStartDateViaCalendar(pastDate.getYear(), pastDate.getMonthValue(), pastDate.getDayOfMonth());
    }
}