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
public class DomsWordsAssign extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, ArgList x_list, 
					int how_many, String what_search) {
	if (clearingHouse(what_search).equals("MORE")) {
	    return(DomsWordsMore.Plain(sparse, x_list, how_many)); }
	if (clearingHouse(what_search).equals("LESS")) {
	    return(DomsWordsLess.Plain(sparse, x_list, how_many)); }
	else {
	    return(DomsWords.Plain(sparse, x_list, how_many)); }}

    private static String clearingHouse (String which_search) {

	if (which_search.endsWith("More") | which_search.endsWith("more")
	    | which_search.endsWith(">")  | which_search.endsWith("MORE")) {
	    return ("MORE"); } 
	if (which_search.endsWith("Less") | which_search.endsWith("less")
	    | which_search.endsWith("<")  | which_search.endsWith("LESS")) {
	    return ("LESS"); }
	else { return("PLAIN"); } }
} 



