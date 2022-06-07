

    package fsktm.fop;
 
import fsktm.fop.Shape.Tetrominoe;
import fsktm.fop.HighScore;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
   


 
import java.util.ArrayList;
import java.util.Scanner;
 
public class CLI {
 
    /*
    TODO
    1 - Make the CLI version work with Shape class
    2 - Introduce Block preview
    3 - Once the core game functions are fully working, then make the GUI
     */
    int width = 10;
    int height = 10;
    int previewWidth = width;
    int previewHeight = 4;
    private Tetrominoe[] board = new Tetrominoe[height * width];
    private int[] numbers = new int [width * height];
    private Tetrominoe[] previewBoard = new Tetrominoe[previewHeight * previewWidth];
    private int[] previewNumbers = new int[previewHeight * previewWidth];
    private static ArrayList<Shape> previewShape = new ArrayList<>(4);
 
    private static Shape currentShape;
    private static HighScore Score;
    private int currentX, currentY;
    private boolean isFull = false;
 
    CLI() {
        initBoard();
        initPreviewBoard();
        
 
        /*
        TODO ArrayList of Shape class
         */
        currentX = width / 2 - 1;
        currentY =  height / 2 - 1;
        putShadowShapeOnBoard(currentX ,currentY, currentShape);
 
        printBoard();
        printBlockPreviews();
        
 
        Scanner scan = new Scanner(System.in);
        String input;
        while (true) {
             displayScore();
            System.out.print("a [<-] d [->] w [↑] s [↓] r [ROTATE] i [INSERT] e [EXIT]: ");
            input = scan.nextLine();
            currentShape = previewShape.get(0);;
            if (input.equals("a")) {
                if (tryMove(currentX - 1, currentY, currentShape)) {
                    move(currentX - 1, currentY, currentShape);
                    currentX--;
                }
            } else if (input.equals("d")) {
                if (tryMove(currentX + 1, currentY, currentShape)) {
                    move(currentX + 1, currentY, currentShape);
                    currentX++;
                }
            } else if (input.equals("w")) {
                if (tryMove(currentX, currentY - 1, currentShape)) {
                    move(currentX, currentY - 1, currentShape);
                    currentY--;
                }
            } else if (input.equals("s")) {
                if (tryMove(currentX , currentY + 1, currentShape)) {
                    move(currentX, currentY + 1, currentShape);
                    currentY++;
                }
            } else if (input.equals("r")) {
                if (canRotate()) {
                    removeShadowShapeOnBoard(currentX, currentY, currentShape);
                    currentShape = currentShape.rotateRight();
                    putShadowShapeOnBoard(currentX, currentY, currentShape);
                    previewShape.set(0, currentShape);
                    
                }
                
            } else if (input.equals("i")) {
                insert();
            } else if (input.equals("e")) {
                break;
            }
            if (isFull) { // Game over
                System.out.println("GAME OVER!");
                break;
            }
            checkForColumnAndRow();
            printBoard();
            printBlockPreviews();
            
        }
    }
 
    private void checkForColumnAndRow() {
        /*
        TODO - What if both rows and columns are simultaneously even?
        boolean rowEven = false, columnEven = false;
        */
        for (int i = 0; i < width; i++) {
            if (sumIsEvenForRow(i)) {
                clearRow(i);
                System.out.println("Row " + (i+1) + " cleared!");
            }
        }
        
        for (int i = 0; i < height; i++) {
            if (sumIsEvenForColumn(i)) {
                clearColumn(i);
                System.out.println("Column " + (i+1) + " cleared!");
            }
        }
        
        
    }
 
    private boolean canRotate() {
        for (int i = 0; i < 4; i++) {
            int newX = currentX + currentShape.x(i);
            int newY = currentY + currentShape.y(i);
            if (!tryMove(newX, newY, currentShape)) {
                return false;
            }
        }
        return true;
    }
    private void initBoard() {
        for (int i = 0; i < width * height; i++) {
            board[i] = Tetrominoe.NoShape;
            numbers[i] = -1; // Use -1 to indicate its empty
        }
    }
 
    private void initPreviewBoard() {
        clearPreviewBoard();
        previewShape.add(generateRandomShape());
        previewShape.add(generateRandomShape());
        previewShape.add(generateRandomShape());
        updatePreviewBoard(); // <- Redundant (TODO)
        currentShape = previewShape.get(0);
    }
 
    private void clearPreviewBoard() {
        for (int i = 0; i < previewWidth * previewHeight; i++) {
            previewBoard[i] = Tetrominoe.NoShape;
            previewNumbers[i] = -1;
        }
    }
 
    private void updatePreviewBoard() {
        clearPreviewBoard();
        putShapeOnPreviewBoard(1,1,previewShape.get(2));
        putShapeOnPreviewBoard(4,1,previewShape.get(1));
        putShapeOnPreviewBoard(8,1,previewShape.get(0));
    }
 
    private void insert() {
        putShapeOnBoard(currentX, currentY, previewShape.get(0));
        previewShape.remove(0);
        Shape newShape = generateRandomShape();
        previewShape.add(newShape);
        currentShape = previewShape.get(0);
 
        if (blocksIsAvailable()) {
            putShadowShapeOnBoard(currentX, currentY, currentShape);
        }
        updatePreviewBoard();
    }
 
    // TODO -
    private boolean blocksIsAvailable() {
        int x, y;
        for (int i = 0; i < width * height; i++) {
            if (numbers[i] >= 0) continue;
            x = i % width;
            y = (i - x) / 10;
            if (tryMove(x, y, currentShape)) {
                currentX = x;
                currentY = y;
                return true;
            }
        }
        isFull = true;
        return false;
    }
    /*
    TODO - Total count of vertical and horizontal columns
     */
    private boolean sumIsEvenForRow(int row) {
        int sumHorizontal = 0;
        int count = 0;
        for (int j = 0; j < width; j++) {
            if (board[(row * width) + j] != Tetrominoe.NoShape && numbers[(row * width) + j] >= 0) {
                sumHorizontal += numbers[(row * width) + j];
                count++;
            }
        }
        if ((sumHorizontal % 2 == 0) && (count == width)) {
           return true;
        }
        return false;
    }
    
    
    private boolean sumIsEvenForColumn(int column) {
        int sumVertical = 0;
        int count = 0;
        for (int j = 0; j < width; j++) {
            if (board[(j * height) + column] != Tetrominoe.NoShape && numbers[(j * height) + column] >= 0) {
                sumVertical += numbers[(j * height) + column];
                count++;
                
            }
        }
        if (sumVertical % 2 == 0 && count == height) {
            return true;
        }
        return false;
    }
 
    private void clearRow(int index) {
        addScoreSingle();
        for (int i = 0; i < width; i++) {
            board[(index * width) + i] = Tetrominoe.NoShape;
            numbers[(index * width) + i] = -1;
            
        }
    }
 
    private void clearColumn(int index) {
        addScoreSingle();
        for (int i = 0; i < height; i++) {
            board[(i * height) + index] = Tetrominoe.NoShape;
            numbers[(i * height) + index] = -1;
        }
    }
 
    private void putShapeOnBoard(int a, int b, Shape shape) {
        for (int i = 0; i < 4; i++) {
            int x = a + shape.x(i);
            int y = b + shape.y(i);
            board[(y * width) + x] = shape.getShape();
            numbers[(y * width) + x] = shape.getNumberAt(i);
        }
    }
 
    private void putShadowShapeOnBoard(int a, int b, Shape newShape) {
        for (int i = 0; i < 4; i++) {
            int x = a + newShape.x(i);
            int y = b + newShape.y(i);
            board[(y * width) + x] = newShape.getShape();
            numbers[(y * width) + x] = -2; //-2 as a value for shadow
        }
    }
 
    private void removeShadowShapeOnBoard(int a, int b, Shape oldShape) {
        for (int i = 0; i < 4; i++) {
            int x = a + oldShape.x(i);
            int y = b + oldShape.y(i);
            board[(y * width) + x] = Tetrominoe.NoShape;
            numbers[(y * width) + x] = -1; // -1 to indicate its nothing
        }
    }
 
    /*
    TODO - Tembus
     */
    private boolean tryMove(int newX, int newY, Shape shape) {
        for (int i = 0; i < 4; i++) {
            int x = newX + shape.x(i);
            int y = newY + shape.y(i);
            if (x < 0 || x >= width || y < 0 || y >= height) {
                return false;
            }
 
            if (shapeAt(x, y) != Tetrominoe.NoShape && numberAt(x, y) >= 0) {
                return false;
            }
        }
        currentShape = shape;
//        currentX = newX;
//        currentY = newY;
        return true;
    }
 
    private void move(int newX, int newY, Shape shape) {
        /*
        Replace board and numbers from old to new
         */
        Tetrominoe tetrominoe = shape.getShape();
        for (int i = 0; i < 4; i++) {
            int oldX = currentX + shape.x(i);
            int oldY = currentY + shape.y(i);
            board[(oldY * width) + oldX] = Tetrominoe.NoShape;
            numbers[(oldY * width) + oldX] = -2; //can only move shadow
        }
        for (int i = 0; i < 4; i++) {
            int x = newX + shape.x(i);
            int y = newY + shape.y(i);
            board[(y * width) + x] = tetrominoe;
            numbers[(y * width) + x] = -2; //can only move shadow
        }
    }
 
    private Tetrominoe shapeAt(int x, int y) {
        return board[(y * width) + x];
    }
 
    private int numberAt(int x, int y) {
        return numbers[(y * width) + x];
    }
 
    private Shape generateRandomShape() {
        Shape shape = new Shape();
        shape.setRandomShape();
        return shape;
    }
 
    private void printBoard() {
       
        for(int a = 0; a < width + 2; a++) System.out.printf("/  ");
        System.out.println();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j == 0) System.out.printf("/ "); //border stuff
                if (board[(i * width) + j] != Tetrominoe.NoShape && numbers[(i * width) + j] >= 0) {
                    System.out.printf(" %d ", numbers[(i * width) + j]); //The number
                } else if (board[(i * width) + j] != Tetrominoe.NoShape && numbers[(i * width) + j] == -2) {
                    System.out.printf(" + "); // Shadow
                } else {
                    System.out.printf("   ");
                }
                if (j == width - 1) System.out.printf(" /"); //border stuff
            }
            System.out.println();
        }
        for(int a = 0; a < width + 2; a++) System.out.printf("/  ");
        System.out.println();
        
      
    }
 
    private void putShapeOnPreviewBoard(int a, int b, Shape newShape) {
        for (int i = 0; i < 4; i++) {
            int x = a + newShape.x(i);
            int y = b + newShape.y(i);
            previewBoard[(y * previewWidth) + x] = newShape.getShape();
            previewNumbers[(y * previewWidth) + x] = newShape.getNumberAt(i);
        }
    }
 
    private void printBlockPreviews() {
        for (int i = 0; i < previewHeight; i++) { // for the height of 4
            for (int j = 0; j < previewWidth; j++) { // for the width of 10
                if ((previewBoard[(i * previewWidth) + j] != Tetrominoe.NoShape) && (previewNumbers[(i * previewWidth) + j] != -1)) {
                    System.out.printf(" %d ", previewNumbers[(i * previewWidth) + j]);
                } else {
                    System.out.printf("   ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


private static int score;
	private String name;
	
	//The constructor
	public CLI(int s,String n)
	{
		score=s;
		setName(n);
	}
	
	//The setters and getters
	public void setScore(int score)
	{
		this.score=score;
	}
	
	public int getScore()
	{
		return score;
	}
        
        
        public void displayScore(){
            System.out.println("Current Score is = " + this.score);
        }
        
        public void addScoreSingle(){
            this.score+=10;
        }
        
         public void addScoreDouble(){
            this.score+=20;
        }
	

	public void setName(String n) 
	{
		this.name = n;
	}

	public String getName() 
	{
		return name;
	}
	//Static methods
	//Decides whether this HighScore is greater than, less than, or equal to the argument
	public int compareTo(HighScore h)
	{
		return new Integer(this.score).compareTo(h.score);
	}
	
	//This is called when there is an empty file in order prevent exceptions
	private static void initializeFile()
	{
		HighScore[] h={new HighScore(0," "),new HighScore(0," "),new HighScore(0," "),
				new HighScore(0," "),new HighScore(0," "),new HighScore(0," "),
				new HighScore(0," "),new HighScore(0," "),new HighScore(0," "),
				new HighScore(0," ")};
		try 
		{
			System.out.println("Player score : ");
			ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream("HighScores.dat"));
			o.writeObject(h);
			o.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
	}
	
	//Reads the .dat file and returns the constants
	public static HighScore[] getHighScores()
	{
		if (!new File("HighScores.dat").exists())
			initializeFile();
		try 
		{
			ObjectInputStream o=new ObjectInputStream(new FileInputStream("HighScores.dat"));
			HighScore[] h=(HighScore[]) o.readObject();
			return h;
		} catch (IOException e) {e.printStackTrace();} 
		catch (ClassNotFoundException e) {e.printStackTrace();}
		return null;
	}
	
	//Adds a new HighScore to the .dat file and maintains the proper order
	public static void addHighScore(HighScore h)
	{
		HighScore[] highScores=getHighScores();
		highScores[highScores.length-1]=h;
		for (int i=highScores.length-2; i>=0; i--)
		{
			if (highScores[i+1].compareTo(highScores[i])>0)
			{
				HighScore temp=highScores[i];
				highScores[i]=highScores[i+1];
				highScores[i+1]=temp;
			}
		}
		try 
		{
			ObjectOutputStream o=new ObjectOutputStream(new FileOutputStream("HighScores.dat"));
			o.writeObject(highScores);
			o.close();
		} catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	
}