/**
 * Created by liuyl on 16/5/22.
 * 判断字符串是否是回文
 */
public class IsPralindorm {

    public static void main(String[] agrs){

        char[] str = new char[]{'a','b','c','b','a'};
        System.out.println(isPralindorm(str, 0, 4));

        str = new char[]{'a','b','c','b','a', 'e'};
        System.out.println(isPralindorm(str, 0, 5));
    }


    public static boolean isPralindorm(char[] str, int from, int to){

        if( from == to ){
            return true;
        }

        int begin = from;
        int end = to;

        while(begin < end){
            if (str[begin] == str[end]) {
                begin++;
                end--;
                continue;
            }else {
                return false;
            }
        }

        return true;
    }
}
