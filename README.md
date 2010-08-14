# MultiSudoku #

### Build Instructions ###

MultiSudoku has only been tested on Ubuntu. the following steps will get you up and running:

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



GNU General Public License,

Copyright 2010 Rasika Bindoo


This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
