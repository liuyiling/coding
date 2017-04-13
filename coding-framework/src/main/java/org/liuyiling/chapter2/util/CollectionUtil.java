package org.liuyiling.chapter2.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by liuyl on 2016/11/8.
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection){
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isNotEmpty(Collection<?> collection){
        return !CollectionUtils.isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map){
        return MapUtils.isEmpty(map);
    }

    public static boolean isNotEmpty(Map<?, ?> map){
        return !isEmpty(map);
    }
}
