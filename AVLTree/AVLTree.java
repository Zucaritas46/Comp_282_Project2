package AVLTree;


import StackAndQueuePackage.LinkedQueue;
import StackAndQueuePackage.LinkedStack;
import StackAndQueuePackage.QueueInterface;
import StackAndQueuePackage.StackInterface;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class AVLTree <T> extends BinaryTree<T>{

    private AVLTreeNode<T> root;
    public AVLTree(){
        this.root = null;
    }

    public AVLTree(T rootData){
        root = new AVLTreeNode<>(rootData);
    }

    public AVLTree(T rootData, BinaryTreeInterface<T> leftTree, BinaryTreeInterface<T> rightTree){
        initializeTree(rootData, (AVLTree<T>)leftTree, (AVLTree<T>)rightTree);
    }

    @Override
    public void setTree(T rootData, BinaryTreeInterface<T> leftTree, BinaryTreeInterface<T> rightTree) {
        initializeTree(rootData, (AVLTree<T>)leftTree, (AVLTree<T>)rightTree);
    }
    // -- Not Overriden --
    // setRootData(T rootData)
    // T getRootData
    // isEmpty
    // clear
    // getHeight
    // getNumberOfNodes

    protected void setRootNode(AVLTreeNode<T> rootNode){ root = rootNode; }

    protected AVLTreeNode<T> getRootNode(){return root;}

    private void initializeTree(T rootData, AVLTree<T>leftTree, AVLTree<T>rightTree){
        root = new AVLTreeNode<>(rootData);

        if ((leftTree != null) && !leftTree.isEmpty())
            root.setLeftChild(leftTree.root);

        if ((rightTree != null) && !rightTree.isEmpty())
        {
            if (rightTree != leftTree)
                root.setRightChild(rightTree.root);
            else
                root.setRightChild(rightTree.root.copy());
        } // end if

        if ((leftTree != null) && (leftTree != this))
            leftTree.clear();

        if ((rightTree != null) && (rightTree != this))
            rightTree.clear();
    }

    //-------------Iterators-----------//
    public void iterativePreorderTraverse() {
        StackInterface<AVLTreeNode<T>> nodeStack = new LinkedStack<>();
        if (root != null) nodeStack.push(root);
        AVLTreeNode<T> nextNode;
        while (!nodeStack.isEmpty()) {

            nextNode = nodeStack.pop();
            AVLTreeNode<T> leftChild = nextNode.getLeftChild();
            AVLTreeNode<T> rightChild = nextNode.getRightChild();

            // Push into stack in reverse order of recursive calls
            if (rightChild != null) nodeStack.push(rightChild);

            if (leftChild != null) nodeStack.push(leftChild);

            System.out.print(nextNode.getData() + " ");
        } // end while
    } // end iterativePreorderTraverse

    public void iterativeInorderTraverse() {
        StackInterface<AVLTreeNode<T>> nodeStack = new LinkedStack<>();
        AVLTreeNode<T> currentNode = root;

        while (!nodeStack.isEmpty() || (currentNode != null))
        {
            // Find leftmost node with no left child
            while (currentNode != null){
                nodeStack.push(currentNode);
                currentNode = currentNode.getLeftChild();
            } // end while

            // Visit leftmost node, then traverse its right subtree
            if (!nodeStack.isEmpty()){
                AVLTreeNode<T> nextNode = nodeStack.pop();
                // Assertion: nextNode != null, since nodeStack was not empty
                // before the pop
                System.out.print(nextNode.getData() + " ");
                currentNode = nextNode.getRightChild();
            } // end if
        } // end while
    } // end iterativeInorderTraverse

    private class PreorderIterator implements Iterator<T> {
        private StackInterface<AVLTreeNode<T>> nodeStack;

        public PreorderIterator() {
            nodeStack = new LinkedStack<>();
            if (root != null)
                nodeStack.push(root);
        } // end default constructor

        public boolean hasNext()
        {
            return !nodeStack.isEmpty();
        } // end hasNext

        public T next() {
            AVLTreeNode<T> nextNode;

            if (hasNext()){
                nextNode = nodeStack.pop();
                AVLTreeNode<T> leftChild = nextNode.getLeftChild();
                AVLTreeNode<T> rightChild = nextNode.getRightChild();

                // Push into stack in reverse order of recursive calls
                if (rightChild != null)
                    nodeStack.push(rightChild);

                if (leftChild != null)
                    nodeStack.push(leftChild);
            }
            else throw new NoSuchElementException();

            return nextNode.getData();
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end PreorderIterator

    private class PostorderIterator implements Iterator<T> {
        private StackInterface<AVLTreeNode<T>> nodeStack;
        private AVLTreeNode<T> currentNode;

        public PostorderIterator() {
            nodeStack = new LinkedStack<>();
            currentNode = root;
        } // end default constructor

        public boolean hasNext()
        {
            return !nodeStack.isEmpty() || (currentNode != null);
        } // end hasNext

        public T next() {
            AVLTreeNode<T> leftChild, rightChild, nextNode = null;

            // Find leftmost leaf
            while (currentNode != null) {
                nodeStack.push(currentNode);
                leftChild = currentNode.getLeftChild();
                if (leftChild == null) currentNode = currentNode.getRightChild();
                else currentNode = leftChild;
            } // end while

            // Stack is not empty either because we just pushed a node, or
            // it wasn't empty initially since hasNext() is true.
            // But Iterator specifies an exception for next() in case
            // hasNext() is false.

            if (!nodeStack.isEmpty()) {
                nextNode = nodeStack.pop();
                // nextNode != null since stack was not empty before pop

                AVLTreeNode<T> parent = null;
                if (!nodeStack.isEmpty()) {
                    parent = nodeStack.peek();
                    if (nextNode == parent.getLeftChild()) currentNode = parent.getRightChild();
                    else currentNode = null;
                }
                else
                    currentNode = null;
            }
            else throw new NoSuchElementException();


            return nextNode.getData();
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end PostorderIterator

    private class InorderIterator implements Iterator<T> {
        private StackInterface<AVLTreeNode<T>> nodeStack;
        private AVLTreeNode<T> currentNode;

        public InorderIterator() {
            nodeStack = new LinkedStack<>();
            currentNode = root;
        } // end default constructor

        public boolean hasNext()
        {
            return !nodeStack.isEmpty() || (currentNode != null);
        } // end hasNext

        public T next() {
            AVLTreeNode<T> nextNode = null;

            // Find leftmost node with no left child
            while (currentNode != null) {
                nodeStack.push(currentNode);
                currentNode = currentNode.getLeftChild();
            } // end while

            // Get leftmost node, then move to its right subtree
            if (!nodeStack.isEmpty()) {
                nextNode = nodeStack.pop();
                // Assertion: nextNode != null, since nodeStack was not empty
                // before the pop
                currentNode = nextNode.getRightChild();
            }
            else throw new NoSuchElementException();

            return nextNode.getData();
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end InorderIterator

    private class LevelOrderIterator implements Iterator<T> {
        private QueueInterface<AVLTreeNode<T>> nodeQueue;

        public LevelOrderIterator() {
            nodeQueue = new LinkedQueue<>();
            if (root != null)
                nodeQueue.enqueue(root);
        } // end default constructor

        public boolean hasNext()
        {
            return !nodeQueue.isEmpty();
        } // end hasNext

        public T next() {
            AVLTreeNode<T> nextNode;

            if (hasNext()) {
                nextNode = nodeQueue.dequeue();
                AVLTreeNode<T> leftChild = nextNode.getLeftChild();
                AVLTreeNode<T> rightChild = nextNode.getRightChild();

                // Add to queue in order of recursive calls
                if (leftChild != null)
                    nodeQueue.enqueue(leftChild);

                if (rightChild != null)
                    nodeQueue.enqueue(rightChild);
            }
            else throw new NoSuchElementException();

            return nextNode.getData();
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end LevelOrderIterator
}
