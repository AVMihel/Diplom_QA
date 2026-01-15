package ru.iteco.fmhandroid.ui.core;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import ru.iteco.fmhandroid.ui.AppActivity;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;
import ru.iteco.fmhandroid.ui.pages.MainPage;

// Базовый класс для всех тестов, содержащий общую настройку и утилитные методы
@LargeTest
public abstract class BaseTest {

    // Правило для запуска активности перед каждым тестом
    @Rule
    public ActivityScenarioRule<AppActivity> activityRule =
            new ActivityScenarioRule<>(AppActivity.class);

    protected AuthorizationPage authPage = new AuthorizationPage();
    protected MainPage mainPage = new MainPage();

    // Метод выполняется перед каждым тестом: регистрирует ресурсы и ждет запуск приложения
    @Before
    public void setUp() {
    }

    // Метод выполняется после каждого теста: очищает зарегистрированные ресурсы
    @After
    public void tearDown() {
    }

    // Выполняет авторизацию с валидными данными и проверяет переход на главный экран
    protected void performLoginAndGoToMainScreen() {
        try {
            // Всегда начинаем с экрана авторизации
            authPage.checkAuthorizationScreenIsDisplayed();

            // Авторизуемся
            authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);

            // Проверяем успешный вход
            mainPage.checkMainScreenIsDisplayed();

        } catch (Exception e) {
            throw new RuntimeException("Failed to perform login", e);
        }
    }
}