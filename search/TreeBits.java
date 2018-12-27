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
import basicinfo.*;

/**
 * contains a BitSet used to manipulate subsets of the tree.
 * 
 */

public class TreeBits extends Syntax {

    private BitSet sparse_bits;

    public TreeBits (SynTree sparse) {
	sparse_bits = new BitSet(sparse.size()); }

    public TreeBits(int i) {
	sparse_bits = new BitSet(i); }

    /**
     * returns BitSet representing boundary nodes in sentence.
    */
    public void SetBounds (SynTree sparse) {
	Node current;
	int i;

	for (i = 0; i < sparse.size(); i++) {
	    current = sparse.NodeAt(i);
	    if (IsBoundary(sparse, current)) {
		sparse_bits.set(i); } }
	return; }

    /**
     * returns BitSet representing descendants of input node.
    */
    public void Descending_Bits (SynTree sparse, Node ancestor) {
	Vector descendants;
        Node one_desc = new Node();
        int i, dex;

	descendants = sparse.GetDescendants(ancestor);
        for (i = 0; i < descendants.size(); i++) {
	    one_desc = (Node)descendants.elementAt(i);
	    dex = one_desc.getIndex_int();
	    sparse_bits.set(dex); }
        return; }

    public void ClearNode (Node nodal) {
	int dex;

	dex = nodal.getIndex_int();
	sparse_bits.clear(dex);
	return; }

    public void ClearSubTree (SynTree sparse, Node erase) {
	Vector descendants;
	Node one_desc;
	int i, dex;

	dex = erase.getIndex_int();
	sparse_bits.clear(dex);
	descendants = sparse.GetDescendants(erase);
	for (i = 0; i < descendants.size(); i++) {
            one_desc = (Node)descendants.elementAt(i);
            dex = one_desc.getIndex_int();
            sparse_bits.clear(dex); }
	return; }

    public Node NextNodeForBits (SynTree sparse, Node nodal) {
	return (this.NextNodeForBits(sparse, nodal.getIndex_int())); }

    public Node NextNodeForBits (SynTree sparse, int index) {
	int i;
	Node nullster;

	for (i = index + 1; i < sparse_bits.size(); i++) {
	    if (sparse_bits.get(i)) {
		return (sparse.NodeAt(i)); } }
	nullster = new Node("NULL");
	return (nullster); }

    /**
     * returns list of nodes corresponding to set bits in BitSet.
    */
    public Vector NodesForBits (SynTree sparse) {
	int i;
	Vector nodes = new Vector();

	for (i = 0;  i < sparse_bits.size(); i++) {
            if (sparse_bits.get(i)) {
		nodes.addElement(sparse.NodeAt(i)); }}
	return nodes; }

    public void PrintToSystemErr(SynTree sparse) {
        Node nodal = new Node("NULL");
	Vector nodes = new Vector();
	nodes = this.NodesForBits(sparse);
	nodal.PrintNodeVector(nodes);
	System.err.println(""); }

} 






