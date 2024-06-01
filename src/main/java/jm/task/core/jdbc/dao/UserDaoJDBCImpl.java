package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Connection connection;

    public UserDaoJDBCImpl() {
        connection = new Util().getConnection();
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id  BIGINT AUTO_INCREMENT PRIMARY KEY," +
                " username VARCHAR(50) NOT NULL," +
                " lastname VARCHAR(50) NOT NULL," +
                " age INT NOT NULL)";

        try (Statement statement = connection.createStatement() ) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";

        try (Statement statement = connection.createStatement() ) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO users (username, lastname, age) VALUES (?, ?, ?)";

        try(PreparedStatement ps = connection.prepareStatement(sql) ) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setInt(3, age);

            System.out.println("User с именем — " + name + " добавлен в базу данных");

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql) ){
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try(Statement stmt = connection.createStatement() ) {
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next() ) {
                User user = new User();
                user.setId(rs.getLong("id") );
                user.setName(rs.getString("username") );
                user.setLastName(rs.getString("lastname") );
                user.setAge(rs.getByte("age") );

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE users";

        try (Statement statement = connection.createStatement() ) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
