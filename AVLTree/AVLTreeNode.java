package AVLTree;

public class AVLTreeNode <T> extends BinaryNode <T>{

    private T data;
    private AVLTreeNode<T> leftChild;
    private AVLTreeNode<T> rightChild;

    public AVLTreeNode(){this(null);}

    public AVLTreeNode(T data){
        this(data,null,null);
    }
    public AVLTreeNode(T dataStuff, AVLTreeNode<T> newLeftChild, AVLTreeNode<T> newRightChild){
        data = dataStuff;
        leftChild = newLeftChild;
        rightChild = newRightChild;
    }
    /**
     * The following are not overloaded and take from the BinaryNode class:
     * public T getData();
     * public void setData();
     *
     */
    // Just the Left Child things <3
    public AVLTreeNode<T> getLeftChild(){return leftChild;}
    public void  setLeftChild(AVLTreeNode<T> newLeftChild){leftChild = newLeftChild;}

    // Right Child
    public AVLTreeNode<T> getRightChild(){return rightChild;}
    public void setRightChild(AVLTreeNode<T> newRightChild){rightChild = newRightChild;}

    public AVLTreeNode<T> copy(){
        AVLTreeNode<T> newRoot = new AVLTreeNode<T>(data);
        if (leftChild != null) newRoot.setLeftChild(leftChild.copy());

        if (rightChild != null) newRoot.setRightChild(rightChild.copy());

        return newRoot;
    }
    public int getHeight(AVLTreeNode<T> node){
        int height = 0;
        if (node != null)
            height = 1 + Math.max( getHeight( node.getLeftChild() ), getHeight( node.getRightChild() ) );
        return height;
    }
}
