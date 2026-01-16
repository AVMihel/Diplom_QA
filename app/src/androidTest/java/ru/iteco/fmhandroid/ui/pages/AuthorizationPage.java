package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

// Page Object для работы с экраном авторизации
public class AuthorizationPage {

    // Проверяет, что экран авторизации отображается
    public void checkAuthorizationScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.login_text_input_layout, 10000);
    }

    // Вводит логин в поле ввода
    public void enterLogin(String login) {
        onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.login_text_input_layout))
        )).perform(replaceText(login), closeSoftKeyboard());
    }

    // Вводит пароль в поле ввода
    public void enterPassword(String password) {
        onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.password_text_input_layout))
        )).perform(replaceText(password), closeSoftKeyboard());
    }

    // Нажимает кнопку "SIGN IN" для выполнения авторизации
    public void clickSignInButton() {
        onView(allOf(withId(R.id.enter_button), withText("Sign in"), isDisplayed()))
                .perform(click());
    }

    // Выполняет полный процесс авторизации
    public void login(String login, String password) {
        checkAuthorizationScreenIsDisplayed();
        enterLogin(login);
        enterPassword(password);
        clickSignInButton();
    }

    // Проверяет, что авторизация не удалась (пользователь остался на экране авторизации)
    public void checkLoginFailed() {
        checkAuthorizationScreenIsDisplayed();
    }

    // Проверяет, что отображается ошибка валидации (кнопка "SIGN IN" все еще видна)
    public void checkValidationErrorIsDisplayed() {
        onView(allOf(withId(R.id.enter_button), withText("Sign in"), isDisplayed()))
                .check(matches(isDisplayed()));
    }

    // Проверяет, что поле логина пустое
    public AuthorizationPage checkLoginFieldIsEmpty() {
        ViewInteraction loginField = onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.login_text_input_layout))
        ));

        WaitUtils.waitForElement(loginField, 3000);
        loginField.check(matches(withText("")));
        return this;
    }

    // Проверяет, что поле пароля пустое
    public AuthorizationPage checkPasswordFieldIsEmpty() {
        ViewInteraction passwordField = onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.password_text_input_layout))
        ));

        WaitUtils.waitForElement(passwordField, 3000);
        passwordField.check(matches(withText("")));
        return this;
    }

    // Проверяет, что оба поля пустые (после выхода)
    public void checkAllFieldsAreEmpty() {
        checkAuthorizationScreenIsDisplayed();
        checkLoginFieldIsEmpty();
        checkPasswordFieldIsEmpty();
    }
}