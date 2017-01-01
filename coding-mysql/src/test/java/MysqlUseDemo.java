import com.liuyiling.coding.mysql.example.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created by liuyl on 2016/12/30.
 */
public class MysqlUseDemo {

    private static ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/coding-mysql-dao.xml"});
    private static BookDaoImpl bookDao = (BookDaoImpl) context.getBean("bookDao");
    private static OrderDaoImpl orderDao = (OrderDaoImpl) context.getBean("orderDao");

    public static void main(String[] agrs){

        Book book = new Book("book_name_1", 1, "book_params", new Date(), BookLanguage.CHINESE.value());
        //long id = bookDao.insertBook(book);
        //bookDao.getChineseBookByName("book_name_1");
        //bookDao.getChineseBooksByType(1);
        //bookDao.delChineseBook(id);

        Order order = new Order(1, 1, 1);
        orderDao.insertOrder(order);
        orderDao.insertFinishedOrder(order);


    }

}
