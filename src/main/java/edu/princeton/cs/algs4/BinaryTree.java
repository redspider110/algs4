/**
 * preOrderTraversal: ABDCEGFHI
 * inOrderTraversal: DBAGECHFI
 * postOrderTraversal: DBGEHIFCA
 * preOrderNonRecur1: ABDCEGFHI
 * preOrderNonRecur: ABDCEGFHI
 * inOrderNonRecur1: DBAGECHFI
 * inOrderNonRecur: DBAGECHFI
 * postOrderNonRecur1: DBGEHIFCA
 * postOrderNonRecur: DBGEHIFCA
 * levelTraversal: ABCDEFGHI
 */

package edu.princeton.cs.algs4;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * @Author: xmt
 * @Date: 2018/10/8
 */
public class BinaryTree {
    private Node root;

    private class Node{
        char value;
        Node leftChild, rightChild;

        public Node(char value){
            this.value = value;
        }
    }

    private void randomInit(){
        root = new Node('A');
        int n = 8;
        Node[] nodes = new Node[n];
        char[] values = {'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
        for (int i = 0; i < n; i++){
            nodes[i] = new Node(values[i]);
        }

        nodes[4].rightChild = nodes[7];
        nodes[4].leftChild = nodes[6];
        nodes[3].leftChild = nodes[5];
        nodes[1].rightChild = nodes[4];
        nodes[1].leftChild = nodes[3];
        nodes[0].leftChild = nodes[2];
        root.rightChild = nodes[1];
        root.leftChild = nodes[0];
    }

    private void preOrderTraversal(Node node){
        if (node != null){
            visit(node.value);
            preOrderTraversal(node.leftChild);
            preOrderTraversal(node.rightChild);
        }
    }

    private void inOrderTraversal(Node node){
        if (node != null){
            inOrderTraversal(node.leftChild);
            visit(node.value);
            inOrderTraversal(node.rightChild);
        }
    }

    private void postOrderTraversal(Node node){
        if (node != null){
            postOrderTraversal(node.leftChild);
            postOrderTraversal(node.rightChild);
            visit(node.value);
        }
    }

    private void preOrderNonRecur1(Node node){
        // Method 1
        Stack<Node> stack = new Stack<>();

        while (node != null || !stack.empty()){
            while (node != null){
                visit(node.value);
                stack.push(node);
                node = node.leftChild;
            }

            if (!stack.empty()){
                node = stack.pop();
                node = node.rightChild;
            }
        }
    }

    private void preOrderNonRecur(Node node){
        // Method 2
        Stack<Node> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            node = stack.pop();
            visit(node.value);

            if (node.rightChild != null){
                stack.push(node.rightChild);
            }

            if (node.leftChild != null){
                stack.push(node.leftChild);
            }
        }
    }

    private void inOrderNonRecur1(Node node){
        // Method 1.
        Stack<Node> stack = new Stack<>();

        while (node != null || !stack.empty()){
            while (node != null){
                stack.push(node);
                node = node.leftChild;
            }

            if (!stack.empty()){
                node = stack.pop();
                visit(node.value);
                node = node.rightChild;
            }
        }
    }

    private void inOrderNonRecur(Node node){
        // Method 2
        Stack<Node> stack = new Stack<>();
        while (node != null || !stack.isEmpty()) {
            if (node != null) {
                stack.push(node);
                node = node.leftChild;
            }else {
                node = stack.pop();
                visit(node.value);
                node = node.rightChild;
            }
        }
    }

    private void postOrderNonRecur1(Node node) {
        Stack<Node> stack = new Stack<>();
        Node lastVisited = null;

        while (node != null || !stack.empty()){
            if (node != null){
                // 根节点左子节点入栈
                stack.push(node);
                node = node.leftChild;
            } else {
                // 取出根节点
                node = stack.pop();
                // 一个根节点被访问的前提是：无右子树或右子树已被访问过
                if (node.rightChild == null || node.rightChild == lastVisited){
                    visit(node.value);
                    lastVisited = node;
                    // 访问完置空，等待取出栈中下个节点
                    node = null;
                } else {
                    // 根节点重新入栈
                    stack.push(node);
                    // 根节点右子节点入栈
                    node = node.rightChild;
                    stack.push(node);
                    // 右子节点的左子树
                    node = node.leftChild;
                }
            }
        }
    }

    private void postOrderNonRecur(Node node){
        Stack<Node> stack1 = new Stack<>();
        Stack<Node> stack2 = new Stack<>();
        stack1.add(node);
        while (!stack1.isEmpty()) {
            node = stack1.pop();
            stack2.push(node);
            if (node.leftChild != null) {
                stack1.push(node.leftChild);
            }
            if (node.rightChild != null) {
                stack1.push(node.rightChild);
            }
        }

        Node n;
        while (!stack2.empty()){
            n = stack2.pop();
            visit(n.value);
        }
    }

    private void levelTraversal(Node node){
        Queue<Node> queue = new LinkedList<>();

        while (node != null || !queue.isEmpty()){
            visit(node.value);

            if (node.leftChild != null) {
                queue.offer(node.leftChild);
            }

            if (node.rightChild != null) {
                queue.offer(node.rightChild);
            }

            node = queue.poll();
        }
    }

    private void visit(char value){
        System.out.print(value);
    }

    public static void main(String[] args) {
        BinaryTree binaryTree = new BinaryTree();
        binaryTree.randomInit();

        // ---------- Recur ------------
        System.out.print("preOrderTraversal: ");
        binaryTree.preOrderTraversal(binaryTree.root);
        System.out.println();

        System.out.print("inOrderTraversal: ");
        binaryTree.inOrderTraversal(binaryTree.root);
        System.out.println();

        System.out.print("postOrderTraversal: ");
        binaryTree.postOrderTraversal(binaryTree.root);
        System.out.println();

        // ---------- Non Recur ----------
        System.out.print("preOrderNonRecur1: ");
        binaryTree.preOrderNonRecur1(binaryTree.root);
        System.out.println();

        System.out.print("preOrderNonRecur: ");
        binaryTree.preOrderNonRecur(binaryTree.root);
        System.out.println();

        System.out.print("inOrderNonRecur1: ");
        binaryTree.inOrderNonRecur1(binaryTree.root);
        System.out.println();

        System.out.print("inOrderNonRecur: ");
        binaryTree.inOrderNonRecur(binaryTree.root);
        System.out.println();

        System.out.print("postOrderNonRecur1: ");
        binaryTree.postOrderNonRecur1(binaryTree.root);
        System.out.println();

        System.out.print("postOrderNonRecur: ");
        binaryTree.postOrderNonRecur(binaryTree.root);
        System.out.println();

        // --------- level ------------
        System.out.print("levelTraversal: ");
        binaryTree.levelTraversal(binaryTree.root);
        System.out.println();
    }
}
