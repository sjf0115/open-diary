package com.sjf.open.base;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by xiaosi on 17-5-23.
 */
public class TreeNode<T> implements Iterable<TreeNode<T>>{

    T data;
    TreeNode<T> parent;
    List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }

    public TreeNode<T> addParent(T parent){
        TreeNode<T> parentNode = new TreeNode<T>(parent);
        parentNode.parent = this;
        this.children.add(parentNode);
        return parentNode;
    }

    @Override
    public Iterator<TreeNode<T>> iterator() {
        return null;
    }
}
