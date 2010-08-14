/**
 * 
 * 
 * @author Nikolay G. GEorgiev
 */
package net.gitHub.MultiSudoku.gui;

import java.awt.Color;
import java.awt.Dimension;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import net.gitHub.MultiSudoku.GV;
import net.gitHub.MultiSudoku.io.SerFieldVals;



public class SudField extends JPanel implements MouseListener {
    
    private FieldVals fv;
    private Color bg; 
    private boolean isDefault;
    
    public SudField(int x, int y, Color bg, SudFrame sf) {
        
        fv = new FieldVals(x, y, true, sf);
        
        this.bg = bg;
        isDefault = false;
        
        Dimension dim = new Dimension(52, 52);
        setSize(dim);
        setPreferredSize(dim);
        setMaximumSize(dim);
        
        setBackground(bg);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        addMouseListener(this);
    }
    
    public void setInitVal(int val, boolean isDefault) {
        fv.setInitVal(val);
        this.isDefault = isDefault;
        fv.setEditable(!isDefault);
    }
    
    public void setEditable(boolean b) {
        fv.setEditable(b);
    }
    
    public boolean isEditable() {
        return fv.isEditable();
    }
    
    public boolean isDefault() {
        return isDefault;
    }
    
    public void setDefault(boolean b) {
        isDefault = b;
    }

    public int getVal() {
        return fv.getVal();
    }
    
    public void delAllHelps() {
        fv.delAllHelps();
    }

    public void paint(Graphics g) {
        super.paint(g);
        
        g.setColor(bg);
        g.fillRect(0,0,49,49);
        

        g.setFont(GV.FONT_BIG);
        g.setColor(Color.BLACK);
        g.setColor((isDefault) ? Color.BLACK : Color.BLUE);

        g.drawString(fv.valAsString(), GV.FONT_BIG_H, GV.FONT_BIG_W);
        
        //if(!isDefault) {
            g.setFont(GV.FONT_S);
            
            g.drawString(fv.helpAsString(0),GV.W1,GV.H1);
            g.drawString(fv.helpAsString(1),GV.W2,GV.H1);
            g.drawString(fv.helpAsString(2),GV.W3,GV.H1);
            g.drawString(fv.helpAsString(3),GV.W1,GV.H2);
            g.drawString(fv.helpAsString(4),GV.W3,GV.H2);
            g.drawString(fv.helpAsString(5),GV.W1,GV.H3);
            g.drawString(fv.helpAsString(6),GV.W2,GV.H3);
            g.drawString(fv.helpAsString(7),GV.W3,GV.H3);
       // }
    }
    
    /*
     * public set vals
     */
    public SerFieldVals getSerFieldVals() {
        return fv.getSerFieldVars();
    }
    
    public void setSerFieldVals(SerFieldVals s) {
        this.isDefault = s.isDefault;
        fv.setSerFieldVars(s);
    }

    public void mouseClicked(MouseEvent me) {
        int i = 0;
        String bText = SudFrame.lastBut.getText();
        if(bText != null && bText.equalsIgnoreCase("delete")) {
            i = Integer.MAX_VALUE;
        } else {
            i = Integer.parseInt(bText);
        }
        
        if(me.getButton() == MouseEvent.BUTTON1) {
            if(i == Integer.MAX_VALUE)
                fv.delVal();
            else fv.setVal(i);
        } else if(me.getButton() == MouseEvent.BUTTON3) {
            if(i == Integer.MAX_VALUE)
                fv.delAllHelps();
            else fv.addHelp(i);
        }
        repaint();   
    }

    public void mousePressed(MouseEvent arg0) {}

    public void mouseReleased(MouseEvent arg0) {}

    public void mouseEntered(MouseEvent arg0) {}

    public void mouseExited(MouseEvent arg0) {}

}

