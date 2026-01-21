package ru.iteco.fmhandroid.ui.tests;

import org.junit.Test;

import ru.iteco.fmhandroid.R;
import ru.iteco.fmhandroid.ui.core.BaseTest;
import ru.iteco.fmhandroid.ui.core.TestData;
import ru.iteco.fmhandroid.ui.pages.ControlPanelPage;
import ru.iteco.fmhandroid.ui.pages.CreateEditNewsPage;
import ru.iteco.fmhandroid.ui.utils.WaitUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertTrue;

public class NewsCreationTest extends BaseTest {

    private final ControlPanelPage controlPanelPage = new ControlPanelPage();

    // TC-NEWS-CREATE-01: Полный E2E сценарий создания новости
    @Test
    public void testE2ENewsCreation() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        // 3. Используем данные из TestData
        TestData.NewsItem testNews = TestData.News.E2E_NEWS;

        try {
            // 4. Нажимаем "+" для создания новости
            onView(allOf(withId(R.id.add_news_image_view)))
                    .check(matches(isDisplayed()))
                    .perform(click());

            WaitUtils.waitForMillis(1000);

            // 5. Заполняем все обязательные поля
            CreateEditNewsPage createPage = new CreateEditNewsPage();

            // Категория
            onView(withId(R.id.news_item_category_text_auto_complete_text_view))
                    .perform(replaceText(testNews.category));
            WaitUtils.waitForMillis(500);

            // Заголовок
            onView(withId(R.id.news_item_title_text_input_edit_text))
                    .perform(replaceText(testNews.title));
            WaitUtils.waitForMillis(500);

            // Дата публикации
            createPage.selectCurrentDate();
            WaitUtils.waitForMillis(500);

            // Время публикации
            createPage.selectCurrentTime();
            WaitUtils.waitForMillis(500);

            // Описание
            onView(withId(R.id.news_item_description_text_input_edit_text))
                    .perform(replaceText(testNews.description));
            WaitUtils.waitForMillis(500);

            // 6. Нажимаем "SAVE"
            onView(withId(R.id.save_button))
                    .perform(click());

            // 7. Ждем возврата в Control Panel
            controlPanelPage.checkControlPanelIsDisplayed();
            WaitUtils.waitForMillis(2000);

            // 8. Проверяем, что новость появилась в списке "Control Panel"
            boolean newsCreated = controlPanelPage.findNewsByTitleWithScroll(testNews.title);
            assertTrue("Е2Е: Новость должна быть создана и отображаться в списке Control Panel", newsCreated);

            // 9. Очистка: удаляем созданную новость
            if (newsCreated) {
                try {
                    controlPanelPage.deleteCreatedNewsByExactTitle(testNews.title);

                    // Проверяем удаление
                    WaitUtils.waitForMillis(2000);
                    boolean isDeleted = controlPanelPage.isNewsDeleted(testNews.title, 5000);
                    assertTrue("Созданная новость должна быть удалена после теста", isDeleted);
                } catch (Exception deleteEx) {
                    // Игнорируем ошибки при очистке
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("TC-NEWS-CREATE-01: Е2Е тест создания новости не прошел: " + e.getMessage(), e);
        }
    }

    // TC-NEWS-CP-05: Удаление новости
    @Test
    public void testE2ENewsDeletion() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        try {
            // 3. Создаем тестовую новость для удаления
            String testTitle = controlPanelPage.createNewsForDeletionTest();

            // 4. Проверяем создание
            boolean isCreated = controlPanelPage.findNewsByTitleWithScroll(testTitle);
            assertTrue("TC-NEWS-CP-05: Тестовая новость должна быть создана и найдена в списке", isCreated);

            // 5. Удаляем созданную новость
            controlPanelPage.deleteCreatedNewsByExactTitle(testTitle);

            // 6. Проверяем удаление
            WaitUtils.waitForMillis(2000);
            boolean isDeleted = controlPanelPage.isNewsDeleted(testTitle, 5000);
            assertTrue("TC-NEWS-CP-05: Созданная новость должна быть удалена и не найдена в списке", isDeleted);

        } catch (Exception e) {
            throw new RuntimeException("TC-NEWS-CP-05: Е2Е тест удаления новости не прошел: " + e.getMessage(), e);
        }
    }

    // TC-NEWS-EDIT-01: Предзаполнение полей при редактировании
    @Test
    public void testE2ENewsEditing() {
        // 1. Гарантируем, что мы на главном экране
        ensureOnMainScreen();

        // 2. Переходим в Control Panel
        controlPanelPage.navigateToControlPanel();

        try {
            // 3. Создаем тестовую новость для редактирования
            String originalTitle = "Оригинальная новость_" + System.currentTimeMillis();
            String originalCategory = "Объявление";
            String originalDate = "01.01.2025";
            String originalTime = "12:00";
            String originalDescription = "Оригинальное описание новости";

            String createdTitle = controlPanelPage.createTestNews(
                    originalTitle,
                    originalCategory,
                    originalDate,
                    originalTime,
                    originalDescription
            );

            // 4. Проверяем создание
            boolean isCreated = controlPanelPage.findNewsByTitleWithScroll(createdTitle);
            assertTrue("TC-NEWS-EDIT-01: Тестовая новость должна быть создана для редактирования", isCreated);

            // 5. Переходим к редактированию новости
            CreateEditNewsPage editPage = controlPanelPage.navigateToEditNews();
            editPage.checkEditScreenIsDisplayed();

            // 6. Проверяем предзаполнение полей
            editPage.checkTitleFieldDisplayed();
            editPage.checkCategoryFieldDisplayed();

            // 7. Изменяем заголовок
            String updatedTitle = "Обновленная новость_" + System.currentTimeMillis();
            editPage.fillTitle(updatedTitle);
            WaitUtils.waitForMillis(500);

            // 8. Сохраняем изменения
            editPage.clickSaveButton();

            // 9. Ждем возврата в Control Panel
            controlPanelPage.checkControlPanelIsDisplayed();
            WaitUtils.waitForMillis(2000);

            // 10. Проверяем обновление
            boolean isUpdated = controlPanelPage.findNewsByTitleWithScroll(updatedTitle);
            assertTrue("TC-NEWS-EDIT-01: Новость должна быть обновлена с новым заголовком", isUpdated);

            // 11. Проверяем что старая версия новости не отображается
            boolean oldVersionExists = controlPanelPage.isNewsDisplayed(originalTitle);
            assertTrue("TC-NEWS-EDIT-01: Старая версия новости не должна отображаться", !oldVersionExists);

            // 12. Очистка: удаляем обновленную новость
            try {
                controlPanelPage.deleteCreatedNewsByExactTitle(updatedTitle);

                // Проверяем удаление
                WaitUtils.waitForMillis(2000);
                boolean isDeleted = controlPanelPage.isNewsDeleted(updatedTitle, 5000);
                assertTrue("Обновленная новость должна быть удалена после теста", isDeleted);
            } catch (Exception deleteEx) {
                // Игнорируем ошибки при очистке
            }

        } catch (Exception e) {
            throw new RuntimeException("TC-NEWS-EDIT-01: Е2Е тест редактирования новости не прошел: " + e.getMessage(), e);
        }
    }
}
