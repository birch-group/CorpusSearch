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
	Beth Randall:  Oct. 2000
	CorpusSearch.java
	This is the main class of CorpusSearch.  
*/
package csearch;

import java.io.*;
import java.util.*;
import command.*;
import print.*;
import basicinfo.*;

/**
 */
public class CorpusSearch {
    public static File command_file, destination_file;
    public static String command_name, dest_name, comp_name = "DIDO";
    public static FileWriter destination;
    public static String new_args[], err_message;
    public static Vector source_list = new Vector();
    public static FileInputStream command;

    /**
     * directs the search.
     */
    public static void main(String args[]) throws IOException {
	Timing search_time = new Timing();
        try {
            InFace.PrintBookmark();
            if (args.length == 1) {
                WrongArgsError(); }
            if (args.length == 0) {
                Goodbye.setEmptyCommandLine(true);
                InFace.GetQueryFile();
                InFace.GetSourceFiles();
                InFace.GetOutFile();
                FilePrep.FindFiles(new_args, command_name); }
            if (args.length > 0) {
                FilePrep.UnloadCommandLine(args);
                FilePrep.FindFiles(args, command_name);
	        
		//if (args[args.length - 1] == "&") {
		//  System.err.println("found &"); }
	    }
	    FilePrep.GetPrefs();
            Meat.CrankThrough();
        } 
        catch (Exception e) {
            System.err.println("in CorpusSearch:  ");
            System.err.println(e.getMessage()); 
	    e.printStackTrace(); }
        finally {
            if (destination != null)
            try {destination.close();} catch(IOException e){}
            search_time.StopClock();
            Closure(); }
    } 

    /**
     * prints messages at close of program.
     */
    public static void Closure () {
        System.err.println("");
        System.err.println("Search completed.  ");
        return; }

    /**
     * prints error message if not enough information was entered on command line.
     */
    public static void WrongArgsError() {

        System.err.print("ERROR!  Not enough information ");
        System.err.println("to run CorpusSearch.");
        System.err.println("The command line should follow this pattern:  ");
        System.err.print("    java -classpath . csearch/CorpusSearch ");
        System.err.println("<command_file> <source_file(s)>");
        System.err.println("Search aborted.");
        System.exit(1);
        return; }

} 



