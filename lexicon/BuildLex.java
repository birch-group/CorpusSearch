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
  Beth Randall:  Jan 2001
*/
package lexicon;

import java.io.*;
import java.util.*;
import syntree.*;
import basicinfo.*;
import search.*;

public class BuildLex extends LocalLoop {

    /*
     */
    public static boolean IgnoreIt(Node nodal) {
	String ig, nore;
       
	for (int i = 0; i < ignore_for_lex.size(); i++) {
	    ig = (String)ignore_for_lex.elementAt(i);
	    nore = nodal.getLabel();
	    if (Matcher.StarMatch(ig, nore)) {
		return true;
	    }
	}
	return false;
    }

    /*
     */
    public static boolean GoodPos(Node nodal) {
        String pos, lab;

        for (int i = 0; i < pos_list.size(); i++) {
            pos = (String)pos_list.elementAt(i);
            lab = nodal.getLabel();
            if (Matcher.StarMatch(pos, lab)) {
                return true;
            }
        }
        return false;
    }

    /*
     */
    public static boolean GoodText(Node nodal) {
        String text, lab;

        for (int i = 0; i < text_list.size(); i++) {
            text = (String)text_list.elementAt(i);
            lab = nodal.getLabel();
            if (Matcher.StarMatch(text, lab)) {
                return true;
            }
        }
        return false;
    }

    public static boolean GoodLeaf(Node pos_node, Node text_node) {

	if (IgnoreIt(pos_node)) {
	    return false; }
	if (IgnoreIt(text_node)) {
	    return false; }
	if (use_pos_list && !(GoodPos(pos_node))) {
	    return false; }
	if (use_text_list && !(GoodText(text_node))) {
	    return false; }
	return true;
    }

    /*
      OneSentence -- builds sub-cat frame for one sentence.
      input -- x_List.
      y_List
      output -- void.
    */
    public static SentenceList OneSentence () { 
	SentenceList per_sentence;
	POSPair paris;
	Node curr_node, text_node;
	int k, n; // loop indices.

	per_sentence = new SentenceList();
	try {
	    for (n = 0; n < sparse.size(); n++) {
		curr_node = sparse.NodeAt(n);
		pos_if: if (sparse.IsLeafPOS(curr_node)) {
		    text_node = sparse.FirstDaughter(curr_node);
		    if (GoodLeaf(curr_node, text_node)) {
			paris = new POSPair(curr_node, text_node);
			per_sentence.addPOSPair(paris);
		    }
		} // end if sparse.IsLeafPOS(curr_node)
	    } // end for n= 1; n < sparse.size()
	} // end try
	catch (Exception e) {
	    System.err.println("in BuildLex:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	}
	finally {
	    return per_sentence;
	}
    } // end OneSentence

} // end class BuildLex


