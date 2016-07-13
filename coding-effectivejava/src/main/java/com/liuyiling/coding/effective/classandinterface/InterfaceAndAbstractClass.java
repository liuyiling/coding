package com.liuyiling.coding.effective.classandinterface;

/**
 * Created by liuyl on 16/4/3.
 * 很多时候，我们以为使用接口或抽象类都是合理地选择，两者之间没有什么差别，其实不然
 * 既然java之父在设计java的时候包含了接口和抽象类，那么他们就肯定有一些差别（即使不大）
 * 下面，让我们通过一些例子来看
 */


/**
 * 我是歌手
 */
abstract class Singer{
    abstract void sing();
}

/**
 * 我是作曲家
 */
abstract class Songwriter{
    abstract void writeSong();
}


/**
 * 糟糕，遇上周杰伦这种天才，我们设计的系统就不够用啦
 * 他不仅会唱歌，还会作曲呢，怎么办！
 * 没事！看看下面的例子：我们使用接口来看一下
 */
class JAY extends Singer{
    @Override
    void sing() {
    }
}


/**
 * 定义歌唱家接口
 */
interface Singer2{
    void sing();
}

/**
 * 定义作曲家接口
 */
interface SongWriter{
    void writeSong();
}


/**
 * 我的天呐，周杰伦即会唱歌又会作曲呢！
 */
class JAY2 implements Singer2,SongWriter{
    @Override
    public void sing() {

    }

    @Override
    public void writeSong() {

    }
}

/**
 * 哎，糟糕,接口都不能去实现定义方法呢，怎么办！大部分歌唱家其实都是一定的套路呢
 * 难不成即使套路一样，我也需要去重写每个歌手的sing和writeSong方法吗？Oh,no!
 * 没事，看看我如何解决！
 * 为每个接口的组合提供一个抽象骨架实现,这样一来，就不需要去复写每个歌手的sing方法了，当然，在这个例子中仅仅省去了5行代码
 * 但是如果sing的逻辑复杂，还是令代码更加整洁了，难道不是吗！
 */
abstract class SingerAndWriter implements Singer2,SongWriter{

    @Override
    public void sing() {
        //观众问好
        sayHello();
        //示意开始
        canBegin();
        //尽情歌唱
        realSing();
        //感谢观众
        sayThanyou();
    }

    protected abstract void sayThanyou();

    protected abstract void canBegin();

    protected abstract void realSing();

    protected abstract void sayHello();


    @Override
    public void writeSong() {

    }


}


interface View{
    void showDialog();
    void dismissDialog();
}

abstract class BaseFragment implements View{

    @Override
    public void showDialog() {
        System.out.println("BaseFragment showDialog");
    }

    @Override
    public void dismissDialog() {
        System.out.println("BaseFragment dismissDialog");
    }
}

class CustomerFragment extends BaseFragment implements View{

    public void showDiaLog(){
        super.showDialog();
    }

    @Override
    public void showDialog() {
        System.out.println("FragmentOne showDialog");
    }
}

public class InterfaceAndAbstractClass {

    public static void main(String[] agrs){
        View fragmentOne = new CustomerFragment();
        fragmentOne.showDialog();
    }
}













