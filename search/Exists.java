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
 * A Exists if A is contained anwhere in the tree.
 */
public class Exists extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, ArgList x_list) {
	SentenceResult Indices = new SentenceResult();
	Node current, forebear;
	int n;

	tree_loop:  for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    if (IsOnList(sparse, current, x_list)) {
		forebear = GetBoundaryNode(sparse, current);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, current); }
	    }
        }
	return Indices;
    } 

    public static SentenceResult Not_x (SynTree sparse, ArgList x_list) {
	SentenceResult Indices = new SentenceResult();
        Node current, maybe_x;
        int n, i;
	Vector descendants;

        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            bound_loop: if (IsBoundary(sparse, current)) {
		descendants = sparse.GetDescendants(n);
		maybe_x = new Node("NULL");
		for (i = 0; i < descendants.size(); i++) {
		    maybe_x = (Node)descendants.elementAt(i);
		    if (IsOnList(sparse, maybe_x, x_list)) {
			break bound_loop; }
		}
		Indices.addSubResult(current, maybe_x, maybe_x);
	    } 
	}
	return Indices;
    } 

} 



