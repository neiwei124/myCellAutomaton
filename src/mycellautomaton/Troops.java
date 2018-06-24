/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mycellautomaton;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import static mycellautomaton.Troops.pasteCell;

public class Troops {
    Soldier[][] troopCell;
    ArrayList<Soldier> soldiers;
    ArrayList<Tank> tanks;
    King[] kings;
    static Soldier[][] pasteCell;
    static Soldier[][] deadCell;
    

    //creates soldier, tank, deadcell, king, troopcells arraylist for both troops sides
    public Troops(int nc){
        troopCell = new Soldier[nc][nc];
        soldiers = new ArrayList();
        tanks = new ArrayList(); 
        kings = new King[1];
        pasteCell = new Soldier[nc][nc];
        deadCell = new Soldier[nc][nc];
        
        
    }
    
    //pastes both sides of troops onto this array here so both troops are visible on screen
    public static void pasteTroops(Troops dyn, Troops gus){
        for (int i = 0; i < pasteCell.length; i++) {
            for (int j = 0; j < pasteCell[0].length; j++) {
                if (deadCell[i][j] != null) {
                    pasteCell[i][j] = deadCell[i][j];
                }
                
                if (dyn.troopCell[i][j] != null ) {
                    pasteCell[i][j] = dyn.troopCell[i][j];
                }
                
                if (gus.troopCell[i][j] != null) {
                    pasteCell[i][j] = gus.troopCell[i][j];
                }
 
            }
            
        }
    }
    
    //add soldiers onto soldier array list
    public void appendSoldier(Soldier s){
        soldiers.add(s);
    }
    
    //add tanks onto tank array list
    public void appendTanks(Tank t){
        tanks.add(t);
    }
    
    //add kings onto king array
    public void appendKing(King k){
        kings[0] = k;
    }
    
    //update troop's position
    public void updateUnits(Soldier s, int j, int i){
        if (s.state){
            troopCell[i][j] = s;
        }
        else {
            removeUnits(s, i, j);
        }
    }

    //removes the living cell's position and replaces with dead cell's 
    public void removeUnits(Soldier s, int j, int i){
        deadCell[i][j] = troopCell[i][j];
        troopCell[i][j] = null;
    }
    
    //update tank position
    public void updateTanks(Tank t, int j, int i){
        for (int a = 0; a < t.tankCells.length; a++) {
            for (int b = 0; b < t.tankCells[0].length; b++) {
                updateUnits(t.tankCells[a][b], j+b, i+a);       
            }
        }
    }
    
    //draws the king
    public void createKing(King k, int j, int i){
        for (int a = 0; a < k.kingCells.length; a++) {
            for (int b = 0; b < k.kingCells[0].length; b++) {
                updateUnits(k.kingCells[a][b], j+b, i+a);            
            }       
        }  
    }
    
    //method to move troops position
    public void moveTroops(int start, int m1, int m2, int end){
        int i;
        for (i = 0; i < soldiers.size(); i++) {
            soldiers.get(i).move(start, m1, m2, end);
            updateUnits(soldiers.get(i), soldiers.get(i).x, soldiers.get(i).y); 
        }
        
        for (i = 0; i < tanks.size(); i++) {
            tanks.get(i).move(); 
            updateTanks(tanks.get(i), tanks.get(i).a.x, tanks.get(i).a.y);
        }
    }
    
    //resets array for painting reasons
    public static void clear(Soldier[][] t){
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                t[i][j] = null; 
            } 
        }
    }
    
    //kills the inputted tank
    public void killTanks(Tank t, int x, int y){
        for (int i = 0; i < t.tankCells.length; i++) {
            for (int j = 0; j < t.tankCells[0].length; j++) {
                  t.tankCells[i][j].color = Color.yellow;
                  deadCell[y+i][x+j] = t.tankCells[i][j];
                    } 
                }
       tanks.remove(t);
    }
    
    //method to make soldier fight
    public static Soldier fight(Soldier a, Soldier b){
        Random r = new Random();
        int aNum = r.nextInt(50);
        int bNum = r.nextInt(50);
        if (aNum > bNum) return b;
        else if (bNum > aNum) return a;
        return fight(a,b);
    }
    
    //kill soldiers
    public static void remove(Soldier s, Troops troops){
        s.color = Color.yellow;
        s.state = false;
        Troops.deadCell[s.y][s.x] = s;
        troops.soldiers.remove(s);
    }
}
