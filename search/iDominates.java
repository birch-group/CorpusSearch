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
 * A iDominates B if B is a child of A.
 */
public class iDominates extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node mother, forebear, current;
        int n;
	
	//System.err.print("in iDoms: x_list, y_list:  " + x_list.toString());
	//System.err.println(", "  + y_list.toString());
			   //	x_list.PrintToSystemErr();
			   //y_list.PrintToSystemErr();
        for (n = 0; n < sparse.size(); n++) {
	    current = (Node)sparse.NodeAt(n);
	    if (IsOnList(sparse, current,  y_list)) {
		mother = sparse.GetMother(current);
		if (IsOnList(sparse, mother, x_list)) {
		    forebear = GetBoundaryNode(sparse, mother);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, mother, current); }}}}
	//if (!Indices.isEmpty()) {
	//System.err.println("in iDominates: ");
	//Indices.PrintToSystemErr();}
        return Indices; }

    public static SentenceResult Not_x (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node mother, forebear, current;
        int n;

        sparse_loop: for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current,  y_list)) {
                mother = sparse.GetMother(n);
                if (!IsOnList(sparse, mother, x_list)) {
		    forebear = GetBoundaryNode(sparse, mother);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, mother, current); }
		}
            } // end if IsOnList(current, y_List)
        } // end for n= 0;  n < sparse.size(); n++
        return Indices; }

    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
        Vector children;
	SentenceResult Indices = new SentenceResult();
        Node child = new Node("NULL"), forebear, current;
        int n, i;

        sparse_loop: for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
            found_x: if (IsOnList(sparse, current,  x_list)) {
		children = sparse.GetDaughters(n);
		children = PipeList.PurgeNodeList(sparse, children, 
						  Vitals.ignore_list);
		if (children.isEmpty()) {
		    continue sparse_loop; }
		for (i = 0; i < children.size(); i++) {
		    child = (Node)children.elementAt(i);
		    if (IsOnList(sparse, child, y_list)) {
			break found_x; } }
		// if we got to this line of code, x doesn't iDominate y.
		forebear = GetBoundaryNode(sparse, current);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, child); }
	    } 
        } 
        return Indices; }

} 



