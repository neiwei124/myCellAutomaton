/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mycellautomaton;

import java.awt.Color;

public class Tank {
    Soldier a;
    Soldier[][] tankCells;
    int moveSpace = -1;
    boolean isATank;
    boolean tankState;
    
    //create a tank
    public Tank(Soldier s1, int s){
        this.a = s1;
        this.tankCells = new Soldier[s][s];
        this.isATank = true;
        this.tankState = true;
        for (int i = 0; i < tankCells.length; i++) {
            for (int j = 0; j < tankCells.length; j++) {
                tankCells[i][j] = a;
            }
        }
    }
    
    //move options for a tank
    public void move(){
        if (a.color == Color.cyan)
            moveSpace = 1;
        
        a.y += moveSpace;
    }
    
    //checks tank's surroundings
    public void checkSurroundings(Troops troops){
        int m, n=-1;
        boolean TankDead;
        for (int i = 0; i < 4; i++) {
            if (i%2 == 0) {
                m = -1;   
            }
            
            else{
                m = 1;
            }
            
            if (i >= 2) {
                n = 1;
            }
            
            killSoldiers(troops, this.tankCells[0][0].x+m, this.tankCells[0][0].y+n);
            TankDead = checkTankState(troops, this.tankCells[0][0], this.tankCells[0][0].x+m, this.tankCells[0][0].y+n);
            
            if (TankDead == true) {              
                this.tankState = false;
            }
        } 
    }
    
    //kills any soldiers nearby
    public void killSoldiers(Troops troops, int x, int y){
        for (int i = 0; i < tankCells.length; i++) {       
            for (int j = 0; j < tankCells[0].length; j++) {
                    for (int k = 0; k < troops.soldiers.size(); k++) {
                        if ((x+j) == troops.soldiers.get(k).x && (y+i) == troops.soldiers.get(k).y) {
                            Troops.remove(troops.soldiers.get(k), troops);        
                        }
                    }   
                }  
            }
        }
    
    //checks if tank should be dead or not
    public boolean checkTankState(Troops troops, Soldier s, int x, int y){
        boolean flag = false;
        for (int i = 0; i < tankCells.length; i++) { 
            for (int j = 0; j < tankCells[0].length; j++) {
                
                for (int k = 0; k < troops.tanks.size(); k++) {
                    for (int l = 0; l < tankCells.length; l++) {
                        for (int m = 0; m < tankCells[0].length; m++) {
                            if (((troops.tanks.get(k).isATank) 
                                && troops.tanks.get(k).a != this.a && 
                                    (troops.tanks.get(k).a.x+m) == (x+j) && (y+i) == (troops.tanks.get(k).a.y+l))) {
                                flag = true;
                            }
                        }
                    }
                }
            }
        }
        return flag;
    }
    
    
}