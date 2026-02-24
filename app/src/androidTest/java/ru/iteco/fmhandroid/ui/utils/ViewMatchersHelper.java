package ru.iteco.fmhandroid.ui.utils;

import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.endsWith;

import android.view.View;
import android.view.WindowManager;

import androidx.test.espresso.Root;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import ru.iteco.fmhandroid.R;

public class ViewMatchersHelper {

    private ViewMatchersHelper() {
    }

    // Получение Matcher для поля ввода логина
    public static Matcher<View> getLoginField() {
        return allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.login_text_input_layout))
        );
    }

    // Получение Matcher для поля ввода пароля
    public static Matcher<View> getPasswordField() {
        return allOf(
                withClassName(endsWith("EditText")),
                isDescendantOfA(withId(R.id.password_text_input_layout))
        );
    }

    // Matcher для проверки toast сообщений
    public static Matcher<Root> isToast() {
        return new TypeSafeMatcher<Root>() {
            @Override
            protected boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                return type == WindowManager.LayoutParams.TYPE_TOAST ||
                        type == WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }
        };
    }
}
