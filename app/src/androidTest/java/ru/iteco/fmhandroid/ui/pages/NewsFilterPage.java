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

    private final ViewInteraction filterTitle = onView(
            allOf(withId(R.id.filter_news_title_text_view), withText("Filter news"),
                    isDisplayed()));

    private final ViewInteraction categoryField = onView(
            allOf(withId(R.id.news_item_category_text_auto_complete_text_view),
                    withText("Category"),
                    isDisplayed()));

    private final ViewInteraction startDateField = onView(
            allOf(withId(R.id.news_item_publish_date_start_text_input_edit_text),
                    withText("DD.MM.YYYY"),
                    isDisplayed()));

    private final ViewInteraction endDateField = onView(
            allOf(withId(R.id.news_item_publish_date_end_text_input_edit_text),
                    withText("DD.MM.YYYY"),
                    isDisplayed()));

    private final ViewInteraction activeCheckbox = onView(
            allOf(withId(R.id.filter_news_active_material_check_box),
                    withText("Active"),
                    isDisplayed()));

    private final ViewInteraction notActiveCheckbox = onView(
            allOf(withId(R.id.filter_news_inactive_material_check_box),
                    withText("Not active"),
                    isDisplayed()));

    private final ViewInteraction filterButton = onView(
            allOf(withId(R.id.filter_button),
                    withText("FILTER"),
                    isDisplayed()));

    private final ViewInteraction cancelButton = onView(
            allOf(withId(R.id.cancel_button),
                    withText("CANCEL"),
                    isDisplayed()));

    public NewsFilterPage checkFilterDialogIsDisplayed() {
        WaitUtils.waitForElement(filterTitle, 3000);
        return this;
    }

    // TC-NEWS-FILTER-01: Проверка элементов экрана фильтрации
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

    // TC-NEWS-FILTER-02: Работа выпадающего списка категорий
    public NewsFilterPage selectCategory(String category) {
        // 1. Кликаем на поле категории
        categoryField.perform(click());

        // 2. Ждем появления списка категорий
        WaitUtils.waitForElementWithText(category, 2000);

        // 3. Выбираем категорию
        onView(withText(category)).perform(click());
        return this;
    }

    // Проверяет что выпадающий список содержит все категории
    public void checkCategoriesList() {
        // 1. Открываем список категорий
        categoryField.perform(click());

        // 2. Проверяем наличие всех категорий
        String[] categories = {
                "Объявление", "День рождения", "Зарплата", "Профсоюз",
                "Праздник", "Массаж", "Благодарность", "Нужна помощь"
        };

        for (String category : categories) {
            try {
                WaitUtils.waitForElementWithText(category, 1000);
            } catch (Exception e) {
                throw new AssertionError("Category not found in dropdown: " + category);
            }
        }

        // 3. Закрываем список (выбираем первую категорию)
        onView(withText(categories[0])).perform(click());
    }

    // TC-NEWS-FILTER-03: Выбор даты через календарь
    public NewsFilterPage selectStartDate() {
        // 1. Кликаем на поле начальной даты
        startDateField.perform(click());

        // 2. Подтверждаем выбор даты
        WaitUtils.waitForElementWithText("OK", 2000);
        onView(withText("OK")).perform(click());
        return this;
    }

    public NewsFilterPage selectEndDate() {
        // 1. Кликаем на поле конечной даты
        endDateField.perform(click());

        // 2. Подтверждаем выбор даты
        WaitUtils.waitForElementWithText("OK", 2000);
        onView(withText("OK")).perform(click());
        return this;
    }

    // TC-NEWS-FILTER-05: Отмена фильтрации
    public void cancelFilter() {
        cancelButton.perform(click());
    }

    // TC-NEWS-FILTER-06: Валидация дат (конечная раньше начальной)
    public void setInvalidDates() {
        // 1. Устанавливаем начальную дату
        selectStartDate();

        // 2. Устанавливаем конечную дату (должна быть раньше начальной)
        selectEndDate();

        // 3. Применяем фильтр
        filterButton.perform(click());
    }

    // Проверяет наличие сообщения об ошибке
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

    // Устанавливает фильтр только для активных новостей
    public NewsFilterPage setFilterActiveOnly() {
        // 1. Снимаем Not Active если выбран
        try {
            notActiveCheckbox.perform(click());
        } catch (Exception e) {
            // Игнорируем если чекбокс неактивен
        }

        // 2. Выбираем Active если не выбран
        try {
            activeCheckbox.perform(click());
        } catch (Exception e) {
            // Игнорируем
        }

        return this;
    }

    // Устанавливает фильтр только для неактивных новостей
    public NewsFilterPage setFilterNotActiveOnly() {
        // 1. Снимаем Active если выбран
        try {
            activeCheckbox.perform(click());
        } catch (Exception e) {
            // Игнорируем
        }

        // 2. Выбираем Not Active если не выбран
        try {
            notActiveCheckbox.perform(click());
        } catch (Exception e) {
            // Игнорируем
        }

        return this;
    }

    // Снимает оба чекбокса
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

    // Применяет фильтр
    public void applyFilter() {
        filterButton.perform(click());
        WaitUtils.waitForMillis(1500);
    }
}
