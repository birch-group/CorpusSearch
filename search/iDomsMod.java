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
 * A iDomsMod C B if A dominates B and the only nodes (if any)
 * intervening between A and B have label C.
 * Designed for the conjunction problem.
 */
public class iDomsMod extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, ArgList x_list, 
					ArgList mod_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node forebear, geezer, current;
	Vector ancestors;
        int n, i;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    if (IsOnList(sparse, current, y_list)) {
		ancestors = sparse.GetAncestors(n);
		ancestors_loop:  for (i = 0; i < ancestors.size(); i++) {
		    geezer = (Node)ancestors.elementAt(i);
		    if (IsOnList(sparse, geezer, x_list)) {
			forebear = GetBoundaryNode(sparse, geezer);
			if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, geezer, current); } 
		    } 
		    if (IsOnList(sparse, geezer, mod_list)) {
			continue ancestors_loop; }
		    else { break ancestors_loop; }
		} 
	    } 
	} 
	return Indices; }

    /*
      Not_x --
    */
    public static SentenceResult Not_x (SynTree sparse, ArgList x_list,
                                        ArgList mod_list, ArgList y_list) {
        SentenceResult Indices = new SentenceResult();
        Node forebear, geezer, current;
        int n, i;
	Vector ancestors;

        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            if (IsOnList(sparse, current, y_list)) {
                ancestors = sparse.GetAncestors(n);
                ancestors_loop:  for (i = 0; i < ancestors.size(); i++) {
                    geezer = (Node)ancestors.elementAt(i);
                    if (!IsOnList(sparse, geezer, x_list)) {
			forebear = GetBoundaryNode(sparse, geezer);
			if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, geezer, current); } 
                    } 
                    if (IsOnList(sparse, geezer, mod_list)) {
                        continue ancestors_loop; }
		    else { break ancestors_loop; }
                } 
            } 
        } 
        return Indices; }

    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse, ArgList x_list,
                                        ArgList mod_list, ArgList y_list) {
        Vector descendants;
        SentenceResult Indices = new SentenceResult();
        Node heir = new Node("NULL"), forebear, current;
        int n, i;

        tree_loop: for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            if (IsOnList(sparse, current, x_list)) {
                descendants = sparse.GetDescendants(n);
                descendants_loop:  for (i = 0; i < descendants.size(); i++) {
                    heir = (Node)descendants.elementAt(i);
                    if (IsOnList(sparse, heir, y_list)) {
			continue tree_loop; }
		    if (IsOnList(sparse, heir, mod_list)) {
                        continue descendants_loop; }
                    else { 
			descendants = RemoveSubtree(sparse, descendants, i); }
                } 
		forebear = GetBoundaryNode(sparse, current);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, heir); }
	    } 
	} 
        return Indices; }

    private static Vector RemoveSubtree(SynTree sparse, Vector descendants, int index) {
	Node sub_root, descend;
	
	sub_root = (Node)descendants.elementAt(index);
	for (int j = index; j < descendants.size(); j++) {
	    descend = (Node)descendants.elementAt(j);
	    if (sparse.dominates(sub_root, descend)) {
		descendants.removeElementAt(j); 
		j--; } }
	return descendants; }
} 



