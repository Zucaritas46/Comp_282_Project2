package AVLTree;

import java.util.Scanner;

public class Project_2_282 {
    private final static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        // create the binary tree
        AVLTree avlt = new AVLTree<>();
        boolean keepGoing = true;
        int userIn = -1;

        while(keepGoing){
            System.out.println("What would you like to do?");
            System.out.println("1. Add number");
            System.out.println("2. Delete number");
            System.out.println("0. Quit");
            try{
                userIn = scanner.nextInt();
                scanner.nextLine();
            }catch(Exception e){
                System.out.println("Must be an Integer! ");
            }
            switch (userIn){
                case 1:
                    avlt.insert(0);
                    break;
                case 2:
                    System.out.println();
                    avlt.delete(0);
                    break;
                case 0:
                    keepGoing = false;
                    break;
                default:
                    System.out.println("Invalid Entry ");
            }
        }




    }
}
