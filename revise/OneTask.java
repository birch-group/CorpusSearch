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
package revise;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class OneTask {

    private String orig_call;
    private String task_name;
    private String task_stuff;
    private String new_label;
    private String pos_label;
    private String text_label;
    private Vector task_dexes;
    private Vector query_dexes;

    public OneTask() {
	task_dexes = new Vector(); 
        query_dexes = new Vector(); }

    // will be called with e.g.
    // OneTask ("append_label{1}: NP");
    public OneTask(String stuff) {
	this.orig_call = stuff;
	task_dexes = new Vector();
	query_dexes = new Vector();
	this.new_label = stuff.substring(stuff.indexOf(':') + 1);
	this.new_label = this.new_label.trim();
	this.task_stuff = stuff.substring(0, stuff.indexOf(':') + 1);
	this.unPack();
	if (this.task_name.startsWith("add_leaf") 
	    || this.task_name.startsWith("trace")) {
	    unPackLeaf(this.new_label); }
	if (this.task_name.startsWith("concat") && this.new_label.equals("")) {
	    this.new_label = ":"; }
	//this.PrintToSystemErr();
    }

    private void unPack() {
	String int_list, one_int = "";
	char c;
	int j;
	Integer task_dex;

	try {
	    this.task_name = task_stuff.substring(0, task_stuff.indexOf("{"));
	    int_list = task_stuff.substring((task_stuff.indexOf("{")) + 1, 
					    task_stuff.indexOf(":"));
	    for (j = 0; j < int_list.length(); j++) {
		c = int_list.charAt(j);
		if (c >= 48 && c <= 57) { // c is a digit 0 -- 9.
		    one_int += c; }
		else {
		    if (!one_int.equals("")) {
			task_dex = new Integer(one_int);
			task_dexes.addElement(task_dex); 
			one_int = ""; } } }
	    // if add_internal_node was listed with one index, list 
	    // it twice.
	    if (task_name.equals("add_internal_node") &&
		task_dexes.size() == 1) {
		task_dexes.addElement((Integer)task_dexes.elementAt(0)); }
	    //this.PrintToSystemErr();
	} 
	catch (Exception e) {
	    System.err.println("in revise.OneTask:  ");
	    System.err.println("cannot unpack task_stuff: " + task_stuff);
	    System.err.println("example of correct syntax:  ");
	    System.err.println("    append_label{1}: TMP");
	    e.printStackTrace(); }
	finally { return; }
    }

    // leaf_label example: "(D-P os)"
    private void unPackLeaf(String leaf_label) {
	this.pos_label = leaf_label.substring(1, leaf_label.indexOf(" "));
	this.pos_label = this.pos_label.trim();
	this.text_label = leaf_label.substring(leaf_label.indexOf(" ")+1, 
					       leaf_label.length()-1);
	this.text_label = this.text_label.trim();
    }

    public String getCommand() {
	String commodore;
	
	if (this.new_label.length() != 0) {
	    commodore = this.task_stuff + " " + this.new_label; }
	else {
	    commodore = this.task_stuff; }
	return(commodore); }

    public int size() {
	return (this.task_dexes.size()); }

    public void InstallQueryDexes(Vector query_curlies) {
        Integer task_dex, query_dex = new Integer(-4), query_where;
	boolean found = false;

        for (int i = 0; i < task_dexes.size(); i++) {
            task_dex = (Integer)task_dexes.elementAt(i);
	    found = false;
            one_dex: for (int j = 0; j < query_curlies.size(); j++) {
                query_dex = (Integer)query_curlies.elementAt(j);
                if (task_dex.equals(query_dex)) {
                    query_where = new Integer(j);
                    query_dexes.addElement(query_where); 
		    if (found) { SubFlagWarning(query_dex); } 
		    if (!found) { found = true; } }
            }
	    if (!found) { TaskDexError(task_dex); } }
        return; }

    private void SubFlagWarning(Integer query_dex) {
	System.err.println("");
	System.err.print("WARNING!  Subsequent flag {" + query_dex +"}");
	System.err.println(" has been ignored.");
	System.err.println("");
	return; }

    private void TaskDexError(Integer task_dex) {
	System.err.println("");
	System.err.print("ERROR!  Could not find query argument flagged {");
	System.err.println(task_dex + "}, as called here:");
	System.err.println("    " + this.orig_call);
	System.err.println("Search aborted.");
	System.err.println("");
	Goodbye.SearchExit(); }

    public String getTaskName() {
        return (this.task_name); }

    public String getNewLabel() {
        return (this.new_label); }

    public Vector getQueryDexes() {
        return (this.query_dexes); }

    public String getPosLabel() {
	return (this.pos_label); }

    public String getTextLabel() {
	return (this.text_label); }

    public void PrintToSystemErr() {
	System.err.println("");
	System.err.println("task_stuff: " + this.task_stuff);
	System.err.println("task_name:  " + task_name);
	if (!task_name.startsWith("add_leaf")) {
	    System.err.println("new_label: " + new_label); }
	else {
	    System.err.println("pos_label:  " + pos_label);
	    System.err.println("text_label: " + text_label); }
	System.err.println("task_dexes: " + task_dexes);
	System.err.println("query_dexes: " + query_dexes);
	System.err.println("");
    }

    public static void main (String[] args) {
	OneTask ot;
	ot = new OneTask("append_label{1}: TMP");
	ot.PrintToSystemErr();
	ot = new OneTask("add_internal_node{1, 4}: QP");
	ot.PrintToSystemErr();
	ot = new OneTask("remove_nodes{2 5}:");
	ot.PrintToSystemErr();
	return;
    }

} 
