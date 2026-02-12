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

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;

public class DatePickerUtils {

    public static void selectDateViaCalendar(int year, int month, int day) {
        Allure.step("Выбрать дату через календарь: " + day + "." + month + "." + year);
        onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());
        WaitUtils.waitForMillis(500);
        onView(withClassName(is(DatePicker.class.getName()))).perform(setDate(year, month, day));
        onView(withId(android.R.id.button1)).perform(click());
        WaitUtils.waitForMillis(300);
    }

    public static void selectCurrentDateViaCalendar() {
        Allure.step("Выбрать текущую дату через календарь");
        LocalDate today = LocalDate.now();
        selectDateViaCalendar(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
    }

    public static void selectFutureDateViaCalendar(int days) {
        Allure.step("Выбрать будущую дату через календарь (через " + days + " дней)");
        LocalDate futureDate = LocalDate.now().plusDays(days);
        selectDateViaCalendar(futureDate.getYear(), futureDate.getMonthValue(), futureDate.getDayOfMonth());
    }

    public static void selectPastDateViaCalendar(int days) {
        Allure.step("Выбрать прошедшую дату через календарь (за " + days + " дней до сегодня)");
        LocalDate pastDate = LocalDate.now().minusDays(days);
        selectDateViaCalendar(pastDate.getYear(), pastDate.getMonthValue(), pastDate.getDayOfMonth());
    }

    public static void selectTimeViaTimePicker(int hour, int minute) {
        Allure.step("Выбрать время через часы: " + hour + ":" + (minute < 10 ? "0" + minute : minute));
        onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());
        WaitUtils.waitForMillis(500);
        onView(withClassName(is(TimePicker.class.getName()))).perform(setTime(hour, minute));
        onView(withId(android.R.id.button1)).perform(click());
        WaitUtils.waitForMillis(300);
    }

    public static void selectCurrentTimeViaTimePicker() {
        Allure.step("Выбрать текущее время через часы");
        LocalDateTime now = LocalDateTime.now();
        selectTimeViaTimePicker(now.getHour(), now.getMinute());
    }

    public static void selectTimeViaTimePicker(String time) {
        Allure.step("Выбрать время через часы: " + time);
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        selectTimeViaTimePicker(hour, minute);
    }
}