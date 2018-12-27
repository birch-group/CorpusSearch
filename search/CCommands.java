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
 * A CCommands B iff A does not dominate B and the first
 * branching node that dominates A also dominates B.
 */
public class CCommands extends Syntax {

    public static SentenceResult Plain (SynTree sparse, 
					ArgList x_list, ArgList y_list) {
        Vector x_ancestors, kids, descendants;
	SubResult result;
	SentenceResult Indices = new SentenceResult();
	TreeBits descend;
        Node forebear, geezer, current, maybe_y;
        int n, j, i, k;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    found_x:  if (IsOnList(sparse, current, x_list)) {
		x_ancestors = sparse.GetAncestors(n);
		for (i = 0; i < x_ancestors.size(); i++) {
		    forebear = (Node)x_ancestors.elementAt(i);
		    // find first branching (more than one child) ancestor.
		    kids = sparse.GetDaughters(forebear);
		    if (kids.size() > 1) {
			// does forebear dominate y?
			descend = new TreeBits(sparse);
			descend.Descending_Bits(sparse, forebear);
			maybe_y = forebear;
			descending_loop: while (!maybe_y.IsNullNode()) {
			    maybe_y = descend.NextNodeForBits(sparse, maybe_y);
			    // disallow x and its descendants.
			    if (maybe_y.equals(current)) {
				descend.ClearSubTree(sparse, maybe_y);
				continue descending_loop; }
			    if (IsOnList(sparse, maybe_y, y_list)) {
				geezer = GetBoundaryNode(sparse, current, maybe_y);
				if (!geezer.IsNullNode()) {
					Indices.addSubResult(geezer, current, 
							     maybe_y); } }
			} // end descending_loop
			break found_x;
		    } // end if kids.size() > 1
		} // end for i = 0; i < x_ancestors.size(); i++
	    } // end found_x
	} // end for n = 0; n < sparse.size(); n++
	return Indices;
    } // end method Plain

    /*
      Not_x --
    */
    public static SentenceResult Not_x (SynTree sparse, 
					ArgList x_list, ArgList y_list) {
	return(Not_y(sparse, y_list, x_list)); }

    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector x_ancestors, kids, descendants;
	SubResult result;
	SentenceResult Indices = new SentenceResult();
	TreeBits descend;
        Node forebear, geezer, current, maybe_y;
        int n, j, i, k;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    found_x:  if (IsOnList(sparse, current, x_list)) {
		x_ancestors = sparse.GetAncestors(n);
		for (i = 0; i < x_ancestors.size(); i++) {
		    forebear = (Node)x_ancestors.elementAt(i);
		    // find first branching (more than one child) ancestor.
		    kids = sparse.GetDaughters(forebear);
		    if (kids.size() > 1) {
			// does forebear dominate y?
			descend = new TreeBits(sparse);
			descend.Descending_Bits(sparse, forebear);
			maybe_y = forebear;
			descending_loop: while (!maybe_y.IsNullNode()) {
			    maybe_y = descend.NextNodeForBits(sparse, maybe_y);
			    // disallow x and its descendants.
			    if (maybe_y.equals(current)) {
				descend.ClearSubTree(sparse, maybe_y);
				continue descending_loop; }
			    if (IsOnList(sparse, maybe_y, y_list)) {
				break found_x; }
			} // end descending_loop
			geezer = GetBoundaryNode(sparse, current, current);
			if (!geezer.IsNullNode()) {
			    Indices.addSubResult(geezer, current, current); } 
			break found_x;
		    } // end if kids.size() > 1
		} // end for i = 0; i < x_ancestors.size(); i++
	    } // end found_x
	} // end for n = 0; n < sparse.size(); n++
	return Indices;
    } // end method Not_y

} 



