/**
 * Created by liuyl on 16/5/22.
 * 检查字符串是否包含
 */
public class StringConatin {

    public static void main(String[] agrs){
        char orginStr[] = new char[]{'A','B','C','D','E','F'};
        char findStr[] = new char[]{'C','D','E'};
        Boolean result = StringContain(orginStr, findStr);

        System.out.println(result);
    }

    public static Boolean StringContain(char[] orginStr, char[] findStr){

        int length = orginStr.length;
        int[] arr = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        for( int i = 0; i < length; i++){
            int hash = orginStr[i] - 'A';
            arr[hash] = 1;
        }

        for( int i = 0; i < 26; i++){
            System.out.print(arr[i]);
        }
        System.out.print("...\n");
        length = findStr.length;
        for( int i = 0; i < length; i++){
            int hash = findStr[i] - 'A';
            if( arr[hash] == 0){
                return false;
            }
        }

        return true;
    }

}
