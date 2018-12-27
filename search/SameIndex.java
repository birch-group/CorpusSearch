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
import search_result.*;
import basicinfo.*;

/**
 * label A and label B have the same index if they end
 * with the same digit -#.
 * e.g.: CP-1 and *T*-1.
 */
public class SameIndex extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	Vector x_nodes, y_nodes;
	Node current;
	int n;

	x_nodes = new Vector();
	y_nodes = new Vector();
	for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    if (IsOnList(sparse, current, x_list) && 
		!((current.getLabelDex()).equals(""))) {
		x_nodes.addElement(current); } 
	    if (IsOnList(sparse, current, y_list) &&
		!((current.getLabelDex()).equals(""))) {
		y_nodes.addElement(current); }
	}
	return (getPairs(sparse, x_nodes, y_nodes)); }

    /*
      Not_x --
    */
    public static SentenceResult Not_x (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector x_nodes, y_nodes;
        Node current;
        int n;

        x_nodes = new Vector();
        y_nodes = new Vector();
        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            if (!IsOnList(sparse, current, x_list) &&
                !((current.getLabelDex()).equals(""))) {
                x_nodes.addElement(current); }
            if (IsOnList(sparse, current, y_list) &&
                !((current.getLabelDex()).equals(""))) {
                y_nodes.addElement(current); }
        }
        return (getPairs(sparse, x_nodes, y_nodes)); }

    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector x_nodes, y_nodes;
        Node current;
        int n;

        x_nodes = new Vector();
        y_nodes = new Vector();
        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            if (IsOnList(sparse, current, x_list) &&
                !((current.getLabelDex()).equals(""))) {
                x_nodes.addElement(current); }
            if (!IsOnList(sparse, current, y_list) &&
                !((current.getLabelDex()).equals(""))) {
                y_nodes.addElement(current); }
        }
        return (getPairs(sparse, x_nodes, y_nodes)); }

    private static SentenceResult getPairs(SynTree sparse,
					   Vector x_nodes, Vector y_nodes) {
	SentenceResult Indices = new SentenceResult();
	int i, j;
	Node x_node, y_node, forebear;

	for (i = 0; i < x_nodes.size(); i++) {
	    x_node = (Node)x_nodes.elementAt(i);
	    j_loop: for (j = 0; j < y_nodes.size(); j++) {
		y_node = (Node)y_nodes.elementAt(j);
		if (x_node.sameLabelDex(y_node) &&
		    !x_node.equals(y_node)) {
		    if (sparse.dominates(x_node, y_node)  && 
			IsBoundary(sparse, x_node)) {
			Indices.addSubResult(x_node, x_node, y_node);
			continue j_loop; }
		    if (sparse.dominates(y_node, x_node) && 
			IsBoundary(sparse, y_node)) {
			Indices.addSubResult(y_node, x_node, y_node);
			continue j_loop; }
		    forebear = GetBoundaryNode(sparse, x_node, y_node);
		    Indices.addSubResult(forebear, x_node, y_node);
		}
	    }
	}
	return Indices; }
} 



