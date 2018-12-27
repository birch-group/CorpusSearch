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

package search;

import java.util.*;
import syntree.*;
import search_result.*;
import revise.*;

/**
 * used for making changes to the input tree.
 */
public class Revisions extends Syntax {

    private static Vector changed_dexes, querydexes;
    private static String taskname, old_label, newlabel;
    private static String appendicitis, pos, text;
    private static int index_to_change, replace_this, i, j, k, l;
    private static int replace_this2, start, finish, temp;
    private static Node node_to_change, node2;
    private static SubResult subb;
    private static OneTask ot;
    private static ChangeTree changeable;
    private static boolean done;

    /*
      Plain --
    */
    public static void Plain (SynTree sparse, 
			      TaskList tl, SentenceResult sr) {

	changeable = new ChangeTree(sparse);
	//	tl.PrintToSystemErr();
	task_loop:  for (j = 0; j < tl.size(); j++) {
	    ot = tl.taskAt(j);
	    unPackOneTask(ot);
	    if (taskname.equals("replace_label") 
		|| taskname.equals("append_label")
		|| taskname.equals("prepend_label")
		|| taskname.equals("post_crop_label")
		|| taskname.equals("pre_crop_label")  ) {
		labelChangesTask(sr);
		continue task_loop; }
	    if (taskname.equals("add_leaf_before")) {
		addLeafBeforeTask(sr);
		continue task_loop; }
	    if (taskname.equals("add_leaf_after")) {
                addLeafAfterTask(sr);
                continue task_loop; }
	    if (taskname.equals("delete_leaf")) {
		deleteLeafTask(sr);
		continue task_loop; }
	    if (taskname.equals("delete_node")) {
		deleteNodeTask(sr);
		continue task_loop; }
	    if (taskname.equals("delete_subtree")) {
                deleteSubtreeTask(sr);
                continue task_loop; }
	    if (taskname.equals("make_daughter")) {
		makeDaughterTask(sr); 
		continue task_loop; }
	    if (taskname.equals("move_to")) {
		moveToTask(sr);
		continue task_loop; }
	    if (taskname.equals("move_up_node")) {
		moveUpNodeTask(sr);
		continue task_loop; }
	    if (taskname.equals("extend_span")) {
		extendSpanTask(sr);
		continue task_loop; }
	    if (taskname.equals("move_up_nodes")) {
		moveUpNodesTask(sr);
		continue task_loop; }
	    if (taskname.equals("add_internal_node")) {
		addInternalNodeTask(sr);
		continue task_loop; }
	    if (taskname.equals("trace_after")) {
		traceAfterTask(sr);
		continue task_loop; }
	    if (taskname.equals("trace_before")) {
		traceBeforeTask(sr);
		continue task_loop; }
	    if (taskname.equals("co_index")) {
		coIndexTask(sr);
		continue task_loop; }
	    if (taskname.equals("concat")) {
		concatTask(sr);
		continue task_loop; }
	    //System.err.println("task name not recognized:  " + taskname);
	} // end task_loop
	changeable.setConstantNodes();
	//sr.update(changeable);
    } // end method Plain

    private static void unPackOneTask(OneTask ot) {
	taskname = ot.getTaskName();
	querydexes = ot.getQueryDexes();
	newlabel = ot.getNewLabel();
	replace_this = ((Integer)querydexes.elementAt(0)).intValue(); }

    private static boolean notDupe(Node nodal) {
	return (notDupe(nodal.getIndex_int())); }

    private static boolean notDupe(int to_change) {
	Integer changed;

	for (int i = 0; i < changed_dexes.size(); i++) {
	    changed = (Integer)changed_dexes.elementAt(i);
	    if (changed.intValue() == to_change) {
		return false; } }
	changed = new Integer(to_change);
	changed_dexes.addElement(changed);
	return true; }

    private static void labelChangesTask(SentenceResult sr) {
	changed_dexes = new Vector();
	for (i = 0; i < sr.size(); i++) {
	    subb = sr.subResultAt(i);
	    node_to_change = subb.matchAt(replace_this);
	    index_to_change = node_to_change.getIndex_int();
	    if (notDupe(index_to_change)) {
		changeable.ChangeLabel(taskname, newlabel, index_to_change, 
				       true); } }}

    private static void concatTask(SentenceResult sr) {
	changed_dexes = new Vector();
	replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
	for (i = 0; i < sr.size(); i++) {
	    subb = sr.subResultAt(i);
	    node_to_change = subb.matchAt(replace_this);
            node2 = subb.matchAt(replace_this2);
	    index_to_change = node_to_change.getIndex_int();
	    if (notDupe(index_to_change)) {
		changeable.Concat(node_to_change, node2, 
				  newlabel, true); } }}

    private static void addLeafBeforeTask(SentenceResult sr) {
	changed_dexes = new Vector();
	for (i = 0; i < sr.size(); i++) {
	    subb = sr.subResultAt(i);
	    node_to_change = subb.matchAt(replace_this);
	    index_to_change = node_to_change.getIndex_int();
	    if (notDupe(index_to_change)) {
		pos = ot.getPosLabel();
		text = ot.getTextLabel();
		changeable.InsertLeafBefore(pos, text, 
					    index_to_change, true); } }}

    private static void addLeafAfterTask(SentenceResult sr) {
        changed_dexes = new Vector();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            index_to_change = node_to_change.getIndex_int();
            if (notDupe(index_to_change)) {
                pos = ot.getPosLabel();
                text = ot.getTextLabel();
                changeable.InsertLeafAfter(pos, text, index_to_change, true); }
        }}

    private static void traceBeforeTask(SentenceResult sr) {
	String new_trace_dex;

	changed_dexes = new Vector();
        replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
	for (i = 0; i < sr.size(); i++) {
	    new_trace_dex = changeable.getNewLabelDex();
	    subb = sr.subResultAt(i);
	    antecedentTask(subb, new_trace_dex);
	    node_to_change = subb.matchAt(replace_this2);
	    index_to_change = node_to_change.getIndex_int();
	    if (notDupe(index_to_change)) {
		pos = ot.getPosLabel();
		text = ot.getTextLabel() + "-" + new_trace_dex;
		changeable.InsertLeafBefore(pos, text, 
					    index_to_change, true); } }}

    private static void traceAfterTask(SentenceResult sr) {
	String new_trace_dex;

        changed_dexes = new Vector();
        replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
        for (i = 0; i < sr.size(); i++) {
	    new_trace_dex = changeable.getNewLabelDex(); 
            subb = sr.subResultAt(i);
	    antecedentTask(subb, new_trace_dex);
            node_to_change = subb.matchAt(replace_this2);
            index_to_change = node_to_change.getIndex_int();
            if (notDupe(index_to_change)) {
                pos = ot.getPosLabel();
                text = ot.getTextLabel() + "-" + new_trace_dex;
                changeable.InsertLeafAfter(pos, text, index_to_change, true); }
        } }

    private static void antecedentTask(SubResult subb, String new_trace_dex) {
	node_to_change = subb.matchAt(replace_this);
	index_to_change = node_to_change.getIndex_int();
	if (notDupe(index_to_change)) {
	    changeable.ChangeLabel("append_label", "-" + new_trace_dex, 
				   index_to_change, true); } }

    private static void deleteNodeTask(SentenceResult sr) {

	changed_dexes = new Vector();
	for (i = 0; i < sr.size(); i++) {
	    subb = sr.subResultAt(i);
	    node_to_change = subb.matchAt(replace_this);
	    index_to_change = node_to_change.getIndex_int();
	    if (notDupe(index_to_change)) {
		done = changeable.DeleteInNode(index_to_change, true); 
	        if (!done) { CommentStuff(sr, node_to_change); }} }}

    private static void deleteLeafTask(SentenceResult sr) {
        changed_dexes = new Vector();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            index_to_change = node_to_change.getIndex_int();
            if (notDupe(index_to_change)) {
                done = changeable.DeleteLeaf(index_to_change, true);
                if (!done) { CommentStuff(sr, node_to_change); }} }}

    private static void deleteSubtreeTask(SentenceResult sr) {
        changed_dexes = new Vector();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            index_to_change = node_to_change.getIndex_int();
            if (notDupe(index_to_change)) {
                done = changeable.DeleteSubtree(index_to_change, true);
                if (!done) { CommentStuff(sr, node_to_change); }} }}

    private static void makeDaughterTask(SentenceResult sr) {
        changed_dexes = new Vector();
        replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            start = node_to_change.getIndex_int();
            node2 = subb.matchAt(replace_this2);
            finish = node2.getIndex_int();
	    //	    correctOrder();
            if (notDupe(start)) {
                done = changeable.makeDaughter(start, finish, true);
                if (!done) { CommentStuff(sr, node_to_change, node2); }}}}

    private static void moveToTask(SentenceResult sr) {
        changed_dexes = new Vector();
        replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            node2 = subb.matchAt(replace_this2);
            if (notDupe(node_to_change)) {
                done = changeable.MoveTo(node_to_change, node2, true);
                if (!done) { CommentStuff(sr, node_to_change, node2); }} }}

    private static void extendSpanTask(SentenceResult sr) {
        changed_dexes = new Vector();
        replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            node2 = subb.matchAt(replace_this2);
            if (notDupe(node_to_change)) {
                done = changeable.extendSpan(node_to_change, node2, true);
                if (!done) { CommentStuff(sr, node_to_change, node2); }} }}

    private static void coIndexTask(SentenceResult sr) {
        changed_dexes = new Vector();
        replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            node2 = subb.matchAt(replace_this2);
            if (notDupe(node_to_change)) {
                done = changeable.coIndex(node_to_change, node2);
                if (!done) { CommentStuff(sr, node_to_change, node2); }} }}
	
    private static void moveUpNodeTask(SentenceResult sr) {
	changed_dexes = new Vector();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            index_to_change = node_to_change.getIndex_int();
            if (notDupe(index_to_change)) {
                done = changeable.MoveUp(index_to_change, true); 
	        if (!done) { CommentStuff(sr, node_to_change); }} }}

    private static void moveUpNodesTask(SentenceResult sr) {
        changed_dexes = new Vector();
        replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            start = node_to_change.getIndex_int();
            node2 = subb.matchAt(replace_this2);
            finish = node2.getIndex_int();
	    correctOrder();
            if (notDupe(start)) {
                done = changeable.MoveUpMulti(start, finish, true);
                if (!done) { CommentStuff(sr, node_to_change, node2); }} }}

    private static void addInternalNodeTask(SentenceResult sr) {
	changed_dexes = new Vector();
	replace_this2 = ((Integer)querydexes.elementAt(1)).intValue();
        for (i = 0; i < sr.size(); i++) {
            subb = sr.subResultAt(i);
            node_to_change = subb.matchAt(replace_this);
            start = node_to_change.getIndex_int();
	    node2 = subb.matchAt(replace_this2);
	    finish = node2.getIndex_int();
	    correctOrder();
            if (notDupe(start)) {
                done = changeable.AddInternalNode(newlabel, start, 
						  finish, true); 
	        if (!done) { CommentStuff(sr, node_to_change, node2); }} }}

    private static void correctOrder () {
	if (start > finish) {
	    temp = finish;
	    finish = start;
	    start = temp; }
	return; }

    private static void CommentStuff(SentenceResult sr, 
				     Node node_to_change) {
	sr.addComment(makeComment(node_to_change)); }

    private static void CommentStuff(SentenceResult sr, 
				     Node node1, Node node2) {
	sr.addComment(makeComment(node1, node2)); }

    private static String makeComment(Node node_to_change) {
	String comment;
	comment = "WARNING! could not ";
	comment += ot.getCommand() + " ";
	comment += node_to_change.toString();
	return comment; }

    private static String makeComment(Node node1, Node node2) {
	String comment;
	comment = "WARNING! could not ";
	comment += ot.getCommand() + " ";
	comment += node1.toString() + ", ";
	comment += node2.toString();
	return comment; }
} 



