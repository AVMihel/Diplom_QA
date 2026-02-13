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
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;

import org.hamcrest.Matcher;

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
    private static final int SWITCHER_ID = R.id.switcher;

    // Проверка отображения экрана редактирования новости
    public void checkEditScreenIsDisplayed() {
        waitForElementWithId(TITLE_FIELD_ID, LONG_DELAY);
        checkTitleFieldDisplayed();
    }

    // Проверка отображения экрана редактирования
    public boolean isEditScreenDisplayed() {
        try {
            checkEditScreenIsDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка отображения экрана создания новости
    public void checkCreateScreenIsDisplayed() {
        if (WaitUtils.isElementDisplayedWithId(CATEGORY_FIELD_ID, MEDIUM_DELAY)) {
            checkCategoryFieldDisplayed();
        } else {
            waitForElementWithId(TITLE_FIELD_ID, LONG_DELAY);
            checkTitleFieldDisplayed();
        }
    }

    // Проверка отображения экрана создания
    public boolean isCreateScreenDisplayed() {
        try {
            checkCreateScreenIsDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Заполнение заголовка новости
    public CreateEditNewsPage fillTitle(String title) {
        ViewInteraction titleField = onView(withId(TITLE_FIELD_ID));
        waitForElement(titleField, LONG_DELAY);
        titleField.perform(replaceText(title), closeSoftKeyboard());
        delay();
        return this;
    }

    // Выбор категории новости
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
                // Игнорируем исключение
            }
        }
        delay();
        return this;
    }

    // Заполнение описания новости
    public CreateEditNewsPage fillDescription(String description) {
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

    // Нажатие кнопки SAVE для сохранения новости
    public void clickSaveButton() {
        ViewInteraction saveButton = onView(withId(SAVE_BUTTON_ID));
        waitForElement(saveButton, LONG_DELAY);
        saveButton.perform(click());
    }

    // Отмена редактирования с подтверждением
    public void cancelWithConfirmation() {
        try {
            onView(withId(CANCEL_BUTTON_ID)).perform(scrollTo(), click());
            confirmDialog("OK");
        } catch (Exception e) {
            onView(withId(CANCEL_BUTTON_ID)).perform(click());
        }
    }

    // Проверка отображения сообщения об ошибке
    public boolean isErrorMessageDisplayed(String message) {
        return WaitUtils.isElementDisplayedWithText(message, LONG_DELAY);
    }

    // Проверка отображения поля заголовка
    public void checkTitleFieldDisplayed() {
        onView(withId(TITLE_FIELD_ID)).check(matches(isDisplayed()));
    }

    // Проверка отображения поля категории
    public void checkCategoryFieldDisplayed() {
        onView(withId(CATEGORY_FIELD_ID)).check(matches(isDisplayed()));
    }

    // Проверка отображения поля категории
    public boolean isCategoryFieldDisplayed() {
        try {
            checkCategoryFieldDisplayed();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Выбор текущей даты публикации через календарь
    public CreateEditNewsPage selectCurrentDate() {
        DatePickerUtils.selectCurrentDateViaCalendar();
        return this;
    }

    // Выбор текущего времени публикации через часы
    public CreateEditNewsPage selectCurrentTime() {
        DatePickerUtils.selectCurrentTimeViaTimePicker();
        return this;
    }

    // Выбор прошедшей даты публикации через календарь
    public CreateEditNewsPage selectPastDate(int days) {
        DatePickerUtils.selectPastDateViaCalendar(days);
        return this;
    }

    // Проверка отображения ошибки валидации
    public boolean isValidationErrorDisplayed() {
        return isErrorMessageDisplayed("Saving failed") ||
                isErrorMessageDisplayed("Try again later");
    }

    // Проверка, что остались на экране создания/редактирования
    public boolean isStillOnEditScreen() {
        return WaitUtils.isElementDisplayedWithId(TITLE_FIELD_ID, MEDIUM_DELAY);
    }

    // Внутренний метод для получения текста из поля по ID
    private String getTextFromField(int fieldId) {
        final String[] text = new String[1];

        WaitUtils.waitForElementWithId(fieldId, LONG_DELAY);

        onView(allOf(withId(fieldId), isDisplayed()))
                .perform(new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isDisplayed();
                    }

                    @Override
                    public String getDescription() {
                        return "get text from field with id: " + fieldId;
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        if (view instanceof TextView) {
                            text[0] = ((TextView) view).getText().toString();
                        } else if (view instanceof EditText) {
                            text[0] = ((EditText) view).getText().toString();
                        } else {
                            text[0] = "";
                        }
                        uiController.loopMainThreadUntilIdle();
                    }
                });

        return text[0] != null ? text[0].trim() : "";
    }

    // Получение текста из поля заголовка
    public String getTitleText() {
        return getTextFromField(TITLE_FIELD_ID);
    }

    // Получение текста из поля описания
    public String getDescriptionText() {
        return getTextFromField(DESCRIPTION_FIELD_ID);
    }

    // Получение текста из поля категории
    public String getCategoryText() {
        return getTextFromField(CATEGORY_FIELD_ID);
    }

    // Получение текста из поля даты публикации
    public String getPublishDateText() {
        return getTextFromField(PUBLISH_DATE_FIELD_ID);
    }

    // Получение текста из поля времени публикации
    public String getPublishTimeText() {
        return getTextFromField(PUBLISH_TIME_FIELD_ID);
    }

    // Получение статуса переключателя Active
    public boolean isActiveSwitchChecked() {
        final boolean[] isChecked = new boolean[1];
        WaitUtils.waitForElementWithId(SWITCHER_ID, LONG_DELAY);

        onView(withId(SWITCHER_ID)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "get switch state";
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof android.widget.Switch) {
                    isChecked[0] = ((android.widget.Switch) view).isChecked();
                } else if (view instanceof android.widget.CompoundButton) {
                    isChecked[0] = ((android.widget.CompoundButton) view).isChecked();
                }
                uiController.loopMainThreadUntilIdle();
            }
        });

        return isChecked[0];
    }

    // Подтверждение диалога с кнопкой
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