package ru.iteco.fmhandroid.ui.core;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

            // Даты
            public static final String DEFAULT_DATE = "01.01.2025";
        }

        // Тестовые данные для валидации
        public static class Validation {

            // Тесты длины заголовка
            public static final String LENGTH_TEST_TITLE_PREFIX = "Тест прошедшей даты_";
            public static final String LENGTH_TEST_DESCRIPTION = "Описание для теста прошедшей даты";

            // Тесты категорий
            public static final String MANUAL_CATEGORY_TEST_TITLE_PREFIX = "Тест ручного ввода категории_";
            public static final String MANUAL_CATEGORY_TEST_DESCRIPTION = "Описание для теста ручного ввода категории";

            // Тесты спецсимволов
            public static final String SPECIAL_CHARS_TEST_DESCRIPTION = "Описание для теста спецсимволов";

            // Тесты пробелов
            public static final String SPACES_TEST_DESCRIPTION = "Описание для теста пробелов";

            // Тесты многострочного описания
            public static final String MULTILINE_TEST_TITLE_PREFIX = "Тест многострочного описания_";

            // Тесты отмены
            public static final String CANCEL_TEST_TITLE_PREFIX = "Тест отмены создания_";
        }

        // Общие тестовые данные
        public static class Common {
            public static final String DATE_TEST_TITLE = "Тест даты_";
            public static final String DATE_TEST_DESCRIPTION = "Описание для теста дат";
        }

        // Объект новости для E2E теста создания
        public static final NewsItem E2E_NEWS = new NewsItem(
                "Автотест: Важное объявление",
                CATEGORY_ANNOUNCEMENT,
                getFutureDate(1),
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
    }

    // Объект новости
    public static class NewsItem {
        public final String title;
        public final String category;
        public final String date;
        public final String time;
        public final String description;
        public final boolean active;

        // Конструктор объекта новости
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
        // Данные для теста длины заголовка
        public static String getVeryLongTitle() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                sb.append("ОченьДлинныйТекст");
            }
            return sb.toString();
        }

        // Данные для теста спецсимволов
        public static final String SPECIAL_CHARS_TITLE = "!@#$%^&*";

        // Данные для теста пробелов
        public static final String SPACES_ONLY_TITLE = "   ";

        // Данные для теста многострочного описания
        public static final String MULTILINE_DESCRIPTION =
                "Первая строка описания.\n" +
                        "Вторая строка с переносом.\n" +
                        "Третья строка.\n" +
                        "Еще одна строка для тестирования.";

        // Невалидная категория для теста ручного ввода
        public static final String INVALID_CATEGORY = "TestInvalidCategory";

        // Данные для теста прошедшей даты
        public static String getPastDateString() {
            LocalDate pastDate = LocalDate.now().minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return pastDate.format(formatter);
        }
    }
}
