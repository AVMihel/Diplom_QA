package ru.iteco.fmhandroid.ui.tests;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;

public class NavigationTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();

    // TC-NAV-01: Основная навигация через боковое меню с главного экрана
    @Test
    public void testMainNavigationThroughSideMenu() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();

        // 3. Открываем боковое меню
        navigationDrawer.openMenu();

        // 4. Проверяем пункты меню
        navigationDrawer.checkMainMenuItemIsDisplayed();
        navigationDrawer.checkNewsMenuItemIsDisplayed();
        navigationDrawer.checkAboutMenuItemIsDisplayed();

        // 5. Переходим в раздел News
        navigationDrawer.clickNewsMenuItem();

        // 6. Проверяем экран News
        mainPage.checkNewsScreenIsDisplayed();

        // 7. Возвращаемся в Main через меню
        navigationDrawer.openMenu().clickMainMenuItem();

        // 8. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();

        // 9. Переходим в раздел About
        navigationDrawer.openMenu().clickAboutMenuItem();

        // 10. Проверяем экран About
        mainPage.checkAboutScreenIsDisplayed();

        // 11. Возвращаемся через кнопку "Назад"
        onView(allOf(withId(R.id.about_back_image_button),
                childAtPosition(
                        allOf(withId(R.id.container_custom_app_bar_include_on_fragment_about),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0)),
                        1),
                isDisplayed())).perform(click());

        // 12. Проверяем возврат на главный экран
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-NAV-02: Проверка логики активности пунктов меню в разделе "News"
    @Test
    public void testMenuLogicInNewsSection() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();

        // 3. Переходим в раздел News
        navigationDrawer.openMenu().clickNewsMenuItem();

        // 4. Проверяем экран News
        mainPage.checkNewsScreenIsDisplayed();

        // 5. Открываем боковое меню
        navigationDrawer.openMenu().checkMenuIsDisplayed();

        // 6. Проверяем пункты меню
        navigationDrawer.checkMainMenuItemIsDisplayed();
        navigationDrawer.checkNewsMenuItemIsDisplayed();
        navigationDrawer.checkAboutMenuItemIsDisplayed();

        // 7. Возвращаемся в Main
        navigationDrawer.clickMainMenuItem();

        // 8. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-NAV-03: Навигация через верхнюю панель
    @Test
    public void testNavigationThroughTopPanel() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();

        // 3. Переходим в раздел Quotes
        mainPage.clickQuotesButton();

        // 4. Проверяем экран Quotes
        mainPage.checkQuotesScreenIsDisplayed();

        // 5. Возвращаемся через боковое меню
        navigationDrawer.openMenu().clickMainMenuItem();

        // 6. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();

        // 7. Выполняем выход
        mainPage.logout();

        // 8. Проверяем экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();
    }

    // TC-NAV-04: Проверка логики активности пунктов бокового меню в разделе "Quotes"
    @Test
    public void testMenuLogicInQuotesSection() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();

        // 3. Переходим в раздел Quotes
        mainPage.clickQuotesButton();

        // 4. Проверяем экран Quotes
        mainPage.checkQuotesScreenIsDisplayed();

        // 5. Открываем боковое меню
        navigationDrawer.openMenu().checkMenuIsDisplayed();

        // 6. Проверяем пункты меню
        navigationDrawer.checkMainMenuItemIsDisplayed();
        navigationDrawer.checkNewsMenuItemIsDisplayed();
        navigationDrawer.checkAboutMenuItemIsDisplayed();

        // 7. Возвращаемся в Main
        navigationDrawer.clickMainMenuItem();

        // 8. Проверяем главный экран
        mainPage.checkMainScreenIsDisplayed();
    }

    // Вспомогательный метод для поиска дочернего элемента по позиции в иерархии View
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}