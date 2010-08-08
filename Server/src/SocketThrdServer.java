
/**
 *
 * 
 * @author Rasika Bindoo
 */

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.io.*;
import java.net.*;
import java.util.Date;

import starsudoku. *;
import starsudoku.GV.NumDistributuon;

class ClientWorker implements Runnable 
{
	
	private Socket client;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	public int [][] gridd;
	public boolean[][] fieldsDefault;
	GridGenerator gg;
	
	ClientWorker(Socket client, GridGenerator gg) 
	{	
	    try
	    {   	
	    	this.client = client;
	    	this.gg = gg;
	    
	    	gridd = new int[9][9];
	    	gridd = gg.getGrid().getFields();
	    	
	    	fieldsDefault = new boolean[9][9];
	    	fieldsDefault = gg.getGrid().getfieldsDefault();
	        	    	
	    	System.out.println("Connected to" + client.getInetAddress() + " on port " + client.getPort() + "from port " + client.getLocalPort() + " of " +  client.getLocalAddress());
	    	SocketThrdServer.arrayClientSockets[SocketThrdServer.index] = client;
	    	SocketThrdServer.arrayClientNames[SocketThrdServer.index] = "ClientChaNaavv";
	    	SocketThrdServer.index = SocketThrdServer.index + 1;
	    	 ois = new ObjectInputStream(client.getInputStream());
	         oos = new ObjectOutputStream(client.getOutputStream());
	    	
	    	for(int i=0; i<10; i++)
	    	{
	    		if(SocketThrdServer.arrayClientSockets[i]!=null)
	    		{
	    			System.out.println("\n" + SocketThrdServer.arrayClientSockets[i].getPort());
	    		}
	    	}
	    	
	    }
	    catch(Exception e1)
	    {
	         try 
	         {
	            client.close();
	         }
	         catch(Exception e) 
	         {		
	           System.out.println(e.getMessage());
	         }
	         return;
	     }
	}

	public void run()	
	{
		String line;
		BufferedReader in  = null;
		PrintWriter[] out = new PrintWriter[10] ;
		
		try 
		{
			System.out.println("Before Writing the object");
			oos.writeObject(gridd);
			oos.writeObject(fieldsDefault);
			System.out.println("After Writing the object");
			oos.flush();
			// close streams and connections
		//	ois.close();
		//	oos.close();
 
		}
		
		
		catch(Exception e) 
		{
			System.out.println("Exception in run thing" + e);
		}  
		try
		{	
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));	
		} 
		
		catch (UnknownHostException e) {
			System.out.println("Unknown host: kq6py.eng");
			System.exit(1);
		}
		
		catch (IOException e) {
			System.out.println("No I/O   : "+e);
			System.exit(1);
		}
		System.out.println("Before Client Signal");
		boolean clientSignal = true;
		while(clientSignal)
		{
			try
			{
				
				line = in.readLine();
				
				if(line!= null)
				{
					clientSignal= false;
					System.out.println("******* String line :  " + line);
					for(int i=0; i<10; i++)
					{
						System.out.println("I is " + i);
						if(SocketThrdServer.arrayClientSockets[i]!=null && (SocketThrdServer.arrayClientSockets[i] != client ))
						//if(SocketThrdServer.arrayClientSockets[i] != client )
						{
							out[i] = new PrintWriter(SocketThrdServer.arrayClientSockets[i].getOutputStream(), true);
							System.out.println("Port is " + SocketThrdServer.arrayClientSockets[i].getPort());
//							System.out.println("I is " + i);
							System.out.println("I is " + i);
							out[i].println(line);

						}
					}
				}
			}
			catch (IOException e) 
			{
				System.out.println("**********************"+e);
				System.out.println("Read failed");
				System.exit(-1);
			}
    	}
		System.out.println("------------After while--------------- " );
		
	}
}

public class SocketThrdServer extends JPanel implements ListSelectionListener {
	
	protected static GridGenerator gg;
	private int diff;
	private NumDistributuon nD;
	ServerSocket server = null;
	public static Socket[] arrayClientSockets = new Socket[10] ;
	public static String[] arrayClientNames = new String[10] ;
	public static int index = 0;

	 private JLabel pictureLabel;
	 private JList list;
	 private JSplitPane splitPane;
	 private JFrame frame ;
/*	 private String[] imageNames = { "Bird", "Cat", "Dog", "Rabbit", "Pig", "dukeWaveRed",
	        "kathyCosmo", "lainesTongue", "left", "middle", "right", "stickerface"};
*/		
	/*JLabel label = new JLabel("Text received over socket:");
	JPanel panel;
	JTextArea textArea = new JTextArea();
*/
	
	SocketThrdServer()
	{ 
		gg = new GridGenerator();
	    diff = GV.DIFF_NORMAL;
	    nD = NumDistributuon.evenlyFilled3x3Cubes;
	            
	    gg.getGrid().resetGrid();
	    gg.generate(diff, nD);
	     
	    frame = new JFrame("SplitPaneDemo");
	 
	    
	    list = new JList(arrayClientNames);
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    list.setSelectedIndex(0);
	    list.addListSelectionListener(this);
	         
        JScrollPane listScrollPane = new JScrollPane(list);
       
        pictureLabel = new JLabel();
        pictureLabel.setFont(pictureLabel.getFont().deriveFont(Font.ITALIC));
        pictureLabel.setHorizontalAlignment(JLabel.CENTER);
        
        JScrollPane pictureScrollPane = new JScrollPane(pictureLabel);

	        //Create a split pane with the two scroll panes in it.
	        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
	                                   listScrollPane, pictureScrollPane);
	        splitPane.setOneTouchExpandable(true);
	        splitPane.setDividerLocation(150);

	        //Provide minimum sizes for the two components in the split pane.
	        Dimension minimumSize = new Dimension(100, 50);
	        listScrollPane.setMinimumSize(minimumSize);
	        pictureScrollPane.setMinimumSize(minimumSize);

	        //Provide a preferred size for the split pane.
	        splitPane.setPreferredSize(new Dimension(400, 200));
	        updateLabel(arrayClientNames[list.getSelectedIndex()]);
	        
	        
	       
	} //End Constructor
	
	 public void valueChanged(ListSelectionEvent e) {
	        JList list = (JList)e.getSource();
	        updateLabel(arrayClientNames[list.getSelectedIndex()]);
	    }
	    
	 protected void updateLabel (String name) {
	        ImageIcon icon = createImageIcon("images/" + name + ".gif");
	        pictureLabel.setIcon(icon);
	        if  (icon != null) {
	        	pictureLabel.setText(null);
	        } else {
	        	pictureLabel.setText("Image not found");
	        }
	    }
	 
	 //Used by SplitPaneDemo2
	    public JList getImageList() {
	        return list;
	    }

	    public JSplitPane getSplitPane() {
	        return splitPane;
	    }
	    
	    protected static ImageIcon createImageIcon(String path) {
	        java.net.URL imgURL = SocketThrdServer.class.getResource(path);
	         if (imgURL != null) {
	             return new ImageIcon(imgURL);
	         } else {
	             System.err.println("Couldn't find file: " + path);
	             return null;
	         }
	     }
	    public void createAndShowGUI() {

	        //Create and set up the window.
	       
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        SocketThrdServer splitPaneDemo = new SocketThrdServer();
	        frame.getContentPane().add(splitPaneDemo.getSplitPane());

	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
	        frame.repaint();
	    }


	public void listenSocket()	
	{
		try
		{
			server = new ServerSocket(12340); 
		}
		catch (IOException e) 	
		{
			System.out.println("Could not listen on port 4444");
			System.exit(-1);
		}
		while(true)
		{
			ClientWorker w;
			try
			{
			//	System.out.println("Inside WHILE");
				w = new ClientWorker(server.accept(), gg);
				Thread t = new Thread(w);
				t.start();
				frame.repaint();
			}
			catch (IOException e) 
			{
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}
		}
	}

	protected void finalize()
	{
	//Objects created in run method are finalized when 
	//program terminates and thread exits
		try
		{
			server.close();
		}
		catch (IOException e) 
		{
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}

	public static void main(String[] args)
	{
	//--------------------------------------------------------------------------------
		
		SocketThrdServer server = new SocketThrdServer();
		server.createAndShowGUI();
	//	frame.setTitle("Server Program");
	    /*    WindowListener l = new WindowAdapter() 
		{
	                public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
                	}
        	};
*/	/*        frame.addWindowListener(l);
        	frame.pack();
        	frame.setVisible(true);
        */	server.listenSocket();
        	System.out.println("End printf for the mad repo");
        	
        	
        	//------------------------------------------------------------------------
        	 
		//SudFrame f = new SudFrame();
  	}	
}


//**************************
