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
package label_lexicon;

import java.io.*;
import java.util.*;
import syntree.*;
import basicinfo.*;
import search.*;

public class BuildLex extends LocalLoop {

    /*
      OneSentence -- builds sub-cat frame for one sentence.
      input -- x_List.
      y_List
      output -- void.
    */
    public static SentenceList OneSentence () { 
	SentenceList per_sentence;
	Node curr_node;
	int n;

	per_sentence = new SentenceList();
	try {
	    for (n = 0; n < sparse.size(); n++) {
		curr_node = sparse.NodeAt(n);
		if (!sparse.IsLeafText(curr_node)) {
		    per_sentence.addLexEntry(curr_node.getLabel()); }
	    } }		    
	catch (Exception e) {
	    System.err.println("in BuildLex:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace(); }
	finally { return per_sentence; }
    } 

} 


