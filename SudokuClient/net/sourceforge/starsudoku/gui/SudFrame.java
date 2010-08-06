/* SubBoard created on 08.07.2006 */
package net.sourceforge.starsudoku.gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.starsudoku.GV;
//import net.sourceforge.starsudoku.Grid;
//import net.sourceforge.starsudoku.GridGenerator;
import net.sourceforge.starsudoku.GV.NumDistributuon;
import net.sourceforge.starsudoku.io.LoadSaver;
import net.sourceforge.starsudoku.io.SerGrid;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;

import java.io.*;
import java.net.*;

public class SudFrame extends JFrame implements Observer {

	// --------------------------------
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	// ----------------------------
	protected static GridGenerator gg;

	private JPanel all;

	// HANDLERS
	private ButtonHandlerUp bHUp;
	private ButtonHandlerDown bHDown;
	private WindowHandler wH;
	private MenuHandler mH;
	// Action
	private ActionButDown actButDown;
	private ActionButUp actButUp;

	// MENU
	private JMenuItem sudNew;
	private JMenuItem sudCreate;
	private JMenuItem sudOpen;
	private JMenuItem sudSave;
	private JMenuItem sudSaveAs;
	private JMenuItem sudHint;
	private JMenuItem sudClear;
	private JMenuItem sudSolve;

	private JMenuItem sudExport;
	private JMenuItem sudExit;

	private JRadioButtonMenuItem opDiffHard;
	private JRadioButtonMenuItem opDiffNormal;
	private JRadioButtonMenuItem opDiffEasy;
	private JRadioButtonMenuItem opDiffCustom;
	private JRadioButtonMenuItem opND1;
	private JRadioButtonMenuItem opND2;
	private JRadioButtonMenuItem opND3;
	private JMenuItem opPref;

	private JMenuItem helpAbout;

	// GRID
	private SudGrid sGrid;

	// BUTTONS
	private JPanel butUp;
	private JButton[] bUp;
	private JPanel butDown;
	private JButton[] bDown;
	private JButton delBut;
	protected static JButton lastBut;

	private JTextField jTextDiff;

	private JFileChooser fc;

	private int diff;
	private NumDistributuon nD;

	public SudFrame() {
		super(GV.NAME + GV.VERSION);

		bHUp = new ButtonHandlerUp();
		bHDown = new ButtonHandlerDown();
		mH = new MenuHandler();
		wH = new WindowHandler();

		actButDown = new ActionButDown();
		actButUp = new ActionButUp();
		//		listenSocket();
		initMenu();
		initGUI();
		setNotEnabledItems();

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setIconImage(loadImage("star16x16.png"));

		Container cp = getContentPane();
		cp.add(all);
		pack();
		setResizable(false);
		setLocation(50, 50);
		setVisible(true);
		addWindowListener(wH);

		gg = new GridGenerator();
		diff = GV.DIFF_NORMAL;
		nD = NumDistributuon.evenlyFilled3x3Cubes;

		// ----------------------
		gg = new GridGenerator();
		listenSocket();
		// ----------------------
		// doSolved();
	}

	// --------------------------------------------------------

	public void listenSocket() 
	{
		// Create socket connection
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		Date date = null;
		int[][] gridd = new int[9][9];
		boolean [][]fieldsDefault = new boolean [9][9];

		try 
		{
			socket = new Socket("localhost", 12340);
			System.out
			.println("Connected to" + socket.getInetAddress()
					+ " on port " + socket.getPort() + "from port "
					+ socket.getLocalPort() + " of "
					+ socket.getLocalAddress());

			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			// read an object from the server
			// System.out.println("Before reading the object");
			// date = (Date) ois.readObject();


			System.out.println("Before reading the object");

			gridd = (int[][]) ois.readObject();
			fieldsDefault = (boolean[][]) ois.readObject();
			System.out.println("After reading the object");

			for(int i=0; i<9; i++){
				for(int j=0;j<9;j++){
					System.out.println(gridd[i][j] + " |");
				}
				System.out.println("\n");
			}
			for(int i=0; i<9; i++){
				for(int j=0;j<9;j++){
					System.out.println("**************************"+ fieldsDefault[i][j] + " |");
				}
				System.out.println("\n");
			}


			gg.getGrid().resetGrid();
			gg.getGrid().setFieldss(gridd);
			// System.out.print("The date is: " + date);
			System.out.println("Blah Blah");

		//	oos.close();
		//	ois.close();
		}
		catch(Exception e) 
		{
			System.out.println(e);
		}

		// ----------------------------------------------------------------------------------------------------------------
		try
		{
			out = new PrintWriter(socket.getOutputStream(), true);
			
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown host: kq6py.eng");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O"+e);
			System.exit(1);
		}


		int i = -1;
		while (i == -1) 
		{
			try 
			{
				String line = in.readLine();
				/*System.out.println("Line is  :  "+ line);
				System.out
				.println("Connected to" + socket.getInetAddress()
						+ " on port " + socket.getPort() + "from port "
						+ socket.getLocalPort() + " of "
						+ socket.getLocalAddress());
						*/
				//if (line.length() > 0) 
				if (line != null)
				{
					System.out.println("Someone else won!!!");
					doOtherPersonWon();
					i = 0;
				}

			} 
			catch (IOException e) 
			{
				System.out.println("Read failed");
				System.exit(1);
			}
		}

	}

	// ----------------------------------------------------------------------

	private Image loadImage(String imageName) {
		try {
			Image im = ImageIO.read(new BufferedInputStream(getClass()
					.getResourceAsStream(GV.IMG_FOLDER + imageName)));
			return im;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private ImageIcon loadIcon(String imageName) {

		try {
			return new ImageIcon(loadImage(imageName));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private void initMenu() {

		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		{
			// SUDOKU
			JMenu mSudoku = new JMenu();
			menu.add(mSudoku);
			mSudoku.setText("Sudoku");
			mSudoku.setMnemonic('S');
			{
				sudNew = new JMenuItem("New", loadIcon("star1.png"));
				mSudoku.add(sudNew);
				setCtrlAcceleratorMenu(sudNew, 'N');
				sudNew.addActionListener(mH);
				sudNew.setMnemonic('N');

				sudCreate = new JMenuItem("Create", loadIcon("board1.png"));
				mSudoku.add(sudCreate);
				sudCreate.addActionListener(mH);
				sudCreate.setMnemonic('R');

				sudOpen = new JMenuItem("Open...", loadIcon("open1.png"));
				mSudoku.add(sudOpen);
				setCtrlAcceleratorMenu(sudOpen, 'O');
				sudOpen.addActionListener(mH);
				sudOpen.setMnemonic('O');

				sudSave = new JMenuItem("Save", loadIcon("disk1.png"));
				mSudoku.add(sudSave);
				setCtrlAcceleratorMenu(sudSave, 'S');
				sudSave.addActionListener(mH);
				sudSave.setMnemonic('S');

				sudSaveAs = new JMenuItem("Save as...", loadIcon("diskAs1.png"));
				mSudoku.add(sudSaveAs);
				sudSaveAs.addActionListener(mH);
				setKeyAcceleratorMenu(sudSaveAs, 'S', Event.SHIFT_MASK
						+ Event.CTRL_MASK);
				sudSaveAs.setMnemonic('A');

				mSudoku.addSeparator();

				sudClear = new JMenuItem("Clear", loadIcon("bin1.png"));
				mSudoku.add(sudClear);
				sudClear.addActionListener(mH);
				sudClear.setMnemonic('C');

				sudHint = new JMenuItem("Hint", loadIcon("help1.png"));
				mSudoku.add(sudHint);
				sudHint.addActionListener(mH);
				sudHint.setMnemonic('H');
				setCtrlAcceleratorMenu(sudHint, 'H');

				sudSolve = new JMenuItem("Solve", loadIcon("tick1.png"));
				mSudoku.add(sudSolve);
				sudSolve.addActionListener(mH);
				sudSolve.setMnemonic('L');

				mSudoku.addSeparator();

				sudExport = new JMenuItem("Export...", loadIcon("export1.png"));
				mSudoku.add(sudExport);
				sudExport.addActionListener(mH);
				setCtrlAcceleratorMenu(sudExport, 'T');
				sudExport.setMnemonic('T');

				mSudoku.addSeparator();

				sudExit = new JMenuItem("Exit", loadIcon("cross1.png"));
				mSudoku.add(sudExit);
				setKeyAcceleratorMenu(sudExit, Event.ESCAPE, 0);
				sudExit.addActionListener(mH);
				sudExit.setMnemonic('X');

			}

			// Options
			JMenu mOptions = new JMenu("Options");
			mOptions.setMnemonic('O');
			menu.add(mOptions);
			{
				JLabel jl1 = new JLabel(
				"<HTML>  &#032&#032<U>Difficulty :</U><HTML>");
				jl1.setHorizontalAlignment(JLabel.LEFT);
				jl1.setHorizontalTextPosition(JLabel.LEFT);
				mOptions.add(jl1);

				ButtonGroup bGr1 = new ButtonGroup();

				opDiffEasy = new JRadioButtonMenuItem("Easy");
				opDiffEasy.addActionListener(mH);
				opDiffEasy.setMnemonic('E');
				mOptions.add(opDiffEasy);
				bGr1.add(opDiffEasy);

				opDiffNormal = new JRadioButtonMenuItem("Normal", true);
				opDiffNormal.addActionListener(mH);
				opDiffNormal.setMnemonic('N');
				mOptions.add(opDiffNormal);
				bGr1.add(opDiffNormal);

				opDiffHard = new JRadioButtonMenuItem("Hard");
				opDiffHard.addActionListener(mH);
				opDiffHard.setMnemonic('H');
				mOptions.add(opDiffHard);
				bGr1.add(opDiffHard);

				opDiffCustom = new JRadioButtonMenuItem("Custom...");
				mOptions.add(opDiffCustom);
				opDiffCustom.addActionListener(mH);
				opDiffCustom.setMnemonic('C');
				bGr1.add(opDiffCustom);

				mOptions.addSeparator();

				JLabel jl2 = new JLabel(
				"<HTML>  &#032&#032<U>Number Distribution :</U><HTML>");
				jl2.setHorizontalAlignment(JLabel.LEFT);
				jl2.setHorizontalTextPosition(JLabel.LEFT);
				mOptions.add(jl2);

				ButtonGroup bGr2 = new ButtonGroup();

				opND1 = new JRadioButtonMenuItem("Evenly filled 3x3 Cubes",
						true);
				opND1.addActionListener(mH);
				opND1.setMnemonic('F');
				mOptions.add(opND1);
				bGr2.add(opND1);

				opND2 = new JRadioButtonMenuItem("Evenly distributed Numbers");
				opND2.addActionListener(mH);
				opND2.setMnemonic('D');
				mOptions.add(opND2);
				bGr2.add(opND2);

				opND3 = new JRadioButtonMenuItem("Random");
				opND3.addActionListener(mH);
				opND3.setMnemonic('R');
				mOptions.add(opND3);
				bGr2.add(opND3);

				mOptions.addSeparator();

				opPref = new JMenuItem("Preferences...",
						loadIcon("wrench1.png"));
				mOptions.add(opPref);
				opPref.addActionListener(mH);
				setCtrlAcceleratorMenu(opPref, 'P');
				opPref.setMnemonic('P');
			}

			// Help
			JMenu mHelp = new JMenu("Help");
			mHelp.setMnemonic('H');
			menu.add(mHelp);
			{
				helpAbout = new JMenuItem("About", loadIcon("cup1.png"));
				mHelp.add(helpAbout);
				helpAbout.addActionListener(mH);
				helpAbout.setMnemonic('A');
			}
		}
	}

	private void initGUI() {

		all = new JPanel();
		all.setLayout(new BorderLayout());
		all.setBackground(GV.BORDER_COLOR);
		all.setBorder(BorderFactory.createTitledBorder(""));

		// BUTTONS UP
		butUp = new JPanel();
		BoxLayout bUpLayout = new BoxLayout(butUp, BoxLayout.X_AXIS);
		butUp.setLayout(bUpLayout);
		butUp.setBorder(new EtchedBorder());
		bUp = new JButton[10];
		{
			Dimension butUpDim = new Dimension(24, 24);

			bUp[0] = createButUp("star1.png", "star2.png",
					"Generate new Sudoku", butUpDim);
			butUp.add(bUp[0]);
			bUp[1] = createButUp("board1.png", "board2.png",
					"Create new Sudoku", butUpDim);
			butUp.add(bUp[1]);
			bUp[2] = createButUp("open1.png", "open2.png", "Open Sudoku",
					butUpDim);
			butUp.add(bUp[2]);
			bUp[3] = createButUp("disk1.png", "disk2.png", "Save Sudoku As",
					butUpDim);
			butUp.add(bUp[3]);

			// SEPARATOR
			createButUpSeparator();

			bUp[4] = createButUp("redo1.png", "redo2.png", "Redo", butUpDim);
			butUp.add(bUp[4]);
			setKeyAcceleratorButton(bUp[4], actButUp, "redo", 'Y',
					Event.CTRL_MASK);

			bUp[5] = createButUp("undo1.png", "undo2.png", "Undo", butUpDim);
			butUp.add(bUp[5]);
			setKeyAcceleratorButton(bUp[5], actButUp, "undo", 'Z',
					Event.CTRL_MASK);

			// SEPARATOR
			createButUpSeparator();

			bUp[6] = createButUp("bin1.png", "bin2.png", "Clear Sudoku",
					butUpDim);
			butUp.add(bUp[6]);

			bUp[7] = createButUp("help1.png", "help2.png", "Hint", butUpDim);
			butUp.add(bUp[7]);
			System.out.println("***********************************");

			bUp[8] = createButUp("tick1.png", "tick2.png", "Solve", butUpDim);
			butUp.add(bUp[8]);

			// SEPARATOR
			createButUpSeparator();

			// Difficulty
			butUp.add(Box.createRigidArea(GV.DIM_RA));
			JLabel diffLabel = new JLabel("Difficulty :  ");
			butUp.add(diffLabel);

			jTextDiff = new JTextField();
			jTextDiff.setEditable(false);
			jTextDiff.setText("Normal ");
			jTextDiff.setMaximumSize(new Dimension(60, 18));
			butUp.add(jTextDiff);

			butUp.add(Box.createRigidArea(new Dimension(85, 1)));
			bUp[9] = createButUp("cross1.png", "cross2.png",
					"<HTML>Close</HTML>", butUpDim);
			butUp.add(bUp[9]);
			setKeyAcceleratorButton(bUp[9], actButUp, "close", Event.ESCAPE, 0);

		}

		all.add(butUp, BorderLayout.NORTH);

		// GRID
		sGrid = new SudGrid(this);
		sGrid.setBorder(new LineBorder(new Color(238, 238, 238), 4, false));
		all.add(sGrid, BorderLayout.CENTER);

		// BUTONS DOWN
		butDown = new JPanel();
		BoxLayout butLayout = new BoxLayout(butDown, BoxLayout.X_AXIS);
		butDown.setLayout(butLayout);
		butDown.setBorder(new LineBorder(GV.BORDER_COLOR, 2, false));
		butDown.setBackground(GV.BORDER_COLOR);

		bDown = new JButton[9];
		{
			butDown.add(Box.createRigidArea(GV.DIM_RA));
			for (int i = 0; i < bDown.length; i++) {
				bDown[i] = new JButton();
				butDown.add(bDown[i]);
				bDown[i].setText("" + (i + 1));
				bDown[i].addActionListener(bHDown);
				butDown.add(Box.createRigidArea(GV.DIM_RA));
				bDown[i].setToolTipText("<HTML>Press" + (i + 1) + "<HTML>");
				setKeyAcceleratorButton(bDown[i], actButDown,
						"press" + (i + 1), 49 + i, 0);
				// bDown[i].setBackground(GlobalVars.BORDER_COLOR);
			}

			delBut = new JButton();
			delBut.addActionListener(bHDown);
			butDown.add(delBut);
			delBut.setText("Delete");
			delBut.setToolTipText("<HTML>Press D <HTML>");
			setKeyAcceleratorButton(delBut, actButDown, "delBut", 'D', 0);
			// delBut.setBackground(GlobalVars.BORDER_COLOR);

			lastBut = bDown[0];
			lastBut.setForeground(Color.BLUE);
			lastBut.requestFocus();
		}

		// FileChooser
		fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory())
					return true;
				return f.getName().toLowerCase().endsWith(".ssud");
			}

			public String getDescription() {
				return GV.NAME + "Save File (*.ssud)";
			}
		});

		fc.setMultiSelectionEnabled(false);

		all.add(butDown, BorderLayout.SOUTH);
	}

	private void setNotEnabledItems() {
		sudSave.setEnabled(false);
		sudHint.setEnabled(false);
		sudExport.setEnabled(false);

		opPref.setEnabled(false);

		bUp[4].setEnabled(false);
		bUp[5].setEnabled(false);
		bUp[7].setEnabled(false);
	}

	private JButton createButUp(String img, String img2, String toolTip,
			Dimension d) {

		JButton jB = new JButton(loadIcon(img));
		jB.setRolloverIcon(loadIcon(img2));
		jB.setToolTipText(toolTip);
		jB.setSize(d);
		jB.setPreferredSize(d);
		jB.setMaximumSize(d);
		jB.setFocusPainted(false);
		jB.setBorderPainted(false);
		jB.setContentAreaFilled(false);
		jB.addActionListener(bHUp);

		return jB;
	}

	private JSeparator createButUpSeparator() {
		butUp.add(Box.createRigidArea(GV.DIM_RA));
		JSeparator jSep = new JSeparator();
		jSep.setOrientation(SwingConstants.VERTICAL);
		jSep.setMaximumSize(new Dimension(2, 18));
		butUp.add(jSep);
		butUp.add(Box.createRigidArea(GV.DIM_RA));

		return jSep;
	}

	private void setKeyAcceleratorButton(JButton b, AbstractAction act,
			String actionName, int keyCode, int mask) {
		KeyStroke ks = KeyStroke.getKeyStroke(keyCode, mask);
		b.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, actionName);
		b.getActionMap().put(actionName, act);
	}

	private void setKeyAcceleratorMenu(JMenuItem mi, int keyCode, int mask) {
		KeyStroke ks = KeyStroke.getKeyStroke(keyCode, mask);
		mi.setAccelerator(ks);
	}

	private void setCtrlAcceleratorMenu(JMenuItem mi, char acc) {
		setKeyAcceleratorMenu(mi, (int) acc, Event.CTRL_MASK);
	}

	private void doExit() {

		int result = JOptionPane.showConfirmDialog(this,
				"Do you realy what to exit?", "Exit",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else if (result == JOptionPane.NO_OPTION) {
			WindowEvent we1 = new WindowEvent(this,
					WindowEvent.WINDOW_STATE_CHANGED,
					WindowEvent.WINDOW_DEACTIVATED,
					WindowEvent.WINDOW_ACTIVATED);
			wH.windowActivated(we1);
		}
	}

	private void doCustomDiff() {
		String s = JOptionPane.showInputDialog(this,
				"Enter a number between 1 and 81", "Custom Difficulty",
				JOptionPane.PLAIN_MESSAGE);
		if (s != null) {
			try {
				int i = Integer.parseInt(s.trim());
				if (i >= 1 && i <= 81) {
					diff = i;
					return;
				}
			} catch (Exception e) {
			}
			JOptionPane.showMessageDialog(this, "Invalid Number!\n"
					+ "Enter a number between 1 and 81", "Error",
					JOptionPane.ERROR_MESSAGE);
			doCustomDiff();
		}
	}

	private void doAbout() {
		JOptionPane.showMessageDialog(this, GV.ABOUT, "About",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void generateNewSud() {

		System.out.println("Inside generate sudoku");
		// ObjectInputStream ois = null;

		/*
		 * switch(diff) { case GV.DIFF_EASY : jTextDiff.setText("Easy"); break;
		 * case GV.DIFF_NORMAL : jTextDiff.setText("Normal"); break; case
		 * GV.DIFF_HARD : jTextDiff.setText("Hard"); break; default :
		 * jTextDiff.setText("Custom"); }
		 */
		/*
		 * try { System.out.println("Before new object stream");
		 * 
		 * ois = new ObjectInputStream(socket.getInputStream());
		 * 
		 * System.out.println("Before read object");
		 * 
		 * gg = (GridGenerator) ois.readObject();
		 * System.out.println("After read object");
		 * 
		 * // gg.getGrid().resetGrid(); // gg.generate(diff, nD); //gg= }
		 * catch(IOException e) { System.out.println("IO Exception");
		 * 
		 * } catch (ClassNotFoundException e) {
		 * System.out.println("Class not found Exception"); }
		 */
		sinchronizeGrids();
		sGrid.repaint();

	}

	private void doCreate() {
		gg.getGrid().resetGrid();
		sinchronizeGrids();
		sGrid.repaint();
	}

	private void doSolve() {
		try {
			gg.solve();
			sinchronizeGrids();
			setGridToFinished();
			sGrid.repaint();
		} catch (NullPointerException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Sudoku seems to be solved.",
					"Solved", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// -------------------------------------------------------------------------------------------------
	private boolean checkIfSolved() {
		// boolean b = gg.isGridValidGG();
		boolean b = gg.getGrid().isGridSolved();
		return b;

	}

	// -------------------------------------------------------------------------------------------------

	private void doClear() {
		gg.getGrid().clearNoDefaultFields();
		sinchronizeGrids();
		sGrid.repaint();
	}

	private void doSolved() {
		JOptionPane.showMessageDialog(this, "Sudoku solved :)", "Success",
				JOptionPane.INFORMATION_MESSAGE);
		setGridToFinished();
		sGrid.repaint();

		// --------------------------------

		// Send data over socket
		String text = "ClientWon";
		out.println(text);
		// textField.setText(new String(""));

		// ----------------------------------
	}

	// ----------------------------------------------------------------------------------
	private void doNotSolved() {
		JOptionPane.showMessageDialog(this, "Sudoku not solved :)", "Damn",
				JOptionPane.INFORMATION_MESSAGE);
		setGridToFinished();
		sGrid.repaint();

		// --------------------------------

		/*
		 * //Send data over socket String text = "ClientWon"; out.println(text);
		 * // textField.setText(new String(""));
		 */// ----------------------------------
	}

	// -----------------------------------------------------------------------------------

	// ----------------------------------------------------------------------------------
	private void doOtherPersonWon() {
		JOptionPane.showMessageDialog(this, "Somebody else won!! :)", "Oops",
				JOptionPane.INFORMATION_MESSAGE);
		setGridToFinished();
		sGrid.repaint();

		// --------------------------------

		/*
		 * //Send data over socket String text = "ClientWon"; out.println(text);
		 * // textField.setText(new String(""));
		 */// ----------------------------------
	}

	// -----------------------------------------------------------------------------------

	private void doSave() {
		if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				SerGrid sg = sGrid.getSerGrid();
				File file = fc.getSelectedFile();
				String aPath = file.getAbsolutePath();
				if (!aPath.toLowerCase().endsWith(".ssud")) {
					aPath = aPath + ".ssud";
					file = new File(aPath);
				}
				LoadSaver.save(sg, file);
				fc.setSelectedFile(file);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Sudoku game have not been saved!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void doLoad() {
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				SerGrid sg = LoadSaver.load(fc.getSelectedFile()
						.getAbsolutePath());
				sGrid.setSerGrid(sg, gg.getGrid());
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,
						"Sudoku game have not been saved!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		sGrid.repaint();
	}

	private void setGridToFinished() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sGrid.fields[i][j].setEditable(false);
				sGrid.fields[i][j].delAllHelps();
			}
		}
	}

	private void sinchronizeGrids() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sGrid.fields[i][j].setEditable(true);
				sGrid.fields[i][j].setInitVal(gg.getGrid().getField(i, j), gg
						.getGrid().isFieldDefault(i, j));
			}
		}
	}

	public void update(Observable arg0, Object arg1) {
		try {
			FieldVals fv = (FieldVals) arg1;
			gg.getGrid().setField(fv.getY(), fv.getX(), fv.getVal());
			boolean b = gg.getGrid().isGridSolved();
			// -----------------------------------------------------------------------------------

			// boolean b = true;
			/*
			 * if(b) { sGrid.repaint(); doSolved(); }
			 */

			// -----------------------------------------------------------------------------------
		} catch (ClassCastException ex) {
			ex.printStackTrace();
		}
	}

	private class ButtonHandlerUp implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				JButton tmp = (JButton) e.getSource();

				if (tmp == bUp[0]) {
					generateNewSud();
					tmp.setEnabled(false);
				} else if (tmp == bUp[1]) {
					doCreate();
				} else if (tmp == bUp[2]) {
					doLoad();
				} else if (tmp == bUp[3]) {
					doSave();
				} else if (tmp == bUp[6]) {
					doClear();
				} else if (tmp == bUp[8]) {
					// doSolve();
					// -----------------------------------------------
					System.out.println("Button is clicked");
					boolean b = checkIfSolved();
					System.out.println(b);
					// if(!checkIfSolved())

					if (!checkIfSolved()) {
						System.out.println("Inside if");
						doNotSolved();
					} else {
						doSolved();
					}
					// */
					// doSolved();

					// -----------------------------------------------
				} else if (tmp == bUp[9]) {
					doExit();
				}
			} catch (ClassCastException ex) {
				ex.printStackTrace();
			}
		}
	}

	private class ButtonHandlerDown implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				JButton tmp = (JButton) e.getSource();
				tmp.requestFocus();
				tmp.getText();
				if (lastBut != null) {
					lastBut.setForeground(Color.BLACK);
				}
				lastBut = tmp;
				lastBut.setForeground(Color.BLUE);

			} catch (ClassCastException ex) {
				ex.printStackTrace();
			}
		}
	}

	private class WindowHandler implements WindowListener {

		public void windowOpened(WindowEvent arg0) {
			System.out.println("open");
		}

		public void windowClosing(WindowEvent arg0) {
			System.out.println("closing");
			doExit();
		}

		public void windowClosed(WindowEvent arg0) {
			System.out.println("closed");
		}

		public void windowIconified(WindowEvent arg0) {
			System.out.println("Iconified");
		}

		public void windowDeiconified(WindowEvent arg0) {
			System.out.println("Deiconified");
		}

		public void windowActivated(WindowEvent arg0) {
			System.out.println("Activated");
		}

		public void windowDeactivated(WindowEvent arg0) {
			System.out.println("Deactivated");
		}
	}

	private class MenuHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			try {
				JMenuItem tmp = (JMenuItem) e.getSource();

				if (tmp == sudNew) {
					generateNewSud();
				} else if (tmp == sudOpen) {
					doLoad();
				} else if (tmp == sudSaveAs) {
					doSave();
				} else if (tmp == opDiffEasy) {
					diff = GV.DIFF_EASY;
				} else if (tmp == opDiffNormal) {
					diff = GV.DIFF_NORMAL;
				} else if (tmp == opDiffHard) {
					diff = GV.DIFF_HARD;
				} else if (tmp == opDiffCustom) {
					doCustomDiff();
				} else if (tmp == opND1) {
					nD = NumDistributuon.evenlyFilled3x3Cubes;
				} else if (tmp == opND2) {
					nD = NumDistributuon.evenlyDistributedNumbers;
				} else if (tmp == opND3) {
					nD = NumDistributuon.random;
				} else if (tmp == helpAbout) {
					doAbout();
				} else if (tmp == sudExit) {
					doExit();
				} else if (tmp == sudSolve) {
					doSolve();
				} else if (tmp == sudCreate) {
					doCreate();
				} else if (tmp == sudClear) {
					doClear();
				}
			} catch (ClassCastException ex) {
				ex.printStackTrace();
			}
		}
	}

	private class ActionButDown extends AbstractAction {

		public void actionPerformed(ActionEvent evt) {
			bHDown.actionPerformed(evt);
		}
	}

	private class ActionButUp extends AbstractAction {

		public void actionPerformed(ActionEvent evt) {
			bHUp.actionPerformed(evt);
		}
	}
}

//*********************************************************************
//*********************************************************************