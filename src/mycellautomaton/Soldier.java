/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mycellautomaton;

import java.awt.Color;
import java.util.Random;

public class Soldier {
    int x;
    int y;
    boolean state;
    boolean isFighting;
    boolean isASoldier;
    boolean isOverlapping;
    Color color;
    int[] xMove = {-1, 0, 1}; //move choices for soldier
    int[] yMove = {0, -1};
    
    //create a soldier
    public Soldier(int j, int i, Color c) {
        this.x = j;
        this.y = i;
        this.isFighting = false;
        this.state = true;
        this.color = c;
        this.isASoldier = true;
        this.isOverlapping = false;
        
    }
    
    //makes soldiers move
    public void move(int start, int m1, int m2, int end){
        Random r = new Random();
        if (color == Color.cyan)
            yMove[1] = 1;
        
        int xChoice = getXMove(start, m1, m2, end), yNum = r.nextInt(101), yChoice;
        
        if (yNum > 30 || xChoice == 1) {
            yChoice = 1;
        }
        
        else {
            yChoice = 0;
        }
        
        this.x += xMove[xChoice];
        this.y += yMove[yChoice];
        
    }
    
    //figure out what its xposition move should be
    public int getXMove(int start, int m1, int m2, int end){
        Random r = new Random();
        int xNum, rNum;
        if (this.x < m1) {
            rNum = r.nextInt(101);
            if (rNum > 37) 
                xNum = 2;
            else
                xNum = 1;
        }
        else if (this.x < m2) {
            xNum = r.nextInt(3);
        }
        else{
            rNum = r.nextInt(101);
            if (rNum > 37) 
                xNum = 0;
            else
                xNum = 1;
        }
        return xNum;
    }
    
    //see if any soldiers of the same side are overlapping each other
    public void getOverlappingCells(Troops troops, int a) {
        for (int i = 0; i < troops.soldiers.size(); i++) {
            if (a != i) {
                if (troops.soldiers.get(i).x == troops.soldiers.get(a).x && troops.soldiers.get(i).y == troops.soldiers.get(a).y) {
                    troops.soldiers.get(i).isOverlapping = true;
                    troops.soldiers.get(a).isOverlapping = true;
                }
                
            }
            
        }
    }
    
    //checks for nearby soldiers
    public void checkSurroundings(Troops ally, Troops enemy){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = 0; k < enemy.soldiers.size(); k++) {
                    if (enemy.soldiers.get(k).isFighting == false && this.isFighting == false) {
                        //fights enemy soldiers if one is nearby
                        if (enemy.soldiers.get(k).x == (this.x+j) && enemy.soldiers.get(k).y == (this.y+i)) {
                            this.isFighting = true;
                            enemy.soldiers.get(k).isFighting = true;
                            Soldier loser = Troops.fight(this, enemy.soldiers.get(k));
                            //loser dies
                            if (loser == this) {
                                Troops.remove(this, ally);
                            }
                            else{
                                Troops.remove(enemy.soldiers.get(k), enemy);
                            }
                        }
                    }  
                }  
            } 
        }
    }
    

}