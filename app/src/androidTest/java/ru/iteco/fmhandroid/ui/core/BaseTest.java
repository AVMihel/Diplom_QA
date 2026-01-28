package ru.iteco.fmhandroid.ui.core;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

@LargeTest
public abstract class BaseTest {

    private static final int SHORT_DELAY = 200;
    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;
    private static final int EXTRA_LONG_DELAY = 3000;
    private static final int POLLING_DELAY = 200;

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    protected AuthorizationPage authPage = new AuthorizationPage();
    protected MainPage mainPage = new MainPage();

    @Before
    public void setUp() {
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
                if (!authPage.isAuthScreenDisplayedQuick(LONG_DELAY)) {
                    // Не удалось определить состояние экрана
                }
                break;
        }

        ensureOnAuthScreen();
    }

    @After
    public void tearDown() {
        try {
            if (mainPage.isMainScreenDisplayedQuick(LONG_DELAY)) {
                mainPage.tryToLogout();
                WaitUtils.waitForMillis(MEDIUM_DELAY);
            }

        } catch (Exception e) {
            // Игнорируем ошибки очистки, чтобы не влиять на результаты тестов
        }
    }

    private enum ScreenState {
        AUTH_SCREEN, MAIN_SCREEN, UNKNOWN_SCREEN
    }

    // Определение текущего экрана приложения
    private ScreenState detectCurrentScreen() {
        if (authPage.isAuthScreenDisplayedQuick(MEDIUM_DELAY)) {
            return ScreenState.AUTH_SCREEN;
        }

        if (mainPage.isMainScreenDisplayedQuick(MEDIUM_DELAY)) {
            return ScreenState.MAIN_SCREEN;
        }

        return ScreenState.UNKNOWN_SCREEN;
    }

    // Безопасный выход из системы
    private void performSafeLogout() {
        try {
            mainPage.tryToLogout();
            WaitUtils.waitForMillis(MEDIUM_DELAY);
        } catch (Exception e) {
            // Игнорируем ошибки безопасного выхода
        }
    }

    // Ожидание появления экрана авторизации
    private void waitForAuthScreen(int timeoutMillis) {
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() < endTime) {
            if (authPage.isAuthScreenDisplayedQuick(SHORT_DELAY)) {
                return;
            }
            WaitUtils.waitForMillis(POLLING_DELAY);
        }
    }

    // Проверка что мы на экране авторизации
    private void ensureOnAuthScreen() {
        if (!authPage.isAuthScreenDisplayedQuick(EXTRA_LONG_DELAY)) {
            // Не удалось перейти на экран авторизации
        }
    }

    // Авторизация и переход на главный экран
    protected void loginAndGoToMainScreen() {
        authPage.checkAuthorizationScreenIsDisplayed();
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainScreenIsDisplayed();
    }

    // Проверка что мы на главном экране
    protected void ensureOnMainScreen() {
        if (!mainPage.isMainScreenDisplayedQuick(LONG_DELAY)) {
            if (authPage.isAuthScreenDisplayedQuick(LONG_DELAY)) {
                loginAndGoToMainScreen();
            } else {
                WaitUtils.waitForMillis(LONG_DELAY);
                if (authPage.isAuthScreenDisplayedQuick(LONG_DELAY)) {
                    loginAndGoToMainScreen();
                } else {
                    throw new IllegalStateException("Не удалось определить состояние приложения");
                }
            }
        }
    }
}