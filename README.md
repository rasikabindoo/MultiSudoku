# MultiSudoku #




This software is developed to conduct Sudoku competitions.

Purpose:
This project is aimed building a software that can be used to conduct Sudoku competitions.

What is a Sudoku?
Sudoku is a logic-based, number-placement puzzle. The objective is to fill a 9×9 grid with digits so that each column, each row, and each of the nine 3×3 sub-grids that compose the grid (also called “boxes”, “blocks”, “regions”, or “sub-squares”) contains all of the digits from 1 to 9. The puzzle setter provides a partially completed grid, which typically has a unique solution.

What is MultiSudoku all about?
MultiSudoku has a server to which the clients connect. All the clients get the same puzzle from the server. Whenever a player solves the Sudoku puzzle correctly a signal is sent to the server. The server immidiately sends a signal to the remaining clients indicating that a player has won.
The winner name is displayed on the server UI as well as on the other players’ UI.

In Case of any issues you can mail me at : mailto:rasika@pdx.edu

### Build Instructions ###

MultiSudoku works on Windows ,linux and Mac . 
The following are the build instructions for linux :

Remember to run the Server first and then the client 

   Clone the git repository

	> git clone git@github.com:rasikabindoo/MultiSudoku.git 

   Build the Server

	> cd MultiSudoku
	> cd Server
	> ant -f build.xml
		This will place the jar file in the dist folder
	> cd dist
	> java -jar Server.jar

   Build the Client

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

