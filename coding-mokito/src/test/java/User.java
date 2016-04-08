/**
 * Created by liuyl on 15/11/29.
 */
public class User {


    private Long userId;
    private String userName;


    public User(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public User() {

    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
