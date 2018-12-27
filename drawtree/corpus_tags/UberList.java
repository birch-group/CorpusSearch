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

package drawtree;

import java.util.*;
import java.io.*;
import io.*;
import basicinfo.*;

public class UberList extends CorpusTags {
    private POSTagList postle;
    private SynTagList syntle;
    private CatTagList cattle;
    private InFileDominatrix tags_in_file;
    private String file_name, syn_divide_str, pos_divide_str, empty_divide_str;
    private Vector syn_divide, pos_divide, empty_divide;

    public UberList() {};

    public boolean makeUberList(String in_file_name) {
	boolean did_it = true;

	try {
	    this.file_name = in_file_name;
	    tags_in_file = new InFileDominatrix(in_file_name);
	    postle = new POSTagList();
	    syntle = new SynTagList();
	    ReadTagsFile(tags_in_file);
	    syn_divide = PipeList.makeCharacterList(syn_divide_str);
	    pos_divide = PipeList.makeCharacterList(pos_divide_str);
	    empty_divide = PipeList.makeCharacterList(empty_divide_str); }
	catch (Exception e) {
	    System.err.print("WARNING! unable to process ");
	    System.err.println(in_file_name + " as tags file.");
	    System.err.println("");
	    did_it = false; } 
        finally { return did_it; }}

    private void ReadTagsFile(InFileDominatrix in_rix) {
	String str;
	OneTag onet;
	boolean syn_mode = true;
	boolean pos_mode = false;
	boolean empty_cat_mode = false;

	read_loop:  while(!in_rix.EOF) {
	    str = in_rix.NextString();
	    if (in_rix.EOF) { break read_loop; }
	    if (str.equals("~SYNTACTIC")) {
		syn_mode = true;
		in_rix.FlushLine(); 
	        continue read_loop; }
	    if (str.equals("~SYN_DIVIDERS:")) {
		syn_divide_str = in_rix.NextString(); 
	        continue read_loop; }
	    if (str.equals("~POS")) {
		syn_mode = false;
		pos_mode = true;
		in_rix.FlushLine(); 
		continue read_loop; }
	    if (str.equals("~POS_DIVIDERS:")) {
		pos_divide_str = in_rix.NextString(); 
	        continue read_loop; }
	    if (str.equals("~EMPTY")) {
		syn_mode = false;
		pos_mode = false;
		empty_cat_mode = true;
		in_rix.FlushLine();
		continue read_loop; }
	    if (str.equals("~EMPTY_CAT_DIVIDERS:")) {
		empty_divide_str = in_rix.NextString();
		continue read_loop; }
	    if (empty_cat_mode) {
		cattle = new CatTagList(str);
		continue read_loop;
	    }
	    onet = new OneTag(str); 
	    if (syn_mode) {
		syntle.addOneTag(onet); 
	        continue read_loop; }
	    if (pos_mode) {
		postle.addOneTag(onet); 
	        continue read_loop; } }
	in_rix.Close();
    }

    public boolean legitSynTag(String taggle) {
	return (syntle.legitTag(taggle, syn_divide));
    }

    public boolean legitPOSTag(String taggle) {
	return (postle.legitTag(taggle, pos_divide)); 
    }

    public boolean legitCatTag(String taggle) {
	return (cattle.legitTag(taggle, empty_divide)); } 

    public POSTagList getPOSTagList() {
	return postle; }

    public SynTagList getSynTagList() {
	return syntle; }

    public CatTagList getCatTagList() {
	return cattle; }

    public void PrintToSystemErr() {
	System.err.println("tags file:  " + file_name);
	postle.PrintToSystemErr();
	syntle.PrintToSystemErr(); 
    }

} 






