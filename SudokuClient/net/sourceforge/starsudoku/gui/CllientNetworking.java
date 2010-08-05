package net.sourceforge.starsudoku.gui;

import java.net.*;
import java.io.*;

class ClientNetworking
{
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	ClientNetworking()
	{
		
	}
	public void listenSocket()
	{
		//Create socket connection
		try
		{
		       socket = new Socket("localhost", 12346);
		       System.out.println("Connected to" + socket.getInetAddress() + " on port " + socket.getPort() + "from port " + socket.getLocalPort() + " of " +  socket.getLocalAddress());
		       out = new PrintWriter(socket.getOutputStream(), true);
		       in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (UnknownHostException e) 
		{
			System.out.println("Unknown host: kq6py.eng");
			System.exit(1);
		}
		catch  (IOException e) 
		{
			System.out.println("No I/O");
			System.exit(1);
		}
		int i=-1;
		while(i==-1)	
		{		
			try
			{
				String line = in.readLine();
				if(line.length() > 0)
				{
					System.out.println("You Won!!!");
					i=0;
				}
			}
			catch (IOException e)
			{
				System.out.println("Read failed");
				System.exit(1);
			}	
		}		
	}

}