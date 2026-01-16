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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;

// Тестовый класс для проверки навигации в приложении
public class NavigationTest extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();

    @Before
    public void setUpTest() {
        performLoginAndGoToMainScreen();
    }

    @After
    public void tearDownTest() {
        try {
            if (mainPage.isMainScreenDisplayed()) {
                mainPage.forceLogout();
                authPage.checkAuthorizationScreenIsDisplayed();
            }
        } catch (Exception e) {
        }
    }

    // TC-NAV-01: Основная навигация через боковое меню с главного экрана
    @Test
    public void testMainNavigationThroughSideMenu() {
        // Проверяем, что находимся на главном экране (после авторизации)
        mainPage.checkMainScreenIsDisplayed();

        // 1. Открываем боковое меню и проверяем все пункты
        navigationDrawer.openMenu();
        navigationDrawer.checkMainMenuItemIsDisplayed();
        navigationDrawer.checkNewsMenuItemIsDisplayed();
        navigationDrawer.checkAboutMenuItemIsDisplayed();

        // 2. Переходим в раздел News и проверяем переход
        navigationDrawer.clickNewsMenuItem();
        mainPage.checkNewsScreenIsDisplayed();

        // 3. Возвращаемся в Main через меню
        navigationDrawer.openMenu().clickMainMenuItem();
        mainPage.checkMainScreenIsDisplayed();

        // 4. Переходим в раздел About и проверяем переход
        navigationDrawer.openMenu().clickAboutMenuItem();
        mainPage.checkAboutScreenIsDisplayed();

        // 5. Возвращаемся на главный экран через кнопку "Назад"
        onView(allOf(withId(R.id.about_back_image_button),
                childAtPosition(
                        allOf(withId(R.id.container_custom_app_bar_include_on_fragment_about),
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0)),
                        1),
                isDisplayed())).perform(click());

        // Проверяем возврат на главный экран
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-NAV-02: Проверка логики активности пунктов меню в разделе "News"
    @Test
    public void testMenuLogicInNewsSection() {
        // Проверяем, что находимся на главном экране
        mainPage.checkMainScreenIsDisplayed();

        // 1. Переходим в раздел News через боковое меню
        navigationDrawer.openMenu().clickNewsMenuItem();
        mainPage.checkNewsScreenIsDisplayed();

        // 2. Открываем боковое меню и проверяем состояние пунктов
        navigationDrawer.openMenu().checkMenuIsDisplayed();
        navigationDrawer.checkMainMenuItemIsDisplayed();
        navigationDrawer.checkNewsMenuItemIsDisplayed();
        navigationDrawer.checkAboutMenuItemIsDisplayed();

        // 3. Возвращаемся в Main через меню
        navigationDrawer.clickMainMenuItem();
        mainPage.checkMainScreenIsDisplayed();
    }

    // TC-NAV-03: Навигация через верхнюю панель
    @Test
    public void testNavigationThroughTopPanel() {
        // Проверяем, что находимся на главном экране
        mainPage.checkMainScreenIsDisplayed();

        // 1. Переходим в раздел Quotes через кнопку на верхней панели
        mainPage.clickQuotesButton();
        mainPage.verifyQuotesScreenOpened(); // ← ОБНОВЛЕНО

        // 2. Возвращаемся на главный экран через боковое меню
        navigationDrawer.openMenu().clickMainMenuItem();
        mainPage.checkMainScreenIsDisplayed();

        // 3. Выполняем выход из системы через верхнюю панель
        mainPage.logout();

        // Проверяем, что вернулись на экран авторизации
        authPage.checkAuthorizationScreenIsDisplayed();
    }

    // TC-NAV-04: Проверка логики активности пунктов бокового меню в разделе "Quotes"
    @Test
    public void testMenuLogicInQuotesSection() {
        // Проверяем, что находимся на главном экране
        mainPage.checkMainScreenIsDisplayed();

        // 1. Переходим в раздел Quotes через верхнюю панель
        mainPage.clickQuotesButton();
        mainPage.verifyQuotesScreenOpened(); // ← ОБНОВЛЕНО

        // 2. Открываем боковое меню и проверяем состояние пунктов
        navigationDrawer.openMenu().checkMenuIsDisplayed();
        navigationDrawer.checkMainMenuItemIsDisplayed();
        navigationDrawer.checkNewsMenuItemIsDisplayed();
        navigationDrawer.checkAboutMenuItemIsDisplayed();

        // 3. Возвращаемся в Main через меню
        navigationDrawer.clickMainMenuItem();
        mainPage.checkMainScreenIsDisplayed();
    }

    // Вспомогательный метод для поиска дочерних элементов в иерархии View
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