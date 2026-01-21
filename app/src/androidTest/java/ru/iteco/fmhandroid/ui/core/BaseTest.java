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

    // Правило для запуска тестовой активности
    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    // Объекты страниц для использования в тестах
    protected AuthorizationPage authPage = new AuthorizationPage();
    protected MainPage mainPage = new MainPage();

    // Метод настройки перед каждым тестом
    @Before
    public void setUp() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверка отображения экрана авторизации
        if (authPage.isAuthScreenDisplayedQuick(2000)) {
            return;
        }

        // Если на главном экране, выполняем выход
        if (mainPage.isMainScreenDisplayedQuick(2000)) {
            mainPage.tryToLogout();
        }

        // Ожидание экрана авторизации
        authPage.checkAuthorizationScreenIsDisplayed();
    }

    // Метод очистки после каждого теста
    @After
    public void tearDown() {
        try {
            // Попытка выхода, если на главном экране
            if (mainPage.isMainScreenDisplayedQuick(1500)) {
                mainPage.tryToLogout();
            }
        } catch (Exception e) {
            // Игнорируем ошибки при очистке
        }
    }

    // Вход в приложение и переход на главный экран
    protected void loginAndGoToMainScreen() {
        authPage.checkAuthorizationScreenIsDisplayed();
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        mainPage.checkMainScreenIsDisplayed();
    }

    // Гарантированное нахождение на главном экране
    protected void ensureOnMainScreen() {
        if (!mainPage.isMainScreenDisplayedQuick(2000)) {
            loginAndGoToMainScreen();
        }
    }
}