package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = new Util().getConnection();

    public UserDaoJDBCImpl() {}

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeQuery(String sql) {
        Util util = new Util();
        connection = util.getConnection();
        try (Statement statement = connection.createStatement() ) {
            connection.setAutoCommit(false);
            statement.execute(sql);
            connection.commit();
        } catch (SQLException e) {
            rollbackConnection();
        } finally {
            util.closeConnection();
        }
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id  BIGINT AUTO_INCREMENT PRIMARY KEY," +
                " username VARCHAR(50) NOT NULL," +
                " lastname VARCHAR(50) NOT NULL," +
                " age INT NOT NULL)";
        executeQuery(sql);
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        executeQuery(sql);
    }

    public void saveUser(String name, String lastName, byte age) {
        Util util = new Util();
        connection = util.getConnection();
        String sql = "INSERT INTO users (username, lastname, age) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setInt(3, age);
            System.out.println("User с именем — " + name + " добавлен в базу данных");
            ps.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollbackConnection();
        } finally {
            util.closeConnection();
        }
    }

    public void removeUserById(long id) {
        Util util = new Util();
        connection = util.getConnection();

        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            rollbackConnection();
        } finally {
            util.closeConnection();
        }
    }

    public List<User> getAllUsers() {
        Util util = new Util();
        connection = util.getConnection();

        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try(Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql) ) {
            connection.setAutoCommit(false);
            while(rs.next() ) {
                User user = new User();
                user.setId(rs.getLong("id") );
                user.setName(rs.getString("username") );
                user.setLastName(rs.getString("lastname") );
                user.setAge(rs.getByte("age") );
                users.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            rollbackConnection();
        } finally {
            util.closeConnection();
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";
        executeQuery(sql);
    }
}
