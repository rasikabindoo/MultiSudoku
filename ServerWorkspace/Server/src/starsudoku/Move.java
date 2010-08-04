/* Move created on 30.01.2006 */
//package net.sourceforge.starsudoku;

package starsudoku;

import java.io.Serializable;

public class Move implements Serializable {
    
    private int y;
    private int x;
    private int valIndex;
    private int[] availabeMoves;
    
    public Move(int y, int x, int[] availabeMoves, int valIndex) {
        this.y = y;
        this.x = x;
        this.availabeMoves = availabeMoves;
        this.valIndex = valIndex;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int[] getAvailabeMoves() {
        return availabeMoves;
    }
    
    public int getValIndex() {
        return valIndex;
    }
    
    public int getVal() {
        return availabeMoves[valIndex];
    }
    
    public boolean setNextMove() {
        
        valIndex++;
        if(valIndex <= availabeMoves.length-1) {
            return true;
        }
        valIndex--;
        return false;
    }
    
    public String toString() {
        return "x: "+x+"; y: "+y+"; val: " + getVal();
    }
}
