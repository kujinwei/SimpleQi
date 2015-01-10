package com.icool.go;

import java.util.ArrayList;
import java.util.List;



public class Block {
	public List<Coordinate> strings = new ArrayList<Coordinate>();
	public int airCount=0;
	public char bw;
	
	public Block(char bw){
		this.bw=bw;
	}
	
	public char getBw(){
		return bw;
	}
	
	public void add(Coordinate c){
		strings.add(c);
	}
	
	public void addAir(int air){
		airCount+=air;
	}
	
	public void addBlock(Block b){
		for (Coordinate c : b.strings) {
			strings.add(c) ;
		}
	}
	
	public boolean isLive(){
		if(airCount>0 && strings.size()>0)return true;
		return false;
	}
	
//	public void each(Function f){
//		for(Coordinate c:block){
//			f.apply(c);
//		}
//	}
}
