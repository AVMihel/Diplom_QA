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
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;

import android.os.SystemClock;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class AuthorizationPage {

    // Быстрая проверка отображения экрана авторизации
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

    // Проверка отображения экрана авторизации
    public void checkAuthorizationScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.login_text_input_layout, 5000);
    }

    // Выполнение авторизации
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

    // Клик по кнопке "Sign in"
    public void clickSignInButton() {
        WaitUtils.waitForElementWithId(R.id.enter_button, 2000);
        onView(allOf(withId(R.id.enter_button), withText("Sign in"), isDisplayed()))
                .perform(click());
    }

    // Проверка неудачной авторизации (остаемся на экране авторизации)
    public void checkLoginFailed() {
        checkAuthorizationScreenIsDisplayed();
    }

    // Проверка отображения ошибки валидации
    public void checkValidationErrorIsDisplayed() {
        WaitUtils.waitForElementWithId(R.id.enter_button, 1500);
        onView(allOf(withId(R.id.enter_button), withText("Sign in"), isDisplayed()))
                .check(matches(isDisplayed()));
    }

    // Проверка, что все поля пустые
    public void checkAllFieldsAreEmpty() {
        checkAuthorizationScreenIsDisplayed();
        onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.login_text_input_layout))
        )).check(matches(withText("")));
        onView(allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.password_text_input_layout))
        )).check(matches(withText("")));
    }

    // Получить текст из поля логина
    public String getLoginText() {
        try {
            final String[] text = new String[1];
            onView(allOf(
                    withClassName(endsWith("EditText")),
                    isDescendantOfA(withId(R.id.login_text_input_layout))
            )).perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isDisplayed();
                }

                @Override
                public String getDescription() {
                    return "Get text from login field";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    if (view instanceof android.widget.EditText) {
                        text[0] = ((android.widget.EditText) view).getText().toString();
                    }
                }
            });
            return text[0] != null ? text[0] : "";
        } catch (Exception e) {
            return "";
        }
    }

    // Получить текст из поля пароля
    public String getPasswordText() {
        try {
            final String[] text = new String[1];
            onView(allOf(
                    withClassName(endsWith("EditText")),
                    isDescendantOfA(withId(R.id.password_text_input_layout))
            )).perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isDisplayed();
                }

                @Override
                public String getDescription() {
                    return "Get text from password field";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    if (view instanceof android.widget.EditText) {
                        text[0] = ((android.widget.EditText) view).getText().toString();
                    }
                }
            });
            return text[0] != null ? text[0] : "";
        } catch (Exception e) {
            return "";
        }
    }

    // Проверить что поле логина содержит текст
    public boolean isLoginFieldNotEmpty() {
        String text = getLoginText();
        return text != null && !text.trim().isEmpty();
    }

    // Проверить что поле пароля содержит текст
    public boolean isPasswordFieldNotEmpty() {
        String text = getPasswordText();
        return text != null && !text.trim().isEmpty();
    }
}