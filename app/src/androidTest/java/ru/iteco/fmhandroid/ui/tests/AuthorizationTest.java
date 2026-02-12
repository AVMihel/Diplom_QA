package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Allure;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Авторизация")
@Feature("Функциональность входа в систему")
@DisplayName("Тесты авторизации в мобильном приложении")
public class AuthorizationTest extends BaseTest {

    @Before
    public void setUp() {
        Allure.step("Настройка тестового окружения - переход на экран авторизации");
        setUpToAuthScreen();
    }

    @After
    public void tearDown() {
        Allure.step("Очистка после теста - возврат на экран авторизации");
        tearDownToAuthScreen();
    }

    @Test
    @DisplayName("Успешная авторизация с валидными данными")
    @Description("TC-AUTH-01: Успешная авторизация с валидными данными")
    @Story("Пользователь может войти с правильными учетными данными")
    public void testSuccessfulAuthorizationWithValidCredentials() {
        Allure.step("Шаг 1: Проверка отображения экрана авторизации");
        authPage.checkAuthorizationScreenIsDisplayed();

        Allure.step("Шаг 2: Ввод валидных учетных данных");
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);

        Allure.step("Шаг 3: Проверка перехода на главный экран");
        assertTrue("BUG: Main screen should be displayed after successful login with valid credentials",
                mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Неуспешная авторизация с неверным логином")
    @Description("TC-AUTH-02: Проверка обработки неверного логина")
    @Story("Система отклоняет неверный логин")
    public void testFailedAuthorizationWithWrongLogin() {
        Allure.step("Шаг 1: Выполнение авторизации с неверным логином");
        authPage.login(TestData.INVALID_LOGIN, TestData.VALID_PASSWORD);

        Allure.step("Шаг 2: Проверка сообщения об ошибке");
        authPage.checkSomethingWentWrongMessage();

        Allure.step("Шаг 3: Проверка, что остались на экране авторизации");
        assertTrue("BUG: Authorization with invalid Login should not be successful",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Неуспешная авторизация с неверным паролем")
    @Description("TC-AUTH-03: Неуспешная авторизация с неверным паролем")
    @Story("Система отклоняет неверный пароль")
    public void testFailedAuthorizationWithWrongPassword() {
        Allure.step("Шаг 1: Выполнение авторизации с неверным паролем");
        authPage.login(TestData.VALID_LOGIN, TestData.INVALID_PASSWORD);

        Allure.step("Шаг 2: Проверка сообщения об ошибке");
        authPage.checkSomethingWentWrongMessage();

        Allure.step("Шаг 3: Проверка, что остались на экране авторизации");
        assertTrue("BUG: Authorization with invalid Password should not be successful",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Валидация пустого поля 'Логин'")
    @Description("TC-AUTH-04: Валидация пустого поля 'Логин'")
    @Story("Система проверяет обязательные поля")
    public void testValidationOfEmptyLoginField() {
        Allure.step("Шаг 1: Проверка отображения экрана авторизации");
        authPage.checkAuthorizationScreenIsDisplayed();

        Allure.step("Шаг 2: Ввод только пароля");
        authPage.enterPassword(TestData.VALID_PASSWORD);

        Allure.step("Шаг 3: Клик по кнопке 'Sign in'");
        authPage.clickSignInButton();

        Allure.step("Шаг 4: Проверка сообщения об ошибке");
        authPage.checkEmptyFieldsMessage();

        Allure.step("Шаг 5: Проверка, что остались на экране авторизации");
        assertTrue("BUG: Authorization with empty Login field should not be successful",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Валидация пустого поля 'Пароль'")
    @Description("TC-AUTH-05: Валидация пустого поля 'Пароль'")
    @Story("Система проверяет обязательные поля")
    public void testValidationOfEmptyPasswordField() {
        Allure.step("Шаг 1: Проверка отображения экрана авторизации");
        authPage.checkAuthorizationScreenIsDisplayed();

        Allure.step("Шаг 2: Ввод только логина");
        authPage.enterLogin(TestData.VALID_LOGIN);

        Allure.step("Шаг 3: Клик по кнопке 'Sign in'");
        authPage.clickSignInButton();

        Allure.step("Шаг 4: Проверка сообщения об ошибке");
        authPage.checkEmptyFieldsMessage();

        Allure.step("Шаг 5: Проверка, что остались на экране авторизации");
        assertTrue("BUG: Authorization with empty Password field should not be successful",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Валидация логина в верхнем регистре")
    @Description("TC-AUTH-06: Валидация логина в верхнем регистре")
    @Story("Логин чувствителен к регистру")
    public void testLoginCaseSensitivity() {
        Allure.step("Шаг 1: Выполнение авторизации с логином в верхнем регистре");
        authPage.login(TestData.UPPERCASE_LOGIN, TestData.VALID_PASSWORD);

        Allure.step("Шаг 2: Проверка сообщения об ошибке");
        authPage.checkSomethingWentWrongMessage();

        Allure.step("Шаг 3: Проверка, что остались на экране авторизации");
        assertTrue("BUG: Authorization with uppercase Login should not be successful (case sensitive)",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Валидация пароля в верхнем регистре")
    @Description("TC-AUTH-08: Валидация пароля в верхнем регистре")
    @Story("Пароль чувствителен к регистру")
    public void testPasswordCaseSensitivity() {
        Allure.step("Шаг 1: Выполнение авторизации с паролем в верхнем регистре");
        authPage.login(TestData.VALID_LOGIN, TestData.UPPERCASE_PASSWORD);

        Allure.step("Шаг 2: Проверка сообщения об ошибке");
        authPage.checkSomethingWentWrongMessage();

        Allure.step("Шаг 3: Проверка, что остались на экране авторизации");
        assertTrue("BUG: Authorization with uppercase Password should not be successful (case sensitive)",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Валидация логина с пробелами в середине")
    @Description("TC-AUTH-09: Валидация логина с пробелами в середине")
    @Story("Логин не должен содержать пробелы")
    public void testLoginWithSpacesInMiddle() {
        Allure.step("Шаг 1: Выполнение авторизации с логином содержащим пробелы");
        authPage.login(TestData.LOGIN_WITH_SPACES, TestData.VALID_PASSWORD);

        Allure.step("Шаг 2: Проверка сообщения об ошибке");
        authPage.checkSomethingWentWrongMessage();

        Allure.step("Шаг 3: Проверка, что остались на экране авторизации");
        assertTrue("BUG: Authorization with Login containing spaces should not be successful",
                authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Валидация логина из одних пробелов")
    @Description("TC-AUTH-10: Валидация логина из одних пробелов")
    @Story("Логин не может состоять только из пробелов")
    public void testLoginWithOnlySpaces() {
        Allure.step("Шаг 1: Проверка отображения экрана авторизации");
        authPage.checkAuthorizationScreenIsDisplayed();

        Allure.step("Шаг 2: Ввод логина, состоящего только из пробелов");
        authPage.enterLogin(TestData.LOGIN_ONLY_SPACES);

        Allure.step("Шаг 3: Ввод валидного пароля");
        authPage.enterPassword(TestData.VALID_PASSWORD);

        Allure.step("Шаг 4: Клик по кнопке 'Sign in'");
        authPage.clickSignInButton();

        Allure.step("Шаг 5: Проверка сообщения об ошибке");
        authPage.checkEmptyFieldsMessage();

        Allure.step("Шаг 6: Проверка, что остались на экране авторизации");
        assertTrue("BUG: Authorization with Login field filled with spaces should not be successful",
                authPage.isAuthorizationScreenDisplayed());
    }
}