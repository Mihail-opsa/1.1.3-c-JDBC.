package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {


    public UserDaoJDBCImpl() {
    }

        public void createUsersTable () { // добаление таблицы 4 колонок
            String sql = "CREATE TABLE IF NOT EXISTS user (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL," +
                    "lastName VARCHAR(255) NOT NULL," +
                    "age TINYINT NOT NULL" +
                    ")";
            try (Connection connection = Util.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                System.out.println("Таблица user создана или уже существует");


            } catch (SQLException e) {
                System.err.println("Ошибка при создании таблицы: " + e.getMessage());
            }

        }


        public void dropUsersTable () { // удаление таблицы
            String sql = "DROP TABLE IF EXISTS user";
            try (Connection connection = Util.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                System.out.println("Таблица user удалена или не существовала ");
            } catch (SQLException e) {
                System.err.println("Ошибка при удалении таблицы" + e.getMessage());
            }

        }

        public void saveUser (String name, String lastName,byte age){//ДОБАВЛЕНИЕ в таблицу
            String sql = "insert into user (name, lastName, age) values (?, ?, ?)";

            try (Connection connection = Util.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);
                preparedStatement.executeUpdate();
                System.out.println("User с именем " + name + "добавлен в базу данных");
            } catch (SQLException e) {
                System.err.println("Ошибка при добавлении пользователя:" + e.getMessage());
            }
        }

        public void removeUserById ( long id){ // удаление user id
            String sql = "delete from user where id = ?";
            try (Connection connection = Util.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                System.out.println("User с id" + id + " удален из базы данных");
            } catch (SQLException e) {
                System.err.println("Ошибка удаления пользователя" + e.getMessage());
            }
        }

        public List<User> getAllUsers () { // (select выбери) (*все столбцы) (from из) user!
            List<User> users = new ArrayList<>();
            String sql = "SELECT * FROM user";

            try (Connection connection = Util.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {


                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setAge(resultSet.getByte("age"));
                    users.add(user);
                }
            } catch (SQLException e) {
                System.err.println("Ошибка при получении пользователей:" + e.getMessage());

            }
            return users;
        }

        public void cleanUsersTable () {
            String sql = "TRUNCATE TABLE user";
            try (Connection connection = Util.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql);
                System.out.println("Таблица users очищена");
            } catch (SQLException e) {
                System.err.println("Ошибка при очистке таблицы: " + e.getMessage());
            }

        }
    }
