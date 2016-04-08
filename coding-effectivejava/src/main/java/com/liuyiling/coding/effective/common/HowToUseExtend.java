package com.liuyiling.coding.effective.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by liuyl on 16/4/2.
 * 什么时候使用继承，我们有一个原则：只有当is-a关系的时候，即B是A的时候，B才应该继承A
 * 否则，就要尽量使用复合关系，B中包含A
 */
public class HowToUseExtend {

}


/**
 * 这些是错误的方式
 * @param <E>
 */
class InstrumentedHashSet<E> extends HashSet<E>{

    private int addCount = 0;

    public InstrumentedHashSet() {
    }

    @Override
    public boolean add(E e) {
        addCount++;
        return super.add(e);
    }


    /**
     * addAll实际调用了父类的add方法，但是父类又找到子类复写的add方法
     * 造成了size整整加了2倍
     * @param c
     * @return
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        addCount += c.size();
        return super.addAll(c);
    }
}


/**
 * 底下是正确的方式:使用了组合而不是继承，
 * 原因如下：需要生成的类与set集合已经不是is-a关系了，而是具有另外的特性
 *
 */
class CorrectSet extends ForwardingSet{

    private int addCount = 0;

    public CorrectSet(Set s) {
        super(s);
    }

    @Override
    public boolean add(Object o) {
        addCount++;
        return super.add(o);
    }

    @Override
    public boolean addAll(Collection c) {
        addCount += c.size();
        return super.addAll(c);
    }

    public int getAddCount() {
        return addCount;
    }
}


class ForwardingSet<E> implements Set<E>{
    private final Set<E> s;

    public ForwardingSet(Set<E> s) {
        this.s = s;
    }

    @Override
    public int size() {
        return s.size();
    }

    @Override
    public boolean isEmpty() {
        return s.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return s.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return s.iterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return s.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return s.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return s.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return s.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return s.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return s.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return s.removeAll(c);
    }

    @Override
    public void clear() {
        s.clear();
    }
}