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
    public static final String PASSWORD_ONLY_SPACES = "   ";
    public static final String LOGIN_WITH_LEADING_TRAILING_SPACES = " login2 ";

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

        // Тестовые данные для тестов сортировки
        public static class Sorting {
            public static final String TODAY_NEWS_PREFIX = "СортСегодня";
            public static final String TOMORROW_NEWS_PREFIX = "СортЗавтра";

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

        // Получение даты в будущем в формате dd.MM.yyyy
        public static String getFutureDate(int daysToAdd) {
            LocalDate futureDate = LocalDate.now().plusDays(daysToAdd);
            return String.format("%02d.%02d.%d",
                    futureDate.getDayOfMonth(),
                    futureDate.getMonthValue(),
                    futureDate.getYear());
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
    }
}
