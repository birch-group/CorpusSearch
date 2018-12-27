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

package syntree;

import java.io.*;
import java.util.*;
import basicinfo.*;

public class ChangeTree extends SynTree {

    protected int num_id = 0;
    protected String file_name = "FILE_NAME_UNKNOWN";
    Vector index_changes;
    Integer minus1 = new Integer(-1);

    public ChangeTree () { }

    public ChangeTree (SynTree sparse) {
	super.setSparseMatrix(sparse);
	changesInit(sparse.size()); }

    public ChangeTree(SynTree sparse, int numID, String fname) {
	super.setSparseMatrix(sparse);
	changesInit(sparse.size());
	this.setNumID(numID);
	this.setFileName(fname);
	this.setORTHO(sparse.hasORTHO()); }

    public void setConstantNodes() {
	super.setConstantNodes(); }

    public int size() {
	return (super.size()); }

    public void setFileName(String fname) {
	file_name = fname; }

    public String getFileName() {
	return this.file_name; }

    public boolean sameFileName(ChangeTree other) {
	if (this.getFileName().equals(other.getFileName())) {
	    return true; }
	return false; }

    public void setNumID(int x) {
	this.num_id = x; }

    public int getNumID() {
	return this.num_id; }

    public boolean lessThan(ChangeTree other) {

	if (this.num_id < other.getNumID()) {
	    return true; }
	return false; }
 
    public boolean greaterThan(ChangeTree other) {

	if (this.num_id > other.getNumID()) {
	    return true; }
	return false; }

    public void removeAllElements() {
	super.removeAllElements(); }

    public void changesInit(Vector in_changes) {
	index_changes = in_changes; }

    public void changesInit() {
	this.changesInit(this.size()); }

    private void changesInit(int siz) {
	Integer dex;
	this.index_changes = new Vector();
	for (int i = 0; i < siz; i++) {
	    dex = new Integer(i);
	    index_changes.addElement(dex); }
	return; }

    public Vector getChanges() {
	return this.index_changes; }

    private void update(int old_dex, int how_much) {
	Integer old_update;
	Integer new_Int;

	old_update = (Integer)index_changes.elementAt(old_dex);
	new_Int = new Integer(old_update.intValue() + how_much);
	index_changes.setElementAt(new_Int, old_dex); 
        super.setConstantNodes(); }

    public Node getUpdate(Node in_node) {
	Integer dex = in_node.getIndex();
	int new_dex = getUpdate(dex);
	return(super.NodeAt(new_dex)); }

    public int getUpdate(int old) {
	Integer old_Int = (Integer)index_changes.elementAt(old);
	return(old_Int.intValue()); }

    public int getUpdate(Integer old) {
	Integer old_Int = (Integer)index_changes.elementAt(old.intValue());
	return(old_Int.intValue()); }

    public int getDowndate (int down_dex) { // inverse of getUpdate.
	Integer change_dex;

	for (int i = 0; i < index_changes.size(); i++) {
	    change_dex = (Integer)index_changes.elementAt(i);
	    if (change_dex.intValue() == down_dex) {
		return i; } }
	return (-1); }
    
    public Node getDowndate (Node in_node) {
	Integer dex = in_node.getIndex();
	int new_dex = getDowndate(dex);
	if (new_dex == -1) { return (new Node("NULL")); }
	return(super.NodeAt(new_dex)); }

    public int getDowndate (Integer new_dex) {
	return (this.getDowndate(new_dex.intValue())); }
	
    public void setUpdate (Integer where, Integer to) {
	index_changes.setElementAt(to, where.intValue()); }

    public Node NodeAt(Integer dex) {
	return(super.NodeAt(dex)); }

    public Node NodeAt(int dex) {
	return(super.NodeAt(dex)); }

    public ChangeTree copy() {
	int i;
	Vector copy_end_vec = new Vector(); 
	Vector copy_label_vec = new Vector();
	Vector copy_index_changes = new Vector();
	Integer change_Int;
	ChangeTree copy_change;
	SynTree copy_syn;
	// copy this end vector into new SparseMatrix.
	for (i = 0; i < super.size(); i++) {
	    copy_end_vec.addElement(super.getEndAt(i)); }
	// copy this label vector into new SparseMatrix.
	for (i = 0; i < super.size(); i++) {
	    copy_label_vec.addElement(super.getLabelAt(i)); }
	copy_change = new ChangeTree();
	copy_change.setEndVector(copy_end_vec);
	copy_change.setLabelVector(copy_label_vec);
	copy_change.setNumID(this.getNumID());
	copy_change.setFileName(this.getFileName());
	copy_change.changesInit(this.index_changes);
	copy_change.setORTHO(this.hasORTHO());
	return copy_change; }
      
    public void setLabelVector(Vector new_label_vec) {
	super.setLabelVector(new_label_vec); }

    public void setEndVector(Vector new_end_vec) {
	super.setEndVector(new_end_vec); }

    public Vector getEndVector() {
	return (super.getEndVector()); }

    public Vector getLabelVector() {
	return (super.getLabelVector()); }


    public boolean coIndex(Node node_1, Node node_2) {
	String new_dex, new_label; 

	new_dex = super.getNewLabelDex();
	new_label = node_1.getLabel() + "-" + new_dex;
	super.SetLabelAt(new_label, node_1.getIndex());
	new_label = node_2.getLabel() + "-" + new_dex;
	super.SetLabelAt(new_label, node_2.getIndex());
	orderIndices();
	return true; }

    public boolean AddInternalNode(String new_label, Node start_node, 
				   Node finish_node, boolean update_me) {
	int start, finish;
	start = start_node.getIndex_int();
	finish = finish_node.getIndex_int();
	if (finish < start) {
	    return (AddInternalNode(new_label, finish, start, update_me)); }
	return (AddInternalNode(new_label, start, finish, update_me)); }

    /*
      adds node spanning from start node to finish node.
    */
    public boolean AddInternalNode (String new_label, int start, int finish,
				    boolean update_me) { 
	int i, up_start = start, up_finish = finish;
	Integer old_end;

	if (update_me) {
	    up_finish = getUpdate(finish);
	    up_start = getUpdate(start); }
	// get the EndDex, to prevent crossing branches.
	up_finish = (super.EndDexAt(up_finish)).intValue(); 
	if (!isLegalInternalNode(up_start, up_finish)) { return false; }
	// retain preceding nodes, adjusting end indices as necessary.
	ShiftPreEnds(up_start, up_finish, 1);
	// move following nodes ahead one space to make room for new node;
	// update end indices.
	ShiftRight(up_start, 1);
	for (i = start; i < index_changes.size(); i++) {
	    this.update(i, 1); }
	// add new node.
	super.SetLabelAt(new_label, up_start);
	super.SetEndDexAt(up_finish + 1, up_start);
	return true;
    } // end method AddInternalNode 

    public boolean isLegalInternalNode(int start, int finish) {
	int i, end;

	// looks for proposed crossing branches.
	// first, is there a branch before start that ends between
        // start and finish?
	for (i = 0; i < start; i++) {
	    end = (super.EndDexAt(i)).intValue();
	    if (start <= end && end < finish) { return false; } }
	// is there a branch after start that ends after finish?
	for (i = start; i < finish; i++) {
	    end = (super.EndDexAt(i)).intValue();
	    if (end > finish) { return false; } }
	return true; }

    /*
      inserts leaf after specified index. 
    */
    public boolean InsertLeafAfter (String pos_label, String text_label, 
				    int new_dex, boolean update_me) {
        int i, up_start_dex = new_dex, up_end_dex, end_dex;

        try {
	    end_dex = super.intEndDexAt(new_dex);
	    if (update_me) {
		up_start_dex = getUpdate(new_dex); }
	    up_end_dex = (super.EndDexAt(up_start_dex)).intValue();
            // retain preceding nodes, adjusting end indices as necessary.
            ShiftPreEnds(up_start_dex, up_end_dex, 2);
            // move following nodes ahead 2 spaces to make room for new leaf
            ShiftRight(up_end_dex + 1, 2);
            // update moved indices.
            for (i = end_dex + 1; i < index_changes.size(); i++) {
                this.update(i, 2); }
            // add new nodes.
            super.SetLabelAt(pos_label, up_end_dex + 1);
            super.SetEndDexAt(up_end_dex + 2, up_end_dex + 1);
            super.SetLabelAt(text_label, up_end_dex + 2);
            super.SetEndDexAt(up_end_dex + 2, up_end_dex + 2);
        }
        catch (Exception e) { e.printStackTrace(); return false; }
        finally { return true; } }

    /*
      inserts leaf before specified index. 
    */
    public boolean InsertLeafBefore (String pos_label, String text_label, 
				     int new_dex, boolean update_me) {
        int i, up_start_dex = new_dex, up_end_dex, end_dex;

        try {
	    end_dex = super.intEndDexAt(new_dex);
	    if (update_me) {
		up_start_dex = getUpdate(new_dex); }
	    up_end_dex = (super.EndDexAt(up_start_dex)).intValue();
            // retain preceding nodes, adjusting end indices as necessary.
            ShiftPreEnds(up_start_dex, up_end_dex, 2);
            // move nodes ahead 2 spaces to make room for new leaf
            ShiftRight(up_start_dex -1, 2);
            // update moved indices.
            for (i = new_dex + 1; i < index_changes.size(); i++) {
                this.update(i, 2); }
            // add new nodes.
            super.SetLabelAt(pos_label, up_start_dex);
            super.SetEndDexAt(up_start_dex + 1, up_start_dex);
            super.SetLabelAt(text_label, up_start_dex + 1);
            super.SetEndDexAt(up_start_dex + 1, up_start_dex + 1);
	    //super.PrintToSystemErr(up_start_dex - 1, up_end_dex + 2);
        }
        catch (Exception e) { e.printStackTrace(); return false; }
        finally { return true; } }

    public boolean DeleteInNode (Node node_to_delete, boolean update_me) {
	return (DeleteInNode(node_to_delete.getIndex_int(), update_me)); }

    public boolean DeleteInNode (int dex_to_delete, boolean update_me) {
        int i, up_delete = dex_to_delete;

        try {
	    if (update_me) {
		up_delete = getUpdate(dex_to_delete); }
            //      if (super.IsRoot(up_delete)) {
            //  return false; }
            if (super.IsLeafText(up_delete) || super.IsLeafPOS(up_delete)) {
                return (false); }
            // retain preceding nodes, adjusting end indices as necessary.
            ShiftPreEnds(up_delete, up_delete, -1);
            // delete affected node.
            super.deleteNode(up_delete);
            // change end indices of nodes after the deleted node.
            ShiftFollowEnds(up_delete, -1);
            // update end indices.
            for (i = dex_to_delete; i < index_changes.size(); i++) {
                this.update(i, -1); }
        }
        catch (Exception e) { e.printStackTrace(); return false; }
        finally { return true; } }

    public boolean RazeNode(int dex_to_raze, boolean update_me) {
	Vector trace_dexes = new Vector();
	return (RazeNode(dex_to_raze, update_me, trace_dexes)); }

    public boolean RazeNode (Node node_to_raze, boolean update_me) {
	Vector trace_dexes = new Vector();
	return (RazeNode(node_to_raze.getIndex_int(), update_me, 
			 trace_dexes)); }

    public boolean RazeNode (Node node_to_raze, boolean update_me,
			     Vector trace_dexes) {
	return (RazeNode(node_to_raze.getIndex_int(), update_me,
			 trace_dexes)); }

    public boolean RazeNode (int dex_to_raze, boolean update_me,
			     Vector trace_dexes) {
        int i, up_raze=dex_to_raze, new_leaf_end; 
	int diff, raze_end, new_raze_end, new_end_dex;
	ChangeTree newTree = new ChangeTree();
	Vector descendants;
	Node desc;
	String new_label;

        try {
	    if (update_me) {
		up_raze = getUpdate(dex_to_raze); }
            if (super.IsLeafText(up_raze) || super.IsLeafPOS(up_raze)) {
                return (false); }
	    copyOver(newTree, 0, dex_to_raze);
	    raze_end = super.intEndDexAt(dex_to_raze);
	    descendants = super.GetDescendants(dex_to_raze);
	    new_leaf_end = dex_to_raze + 2;
	    for (i = 0; i < descendants.size(); i++) {
		desc = (Node)descendants.elementAt(i);
		if (super.IsLeafPOS(desc)) {
		    if (!inTraceDexes(desc, trace_dexes)) { 
			newTree.AddItem(desc.getLabel(), new_leaf_end);
			i += 1;
			desc = (Node)descendants.elementAt(i);
			newTree.AddItem(desc.getLabel(), new_leaf_end); 
			new_leaf_end += 2; }}}
	    // get diff as a negative number, for later end shifting.
	    new_raze_end = newTree.size();
	    diff = new_raze_end - raze_end - 1;
	    //System.err.println("diff:  " + diff);
	    newTree.ShiftPreEnds(dex_to_raze + 1, raze_end, diff);
	    copyOver(newTree, raze_end + 1, this.size() - 1);
	    newTree.ShiftFollowEnds(new_raze_end, diff, this.size() - 1);
	    //newTree.setFileName(super.getFileName());
	    //newTree.setNumID(super.getNumID());
	    //newTree.PrintToSystemErr();
	    //copy newTree onto super tree.
	    super.removeAllElements();
	    for (i = 0; i < newTree.size(); i++) {
		new_label = newTree.labelAt(i);
		super.setLabelAt(new_label, i);
		new_end_dex = newTree.intEndDexAt(i);
		super.SetEndDexAt(new_end_dex, i); }
	    //super.PrintToSystemErr();
        }
        catch (Exception e) { e.printStackTrace(); return false; }
        finally { return true; } }

    public boolean inTraceDexes (Node desc, Vector trace_dexes) {
	int j, node_dex, trace_dex;

	node_dex = desc.getIndex_int();
	for (j = 0; j < trace_dexes.size(); j++) {
	    trace_dex = ((Integer)trace_dexes.elementAt(j)).intValue();
	    if (node_dex == trace_dex) { return true; }}
	return false; }
	    
	


    public boolean DeleteSubtree (int dex_to_delete, boolean update_me) {
        int i, num_nodes, move_end, up_delete, end_delete;
	Node mom, child;

        try {
	    end_delete = super.intEndDexAt(dex_to_delete);
	    up_delete = dex_to_delete;
	    if (update_me) {
		up_delete = getUpdate(dex_to_delete); }
	    //      if (super.IsRoot(up_delete)) {
	    //  return false; }
	    if (super.IsLeafText(up_delete) || super.IsLeafPOS(up_delete)) {
		return (false); }
	    child = super.NodeAt(up_delete);
	    mom = GetMother(up_delete);
	    if (isOnlyChild(child, mom)) { return false; }
	    move_end = (super.EndDexAt(up_delete)).intValue();
	    num_nodes = move_end - up_delete + 1;
            // retain preceding nodes, adjusting end indices as necessary.
            ShiftPreEnds(up_delete, up_delete, -num_nodes);
            // delete affected nodes.
	    for (i = 0; i < num_nodes; i++) {
		super.deleteNode(up_delete); }
            // change end indices of nodes after the deleted node.
            ShiftFollowEnds(up_delete, -num_nodes);
            // update end indices.
            for (i = end_delete; i < index_changes.size(); i++) {
                this.update(i, -num_nodes); }
        }
        catch (Exception e) { e.printStackTrace(); return false; }
        finally { return true; } }

    /*
      deletes leaf at specified pos_index.
    */
    public boolean DeleteLeaf (int dex_to_delete, boolean update_me) {
        int i, up_delete = dex_to_delete;
        Integer old_end;
	Node mom, child;

        try {
	    if (update_me) {
		up_delete = getUpdate(dex_to_delete); }
	    if (super.IsLeafText(up_delete)) {
		dex_to_delete -= 1;
		up_delete -= 1; }
	    child = super.NodeAt(up_delete);
	    mom = GetMother(up_delete);
	    if (isOnlyChild(child, mom)) { return false; }
	    if (!(super.IsLeafPOS(up_delete))) {
		return (false); }
            // retain preceding nodes, adjusting end indices as necessary.
	    ShiftPreEnds(up_delete, up_delete, -2);
	    // delete affected nodes.
	    super.deleteNode(up_delete);
	    super.deleteNode(up_delete);
            // change end indices of nodes after the deleted node.
	    ShiftFollowEnds(up_delete, -2);
            // update end indices.
            for (i = dex_to_delete; i < index_changes.size(); i++) {
                this.update(i, -2); }
        }
        catch (Exception e) { e.printStackTrace(); return false; }
        finally { return true; } }

    public boolean DeleteLeaf (Node nodal, boolean update_me) {
	return (DeleteLeaf(nodal.getIndex_int(), update_me)); }

    public boolean RogueMove (Node move, Node targ, boolean updat) {
	
	int i, j, delete_dex, shift_sz, new_end, old_dex, anc_dex, end_anc_dex;
	int start_move, end_move, start_target, end_target;
	int up_start_move, up_end_move, up_start_target = 0, up_end_target;
	String label;
	ChangeTree tree_parts = new ChangeTree();
	Vector ancestors;
	Node ancestor;

	try {
	   // first, trace precedes antecedent.
	    start_move = move.getIndex_int();
	    end_move = super.intEndDexAt(start_move);
	    start_target = targ.getIndex_int();
	    up_start_move = start_move;
	    up_start_target = start_target;
	    if (updat) {
		up_start_move = getUpdate(start_move);
		up_start_target = getUpdate(start_target); }
	    up_end_target = (super.EndDexAt(up_start_target)).intValue();
	    up_end_move = (super.EndDexAt(up_start_move)).intValue();
	    //tree_parts = new ChangeTree();
	    copyOver(tree_parts, up_start_move, up_end_move);
	    // strip index from antecedent label.
	    label = tree_parts.LabelAt(0);
	    label = label.substring(0, label.lastIndexOf('-'));
	    tree_parts.SetLabelAt(label, 0);
	    ancestors = super.GetAncestors(up_start_target);
	    this.DeleteSubtree(up_start_move, false);
	    this.DeleteLeaf(up_start_target, false);
	    // first, trace precedes antecedent.
	    if (up_start_move > up_start_target) {
		// retain preceding nodes, adjusting end indices as necessary.
		ShiftPreEnds(up_start_target, up_start_target, 
			     tree_parts.size());
		// insert subtree.
		shift_sz = up_start_move - up_start_target;
		for (i = 0; i < tree_parts.size();  i++) {
		    label = tree_parts.LabelAt(i);
		    new_end = tree_parts.intEndDexAt(i) - shift_sz; 
		    super.insertNode(label, new_end, up_start_target + i); }
		this.ShiftFollowEnds(up_start_target + tree_parts.size(), 
				 tree_parts.size(), this.size() - 1);
		// update moved indices.
		for (j = start_move; j <= end_move; j++) {
		    this.update(j, -shift_sz); }
		for (j = start_target; j <= start_target + shift_sz; j++) {
		    this.update(j, tree_parts.size()); }
		// fix end indices of ancestors.
		for (j = 0; j < ancestors.size(); j++) {
		    ancestor = (Node)ancestors.elementAt(j);
		    anc_dex = ancestor.getIndex_int();
		    end_anc_dex = super.intEndDexAt(anc_dex);
		    super.SetEndDexAt(end_anc_dex + tree_parts.size(), 
				      anc_dex); }
	    }

	    // or, antecedent precedes trace.
	}
	catch (Exception e) { e.printStackTrace(); return false; }
	finally { 
	    //super.PrintToSystemErr(53, 93);
	    return true; } }

    public boolean reconstruct(ArgList recon_arg) { 
	// inserts antecedents at trace.
	int n;
	Node current, ant;
	boolean did_it;

	tree_loop:  for (n = 0; n < super.size(); n++) {
	    current = super.NodeAt(n);
	    if (super.IsLeafPOS(current)) {
		ant = super.getAntecedent(current);
		if (ant.IsNullNode() || 
		    !recon_arg.hasMatch(current)) {
		    continue tree_loop; }
		did_it = RogueMove(ant, current, true);
	        if (!did_it) { return false; }}}
	return true; }


    public boolean isLegalMove (Node to_move, Node target, Node common,
				Vector to_move_ancestors, 
				Vector target_ancestors) {
	int i, common_dex;
	Node previous, next, to_move_previous, target_previous;
	Node to_move_mom = super.GetMother(to_move);
	Node target_mom = super.GetMother(target);

	
	if (to_move.equals(target)) { return false; }
	if (super.IsLeafText(to_move) || super.IsLeafPOS(target) ||
	    super.IsLeafText(target)) {
	    return false; }
	if (isOnlyChild(to_move, to_move_mom)) { return false; }
	if (iPres(to_move, target) || iPres(target, to_move)) {
	    return true; }
	if (isLastChild(to_move, to_move_mom) || 
	   (to_move_mom.equals(common) && to_move.lessThan(target))) {
	    // must be a path of last children up to common ancestor.
	    previous = to_move;
	    to_move_previous = to_move;
	    for (i = 0; i < to_move_ancestors.size(); i++) {
		next = (Node)to_move_ancestors.elementAt(i);
		if (next.equals(target)) { return true; }
		if (next.equals(common)) {
		    to_move_previous = previous;
		    break; }
		if (!isLastChild(previous, next)) { return false; }
		previous = next; }
	    common_dex = getVectorIndex(common, target_ancestors);
	    target_previous = target;
	    if (common_dex == 0) {
		target_previous = target; }
	    else {
		target_previous = (Node)target_ancestors.
		    elementAt(common_dex - 1); }
	    if (!iPres(to_move_previous, target_previous)) {
		return false; }
	    previous = target_previous;
	    for (i = common_dex - 2; i >= 0 ; i--) {
		next = (Node)target_ancestors.elementAt(i);
		if (!isFirstChild(next, previous)) { return false; }
		previous = next; }
	    return true; }
	if (isFirstChild(to_move, to_move_mom) ||
	   (to_move_mom.equals(common) && to_move.greaterThan(target))) {
	    // must be a path of first children up to common ancestor.
	    previous = to_move;
	    to_move_previous = to_move;
	    for (i = 0; i < to_move_ancestors.size(); i++) {
		next = (Node)to_move_ancestors.elementAt(i);
		if (next.equals(target)) { return true; }
		if (next.equals(common)) {
		    to_move_previous = previous;
		    break; }
		if (!isFirstChild(previous, next)) { return false; }
		previous = next; }
	    common_dex = getVectorIndex(common, target_ancestors);
	    target_previous = target;
	    if (common_dex == 0) {
		target_previous = target; }
	    else {
		target_previous = (Node)target_ancestors.
		    elementAt(common_dex - 1); }
	    if (!iPres(target_previous, to_move_previous)) {
		return false; }
	    previous = target_previous;
	    for (i = common_dex - 2; i >= 0 ; i--) {
		next = (Node)target_ancestors.elementAt(i);
		if (!isLastChild(next, previous)) { return false; }
		if (next.equals(target)) { return true; }
		previous = next; }
	    return true; }

	return false; }

    public boolean MoveTo(Node to_move, Node target, boolean update_me) {
	int move_dex, target_dex;
	move_dex = to_move.getIndex_int();
	target_dex = target.getIndex_int();
	return (MoveTo(move_dex, target_dex, update_me)); }

    public boolean MoveTo(int move_dex, int target_dex, boolean update_me) {
	int up_move = move_dex, up_target = target_dex;
	Node to_move, target, common, anc;
	Vector to_move_ancestors, target_ancestors;
	int i, common_dex;
	boolean did_it, update_next = false;

	if (update_me) {
	  up_move = getUpdate(move_dex);
	  up_target = getUpdate(target_dex); }
	to_move = super.NodeAt(up_move);
	target = super.NodeAt(up_target);
	common = super.GetCommonAncestor(to_move, target);
	to_move_ancestors = super.GetAncestors(to_move);
	target_ancestors = super.GetAncestors(target);
	if (!isLegalMove(to_move, target, common, 
			 to_move_ancestors, target_ancestors)) {
	    return false; }
	// move to_move up to common ancestor.
	for (i = 0; i < to_move_ancestors.size(); i++) {
	    anc = (Node)to_move_ancestors.elementAt(i);
	    if (anc.equals(target)) { return true; }
	    if (anc.equals(common)) { break; }
	    did_it = MoveUp(to_move, update_next);
	    update_next = true;
	    //System.err.println("moved up: to_move, anc:  " + to_move.toString() +", " + anc.toString());
	    //PrintChangesToSystemErr(2, 10);
	    if (!did_it) { 
		//System.err.println("did not do it:  " + to_move.toString() + ", " + anc.toString());
		return false; } }

	//	System.err.println("about to descend:  ");
	//this.PrintChangesToSystemErr(0, 10);
	// now, descend to target.
	common_dex = getVectorIndex(common, target_ancestors);
	for (i = common_dex - 1; i >= 0; i--) {
	    anc = (Node)target_ancestors.elementAt(i);
	    did_it = makeDaughter(anc, to_move, update_next);
	    update_next = true;
	    if (!did_it) {
		return false; } }
	did_it = makeDaughter(target, to_move, update_next);
	return did_it;
    }

    public int getVectorIndex(Node nodal, Vector node_vec) {
	Node nord;

	for (int i = 0; i < node_vec.size(); i++) {
	    nord = (Node)node_vec.elementAt(i);
	    if (nord.equals(nodal)) { return i; } }
	return -3; }

    public boolean MoveUpManyRight(Node to_move, Node target, 
				   boolean update_me) {
	Vector ancestors = super.GetAncestors(to_move);
	Node mumsie;
	int i;

	// MoveUpRight until new mother is reached.
	for (i = 0; i < ancestors.size(); i++) {
	    mumsie = (Node)ancestors.elementAt(i);
	    if (mumsie.equals(target)) { return true; }
	    MoveUpRight(to_move, mumsie, update_me); }
	return false; }

    public boolean MoveUp(Node nodal, boolean update_me) {
	return (MoveUp(nodal.getIndex_int(), update_me)); }
    
    public boolean MoveUp(Node node1, Node node2, boolean update_me) {
	int dex1, dex2;

	dex1 = node1.getIndex_int();
	dex2 = node2.getIndex_int();
	if (dex1 < dex2) {
	    return (MoveUpMulti(dex1, dex2, update_me)); }
	else {
	    return (MoveUpMulti(dex2, dex1, update_me)); } }

    public boolean MoveUp(int dex_to_move, boolean update_me) {
	Node mother, child;
	int up_dex = dex_to_move;

	if (update_me) {
	    up_dex = getUpdate(dex_to_move); }
	mother = GetMother(up_dex);
	child = super.NodeAt(up_dex);
	//	System.err.println("in MoveUp:  mother, child:  " + mother.toString() + ", " + child.toString()); 
	if (isFirstChild(child, mother)
	    && !isLastChild(child, mother)) {
	    return (MoveUpLeft(child, mother, false)); }
	if (isLastChild(child, mother)
	    && !isFirstChild(child, mother)) {
	    return (MoveUpRight(child, mother, false)); }
        else { return false; }
    }

    public boolean MoveUpMulti(int start, int finish, boolean update_me) {
	Node start_mom, finish_mom, ancestor;
	Node start_move, finish_move; 
	int up_start = start, up_finish = finish;

	if (update_me) {
	    up_start = getUpdate(start);
	    up_finish = getUpdate(finish); }
	start_move = super.NodeAt(up_start);
	finish_move = super.NodeAt(up_finish);
	start_mom = GetMother(up_start);
	finish_mom = GetMother(up_finish);
	// if start_move and finish_move have the same
	// mother, they are sisters.
	if (start_mom.equals(finish_mom)) {
	    if (isFirstChild(start_move, start_mom)
		&& !isLastChild(finish_move, start_mom)) {
		return (MoveUpLeftMulti(start_move, finish_move, start_mom, 
					start, finish, update_me)); }
	    if (isLastChild(finish_move, start_mom)
		&& !isFirstChild(start_move, start_mom)) {
		return (MoveUpRightMulti(start_move, finish_move, start_mom,
					 update_me)); }
	    return false;
	}
	// if not sisters, do single move up on 
	// common ancestor.
	ancestor = GetCommonAncestor(start, finish);
	return (MoveUp(ancestor.getIndex_int(), update_me));
    }

    public boolean makeDaughter (Node mom, Node daughter, boolean update_me) {
	return (makeDaughter(mom.getIndex_int(), daughter.getIndex_int(), 
		update_me)); }

    public boolean makeDaughter (int mom_dex, int daughter_dex, 
				 boolean update_me) {
	int daughter_end, up_mom = mom_dex, up_daughter = daughter_dex, i;
	Node mom, daughter;

	if (update_me) {
	    up_mom = getUpdate(mom_dex);
	    up_daughter = getUpdate(daughter_dex); }
	mom = super.NodeAt(up_mom);
	daughter = super.NodeAt(up_daughter);
	// shameless hack.
	if (super.IsLeafPOS(mom) || super.IsLeafText(mom) ||
	    super.IsLeafText(daughter)) {
	    mom = super.NodeAt(mom_dex);
	    daughter = super.NodeAt(daughter_dex); }
	//	    return false; }
	// simplest case -- mom iprecedes would-be daughter.
	if (iPres(mom, daughter )) {
	    daughter_end = super.intEndDexAt(daughter.getIndex());
	    setAncestors(mom, daughter_end);
	    return true; }
	// next case -- daughter iprecedes would-be mother.
	if (iPres(daughter, mom)) {
	    // insert mother before daughter.
	    super.insertNode(mom.getLabel(), super.EndDexAt(mom.getIndex()), 
			     daughter.getIndex_int());
	    // fix end dexes between new mother and old mother.
	    ShiftFollowEnds(daughter.getIndex_int() + 1, 
			    1, mom.getIndex_int());
	    // delete old mother.
	    super.deleteNode(mom.getIndex_int() + 1);
	    // set updates.
	    //for(i = daughter.getIndex_int();
	    for (i = daughter_dex;
		 i < super.intEndDexAt(mom.getIndex()); i++) {
		this.update(i, 1); }
	    this.setUpdate(mom.getIndex(), daughter.getIndex());
	    return true; }
	return false; }

    public boolean extendSpan (Node mom_node, Node daughter_node,
			       boolean update_me) {
	int mom_dex, daughter_dex;

	mom_dex = mom_node.getIndex_int();
	daughter_dex = daughter_node.getIndex_int();
	return (extendSpan(mom_dex, daughter_dex, update_me)); }

    public boolean extendSpan (int mom_dex, int daughter_dex, 
				 boolean update_me) {
	int daughter_end, up_mom = mom_dex, up_daughter = daughter_dex, i;
	int mom_sib_dex, daughter_sib_dex;
	Node mom, daughter;
	Vector all_sibs;

	if (update_me) {
	    up_mom = getUpdate(mom_dex);
	    up_daughter = getUpdate(daughter_dex); }
	mom = super.NodeAt(up_mom);
	daughter = super.NodeAt(up_daughter);
	// shameless hack.
	if (super.IsLeafPOS(mom) || super.IsLeafText(mom) ||
	    super.IsLeafText(daughter)) {
	    mom = super.NodeAt(mom_dex);
	    daughter = super.NodeAt(daughter_dex); }
	//	    return false; }
	// simplest case -- mom is preceding sister of would-be daughter.
	all_sibs = super.GetAllSisters(daughter);
	mom_sib_dex = getSibDex(all_sibs, mom);
	if (mom_sib_dex < 0) { return false; }
	daughter_sib_dex = getSibDex(all_sibs, daughter);
	// first case -- daughter is following sister of would-be mother.
	if (mom_sib_dex < daughter_sib_dex) {
	    daughter_end = super.intEndDexAt(daughter.getIndex());
	    setAncestors(mom, daughter_end);
	    return true; }
	// next case -- daughter is previous sister of would-be mother.
	if (daughter_sib_dex < mom_sib_dex) {
	    // insert mother before daughter.
	    super.insertNode(mom.getLabel(), super.EndDexAt(mom.getIndex()), 
			     daughter.getIndex_int());
	    // fix end dexes between new mother and old mother.
	    ShiftFollowEnds(daughter.getIndex_int() + 1, 
			    1, mom.getIndex_int());
	    // delete old mother.
	    super.deleteNode(mom.getIndex_int() + 1);
	    // set updates.
	    //for(i = daughter.getIndex_int();
	    for (i = daughter_dex;
		 i < super.intEndDexAt(mom.getIndex()); i++) {
		this.update(i, 1); }
	    this.setUpdate(mom.getIndex(), daughter.getIndex());
	    //super.PrintToSystemErr(45, 50);
	    //this.PrintChangesToSystemErr(45, 50);
	    return true; }
	return false; }

    /**
     * when changing the end_dex on one node to include more descendants, 
     * also change all its ancestor nodes to include the descendants.
     */
    public void setAncestors (Node mom, int new_end) {
	Vector ancestors;
	Node ancestor;
	
	ancestors = super.GetAncestors(mom);
	ancestors.addElement(mom);
	for (int i = 0; i < ancestors.size(); i++) {
	    ancestor = (Node)ancestors.elementAt(i);
	    if (super.intEndDexAt(ancestor.getIndex()) < new_end) {
		super.SetEndDexAt(new_end, ancestor.getIndex_int()); } }
	return; }
		
    public boolean iPres (Node one, Node two) {
	int one_start, one_end, two_start;

	one_start = one.getIndex_int();
	one_end = (super.EndDexAt(one_start)).intValue();
	two_start = two.getIndex_int();
	if (one_end + 1 == two_start) {
	    return true; }
	else { return false; } }

    public boolean isFirstChild(int dex) {
	Node child, mother;

	child = super.NodeAt(dex);
	mother = super.GetMother(child);
	return(isFirstChild(child, mother)); }

    public boolean isFirstChild(Node child, Node mother) {
	if (child.getIndex_int() == mother.getIndex_int() + 1) {
	    return true; }
	return false; }

    /**
       input nodes are assumed to be mother and child.
    **/
    public boolean isLastChild(Node child, Node mother) {
	Integer mom_end = super.EndDexAt(mother.getIndex());
	Integer child_end = super.EndDexAt(child.getIndex());
	if (child_end.equals(mom_end)) {
	    return true; }
	return false; }

    public boolean isOnlyChild(Node child, Node mother) {
	if (isFirstChild(child, mother) && isLastChild(child, mother)) {
	    return true; }
	return false; }

    public boolean MoveUpRight(Node child, Node mother, boolean update_me) {
	return (MoveUpRight(child.getIndex_int(), mother.getIndex_int(),
			    update_me)); }

    public boolean MoveUpRight(int old_child_dex, int old_mom_dex, 
			       boolean update_me) {
	int up_dex = old_child_dex, mom_dex = old_mom_dex;

	if (update_me) {
	    up_dex = getUpdate(old_child_dex);
	    mom_dex = getUpdate(old_mom_dex); }
	super.SetEndDexAt(up_dex - 1,mom_dex);
	// since no nodes were moved, no updates are needed.
	return true; }

    public boolean MoveUpLeft (Node child, Node mother, boolean update_me) {
	return (MoveUpLeft(child.getIndex_int(), mother.getIndex_int(), 
			   update_me)); }

    public boolean MoveUpLeft(int old_child_dex, int old_mom_dex,
			      boolean update_me) {
	int i, num_nodes, move_end, old_dex, mom_diff;
	int mom_dex = old_mom_dex, up_dex = old_child_dex;
	int diff, old_spot, new_spot;
	String label;

	if (update_me) {
	    up_dex = getUpdate(old_child_dex);
	    mom_dex = getUpdate(old_mom_dex); }
	move_end = (super.EndDexAt(up_dex)).intValue();
	num_nodes = move_end - up_dex + 1;
	// move nodes ahead to make room for moved nodes.
	// start with up_dex's mother.
	ShiftRight(mom_dex, num_nodes);
	// move up left.
	diff = up_dex - mom_dex;
	//System.err.println("diff:  " + diff);
	for (i = 0; i < num_nodes; i++) {
	    old_spot = mom_dex + num_nodes + diff + i;
	    new_spot = mom_dex + i;
	    label = super.LabelAt(old_spot);
	    super.SetLabelAt(label, new_spot);
	    old_dex = (super.EndDexAt(old_spot)).intValue();
            super.SetEndDexAt(old_dex - num_nodes - diff, new_spot); }
	// now, delete old copies of the moved nodes.
	for (i = 0; i < num_nodes; i++) {
	    old_spot = mom_dex + num_nodes + diff;
	    super.deleteNode(old_spot); }
	// correct end indices after deleted nodes.
	ShiftFollowEnds(mom_dex + num_nodes, -num_nodes);
	// update moved nodes.
	//System.err.println("num_nodes:  " + num_nodes);
	for (i = 0; i < num_nodes; i++) {
	    update(old_child_dex + i, -diff); }
	    //	    update(old_child_dex + i, -1); }
	// update mother node.
	mom_diff = old_mom_dex - getUpdate(old_mom_dex) + num_nodes;
	update (old_mom_dex, mom_diff);
	return true;
    }

    public boolean MoveUpRightMulti(Node start_move, Node finish_move, 
				    Node start_mom, boolean update_me){
	// Since only end_dexes have been changed, no
        // updating is necessary.
	super.SetEndDexAt(start_move.getIndex_int() - 1,
                          start_mom.getIndex().intValue());
	return true;
    }

    public boolean MoveUpLeftMulti(Node start_move, Node finish_move,
				   Node mother, int start, int finish,
				   boolean update_me) {
	int move_end, up_dex, old_spot, new_spot; 
	int i, num_nodes, diff, mom_dex, old_dex, finish_dex;
	String label;

	finish_dex = finish_move.getIndex_int();
	up_dex = start_move.getIndex_int();
        move_end = (super.EndDexAt(finish_dex)).intValue();
        num_nodes = move_end - up_dex + 1;
        mom_dex = mother.getIndex_int();
        // move nodes ahead to make room for moved nodes.
        // start with up_dex's mother.
        ShiftRight(mom_dex, num_nodes);
        // move up left.
        diff = up_dex - mom_dex;
        for (i = 0; i < num_nodes; i++) {
            old_spot = mom_dex + num_nodes + diff + i;
            new_spot = mom_dex + i;
            label = super.LabelAt(old_spot);
            super.SetLabelAt(label, new_spot);
            old_dex = (super.EndDexAt(old_spot)).intValue();
            super.SetEndDexAt(old_dex - num_nodes - diff, new_spot); }
        // now, delete old copies of the moved nodes.
        for (i = 0; i < num_nodes; i++) {
            old_spot = mom_dex + num_nodes + diff;
            super.deleteNode(old_spot); }
        // correct end indices after deleted nodes.
        ShiftFollowEnds(mom_dex + num_nodes, -num_nodes);
        // update moved nodes.
        for (i = 0; i < num_nodes; i++) {
            update(start + i, -diff); }
        // update mother node.
        update (start - 1, num_nodes);
        return true;
    }

    public void ChangeLabel (String newlabel, Node nodal, boolean update_me) {
	ChangeLabel(newlabel, nodal.getIndex_int(), update_me); 
        return; }

    public void ChangeLabel (String newlabel, int x, boolean update_me) {
        int new_x = x;
  
        if (update_me) { new_x = getUpdate(x); }
        super.SetLabelAt(newlabel, new_x);
        return; }

    public void ChangeLabel (String taskname, String newlabel, int x, 
			     boolean update_me) {
        int new_x = x;
        String changed, oldlabel;

	if (update_me) {
	    new_x = getUpdate(x); }
        oldlabel = super.LabelAt(new_x);
        changed = changedLabel(taskname, newlabel, oldlabel);
        super.SetLabelAt(changed, new_x);
        return; }

    private String changedLabel(String taskname, String newlabel, 
				String oldlabel) {
        if (taskname.equals("replace_label")) {
            return newlabel; }
        if (taskname.equals("append_label")) {
            return(oldlabel + newlabel); }
	if (taskname.equals("prepend_label")) {
	    return(newlabel + oldlabel); }
        if (taskname.equals("post_crop_label")) {
            return(oldlabel.substring(0, oldlabel.indexOf(newlabel))); }
        if (taskname.equals("pre_crop_label")) {
            return(oldlabel.substring(oldlabel.indexOf(newlabel)+1,
                                       oldlabel.length())); }
        return ("BULLWINKLE");
    }

    public void MergePrevious(ChangeTree prev_tree) {
	Node prev_root, this_root;
	int prev_root_dex, prev_root_end, prev_size;
	int root_diff, this_root_dex, i;

	//prev_tree.PrintToSystemErr();
	//this.PrintToSystemErr();
	prev_root = prev_tree.getRootNode();
	prev_root_dex = prev_root.getIndex_int();
	prev_root_end = prev_tree.intEndDexAt(prev_root_dex);
	prev_size = prev_root_end - prev_root_dex + 1;
	this_root = this.getRootNode();
	this_root_dex = this_root.getIndex_int();
	// make room to merge previous tree.
	this.ShiftRight(this_root_dex + 1, prev_size);
	// merge previous tree, containing root.
	this.addSubTree(prev_tree, this_root_dex, 
			prev_root_dex - 1, prev_size);
	// adjust end indices of merged prev_tree.
	root_diff = this_root_dex - prev_root_dex + 1;
	this.ShiftFollowEnds(this_root_dex + 1, root_diff, 
			     this_root_dex + prev_size);
	// adjust end indices of this_tree in nodes before merged prev_tree.
	this.ShiftPreEnds(this_root_dex + 1, this_root_dex, prev_size);
	//this.PrintToSystemErr();
        // update.
	//	this.changesInit();
	for (i = this_root_dex + 1; i < index_changes.size(); i++) {
	    this.update(i, prev_size); }
        }

    public void MergeFollowing(ChangeTree follow_tree) {
	Node follow_root, this_root;
	int follow_root_dex, follow_root_end, follow_size;
	int diff, this_root_dex, this_root_end;

	follow_root = follow_tree.getRootNode();
	follow_root_dex = follow_root.getIndex_int();
	follow_root_end = follow_tree.intEndDexAt(follow_root_dex);
	follow_size = follow_root_end - follow_root_dex + 1;
	this_root = this.getRootNode();
	this_root_dex = this_root.getIndex_int();
	this_root_end = super.intEndDexAt(this_root_dex);
	// make room to merge following tree.
	this.ShiftRight(this_root_end + 1, follow_size);
	// merge following tree.
	this.addSubTree(follow_tree, this_root_end, 
			follow_root_dex - 1, follow_size);
	// adjust end indices of merged follow_tree.

	diff = this_root_end - this_root_dex + 1;
	this.ShiftFollowEnds(this_root_end + 1, diff, 
			     this_root_end + follow_size);
	// adjust end indices of this before merged follow_tree.
	this.ShiftPreEnds(this_root_dex + 1, this_root_end, follow_size); }
    //this.PrintToSystemErr(); }

    private void addSubTree(ChangeTree to_add, int this_start, 
			    int sub_start, int how_much) {
	int this_dex, add_dex, i;

	for (i = 1; i <= how_much; i++) {
	    this_dex = this_start + i;
	    add_dex = sub_start + i;
	    this.SetLabelAt(to_add.LabelAt(add_dex), this_dex);
	    this.SetEndDexAt(to_add.EndDexAt(add_dex), this_dex); }
	return; }

    // return 0 if not legit
    // return -1 if split at beginning of tree
    // return 1 if split at end of tree
    public int legitSplit (Node split_root) {
	Node this_root, split_mom;
	int end_split_root, split_size, split_dex;
	int split_end, this_root_dex, this_root_end;
	boolean moved;

	this_root = this.getRootNode();
	split_mom = this.GetMother(split_root);
	if (!split_mom.equals(this_root)) { return 0; }
	//  moved = this.MoveTo(split_root, this_root, false);
	//  if (moved == false) { return 0; }
	//  split_root = getUpdate(split_root); }
	this_root_dex = this_root.getIndex_int();
	this_root_end = super.intEndDexAt(this_root_dex);
	split_dex = split_root.getIndex_int();
	end_split_root = super.intEndDexAt(split_dex);
	if (end_split_root == this_root_end) { 
	    return 1; }
	if (this_root_dex + 1 == split_dex) {
	    return -1; }
	return 0; }

    public ChangeTree split(Node split_root) {
	// return the later of the two resulting trees.
	Node this_root, split_node, this_node;
	int i, end_split_root, split_size, split_dex, diff;
	int split_end, this_root_dex, this_root_end, this_end;
	ChangeTree new_tree;

	if (legitSplit(split_root) == 0) {
	    return this; }
	this_root = this.getRootNode();
	this_root_dex = this_root.getIndex_int();
	this_root_end = super.intEndDexAt(this_root_dex);
	split_dex = split_root.getIndex_int();
	end_split_root = super.intEndDexAt(split_dex);
	split_size = end_split_root - split_dex;
	new_tree = new ChangeTree();
	// first, easier case: split subtree is at end of to_split.
	if (end_split_root == this_root_end) {
	    // BUILD NEW TREE.
	    // get diff as a negative number, for later end shifting.
	    diff = this_root_dex - split_dex;
	    // copy over original tree up to but not including original root.
	    copyOver(new_tree, 0, this_root_dex - 1);
	    new_tree.ShiftPreEnds(this_root_dex, end_split_root, 
				  diff);
	    // copy over split subtree.
	    copyOver(new_tree, split_dex, end_split_root);
	    // copy over metadata after syntactic tree, e.g., ID node.
	    copyOver(new_tree, this_root_end + 1, this.size() - 1);
	    new_tree.ShiftFollowEnds(this_root_dex, diff);
	    // CHANGE THIS TREE.
	    // delete split_tree from this tree.
	    deleteBatch(split_dex, end_split_root);
	    this.ShiftPreEnds(this_root_dex + 1, split_dex, -split_size-1);
	    this.ShiftFollowEnds(split_dex, -split_size-1, this.size() - 1);
	    new_tree.setFileName(this.getFileName());
	    new_tree.setNumID(this.getNumID() + 1);
	    //new_tree.PrintToSystemErr();
	    return new_tree; }

	// second case; split subtree just after original root.
	if (this_root_dex + 1 == split_dex) {
	    // BUILD NEW TREE.
	    // get diff as a negative number, for later end shifting.
	    diff = split_dex - end_split_root;
	    // copy over original tree including original root.
	    copyOver(new_tree, 0, this_root_dex);
	    new_tree.ShiftPreEnds(this_root_dex + 1, split_dex, 
				  diff - 1);
	    // copy over everything after split subtree.
	    copyOver(new_tree, end_split_root + 1, this.size() - 1);
	    new_tree.ShiftFollowEnds(this_root_dex + 1, diff - 1);
	    // new_tree.PrintToSystemErr();
	    // CHANGE THIS TREE.
	    // delete stuff after split_tree from this tree.
	    deleteBatch(end_split_root + 1, this_root_end);
	    diff = end_split_root - this_root_end;
	    this.ShiftPreEnds(this_root_dex + 1, split_dex, diff);
	    this.ShiftFollowEnds(end_split_root + 1, diff, this.size() - 1);
	    // delete original root node.
	    this.DeleteInNode(super.getRootNode(), false);
	    //this.PrintToSystemErr();
	    new_tree.setFileName(this.getFileName());
	    new_tree.setNumID(this.getNumID() + 1);
	    return new_tree;
	}
	return new_tree;
    }

    public void copyOver (ChangeTree new_tree, int start, int up_to) {
	int i, this_end; 
	Node this_node;

	for (i = start; i <= up_to; i++) {
	    this_node = super.NodeAt(i);
	    this_end = super.intEndDexAt(i);
	    new_tree.AddItem(this_node.getLabel(), this_end); } }

    public void deleteBatch(int start, int finish) {

	for (int i = finish; i >= start; i--) {
	    super.deleteNode(i); } }

    public void orderIndices() {
	Vector pairs, sett;
	int i, j, dash_dex;
	Node nodal;
	String label, new_dex;
	Integer ex_machina;

	pairs = getTracePairs();
	for (i = 0; i < pairs.size(); i++) {
	    sett = (Vector)pairs.elementAt(i);
	    ex_machina = new Integer(i + 1);
	    new_dex = ex_machina.toString();
	    for (j = 0; j < sett.size(); j++) {
		nodal = (Node)sett.elementAt(j);
		label = nodal.getLabel();
		dash_dex = getLastDash(label);
		label = label.substring(0, dash_dex + 1);
		label += new_dex;
	        super.SetLabelAt(label, nodal.getIndex()); }}
	return; }

    public int getLastDash(String label) {
	int k;

	for (k = label.length() - 1; k >= 0; k--) {
	    if (label.charAt(k) == '-' || label.charAt(k) == '=') {
		return k; }}
	return -1; }

    

    public Vector getTracePairs() {
	Vector pairs = new Vector(), one_pair, ant;
	Vector done = new Vector();
	Node current, dexed;
	int i, j;
	Integer dexInt = new Integer(3);
	String dexStr;
	    
	for (i = 0; i < super.size(); i++) {
	    current = super.NodeAt(i);
	    if (current.hasLabelDex() && !super.IsLeafText(current)) {
		dexStr = current.getLabelDex();
		dexInt = dexInt.valueOf(dexStr);
		if (!isDone(dexInt, done)) {
		    one_pair = new Vector();
		    one_pair.addElement(current);
		    ant = super.getCoIndexed(current);
		    for (j = 0; j < ant.size(); j++) {
			one_pair.addElement(ant.elementAt(j)); }
		    pairs.addElement(one_pair);
		    done.addElement(dexInt);}}}
	//printTracePairs(pairs);
	return pairs; }

    public boolean Concat(Node source, Node target, String newlabel, 
			  boolean update_me) {
	Node up_source, up_target;
	String source_label, target_label, concatted;

	up_source = source;
	up_target = target;
	if (update_me) {
	    up_source = getUpdate(source);
	    up_target = getUpdate(target); }
	source_label = up_source.getLabel();
	target_label = up_target.getLabel();
	concatted = target_label + newlabel + source_label;
	super.SetLabelAt(concatted, up_target.getIndex());
	return true; }

    public boolean isDone(Integer labeldex, Vector done) {
	int j;
	Integer dexx;

	for (j = 0; j < done.size(); j++) {
	    dexx = (Integer)done.elementAt(j);
	    if (dexx.equals(labeldex)) { return true; }}
	return false; }

    public void printTracePairs(Vector trace_pairs) {
	int i, j;
	Vector a_pair;
	Node nodal;

	for (i = 0; i < trace_pairs.size(); i++) {
	    a_pair = (Vector)trace_pairs.elementAt(i);
	    for (j = 0; j < a_pair.size(); j++) {
		nodal = (Node)a_pair.elementAt(j);
		if (j < a_pair.size() - 1) {
		    System.err.print(nodal.toString() + ", "); }
		else {
		    System.err.println(nodal.toString()); } }} }

    /*
      when nodes are added to the tree, following nodes
      must be shifted right to make room.
    */
    private void ShiftRight (int from, int how_much) {
	int i;
	for (i = super.size() - 1; i >= from; i--) {
	    super.SetLabelAt(super.LabelAt(i), i + how_much);
	    super.SetEndDexAt((super.EndDexAt(i)).intValue() + how_much, 
			      i + how_much); }}

    private void ShiftLeft (int from, int how_much) {
	int i;
	for (i = super.size() - 1; i >= from; i--) {
	    super.SetLabelAt(super.LabelAt(i), i + how_much);
	    super.SetEndDexAt((super.EndDexAt(i)).intValue() + how_much,
			  i + how_much); } }

    /*
      when nodes are inserted/deleted in the tree, 
      preceding nodes that end after the insertion/deletion 
      need to have their end_dexes shifted.
    */
    private void ShiftPreEnds (int up_to, int finish, 
			       int how_much) {
	int i, old_end;
	for  (i = 0; i < up_to; i++) {
	    old_end = (super.EndDexAt(i)).intValue();
	    if (old_end >= finish) {
		super.SetEndDexAt(old_end + how_much, i); }}}

    /*
      when nodes are added/deleted from the tree, following
      nodes need to have their end_dexes shifted.
    */
    private void ShiftFollowEnds(int from, int how_much) {
	int i, old_end;
	for (i = super.size() - 1; i >= from; i--) {
	    old_end = (super.EndDexAt(i)).intValue();
	    super.SetEndDexAt(old_end + how_much, i); }}

    private void ShiftFollowEnds(int from, int how_much, int to) {
	int i, old_end;
	for (i = from; i <= to; i++) {
	    old_end = (super.EndDexAt(i)).intValue();
	    super.SetEndDexAt(old_end + how_much, i); }}

    private int getSibDex(Vector sibs, Node nodal) {
	int i;
	Node sib;

	for (i = 0; i < sibs.size(); i++) {
	    sib = (Node)sibs.elementAt(i);
	    if (sib.equals(nodal)) { return i; }}
	return -1; }

    public void PrintChangesToSystemErr() {
	PrintChangesToSystemErr(0, index_changes.size()); }

    public void PrintChangesToSystemErr(int start, int end) {
	Integer dex;
	System.err.println("changes:  " );
	if (start < 0) { start = 0; }
	if (end > index_changes.size()) { end = index_changes.size(); }
	for (int i = start; i < end; i++) {
	    dex = (Integer)index_changes.elementAt(i);
	    System.err.print(i+".)  ");
	    System.err.print(dex + "  ");
	    if (dex.intValue() < 0 || dex.intValue() >= super.size()) {
		System.err.println("NULL"); }
	    else {
		System.err.println(super.LabelAt(dex)); } }
    }


} 
