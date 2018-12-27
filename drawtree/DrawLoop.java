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

import java.io.*;
import java.awt.event.*;
import java.util.*;
import io.*;
import print.*;
import syntree.*;
import stats.*;
import search.*;
import search_result.*;
import CSParse.*;
import command.*;
import basicinfo.*;


public class DrawLoop extends CorpusDraw {

    private static boolean PREV_EOF = false, has_collapse_list;
    private static TBToRix mytrees;
    private static String curr_file_name, my_collapse_list = "";
    private static int sent_num_id, save_response;
    private static Node to_node;

    
    public static TBToRix getMyTBToRix() {return mytrees; }

    public static String getCollapseList() {
	if (!has_collapse_list) { return (""); }
	return my_collapse_list; }

    public static boolean hasCollapseList() { return has_collapse_list; }

    /*
      thruFile -- loops through an input file, finding all parsed 
      sentences and searching them. 
      input -- void.
      output -- void.
    */
    public static void thruFile (String file_name, ToolView toole) {
        SentenceResult a_result;
        int one_hits;
	boolean did_it, first;
	String message;

	sent_num_id = 0; 
	mytrees = new TBToRix(file_name);
	first = true;
	try {
            //file_info = new StatsPerFile(file_name);
	    //	    PrintOut.Header(file_info.getFileID(), OutStuff);
	    loadNextTree(mytrees);
	    // the following code is a shameless hack
	    // to deal with scrolling issues.
	    //	    loadNextTree(mytrees); 
	    if (PREV_EOF) {
		System.err.println("");
		CorpusDraw.toole.treeve.moveScrollBars(0, 300000); }
	    else {
		did_it = CorpusDraw.goBack();
		CorpusDraw.toole.treeve.treece.correctUrText();
		MyEvents.numtf.put(1); }
	    // end shameless hack.
	    if (corpse_tags.hasTags()) {
		message = "tags:   ";
		message += corpse_tags.getShortFileName();
		MyEvents.warn(message); }
	    if (corpse_tags.hasError()) {
		message = "WARNING! unable to process ";
		message += corpse_tags.getShortFileName();
	        MyEvents.warn(message); }
	    get_action: while (true) {
		// the following line is mysteriously necessary.
		System.err.print("");
		// back button.
		if (MyEvents.bb.getPressed() || MyEvents.prevToken()) {
		    MyEvents.clearTextFields();
		    MyEvents.bb.resetButton();
		    MyEvents.resetPrevToken();
		    did_it = CorpusDraw.goBack();
		    if (did_it) {
			toole.treeve.treece.addTree(); }
		    else {
			message = "there's nothing to go back to.";
			MyEvents.warn(message); } }
		// next button.
		if (MyEvents.nb.getPressed() || MyEvents.nextToken()) {
		    MyEvents.clearTextFields();
		    MyEvents.nb.resetButton();
		    MyEvents.resetNextToken(); 
		    loadNextTree(mytrees); }
		// goto button.
		if (MyEvents.gtb.getPressed()) {
		    did_it = goToTree(mytrees);
		    if (did_it) {
			MyEvents.clearTextFields(); }
		    MyEvents.gtb.resetButton(); }
		// scroll from ur_text.
		if (MyEvents.urg.isSelected()) {
		    to_node = toole.treeve.
			scrollToWord(MyEvents.urg.getWordDex(),
				     MyEvents.urg.getSelectWord());
		    MyEvents.warn("will scroll to " + to_node.toString());
		    MyEvents.urg.resetSelected(); }
		// delete button.
		if (MyEvents.db.getPressed()) {
		    MyEvents.clearTextFields();
		    MyEvents.db.resetButton();
		    CorpusDraw.currTree().delete_node();
		    toole.treeve.treece.resetTree(); }
		// raze button.
		if (MyEvents.rzb.getPressed()) {
		    MyEvents.clearTextFields();
		    MyEvents.rzb.resetButton();
		    CorpusDraw.currTree().raze_node();
		    toole.treeve.treece.resetTree(); }
		// reset button.
		if (MyEvents.rsb.getPressed()) {
		    MyEvents.clearButtons();
		    MyEvents.clearTextFields();
		    CorpusDraw.currTree().clearSelected();
		    CorpusDraw.currTree().clearCollapsed();
		    MyEvents.warn("have reset"); 
		    toole.treeve.treece.resetTree(); }
		// undo button.
		if (MyEvents.ub.getPressed()) {
		    MyEvents.clearTextFields();
		    MyEvents.ub.resetButton();
		    CorpusDraw.currTree().undo();
		    toole.treeve.treece.resetTree(); }
		// redo button.
		if (MyEvents.rb.getPressed()) {
		    MyEvents.clearTextFields();
		    MyEvents.rb.resetButton();
		    CorpusDraw.currTree().redo();
		    toole.treeve.treece.resetTree(); }
		// replace button.
		if (MyEvents.rlb.getPressed()) {
		    MyEvents.rlb.resetButton();
		    MyEvents.intf.resetChanged();
		    CorpusDraw.currTree().replaceLabel();
		    toole.treeve.treece.resetTree(); }
		// add internal node button.
		if (MyEvents.ainb.getPressed()) {
		    MyEvents.ainb.resetButton();
		    CorpusDraw.currTree().addInternalNode();
		    toole.treeve.treece.resetTree(); }
		// add leaf after.
		if (MyEvents.lab.getPressed()) {
		    MyEvents.lab.resetButton();
		    CorpusDraw.currTree().InsertLeafAfter();
		    toole.treeve.treece.resetTree(); }
		// add leaf before.
		if (MyEvents.lbb.getPressed()) {
		    MyEvents.lbb.resetButton();
		    CorpusDraw.currTree().InsertLeafBefore();
		    toole.treeve.treece.resetTree(); }
		// move to button.
		if (MyEvents.mtb.getPressed()) {
		    MyEvents.mtb.resetButton();
		    CorpusDraw.currTree().moveTo();
		    toole.treeve.treece.resetTree(); }
		// trace before button.
		if (MyEvents.tbb.getPressed()) {
		    MyEvents.tbb.resetButton();
		    CorpusDraw.currTree().TraceBefore();
		    toole.treeve.treece.resetTree(); }
		// ich trace after button.
		if (MyEvents.tab.getPressed()) {
		    MyEvents.tab.resetButton();
		    CorpusDraw.currTree().TraceAfter();
		    toole.treeve.treece.resetTree(); }
		// merge previous button.
		if (MyEvents.mpb.getPressed()) {
		    MyEvents.mpb.resetButton();
		    CorpusDraw.currTree().mergePrevious();
		    toole.treeve.treece.resetTree(); }
		// coindex button.
		if (MyEvents.cib.getPressed()) {
		    MyEvents.cib.resetButton();
		    CorpusDraw.currTree().co_index();
		    toole.treeve.treece.resetTree(); }
		// merge following button.
		if (MyEvents.mfb.getPressed()) {
		    MyEvents.mfb.resetButton();
		    CorpusDraw.currTree().mergeFollowing();
		    toole.treeve.treece.resetTree(); }
		// split button.
		if (MyEvents.sb.getPressed()) {
		    MyEvents.sb.resetButton();
		    CorpusDraw.currTree().split();
		    toole.treeve.treece.resetTree(); }
		// shrink button.
		if (MyEvents.shb.getPressed()) {
		    MyEvents.shb.resetButton();
		    CorpusDraw.currTree().shrink();
		    toole.treeve.treece.resetTree(); }
		// swell button.
		if (MyEvents.swb.getPressed()) {
		    MyEvents.swb.resetButton();
		    CorpusDraw.currTree().swell();
		    toole.treeve.treece.resetTree(); }
		// show only button.
		if (MyEvents.sob.getPressed()) {
		    MyEvents.sob.resetButton();
		    toole.treeve.treece.draw_show_only(); 
		    continue; }
		// show all button.
		if (MyEvents.sab.getPressed()) {
		    MyEvents.sab.resetButton();
		    toole.treeve.treece.draw_show_all(); }
		// show only list button.
		if (MyEvents.solb.getPressed()) {
		    MyEvents.solb.resetButton();
		    toole.treeve.treece.set_show_only(); }
		// collapse button.
		if (MyEvents.cb.getPressed()) {
		    MyEvents.cb.resetButton();
		    CorpusDraw.currTree().collapseNodes();
		    toole.treeve.treece.resetTree();}
		// expand button.
		if (MyEvents.eb.getPressed()) {
		    MyEvents.eb.resetButton();
		    CorpusDraw.currTree().expandNodes(); 
		    toole.treeve.treece.resetTree(); }
		// expand all button.
		if (MyEvents.eab.getPressed()) {
		    MyEvents.eab.resetButton();
		    CorpusDraw.currTree().expandAllNodes();
		    toole.treeve.treece.resetTree(); }
		// collapse list button.
		if (MyEvents.clb.getPressed()) {
		    MyEvents.clb.resetButton();
		    my_collapse_list = CorpusDraw.currTree().
			collapseList(my_collapse_list);
		    if (my_collapse_list.equals("")) {
			has_collapse_list = false; }
		    else { has_collapse_list = true; }
		    toole.treeve.treece.resetTree(); }
		// collapse clear button.
		if (MyEvents.ccb.getPressed()) {
		    MyEvents.ccb.resetButton();
		    //my_collapse_list = "";
		    //has_collapse_list = false;
		    CorpusDraw.currTree().clearCollapsed();
		    toole.treeve.treece.resetTree(); }
		// save button.
		if (MyEvents.svb.getPressed()) {
		    saveFile(mytrees);
		    MyEvents.svb.resetButton(); }
		// quit file button.
		if (MyEvents.qfb.getPressed()) {
		    MyEvents.qfb.resetButton();
		    save_response = MySave.willSave();
		    if (save_response == -1) {
			continue get_action; }
		    if (save_response == 1) {
			quitThisFile(mytrees);
		        loadNextTree(mytrees);
		        continue get_action; }
		    if (save_response == 0) {
			loadNextTree(mytrees);
		        continue get_action; } }
		// quit button.    
		if (MyEvents.qb.getPressed()) {
		    MyEvents.qb.resetButton();
		    save_response = MySave.willSave();
		    if (save_response == -1 ) {
			continue get_action; }
		    if (save_response == 1) {
			finishFile(mytrees); 
		        System.exit(1); }
		    if (save_response == 0) {
			System.exit(1); } }
		}
        } // end try
        catch (Exception e) {
            System.err.println("in DrawLoop.thruFile:  ");
            System.err.println(e.getMessage());
            e.printStackTrace();
	    mytrees.CorpusError();
            Goodbye.SearchExit(); }
        finally {
	    //	    search_info.file_statsAdd(file_info);
	    //PrintOut.Footer(file_info, OutStuff);
	    //finishFile();
	    return; }
    } 

    public static void incSentNumID() {
	sent_num_id += 1; }

    public static void decSentNumID() {
	sent_num_id -= 1; }

    public static int getSentNumID() {
	return(sent_num_id); }

    public static ChangeTree readNextTree() {
	SynTree sparse = new SynTree();
	ChangeTree ct;
 
	try {
	    sparse = mytrees.OneSentence();
	    sent_num_id += 1; }
	catch (Exception e) {
	    System.err.println("in DrawLoop: readNextTree: ");
	    e.printStackTrace(); }
        finally {
	    ct = new ChangeTree(sparse, sent_num_id, mytrees.getFileName());
	    return (ct);} }

    public static boolean goToTree() {
	return(goToTree(mytrees)); }

    public static boolean goToTree(TBToRix mytrees) {
	int to_where = CorpusDraw.currTree().getGoToNum();
	return goToTree(mytrees, to_where); } 


    public static boolean goToTree(TBToRix mytrees, int to_where) {
	ChangeGraphicTree cgt = CorpusDraw.currTree();
	SynTree sparse;
	ChangeTree ct;
	int orig_show_dex;
	String message;
	boolean done = false;
	SentenceResult a_result;

	orig_show_dex = CorpusDraw.getShowDex();
	if (to_where < 0) { return done; }
	try {
	    CorpusDraw.currTree().lapseRemoveAll();
	    if (sent_num_id > to_where) {
		CorpusDraw.goToPrevious(to_where); 
		toole.treeve.treece.addTree();
		done = true;
		return done; }
	    while (sent_num_id < to_where) {
		if (CorpusDraw.gotForward()) {
		    CorpusDraw.popForward();
		    if (sent_num_id == to_where) {
			CorpusDraw.showLast(); break; }
		    continue; }
		sparse = mytrees.OneSentence();
		sent_num_id += 1;
		if (CorpusDraw.hasQuery()) {
		    a_result = ParseQuery.evaluable(sparse);
		    CorpusDraw.addToBuffer(sparse, sent_num_id,
					   mytrees.getFileName(),
					   a_result); }
		else {
		    CorpusDraw.addToBuffer(sparse, sent_num_id, 
					   mytrees.getFileName()); }
		if (sent_num_id == to_where) { 
		    CorpusDraw.showLast(); break; }
		if (sparse.isEmpty()) {
		    message = "ERROR! there are less than ";
		    message += to_where + " trees in the file.";
		    MyEvents.warn(message);
		    CorpusDraw.setShowDex(orig_show_dex);
		    return done; } }
	    toole.treeve.treece.addTree();
            done = true; return done; } 
	catch (Exception e) {
	    System.err.println("in DrawLoop.goToTree:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	    Goodbye.SearchExit(); } 
        finally { return done; } }

    public static void loadNextTree(TBToRix mytrees) {
	SynTree sparse;
	SentenceResult a_result;

	try {
	    if (PREV_EOF) {
		PREV_EOF = false; 
		nextFile(); }
	    if (CorpusDraw.gotForward()) {
		CorpusDraw.popForward(); 
		toole.treeve.treece.addTree(); 
	        return; }
	    sparse = mytrees.OneSentence();
	    sent_num_id += 1;
	    //	    sparse.PrintToSystemErr();
	    if (sparse.isEmpty()) {
		PREV_EOF = true;
		return; }
	    if (CorpusDraw.hasQuery()) {
		a_result = ParseQuery.evaluable(sparse);
		CorpusDraw.addToBuffer(sparse, sent_num_id,
				       mytrees.getFileName(),
				       a_result); 
		while (a_result.isEmpty()) {
		    sparse = mytrees.OneSentence();
		    sent_num_id += 1;
		    if (sparse.isEmpty()) {
			nextFile();
			PREV_EOF = true;
			return; }
		    a_result = ParseQuery.evaluable(sparse); 
		    CorpusDraw.addToBuffer(sparse, sent_num_id,
					   mytrees.getFileName(),
					   a_result); } }
	    else {
		CorpusDraw.addToBuffer(sparse, sent_num_id, 
				       mytrees.getFileName()); }
	    toole.treeve.treece.addTree(); }
	catch (Exception e) {
	    System.err.println("in DrawLoop.loadNextTree:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	    Goodbye.SearchExit(); }
	finally {
	    return; }}
    
    public static void readAllTrees(TBToRix mytrees) {
	SynTree sparse;
	SentenceResult a_result;

	//MyEvents.warn("reading input file:  " + mytrees.getFileName());
	try {
	    while(true) {
		sparse = mytrees.OneSentence();
		sent_num_id += 1;
		if (sparse.isEmpty()) {
		    return; }
		if (CorpusDraw.hasQuery()) {
		    a_result = ParseQuery.evaluable(sparse); 
		    CorpusDraw.blindAddToBuffer(sparse, sent_num_id,
						mytrees.getFileName(),
						a_result); }
		else {
		    CorpusDraw.blindAddToBuffer(sparse, sent_num_id, 
						mytrees.getFileName()); } } }
	catch (Exception e) {
	    System.err.println("in DrawLoop.readAllTrees:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	    Goodbye.SearchExit(); }
	finally { return; }}
    
    public static void nextFile() {
	int save_response;

	curr_file_name = DrawMeat.nextSourceFile();
	if (curr_file_name.equals("NO_FILE_FOUND")) {
	    //toole.treeve.treece.setEndOfFile();
	    MyEvents.warn("END OF FILE");
	    //  System.err.println("will finish file:  ");
	    save_response = MySave.willSave();
	    if (save_response == -1 ) {
		return; }
	    if (save_response == 1) {
		finishFile(mytrees); 
		System.exit(1); }
	    if (save_response == 0) {
		System.exit(1); } }
	thruFile(curr_file_name, toole);
	return; } 

    public static void quitThisFile(TBToRix my_trees) {
	int save_response;

	curr_file_name = DrawMeat.nextSourceFile();
	if (curr_file_name.equals("NO_FILE_FOUND")) {
	    finishFile(mytrees);
	    System.exit(1); }
	thruFile(curr_file_name, toole);
	return; } 

    public static void saveFile() {
	saveFile(mytrees); }

    public static void saveFile(TBToRix mytrees) {
	saveWarn();
	readAllTrees(mytrees);
	CorpusDraw.printTreeBuffer();  
	MyEvents.warn("saved as " + CorpusDraw.dest_name);
        System.err.println("saved as " + CorpusDraw.dest_name);}

    public static int saveWarn() {
	System.err.println("will save: " + mytrees.getFileName());
	MyEvents.warn("will save:  " + mytrees.getFileName());  
        return (1); }

    public static void finishFile(TBToRix mytrees) {
	PREV_EOF = true;
	saveFile(mytrees); }

} 


