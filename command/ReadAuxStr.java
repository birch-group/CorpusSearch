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
  Beth Randall:  March 2000
  ReadAuxStr.java
  This class contains methods used by classes that read in
  command or coding files, ie, ReadIn.java and ReadCode.java.
*/
package command;

import java.io.*;
import java.util.*;
import io.*;
import print.*;
import search.*;
import basicinfo.*;

public class ReadAuxStr extends CommandInfo {

    /*
      Get_boolean -- gets value for function-vector boolean variable. 
      Called by method CommandFile.
      input -- String -- latest token from CommandFile, known to
      begin with option name
      -- String -- option_name -- e.g., "print_ur_text:"
      output -- boolean -- boolean value found in CommandFile.
    */
    public static boolean Get_boolean (String option_name) {
        String next;

        try {
	    next = option_name.substring(option_name.indexOf(":") + 1); 
	    next = next.trim();
	    if ((next.equals("T")) || (next.equals("t")) ||
		(next.equals("True")) || (next.equals("true")) ||
		(next.equals("TRUE"))) {
		return true; }
	    if ((next.equals("F")) || (next.equals("f")) ||
		(next.equals("false")) || (next.equals("False")) ||
		(next.equals("FALSE"))) {
		return false; }
	    else {
		System.err.print("ERROR! in query file:  ");
		System.err.print(option_name);
		System.err.print(" must be followed by true ");
		System.err.println("or false.");
		System.err.println("Search aborted.");
		Goodbye.SearchExit(); }
        } 
        catch (Exception e) { e.printStackTrace(); }
	System.err.print("ReadAuxStr.Get_boolean: ");
	System.err.println("reached end of file without finding boolean.");
	Goodbye.SearchExit(); return false; }

    /*
      Get_String:  gets string value for function_calls vector.
      Called by method CommandFile.
      input -- String -- latest token from CommandFile, known to
      begin with option name
      -- String -- option_name -- e.g., "node:"
      output -- String -- String value found in CommandFile.
    */
    public static String Get_String (String option_name) {

        String got_it = "";
	try {
	    got_it = option_name.substring(option_name.indexOf(":") + 1); 
	    got_it = got_it.trim(); }
        catch (Exception e) { e.printStackTrace(); }
	return (got_it); }

    /*
      Get_int:  gets int value for function_calls vector.
      Called by method CommandFile.
      input -- String -- var_token -- latest token from CommandFile, 
      known to begin with option name
      -- String -- option_name -- e.g., "debug_corpus_begin:"
      output -- int -- int value found in CommandFile.
    */
    public static int Get_int (String option_name) {
        Integer value = new Integer(-1);
        String int_str;

        try {
	    int_str = option_name.substring(option_name.indexOf(":") + 1); 
	    int_str = int_str.trim();
	    value = new Integer(int_str); }
        catch (Exception exc) {
            System.err.println("In ReadIn.Get_Integer:");
            System.err.println("IOException:  " + exc.getMessage()); }
        return (value.intValue()); }

    /*
      Get_Remark:  gets multi-token string value for remark.
      Called by method ReadIn.
      input -- String -- string read in from command file,
      beginning with "begin_remark:" 
      output -- void
      side-effect -- CommandInfo.remark contains remark found
      in query file.
    */
    public static void Get_Remark (String option_name) {
        String remarque = "", next;

	try {
	    Parameters.remark = new Vector();
	    remarque = option_name.substring(option_name.indexOf(":") + 1); 
	    remarque = remarque.trim();
	    Parameters.remark.addElement(remarque);
            Parameters.remark_exists = true; }
        catch (Exception e) {
            System.err.println("in ReadAuxStr.Get_Remark:  ");
            System.err.println(e.getMessage());
	    e.printStackTrace(); }
        finally { return; } }

    /*
      CheckNodeBoundary -- checks whether node boundary has been entered.  
      If not, disallows search.
      input --
      output -- true if node boundary is OK, false otherwise.
    */
    public static boolean CheckNodeBoundary() {

        if (node.equals("*")) {
            System.err.println("");
            System.err.println("ERROR!  No node boundary has been specified.");
            System.err.println("Add a line like this to your query file:");
            System.err.println("    node: <node boundary>");
            System.err.println("If you don't know what node boundary to use,");
            System.err.print(" use the root node, $ROOT.");
            System.err.println("");
            System.err.println("SEARCH ABORTED.");
            System.err.println("");
            Goodbye.SearchExit();
            return false; }
        return true; }

} 


