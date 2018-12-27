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
/**
 *  Used by coding for "ELSE", builds a 
 *  SentenceResult containing all the 
 *  boundary nodes of the tree.
*/
public class Bounds extends Syntax {

    public static SentenceResult Basic (SynTree sparse) {
	SentenceResult Indices = new SentenceResult();
	TreeBits tb = new TreeBits(sparse);
	Vector boundaries;
	Node bounder;

	tb.SetBounds(sparse);
	boundaries = tb.NodesForBits(sparse);
	for (int n = 0; n < boundaries.size(); n++) {
	    bounder = (Node)boundaries.elementAt(n);
	    Indices.addSubResult(bounder, bounder, bounder); }
	return Indices;
    } 

} 



