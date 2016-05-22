import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by liuyl on 16/5/18.
 * 二叉树的相关面试题
 */
public class BinaryTree {

    public static void main(String[] agrs){
        //preOrderTravels();
        //levelOrderTravel();
        levelOrderNoRecursion();
    }


    //节点的类描述
    public static class Node{
        Node left;
        Node right;
        int data;

        public Node(Node left, Node right, int data) {
            this.left = left;
            this.right = right;
            this.data = data;
        }
    }

    //生成二叉树
    public static LinkedList<Node> createBinaryTree(int[] arr){

        LinkedList<Node> tree = new LinkedList<>();

        for(int i = 0; i < arr.length ; i++){
            Node node = new Node(null,null,arr[i]);
            tree.add(node);
        }

        for(int i = 0; i < arr.length / 2 - 1; i++){
            Node node = tree.get(i);
            Node leftChild = tree.get(i * 2 + 1);
            Node rightChild = tree.get(i * 2 + 2);

            node.left = leftChild;
            node.right = rightChild;
        }


        int lastNode = arr.length / 2 - 1;
        Node node = tree.get(lastNode);
        Node leftChild = tree.get(lastNode * 2 + 1);
        node.left = leftChild;

        if(arr.length % 2 != 0){
            Node rightChild = tree.get(lastNode * 2 + 2);

            node.right = rightChild;
        }

        return tree;
    }

    public static void preOrderTravels(){
        int[] arr = new int[]{
                1,2,3,4,5,6,7
        };
        LinkedList<Node> binaryTree = createBinaryTree(arr);
        Node node = binaryTree.get(0);

        preOrderTravel(node);
    }

    public static void preOrderTravel(Node node){

        if (node == null){
            return;
        }
        preOrderTravel(node.left);
        preOrderTravel(node.right);

        System.out.println(node.data + " ");

    }

    public static void levelOrderTravel(){
        int[] arr = new int[]{
                1,2,3,4,5,6,7
        };
        LinkedList<Node> binaryTree = createBinaryTree(arr);

        levelOrderTravel(binaryTree);
    }

    public static void levelOrderTravel(LinkedList<Node> tree){

        Queue<Node> queue = new ArrayDeque<>(tree.size());

        Node levelOne = tree.get(0);
        queue.add(levelOne);

        while(!queue.isEmpty()){
            Node node = queue.poll();
            System.out.println(node.data);
            if(node.left != null){
                queue.add(node.left);
            }
            if(node.right != null){
                queue.add(node.right);
            }
        }

    }

    public static void levelOrderNoRecursion(){
        int[] arr = new int[]{
                1,2,3,4,5,6,7
        };
        LinkedList<Node> binaryTree = createBinaryTree(arr);

        Node node = binaryTree.get(0);
        int heightForTheTree = getHeightForTheTree(node);

        for(int i = 1; i <= heightForTheTree; i++){
            levelOrderNoRecursion(node, i);
        }
    }

    public static void levelOrderNoRecursion(Node node, int level){


        if(node == null || level < 1){
            return;
        }

        if(level == 1){
           System.out.println(node.data);
            return;
        }

        levelOrderNoRecursion(node.left, level - 1);
        levelOrderNoRecursion(node.right, level - 1);
    }

    public static int getHeightForTheTree(Node node){
        if(node == null){
            return 0;
        }

        return Math.max(getHeightForTheTree(node.left), getHeightForTheTree(node.right)) + 1;

    }


}
