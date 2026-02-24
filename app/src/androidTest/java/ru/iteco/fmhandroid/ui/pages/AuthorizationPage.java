package ru.iteco.fmhandroid.ui.pages;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import io.qameta.allure.kotlin.Allure;
import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.ViewMatchersHelper;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class AuthorizationPage {

    private static final int DEFAULT_TIMEOUT = 3000;
    private static final int SHORT_TIMEOUT = 2000;

    public static final String ERROR_SOMETHING_WENT_WRONG = "Something went wrong. Try again later.";
    public static final String ERROR_EMPTY_FIELDS = "Login and password cannot be empty";


    public AuthorizationPage login(String login, String password) {
        Allure.step("Авторизация с логином: " + login + ", паролем: " + password);
        waitForPageLoaded();
        enterLogin(login);
        enterPassword(password);
        clickSignInButton();
        return this;
    }

    public AuthorizationPage enterLogin(String login) {
        Allure.step("Ввод логина: " + login);
        onView(ViewMatchersHelper.getLoginField())
                .perform(replaceText(login), closeSoftKeyboard());
        return this;
    }

    public AuthorizationPage enterPassword(String password) {
        Allure.step("Ввод пароля: " + password);
        onView(ViewMatchersHelper.getPasswordField())
                .perform(replaceText(password), closeSoftKeyboard());
        return this;
    }

    public AuthorizationPage clickSignInButton() {
        Allure.step("Нажатие кнопки SIGN IN");
        onView(allOf(withId(R.id.enter_button), withText("Sign in")))
                .perform(click());
        return this;
    }

    public AuthorizationPage clearFields() {
        Allure.step("Очистка полей ввода");
        onView(ViewMatchersHelper.getLoginField()).perform(replaceText(""));
        onView(ViewMatchersHelper.getPasswordField()).perform(replaceText(""));
        return this;
    }

    public void checkErrorMessage(String message) {
        Allure.step("Проверка сообщения об ошибке: " + message);
        onView(withText(message))
                .inRoot(ViewMatchersHelper.isToast())
                .check(matches(isDisplayed()));
    }

    public void waitForPageLoaded() {
        WaitUtils.waitForElementWithId(R.id.login_text_input_layout, DEFAULT_TIMEOUT);
    }

    public boolean isAuthorizationScreenDisplayed() {
        return WaitUtils.isElementDisplayedWithId(R.id.login_text_input_layout, SHORT_TIMEOUT);
    }

    public boolean areAllFieldsEmpty() {
        Allure.step("Проверка, что все поля авторизации пусты");
        try {
            onView(ViewMatchersHelper.getLoginField()).check(matches(withText("")));
            onView(ViewMatchersHelper.getPasswordField()).check(matches(withText("")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}