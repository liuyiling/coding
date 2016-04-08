
/**
 * Created by liuyl on 15/11/29.
 */
public class UserService {

    public UserService() {
        System.out.println("userService constructor");
    }

    UserDao userDao;

    public boolean addUser(long userId, String userName){

        User user = new User(userId,userName);
        return userDao.add(user);

    }


    public User getUser(long userId){
        return userDao.get(userId);
    }

    public void setUserDao(UserDao userDao) {
        System.out.println("hehe");
        this.userDao = userDao;
    }

    public void print(){
        System.out.println("test!");
    }
}
