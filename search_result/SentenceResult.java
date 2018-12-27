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

package search_result;

import java.io.*;
import java.util.*;
import syntree.*;

/**
 * Handles the results of a search on one sentence.
 * Essentially, this is a list of SubResults (one per boundary node).
 */

public class SentenceResult {

    private BitSet no_dupes;
    private boolean no_dupes_set;
    private Vector results;
    private Vector comments; 
    private SubResult nullster = new SubResult("NULL");

    public SentenceResult () {
	this.results = new Vector(); 
        this.comments = new Vector();
        no_dupes_set = false; }

    public SentenceResult copy() {
	SentenceResult sent_res_copy = new SentenceResult();
	SubResult one_sub;

	for (int i = 0; i < this.size(); i++) {
	    one_sub = this.subResultAt(i);
	    sent_res_copy.addSubResult(one_sub.copy()); }
	return sent_res_copy; }

    public void addComment (String comment) {
	(this.comments).addElement(comment); }

    public int commentsSize() {
	return((this.comments).size()); }

    public String getComment(int i) {
	return ((String)(this.comments).elementAt(i)); }

    public void addSubResult (SubResult per_bound) {
	(this.results).addElement(per_bound); }

    public void addSubResult (Node bound, Node node1, Node node2) {
	SubResult subb = new SubResult(bound, node1, node2);
	this.addSubResult(subb); }

    public void addSubResult(Node bound, Vector matches1, Vector matches2) {
	SubResult subb = new SubResult(bound, matches1, matches2);
	this.addSubResult(subb); }

    public void addNullstoSub (int index, int num_nulls){
	(this.subResultAt(index)).addNulls(num_nulls); }

    public void addTwoSubs(SubResult sub1, SubResult sub2) {

	this.addSubResult(new SubResult(sub1, sub2));
	return; }

    public void addSentenceResult(SentenceResult sr) {
	SubResult subb;
	for (int j = 0; j < sr.size(); j++) {
	    subb = sr.subResultAt(j);
	    this.addSubResult(subb); }
	return; }

    public void rmvSubResult(int i) {
	try {
	    (this.results).remove(i); }
	catch (Exception e) {
	    System.err.println("in SentenceResult.rmvSubResult(): ");
	    e.printStackTrace(); } }

    public boolean isEmpty() {
	return (results.isEmpty()); }

    public int size() {
	return ((this.results).size()); }

    public Vector getResult() {
	return this.results; }

    public SubResult subResultAt(int n) {
	return ((SubResult)(this.results).elementAt(n)); }

    public boolean hasSubResultAt(int n) {
	return (n < this.size()); }


    /**
     * Counts number of distinct boundary nodes 
     * contained in this SentenceResult.  This number is 
     * reported as "hits".
    */
    public int CountHits () {
        Vector distinct;
        int i, j;
        Integer node_dex, old_dex;
        Node nodal;
        SubResult sub_res;

        distinct = new Vector();
	count_loop: for (i = 0; i < this.size(); i++) {
            sub_res = this.subResultAt(i);
            nodal = sub_res.getBoundary();
            node_dex = nodal.getIndex();
            if (distinct.isEmpty()) {
                distinct.addElement(node_dex);
                continue count_loop; }
            for (j = 0; j < distinct.size(); j++) {
                old_dex = (Integer)distinct.elementAt(j);
                if (node_dex.equals(old_dex)) {
                    continue count_loop; }
            } // end for j = 0; j < distinct.size(); j++
            distinct.addElement(node_dex);
        } // end res_vec_loop: for i = 0; i < res_vec.size(); i++
        return (distinct.size());
    } // end method CountHits

    public void setNoDupes() {
	int i, j;
	SubResult sublet1, sublet2;

	no_dupes = new BitSet(this.size());
	for (i = 0; i < this.size(); i++) {
	    sublet1 = this.subResultAt(i);
	    sublet1.setNoDupes(); }
	sub_loop:  for (i = 0; i < this.size(); i++) {
	    for (j = i+1; j < this.size(); j++) {
		sublet1 = this.subResultAt(i);
		sublet2 = this.subResultAt(j);
		if (sublet1.equals(sublet2)) {
		    continue sub_loop; } } 
	    no_dupes.set(i); }
	this.no_dupes_set = true;
	return; }

    public SubResult NoDupesAt(int i) {

	if (no_dupes.get(i)) {
	    return (this.subResultAt(i)); }
        return (nullster); }

    public boolean containsNode(Node in_nodal) {
	 int j, k;
	 SubResult subble;
	 Node boundary, nodal;

	if (!this.no_dupes_set) {
	    this.setNoDupes(); }
        thru_result: for (j = 0; j < this.size(); j++) {
            subble = this.NoDupesAt(j);
	    boundary = subble.getBoundary();
	    if (in_nodal.equals(boundary)) {
		return true; }
            thru_sub: for (k = 0; k < subble.size(); k++) {
                nodal = subble.NoDupesAt(k);
		if (in_nodal.equals(nodal)) {
		    return true; } } }
	return false; }


     /** returns a Vector containing a list of non_boundary nodes contained in
	the result, with no duplicates. */
    public Vector getNodeList() {
	Vector list = new Vector();
	int j, k;
	Node nodal;
	SubResult subble;

	if (!this.no_dupes_set) {
	    this.setNoDupes(); }
        thru_result: for (j = 0; j < this.size(); j++) {
            subble = this.NoDupesAt(j);
            thru_sub: for (k = 0; k < subble.size(); k++) {
                nodal = subble.NoDupesAt(k);
		list.addElement(nodal); } }
	return list; }
 
     /** returns a Vector containing a list of boundary nodes contained in
	the result, with no duplicates. */
    public Vector getBoundList() {
	Vector list = new Vector();
	int j, k;
	Node boundary, nodal;
	SubResult subble; 

	try {
	    //this.PrintToSystemErr();
	    if (!this.no_dupes_set) {
		this.setNoDupes(); }
	    thru_result: for (j = 0; j < this.size(); j++) {
		subble = this.NoDupesAt(j);
		if (subble.IsNull()) { continue thru_result; }
		boundary = subble.getBoundary();
		if (boundary.IsNullNode()) { continue thru_result; }
		if (list.size() == 0) {
		    list.addElement(boundary); 
		    continue thru_result; }
		thru_list: for (k = 0; k < list.size(); k++) {
		    nodal = (Node)list.elementAt(k);
		    if (nodal.IsNullNode()) { continue thru_result; }
		    if (boundary.equals(nodal)) {
			continue thru_result; } }
		list.addElement(boundary); } } 
	catch(Exception e) {
	    e.printStackTrace();
	    return list; }
	finally { return list; }}

    public void rmNode(Node to_rmv) {
	Node nullski = new Node("NULL"), boundary, nodal;
	int i, j;
	SubResult subble;

	for (i = 0; i < this.size(); i++) {
	    subble = this.subResultAt(i);
	    boundary = subble.getBoundary();
	    if (boundary.equals(to_rmv)) {
		subble.setBoundary(nullski); }
	    for (j = 0; j < subble.size(); j++) {
		nodal = subble.matchAt(j);
		if (nodal.equals(to_rmv)) {
		    subble.setMatch(nullski, j); }}}
	return; }


    public void update(ChangeTree changeable) {
	SubResult subb;

	for (int i = 0; i < this.size(); i++) {
	    subb = this.subResultAt(i);
	    subb.update(changeable); }
	return; }

    public int maxSubSize() {
	int max = 0, i;
	SubResult subb;

	for (i = 0; i < this.size(); i++) {
	    subb = this.subResultAt(i);
	    if (subb.size() > max) {
		max = subb.size(); } }
	return max; }

    /**
     * ensures that all SubResults have same size by
     * padding out with NULL nodes.  Used by NOT.
     */
    public void adjustSubSize() {
	SubResult subb;
	int max, i;

	max = this.maxSubSize();
	for (i = 0; i < this.size(); i++) {
	    subb = this.subResultAt(i);
	    if (subb.size() < max) {
		subb.addNulls(max - subb.size()); } }
	return; }

    public void PrintToPrintWriter(PrintWriter outt) {
	SubResult subb;
	outt.println("/*");
	for (int i = 0; i < this.size(); i++) {
	    subb = this.subResultAt(i);
	    subb.PrintToPrintWriter(outt); }
	outt.println("*/");
	return; }

    public void PrintToSystemErr () {
	int i;
	SubResult each;
	if (results.isEmpty()) {
	    System.err.println("[]"); 
	    return; }
	for (i = 0;i < results.size(); i++) {
	    each = (SubResult)results.elementAt(i);
	    System.err.print(i + ".  ");
	    each.PrintToSystemErr(); }
	return; }

}







