package user;

public class User {
    String userKey;
    String userID;
    String userPassword;
    String userName;

    public User(String userKey, String userID, String userPassword, String userName) {
        this.userKey = userKey;
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
