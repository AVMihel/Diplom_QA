package ru.iteco.fmhandroid.ui.tests;

import org.junit.Test;

import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;

public class AuthorizationTest extends BaseTest {

    // TC-AUTH-01: Успешная авторизация с валидными данными
    @Test
    public void testSuccessfulAuthorizationWithValidCredentials() {
        // 1. Проверяем экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();

        // 2. Вводим валидный логин
        authPage.enterLogin(TestData.VALID_LOGIN);

        // 3. Вводим валидный пароль
        authPage.enterPassword(TestData.VALID_PASSWORD);

        // 4. Нажимаем кнопку "SIGN IN"
        authPage.clickSignInButton();

        // 5. Проверяем успешный вход и переход на главный экран
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-AUTH-02: Неуспешная авторизация с неверным логином
    @Test
    public void testFailedAuthorizationWithWrongLogin() {
        // 1. Вводим неверный логин и верный пароль
        authPage.login(TestData.INVALID_LOGIN, TestData.VALID_PASSWORD);

        // 2. Проверяем, что авторизация не удалась
        authPage.checkLoginFailed();
    }

    // TC-AUTH-03: Неуспешная авторизация с неверным паролем
    @Test
    public void testFailedAuthorizationWithWrongPassword() {
        // 1. Вводим верный логин и неверный пароль
        authPage.login(TestData.VALID_LOGIN, TestData.INVALID_PASSWORD);

        // 2. Проверяем, что авторизация не удалась
        authPage.checkLoginFailed();
    }

    // TC-AUTH-04: Валидация пустого поля "Логин"
    @Test
    public void testValidationOfEmptyLoginField() {
        // 1. Проверяем экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();

        // 2. Оставляем поле "Логин" пустым, вводим пароль
        authPage.enterPassword(TestData.VALID_PASSWORD);

        // 3. Нажимаем кнопку "SIGN IN"
        authPage.clickSignInButton();

        // 4. Проверяем ошибку валидации
        authPage.checkValidationErrorIsDisplayed();
    }

    // TC-AUTH-05: Валидация пустого поля "Пароль"
    @Test
    public void testValidationOfEmptyPasswordField() {
        // 1. Проверяем экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();

        // 2. Вводим логин, оставляем поле "Пароль" пустым
        authPage.enterLogin(TestData.VALID_LOGIN);

        // 3. Нажимаем кнопку "SIGN IN"
        authPage.clickSignInButton();

        // 4. Проверяем ошибку валидации
        authPage.checkValidationErrorIsDisplayed();
    }

    // TC-AUTH-06: Валидация логина в верхнем регистре
    @Test
    public void testLoginCaseSensitivity() {
        // 1. Вводим логин в верхнем регистре
        authPage.login(TestData.UPPERCASE_LOGIN, TestData.VALID_PASSWORD);

        // 2. Проверяем, что авторизация не удалась
        authPage.checkLoginFailed();
    }

    // TC-AUTH-08: Валидация пароля в верхнем регистре
    @Test
    public void testPasswordCaseSensitivity() {
        // 1. Вводим пароль в верхнем регистре
        authPage.login(TestData.VALID_LOGIN, TestData.UPPERCASE_PASSWORD);

        // 2. Проверяем, что авторизация не удалась
        authPage.checkLoginFailed();
    }

    // TC-AUTH-09: Валидация логина с пробелами в середине
    @Test
    public void testLoginWithSpacesInMiddle() {
        // 1. Вводим логин с пробелами в середине
        authPage.login(TestData.LOGIN_WITH_SPACES, TestData.VALID_PASSWORD);

        // 2. Проверяем, что авторизация не удалась
        authPage.checkLoginFailed();
    }

    // TC-AUTH-10: Валидация логина из одних пробелов
    @Test
    public void testLoginWithOnlySpaces() {
        // 1. Вводим логин из одних пробелов
        authPage.login(TestData.LOGIN_ONLY_SPACES, TestData.VALID_PASSWORD);

        // 2. Проверяем ошибку валидации
        authPage.checkValidationErrorIsDisplayed();
    }
}
