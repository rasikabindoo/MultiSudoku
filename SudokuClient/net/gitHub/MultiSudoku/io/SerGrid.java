
package net.gitHub.MultiSudoku.io;

import java.io.Serializable;

public class SerGrid implements Serializable {
    
    public SerFieldVals[][] grid;
    
    public SerGrid() {
        grid = new SerFieldVals[9][9];
    }

}
