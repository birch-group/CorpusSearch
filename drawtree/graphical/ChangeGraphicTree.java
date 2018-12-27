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

public class ChangeGraphicTree extends SetGraphicTree {

    private String message, instruct, warn, show_str;
    private String myPos = "YiLan", myText = "DaRen", myNewLabel = "QingTian";
    private GraphicNode selected;
    private Node s_node, s_node2, up_snode2, new_node;
    private boolean did_it, is_metadata;
    private int space_dex;

    public ChangeGraphicTree() {super.Init();}

    public ChangeGraphicTree(ChangeTree ct) {
	super.Init(ct);}

    public ChangeGraphicTree(ChangeTree ct, String collapse_list) { 
	super.Init(ct);
	setCollapseList(collapse_list);
	super.lapse.expandAll(false);
	if (!collapse_list.equals("")) {
	    warn = "collapse:  " + super.lapse.toString();
	    MyEvents.warn(warn); }
	super.setTreeCoords(); }

    public ChangeGraphicTree(ChangeTree ct, SentenceResult dices) {
	super.Init(ct, dices); }

    public ChangeGraphicTree copy() {
	ChangeGraphicTree copy_cgt;
        ChangeTree copy_sparse;
	SentenceResult copy_indices;
	Node copy_bullet;

	try {
	    copy_sparse = sparse.copy(); }
	catch (NullPointerException npe) {
	    copy_sparse = new ChangeTree(); }
	if (super.hasIndices()) {
	    copy_indices = super.getIndices().copy();
	    copy_cgt = new ChangeGraphicTree(copy_sparse, copy_indices); 
	    copy_bullet = (super.getBulletNode()).copy();
	    copy_cgt.setBulletNode(copy_bullet); }
	else { 
	    copy_cgt = new ChangeGraphicTree(copy_sparse);
	    copy_bullet = (super.getBulletNode()).copy();
	    copy_cgt.setBulletNode(copy_bullet); }
	return copy_cgt; }

    public boolean sameFileName(ChangeGraphicTree other) {
	return (this.getChangeTree().sameFileName(other.getChangeTree())); }

    public boolean sameFileName(ChangeTree other) {
	return (this.getChangeTree().sameFileName(other)); }

    public int getNumID() {
	return(super.getChangeTree().getNumID()); }

    public String getMyNewLabel() { return myNewLabel; }

    public Node getUpSNode2 () { return up_snode2; }

    public GraphicNode getRootGNode() {
	Node root_node = super.sparse.getRootNode();
	return(this.getGNode(root_node)); }

    public void clearCollapsed() {
	MyEvents.warn("");
	MyEvents.intf.setInputText("");
	super.lapse.clearCollapsed(); }

    public void setCollapsedBits() { 
	super.lapse.setCollapsedBits(super.sparse); }

    public void setCollapseList(String collist) {
	super.lapse.setCollapseList(super.sparse, collist.trim());
        super.setTreeCoords(); } 

    public boolean isCollapsed(int i) {return (super.lapse.isCollapsed(i)); }

    public int getCollapsedEnd(Node nodal) {
	return super.lapse.getCollapsedEnd(nodal); }

    public boolean isCollapseSubRoot(Node nodal) {
	return super.lapse.isCollapseSubRoot(nodal); }

    public boolean lapseRootsEmpty() { return super.lapse.rootsEmpty(); }

    public void lapseRemoveAll() { 
	super.lapse.removeAllCollapsed();
	super.lapse.removeAllExpanded(); }

    public boolean willCollapse() { return (super.lapse.willCollapse()); }

    public void collapseNodes() {
	
	super.lapse.expandAll(false);
	selected = super.peekSelected();
	if (selected.IsNullGNode()) {
	    if (super.lapse.rootsEmpty()) {
		MyEvents.warn("must select node(s) to collapse"); 
		return; }
	    else { 
		warn = "toggle collapse:  " + super.lapse.toString();
		MyEvents.warn(warn);
		lapse.removeAllExpanded();
		super.setTreeCoords();  return; }}
	super.lapse.removeAllCollapsed();
	for (int i = 0; i < super.selectedSize(); i++) {
	    selected = super.getSelected(i);
	    if (!selected.IsNullGNode()) {
		if (super.sparse.IsLeafText(selected.getNode())) {
		    new_node = super.sparse.GetMother(selected.getNode());
		    super.lapse.addToCollapsedRoots(super.sparse, new_node); }
		else {
		    super.lapse.addToCollapsedRoots(super.sparse,
						    selected); }}}
	super.clearSelected();
	warn = "collapse:  " + super.lapse.toString();
	MyEvents.warn(warn);
	super.setTreeCoords();
	return; }

    public void expandNodes() {

	selected = super.peekSelected();
	if (selected.IsNullGNode()) {
	    if (super.lapse.rootsEmpty()) {
		MyEvents.warn("must select node(s) to expand"); return; }
	    else {
		MyEvents.warn("toggle expand");
		super.lapse.expandSelected();
		super.setTreeCoords(); return; }}
	super.lapse.removeAllExpanded();
	warn = "expand:  ";
	for (int i = 0; i < super.selectedSize(); i++) {
	    selected = super.getSelected(i);
	    if (!selected.IsNullGNode()) {
		warn += selected.getNode().toString() + "  ";
		super.lapse.removeFromCollapsedRoots(selected); }}
	super.clearSelected();
	MyEvents.warn(warn);
	super.setTreeCoords(); return; }

    public void expandAllNodes() {
	super.lapse.expandAll();
	super.setTreeCoords(); return; }

    public void delete_node() {
	Node text_node;
	String pos_str, text_str;

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select a node to delete");
	    return; }
	//selected.selectOff();
	s_node = selected.getNode();
	if (CorpusDraw.currTree().getChangeTree().
	    getRootNode().equals(s_node)) {
	    MyEvents.warn("cannot delete root!"); 
	    return; }
	MyEvents.errtf.willDo("delete", s_node.toString());
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	if (sparse.IsLeafPOS(s_node)) {
	    if (!CorpusDraw.corpse_tags.hasTags()) {
	        sparse.DeleteLeaf(s_node.getIndex_int(), true);
		actor = new ActionRecord("delete_node", CorpusDraw.for_undo);
		undoing.push(actor);
		return; }
	    text_node = sparse.FirstDaughter(s_node);
	    pos_str = s_node.getLabel();
	    text_str = text_node.getLabel();
	    if (!pos_str.startsWith("COMMENT") &&
		!pos_str.equals("ID") &&
		!pos_str.equals("METADATA") &&
		!CorpusDraw.corpse_tags.legitCatTag(text_str)) {
		MyEvents.warn("cannot delete text or POS!");
		return; } 
	    // WARNING -- had to send "false"
	    did_it = sparse.DeleteLeaf(s_node.getIndex_int(), false);	    
	    if (!did_it) {
		MyEvents.errtf.cant("delete", s_node.toString()); 
		return; }
	    if (super.hasIndices()) {
		super.indices.rmNode(s_node);
	        super.indices.rmNode(text_node); }
	    super.updateIndices();
	    super.setTreeCoords();
	    actor = new ActionRecord("delete_node", CorpusDraw.for_undo);
	    undoing.push(actor);
	    return; }
	super.sparse.changesInit();
	did_it = sparse.DeleteInNode(s_node, true); 
        if (!did_it) {
	    MyEvents.errtf.cant("delete", s_node.toString()); 
	    return; }
	if (super.hasIndices()) {
	    super.indices.rmNode(s_node); }
	super.updateIndices();
	super.setTreeCoords();
	actor = new ActionRecord("delete_node", CorpusDraw.for_undo);
	undoing.push(actor);
	return;
    }

    public void raze_node() {
	Node text_node;
	String pos_str, text_str;
	Vector descendants, trace_dexes;

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select a node to delete"); return; }
	//selected.selectOff();
	s_node = selected.getNode();
	MyEvents.errtf.willDo("raze", s_node.toString());
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	if (sparse.IsLeafPOS(s_node) || sparse.IsLeafText(s_node)) {
	    MyEvents.errtf.cant("raze", s_node.toString()); return; }
	descendants = super.sparse.GetDescendants(s_node);
	trace_dexes = getTraceDexes(descendants);
	super.sparse.changesInit();
	did_it = sparse.RazeNode(s_node, true, trace_dexes); 
        if (!did_it) {
	    MyEvents.errtf.cant("raze", s_node.toString()); 
	    return; }
	if (super.hasIndices()) {
	    super.indices.rmNode(s_node); }
	super.updateIndices();
	super.setTreeCoords();
	actor = new ActionRecord("raze_node", CorpusDraw.for_undo);
	undoing.push(actor);
	return; }

    public Vector getTraceDexes (Vector descendants) {
	Node desc;
	Integer trace_dex;
	int i;
	String label;
	Vector trace_dexes = new Vector();

	if (!CorpusDraw.corpse_tags.hasTags()) { return trace_dexes; }
	for (i = 0; i < descendants.size(); i++) {
	    desc = (Node)descendants.elementAt(i);
	    if (super.sparse.IsLeafText(desc)) {
		label = desc.getLabel(); 
		if (CorpusDraw.corpse_tags.legitCatTag(label)) {
		    trace_dex = new Integer(desc.getIndex_int());
		    trace_dexes.addElement(trace_dex);
		    trace_dex = new Integer(desc.getIndex_int() - 1);
		    trace_dexes.addElement(trace_dex); }}}
	return trace_dexes; }
	

    public boolean getLabel(String interr, Node s_node) {

	wait_loop: while (true) {
	    if (MyEvents.willReset()) {
		return false; }
	    if (MyEvents.interrupt(interr)) {
		return false; }
	    if (MyEvents.intf.getChanged()) {
		myNewLabel = MyEvents.intf.getInputText();
		did_it = cleanLabel(myNewLabel, s_node);
		if (!did_it) { continue wait_loop; }
		break; } }
	return true; }

    public boolean cleanLabel(String labell, Node s_node) {

	myNewLabel = labell.trim();
	if (!sparse.IsLeafText(s_node) || CorpusDraw.corpse_tags.hasTags()) {
	    myNewLabel = (labell.toUpperCase()).trim(); }
	if (!isMetadata(s_node) && !legitLabel(myNewLabel, s_node)) {
	    return false; }
	MyEvents.warn("got new label:  " + myNewLabel);
	MyEvents.intf.resetChanged();
	return true; }

    public boolean getLabelAddNode(String interr, Node s_node) {

	wait_loop: while (true) {
	    if (MyEvents.willReset()) {
		return false; }
	    if (MyEvents.interrupt(interr)) {
		return false; }
	    if (MyEvents.intf.getChanged()) {
		myNewLabel = MyEvents.intf.getInputText();
		did_it = cleanLabelAddNode(myNewLabel, s_node);
		if (!did_it) { continue wait_loop; }
		break; } }
	return true; }

    public boolean cleanLabelAddNode(String labell, Node s_node) {

	myNewLabel = labell.trim();
	if (!sparse.IsLeafText(s_node) || CorpusDraw.corpse_tags.hasTags()) {
	    myNewLabel = (labell.toUpperCase()).trim(); }
	if (!isMetadata(s_node) && sparse.IsLeafText(s_node) && 
	    !legitPOSLabel(myNewLabel)) { return false; }
	if (!isMetadata(s_node) && sparse.IsLeafPOS(s_node) && 
	    !legitSynLabel(myNewLabel)) { return false; }
	if (!isMetadata(s_node) && !sparse.IsLeafText(s_node) && 
	    !sparse.IsLeafPOS(s_node) && !legitSynLabel(myNewLabel)) { 
	    return false; }
	MyEvents.warn("got new label:  " + myNewLabel);
	MyEvents.intf.resetChanged();
	return true; }

    public void replaceLabel() {
	
	//System.err.println("in replaceLabel:  ");
	instruct = "to replace label, enter new label in input field.";
	did_it = replaceLabel0();
	if (!did_it) { return; }
	MyEvents.intf.setInputText(s_node.getLabel());
	MyEvents.intf.requestFocus();
	MyEvents.warn(instruct);
	did_it = getLabel("replace label", s_node);
	if (!did_it) { return; }
	replaceLabel2(myNewLabel, s_node);
	return; }


    public boolean replaceLabel0 () {

	selected = super.peekSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select a node to replace label");
	    return false; }
	//selected.selectOff();
	s_node = selected.getNode();
	is_metadata = isMetadata(s_node);
	if (!is_metadata && trueText(s_node)) {
	    message = "cannot replace text:  " + s_node.getLabel(); 
	    MyEvents.warn(message);
	    return false; }
	return true; }

    public void replaceLabel2 (String new_label, Node s_node) { 
	replaceLabel2(new_label, s_node, 0); return; }

    public void replaceLabel2 (String new_label, Node s_node, 
			       int will_undo_next) {

	MyEvents.errtf.willDo("replace label", s_node.toString());
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	sparse.ChangeLabel(new_label, s_node, false);
	super.setTreeCoords();
	actor = new ActionRecord("replace_label", CorpusDraw.for_undo, 
				 will_undo_next);
	undoing.push(actor);
	return; }

    public int getGoToNum() {
	String num_str = "000";
	Integer tree_Int = new Integer(-1);
	int tree_num = 1; 

	instruct = "enter tree number, e.g. 210";
	MyEvents.intf.clear();
	MyEvents.intf.requestFocus();
	MyEvents.warn(instruct);
	wait_loop: while (true) {	    
	    if (MyEvents.interrupt("goto")) {
		return -1; }
	    if (MyEvents.intf.getChanged()) {
		num_str = MyEvents.intf.getInputText();
		try {
		    tree_num = tree_Int.parseInt(num_str); }
		catch (NumberFormatException nfe) {
		    MyEvents.warn(num_str + " not a number! try again:  ");
		    MyEvents.intf.clear();
		    MyEvents.intf.resetChanged();
		    continue; }
		if (tree_num < 1) {
		    MyEvents.warn(num_str + " not legit number! try again: ");
		    MyEvents.intf.clear();
		    MyEvents.intf.resetChanged();
		    continue; }
		break; } }
	MyEvents.intf.resetChanged();
	goTo2(num_str);
	return tree_num; }

    public void goTo2(String num_str) {
	MyEvents.errtf.willDo("go to", num_str);
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	actor = new ActionRecord("go to ", CorpusDraw.for_undo);
	undoing.push(actor); }

    public boolean trueText(Node s_node) {

	if (!CorpusDraw.corpse_tags.hasTags()) { return false; }
    	if (CorpusDraw.corpse_tags.hasTags() && sparse.IsLeafText(s_node) &&
	    !CorpusDraw.corpse_tags.legitCatTag(s_node.getLabel())) {
	    return true; }
	return false; }

    public boolean isMetadata(Node s_node) {
	Node mom;

	mom = this.sparse.GetMother(s_node);
	if (mom.getLabel().equals("ID")) {
	    return true; }
	return false; }


    public boolean legitTracePOS (String pos_label) {
	if (!CorpusDraw.corpse_tags.hasTags()) {
	    return true; }
	if (pos_label.startsWith("COMMENT")) {
	    return true; }
	if (!CorpusDraw.corpse_tags.legitPOSTag(pos_label) &&
	    !CorpusDraw.corpse_tags.legitSynTag(pos_label)) {
	    message = "not legit tag:  " + pos_label + ":  try again!";
	    MyEvents.warn(message);
	    return false; }
	return true; }

    public boolean legitEmptyCat (String text_label) {
	if (!CorpusDraw.corpse_tags.hasTags()) {
	    return true; }
	if (!CorpusDraw.corpse_tags.legitCatTag(text_label)) {
	    message = "not legit empty category:  "+text_label+":  try again!";
	    MyEvents.warn(message);
	    return false; }
	return true; }

    public boolean legitLeaf(String pos_label, String text_label) {

	if (!CorpusDraw.corpse_tags.hasTags()) {
	    return true; }
	if (pos_label.startsWith("COMMENT")) {
	    return true; }
	if (!CorpusDraw.corpse_tags.legitPOSTag(pos_label) &&
	    !CorpusDraw.corpse_tags.legitSynTag(pos_label)) {
	    message = "not legit tag:  " + pos_label + ":  try again!";
	    reType(pos_label, message);
	    return false; }
	if (!CorpusDraw.corpse_tags.legitCatTag(text_label)) {
	    message = "not legit empty category:  "+text_label+":  try again!";
	    reType(text_label, message);
	    return false; }
	return true; }

    public boolean legitLabel(String new_label, Node s_node) {

	if (!CorpusDraw.corpse_tags.hasTags()) {
	    return true; }
	if (sparse.IsLeafText(s_node) && 
	    !CorpusDraw.corpse_tags.legitCatTag(new_label)) {
	    message = "not legit empty category tag: " + new_label;
	    message += ":  try again!";
	    reType(new_label, message);
	    return false; }
	if (sparse.IsLeafPOS(s_node) && 
	    !CorpusDraw.corpse_tags.legitPOSTag(new_label)) {
	    message = "not legit POS tag:  " + new_label + ":  try again!";
	    reType(new_label, message);
	    return false; }
	if (!sparse.IsLeafPOS(s_node) && !sparse.IsLeafText(s_node) && 
	    !CorpusDraw.corpse_tags.legitSynTag(new_label)) {
	    message = "not legit syntactic tag:  " + new_label;
	    message += ":  try again!";
	    reType(new_label, message);
	    return false; }
	return true; }

    public boolean legitSynLabel(String new_label) {

	if (!CorpusDraw.corpse_tags.hasTags()) {
	    return true; }
	if (!CorpusDraw.corpse_tags.legitSynTag(new_label)) {
	    message = "not legit syntactic tag:  " + new_label;
	    message += ":  try again!";
	    reType(new_label, message);
	    return false; }
	return true; }

    public boolean legitPOSLabel(String new_label) {

	if (!CorpusDraw.corpse_tags.hasTags()) {
	    return true; }
	if (!CorpusDraw.corpse_tags.legitPOSTag(new_label)) {
	    message = "not legit POS tag:  " + new_label;
	    message += ":  try again!";
	    reType(new_label, message);
	    return false; }
	return true; }

    public void reType (String input, String err_message) {
	MyEvents.warn(err_message);
	MyEvents.intf.clear();
	MyEvents.intf.setInputText(input);
	MyEvents.intf.resetChanged();
	MyEvents.intf.requestFocus(); }

    public void addInternalNode() {

	MyEvents.clearTextFields();
	instruct = "to add internal node, enter new label in input field.";
	MyEvents.warn(instruct);
	MyEvents.intf.requestFocus();
	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select daughter(s) of new node");
	    return; }
	s_node = selected.getNode();
	selected = super.popSelected();
	did_it = getLabelAddNode("add internal node", s_node);
	if (!did_it) { return; }
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	if (selected.IsNullGNode()) { // add over one daughter.
	    MyEvents.errtf.willDo("add internal node", s_node.toString());
	    super.sparse.changesInit();
	    did_it = sparse.AddInternalNode(myNewLabel, s_node, 
					    s_node, true);	    
	    if (!did_it) {
		MyEvents.errtf.cant("add internal node", s_node.toString(),
				 s_node.toString()); 
		return; } }
	else { // add over two daughters.    
	    s_node2 = selected.getNode();
	    MyEvents.errtf.willDo("add_internal_node", s_node.toString(), 
			       s_node2.toString());
	    super.sparse.changesInit();
	    did_it = sparse.AddInternalNode(myNewLabel, s_node, 
					    s_node2, true);
	    if (!did_it) {
		MyEvents.errtf.cant("add internal node", s_node.toString(),
				 s_node2.toString()); 
		return; } }
	actor = new ActionRecord("add_internal_node", CorpusDraw.for_undo);
	undoing.push(actor); 
	super.updateIndices();
	super.setTreeCoords();
	return; }

    public GraphicNode addBlankSynNode() {
	GraphicNode newGNode = new GraphicNode("NULL"); 
	Node up_s_node, mom;

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select daughter(s) of new node");
	    return newGNode; }
	s_node = selected.getNode();
	selected = super.popSelected();
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	if (selected.IsNullGNode()) { // add over one daughter.
	    MyEvents.errtf.willDo("add internal node", s_node.toString());
	    super.sparse.changesInit();
	    did_it = sparse.AddInternalNode("", s_node, 
					    s_node, true);	    
	    if (!did_it) {
		MyEvents.errtf.cant("add internal node", s_node.toString(),
				 s_node.toString()); 
		return newGNode; } }
	else { // add over two daughters.    
	    s_node2 = selected.getNode();
	    MyEvents.errtf.willDo("add_internal_node", s_node.toString(), 
			       s_node2.toString());
	    super.sparse.changesInit();
	    did_it = sparse.AddInternalNode(" ", s_node, 
					    s_node2, true);
	    if (!did_it) {
		MyEvents.errtf.cant("add internal node", s_node.toString(),
				 s_node2.toString()); 
		return newGNode; } }
	actor = new ActionRecord("add_blank_node", CorpusDraw.for_undo);
	undoing.push(actor); 
	super.updateIndices();
	super.setTreeCoords();
	up_s_node = super.sparse.getUpdate(s_node);
	mom = super.sparse.GetMother(up_s_node);
	newGNode = super.getGNode(mom);
	return newGNode; }

    public Node getNewNode(Node nodal) {
	return (super.sparse.NodeAt(nodal.getIndex_int())); }

    public void co_index (Node node1, Node node2) {
	this.co_index(node1, node2, 0); return; }

    public void co_index(Node node1, Node node2, int must_undo) {
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	sparse.coIndex(node1, node2);
	actor = new ActionRecord("coindex", CorpusDraw.for_undo, must_undo);
	undoing.push(actor); 
	super.setTreeCoords();
        return; }

    public void co_index() {
	String new_dex;
	Node new_node, temp;

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select two nodes to coindex");
	    return; }
	s_node = selected.getNode();
	if (trueText(s_node)) {
	    message = "cannot coindex text:  " + s_node.getLabel();
	    MyEvents.warn(message);
	    return; }
	selected = super.popSelected();
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	if (selected.IsNullGNode()) { 
	    MyEvents.warn("must select two nodes to coindex");
	    return; }
	s_node2 = selected.getNode();
	if (trueText(s_node2)) {
	    message = "cannot coindex text:  " + s_node2.getLabel();
	    MyEvents.warn(message);
	    return; }
	MyEvents.errtf.willDo("coindex", s_node.toString(), 
			   s_node2.toString());
	did_it = sparse.coIndex(s_node, s_node2);
	if (!did_it) {
	    MyEvents.errtf.cant("coindex", s_node.toString(),
			     s_node2.toString()); 
	    return; } 
	actor = new ActionRecord("coindex", CorpusDraw.for_undo);
	undoing.push(actor); 
	super.setTreeCoords();
	return; }

    private boolean getPosText(String interr) {

 	wait_loop: while (true) {
	    if (MyEvents.willReset()) {
		return false; }
 	    if (MyEvents.interrupt(interr)) {
		return false; }
 	    if (MyEvents.intf.getChanged()) {
 		myNewLabel = MyEvents.intf.getInputText();
 		space_dex = myNewLabel.indexOf(" ");
 		if (space_dex == -1) {
 		    message = "ERROR! leaf must have form:  POS TEXT";
 		    MyEvents.warn(message);
		    MyEvents.intf.resetChanged();
		    MyEvents.intf.setInputText(myNewLabel);
 		    continue wait_loop; }
 		myPos = (myNewLabel.substring(0, space_dex)).trim();
 		myPos = myPos.toUpperCase();
 		myText = (myNewLabel.substring(space_dex + 1)).trim();
 		if (!legitLeaf(myPos, myText)) {
 		  continue wait_loop; }
 		MyEvents.warn("got new leaf:  " + myNewLabel);
 		MyEvents.intf.resetChanged();
 	        break; } 
 	    if (MyEvents.ub.getPressed()) {
 		break; } }
	return true; }

    public GraphicNode blankLeafAfter() {
	GraphicNode gNode = new GraphicNode("NULL");
	Node nodal, new_node;


	selected = super.popSelected();
 	if (selected.IsNullGNode()) {
 	    MyEvents.warn("must select node to insert after");
 	    return gNode; }
	s_node = selected.getNode();
	if (sparse.IsLeafText(s_node)) {
	    s_node = sparse.GetMother(s_node ); }
 	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
 	MyEvents.errtf.willDo("add_leaf_after", s_node.toString());
 	super.sparse.changesInit();
 	did_it = sparse.InsertLeafAfter("WXP", "0", 
 					s_node.getIndex_int(), true);
 	if (!did_it) {
 	    MyEvents.errtf.cant("add_leaf_after", s_node.toString());
 	    return gNode; } 
 	actor = new ActionRecord("add_leaf_after", CorpusDraw.for_undo);

 	undoing.push(actor);
	new_node = super.sparse.nextSister(s_node);
 	super.updateIndices();
 	super.setTreeCoords();
	gNode = super.getGNode(new_node);
 	return gNode; }

    public void InsertLeafAfter() {
	String instruct = "to add leaf after, ";

	selected = super.popSelected();
 	if (selected.IsNullGNode()) {
 	    MyEvents.warn("must select node to insert after");
 	    return; }
	s_node = selected.getNode();
	if (sparse.IsLeafText(s_node)) {
	    s_node = sparse.GetMother(s_node ); }
 	instruct += "enter leaf e.g.:  WNP 0";
 	MyEvents.warn(instruct);
	MyEvents.intf.setInputText("WXP 0");
 	MyEvents.intf.requestFocus();
	did_it = getPosText("insert leaf after");
	if (!did_it) { return; }
 	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
 	MyEvents.errtf.willDo("add_leaf_after", s_node.toString());
 	super.sparse.changesInit();
 	did_it = sparse.InsertLeafAfter(myPos, myText, 
 					s_node.getIndex_int(), true);
 	if (!did_it) {
 	    MyEvents.errtf.cant("add_leaf_after", s_node.toString());
 	    return; } 
 	actor = new ActionRecord("add_leaf_after", CorpusDraw.for_undo);
 	undoing.push(actor);
 	super.updateIndices();
 	super.setTreeCoords();
 	return; }

     public void InsertLeafBefore() {

 	instruct = "to add leaf before, enter leaf e.g.:  WNP 0";
	selected = super.popSelected();
 	if (selected.IsNullGNode()) {
 	    MyEvents.warn("must select node to insert before");
 	    return; }
	s_node = selected.getNode();
	if (sparse.IsLeafText(s_node)) {
	    s_node = sparse.GetMother(s_node ); }
	MyEvents.warn(instruct);
	MyEvents.intf.setInputText("WXP 0");
	MyEvents.intf.requestFocus();
	did_it = getPosText("insert leaf before");
	if (!did_it) { return; }
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	MyEvents.errtf.willDo("add_leaf_before", s_node.toString());
	super.sparse.changesInit();
	did_it = sparse.InsertLeafBefore(myPos, myText, 
					 s_node.getIndex_int(), true);
	if (!did_it) {
	    MyEvents.errtf.cant("add_leaf_before", s_node.toString());
	    return; } 
	actor = new ActionRecord("add_leaf_before", CorpusDraw.for_undo);
	undoing.push(actor); 
	super.updateIndices();
	super.setTreeCoords();
	return; }

     public GraphicNode blankLeafBefore() {
	 GraphicNode newGNode = new GraphicNode("NULL");
	 Node new_node;

	selected = super.popSelected();
 	if (selected.IsNullGNode()) {
 	    MyEvents.warn("must select node to insert before");
 	    return newGNode; }
	s_node = selected.getNode();
	if (sparse.IsLeafText(s_node)) {
	    s_node = sparse.GetMother(s_node ); }
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	MyEvents.errtf.willDo("add_leaf_before", s_node.toString());
	super.sparse.changesInit();
	did_it = sparse.InsertLeafBefore("WXP", "0", 
					 s_node.getIndex_int(), true);
	if (!did_it) {
	    MyEvents.errtf.cant("add_leaf_before", s_node.toString());
	    return newGNode; } 
	actor = new ActionRecord("add_leaf_before", CorpusDraw.for_undo);
	undoing.push(actor); 
	super.updateIndices();
	super.setTreeCoords();
	new_node = super.sparse.NodeAt(s_node.getIndex_int());
	newGNode = super.getGNode(new_node);
	return newGNode; }


    public void moveTo() {

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select node to move, target");
	    return; }
	s_node2 = selected.getNode();
	selected = super.popSelected();
	if (selected.IsNullGNode()) { 
	    MyEvents.warn("must select node to move, target");
	    return; }
	s_node = selected.getNode();
	MyEvents.errtf.willDo("move to", s_node.toString(), 
			      s_node2.toString());
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	super.sparse.changesInit();
	did_it = super.sparse.MoveTo(s_node, s_node2, true);
	if (!did_it) {
	    MyEvents.errtf.cant("move to", s_node.toString(), 
				s_node2.toString()); 
	    return; }
	actor = new ActionRecord("move_to", CorpusDraw.for_undo);
	undoing.push(actor);
	super.updateIndices();
	super.setTreeCoords();
	return; }
   
    public void TraceBefore() {

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select trace before, antecedent");
	    return; }
	s_node2 = selected.getNode();
	selected = super.popSelected();
	if (selected.IsNullGNode()) { 
	    MyEvents.warn("must select trace before, antecedent");
	    return; }
	s_node = selected.getNode();
	MyEvents.errtf.willDo("*T* trace before", s_node.toString(), 
			      s_node2.toString());
	MyEvents.intf.setInputText(s_node2.getLabel() + " *T*");
 	MyEvents.intf.requestFocus();
	did_it = getPosText("trace before");
	if (!did_it) { return; }
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	super.sparse.changesInit();
	did_it = super.sparse.InsertLeafBefore(myPos, myText, 
					       s_node.getIndex_int(),true);
	if (!did_it) {
	    MyEvents.errtf.cant("*T* trace before", s_node.toString(), 
				s_node2.toString()); 
	    return; }
	new_node = super.sparse.NodeAt(s_node.getIndex_int());
	if (super.sparse.IsLeafPOS(new_node)) {
	    new_node = super.sparse.FirstDaughter(new_node); }
	up_snode2 = super.sparse.getUpdate(s_node2);
	did_it = super.sparse.coIndex(new_node, up_snode2);
	actor = new ActionRecord("*T* trace before", CorpusDraw.for_undo);
	undoing.push(actor);
	super.updateIndices();
	super.setTreeCoords();
	return; }

   

    public void TraceAfter() {

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select trace after, antecedent");
	    return; }
	s_node2 = selected.getNode();
	selected = super.popSelected();
	if (selected.IsNullGNode()) { 
	    MyEvents.warn("must select trace after, antecedent");
	    return; }
	s_node = selected.getNode();
	MyEvents.errtf.willDo("*ICH* trace after", s_node.toString(), 
			      s_node2.toString());
	MyEvents.intf.setInputText(s_node2.getLabel() + " *ICH*");
 	MyEvents.intf.requestFocus();
	did_it = getPosText("trace after");
	if (!did_it) { return; }
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	super.sparse.changesInit();
	did_it = super.sparse.InsertLeafAfter(myPos, myText, 
					       s_node.getIndex_int(),true);
	if (!did_it) {
	    MyEvents.errtf.cant("*ICH* trace after", s_node.toString(), 
				s_node2.toString()); 
	    return; }
	new_node = super.sparse.nextSister(s_node);
	if (super.sparse.IsLeafPOS(new_node)) {
	    new_node = super.sparse.FirstDaughter(new_node); }
	up_snode2 = super.sparse.getUpdate(s_node2);
	did_it = super.sparse.coIndex(new_node, up_snode2);
	actor = new ActionRecord("*ICH* trace after", CorpusDraw.for_undo);
	undoing.push(actor);
	super.updateIndices();
	super.setTreeCoords();
	return; }

    public GraphicNode blankTraceAfter() {
	GraphicNode newGNode = new GraphicNode("NULL");

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select trace after, antecedent");
	    return newGNode; }
	s_node2 = selected.getNode();
	selected = super.popSelected();
	if (selected.IsNullGNode()) { 
	    MyEvents.warn("must select trace after, antecedent");
	    return newGNode; }
	s_node = selected.getNode();
	MyEvents.errtf.willDo("*ICH* trace after", s_node.toString(), 
			      s_node2.toString());
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	super.sparse.changesInit();
	did_it = super.sparse.InsertLeafAfter(s_node2.getLabel(), "*ICH*", 
					      s_node.getIndex_int(),true);
	if (!did_it) {
	    MyEvents.errtf.cant("*ICH* trace after", s_node.toString(), 
				s_node2.toString()); 
	    return newGNode; }
	actor = new ActionRecord("*ICH* trace after", CorpusDraw.for_undo);
	undoing.push(actor);
	super.updateIndices();
	super.setTreeCoords();
	new_node = super.sparse.nextSister(s_node);
	up_snode2 = super.sparse.getUpdate(s_node2);
	newGNode = super.getGNode(new_node);
	return newGNode; }

    public GraphicNode blankTraceBefore() {
	GraphicNode newGNode = new GraphicNode("NULL");

	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select trace before, antecedent");
	    return newGNode; }
	s_node2 = selected.getNode();
	selected = super.popSelected();
	if (selected.IsNullGNode()) { 
	    MyEvents.warn("must select trace before, antecedent");
	    return newGNode; }
	s_node = selected.getNode();
	MyEvents.errtf.willDo("*ICH* trace before", s_node.toString(), 
			      s_node2.toString());
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	super.sparse.changesInit();
	did_it = super.sparse.InsertLeafBefore(s_node2.getLabel(), "*ICH*", 
					      s_node.getIndex_int(),true);
	if (!did_it) {
	    MyEvents.errtf.cant("*ICH* trace before", s_node.toString(), 
				s_node2.toString()); 
	    return newGNode; }
	actor = new ActionRecord("*ICH* trace before", CorpusDraw.for_undo);
	undoing.push(actor);
	super.updateIndices();
	super.setTreeCoords();
	//	new_node = super.sparse.prevSister(s_node);
	new_node = s_node;
	up_snode2 = super.sparse.getUpdate(s_node2);
	newGNode = super.getGNode(new_node);
	return newGNode; }

    public Node firstDaughter(Node mom_node) {
	return(super.sparse.FirstDaughter(mom_node)); }

    public void mergePrevious() {
	ChangeGraphicTree previous;

	previous = CorpusDraw.previousTree();
	if (previous.isEmpty()) {
	    MyEvents.warn("there is no previous tree in this file!");
	    return; }
	CorpusDraw.for_undo1 = previous.copy();
	CorpusDraw.for_undo2 = (CorpusDraw.currTree()).copy();
	super.sparse.changesInit();
	(CorpusDraw.currTree().getChangeTree()).
	    MergePrevious(previous.getChangeTree());
	actor = new ActionRecord("merge_previous", CorpusDraw.for_undo1, 
				 CorpusDraw.for_undo2);
	undoing.push(actor);
	super.updateIndices();
	//DrawLoop.decSentNumID();
	MyEvents.setUrText(CorpusDraw.currTree());
	super.setTreeCoords();
	CorpusDraw.rmPrev();
	return; }

    public void mergeFollowing() {
	ChangeGraphicTree follow;

	try {
	    follow = CorpusDraw.followingTree();
	    if (follow.getChangeTree().isEmpty()) {
		MyEvents.warn("there is no following tree!");
		return; }
	    CorpusDraw.for_undo2 = follow.copy();
	    CorpusDraw.for_undo1 = (CorpusDraw.currTree()).copy();
	    super.sparse.changesInit();
	    (CorpusDraw.currTree().getChangeTree()).
		MergeFollowing(follow.getChangeTree());
	    actor = new ActionRecord("merge_following", 
				     CorpusDraw.for_undo1, 
				     CorpusDraw.for_undo2);
	    undoing.push(actor); }
	catch (Exception e) {
	    System.err.println("in mergeFollowing:  ");
	    e.printStackTrace(); }
	//DrawLoop.decSentNumID();
	super.updateIndices();	
	MyEvents.setUrText(CorpusDraw.currTree());
	super.setTreeCoords();
	CorpusDraw.rmFoll();
	return; }


    public void split() {
	ChangeGraphicTree next_gt;
	ChangeTree next;
	Node sel_node;
	int split_flag;

	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	selected = super.popSelected();
	if (selected.IsNullGNode()) {
	    MyEvents.warn("must select root node of split tree");
	    return; }
	sel_node = selected.getNode();
	split_flag = (CorpusDraw.currTree().getChangeTree()).
	    legitSplit(sel_node);
	if (split_flag == 0) {
	    MyEvents.warn("not legit root of split tree."); 
	    return; }
	super.sparse.changesInit();
	next = (CorpusDraw.currTree().getChangeTree()).split(sel_node);
	next_gt = new ChangeGraphicTree(next);
	//next_gt.PrintToSystemErr();
	CorpusDraw.pushForward(next_gt, split_flag);
	MyEvents.setUrText(CorpusDraw.currTree());
	actor = new ActionRecord("split", CorpusDraw.for_undo);
	undoing.push(actor);
	//super.indices.update(sparse);
	super.setTreeCoords(); }

    public void showOnlyList() {

	MyEvents.warn("enter new show_only_list, e.g. IP*|CP*");
	MyEvents.intf.requestFocus();
	MyEvents.intf.setInputText(CorpusDraw.show_str);
	while (true) {
	    if (MyEvents.interrupt("show only list")) { 
		MyEvents.solb.resetButton();
		return; }
	    if (MyEvents.intf.getChanged()) {
		show_str = MyEvents.intf.getInputText();
		MyEvents.warn("got new list:  " + show_str);
		MyEvents.intf.resetChanged();
 	        break; } 
	    if (MyEvents.ub.getPressed()) {
		break; } }
	//selected.selectOff();
	super.setTreeCoords();
	return; }

    public void shrink() {
	MyEvents.warn("will shrink.");
	super.shrink();
	super.setTreeCoords(); }
    
    public void swell() {
	MyEvents.warn("will swell.");
	super.swell();
	super.setTreeCoords(); }

    public String collapseList(String old_collapse) {
	String collapse_str = "";

	MyEvents.warn("enter new collapse list, e.g. NP*|PP*.");
	MyEvents.intf.requestFocus();
	MyEvents.intf.setInputText(old_collapse);
	while (true) {
	    if (MyEvents.interrupt("collapse list")) { 
		return (""); }
	    if (MyEvents.intf.getChanged()) {
		collapse_str = MyEvents.intf.getInputText();
		MyEvents.warn("got new list:  " + collapse_str);
		MyEvents.intf.resetChanged(); 
		super.lapse.setCollapseList(super.sparse, collapse_str);
		break; }
	    if (MyEvents.ub.getPressed()) {
		break; } }
	super.lapse.setCollapsedBits(super.sparse);
	super.setTreeCoords();
	return collapse_str; }

    public void undo() {
	ActionRecord un_action, re_action;
	int must_undo, dex;
	
	if (undoing.isEmpty()) {
	    MyEvents.warn("there is nothing to be undone!");
	    return; }
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	MyEvents.errtf.willDo("undo");
	un_action = (ActionRecord)undoing.pop();
	if (!CorpusDraw.hasQuery()) {
	    super.sparse = un_action.getTree1().getChangeTree(); 
	    if (un_action.hasTree2()) {
		ChangeGraphicTree next = un_action.getTree2();
		CorpusDraw.pushForward(next, 0); }}
	else { 
	    if (un_action.getTree1().hasIndices()) {
		super.sparse = un_action.getTree1().getChangeTree(); 
		super.setIndices(un_action.getTree1().getIndices()); 
		if (un_action.hasTree2()) {
		    ChangeGraphicTree next = un_action.getTree2();
		    CorpusDraw.pushForward(next, 0); }}
	    else {
		// tree was merged with prev tree that didn't answer query.
		//CorpusDraw.flushTree(un_action.getTree1().getChangeTree());
		super.sparse = un_action.getTree2().getChangeTree();
		super.setIndices(un_action.getTree2().getIndices()); } }
	must_undo = un_action.unDoNext();
	if (must_undo > 0) {
	    for (dex = 0; dex < must_undo; dex++) { 
		un_action = (ActionRecord)undoing.pop(); }
	    super.sparse = un_action.getTree1().getChangeTree(); }
	re_action = new ActionRecord(un_action.getAction(), 
				     CorpusDraw.for_undo);
	redoing.push(re_action);
	super.setTreeCoords();
	return; }

    public void redo() {
	ActionRecord un_action, re_action;
	
	if (redoing.isEmpty()) {
	    MyEvents.warn("there is nothing to be redone!");
	    return; }
	CorpusDraw.for_undo = CorpusDraw.currTree().copy();
	MyEvents.errtf.willDo("redo");
	re_action = (ActionRecord)redoing.pop();
	super.sparse = re_action.getTree1().getChangeTree();
	if (re_action.getTree1().hasIndices()) {
	    super.setIndices(re_action.getTree1().getIndices()); }
	if (re_action.hasTree2()) {
	    ChangeGraphicTree next = re_action.getTree2();
	    CorpusDraw.pushForward(next, 0); }
	un_action = new ActionRecord(re_action.getAction(), 
				     CorpusDraw.for_undo);
	undoing.push(un_action);
	super.setTreeCoords();
	return; }

    public void PrintToSystemErr() {
	PrintToSystemErr(0, super.gt_size()); }

    public void PrintToSystemErr(int start, int finish) {
	GraphicNode gnode;

	if (start < 0) { start = 0; }
	if (finish > super.gt_size()) {
	    finish = super.gt_size(); }
	for (int i = start; i < finish; i++) {
	    gnode = super.graphicNodeAt(i);
	    gnode.PrintToSystemErr(); } }

    public void PrintLapseToSystemErr() {
	this.lapse.PrintToSystemErr(); }

}

