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


/*
  Beth Randall:  Oct. 2000
  Commander.java
  This class contains methods that deal with an input command file
  for CorpusSearch.
  methods:
  Sameness
  Num_or_Last
  ClearingHouse
*/
package command;

import java.io.*;
import java.util.*;
import search.*;

public class Commander extends CommandInfo {

    /*
      Sameness --finds pairs of indices which must coincide or 
      not coincide.
    */
    public static SameList Sameness (Vector arg_List) {
        SameList sames = new SameList();
	String arg1, arg2, nude1, nude2;
	Integer dex;
        int j, k;

        // look for matching labels to implement same-instance.
        for (j = 0; j < arg_List.size(); j++) {
            for (k = (j+1); k < arg_List.size(); k++){
                arg1 = (String)arg_List.elementAt(j);
                arg2 = (String)arg_List.elementAt(k);
                if (arg1.equals(arg2)) {
                    // no need to find elt at k again
                    // Also, j and k must refer to different search-function
                    // calls.  To ensure this, disallow
                    // j%2 = 0 and k-j = 1.
                    dex = new Integer(k);
                    arg_List.setElementAt("dummy" + dex.toString(), k);
                    if (!(((k - j) == 1) && (j%2 == 0))) {
			sames.addCoincidence(true, j, k);
                    } // end if !(((k - j) == 1) && (j%2 == 0)
                } // end if match found
                else {   // look for "must not match."
                    // k must be greater than arg_list.size() - 3 to
                    // ensure that we are only dealing with
                    // arguments to the most recent call to AND.  It's not a
                    // problem when coincide is true because
                    // matching args have been replaced with dummies.
                    // Also, j and k must refer to different search-function
                    // calls.  To ensure this, disallow
                    // j%2 = 0 and k-j = 1.
                    if (HasPrefix(arg1) && HasPrefix(arg2)
			&& (k > (arg_List.size() - 3)) && (!(((k - j) == 1)
							     && (j%2 == 0)))) {
                        // don't enforce "must not match" unless the
                        // arguments are the same, except for
                        // different indices.
                        nude1 = StripArg(arg1);
                        nude2 = StripArg(arg2);
                        if (nude1.equals(nude2)) {
			    sames.addCoincidence(false, j, k);
                        } // end if nude1 == nude2
                    } // end for must not match
                } // end else (did not match)
            } // end for k
        } // end for j
        return sames;
    } // end method Sameness

    /*
      strips prefix (e.g., [2]) from argument.
    */
    public static String StripArg (String next_val) {

	if ((next_val.startsWith("[")) &&
	    (next_val.indexOf("]") > 0)) {
	    return(next_val.substring(next_val.indexOf(']')+1));
	} // end if next_val startsWith "["
	if ((next_val.startsWith("!["))
	    && (next_val.indexOf("]") > 0)) {
            return("!" + next_val.substring(next_val.indexOf(']')+1));
	} // end if next_val startsWith "!["
	return next_val;
    } // end method strip_arg

    public static boolean HasPrefix (String arg) {
	if (arg.startsWith("[")) {
	    return true;
	}
	return false;
    }

} // end class Commander.java

