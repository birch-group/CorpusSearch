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
 * A iDomsNumber x if A has x number of children.
 */
public class iDomsNumber extends Syntax {

    public static SentenceResult Plain (SynTree sparse, ArgList x_List, 
					ArgList y_List, int which) {
	Vector children;
        SentenceResult Indices = new SentenceResult();
        Node forebear, current, which_child;
        int n;

        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current,  x_List)) {
		children = sparse.GetDaughters(current);
		children = PipeList.PurgeNodeList(sparse, children, 
						  Vitals.ignore_list);
                if (which - 1 < children.size()) {
		    which_child = (Node)children.elementAt(which - 1);
		    if (IsOnList(sparse, which_child, y_List)) {
			forebear = GetBoundaryNode(sparse, current);
			if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, current, which_child); }
		    } 
		} 
            } 
        } 
        return Indices; }

    public static SentenceResult Not_y (SynTree sparse, ArgList x_List, 
					ArgList y_List, int which) {
        Vector ancestors, children;
        SentenceResult Indices = new SentenceResult();
        Node forebear, current, which_child;
        int n, i;

        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current,  x_List)) {
                children = sparse.GetDaughters(current);
                children = PipeList.PurgeNodeList(sparse, children, 
						  Vitals.ignore_list);
                if (which - 1 < children.size()) {
                    which_child = (Node)children.elementAt(which - 1);
                    if (!IsOnList(sparse, which_child, y_List)) {
			forebear = GetBoundaryNode(sparse, current);
			if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, current, which_child); }
                    } 
                } 
            } 
        } 
        return Indices; }

} 

