import java.util.ArrayList;

/**
 * Created by liuyl on 16/1/28.
 * 观察者设计模式：
 */
public class ObserverPattern {

    public static void main(String[] agrs){

        /**
         * 创建一个发布者"liuyiling"
         */
        ConcretePublisher liuyiling = new ConcretePublisher("liuyiling");
        ConcretePublisher wangjingbo = new ConcretePublisher("wangjingbo");

        /**
         * 创建3个订阅者:洗脸，刷牙，起床
         */
        ConcreteSubscribler washFace = new ConcreteSubscribler("wash face");
        ConcreteSubscribler brushTeeth = new ConcreteSubscribler("brush teeth");

        /**
         * 3个订阅者都订阅了”liuyiling"
         */
        liuyiling.addSubscribler(washFace);

        wangjingbo.addSubscribler(brushTeeth);

        liuyiling.changeState("wake up");
        wangjingbo.changeState("wake up");
    }
}

/**
 * 发布者
 */
class Publisher{

    private String name;

    public Publisher(String name) {
        this.name = name;
    }

    /**
     * 订阅者列表
     */
    private ArrayList<Subscribler> subscriblerList = new ArrayList();

    /**
     * 增加订阅者
     * @param subscribler
     */
    public void addSubscribler(Subscribler subscribler){
        subscriblerList.add(subscribler);
    }

    /**
     * 删除订阅者
     * @param subscribler
     */
    public void removerSubscribler(Subscribler subscribler){
        subscriblerList.remove(subscribler);
    }

    /**
     * 通知消息到订阅者
     */
    public void notifySubsribler(){
        for( Subscribler subscribler : subscriblerList){
            subscribler.update(this);
        }
    }

    public String getName() {
        return name;
    }

}

/**
 * 具体的发布者实例
 */
class ConcretePublisher extends Publisher{

    public ConcretePublisher(String name) {
        super(name);
    }

    private String state;

    /**
     * 状态发生变化的时候通知所有的订阅者
     * @param state
     */
    public void changeState(String state){
        this.state = state;
        notifySubsribler();
    }

    public String getState(){
        return this.state;
    }

}

/**
 * 订阅者
 */
class Subscribler{

    private String name;

    public void update(Publisher publisher){};

    public Subscribler() {
    }

    public Subscribler(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

/**
 * 订阅者的具体实现
 */
class ConcreteSubscribler extends Subscribler{


    public ConcreteSubscribler(String name) {
        super(name);
    }

    @Override
    public void update(Publisher publisher) {
        System.out.println(publisher.getName() + " should " + getName());
    }
}