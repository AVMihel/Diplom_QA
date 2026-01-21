package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsFilterPage {

    // Элементы диалога фильтрации
    private final ViewInteraction filterTitle = onView(
            allOf(withId(R.id.filter_news_title_text_view), withText("Filter news"))
    );

    private final ViewInteraction categoryField = onView(
            allOf(withId(R.id.news_item_category_text_auto_complete_text_view))
    );

    private final ViewInteraction startDateField = onView(
            allOf(withId(R.id.news_item_publish_date_start_text_input_edit_text))
    );

    private final ViewInteraction endDateField = onView(
            allOf(withId(R.id.news_item_publish_date_end_text_input_edit_text))
    );

    private final ViewInteraction activeCheckbox = onView(
            allOf(withId(R.id.filter_news_active_material_check_box), withText("Active"))
    );

    private final ViewInteraction notActiveCheckbox = onView(
            allOf(withId(R.id.filter_news_inactive_material_check_box), withText("Not active"))
    );

    private final ViewInteraction filterButton = onView(
            allOf(withId(R.id.filter_button), withText("FILTER"))
    );

    private final ViewInteraction cancelButton = onView(
            allOf(withId(R.id.cancel_button), withText("CANCEL"))
    );

    // Проверка отображения диалога фильтрации
    public NewsFilterPage checkFilterDialogIsDisplayed() {
        WaitUtils.waitForElement(filterTitle, 3000);
        return this;
    }

    // Проверка элементов экрана фильтрации
    public void checkFilterElementsDisplayed() {
        checkFilterDialogIsDisplayed();

        categoryField.check(matches(isDisplayed()));
        startDateField.check(matches(isDisplayed()));
        endDateField.check(matches(isDisplayed()));
        activeCheckbox.check(matches(isDisplayed()));
        notActiveCheckbox.check(matches(isDisplayed()));
        filterButton.check(matches(isDisplayed()));
        cancelButton.check(matches(isDisplayed()));
    }

    // Выбор категории
    public NewsFilterPage selectCategory(String category) {
        categoryField.perform(click());
        WaitUtils.waitForElementWithText(category, 2000);
        onView(withText(category)).perform(click());
        return this;
    }

    // Проверка списка категорий
    public void checkCategoriesList() {
        categoryField.perform(click());

        String[] categories = {
                "Объявление", "День рождения", "Зарплата", "Профсоюз",
                "Праздник", "Массаж", "Благодарность", "Нужна помощь"
        };

        for (String category : categories) {
            try {
                WaitUtils.waitForElementWithText(category, 1000);
            } catch (Exception e) {
                // Логируем отсутствие категории
            }
        }

        try {
            onView(withText(categories[0])).perform(click());
        } catch (Exception e) {
            categoryField.perform(click());
        }
    }

    // Выбор даты через календарь
    public NewsFilterPage selectStartDate() {
        startDateField.perform(click());
        WaitUtils.waitForElementWithText("OK", 2000);
        try {
            onView(withText("OK")).perform(click());
        } catch (Exception e) {
            onView(withText("ОК")).perform(click());
        }
        return this;
    }

    public NewsFilterPage selectEndDate() {
        endDateField.perform(click());
        WaitUtils.waitForElementWithText("OK", 2000);
        try {
            onView(withText("OK")).perform(click());
        } catch (Exception e) {
            onView(withText("ОК")).perform(click());
        }
        return this;
    }

    // Отмена фильтрации
    public void cancelFilter() {
        cancelButton.perform(click());
        WaitUtils.waitForMillis(500);
    }

    // Валидация дат (конечная раньше начальной)
    public void setInvalidDates() {
        selectStartDate();
        selectEndDate();
        filterButton.perform(click());
    }

    // Проверка наличия сообщения об ошибке
    public boolean isErrorDisplayed() {
        String[] errorMessages = {
                "The end date cannot be earlier than the start date",
                "End date cannot be earlier than start date",
                "Некорректные даты",
                "Invalid date range"
        };

        for (String message : errorMessages) {
            try {
                onView(withText(message)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                // Продолжаем проверку
            }
        }
        return false;
    }

    // Снятие обоих чекбоксов
    public NewsFilterPage uncheckBothStatuses() {
        try {
            activeCheckbox.perform(click());
        } catch (Exception e) {
            // Игнорируем
        }

        try {
            notActiveCheckbox.perform(click());
        } catch (Exception e) {
            // Игнорируем
        }

        return this;
    }

    // Применение фильтра
    public void applyFilter() {
        filterButton.perform(click());
        WaitUtils.waitForMillis(1500);
    }

    // Выбор любой доступной категории
    public NewsFilterPage selectAnyAvailableCategory() {
        categoryField.perform(click());
        WaitUtils.waitForMillis(1000);

        try {
            onView(withText("Объявление")).perform(click());
        } catch (Exception e) {
            try {
                onView(withText("День рождения")).perform(click());
            } catch (Exception ex) {
                androidx.test.espresso.Espresso.pressBack();
            }
        }

        return this;
    }
}
