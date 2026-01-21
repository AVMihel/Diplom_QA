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

        // Объект новости для end-to-end теста
        public static final NewsItem E2E_NEWS = new NewsItem(
                "Автотест: Важное объявление",
                CATEGORY_ANNOUNCEMENT,
                getFutureDate(1),
                "10:00",
                "Это автоматически созданная новость для тестирования функционала.",
                true
        );

        // Объекты новостей для теста сортировки (от самой старой к самой новой)
        public static final NewsItem SORT_NEWS_1 = new NewsItem(
                "Сортировка: Самая старая",
                CATEGORY_BIRTHDAY,
                getFutureDate(3),
                "09:00",
                "Новость для теста сортировки - должна быть первой при сортировке по возрастанию.",
                true
        );

        public static final NewsItem SORT_NEWS_2 = new NewsItem(
                "Сортировка: Средняя",
                CATEGORY_HOLIDAY,
                getFutureDate(5),
                "12:00",
                "Новость для теста сортировки - средняя по дате.",
                true
        );

        public static final NewsItem SORT_NEWS_3 = new NewsItem(
                "Сортировка: Самая новая",
                CATEGORY_SALARY,
                getFutureDate(7),
                "15:00",
                "Новость для теста сортировки - должна быть последней при сортировке по возрастанию.",
                true
        );

        // Объекты новостей для теста фильтрации по статусу
        public static final NewsItem ACTIVE_NEWS = new NewsItem(
                "Фильтр: Активная новость",
                CATEGORY_TRADE_UNION,
                getFutureDate(2),
                "11:00",
                "Эта новость активна и должна отображаться при фильтрации по Active.",
                true
        );

        public static final NewsItem NOT_ACTIVE_NEWS = new NewsItem(
                "Фильтр: Неактивная новость",
                CATEGORY_MASSAGE,
                getFutureDate(4),
                "14:00",
                "Эта новость неактивна и должна отображаться при фильтрации по Not Active.",
                false
        );

        // Получение даты в будущем в формате dd.MM.yyyy
        private static String getFutureDate(int daysToAdd) {
            LocalDate futureDate = LocalDate.now().plusDays(daysToAdd);
            return String.format("%02d.%02d.%d",
                    futureDate.getDayOfMonth(),
                    futureDate.getMonthValue(),
                    futureDate.getYear());
        }

        // Получение текущей метки времени
        private static String getCurrentTimestamp() {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                    "_" + System.currentTimeMillis() % 1000;
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
}
