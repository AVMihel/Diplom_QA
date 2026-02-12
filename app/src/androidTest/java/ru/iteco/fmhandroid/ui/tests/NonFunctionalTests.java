package ru.iteco.fmhandroid.ui.tests;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.qameta.allure.android.runners.AllureAndroidJUnit4;
import io.qameta.allure.kotlin.Allure;
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
        Allure.step("Настройка тестового окружения - переход на экран авторизации и портретная ориентация");
        setUpToAuthScreen();
        OrientationUtils.rotateToPortrait();
    }

    @After
    public void tearDown() {
        Allure.step("Очистка после теста - выход из системы и возврат в портретную ориентацию");
        tearDownToAuthScreen();
        OrientationUtils.rotateToPortrait();
    }

    @Test
    @DisplayName("Сохранение данных при повороте экрана")
    @Description("TC-AUTH-12: Проверка сохранения данных в полях при повороте экрана")
    @Story("Данные в полях должны сохраняться при смене ориентации")
    @Ignore("Тест временно отключен: Приложение падает при повороте на экране авторизации")
    public void testDataPreservationOnScreenRotation() {
        Allure.step("Шаг 1: Проверка отображения экрана авторизации");
        authPage.checkAuthorizationScreenIsDisplayed();

        String testLogin = TestData.VALID_LOGIN;
        String testPassword = TestData.VALID_PASSWORD;

        Allure.step("Шаг 2: Ввод логина: " + testLogin);
        authPage.enterLogin(testLogin);

        Allure.step("Шаг 3: Ввод пароля: " + testPassword);
        authPage.enterPassword(testPassword);

        Allure.step("Шаг 4: Получение значения логина до поворота");
        String loginBefore = authPage.getLoginText();

        Allure.step("Шаг 5: Поворот экрана в ландшафтную ориентацию");
        OrientationUtils.rotateToLandscape();

        Allure.step("Шаг 6: Получение значения логина после поворота");
        String loginAfter = authPage.getLoginText();

        Allure.step("Шаг 7: Поворот экрана обратно в портретную ориентацию");
        OrientationUtils.rotateToPortrait();

        Allure.step("Шаг 8: Проверка отображения экрана авторизации");
        authPage.isAuthorizationScreenDisplayed();

        Allure.step("Шаг 9: Проверка сохранения данных в поле логина");
        assertTrue("BUG: Login data should be preserved when screen orientation changes",
                !loginAfter.isEmpty() && loginBefore.equals(loginAfter));
    }

    @Test
    @DisplayName("Адаптивность верстки при повороте экрана")
    @Description("TC-NF-02: Проверка адаптивности интерфейса при смене ориентации")
    @Story("Интерфейс должен адаптироваться к ландшафтной ориентации")
    public void testLayoutAdaptabilityOnScreenRotation() {
        Allure.step("Шаг 1: Авторизация и переход на главный экран");
        loginAndGoToMainScreen();

        Allure.step("Шаг 2: Переход на экран новостей через меню");
        navigationDrawer.openMenu().clickNewsMenuItem();

        Allure.step("Шаг 3: Проверка отображения экрана новостей");
        assertTrue("News screen should be displayed", mainPage.isNewsScreenDisplayed());

        Allure.step("Шаг 4: Проверка, что список новостей пуст");
        boolean isNewsListEmpty = newsPage.isNewsListEmpty();
        assertTrue("News list should be empty for this test. Bug appears only with empty news list.",
                isNewsListEmpty);

        Allure.step("Шаг 5: Поворот экрана в ландшафтную ориентацию");
        OrientationUtils.rotateToLandscape();

        Allure.step("Шаг 6: Проверка доступности кнопки Refresh в ландшафтной ориентации");
        boolean refreshAvailableInLandscape = mainPage.isRefreshButtonAccessibleInLandscape();
        assertTrue("BUG: Refresh button should be accessible in landscape orientation with empty news list",
                refreshAvailableInLandscape);

        Allure.step("Шаг 7: Поворот экрана обратно в портретную ориентацию");
        OrientationUtils.rotateToPortrait();

        Allure.step("Шаг 8: Проверка отображения экрана новостей после возврата в портретную ориентацию");
        assertTrue("News screen should be displayed after returning to portrait mode",
                mainPage.isNewsScreenDisplayed());

        Allure.step("Шаг 9: Возврат на главный экран через меню");
        navigationDrawer.openMenu().clickMainMenuItem();

        Allure.step("Шаг 10: Проверка отображения главного экрана");
        ensureOnMainScreen();
    }
}