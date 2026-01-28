package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Before;
import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;
import ru.iteco.fmhandroid.ui.pages.QuotesPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class QuotesAboutLogoutTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final QuotesPage quotesPage = new QuotesPage();

    @Before
    public void setUp() {
        ensureOnMainScreen();
    }

    // TC-QUOTES-01: Полная проверка работы с цитатами
    @Test
    public void testQuotesFunctionality() {
        mainPage.clickQuotesButton();
        quotesPage.checkQuotesScreenIsDisplayed();
        quotesPage.checkQuotesListIsDisplayed();
        quotesPage.expandQuoteAtPosition(0);
        quotesPage.checkQuoteDescriptionIsDisplayed();
        quotesPage.expandQuoteAtPosition(0);
        quotesPage.checkQuoteDescriptionIsHidden();
        quotesPage.goBackToMainScreen();
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-ABOUT-01: Проверка раздела "О приложении"
    @Test
    public void testAboutSection() {
        navigationDrawer.openMenu();
        navigationDrawer.clickAboutMenuItem();
        mainPage.checkAboutScreenIsDisplayed();
        onView(withId(R.id.about_version_title_text_view))
                .check(matches(withText("Version:")));
        onView(withId(R.id.about_version_value_text_view))
                .check(matches(isDisplayed()));
        onView(withId(R.id.about_back_image_button)).perform(click());
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-LOGOUT-01: Выход из системы
    @Test
    public void testLogout() {
        mainPage.logout();
        authPage.checkAuthorizationScreenIsDisplayed();
    }

    @Test
    public void testClearFieldsAfterReturnFromMainScreen() {
        mainPage.logout();
        WaitUtils.waitForMillis(1500);
        authPage.checkAllFieldsAreEmpty();
    }
}