/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication216;
/**
 *
 * @author xiexinyu
 */
import java.util.Scanner;
public class Holdblock {
String [][]block;
        Node head;

        public Holdblock() {
            head=null;
        }
        public void dispaly(){
            Node node=head;
            System.out.println("Show block");
            while(node!=null){;
                node.display();
                node=node.next;
            }
        }
         private void addNode1(int[][] block) {
       
    }
        public void addNode(int [][]block){
            Node current=head;
            if(head==null){
                Node node=new Node(block);
                node.next=head;
                head=node;
        }
    }
        public void removeNode(){
            Node current=head;
            if(head.next==null){
                head=head.next;
            }
        }
    

    public void holdblock(int [][]block){// store current block
            Scanner sc=new Scanner(System.in);
        System.out.println("whether you want to hold this block");
        System.out.println("1-yes,2-no");
        int a=sc.nextInt();
        if(a==1){
            addNode1(block);
        }
        else if(a==2){
        System.out.println("need connect");
    }
    }
        public void useblock(int [][]block){ //if you want to use block stored ,and continue to store block.
            Scanner sc=new Scanner(System.in);
        System.out.println("do you want to use the block stored ? ");
        System.out.println("1-yes,2-no");
        int a=sc.nextInt();
        if(a==1){
            removeNode();
            addNode(block);
        }
        else if(a==2){
        System.out.println("need connect " );
    }
    }
                



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Holdblock H=new Holdblock();
        int[][] salutation = {
            {1, 2, 3},
            {5}};
        H.addNode(salutation);
     
        H.dispaly();
    }
       
    
    
}
