/* FieldVals created on 12.07.2006 */
package net.sourceforge.starsudoku.gui;

import java.util.Arrays;
import java.util.Observable;

import net.sourceforge.starsudoku.io.SerFieldVals;



public class FieldVals extends Observable {
    
    private int[] helps;
    private int val;
    private int x;
    private int y;
    private boolean isEditable;
    
    public FieldVals(int y, int x, boolean isEditable, SudFrame sf) {
        this.x = x;
        this.y = y;
        helps = new int[8];
        Arrays.fill(helps, Integer.MAX_VALUE);
        val = 0;
        addObserver(sf);
        this.isEditable = isEditable;
    }
    
    public void setEditable(boolean b) {
        isEditable = b;
    }
    
    public boolean isEditable() {
        return isEditable;
    }
    
    public void setInitVal(int val) {
        if(!isEditable) return;
        if(val < 0 || val> 9) 
            throw new IllegalArgumentException("Invalid value...");
        
        this.val = val;
        delAllHelps();
        
    }
    
    public void setVal(int val) {
        if(!isEditable) return;
        if(val < 0 || val> 9) {
            throw new IllegalArgumentException("Invalid value...");
        }
        
        if(this.val == val) { //DELETE
            this.val = 0;
        } else {
            this.val = val;
            checkHelpsAfterNewVal();
        }
        
        setChanged();
        notifyObservers(this);
    }
    
    public int getX() {return x;}
    public int getY() {return y;}
    
    public int getVal() {
        return val;
    }
    
    public void delVal() {
        if(!isEditable) return;
        val = 0;
    }
    
    public void addHelp(int hval) {
        if(!isEditable) return;
        if(hval < 1 || hval> 9) {
            throw new IllegalArgumentException("Invalid help value...");
        }
        if(hval == val) return;
        int index = Arrays.binarySearch(helps, hval);
        if(index >= 0) { //DELETE
            helps[index] = Integer.MAX_VALUE;
        } else {
            index = Arrays.binarySearch(helps, Integer.MAX_VALUE);
            if(index >= 0) {
                helps[index] = hval;
            } else {
                helps[0] = hval; //REDUNTANT???
            }
        }
        
        Arrays.sort(helps);
    }
    
    public int getHelp(int index) {
        if(index < 0 || index> 8) {
            throw new IllegalArgumentException("Invalid index for help value...");
        }
        return helps[index];
    }
    
    public void delAllHelps() {
        Arrays.fill(helps, Integer.MAX_VALUE);
    }
    
    private void checkHelpsAfterNewVal() {
        int index = Arrays.binarySearch(helps, val);
        if(index >= 0) {
            helps[index] = Integer.MAX_VALUE;
            Arrays.sort(helps);
        }
    }
    
    public String valAsString() {
        if(val==0) {
            return "";
        } else {
            return ""+val;
        }
    }
    
    public String helpAsString(int index) {
        if(index < 0 || index> 8) {
            throw new IllegalArgumentException("Invalid index for help value...");
        }
        
        if(helps[index] == Integer.MAX_VALUE) {
            return "";
        } else {
            return ""+helps[index];
        }
    }
    public SerFieldVals getSerFieldVars() {
        SerFieldVals res = new SerFieldVals(x,y,val,helps,isEditable());
        return res;
    }
    
    public void setSerFieldVars(SerFieldVals s) {
        this.x = s.x;
        this.y = s.y;
        this.val = s.val;
        this.helps = s.helps;
        this.isEditable = s.isEditable;
    }
    
}



