package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class UserDaoJDBCImpl implements UserDao {


    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {// добаление таблицы 4 колонок
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = Util.getConnection();
            String sql = "CREATE TABLE IF NOT EXISTS user (" +
                    "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL," +
                    "lastName VARCHAR(255) NOT NULL," +
                    "age TINYINT NOT NULL" +
                    ")";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("Таблица user создана или уже существует");
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println(" Транзакция откачена: " + e.getMessage());
                } catch (SQLException rollbackExeprion) {
                    System.err.println(" Ошибка при откате: " + rollbackExeprion.getMessage());
                }
            }
        } finally {
            closeResources(null, preparedStatement, connection);
        }
    }

    public void dropUsersTable() { // удаление таблицы
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = Util.getConnection();
            String sql = "DROP TABLE IF EXISTS user";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            connection.commit();
            out.println("Таблица user удалена или не существовала");
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println(" Транзакция откачена: " + e.getMessage());
                } catch (SQLException rollbackExeprion) {
                    System.err.println(" Ошибка при откате: " + rollbackExeprion.getMessage());
                }
            }
        } finally {
            closeResources(null, preparedStatement, connection); //!!!!!!!!!!!!!!этот метод в конце не проеби его !!!!!!!! закрытие ресурсов
        }
    }

    public void saveUser(String name, String lastName, byte age) {//ДОБАВЛЕНИЕ в таблицу
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = Util.getConnection();
            String sql = "insert into user (name, lastName, age) values (?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("User с именем " + name + " добавлен в базу данных");

        } catch
        (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println(" Транзакция откачена: " + e.getMessage());
                } catch (SQLException rollbackExeprion) {
                    System.err.println(" Ошибка при откате: " + rollbackExeprion.getMessage());
                }
            }

        } finally {
            closeResources(null, preparedStatement, connection);
        }
    }


    public void removeUserById(long id) { // удаление user id
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = Util.getConnection();
            String sql = "DELETE FROM user WHERE id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("User с id " + id + " удален из базы данных");

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println(" Транзакция откачена: " + e.getMessage());
                } catch (SQLException rollbackExeprion) {
                    System.err.println(" Ошибка при откате: " + rollbackExeprion.getMessage());

                }
            }
        } finally {
            closeResources(null, preparedStatement, connection);
        }

    }

    public List<User> getAllUsers() { // (select выбери) (*все столбцы) (from из) user!
        List<User> users = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Util.getConnection();
            String sql = "SELECT * FROM user";

            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }

            connection.commit();

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println(" Транзакция откачена: " + e.getMessage());// оставлю тут
                } catch (SQLException rollbackExeprion) {
                    System.err.println(" Ошибка при откате: " + rollbackExeprion.getMessage());// оставлю тут

                }
            }
            System.err.println("Ошибка при получении пользователей: " + e.getMessage());

        } finally {
            closeResources(resultSet, preparedStatement, connection);
        }
        return users;
    }

    public void cleanUsersTable() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;


        try {
            connection = Util.getConnection();
            String sql = "TRUNCATE TABLE user";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            connection.commit();
            System.out.println("Таблица users очищена");

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println(" Транзакция откачена: " + e.getMessage());
                } catch (SQLException rollbackExeprion) {
                    System.err.println(" Ошибка при откате: " + rollbackExeprion.getMessage());

                }
            }
        } finally {
            closeResources(null, preparedStatement, connection);
        }
    }
// тут можно еще добавить откат незавершенных транзакций но я думаю избыточно будет
    private void closeResources(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null&& !connection.isClosed()) { // закрытие с проверкой
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
        }

    }
}
