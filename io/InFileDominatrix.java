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

package io;

import java.io.*;
import java.util.*;

public class InFileDominatrix {
    public boolean EOF, EOL;
    public int LINE_NUMBER, PREV_CHAR;
    public boolean OPEN_PAREN, CLOSE_PAREN;
    public boolean PREV_OPEN_PAREN, OUT_INDEX;
    public String START_COMMENT = "/*", END_COMMENT = "*/";
    public String LINE_COMMENT = "//";
    public String FILE_NAME = "unknown_file";
    public String FORMAT = "PTB", ENCODING = "UTF-8";
    private BufferedReader buffoon;
    private int currlen = 0, maxlen = 256, c;
    private int chunklen = 0, maxchunklen = 2256;
    private char wordbuf[] = new char[maxlen];
    private char chunkbuf[] = new char[maxchunklen];
    private String next;
    
    public InFileDominatrix(String in_file_name) {
	
        try {
	    String in_code = IoInfo.getCorpusEncoding();
	    Init(in_file_name);
            FileInputStream input_fis = new FileInputStream(in_file_name);
            InputStreamReader input_sr = new InputStreamReader(input_fis, 
							       in_code);
	    //System.out.println("corpus encoding:  " +input_sr.getEncoding());
            buffoon = new BufferedReader(input_sr); 
	    this.firstLineInfo(); }
	catch (Exception e) {
	    System.out.println("in InFileDominatrix.java:  ");
	    System.out.println(e.toString());
	    e.printStackTrace();
	    System.exit(0); }
	finally { return; } }
    

    public InFileDominatrix(String in_file_name, String in_code) {
        try {
	    Init(in_file_name);
            FileInputStream input_fis = new FileInputStream(in_file_name);
            InputStreamReader input_sr = new InputStreamReader(input_fis, 
							       in_code);
	    //System.out.println("corpus encoding:  "+input_sr.getEncoding());
            buffoon = new BufferedReader(input_sr); 
	    this.firstLineInfo(); }
	catch (Exception e) {
	    System.out.println("in InFileDominatrix.java:  ");
	    System.out.println(e.toString());
	    e.printStackTrace();
	    System.exit(0); }
	finally { return; } }

    private void Init(String in_file_name) {
	FILE_NAME = in_file_name;
	EOF = false;
	EOL = false;
	LINE_NUMBER = 1;
        OPEN_PAREN = false;
        CLOSE_PAREN = false;
	PREV_OPEN_PAREN = false;
	OUT_INDEX = false;
        PREV_CHAR = 'Q'; 
    }


    private void firstLineInfo() {
	char first, second;
	String rest_of_line, part_1 = "XXX", part_2 = "QQQ";
	int amp_dex;

	try {
	    buffoon.mark(200);
	    first = (char)this.NextChar();
	    if (first != '#') { buffoon.reset(); return; }
	    second = (char)this.NextChar();
	    if (second != '!') { buffoon.reset(); return; }
	    rest_of_line = this.RestOfLine();
	    part_1 = rest_of_line;
	    amp_dex = rest_of_line.indexOf('&');
	    if (amp_dex > -1) {
		part_1 = rest_of_line.substring(0, amp_dex);
		part_2 = rest_of_line.substring(amp_dex + 1); }
	    lineByParts(part_1);
	    lineByParts(part_2);

	}
	catch (Exception e) { e.printStackTrace(); return; }
	finally { return; } }

    private void lineByParts(String a_part) {
	int equals_dex;
	String before_eq, after_eq;

	equals_dex = a_part.indexOf('=');
	if (equals_dex < 0) { return; }
	before_eq = a_part.substring(0, equals_dex);
	after_eq = a_part.substring(equals_dex + 1);
	if (before_eq.equals("FORMAT")) {
	    this.FORMAT = after_eq;
	    return; }}

    public String getFormat() {
	return (this.FORMAT); }
	    

    public String getChunk() {
	return (new String(chunkbuf, 0, chunklen)); }

    public void newChunk() {
	chunklen = 0; }

    public void ChunkToSystemErr() {
	System.out.println(new String(chunkbuf, 0, chunklen)); }

    public void Close() {
	try { buffoon.close(); }
	catch (Exception e) {
	    System.out.println(e.getMessage());
	    System.exit(0); }
	finally { return; } }
 
    public int NextChar () {
	c = 0;
	try {
	    c = buffoon.read();
	    if (c == -1) {
		EOF = true; }
	    endOfLine(c);
	} 
	catch (Exception e) {
	    System.out.println("in InFileDominatrix.java:  NextChar:  ");
	    System.out.println(e.toString());
	    e.printStackTrace(); }
	finally { return c; } 
    } 

    /*
     */
    public String RestOfLine () {
	String rest = "", next = "";
	c = 0; currlen = 0;
	try {
	    while (!this.EOL && !this.EOF) {
		next = this.NextString();
		if (IsComment(next)) { 
		    next = ""; 
		    continue; }
		rest += next + " "; }
	    rest = rest.trim(); }
	catch (Exception e) {
	    System.out.println("in InFileDominatrix.java:  ");
	    System.out.println(e.getMessage());
	    e.printStackTrace(); }
	finally { return rest; }
    } 

    public boolean isStartXMLTag(String is_it) {
	if (is_it.length() < 2) { return false; }
	if (is_it.charAt(0) == '<' && 
	    is_it.charAt(1) != '\\') {
	    return true; }
	return false; }

   public boolean isEndXMLTag(String is_it) {
       if (is_it.length() < 2) { return false; }
	if (is_it.charAt(0) == '<' && 
	    is_it.charAt(1) == '\\') {
	    return true; }
	return false; }

    public boolean isXMLTagStart(String is_it) {
	if (is_it.length() < 1) { return false; }
	if (is_it.charAt(0) == '<') {
	    return true; }
	return false; }

    public boolean isXMLTagEnd(String is_it) {
	if (is_it.endsWith(">")) {
	    return true; } 
	return false; }

    public String GetXMLTag(String in_label) {
	String part_label, return_label = in_label;
	c = 0; currlen = 0; 

	if (this.isStartXMLTag(in_label)) {
	    return_label = in_label.substring(1); 
	    if (this.isXMLTagEnd(in_label)) { // e.g., "<text>"
		return_label = in_label.substring(1, in_label.length() - 1);
		return return_label; }}
	    if (this.isEndXMLTag(in_label)) {
		return_label = in_label.substring(2); }
	    if (this.EOF) { return return_label; }
	    try {
		char_by_char: do {
		    EOL = false;
		    c = buffoon.read();
		    endOfLine(c);
		    if (c != '>') { 
			adjustWordbuf();
			wordbuf[currlen++] = (char)c; } 
		    else {
			if (currlen > 0) {
			    part_label = new String(wordbuf, 0, currlen);
			    return_label += part_label;
			    return return_label; } }
		PREV_CHAR = c;
	    } while (c != -1);
	    EOF = true;
	    this.Close(); }
	catch (Exception e) {
	    System.out.println("in InFileDominatrix.java:  ");
	    System.out.println(e.toString());
	    e.printStackTrace(); }
	finally { return return_label; }
    } 

    public String ReadBetweenXMLTags(String in_label) {
	String end_label, next, between_stuff = "";

	end_label = "</" + in_label.substring(1);
	if (end_label.endsWith(">")) {
	    end_label = end_label.substring(0, end_label.indexOf(">")); }
	try{
	    word_by_word: do {
		next = this.NextString();
		if (next.startsWith(end_label)) {
		    if (next.endsWith(">")) {
			return between_stuff; }
		    else {
			while (!next.endsWith(">")) {
			    next = this.NextString(); }
			return between_stuff; }}
		    between_stuff += next;
	    } while (!this.EOF);
	    EOF = true;
	    this.Close(); }
	catch (Exception e) {
	    System.out.println("in InFileDominatrix.java:  ");
	    System.out.println(e.toString());
	    e.printStackTrace(); }
	finally { return between_stuff; }
    } 


    public String NextString() {
	String label = "";
	c = 0; currlen = 0;

	if (this.EOF) { return label; }
	try {
	    char_by_char: do {
		EOL = false;
		c = buffoon.read();
		if (c > 32) { // i.e., c != ' '
		    adjustWordbuf();
		    wordbuf[currlen++] = (char)c; }
		else {
		    endOfLine(c);
		    if (currlen > 0) {
			label = new String(wordbuf, 0, currlen);
			if (IsComment(label)) {
			    c = 0; currlen = 0;
			    label = "";
			    continue char_by_char; }
			return label;
		    } // end if currlen > 0
		} // end else (c is ' ')
		PREV_CHAR = c;
	    } while (c != -1);
	    EOF = true;
	    this.Close(); }
	catch (Exception e) {
	    System.out.println("in InFileDominatrix.java:  ");
	    System.out.println(e.toString());
	    e.printStackTrace(); }
	finally { return label; }
    } 

    /**
     * Returns next string (surrounded by spaces), or next open 
     * or close paren, whichever comes first.   Used for reading treebank-style 
     * files.
     */
    public String NextStringParen () {
	String label = "";
	c = 0; currlen = 0;

	if (this.EOF) { return label; }
	try {
	    OUT_INDEX = false;
	    char_by_char: do {
		EOL = false;
		OPEN_PAREN = false;
		CLOSE_PAREN = false;
		c = buffoon.read();
		adjustChunkbuf();
		chunkbuf[chunklen++] = (char)c;
		if (PREV_OPEN_PAREN && c >= 48 && c <= 57) {
		    OUT_INDEX = true; }
		if (c == 40) { // open paren.
		    OPEN_PAREN = true;
		    PREV_OPEN_PAREN = true;
		    return ("("); }
		PREV_OPEN_PAREN = false;
		if (c == 41) { // close paren.
		    CLOSE_PAREN = true;
		    label = new String(wordbuf, 0, currlen);
		    return (label); }
		if (c > 32) {
		    //System.out.println("c:  " + c + "  " + (char)c);
		    adjustWordbuf();
		    wordbuf[currlen++] = (char)c;
		    PREV_CHAR = c;
		    continue char_by_char; }
		else {
		    endOfLine(c);
		    if (currlen > 0) {
			label = new String(wordbuf, 0, currlen);
			if (IsComment(label)) {
			    c = 0; currlen = 0;
			    label = "";
			    continue char_by_char; }                 
			return label; }
		} 
		PREV_CHAR = c;
	    } while (c != -1);
	    EOF = true;
	    //this.Close();
	} 
	catch (Exception e) {
	    System.out.println("in InFileDominatrix.java:  ");
	    System.out.println(e.toString());
	    e.printStackTrace(); }
	finally { return label; }
    } 

    private boolean IsComment (String label) {
	if (label.startsWith(START_COMMENT)) {
	    this.FlushComment(label, END_COMMENT);
	    return true; }
	if (label.startsWith(LINE_COMMENT)) {
	    this.FlushLine();
	    return true; }
	return false; }

    protected void FlushComment(String label, String the_end) {
	if (label.endsWith(the_end)) { return; }
	do {
	    next = this.NextString();
	} while (!next.endsWith(the_end));
	return; }

    public void FlushLine() {
	if (this.EOL) return;
	try {
	    do {
		c = buffoon.read();
		if (endOfLine(c)) { return; }
	    } while (c != -1);
	    this.EOF = true;
	    this.Close();
	}
	catch (Exception e) {
	    System.out.println("in InFileDominatrix:  FlushLine:  ");
	    e.printStackTrace(); }
	finally { return; }}

    protected void flushFile(PrintWriter outt) {
	if (this.EOF) {
	    outt.flush(); return; }
	try {
	    do {
		c = buffoon.read();
		outt.print((char)c);
	    } while (c != -1);
	    this.EOF = true;
	    outt.flush();
	    outt.println("");
	    this.Close();
	}
	catch (Exception e) {
	    System.out.println("in InFileDominatrix:  flushFile:  ");
	    e.printStackTrace(); }
	finally { return; }}

    protected boolean endOfLine (int c) {
	if (c == 10) {
	    LINE_NUMBER += 1;
	    EOL = true; 
	    return true; }
	else {return false; } }

    private void adjustWordbuf() {
	if (currlen == maxlen) {
	    maxlen *= 1.5;
	    char xbuf[] = new char[maxlen];
	    System.arraycopy(wordbuf, 0, xbuf, 0, currlen);
	    wordbuf = xbuf; }
	return; }

    private void adjustChunkbuf() {
	if (chunklen == maxchunklen) {
	    maxchunklen *= 1.5;
	    char xbuf[] = new char[maxchunklen];
	    System.arraycopy(chunkbuf, 0, xbuf, 0, chunklen);
	    chunkbuf = xbuf; }
	return; }
    
    public void PrintToSystemErr() {
	System.out.println("");
	System.out.println("file_name:  " + this.FILE_NAME);
	System.out.println("EOF:  " + this.EOF);
	System.out.println("");
    }

	      
} 







