package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/kata";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
            System.out.println("✅ Драйвер MySQL успешно загружен");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ Не удалось загрузить драйвер MySQL: " + e.getMessage(), e);
        }
    }

    private Util() {
        // Приватный конструктор для утильного класса
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // Отключаем авто-коммит
            System.out.println("✅ Соединение с MySQL установлено успешно!");
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Ошибка подключения к MySQL: " + e.getMessage());
            throw e; // Пробрасываем исключение дальше
        }
    }
}