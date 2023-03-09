package AVLTree;

import java.util.*;


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

    public String treeToString(AVLTreeNode<E> subtreeRoot) {
        if (subtreeRoot == null) return "(empty tree)";

        // First convert the tree to an array of line strings
        String[] lines = treeToLines(subtreeRoot);

        // Combine all lines into 1 string
        String treeString = lines[0];
        for (int i = 1; i < lines.length; i++)
            treeString += ("\n" + lines[i]);

        return treeString;
    }

    private String[] treeToLines(AVLTreeNode<E> subtreeRoot) {
        if (subtreeRoot == null) return new String[0];


        // Make a string with the subtreeRoot's key enclosed in brackets
        String rootString = "[" + subtreeRoot.element + "]";
        int rootStrLen = rootString.length();

        // Case 1: subtreeRoot is a leaf
        if (subtreeRoot.left == null && subtreeRoot.right == null) {
            String[] oneLine = new String[1];
            oneLine[0] = rootString;
            return oneLine;
        }

        // Recursively make line strings for each child
        String[] leftLines = treeToLines((AVLTreeNode<E>) subtreeRoot.left);
        String[] rightLines = treeToLines((AVLTreeNode<E>) subtreeRoot.right);

        int lineCount = Math.max(leftLines.length, rightLines.length);
        String[] allLines = new String[lineCount + 2];

        // Case 2: subtreeRoot has no left child
        if (subtreeRoot.left == null) {
            // Create the first 2 lines, not yet indented
            allLines[0] = rootString;
            allLines[1] = getSpaces(rootStrLen) + "\\";

            // Find where the right child starts
            int rightChildIndent = rightLines[0].indexOf('[');

            // Goal: Indent lines appropriately so that the parent's right branch
            // character ('\') matches up with the right child's '['.

            if (rightChildIndent <= rootStrLen) {
                // Indent all lines below
                indentLines(rightLines, rootStrLen - rightChildIndent);
            }
            else {
                // Indent first 2 lines
                String indent = getSpaces(rightChildIndent - rootStrLen);
                allLines[0] = indent + allLines[0];
                allLines[1] = indent + allLines[1];
            }

            // Copy rightLines into allLines starting at index 2
            System.arraycopy(rightLines, 0, allLines, 2, rightLines.length);

            return allLines;
        }

        // Case 3: subtreeRoot has no right child
        if (subtreeRoot.right == null) {
            // Goal: Indent lines appropriately so that the parent's left branch
            // character ('/') matches up with the left child's ']'.

            // Create the first 2 lines
            String indent = getSpaces(leftLines[0].indexOf(']'));
            allLines[0] = indent + " " + rootString;
            allLines[1] = indent + "/";

            // Copy leftLines into allLines starting at index 2
            System.arraycopy(leftLines, 0, allLines, 2, leftLines.length);

            return allLines;
        }

        // Case 4: subtreeRoot has both a left and right child

        // The goal is to have the two child nodes as close to the parent as
        // possible without overlap on any level.

        // Compute absolute indentation, in number of spaces, needed for right lines
        int indentNeeded = 0;
        if (rightLines.length > 0) {
            // Indent should at least get the immediate right child to be to the
            // right of the root
            indentNeeded = Math.max(0,
                    leftLines[0].length() + rootString.length() - rightLines[0].indexOf('['));
        }
        for (int i = 0; i < leftLines.length && i < rightLines.length; i += 2) {
            // Lines with branches are skipped, so the line of interest has only
            // nodes. The difference between where the left line ends and the
            // right line begins should be at least 3 spaces for clarity.
            int leftEnd = leftLines[i].lastIndexOf(']');
            int rightBegin = rightLines[i].indexOf('[');

            int forThisLine = leftLines[i].length() + 3 - rightBegin;
            indentNeeded = Math.max(indentNeeded, forThisLine);
        }

        // Build final lines in allLines starting at index 2
        String absoluteIndent = getSpaces(indentNeeded);
        for (int i = 0; i < leftLines.length || i < rightLines.length; i++) {
            // If no right line, just take the left
            if (i >= rightLines.length) {
                allLines[2 + i] = leftLines[i];
            }
            else {
                String left = "";
                if (i < leftLines.length) {
                    left = leftLines[i];
                }
                String right = absoluteIndent + rightLines[i];
                allLines[2 + i] = left + right.substring(left.length());
            }
        }

        // The first 2 lines remain. allLines[2] has the proper string for the
        // 2 child nodes, and thus can be used to create branches in allLines[1].
        int leftIndex = allLines[2].indexOf(']');
        int rightIndex = allLines[2].lastIndexOf('[');
        allLines[1] = getSpaces(leftIndex) + "/" +
                getSpaces(rightIndex - leftIndex - 1) + "\\";

        // The space between leftIndex and rightIndex is the space that
        // subtreeRoot's string should occupy. If rootString is too short, put
        // underscores on the sides.
        rootStrLen = rightIndex - leftIndex - 1;
        if (rootString.length() < rootStrLen) {
            int difference = rootStrLen - rootString.length();
            String underscores = getRepeated('_', difference / 2);
            if (difference % 2 == 0) {
                rootString = underscores + rootString + underscores;
            }
            else {
                rootString = underscores + rootString + underscores + "_";
            }
        }
        allLines[0] = getSpaces(leftIndex + 1) + rootString;

        return allLines;
    }

    private String getRepeated(char toRepeat, int numberOfTimes) {
        if (numberOfTimes <= 0) return "";

        char[] chars = new char[numberOfTimes];
        for (int i = 0; i < numberOfTimes; i++)
            chars[i] = toRepeat;

        return new String(chars);
    }

    private String getSpaces(int numberOfSpaces) {return getRepeated(' ', numberOfSpaces);}

    private void indentLines(String[] lines, int numberOfSpaces) {
        if (numberOfSpaces <= 0) return;

        // Prepend indentation to each line
        String indent = getSpaces(numberOfSpaces);
        for (int i = 0; i < lines.length; i++)
            lines[i] = indent + lines[i];

    }

    protected static class AVLTreeNode<E> extends BST.TreeNode<E>{
        protected int height = 0;
        public AVLTreeNode(E o){
            super(o);
        }
    }
}
