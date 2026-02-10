package ru.iteco.fmhandroid.ui.core;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Rule;

import io.qameta.allure.kotlin.Step;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

@LargeTest
@DisplayName("Базовый класс для тестов")
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

    @Step("Определение текущего экрана приложения")
    private ScreenState detectCurrentScreen() {
        if (authPage.isAuthorizationScreenDisplayed()) {
            return ScreenState.AUTH_SCREEN;
        }

        if (mainPage.isMainScreenDisplayed()) {
            return ScreenState.MAIN_SCREEN;
        }

        return ScreenState.UNKNOWN_SCREEN;
    }

    @Step("Безопасный выход из системы")
    protected void performSafeLogout() {
        try {
            mainPage.tryToLogout();
            WaitUtils.waitForMillis(MEDIUM_DELAY);
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
        }
    }

    @Step("Ожидание появления экрана авторизации")
    protected void waitForAuthScreen(int timeoutMillis) {
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            if (authPage.isAuthorizationScreenDisplayed()) {
                return;
            }
            WaitUtils.waitForMillis(POLLING_DELAY);
        }
    }

    @Step("Проверка что мы на экране авторизации")
    protected void ensureOnAuthScreen() {
        if (!authPage.isAuthorizationScreenDisplayed()) {
            throw new IllegalStateException("Not on authorization screen");
        }
    }

    @Step("Настройка тестового окружения - вернуться на экран авторизации")
    protected void setUpToAuthScreen() {
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

    @Step("Завершение теста - вернуться на экран авторизации")
    protected void tearDownToAuthScreen() {
        try {
            if (mainPage.isMainScreenDisplayed()) {
                mainPage.tryToLogout();
                WaitUtils.waitForMillis(MEDIUM_DELAY);
            }
        } catch (Exception e) {
            System.err.println("Error during tearDown logout: " + e.getMessage());
        }
    }

    @Step("Авторизация и переход на главный экран")
    protected void loginAndGoToMainScreen() {
        authPage.checkAuthorizationScreenIsDisplayed();
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);

        WaitUtils.waitForElementWithId(R.id.all_news_text_view, LONG_DELAY);

        if (!mainPage.isMainScreenDisplayed()) {
            throw new IllegalStateException("Failed to navigate to main screen after authorization");
        }
    }

    @Step("Проверка что мы на главном экране")
    protected void ensureOnMainScreen() {
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