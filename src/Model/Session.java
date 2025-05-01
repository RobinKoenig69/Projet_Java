package Model;

public class Session {
    private static String userName;
    private static int userID;
    private static int userBooking;

    public Session(String userName, int userID, int  userBooking) {
        Session.userName = userName;
        Session.userID = userID;
        Session.userBooking = userBooking;
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

    public static int getUserBooking() { return userBooking; }

    public static void setUserBooking(int  booking) { userBooking = booking; }
}
