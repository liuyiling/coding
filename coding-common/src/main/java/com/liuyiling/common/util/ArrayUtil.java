package com.liuyiling.common.util;

import org.apache.commons.lang.ArrayUtils;

import java.util.*;

/**
 * Created by liuyl on 2016/12/29.
 */
public class ArrayUtil {

    private ArrayUtil() {
    }

    public static Long[] toLongArr(String[] strArr) {
        Long[] longArr = new Long[strArr.length];

        for(int i = 0; i < strArr.length; ++i) {
            longArr[i] = Long.valueOf(Long.parseLong(strArr[i]));
        }

        return longArr;
    }

    public static Long[] toLongArr(long[] longArr) {
        Long[] rs = new Long[longArr.length];

        for(int i = 0; i < longArr.length; ++i) {
            rs[i] = Long.valueOf(longArr[i]);
        }

        return rs;
    }

    public static long[] toLongArr(Long[] longArr) {
        long[] rs = new long[longArr.length];

        for(int i = 0; i < longArr.length; ++i) {
            rs[i] = Long.valueOf(longArr[i].longValue()).longValue();
        }

        return rs;
    }

    public static long[] toRawLongArr(String[] strArr) {
        long[] longArr = new long[strArr.length];

        for(int i = 0; i < strArr.length; ++i) {
            longArr[i] = Long.parseLong(strArr[i]);
        }

        return longArr;
    }

    public static int[] toRawIntArr(String[] strArr) {
        int[] intArr = new int[strArr.length];

        for(int i = 0; i < strArr.length; ++i) {
            intArr[i] = Integer.parseInt(strArr[i]);
        }

        return intArr;
    }

    public static long[] toLongArr(Collection<Long> ids) {
        if(ids != null && ids.size() != 0) {
            long[] idsArr = new long[ids.size()];
            int idx = 0;

            long id;
            for(Iterator var3 = ids.iterator(); var3.hasNext(); idsArr[idx++] = id) {
                id = ((Long)var3.next()).longValue();
            }

            return idsArr;
        } else {
            return new long[0];
        }
    }

    public static int[] toIntArr(Collection<Integer> ids) {
        if(ids != null && ids.size() != 0) {
            int[] idsArr = new int[ids.size()];
            int idx = 0;

            int id;
            for(Iterator var3 = ids.iterator(); var3.hasNext(); idsArr[idx++] = id) {
                id = ((Integer)var3.next()).intValue();
            }

            return idsArr;
        } else {
            return new int[0];
        }
    }

    public static Integer[] toIntegerArr(int[] intAry) {
        if(intAry != null && intAry.length != 0) {
            Integer[] retAry = new Integer[intAry.length];

            for(int i = 0; i < retAry.length; ++i) {
                retAry[i] = Integer.valueOf(intAry[i]);
            }

            return retAry;
        } else {
            return new Integer[0];
        }
    }

    public static Byte[] toByteArr(byte[] byteAry) {
        if(byteAry != null && byteAry.length != 0) {
            Byte[] retAry = new Byte[byteAry.length];

            for(int i = 0; i < retAry.length; ++i) {
                retAry[i] = Byte.valueOf(byteAry[i]);
            }

            return retAry;
        } else {
            return new Byte[0];
        }
    }

    public static String longArrToPrintableString(long[] longArr) {
        StringBuilder value = new StringBuilder();
        value.append("[");
        if(null != longArr && longArr.length > 0) {
            long[] var2 = longArr;
            int var3 = longArr.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                long l = var2[var4];
                value.append(l).append(",");
            }

            value.setLength(value.length() - 1);
        }

        value.append("]");
        return value.toString();
    }

    public static String stringArrToPrintableString(String[] objectArr) {
        StringBuilder value = new StringBuilder();
        value.append("[");
        if(null != objectArr && objectArr.length > 0) {
            String[] var2 = objectArr;
            int var3 = objectArr.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String s = var2[var4];
                value.append(s).append(",");
            }

            value.setLength(value.length() - 1);
        }

        value.append("]");
        return value.toString();
    }

    /** @deprecated */
    @Deprecated
    public static String arrayToString(long[] ids) {
        StringBuilder sbuf = new StringBuilder(ids.length * 8);
        long[] var2 = ids;
        int var3 = ids.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            long id = var2[var4];
            sbuf.append(id).append(",");
        }

        return sbuf.toString();
    }

    public static String arrayToString(Object[] arrs) {
        return arrayToString(arrs, ",");
    }

    public static String arrayToString(Object[] arrs, String split) {
        if(arrs == null) {
            return "";
        } else {
            int iMax = arrs.length - 1;
            if(iMax == -1) {
                return "";
            } else {
                StringBuilder b = new StringBuilder();
                int i = 0;

                while(true) {
                    b.append(String.valueOf(arrs[i]));
                    if(i == iMax) {
                        return b.toString();
                    }

                    b.append(split);
                    ++i;
                }
            }
        }
    }

    public static String toSimpleString(long[] arrs) {
        if(arrs == null) {
            return "";
        } else {
            int iMax = arrs.length - 1;
            if(iMax == -1) {
                return "";
            } else {
                StringBuilder b = new StringBuilder();
                int i = 0;

                while(true) {
                    b.append(arrs[i]);
                    if(i == iMax) {
                        return b.toString();
                    }

                    b.append(",");
                    ++i;
                }
            }
        }
    }

    public static String[] trimDuplicates(String[] strs) {
        if(strs != null && strs.length != 0) {
            LinkedHashSet set = new LinkedHashSet(strs.length);
            String[] var2 = strs;
            int var3 = strs.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String str = var2[var4];
                if(str != null) {
                    set.add(str);
                }
            }

            return (String[])set.toArray(new String[0]);
        } else {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }
    }

    /** @deprecated */
    @Deprecated
    public static String toJSONStr(long[] ids) {
        return toJson(ids);
    }

    public static String toJson(long[] ids) {
        String str = "[]";
        if(ids != null && ids.length > 0) {
            int iMax = ids.length - 1;
            StringBuilder b = new StringBuilder();
            b.append('[');
            int i = 0;

            while(true) {
                b.append('\"').append(ids[i]).append('\"');
                if(i == iMax) {
                    b.append(']').toString();
                    str = b.toString();
                    break;
                }

                b.append(", ");
                ++i;
            }
        }

        return str;
    }


    public static void reverse(long[] b) {
        int left = 0;

        for(int right = b.length - 1; left < right; --right) {
            long temp = b[left];
            b[left] = b[right];
            b[right] = temp;
            ++left;
        }

    }

    public static void reverse(long[][] b) {
        int left = 0;

        for(int right = b.length - 1; left < right; --right) {
            long[] temp = b[left];
            b[left] = b[right];
            b[right] = temp;
            ++left;
        }

    }

    public static long[] reverseCopy(long[] original, int newLength) {
        long[] result = new long[newLength];
        int originalLimit = original.length - newLength;
        int i = original.length - 1;

        for(int resultIdx = 0; i >= originalLimit; --i) {
            result[resultIdx++] = original[i];
        }

        return result;
    }

    public static long[] reverseCopyRange(long[] original, int from, int to) {
        long[] results = Arrays.copyOfRange(original, from, to);
        reverse(results);
        return results;
    }

    public static long[] removeAll(long[] sourceArray, long[] removeArray) {
        int sourceLength = sourceArray.length;
        int removeLength = removeArray.length;
        if(sourceLength != 0 && removeLength != 0) {
            long[] temp = new long[sourceLength];
            int i = 0;
            int var8;
            if(removeLength > 2) {
                HashSet removeSet = new HashSet(removeLength);
                long[] var7 = removeArray;
                var8 = removeArray.length;

                int source;
                long source1;
                for(source = 0; source < var8; ++source) {
                    source1 = var7[source];
                    removeSet.add(Long.valueOf(source1));
                }

                var7 = sourceArray;
                var8 = sourceArray.length;

                for(source = 0; source < var8; ++source) {
                    source1 = var7[source];
                    if(!removeSet.contains(Long.valueOf(source1))) {
                        temp[i++] = source1;
                    }
                }
            } else {
                long[] var12 = sourceArray;
                int var13 = sourceArray.length;

                for(var8 = 0; var8 < var13; ++var8) {
                    long var14 = var12[var8];
                    if(!arrayContains(removeArray, var14)) {
                        temp[i++] = var14;
                    }
                }
            }

            return i < temp.length?Arrays.copyOf(temp, i):temp;
        } else {
            return sourceArray;
        }
    }

    private static boolean arrayContains(long[] arrs, long value) {
        long[] var3 = arrs;
        int var4 = arrs.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            long arr = var3[var5];
            if(arr == value) {
                return true;
            }
        }

        return false;
    }

    public static <T> List<T> removeAll(List<T> sourceList, Collection<T> removeList) {
        ArrayList leftList = new ArrayList(sourceList.size() - removeList.size());
        HashSet removeSet = new HashSet(removeList.size());
        removeSet.addAll(removeList);
        Iterator var4 = sourceList.iterator();

        while(var4.hasNext()) {
            Object item = var4.next();
            if(!removeSet.contains(item)) {
                leftList.add(item);
            }
        }

        return leftList;
    }

    public static long[] removeAll(long[] sourceArray, Set<Long> removeSet) {
        int sourceLength = sourceArray.length;
        int removeLength = removeSet.size();
        if(sourceLength != 0 && removeLength != 0) {
            long[] temp = new long[sourceLength];
            int i = 0;
            long[] var6 = sourceArray;
            int var7 = sourceArray.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                long source = var6[var8];
                if(!removeSet.contains(Long.valueOf(source))) {
                    temp[i++] = source;
                }
            }

            if(i < temp.length) {
                return Arrays.copyOf(temp, i);
            } else {
                return temp;
            }
        } else {
            return sourceArray;
        }
    }

    public static long[] sort(long[] left, long[] right) {
        long[] result = addTo(left, right);
        Arrays.sort(result);
        return result;
    }

    public static void sortDesc(long[] a) {
        Arrays.sort(a);
        reverse(a);
    }

    public static long[] intersectionOrder(long[] arr1, long[] arr2) {
        HashSet tempSet = new HashSet();

        for(int tmpResult = 0; tmpResult < arr2.length; ++tmpResult) {
            tempSet.add(Long.valueOf(arr2[tmpResult]));
        }

        long[] var6 = new long[arr1.length];
        int count = 0;

        for(int i = 0; i < arr1.length; ++i) {
            if(tempSet.contains(Long.valueOf(arr1[i]))) {
                var6[count] = arr1[i];
                ++count;
            }
        }

        return Arrays.copyOf(var6, count);
    }

    public static long[] addTo(long[] left, long[] right) {
        if(left == null) {
            return ArrayUtils.clone(right);
        } else if(right == null) {
            return ArrayUtils.clone(left);
        } else {
            long[] result = new long[left.length + right.length];
            byte pos = 0;
            System.arraycopy(left, 0, result, pos, left.length);
            int pos1 = pos + left.length;
            System.arraycopy(right, 0, result, pos1, right.length);
            return result;
        }
    }

    public static long[] addTo(long[] arr, long id) {
        return addTo(arr, new long[]{id});
    }

    public static long[] addTo(long[] arr, long id, int limit) {
        if(ArrayUtils.contains(arr, id)) {
            return arr;
        } else {
            arr = addTo(arr, id);
            return getLimited(arr, limit);
        }
    }

    public static long[] getLimited(long[] arr, int limit) {
        if(arr == null) {
            return arr;
        } else if(arr.length > limit) {
            long[] result = new long[limit];
            System.arraycopy(arr, arr.length - limit, result, 0, limit);
            return result;
        } else {
            return arr;
        }
    }

    public static List<Integer> arrayToList(int[] arr) {
        if(ArrayUtils.isEmpty(arr)) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList(arr.length);
            int[] var2 = arr;
            int var3 = arr.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                int i = var2[var4];
                result.add(Integer.valueOf(i));
            }

            return result;
        }
    }

    public static Set<Integer> arrayToSet(int[] arr) {
        if(ArrayUtils.isEmpty(arr)) {
            return new HashSet(0);
        } else {
            HashSet result = new HashSet(arr.length);
            int[] var2 = arr;
            int var3 = arr.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                int i = var2[var4];
                result.add(Integer.valueOf(i));
            }

            return result;
        }
    }

    public static List<Long> arrayToList(long[] arr) {
        if(ArrayUtils.isEmpty(arr)) {
            return new ArrayList(0);
        } else {
            ArrayList result = new ArrayList(arr.length);
            long[] var2 = arr;
            int var3 = arr.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                long i = var2[var4];
                result.add(Long.valueOf(i));
            }

            return result;
        }
    }

    public static Set<Long> arrayToSet(long[] arr) {
        if(ArrayUtils.isEmpty(arr)) {
            return new HashSet(0);
        } else {
            HashSet result = new HashSet(arr.length);
            long[] var2 = arr;
            int var3 = arr.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                long i = var2[var4];
                result.add(Long.valueOf(i));
            }

            return result;
        }
    }

    public static <T> int indexOf(List<? extends Comparable<? super T>> list, T target) {
        for(int i = 0; i < list.size(); ++i) {
            if(((Comparable)list.get(i)).compareTo(target) == 0) {
                return i;
            }
        }

        return -1;
    }

    public static <T> int binarySearchForInsert(List<? extends Comparable<? super T>> list, T key) {
        if(list == null) {
            return -1;
        } else if(list.size() == 0) {
            return 0;
        } else {
            int position = Collections.binarySearch(list, key);
            return position >= 0?position:Math.abs(position + 1);
        }
    }


    public static int binarySearchForInsert(long[] array, long target) {
        int position = Arrays.binarySearch(array, target);
        return position >= 0?position:Math.abs(position + 1);
    }

    public static void revers(byte[] ary) {
        for(int i = 0; i < ary.length / 2; ++i) {
            Byte b = Byte.valueOf(ary[i]);
            ary[i] = ary[ary.length - i - 1];
            ary[ary.length - i - 1] = b.byteValue();
        }

    }
}
