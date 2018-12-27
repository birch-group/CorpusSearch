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
  Beth Randall:  August 2000
  SentenceLoop.java
*/
package csearch;

import java.io.*;
import java.util.*;
import io.*;
import print.*;
import syntree.*;
import stats.*;
import search.*;
import search_result.*;
import CSParse.*;
import command.*;
import basicinfo.*;

public class SentenceLoop extends Meat {

    public static String prev_ID = "NO_FILE_ID";
    public static SynTree sparse;
    public static boolean did_it;

    /*
      thruFile -- loops through an input file, finding all parsed 
      sentences and searching them. 
      input -- void.
      output -- void.
    */
    public static void thruFile (String file_name) {
        SentenceResult OneResult;
	TBToRix mytrees;
	OutFileDominatrix outt;
	InFileDominatrix inn;
        int one_hits;

	inn = new InFileDominatrix(file_name);
	if (inn.getFormat().equals("POS_0")) {
	    SentenceLoopOtt.thruFile(inn, OutStuff); 
	    return; }
	if (inn.getFormat().equals("POS_1")) {
	    SentenceLoopPOS_1.thruFile(inn, OutStuff); 
	    return; }
	mytrees = new TBToRix(inn, OutStuff);
	try {
            file_info = new StatsPerFile(file_name);
	    if (CommandInfo.print_complement) {
		comp_file_info = new StatsPerFile(comp_name); }
	    PrintOut.Header(file_info.getFileID(), OutStuff);
	    sent_loop:  do {
		sparse = mytrees.OneSentence();
		//sparse.PrintToSystemErr();
		//mytrees.CorpusError();
                if (sparse.isEmpty()) {
                    break sent_loop; }
		OneResult = ParseQuery.evaluable(sparse); 	    
		// reconstruct goes here.
		if (CommandInfo.reconstruct) {
		    SynTree sparse_copy = sparse.synCopy();
		    ChangeTree changeable = new ChangeTree(sparse_copy);
		    did_it = changeable.reconstruct(CommandInfo.recon_arg); 
		    OneResult = ParseQuery.evaluable(sparse_copy); }
		//OneResult.PrintToSystemErr();
                if (searching_output) {
		    outputFileNames(); }
                // Now that it's established what file-vector we're adding to,
                // increment total.
                file_info.totalAdd1();
		if (CommandInfo.print_complement) { comp_file_info.totalAdd1(); }
                if (!OneResult.isEmpty()) {
		    if (CommandInfo.revise) {
			Revisions.Plain(sparse, CommandInfo.tasker, OneResult); }
                    one_hits = OneResult.CountHits();
                    file_info.hitsAdd(one_hits);
                    file_info.tokensAdd1();
                    PrintOut.UrTextStuff(VectorAux.ur_vec, sparse, OutStuff);
                    PrintOut.Comments(OneResult, OutStuff);
                    PrintTree.PrintToken(sparse, OneResult, OutStuff); 
		    continue sent_loop; }
		// now deal with empty result.
		if (CommandInfo.print_complement) {
		    one_hits = OneResult.CountHits();
		    comp_file_info.hitsAdd(one_hits);
		    comp_file_info.tokensAdd1();
		    PrintOut.UrTextStuff(VectorAux.ur_vec, sparse, CompStuff);
		    PrintTree.PrintToken(sparse, OneResult, CompStuff); } 
	} while (!sparse.isEmpty()); 
    } 
    catch (Exception e) {
	System.err.println("in SentenceLoop.thruFile:  ");
	System.err.println(e.getMessage());
	e.printStackTrace();
	mytrees.CorpusError();
	Goodbye.SearchExit(); }
    finally {
	search_info.file_statsAdd(file_info);
	PrintOut.Footer(file_info, OutStuff);
	if (CommandInfo.print_complement) {
	    comp_search_info.file_statsAdd(comp_file_info);
	    PrintOut.Footer(comp_file_info, CompStuff); }
	return; }
    }

    private static void outputFileNames() {

	if (prev_ID.equals("NO_FILE_ID")) {
	    file_info = new StatsPerFile(VectorAux.file_ID);
	    PrintOut.Header(VectorAux.file_ID, OutStuff);
	    if (CommandInfo.print_complement) {
		comp_file_info = new StatsPerFile(VectorAux.file_ID); 
		PrintOut.Header(VectorAux.file_ID, CompStuff); } }
	else {
	    if (!(VectorAux.file_ID.equals(prev_ID))) {
		PrintOut.Footer(file_info, OutStuff);
		PrintOut.Header(VectorAux.file_ID, OutStuff);
		search_info.file_statsAdd(file_info);
		file_info = new StatsPerFile(VectorAux.file_ID);
		if (CommandInfo.print_complement) {
		    PrintOut.Footer(file_info, CompStuff);
		    PrintOut.Header(VectorAux.file_ID, CompStuff);
		    comp_search_info.file_statsAdd(comp_file_info);
		    comp_file_info=new StatsPerFile(VectorAux.file_ID); } } }
	prev_ID = VectorAux.file_ID; }

} 



