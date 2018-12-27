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
  Definite.java
  This class deals with definition files.
*/
package command;

import java.util.*;
import java.io.*;
import io.*;
import basicinfo.*;

public class Definite extends CommandInfo{

    private static Vector defs = new Vector();
    private static String def_name_ok;

    private static boolean FindDefFile(String def_name) {
	File def_file;
	String prefs_dir, new_def_name;
	int slash_dex;

	def_name_ok = def_name;
	def_file = new File(def_name);
	if (!def_file.exists()) {
	    if (use_prefs) {
		slash_dex = CommandInfo.prefs_name.lastIndexOf("/");
		if (slash_dex < 0) {
		    System.err.print("in Definite.java: ");
		    System.err.println("prefs_name has no backslash!");
		    return false; }
		prefs_dir = CommandInfo.prefs_name.substring(0, slash_dex + 1);
		new_def_name = prefs_dir + def_name;
		//System.err.println("new_def_name:  " + new_def_name);
		def_file = new File(new_def_name);
		if (def_file.exists()) {
		    def_name_ok = new_def_name; 
		    return true; }
		System.err.println("WARNING!  in Definite.java:  ");
		System.err.println("def file" + def_name + " does not exist!");
		return false; } }
	return true; }
    
    /*
      MakeDefVector:  makes definition vector containing information
      found in the definition file.  Vector defs is stored
      in CommandInfo.java.
      input -- void, but uses CommandInfo.def_name.
      output -- void.
      side-effect -- CommandInfo.defs contains information found
      in definition file.
    */
    public static boolean MakeDefVector (String def_name) {
	InFileDominatrix infile;
	Vector one_def = new Vector();
	String def;

	defs = new Vector();
	try {
	    if (!(def_name.endsWith(".def"))) {
		System.err.print("ERROR!  Name of definition file ");
		System.err.println("must end with \".def\".");
		System.err.println(def_name + " is not an acceptable name."); 
		System.err.println("Search aborted.");
		System.err.println("");
		return false;
	    }
	    if (!FindDefFile(def_name)) {
		return false; }

	    infile = new InFileDominatrix(def_name_ok);
	    use_def_file = true;
	    one_def = new Vector();
	    char_by_char:  do {
		def  = infile.NextString();
		if (def.endsWith(":")) {
		    one_def = new Vector();
		    one_def.addElement
			(def.substring(0, def.length() - 1));
		    def = infile.NextString();
		    one_def.addElement(def);
		    defs.addElement(one_def);
		} // end if def.endsWith(":")
	    } while (!infile.EOF);  
	} // end try
	catch (Exception e) {
	    System.err.println("in command.Definite.java:  ");
	    e.printStackTrace();
	    Goodbye.SearchExit();
	} // end catch
	finally {
	    SelfRefer();
	    return true; } 
    } 

    /*
      SelfRefer -- finishes def_vector by handling self-references;
      i.e.:  same: same|saam
      samemore: $saam|sam
      should become this sub_vector:
      samemore: same|saam|sam
      Called by MakeDefVector.
      input
      output 
      side-effect: self-reference installed in CommandInfo.defs.
    */
    public static void SelfRefer () {
	String defee, one_defee, uno_def, uno_defee, dollarless, defee_str;
	Vector one_def, defee_list, sub_def;
	int i, j, k;

	for (i = 0; i < defs.size(); i++) {
	    one_def = (Vector)defs.elementAt(i);
	    defee =(String)one_def.elementAt(1);
	    // defee is a PipeList, e.g., some|one|to|watch
	    defee_list = PipeList.MakeList(defee);
	    for (j = 0; j < defee_list.size(); j++) {
		one_defee = (String)defee_list.elementAt(j);
		if (one_defee.startsWith("$")) {
		    dollarless = one_defee.substring(1);
		    for (k = 0; k < defs.size(); k++) {
			sub_def = (Vector)defs.elementAt(k);
			uno_def = (String)sub_def.elementAt(0);
			if (dollarless.equals(uno_def)) {	
			    uno_defee = (String)sub_def.elementAt(1);
			    defee_list.setElementAt(uno_defee, j);
			}
		    } // end for k = 0; k < defs.size()		
		} // end if defee.startsWith("$")
	    } // end for j = 0; j < defee_list.size(); j++
	    // build PipeList from vector list.
	    defee_str = "";
	    for (j = 0; j < defee_list.size(); j++) {
		defee_str += (String)defee_list.elementAt(j) + "|";	
	    }
	    // delete last "|".		
	    defee_str = defee_str.substring(0, defee_str.length() - 1);	
	    one_def.setElementAt(defee_str, 1);
	    defs.setElementAt(one_def, i);		
	} // end for i = 0; i < defs.size(); i)
    } // end method SelfRefer

    /*
      InstallDefs -- installs definitions found in definition file
      in query.  e.g., replaces "finite_verb" with
      "*MD|*HVP|*HVD|*DOP|*DOD|*BEP|*BED|*VBP|*VBD".
      input -- void
      output -- void
      side-effect -- CommandInfo.query has definitions 
      installed.
    */
    public static String InstallDefs (String orig) {
	StringReader in_queer;
	StringBuffer query_buff = new StringBuffer();
	StringBuffer arg_buff = new StringBuffer();
	StringBuffer stall_buff = new StringBuffer();
	StringBuffer close_buff = new StringBuffer();
	String arg, pre_str = "", stalled;
	Vector one_def;
	String shorthand = "";
	int i,j, c = 0, begin_pipe = -1;
	boolean first_pipe = true;

	in_queer = new StringReader(orig);
	try{
	    queer_by_char: do {
		c = in_queer.read();
		if (c > 32) {
		    arg_buff.append((char)c);
		    continue queer_by_char; }
		if (c <= 32) {
		    arg = arg_buff.toString();
		    arg_buff = new StringBuffer();
		    while (arg.startsWith("(")) {
			arg = arg.substring(1);
			query_buff.append("("); }
		    while (arg.endsWith(")")) {
			arg = arg.substring(0, arg.length() - 1);
			close_buff.append(")"); }
		    if (arg.startsWith("!")) {
			arg = arg.substring(1);
			stall_buff.append("!"); }
		    if (arg.startsWith("[")) {
			pre_str = 
			    arg.substring(0, arg.indexOf("]") + 1);
			arg = arg.substring(arg.indexOf("]") + 1);
			stall_buff.append(pre_str); }
		    if (arg.startsWith("{")) {
			pre_str = 
			    arg.substring(0, arg.indexOf("}") + 1);
			arg = arg.substring(arg.indexOf("}") + 1);
			stall_buff.append(pre_str); }
		    begin_pipe = arg.indexOf("|");
		    if (begin_pipe == -1) {
			stall_buff.append(StallOne(arg));
			query_buff.append(stall_buff.toString());
			query_buff.append(close_buff.toString() + " ");
			stall_buff = new StringBuffer();
			close_buff = new StringBuffer();
			continue queer_by_char; }
		    first_pipe = true;
		    pipe_loop:  while ((begin_pipe != -1) 
				       && (begin_pipe < arg.length())) {
			String one;
			int end_pipe = -3; 
			if (first_pipe) {
			    one = arg.substring(0, begin_pipe);
			    first_pipe = false;
			    begin_pipe += 1; }
			else {
			    end_pipe = 
				arg.indexOf("|", begin_pipe + 1);
			    if (end_pipe == -1) {
				end_pipe = arg.length(); }
			    one = arg.substring(begin_pipe, end_pipe);	
			    begin_pipe = end_pipe + 1; }
			stall_buff.append(StallOne(one));
			if (end_pipe != arg.length()) {
			    stall_buff.append("|"); }
			continue pipe_loop;
		    } // end if begin_pipe != -1
		    query_buff.append(stall_buff.toString());
		    query_buff.append(close_buff + " ");
		    stall_buff = new StringBuffer();
		    close_buff = new StringBuffer();
		} // end if (c <= 32)
	    }  while (c != -1);  // end queer_by_char
	} // end try
	catch (Exception e) {
	    System.err.println("in ReadIn.java:  InstallDefs:  ");
	    System.err.println(e.getMessage());
	    System.err.println("");
	    Goodbye.SearchExit();
	} //end catch
	finally {
	    //	    System.err.println("query_buff.toString():  " + query_buff.toString());
	    return(query_buff.toString());
	} // end finally
    } // end method InstallDefs

    /*
      StallOne -- given one argument, searches defs vector for
      matching label.  If label is found, returns
      definition.
      Input does not contain "|".
      input -- String -- argument
      output -- String -- definition, or same as input if 
      no definition found.
    */
    public static String StallOne (String one) {
	Vector one_def;
	String shorthand;
	int i;

	for (i = 0; i < defs.size(); i++) {
	    one_def = (Vector)defs.elementAt(i);
	    shorthand = (String)one_def.elementAt(0);
	    if (one.equals(shorthand)) {
		return (String)one_def.elementAt(1);
	    } // end for i= 0; i < defs.size(); i++
	} // end for i = 0; i < defs.size();
	return one;
    } 

} 


