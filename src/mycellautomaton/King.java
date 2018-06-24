/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mycellautomaton;

import java.awt.Color;

public class King {
    Soldier[][] kingCells;
    
    //create king
    public King(Soldier s1, int s){
        this.kingCells = new Soldier[s][s];
        for (int i = 0; i < kingCells.length; i++) {
            for (int j = 0; j < kingCells[0].length; j++) {
                kingCells[i][j] = s1;
            }
        }   
    }
    
    //king checks its surroundings
    public int checkSurroundings(Troops enemy){
        int dmg = 0;
        Tank[] tankHit = new Tank[enemy.tanks.size()];
        
        for (int i = 0; i < kingCells.length; i++) {
            for (int j = 0; j < kingCells[0].length; j++) {
                //checks for any enemy soldiers on the king
                for (int k = 0; k < enemy.soldiers.size(); k++) {
                    if (enemy.soldiers.get(k).x == kingCells[i][j].x+j && enemy.soldiers.get(k).y == kingCells[i][j].y+i) {
                        Troops.remove(enemy.soldiers.get(k), enemy);
                        dmg++;
                    }   
                }
                
                //checks for any enemy tanks on the king
                for (int k = 0; k < enemy.tanks.size(); k++) {
                    
                    for (int l = 0; l < enemy.tanks.get(k).tankCells.length; l++) {
                        for (int m = 0; m < enemy.tanks.get(k).tankCells[0].length; m++) {
                            if (enemy.tanks.get(k).tankCells[l][m].x+m == kingCells[i][j].x+j 
                                    && enemy.tanks.get(k).tankCells[l][m].y+l == kingCells[i][j].y+i) {
                                enemy.tanks.get(k).tankState = false;
                                tankHit[k] = enemy.tanks.get(k);
                            } 
                        }   
                    }   
                } 
            } 
        }
        //calculates tank damage it took
        for (int i = 0; i < tankHit.length; i++) {
            if (tankHit[i] != null) {
                dmg += 4;
            }
        }
        return dmg;
    }
    
    public void destroy(){
        for (int i = 0; i < kingCells.length; i++) {
            for (int j = 0; j < kingCells[0].length; j++) {
                kingCells[i][j].color = Color.yellow;
                
            }
            
        }
    }
}
