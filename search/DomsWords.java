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
 * A domsWords x if the subtree rooted at A contains 
 * x number of leaf nodes.
 */
public class DomsWords extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, 
					ArgList x_list, int how_many) {
        Vector text_nodes;
	SentenceResult Indices = new SentenceResult();
        Node forebear, current, null_node = new Node("NULL");
        int n;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    x_loop:  if (IsOnList(sparse, current, x_list)) {
		text_nodes = GetTextNodes(sparse, current);
		text_nodes = PipeList.PurgeNodeList(sparse, text_nodes, 
						    Vitals.word_ignore_list);
		if (text_nodes.size() == how_many) {
		    forebear = GetBoundaryNode(sparse, current);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, current, null_node); }
		}
	    } // end x_loop
	} // end for n= 0; n < sparse.size(); n++
	return Indices;
    } 

} 



