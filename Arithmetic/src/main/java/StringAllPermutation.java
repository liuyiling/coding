/**
 * Created by liuyl on 16/5/22.
 * 字符串的全排序
 * 只能使用递归排序算法，其他算法的时间复杂度也是O(n)
 */
public class StringAllPermutation {

    public static void main(String[] agrs){

        char str[] = new char[]{'a', 'b', 'c', 'd', 'e'};
        CalAllPermutation(str, 0, 5);

    }

    public static void CalAllPermutation(char str[], int from, int to){

        if(to <= 1){
            return;
        }

        if(from == to){
            for(int i = 0; i < to; i++){
                System.out.print(str[i]);
            }
            System.out.println();
        }

        for(int i = from; i < to; i++){
            swap(str, i, from);
            CalAllPermutation(str, from+1, to);
            swap(str, i, from);
        }

    }

    private static void swap(char[] str, int i, int from) {

        char temp = str[from];
        str[from] = str[i];
        str[i] = temp;

    }

}
