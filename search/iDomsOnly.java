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
 * A iDomsOnly B if B is the only child of A.
 */
public class iDomsOnly extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, 
					ArgList x_list, ArgList y_list) {
        Vector daughters;
	SentenceResult Indices = new SentenceResult();
        Node forebear, current, first;
        int n;

        tree_loop: for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    current_loop: if (IsOnList(sparse, current,  x_list)) {
		daughters = sparse.GetDaughters(n);
		daughters = PipeList.PurgeNodeList(sparse, daughters, 
						   Vitals.ignore_list);
		if (daughters.size() != 1) { continue tree_loop; }
		first = (Node)daughters.firstElement();
		if (IsOnList(sparse, first , y_list)) {
		    forebear = GetBoundaryNode(sparse, current);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, current, first ); }
		} 
	    } 
	} 
        return Indices; }

    /*
      Not_x --
    */
    public static SentenceResult Not_x (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector ancestors, daughters;
        SentenceResult Indices = new SentenceResult();
        Node mother = new Node("NULL"), forebear, current, first ;
        int n, i;

        tree_loop: for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current,  y_list)) {
		mother = sparse.GetMother(n);
		if (!IsOnList(sparse, mother, x_list)) {
		    // ensure that current is the first  child of mother.
		    daughters = sparse.GetDaughters(mother);
		    daughters = PipeList.PurgeNodeList(sparse, daughters, 
						       Vitals.ignore_list);
		    if (daughters.size() != 1) { continue tree_loop; }
		    first  = (Node)daughters.firstElement();
		    if (first.equals(current)) {
			forebear = GetBoundaryNode(sparse, mother);
			if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, mother, first ); }
		    } 
		} 
	    } 
	} 
 	return Indices; }

    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector ancestors, daughters;
        SentenceResult Indices = new SentenceResult();
        Node forebear, current, first ;
        int n, i;

        tree_loop: for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current, x_list)) {
                daughters = sparse.GetDaughters(n);
                daughters = PipeList.PurgeNodeList(sparse, daughters, 
						   Vitals.ignore_list);
                if (daughters.size() != 1) { continue tree_loop; }
                first  = (Node)daughters.firstElement();
                if (!IsOnList(sparse, first , y_list)) {
		    forebear = GetBoundaryNode(sparse, current);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, current, first ); }
		} 
	    } 
	} 
	return Indices; }

} 


