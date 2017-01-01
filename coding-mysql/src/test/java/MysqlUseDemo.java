import com.liuyiling.coding.mysql.example.Book;
import com.liuyiling.coding.mysql.example.BookDaoImpl;
import com.liuyiling.coding.mysql.example.BookLanguage;
import com.liuyiling.coding.mysql.example.OrderDaoImpl;
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

        Book book = new Book("book_name_1", 1, "book_params".toCharArray(), new Date(), BookLanguage.CHINESE.value());
        bookDao.insertBook(book);



    }

}
