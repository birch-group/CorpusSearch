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

package drawtree;

import java.awt.*;
import java.util.*;
import java.io.*;
import command.*;
import basicinfo.*;
import CSParse.*;
import syntree.*;
import print.*;
import io.*;
import search_result.*;

public class CorpusDraw {

    public static ChangeGraphicTree for_undo, for_undo1, for_undo2;
    public static String prev_file_name, file_name, curr_file_name; 
    public static String command_name, dest_name, show_str = "", query;
    public static ToolView toole;
    public static ArgList show_list = new ArgList();
    // MIN_HT and MIN_WDTH are necessary for the setting of TreeCanvas,
    // to solve the big-tree problem.
    public static int MIN_HT =1200, MIN_WDTH = 20000, file_dex = 0;
    public static int max_ht, max_wdth;
    public static Vector source_list;
    public static OutFileDominatrix out_dom;
    public static PrintWriter out_stuff;
    public static CorpusTags corpse_tags;
    public static boolean has_query, show_only, copy_corpus, first_out_file;
    public static TreeBuffer tree_buff;

    public static void main(String argv[]) {
	try {
	    PrintBookmark();
	    Init();
	    prev_file_name = "RAO_QING_TIAN";
	    first_out_file = true;
	    MyEvents.urt = new UrText();
	if (argv.length == 1) {
	    file_name = argv[0]; 
  	    source_list.addElement(file_name); 
	    toole = new ToolView();
	    DrawMeat.CrankThrough(source_list, toole); }
	if (argv.length > 1) {
	    command_name = argv[0];
	    if (command_name.endsWith(".q") || command_name.endsWith(".c")) {
		file_dex = 1;
		ReadIn.PrefsAndQuery(command_name);
	        Init(); }
	    if (CommandInfo.gotQuery()) {
		has_query = true; 
		query = CommandInfo.query;
		ParseQuery.makeQueryTree(new StringReader(CommandInfo.query));}
	    show_only = CommandInfo.show_only;
	    show_str = CommandInfo.show_str;
	    if (show_only) {
		show_list = new ArgList(show_str); }
	    toole = new ToolView();
	    for (int i = file_dex; i < argv.length; i++) {
		file_name = argv[i];
		source_list.addElement(file_name); }
	    DrawMeat.CrankThrough(source_list, toole); } 
	if (argv.length < 1) {
	    WrongArgsMessage(); }
	}
	catch (Exception e) {
	    System.err.println("in CorpusDraw: main: ");
	    e.printStackTrace();
	    Goodbye.SearchExit(); }
	finally {
	    System.exit(1);
	    return; }
	}

    public static boolean hasQuery() {
	return has_query; }

    private static void Init() {
	tree_buff = new TreeBuffer();
	source_list = new Vector();
	has_query = false;
	show_only = false;
        copy_corpus = CommandInfo.copy_corpus;
        max_ht = 0; max_wdth = 0; } 

    public static ChangeGraphicTree currTree() {
	return(tree_buff.getCurrTree()); }

    public static ChangeGraphicTree previousTree() {
	return (tree_buff.previousTree()); }

    public static ChangeGraphicTree followingTree() {
	return (tree_buff.followingTree()); }

    public static int getShowDex() {
	return (tree_buff.getShowDex()); }

    public static void setShowDex(int new_dex) {
	tree_buff.setShowDex(new_dex); }

    public static void popForward() {
	tree_buff.setNext();
	return; }

    public static void rmPrev() {
	tree_buff.rmPrev(); return; }

    public static void rmFoll() {
	tree_buff.rmFoll(); return; }

    public static boolean gotForward() {
	return (tree_buff.gotForward()); }

    public static void addToBuffer(SynTree sparse, int sent_num_id,
				   String file_name) {
	ChangeGraphicTree cgt;
	ChangeTree ct;

	ct = new ChangeTree(sparse, sent_num_id, file_name);
	if (DrawLoop.hasCollapseList()) {
	    cgt = new ChangeGraphicTree(ct, DrawLoop.getCollapseList()); } 
	else { cgt = new ChangeGraphicTree(ct); } 
	addToBuffer(cgt); }

    public static void showLast() {
	tree_buff.showLast(); }

    public static void addToBuffer(ChangeGraphicTree cgt) {
	if (cgt.getOptHeight() > max_ht) {
	    max_ht = cgt.getOptHeight(); }
	if (cgt.getOptWidth() > max_wdth) {
	    max_wdth = cgt.getOptWidth(); }
	//cgt.PrintToSystemErr(0, 5);
	//System.err.println("");
	tree_buff.addToTreeBuffer(cgt); }

    public static void blindAddToBuffer(SynTree sparse, int sent_num_id, 
					String file_name, 
					SentenceResult a_result) {
	ChangeTree ct = new ChangeTree(sparse, sent_num_id, 
				       file_name);
	ChangeGraphicTree cgt = new ChangeGraphicTree(ct, a_result); 
	tree_buff.blindAddToTreeBuffer(cgt, !a_result.isEmpty()); }

    public static void blindAddToBuffer(SynTree sparse, int sent_num_id, 
					String file_name) { 
	ChangeTree ct = new ChangeTree(sparse, sent_num_id, 
				       file_name);
	ChangeGraphicTree cgt = new ChangeGraphicTree(ct); 
	blindAddToBuffer(cgt); }

    public static void blindAddToBuffer(ChangeGraphicTree cgt) {
	tree_buff.blindAddToTreeBuffer(cgt); }

    public static void addToBuffer(SynTree sparse, int sent_num_id, 
				   String file_name, SentenceResult a_result) {
	ChangeTree ct = new ChangeTree(sparse, sent_num_id, 
				       file_name);
	ChangeGraphicTree cgt = new ChangeGraphicTree(ct, a_result); 
	addToBuffer(cgt, !a_result.isEmpty()); }

    public static void addToBuffer(ChangeGraphicTree cgt, boolean has_result) {
	tree_buff.addToTreeBuffer(cgt, has_result); }

    // split_flag is -1 for split at beginning of tree,
    // 1 for split at end of tree.
    // 0 for undo and redo -- don't change the index.
    public static void pushForward(ChangeGraphicTree cgt, int split_flag) {
	if (split_flag == 1 || split_flag == -1) {
	    DrawLoop.incSentNumID(); }
	tree_buff.pushForward(cgt); }

    public static boolean goBack() {
	return (tree_buff.setPrevious()); }

    public static boolean goToPrevious(int to_where) {
	return (tree_buff.goToPrevious(to_where)); }

    public static void saveAll() {
	printTreeBuffer(); }

    public static void printTreeBuffer() {
	out_dom = new OutFileDominatrix(dest_name);
	out_stuff = out_dom.getPrintWriter();

	for (int i = 0; i < tree_buff.size(); i++) {
	    printToCorrectOutput(tree_buff.changeTreeAt(i)); }
	out_stuff.println("");
	out_stuff.flush(); }
    
    public static void printToCorrectOutput(ChangeTree ct) {

	if (ct.isEmpty()) { return; }
	try {
	    curr_file_name = ct.getFileName();
	    correctOutDom();
	    PrintTree.PrintToken(ct, out_stuff); }
	catch (NullPointerException npe) {
	    System.err.println("in CorpusDraw.printToCorrectOutput:  ");
	    ct.PrintToSystemErr(0, 5);
	    System.err.println("");
	    System.exit(1); }
	}


    public static void correctOutDom() {
	if (prev_file_name.equals(curr_file_name)) {
	    return; }
	if (!first_out_file) {
	    out_stuff.flush(); 
	    out_stuff.println("");
	    out_dom.close();
	    OutMessage(); }
	first_out_file = false;
	dest_name = curr_file_name + ".new";
	out_dom = new OutFileDominatrix(dest_name);
	out_stuff = out_dom.getPrintWriter(); 
	prev_file_name = curr_file_name;
	return; }


   /**
     * prints version number and bookmark to CorpusDraw Manual.
    */
    public static void PrintBookmark () {

	System.err.println("");
	System.err.println(Vitals.c_note);
	System.err.println("");
	System.err.println("CorpusDraw version " + Vitals.version_number);
        System.err.println("");
        System.err.print("CorpusDraw Manual:  ");
	System.err.println(Vitals.manual_address);
	System.err.println(""); }
 
    public static void WrongArgsMessage() {
	System.err.println("");
	System.err.println("to run CorpusDraw, use 1 or 2 arguments:  ");
	System.err.println("    CorpusDraw <source_file_name>");
	System.err.println("or:  ");
	System.err.print("    CorpusDraw <query_file_name> ");
	System.err.println("<source_file_name>");
	System.err.println(""); }


    public static void OutMessage() {
	System.err.println("");
	System.err.println("");
	System.err.println("Output file is ");
	System.err.println("    " + dest_name); 
        System.err.println("");
        System.err.println(""); }

}

    






