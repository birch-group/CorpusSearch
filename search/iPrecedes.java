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
 * A iPrecedes B if
 */
public class iPrecedes extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector siblings, lefties;
	SentenceResult Indices = new SentenceResult();
        Node curr_node, current, mom, daughter, aunt, left;
        int n, j;

        current_loop: for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    x_loop: if (IsOnList(sparse, current, x_list)) {
		aunt = NextLegitSister(sparse, current);
		if (aunt.IsNullNode()) {
		    mom = current;
		    // travel up rightmost path.
		    while (aunt.IsNullNode() && !mom.IsNullNode()) {
			mom = sparse.GetMother(mom);
			if (mom.IsNullNode()) { break x_loop; }
			aunt = NextLegitSister(sparse, mom); } 
		    if (aunt.IsNullNode()) { break x_loop; }
		}
		if (IsOnList(sparse, aunt, y_list)) {
		    curr_node = GetBoundaryNode(sparse, current, aunt);
		    if (!curr_node.IsNullNode()) {
			Indices.addSubResult(curr_node, current, aunt); }
            	} 
		leftward: while (!sparse.IsLeafText(aunt)) {
		    lefties = sparse.GetDaughters(aunt);
		    lefties = PipeList.PurgeNodeList(sparse, lefties, 
						     Vitals.ignore_list);
		    if (lefties.isEmpty()) { break leftward; }
		    left = (Node)lefties.firstElement();
		    if (IsOnList(sparse, left, y_list)) {
			curr_node = GetBoundaryNode(sparse, current, left); 
			if (!curr_node.IsNullNode()) {
			    Indices.addSubResult(curr_node, current, left); }
		    }
		    aunt = left;
		} // end while
	    } // end if IsOnList(...., x_list)
	} // end for (n = 0; n < sparse.size(); n++
	return Indices; }

    /*
      Not_x  --
    */
    public static SentenceResult Not_x (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector siblings, righties;
        SentenceResult Indices = new SentenceResult();
        Node curr_node, current, mom, daughter, aunt, right;
        int n, j;

        sparse_loop: for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            y_loop: if (IsOnList(sparse, current, y_list)) {
		aunt = PrevLegitSister(sparse, current);
		if (aunt.IsNullNode()) {
		    mom = current;
		    // travel up rightmost path.
		    while (aunt.IsNullNode() && !mom.IsNullNode()) {
			mom = sparse.GetMother(mom);
			aunt = PrevLegitSister(sparse, mom); }
		    if (aunt.IsNullNode()) { break y_loop; }
		}
                if (IsOnList(sparse, aunt, x_list)) { break y_loop; }
                rightward: while (!sparse.IsLeafText(aunt)) {
                    righties = sparse.GetDaughters(aunt);
                    righties = PipeList.PurgeNodeList(sparse, righties, 
						      Vitals.ignore_list);
		    if (righties.isEmpty()) { break rightward; }
                    right = (Node)righties.lastElement();
                    if (IsOnList(sparse, right, x_list)) {
			break y_loop; }
		    aunt = right;
                } // end while
		curr_node = GetBoundaryNode(sparse, current, aunt);
		if (!curr_node.IsNullNode()) {
		    Indices.addSubResult(curr_node, current, current); }
            } // end if IsOnList(...., y_list)
        } // end for (n = 0; n < sparse.size(); n++
        return Indices;
    } // end method Not_x


    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector siblings, lefties;
        SentenceResult Indices = new SentenceResult();
        Node curr_node, current, mom, daughter, aunt, left;
        int n, j;

        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            x_loop: if (IsOnList(sparse, current, x_list)) {
		aunt = NextLegitSister(sparse, current); 
		if (aunt.IsNullNode()) {
		    mom = current;
		    // travel up leftmost path.
		    while (aunt.IsNullNode() && !mom.IsNullNode()) {
			mom = sparse.GetMother(mom);
			aunt = NextLegitSister(sparse, mom); }
		    if (aunt.IsNullNode()) { break x_loop; }
		}
                if (IsOnList(sparse, aunt, y_list)) { break x_loop; }
                leftward: while (!sparse.IsLeafText(aunt)) {
                    lefties = sparse.GetDaughters(aunt);
                    lefties = PipeList.PurgeNodeList(sparse, lefties, 
						     Vitals.ignore_list);
		    if (lefties.isEmpty()) { break leftward; }
                    left = (Node)lefties.firstElement();
                    if (IsOnList(sparse, left, y_list)) {
			break x_loop; }
		    aunt = left;
                } // end while
		curr_node = GetBoundaryNode(sparse, current, aunt);
		if (!curr_node.IsNullNode()) {
		    Indices.addSubResult(curr_node, current, current); }
            } // end if IsOnList(...., x_list)
        } // end for (n = 0; n < sparse.size(); n++
        return Indices; }

    public static Node NextLegitSister(SynTree sparse, Node nodal) {
	return(NextLegitSister(sparse, nodal.getIndex_int())); }
	
    public static Node NextLegitSister (SynTree sparse, int dex) {
	Vector siblings;
	Node sister = new Node("NULL");
	int sis_dex = 0;

	siblings = sparse.GetSisters(dex);
	if (!siblings.isEmpty()) {
	    sister = (Node)siblings.elementAt(0);
	    while (ignoreIt(sparse, sister)) {
		sis_dex += 1;
		if (sis_dex < siblings.size()) {
		    sister = (Node)siblings.elementAt(sis_dex); }
		else { // no legitimate sister.
		    sister = new Node("NULL");
		    return sister; }
	    } 
	}
	return sister; }

    public static Node PrevLegitSister (SynTree sparse, Node nodal) {
	return(PrevLegitSister(sparse, nodal.getIndex_int())); }

    public static Node PrevLegitSister (SynTree sparse, int dex) {
	Vector siblings;
	Node sib, mom, sister = new Node("NULL");
	int i, y_dex = 0, sis_dex = 0;
	
	mom = sparse.GetMother(dex);
	if (mom.IsNullNode()) { return sister; }
	siblings = sparse.GetDaughters(mom);
	// find index of input dex in siblings vector.
	sib_loop: for (i = 0; i < siblings.size(); i++) {
	    sib = (Node)siblings.elementAt(i);
	    if ((sib.getIndex()).intValue() == dex) {
		y_dex = i;
		break sib_loop; }
	}
	if (y_dex - 1 < 0) { return sister; }
	sister = (Node)siblings.elementAt(y_dex - 1);
	while (ignoreIt(sparse, sister)) {
	    sis_dex -= 1;
	    if (sis_dex > 0) {
		sister = (Node)siblings.elementAt(sis_dex); }
	    else { // no legitimate sister.
		sister = new Node("NULL");
		return sister; }
	} 
	return sister; }

} 




