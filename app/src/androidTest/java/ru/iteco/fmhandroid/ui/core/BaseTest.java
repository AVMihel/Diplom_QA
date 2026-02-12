package ru.iteco.fmhandroid.ui.core;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Rule;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

@LargeTest
public abstract class BaseTest {

    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 2000;
    private static final int EXTRA_LONG_DELAY = 3000;
    private static final int POLLING_DELAY = 200;

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    protected AuthorizationPage authPage = new AuthorizationPage();
    protected MainPage mainPage = new MainPage();

    private enum ScreenState {
        AUTH_SCREEN, MAIN_SCREEN, UNKNOWN_SCREEN
    }

    private ScreenState detectCurrentScreen() {
        Allure.step("Определение текущего экрана приложения");
        if (authPage.isAuthorizationScreenDisplayed()) {
            return ScreenState.AUTH_SCREEN;
        }

        if (mainPage.isMainScreenDisplayed()) {
            return ScreenState.MAIN_SCREEN;
        }

        return ScreenState.UNKNOWN_SCREEN;
    }

    protected void performSafeLogout() {
        Allure.step("Безопасный выход из системы");
        try {
            mainPage.tryToLogout();
            WaitUtils.waitForMillis(MEDIUM_DELAY);
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
        }
    }

    protected void waitForAuthScreen(int timeoutMillis) {
        Allure.step("Ожидание появления экрана авторизации");
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            if (authPage.isAuthorizationScreenDisplayed()) {
                return;
            }
            WaitUtils.waitForMillis(POLLING_DELAY);
        }
    }

    protected void ensureOnAuthScreen() {
        Allure.step("Проверка что мы на экране авторизации");
        if (!authPage.isAuthorizationScreenDisplayed()) {
            throw new IllegalStateException("Not on authorization screen");
        }
    }

    protected void setUpToAuthScreen() {
        Allure.step("Настройка тестового окружения - вернуться на экран авторизации");
        WaitUtils.waitForMillis(MEDIUM_DELAY);

        ScreenState currentState = detectCurrentScreen();

        switch (currentState) {
            case AUTH_SCREEN:
                break;
            case MAIN_SCREEN:
                performSafeLogout();
                waitForAuthScreen(EXTRA_LONG_DELAY);
                break;
            case UNKNOWN_SCREEN:
                WaitUtils.waitForMillis(LONG_DELAY);
                currentState = detectCurrentScreen();
                if (currentState == ScreenState.MAIN_SCREEN) {
                    performSafeLogout();
                    waitForAuthScreen(EXTRA_LONG_DELAY);
                } else if (currentState == ScreenState.UNKNOWN_SCREEN) {
                    throw new IllegalStateException(
                            "Cannot detect app screen state. " +
                                    "App may need manual restart or is in unexpected state."
                    );
                }
                break;
        }

        ensureOnAuthScreen();
    }

    protected void tearDownToAuthScreen() {
        Allure.step("Завершение теста - вернуться на экран авторизации");
        try {
            if (mainPage.isMainScreenDisplayed()) {
                mainPage.tryToLogout();
                WaitUtils.waitForMillis(MEDIUM_DELAY);
            }
        } catch (Exception e) {
            System.err.println("Error during tearDown logout: " + e.getMessage());
        }
    }

    protected void loginAndGoToMainScreen() {
        Allure.step("Авторизация и переход на главный экран");
        authPage.checkAuthorizationScreenIsDisplayed();
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);

        WaitUtils.waitForElementWithId(R.id.all_news_text_view, LONG_DELAY);

        if (!mainPage.isMainScreenDisplayed()) {
            throw new IllegalStateException("Failed to navigate to main screen after authorization");
        }
    }

    protected void ensureOnMainScreen() {
        Allure.step("Проверка что мы на главном экране");
        if (!mainPage.isMainScreenDisplayed()) {
            if (authPage.isAuthorizationScreenDisplayed()) {
                loginAndGoToMainScreen();
            } else {
                WaitUtils.waitForMillis(LONG_DELAY);
                if (authPage.isAuthorizationScreenDisplayed()) {
                    loginAndGoToMainScreen();
                } else {
                    setUpToAuthScreen();
                    loginAndGoToMainScreen();
                }
            }
        }
    }
}