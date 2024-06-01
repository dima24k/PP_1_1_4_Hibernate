package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        userService.saveUser("Dima", "Timoshenko", (byte) 22);
        userService.saveUser("Didier", "Drogba", (byte) 46);
        userService.saveUser("John", "Terry", (byte) 43);
        userService.saveUser("Frank", "Lampard", (byte) 45);

        for (User user : userService.getAllUsers() ) {
            System.out.println(user);
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
