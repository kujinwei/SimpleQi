

package com.icool.go;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;



/**
 * 
 * Model of a Go board.  Stores the state of each board point.  
 *
 */
public class Board extends JPanel{
	
	private static final int ACode = (int) 'a';
	
	public final static int DEFAULT_SIZE = 19 ;
	
	public int nSize;
	
	int xOffset , yOffset ;
	int boardWidth , cellWidth;
	
	
	Image bgImg , blackImg , whiteImg ;
	
	char currTurn = Game.BLACK; 
	Image currImg ;
	
	public List<Block> blocks = new ArrayList<Block>() ; //current blocks on board
	public List<Stone> stones = new ArrayList<Stone>() ;
	
	List<Coordinate> sgfCoordinates ;
	int currStep = 0 ;
	boolean canToggle = true ;
	Coordinate lastFocus = null ;

	/**
	 * Constructs a standard <code>Board</code> of size 19x19. 
	 */
	public Board(int boardSize){
		this.nSize = boardSize; 
		
		Border bevelBorder = new BevelBorder(BevelBorder.RAISED, Color.red,
		        Color.red.darker(), Color.pink, Color.pink.brighter());
		setBorder(bevelBorder) ;
		setPreferredSize(new Dimension(560 , 560)) ;
		
		loadImages();
	}
	
	public void resizeImages() {
		float resizeTimes ;
		BufferedImage img ;
		
		try {
			img = ImageIO.read(ClassLoader.getSystemResource("com/icool/go/black.png"));
			resizeTimes = img.getWidth(null)/cellWidth ;
			blackImg = ImageUtil.resizeImage(img, resizeTimes) ;
			
			img = ImageIO.read(ClassLoader.getSystemResource("com/icool/go/white.png"));
			resizeTimes = img.getWidth(null)/cellWidth ;
			whiteImg = ImageUtil.resizeImage(img, resizeTimes) ;
			
		} catch (Exception e) {
			e.printStackTrace() ;
		}
		
		
	}
	
	public void toogle() {
		if (currTurn == Game.BLACK) {
			currImg = blackImg ;
			currTurn = Game.WHITE ;
		} else {
			currImg = whiteImg ;
			currTurn = Game.BLACK ;
		}
	}
	
	public void init() {
		
		boardWidth = 560 - 80  ;
		cellWidth = (int)(boardWidth/(nSize-1)) ;
		xOffset = 20 ;
		yOffset = 20 ;
		
//		resizeImages() ;
		
//		loadSGF() ;
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				float xf = e.getX();
				float yf = e.getY();

				int x = x2Coordinate(xf);
				int y = y2Coordinate(yf);
				
//				System.out.println("x= " + x + ", y=" + y);
				
				
				Coordinate c = new Coordinate(x , y) ;				
				char color = currTurn ;
				Stone s = new Stone(c , color) ;
				putStone(s) ;
//				printBlocks() ;
				update(getGraphics()) ;
				
				
				if(canToggle) {
					toogle() ;
				}
				
//				getGraphics().drawImage(currImg, x*cellWidth-cellWidth/2 + xOffset , y*cellWidth-cellWidth/2 + yOffset, cellWidth , cellWidth , null) ;
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
//				System.out.println(e.getX());
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}) ;
		
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {

			}

			public void mouseMoved(MouseEvent e) {
				
				float xf = e.getX();
				float yf = e.getY();

				int x = x2Coordinate(xf);
				int y = y2Coordinate(yf);
				Coordinate c = new Coordinate(x , y) ;	
//				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				drawFocus(c) ;
				lastFocus = c ;

			}

		});
		
	}
	
	/**
	 * 
	 * @param g
	 * @param stone
	 */
	public void drawStone(Graphics g , Stone stone) {
		
		Coordinate c = stone.c ;
		char color = stone.color ;
		Image currImg = null ;
		if (color == Game.BLACK) {
			currImg = blackImg ;			
		} else if (color == Game.WHITE ){
			currImg = whiteImg ;			
		}
		
		g.drawImage(currImg, c.x*cellWidth-cellWidth/2 + xOffset , c.y*cellWidth-cellWidth/2 + yOffset, cellWidth , cellWidth , null) ;
		
	}
	
	private void loadImages() {

		try {
			bgImg = ImageIO.read(ClassLoader.getSystemResource("com/icool/go/board.jpg"));
			blackImg = ImageIO.read(ClassLoader.getSystemResource("com/icool/go/black.png"));
			whiteImg = ImageIO.read(ClassLoader.getSystemResource("com/icool/go/white.png"));
			
			MediaTracker tracker = new MediaTracker(this);
			tracker.addImage(bgImg, 0);
			tracker.waitForID(0);
		} catch (Exception e) {
			e.printStackTrace() ;
		}

	}
	
	public void reset() {

		currStep = 0 ;
		currTurn = Game.BLACK ;
		
		blocks.clear() ;
		stones.clear() ;
		if (sgfCoordinates != null) {
			sgfCoordinates.clear() ;
			sgfCoordinates = null ;
		}
		
		update() ;
	}
	
	public SgfData loadSGF(String file) {
		SgfData sgf = null ;
		try {
			FileInputStream fins = new FileInputStream(file) ;
			if (fins == null) {
				System.out.println("ins is NULL");
			}
			sgf = SgfReader.loadSGF(fins) ;
			sgfCoordinates = sgf.coordinates ;
			
		} catch (Exception e) {
			e.printStackTrace() ;
		}
		return sgf ;
	}
	
	public Block getBlockByCoodinate(Coordinate c) {
		Block b = null ;
		
		for (Block block  : blocks) {
			List<Coordinate> strings = block.strings ;
			for (Coordinate coordinate : strings) {
				if (coordinate.equals(c)) {
					return block ;
				}
			}
		}
		
		return b ;
	}
	
	/**
	 * 
	 */
	public void goNext() {
		if (sgfCoordinates == null) {
			return ;
		} else {
			Coordinate c = sgfCoordinates.get(currStep) ;
			Stone stone = new Stone(c , currTurn) ;
			putStone(stone) ;
			update() ;
			currStep ++ ;
			
			toogle() ;
		}
		
		
		
	}
	
	public void goBefore() {
		
		Stone currStone = stones.get(stones.size() - 1) ;
//		System.out.println("currStone.newBlock.strings.size = " + currStone.newBlock.strings.size());
		blocks.remove(currStone.newBlock) ;
		
		for (Block block : currStone.killedBlocks) {			
			blocks.add(block) ;
		}
		
		for (Block block : currStone.deletedBlocks) {			
			blocks.add(block) ;
		}
		
		stones.remove(currStone) ;
		calcAllBlocksAir() ;
		update() ;
		currStep -- ;
		
		toogle() ;
		
		
	}
	
	public void update() {
		this.update(getGraphics()) ;
	}
	
	/**
	 * put a stone on the board
	 * @param stone
	 */
	public void putStone(Stone stone) {
		
		boolean bKilled = false ;
		//detect all blocks near this stone
		HashMap<Block , Block> nearBlocks = new HashMap<Block , Block>() ;
//		int myair = 4 ;
		for (Coordinate c : stone.c.near) {
			Block b = getBlockByCoodinate(c) ;
			if (b != null && !nearBlocks.containsKey(b)) {
				nearBlocks.put(b , b) ;
//				myair -- ;
			}
			
		}
		
		for (Block block : nearBlocks.values()) {
			if (block.bw == stone.color) {
				stone.deletedBlocks.add(block) ;
				
			}else{
				if (block.airCount == 1) {//kill this block
					stone.killedBlocks.add(block) ;					
					bKilled = true ;
				}else{
					block.airCount -- ;
				}
			}
		}
		
		//each stone, a new block is created on the board
		Block newBlock = new Block(stone.color) ;
		for (Block block : stone.deletedBlocks) {
			newBlock.addBlock(block) ;
//			newBlock.airCount += block.airCount - 1 ;
//			
		}
//		
		newBlock.add(stone.c) ;
		blocks.add(newBlock) ;
		int newAir = calcBlockAir(newBlock);

		
		if(newAir == 0 && !bKilled) {			
			canToggle = false ;
			blocks.remove(newBlock) ;
			System.out.println("Not allowed!");
			
			
		} else {
			canToggle = true ;
			for (Block block : stone.deletedBlocks) {				
				blocks.remove(block) ;
			}
			for (Block block : stone.killedBlocks) {				
				blocks.remove(block) ;
			}
			
			
			stone.newBlock = newBlock ;
			//calculate the air for all blocks
//			if (bKilled) {
//				calcAllBlocksAir() ;
//			}
			
			
			stones.add(stone) ;
		}
		calcAllBlocksAir() ;
//		GoGui.getGUI().log("All blocks : " + blocks.size()) ;
		
		
		
		
//		Util.printHeapUsage() ;
		
		
	}
	
	/**
	 * 计算所有块的气数
	 */
	public void calcAllBlocksAir(){
		for (Block block  : blocks) {
			calcBlockAir(block) ;
			
		}		
	}

	public int calcBlockAir(Block b) {
		int air = 0 ;
		
		HashMap map = new HashMap() ;
		
		for (Coordinate c : b.strings) {
			Coordinate[] near = c.near ;
			for (Coordinate coordinate : near) {
				if (coordinate == null) {
					continue ;
				}
				if (getValue(coordinate.x, coordinate.y) == Game.EMPTY) {
					if (!map.containsKey(coordinate)) {
						map.put(coordinate, coordinate) ;
					}
				}
			}
			
		}
		air = map.keySet().size() ;
		b.airCount = air ;
		
		return air ;
	}
	
	public String printBlocks() {
		StringBuffer buf = new StringBuffer() ;
		buf.append("======all blocks=======\n") ;
		int i = 0 ;
		for (Block block  : blocks) {
			i++ ;
			buf.append(i).append(" " + block.bw).append(": 子数=").append(block.strings.size()).append(" 气数=").append(block.airCount).append("\n");
//			List<Coordinate> strings = block.strings ;
			
		}
		return buf.toString() ;
		
	}
	/**
	 * Return the number of vertical or horizontal lines in the board.
	 * @return	 the number of vertical or horizontal lines in the board.
	 */
	public int getBoardSize(){
		return this.nSize;
	}

	public char getValue(int x , int y) {
		for (Block block  : blocks) {
			List<Coordinate> strings = block.strings ;
			for (Coordinate coordinate : strings) {
				if (coordinate.x == x && coordinate.y == y) {
					return block.bw ;
				}
			}
		}
		
		return Game.EMPTY ;
	}
	

	public void paint(Graphics g) {
//		super.paint(g) ;
		
//		setBackground(Color.WHITE) ;
		
//		System.out.println("draw the board: " + "boardWidth=" + boardWidth + " cellWidth=" + cellWidth + " height=" + getHeight());
		
		g.drawImage(bgImg , 0 , 0, getWidth(), getHeight() , null) ;
		drawLineGrid(g);
		drawStar(g);
		
		drawBlocks(g);
		drawFlag(g);
	}

	public void update(Graphics g){
	    paint(g);
	}
	
	
	private void drawBlocks(Graphics g) {
//		System.out.println("draw blocks");
		Stone stone ;
		for (Block block  : blocks) {
			List<Coordinate> strings = block.strings ;
			for (Coordinate coordinate : strings) {
				stone = new Stone(coordinate , block.bw) ;
				drawStone(g, stone) ;
			}
		}
	}
	
	
//	private void drawStones(Graphics g) {
//		for (int x = 0; x < nSize; x += 1) {
//			for (int y = 0; y < nSize; y += 1) {				
//				
//				char bw = getValue(x, y);
//				System.out.println("bw="+bw);
//				if (bw == Game.BLACK) {
//					
//				}
//				if (bw == Game.WHITE) {
//					
//				}
////				if (bw != Board.None) {
////					if (bw == Board.Black)
////						g.setColor(Color.BLACK);
////					else
////						g.setColor(Color.WHITE);
//
////					Graphics.drawCircle(x2Screen(x), y2Screen(y),
////							(float) (tileSize / 2d), paint);	
//				}
//			}
//		}
//	}

	
	private void drawFlag(Graphics g) {
		g.setColor(Color.RED);
//		Coordinate c=board.getLastPosition();
//		if(c!=null){
//			g.drawRect(x2Screen(c.x)-3, y2Screen(c.y)-3
//				, x2Screen(c.x)+3f, y2Screen(c.y)+3f, paint);
//		}
	}
	
	private void drawFocus(Coordinate c) {
		
		Graphics g = getGraphics() ;
		
		if(lastFocus != null) {
			g.setXORMode( Color.WHITE );
			g.setColor(Color.BLUE);
//			Coordinate c=board.getLastPosition();
			if(c!=null){
				g.fillRect(x2Screen(lastFocus.x)-3, y2Screen(lastFocus.y)-3 , 6, 6);
			}
			
		}
		
		g.setXORMode( Color.WHITE );
		g.setColor(Color.BLUE);
//		Coordinate c=board.getLastPosition();
		if(c!=null){
			g.fillRect(x2Screen(c.x)-3, y2Screen(c.y)-3 , 6, 6);
		}
		
	}
	
	public Coordinate[] createStar(){
		Coordinate[] cs=new Coordinate[9];
		
		int dao3=nSize-4;
		cs[0]=new Coordinate(3,3);
		cs[1]=new Coordinate(dao3,3);
		cs[2]=new Coordinate(3,dao3);
		cs[3]=new Coordinate(dao3,dao3);
		
		int zhong=nSize/2;
		
		cs[4]=new Coordinate(3,zhong);
		cs[5]=new Coordinate(zhong,3);
		cs[6]=new Coordinate(zhong,dao3);
		cs[7]=new Coordinate(dao3,zhong);
		
		cs[8]=new Coordinate(zhong,zhong);
		
		return cs;
	}
	
	private void drawStar(Graphics g) {
		g.setColor(Color.BLACK);
//		
		for(Coordinate c: createStar()){
			if(c!=null){
				
//				g.drawOval(x2Screen(c.x)-2, y2Screen(c.y)-2, 4 , 4);
				g.fillOval(x2Screen(c.x)-3, y2Screen(c.y)-3, 6 , 6);
			}
		}
	}

	
	private void drawLineGrid(Graphics g) {
		g.setColor(Color.BLUE);
		for (int i = 0; i < nSize; i++) {
			drawVLine(g, i);
			drawHLine(g, i);
		}
	}

	
	private void drawVLine(Graphics g, int i) {
		g.drawLine(x2Screen(i), y2Screen(0), x2Screen(i),
				y2Screen(nSize - 1));
		
		char ch = (char)(ACode + i) ;
		g.drawString(String.valueOf(ch), x2Screen(i), y2Screen(nSize - 1) + 10) ;
	}

	
	private void drawHLine(Graphics g, int i) {
		g.drawLine(x2Screen(0), y2Screen(i), x2Screen(nSize - 1),
				y2Screen(i));
		char ch = (char)(ACode + i) ;
		g.drawString(String.valueOf(ch), x2Screen(nSize - 1) + 10,  y2Screen(i)) ;
	}

	
	private int x2Screen(int x) {
//		System.out.println("boardWidth = " + boardWidth);
		return (int) (x * cellWidth + xOffset);
	}

	private int y2Screen(int y) {
		return (int) (y * cellWidth + yOffset);
	}

	private int x2Coordinate(float x) {
		return (int) Math.round((x - xOffset) / cellWidth);
	}

	private int y2Coordinate(float y) {
		return (int) Math.round((y - yOffset) / cellWidth);
	}

	//------------------------------------------------------------------�¼�
	
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		int wh = Math.min(w, h);
//		tileSize = wh / (double) Board.n;
//
//		xOffset = tileSize / 2;
//		yOffset = tileSize / 2;
//
//		super.onSizeChanged(w, wh, oldw, oldh);
	}

}

class BoardSizeException extends RuntimeException{
	BoardSizeException(){ super(); }
}

class IllegalPointException extends RuntimeException{
	IllegalPointException(){ super(); }
}