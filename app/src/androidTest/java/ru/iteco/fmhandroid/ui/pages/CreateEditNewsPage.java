package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.DatePickerUtils;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class CreateEditNewsPage {

    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;

    // ID элементов
    private static final int TITLE_FIELD_ID = R.id.news_item_title_text_input_edit_text;
    private static final int CATEGORY_FIELD_ID = R.id.news_item_category_text_auto_complete_text_view;
    private static final int DESCRIPTION_FIELD_ID = R.id.news_item_description_text_input_edit_text;
    private static final int SAVE_BUTTON_ID = R.id.save_button;
    private static final int CANCEL_BUTTON_ID = R.id.cancel_button;
    private static final int PUBLISH_DATE_FIELD_ID = R.id.news_item_publish_date_text_input_edit_text;
    private static final int PUBLISH_TIME_FIELD_ID = R.id.news_item_publish_time_text_input_edit_text;

    public void checkEditScreenIsDisplayed() {
        Allure.step("Проверка отображения экрана редактирования новости");
        waitForElementWithId(TITLE_FIELD_ID, LONG_DELAY);
        checkTitleFieldDisplayed();
    }

    public boolean isEditScreenDisplayed() {
        Allure.step("Проверка отображения экрана редактирования");
        try {
            checkEditScreenIsDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void checkCreateScreenIsDisplayed() {
        Allure.step("Проверка отображения экрана создания новости");
        if (WaitUtils.isElementDisplayedWithId(CATEGORY_FIELD_ID, MEDIUM_DELAY)) {
            checkCategoryFieldDisplayed();
        } else {
            waitForElementWithId(TITLE_FIELD_ID, LONG_DELAY);
            checkTitleFieldDisplayed();
        }
    }

    public boolean isCreateScreenDisplayed() {
        Allure.step("Проверка отображения экрана создания");
        try {
            checkCreateScreenIsDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CreateEditNewsPage fillTitle(String title) {
        Allure.step("Заполнение заголовка новости: " + title);
        ViewInteraction titleField = onView(withId(TITLE_FIELD_ID));
        waitForElement(titleField, LONG_DELAY);
        titleField.perform(replaceText(title), closeSoftKeyboard());
        delay();
        return this;
    }

    public CreateEditNewsPage selectCategorySimple(String category) {
        Allure.step("Выбор категории новости: " + category);
        try {
            onView(withId(CATEGORY_FIELD_ID))
                    .perform(replaceText(category), closeSoftKeyboard());
        } catch (Exception e) {
            try {
                onView(withId(CATEGORY_FIELD_ID)).perform(click());
                WaitUtils.waitForMillis(LONG_DELAY);
                onView(withText(category)).perform(click());
            } catch (Exception e2) {
                // Игнорируем исключение
            }
        }
        delay();
        return this;
    }

    public CreateEditNewsPage fillDescription(String description) {
        Allure.step("Заполнение описания новости");
        try {
            ViewInteraction descriptionField = onView(withId(DESCRIPTION_FIELD_ID));
            waitForElement(descriptionField, LONG_DELAY);
            descriptionField.perform(replaceText(description), closeSoftKeyboard());
        } catch (Exception e) {
            // Игнорируем исключение
        }
        delay();
        return this;
    }

    public void clickSaveButton() {
        Allure.step("Нажатие кнопки SAVE для сохранения новости");
        ViewInteraction saveButton = onView(withId(SAVE_BUTTON_ID));
        waitForElement(saveButton, LONG_DELAY);
        saveButton.perform(click());
    }

    public void cancelWithConfirmation() {
        Allure.step("Отмена редактирования с подтверждением");
        try {
            onView(withId(CANCEL_BUTTON_ID)).perform(scrollTo(), click());
            confirmDialog("OK");
        } catch (Exception e) {
            onView(withId(CANCEL_BUTTON_ID)).perform(click());
        }
    }

    public boolean isErrorMessageDisplayed(String message) {
        Allure.step("Проверка отображения сообщения об ошибке: " + message);
        return WaitUtils.isElementDisplayedWithText(message, LONG_DELAY);
    }

    public void checkTitleFieldDisplayed() {
        Allure.step("Проверка отображения поля заголовка");
        onView(withId(TITLE_FIELD_ID)).check(matches(isDisplayed()));
    }

    public void checkCategoryFieldDisplayed() {
        Allure.step("Проверка отображения поля категории");
        onView(withId(CATEGORY_FIELD_ID)).check(matches(isDisplayed()));
    }

    public boolean isCategoryFieldDisplayed() {
        Allure.step("Проверка отображения поля категории");
        try {
            checkCategoryFieldDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CreateEditNewsPage selectCurrentDate() {
        Allure.step("Выбор текущей даты публикации через календарь");
        DatePickerUtils.selectCurrentDateViaCalendar();
        return this;
    }

    public CreateEditNewsPage selectCurrentTime() {
        Allure.step("Выбор текущего времени публикации через часы");
        DatePickerUtils.selectCurrentTimeViaTimePicker();
        return this;
    }

    public CreateEditNewsPage selectFutureDate(int days) {
        Allure.step("Выбор будущей даты публикации через календарь (через " + days + " дней)");
        DatePickerUtils.selectFutureDateViaCalendar(days);
        return this;
    }

    public CreateEditNewsPage selectPastDate(int days) {
        Allure.step("Выбор прошедшей даты публикации через календарь (за " + days + " дней до сегодня)");
        DatePickerUtils.selectPastDateViaCalendar(days);
        return this;
    }

    public CreateEditNewsPage selectTime(String time) {
        Allure.step("Выбор времени публикации через часы: " + time);
        DatePickerUtils.selectTimeViaTimePicker(time);
        return this;
    }

    public boolean isValidationErrorDisplayed() {
        Allure.step("Проверка отображения ошибки валидации");
        return isErrorMessageDisplayed("Saving failed") ||
                isErrorMessageDisplayed("Try again later");
    }

    public boolean isStillOnEditScreen() {
        Allure.step("Проверка, что остались на экране создания/редактирования");
        return WaitUtils.isElementDisplayedWithId(TITLE_FIELD_ID, MEDIUM_DELAY);
    }

    private void confirmDialog(String buttonText) {
        Allure.step("Подтверждение диалога с кнопкой: " + buttonText);
        WaitUtils.waitForElementWithText(buttonText, LONG_DELAY);
        onView(withText(buttonText)).inRoot(isDialog()).perform(click());
    }

    // Вспомогательные методы
    private void delay() {
        WaitUtils.waitForMillis(SHORT_DELAY);
    }

    private void waitForElement(ViewInteraction element, long timeout) {
        WaitUtils.waitForElement(element, timeout);
    }

    private void waitForElementWithId(int elementId, long timeout) {
        WaitUtils.waitForElementWithId(elementId, timeout);
    }
}