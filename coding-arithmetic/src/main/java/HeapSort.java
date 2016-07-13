/**
 * Created by liuyl on 16/5/22.
 * 堆排序借助于堆这个数据结构，我们这边采用最大堆来实现堆排序
 * 堆排序无非就是两个步骤：
 *  1.构建最大堆
 *  2.交换堆顶和元素值
 */
public class HeapSort {

    public static void main(String[] agrs){
        int[] arr = new int[]{7,3,6,1,5,4,2};
        heapSort(arr);
    }

    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void heapSort(int arr[]){
        for(int i = 0; i < arr.length; i++){
            createMaxHeap(arr, arr.length - i - 1);
            swap(arr, 0, arr.length - i - 1);
        }

        for(int i : arr) {
            System.out.print(i + ",");
        }
    }

    //从下往上遍历构建最大堆
    public static void createMaxHeap(int arr[], int lastIndex){
        for(int i = (lastIndex - 1) / 2; i >= 0 ; i--){

            //记录所判断的节点
            int k = i;

            //遍历到最后一个节点，然后将大于该节点的值与当前节点交换
            while( 2 * k + 1 <= lastIndex){

                int biggerIndex = 2 * k + 1;
                //存在右节点
                if( biggerIndex + 1 <= lastIndex){
                    //且右节点大于左节点
                    if(arr[biggerIndex] < arr[biggerIndex + 1]){
                        biggerIndex++;
                    }
                }

                //确认完子节点的大小之后进行交换
                if(arr[k] < arr[biggerIndex]){
                    swap(arr, k, biggerIndex);
                    k = biggerIndex;
                } else {
                    break;
                }
            }
        }
    }

}
