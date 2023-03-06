package AVLTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class AVLTree <E> extends BST<E>{

    public AVLTree(){}
    public AVLTree(Comparator<E> c){super(c);}
    public AVLTree(E[] objects){super(objects);}
    @Override
    protected AVLTreeNode<E> createNewNode(E data){return new AVLTreeNode<E>(data);}
    @Override
    public boolean insert(E data){
        boolean successful = super.insert(data);
        if(!successful) return false;
        else balancePath(data);

        return true;
    }

    private void updateHeight(AVLTreeNode<E> node){
        if(node.left == null && node.right == null) node.height = 0;
        else if(node.left == null) node.height = 1 + ((AVLTreeNode<E>)(node.right)).height;
        else if(node.right == null) node.height = 1 + ((AVLTreeNode<E>)(node.left)).height;
        else node.height = 1 + Math.max(((AVLTreeNode<E>)(node.left)).height,((AVLTreeNode<E>)(node.right)).height);
    }

    private void balancePath(E data){
        ArrayList<TreeNode<E>> path = path(data);
        for(int i = path.size()-1; i >=0; i--){
            AVLTreeNode<E> A = (AVLTreeNode<E>)(path.get(i));
            updateHeight(A);
            AVLTreeNode<E> parentOfA = (A==root) ? null : (AVLTreeNode<E>) (path.get(i-1));

            switch(balanceFactor(A)){
                case -2:
                    if(balanceFactor((AVLTreeNode<E>)A.left)<=0) balanceLL(A,parentOfA);
                    else balanceLR(A,parentOfA);
                    break;
                case +2:
                    if(balanceFactor((AVLTreeNode<E>)A.right) >=0) balanceRR(A,parentOfA);
                    else balanceRL(A,parentOfA);
            }//End Switch Cases
        }//End for loop
    }

    private int balanceFactor(AVLTreeNode<E> node){
        if(node.right == null) return -node.height;
        else if(node.left == null) return +node.height;
        else return ((AVLTreeNode<E>)node.right).height - ((AVLTreeNode<E>)node.left).height;
    }

    private void balanceLL(TreeNode<E>A,TreeNode<E> parentOfA){
        TreeNode<E> B = A.left;

        if(A == root) root = B;
        else{
            if(parentOfA.left == A) parentOfA.left = B;
            else parentOfA.right = B;
        }
        A.left = B.right;
        B.right = A;
        updateHeight((AVLTreeNode<E>) A);
        updateHeight((AVLTreeNode<E>) B);
    }

    private void balanceLR(TreeNode<E> A, TreeNode<E> parentOfA){
        TreeNode<E> B = A.left;
        TreeNode<E> C = B.right;

        if(A == root) root = C;
        else{
            if(parentOfA.left == A) parentOfA.left = C;
            else parentOfA.right = C;
        }

        A.left = C.right;
        B.right = C.left;
        C.left = B;
        C.right = A;

        updateHeight((AVLTreeNode<E>) A);
        updateHeight((AVLTreeNode<E>) B);
        updateHeight((AVLTreeNode<E>) C);
    }

    private void balanceRR(TreeNode<E>A,TreeNode<E> parentOfA){
        TreeNode<E> B = A.right;

        if(A == root) root = B;
        else{
            if(parentOfA.left == A) parentOfA.left = B;
            else parentOfA.right = B;
        }
        A.right = B.left;
        B.left = A;
        updateHeight((AVLTreeNode<E>) A);
        updateHeight((AVLTreeNode<E>) B);
    }

    private void balanceRL(TreeNode<E> A, TreeNode<E> parentOfA){
        TreeNode<E> B = A.right;
        TreeNode<E> C = B.left;

        if(A == root) root = C;
        else{
            if(parentOfA.left == A) parentOfA.left = C;
            else parentOfA.right = C;
        }

        A.right = C.left;
        B.left = C.right;
        C.left = A;
        C.right = B;

        updateHeight((AVLTreeNode<E>) A);
        updateHeight((AVLTreeNode<E>) B);
        updateHeight((AVLTreeNode<E>) C);
    }

    public boolean delete(E data){
        if(root == null) return false;

        TreeNode<E> parent = null;
        TreeNode<E> current = root;
        while(current != null){
            if(c.compare(data, current.element) < 0){
                parent = current;
                current = current.left;
            }
            else if(c.compare(data, current.element) > 0){
                parent = current;
                current = current.right;
            }
            else break;
        }//End While loop

        if(current == null) return false;
        // Case 1: Current node has no left children
        if(current.left == null){
            // The Current node is the root node
            if(parent == null) root = current.right;
            else{
                if(c.compare(data, parent.element)<0) parent.left = current.right;
                else parent.right = current.right;
                balancePath(parent.element);
            }
        }// End Case 1
        // Case 2: The current node has a left child
        // Locate the rightmost node in the left subtree
        // of the current node and also its parent
        else{
            TreeNode<E> parentOfRightMost = current;
            TreeNode<E> rightMost = current.left;

            while(rightMost.right != null){
                parentOfRightMost = rightMost;
                rightMost = rightMost.right;
            }//End While loop
            current.element = rightMost.element;

            if(parentOfRightMost.right == rightMost) parentOfRightMost.right = rightMost.left;
            else parentOfRightMost.left = rightMost.left;

            balancePath(parentOfRightMost.element);
        }
        size--;
        return true;
    }
    protected static class AVLTreeNode<E> extends BST.TreeNode<E>{
        protected int height = 0;
        public AVLTreeNode(E o){
            super(o);
        }
    }
}
