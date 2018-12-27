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

package print;

import java.io.*;
import java.util.*;
import syntree.*;
import command.*;
import stats.*;
import search_result.*;
import revise.*;
import io.*;
import basicinfo.*;

/**
 * handles printing output for CorpusSearch.
 */
public class PrintOut extends Parameters{

    /**
     * prints query (regular or shorthand).
     */
    public static void Queries (String queer, int tab, PrintWriter outer) {
        StringReader command_read;
        StringBuffer val_buff = new StringBuffer();
        String next_val = "", prev_val = "";
        int c = 0, i, t, pretty_length = 0;

        command_read = new StringReader(queer);
        try {
	    char_by_char:  do {
                c = command_read.read();
                if (c > 32) {
                    val_buff.append((char)c);
                    continue char_by_char; }
                else {
                    next_val = val_buff.toString();
                    val_buff = new StringBuffer();
                    if (next_val.equals("AND") || next_val.equals("OR")
			|| next_val.equals("XOR")) {
                        outer.println("");
                        for (i = 0; i < tab; i++) {
                            outer.print(" "); }
                        pretty_length = tab; }
                    if (!(next_val.startsWith("(")) &&
			!(next_val.startsWith(")"))){
                        pretty_length += next_val.length();
                        if (pretty_length > margin) {
                            outer.println("");
                            for (i = 0; i < tab; i++) {
                                outer.print(" "); }
                            pretty_length = tab; }
                        if (!(prev_val.startsWith("(") ||
			      prev_val.endsWith(")")))  {
                            outer.print(" ");
                            pretty_length += 1; }
                        outer.print(next_val + " "); }
                    else {
                        pretty_length += next_val.length();
                        if (pretty_length > margin) {
                            outer.println("");
                            for (i = 0; i < tab; i++) {
                                outer.print(" "); }
                            pretty_length = tab; }
                        if (prev_val.endsWith("AND") || prev_val.endsWith("OR")
			    || prev_val.endsWith("XOR")) {
                            outer.print(" ");
                            pretty_length += 1; }
                        outer.print(next_val + " "); }
                } // end else (c <= 32)
                prev_val = next_val;
            } while (c != -1); // end char_by_char
            outer.println("");
        } // end try
        catch (Exception e) {
            System.err.println("In PrintOut.Queries:  ");
            System.err.println("queer:  " + queer);
            System.err.println("next_val:  " + next_val);
            System.err.println(e.getMessage()); }
        finally { return; }
    } // end method Queries

    /**
     * prints out a vector of strings; each string printed on a 
     * separate line; avoids using \n.
     * @param stringy -- Vector of strings.
     * @param tab -- number of spaces to print before each string.
     * @param outer -- PrintWriter to print strings in.
     * @param tab_first -- true if first item in Vector will be tabbed.
     * @return -- void.
     */
    public static void StringVec (Vector stringy, int tab,
                                  PrintWriter outer, boolean tab_first) {
        int i, j;

        for (i = 0; i < stringy.size(); i++) {
            if ((i > 0) || (tab_first)) {
                for (j = 0; j < tab; j++) {
                    outer.print(" "); } }
            outer.println((String)stringy.elementAt(i)); }
        return; }

    /**
     * prints Preface at top of output file.
     */
    public static void Preface (String dest_name, String command_name,
                                PrintWriter outer, Vector source_list) {

	CommonPreface(dest_name, command_name, outer, source_list);
        outer.println("node:   " + CommandInfo.node);
	if (CommandInfo.reconstruct) {
	    outer.println("reconstruct:  " + CommandInfo.recon_str); }
        outer.print("query:  ");
        Queries(CommandInfo.query, 2, outer);
	if (CommandInfo.revise) { 
	    PrefaceRevise(outer, CommandInfo.tasker); }
        outer.println("*/"); }

    public static void PrefaceComplement (String dest_name, 
					  String command_name,
					  PrintWriter outer, 
					  Vector source_list) {
	
	CommonPreface(dest_name, command_name, outer, source_list);
        outer.println("node:   " + CommandInfo.node);
        outer.print("query:  ");
        Queries(CommandInfo.query, 2, outer);
	outer.println("");
	outer.println("COMPLEMENT FILE.");
	outer.println("*/"); }


    /**
     * prints Preface at top of output file.
     */
    public static void Preface (OutFileDominatrix outt, String command_name, 
				Vector source_list) {

	PrintWriter outer = outt.getPrintWriter();
	String dest_name = outt.FILE_NAME;
        CommonPreface(dest_name, command_name, outer, source_list);
        outer.println("node:   " + CommandInfo.node);
	if (CommandInfo.reconstruct) {
	    outer.println("reconstruct:  " + CommandInfo.recon_str); }
        outer.print("query:  ");
        Queries(CommandInfo.query, 2, outer);
        if (CommandInfo.revise) {
            PrefaceRevise(outer, CommandInfo.tasker); }
        outer.println("*/"); }

    public static void PrefaceRevise (PrintWriter outer, TaskList tl) { 
	OneTask ot;

	outer.println("");
	for (int i = 0; i < tl.size(); i++) {
	    ot = tl.taskAt(i);
	    outer.println(ot.getCommand()); } }

    /**
     * prints parts of the preface that are common to output, frames,
     *  and lexicon files.
    */
    public static void CommonPreface (String dest_name, String command_name,
				      PrintWriter outer, Vector source_list) {
	Date calendar = new Date();
	String today = new String("didn't unload date properly");
	today = calendar.toString();
	int i;

	outer.println("/*");
	outer.println("PREFACE:  ");
	outer.println(Vitals.c_note);
	outer.println("Date:  " + today);
	PrintOut.FileBlock(outer, source_list, dest_name, command_name);
	if (remark_exists) {
	    outer.print("remark:  ");
	    PrintOut.StringVec(remark, 2, outer, false);
	    outer.println(""); }
	if (CommandInfo.use_def_file) {
	    outer.print("definition file:  ");
	    outer.println(CommandInfo.def_name);
	    //outer.print("shorthand:  ");
	    //Queries(CommandInfo.orig_query, 2, outer);
	} 
	return; }

    /**
     * prints Preface at top of frames output file.
     */
    public static void PrefaceFrames (String dest_name, String command_name,
				      PrintWriter outer, Vector source_list) {

	CommonPreface(dest_name, command_name, outer, source_list);
        outer.print("local frames:  ");
        Queries(CommandInfo.query, 2, outer);
        outer.println("*/"); }

    public static void PrefaceLexicon (String dest_name, String command_name, 
				       PrintWriter outer, Vector source_list) {

	CommonPreface(dest_name, command_name, outer, source_list);
	outer.println("Lexicon:  ");
	outer.println("*/");
	outer.println(""); }

    public static void PrefaceLabelLexicon (String dest_name, 
					    String command_name, 
					    PrintWriter outer, 
					    Vector source_list) {

	CommonPreface(dest_name, command_name, outer, source_list);
	outer.println("Label Lexicon:  ");
	outer.println("*/");
	outer.println(""); }

    public static void PrefaceTagList (String dest_name, String command_name,
				       PrintWriter outer, Vector source_list) {

	CommonPreface(dest_name, command_name, outer, source_list);
	outer.println("Tag List:  for use with CorpusDraw:  ");
	outer.println("*/");
	outer.println(""); }

    public static void PrefaceCoding (String dest_name, String command_name,
                                       PrintWriter outer, Vector source_list) {

	CommonPreface(dest_name, command_name, outer, source_list);
	outer.println("node: " + CommandInfo.node);
	if (CommandInfo.reconstruct) {
	    outer.println("reconstruct:  " + CommandInfo.recon_str); }
        outer.println("coding_query:  ");
	PrintCodingQuery(CommandInfo.coding_query, outer);
        outer.println("*/");
        outer.println(""); }

      public static void PrintCodingQuery (String codquer, PrintWriter outer) {
	StringBuffer next_bit = new StringBuffer("");
	char a_char;
	int i;
	boolean in_curlies = false;
	boolean prev_end_curly = false;

	cod_loop:  for (i = 0; i < codquer.length(); i++) {
	    a_char = codquer.charAt(i);
	    if (prev_end_curly && a_char == ' ') {
		prev_end_curly = false;
		continue cod_loop; }
	    next_bit.append(a_char);
	    if (a_char == '{') {
		in_curlies = true;
		outer.print(next_bit.toString());
		next_bit.delete(0, next_bit.length()); 
		continue cod_loop; }
	    if (a_char == ':' && in_curlies) {
		outer.println("");
		outer.print("    " + next_bit.toString());
		next_bit.delete(0, next_bit.length()); 
	        continue cod_loop; }
	    if (a_char == ' ') {
		outer.print(next_bit);
		next_bit.delete(0, next_bit.length());
		continue cod_loop; }
	    if (a_char == '}') {
		in_curlies = false;
		prev_end_curly = true;
		outer.println("");
		outer.println("}");
		next_bit.delete(0, next_bit.length());
		continue cod_loop; }
	} // end cod_loop
	return; }

    /**
     * prints Header that appears once per source file.
     */
    public static void Header (String name, PrintWriter outer) {

        outer.println("/*");
        outer.println("HEADER:");
        outer.println("source file:  " + name);
        outer.println("*/"); }

    /**
     * prints Footer that appears once per source file.
     */
    public static void Footer (StatsPerFile spats, PrintWriter outer) {

        outer.println("/*");
        outer.println("FOOTER");
	spats.PrintToPrintWriter(outer);
        outer.println("*/"); }

    /**
     * prints summary block at end of output file.
     */
    public static void BigFooter (StatsPerSearch search_info, 
				  PrintWriter outer) {
	    
        try {
            outer.println("/*");
            outer.println("SUMMARY:  ");
	    search_info.PrintToPrintWriter(outer);
	    outer.println("*/"); 
	    if (Parameters.output_format.equals("HTML")) {
		outer.println("</PRE></HTML>");} }
        catch (Exception e) {
            System.err.println("ERROR!  in PrintOut.BigFooter:  " );
            System.err.println(e.getMessage()); }
        finally {  return; }}

    /**
     * prints listing of result indices.
     */
    public static void Comments(SentenceResult Indices, PrintWriter outer) {
        int j, k, length = 0;
	SubResult subble;
        Node nodal, boundary;
	String to_print;
	boolean first = true;

	if (Parameters.ur_text_only) { return; }
	//Indices.PrintToPrintWriter(outer);
        outer.println("/*");
	Indices.setNoDupes();
        thru_result: for (j = 0; j < Indices.size(); j++) {
            subble = Indices.NoDupesAt(j);
	    if (subble.IsNull()) { continue thru_result; }
	    length = 0;
	    boundary = subble.getBoundary();
	    to_print = boundary.getIndex() + " " + boundary.getLabel() + ":  ";
	    length += to_print.length();
	    outer.print(to_print);
	    first = true;
            thru_sub: for (k = 0; k < subble.size(); k++) {
                nodal = subble.NoDupesAt(k);
		if (nodal.IsNullNode()) { continue thru_sub; }
		if (first) {
		    to_print = nodal.getIndex() + " " + nodal.getLabel(); 
		    first = false; }
		else {
		    to_print = ", " + nodal.getIndex() + " " + nodal.getLabel(); }
		length += to_print.length();
		if (length > margin) {
		    outer.println("");
		    length = 0; }
		outer.print(to_print); }
            outer.println(""); }
        outer.println("*/");
	for (j = 0; j < Indices.commentsSize(); j++) {
	    to_print = Indices.getComment(j);
	    outer.println("/*");
	    outer.println(to_print);
	    outer.println("*/"); }
	//	outer.println("");
        return;
    } 

    /**
     * prints block giving names of command file, input files, and output file.
     */
    public static void FileBlock (PrintWriter out_file, Vector source_list, 
				  String dest_name, String command_name) {
        String first, last;

        out_file.println("");
	if (!Parameters.output_format.equals("HTML")) {
	    out_file.println("command file:     " + command_name); }
        if (CommandInfo.use_prefs) {
            out_file.println("preference file:  " + CommandInfo.prefs_name); }
        first = (String)source_list.firstElement();
        if (!first.endsWith(corpus_file_extension)) {
            if (source_list.size() == 1) {
                out_file.println("input file:       " + first); }
            else {
		last = (String)source_list.lastElement();
                out_file.println("input files:      " + first);
		out_file.println("through:          " + last);
		out_file.println(source_list.size() + " files."); }}  
	if (!Parameters.output_format.equals("HTML")) {
	    out_file.println("output file:      " + dest_name); }
        out_file.println("");
        return; }

    /*
      UrTextStuff -- directs printing ur_text in output files.
      input -- PrintWriter outfile -- file in which to print.
      output -- void.
    */
    public static void UrTextStuff (Vector ur, SynTree sparse, 
				    PrintWriter outfile) {

	if (ur.isEmpty()) {
            urk.PureText(sparse, outfile);
            return; }
        PrintOut.StringVec(VectorAux.ur_vec, 0, outfile, false);
        return;
    } 

} 










