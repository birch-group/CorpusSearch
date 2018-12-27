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
import basicinfo.*;
import command.*;

/**
 * prints only nodes with a given list of POS labels.
 * Designed for use with CODING, to print an output file
 * of only CODING nodes.
 */
public class GetOnly extends Syntax {


    /*
      Plain --
    */
    public static Vector Plain (SynTree sparse, ArgList lonely_list) {
        Vector result = new Vector();
	String to_print = new String("Phineas");
        Node current, daughter;
        int n;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    if (IsOnList(sparse, current, lonely_list)) {
		daughter = sparse.FirstDaughter(current);
		to_print = daughter.getLabel();
		if (CommandInfo.print_only_add_IDs) {
		    to_print += "@";
		    if (!(sparse.ID_POS).IsNullNode()) {
			to_print += sparse.ID_TEXT.getLabel(); }}
		result.addElement(to_print); }
	} 
        return result;
    } 

} 



