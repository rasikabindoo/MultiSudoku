# MultiSudoku #

### Build Instructions ###

MultiSudoku works on Windows ,linux and Mac . 
The following are the build instructions for linux :

Remember to run the Server first and then the client 

  1. Clone the git repository

	> git clone git@github.com:rasikabindoo/MultiSudoku.git 

  2. Build the Server

	> cd MultiSudoku
	> cd Server
	> ant -f build.xml
		This will place the jar file in the dist folder
	> cd dist
	> java -jar Server.jar

  3. Build the Client

	> cd MultiSudoku
	> cd Sudokuclient
	> ant -f build.xml
		This will place the jar file in the dist folder
	> cd dist
	> java -jar SudokuClient.jar


All you need to know is the IP address of the system in which the server is running.
You can connect all the clients to the server and start playing!!!


GNU General Public License,

Copyright 2010 Rasika Bindoo

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.

