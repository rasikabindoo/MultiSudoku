import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;
import java.util.Date;

import starsudoku. *;
import starsudoku.GV.NumDistributuon;

class ClientWorker implements Runnable 
{
	
	private Socket client;
	private JTextArea textArea;
	
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	
	public int [][] gridd;
	public boolean[][] fieldsDefault;
	
	GridGenerator gg;
	ClientWorker(Socket client, JTextArea textArea,GridGenerator gg) 
	{
		
	    try
	    {
	    	
	    	this.client = client;
	    	this.gg = gg;
	    	
	    	//--------------------
	    	
	    	gridd = new int[9][9];
	    	gridd = gg.getGrid().getFields();
	    	
	    	fieldsDefault = new boolean[9][9];
	    	fieldsDefault = gg.getGrid().getfieldsDefault();
	        
	    	//---------------------
	    	
	    	System.out.println("Connected to" + client.getInetAddress() + " on port " + client.getPort() + "from port " + client.getLocalPort() + " of " +  client.getLocalAddress());
	    	SocketThrdServer.arrayClientSockets[SocketThrdServer.index] = client;
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
	    //this.start(gg);
	}

	public void run()	
	{
		String line;
		BufferedReader in  = null;
		PrintWriter[] out = new PrintWriter[10] ;
		
		try 
		{
			//oos.writeObject(new Date());
			System.out.println("Before Writing the object");
			//oos.writeObject(gg.getGrid());
			oos.writeObject(gridd);
			oos.writeObject(fieldsDefault);
			System.out.println("After Writing the object");
			oos.flush();
			// close streams and connections
			ois.close();
			oos.close();
		//	client.close(); 
		}
		catch(Exception e) 
		{
			System.out.println("Exception in run thing" + e);
		}  
		
			boolean clientSignal = false;
			while(clientSignal)
	//	while(false)
		{
			try
			{
				System.out.println(client.getPort());
				line = in.readLine();
				if(line.length()>0)
				{
					clientSignal= false;
					for(int i=0; i<10; i++)
					{
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
				//Send data back to client
			//	sleep(100);
			//	out.println(line);
				textArea.append(line);
			}
			catch (IOException e) 
			{
				System.out.println("**********************"+e);
				System.out.println("Read failed");
				System.exit(-1);
			}
    	}
	}
}

public class SocketThrdServer extends JFrame
{
	//----------------------------------------------------
	protected static GridGenerator gg;
	
	  private int diff;
	  private NumDistributuon nD;
	//-----------------------------------------------------
	
	
	JLabel label = new JLabel("Text received over socket:");
	JPanel panel;
	JTextArea textArea = new JTextArea();
	ServerSocket server = null;
//	public int array [20] ;
	//int[] dummyArray;
//	dummyArray[] = new int[10];

	public static Socket[] arrayClientSockets = new Socket[10] ;
	public static int index = 0;

	//arrayClientSockets[] = dummyArray[];


	//int[] i = new int[10]; 

	SocketThrdServer()
	{ 
		//Begin Constructor
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.white);
		getContentPane().add(panel);
		panel.add("North", label);
		panel.add("Center", textArea);
		
		//--------------------------------------------------------------------------
		  gg = new GridGenerator();
	        diff = GV.DIFF_NORMAL;
	        nD = NumDistributuon.evenlyFilled3x3Cubes;
	        
	        
	        gg.getGrid().resetGrid();
	        gg.generate(diff, nD);
	        
	       // int [][] gridd = new int[9][9];
	        
	      //  gridd = gg.getGrid().getFields();
	        
	    /*    for(int i=0; i<9; i++){
	        	for(int j=0;j<9;j++){
	        		System.out.println("\t"+gridd[i][j]);
	        	}
	        	System.out.println("\n");
	        }
	      */  
	        
		//--------------------------------------------------------------------------
	        
	       
	} //End Constructor

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
				w = new ClientWorker(server.accept(), textArea, gg);
				Thread t = new Thread(w);
				t.start();
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
		SocketThrdServer frame = new SocketThrdServer();
		frame.setTitle("Server Program");
	        WindowListener l = new WindowAdapter() 
		{
	                public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
                	}
        	};
	        frame.addWindowListener(l);
        	frame.pack();
        	frame.setVisible(true);
        	frame.listenSocket();
  	}	
}


//--------------------------------------------------------------------------------------------




