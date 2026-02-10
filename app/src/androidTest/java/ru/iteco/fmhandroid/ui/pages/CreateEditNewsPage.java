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

import io.qameta.allure.kotlin.Step;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.DatePickerUtils;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

@DisplayName("Страница создания/редактирования новостей")
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

    @Step("Проверка отображения экрана редактирования новости")
    public void checkEditScreenIsDisplayed() {
        waitForElementWithId(TITLE_FIELD_ID, LONG_DELAY);
        checkTitleFieldDisplayed();
    }

    @Step("Проверка отображения экрана редактирования (возвращает 'boolean')")
    public boolean isEditScreenDisplayed() {
        try {
            checkEditScreenIsDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Проверка отображения экрана создания новости")
    public void checkCreateScreenIsDisplayed() {
        if (WaitUtils.isElementDisplayedWithId(CATEGORY_FIELD_ID, MEDIUM_DELAY)) {
            checkCategoryFieldDisplayed();
        } else {
            waitForElementWithId(TITLE_FIELD_ID, LONG_DELAY);
            checkTitleFieldDisplayed();
        }
    }

    @Step("Проверка отображения экрана создания (возвращает 'boolean')")
    public boolean isCreateScreenDisplayed() {
        try {
            checkCreateScreenIsDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Заполнение заголовка новости: {title}")
    public CreateEditNewsPage fillTitle(String title) {
        ViewInteraction titleField = onView(withId(TITLE_FIELD_ID));
        waitForElement(titleField, LONG_DELAY);
        titleField.perform(replaceText(title), closeSoftKeyboard());
        delay();
        return this;
    }

    @Step("Выбор категории новости: {category}")
    public CreateEditNewsPage selectCategorySimple(String category) {
        try {
            onView(withId(CATEGORY_FIELD_ID))
                    .perform(replaceText(category), closeSoftKeyboard());
        } catch (Exception e) {
            try {
                onView(withId(CATEGORY_FIELD_ID)).perform(click());
                WaitUtils.waitForMillis(LONG_DELAY);
                onView(withText(category)).perform(click());
            } catch (Exception e2) {
            }
        }
        delay();
        return this;
    }

    @Step("Заполнение описания новости")
    public CreateEditNewsPage fillDescription(String description) {
        try {
            ViewInteraction descriptionField = onView(withId(DESCRIPTION_FIELD_ID));
            waitForElement(descriptionField, LONG_DELAY);
            descriptionField.perform(replaceText(description), closeSoftKeyboard());
        } catch (Exception e) {
        }
        delay();
        return this;
    }

    @Step("Нажатие кнопки SAVE для сохранения новости")
    public void clickSaveButton() {
        ViewInteraction saveButton = onView(withId(SAVE_BUTTON_ID));
        waitForElement(saveButton, LONG_DELAY);
        saveButton.perform(click());
    }

    @Step("Отмена редактирования с подтверждением")
    public void cancelWithConfirmation() {
        try {
            onView(withId(CANCEL_BUTTON_ID)).perform(scrollTo(), click());
            confirmDialog("OK");
        } catch (Exception e) {
            onView(withId(CANCEL_BUTTON_ID)).perform(click());
        }
    }

    @Step("Проверка отображения сообщения об ошибке: {message}")
    public boolean isErrorMessageDisplayed(String message) {
        return WaitUtils.isElementDisplayedWithText(message, LONG_DELAY);
    }

    @Step("Проверка отображения поля заголовка")
    public void checkTitleFieldDisplayed() {
        onView(withId(TITLE_FIELD_ID)).check(matches(isDisplayed()));
    }

    @Step("Проверка отображения поля категории")
    public void checkCategoryFieldDisplayed() {
        onView(withId(CATEGORY_FIELD_ID)).check(matches(isDisplayed()));
    }

    @Step("Проверка отображения поля категории (возвращает 'boolean')")
    public boolean isCategoryFieldDisplayed() {
        try {
            checkCategoryFieldDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Выбор текущей даты публикации через календарь")
    public CreateEditNewsPage selectCurrentDate() {
        DatePickerUtils.selectCurrentDateViaCalendar();
        return this;
    }

    @Step("Выбор текущего времени публикации через часы")
    public CreateEditNewsPage selectCurrentTime() {
        DatePickerUtils.selectCurrentTimeViaTimePicker();
        return this;
    }

    @Step("Выбор будущей даты публикации через календарь (через {days} дней)")
    public CreateEditNewsPage selectFutureDate(int days) {
        DatePickerUtils.selectFutureDateViaCalendar(days);
        return this;
    }

    @Step("Выбор прошедшей даты публикации через календарь (за {days} дней до сегодня)")
    public CreateEditNewsPage selectPastDate(int days) {
        DatePickerUtils.selectPastDateViaCalendar(days);
        return this;
    }

    @Step("Выбор времени публикации через часы: {time}")
    public CreateEditNewsPage selectTime(String time) {
        DatePickerUtils.selectTimeViaTimePicker(time);
        return this;
    }

    @Step("Проверка отображения ошибки валидации")
    public boolean isValidationErrorDisplayed() {
        return isErrorMessageDisplayed("Saving failed") ||
                isErrorMessageDisplayed("Try again later");
    }

    @Step("Проверка, что остались на экране создания/редактирования")
    public boolean isStillOnEditScreen() {
        return WaitUtils.isElementDisplayedWithId(TITLE_FIELD_ID, MEDIUM_DELAY);
    }

    @Step("Подтверждение диалога с кнопкой: {buttonText}")
    private void confirmDialog(String buttonText) {
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