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

package command;

import java.util.*;
import java.io.*;
import basicinfo.*;

public class CommandAux extends CommandInfo{

    public static void CheckListForIgnore(ArgList args_list) {
	RegExpStr arg;
	int i, j;
	Vector arse;
	
	arse = args_list.toVector();
	for (j = 0; j < arse.size(); j++) {
	    arg = (RegExpStr)arse.elementAt(j);
	    CheckIgnore(arg); }
	return; }

    /**
     * checks whether search-function arguments are on the ignore_list.
    */
    public static void CheckIgnore (RegExpStr arg) {
	// found_arg mimics an example of arg found in input.
	String found_arg= ""; 
	StringBuffer found_buff = new StringBuffer();
	char chart;

	// TEMPORARY HACK.

	//arg_loop:  for (int i = 0; i < arg.length(); i++) {
	//  chart = arg.charAt(i);
	//  if (chart == '*' && 
	//(i == 0 || arg.charAt(i - 1) != '\\')) {
	//continue arg_loop; }
	//  if (chart == '\\') {
	//continue arg_loop; }
	//  found_buff.append(chart); }
	//found_arg = found_buff.toString();
	found_arg = "BULLWINKLE";
	if (Vitals.ignore_list.hasMatch(found_arg)) { 
	    System.err.println("");
	    System.err.print("WARNING!  " + arg.toString());
	    System.err.println(" is on the ignore_list.");
	    System.err.println("");
	    System.err.print("    To make the ignore_list empty, ");
	    System.err.println("add this line to your command file:");
	    System.err.println("");
	    System.err.println("        ignore_nodes: null");
	    System.err.println("");
	    System.err.print("    To write your own ignore_list, ");
	    System.err.println("add this line to your command file:");
	    System.err.println("");
	    System.err.println("        ignore_nodes: <your_list_here>");
	    System.err.println(""); }
        return; }

    /**
     * checks that user has set a node boundary.
     */
    public static void CheckNodeBoundary() {
	if (!do_frames && !make_lexicon && !make_label_lexicon && 
	    !print_only  && !reformat_corpus && !make_tag_list &&
	    node == "NO_NODE_BOUNDARY_FOUND") {
	    System.err.println("");
	    System.err.println("ERROR!  No node boundary specified.");
	    System.err.println("");
	    System.err.println("Search aborted.");
	    Goodbye.SearchExit(); }
	return; }

} 








