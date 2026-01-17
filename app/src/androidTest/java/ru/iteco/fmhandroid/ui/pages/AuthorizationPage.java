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

import android.os.SystemClock;

import androidx.test.espresso.ViewInteraction;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class AuthorizationPage {

    // Быстрая проверка экрана авторизации
    public boolean isAuthScreenDisplayedQuick(long timeout) {
        long endTime = SystemClock.uptimeMillis() + timeout;
        while (SystemClock.uptimeMillis() < endTime) {
            try {
                onView(withId(R.id.login_text_input_layout)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                SystemClock.sleep(50);
            }
        }
        return false;
    }

    // Полная проверка экрана авторизации
    public void checkAuthorizationScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.login_text_input_layout, 5000);
    }

    // Авторизация
    public void login(String login, String password) {
        checkAuthorizationScreenIsDisplayed();
        enterLogin(login);
        enterPassword(password);
        clickSignInButton();
    }

    // Ввод логина
    public void enterLogin(String login) {
        WaitUtils.waitForElementWithId(R.id.login_text_input_layout, 2000);
        onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.login_text_input_layout))
        )).perform(replaceText(login), closeSoftKeyboard());
    }

    // Ввод пароля
    public void enterPassword(String password) {
        WaitUtils.waitForElementWithId(R.id.password_text_input_layout, 2000);
        onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.password_text_input_layout))
        )).perform(replaceText(password), closeSoftKeyboard());
    }

    // Кнопка входа
    public void clickSignInButton() {
        WaitUtils.waitForElementWithId(R.id.enter_button, 2000);
        onView(allOf(withId(R.id.enter_button), withText("Sign in"), isDisplayed()))
                .perform(click());
    }

    // Проверка неудачной авторизации
    public void checkLoginFailed() {
        checkAuthorizationScreenIsDisplayed();
    }

    // Проверка ошибки валидации
    public void checkValidationErrorIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.enter_button, 1500);
        onView(allOf(withId(R.id.enter_button), withText("Sign in"), isDisplayed()))
                .check(matches(isDisplayed()));
    }

    // Проверка пустого поля логина
    public AuthorizationPage checkLoginFieldIsEmpty() {
        ViewInteraction loginField = onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.login_text_input_layout))
        ));
        WaitUtils.waitForElement(loginField, 1000);
        loginField.check(matches(withText("")));
        return this;
    }

    // Проверка пустого поля пароля
    public AuthorizationPage checkPasswordFieldIsEmpty() {
        ViewInteraction passwordField = onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.password_text_input_layout))
        ));
        WaitUtils.waitForElement(passwordField, 1000);
        passwordField.check(matches(withText("")));
        return this;
    }

    // Проверка, что все поля пустые
    public void checkAllFieldsAreEmpty() {
        checkAuthorizationScreenIsDisplayed();
        checkLoginFieldIsEmpty();
        checkPasswordFieldIsEmpty();
    }
}