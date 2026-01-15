package ru.iteco.fmhandroid.ui.utils;

import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ViewMatchersUtils {

    // Проверяет, что TextView имеет непустой текст
    public static TypeSafeMatcher<View> hasNonEmptyText() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof TextView) {
                    String text = ((TextView) view).getText().toString();
                    return text != null && !text.trim().isEmpty();
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has non-empty text");
            }
        };
    }
}
