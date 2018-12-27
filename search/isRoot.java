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

/** A isRoot if A is the root of the sentence tree.
 */
public class isRoot extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, ArgList x_list) {
	SentenceResult Indices = new SentenceResult();

	if (IsOnList(sparse, sparse.ROOT, x_list)) {
	    Indices.addSubResult(sparse.METAROOT, 
				 sparse.ROOT, sparse.ROOT); }
	return Indices; }

    public static SentenceResult Not_x (SynTree sparse, ArgList x_list) {
	SentenceResult Indices = new SentenceResult();

        if (!IsOnList(sparse, sparse.ROOT, x_list)) {
            Indices.addSubResult(sparse.METAROOT, 
				 sparse.ROOT, sparse.ROOT); }
        return Indices; }

} 



