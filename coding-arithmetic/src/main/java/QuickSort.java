import java.util.Random;

/**
 * Created by liuyl on 16/5/22.
 * 快速排序
 */
public class QuickSort {


    public static void main(String[] agrs) {


            Random random =new Random();
            int whichMovie = random.nextInt(10) + 1;
            if(whichMovie % 2 == 0){
                System.out.println("冰川");
            }else {
                System.out.println("谍影");
            }


        //
        //    int[] arr = new int[]{7,3,6,1,5,4,2};
        //quickSort(arr, 0, 6);
        //for(int i : arr){
        //    System.out.print(i+",");
        //}
    }

    public static void quickSort(int[] arr, int start, int end){

        int i = start;
        int j = end;

        if(start == end){
            return;
        }

        int baseNum = arr[i];
        while(i < j){

            while(i < j && arr[j] >= baseNum){
                j--;
            }

            swap(arr, i, j);

            while(i < j && arr[i] <= baseNum){
                i++;
            }

            swap(arr, i, j);
        }

        if(i == j){
            arr[i] = baseNum;
        }

        if(i - start > 1){
            quickSort(arr, start, i);
        }

        if(end - i > 1){
            quickSort(arr, i+1, end);
        }
    }

    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
