import com.liuyiling.coding.redis.cluster.RedisCluster;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Redis框架的使用方式-Demo示例
 * Created by liuyl on 2016/12/23.
 */
public class RedisUseDemo {

    private static ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"redis-test.xml"});
    private static RedisCluster redisCluster = (RedisCluster) context.getBean("bizRedisCluster");

    public static void main(String[] agrs){
        redisCluster.set("foo", "bar");
        String foo = redisCluster.get("foo", String.class);
        System.out.println(foo);
    }



}
