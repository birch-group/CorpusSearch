//  Copyright 2010 Beth Randall

/*********************************
This file is part of CorpusSearch.

CorpusSearch is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CorpusSearch is distributed in the hope that ißt will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with CorpusSearch.  If not, see <http://www.gnu.org/licenses/>.
************************************/

package io;

import java.io.*;
import java.util.*;
import syntree.*;
import basicinfo.*;

public class TBToRix extends Errata{

    protected static InFileDominatrix infile;
    protected SynTree spare;
    protected Stack index_stack;
    protected Stack end_dex_stack;
    protected Integer minus1 = new Integer(-1);
    private boolean prev_open_paren = false, got_wrapper = false;
    

    public TBToRix() {}
    
    public TBToRix (String in_file_name) {
	Init_1(in_file_name); 
        Init_2(); }

    public TBToRix (String in_file_name, PrintWriter out_file) {
	Init_1(in_file_name);
	Init_2();
        super.out_fox = out_file; }

    private void Init_1(String in_file_name) {

	infile = new InFileDominatrix(in_file_name, 
				      IoInfo.corpus_encoding); }

    private void Init_2() {
	spare = new SynTree(); 
        index_stack = new Stack();
        end_dex_stack = new Stack(); }


    public TBToRix (InFileDominatrix inn, PrintWriter out_file) {
	infile = inn;
	Init_2();
	super.out_fox = out_file;
	spare = new SynTree(); }

    public String getFileName() {
	return(infile.FILE_NAME); }

    /*
      OneSentence --  finds next sentence in input data file and builds
      vector representing it.
      Input -- void.  Uses BufferedReader buffoon found in FileInfo.
      Output -- Vector -- vector representing next input sentence.  
    */
    public SynTree OneSentence() throws IOException

    {
        String label = "";
        Integer index = new Integer(-1), last_dex, end_dex, prev_dex;
        int dex = -1; // means that first index in sentence is 0 mod matrix.

	index_stack.removeAllElements();
	end_dex_stack.removeAllElements();
	spare.RemoveAllElements();
	spare.setORTHOFalse();
	infile.newChunk();
	try {
	    got_wrapper = false;
	    read_loop: while (!infile.EOF) {
		label = infile.NextStringParen();
		if (!spare.hasORTHO()) {
		    if (label.equals("ORTHO")) {
			//System.err.println("found ORTHO:  ");
			spare.setORTHOTrue(); }}
		if (infile.EOF) {
		    break read_loop; }
 		if (infile.OPEN_PAREN) {
		    if (prev_open_paren) { 
			if (dex == 0) {
			    spare.AddLabel("");
			    got_wrapper = true; }
			else {
			    spare.AddLabel("UNKNOWN");
			    MissingSynLabelError(); } }
		    dex += 1;
		    index = new Integer(dex);
		    index_stack.push(index);
		    end_dex_stack.push(minus1);
		    prev_open_paren = true;
		    continue read_loop;
		} // end if '('
		if (infile.CLOSE_PAREN) { 
		    if (prev_open_paren) {
		        super.DegenerateLeafError(label); }
		    if (label.length() > 0) { 
			// got label or word; finishing leaf
			// add 1 to leaf index to make consistent 
			// sparse matrix.
			if (spare.intEndDexAt(dex) == dex) {
			    MissingPOSLabelError(label);
			    dex += 1;
			    spare.AddLabel("UNKNOWN");
			    spare.SetEndDexAt(dex + 1, dex); }
			dex += 1;
			spare.AddLabel(label);
			spare.SetEndDexAt(dex, dex);
			spare.SetEndDexAt(dex, dex + 1);
			end_dex = new Integer(dex);
			end_dex_stack.push(end_dex);
		    } // end if label.length() > 0
		    index = (Integer)index_stack.pop();
		    if (index.intValue() == 0) {
			dex = -1;
			spare.SetEndDexAt(spare.size() - 1, 0);
			// Tree is finished and returned here.
			// spare.PrintToSystemErr();
			spare.setConstantNodes();
			VectorAux.HandleIDStuff(spare.ID_TEXT.getLabel());
			if (!got_wrapper) {
			    MissingWrapperError();
			    spare.addWrapper(); }
			//spare.PrintToSystemErr(28, 34);
			return spare;
		    } // end if index == 0
		    // if we reached this line of code, index != 0.
		    end_dex = (Integer)end_dex_stack.pop();
		    prev_dex = (Integer)end_dex_stack.pop();
		    end_dex_stack.push(end_dex);
		    spare.SetEndDexAt(end_dex, index);
		    prev_open_paren = false;
		    continue read_loop;
		} // if infile.CLOSE_PAREN
		// if file is already indexed (such as output), 
		// throw out old indices.
		if (infile.OUT_INDEX) { 
		    prev_open_paren = true;
		    continue read_loop; }
		// got label or word
		if (VectorAux.HandleNonParsedStuff(label)) {
		    prev_open_paren = false;
		    continue read_loop; }
		if (!prev_open_paren) {
		    super.ExtraLabelError(label); 
		    prev_open_paren = false;
		    continue read_loop; }
		spare.AddLabel(label);
		prev_open_paren = false;
	    } // end while !infile.EOF
	    //outrage.close();
	} // end try
	catch (Exception e) {
	    System.err.println("in TBToRix.java:  ");
	    e.printStackTrace();
	    super.CorpusError();
	    Goodbye.SearchExit(); }
	finally {
	    if (!index_stack.isEmpty()) { super.FragError(); }
	    if (spare.isEmpty() && !infile.EOF) {
		super.EmptyTreeError(); }
	    spare.setConstantNodes();
	    //	    spare.PrintToSystemErr(0, 5);
	    return spare; }
    } // end method Run


    public void flushFile(PrintWriter outt) {
	infile.flushFile(outt); }
} 
