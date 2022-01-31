package models;

import uJo.decorator.type.Column;
import uJo.decorator.type.Table;
import uJo.decorator.typedata.TypeInt;
import uJo.decorator.typedata.TypeTimestamp;
import uJo.decorator.typedata.TypeVarchar;

@Table("users")
public class UserDto {
    @Column("id")
    @TypeInt(primaryKey = true, autoincrement = true)
    Integer id;

    @Column("username")
    @TypeVarchar(required = true)
    String username;

    @Column("create_at")
    @TypeTimestamp(required = true, value = "current_timestamp")
    String create_at;

    @Column("update_at")
    @TypeTimestamp(required = true, value = "current_timestamp")
    String update_at;

    public Integer getId() {
        return id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", create_at='" + create_at + '\'' +
                ", update_at='" + update_at + '\'' +
                '}';
    }
}
