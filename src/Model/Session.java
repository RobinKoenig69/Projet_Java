package Model;

public class Session {
    private static String userName;
    private static int userID;

    public Session(String userName, int userID) {
        Session.userName = userName;
        Session.userID = userID;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String name) {
        userName = name;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int id) {
        userID = id;
    }
}
