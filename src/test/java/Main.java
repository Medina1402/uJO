import uJo.database.Database;
import models.UserController;
import models.UserDto;

public class Main {
    public static void main(String[] args) {
        Database.DATABASE = "uJOTest";

        UserController userController = new UserController();
        UserDto userModel = userController.create("Yellow");
        System.out.println(">> Created: " + userModel);

        userModel = userController.update(userModel.getId(), "username", "Purple");
        System.out.println(">> Updated: " + userModel);
    }
}
