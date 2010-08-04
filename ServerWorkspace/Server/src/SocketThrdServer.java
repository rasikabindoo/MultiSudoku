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
	
  	
	ClientWorker(Socket client, JTextArea textArea,GridGenerator gg) 
	{
		
	 //   try
	//    {
	    	
	    	this.client = client;
	    	
	    	System.out.println("Connected to" + client.getInetAddress() + " on port " + client.getPort() + "from port " + client.getLocalPort() + " of " +  client.getLocalAddress());
	    	SocketThrdServer.arrayClientSockets[SocketThrdServer.index] = client;
	    	SocketThrdServer.index = SocketThrdServer.index + 1;
	    	for(int i=0; i<10; i++)
	    	{
	    		if(SocketThrdServer.arrayClientSockets[i]!=null)
	    		{
	    			System.out.println("\n" + SocketThrdServer.arrayClientSockets[i].getPort());
	    		}
	    	}
	    	System.out.println("Before creating the object connect");
	    	Connect c = new Connect(client,gg);
	    	
	    	/*this.textArea = textArea;
		
	    	ObjectOutputStream oos = null;

	    	oos = new ObjectOutputStream(client.getOutputStream());
		
	    	oos.writeObject(gg);
*/
	    	
	//    }
/*	    catch(IOException e)
	    {
	    	System.out.println("IO exception");	    	
	    
	    }*/
	    	
	    	
	    	//--------------------------------------------------
	    	ObjectOutputStream oos;
	    	ObjectInputStream ois;
	    	
	    	 try 
			   {
				   ois = new ObjectInputStream(client.getInputStream());
				   oos = new ObjectOutputStream(client.getOutputStream());
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
			   System.out.println("Before writing the object");
			   try
			   {
	    	//		   oos.writeObject(gg);
	    			   
	    			   oos.writeObject(new Date());

	    			   	System.out.println("After writing the object");
	    			   	
	    		        oos.flush();
	    		        // close connections
	    		        ois.close();
	    		        oos.close();
	    	//	        client.close(); 
			   }
	    		catch (IOException e)
	    		{
	    			   System.out.println(e);
	    		}
	    	//--------------------------------------------------
	}

	public void run()	
	{
		String line;
		BufferedReader in  = null;
		PrintWriter[] out = new PrintWriter[10] ;
		//public static Socket[] arrayClientSockets = new Socket[10] ;
	//	try
//		{
			//*******************************
		//	in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		//	out = new PrintWriter(client.getOutputStream(), true);
//		}
	/*	catch (IOException e) 
		{
			System.out.println("in or out failed");
			System.exit(-1);
		}*/
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
//Printing out to all the clients
							
							/*out[1] = new PrintWriter(SocketThrdServer.arrayClientSockets[1].getOutputStream(), true);
							System.out.println("Port is " + SocketThrdServer.arrayClientSockets[1].getPort());
//							System.out.println("I is " + i);
							System.out.println("I is " + 0);
							out[1].println(line);
*/

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
		//--------------------------------------------------------------------------
	        
	        
	} //End Constructor

	public void listenSocket()	
	{
		try
		{
			server = new ServerSocket(12346); 
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
class Connect extends Thread
{
	   private Socket client = null;
	   //private ObjectInputStream ois = null;
	   private ObjectOutputStream oos = null;
	   
	   GridGenerator gg;
	    
	   public Connect() {}

	   public Connect(Socket clientSocket,GridGenerator gg) 
	   {
		   System.out.println("in the constructor of socket");
		   client = clientSocket;
		   this.gg= gg;
	     
		   try 
		   {
			   // ois = new ObjectInputStream(client.getInputStream());
			   oos = new ObjectOutputStream(client.getOutputStream());
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
		   //-------------------------------------------------------
		//   	this.start();
		   //-------------------------------------------------------
	   }

	   public void run() 
	   {
		   try
		   {
			   System.out.println("Before writing the object");
			   
	//		   oos.writeObject(gg);
			   
			   oos.writeObject(new Date());

			   	System.out.println("After writing the object");
			   	
		        oos.flush();
		        // close connections
//		        ois.close();
//		        oos.close();
//		        client.close(); 
		        oos.flush();
		   }
		   catch (IOException e)
		   {
			   System.out.println(e);
		   }
		    
	    }
}
	





