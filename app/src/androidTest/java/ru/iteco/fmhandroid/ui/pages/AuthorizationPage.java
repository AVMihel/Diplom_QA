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

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class AuthorizationPage {

    private static final int MEDIUM_DELAY = 500;
    private static final int LONG_DELAY = 1500;
    private static final int POLLING_DELAY = 50;
    private static final int RETRY_ATTEMPTS = 3;
    private static final int RETRY_DELAY = 300;

    // Текстовые константы
    private static final String SIGN_IN_TEXT = "Sign in";
    private static final String GET_LOGIN_TEXT_DESC = "Get text from login field";
    private static final String GET_PASSWORD_TEXT_DESC = "Get text from password field";

    // ID элементов
    private static final int LOGIN_INPUT_LAYOUT_ID = R.id.login_text_input_layout;
    private static final int PASSWORD_INPUT_LAYOUT_ID = R.id.password_text_input_layout;
    private static final int ENTER_BUTTON_ID = R.id.enter_button;

    // Быстрая проверка отображения экрана авторизации
    public boolean isAuthScreenDisplayedQuick(long timeout) {
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            try {
                onView(withId(LOGIN_INPUT_LAYOUT_ID)).check(matches(isDisplayed()));
                return true;
            } catch (Exception e) {
                WaitUtils.waitForMillis(POLLING_DELAY);
            }
        }
        return false;
    }

    // Проверка отображения экрана авторизации
    public void checkAuthorizationScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(LOGIN_INPUT_LAYOUT_ID, LONG_DELAY);
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
        WaitUtils.waitForElementWithId(LOGIN_INPUT_LAYOUT_ID, LONG_DELAY);
        onView(getLoginField()).perform(replaceText(login), closeSoftKeyboard());
    }

    // Ввод пароля
    public void enterPassword(String password) {
        WaitUtils.waitForElementWithId(PASSWORD_INPUT_LAYOUT_ID, LONG_DELAY);
        onView(getPasswordField()).perform(replaceText(password), closeSoftKeyboard());
    }

    // Клик по кнопке "Sign in"
    public void clickSignInButton() {
        WaitUtils.waitForElementWithId(ENTER_BUTTON_ID, LONG_DELAY);
        onView(allOf(withId(ENTER_BUTTON_ID), withText(SIGN_IN_TEXT), isDisplayed()))
                .perform(click());
    }

    // Проверка неудачной авторизации (остаемся на экране авторизации)
    public void checkLoginFailed() {
        checkAuthorizationScreenIsDisplayed();
    }

    // Проверка отображения ошибки валидации
    public void checkValidationErrorIsDisplayed() {
        WaitUtils.waitForElementWithId(ENTER_BUTTON_ID, LONG_DELAY);
        onView(allOf(withId(ENTER_BUTTON_ID), withText(SIGN_IN_TEXT), isDisplayed()))
                .check(matches(isDisplayed()));
    }

    // Проверка, что все поля пустые (с retry логикой)
    public void checkAllFieldsAreEmpty() {
        checkAuthorizationScreenIsDisplayed();
        WaitUtils.waitForMillis(MEDIUM_DELAY);

        for (int attempt = 0; attempt < RETRY_ATTEMPTS; attempt++) {
            try {
                onView(getLoginField()).check(matches(withText("")));
                onView(getPasswordField()).check(matches(withText("")));
                return; // Успешно
            } catch (Exception e) {
                if (attempt == RETRY_ATTEMPTS - 1) throw e;
                WaitUtils.waitForMillis(RETRY_DELAY);
            }
        }
    }

    // Получить текст из поля логина
    public String getLoginText() {
        return getTextFieldText(LOGIN_INPUT_LAYOUT_ID, GET_LOGIN_TEXT_DESC);
    }

    // Получить текст из поля пароля
    public String getPasswordText() {
        return getTextFieldText(PASSWORD_INPUT_LAYOUT_ID, GET_PASSWORD_TEXT_DESC);
    }

    // Получение Matcher для поля ввода логина (EditText внутри TextInputLayout)
    private Matcher<View> getLoginField() {
        return allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(LOGIN_INPUT_LAYOUT_ID))
        );
    }

    // Получение Matcher для поля ввода пароля (EditText внутри TextInputLayout)
    private Matcher<View> getPasswordField() {
        return allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(PASSWORD_INPUT_LAYOUT_ID))
        );
    }

    // Получение текста из поля ввода по ID его контейнера (TextInputLayout)
    private String getTextFieldText(int layoutId, String description) {
        try {
            final String[] text = new String[1];
            onView(allOf(
                    withClassName(endsWith("EditText")),
                    isDescendantOfA(withId(layoutId))
            )).perform(createGetTextAction(text, description));
            return text[0] != null ? text[0] : "";
        } catch (Exception e) {
            return "";
        }
    }

    // Создание кастомного ViewAction для извлечения текста из EditText
    private ViewAction createGetTextAction(final String[] textHolder, final String description) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof android.widget.EditText) {
                    textHolder[0] = ((android.widget.EditText) view).getText().toString();
                }
            }
        };
    }
}