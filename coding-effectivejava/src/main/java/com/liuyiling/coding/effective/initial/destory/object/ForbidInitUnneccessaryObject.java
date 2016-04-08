package com.liuyiling.coding.effective.initial.destory.object;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyl on 16/1/11.
 * 避免创建不必要的对象很关键，可以节约内存的资源
 * 或者说，我们一定要避免创建一样的对象
 * 接下来的代码中：
 * Student相比Student2来说,虽然基于一样的方法调用,但是new了更少的对象
 */
public class ForbidInitUnneccessaryObject {

    public static void main(String[] agrs){
        Map<String, Long> map = new HashMap<>();
        //注意，所有调用该方法keySet()返回的都是同一个视图
        map.keySet();

        /**
         * 尽量使用基本类型而不是装箱类型，把底下的代码改成long试试看性能的提高
         */
        Long sum = 0l;
        for( int i = 0; i < Integer.MAX_VALUE; i++){
            sum = sum + 1;
        }
    }
}

/**
 * 判断一个学生是否是90后
 */
class Student{

    public final Date birthday;

    public Student(Date birthday) {
        this.birthday = birthday;
    }

    //时间判断
    public boolean isNintyAfter(){

        Calendar gtmCal = Calendar.getInstance();
        gtmCal.set(1990,1,1);
        Date startDate = gtmCal.getTime();
        gtmCal.set(1999,12,31);
        Date endDate = gtmCal.getTime();

        return birthday.compareTo(startDate) >=0 && birthday.compareTo(endDate) <= 0;
    }

}

class Student2{

    public final Date birthday;

    public final Date startDate;
    public final Date endDate;

    {
        Calendar gtmCal = Calendar.getInstance();
        gtmCal.set(1990,1,1);
        startDate = gtmCal.getTime();
        gtmCal.set(1999,12,31);
        endDate = gtmCal.getTime();
    }

    public Student2(Date birthday) {
        this.birthday = birthday;
    }

    //时间判断
    public boolean isNintyAfter(){
        return birthday.compareTo(startDate) >=0 && birthday.compareTo(endDate) <= 0;
    }
}
