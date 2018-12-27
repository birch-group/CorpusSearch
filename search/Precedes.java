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

/** A precedes B if 
 */
public class Precedes extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, 
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node bound, curr_x, curr_y;
        int n, j;

        for (n = 0; n < sparse.size(); n++) {
	    curr_x = sparse.NodeAt(n);
	    x_loop: if (IsOnList(sparse, curr_x, x_list)) {
		y_loop: for (j = n + 1; j < sparse.size(); j++) {
		    curr_y = sparse.NodeAt(j);
		    if (IsOnList(sparse, curr_y, y_list)) {
			if (sparse.dominates(curr_x, curr_y)) {
			    continue y_loop; }
			bound = GetBoundaryNode(sparse, curr_x, curr_y);
			if (!bound.IsNullNode()) {
			    Indices.addSubResult(bound, curr_x, curr_y); }}
		} // end y_loop
	    } // end x_loop
	} // end for (n = 0; n < sparse.size(); n++
	return Indices;
    } 

    /*
      Not_x --
    */
    public static SentenceResult Not_x (SynTree sparse, 
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node bound, curr_x, curr_y;
        int n, j;

	curr_x = new Node("NULL");
        tree_loop: for (n = 0; n < sparse.size(); n++) {
	    curr_y = sparse.NodeAt(n);
	    y_loop: if (IsOnList(sparse, curr_y, y_list)) {
		x_loop: for (j = 0; j < n; j++) {
		    curr_x = sparse.NodeAt(j);
		    if (IsOnList(sparse, curr_x, x_list)) {
			if (sparse.dominates(curr_x, curr_y)) {
			    continue x_loop; }
			continue tree_loop; }
		} // end x_loop
		bound = GetBoundaryNode(sparse, curr_x, curr_y);
		if (!bound.IsNullNode()) {
		    Indices.addSubResult(bound, curr_x, curr_y); } 
	    } // end y_loop
	} // end tree_loop
	return Indices;
    } // end method Not_x

    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node bound, curr_x, curr_y;
        int n, j;

	curr_y = new Node("NULL");
        tree_loop: for (n = 0; n < sparse.size(); n++) {
	    curr_x = sparse.NodeAt(n);
	    x_loop: if (IsOnList(sparse, curr_x, x_list)) {
		y_loop: for (j = n + 1; j < sparse.size(); j++) {
		    curr_y = sparse.NodeAt(j);
		    if (IsOnList(sparse, curr_y, y_list)) {
			if (sparse.dominates(curr_x, curr_y)) {
			    continue y_loop; }
			continue tree_loop;
		    }
		} // end y_loop;
		bound = GetBoundaryNode(sparse, curr_x, curr_y);
		if (!bound.IsNullNode()) {		
		    Indices.addSubResult(bound, curr_x, curr_y); } 
	    } // end x_loop
	} // end tree_loop
	return Indices;
    } // end method Not_y

} 




