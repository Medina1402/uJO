package models;

import uJo.database.Database;
import uJo.database.repository.Repository;

import java.sql.Timestamp;

public class UserController {
    private final Repository<UserDto> repository;

    public UserController() {
        Database.CreateTable(UserDto.class);
        repository = new Repository<>(UserDto.class);
    }

    public UserDto create(String username) {
        final UserDto userModel = new UserDto();
        userModel.username = username;
        return repository.create(userModel);
    }

    public UserDto update(Object id, String column, Object value) {
        //boolean status = repository.update(id, column, value);
        boolean status = repository.update(
                new String[]{column, "update_at"},
                new Object[]{value, new Timestamp(System.currentTimeMillis()).toString()},
                new String[]{"id"},
                new Object[]{id},
                new String[]{"="}
        );
        return (status) ?repository.findLast("id") :null;
    }
}
