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
 * A dominates B if B is contained in the subtree with A at its root.
 */
public class Dominates extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, 
					ArgList x_list, ArgList y_list) {
        Vector ancestors;
	SentenceResult Indices = new SentenceResult();
        Node forebear, geezer, current;
        int n, j, i;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    if (IsOnList(sparse, current, y_list)) {
		ancestors = sparse.GetAncestors(n);
		for (i = 0; i < ancestors.size(); i++) {
		    geezer = (Node)ancestors.elementAt(i);
		    if (IsOnList(sparse, geezer, x_list)) {
			forebear = GetBoundaryNode(sparse, geezer);
			if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, geezer, current); } 
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
        Vector ancestors;
        SentenceResult Indices = new SentenceResult();
        Node forebear, geezer, current;
        int n, j, i;

        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            y_loop: if (IsOnList(sparse, current, y_list)) {
                ancestors = sparse.GetAncestors(n);
                for (i = 0; i < ancestors.size(); i++) {
                    geezer = (Node)ancestors.elementAt(i);
                    if (IsOnList(sparse, geezer, x_list)) {
			break y_loop; } }
		forebear = GetBoundaryNode(sparse, current);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, current); }
	    } 
        } 
        return Indices;
    } 

    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse, 
					ArgList x_list, ArgList y_list) {
        Vector descendants, ancestors;
        SentenceResult Indices = new SentenceResult();
        Node heir, forebear, current;
        int n, j, i;

        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
	    heir = current;
            x_loop: if (IsOnList(sparse, current, x_list)) {
                descendants = sparse.GetDescendants(n);
                for (i = 0; i < descendants.size(); i++) {
                    heir = (Node)descendants.elementAt(i);
                    if (IsOnList(sparse, heir, y_list)) {
			break x_loop; }
		}
		forebear = GetBoundaryNode(sparse, current);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, heir); }
	    } 
        } 
        return Indices;
    } 

}



