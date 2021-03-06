

package net.gitHub.MultiSudoku.gui;

import java.io.Serializable;
import java.util.Observable;

public class Grid extends Observable implements Serializable{

    private int[][] fields;
    private boolean[][] fieldsDefault;
    private int[] sumsVertical;
    private int[] sumsHorizontal;
    private int[] sumsCube;
    
   
    public Grid () {
        fields = new int[9][9];
        fieldsDefault = new boolean[9][9];
        sumsVertical = new int[9];
        sumsHorizontal = new int[9];
        sumsCube = new int[9];
    }
    
    public void setFieldss(int fields[][])
    {
    	for (int i=0; i<9; i++)
    	{
    		for (int j=0; j<9; j++)
    		{
    			this.fields[i][j]= fields[i][j];
    		}
    	}
    	
   /* 	for(int i=0; i<9; i++)
    	{
        	for(int j=0;j<9;j++)
        	{
        		System.out.println(fields[i][j] + " |");
        	}
        	System.out.println("\n");
        }*/
    }
    
    public void setFieldsDefault(boolean fieldsDefault[][])
    {
    	for (int i=0; i<9; i++)
    	{
    		for (int j=0; j<9; j++)
    		{
    			this.fieldsDefault[i][j]= fieldsDefault[i][j];
    		}
    	}
    	for(int i=0; i<9; i++)
    	{
           	for(int j=0;j<9;j++)
           	{
           		System.out.println(fieldsDefault[i][j] + " |");
           	}
           	System.out.println("\n");
            
    	}
    }
    
    
    public void setField(int y, int x, int val) {
        
        if((y < 0) || (y > 8) || (x < 0) || (x > 8)) {
            return;
        }
        
        int trueVal = 0;
        switch (val) {
        
        case 1: trueVal = 1; break;
        case 2: trueVal = 10; break;
        case 3: trueVal = 100; break;
        case 4: trueVal = 1000; break;
        case 5: trueVal = 10000; break;
        case 6: trueVal = 100000; break;
        case 7: trueVal = 1000000; break;
        case 8: trueVal = 10000000; break;
        case 9: trueVal = 100000000; break;
        default: trueVal = 0;
        }
        
        fields[y][x] = trueVal;
        setSums();
    }
    
    public int getField(int y, int x) {
        
        if((y < 0) || (y > 8) || (x < 0) || (x > 8)) {
            return 0;
        }
        
        int trueVal = fields[y][x];
        int val = 0;
        
        switch (trueVal) {
        case 1: val = 1; break;
        case 10: val = 2; break;
        case 100: val = 3; break;
        case 1000: val = 4; break;
        case 10000: val = 5; break;
        case 100000: val = 6; break;
        case 1000000: val = 7; break;
        case 10000000: val = 8; break;
        case 100000000: val = 9; break;
        default: val = 0;
        }
        return val;
    }
    
    public void setFieldDefault(int y, int x, boolean isDefault) {
        if((y < 0) || (y > 8) || (x < 0) || (x > 8)) {
            return;
        }
        fieldsDefault[y][x] = isDefault;
    }
    
    public boolean isFieldDefault(int y, int x) {
        if((y < 0) || (y > 8) || (x < 0) || (x > 8)) {
            return false;
        }
        return fieldsDefault[y][x];
    }
    
    public int[] getAvailabeValuesField(int y, int x) {
        if((y < 0) || (y > 8) || (x < 0) || (x > 8)) {
            throw new IllegalArgumentException("Invalid field address.");
        }
        
        //TODO IS THIS NEEDED?
        if(isFieldDefault(y,x)) {
            int[] result = new int[1];
            result[0] = getField(y,x);
        }
        
        int[] sumV = sumToArray(sumsVertical[x]);
        int[] sumH = sumToArray(sumsHorizontal[y]);
        int[] sumC = sumToArray(sumsCube[3*(y/3)+(x/3)]);
        int k = 0;
        boolean[] b = new boolean[9];
        
        for(int i = 0; i < 9; i++) {
            if((sumV[i] == 0) && (sumH[i] == 0) && (sumC[i] == 0)) {
                b[i] = true;
                k++;
            }
        }
        int[] result = new int[k];
        k = 0;
        for(int i = 8; i >= 0; i--) {
            if(b[i]) {
                result[k] = 9-i;
                k++;
            }
        }
        return result;
    }
    
    public Move getFirstMove() {
        return getNextMove(-1,0);
    }
    
    public Move getNextMove(int y, int x) {
        do { //No default Fields
            if(y + 1 > 8) { //y mod 9;
                if(x + 1 > 8) {
                    return null;
                }
                y = 0;
                x += 1;
            } else {
                y += 1;
            }
        } while(isFieldDefault(y,x));

        int[] moves = getAvailabeValuesField(y,x);
        if(moves.length > 0) {
            return new Move(y,x,moves,0);
        }
        return null;
    }
    
    private int[] sumToArray(int sum) {
        if((sum < 0) || (sum > 999999999)) {
            throw new IllegalArgumentException("Invalid sum.");
        }
        
        int[] result = new int[9];
        int d = 100000000;
        
        for(int i = 0; i < 9; i++) {
            result[i] = sum / d;
            sum %= d;
            d /= 10;
        }
        return result;
    }
    
    public boolean isGridValid() {
        for(int i = 0; i < 9; i++) {
            int[] sumV = sumToArray(sumsVertical[i]);
            int[] sumH = sumToArray(sumsHorizontal[i]);
            int[] sumC = sumToArray(sumsCube[i]);
            for(int j = 0; j < 9; j++) {
                if(sumV[j] > 1 || sumH[j] > 1 || sumC[j] > 1) {
                    return false;
                }
            }
            
        }
        return true;
    }
    
    public boolean isGridSolved() {
        for(int i = 0; i < 9; i++) {
            int[] sumV = sumToArray(sumsVertical[i]);
            int[] sumH = sumToArray(sumsHorizontal[i]);
            int[] sumC = sumToArray(sumsCube[i]);
            for(int j = 0; j < 9; j++) {
                if(sumV[j] != 1 || sumH[j] != 1 || sumC[j] != 1) {
                    return false;
                }
            } 
        }
        return true;
    }
    
    
    public void setSums() {
        for(int i = 0; i < 9; i++) {
            sumsVertical[i] = sumsHorizontal[i] = sumsCube[i] = 0;
        }
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                sumsHorizontal[i] += fields[i][j];
                sumsVertical[i] += fields[j][i];
                int m = i / 3;
                int n = j / 3;
                sumsCube[3*m + n] +=  fields[i][j];
            }
        }
    }
    
    public void clearNoDefaultFields() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(!isFieldDefault(i,j)){
                    setField(i,j,0);
                }
            }
        }
    }
    
    public void resetGrid() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                setField(i,j,0);
                setFieldDefault(i,j,false);
            }
        }
    }
    

    public String toString() {
        
        String str = "";
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                str += "|"+getField(i,j);
            }
            str += "|\n";
        }
        return str;
    }
    
    //NOT USED
    public boolean isGridSolved1() {
        return testSums("1{9}");
    }
    
    //NOT USED
    public boolean isGridValid1() {
        return testSums("(1|0){1,9}");
    }
    
    //NOT USED
    private boolean testSums(String regex) {
        String str;
        for(int i = 0; i < 9; i++) {
            str = Integer.toString(sumsHorizontal[i]);
            if(!str.matches(regex)) return false;
            str = Integer.toString(sumsVertical[i]);
            if(!str.matches(regex)) return false;
            str = Integer.toString(sumsCube[i]);
            if(!str.matches(regex)) return false; 
        }
        return true;
    }
    
    //DEBUG
    public void printArray(int[] a, String name) {
        System.out.print(name+": ");
        for(int i = 0; i < a.length; i++) {
            System.out.print(a[i]+";");
        }
        System.out.println();
    }
    
    //DEBUG
    public void printSums() {
        
        System.out.println("sums\tV\tH\tC");
        for (int i = 0; i < 9; i++) {
            System.out.println(i+"\t"+sumsVertical[i]+"\t"+sumsHorizontal[i]+
                    "\t"+sumsCube[i]);
        }
    }
    
    //DEBUG
    public void printDefFields() {
        String str = "";
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                str += "|"+(isFieldDefault(i,j) ? 1 : 0);
            }
            str += "|\n";
        }
        System.out.println(str);
    }
}
