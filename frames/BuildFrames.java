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
  BuildFrames.java
  builds frames_vec (containing subcat frames information) from
  one sentence vector.
*/
package frames;

import java.io.*;
import java.util.*;
import syntree.*;
import basicinfo.*;
import search.*;

public class BuildFrames extends LocalLoop {

    /*
      OneSentence -- builds sub-cat frame for one sentence.
      input -- x_List.
      y_List
      output -- void.
    */
    public static SentenceFrame OneSentence (Vector x_List, Vector y_List) { 
	SingleFrame framed;
	SentenceFrame per_sentence;
	Node curr_node, mother, grandma, one_sis, first;
	Vector one_item, curr_vec, sisters, syn_kernel;
	Vector text;
	int k, n; // loop indices.

	per_sentence = new SentenceFrame();
	try {
	    for (n = 0; n < sparse.size(); n++) {
		curr_node = sparse.NodeAt(n);
		if (PipeList.IsOnList (curr_node, y_List)) {
		    mother = sparse.GetMother(curr_node);
		    if (PipeList.IsOnList(mother, x_List)) {
			framed = new SingleFrame();
			grandma = sparse.GetMother(mother);
			sisters = sparse.GetDaughters(grandma);
			for (k = 0; k < sisters.size(); k++) {
			    one_sis = (Node)sisters.elementAt(k);
			    framed.addToSisFrame(one_sis);
			    if (Matcher.StarMatch("PP*", one_sis.getLabel())) {
				text = sparse.getText(one_sis);
				first = (Node)text.elementAt(0);
                                framed.addToSisFrame(first); }
			    if (one_sis.equals(mother)) {
				framed.addToSynKernel(mother);
				framed.addToSynKernel(curr_node); }
			    if (PipeList.IsOnList(one_sis, 
						  fframes.getSynLabelsList())) {
				framed.addToSynKernel(one_sis); }
			} // end for k = 0; k < sisters.size()
			framed.setIDNode(sparse.ID_POS);
			framed.purgeSister(sparse);
			per_sentence.addSingleFrame(framed);
		    } 
		} 
	    } 
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    System.err.println("in BuildFrames.subFrame:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace(); }
	finally { return per_sentence; }
    } 

} 


