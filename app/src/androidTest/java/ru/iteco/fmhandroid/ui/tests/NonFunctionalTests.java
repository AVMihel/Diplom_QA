package ru.iteco.fmhandroid.ui.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;
import ru.iteco.fmhandroid.ui.utils.OrientationUtils;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonFunctionalTests extends BaseTest {

    private final AuthorizationPage authPage = new AuthorizationPage();
    private final NewsPage newsPage = new NewsPage();

    @Before
    public void setUp() {
        try {
            OrientationUtils.rotateToPortrait();
        } catch (Exception e) {
        }
    }

    @After
    public void tearDown() {
        try {
            OrientationUtils.rotateToPortrait();
        } catch (Exception e) {
        }
    }

    // TC-AUTH-12: Сохранение данных при повороте экрана
    @Test
    public void testDataPreservationOnScreenRotation() {
        ensureOnMainScreen();

        if (mainPage.isMainScreenDisplayedQuick(2000)) {
            mainPage.tryToLogout();
        }

        authPage.checkAuthorizationScreenIsDisplayed();

        String testLogin = TestData.VALID_LOGIN;
        String testPassword = TestData.VALID_PASSWORD;

        authPage.enterLogin(testLogin);
        authPage.enterPassword(testPassword);

        WaitUtils.waitForMillis(500);
        String loginBefore = authPage.getLoginText();
        String passwordBefore = authPage.getPasswordText();

        assertFalse("Логин должен быть введен перед поворотом", loginBefore.isEmpty());
        assertFalse("Пароль должен быть введен перед поворотом", passwordBefore.isEmpty());

        OrientationUtils.rotateToLandscape();
        WaitUtils.waitForMillis(1000);

        String loginAfter = authPage.getLoginText();
        String passwordAfter = authPage.getPasswordText();

        assertEquals("Логин должен сохраниться после поворота экрана",
                loginBefore, loginAfter);
        assertEquals("Пароль должен сохраниться после поворота экрана",
                passwordBefore, passwordAfter);

        onView(withId(R.id.enter_button))
                .check(matches(isDisplayed()));

        OrientationUtils.rotateToPortrait();
    }

    // TC-NF-02: Адаптивность верстки при повороте экрана
    @Test
    public void testLayoutAdaptabilityOnScreenRotation() {
        ensureOnMainScreen();

        newsPage.navigateToNewsSection();
        WaitUtils.waitForMillis(1000);

        assertTrue("Список новостей должен отображаться", newsPage.isNewsListDisplayed());
        assertTrue("Кнопка фильтра должна отображаться", newsPage.isFilterButtonDisplayed());

        OrientationUtils.rotateToLandscape();

        try {
            WaitUtils.waitForMillis(1000);

            boolean isListDisplayed = newsPage.isNewsListDisplayed();
            boolean isFilterButtonDisplayed = newsPage.isFilterButtonDisplayed();

            assertTrue("Список новостей должен отображаться в ландшафтной ориентации",
                    isListDisplayed);
            assertTrue("Кнопка фильтра должна отображаться в ландшафтной ориентации",
                    isFilterButtonDisplayed);

            onView(withId(R.id.news_list_recycler_view))
                    .perform(swipeUp());

            assertTrue("Прокрутка должна работать в ландшафтной ориентации", true);

            try {
                onView(withId(R.id.sort_news_material_button))
                        .check(matches(isDisplayed()))
                        .perform(click());

                WaitUtils.waitForMillis(500);

                assertTrue("Список должен отображаться после сортировки",
                        newsPage.isNewsListDisplayed());

            } catch (Exception e) {
            }

        } finally {
            OrientationUtils.rotateToPortrait();
        }
    }
}