package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.AuthorizationPage;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Авторизация")
@Feature("Функциональность входа в систему")
@DisplayName("Тесты авторизации")
public class AuthorizationTest extends BaseTest {

    @Before
    public void setUp() {
        ensureOnAuthScreen();
    }

    @After
    public void tearDown() {
        try {
            if (mainPage.isMainScreenDisplayed()) {
                performLogout();
            }
        } catch (Exception e) {
        }
    }

    @Test
    @DisplayName("Успешная авторизация с валидными данными")
    @Description("TC-AUTH-01: Успешная авторизация с валидными данными")
    @Story("Успешный вход")
    public void testSuccessfulAuthorization() {
        authPage.login(TestData.VALID_LOGIN, TestData.VALID_PASSWORD);
        assertTrue("Main screen is not displayed after successful authorization",
                mainPage.isMainScreenDisplayed());
    }

    @Test
    @DisplayName("Валидация пустых полей")
    @Description("TC-AUTH-02: Валидация пустых полей")
    @Story("Валидация обязательных полей")
    public void testEmptyFieldsValidation() {

        // Пустой логин
        authPage.enterPassword(TestData.VALID_PASSWORD)
                .clickSignInButton();
        authPage.checkErrorMessage(AuthorizationPage.ERROR_EMPTY_FIELDS);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());

        // Пустой пароль
        authPage.clearFields()
                .enterLogin(TestData.VALID_LOGIN)
                .clickSignInButton();
        authPage.checkErrorMessage(AuthorizationPage.ERROR_EMPTY_FIELDS);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());

        // Логин из пробелов
        authPage.clearFields()
                .enterLogin(TestData.LOGIN_ONLY_SPACES)
                .enterPassword(TestData.VALID_PASSWORD)
                .clickSignInButton();
        authPage.checkErrorMessage(AuthorizationPage.ERROR_EMPTY_FIELDS);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());

        // Пароль из пробелов
        authPage.clearFields()
                .enterLogin(TestData.VALID_LOGIN)
                .enterPassword(TestData.PASSWORD_ONLY_SPACES)
                .clickSignInButton();
        authPage.checkErrorMessage(AuthorizationPage.ERROR_EMPTY_FIELDS);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Негативная авторизация с невалидными данными")
    @Description("TC-AUTH-03: Негативная авторизация с различными невалидными данными")
    @Story("Обработка неверных учетных данных")
    public void testInvalidCredentials() {

        // Неверный логин
        authPage.login(TestData.INVALID_LOGIN, TestData.VALID_PASSWORD);
        authPage.checkErrorMessage(AuthorizationPage.ERROR_SOMETHING_WENT_WRONG);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());

        // Неверный пароль
        authPage.login(TestData.VALID_LOGIN, TestData.INVALID_PASSWORD);
        authPage.checkErrorMessage(AuthorizationPage.ERROR_SOMETHING_WENT_WRONG);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());

        // Логин в верхнем регистре
        authPage.login(TestData.UPPERCASE_LOGIN, TestData.VALID_PASSWORD);
        authPage.checkErrorMessage(AuthorizationPage.ERROR_SOMETHING_WENT_WRONG);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());

        // Пароль в верхнем регистре
        authPage.login(TestData.VALID_LOGIN, TestData.UPPERCASE_PASSWORD);
        authPage.checkErrorMessage(AuthorizationPage.ERROR_SOMETHING_WENT_WRONG);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());

        // Логин с пробелами в середине
        authPage.login(TestData.LOGIN_WITH_SPACES, TestData.VALID_PASSWORD);
        authPage.checkErrorMessage(AuthorizationPage.ERROR_SOMETHING_WENT_WRONG);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());
    }

    @Test
    @DisplayName("Валидация пробелов в начале/конце логина")
    @Description("TC-AUTH-04: Валидация пробелов в начале/конце логина")
    @Story("Обработка пробелов в логине")
    public void testLoginWithLeadingTrailingSpaces() {
        authPage.login(TestData.LOGIN_WITH_LEADING_TRAILING_SPACES, TestData.VALID_PASSWORD);

        if (mainPage.isMainScreenDisplayed()) {
            fail("Login with leading/trailing spaces succeeded: '" +
                    TestData.LOGIN_WITH_LEADING_TRAILING_SPACES + "'");
        }

        authPage.checkErrorMessage(AuthorizationPage.ERROR_SOMETHING_WENT_WRONG);
        assertTrue("Authorization screen is not displayed", authPage.isAuthorizationScreenDisplayed());
    }
}