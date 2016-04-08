package com.liuyiling.coding.effective.classandinterface;

/**
 * Created by liuyl on 16/1/16.
 * 良好设计的类，是把实现和暴露的API隔离开来，只使用API与其他类进行通信,需要遵守以下原则：
 * 1.尽可能地使用每个类或者成员不被外界访问
 * 2.public和protected的类是与外界通信的导出API的一部分,必须一直维护，且不能轻易修改
 * 3.public类不能暴露公有域，只能暴露get set方法来操作公有域
 *
 * 总而言之，一定要遵守几个原则：
 * 1.尽可能降低访问性
 * 2.公有类都不要包含公有域
 * 3.确保公有类中的static final引用的对象不可变
 */

/**
 * class和intreface的修饰符有两种
 * 1.包私有级别的，这种肯定是首选。如果这个类不是该包所需要导出的API，则设置成为包私有级别的，无需担心对其修改会影响到先有的客户端程序,也就是说，包私有级别的类和接口不需要对其他模块负责，因为他没有暴露出来
 * 2.公有的，你要确保他永远正确，需要对其他模块负责
 * 3.如果这个类只会被某一个类的内部使用，尽量声明成内部嵌套类
 */
class CommonTips {

    /**
     * 实例域绝对不能是公有的，必须为private的
     * 如果域是非final的或者是一个指向可变对象的final引用，一旦加入了public修饰符，就相当于放弃了强制这个域不可变的能力
     */
    private int num;

    private Person person;

    /**
     * 如果这个类只会被某一个类的内部使用，尽量声明成内部嵌套类
     * 例如Person这个类只会在CommonTips使用，就可以搞成内部私有嵌套类
     */
    class Person {
        private String name;
        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}

