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
  Beth Randall: Jan. 2001 
  LocalLoop.java
*/
package label_lexicon;

import java.io.*;
import java.util.*;

import syntree.*;
import io.*;
import print.*;
import stats.*;
import basicinfo.*;

public class LocalLoop {

    public static SynTree sparse;
    public UberList ulist;
    public FileList flist;
    public StatsPerFile file_info;
    public PrintWriter OutStuff;
    public StatsPerSearch search_info;

    public LocalLoop(PrintWriter OutStuff, StatsPerSearch search_info) {
	ulist = new UberList();
	this.search_info = search_info;
	this.OutStuff = OutStuff; }

    public LocalLoop() {}

    /*
      thruFile -- loops through an input file, finding all parsed 
      sentences and searching them. 
      input -- void.
      output -- void.
    */
    public void thruFile (String file_name) {
	TBToRix mytrees;
	SentenceList per_sentence;

	file_info = new StatsPerFile(file_name);
	flist = new FileList();
	mytrees = new TBToRix(file_name, this.OutStuff);
	try {
            sent_loop:  do {
                sparse = mytrees.OneSentence();
                //sparse.PrintToSystemErr();
                if (sparse.isEmpty()) {
                    break sent_loop; }
		file_info.totalAdd1();
		per_sentence = BuildLex.OneSentence();
	        if (!per_sentence.isEmpty()) {
		    flist.addSentenceList(per_sentence);
		    file_info.tokensAdd1();
		    file_info.hitsAdd(per_sentence.size()); }
	    } while (!sparse.isEmpty());
	} 
	catch (Exception e) {
	    System.err.println("in LocalLoop.thruFile:  ");
	    System.err.println(e.getMessage());
	    mytrees.CorpusError();
	    Goodbye.SearchExit(); } 
	finally {
	    if (flist.isEmpty()) {
		EmptyFileListError(file_name);
		return;}
	    ulist.addFileList(flist);
	    search_info.file_statsAdd(file_info);
	    //PrintOut.Footer(file_info, OutStuff);
	    return; }
    } 

    /*
      EmptyFileListError -- deals with error of empty filelist.
      input -- void
      output -- void.
      side-effect -- exits program.
    */
    private void EmptyFileListError(String file_name) {
	System.err.println("");
	System.err.print("WARNING! No lexical entries were found in ");
	System.err.println(file_name);
	System.err.println(""); }

    public void EndOfSearch() {
	ulist.Sort();
	PrintLabelLex.UberList(ulist, OutStuff);
	//ulist.PrintToSystemErr();
	return; }

} 

