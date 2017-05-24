package com.sjf.open.base;

/**
 * Created by xiaosi on 17-5-23.
 */
public class TreeDemo {
    public static void main(String[] args) {
        TreeNode<String> node1 = new TreeNode<String>("1");
        TreeNode<String> node2 = new TreeNode<String>("2");
        TreeNode<String> node3 = new TreeNode<String>("3");
        TreeNode<String> node4 = new TreeNode<String>("4");
        TreeNode<String> node5 = new TreeNode<String>("32");

        node2.parent = node1;
        node1.children.add(node2);


    }
}
