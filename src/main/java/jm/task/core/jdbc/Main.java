package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        try {
            userService.createUsersTable();
            System.out.println("Таблица users создана");

            userService.saveUser("Петр", "Петров", (byte) 22);
            System.out.println("User c именем - Петр добавлен в базу данных");

            userService.saveUser("Иван", "Иванов", (byte) 33);
            System.out.println("User c именем - Иван добавлен в базу данных");

            userService.saveUser("Михаил", "Михайлов", (byte) 44);
            System.out.println("User c именем - Михаил добавлен в базу данных");

            userService.saveUser("Владимир", "Владимирович", (byte) 55);
            System.out.println("User c именем - Владимир добавлен в базу данных");

            // Получение всех пользователей
            System.out.println("\nВсе пользователи:");
            userService.getAllUsers();

            // Очистка таблицы
            userService.cleanUsersTable();
            System.out.println("\nТаблица очищена");

            // Удаление таблицы
            userService.dropUsersTable();
            System.out.println("Таблица удалена");



        } catch (Exception e) {
            System.err.println("Unknown error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
