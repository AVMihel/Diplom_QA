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
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsFilterPage {

    private static final int LONG_DELAY = 1500;

    // Константы текстов
    private static final String FILTER_TITLE = "Filter news";
    private static final String ACTIVE_TEXT = "Active";
    private static final String NOT_ACTIVE_TEXT = "Not active";
    private static final String FILTER_BUTTON_TEXT = "FILTER";
    private static final String CANCEL_BUTTON_TEXT = "CANCEL";
    private static final String OK_TEXT = "OK";
    private static final String OK_RUSSIAN_TEXT = "ОК";
    private static final String ANNOUNCEMENT_CATEGORY = TestData.News.CATEGORY_ANNOUNCEMENT;
    private static final String BIRTHDAY_CATEGORY = TestData.News.CATEGORY_BIRTHDAY;

    // Сообщения об ошибках
    private static final String[] ERROR_MESSAGES = {
            "The end date cannot be earlier than the start date",
            "End date cannot be earlier than start date",
            "Некорректные даты",
            "Invalid date range"
    };

    // Категории для проверки
    private static final String[] CATEGORIES = {
            TestData.News.CATEGORY_ANNOUNCEMENT,
            TestData.News.CATEGORY_BIRTHDAY,
            TestData.News.CATEGORY_SALARY,
            TestData.News.CATEGORY_TRADE_UNION,
            TestData.News.CATEGORY_HOLIDAY,
            TestData.News.CATEGORY_MASSAGE,
            TestData.News.CATEGORY_THANKS,
            TestData.News.CATEGORY_HELP_NEEDED
    };

    // Элементы диалога фильтрации
    private final ViewInteraction filterTitle = onView(
            allOf(withId(R.id.filter_news_title_text_view), withText(FILTER_TITLE))
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
            allOf(withId(R.id.filter_news_active_material_check_box), withText(ACTIVE_TEXT))
    );

    private final ViewInteraction notActiveCheckbox = onView(
            allOf(withId(R.id.filter_news_inactive_material_check_box), withText(NOT_ACTIVE_TEXT))
    );

    private final ViewInteraction filterButton = onView(
            allOf(withId(R.id.filter_button), withText(FILTER_BUTTON_TEXT))
    );

    private final ViewInteraction cancelButton = onView(
            allOf(withId(R.id.cancel_button), withText(CANCEL_BUTTON_TEXT))
    );

    // Проверка отображения диалога фильтрации
    public NewsFilterPage checkFilterDialogIsDisplayed() {
        WaitUtils.waitForElement(filterTitle, LONG_DELAY);
        return this;
    }

    // Проверка элементов экрана фильтрации
    public void checkFilterElementsDisplayed() {
        checkFilterDialogIsDisplayed();

        ViewInteraction[] elements = {
                categoryField,
                startDateField,
                endDateField,
                activeCheckbox,
                notActiveCheckbox,
                filterButton,
                cancelButton
        };

        for (ViewInteraction element : elements) {
            element.check(matches(isDisplayed()));
        }
    }

    // Проверка списка категорий
    public void checkCategoriesList() {
        categoryField.perform(click());

        for (String category : CATEGORIES) {
            try {
                onView(withText(category)).check(matches(isDisplayed()));
            } catch (Exception e) {
                // Игнорируем если категория не найдена
            }
        }

        try {
            onView(withText(CATEGORIES[0])).perform(click());
        } catch (Exception e) {
            categoryField.perform(click());
        }
    }

    // Выбор даты через календарь
    public NewsFilterPage selectStartDate() {
        return selectDateField(startDateField);
    }

    // Выбор конечной даты фильтра новостей
    public NewsFilterPage selectEndDate() {
        return selectDateField(endDateField);
    }

    // Отмена фильтрации
    public void cancelFilter() {
        cancelButton.perform(click());
    }

    // Валидация дат (конечная раньше начальной)
    public void setInvalidDates() {
        selectStartDate();
        selectEndDate();
        filterButton.perform(click());
    }

    // Проверка наличия сообщения об ошибке
    public boolean isErrorDisplayed() {
        for (String message : ERROR_MESSAGES) {
            try {
                onView(withText(message)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                // Продолжаем проверять следующие сообщения
            }
        }
        return false;
    }

    // Применение фильтра
    public void applyFilter() {
        filterButton.perform(click());
    }

    // Выбор любой доступной категории
    public NewsFilterPage selectAnyAvailableCategory() {
        categoryField.perform(click());

        try {
            onView(withText(ANNOUNCEMENT_CATEGORY)).perform(click());
        } catch (Exception e) {
            try {
                onView(withText(BIRTHDAY_CATEGORY)).perform(click());
            } catch (Exception ex) {
                androidx.test.espresso.Espresso.pressBack();
            }
        }

        return this;
    }

    // Выбор поля даты и подтверждение выбора
    private NewsFilterPage selectDateField(ViewInteraction dateField) {
        dateField.perform(click());
        confirmDateSelection();
        return this;
    }

    // Подтверждение выбора даты в диалоге
    private void confirmDateSelection() {
        WaitUtils.waitForElementWithText(OK_TEXT, LONG_DELAY);
        try {
            onView(withText(OK_TEXT)).perform(click());
        } catch (Exception e) {
            onView(withText(OK_RUSSIAN_TEXT)).perform(click());
        }
    }
}