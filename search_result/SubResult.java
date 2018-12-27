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
import basicinfo.*;

/**
 * stores the result of a search on one boundary node.  Contains:
 <DL><DD>the boundary node<BR>
 <DD>a list of the nodes found that correspond to the searched-for structure.<BR>
 </DL>
 * N.B. The list of nodes is in the exact same order as the search query.
 *  This is vital for resolving same-instance issues, and for the corpus
 * revision code.
 */

public class SubResult {

    private Node nullster = new Node("NULL");
    private Node bound_node;
    private Vector matches; // a Vector of Nodes.
    private BitSet no_dupes;
    private boolean is_null = false;

    public SubResult () {
        this.bound_node = new Node("NULL");
	this.matches = new Vector(); }

    public SubResult (String nullski) {
	if (nullski.equals("NULL")) {
	    this.is_null = true; } }

    public SubResult (Node bound, Node node2, Node node3) {
	this.bound_node = bound;
	this.matches = new Vector();
	this.addMatch(node2);
	this.addMatch(node3); }

    public SubResult (Node bound, Vector matches1, Vector matches2) {
	this.bound_node = bound;
	this.matches = new Vector();
	this.addMatches(matches1);
	this.addMatches(matches2); }

    public SubResult (Node bound) {
	this.bound_node = bound;
	this.matches = new Vector(); }

    public SubResult (Node bound, int num_nulls) {
	Node nodal;
	this.matches = new Vector();
	for (int i = 0; i < num_nulls; i++) {
	    nodal = new Node("NULL");
	    this.addMatch(nodal); }
	this.bound_node = bound; }

    /**
     * I assume subb1 and subb2 have the same boundary node. 
     * But if they matched because one was METAROOT, use
     * the other node.
     */
    public SubResult (SubResult subb1, SubResult subb2) {
	if ((subb1.getBoundary()).IsMETAROOT()) {
	    this.bound_node = subb2.getBoundary(); }
	else {
	    this.bound_node = subb1.getBoundary(); }
	this.matches = new Vector();
	this.addMatches(subb1.getMatches());
	this.addMatches(subb2.getMatches()); }

    public SubResult copy() {
	SubResult sub_res_copy = new SubResult();

	sub_res_copy.setBoundary(this.getBoundary().copy());
	for (int i = 0; i < this.matches.size(); i++) {
	    sub_res_copy.addMatch(this.matchAt(i).copy()); }
	return sub_res_copy; }

    public boolean IsNull() {
	return (this.is_null); }

    public void setBoundary(Node bound) {
	this.bound_node = bound; }

    public int size() {
	int sz = 0;

	try {
	    sz = (this.matches).size(); }
	catch (Exception e) {
	    sz = 0; }
	finally { return sz; }}

    public void addMatch (Node match) {
	(this.matches).addElement(match); }

    public void addMatches (Vector nodes) {
	Node each;
	
	for (int i = 0; i < nodes.size(); i++) {
	    each = (Node)nodes.elementAt(i);
	    this.addMatch(each); } }

    public void addNulls (int n) {
	Node nodal;

	for (int i = 0; i < n; i ++) {
	    nodal = new Node("NULL");
	    this.addMatch(nodal); }
	return; }

    public Node getBoundary() {
	return this.bound_node; }

    public Vector getMatches() {
	return this.matches; }

    public Node matchAt(int n) {
	Node nodal = new Node("NULL");

	try {
	    if (this.matches.size() <= n) { 
		System.err.print("WARNING! cannot access subresult");
		System.err.println(" match at: " + n);
		System.err.println("subresult:  ");
		nodal.PrintNodeVector(matches);
		System.err.println("");
		return nodal; }
	    nodal = (Node)(this.matches).elementAt(n); }
	catch (Exception e) {
	    System.err.println("in search_result/SubResult.java: n: " + n);
	    System.err.println("matches:  ");
	    nodal.PrintNodeVector(matches);
	    System.err.println("");
	    e.printStackTrace(); 
	    Goodbye.SearchExit(); 
	}
	finally { return nodal; } }

    public void setMatch(Node nodal, int k) {
	matches.setElementAt(nodal, k); }


    public boolean sameBound(SubResult other_result) {
	Node other_bound;
	other_bound = other_result.getBoundary();
	if ((this.bound_node).equals(other_bound)) {
	    return true; }
	if ((this.bound_node).IsMETAROOT() ||
	    (other_bound).IsMETAROOT()) {
	    return true; }
	return false; }

    public void setNoDupes() {
	int i, k;
	Node curr_node, prev_node;
	
	no_dupes = new BitSet(this.size());
	i_loop: for (i = 0; i < (this.matches).size(); i++) {
	    curr_node = this.matchAt(i);
	    if (curr_node.IsNullNode()) {
		continue i_loop; }
	    for (k = 0; k < i; k++) {
		prev_node = this.matchAt(k);
		if (curr_node.equals(prev_node)) {
		    continue i_loop; } }
	    no_dupes.set(i); }
	return; }

    public Node NoDupesAt (int i) {
	if (no_dupes.get(i)) {
	    return ((Node)matches.elementAt(i)); }
        return (nullster); }

    public boolean equals(SubResult subb) {
	Node node1, node2;
	if (!((this.getBoundary()).equals(subb.getBoundary()))) 
	    { return false; }
	if (!(this.size() == subb.size()))
	    { return false; }
	for (int i = 0; i < this.size(); i++) {
	    node1 = this.matchAt(i);
	    node2 = subb.matchAt(i);
	    if (!(node1.equals(node2))) { return false; } }
	return true; }

    public void update(ChangeTree changeable) {
	int new_dex;
	Integer old_dex;
	Node old_match, new_match;

	if (!this.bound_node.IsNullNode()) {
	    old_dex = (this.bound_node).getIndex();
	    new_dex = changeable.getUpdate(old_dex);
	    this.bound_node = changeable.NodeAt(new_dex); }
	thru_matches:  for (int i = 0; i < matches.size(); i++) {
	    old_match = (Node)matches.elementAt(i);
	    if (old_match.IsNullNode()) {
		continue thru_matches; }
	    old_dex = old_match.getIndex();
	    new_dex = changeable.getUpdate(old_dex);
	    new_match = changeable.NodeAt(new_dex);
	    matches.setElementAt(new_match, i); }
	return; }

    public void PrintToPrintWriter(PrintWriter outt) {
	Node nodal;

	outt.print(bound_node.toString());
	for (int i = 0; i < this.size(); i++) {
	    nodal = (Node)matches.elementAt(i);
	    outt.print(nodal.toString()); }
	outt.println("");
	return; }

    public void PrintToSystemErr () {
	bound_node.PrintToSystemErr();
	System.err.print(":  ");
	bound_node.PrintNodeVector(matches);
	System.err.println("");
	return; }

}






