
package net.gitHub.MultiSudoku.gui;


import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.Random;
import java.util.Stack;

import net.gitHub.MultiSudoku.GV.NumDistributuon;

public class GridGenerator implements Serializable
{

    public Grid grid;
    private Stack st;
    
    public GridGenerator() {
        grid = new Grid();
        st = new Stack();
    } 
    
    public GridGenerator(Grid grid) {
        this.grid = grid;
        st = new Stack();
    }

    public Grid getGrid() {
        return this.grid;
    }
    
    public void solve() {
        st.clear();
        Move m = grid.getFirstMove();
        grid.setField(m.getY(), m.getX(), m.getVal());
        st.push(m);
        solveIterative(m);
    }
    
    public void generate() {
        generateFirst9Moves();
        solve();
        for(int i = 0; i < 9; i++) {
            grid.setFieldDefault(i,0,false);
        }
    }
    
    
    public void generate(int openFields, NumDistributuon nD) {
        generate();

        int[] countNum = new int[9];
        int[][] countCube = new int[3][3];
        int min = openFields / 9;
                
        Random r = new Random();
        int x = 0, y = 0, cubeY = 0, cubeX = 0;

        //long time = System.currentTimeMillis();
        for (int j = 0; j < openFields;) {
            y = r.nextInt(9);
            x = r.nextInt(9);
            if(grid.isFieldDefault(y,x)) continue;
            
            switch (nD) {
            case random: break;
            case evenlyFilled3x3Cubes:
                cubeX = (x / 3);
                cubeY = (y / 3);
                if(countCube[cubeX][cubeY] >= min &&
                        !allHaveMinCount(countCube, min)) continue;
                countCube[cubeX][cubeY]++;
                break;
            case evenlyDistributedNumbers:
                if(countNum[grid.getField(y,x)-1] >= min 
                        && !allHaveMinCount(countNum, min)) continue;
                countNum[grid.getField(y,x)-1]++;
                break;
            }
            grid.setFieldDefault(y,x,true);            
            j++;
        }
        //time -= System.currentTimeMillis();
        //System.out.println("TIME TO GENERATE: " + time);
        grid.clearNoDefaultFields();
    }
    
    private boolean allHaveMinCount(int[] count,int min) {
        
        for (int i = 0; i < count.length; i++) {
            if(count[i] < min) return false;
        }
        
        return true;
    }
    
    private boolean allHaveMinCount(int[][] count,int min) {
        
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(count[i][j] < min) return false; 
            }   
        }
        return true;
    }
    
    
    
    private void generateFirst9Moves() {
        boolean[] b = new boolean[9];
        for(int i = 0; i < 9; i++) {
            int val;
            do {
                val = (int) (Math.random() * 9);
            } while(b[val]);
            grid.setField(i,0,val+1);
            grid.setFieldDefault(i,0,true);
            b[val] = true;
        }
    }
    
    private boolean solveIterative(Move m) {
        int x = m.getX(); 
        int y = m.getY();
        if(!grid.isGridValid()) {
            return false;
        }
        while(!grid.isGridSolved()) {
            Move next = grid.getNextMove(y,x);
            if(next != null) {
                grid.setField(next.getY(), next.getX(), next.getVal());
                st.push(next);
                y = next.getY();
                x = next.getX();
            } else {
                try{
                    next = (Move) st.pop();
                    while(next.setNextMove() == false) {
                        grid.setField(next.getY(), next.getX(), 0);
                        next = (Move) st.pop(); 
                    }
                    grid.setField(next.getY(), next.getX(), next.getVal());
                    st.push(next);
                    y = next.getY();
                    x = next.getX();
                } catch (EmptyStackException e) {
                    return false;
                } 
            }
        } 
        return true;
    }
    
    
    
    //--------------------------------------
    public boolean isGridValidGG()
    {
    	boolean b = grid.isGridSolved();
    	return b;
    }
    //----------------------------------------
    
    
    
    //DEBUG
    private void printArray(int[] a, String name) {
        System.out.print(name+": ");
        for(int i = 0; i < a.length; i++) {
            System.out.print(a[i]+";");
        }
        System.out.println();
    }
}
