package com.icool.go;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ActionPanel extends JPanel{
	
	JTextArea console = new JTextArea();
	StringBuffer buffer = new StringBuffer() ;
	
	public ActionPanel() {
		
//		setPreferredSize(new Dimension(200 , 680)) ;
		
		setLayout(new BorderLayout());
		console.setPreferredSize(new Dimension(0 , 400)) ;
		add(console , BorderLayout.NORTH) ;
		
		console.setText("ª∂”≠ π”√GoGUI\n") ;
		
	}
	
	public void setMessage(String msg) {
		
		console.setText(msg) ;
		
	}
	
	public void appendMessage(String msg) {
		buffer.append(msg + "\n") ;
		
		console.append(msg + "\n") ;
	}
	
	
	
	

}
