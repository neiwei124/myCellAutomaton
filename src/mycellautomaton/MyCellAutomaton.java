
package mycellautomaton;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static mycellautomaton.MyCellAutomaton.gSize;

/**
 *
 * @author Neien
 */
public class MyCellAutomaton extends JFrame{

    static int numCells = 200; //number of cells i want
    static int days = 1;  //number of days passed
    final int explosionPercentage = 0; //units in %
    int borderWidth = 100;  //borderwidth value
    static int gSize = 800; //gridsize
    static boolean winnerDeclared = false;
    int kingHP = 16;  //king's hp
    Color[] dHp = new Color[kingHP];  //king's hp bars
    Color[] gHp= new Color[kingHP];
    int dynDmg = 0; //total damage each kings took
    int gusDmg = 0;
    Troops dynTroops;  //dyn tribe
    Troops gusTroops;  //gus tribe
    Troops winner;
    Troops loser;
    int CellSize = (gSize-2*borderWidth)/numCells;  //the cellsize of each cells
    
    static int sleepTimer = 100; //sleep timer


    //methods
    //sleep timer
    public void sleep( int numMilliseconds ) {
        try {
            Thread.sleep( numMilliseconds );
        } 
        
        catch (Exception e) {
        }
    }
    
    //pastes troops on screen
    public void troopsOnScreen(){
        Troops.clear(Troops.pasteCell);
        Troops.pasteTroops(dynTroops, gusTroops);        
    }

    //initialize armies
    public void getMoreArmy() {
        fillTroops();
        troopsOnScreen();
    }

    //creates 2 kings
    public void drawKing() {
        Soldier s = new Soldier(numCells/2-2, numCells-4, Color.red);
        King dyn = new King(s, 4);
        dynTroops.appendKing(dyn);
        dynTroops.createKing(dyn, s.x, s.y);
        
        
        Soldier s1 = new Soldier(numCells/2-2, 0, Color.cyan);
        King gus = new King(s1, 4);
        gusTroops.appendKing(gus);
        gusTroops.createKing(gus, s1.x, s1.y);
 
    }
    
    //initialize soldiers
    public void drawSoldier(Troops t, int limit, int min, int max, Color c){
        Random r = new Random();
        int i, j;
        for (int x = 0; x < limit; x++) {
            i = r.nextInt(max-min) + min;
            j = r.nextInt(numCells-5);
            Soldier s1 = new Soldier(j, i, c);
            t.appendSoldier(s1);
            t.updateUnits(s1, j, i);
            j++;
        }
    }
    
    //creates tanks
    public Tank createTank(int j, int i, Color c) {
        Soldier s1 = new Soldier(j, i, c);
        Tank t = new Tank(s1, 2);
        return t;
    }
    
    //initialize tanks
    public void drawTanks(Troops t, int limit, int min, int max, Color c){
        Random r = new Random();
        int i, j;
        for (int x = 0; x < limit; x++) {
            i = r.nextInt(max-min) + min;
            j = r.nextInt(numCells-5);
            Tank t1 = createTank(j, i, c);
            t.appendTanks(t1);
            t.updateTanks(t1, j, i);
        }
    }
    
    //calls the methods to initialize soldiers and tanks
    public void fillTroops() {
        Random r = new Random();
        drawKing();
        //number of troops and their positions
        int sNum = r.nextInt(10) + 5;
        int tNum = r.nextInt(10) + 3;
        
        int dynSMin = 175, dynSMax = 195, dynTMin = 150, dynTMax = 170;
        int gusSMin = 1, gusSMax = 25, gusTMin = 30, gusTMax = 50;
        
        //dyn troops
        drawSoldier(dynTroops, sNum, dynSMin, dynSMax, Color.red);
        drawTanks(dynTroops, tNum, dynTMin, dynTMax, Color.red);
        
        //gus troops
        drawSoldier(gusTroops, sNum, gusSMin, gusSMax, Color.cyan);
        drawTanks(gusTroops, tNum, gusTMin, gusTMax, Color.cyan);

    }
    
    //make sure troops dont go over the cell boundaries
    public void checkBoundary(Troops troops){
        for (int i = 0; i < troops.tanks.size(); i++) {
            if ((troops.tanks.get(i).a.y == 0) || (troops.tanks.get(i).a.y+1 == numCells-1)) {
                troops.tanks.remove(troops.tanks.get(i));
                i--;
            }            
        }
        
        for (int i = 0; i < troops.soldiers.size(); i++) {
            if ((troops.soldiers.get(i).y == 0) || (troops.soldiers.get(i).y == numCells-1)) {
                troops.soldiers.remove(troops.soldiers.get(i));
                i--; 
            }
        }        
    }
    
    //calls methods to fill the next day troop position
    public void getNextDay() {
        int start = 0, m1 = numCells/2-2, m2 = numCells/2+2, end = numCells;
        
        setSoldierFightingState();
        checkBoundary(dynTroops);
        checkBoundary(gusTroops);
        checkExplosion(dynTroops);
        checkExplosion(gusTroops);
        kingAndTankCheck(dynTroops, gusTroops);
        kingAndTankCheck(gusTroops, dynTroops);
        killTank(dynTroops);
        killTank(gusTroops);
        soldierCheck(dynTroops, gusTroops);
        soldierCheck(gusTroops, dynTroops);
        updateHpBar(dHp, dynDmg);
        updateHpBar(gHp, gusDmg);
        Troops.clear(dynTroops.troopCell);
        Troops.clear(gusTroops.troopCell);
        drawKing();
        dynTroops.moveTroops(start, m1, m2, end);
        gusTroops.moveTroops(start, m1, m2, end);
        
    }
    
    //updates king's hp bar
    public void updateHpBar(Color[] c, int dmg){
        int a = 0;
        for (int i = 0; i < c.length; i++) {
            if (a < dmg) {
                c[i] = Color.yellow;
                a++;
            }
        }
    }
    
    //see if each tank explodes or not
    public void checkExplosion(Troops troops){
        Random r = new Random();
        int rNum;
        for (int i = 0; i < troops.tanks.size(); i++) {
            rNum = r.nextInt(100)+1;
            if (rNum <= explosionPercentage) {
                troops.tanks.get(i).tankState = false;
            }
        }
    }
    
    //reset soldier's fighting state for each day
    public void setSoldierFightingState(){
        for (int i = 0; i < dynTroops.soldiers.size(); i++) {
            dynTroops.soldiers.get(i).isFighting = false;
        }
        
        for (int i = 0; i < gusTroops.soldiers.size(); i++) {
            gusTroops.soldiers.get(i).isFighting = false;
        }
    }
    
    //king and tank check for their surroundings
    public void kingAndTankCheck(Troops ally, Troops enemy){
        //checks if kings took damage or not
        int dmg = ally.kings[0].checkSurroundings(enemy);
        if (ally.kings[0].kingCells[0][0].color == Color.red) {
            dynDmg += dmg;
        }
        else{
            gusDmg += dmg;
        }
        
        //tanks check their surroundings
        for (int i = 0; i < ally.tanks.size(); i++) {
            ally.tanks.get(i).checkSurroundings(ally);
            ally.tanks.get(i).checkSurroundings(enemy);    
        }
    }
    
    //soldiers check for their surroundings
    public void soldierCheck(Troops ally, Troops enemy){
        //checks if any soldiers of the same sides are overlapping each other
        for (int i = 0; i < ally.soldiers.size(); i++) {
             ally.soldiers.get(i).getOverlappingCells(ally, i);
        }
        
        for (int i = 0; i < ally.soldiers.size(); i++) {
            if (ally.soldiers.get(i).isOverlapping) {
                Troops.remove(ally.soldiers.get(i), ally);
                i--;
            }
        }

        //soldiers fight!
        for (int i = 0; i < ally.soldiers.size(); i++) {
            ally.soldiers.get(i).checkSurroundings(ally, enemy);
        } 
    }
    
    //method to kill tanks
    public void killTank(Troops troops){
        for (int i = 0; i < troops.tanks.size(); i++) {
                if (troops.tanks.get(i).tankState == false) {
                    troops.killTanks(troops.tanks.get(i), troops.tanks.get(i).a.x, troops.tanks.get(i).a.y);
                    i--;
            }
        }

    }

    //paint stuff
    @Override
    public void paint(Graphics g) {
        Image img = getImage();
        g.drawImage(img, 0, 0, rootPane);
    }
    
    //paints hp bar
    public void drawHPBar(Color[] c, Graphics g2, int x, int y){
        int xPos = x, yPos = y;
        int Size = (CellSize*numCells)/c.length;
        for (int i = 0; i < c.length; i++) {
                g2.setColor(c[i]);
                g2.fillRect(xPos, yPos, Size, Size);
                g2.setColor(Color.black);
                g2.drawRect(xPos, yPos, Size, Size);
                xPos += Size;
            }
             
    }
    
    //paints buffered images
    public Image getImage(){
        
        BufferedImage bi = new BufferedImage(gSize, gSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        drawHPBar(dHp, g2, borderWidth, gSize - (borderWidth - (borderWidth/2)));
        drawHPBar(gHp, g2, borderWidth, (borderWidth - (borderWidth/2)));
        g2.setColor(Color.magenta);
        g2.drawString("Vod Gus HP:", 0, 65);
        g2.drawString("Puu Dyn HP:", 0, 770);
        g2.drawString("Day: " + days, 730, 50);
        
        int xPos, yPos = borderWidth;
        
        for (int i = 0; i < Troops.pasteCell.length; i++) {
            xPos = borderWidth;
            
            for (int j = 0; j < Troops.pasteCell[0].length; j++) {
                if (Troops.pasteCell[i][j] != null) {
                    g2.setColor(Troops.pasteCell[i][j].color);
                }
                
                else {
                    g2.setColor(Color.black);
                }
                
                g2.fillRect(xPos, yPos, CellSize,CellSize);
                g2.setColor(Color.black);
                g2.drawRect(xPos, yPos, CellSize,CellSize);
                xPos += CellSize;
            }
            yPos += CellSize;  
        }
        
        //print victory message if theres a winner
        if (winnerDeclared) {
            
            String tribeName, victoryMsg;
            if (winner == gusTroops) {
                g2.setColor(Color.cyan);
                tribeName = "Gus";
                victoryMsg = "The era of butter side up is over!";
            }
            else{
                g2.setColor(Color.red);
                tribeName = "Dyn";
                victoryMsg = "Butter side down no more! Hoorah!";
            }
            
            g2.drawString("The " + tribeName + " Tribe has won! " + victoryMsg, gSize/2-150, gSize/2);  
        }
        return bi;
    }
    
    //initialize king's hp bars
    public void initializeHpBar(Color[] c){
        for (int i = 0; i < c.length; i++) {
            c[i] = Color.green;
        }
    }
    
    //initialize all the beginning stuff in main
    public void initializeStuff(){
        dynTroops = new Troops(numCells);
        gusTroops = new Troops(numCells);
        initializeHpBar(dHp);
        initializeHpBar(gHp);

        setTitle("My Cell Automaton");
        setSize(gSize, gSize);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(  Color.black  );
    }
    
    //count king's hp
    public int countHp(Color[] c){
        int hp = 0;
        for (int i = 0; i < c.length; i++) {
            if (c[i] == Color.green) {
                hp++;
            } 
        }
        return hp;
    }
    
    //check if theres a winner or not
    public boolean checkWinner(){
        int dynHp = countHp(dHp), gusHp = countHp(gHp);
        if (dynHp == 0 || gusHp == 0) {
            winnerDeclared = true;
            if (dynHp == 0) {
                winner = gusTroops;
                loser = dynTroops;
            }
            else{
                winner = dynTroops;
                loser = gusTroops;
            }
        }
        return winnerDeclared;
    }
    
    public void killKing(Troops loser) {
        loser.kings[0].destroy();
        
    }
    
    //main method
    public static void main(String[] Args) {
            
            MyCellAutomaton mca = new MyCellAutomaton();
            
            //draws first frame
            mca.initializeStuff();
            mca.getMoreArmy();
            mca.setVisible(true);
            mca.sleep(sleepTimer);

            //loop to draw unlimited number of days until a winner has been declared
            while(true) {
                if (mca.checkWinner()) {
                    break;
                }
                if (days%16 == 0) {
                    mca.getMoreArmy();
                }
                mca.getNextDay();  
                mca.troopsOnScreen();
                mca.repaint();    
                mca.sleep(sleepTimer);
                days++;
            }
            //paints victory message along
            mca.killKing(mca.loser);
            mca.repaint();
    }

}
