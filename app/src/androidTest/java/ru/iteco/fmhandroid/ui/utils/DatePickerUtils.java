package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;

import io.qameta.allure.kotlin.Step;
import ru.iteco.fmhandroid.R;

public class DatePickerUtils {

    @Step("Выбрать дату через календарь: {day}.{month}.{year}")
    public static void selectDateViaCalendar(int year, int month, int day) {
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
        WaitUtils.waitForMillis(500);
        onView(withClassName(is(DatePicker.class.getName()))).perform(setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
        WaitUtils.waitForMillis(300);
    }

    @Step("Выбрать текущую дату через календарь")
    public static void selectCurrentDateViaCalendar() {
        LocalDate today = LocalDate.now();
        selectDateViaCalendar(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
    }

    @Step("Выбрать будущую дату через календарь (через {days} дней)")
    public static void selectFutureDateViaCalendar(int days) {
        LocalDate futureDate = LocalDate.now().plusDays(days);
        selectDateViaCalendar(futureDate.getYear(), futureDate.getMonthValue(), futureDate.getDayOfMonth());
    }

    @Step("Выбрать прошедшую дату через календарь (за {days} дней до сегодня)")
    public static void selectPastDateViaCalendar(int days) {
        LocalDate pastDate = LocalDate.now().minusDays(days);
        selectDateViaCalendar(pastDate.getYear(), pastDate.getMonthValue(), pastDate.getDayOfMonth());
    }

    @Step("Выбрать время через часы: {hour}:{minute}")
    public static void selectTimeViaTimePicker(int hour, int minute) {
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
        WaitUtils.waitForMillis(500);
        onView(withClassName(is(TimePicker.class.getName()))).perform(setTime(hour, minute));
        onView(withId(android.R.id.button1)).perform(click());
        WaitUtils.waitForMillis(300);
    }

    @Step("Выбрать текущее время через часы")
    public static void selectCurrentTimeViaTimePicker() {
        LocalDateTime now = LocalDateTime.now();
        selectTimeViaTimePicker(now.getHour(), now.getMinute());
    }

    @Step("Выбрать время через часы (используя строку формата HH:mm)")
    public static void selectTimeViaTimePicker(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        selectTimeViaTimePicker(hour, minute);
    }
}