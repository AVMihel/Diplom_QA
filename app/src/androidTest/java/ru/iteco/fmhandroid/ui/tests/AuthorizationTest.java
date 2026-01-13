package ru.iteco.fmhandroid.ui.tests;

import org.junit.Test;

import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;

// Тестовый класс для проверки функциональности авторизации
public class AuthorizationTest extends BaseTest {

    // TC-AUTH-01: Успешная авторизация с валидными данными
    @Test
    public void testSuccessfulAuthorizationWithValidCredentials() {
        // Проверяем отображение экрана авторизации
        authPage.checkAuthorizationScreenIsDisplayed();

        // Вводим валидный логин и пароль
        authPage.enterLogin(TestData.VALID_LOGIN);
        authPage.enterPassword(TestData.VALID_PASSWORD);
        authPage.clickSignInButton();

        // Проверяем успешную авторизацию (пользователь на главном экране)
        mainPage.checkSuccessfulAuthorization();

        // Выполняем выход из системы
        mainPage.logout();

        // Проверяем возврат на экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();
    }

    // TC-AUTH-02: Неуспешная авторизация с неверным логином
    @Test
    public void testFailedAuthorizationWithWrongLogin() {
        // Пытаемся авторизоваться с неверным логином
        authPage.login(TestData.INVALID_LOGIN, TestData.VALID_PASSWORD);

        // Проверяем, что авторизация не удалась (остаемся на экране авторизации)
        authPage.checkLoginFailed();
    }

    // TC-AUTH-03: Неуспешная авторизация с неверным паролем
    @Test
    public void testFailedAuthorizationWithWrongPassword() {
        // Пытаемся авторизоваться с неверным паролем
        authPage.login(TestData.VALID_LOGIN, TestData.INVALID_PASSWORD);

        // Проверяем, что авторизация не удалась
        authPage.checkLoginFailed();
    }

    // TC-AUTH-04: Валидация пустого поля "Логин"
    @Test
    public void testValidationOfEmptyLoginField() {
        // Проверяем экран авторизации и вводим только пароль
        authPage.checkAuthorizationScreenIsDisplayed()
                .enterPassword(TestData.VALID_PASSWORD);

        // Пытаемся войти без логина
        authPage.clickSignInButton();

        // Проверяем, что отображается ошибка валидации
        authPage.checkValidationErrorIsDisplayed();
    }

    // TC-AUTH-05: Валидация пустого поля "Пароль"
    @Test
    public void testValidationOfEmptyPasswordField() {
        // Проверяем экран авторизации и вводим только логин
        authPage.checkAuthorizationScreenIsDisplayed()
                .enterLogin(TestData.VALID_LOGIN);

        // Пытаемся войти без пароля
        authPage.clickSignInButton();

        // Проверяем ошибку валидации
        authPage.checkValidationErrorIsDisplayed();
    }

    // TC-AUTH-06: Валидация логина в верхнем регистре
    @Test
    public void testLoginCaseSensitivity() {
        // Пытаемся авторизоваться с логином в верхнем регистре
        authPage.login(TestData.UPPERCASE_LOGIN, TestData.VALID_PASSWORD);

        // Проверяем, что авторизация не удалась (система чувствительна к регистру)
        authPage.checkLoginFailed();
    }

    // TC-AUTH-08: Валидация пароля в верхнем регистре
    @Test
    public void testPasswordCaseSensitivity() {
        // Пытаемся авторизоваться с паролем в верхнем регистре
        authPage.login(TestData.VALID_LOGIN, TestData.UPPERCASE_PASSWORD);

        // Проверяем, что авторизация не удалась
        authPage.checkLoginFailed();
    }

    // TC-AUTH-09: Валидация логина с пробелами в середине
    @Test
    public void testLoginWithSpacesInMiddle() {
        // Пытаемся авторизоваться с логином, содержащим пробелы
        authPage.login(TestData.LOGIN_WITH_SPACES, TestData.VALID_PASSWORD);

        // Проверяем результат (ожидаемо - авторизация не должна проходить)
        authPage.checkLoginFailed();
    }

    // TC-AUTH-10: Валидация логина из одних пробелов
    @Test
    public void testLoginWithOnlySpaces() {
        // Пытаемся авторизоваться с логином из пробелов
        authPage.login(TestData.LOGIN_ONLY_SPACES, TestData.VALID_PASSWORD);

        // Проверяем сообщение валидации (ожидаем ошибку валидации)
        authPage.checkValidationErrorIsDisplayed();
    }
}
