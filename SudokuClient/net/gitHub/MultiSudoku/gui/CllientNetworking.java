
/**
 * 
 * 
 * @author Rasika Bindoo
 
 This file is part of Multisudoku.

    Multisudoku is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Multisudoku is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Multisudoku.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.gitHub.MultiSudoku.gui;

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