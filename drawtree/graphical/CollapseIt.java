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
import java.awt.event.*;
import java.util.*;
import syntree.*;
import basicinfo.*;
import javax.swing.*;

public class CollapseIt {
    // there are two sources of collapsed roots; 
    // 1.) nodes selected by the user
    // 2.) nodes that answer to the list collapse_list.
    private BitSet collapsed_bits;
    private Vector selected_coll_roots, expanded_roots, listed_coll_roots;
    private static boolean collapse, expand_all, expand_selected;
    private static String collapse_str;
    private static ArgList collapse_list;
 
    public CollapseIt() { Init(); }

    public void Init() {
        this.selected_coll_roots = new Vector();
	this.listed_coll_roots = new Vector();
	this.expanded_roots = new Vector();
        this.collapse = false; }

    public void clearCollapsed() {
	this.selected_coll_roots.removeAllElements();
	//this.listed_coll_roots.removeAllElements();
	this.expanded_roots.removeAllElements();
	//this.collapse_list = new ArgList();
	this.collapse_str = "";
	this.collapse = false; }

    public String toString() {
	String str = "";
	Node nodal;

	for (int i = 0; i < selected_coll_roots.size(); i++) {
	    nodal = (Node)selected_coll_roots.elementAt(i);
	    str += nodal.toString() + "  "; }
	if (!collapse_str.equals("")) { str += "[" + collapse_str + "]"; }
	return str; }

    public boolean rootsEmpty() {
	if (selected_coll_roots.isEmpty() &&
		listed_coll_roots.isEmpty()) { return true; }
	return false; }

    public void removeAllCollapsed () {
	selected_coll_roots.removeAllElements();
	if (this.collapse_str.equals("")) {
	    this.collapse = false; }
	return; }

    public void removeAllExpanded() {
	this.expand_selected = false;
	expanded_roots.removeAllElements(); }

    public void expandAll() {
	this.expand_all = true; }

    public void expandAll(boolean t_or_f) {
	this.expand_all = t_or_f; }

    public void expandSelected() {
	this.expand_selected = true; }

    public void setCollapseList(ChangeTree sparse, String to_collapse) {
	this.collapse_str = to_collapse;
	listed_coll_roots.removeAllElements();
	if (to_collapse.equals("")) {
	    this.setCollapsedBits(sparse); return; }
	this.collapse_list = new ArgList(collapse_str);
	this.collapse = true;
        this.getRootsFromList(sparse);
        this.setCollapsedBits(sparse); }

    public String getCollapseStr() { return this.collapse_str; }

    public void setCollapse(boolean t_or_f) {
	if (t_or_f) {
	    expanded_roots.removeAllElements(); }
	this.collapse = t_or_f; }

    public boolean willCollapse() { return collapse; }

    public boolean isCollapsed (int i) { return (collapsed_bits.get(i)); }

    public boolean isCollapsed (Integer dex) {
	return (isCollapsed(dex.intValue())); }

    public void addToCollapsedRoots(ChangeTree sparse, GraphicNode gnode) {
	this.addToCollapsedRoots(sparse, gnode.getNode()); }

    public void addToCollapsedRoots(ChangeTree sparse, Node nodal) {
	Vector ancestors;

	this.collapse = true;
	this.expand_all = false;
	this.expand_selected = false;
	// remove newly collapsed node from expand list.
	removeNodeFromList(nodal, expanded_roots);
        // remove ancestors of newly collapsed node from collapse list.
	ancestors = sparse.GetAncestors(nodal);
	removeList1FromList2(ancestors, selected_coll_roots);
	// prevent duplicate copies of nodal being 
	// added to selected_coll_roots.
	removeNodeFromList(nodal, selected_coll_roots);
	selected_coll_roots.addElement(nodal); }

    public void removeFromCollapsedRoots(GraphicNode gnode) {
	this.removeFromCollapsedRoots(gnode.getNode()); }

    public void removeFromCollapsedRoots(Node nodal) {
	expanded_roots.addElement(nodal); return; }

    public void getRootsFromList(ChangeTree sparse) {
	Node nodal;
	int n;

	if (collapse_list.isEmpty()) { 
	    if (listed_coll_roots.isEmpty()) {
		collapse = false; return; }
	    else { collapse = true; return; }}
	for (n = 0; n < sparse.size(); n++) {
	    nodal = sparse.NodeAt(n);
	    if (collapse_list.hasMatch(nodal)) {
		addNodeToListNoDupes(nodal, listed_coll_roots); } }
	if (listed_coll_roots.isEmpty()) { collapse = false; }
	else { collapse = true; }
	return; }
	    
    public void setCollapsedBits(ChangeTree sparse) {
	int i, j, end_dex;
	Node root, root2, rootn;

	try {
	    collapsed_bits = new BitSet(sparse.size());
	    if (expand_all) { 
		//|| (selected_coll_roots.size() == 0 &&
		//collapse_str.equals(""))) {
		collapse = false; 
		return; }
	    // first, expand.
	    // remove expanded roots from list of selected collapse roots.
	    removeList1FromList2 (expanded_roots, selected_coll_roots);
	    // remove expanded nodes from roots answering to expand list.
	    removeList1FromList2(expanded_roots, listed_coll_roots);
	    // now, collapse.
	    // first, collapse nodes selected by user.
	    if (!expand_selected) {
		for (i = 0; i < selected_coll_roots.size(); i++) {
		    collapse = true;
		    rootn = (Node)selected_coll_roots.elementAt(i);
		    end_dex = sparse.intEndDexAt(rootn.getIndex());
		    for (j = rootn.getIndex_int(); j <= end_dex; j++) { 
			collapsed_bits.set(j); }}}
	    //  collapse nodes answering to list.
	    for (i = 0; i < listed_coll_roots.size(); i++) {
		collapse = true;
		rootn = (Node)listed_coll_roots.elementAt(i);
		end_dex = sparse.intEndDexAt(rootn.getIndex());
		for (j = rootn.getIndex_int(); j <= end_dex; j++) { 
		    collapsed_bits.set(j); } } }
	catch (Exception e) { e.printStackTrace(); }
	finally { //this.PrintToSystemErr(); 
	    return; } }

    public Vector getCollapsedLeaves(int first_dex, ChangeTree sparse) {
	int j, root_dex, end_dex;
	Vector coll_leaves = new Vector();
	Node sub_root, prev_root, in_leaf;

	if (!this.collapse) { return coll_leaves; }
	in_leaf = sparse.NodeAt(first_dex);
	sub_root = sparse.GetMother(in_leaf);
	prev_root = sub_root;
	get_sub_root: while (!sub_root.IsNullNode()) {
	    root_dex = sub_root.getIndex_int();
	    if (!collapsed_bits.get(root_dex)) {
		sub_root = prev_root;
		break get_sub_root; }
	    prev_root = sub_root;
	    sub_root = sparse.GetMother(prev_root); }
	end_dex = sparse.intEndDexAt(sub_root.getIndex_int());    
	for (j = first_dex; j <= end_dex; j++) {
	    if (sparse.IsLeafText(j)) {
		coll_leaves.addElement(sparse.NodeAt(j)); }
	}
	return coll_leaves; }

    public Node getCollRoot(Vector leaves) {
	return (getCollRoot(leaves, CorpusDraw.currTree().getChangeTree())); }

    public Node getCollRoot(Vector leaves, ChangeTree sparse) {
	Node first = new Node("NULL"), last = new Node("NULL");
	Node common = new Node("NULL");

	if (leaves.isEmpty()) { return common; }
	try {
	    first = (Node)leaves.firstElement();
	    last = (Node)leaves.lastElement();
	    common = sparse.GetCommonAncestor(first, last); }
	catch (Exception e) { 
	    e.printStackTrace();
	    System.err.println("first:  " + first.toString());
	    System.err.println("last:  " + last.toString());}
	finally { return (common); }}

    public Node getCollapseSubRoot(Node leaf) {
	return (getCollapseSubRoot(leaf, 
				   CorpusDraw.currTree().getChangeTree())); }

    public Node getCollapseSubRoot(Node leaf, ChangeTree sparse) {
	Node mom, prev_mom;

	mom = sparse.GetMother(leaf);
	prev_mom = mom;
	while (collapsed_bits.get(mom.getIndex_int())) {
	    prev_mom = mom;
	    mom = sparse.GetMother(mom); }
	return prev_mom; }

    public boolean isCollapseSubRoot(Node coll) {
	return (isCollapseSubRoot(coll, 
				  CorpusDraw.currTree().getChangeTree())); }

    public boolean isCollapseSubRoot(Node coll, ChangeTree sparse) {
	// coll is the root of a collapsed sub tree if it is collapsed,
	// but its mother is not.

	Node mom = sparse.GetMother(coll);
	//	if (sparse.IsLeafPOS(coll)) {
	//  return false; }
	if (collapsed_bits.get(coll.getIndex_int()) &&
	    !collapsed_bits.get(mom.getIndex_int())) {
	    return true; }
	return false; }

    public int getCollapsedEnd(Node leaf) {
	return (getCollapsedEnd(leaf, 
				CorpusDraw.currTree().getChangeTree())); }

    public int getCollapsedEnd (Node leaf, ChangeTree sparse) {
	Node mom;
	int coll_end;

	mom = getCollapseSubRoot(leaf, sparse);
	coll_end = sparse.intEndDexAt(mom.getIndex_int());
	return coll_end; }

    public void PrintToSystemErr() {
	Node nodal;

	System.err.print("collapse:  " + collapse +",  ");
	System.err.println("expand_all:  " + expand_all);
	PrintNodesVectorToSystemErr(selected_coll_roots, 
				    "selected_coll_roots");
	PrintNodesVectorToSystemErr(listed_coll_roots, 
				    "listed_coll_roots");
	PrintNodesVectorToSystemErr(expanded_roots, 
				    "expanded_roots");
    }

    public void PrintNodesVectorToSystemErr(Vector nodes_vector, 
					    String my_name) {
	Node nodal;

	System.err.println(my_name + ": ");
	System.err.print("  ");
	for (int i = 0; i < nodes_vector.size(); i++) {
	    nodal = (Node)nodes_vector.elementAt(i);
	    System.err.print(nodal.toString());
	    if (i < nodes_vector.size() - 1) { 
		System.err.print(", "); }}
	if (nodes_vector.isEmpty()) { System.err.print("EMPTY"); }
	System.err.println(""); }

    public void removeList1FromList2 (Vector list1, Vector list2) {
	int i, j;
	Node root1, root2;

	if (list1.isEmpty() || list2.isEmpty()) { return; }
	for (i = 0; i < list1.size(); i++) {
	    root1 = (Node)list1.elementAt(i);
	    for (j = 0; j < list2.size(); j++) {
		    root2 = (Node)list2.elementAt(j);
		    if (root1.equals(root2)) {
			list2.removeElementAt(j); } } } return; }
    

    public void removeNodeFromList (Node nodal, Vector list1) {
	int i;
	Node root1;

	if (list1.isEmpty()) { return; }
	for (i = 0; i < list1.size(); i++) {
	    root1 = (Node)list1.elementAt(i);
	    if (root1.equals(nodal)) {
		list1.removeElementAt(i); } } return; }
    
    public void addNodeToListNoDupes (Node nodal, Vector list1) {
	int i;
	Node root1;

	if (list1.isEmpty()) { list1.addElement(nodal); return; }
	for (i = 0; i < list1.size(); i++) {
	    root1 = (Node)list1.elementAt(i);
	    if (root1.equals(nodal)) { return; }}
	list1.addElement(nodal); }
}
