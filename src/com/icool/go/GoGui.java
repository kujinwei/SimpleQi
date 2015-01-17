package com.icool.go;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GoGui{

	JFrame mainframe ;
	JPanel mainPnl ;
	
	String[] buttons = { "Go First", "Before", "Next", "Go Last", 
			"Next 5" , "Before 5" , "Reset" ,"Save" , "All Blocks",
			"Air" , "Clear Console"
			};
	JButton[] btns = new JButton[buttons.length];
	Board board;
	ActionPanel actionPnl ;
	
	
	MainListener listener ;
	protected static GoGui instance ;

	public GoGui() {

	}
	
	public static GoGui getGUI() {
		if (instance == null) {
			instance = new GoGui() ;
		}
		return instance ;
	}
	
	

	public void centre(Window w) {
		// After packing a Frame or Dialog, centre it on the screen.
		Dimension us = w.getSize(), them = Toolkit.getDefaultToolkit()
				.getScreenSize();
		int newX = (them.width - us.width) / 2;
		int newY = (them.height - us.height) / 2;
		w.setLocation(newX, newY);
	}

	private JMenuItem menuItem(String label, 
	           ActionListener listener, String command, 
	           int mnemonic, int acceleratorKey) {
	    JMenuItem item = new JMenuItem(label);
	    item.addActionListener(listener);
	    item.setActionCommand(command);
	    if (mnemonic != 0) item.setMnemonic((char) mnemonic);
	    if (acceleratorKey != 0) 
	      item.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey, 
	             java.awt.Event.CTRL_MASK));
	    return item;
	  }

	public void initMenu() {
		JMenu file = new JMenu("File");
	    file.setMnemonic('F');
	    
	    file.add(menuItem("Open", listener, "open", 'O', KeyEvent.VK_O));
	    file.add(menuItem("Save", listener, "save", 'S', KeyEvent.VK_S));
	    file.add(menuItem("Save As", listener, "saveas", 'A', KeyEvent.VK_A));

	    JMenuBar menubar = new JMenuBar();
	    menubar.add(file);
	  

	    // Add menubar to the main window.  Note special method to add menubars
	    mainframe.setJMenuBar(menubar); 

	}
	
	public void init() {
		mainframe = new JFrame();
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		mainPnl = new JPanel() ;
		mainPnl.setLayout(new BorderLayout());
		board = new Board(19);
		mainPnl.add(board, BorderLayout.WEST);
		
		actionPnl = new ActionPanel() ;		
		mainPnl.add(actionPnl, BorderLayout.CENTER);

		listener = new MainListener(this) ;
		
		JPanel pnl = new JPanel();
		pnl.setLayout(new GridLayout(0,6));
		for (int i = 0; i < buttons.length ; i++) { // Add children to the pane
			btns[i] = new JButton(buttons[i]);
			pnl.add(btns[i]); // Using this constraint

			btns[i].addActionListener(listener);
		}
		mainPnl.add(pnl, BorderLayout.SOUTH);
		
		mainframe.setContentPane(mainPnl);
		initMenu() ;
		
		mainframe.setTitle("Go Client");
		mainframe.setSize(800, 720);
		centre(mainframe);
//		mainframe.pack();
		mainframe.setVisible(true);

		board.init();
		
		
	}
	
	public void log(String msg) {
		actionPnl.appendMessage(msg) ;
	}
	
	public static void main(String[] args) {
		GoGui gui = GoGui.getGUI();
		gui.init() ;
		

	}

	

}

class MainListener implements ActionListener {
	
	GoGui gui ;
	public MainListener(GoGui gui) {
		this.gui = gui ;
	}
	
	public void onButtonClick(JButton btn) {
		
		// System.out.println(btn.getText());
		if (btn.getText().equals("Next")) {
			gui.board.goNext();
		}

		if (btn.getText().equals("Before")) {
			gui.board.goBefore();
		}
		
		if (btn.getText().equals("Reset")) {
			gui.board.reset() ;
		}
		
		if (btn.getText().equals("All Blocks")) {
			gui.log("×Ü¿éÊý£º"+ gui.board.blocks.size() ) ;
			gui.log(""+ gui.board.printBlocks()) ;
		}
		
		
		
		if (btn.getText().equals("Clear Console")) {
			gui.actionPnl.clear();
		}
		
		
	}
	
	public void onMenuitemClick(JMenuItem menuitem) {
		System.out.println(menuitem.getText());
		if (menuitem.getText().equals("Open")) {
			
			
			JFileChooser fileChooser = new JFileChooser(".");
		    fileChooser.setControlButtonsAreShown(false);
		    int r = fileChooser.showOpenDialog(new JFrame());
		    if (r == JFileChooser.APPROVE_OPTION) {
//		      String name = fileChooser.getSelectedFile().getName();
		      gui.board.reset() ;
		      File f = fileChooser.getSelectedFile() ;
		      SgfData sgf = gui.board.loadSGF(f.getAbsolutePath()) ;
//		      System.out.println(f.getAbsolutePath());
		      gui.mainframe.setTitle(sgf.header.get("PB") + sgf.header.get("PW") + sgf.header.get("RE")) ;
		      
		    }

		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if (obj instanceof JButton) {
			JButton btn = (JButton) obj ;
			onButtonClick(btn) ;
		}
		
		if (obj instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) obj ;
			onMenuitemClick(item) ;
		}
		
	}
}
