//  Copyright 2010 Beth Randall

/*********************************
This file is part of CorpusSearch.

CorpusSearch is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CorpusSearch is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with CorpusSearch.  If not, see <http://www.gnu.org/licenses/>.
************************************/

/*
  Beth Randall:  Oct 2000
  VectorAux.java
*/
package io;

import java.io.*;
import java.util.*;

public class VectorAux extends TBToRix{

    public static String UR_BEGIN = "/~*";
    public static String UR_END = "*~/";
    public static Vector ur_vec = new Vector();
    public static String ID_str = "UNKNOWN_ID_STR";
    public static String file_ID = "UNKNOWN_FILE_ID";
    //    public static Integer sent_ID;
 
   /*
      HandleNonParsedStuff -- handles material found in 
      input file that is not part of a parsed
      sentence.  Calls other methods in 
      VectorAux.
      input -- String label -- last full label read
      int c -- last char read
      output -- boolean -- true if label indicated 
      non-parsed material;
      false otherwise.
    */
    public static  boolean HandleNonParsedStuff (String label) {
	CommPair comm_pair;
	String begin, end;
	int i;

	for (i = 0; i < (IoInfo.commie).size(); i++) {
	    comm_pair = (IoInfo.commie).pairAt(i);
	    begin = comm_pair.getBegin();
	    if (label.startsWith(begin)) {
		infile.FlushComment(label, comm_pair.getEnd());
		return true; } }
	for (i = 0; i <(IoInfo.linie).size(); i++) {
	    begin = (IoInfo.linie).lineCommAt(i);
	    if (label.startsWith(begin)) {
		infile.FlushLine();
		return true; } }
	if (label.startsWith(UR_BEGIN)) {
	    ur_vec.removeAllElements();
	    HandleUrText(label);
	    return true; }
	return false; }

    /*
      HandleUrText: handles ur_text found in output files.
      reads through ur_text, stores it in ur_vec.
      input -- String label -- first string in ur_text,
      found in input file.
      output -- void 
    */
    public static void HandleUrText (String label) { 
	int c;
	Character one_char;
	String a_word, one_line;
	StringBuffer ur_buff = new StringBuffer();
	StringBuffer word_buff = new StringBuffer();

	try {
	    // ensure that label contains only "/~*", with nothing trailing.
	    label = UR_BEGIN;
	    ur_vec.addElement(label);
	    char_by_char: do {
        	c = infile.NextChar();
		// ASCII 13 is CR character, used only by windows.  
		// causes windows-only bug (extra blank lines).
		if (c == 13) { continue char_by_char; }
		one_char = new Character((char)c);
		if (!one_char.isWhitespace(one_char.charValue())) {
		    word_buff.append(one_char.charValue()); 
		    continue char_by_char; }
		a_word = word_buff.toString();
		ur_buff.append(a_word + " ");
		word_buff = new StringBuffer("");
		if (a_word.endsWith(UR_END)) {
		    one_line = ur_buff.toString();
		    one_line = one_line.trim();
		    ur_vec.addElement(one_line);
		    return; }
		if (!one_char.isSpaceChar(one_char.charValue())) {
		    one_line = ur_buff.toString();
		    one_line = one_line.trim();
		    if (one_line.length() > 1) {
			ur_vec.addElement(one_line); }
		    ur_buff = new StringBuffer(); }
	    } while (c != -1);
	} // end try
	catch (Exception e) {
	    System.err.println("in MakeVector:  HandleUrText:");
	    System.err.println(e.getMessage()); }
	finally { return; }
    } 

    public static void HandleIDStuff (String ID_text) {
	int comma_dex;

	ID_str = ID_text;
	file_ID = ID_text;
	comma_dex = ID_str.indexOf(',');
	if (comma_dex != -1) {
	    file_ID = ID_str.substring(0, comma_dex); }
	return; }


} 


