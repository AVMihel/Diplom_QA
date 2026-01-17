package ru.iteco.fmhandroid.ui.core;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

@LargeTest
public abstract class BaseTest {

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    protected AuthorizationPage authPage = new AuthorizationPage();
    protected MainPage mainPage = new MainPage();

    @Before
    public void setUp() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean isOnAuthScreen = authPage.isAuthScreenDisplayedQuick(2000);

        if (!isOnAuthScreen) {
            boolean isOnMainScreen = mainPage.isMainScreenDisplayedQuick(2000);
            if (isOnMainScreen) {
                mainPage.tryToLogout();
                authPage.checkAuthorizationScreenIsDisplayed();
            } else {
                authPage.checkAuthorizationScreenIsDisplayed();
            }
        }
    }

    @After
    public void tearDown() {
        try {
            if (mainPage.isMainScreenDisplayedQuick(1500)) {
                mainPage.tryToLogout();
            }
        } catch (Exception e) {
        }
    }

    // Авторизация и переход на главный экран
    protected void loginAndGoToMainScreen() {
        authPage.checkAuthorizationScreenIsDisplayed();
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainScreenIsDisplayed();
    }

    // Гарантирует, что мы на главном экране
    protected void ensureOnMainScreen() {
        try {
            if (mainPage.isMainScreenDisplayedQuick(2000)) {
                return;
            }
        } catch (Exception e) {
        }
        loginAndGoToMainScreen();
    }
}