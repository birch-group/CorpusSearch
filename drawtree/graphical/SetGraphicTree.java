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
import search_result.*;

/** sets the coordinates of super class GraphicTree. */
public class SetGraphicTree extends GraphicTree{

    //public void Init() {}

    public int X_BUFFER = 6, Y_BUFFER = 16, Y_BUFF = 24, CHAR_WIDTH = 8;

    public void Init(ChangeTree in_sparse) {
	super.Init(in_sparse);
	this.setTreeCoords(); }

    public void Init(ChangeTree in_sparse, SentenceResult indices) { 
	super.Init(in_sparse, indices); 
        this.setTreeCoords(); }

    public void setTreeCoords () {
	if (super.sparse.isEmpty()) {
	    return; }
	super.partInit();
	//super.calcDepth();
	super.setCollapsedBits();
	//super.setBulletNode();
	if (!(super.lapse).willCollapse()) {
	    bottomUp(); this.setHighlights(); 
	    //this.compress(); 
	    super.resetHeight(); 
	    //super.PrintToSystemErr();
	    return; }
	else {
	    bottomUpCollapse(); this.setHighlights(); 
	    return; } }
   
    private void bottomUpCollapse () {
	int i, x_start, x_end, y_start, y_end;
	int j, k, coll_max_x, end_collapse;
	Node leaf;
	GraphicNode gleaf;

	// set nodes in BitSet collapsed_bits, cor
	super.lapse.setCollapsedBits(super.sparse);
        y_start = super.MIN_HT - 2*Y_BUFFER; 
	y_end = y_start - Y_BUFFER;
	// first, draw text leaves of tree.
	x_start = 10;
	tree_loop:  for (i = 0; i < super.sparse.size(); i++) {
	    leaf = super.sparse.NodeAt(i);
	    if (super.sparse.IsLeafText(leaf)) {
		super.setMaxDepth(leaf);
		if (super.lapse.isCollapsed(i)) {
		    coll_max_x = setCollapsedLeaves(i, x_start, y_start);
		    x_start = coll_max_x + 12*X_BUFFER;
		    // advance i past the collapsed subtree.
		    end_collapse = super.lapse.
			getCollapsedEnd(leaf, super.sparse);
		    i = end_collapse;
		    continue tree_loop; }
		else {
		    x_start = oneLeaf(leaf, x_start, y_start); }}}
	for (k = super.getMaxDepth(); k >= 0; k --) {
	    y_start = y_start - Y_BUFF; 
	    subBottomCollapse(k, y_start); }
	//	super.PrintToSystemErr();
	adjustLeavesCollapse(); }

    private int setCollapsedLeaves(int i, int x_start, int y_start) {
	int coll_x_start, coll_x_end, coll_y_start, coll_y_end;
	int k, j, coll_max_x;
	Node leaf, coll_root;
	GraphicNode gleaf;
	Vector leaves, arrange, gleaves;

	leaves = super.lapse.getCollapsedLeaves(i, super.sparse);
	coll_root = super.lapse.getCollRoot(leaves, super.sparse);
	coll_max_x = x_start + 12*X_BUFFER;
	coll_x_end = coll_max_x;
	coll_y_start = y_start-(Y_BUFFER*(leaves.size()-1));
	for (j = 0; j < leaves.size(); j++) {
	    leaf = (Node)leaves.elementAt(j);
	    super.setMaxDepth(leaf);
	    coll_x_end = myXEnd(leaf, x_start);
	    coll_x_start = x_start;
	    //if (coll_x_end > coll_max_x) {
	    //coll_max_x = coll_x_end; }
	    coll_y_end = coll_y_start - Y_BUFFER;
	    gleaf = new GraphicNode(leaf, coll_x_start, coll_max_x, 
				    coll_y_start, coll_y_end);
	    super.addGraphicNode(gleaf); 
	    //gleaf.PrintToSystemErr();
	    coll_y_start += Y_BUFFER + super.Y_DIFF; }
	return coll_max_x; }

    private void bottomUp () {
	int i, k, x_start, y_start;
	Node leaf;

	y_start = super.MIN_HT - 2*Y_BUFFER; 
	// first, draw text leaves of tree.
	x_start = 20;
	for (i = 0; i < super.sparse.size(); i++) {
	    leaf = super.sparse.NodeAt(i);
	    if (super.sparse.IsLeafText(leaf)) {
		try {
		    super.setMaxDepth(leaf); }
		catch (Exception NullPointerException) {
		    super.Init();
		    super.setMaxDepth(leaf);
		    return; }
		x_start = oneLeaf(leaf, x_start, y_start); }}
	for (k = super.getMaxDepth(); k >= 0; k --) {
	    y_start = y_start - Y_BUFF;
	    subBottom(k, y_start); }
	//	super.PrintToSystemErr();
	adjustLeaves(); }
	    
    private int oneLeaf(Node leaf, int x_start, int y_start) {
	int x_end, new_x_start; 
	GraphicNode gleaf;

	x_end = myXEnd(leaf, x_start);
	gleaf = myGNode(leaf, x_start, y_start);
	super.addGraphicNode(gleaf); 
	new_x_start = x_end + 2*X_BUFFER; 
	return new_x_start; }

    private GraphicNode myGNode(Node leaf, int x_start, int y_start) {
	GraphicNode gleaf;
	int g_x_end, g_y_end;
	
	g_x_end = oneNodeXEnd(leaf, x_start);
	g_y_end = y_start - Y_BUFFER;
	gleaf = new GraphicNode(leaf, x_start, g_x_end, y_start, g_y_end);
	return gleaf; }

    private int myXEnd(Node leaf, int x_start) {
	Node max_node, mom;
	int max_x_end, mom_x_end;

	// adjust for skinny, top-heavy subtrees, e.g. WNP-PRO-4 0.
	max_node = leaf;
	max_x_end = oneNodeXEnd(leaf, x_start);
	try {
	    while (!max_node.IsNullNode() && 
		   super.sparse.isOnlyChild(max_node)) {
		mom = super.sparse.GetMother(max_node);
		mom_x_end = oneNodeXEnd(mom, x_start);
		if (mom_x_end > max_x_end) { max_x_end = mom_x_end; }
		max_node = mom; }}
	catch (Exception e) {}
	finally { return (max_x_end); }}

    private int oneNodeXEnd (Node nodal, int x_start) {
	return (x_start + X_BUFFER + lengthPerFont(nodal.getLabel())); }
  
    private void adjustLeavesCollapse() {
	GraphicNode gnode;
	Node nodal;
	int advance, i;

	tree_loop: for (i = 0; i < super.gt_size(); i++) {
	    gnode = super.graphicNodeAt(i);
	    nodal = gnode.getNode();
	    if (super.sparse.IsLeafText(nodal)) {
		if (super.lapse.isCollapsed(i)) {
		    i += 1;
		    continue tree_loop; }
		adjustOneLeaf(gnode, nodal); }}
	return; }

    private void adjustLeaves() {
	GraphicNode gnode;
	Node nodal;

	for (int i = 0; i < super.gt_size(); i++) {
	    gnode = super.graphicNodeAt(i);
	    nodal = gnode.getNode();
	    if (super.sparse.IsLeafText(nodal)) {
		adjustOneLeaf(gnode, nodal); }}
	return; }

    private void adjustOneLeaf(Node nodal) {
	GraphicNode gnode;
	gnode = super.graphicNodeFor(nodal);
	adjustOneLeaf(gnode, nodal); }

    private void adjustOneLeaf(GraphicNode gnode, Node nodal) {
	GraphicNode gmom;
	Node mom;

	mom = super.sparse.GetMother(nodal);
	gmom = super.graphicNodeFor(mom);
	gnode.setYs(gmom.getYStart() + 2*Y_BUFFER,
		    gmom.getYEnd() + 2*Y_BUFFER); }

    private void subBottom(int depth, int y_start) {
	int i;
	Node depth_node = new Node("NULL");
	Vector layer;

	layer = super.sparse.getAllNodesforDepth(depth);
	layer_loop: for (i = 0; i < layer.size(); i++) {
	    depth_node = (Node)layer.elementAt(i); 
	    if (super.sparse.IsLeafText(depth_node)) {
		continue layer_loop; }
	    setSynNode(depth_node, y_start); } }

    private void setSynNode(Node depth_node, int y_start) {
	GraphicNode gnode1, gnode2, gmom;
	Node first_kid, last_kid;
	Vector depth_daughters;

	depth_daughters = super.sparse.GetDaughters(depth_node);
	first_kid = (Node)depth_daughters.firstElement();
	last_kid = (Node)depth_daughters.lastElement();
	gmom = new GraphicNode(depth_node, y_start, 
			       y_start - Y_BUFFER);
	gnode1 = super.getGNode(first_kid);
	gnode2 = super.getGNode(last_kid);
	setXandY(gmom, gnode1, gnode2, y_start);
	super.addGraphicNode(gmom); }

    private void setSynNodeCollapse (Node depth_node, int y_start) {
	GraphicNode gnode1, gmom;
	Node first_kid;
	Vector depth_daughters;

	depth_daughters = super.sparse.getText(depth_node);
	first_kid = (Node)depth_daughters.firstElement();
	gmom = new GraphicNode(depth_node, y_start, 
			       y_start - Y_BUFFER);
	gnode1 = super.getGNode(first_kid);
	setXandY(gmom, gnode1, gnode1, y_start);
	super.addGraphicNode(gmom); }

    private void subBottomCollapse(int depth, int y_start) {
	int i, j, k, y_end;
	GraphicNode gnode1,gnode2, gmom;
	Node first_kid, last_kid, depth_node = new Node("NULL");
	Vector layer, depth_daughters;

	y_end = y_start - 2*Y_BUFFER;
	layer = super.sparse.getAllNodesforDepth(depth);
	layer_loop: for (i = 0; i < layer.size(); i++) {
	    depth_node = (Node)layer.elementAt(i);
	    // is depth_node the root of a collapsed subtree?
	    if (super.lapse.isCollapseSubRoot(depth_node, super.sparse)) {
		setSynNodeCollapse(depth_node, y_start);
		continue layer_loop; }
	    
	    //	    if (super.sparse.IsLeafText(depth_node) || 
	    //super.lapse.isCollapsed(depth_node.getIndex_int())) { 
	    //continue layer_loop; }
	    if (super.sparse.IsLeafText(depth_node)) { continue layer_loop; }
	    if (super.lapse.isCollapsed(depth_node.getIndex_int())) {
		setSynNode(depth_node, y_start); 
		continue layer_loop; }
	    setSynNode(depth_node, y_start);
	}
    }

    private void setXandY(GraphicNode gmom, GraphicNode gnode1, 
			  GraphicNode gnode2, int y_start) {
	int x_start1, x_end2, x_start_mom, x_end_mom, half;

	x_start1 = gnode1.getXStart();
	x_end2 = gnode2.getXEnd();
	half = lengthPerFont(gmom.getLabel())/2;
	x_start_mom = (x_start1 + x_end2)/2 - half;
	x_end_mom = x_start_mom + lengthPerFont(gmom.getLabel());
	gmom.setXs(x_start_mom, x_end_mom);
	gmom.setYs(y_start, y_start - Y_BUFFER); }

    protected void setHighlights() {               
	int i;
	GraphicNode gnode;

	if (!super.has_indices) { return; }
	nodes_loop: for (i = 0; i < super.gt_size(); i++) {
	    gnode = super.graphicNodeAt(i);
	    if (super.inHighNodes(gnode.getNode())) {
		gnode.setHighlight1(true);
		continue nodes_loop; }
	    if (super.inHighBounds(gnode.getNode())) {
		gnode.setHighlight2(true); } }
	return; }

    protected boolean is2or3Subtree(Node nodal) {
	Vector daughters;
	Node grandd;

	if (super.sparse.IsLeafPOS(nodal) ||
	    super.sparse.IsLeafText(nodal)) {
	    return true; }
	daughters = super.sparse.GetDaughters(nodal);
	if (daughters.size() == 1) {
	    grandd = (Node)daughters.firstElement();
	    if (super.sparse.IsLeafPOS(grandd)) {
		return true; }}
	return false; }
	
 
    protected void compress() {
	int i, behind, how_much;
	Vector daughters;
	Node daughter;

	//first, drop long subtrees.
	daughters = super.sparse.GetDaughters(super.sparse.getRootNode());
	for (i = 0; i < daughters.size(); i++) {
	  daughter = (Node)daughters.elementAt(i);
	  if (!super.sparse.IsLeafPOS(daughter)) {
	      super.shiftDownSubtree(daughter,10*Y_DIFF); }}
	compress_loop: for (i = 1; i < this.gt_size(); i++) {
	    behind = super.xSpaceBehind(i);
	    if (behind < 0) {
		super.shiftRight(i, -behind + (X_DIFF));
		continue compress_loop; }
	    if (super.sparse.IsLeafText(i) ||
		super.sparse.isFirstChild(i)) { 
		continue compress_loop; }
	    if (behind > 2*X_DIFF) {
		super.OKShiftLeft(i, behind); }}
	super.correctRoot((Node)daughters.firstElement(), 
		  (Node)daughters.lastElement());
	return; }

    //protected int lengthPerFont (String label) {
    //int flength;

    //flength = CHAR_WIDTH*label.length();
    //return flength; }

    protected int getMiddleX(GraphicNode gnode) {
 	int midx = 0;

 	midx = gnode.getXStart();
 	try {
 	    midx += lengthPerFont(gnode.getLabel())/2; }
 	catch (NullPointerException npe) {
 	    gnode.PrintToSystemErr(); }
 	return midx; }

    // This is a shameless hack.  If I could get FontMetrics to work, 
    // I wouldn't need this.  I'm assuming the Font is (Dialog, 12, 0).
    protected int lengthPerFont(String label) {
	int i, len = 0;
	char ch;

	for (i = 0; i < label.length(); i++) {
	    ch = label.charAt(i);
	    if (ch == 'i' || ch == 'l' || ch == 'I' || ch == '\'') {
		len += 3;
		continue; }
	    if (ch == 'f' || ch == 'j' || ch == 't' || ch == 'J' || 
		ch == ',' || ch == ':' || ch == ';' || ch == '.' ||
		ch == '!' || ch == '(' || ch == ')' || ch == '{' ||
		ch == '}' || ch == '[' || ch == ']' || ch == '|' ||
		ch == '\"') {
		len += 4;
		continue;}
	    if (ch == 'r' || ch == '?') {
		len += 5; 
		continue;}
	    if (ch == 'c' || ch == 's' || ch == 'v' || ch == 'y' ||
		ch == 'F' || ch == 'L' || ch == 'S' || ch =='*' ||
		ch == '_' || ch == '\\') {
		len += 6;
		continue; }
	    if (ch == 'a' || ch == 'e' || ch == 'f' || ch == 'h' || 
		ch == 'k' || ch == 'n' || ch == 'o' || ch == 'u' ||
		ch == 'x' || ch == 'z' || ch == 'B' || ch == 'E' ||
		ch == 'P' || ch == 'Y' || ch == 'Z' || ch == '-') {  
		len += 7;
		continue; }
	    if (ch == 'b' || ch == 'd' || ch == 'p' || ch == 'q' || 
		ch == 'A' || ch == 'C' || ch == 'K' || ch == 'R' ||
		ch == 'T' || ch == 'U' || ch == 'V' || ch == 'X' ||
		ch == '1' || ch == '2' || ch == '3' || ch == '4' ||
		ch == '5' || ch == '6' || ch == '7' || ch == '8' ||
		ch == '9' || ch == '0' || ch == '#' || ch == '~' ||
		ch == '&' || ch == '$' || ch == '%' || ch == '^' ) {
		len += 8;
		continue; }
	    if (ch == 'w' || ch == 'D' || ch == 'G' || ch == 'H' || 
		ch == 'N' || ch == 'O' || ch == 'Q') {
		len += 9;
		continue; }
	    if (ch == 'M' || ch == 'W' || ch == '=' || ch == '@' ||
		ch == '+' || ch == '<' || ch == '>') {
		len += 10;
		continue; }
	    if (ch == 'm') {
		len += 11;
		continue; }
	    else { 
		len += 8;
		continue; } }
	return (len); }

	
    }




