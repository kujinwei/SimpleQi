/*
 * Copyright (C) 2013 Andre Gregori and Mark Garro 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.icool.go;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;



/**
 * 
 * Model of a Go game.  Validates moves, stores history.
 *
 */
public class Game  {
	// Point constants.  Must be chars of positive integers.
	public static final char WHITE = '0';
	public static final char BLACK = '1';
	public static final char EMPTY = '2';
	public static final char OUT_OF_BOUNDS = '3';

	// Rules constants.  Must be positive integers.
	public static final int POSITIONAL = 0;
	public static final int SITUATIONAL = 1;
	public static final int JAPANESE = 2;
	
	// History step direction constants.  Must be int.
	public static final int PREVIOUS = 0;
	public static final int NEXT = 1;
	public static final int FIRST = 2;
	public static final int LAST = 3;

	// Properties constants (to serve as keys for Bundles).  Must be Strings.
	public static final String CAPTURES_KEY = "cap";
	
	// Operations
	public static final int ADD = 1;
	public static final int SUBTRACT = -1;

	// Instance variables
	private int koRule;
	private boolean suicideRule;
	private Board board;
	private char nextTurn;
	
	private boolean running;

	// Static methods.
	public static char invertColor(char color){
		return color == WHITE ? BLACK : color == BLACK ? WHITE : OUT_OF_BOUNDS;
	}

	// Constructors.
	
}