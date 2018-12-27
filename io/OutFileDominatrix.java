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

*/

package io;

import java.io.*;
import java.util.*;
import basicinfo.*;

public class OutFileDominatrix {

    public String FILE_NAME = "BUBBLES";
    public String DIR_NAME = "BUTTERCUP";
    private FileWriter destination;
    private PrintWriter pw; 
    private FileOutputStream fos;
    private OutputStreamWriter osw;
    public boolean NO_ERROR = true;


    public OutFileDominatrix(String out_filename) {
        try {
	    FILE_NAME = out_filename;
	    fos = new FileOutputStream(out_filename);
	    osw = new OutputStreamWriter(fos, IoInfo.corpus_encoding);
	    pw = new PrintWriter(new BufferedWriter(osw)); 
	}
	catch (Exception e) {
	    //System.err.println("in OutFileDom:  FILE_NAME:  " + FILE_NAME);
	    //System.err.println(e.getMessage());
	    //	    e.printStackTrace();
	    NO_ERROR = false; }
        finally { return; }
    } 

    public OutFileDominatrix(String out_filename, String output_format) {
        try {
	    if (output_format.equals("HTML") || 
		output_format.equals("STDOUT")) {
		osw = new OutputStreamWriter(System.out, "UTF-8");
		pw = new PrintWriter(osw);	    
	    }
	    else {
		FILE_NAME = out_filename;
		fos = new FileOutputStream(out_filename);
		osw = new OutputStreamWriter(fos, IoInfo.corpus_encoding);
		pw = new PrintWriter(new BufferedWriter(osw)); }
	}
	catch (Exception e) {
	    //System.err.println("in OutFileDom:  FILE_NAME:  " + FILE_NAME);
	    //System.err.println(e.getMessage());
	    //	    e.printStackTrace();
	    NO_ERROR = false; }
        finally { return; }
    } 

    public PrintWriter getPrintWriter() {
	return pw; }

    public void report() {
	String encode;

	encode = osw.getEncoding();
	System.out.println("OutputStreamWriter encoding:  " + encode); 
	return; }

    public void close() {
	try {
	    fos.close();
	    osw.close(); 
	    pw.close(); }
	catch(Exception e) {
	    System.err.println("in OutFileDom: can't close: " + FILE_NAME); }
	finally { return; } }

} 
