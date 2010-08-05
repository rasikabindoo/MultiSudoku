/* SPanel created on 08.07.2006 */
package net.sourceforge.starsudoku.gui;


import javax.swing.JPanel;

import net.sourceforge.starsudoku.GV;
import net.sourceforge.starsudoku.Grid;
import net.sourceforge.starsudoku.io.SerGrid;


import java.awt.GridLayout;


public class SudGrid extends JPanel {
    
    protected SudField[][] fields;
    
    public SudGrid (SudFrame sf) {
        setLayout(new GridLayout(9, 9));
        fields = new SudField[9][9];

        boolean b1 = false;
        boolean b2 = false;
        for(int i = 0; i < 9; i++) {
            b1 = (i / 3 == 1) ? true : false;
            for(int j = 0; j < 9; j++) {
                b2 = (j / 3 == 1) ? true : false;
                if(b1 && b2) {
                    fields[i][j] = new SudField(i,j,GV.GRID_COLOR_2, sf);
                } else if(b1 && !b2) {
                    fields[i][j] = new SudField(i,j,GV.GRID_COLOR_1, sf);
                }else if(!b1 && b2) {
                    fields[i][j] = new SudField(i,j,GV.GRID_COLOR_1, sf);
                } else{
                    fields[i][j] = new SudField(i,j,GV.GRID_COLOR_2, sf);
                }
                
                add(fields[i][j]);
            }

        }
    }
    
    public SerGrid getSerGrid() {
        
        SerGrid res = new SerGrid();
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                res.grid[i][j] = fields[i][j].getSerFieldVals();
                res.grid[i][j].isDefault = fields[i][j].isDefault();
            }
        }
        return res;
    }
    
    public void setSerGrid(SerGrid sg, Grid g) {
        
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                fields[i][j].setSerFieldVals(sg.grid[i][j]);
                g.setField(i,j,sg.grid[i][j].val);
                g.setFieldDefault(i,j,sg.grid[i][j].isDefault);
            }
        }
    }
}