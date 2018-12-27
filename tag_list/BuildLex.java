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
package tag_list;

import java.io.*;
import java.util.*;
import syntree.*;
import basicinfo.*;
import search.*;

public class BuildLex extends LocalLoop {

    /*
    */
    public static SentenceList OneSentence (Vector syn_divide, 
					    Vector pos_divide,
					    Vector trace_divide,
					    ArgList empties) { 
	SentenceList per_sentence;
	Node curr_node, first;
	int n;
	String label;

	per_sentence = new SentenceList();
	try {
	    thru_sentence: for (n = 0; n < sparse.size(); n++) {
		curr_node = sparse.NodeAt(n);
		if (!sparse.IsLeafText(curr_node)) {
		    label = curr_node.getLabel();
		    if (label.length() == 0) {
			continue thru_sentence; }
		    if (sparse.IsLeafPOS(curr_node)) {
			first = sparse.FirstDaughter(curr_node);
			if (empties.hasMatch(first)) {
			    per_sentence.addSynEntry(label, syn_divide);
			    per_sentence.addTraceEntry(first.getLabel(),
						       trace_divide);
			    continue thru_sentence; }
			else {
			    per_sentence.addPOSEntry(label, pos_divide); 
			    continue thru_sentence; } }
		    else {
			per_sentence.addSynEntry(label, syn_divide); } } }
	}
	catch (Exception e) {
	    System.err.println("in BuildLex:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace(); }
	finally { return per_sentence; }
    } 
    
} 


