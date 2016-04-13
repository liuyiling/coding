import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by liuyl on 15/12/28.
 * 这是mockito的使用姿势,欢迎补充
 */
public class HowToUseMockito {

    /**
     * mock某个接口
     */
    @Test
    public void test1() {

        /**
         *  mock(List.class)此种方式会mock掉所有的方法，我们只能看到方法签名和返回值,
         *  相当于mock出来的List只包含{类,类中方法的签名和返回值},方法中的逻辑是空的
         */
        List mockedList = mock(List.class);

        /**
         * mock方法构建出来的类只是个简单的壳子，其中只有方法签名和返回值等
         * 但是根本不执行逻辑，所以下面这一行代码执行完之后list.size()还是0
         */
        mockedList.add(20);

        /**
         * 因为mock出来的List仅仅暴露了方法签名，并且方法体为空，无法实际存在的方法体（原本List的方法体）
         * 只看到了返回值为int,所以返回int的默认值0
         */
        System.out.println(mockedList.size());

        //验证mock对象的行为是否发生
        verify(mockedList).add(20);

        //重置mock
        reset(mockedList);
    }

    /**
     * 使用mock设置调用某个方法的结果
     */
    @Test
    public void test2() {

        //mock具体的某个类而非接口
        LinkedList mockedList = mock(LinkedList.class);

        /**
         * when.thenReturn会返回设置的值
         */
        when(mockedList.get(1)).thenReturn("first");

        /**
         * 自定义参数
         */
        //when(mockedList.get(anyInt())).thenReturn("first");

        /**
         * 设置第一次和非第一次的返回值
         */
        //when(mockedList.get(1)).thenReturn("first").thenReturn("not first");

        /**
         * doReturn.when会返回设置的值
         */
        doReturn("second").when(mockedList).get(2);


        assertEquals("first", mockedList.get(1));
        assertEquals("second", mockedList.get(2));

        verify(mockedList, times(1)).get(1);
        //也可以这样用
        //verify(mockedList,times(1)).get(eq(1));


        verify(mockedList, times(2)).get(anyInt());
    }




    /**
     * 使用回调生成我们期望的返回
     */
    @Test
    public void test3() {

        //mock某个类
        UserDao userDao = mock(UserDao.class);

        //doAnswer返回复杂的数据结构
        Mockito.doAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                User user = new User();
                user.setUserName(args[0].toString());

                return user;
            }
        }).when(userDao).get(anyInt());

        User result = userDao.get(610);
        System.out.println(result.getUserName());


        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] arguments = invocationOnMock.getArguments();
                if (arguments != null && arguments.length > 0 && arguments[0] != null) {
                    System.out.println("mock void method");
                }
                return null;
            }
        }).when(userDao).doNothing(any(User.class));

        /**
         * 刚才说到mock只能看到方法签名和返回参数，是一个空架子，doAnswer为它塞入方法体
         * 并且doAnswer覆盖了整个mock的方法签名和返回参数
         */
        userDao.doNothing(new User());

        /**
         * 因为userDao是mock出来的，所以也没有实际的方法体，仅仅知道userDao.add返回了false
         * userDao.add()无法调用原生定义的输出语句
         *
         */
        System.out.println(userDao.add(new User()));
    }


    /**
     * when.doReturn和DoReturn.when有一个很大的不同点
     * 那就是：
     * when.doReturn编译时会调用方法，DoReturn.when编译不会调用方法
     * 在mock(xx.class)的情况下，因为mock除了方法签名和返回值外，没有其他的内容，见test4的解释，
     * 所以造成了when.doReturn()和DoReturn.when()没有差别
     */
    @Test
    public void test4() {

        //创建mock具体的类
        UserDao userDao = mock(UserDao.class);

        User user = new User();
        user.setUserName("610");
        //进入userDao.get打一个断点，观察是否进入
        when(userDao.get(anyInt())).thenReturn(user);

        User result = userDao.get(610);
        System.out.println(result.getUserName());

        /**
         * 刚才说到使用mock创建的类只能看到方法签名和返回参数，是一个空架子，doAnswer为它塞入方法体
         * when.thenReturn方法只可以改变返回值，不能塞入方法体
         * 综上所诉，when和doReturn其实是一样的，主要区别是：是否塞入方法体，是否用在void方法(when不能返回void)
         */

        System.out.println(userDao.add(new User()));
    }


    /**
     * 使用spy监控真实的对象,在使用spy的时候,我们需要谨慎的使用when-then语句
     * 改用do-when语句（不会调用真是方法）
     */
    @Test
    public void test5() {

        List list = new LinkedList<>();
        List spy = spy(list);

        //下面这个语句会抛出异常，因为编译时会调用真实对象的get(0),引发数组越界
        //可以在linkedList的get()方法打断点进行跟踪
        //除非把spy.add("one");移动至此
        //when(spy.get(1)).thenReturn(1);


        //下面这个语句不会抛出异常，因为不会调用真实对象的get(0)
        //可以在linkedList的get()方法打断点进行跟踪
        doReturn(2).when(spy).get(2);


        //没有被mock的方法，还是调用真实方法
        spy.add("one");
        spy.add("two");

        //被mock的方法，如果是when-return语句，会进入真实方法

        //调用了真实方法
        System.out.println(spy.get(1));
        //调用了真实方法并且被mock了返回值
        System.out.println(spy.get(2));


        //没有被mock的方法，还是调用真实方法
        System.out.println(spy.size());

        verify(spy, times(1)).add("one");
        verify(spy, times(1)).add("two");


    }


    /**
     * spy 和 mock 的两种区别之mock
     * 因为采用了YiLing yiLing = mock(YiLing.class);
     * mockito只看到了方法签名和返回值
     * class YiLing {
     * public false go()
     * }
     * <p>
     * 所以doreturn-when和when-thenreturn一样
     */
    @Test
    public void test6() {

        class YiLing {
            public boolean go() {
                System.out.println("go go go!");
                return false;
            }
        }


        YiLing yiLing = mock(YiLing.class);

        doReturn(true).when(yiLing).go();

        //when(yiLing.go()).thenReturn(true);

        Assert.assertTrue(yiLing.go());

    }

    /**
     * spy 和 mock 的两种区别之spy
     * 因为是spy(new YiLing());的方式，所以产生了（注意和上一个方法对比）
     * class YiLing {
     * public boolean go() {
     * System.out.println("go go go!");
     * return false;
     * }
     * }
     */
    @Test
    public void test7() {

        /**
         *  在mock出来的类中，我们只看到了参数签名和返回值，只能用doReturn或者when.thenReturn改变返回值，其中doReturn和when.thenReturn有细微的差别：
         *  1.是否塞入方法体，2.是否适用于void方法，3.编译时，when-thenreturn需要进入真实方法，do-return不需要进入真实方法
         *
         *  在spy一个真实的类，我们看到了参数签名和返回值和方法体
         */
        class YiLing {
            public Boolean go() {
                System.out.println("go go go!");
                return false;
            }
        }


        YiLing spy = spy(new YiLing());
        //不会进入真实方法
        doReturn(true).when(spy).go();

        //会进入真实方法
        //when(spy.go()).thenReturn(true);
        Assert.assertTrue(spy.go());

    }


    /**
     * spy 和 mock 的两种区别
     */
    @Test
    public void test8() {

        class YiLing {
            public boolean mock() {
                System.out.println("mock mock mock!");
                return false;
            }

            public boolean spy() {
                System.out.println("spy spy spy!");
                return false;
            }
        }

        YiLing yiLing = mock(YiLing.class);
        //情况1
        when(yiLing.mock()).thenReturn(true);
        //情况2
        //doReturn(true).when(yiLing).mock();

        System.out.println(yiLing.mock());
        System.out.println(yiLing.spy());

    }


    /**
     * spy 和 mock 的两种区别
     */
    @Test
    public void test9() {

        class YiLing {
            public boolean mock() {
                System.out.println("mock mock mock!");
                return false;
            }

            public boolean spy() {
                System.out.println("spy spy spy!");
                return false;
            }
        }

        YiLing spy = spy(new YiLing());
        //情况1
        //when(spy.mock()).thenReturn(true);
        //情况2
        doReturn(true).when(spy).mock();

        System.out.println(spy.mock());
        System.out.println(spy.spy());

    }


    /**
     * 验证执行顺序
     */
    @Test
    public void test10() {

        List list = mock(List.class);
        List list2 = mock(List.class);
        list.add(1);
        list2.add("hello");
        list.add(2);
        list2.add("world");

        InOrder inOrder = inOrder(list, list2);

        inOrder.verify(list).add(1);
        inOrder.verify(list2).add("hello");
        inOrder.verify(list).add(2);
        inOrder.verify(list2).add("world");
    }

}
