package ru.iteco.fmhandroid.ui.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TestData {

    // Константы для тестов авторизации
    public static final String VALID_LOGIN = "login2";
    public static final String VALID_PASSWORD = "password2";
    public static final String INVALID_LOGIN = "invalid_login";
    public static final String INVALID_PASSWORD = "invalid_password";
    public static final String UPPERCASE_LOGIN = "LOGIN2";
    public static final String UPPERCASE_PASSWORD = "PASSWORD2";
    public static final String LOGIN_WITH_SPACES = "log in2";
    public static final String LOGIN_ONLY_SPACES = "   ";

    // Класс для данных тестирования новостей
    public static class News {
        // Константы категорий новостей
        public static final String CATEGORY_ANNOUNCEMENT = "Объявление";
        public static final String CATEGORY_BIRTHDAY = "День рождения";
        public static final String CATEGORY_HOLIDAY = "Праздник";
        public static final String CATEGORY_SALARY = "Зарплата";
        public static final String CATEGORY_TRADE_UNION = "Профсоюз";
        public static final String CATEGORY_MASSAGE = "Массаж";
        public static final String CATEGORY_THANKS = "Благодарность";
        public static final String CATEGORY_HELP_NEEDED = "Нужна помощь";

        // Общие константы времени
        public static final String DEFAULT_TIME = "12:00";
        public static final String E2E_TIME = "10:00";

        // Тестовые данные для редактирования новостей
        public static class Editing {
            public static final String UPDATED_TITLE_PREFIX = "Обновленный заголовок_";
            public static final String ORIGINAL_DESCRIPTION = "Оригинальное описание для тестов редактирования";
            public static final String UPDATED_DESCRIPTION_PREFIX = "Обновленное описание с изменениями ";
            public static final String TITLE_PREFIX_FOR_CANCEL = "Измененный заголовок который не должен сохраниться_";
            public static final String EDITING_ORIGINAL_DESCRIPTION = "Оригинальное описание новости";
        }

        // Тестовые данные для E2E тестов
        public static class E2E {
            public static final String ORIGINAL_TITLE_PREFIX = "Оригинальная новость_";
            public static final String UPDATED_TITLE_PREFIX = "Обновленная новость_";
            public static final String TEST_TITLE_PREFIX = "TestNews_";
            public static final String TEST_DELETION_DESCRIPTION = "Automated test news for deletion";
            public static final String DATE_TEST_TITLE_PREFIX = "ТестДаты_";
            public static final String DATE_TEST_DESCRIPTION_PREFIX = "Новость для теста дат: ";
        }

        // Тестовые данные для валидации
        public static class Validation {
            public static final String LENGTH_TEST_TITLE_PREFIX = "Тест прошедшей даты_";
            public static final String LENGTH_TEST_DESCRIPTION = "Описание для теста прошедшей даты";
            public static final String MANUAL_CATEGORY_TEST_TITLE_PREFIX = "Тест ручного ввода категории_";
            public static final String MANUAL_CATEGORY_TEST_DESCRIPTION = "Описание для теста ручного ввода категории";
            public static final String SPECIAL_CHARS_TEST_DESCRIPTION = "Описание для теста спецсимволов";
            public static final String SPACES_TEST_DESCRIPTION = "Описание для теста пробелов";
            public static final String MULTILINE_TEST_TITLE_PREFIX = "Тест многострочного описания_";
        }

        // Объект новости для E2E теста создания
        public static final NewsItem E2E_NEWS = new NewsItem(
                "Автотест: Важное объявление",
                CATEGORY_ANNOUNCEMENT,
                null,
                E2E_TIME,
                "Это автоматически созданная новость для тестирования функционала.",
                true
        );

        // Получение даты в будущем в формате dd.MM.yyyy
        public static String getFutureDate(int daysToAdd) {
            LocalDate futureDate = LocalDate.now().plusDays(daysToAdd);
            return String.format("%02d.%02d.%d",
                    futureDate.getDayOfMonth(),
                    futureDate.getMonthValue(),
                    futureDate.getYear());
        }

        // Получение времени (для обратной совместимости)
        public static String getTime() {
            return DEFAULT_TIME;
        }

        // Генерация уникального заголовка с указанным префиксом
        public static String generateUniqueTitle(String prefix) {
            return prefix + System.currentTimeMillis() + "_" + new Random().nextInt(1000);
        }

        // Получение даты на указанное количество дней от текущей
        public static String getDateForDaysFromNow(int days) {
            LocalDate date = LocalDate.now().plusDays(days);
            return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

        // Получение текущей даты в формате dd.MM.yyyy
        public static String getCurrentDate() {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

        // Утилиты для тестов сортировки
        public static class Sorting {
            public static final String TODAY_NEWS_PREFIX = "СортСегодня";
            public static final String TOMORROW_NEWS_PREFIX = "СортЗавтра";
            public static final String TODAY_TIME = "20:00";
            public static final String TOMORROW_TIME = "09:00";

            public static String getTodayDate() {
                return getCurrentDate();
            }

            public static String getTomorrowDate() {
                return getDateForDaysFromNow(1);
            }

            public static String generateUniqueTitle(String prefix) {
                return News.generateUniqueTitle(prefix);
            }
        }
    }

    // Объект новости
    public static class NewsItem {
        public final String title;
        public final String category;
        public final String date;
        public final String time;
        public final String description;
        public final boolean active;

        public NewsItem(String title, String category, String date,
                        String time, String description, boolean active) {
            this.title = title;
            this.category = category;
            this.date = date;
            this.time = time;
            this.description = description;
            this.active = active;
        }
    }

    // Класс для тестовых данных создания новостей
    public static class NewsCreation {
        public static String getVeryLongTitle() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                sb.append("ОченьДлинныйТекст");
            }
            return sb.toString();
        }

        public static final String SPECIAL_CHARS_TITLE = "!@#$%^&*";
        public static final String SPACES_ONLY_TITLE = "   ";
        public static final String MULTILINE_DESCRIPTION =
                "Первая строка описания.\n" +
                        "Вторая строка с переносом.\n" +
                        "Третья строка.\n" +
                        "Еще одна строка для тестирования.";
        public static final String INVALID_CATEGORY = "TestInvalidCategory";
    }
}
