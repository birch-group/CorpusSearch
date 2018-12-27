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

/**
This class contains all the methods for navigating a syntactic tree, 
instantiated by a SparseMatrix object.
*/
public class SynTree extends SparseMatrix {
    public Node ROOT, METAROOT, ID_POS, ID_TEXT;
    public boolean ORTHOtype; 

    public SynTree () { }

    public void setORTHOFalse() { ORTHOtype = false; }

    public void setORTHOTrue() { ORTHOtype = true; }

    public void setORTHO(boolean ort) { ORTHOtype = ort; }

    public boolean hasORTHO() { return ORTHOtype; }

    public void removeAllElements() { super.removeAllElements(); }

    public void RemoveAllElements() { super.removeAllElements(); }

    public void setEndVector(Vector new_end_vec) {
	super.setEndVector(new_end_vec); }

    public void setLabelVector(Vector new_label_vec) {
	super.setLabelVector(new_label_vec); }

    public Vector getEndVector() {
	return(super.GetEndVector()); }

    public Vector getLabelVector() {
	return(super.GetLabelVector()); }

    public Integer getEndAt(int i) {
	return (super.EndDexAt(i)); }

    public String getLabelAt(int i) {
	return super.LabelAt(i); }

    public SynTree synCopy() {
	int i;
	Vector copy_end_vec = new Vector(); 
	Vector copy_label_vec = new Vector();

	SynTree copy_syn;
	// copy this end vector into new SparseMatrix.
	for (i = 0; i < super.size(); i++) {
	    copy_end_vec.addElement(this.getEndAt(i)); }
	// copy this label vector into new SparseMatrix.
	for (i = 0; i < super.size(); i++) {
	    copy_label_vec.addElement(this.getLabelAt(i)); }
	copy_syn = new SynTree();
	copy_syn.setEndVector(copy_end_vec);
	copy_syn.setLabelVector(copy_label_vec);
	copy_syn.setConstantNodes();
	copy_syn.setORTHO(this.ORTHOtype);
	//System.err.println("copy_syn ORTHOtype:  " + copy_syn.hasORTHO());
	return copy_syn; }

    /**
       finds descendants of one node.
       @param mom-start-dex index of mother node.
       @return list of descendant Nodes.
    */
    public Vector GetDescendants (Integer mom_start_dex) {
        Node one_daughter;
        Vector daughters = new Vector();
        int n, mom_end_dex_int;

        mom_end_dex_int = (super.EndDexAt(mom_start_dex)).intValue();
        for (n = mom_start_dex.intValue() + 1; n <= mom_end_dex_int; n++) {
            one_daughter = super.NodeAt(n);
            daughters.addElement(one_daughter); }
        return daughters; }

    /**
       @param nodal mother node.
       @return list of descendant Nodes.
    */
    public Vector GetDescendants (Node nodal) {
	Integer dex = nodal.getIndex();
	return(GetDescendants(dex)); }

    /**
       @param n index of mother node.
       @return list of descendant Nodes.
    */
    public Vector GetDescendants (int n) {
	Integer n_Int = new Integer(n);
	return (GetDescendants(n_Int)); }

    /**
       @param list of nodes
       @return list of daughters of all incoming nodes.
    */
    public Vector GetDaughters (Vector nodes) {
	int i, j;
	Vector all_daughters = new Vector(), daughters;
	Node nodal;

	for (i = 0; i < nodes.size(); i++) {
	    nodal = (Node)nodes.elementAt(i);
	    daughters = this.GetDaughters(nodal);
	    for (j = 0; j < daughters.size(); j++) {
		all_daughters.addElement((Node)daughters.elementAt(j)); } }
	return all_daughters; }

    /** 
	@param your_mom index of mother node.
	@return list of daughter Nodes.
    */
    public Vector GetDaughters (int your_mom) {
        Integer mom_start_dex = new Integer(your_mom);
        return(GetDaughters(mom_start_dex)); }

    /** 
	@param mom_node mother node.
	@return list of daughter Nodes.
    */
    public Vector getDaughters (Node mom_node) {
        Integer mom_start_dex = mom_node.getIndex();
        return (GetDaughters(mom_start_dex)); }

    public Vector GetDaughters(Node mom_node) {
	return getDaughters(mom_node); }


    /**
       @param mom_start_dex index of mother node.
       @return list of daughter Nodes.
    */
    public Vector GetDaughters (Integer mom_start_dex) {
        Node one_daughter;
        Vector daughters = new Vector();
        Integer curr_end_dex = new Integer(0), prev_end_dex;
        int n, mom_end_dex_int;

        mom_end_dex_int = (super.EndDexAt(mom_start_dex)).intValue();
        for (n = mom_start_dex.intValue() + 1; n <= mom_end_dex_int; n++) {
            prev_end_dex = curr_end_dex;
            curr_end_dex = super.EndDexAt(n);
            one_daughter= super.NodeAt(n);
            if (!(curr_end_dex.equals(prev_end_dex)) ||
                    (this.IsLeafPOS(curr_end_dex))) {
                daughters.addElement(one_daughter); }
            if (curr_end_dex.intValue() > n + 1) {
                n = curr_end_dex.intValue() - 1; }
        } 
        return daughters; }

    /**
       @param mom_start_dex index of mother.
       @return first daughter node.
    */
    public Node FirstDaughter (Integer mom_start_dex) {
	if (this.IsLeafText(mom_start_dex) 
	    || mom_start_dex.intValue() < 0) {
	    return (new Node("NULL")); }
        return(super.NodeAt(mom_start_dex.intValue() + 1)); }

    /**
       @param mom_node mother node.
       @return first daughter Node.
    */
    public Node FirstDaughter (Node mom_node) {
        Integer mom_dex = mom_node.getIndex();
        return (FirstDaughter(mom_dex)); }

    /**
       @param mom_int index of mother node.
       @return first daughter node.
    */
    public Node FirstDaughter(int mom_int) {
	Integer mom_dex = new Integer(mom_int);
	return (FirstDaughter(mom_dex)); }

    public Node LastDaughter(int dex) {
	return(this.LastDaughter(super.NodeAt(dex))); }

    public Node LastDaughter(Node mom_node) {
	Vector daughters;

	daughters = this.GetDaughters(mom_node);
	if (daughters.isEmpty()) {
	    return (new Node("NULL")); }
	return((Node)daughters.lastElement()); }

    public Node LastDescendant(Node mom) {
	Integer mom_start_dex = mom.getIndex();
	int end_dex = super.intEndDexAt(mom_start_dex);
	if (this.IsLeafText(mom_start_dex) 
	    || mom_start_dex.intValue() < 0) {
	    return (new Node("NULL")); }
	return(super.NodeAt(end_dex)); }

    /**
       @param x index of daughter node.
       @return mother node.
    */
    public Node GetMother (int x) {
	Integer dex = new Integer(x);
	return (this.GetMother(dex)); }

    /**
       @param nodal daughter node.
       @return mother node.
    */
    public Node GetMother (Node nodal) {
	Integer dex = nodal.getIndex();
	return (this.GetMother(dex)); }
					
    /**
      @param dex index of daughter node.
      @return mother node.
    */
    public Node GetMother (Integer dex) {
        Node mother, negative;
        Integer mom_dex, mom_end_dex, orig_end;
        String mom_label;

        orig_end = super.EndDexAt(dex);
        mom_dex = dex;
        while (mom_dex.intValue() > 0) {
            mom_dex = new Integer(mom_dex.intValue() - 1);
            //System.err.println("mom_dex:  " + mom_dex);
            mom_end_dex = super.EndDexAt(mom_dex);
            if (mom_end_dex.intValue() >= orig_end.intValue()) {
                mother = super.NodeAt(mom_dex);
                return (mother); }
        } 
        negative = new Node("NULL");
        return (negative); }

    /**
      returns ancestors in order from source to root.
      @param nodal daughter node
      @return list of ancestor Nodes.
    */
    public Vector GetAncestors (Node nodal) {
	Node mom;
	Vector ancestors = new Vector();
	
	mom = nodal;
	while (!mom.IsNullNode()) {
	    mom = this.GetMother(mom);
	    if (!mom.IsNullNode()) {
		ancestors.addElement(mom); } }
	return ancestors; }
	
    /**
       returns ancestors in order from source to root.
       @param n int index of daughter.
       @return list of ancestor nodes.
    */
    public Vector GetAncestors (int n) {
	Node nodal;

	nodal = super.NodeAt(n);
	return (GetAncestors(nodal)); }
	
    /**
       returns least common ancestor of input Nodes.
       @param node1 first node
              node2 second node
       @return least common ancestor.
    */
    public Node GetCommonAncestor (Node node1, Node node2) {
	Integer first, second;
	return (GetCommonAncestor(node1.getIndex(), node2.getIndex())); }

    /**
       @param int1 int index of first node.
              int2 int index of second node.
       @return least common ancestor node.
    */
    public Node GetCommonAncestor (int int1, int int2) {
	Integer dex1 = new Integer(int1);
	Integer dex2 = new Integer(int2);
	return (GetCommonAncestor(dex1, dex2)); }

    /**
       @param dex1 Integer index of first node.
              dex2 Integer index of second node.
       @return least common ancestor.
    */
    public Node GetCommonAncestor (Integer dex1, Integer dex2) {
	Vector mom1_list = new Vector();
	Node mom1, mom2, fruitless;
	int i;

	mom1 = this.GetMother(dex1);
	while (!mom1.IsNullNode()) {
	    mom1_list.addElement(mom1);
	    mom1 = this.GetMother(mom1); }
	mom2 = this.GetMother(dex2);
	while (!mom2.IsNullNode()) {
	    for (i = 0; i < mom1_list.size(); i++) {
		if (mom2.equals((Node)mom1_list.elementAt(i))) {
		    return mom2; } }
	    mom2 = this.GetMother(mom2); }
	fruitless = new Node("NULL");
	return fruitless; }

    /**
       returns subsequent sisters of input node.
       @param nodal sister Node
       @return list of subsequent sister Nodes.
    */
    public Vector GetSisters (Node nodal) {
	return (GetSisters(nodal.getIndex_int())); }

    /** 
	@param nInt Integer index of sister node.
	@return list of subsequent sister Nodes.
    */
    public Vector GetSisters (Integer nInt) {
	return(GetSisters(nInt.intValue())); }

    /**
       @param int index of sister node.
       @return list of subsequent sister Nodes.
    */
    public Vector GetSisters (int dex) {
        Vector sisters = new Vector();
        int sis_dex, mom_dex, mom_end_dex;
        Node one_sister, mother;

        mother = this.GetMother(dex);
        mom_dex = mother.getIndex_int();
	if (mom_dex == -1) { return sisters; }
        mom_end_dex = super.intEndDexAt(mom_dex);
        sis_dex = super.intEndDexAt(dex) + 1;
        while (sis_dex <= mom_end_dex) {
            one_sister = super.NodeAt(sis_dex);
            sisters.addElement(one_sister);
            sis_dex = super.intEndDexAt(sis_dex) + 1; }
        return (sisters); }

    /**
       @param n index of sister node.
       @return list of all children Nodes of input node's mother.
    */
    public Vector GetAllSisters (int n) {
        Integer n_Int = new Integer(n);
        return(GetAllSisters(n_Int)); }

    /**
       @param nodal sister node.
       @return list of all children Nodes of input node's mother.
    */
    public Vector GetAllSisters (Node nodal) {
	Integer dex = nodal.getIndex();
	return(GetAllSisters(dex)); }
 
    /**
       @param dex Integer index of sister node.
       @return list of all children Nodes of input node's mother.
    */
    public Vector GetAllSisters (Integer dex) {
        Vector sisters = new Vector(); 
        Node mother;

        mother = this.GetMother(dex);
	sisters = this.GetDaughters(mother);
	return sisters; }

    public boolean isOnlyChild (Node nodal) {
	Vector all_sis = GetAllSisters(nodal);
	if (all_sis.size() == 1) { return true; }
	return false; }

    public Node nextSister (Node nodal) {
        int sis_dex, mom_dex, mom_end_dex;
        Node one_sister = new Node("NULL"), mother;

        mother = this.GetMother(nodal);
        mom_dex = mother.getIndex_int();
	if (mom_dex == -1) { return one_sister; }
        mom_end_dex = super.intEndDexAt(mom_dex);
        sis_dex = super.intEndDexAt(nodal.getIndex_int()) + 1;
        if (sis_dex <= mom_end_dex) {
            one_sister = super.NodeAt(sis_dex); }
	return one_sister; }

    public Node prevSister(int dex) {
	return (this.prevSister(super.NodeAt(dex))); }

    public Node prevSister(Node nodal) {
	Node mom, one_sister = new Node("NULL");
	Vector daughters;

	mom = this.GetMother(nodal);
	daughters = this.GetDaughters(mom);
	for (int i = 0; i < daughters.size(); i++) {
	    one_sister = (Node)daughters.elementAt(i);
	    if (one_sister.equals(nodal)) {
		if (i == 0) { return (new Node("NULL")); }
		return ((Node)daughters.elementAt(i - 1)); } }
	return (new Node("NULL")); }
	

    /**
      returns true if input node is rightmost (last)
      child of its mother.
      @param nodal daughter node.
      @return true if input node is rightmost.
    */
    public boolean IsRightMost (Node nodal) {
	Integer mom_end;
	Integer nodal_end;
	Node mother;

	mother = this.GetMother(nodal);
	mom_end = this.EndDexAt(mother.getIndex());
        nodal_end = this.EndDexAt(nodal.getIndex());
	if (mom_end.equals(nodal_end)) {
	    return true; }
	return false; }

    /**
       returns true if input node is leftmost (first)
       child of its mother.
       @param nodal daughter node.
       @return true if input node is leftmost.
    */
    public boolean IsLeftMost(Node nodal) {
	Integer mom_start;
	Integer nodal_start;
	Node mother;

	mother = this.GetMother(nodal);
	mom_start = mother.getIndex();
	nodal_start = nodal.getIndex();
	if (mom_start.intValue() + 1 == nodal_start.intValue()) {
	    return true; }
	return false; }

    /**
      returns true if input index corresponds to pos-label of 
      leaf node; false otherwise.  (Example:  true if input index corresponds
      to "N" from the leaf "N dog".)
      @param dex Integer index of possible pos node.
      @return true if node is pos.
    */
    public boolean IsLeafPOS (Integer dex) {
        Integer end_dex_0, end_dex_1, start_plus_1;

        end_dex_0 = super.EndDexAt(dex);
        start_plus_1 = new Integer(dex.intValue() + 1);
        end_dex_1 = super.EndDexAt(start_plus_1);
        if (end_dex_0.equals(end_dex_1) && end_dex_1.equals(start_plus_1)) {
            return true; }
        else { return false; } }

    /**
       @param dex int index of possible pos node.
       @return true if node is pos.
    */
    public boolean IsLeafPOS (int dex) {
        Integer dex_Int = new Integer(dex);
        return (this.IsLeafPOS(dex_Int)); }

    /**
       @param nodal possible pos node.
       @return true if input node is pos.
    */
    public boolean IsLeafPOS (Node nodal) {
        Integer node_dex;
        node_dex = nodal.getIndex();
        return (this.IsLeafPOS(node_dex)); }

    /**
      returns true if input index corresponds to text of
      leaf node; false otherwise.  (Example:  true if input index corresponds
      to "dog" from the leaf "N dog".)
      @param dex Integer index of possible text node.
      @return true if node is text.
    */
    public boolean IsLeafText (Integer dex) {
        Integer end_dex;
	
        end_dex = super.EndDexAt(dex);
	if (end_dex.equals(dex)) {	
	    return true; }
	else { return false; } }

    /**
       @param dex int index of possible text node.
       @return true if node is text.
    */
    public boolean IsLeafText (int dex) {
        Integer dex_Int = new Integer(dex);
        return (this.IsLeafText(dex_Int)); }

    /**
       @param nodal possible text node.
       @return true if node is text.
    */
    public boolean IsLeafText (Node nodal) {
        Integer node_dex;
        node_dex = nodal.getIndex();
        return (this.IsLeafText(node_dex)); }

    /* returns true if x dominates y. */ 
    public boolean dominates (Node x, Node y) {
	int end_x, x_index, y_index;

	x_index = x.getIndex_int();
	y_index = y.getIndex_int();
	if (y_index <= x_index) { return false; }
	end_x = (super.EndDexAt(x_index)).intValue();
        if (y_index > end_x) { return false; }
	return true; }

    /* returns true if x precedes y. */
    public boolean precedes (Node x, Node y) {
	int x_index, y_index;

	x_index = x.getIndex_int();
	y_index = y.getIndex_int();
	if (x_index <= y_index) { return true; }
	return false; }

    /*
      getText -- returns text nodes dominated by input node.
    */
    public Vector getText(Node nodal) {
	Vector descendants, text = new Vector();
	Node descend;
	
	descendants = GetDescendants(nodal);
	for (int i = 0; i < descendants.size(); i++) {
	    descend = (Node)descendants.elementAt(i);
	    if (IsLeafText(descend)) {
		text.addElement(descend); } }
	return text; }

    public void setIDs() {
	int end;
	Node id;

        end = super.size() - 1;
	while (end > 0) {
	    id = super.NodeAt(end);
	    if (id.getLabel().equals("ID")) {
		this.ID_POS = id;
		this.ID_TEXT = super.NodeAt(end + 1); 
	        return; }
	    end -= 1; }
	this.ID_POS = new Node("NULL");
	this.ID_TEXT = new Node("NULL");
        return; }

    public boolean isRoot(Node nodal) {
	return((this.ROOT).equals(nodal)); }

    public boolean isMetaRoot(Node x) {
	return ((this.METAROOT).equals(x)); }

    public void setConstantNodes() {
	this.setRoot();
	this.setIDs(); }

    public void setRoot() {
	if (super.isEmpty()) { return; }
	this.ROOT = this.getRootNode();
	this.METAROOT = super.NodeAt(0);
	return; }

    public void conflateRoots() { // used for ottawa format.
	this.setRoot();
	this.ROOT = this.METAROOT; }

    public Node getRootNode() {
	String root_label = "";
	int root_dex = 0;

	root_label = super.LabelAt(root_dex);
	// if node has no label (label length is 0), go to next node.
	// if node is a leafPOS, or leafText, go to next node.
	// if node label is METADATA, go to end of METADATA subtree.
        root_look:  while (root_label.length() == 0 || IsLeafPOS(root_dex) 
			   || IsLeafText(root_dex)) { 
	    root_dex += 1;
	    if (IsLeafPOS(root_dex) && (root_dex + 2) >= super.size()) {
		if (super.LabelAt(root_dex).equals("ID")) {
		    return super.NodeAt(1); }
		else {
		    return(super.NodeAt(root_dex)); } }
	    if (root_dex >= super.size()) { 
		root_dex = 0;
		break root_look; }
	    root_label = super.LabelAt(root_dex);
	    if (root_label.equals("METADATA")) {
	        root_dex = super.intEndDexAt(root_dex) + 1; }
	}
        return (super.NodeAt(root_dex)); }

    public Node getMETADATA () {
	Node meta;
	int n;

	for (n = 0; n < super.size(); n++) {
	    meta = super.NodeAt(n);
	    if (meta.getLabel().equals("METADATA")) {
		return meta; } }
	return (new Node("NULL")); }

    public int getDepth(Node nodal) {
	int depth = 0;
	Node mom = nodal;

	while (!mom.IsNullNode()) {
	    mom = this.GetMother(mom);
	    depth += 1;
	    if (mom.equals(this.getRootNode())) {
		return depth; } }
	return depth; }

    public int getDepth (Vector list) {
	int max_depth = 0, i, node_depth;
	Node nodal;

	for (i = 0; i < list.size(); i++) {
	    nodal = (Node)list.elementAt(i); 
	    node_depth = this.getDepth(nodal);
	    if (node_depth > max_depth) {
		max_depth = node_depth; } }
	return max_depth; }

    public int getSubtreeDepth(Node sub_root) {
	return(getSubtreeDepth(sub_root.getIndex_int())); }

    public int getSubtreeDepth(int dex) {
	Vector descendants;
	int max_depth = 0, i;

	descendants = this.GetDescendants(dex);
	max_depth = getDepth(descendants);
	return max_depth; }
	    
    public Vector getNodesforDepth(int depth) {
	Vector list = new Vector(), daughters;
	int i, depth_so_far = 0;

	list.addElement(this.getRootNode());
	for (i = 0; i < depth; i++) {
	    list = this.GetDaughters(list); }
	return list; }

    public Vector getAllNodesforDepth(int depth) {
	Vector list = new Vector(), daughters;
	int i, depth_so_far = 0;

	list.addElement(this.NodeAt(0));
	for (i = 0; i < depth; i++) {
	    list = this.GetDaughters(list); }
	return list; }

    public Vector allLabelDexes() {
	Node nodal;
	String label_dex;
	Vector all_dexes = new Vector();

	for (int i = 0; i < super.size(); i++) {
	    nodal = super.NodeAt(i);
	    label_dex = nodal.getLabelDex();
	    if (!label_dex.equals("UNKNOWN") && !label_dex.equals("")) {
		all_dexes.addElement(label_dex); } }
	return all_dexes; }

    public String getNewLabelDex() {
	Vector all_dexes;
	Integer max;

	all_dexes = this.allLabelDexes();
	max = getMaxDex(all_dexes);
	max = new Integer(max.intValue() + 1);
	return (max.toString()); }

    public Integer getMaxDex(Vector all_dexes) {
	String dex;
	Integer convert, max = new Integer(0);
	    
	for (int i = 0; i < all_dexes.size(); i++) {
	    dex = (String)all_dexes.elementAt(i);
	    convert = new Integer(dex);
	    if (convert.compareTo(max) > 0) {
		max = convert; } }
	return max; }

    public Node getAntecedent (Node trace_POS) {
	String label_dex, minus_tags, maybe;
	int i;
	Node first_daughter;

	first_daughter = this.FirstDaughter(trace_POS);
	label_dex = first_daughter.getLabelDex();
	if (label_dex.equals("")) { return (new Node("NULL")); }
	minus_tags = trace_POS.minusTags();
	for (i = 0; i < this.size(); i++) {
	    maybe = super.LabelAt(i);
	    if (maybe.startsWith(minus_tags) && maybe.endsWith(label_dex)) {
		return(super.NodeAt(i)); } }
	return(new Node("NULL")); }

    // inverse of getAntecedent.
    public Node getTrace (Node ant) {
	String label_dex, minus_tags, maybe, daughter;
	int i;

	label_dex = ant.getLabelDex();
	if (label_dex.equals("")) { return (new Node("NULL")); }
	minus_tags = ant.minusTags();
	for (i = 0; i < this.size(); i++) {
	    maybe = super.LabelAt(i);
	    daughter = (this.FirstDaughter(i)).getLabel();
	    if (maybe.startsWith(minus_tags) && daughter.endsWith(label_dex)) {
		return(super.NodeAt(i)); } }
	return(new Node("NULL")); }

    // returns antecedent if input node is trace, and trace
    // if input node is antecedent.
    public Node getPartner(Node whatever) {

	if (this.IsLeafPOS(whatever)) {
	    return (this.getAntecedent(whatever)); }
	return (this.getTrace(whatever)); }

    public Vector getCoIndexed(Node dexed) {
	Vector covec = new Vector();
	String dexStr1, dexStr2;
	Node nodal;
	int i;

	if (!dexed.hasLabelDex()) { return covec; }
	dexStr1 = dexed.getLabelDex();
	for (i = 0; i < this.size(); i++) {
	    nodal = this.NodeAt(i);
	    if (!nodal.equals(dexed) && nodal.hasLabelDex()) {
		dexStr2 = nodal.getLabelDex();
		if (dexStr2.equals(dexStr1)) { covec.addElement(nodal); }}}
	return covec; }

    public Vector leftDescend(int dex) {
	Node first_child;
	Vector left_descendants = new Vector();

	first_child = this.FirstDaughter(dex);
	while(!first_child.IsNullNode()) {
	    left_descendants.addElement(first_child);
	    first_child = this.FirstDaughter(first_child);
	}
	return left_descendants; }
	    
	


} 


