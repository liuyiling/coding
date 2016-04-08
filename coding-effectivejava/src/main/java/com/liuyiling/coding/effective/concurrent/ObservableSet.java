package com.liuyiling.coding.effective.concurrent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuyl on 16/1/12.
 * 主旨：为了避免数据破坏和死锁，千万不要在同步区域内调用外来方法，要尽量限制同步区域内部的工作量
 * 1.对共享的可变数据加锁
 * 2.在同步区内做少量的工作：
 *   获得锁，检查共享数据，转换数据状态，释放锁
 * 3.对于一个很耗时的动作，应该想办法移动到同步区外面
 * 4.对于一个可变的类要并发使用，一定要保证其是线程安全的，尽量通过内部同步（concurrentHashMap）这样性能更高,而不是从外部锁定整个对象(synchronized(map))
 * 5.如果在内部同步了类，就使用以下手段来体高性能：分拆锁,分离锁,非阻塞锁
 */

class ObservableSetTest{

    public static void main(String[] agrs){

        ObservableSet observableSet = new ObservableSet(new HashSet<>());

        observableSet.addObserver(new SetObserver() {
            @Override
            public void added(ObservableSet set, Integer o) {
                     System.out.println( o );
                if( o.equals(23) ){
                    //借助了可重入锁的机制，所以不会一直等待
                    set.removeObserver(this);
                }
            }
        });

        for( int i = 0; i < 100; i++){
            observableSet.add(i);
        }

    }

}

/**
 * 导致死锁的demo
 */
class ObservableSetTest2{

    public static void main(String[] agrs){

        ObservableSet observableSet = new ObservableSet(new HashSet<>());

        observableSet.addObserver(new SetObserver() {
            @Override
            public void added(ObservableSet set, Integer o) {
                System.out.println( o );
                if( o.equals(23) ){
                    //set.removeObserver(this); 注释掉这一行
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    final SetObserver observer = this;

                    try {
                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                //后台线程调用set.removeObserver,一直等待observers锁,但是notifyObjectAdd一直暂用所
                                set.removeObserver(observer);
                            }
                        }).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } finally {
                        executor.shutdown();
                    }
                }
            }
        });

        for( int i = 0; i < 100; i++){
            observableSet.add(i);
        }

    }

}


/**
 * 正确的修改1
 */

class ObservableSet2 {

    private final Set s;

    public ObservableSet2(Set s) {
        this.s = s;
    }

    //观察者列表
    private List<SetObserver> observers = new ArrayList<>();

    //加入观察者
    public void addObserver(SetObserver so){
        synchronized (observers){
            observers.add(so);
        }
    }

    public void removeObserver(SetObserver so){
        synchronized (observers){
            observers.remove(so);
        }
    }


    private void notifyObjectAdded(Integer o){

        List<SetObserver> snapshot = null;
        synchronized (observers){
            //通知所有的观察者
            for( SetObserver s : observers){
                snapshot = new ArrayList<>(observers);
            }
        }

        for( SetObserver observer : snapshot){
            //observer.added(this, o);
        }
    }

    public boolean add(Integer o){
        boolean added = s.add(o);
        if( added ){
            notifyObjectAdded(o);
        }
        return added;
    }

}


/**
 * 正确的修改2
 */

class ObservableSet3 {

    private final Set s;

    public ObservableSet3(Set s) {
        this.s = s;
    }

    //观察者列表
    private List<SetObserver> observers = new CopyOnWriteArrayList<>();

    //加入观察者
    public void addObserver(SetObserver so){
            observers.add(so);
    }

    public void removeObserver(SetObserver so){
            observers.remove(so);
    }


    private void notifyObjectAdded(Integer o){
            //通知所有的观察者
            for( SetObserver observer : observers){
                //observer.added(this, o);
            }

    }

    public boolean add(Integer o){
        boolean added = s.add(o);
        if( added ){
            notifyObjectAdded(o);
        }
        return added;
    }

}


class ObservableSet {

    private final Set s;

    public ObservableSet(Set s) {
        this.s = s;
    }

    //观察者列表
    private List<SetObserver> observers = new ArrayList<>();

    //加入观察者
    public void addObserver(SetObserver so){
        synchronized (observers){
            observers.add(so);
        }
    }

    public void removeObserver(SetObserver so){
        synchronized (observers){
            observers.remove(so);
        }
    }


    private void notifyObjectAdded(Integer o){
        /**在同步区域中调用了外来方法
         * 在被同步的区域内,不要调用设计成要被override的方法
         */
        synchronized (observers){
            //通知所有的观察者
            for( SetObserver s : observers){
                /**当notifyObjectAdded方法调用观察者的added方法时,正处于遍历observers列表的过程中
                 * 如第26行，set.removeObserver(this); 调用了 observers.remove(so);
                 * 企图在遍历集合的过程中，删除一个元素，是非法的
                 */
                s.added(this, o);
            }
        }
    }

    public boolean add(Integer o){
        boolean added = s.add(o);
        if( added ){
            notifyObjectAdded(o);
        }
        return added;
    }

}

interface SetObserver{

    void added(ObservableSet set, Integer o);
}