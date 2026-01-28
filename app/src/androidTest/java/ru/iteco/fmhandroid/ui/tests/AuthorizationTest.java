package ru.iteco.fmhandroid.ui.tests;

import org.junit.Test;

import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;

public class AuthorizationTest extends BaseTest {

    // TC-AUTH-01: Успешная авторизация с валидными данными
    @Test
    public void testSuccessfulAuthorizationWithValidCredentials() {
        authPage.checkAuthorizationScreenIsDisplayed();
        authPage.enterLogin(TestData.VALID_LOGIN);
        authPage.enterPassword(TestData.VALID_PASSWORD);
        authPage.clickSignInButton();
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-AUTH-02: Неуспешная авторизация с неверным логином
    @Test
    public void testFailedAuthorizationWithWrongLogin() {
        authPage.login(TestData.INVALID_LOGIN, TestData.VALID_PASSWORD);
        authPage.checkLoginFailed();
    }

    // TC-AUTH-03: Неуспешная авторизация с неверным паролем
    @Test
    public void testFailedAuthorizationWithWrongPassword() {
        authPage.login(TestData.VALID_LOGIN, TestData.INVALID_PASSWORD);
        authPage.checkLoginFailed();
    }

    // TC-AUTH-04: Валидация пустого поля "Логин"
    @Test
    public void testValidationOfEmptyLoginField() {
        authPage.checkAuthorizationScreenIsDisplayed();
        authPage.enterPassword(TestData.VALID_PASSWORD);
        authPage.clickSignInButton();
        authPage.checkValidationErrorIsDisplayed();
    }

    // TC-AUTH-05: Валидация пустого поля "Пароль"
    @Test
    public void testValidationOfEmptyPasswordField() {
        authPage.checkAuthorizationScreenIsDisplayed();
        authPage.enterLogin(TestData.VALID_LOGIN);
        authPage.clickSignInButton();
        authPage.checkValidationErrorIsDisplayed();
    }

    // TC-AUTH-06: Валидация логина в верхнем регистре
    @Test
    public void testLoginCaseSensitivity() {
        authPage.login(TestData.UPPERCASE_LOGIN, TestData.VALID_PASSWORD);
        authPage.checkLoginFailed();
    }

    // TC-AUTH-08: Валидация пароля в верхнем регистре
    @Test
    public void testPasswordCaseSensitivity() {
        authPage.login(TestData.VALID_LOGIN, TestData.UPPERCASE_PASSWORD);
        authPage.checkLoginFailed();
    }

    // TC-AUTH-09: Валидация логина с пробелами в середине
    @Test
    public void testLoginWithSpacesInMiddle() {
        authPage.login(TestData.LOGIN_WITH_SPACES, TestData.VALID_PASSWORD);
        authPage.checkLoginFailed();
    }

    // TC-AUTH-10: Валидация логина из одних пробелов
    @Test
    public void testLoginWithOnlySpaces() {
        authPage.login(TestData.LOGIN_ONLY_SPACES, TestData.VALID_PASSWORD);
        authPage.checkValidationErrorIsDisplayed();
    }
}