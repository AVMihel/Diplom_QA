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

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class CreateEditNewsPage {

    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;

    // Проверяет экран редактирования
    public void checkEditScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.news_item_title_text_input_edit_text, LONG_DELAY);
        checkTitleFieldDisplayed();
    }

    // Проверяет экран создания новости
    public void checkCreateScreenIsDisplayed() {
        boolean categoryFound = WaitUtils.isElementDisplayedWithId(R.id.news_item_category_text_auto_complete_text_view, 3000);
        if (categoryFound) {
            checkCategoryFieldDisplayed();
        } else {
            WaitUtils.waitForElementWithId(R.id.news_item_title_text_input_edit_text, LONG_DELAY);
            checkTitleFieldDisplayed();
        }
    }

    // Заполняет заголовок
    public CreateEditNewsPage fillTitle(String title) {
        ViewInteraction titleField = onView(withId(R.id.news_item_title_text_input_edit_text));
        WaitUtils.waitForElement(titleField, LONG_DELAY);
        titleField.perform(replaceText(title), closeSoftKeyboard());
        return this;
    }

    // Упрощенный метод выбора категории
    public CreateEditNewsPage selectCategorySimple(String category) {
        try {
            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(category), closeSoftKeyboard());
        } catch (Exception e) {
            try {
                onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
                WaitUtils.waitForMillis(LONG_DELAY);
                onView(withText(category)).perform(click());
            } catch (Exception e2) {
                // Игнорируем ошибку выбора категории
            }
        }
        return this;
    }

    // Заполняет описание
    public CreateEditNewsPage fillDescription(String description) {
        try {
            ViewInteraction descriptionField = onView(withId(R.id.news_item_description_text_input_edit_text));
            WaitUtils.waitForElement(descriptionField, LONG_DELAY);
            descriptionField.perform(replaceText(description), closeSoftKeyboard());
        } catch (Exception e) {
            // Игнорируем ошибку заполнения описания
        }
        return this;
    }

    // Нажимает кнопку SAVE
    public void clickSaveButton() {
        ViewInteraction saveButton = onView(withId(R.id.save_button));
        WaitUtils.waitForElement(saveButton, LONG_DELAY);
        saveButton.perform(click());
    }

    // Отмена с подтверждением
    public void cancelWithConfirmation() {
        try {
            onView(withId(R.id.cancel_button)).perform(scrollTo(), click());
            confirmDialog("OK");
        } catch (Exception e) {
            onView(withId(R.id.cancel_button)).perform(click());
        }
    }

    // Проверяет наличие сообщения об ошибке
    public boolean isErrorMessageDisplayed(String message) {
        return WaitUtils.isElementDisplayedWithText(message, LONG_DELAY);
    }

    // Проверяет, что поле заголовка отображается
    public void checkTitleFieldDisplayed() {
        onView(withId(R.id.news_item_title_text_input_edit_text)).check(matches(isDisplayed()));
    }

    // Проверяет что поле категории отображается
    public void checkCategoryFieldDisplayed() {
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).check(matches(isDisplayed()));
    }

    // Выбирает текущую дату
    public CreateEditNewsPage selectCurrentDate() {
        return selectDateTimeField(R.id.news_item_publish_date_text_input_edit_text);
    }

    // Выбирает текущее время
    public CreateEditNewsPage selectCurrentTime() {
        return selectDateTimeField(R.id.news_item_publish_time_text_input_edit_text);
    }

    // Метод для проверки наличия ошибки валидации
    public boolean isValidationErrorDisplayed() {
        return isErrorMessageDisplayed("Saving failed") ||
                isErrorMessageDisplayed("Try again later");
    }

    // Метод для проверки, что остались на экране создания/редактирования
    public boolean isStillOnEditScreen() {
        return WaitUtils.isElementDisplayedWithId(
                R.id.news_item_title_text_input_edit_text, MEDIUM_DELAY);
    }

    // Выбор даты или времени через календарь/таймпикер для указанного поля
    private CreateEditNewsPage selectDateTimeField(int fieldId) {
        try {
            onView(withId(fieldId)).perform(click());
            confirmDialog("OK");
        } catch (Exception e) {
            // Игнорируем ошибку выбора даты/времени
        }
        return this;
    }

    // Подтверждение диалога (календаря/таймпикера) с ожиданием кнопки
    private void confirmDialog(String buttonText) {
        WaitUtils.waitForElementWithText(buttonText, LONG_DELAY);
        onView(withText(buttonText)).inRoot(isDialog()).perform(click());
    }
}