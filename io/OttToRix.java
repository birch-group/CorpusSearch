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

package io;

import java.io.*;
import java.util.*;
import syntree.*;
import basicinfo.*;

// dealing with Ottowa POS input.

public class OttToRix extends Errata{

    protected static InFileDominatrix infile;
    protected SynTree spare;
    protected Stack index_stack;
    protected Stack end_dex_stack;
    protected Integer minus1 = new Integer(-1);
    private boolean prev_open_paren = false, got_wrapper = false;
    private boolean through_preamble = false;

    public OttToRix() {}
    
    public OttToRix (String in_file_name) {
	Init_1(in_file_name);
        Init_2(); }

    public OttToRix (String in_file_name, PrintWriter out_file) {
	Init_1(in_file_name);
	Init_2();
        super.out_fox = out_file; }

    public OttToRix(InFileDominatrix inn, PrintWriter out_file) {
	infile = inn;
	Init_2();
	super.out_fox = out_file; }

    private void Init_1(String in_file_name) {
	
	infile = new InFileDominatrix(in_file_name, 
				      IoInfo.corpus_encoding); }

    private void Init_2 () {
	spare = new SynTree(); 
        index_stack = new Stack();
        end_dex_stack = new Stack(); }

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
        String label = "", xml_tag = "", text = "", pos = "";
        Integer index = new Integer(-1), last_dex, end_dex, prev_dex;
        int dex = -1; // means that first index in sentence is 0 mod matrix.
	Vector slash_list;

	index_stack.removeAllElements();
	end_dex_stack.removeAllElements();
	spare.RemoveAllElements();
	infile.newChunk();
	try {
	    got_wrapper = false;
	    // ignore everything before <text>.
	    read_preamble: while (!through_preamble && !infile.EOF) {
		label = infile.NextString();
		if (infile.isXMLTagStart(label)) {
		    xml_tag = infile.GetXMLTag(label);
		    if (xml_tag.equals("text")) {
			through_preamble = true;
			break read_preamble; } } }
	    read_loop: while (!infile.EOF) {
		label = infile.NextString();
		//if (infile.isXMLTagStart(label)) {
		//  xml_tag = infile.GetXMLTag(label);
		//  continue read_loop; }
		if (infile.EOF) {
		    break read_loop; }
		slash_list = SlashList.MakeList(label);
		if (slash_list.size() < 2) {
		    super.StrayWordError(label); 
		    continue read_loop; }
		if (slash_list.size() > 2) {
		    super.ExtraTagsError(label); }
		text = (String)slash_list.firstElement();
		pos = (String)slash_list.lastElement();
		if (pos.equals("CODE")) {
		    continue read_loop; }
		dex += 1;
		spare.AddLabel(pos);
		spare.SetEndDexAt(dex + 1, dex);
		spare.AddLabel(text);
		dex += 1;
		spare.SetEndDexAt(dex, dex);
		if (pos.equals("PONFP")) {
		    dex = -1;
		    //spare.SetEndDexAt(spare.size() - 1, 0);
		    // Tree is finished and returned here.
		    spare.addWrapper();
		    spare.conflateRoots();
		    //spare.PrintToSystemErr(0, 10);
		    return spare; }
	    } // end while !infile.EOF
	    //outrage.close();
	} // end try
	catch (Exception e) {
	    System.err.println("in TBToRix.java:  ");
	    e.printStackTrace();
	    super.CorpusError();
	    Goodbye.SearchExit(); }
	finally {
	    if (spare.isEmpty() && !infile.EOF) {
		super.EmptyTreeError(); }
	    //	    spare.PrintToSystemErr(0, 5);
	    return spare; }
    } // end method Run


    public void flushFile(PrintWriter outt) {
	infile.flushFile(outt); }
} 
