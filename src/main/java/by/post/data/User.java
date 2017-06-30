package by.post.data;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Dmitriy V.Yefremov
 */
public class User {

    private final String name;
    private final String password;
    private final String id;
    private final SimpleBooleanProperty admin;

    public User(String name, String password, String id, boolean admin) {
        this.name = name;
        this.password = password;
        this.id = id;
        this.admin = new SimpleBooleanProperty(admin);
    }


    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public boolean isAdmin() {
        return admin.get();
    }

    public SimpleBooleanProperty adminProperty() {
        return admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", admin=" + admin +
                '}';
    }
}
