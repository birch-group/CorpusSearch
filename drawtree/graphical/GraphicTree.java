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
import search_result.*;

public class GraphicTree {

    protected ChangeTree sparse;
    protected FontMetrics font_met;
    protected Stack undoing, redoing;
    protected Vector selected, tree_list, high_bounds, high_nodes;
    protected ActionRecord actor;
    protected SentenceResult indices;
    protected boolean has_indices = false, just_inits, has_lapse;
    protected Node bullet_node; // node to put a bullet by.
    protected int max_depth, X_DIFF, Y_DIFF, opt_height, opt_width;
    protected int MIN_HT, MIN_WDTH, FONT_SZ = 12;
    protected int word_scroll_ht, word_scroll_wdth;
    protected CollapseIt lapse;

    public GraphicTree() { lapse = new CollapseIt(); Init(); }

    public GraphicTree(ChangeTree in_sparse) {
	lapse = new CollapseIt();
	Init(in_sparse); }

    public GraphicTree(ChangeTree ct, SentenceResult dices) {
	lapse = new CollapseIt();
	Init(ct, dices); }

    public void Init(ChangeTree in_sparse, SentenceResult dices) {
	this.setIndices(dices);
	Init(in_sparse); }

    public void Init(ChangeTree in_sparse) {
	this.sparse = in_sparse;
	Init(); }
    
    public void Init() {
 	tree_list = new Vector();
        selected = new Vector();
 	undoing = new Stack();
	redoing = new Stack();
	setBulletNode();
        max_depth = 0;
        X_DIFF = 4; Y_DIFF = 6; 
        just_inits = false;
	MIN_HT = CorpusDraw.MIN_HT;
	MIN_WDTH = CorpusDraw.MIN_WDTH;
        opt_height = MIN_HT;
        opt_width = MIN_WDTH; }


    //public int canvasLengthPerFont(String label) {
    //return (super.canvasLengthPerFont(label)); }

    public void partInit() {
 	tree_list.removeAllElements();
        //selected.removeAllElements();
 	//undoing = new Stack();
	//redoing = new Stack();
	setBulletNode();
        max_depth = 0;
        X_DIFF = 4; Y_DIFF = 6; 
        just_inits = false;
	MIN_HT = CorpusDraw.MIN_HT;
	MIN_WDTH = CorpusDraw.MIN_WDTH;
        opt_height = MIN_HT;
        opt_width = MIN_WDTH; }

    public void setCollapsedBits() { lapse.setCollapsedBits(sparse); }

    public void shrink() {
	if (this.X_DIFF > 0) {
	    X_DIFF -= 1; 
	    if (this.X_DIFF == 0) {
		just_inits = true;} }
	if (this.Y_DIFF > 3) {
	    Y_DIFF -= 1; } }

    public void swell() {
	X_DIFF += 1;
	if (X_DIFF > 0) { just_inits = false; }
	Y_DIFF += 1; }

    public int get_X_DIFF() {
	return X_DIFF; }

    public int get_Y_DIFF() {
	return Y_DIFF; }

    public boolean justInits() {
	return just_inits; }
			       
    public void resetHeight() {
	GraphicNode gnode, groot;
	Node root;
	int factor, root_end, new_y_start, new_y_end;

	root = sparse.getRootNode();
	groot = this.graphicNodeFor(root);
	root_end = groot.getYEnd();
	this.opt_width = this.calcOptWidth();
	this.opt_height = this.calcOptHeight();
	if (root_end > 0) { 
	    return; }
	// root_start is guaranteed to be <= 0.
	factor = 0 - root_end + 10;
	for (int i = 0; i < this.gt_size(); i ++) {
	    gnode = this.graphicNodeAt(i);
	    new_y_start = gnode.getYStart() + factor;
	    gnode.setYStart(new_y_start);
	    new_y_end = gnode.getYEnd() + factor;
	    gnode.setYEnd(new_y_end); }}

    private int calcOptHeight() {
	GraphicNode groot, gnode;
	Node root;
	int max_y = 0, root_y, j, my_ht;

	root = sparse.getRootNode();
	groot = this.graphicNodeFor(root);
	root_y = groot.getYEnd();
	for (j = 0; j < this.gt_size(); j++) {
	    gnode = this.graphicNodeAt(j);
	    if (gnode.getYStart() > max_y) {
		max_y = gnode.getYStart(); } }
	//my_ht = max_y - root_y;
	my_ht = max_y; 
	if (root_y < 0) {
	    my_ht -= root_y; }
	my_ht += 100 + 12*Y_DIFF;
	if (my_ht < MIN_HT) { return MIN_HT; }
	return my_ht;}
    
    private int calcOptWidth() {
	GraphicNode glast;
	int last_x;

	glast = this.graphicNodeAt(this.gt_size() - 1);
	last_x = glast.getXEnd() + 20;
	if (last_x < MIN_WDTH) { return MIN_WDTH; }
	return last_x;}

    public int getOptHeight() {
	return this.opt_height; }

    public int getOptWidth() {
	return this.opt_width; }

    public int getScrollHt() {
	GraphicNode groot;
	Node root;
	int scroll_height, frame_height;

	root = sparse.getRootNode();
	groot = this.graphicNodeFor(root);
	scroll_height = groot.getYStart();
	scroll_height -= 2*FONT_SZ;
	frame_height = CorpusDraw.toole.getFrameHt();
	if (scroll_height - 2*FONT_SZ > frame_height) {
	    shiftUp(groot.getYStart() - 4*FONT_SZ);
	    scroll_height = 2*FONT_SZ; }
	if (scroll_height < 0) { scroll_height = 0; }
	return scroll_height; }

    protected void shiftUp(int factor) {
	shiftUp(0, factor); }

    protected void shiftUp(int from_where, int factor) {
	GraphicNode gnode;

	for (int i = from_where; i < this.gt_size(); i ++) {
	    gnode = this.graphicNodeAt(i);
	    gnode.shiftUp(factor); } }

    protected void shiftDown(int factor) {
	shiftUp(0, factor); }

    protected void shiftDown(int from_where, int factor) {
	GraphicNode gnode;
	
	for (int i = from_where; i < this.gt_size(); i ++) {
	    gnode = this.graphicNodeAt(i);
	    gnode.shiftDown(factor); } }

    protected void shiftDownSubtree(Node sub_root, int factor) {
	shiftDownSubtree(sub_root.getIndex_int(), factor); }

    protected void shiftDownSubtree(int dex, int factor) {
	int i;
	Node descend;
	GraphicNode gdescend;
	Vector descendants;

	gdescend = this.graphicNodeAt(dex);
	gdescend.shiftDown(factor);
	descendants = sparse.GetDescendants(dex);
	for (i = 0; i < descendants.size(); i++) {
	    descend = (Node)descendants.elementAt(i);
	    gdescend = this.graphicNodeFor(descend);
	    gdescend.shiftDown(factor); }}

    protected void shiftDown(int from_where, int to_where, int factor) {
	int i;
	GraphicNode gnode;

	for (i = from_where; i < to_where; i++) {
	    gnode = this.graphicNodeAt(i);
	    gnode.shiftDown(factor); } }

    protected void shiftRight(int factor) {
	shiftRight(0, factor); }

    protected void shiftRight(int from_where, int factor) {
	int i;
	GraphicNode gnode;

	for (i = from_where; i < this.gt_size(); i ++) {
	    gnode = this.graphicNodeAt(i);
	    gnode.shiftRight(factor); } }

    protected void shiftLeft(int factor) {
	shiftLeft(0, factor); }

    protected void shiftLeft(int from_where, int factor) {
	shiftLeft(from_where, this.gt_size(), factor); }

    protected void shiftLeft(int from_where, int to_where, int factor) {
	int i;
	GraphicNode gnode;

	for (i = from_where; i < to_where; i ++) {
	    gnode = this.graphicNodeAt(i);
	    gnode.shiftLeft(factor); } }

    protected void shiftLeftSubtree(int from_where, int factor) {
	int i, last_dex;
	GraphicNode gnode;
	Node sub_root, last;

	sub_root = sparse.NodeAt(from_where);
	last = sparse.LastDaughter(sub_root);
	last_dex = last.getIndex_int();
	shiftLeft(from_where, last_dex + 1, factor); }

    public int getScrollWidth() {
	GraphicNode groot;
	Node root;
	int scroll_width, factor;

	root = sparse.getRootNode();
	groot = this.graphicNodeFor(root);
	factor = CorpusDraw.toole.getFrameWdth()/2;
	scroll_width = groot.getXStart();
	scroll_width -= factor;
	if (scroll_width < 0) { 
	    shiftRight(factor); 
	    scroll_width += CorpusDraw.toole.getFrameWdth()/2; } 
	return (scroll_width); }


    public GraphicNode gNodeForWordDex(int word_dex, String select_word) {
	Node text_node;
	GraphicNode text_gnode; 

	text_node = correctNode(word_dex, select_word);
	text_gnode = this.graphicNodeFor(text_node);
	return text_gnode; }

    private Node correctNode(int word_dex, String select_word) {
	Vector scroll_nodes;
	Node text_node;
	int j;

	scroll_nodes = MyEvents.urt.toScrollVec(sparse);
	if (word_dex < 0) { word_dex = 0; }
	if (word_dex > scroll_nodes.size() - 1) {
	    word_dex = scroll_nodes.size() - 1; }
	text_node = (Node)scroll_nodes.elementAt(word_dex);
	for (j = word_dex; j < scroll_nodes.size(); j++) {
	    text_node = (Node)scroll_nodes.elementAt(j);
	    if (text_node.getLabel().equals(select_word.trim())) {
		return text_node; } }
	return((Node)scroll_nodes.elementAt(word_dex)); }

    public Node setScrollWordDex(int word_dex, String select_word) {
	GraphicNode gnode;

	gnode = gNodeForWordDex(word_dex, select_word);
	return(setScrollWordDex(gnode)); }

    public Node setScrollWordDex(GraphicNode gnode) {
	word_scroll_ht = gnode.getYEnd();
	word_scroll_ht -= CorpusDraw.toole.getFrameHt()/2;
	//word_scroll_ht += 22;
	//System.err.println("word_scroll_ht:  " + word_scroll_ht);
	//if (word_scroll_ht >= 110) {
	//  word_scroll_ht -= 100; }
	word_scroll_wdth = gnode.getXStart() - 4*FONT_SZ;
	word_scroll_wdth -= CorpusDraw.toole.getFrameWdth()/2;
	if (word_scroll_wdth < 0) {
	    word_scroll_wdth = 0; }
	return(gnode.getNode()); }

    public int getWordScrollHt() {
	return word_scroll_ht; }

    public int getWordScrollWdth() {
	return word_scroll_wdth; }

    public void decNumID() {
	sparse.setNumID(sparse.getNumID() - 1); }

    public void setNumID(int dex) {
	sparse.setNumID(dex); }

    public int getNumID() {
	return sparse.getNumID(); }

    public boolean selectIsEmpty() {
	return (selected.isEmpty()); }

    public void setBulletNode() {
	try {
	    if (has_indices) {
		bullet_node = (Node)high_bounds.firstElement();
		return; }
	    bullet_node = this.sparse.getRootNode(); }
        catch (Exception e) {
	    bullet_node = new Node("NULL"); }
	finally {
	    //System.err.println("bullet_node:  " + bullet_node.toString());
	    return; } }

    public void setBulletNode(Node nodal) {
	this.bullet_node = nodal;
        return; }

    public Node getBulletNode() {
	return this.bullet_node; }

    public GraphicNode getGraphicBulletNode() {
	return (this.graphicNodeFor(this.bullet_node)); }

    public boolean getsBullet(GraphicNode gnode) {
	return (getsBullet(gnode.getNode()));  }

    public boolean getsBullet(Node nodal) {
	return (this.bullet_node.equals(nodal)); }

    public void moveBulletDown() {
	Vector daughters;
	int middle;

	daughters = sparse.GetDaughters(this.bullet_node);
	if (daughters.isEmpty()) { return; }
	if (daughters.size() == 1) {
	    this.bullet_node = (Node)daughters.firstElement(); return; }
	middle = daughters.size()/2;
	this.bullet_node = (Node)daughters.elementAt(middle); }

    public void moveBulletUp() {
	Node mother;

	if (this.bullet_node.equals(sparse.getRootNode())) {
	    return; }
	mother = sparse.GetMother(this.bullet_node);
	if (!mother.IsNullNode()) {
	    this.bullet_node = mother; } }

    public void moveBulletRight() {
	Vector y_vector;
	int i;
	GraphicNode gnode;

	y_vector = this.getNodesforYStart(this.bullet_node);
	for (i = 0; i < y_vector.size(); i++) {
	    gnode = (GraphicNode)y_vector.elementAt(i);
	    if (gnode.getNode().equals(this.bullet_node)) {
		if (i == y_vector.size() - 1) { return; }
		gnode = (GraphicNode)y_vector.elementAt(i + 1);
		this.bullet_node = gnode.getNode();
		return; } }
	return; }

    public void moveBulletLeft() {
	Vector y_vector;
	int i;
	GraphicNode gnode;

	y_vector = this.getNodesforYStart(this.bullet_node);
	for (i = 0; i < y_vector.size(); i++) {
	    gnode = (GraphicNode)y_vector.elementAt(i);
	    if (gnode.getNode().equals(this.bullet_node)) {
		if (i == 0) { return; }
		gnode = (GraphicNode)y_vector.elementAt(i - 1);
		this.bullet_node = gnode.getNode();
		return; } }
	return; }

    public Vector getNodesforYStart(Node nodal) {
	return (getNodesforYStart(this.graphicNodeFor(nodal))); }

    public Vector getNodesforYStart(GraphicNode gnode) {
	return (getNodesforYStart(gnode.getYStart())); }

    public Vector getNodesforYStart(int y_start) {
	GraphicNode gnoid;
	Vector y_nodes = new Vector();

	for (int i = 0; i < this.gt_size(); i++) {
	    gnoid = this.graphicNodeAt(i);
	    if (gnoid.getYStart() == y_start) {
		y_nodes.addElement(gnoid); }}
	return y_nodes; }

    public boolean isEmpty() {
	return (tree_list.isEmpty()); }

    public void setIndices(SentenceResult dices) {
	if (dices.isEmpty()) { return; }
	this.indices = dices; 
        this.has_indices = true; 
        this.high_bounds = dices.getBoundList(); 
        this.high_nodes = dices.getNodeList(); }
    //        setBulletNode((Node)high_bounds.firstElement()); } 

    public void updateIndices() {
	if (!has_indices) { return; }
	this.indices.update(this.sparse);
	this.high_bounds = this.indices.getBoundList();
	this.high_nodes = this.indices.getNodeList(); }
    //        setBulletNode((Node)high_bounds.firstElement()); }

    public boolean inHighBounds(Node nodal) {
	return(this.inList(this.high_bounds, nodal)); }

    public boolean inHighNodes(Node nodal) {
	return(this.inList(this.high_nodes, nodal)); }

    public boolean inList(Vector listt, Node nodal) {
	Node old_node;

	for (int i = 0; i < listt.size(); i++) {
	    old_node = (Node)listt.elementAt(i);
	    if (old_node.equals(nodal)) {
		return true; } }
	return false; }

    public SentenceResult getIndices() {
	return this.indices; }

    public boolean hasIndices() {
	return has_indices; }

    public String getShortFileName() {
	String short_name, gt_file_name;
	int last_dex;

	try {
	gt_file_name = sparse.getFileName();
	last_dex = gt_file_name.lastIndexOf("/");
	if (last_dex < 0 ) {
	    return gt_file_name; }
	short_name = gt_file_name.substring(last_dex + 1); }
	catch (Exception e) { short_name = "NO_FILE_NAME_FOUND"; }
	return short_name; }

    public String getFileName() {
	return sparse.getFileName(); }

    public void setFileName(String fname) {
	sparse.setFileName(fname);
	return; }

    public GraphicNode graphicNodeFor(Node nodal) {
	return (this.graphicNodeAt(nodal.getIndex())); }
  
    public GraphicNode graphicNodeAt(Integer dex) {
	return (this.graphicNodeAt(dex.intValue())); }

    public GraphicNode graphicNodeAt(int i) {
	GraphicNode att = new GraphicNode("NULL");
	
	try { att = (GraphicNode)tree_list.elementAt(i); }
	catch (Exception e) {
	    //System.err.print("in GraphicTree.graphicNodeAt:  ");
	    //System.err.println("i, gt_size():  " + i + ", " + gt_size());
	    //this.PrintToSystemErr(gt_size() - 10, gt_size());
	}
	finally { return att; } }

    public Node NodeAt(int i) {
	GraphicNode gn = this.graphicNodeAt(i);
	return(gn.getNode()); }

    public boolean IsLeafText(Node noid) {
	return(this.sparse.IsLeafText(noid)); }

    public void reset() {
	this.sparse.changesInit();
	tree_list.removeAllElements();
	selected = new Vector(); }

    public void removeAllElements() {
	tree_list.removeAllElements();
	this.sparse.removeAllElements();
	selected.removeAllElements();
	undoing.removeAllElements();
	redoing.removeAllElements();
	has_indices = false;
	max_depth = 0; }

    public void calcDepth() {
	int d;
	max_depth = 0;
	d = sparse.getSubtreeDepth(sparse.getRootNode());
	setMaxDepth(d); }

    public void setMaxDepth(int depth) {
 	if (depth > max_depth) {
 	    max_depth = depth; } }

    public void setMaxDepth(Node leaf) {
 	int depth = this.sparse.getDepth(leaf);
 	setMaxDepth(depth); }

    public int getMaxDepth() {
 	return max_depth; }

    public void addGraphicNode(GraphicNode gnode) {
	int dex, i;
	GraphicNode new_gnode;

	dex = gnode.getNode().getIndex_int();
	if (dex >= tree_list.size()) {
	    for (i = tree_list.size(); i <= dex; i++) {
		new_gnode = new GraphicNode("NULL"); 
		this.tree_list.addElement(new_gnode); }}
	this.tree_list.setElementAt(gnode, dex); }
	
    public ChangeTree getChangeTree() {
 	return sparse; }

    public void addToSelected(GraphicNode gnode) {
 	selected.addElement(gnode); }

    public void removeFromSelected(GraphicNode gnode) {
	GraphicNode selgNode; int i;

	for (i = 0; i < selected.size(); i++) {
	    selgNode = (GraphicNode)selected.elementAt(i);
	    if (selgNode.equals(gnode)) {
		selected.removeElementAt(i); }}}

    public void clearSelected() { selected.removeAllElements(); }

    public boolean isSelected(GraphicNode grunt) {
	GraphicNode gnode; int i;
	
	for (i = 0; i < selected.size(); i++) {
	    gnode = (GraphicNode)selected.elementAt(i);
	    if (gnode.equals(grunt)) { return true; }}
	return false; }

    public void toggleSelected(GraphicNode grunt) {
	GraphicNode gnode; int i;

	if (this.isSelected(grunt)) { this.removeFromSelected(grunt); }
	else { this.addToSelected(grunt); } }

    public GraphicNode peekSelected() {
	if (selected.isEmpty()) {
	    return (new GraphicNode("NULL")); }
	return((GraphicNode)selected.lastElement()); }

    public GraphicNode popSelected() {
	GraphicNode grunt = new GraphicNode("NULL");

 	if (selected.isEmpty()) { return grunt; }
	grunt = (GraphicNode)selected.lastElement();
	selected.removeElementAt(selected.size() - 1);
	return (grunt); }

    public GraphicNode getSelected (int i) {
	if (i < 0 || i >= selected.size()) {
	    return (new GraphicNode("NULL")); }
	return ((GraphicNode)selected.elementAt(i)); }

    public int selectedSize() { return (selected.size()); }

    public void setHighlight1(Node nodal) {
	GraphicNode gnode;

	gnode = this.graphicNodeAt(nodal.getIndex()); 
	gnode.setHighlight1(true); }

    public void setHighlight2(Node nodal) {
	GraphicNode gnode;

	gnode = this.graphicNodeAt(nodal.getIndex()); 
	gnode.setHighlight2(true); }

    public int gt_size() {
	return tree_list.size(); }

    public GraphicNode getNodeforCoords(int x, int y) {
 	GraphicNode gnode;
 	int i;

 	for (i = 0; i < tree_list.size(); i++) {
	    gnode = this.graphicNodeAt(i);
	    if (gnode.containsCoords(x, y)) {
		return gnode; } }
	gnode = new GraphicNode("NULL");
	return gnode; 
    }

    public GraphicNode getGNode(Node nodal) {
	int dex;

	dex = nodal.getIndex_int();
	return(this.graphicNodeAt(dex)); }

    public GraphicNode getMother(GraphicNode gnode) {
	Node nodal, mom;

	nodal = gnode.getNode();
	mom = sparse.GetMother(nodal);
	return(this.getGNode(nodal)); }


    public void OKShiftLeft(int dex, int proposed) {
	int corrected, behind, i, shift_constant = 6*X_DIFF;

	corrected = proposed;
	after_loop:  for (i = dex + 1; i < sparse.size(); i++) {
	    behind = xSpaceBehind(i, dex);
	    if (behind < 0) { continue after_loop; }
	    if (corrected > behind) {
		corrected = behind ; }}
	if (corrected <= shift_constant) { return; }
	this.shiftLeft(dex, corrected - shift_constant); 
	return; }
	

    public int xSpaceBehind(Node nodal) {
	int dex = nodal.getIndex_int();
	return (xSpaceBehind(dex, dex)); }

    public int xSpaceBehind(int dex) {
	return (xSpaceBehind(dex, dex)); }

    public int xSpaceBehind(int dex, int before) {
	int i, curr_x_start, diff_x_end, behind = -3; 
	GraphicNode curr_gnode, diff_gnode;

	if (dex == 0) { return 0; }
	curr_gnode = this.graphicNodeAt(dex);
	curr_x_start = curr_gnode.getXStart();
	for (i = before - 1; i > 0; i--) {
	    diff_gnode = this.graphicNodeAt(i);
	    if (diff_gnode.sameY(curr_gnode)) {
		diff_x_end = diff_gnode.getXEnd();
		behind = curr_x_start - diff_x_end;
		return behind; }}
	return behind; }
		
    protected void correctRoot(Node first_daughter, Node last_daughter) {
	GraphicNode gfirst, glast, groot;
	int new_x_start;

	gfirst = this.graphicNodeFor(first_daughter);
	glast = this.graphicNodeFor(last_daughter);
	new_x_start = (gfirst.getXStart() + glast.getXStart())/2;
	groot = this.graphicNodeFor(sparse.getRootNode());
	groot.setXStart(new_x_start);
	groot.setXEnd(new_x_start + X_DIFF*FONT_SZ/2); 
    }

    public void PrintHighNodes() {
	Node nodal;
	for (int i = 0; i < this.high_nodes.size(); i++) {
	    nodal = (Node)high_nodes.elementAt(i);
	    System.err.print(nodal.toString() + ", " ); }
	System.err.println(""); }


    public void PrintToSystemErr() {
	PrintToSystemErr(0, this.gt_size()); }

    public void PrintToSystemErr(int start, int finish) {
	GraphicNode gnode;

	if (start < 0) { start = 0; }
	if (finish > this.gt_size()) {
	    finish = this.gt_size(); }
	for (int i = start; i < finish; i++) {
	    gnode = this.graphicNodeAt(i);
	    gnode.PrintToSystemErr(); } }

    public void PrintSelectToSystemErr() {
	GraphicNode gnode; Node nodal; int i;

	System.err.print("selected nodes: ");
	for (i = 0; i < selected.size(); i++) {
	    gnode = (GraphicNode)selected.elementAt(i);
	    nodal = gnode.getNode();
	    System.err.print(nodal.toString());
	    if (i < selected.size() - 1) {
		System.err.print(", "); }}
	System.err.println(""); }
}
