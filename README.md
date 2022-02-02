# μJO
MicroJava ORM. Tool for small-scale projects or fast prototyping, it provides us with an abstraction layer that allows us not to use "sentences SQL", also one handler with the most common functions as: ***find, findAll, create, update, delete***, etc.

![License MIT](https://img.shields.io/github/license/Medina1402/uJO)
![Language Program](https://img.shields.io/badge/Java-v1.8.0-red?logo=java)
<a href="https://mvnrepository.com/artifact/mysql/mysql-connector-java/8.0.27">
    <img src="https://img.shields.io/badge/MySQLConnector-v8.0.27-blue?logo=MySQL"/>
</a>

## 🧰 Available Content
You have access to all the functionalities, the most used are the following tools:
1. [Decorator](src/main/java/uJo/decorator)
   1. [Type](src/main/java/uJo/decorator/type)
      1. [Column](src/main/java/uJo/decorator/type/Column.java): Field in the **Table**.
      2. [Table](src/main/java/uJo/decorator/type/Table.java): Table registered in the Database.
   2. [TypeData](src/main/java/uJo/decorator/typedata): Determines the type of data and properties that the **Column** will have in the database.
      1. [Varchar](src/main/java/uJo/decorator/typedata/TypeVarchar.java)
      2. [Char](src/main/java/uJo/decorator/typedata/TypeChar.java)
      3. [Int](src/main/java/uJo/decorator/typedata/TypeInt.java)
      4. [Float](src/main/java/uJo/decorator/typedata/TypeFloat.java)
      5. [Double](src/main/java/uJo/decorator/typedata/TypeDouble.java)
      6. [Timestamp](src/main/java/uJo/decorator/typedata/TypeTimestamp.java)
      7. [Time](src/main/java/uJo/decorator/typedata/TypeTime.java)
      8. [Date](src/main/java/uJo/decorator/typedata/TypeDate.java)
2. [Database](src/main/java/uJo/database)
   1. [Database](src/main/java/uJo/database/Database.java): It is the contact with the database, it allows us to create connections and execute the SQL statements
   2. [Repository](src/main/java/uJo/database/repository/Repository.java): It depends on a **Table** to generate common methods like **find, findAll, create, delete**, etc.
   3. [Query](src/main/java/uJo/database/query): Allows create SQL queries

## 🎒 Basic Usage
Assign table name for database.
```java
@Table("users")
public class User { }
```

Record the fields for table and add method **toString**.
```java
@Table("users")
public class User {
   @Column("id")
   @TypeInt(primaryKey = true, autoincrement = true)
   private Integer id;

   @Column("create_at")
   @TypeTimestamp(value = "current_timestamp")
   private String create_at;

   @Override
   public String toString() {
      return "User{id=" + id + ", create_at='" + create_at + "'}";
   }
}
```

Create repository to manage the model, that registers the model in the database.
```java
Repository<User> userRepository = new Repository<User>(User.class);
```

You need to change the necessary database parameters (host, user, database name):
```java
Database.DATABASE = "uJOExample";
```

Example create User with model defined
```java
class uJOExample {
   public static void main(String[] args) {
      Database.DATABASE = "uJOExample";
      Repository<User> userRepository = new Repository<User>(User.class);
      
      User newUser = new User();
      System.out.println(newUser); // User{id=, create_at=''}
      
      newUser = userRepository.create(newUser);
      System.out.println(newUser); // User{id=1, create_at='2022-01-27 01:52:11'}
   }
}
```
