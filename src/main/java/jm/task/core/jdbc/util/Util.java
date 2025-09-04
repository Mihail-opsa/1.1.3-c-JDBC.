package jm.task.core.jdbc.util;

import org.hibernate.cfg.Configuration;
import jm.task.core.jdbc.model.User;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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
    //hiber методы
   private static SessionFactory buildSessionFactory() {

        try {
            Configuration configuration = new Configuration()
                    .setProperty("hibernate.connection.driver_class", DRIVER)
                    .setProperty("hibernate.connection.url", URL)
                    .setProperty("hibernate.connection.username", USER)
                    .setProperty("hibernate.connection.password", PASSWORD)
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect") //  с какой СУБД работаем
                    .setProperty("hibernate.current_session_context_class", "thread")
                    .setProperty("hibernate.show_sql", "true") // вывод запросов sql в консоль
                    .setProperty("hibernate.format_sql", "true") // для лучшей читаемости
                    .setProperty("hibernate.hbm2ddl.auto", "update"); //Автоматическое управление схемой базы данных.

            // класс сущностей
            configuration.addAnnotatedClass(User.class);



            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());




            SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
            System.out.println("Hibernate session успешно создана !");

            return sessionFactory;

        } catch (Exception e) {
            System.err.println(" Ошибка создания SessionFactory: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }
    private static final SessionFactory sessionFactory = buildSessionFactory();


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("✅ Hibernate SessionFactory закрыта");
        }
    }
}
