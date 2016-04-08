
/**
 * Created by liuyl on 15/11/29.
 */
public class UserDao {

    public boolean add(User user){
    //    连接数据库添加一个user
        System.out.println("UserDao.add");
        return false;
    }

    public User get(long userId){
    //    连接数据库获取userId
        return new User(1L,"1");
    }

    public void doNothing(User user){
        System.out.println("UserDao.doNothing");
    }
}
