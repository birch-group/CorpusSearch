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

package csearch;

import java.io.*;
import java.util.*;
import command.*;
import io.*;
import print.*;
import stats.*;
import CSParse.*;
import frames.*;
import basicinfo.*;
import syntree.*;
import search_result.*;
import revise.*;

public class Meat extends CorpusSearch {

    public static String file_name = "UNKNOWN_FILE";
    public static boolean searching_output = false;
    public static StatsPerFile file_info, comp_file_info;
    public static StatsPerSearch search_info, comp_search_info;
    public static boolean empty_source = false;
    public static File source_file;
    public static OutFileDominatrix out_dom, comp_dom;
    public static PrintWriter OutStuff, CompStuff;

    /*
      CrankThrough -- cranks through input file.
    */
    public static void CrankThrough () {
        int i;

        try {
	    ReadIn.PrefsAndQuery(command_name);
	    if (CommandInfo.print_complement) {
		comp_name = dest_name.substring(0, dest_name.lastIndexOf("."));
		comp_name += ".cmp";
		comp_dom = new OutFileDominatrix(comp_name);
		CompStuff = comp_dom.getPrintWriter(); }
	    if (CommandInfo.print_only) {
		MeatOnly.CrankThrough();
		return; }
	    if (CommandInfo.reformat_corpus) {
		MeatReformat.CrankThrough();
		return; }
	    if (CommandInfo.copy_corpus) {
		MeatCopy.CrankThrough();
		return; }
	    search_info = new StatsPerSearch(command_name, dest_name);
	    if (CommandInfo.print_complement) {
		comp_search_info = new StatsPerSearch(command_name, 
						      comp_name); }
            //ReadAux.CheckNodeBoundary();
	    if (command_name.endsWith(".c") || 
		CommandInfo.coding_query != "NO_CODING_QUERY_FOUND") {
		dest_name = dest_name.substring(0, dest_name.lastIndexOf("."));
		dest_name += ".cod";
		FilePrep.MakeDestinationFile();
		out_dom = new OutFileDominatrix(dest_name);
		OutStuff = out_dom.getPrintWriter();
		MeatCoding.CrankThrough();
		return; }
	    FilePrep.MakeDestinationFile();
	    //	    if (!CommandInfo.multi_output) {
	    if (CommandInfo.make_tag_list) {
		dest_name = dest_name.substring(0, dest_name.lastIndexOf("."));
		dest_name += ".tag";
		out_dom = new OutFileDominatrix(dest_name);
		OutStuff = out_dom.getPrintWriter();
		PrintOut.PrefaceTagList(dest_name, command_name, 
					     OutStuff, source_list);
		MeatTags.CrankThrough(); 
	        return; }
	    out_dom = new OutFileDominatrix(dest_name, 
					    CommandInfo.output_format);
	    OutStuff = out_dom.getPrintWriter();
	    //out_dom.report();
	    if (IoInfo.getInputFormat().equals("ottawa")) {
		MeatOtt.CrankThrough();
		return; }
	    if (CommandInfo.do_frames) {
		PrintOut.PrefaceFrames(dest_name, command_name, 
				       OutStuff, source_list);
                MeatFrames.CrankThrough();
                return; }
	    if (CommandInfo.make_lexicon) {
		PrintOut.PrefaceLexicon(dest_name, command_name,
					OutStuff, source_list);
		MeatLexicon.CrankThrough();
 		return; }
	    if (CommandInfo.make_label_lexicon) {
		PrintOut.PrefaceLabelLexicon(dest_name, command_name, 
					     OutStuff, source_list);
		MeatLabelLexicon.CrankThrough(); 
	        return; }
	    //	    if (!CommandInfo.multi_output) {
	    //PrintOut.Preface(dest_name, command_name, 
				  //	 OutStuff, source_list); }
	    if (CommandInfo.print_complement) {
		PrintOut.PrefaceComplement(dest_name, command_name, 
					   CompStuff, source_list); }
            System.err.println("Working.  Please be patient.");
            // work through list of source files.
	    arg_loop:  for (i = 0; i < source_list.size(); i++) {
                file_name = (String)source_list.elementAt(i);
                if (file_name.equals("-out")) {
                    break arg_loop; }
		if (file_name.endsWith(".out")) {
		    searching_output = true; }
                source_file = new File(file_name);
                MeatAux.CheckSource();
		if (i == 0) {
		    FilePrep.seekPrefs(file_name); 
		    PrintOut.Preface(dest_name, command_name, 
				     OutStuff, source_list);
		    ParseQuery.makeQueryTree(new StringReader(CommandInfo.query));
		    ReviseStuff();}

                empty_source = true;

                SentenceLoop.thruFile(file_name);

            } // end arg_loop
	    PrintOut.BigFooter(search_info, OutStuff);
	    OutStuff.flush();
	    if (CommandInfo.print_complement) {
		PrintOut.BigFooter(comp_search_info, CompStuff);
		CompStuff.flush(); }
	    OutMessage(dest_name); 
        } // end try
        catch (Exception e) {
            System.err.println("ERROR!  In Meat.CrankThrough:  ");
            System.err.println("Exception:  " + e.getMessage());
            System.err.println(e.getMessage());
            System.err.println(e.toString());
            e.printStackTrace();
            Goodbye.SearchExit(); }
        finally { return; } 
    } // end method CrankThrough

    protected static void ReviseStuff() {
	if (CommandInfo.revise) {
	    (CommandInfo.tasker).SetCurlers(CommandInfo.curlies); }
	//(CommandInfo.tasker).PrintToSystemErr(); }
	return; }

    private static void OutMessage(String dest_name) {
	System.err.println("Output file is ");
	System.err.println("    " + dest_name);
	if (CommandInfo.print_complement) {
	    System.err.println("Complement file is ");
	    System.err.println("    " + comp_name); }
	return; }
} 




