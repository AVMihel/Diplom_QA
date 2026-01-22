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

import androidx.test.espresso.ViewInteraction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class CreateEditNewsPage {

    // Проверяет экран редактирования
    public void checkEditScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.news_item_title_text_input_edit_text, 5000);
        onView(withId(R.id.news_item_title_text_input_edit_text)).check(matches(isDisplayed()));
    }

    // Проверяет экран создания новости
    public void checkCreateScreenIsDisplayed() {
        boolean categoryFound = WaitUtils.isElementDisplayedWithId(R.id.news_item_category_text_auto_complete_text_view, 3000);
        if (categoryFound) {
            onView(withId(R.id.news_item_category_text_auto_complete_text_view)).check(matches(isDisplayed()));
        } else {
            WaitUtils.waitForElementWithId(R.id.news_item_title_text_input_edit_text, 3000);
            onView(withId(R.id.news_item_title_text_input_edit_text)).check(matches(isDisplayed()));
        }
    }

    // Заполняет заголовок
    public CreateEditNewsPage fillTitle(String title) {
        ViewInteraction titleField = onView(withId(R.id.news_item_title_text_input_edit_text));
        WaitUtils.waitForElement(titleField, 2000);
        titleField.perform(replaceText(title), closeSoftKeyboard());
        return this;
    }

    // Выбирает категорию из выпадающего списка
    public CreateEditNewsPage selectCategory(String category) {
        try {
            ViewInteraction categoryField = onView(withId(R.id.news_item_category_text_auto_complete_text_view));
            WaitUtils.waitForElement(categoryField, 2000);

            categoryField.perform(click());
            WaitUtils.waitForMillis(1000);

            boolean categorySelected = false;

            try {
                WaitUtils.waitForElementWithText(category, 3000);
                onView(withText(category)).perform(click());
                categorySelected = true;
            } catch (Exception e1) {
                try {
                    categoryField.perform(replaceText(category), closeSoftKeyboard());
                    categorySelected = true;
                } catch (Exception e2) {
                    String alternativeCategory = findAlternativeCategoryName(category);
                    if (alternativeCategory != null) {
                        try {
                            WaitUtils.waitForElementWithText(alternativeCategory, 2000);
                            onView(withText(alternativeCategory)).perform(click());
                            categorySelected = true;
                        } catch (Exception e3) {
                        }
                    }
                }
            }

            if (!categorySelected) {
                try {
                    categoryField.perform(click());
                    WaitUtils.waitForMillis(1000);

                    String[] commonCategories = {"Объявление", "Announcement", "Announce", "Объявл", "Ad"};
                    for (String cat : commonCategories) {
                        try {
                            onView(withText(cat)).perform(click());
                            categorySelected = true;
                            break;
                        } catch (Exception e) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
        }
        return this;
    }

    // Вспомогательный метод для поиска альтернативных названий категорий
    private String findAlternativeCategoryName(String category) {
        switch (category) {
            case "Объявление":
                return "Announcement";
            case "День рождения":
                return "Birthday";
            case "Праздник":
                return "Holiday";
            case "Зарплата":
                return "Salary";
            case "Профсоюз":
                return "Trade Union";
            case "Массаж":
                return "Massage";
            case "Благодарность":
                return "Thanks";
            case "Нужна помощь":
                return "Help Needed";
            default:
                return null;
        }
    }

    // Упрощенный метод выбора категории
    public CreateEditNewsPage selectCategorySimple(String category) {
        try {
            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(category), closeSoftKeyboard());
        } catch (Exception e) {
            try {
                onView(withId(R.id.news_item_category_text_auto_complete_text_view)).perform(click());
                WaitUtils.waitForMillis(1500);
                onView(withText(category)).perform(click());
            } catch (Exception e2) {
            }
        }
        return this;
    }

    // Выбирает дату через календарь
    public CreateEditNewsPage selectDateFromPicker() {
        try {
            ViewInteraction dateField = onView(withId(R.id.news_item_publish_date_text_input_edit_text));
            WaitUtils.waitForElement(dateField, 2000);
            dateField.perform(click());

            WaitUtils.waitForElementWithText("OK", 2000);
            onView(withText("OK")).perform(click());

        } catch (Exception e) {
        }
        return this;
    }

    // Выбирает время через пикер
    public CreateEditNewsPage selectTimeFromPicker() {
        try {
            ViewInteraction timeField = onView(withId(R.id.news_item_publish_time_text_input_edit_text));
            WaitUtils.waitForElement(timeField, 2000);
            timeField.perform(click());

            WaitUtils.waitForElementWithText("OK", 2000);
            onView(withText("OK")).perform(click());

        } catch (Exception e) {
        }
        return this;
    }

    // Заполняет описание
    public CreateEditNewsPage fillDescription(String description) {
        try {
            ViewInteraction descriptionField = onView(withId(R.id.news_item_description_text_input_edit_text));
            WaitUtils.waitForElement(descriptionField, 2000);
            descriptionField.perform(replaceText(description), closeSoftKeyboard());
        } catch (Exception e) {
        }
        return this;
    }

    // Переключает статус Active/Not Active
    public CreateEditNewsPage toggleActiveStatus() {
        try {
            ViewInteraction switcher = onView(withId(R.id.switcher));
            WaitUtils.waitForElement(switcher, 2000);
            switcher.perform(click());
        } catch (Exception e) {
        }
        return this;
    }

    // Нажимает кнопку SAVE
    public void clickSaveButton() {
        try {
            ViewInteraction saveButton = onView(withId(R.id.save_button));
            WaitUtils.waitForElement(saveButton, 2000);
            saveButton.perform(click());
        } catch (Exception e) {
            throw e;
        }
    }

    // Нажимает кнопку SAVE (альтернативный способ)
    public void clickSaveButtonAlt() {
        try {
            onView(withId(R.id.save_button)).perform(scrollTo(), click());
        } catch (Exception e) {
            try {
                onView(allOf(withId(R.id.save_button), withText("Save"))).perform(scrollTo(), click());
            } catch (Exception ex) {
                onView(allOf(withId(R.id.save_button), withText("SAVE"))).perform(scrollTo(), click());
            }
        }
    }

    // Отмена с подтверждением
    public void cancelWithConfirmation() {
        try {
            onView(withId(R.id.cancel_button)).perform(scrollTo(), click());

            WaitUtils.waitForElementWithText("OK", 2000);
            onView(withText("OK")).inRoot(isDialog()).perform(click());

        } catch (Exception e) {
            onView(withId(R.id.cancel_button)).perform(click());
        }
    }

    // Простая отмена (без диалога подтверждения)
    public void cancelSimple() {
        try {
            onView(withId(R.id.cancel_button)).perform(click());
        } catch (Exception e) {
        }
    }

    // Проверяет наличие сообщения об ошибке
    public boolean isErrorMessageDisplayed(String message) {
        return WaitUtils.isElementDisplayedWithText(message, 2000);
    }

    // Проверяет что поле заголовка отображается
    public void checkTitleFieldDisplayed() {
        onView(withId(R.id.news_item_title_text_input_edit_text)).check(matches(isDisplayed()));
    }

    // Проверяет что поле категории отображается
    public void checkCategoryFieldDisplayed() {
        onView(withId(R.id.news_item_category_text_auto_complete_text_view)).check(matches(isDisplayed()));
    }

    // Выбирает текущую дату
    public CreateEditNewsPage selectCurrentDate() {
        try {
            onView(withId(R.id.news_item_publish_date_text_input_edit_text)).perform(click());

            WaitUtils.waitForElementWithText("OK", 2000);
            onView(withText("OK")).perform(click());

        } catch (Exception e) {
        }
        return this;
    }

    // Выбирает текущее время
    public CreateEditNewsPage selectCurrentTime() {
        try {
            onView(withId(R.id.news_item_publish_time_text_input_edit_text)).perform(click());

            WaitUtils.waitForElementWithText("OK", 2000);
            onView(withText("OK")).perform(click());

        } catch (Exception e) {
        }
        return this;
    }

    // Метод для прямого ввода даты
    public CreateEditNewsPage fillDateManually(String date) {
        try {
            onView(withId(R.id.news_item_publish_date_text_input_edit_text))
                    .perform(replaceText(date));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fill date manually: " + e.getMessage());
        }
        return this;
    }

    // Метод для прямого ввода времени
    public CreateEditNewsPage fillTimeManually(String time) {
        try {
            onView(withId(R.id.news_item_publish_time_text_input_edit_text))
                    .perform(replaceText(time));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fill time manually: " + e.getMessage());
        }
        return this;
    }

    // НОВЫЕ ПРОСТЫЕ МЕТОДЫ (без сложной логики):

    // Метод для заполнения всех обязательных полей новости
    public CreateEditNewsPage fillRequiredNewsFields(String title, String category,
                                                     String description) {
        fillTitle(title);
        selectCategorySimple(category);
        selectCurrentDate();
        selectCurrentTime();
        fillDescription(description);
        return this;
    }

    // Метод для ввода прошедшей даты
    public CreateEditNewsPage fillPastDate() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return fillDateManually(pastDate.format(formatter));
    }

    // Метод для ввода будущей даты
    public CreateEditNewsPage fillFutureDate(int daysInFuture) {
        LocalDate futureDate = LocalDate.now().plusDays(daysInFuture);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return fillDateManually(futureDate.format(formatter));
    }

    // Метод для проверки наличия ошибки валидации
    public boolean isValidationErrorDisplayed() {
        return isErrorMessageDisplayed("Saving failed") ||
                isErrorMessageDisplayed("Try again later");
    }

    // Метод для проверки, что остались на экране создания/редактирования
    public boolean isStillOnEditScreen() {
        return WaitUtils.isElementDisplayedWithId(
                R.id.news_item_title_text_input_edit_text, 1000);
    }

    // Метод для заполнения очень длинного заголовка
    public CreateEditNewsPage fillVeryLongTitle() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 50; i++) { // ~500 символов
            longText.append("ОченьДлинныйТекст");
        }
        fillTitle(longText.toString());
        return this;
    }

    // Простой метод для проверки, что поле описания отображается
    public void checkDescriptionFieldDisplayed() {
        onView(withId(R.id.news_item_description_text_input_edit_text)).check(matches(isDisplayed()));
    }
}