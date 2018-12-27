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
import coding.*;
import basicinfo.*;

/**
 * used to search leaf nodes whose text entry contains data in columns,
 * e.g. (CODING q:r:s:t).
 */
public class Column extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, 
					ArgList x_list, int which, ArgList y_list) {
        Vector ancestors;
	SentenceResult Indices = new SentenceResult();
        Node coding_txt, forebear, current;
	CodeObj cobj;
	String col_label;
        int n, i;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    current_loop: if (x_list.hasMatch(current.getLabel())) {
		coding_txt = sparse.FirstDaughter(current);
		cobj = new CodeObj(current, coding_txt.getLabel());
		//cobj.PrintToSystemErr();
		// subtract 1 to allow user to start count at 1 rather 
		// than 0.
		col_label = cobj.getColumn(which - 1);
		if (y_list.hasMatch(col_label)) {
		    ancestors = sparse.GetAncestors(current);
		    for (i = 0; i < ancestors.size(); i++) {
			forebear = (Node)ancestors.elementAt(i);
			if (IsBoundary(sparse, forebear) || sparse.isMetaRoot(forebear)) {
			    Indices.addSubResult(forebear, current, 
						 coding_txt);
			    break current_loop; }
		    }
		} 
	    } 
	} 
        return Indices;
    } 

    /*
      Not_y --
    */
    public static SentenceResult Not_y (SynTree sparse, 
					ArgList x_list, int which, ArgList y_list) {
        Vector ancestors;
        SentenceResult Indices = new SentenceResult();
        Node coding_txt, forebear, current;
        CodeObj cobj;
        String col_label;
        int n, i;

	for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    current_loop: if (x_list.hasMatch(current.getLabel())) {
		coding_txt = sparse.FirstDaughter(current);
		cobj = new CodeObj(coding_txt.getLabel());
		// subtract 1 to allow user to start count at 1 rather
		// than 0.
		col_label = cobj.getColumn(which - 1);
		if (!y_list.hasMatch(col_label)) {
		    ancestors = sparse.GetAncestors(current);
		    for (i = 0; i < ancestors.size(); i++) {
			forebear = (Node)ancestors.elementAt(i);
			if (IsBoundary(sparse, forebear) || sparse.isMetaRoot(forebear)) {
			    Indices.addSubResult(forebear, current,
						 coding_txt);
			    break current_loop; }
		    }
		} 
	    } 
	} 
	return Indices;
    } 

} 



