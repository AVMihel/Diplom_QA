package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.DatePickerUtils;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class CreateEditNewsPage {

    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int SHORT_TIMEOUT = 3000;
    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;

    // Текстовые константы
    private static final String SAVE_BUTTON_TEXT = "SAVE";
    private static final String CANCEL_BUTTON_TEXT = "CANCEL";
    private static final String OK_BUTTON_TEXT = "OK";
    private static final String ERROR_SAVING_FAILED = "Saving failed";
    private static final String ERROR_TRY_AGAIN = "Try again later";

    // ID элементов
    public static final int TITLE_INPUT_ID = R.id.news_item_title_text_input_edit_text;
    public static final int CATEGORY_INPUT_ID = R.id.news_item_category_text_auto_complete_text_view;
    public static final int DATE_INPUT_ID = R.id.news_item_publish_date_text_input_edit_text;
    public static final int TIME_INPUT_ID = R.id.news_item_publish_time_text_input_edit_text;
    public static final int DESCRIPTION_INPUT_ID = R.id.news_item_description_text_input_edit_text;
    public static final int ACTIVE_SWITCH_ID = R.id.switcher;
    public static final int SAVE_BUTTON_ID = R.id.save_button;
    public static final int CANCEL_BUTTON_ID = R.id.cancel_button;


    public boolean isCreateScreenDisplayed() {
        Allure.step("Проверка отображения экрана создания");
        return WaitUtils.isElementDisplayedWithId(TITLE_INPUT_ID, SHORT_TIMEOUT);
    }

    public boolean isEditScreenDisplayed() {
        Allure.step("Проверка отображения экрана редактирования");
        return WaitUtils.isElementDisplayedWithId(TITLE_INPUT_ID, SHORT_TIMEOUT);
    }

    public boolean isStillOnEditScreen() {
        Allure.step("Проверка, что остались на экране");
        return WaitUtils.isElementDisplayedWithId(TITLE_INPUT_ID, SHORT_TIMEOUT);
    }

    public boolean isValidationErrorDisplayed() {
        Allure.step("Проверка ошибки валидации");
        return WaitUtils.isElementDisplayedWithText(ERROR_SAVING_FAILED, SHORT_TIMEOUT) ||
                WaitUtils.isElementDisplayedWithText(ERROR_TRY_AGAIN, SHORT_TIMEOUT);
    }

    public boolean isCategoryFieldDisplayed() {
        try {
            onView(withId(CATEGORY_INPUT_ID)).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSwitchChecked() {
        try {
            onView(withId(ACTIVE_SWITCH_ID)).check(matches(isChecked()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CreateEditNewsPage waitForCreateScreen() {
        Allure.step("Ожидание экрана создания");
        WaitUtils.waitForElementWithId(TITLE_INPUT_ID, DEFAULT_TIMEOUT);
        return this;
    }

    public CreateEditNewsPage waitForEditScreen() {
        Allure.step("Ожидание экрана редактирования");
        WaitUtils.waitForElementWithId(TITLE_INPUT_ID, DEFAULT_TIMEOUT);
        return this;
    }

    public CreateEditNewsPage fillTitle(String title) {
        Allure.step("Ввод заголовка: " + title);
        ViewInteraction titleField = onView(allOf(
                withId(TITLE_INPUT_ID),
                isDisplayed()
        ));
        titleField.perform(replaceText(title), closeSoftKeyboard());
        WaitUtils.waitMillis(SHORT_DELAY);
        return this;
    }

    public CreateEditNewsPage selectCategory(String category) {
        Allure.step("Выбор категории: " + category);
        try {
            onView(withId(CATEGORY_INPUT_ID))
                    .perform(replaceText(category), closeSoftKeyboard());
        } catch (Exception e) {
            onView(withId(CATEGORY_INPUT_ID)).perform(click());
            WaitUtils.waitMillis(MEDIUM_DELAY);
            onView(withText(category)).perform(click());
        }
        WaitUtils.waitMillis(SHORT_DELAY);
        return this;
    }

    public CreateEditNewsPage setDate(String date) {
        Allure.step("Установка даты: " + date);
        try {
            String[] parts = date.split("\\.");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            DatePickerUtils.selectDateViaCalendar(year, month, day);
        } catch (Exception e) {
            DatePickerUtils.selectCurrentDateViaCalendar();
        }
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public CreateEditNewsPage setCurrentDate() {
        Allure.step("Установка текущей даты");
        DatePickerUtils.selectCurrentDateViaCalendar();
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public CreateEditNewsPage setTime(String time) {
        Allure.step("Установка времени: " + time);
        try {
            DatePickerUtils.selectTimeViaTimePicker(time);
        } catch (Exception e) {
            DatePickerUtils.selectCurrentTimeViaTimePicker();
        }
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public CreateEditNewsPage setCurrentTime() {
        Allure.step("Установка текущего времени");
        DatePickerUtils.selectCurrentTimeViaTimePicker();
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public CreateEditNewsPage fillDescription(String description) {
        Allure.step("Ввод описания");
        ViewInteraction descField = onView(allOf(
                withId(DESCRIPTION_INPUT_ID),
                isDisplayed()
        ));
        descField.perform(replaceText(description), closeSoftKeyboard());
        WaitUtils.waitMillis(SHORT_DELAY);
        return this;
    }

    public CreateEditNewsPage setActiveStatus(boolean active) {
        Allure.step("Установка статуса Active = " + active);
        boolean isChecked = isSwitchChecked();
        if (isChecked != active) {
            onView(withId(ACTIVE_SWITCH_ID)).perform(click());
        }
        return this;
    }

    public CreateEditNewsPage clickSaveButton() {
        Allure.step("Нажатие кнопки SAVE");
        WaitUtils.waitForElementWithId(SAVE_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(allOf(withId(SAVE_BUTTON_ID), withText(SAVE_BUTTON_TEXT))).perform(click());
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public CreateEditNewsPage clickCancelButton() {
        Allure.step("Нажатие кнопки CANCEL");
        WaitUtils.waitForElementWithId(CANCEL_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(allOf(withId(CANCEL_BUTTON_ID), withText(CANCEL_BUTTON_TEXT))).perform(click());
        WaitUtils.waitMillis(SHORT_DELAY);
        return this;
    }

    public CreateEditNewsPage confirmDialog() {
        Allure.step("Подтверждение в диалоге");
        WaitUtils.waitForElementWithText(OK_BUTTON_TEXT, DEFAULT_TIMEOUT);
        onView(withText(OK_BUTTON_TEXT)).perform(click());
        WaitUtils.waitMillis(MEDIUM_DELAY);
        return this;
    }

    public CreateEditNewsPage cancelWithConfirmation() {
        Allure.step("Отмена с подтверждением");
        clickCancelButton();
        confirmDialog();
        return this;
    }

    public String getTitleText() {
        return getTextFromField(TITLE_INPUT_ID);
    }

    // Вспомогателные методы

    private String getTextFromField(int fieldId) {
        final String[] text = new String[1];
        WaitUtils.waitForElementWithId(fieldId, DEFAULT_TIMEOUT);

        onView(withId(fieldId)).perform(new androidx.test.espresso.ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "get text";
            }

            @Override
            public void perform(androidx.test.espresso.UiController uiController, View view) {
                if (view instanceof TextView) {
                    text[0] = ((TextView) view).getText().toString();
                } else if (view instanceof android.widget.EditText) {
                    text[0] = ((android.widget.EditText) view).getText().toString();
                }
                uiController.loopMainThreadUntilIdle();
            }
        });

        return text[0] != null ? text[0].trim() : "";
    }
}