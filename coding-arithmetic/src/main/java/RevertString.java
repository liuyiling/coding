/**
 * Created by liuyl on 16/5/22.
 * 字符串反转,给出一个字符串和一个数据，反转字符串
 * abcdef 3 --> defabc
 */
public class RevertString {

    public static void main(String[] agrs){
        char str[] = new char[]{'a','b','c','d','e','f'};
        LeftRateString(str, 6, 3);
        System.out.println(str);
    }

    public static void RevertString(char[] str, int fromInput, int toInput) {

        int from = fromInput;
        int to = toInput;
        while (from < to) {
            char t = str[from];
            str[from++] = str[to];
            str[to--] = t;
        }
    }

    public static void LeftRateString(char[] str, int length, int position) {

        RevertString(str, 0, position - 1);
        RevertString(str, position, length - 1);
        RevertString(str, 0, length - 1);
    }


}



