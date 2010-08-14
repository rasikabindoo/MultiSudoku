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


