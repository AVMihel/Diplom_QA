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

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

// Page Object для работы с экраном авторизации
public class AuthorizationPage {

    // Проверяет, что экран авторизации отображается
    public void checkAuthorizationScreenIsDisplayed() {
        // Простая проверка по полю логина
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

    // Нажимает кнопку "Sign in" для выполнения авторизации
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

    // Проверяет, что отображается ошибка валидации (кнопка "Sign in" все еще видна)
    public void checkValidationErrorIsDisplayed() {
        onView(allOf(withId(R.id.enter_button), withText("Sign in"), isDisplayed()))
                .check(matches(isDisplayed()));
    }
}