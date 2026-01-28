package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;
import ru.iteco.fmhandroid.ui.utils.OrientationUtils;

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
    @Ignore("Отключен из-за критического бага: приложение падает при повороте на экране авторизации")
    public void testDataPreservationOnScreenRotation() {
        ensureOnMainScreen();

        if (mainPage.isMainScreenDisplayedQuick(2000)) {
            mainPage.tryToLogout();
        }

        authPage.checkAuthorizationScreenIsDisplayed();

        authPage.enterLogin(TestData.VALID_LOGIN);
        authPage.enterPassword(TestData.VALID_PASSWORD);

        String loginBefore = authPage.getLoginText();
        String passwordBefore = authPage.getPasswordText();

        OrientationUtils.rotateToLandscape();

        String loginAfter = authPage.getLoginText();
        String passwordAfter = authPage.getPasswordText();

        if (loginAfter.isEmpty() || !loginBefore.equals(loginAfter)) {
            fail("БАГ: Логин не сохраняется при повороте экрана");
        }

        if (passwordAfter.isEmpty() || !passwordBefore.equals(passwordAfter)) {
            fail("БАГ: Пароль не сохраняется при повороте экрана");
        }

        onView(withId(R.id.enter_button))
                .check(matches(isDisplayed()));

        OrientationUtils.rotateToPortrait();
    }

    // TC-NF-02: Адаптивность верстки при повороте экрана
    @Test
    public void testLayoutAdaptabilityOnScreenRotation() {
        ensureOnMainScreen();

        newsPage.navigateToNewsSection();

        OrientationUtils.rotateToLandscape();

        try {
            onView(withId(R.id.news_list_recycler_view))
                    .perform(swipeUp());
        } catch (Exception e) {
            fail("БАГ: Прокрутка не работает в ландшафтной ориентации");
        }

        OrientationUtils.rotateToPortrait();
    }
}