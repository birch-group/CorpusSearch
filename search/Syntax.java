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
 * handles tasks shared by search functions.
 */

public class Syntax {

    /**
     * checks whether input node matches an item on Vitals.ignore_list.
     * If node is leaf_POS, also checks leaf node.
     * @param text string found in corpus
     * @return true if item was found on ignore_list, false otherwise
     */
    public static boolean ignoreIt (SynTree sparse, Node nodal) {
        String label;
	Node leaf;

	label = nodal.getLabel();
	if (Vitals.ignore_list.hasMatch(label)) {
	    return true; }
	if (sparse.IsLeafPOS(nodal)) {
	    leaf = sparse.FirstDaughter(nodal);
	    label = leaf.getLabel();
	    if (Vitals.ignore_list.hasMatch(label)) {
		return(true); } }
        return (false); }

    public static boolean IsOnList(SynTree sparse, 
				   Node nodal, ArgList argle) {

	if (ignoreIt(sparse, nodal)) {
	    return false; }
	if (argle.hasRoot() && nodal.equals(sparse.ROOT)) {
	    return true; }
	if (argle.hasMetaroot() && nodal.equals(sparse.METAROOT)) {
	    return true; }
	return (argle.hasMatch(nodal.getLabel())); }


    /**
     * checks whether the label of the  input node
     * is on the boundary list.
    */
    public static boolean IsBoundary (SynTree sparse, Node nodal) {

	if (Vitals.Node_List.hasRoot() && nodal.equals(sparse.ROOT)) {
	    return true; }
	if (Vitals.Node_List.hasMetaroot() && nodal.equals(sparse.METAROOT)) {
	    return true; }
	return (Vitals.Node_List.hasMatch(nodal.getLabel())); }

    /**
     * given a node, returns a list of text words
     * (leaves) dominated by that node.
     */
    public static Vector GetText(SynTree sparse, Node nodal) {
	Vector descendants, texts = new Vector();
	Node heir, leaf;
        String text;
        int n;
      
	descendants = sparse.GetDescendants(nodal);
        for (n = 0; n < descendants.size(); n++) {
            heir = (Node)descendants.elementAt(n);
            if (sparse.IsLeafText(heir)) {
                texts.addElement(heir.getLabel()); }
        }
        return texts; }

    /**
     * given a node, returns a list of text nodes
     * dominated by the input node.
    */
    public static Vector GetTextNodes(SynTree sparse, Node nodal) {
        Vector descendants, text_nodes = new Vector();
	Node heir;
        String text;
        int n;

	descendants = sparse.GetDescendants(nodal);
	for (n = 0; n < descendants.size(); n++) {
	    heir = (Node)descendants.elementAt(n);
	    if (sparse.IsLeafText(heir)) {
		text_nodes.addElement(heir); } }
	return text_nodes; }

    /** returns boundary for dominates functions.
     */
    public static Node GetBoundaryNode (SynTree sparse, Node x_node) {
	Node forebear;
	Vector ancestors;
	int i;

	if (IsBoundary(sparse, x_node)) { return x_node; }
	ancestors = sparse.GetAncestors(x_node); 
	for (i = 0; i < ancestors.size(); i++) {
	    forebear = (Node)ancestors.elementAt(i);
	    if (IsBoundary(sparse, forebear)) {
		return forebear; } }
	forebear = new Node("NULL");
	return forebear; }

    /**
     * returns boundary for nodes x and y where x does not dominate y
     * and y does not dominate x.
     */
    public static Node GetBoundaryNode (SynTree sparse, 
					Node x_node, Node y_node) {
	Node everymom, forebear;
        Vector ancestors;
	int i;

	everymom = sparse.GetCommonAncestor(x_node, y_node);
	if (IsBoundary(sparse, everymom)) {
	    return everymom; }
	ancestors = sparse.GetAncestors(everymom);
	for (i = 0; i < ancestors.size(); i++) {
	    forebear = (Node)ancestors.elementAt(i);
	    if (IsBoundary(sparse, forebear)) {
		return forebear; } }
	forebear = new Node("NULL");
	return forebear; }

} 






