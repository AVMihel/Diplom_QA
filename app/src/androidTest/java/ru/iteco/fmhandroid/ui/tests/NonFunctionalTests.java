package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Description;
import io.qameta.allure.kotlin.Epic;
import io.qameta.allure.kotlin.Feature;
import io.qameta.allure.kotlin.Story;
import io.qameta.allure.kotlin.junit4.DisplayName;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.NavigationDrawerPage;
import ru.iteco.fmhandroid.ui.pages.NewsPage;
import ru.iteco.fmhandroid.ui.utils.OrientationUtils;

@RunWith(AllureAndroidJUnit4.class)
@Epic("Нефункциональные тесты")
@Feature("Тестирование поведения при изменении ориентации")
@DisplayName("Тесты адаптивности и сохранения данных")
public class NonFunctionalTests extends BaseTest {

    private final NavigationDrawerPage navigationDrawer = new NavigationDrawerPage();
    private final NewsPage newsPage = new NewsPage();

    @Before
    public void setUp() {
        setUpToAuthScreen();
        OrientationUtils.rotateToPortrait();
    }

    @After
    public void tearDown() {
        tearDownToAuthScreen();
        OrientationUtils.rotateToPortrait();
    }

    @Test
    @DisplayName("Сохранение данных при повороте экрана")
    @Description("TC-AUTH-12: Проверка сохранения данных в полях при повороте экрана")
    @Story("Данные в полях должны сохраняться при смене ориентации")
    @Ignore("Тест временно отключен: Приложение падает при повороте на экране авторизации")
    public void testDataPreservationOnScreenRotation() {
        authPage.checkAuthorizationScreenIsDisplayed();

        String testLogin = TestData.VALID_LOGIN;
        String testPassword = TestData.VALID_PASSWORD;
        authPage.enterLogin(testLogin);
        authPage.enterPassword(testPassword);

        String loginBefore = authPage.getLoginText();
        OrientationUtils.rotateToLandscape();

        String loginAfter = authPage.getLoginText();
        OrientationUtils.rotateToPortrait();
        authPage.isAuthorizationScreenDisplayed();

        assertTrue("BUG: Login data should be preserved when screen orientation changes",
                !loginAfter.isEmpty() && loginBefore.equals(loginAfter));
    }

    @Test
    @DisplayName("Адаптивность верстки при повороте экрана")
    @Description("TC-NF-02: Проверка адаптивности интерфейса при смене ориентации")
    @Story("Интерфейс должен адаптироваться к ландшафтной ориентации")
    public void testLayoutAdaptabilityOnScreenRotation() {
        loginAndGoToMainScreen();
        navigationDrawer.openMenu().clickNewsMenuItem();
        mainPage.isNewsScreenDisplayed();

        boolean isNewsListEmpty = newsPage.isNewsListEmpty();
        assertTrue("News list should be empty for this test. Bug appears only with empty news list.", isNewsListEmpty);

        mainPage.isRefreshButtonAccessibleInLandscape();
        OrientationUtils.rotateToLandscape();
        mainPage.isNewsScreenDisplayed();

        assertTrue("BUG: Refresh button should be accessible in landscape orientation with empty news list",
                mainPage.isRefreshButtonAccessibleInLandscape());

        OrientationUtils.rotateToPortrait();
        mainPage.isNewsScreenDisplayed();
        navigationDrawer.openMenu().clickMainMenuItem();
        ensureOnMainScreen();
    }
}