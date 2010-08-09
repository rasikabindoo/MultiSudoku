package net.gitHub.MultiSudoku;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;


public class GV {
    
    public static final String NAME = "MultiSudoku ";
    
    public static final String VERSION = "";
    
    public static final String ABOUT = 
        "<HTML>" + 
        "<B>"+NAME+"</B> ver. "+VERSION+"</BR>" + 
        
        "<P><BR><I>Author: </I><FONT COLOR=\"#0000ff\">Nikolay G. Georgiev</FONT></P>" + 
        "<P><BR><I>Bug Report: </I><A HREF=\"mailto:ngg@users.sourceforge.net\">" +
        "ngg@users.sourceforge.net</A></P>" +   
        "<P><BR></P>This program is released under the GNU General Public License.<BR> " +
        "A copy of this is included with your copy of "+NAME+" and can also be found at<BR> " +
        "<A HREF=\"http://www.opensource.org/licenses/gpl-license.php\">" +
        "http://www.opensource.org/licenses/gpl-license.php</A>" + 
        "</HTML>";

    public static final Color BORDER_COLOR = new Color(184, 207, 229);
    
    public static final Color GRID_COLOR_1 = new Color(230,255,255);
    
    public static final Color GRID_COLOR_2 = BORDER_COLOR;
    
    public static final Dimension DIM_RA = new Dimension(3, 1);

    public static final String IMG_FOLDER = "/res/img/";
    
    //SudField Vars
    public final static Font FONT_BIG = new Font("Tahoma", Font.BOLD, 32);
    
    public final static int FONT_BIG_H = 15;
    
    public final static int FONT_BIG_W = 36;
    
    public final static Font FONT_S = new Font("Tahoma", Font.BOLD, 11);
    
    public final static int H1 = 10;
    
    public final static int W1 = 2;
    
    public final static int H2 = 28;
    
    public final static int W2 = 21;
    
    public final static int H3 = 47;
    
    public final static int W3 = 41;
    
    public final static int DIFF_EASY = 34;
    public final static int DIFF_NORMAL = 30;
    public static final int DIFF_HARD = 26;
    
    public enum NumDistributuon {evenlyDistributedNumbers, evenlyFilled3x3Cubes, random};
}
