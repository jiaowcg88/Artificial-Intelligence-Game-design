Instructions of how to run this program!!

1.	How to compile and run this program:
This project only has one java document named TicTacToe.java. You can compile this file using Java IDE, such as Eclipse. 
1). Once start to click the run the program. 
It will come out likes: 
 
2). Then we click  OK, it will ask for choosing the level of game.
 
3). After click Ok, we need to click the button of easy or medium or hard to start to play the game. 
When we choose any of the button among these three button, it will come out like asking who gonna play first, for computer first then enter 1, otherwise enter 2. 
 
4).If we choose easy button and enter 1 for computer first, then the game starts. First the computer makes a randomly move and it’s our turn to play. So it will keep on running. If the game ends, we can exit system. 
This is computer first and easy level to go:
 
5).if we choose medium or hard and enter 2 for us first, then the game starts, we need to choose the square of the board to place the symbol and then the computer will make a move by running the alpha-beta pruning algorithms.


This is us make a first move and choose medium to go, which is similar to choose hard to go and human make a first move. 
 
Then we can keep playing until the game ends. 
6). If you want to disrupt the game, we can click ‘Quit’ button to quit the program or click ‘Start’ button to start a new game. 
7). When the game ends, it will exit system. 


2.	Description of Game design:
In this Project, we design a TIC TAC TOE game with on AI player ‘X’ and Human player ‘O’. 
2.1 strategy or algorithm used for designing this game
We designed three different level of difficulty for this project: 1(easy),2(intermediate) and 3(difficult). 

For the easy level, we let the AI player ‘X’ to play randomly. In this way, human player ‘O’ can easily compete over AI player. 

For the difficulty level, we use the evaluated function provided by instructor:
 where s is the current state, Xn is the number of rows, columns, or diagonals with exactly n X’s and no O’s. Similarly, On is number of rows, columns, or diagonals with exactly n O’s and no X’s. In order to efficiently find the best move for AI player ‘X’, we used Alpha-Beta Search algorithm to find the best point to move. 
Every time the Alpha-Beta search function is called to return the best action, output the required information such as (1) whether cutoff occurred, (2) maximum depth reached, (3) total number of nodes generated (including root node), and (4) number of times pruning occurred within the MAX-VALUE function and (5) number of times pruning occurred within the MIN-VALUE function.

For the intermediate level, we modify the evaluated function which showed in difficult level description and AI player ‘X’ makes a move according the modified evaluated function. The main difference is that the function here does not check the symbols either ‘X’ or ‘O’ on the two diagonals. So in this way, we play the symbols on the diagonals and will lead to win, otherwise will draw or lose the game. 

2.2	GUI to display the 4X4 grid on the screen
In order to let the human player easily play the Tic Tac Toe, we designed and implemented a graphical user interface(GUI) that displays the 4X4 grid on the screen. So, the human player ‘O’ can pick the available point to place a symbol. 
Here we used JFrame to write this GUI. The framework of JFrame designed as follows:
 
2.3	Classes defined and designed

This project has five classes, including class Point, PointsAndScores, Board, TTTPanel and TicTacToe. 
First, Class Point defines a point position. Class PointsAndScores defines an object holding point position and its score. Class Board defines board of game and many methods to be used for choosing efficient moves. TTTPanel and TicTacToe use GUI to display the board screen and the user can easily use the mouse to select the square to place a symbol. The details of classes were obtained from the code of this TicTacToe.java document. 
