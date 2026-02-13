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

import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;

import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

public class AuthorizationPage {

    private static final int LONG_DELAY = 1500;

    // Текстовые константы
    private static final String SIGN_IN_TEXT = "Sign in";
    private static final String ERROR_SOMETHING_WENT_WRONG = "Something went wrong. Try again later.";
    private static final String ERROR_LOGIN_PASSWORD_EMPTY = "Login and password cannot be empty";

    // ID элементов
    private static final int LOGIN_INPUT_LAYOUT_ID = R.id.login_text_input_layout;
    private static final int PASSWORD_INPUT_LAYOUT_ID = R.id.password_text_input_layout;
    private static final int ENTER_BUTTON_ID = R.id.enter_button;

    // Проверка отображения экрана авторизации
    public boolean isAuthorizationScreenDisplayed() {
        try {
            onView(withId(LOGIN_INPUT_LAYOUT_ID)).check(matches(isDisplayed()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Проверка отображения экрана авторизации
    public AuthorizationPage checkAuthorizationScreenIsDisplayed() {
        WaitUtils.waitForElementWithId(LOGIN_INPUT_LAYOUT_ID, LONG_DELAY);
        return this;
    }

    // Проверка отображения сообщения 'Something went wrong. Try again later.'
    public void checkSomethingWentWrongMessage() {
        try {
            onView(withText(ERROR_SOMETHING_WENT_WRONG))
                    .inRoot(isToast())
                    .check(matches(isDisplayed()));
        } catch (Exception e) {
            onView(withText(ERROR_SOMETHING_WENT_WRONG))
                    .check(matches(isDisplayed()));
        }
    }

    // Проверка отображения сообщения 'Login and password cannot be empty'
    public void checkEmptyFieldsMessage() {
        try {
            onView(withText(ERROR_LOGIN_PASSWORD_EMPTY))
                    .inRoot(isToast())
                    .check(matches(isDisplayed()));
        } catch (Exception e) {
            onView(withText(ERROR_LOGIN_PASSWORD_EMPTY))
                    .check(matches(isDisplayed()));
        }
    }

    // Выполнение авторизации с логином и паролем
    public void login(String login, String password) {
        checkAuthorizationScreenIsDisplayed();
        enterLogin(login);
        enterPassword(password);
        clickSignInButton();
    }

    // Ввод логина
    public AuthorizationPage enterLogin(String login) {
        waitForElementWithId(LOGIN_INPUT_LAYOUT_ID, LONG_DELAY);
        onView(getLoginField()).perform(replaceText(login), closeSoftKeyboard());
        return this;
    }

    // Ввод пароля
    public AuthorizationPage enterPassword(String password) {
        waitForElementWithId(PASSWORD_INPUT_LAYOUT_ID, LONG_DELAY);
        onView(getPasswordField()).perform(replaceText(password), closeSoftKeyboard());
        return this;
    }

    // Клик по кнопке 'Sign in'
    public AuthorizationPage clickSignInButton() {
        waitForElementWithId(ENTER_BUTTON_ID, LONG_DELAY);
        onView(allOf(withId(ENTER_BUTTON_ID), withText(SIGN_IN_TEXT), isDisplayed()))
                .perform(click());
        return this;
    }

    // Проверка, что все поля пустые
    public boolean areAllFieldsEmpty() {
        try {
            String loginText = getLoginText();
            String passwordText = getPasswordText();
            return loginText.isEmpty() && passwordText.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // Получение текста из поля логина
    public String getLoginText() {
        return getTextFieldText(LOGIN_INPUT_LAYOUT_ID);
    }

    // Получение текста из поля пароля
    public String getPasswordText() {
        return getTextFieldText(PASSWORD_INPUT_LAYOUT_ID);
    }

    // Вспомогательные методы
    private void waitForElementWithId(int elementId, long timeout) {
        WaitUtils.waitForElementWithId(elementId, timeout);
    }

    // Получение текста из текстового поля
    private String getTextFieldText(int layoutId) {
        try {
            final String[] text = new String[1];
            onView(allOf(
                    withClassName(endsWith("EditText")),
                    isDescendantOfA(withId(layoutId))
            )).perform(new GetTextAction(text));
            return text[0] != null ? text[0] : "";
        } catch (Exception e) {
            return "";
        }
    }

    // Методы получения Matcher для полей ввода
    private Matcher<View> getLoginField() {
        return allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(LOGIN_INPUT_LAYOUT_ID))
        );
    }

    private Matcher<View> getPasswordField() {
        return allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(PASSWORD_INPUT_LAYOUT_ID))
        );
    }

    // Matcher для проверки toast сообщений
    private static Matcher<Root> isToast() {
        return new TypeSafeMatcher<Root>() {
            @Override
            protected boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    return windowToken == appToken;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }
        };
    }

    // Внутренний класс для получения текста из EditText
    private static class GetTextAction implements ViewAction {
        private final String[] textHolder;

        GetTextAction(String[] textHolder) {
            this.textHolder = textHolder;
        }

        @Override
        public Matcher<View> getConstraints() {
            return isDisplayed();
        }

        @Override
        public String getDescription() {
            return "Получение текста из EditText";
        }

        @Override
        public void perform(UiController uiController, View view) {
            if (view instanceof android.widget.EditText) {
                textHolder[0] = ((android.widget.EditText) view).getText().toString();
            }
        }
    }
}