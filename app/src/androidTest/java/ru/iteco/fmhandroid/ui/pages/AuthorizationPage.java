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

    private final ViewInteraction loginField = onView(
            allOf(
                    withId(R.id.login_text_input_layout),
                    isDisplayed()
            )
    );

    private final ViewInteraction passwordField = onView(
            allOf(
                    withId(R.id.password_text_input_layout),
                    isDisplayed()
            )
    );

    private final ViewInteraction signInButton = onView(
            allOf(withId(R.id.enter_button), withText("Sign in"), isDisplayed())
    );

    private final ViewInteraction authorizationText = onView(
            allOf(withText("Authorization"), isDisplayed())
    );

    // Проверяет, что экран авторизации отображается (по заголовку "Authorization")
    public AuthorizationPage checkAuthorizationScreenIsDisplayed() {
        WaitUtils.waitForElement(authorizationText, 10000);
        authorizationText.check(matches(isDisplayed()));
        return this;
    }

    // Вводит логин в поле ввода (находит EditText внутри TextInputLayout)
    public AuthorizationPage enterLogin(String login) {
        WaitUtils.waitForElement(loginField, 5000);
        onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.login_text_input_layout))
        )).perform(replaceText(login), closeSoftKeyboard());
        return this;
    }

    // Вводит пароль в поле ввода (находит EditText внутри TextInputLayout)
    public AuthorizationPage enterPassword(String password) {
        WaitUtils.waitForElement(passwordField, 5000);
        onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.password_text_input_layout))
        )).perform(replaceText(password), closeSoftKeyboard());
        return this;
    }

    // Нажимает кнопку "Sign in" для выполнения авторизации
    public AuthorizationPage clickSignInButton() {
        WaitUtils.waitForElement(signInButton, 5000);
        signInButton.perform(click());
        return this;
    }

    // Выполняет полный процесс авторизации: проверка экрана → ввод логина → ввод пароля → вход
    public void login(String login, String password) {
        checkAuthorizationScreenIsDisplayed();
        enterLogin(login);
        enterPassword(password);
        clickSignInButton();
    }

    // Проверяет, что авторизация не удалась (пользователь остался на экране авторизации)
    public AuthorizationPage checkLoginFailed() {
        WaitUtils.waitForElement(authorizationText, 3000);
        authorizationText.check(matches(isDisplayed()));
        return this;
    }

    // Проверяет, что отображается ошибка валидации (кнопка "Sign in" все еще видна)
    public AuthorizationPage checkValidationErrorIsDisplayed() {
        WaitUtils.waitForElement(signInButton, 3000);
        signInButton.check(matches(isDisplayed()));
        return this;
    }
}