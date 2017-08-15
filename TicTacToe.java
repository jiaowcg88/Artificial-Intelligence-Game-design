import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * This Class includes five classes including class Point, PointsAndScores, Board, TTTPanel and TicTacToe. 
 * Class Point defines a point position. Class PointsAndScores defines an object holding point position and its score.
 * Class Board defines board of game and many methods to be used for choosing efficient moves. 
 * TTTPanel and TicTacToe use GUI to display the board screen and the user can use the mouse to select the square to place a symbol.
 */

/**
 * Point class defines an object with two variable x, y which is similar as the x axis value of point and y axis value of point. 
 * @author jiao wu
 *
 */
class Point {

    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}

/**
 * 
 *  PointsAndScores class defines an object with two variables, one is score and the other is point. 
 *
 */
class PointsAndScores {

    int score;
    Point point;

    PointsAndScores(int score, Point point) {
        this.score = score;
        this.point = point;
    }
}

/**
 * The Board class models the game-board
 *
 */
class Board {
    List<Point> availablePoints;  // define a list availablePoints to holds all available points  
    char[][] board = new char[4][4]; // initialize board with 4X4 array with holding char type value
    List<PointsAndScores> rootsChildrenScore = new ArrayList<>(); //define a list rootsChildrenScore to hold all PointsAndScores object with their scores and points.
    int countMax = 0;
    int countMin = 0;
    int max = 0;
    boolean cut_off_occur = false;
    //initialize the game board, in the beginning , the board value is empty . 
    int number_node = 0;
    public  void initBoard() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				board[i][j] = ' '; // blank char
			}
		}
	}
    //evaluate the board for hard level of game
    public int evaluateBoard() {
        int score = 0;
        //Check all rows, count the number of X or O IN ALL ROWS. 
        for (int i = 0; i < 4; ++i) {
            int blank = 0;
            int X = 0;
            int O = 0;
            for (int j = 0; j < 4; ++j) {
                if (board[i][j] == ' ') {
                    blank++;
                } else if (board[i][j] == 'X') {
                    X++;
                } else {
                    O++;
                }

            } 
            score+=changeInScore(X, O);  // pass the number of X  and O to the function of changeInScore to get the current score
        }

        //Check all columns, count the number of X or O IN ALL Columns.
        for (int j = 0; j < 4; ++j) {
            int blank = 0;
            int X = 0;
            int O = 0;
            for (int i = 0; i < 4; ++i) {
                if (board[i][j] == ' ') {
                    blank++;
                } else if (board[i][j] == 'X') {
                    X++;
                } else {
                    O++;
                } 
            }
            score+=changeInScore(X, O); // pass the number of X  and O to the function of changeInScore to get the current score
        }

        int blank = 0;
        int X = 0;
        int O = 0;

        //Check diagonal (first)
        for (int i = 0, j = 0; i < 4; ++i, ++j) {
            if (board[i][j] == 'X') {
                X++;
            } else if (board[i][j] == 'O') {
                O++;
            } else {
                blank++;
            }
        }

        score+=changeInScore(X, O);
        blank = 0;
        X = 0;
        O = 0;

        //Check Diagonal (Second)
        for (int i = 3, j = 0; i > -1; --i, ++j) {
            if (board[i][j] == 'X') {
                X++;
            } else if (board[i][j] == 'O') {
                O++;
            } else {
                blank++;
            }
        }

        score+=changeInScore(X, O);

        return score;
    }
    
    // Using heuristic function of 6X3 + 3X2 + X1 -(6O3 + 3O2 + O1)
    private int changeInScore(int X, int O){
        int change;
        if (X == 4) {
            change = 6;
        } else if (X == 3 && O == 0) {
            change = 3 ;
        } else if (X == 2 && O == 0) {
            change = 1;
        } else if (O == 4 && X == 0) {
            change = -6;
        } else if (O == 3 && X == 0) {
            change = -3;
        } else if (O == 2 && X == 0) {
            change = -1;
        } else {
            change = 0;
        } 
        return change;
    }
    
    //Set this to some value if you want to have some specified depth limit for search, alphaBetaMinimax is to compute the best move of player, here is for AI player.
    int uptoDepth = 8;// set the cutoff level is 5 
    public int alphaBetaMinimax(int alpha, int beta, int depth, char turn){
        // cut_off pruning happen here
    	if(beta<=alpha){ 
      	    cut_off_occur = true; 
        	if(turn == 'X'){
        		countMax++; //  count the number of max_value function called
        		return 1000; 
        	} else if (turn == 'O'){
        		countMin++; // count the number of min_value function called
        		return -1000; 
        	}	
        }
        // GameOver or depth reached, evaluate score
        if(depth == uptoDepth || isGameOver()){
    	    if (depth >= max) max = depth;
            return evaluateBoard(); 
        }
        //generated all the points
        List<Point> pointsAvailable = getAvailableStates();   
        // if all points are choose, then return 0
        if(pointsAvailable.isEmpty()) return 0;  
        if(depth==0) rootsChildrenScore.clear();  
        
        int maxValue = -1000, minValue = 1000;
        for (int i=0;i<pointsAvailable.size(); ++i){
             Point point = pointsAvailable.get(i);  // get point   
             int currentScore = 0; 
            //call max_value function
            if(turn == 'X'){  
	            placeAMove(point, 'X'); // place X in point
	            number_node++;
	            currentScore = alphaBetaMinimax(alpha, beta, depth+1, 'O');
	            maxValue = Math.max(maxValue, currentScore); 
	            //Set alpha
	            alpha = Math.max(currentScore, alpha);  
	            if(depth == 0)
	               rootsChildrenScore.add(new PointsAndScores(currentScore, point)); // add object with point position and score to rootsChildrenScore.
	            //call min_value function
            }else if(turn == 'O'){
                placeAMove(point, 'O'); // place O in point
                number_node++;
                currentScore = alphaBetaMinimax(alpha, beta, depth+1, 'X');  
                minValue = Math.min(minValue, currentScore);     
                //Set beta
                beta = Math.min(currentScore, beta);
            }
            
            //reset board check whether it is right or not
            board[point.x][point.y] = ' ';       
            //If a pruning has been done, don't evaluate the rest of the sibling states
            if(currentScore == 1000 || currentScore == -1000) break;
        }
        return turn == 'X' ? maxValue : minValue;
    }  
    
  //This evaluate board function is for medium level of the game, this evaluate function did not check values in the ï½„ï½‰ï½�ï½‡ï½�ï½Žï½�ï½Œ. 
    public int evaluateBoard2() {
        int score = 0;
        //Check all rows
        for (int i = 0; i < 4; ++i) {
            int blank = 0;
            int X = 0;
            int O = 0;
            for (int j = 0; j < 4; ++j) {
                if (board[i][j] == ' ') {
                    blank++;
                } else if (board[i][j] == 'X') {
                    X++;
                } else {
                    O++;
                }

            } 
            score+=changeInScore2(X, O); 
        }

        //Check all columns
        for (int j = 0; j < 4; ++j) {
            int blank = 0;
            int X = 0;
            int O = 0;
            for (int i = 0; i < 4; ++i) {
                if (board[i][j] == ' ') {
                    blank++;
                } else if (board[i][j] == 'X') {
                    X++;
                } else {
                    O++;
                } 
            }
            score+=changeInScore2(X, O);
        }
        return score;
    }
    
    // Using heuristic function of 6X3 + 3X2 + X1 -(6O3 + 3O2 + O1)
    private int changeInScore2(int X, int O){
        int change;
        if (X == 4) {
            change = 6;
        } else if (X == 3 && O == 0) {
            change = 3 ;
        } else if (X == 2 && O == 0) {
            change = 1;
        } else if (O == 4 && X == 0) {
            change = -6;
        } else if (O == 3 && X == 0) {
            change = -3;
        } else if (O == 2 && X == 0) {
            change = -1;
        } else {
            change = 0;
        } 
        return change;
    }
    
   // this alpha beta pruning algorithm is used for the level of medium to calculate the value for going to the next best step
    public int alphaBetaMinimax_med(int alpha, int beta, int depth, char turn){
        // cut_off pruning happen here
        if(beta<=alpha){ 
            cut_off_occur = true; 
            if(turn == 'X'){
                countMax++; //  count the number of max_value function called
                return 1000; 
            } else if (turn == 'O'){
                countMin++; // count the number of min_value function called
                return -1000; 
            }   
        }
        // GameOver or depth reached, evaluate score
        if(depth == uptoDepth || isGameOver()){
            if (depth >= max) max = depth;
            return evaluateBoard2(); 
        }
        //generated all the points
        List<Point> pointsAvailable = getAvailableStates();   
        // if all points are choose, then return 0
        if(pointsAvailable.isEmpty()) return 0;  
        if(depth==0) rootsChildrenScore.clear();  
        
        int maxValue = -1000, minValue = 1000;
        for (int i=0;i<pointsAvailable.size(); ++i){
             Point point = pointsAvailable.get(i);  // get point   
             int currentScore = 0; 
            //call max_value function
            if(turn == 'X'){  
                placeAMove(point, 'X'); // place X in point
                number_node++;
                currentScore = alphaBetaMinimax_med(alpha, beta, depth+1, 'O');
                maxValue = Math.max(maxValue, currentScore); 
                //Set alpha
                alpha = Math.max(currentScore, alpha);  
                if(depth == 0)
                   rootsChildrenScore.add(new PointsAndScores(currentScore, point)); // add object with point position and score to rootsChildrenScore.
                //call min_value function
            }else if(turn == 'O'){
                placeAMove(point, 'O'); // place O in point
                number_node++;
                currentScore = alphaBetaMinimax_med(alpha, beta, depth+1, 'X');  
                minValue = Math.min(minValue, currentScore);     
                //Set beta
                beta = Math.min(currentScore, beta);
            }
            
            //reset board check whether it is right or not
            board[point.x][point.y] = ' ';       
            //If a pruning has been done, don't evaluate the rest of the sibling states
            if(currentScore == 1000 || currentScore == -1000) break;
        }
        return turn == 'X' ? maxValue : minValue;
    }  

  //return whether Game is over or someone has won, or board is full (draw)
    public boolean isGameOver() {
        
        return (hasXWon() || hasOWon() || getAvailableStates().isEmpty());
    }

    // check for X Won
    public boolean hasXWon() {
    	// check two diagonally lines
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == board[3][3]&& board[0][0] == 'X') || (board[0][3] == board[1][2] && board[0][3] == board[2][1]&& board[0][3] == board[3][0] && board[0][3] == 'X')) {
            return true;
        }
        //check all the columns or rows
        for (int i = 0; i < 4; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] &&board[i][0] == board[i][3] && board[i][0] == 'X')
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == board[3][i] && board[0][i] == 'X')) {
                return true;
            }
        }
        return false;
    }
    
    // check for O Won
    public boolean hasOWon() {
    	// check two diagonally lines
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == board[3][3]&& board[0][0] == 'O') || (board[0][3] == board[1][2] && board[0][3] == board[2][1]&& board[0][3] == board[3][0] && board[0][3] == 'O')) {
            return true;
        }
        // check all the columns or rows
        for (int i = 0; i < 4; ++i) {
        	if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] &&board[i][0] == board[i][3] && board[i][0] == 'O')
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == board[3][i] && board[0][i] == 'O')) {
                return true;
            }
        }
        return false;
    }

    // get all available points.
    public List<Point> getAvailableStates() {                  
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (board[i][j] == ' ' ) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints;
    }

    // place the Player in specfic point
    public void placeAMove(Point point, char player) {
        board[point.x][point.y] = player;  
    }
    // return the best move by computing the highest score of the rootsChildrenScore object
    public Point returnBestMove() {
        int MAX = -100000;
        int best = -1;
        for (int i = 0; i < rootsChildrenScore.size(); ++i) {
            if (MAX < rootsChildrenScore.get(i).score) {
                MAX = rootsChildrenScore.get(i).score;
                best = i;
            }
        }

        return rootsChildrenScore.get(best).point;
    }
}

/**
 * Class TTTPanel implements MouseListener, fires a MouseEvent upon mouse-click
 * @author jiao wu
 *
 */
class TTTPanel extends JPanel implements MouseListener {
	private int size = 800;
	private TicTacToe tttGame;
	/**
	 * Constructor for objects of class TTTpanel
	 */
	public TTTPanel(TicTacToe tttGame) {
		setPreferredSize(new Dimension(size, size)); //set the size of TTTpanel
		addMouseListener(this);  
		this.tttGame = tttGame;
	}

	public void paint(Graphics g) { // Draw the grid -lines
		g.setColor(Color.WHITE);
		int weight = getWidth();// get the width of JPanel
		int height = getHeight(); // get the height of JPanel
		g.fillRect(0, 0, weight, height); // 
		g.setColor(Color.BLACK);
		//draw six lines
		g.drawLine(0,200,800,200); // draw a horizontal line from (0,200) to (800, 200)
		g.drawLine(0,400,800,400);//draw a horizontal line from (0,400) to (800, 400)
		g.drawLine(0,600,800,600); //draw a horizontal line from (0,600) to (800, 600)
		g.drawLine(200,0,200,800); // draw a vertical line from (200,0) to (200,800)
		g.drawLine(400,0,400,800);// draw a vertical line from (400,0) to (400,800)
		g.drawLine(600,0,600,800);// draw a vertical line from (600,0) to (600,800)
		Font f = new Font("Arial", Font.PLAIN, 50);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		int a = fm.getAscent();
		int h = fm.getHeight();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				char curSquare = tttGame.getSquare(i,j);
				String curSqStr = Character.toString(curSquare);
				int w = fm.stringWidth(curSqStr);
				g.drawString(curSqStr, 200*i + 100 - w/2, 200*j + 100 + a - h/2); // draw a String to board
			}
		}
		
		repaint(); //draw the board
	}

	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	
    // mouse-clicked handler
	public void mouseClicked(MouseEvent e) {
		//get the row and column clicked
		int x = e.getX() / 200;  
		int y = e.getY() / 200;
        //check the board is empty, computer first or player first, the level of game. 
		if (tttGame.getSquare(x,y) ==' ' && (!tttGame.computerFirst) && (tttGame.gameLevel == 1) ) { // easy level
			tttGame.makeMove(x,y); // player O make a move 
			repaint();  // paint the board
			tttGame.checkForGameEnd(); // check for gameEnd
			tttGame.randomMove();  // for easy level, the computer X play randomly
			tttGame.checkForGameEnd();  // check for gameEnd
		} else if (tttGame.getSquare(x,y) == ' ' && (!tttGame.computerFirst) && (tttGame.gameLevel == 2) ) { // medium level
			tttGame.b.board[x][y] = 'O';  // place O in the board of Board class 
			tttGame.makeMove(x, y); // player O make a move
			repaint(); // paint the board
			tttGame.checkForGameEnd(); //check for gameEnd
	        tttGame.b.alphaBetaMinimax_med(-1000, 1000, 0 , 'X'); // call alphaBetaMinimax-med pruning 
			tttGame.printResult();
            Point point = tttGame.b.returnBestMove(); // return the point with the best move
			tttGame.b.board[point.x][point.y] = 'X';// Place X in the board of Board class
			tttGame.makeMove(point.x, point.y); // computer X make a move according to the point with the best move
			tttGame.checkForGameEnd(); // check for gameEnd
		} else if (tttGame.getSquare(x,y) == ' ' && (!tttGame.computerFirst) && (tttGame.gameLevel == 3) ){ // hard level
			tttGame.b.board[x][y] = 'O';// place O in the board of Board class 
			tttGame.makeMove(x, y); //// player O make a move
			repaint();//paint the board
			tttGame.checkForGameEnd(); //check for gameEnd
			int a = tttGame.b.alphaBetaMinimax(-1000, 1000, 0 , 'X');// call alphaBetaMinimax pruning
			tttGame.printResult();
			Point point = tttGame.b.returnBestMove(); // get the best point to move
			tttGame.b.board[point.x][point.y] = 'X'; // place X in the board of Board class
			tttGame.makeMove(point.x, point.y); // computer X make a move
		    tttGame.checkForGameEnd();// check for game end
		}
	}
}

/**
 * 
 * TicTacToe class for implement tic- tac- toe game
 *
 */
public class TicTacToe extends JFrame implements ActionListener{
	public static final int ROWS = 4, COLS = 4;
	public static boolean computerFirst = true;
	public static char currentPlayer;
	public static int gameLevel;
	private char[][] board;
	private int turn;
	private JLabel message;
    private JPanel topPanel, mainPanel; // Points area
    private JLabel messageLabel, messageLabel2, messageLabel3, messageLabel4,messageLabel5,messageLabel6;
    private JPanel bottomPanel; // Game area
    private JButton newB, quitB, easyB, medB, difB;
    private Font font;
	Board b = new Board();
	
	/**
	 * Constructor for objects of class TicTacToe
	 */
	public TicTacToe() {
		super("Tic Tac Toe");
		// Setup message
        font = new Font("Arial", Font.PLAIN, 28);
        message = new JLabel("You are 'O', computer is 'X'");
        message.setFont(font);
        message.setForeground(new Color(0xFFFFFF));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Top panel for message
        topPanel = new JPanel();
        topPanel.add(message);
        topPanel.setBackground(new Color(0x0076A3));
        
        //Bottom Panel for button
        bottomPanel = new JPanel();
        newB = new JButton("Start");
        quitB = new JButton("Quit");
        easyB = new JButton("Easy");
        medB = new JButton("Medium");
        difB = new JButton("Hard"); 
        newB.setFont(font);
        quitB.setFont(font);
        easyB.setFont(font);
        medB.setFont(font);
        difB.setFont(font);
        bottomPanel.add(newB);
        bottomPanel.add(quitB);
        bottomPanel.add(easyB); 
        bottomPanel.add(medB);
        bottomPanel.add(difB);
        newB.addActionListener(this);
        quitB.addActionListener(this);
        easyB.addActionListener(this);
        medB.addActionListener(this);
        difB.addActionListener(this);
      
        // instantiate the actions for the buttons
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel); // add topPanel to mainPanel
        mainPanel.add(new TTTPanel(this)); // add TTTPanel object to mainPanle
        mainPanel.add(bottomPanel);  // add bottomPanel to mainPanel
        getContentPane().add(mainPanel);  // display all content
		pack();
		setLocationRelativeTo(null);  // show the jframe to the central location
		setVisible(true); // Show the JFrame.

        setAlwaysOnTop(true);   
		setTitle("Tic Tac Toe Game");  // set the frame title is Tic Tac Toe 
		setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;        
		board = new char[4][4]; // initialize board
		initBoard(); 
	    messageLabel = new JLabel("WELCOME TO THIS GAME");  // define a JLable as WELCOME TO THIS GAEM 
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 20)); // change the font of this JLable
		JOptionPane.showMessageDialog(this, messageLabel, "WELCOME", JOptionPane.INFORMATION_MESSAGE); // add the edited JLable to showMessageDialog
		messageLabel2= new JLabel("Please choose the level of game"); // define a JLable as Please choose the level of game
		messageLabel2.setFont(new Font("Arial", Font.PLAIN, 20)); // change the font of this JLable
		JOptionPane.showMessageDialog(this, messageLabel2, "Choose level", JOptionPane.INFORMATION_MESSAGE); // add the edited JLable to showMessageDiaglog
	    
	}
	
	//Button actionEvent 
	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand() == "Quit"){ // if click Quit button, then exit the system
			System.exit(0);	
		} else if (e.getActionCommand() == "Start"){ // if click the Start button, then initialize the game
		   //initBoard();
			TicTacToe newGame = new TicTacToe();
		} else if (e.getActionCommand() == "Easy"){// if click the Easy, then choose the easy level of the game 
			gameLevel = 1;
			makeChoose(); // call the makeChoose function to  choose whether computer X plays first or player O plays first
		} else if (e.getActionCommand() == "Medium"){// if click the Medium button, then choose the medium level of the game
			gameLevel = 2;
			makeChoose();// call the makeChoose function to  choose whether computer X plays first or player O plays first
		} else if (e.getActionCommand() == "Hard"){//if click the Hard button, then choose the hard level of the game
			gameLevel = 3;
			makeChoose();// call the makeChoose function to  choose whether computer X plays first or player O plays first
		}
	}
    // have a choose whether computer X plays first or player O plays first
	public void makeChoose(){
	    messageLabel3= new JLabel("who gonna play first, please enter 1 for computer first or 2 for you first"); //Similar, define the third JLabel as who ganna play first, please enter 1 for computer first or 2 for you first
		messageLabel3.setFont(new Font("Arial", Font.PLAIN, 20)); // change the font 
		String choice = JOptionPane.showInputDialog(this,messageLabel3 , "Choose to Go",1); // add this to showInputDialog
		// choice is 1 for computer first
		if(choice.equals("1") && gameLevel == 1){ // easy level
			currentPlayer = 'X'; // currentPlayer is X
			randomMove(); // Computer X move randomly
			computerFirst = false; // change computerFirst to false
		} else if (choice.equals("1") && (gameLevel == 3)) { // choose hard level
			currentPlayer = 'X'; // currentPlayer is X
			b.initBoard(); // initialize the board of Board class 
		    b.alphaBetaMinimax(-1000, 1000, 0, 'X'); // call alphabetaMinimax function
		    printResult(); // print the result
		    Point point = b.returnBestMove(); // get the best point to move
            b.board[point.x][point.y] = 'X';// update the point of board class to X
            makeMove(point.x, point.y);//Computer X make a move
			computerFirst = false; //change computerFirst to false
		}else if (choice.equals("1") && gameLevel == 2){ // choose medium level
			currentPlayer = 'X'; // currentPlayer is X
			b.initBoard(); // initialize the board of Board class 
		    b.alphaBetaMinimax_med(-1000, 1000, 0 , 'X'); // call alphabetaMinimax function
			printResult();
            Point point = b.returnBestMove(); // get the best point to move 
            b.board[point.x][point.y] = 'X'; // update the point of board class to X
            makeMove(point.x, point.y); // Computer X makes a move
			computerFirst = false; // change computerFirst to false
		}
		else if(choice.equals("2")) { // choice is 2 meaning computer O player first
			b.initBoard(); // initialize the board of Board class
			currentPlayer = 'O'; //current player is O
			computerFirst = false; // change computerFirst to false
		}
	}
	
	//print all information to the screen
	public void printResult(){
		System.out.println("Cut off happens: " + b.cut_off_occur); // print whether cut off happens
	    System.out.println("Reached maximun depth:  "+ b.max); // print reached maximum depth
	    System.out.println("Number of node generated: "+ b.number_node); // print the number of node generated
	    System.out.println("Number of pruning happening in MAX function "+ b.countMax); // print the number of pruning happening in max function
	    System.out.println("Number of pruning happening in Min function "+ b.countMin); // print the number of pruning happening in min function 
	    System.out.println("-----------------------------------------------------------------");
	    // set all value back
	    b.number_node = 0;
	    b.cut_off_occur = false;
	    b.max = 0;
	    b.countMax = 0;
	    b.countMin = 0;
	}
	
	// initialize the board
    public  void initBoard() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				board[i][j] = ' '; // blank char
			}
		}
	}
	
    //get current Player
	public char getCurPlayer() { 
		return currentPlayer; }
	
	//place currentPlayer to position (x,y)
	public void makeMove(int x, int y) {
		setSquare(x, y, getCurPlayer()); 
		if (getCurPlayer() == 'X') { // After currentPlayer X makes a move, then change currentPlayer to O
			currentPlayer = 'O';
		}else  {
			currentPlayer = 'X'; //after currentPlayer O makes a move, then change currentPlayer to X
			
		}
		turn++;
	}
	
	//put c to board[x][y]
	private void setSquare(int x, int y, char c) { board[x][y] = c; }

	//given x, y, we can get the content of board[x][y]
	public char getSquare(int x, int y) {
		return board[x][y];
	}

	// check for wins
	private boolean checkForWin(char p) {
		for (int i = 0; i < 4; i++) {
			// check rows
			if (board[i][0] == p && board[i][1] == p && board[i][2] == p && board[i][3] == p) { return true; }
			// check columns
			if (board[0][i] == p && board[1][i] == p && board[2][i] == p && board[3][i] == p ) { return true; }
		}
		//check diagonals
		if (board[0][0] == p && board[1][1] == p && board[2][2] == p && board[3][3] == p) { return true; }
		if (board[0][3] == p && board[1][2] == p && board[2][1] == p&& board[3][0] == p) { return true; }
		return false;
	}
    
	//after make a move, then getMovesLeft will become less one.
	private int getMovesLeft() { return (ROWS*COLS)-turn; }
    // check whether board is full or not. 
	private boolean checkFullBoard() { return getMovesLeft()<1; }
    
	// check for game end
	public void checkForGameEnd() {
		// JLables will added to showMessageDialog accordingly. 
		messageLabel4 = new JLabel("Unfortunatelly, you losed!!!!");
		messageLabel4.setFont(new Font("Arial", Font.PLAIN, 20));
		messageLabel5 = new JLabel("Congradutations, you Won!!!!");
		messageLabel5.setFont(new Font("Arial", Font.PLAIN, 20));
		messageLabel6 = new JLabel("Game over, draw.");
		messageLabel6.setFont(new Font("Arial", Font.PLAIN, 20));
		if (checkForWin('X')) { // if X won, then show messageLable4
			JOptionPane.showMessageDialog(this,messageLabel4);
			System.exit(0);
		} else if (checkForWin('O')) { // if O won, then show messageLabel5
			JOptionPane.showMessageDialog(this,messageLabel5);
			System.exit(0);
		} else if (checkFullBoard()) { // if board is full, then show mesageLabel6
			JOptionPane.showMessageDialog(this,messageLabel6);
			System.exit(0);
		}

	}
    
	//method for randomMove
    public void randomMove() {
		int pos = new Random().nextInt(getMovesLeft());
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (board[i][j] == ' ') {
					if (--pos<0) {
						makeMove(i,j);
						return;
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		TicTacToe newGame = new TicTacToe(); // main method, once launch, it will initialize a new Tic Tac Toe game. 
	}
	    
}
