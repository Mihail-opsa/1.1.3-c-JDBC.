package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/kata";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";


    private Util() {
    }

    public static Connection getConnection() {

        try {
            // Загрузка драйвера MySQL
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            connection.setAutoCommit(false); // откл авто комит !

            System.out.println("✅ Соединение с MySQL установлено успешно!");
            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Драйвер MySQL не найден: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Ошибка подключения к MySQL: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    public static void commitTransaction(Connection connection) {
        try {
            if (connection != null) {
                connection.commit();
                System.out.println("✅ Транзакция успешно закоммичена!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при коммите транзакции: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void rollbackTransaction(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                System.out.println("✅ Транзакция откачена!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Ошибка при откате транзакции: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Метод для закрытия соединения
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.getAutoCommit() && !connection.isClosed()) {
                    connection.rollback();
                }
                connection.close();
                System.out.println("✅ Соединение с MySQL закрыто!");
            } catch (SQLException e) {
                System.err.println("❌ Ошибка при закрытии соединения: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}

