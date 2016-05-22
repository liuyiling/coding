
/**
 * Created by liuyl on 16/5/22.
 * 快速排序
 */
public class QuickSort {


    public static void main(String[] agrs) {

        int[] arr = new int[]{7,3,6,1,5,4,2};
        quickSort(arr, 0, 6);
        for(int i : arr){
            System.out.print(i+",");
        }
    }

    public static void quickSort(int[] arr, int start, int end) {

        int i = start;
        int j = end;

        if(start == end){
            return;
        }

        while (i < j) {

            //先从右往左遍历
            while (i < j && arr[i] < arr[j]) {
                j--;
            }

            if (i < j) {
                int temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
            }

            //再从左往右遍历
            while (i < j && arr[i] < arr[j]) {
                i++;
            }

            if (i < j) {
                int temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
            }
        }

        if( i - start > 1){
            quickSort(arr, 0, i - 1);
        }

        if(end - i > 1){
            quickSort(arr, i + 1, end);
        }
    }

}
