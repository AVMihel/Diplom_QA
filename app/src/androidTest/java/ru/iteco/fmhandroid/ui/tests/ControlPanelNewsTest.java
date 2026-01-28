package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.graphics.Rect;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.pages.NewsFilterPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class ControlPanelNewsTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();

    // TC-NEWS-CP-01: Переход в "Control Panel"
    @Test
    public void testNavigateToControlPanel() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        onView(withId(R.id.add_news_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.sort_news_material_button)).check(matches(isDisplayed()));
    }

    // TC-NEWS-CP-02: Отображение элементов у новостей в "Control Panel"
    @Test
    public void testDisplayNewsElementsInControlPanel() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.checkNewsElementsExist();
    }

    // TC-NEWS-CP-03: Раскрытие/скрытие описания новости
    @Test
    public void testExpandCollapseNewsDescription() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.expandAndCollapseNewsDescription();
    }

    // TC-NEWS-CP-04: Переход к редактированию новости
    @Test
    public void testNavigateToEditNews() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
        editPage.checkEditScreenIsDisplayed();
        editPage.cancelWithConfirmation();
        controlPanelPage.checkControlPanelIsDisplayed();
    }

    // TC-NEWS-CP-06: Проверка корректности отображения "Publication date"
    @Test
    public void testPublicationDateDisplay() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        String testNewsTitle = controlPanelPage.createNewsForDateTest();
        try {
            controlPanelPage.verifyPublicationDateDisplay(testNewsTitle);
        } finally {
            controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
        }
    }

    // TC-NEWS-CP-07: Проверка корректности отображения "Creation date"
    @Test
    public void testCreationDateDisplay() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        int currentYear = LocalDate.now().getYear();
        String testNewsTitle = controlPanelPage.createNewsForDateTest(currentYear);

        try {
            controlPanelPage.verifyCreationDateYear(testNewsTitle, currentYear);
        } catch (AssertionError e) {
            throw new AssertionError("БАГ: Creation date отображается некорректно. " + e.getMessage());
        } finally {
            controlPanelPage.deleteCreatedNewsByExactTitle(testNewsTitle);
        }
    }

    // TC-NEWS-CP-08: Сортировка новостей по дате публикации
    @Test
    public void testSortNewsByPublicationDate() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();
        controlPanelPage.clickSortButton();
        controlPanelPage.checkNewsAreSorted();
        controlPanelPage.clickSortButton();
        controlPanelPage.checkNewsAreSorted();
    }

    // TC-NEWS-CP-09: Фильтрация с неактивным чекбоксом статуса
    @Test
    public void testFilterWithNoStatusChecked() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        onView(withId(R.id.filter_news_active_material_check_box)).perform(click());
        onView(withId(R.id.filter_news_inactive_material_check_box)).perform(click());
        filterPage.applyFilter();

        WaitUtils.waitForMillis(1000);

        try {
            onView(withText("There is nothing here yet..."))
                    .check(matches(isDisplayed()));
        } catch (Exception e) {
            try {
                onView(withId(R.id.news_list_recycler_view))
                        .check(matches(isDisplayed()));
                throw new AssertionError("БАГ: Новости найдены при снятых чекбоксах!");
            } catch (Exception ex) {
            }
        }
    }

    // TC-NEWS-CP-10: Фильтрация только активных новостей
    @Test
    public void testFilterOnlyActiveNews() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        onView(withId(R.id.filter_news_inactive_material_check_box)).perform(click());
        filterPage.applyFilter();

        WaitUtils.waitForMillis(1500);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        List<UiObject2> newsCards = device.findObjects(
                By.res("ru.iteco.fmhandroid:id/news_item_material_card_view"));

        if (newsCards.isEmpty()) {
            return;
        }

        boolean foundNotActive = false;
        for (UiObject2 card : newsCards) {
            try {
                if (card.isEnabled()) {
                    Rect bounds = card.getVisibleBounds();
                    if (bounds.width() > 10 && bounds.height() > 10) {
                        UiObject2 notActiveText = card.findObject(By.text("NOT ACTIVE"));
                        if (notActiveText != null && notActiveText.isEnabled()) {
                            foundNotActive = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        if (foundNotActive) {
            throw new AssertionError("БАГ: Найдены неактивные новости при фильтре только активных!");
        }
    }

    // TC-NEWS-CP-11: Фильтрация только неактивных новостей
    @Test
    public void testFilterOnlyInactiveNews() {
        ensureOnMainScreen();
        controlPanelPage.navigateToControlPanel();

        NewsFilterPage filterPage = controlPanelPage.openFilterDialog();
        onView(withId(R.id.filter_news_active_material_check_box)).perform(click());
        filterPage.applyFilter();

        WaitUtils.waitForMillis(1500);

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        List<UiObject2> newsCards = device.findObjects(
                By.res("ru.iteco.fmhandroid:id/news_item_material_card_view"));

        if (newsCards.isEmpty()) {
            return;
        }

        boolean foundActive = false;
        for (UiObject2 card : newsCards) {
            try {
                if (card.isEnabled()) {
                    Rect bounds = card.getVisibleBounds();
                    if (bounds.width() > 10 && bounds.height() > 10) {
                        UiObject2 activeText = card.findObject(By.text("ACTIVE"));
                        if (activeText != null && activeText.isEnabled()) {
                            foundActive = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        if (foundActive) {
            throw new AssertionError("БАГ: Найдены активные новости при фильтре только неактивных!");
        }
    }
}