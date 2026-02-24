package ru.iteco.fmhandroid.ui.core;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.statement.UiThreadStatement;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

@LargeTest
public abstract class BaseTest {

    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            activityRule.getScenario().close();
            try {
                UiThreadStatement.runOnUiThread(() -> {});
            } catch (Throwable ex) {}
        }
    };

    protected AuthorizationPage authPage = new AuthorizationPage();
    protected MainPage mainPage = new MainPage();

    protected void ensureOnAuthScreen() {
        Allure.step("Обеспечение нахождения на экране авторизации");
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if (authPage.isAuthorizationScreenDisplayed()) {
                    return;
                }

                if (mainPage.isMainScreenDisplayed()) {
                    Allure.step("Обнаружен главный экран, выполняем выход");
                    performLogout();
                    return;
                }

                if (attempt < maxAttempts) {
                    Allure.step("Неизвестное состояние, перезапускаем активность");
                    activityRule.getScenario().recreate();
                }
            } catch (Exception e) {
                if (attempt < maxAttempts) {
                }
            }
        }
        throw new IllegalStateException("Failed to reach authorization screen after " + maxAttempts + " attempts");
    }

    protected void ensureOnMainScreen() {
        Allure.step("Обеспечение нахождения на главном экране");
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if (mainPage.isMainScreenDisplayed()) {
                    return;
                }

                if (authPage.isAuthorizationScreenDisplayed()) {
                    Allure.step("Обнаружен экран авторизации, выполняем вход");
                    authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
                    waitForMainScreenDisplayed();
                    return;
                }

                if (attempt < maxAttempts) {
                    Allure.step("Неизвестное состояние, перезапускаем активность");
                    activityRule.getScenario().recreate();
                }
            } catch (Exception e) {
                if (attempt < maxAttempts) {
                }
            }
        }
        throw new IllegalStateException("Failed to reach main screen after " + maxAttempts + " attempts");
    }

    protected void waitForMainScreenDisplayed() {
        Allure.step("Ожидание отображения главного экрана");
        long endTime = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < endTime) {
            try {
                if (mainPage.isMainScreenDisplayed()) {
                    return;
                }
            } catch (Exception e) {}
            WaitUtils.waitMillis(200);
        }
        throw new AssertionError("Main screen not displayed after 5 seconds");
    }

    protected void waitForAuthScreenDisplayed() {
        Allure.step("Ожидание отображения экрана авторизации");
        WaitUtils.waitForElementWithId(R.id.login_text_input_layout, 5000);
    }

    protected void performLogout() {
        Allure.step("Выполнение выхода из системы");
        try {
            mainPage.tryToLogout();
            waitForAuthScreenDisplayed();
        } catch (Exception e) {
            activityRule.getScenario().recreate();
        }
    }
}