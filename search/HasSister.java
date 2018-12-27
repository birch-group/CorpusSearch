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
 * A HasSister B if A and B have the same mother.
 * Notice that A can precede B or B can precede A.
 */
public class HasSister extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector siblings;
	SentenceResult Indices = new SentenceResult();
        Node bound, sister, current;
        int n, j, k;

        sparse_loop: for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    x_loop: if (IsOnList(sparse, current, x_list)) {
		siblings = sparse.GetAllSisters(n);
		sib_loop: for (k = 0; k < siblings.size(); k++) { 
		    sister = (Node)siblings.elementAt(k);
		    if (IsOnList(sparse, sister, y_list) && !(sister.equals(current))) {
			bound = GetBoundaryNode(sparse, current, sister);
			if (!bound.IsNullNode()) {
			    Indices.addSubResult(bound, current, sister); 
			    continue sib_loop; }
		    } 
		} 
	    } 
	} 
	return Indices;
    } 

    /*
      Not_x --
    */
    public static SentenceResult Not_x (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector siblings;
        SentenceResult Indices = new SentenceResult();
        Node bound, sister, current;
        int n, j, k;

        sparse_loop: for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            y_loop: if (IsOnList(sparse, current, y_list)) {
                siblings = sparse.GetAllSisters(n);
                for (k = 0; k < siblings.size(); k++) {
                    sister = (Node)siblings.elementAt(k);
                    if (IsOnList(sparse, sister, x_list) && !(sister.equals(current))) {
			continue sparse_loop; }
		}
		bound = GetBoundaryNode(sparse, current, current);
                if (!bound.IsNullNode()) {
		    Indices.addSubResult(bound, current, current);
                    break y_loop; }
            } 
        } 
        return Indices;
    } 

    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector siblings;
        SentenceResult Indices = new SentenceResult();
        Node bound, sister, current;
        int n, j, k;

        sparse_loop: for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            x_loop: if (IsOnList(sparse, current, x_list)) {
                siblings = sparse.GetAllSisters(n);
                for (k = 0; k < siblings.size(); k++) {
                    sister = (Node)siblings.elementAt(k);
                    if (IsOnList(sparse, sister, y_list) && !(sister.equals(current))) {
                        continue sparse_loop; }
                }
                bound = GetBoundaryNode(sparse, current, current);
                if (!bound.IsNullNode()) {
                    Indices.addSubResult(bound, current, current);
                    break x_loop; }
            } // end if IsOnList(...., x_list)
        } // end for (n = 0; n < sparse.size(); n++
        return Indices;
    } // end method Not_y

} 

