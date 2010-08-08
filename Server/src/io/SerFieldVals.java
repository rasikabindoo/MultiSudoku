
package io;

import java.io.Serializable;

public class SerFieldVals implements Serializable {
    
    public int helps[];
    public int val;
    public int x;
    public int y;
    public boolean isEditable;
    public boolean isDefault;
    public boolean isHint;
    
    public SerFieldVals(int x, int y, int val, int[]helps, boolean isEditable,
            boolean isDefault, boolean isHint) {
        
        this.x = x;
        this.y = y;
        this.val = val;
        this.helps = new int[8];
        System.arraycopy(helps,0,this.helps,0,8);
        this.isDefault = isDefault;
        this.isEditable = isEditable;
        this.isHint = isHint;
    }
    
    public SerFieldVals(int x, int y, int val, int[]helps, boolean isEditable) {
        this(x, y, val, helps, isEditable, false, false);
    }
}
