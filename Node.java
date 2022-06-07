/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication216;

import java.util.Arrays;

/**
 *
 * @author xiexinyu
 */
public class Node {
    Node next;
    int [][]block;

    public Node(int[][] block) {
        this.block = block;
    }
    
    public void display(){
        System.out.println(Arrays.deepToString(block));
        
    }

    @Override
    public String toString() {
        return "Node{" + "block=" + Arrays.deepToString(block) + '}';
    }

    
    
    
}
