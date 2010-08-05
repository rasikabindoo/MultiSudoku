/* SerGrid created on 12.07.2006 */
package net.sourceforge.starsudoku.io;

import java.io.Serializable;

public class SerGrid implements Serializable {
    
    public SerFieldVals[][] grid;
    
    public SerGrid() {
        grid = new SerFieldVals[9][9];
    }

}
