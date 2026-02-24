package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class NewsPage {

    private static final int DEFAULT_TIMEOUT = 5000;
    private static final int SHORT_TIMEOUT = 3000;


    // ID элементов на главном экране
    private static final int ALL_NEWS_BUTTON_ID = R.id.all_news_text_view;
    private static final int NEWS_BLOCK_CONTROL_BUTTON_ID = R.id.expand_material_button;
    private static final int NEWS_BLOCK_ID = R.id.container_list_news_include_on_fragment_main;

    // ID элементов на экране новостей
    private static final int SORT_BUTTON_ID = R.id.sort_news_material_button;
    private static final int FILTER_BUTTON_ID = R.id.filter_news_material_button;
    private static final int CONTROL_PANEL_BUTTON_ID = R.id.edit_news_material_button;
    private static final int QUOTES_BUTTON_ID = R.id.our_mission_image_button;


    public boolean isNewsBlockDisplayed() {
        Allure.step("Проверить видимость блока новостей");
        return WaitUtils.isElementDisplayedWithId(NEWS_BLOCK_ID, SHORT_TIMEOUT);
    }

    public boolean isAllNewsButtonDisplayed() {
        Allure.step("Проверить видимость кнопки 'ALL NEWS'");
        return WaitUtils.isElementDisplayedWithId(ALL_NEWS_BUTTON_ID, SHORT_TIMEOUT);
    }

    public boolean isAllNewsButtonHidden() {
        Allure.step("Проверить, что кнопка 'ALL NEWS' скрыта");
        try {
            onView(withId(ALL_NEWS_BUTTON_ID)).check(matches(not(isDisplayed())));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSortButtonDisplayed() {
        Allure.step("Проверить отображение кнопки сортировки");
        return WaitUtils.isElementDisplayedWithId(SORT_BUTTON_ID, SHORT_TIMEOUT);
    }

    public boolean areNewsControlsDisplayed() {
        Allure.step("Проверить отображение всех элементов управления новостями");
        try {
            onView(withId(SORT_BUTTON_ID)).check(matches(isDisplayed()));
            onView(withId(FILTER_BUTTON_ID)).check(matches(isDisplayed()));
            onView(withId(CONTROL_PANEL_BUTTON_ID)).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public NewsPage checkAllNewsButtonIsDisplayed() {
        Allure.step("Проверить отображение кнопки 'ALL NEWS'");
        WaitUtils.waitForElementWithId(ALL_NEWS_BUTTON_ID, DEFAULT_TIMEOUT);
        return this;
    }

    public NewsPage clickExpandButton() {
        Allure.step("Нажать кнопку сворачивания/разворачивания блока новостей");
        WaitUtils.waitForElementWithId(NEWS_BLOCK_CONTROL_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(NEWS_BLOCK_CONTROL_BUTTON_ID)).perform(click());
        return this;
    }

    public NewsPage clickAllNewsButton() {
        Allure.step("Нажать кнопку 'ALL NEWS' для перехода к полному списку");
        WaitUtils.waitForElementWithId(ALL_NEWS_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(ALL_NEWS_BUTTON_ID)).perform(click());
        waitForNewsScreen();
        return this;
    }

    public NewsPage waitForNewsScreen() {
        Allure.step("Ожидание загрузки экрана новостей");
        WaitUtils.waitForElementWithId(SORT_BUTTON_ID, DEFAULT_TIMEOUT);
        return this;
    }

    public NewsPage openNewsFilter() {
        Allure.step("Открыть экран фильтрации новостей");
        WaitUtils.waitForElementWithId(FILTER_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(FILTER_BUTTON_ID)).perform(click());
        return this;
    }

    public NewsPage goToControlPanel() {
        Allure.step("Перейти в панель управления (Control Panel)");
        WaitUtils.waitForElementWithId(CONTROL_PANEL_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(CONTROL_PANEL_BUTTON_ID)).perform(click());
        return this;
    }

    public NewsPage clickQuotesButton() {
        Allure.step("Нажать кнопку перехода к цитатам");
        WaitUtils.waitForElementWithId(QUOTES_BUTTON_ID, DEFAULT_TIMEOUT);
        onView(withId(QUOTES_BUTTON_ID)).perform(click());
        return this;
    }
}