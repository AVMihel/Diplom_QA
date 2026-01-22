package ru.iteco.fmhandroid.ui.core;

import static ru.iteco.fmhandroid.ui.core.TestData.News.CATEGORY_BIRTHDAY;

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

    // Вложенный класс для данных тестирования новостей
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

        // Объект новости для end-to-end теста создания
        public static final NewsItem E2E_NEWS = new NewsItem(
                "Автотест: Важное объявление",
                CATEGORY_ANNOUNCEMENT,
                getFutureDate(1),
                "10:00",
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
    }

    // Класс, представляющий объект новости
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

    // Класс для тестовых данных создания новостей (Новое - добавим только то, чего нет)
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

        // Данные для теста будущей даты (уже есть в News.getFutureDate, но оставим для ясности)
        public static String getFutureDateString(int days) {
            return News.getFutureDate(days);
        }
    }

    // Класс для тестовых данных редактирования новостей (Новое)
    public static class NewsEditing {
        // Префиксы для тестовых новостей
        public static final String TEST_PREFIX = "Тест_";
        public static final String EDIT_PREFIX = "Редактирование_";

        // Данные для изменения при редактировании
        public static final String EDITED_TITLE_SUFFIX = "_изменено";
        public static final String EDITED_DESCRIPTION = "Измененное описание новости";
        public static final String EDITED_CATEGORY = CATEGORY_BIRTHDAY; // Используем существующую константу
    }
}
