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
  Beth Randall: Oct. 2000 
  BasicCommands.java
  This class deals with basic commands found in the query file.
  Used by ReadIn.java, Definite.java
*/
package command;

import java.util.*;
import java.io.*;

import io.*;
import print.Parameters;
import revise.*;
import lexicon.LocalLoop;
import basicinfo.*;

public class BasicCommands extends CommandInfo {

    // these variables are used to set begin and end comment markers.
    private static String begin = "NO_BEGIN_COMMENT_FOUND";
    private static String end = "NO_END_COMMENT_FOUND";

    /*
      Store -- examines command found in query file.
      Stores the information in the variables held by
      CommandInfo.
      input -- String commando -- command found in query file.
      output -- boolean -- true if command was found, 
      false otherwise.
      side-effect -- information from the command file is stored in
      class CommandInfo.
    */
    public static boolean Store (String commando, InFileDominatrix infile) {

        if (Fundamentals(commando, infile)) {
            return true; }
        if (Basics(commando, infile)) {
            return true; }
        if (CorpusSpecs(commando, infile)) {
            return true; }
        if (PrintSpecs(commando, infile)) {
            return true; }
        if (ReviseSpecs(commando, infile)) {
	    return true; }
	if (LexStuff(commando, infile)) {
	    return true; }
	if (DrawStuff(commando, infile)) {
	    return true; }
	CodingQueryError(commando, infile);
        return false; }

    /*
      Fundamentals --  handles commands found in the vast
      majority of query files.
      input -- String commando -- command found in query file.
      output -- boolean -- true if command was found, 
      false otherwise.
      side-effect -- information from the command file is stored in
      class CommandInfo.
    */
    public static boolean Fundamentals (String commando,
                                        InFileDominatrix infile) {

        if (commando.startsWith("query:")) {
            query = ReadAux.Get_Query(commando, infile);
	    orig_query = query;
            return true; }
        if (commando.startsWith("begin_remark:")){
            ReadAux.Get_Remark(commando, infile);
            return true; }
        if (commando.startsWith("node:")) {
            node = ReadAux.Get_String(commando, infile);
            return true; }
        if (commando.startsWith("define:" )) {
	    def_name = ReadAux.Get_String(commando, infile);
	    use_def_file = true;
	    Definite.MakeDefVector(def_name);
	    return true; }
	if (commando.startsWith("local_frames:")) {
	    do_frames = true;
	    query = ReadAux.Get_Query(commando, infile);
	    orig_query = query;
	    return true; }
	if (commando.startsWith("reconstruct:")) {
	    recon_str = ReadAux.Get_String(commando, infile);
	    reconstruct = true;
	    recon_arg = new ArgList(recon_str);
	    return true; }
	if (commando.startsWith("coding_query:")) {
	    coding_query = ReadAux.Get_Query(commando, infile);
	    orig_coding_query = coding_query;
	    do_coding = true;
	    return true; }
        return false; }

    /*
      LexStuff
    */
    public static boolean LexStuff(String commando, 
				   InFileDominatrix infile) {
	if (commando.startsWith("make_lexicon:")) {
            make_lexicon = ReadAux.Get_boolean(commando, infile);
            return true; }
	if (commando.startsWith("make_label_lexicon:")) {
	    make_label_lexicon = ReadAux.Get_boolean(commando, infile);
	    return true; }
	if (commando.startsWith("pos_labels:")) {
	    LocalLoop.SetPosLabels(ReadAux.Get_String(commando, infile)); 
	    return true; }
	if (commando.startsWith("text_labels:")) {
            LocalLoop.SetTextLabels(ReadAux.Get_String(commando, infile)); 
	    return true; }
	if (commando.startsWith("make_tag_list:")) {
	    ignore = "";
	    make_tag_list = ReadAux.Get_boolean(commando, infile); 
	    return true; }
	return false; }

    /*
      CorpusSpecs --  handles commands giving specs of the
      input corpus.
      input -- String commando -- command found in query file.
      output -- boolean -- true if command was found, 
      false otherwise.
      side-effect -- information from the command file is stored in
      class CommandInfo.
    */
    public static boolean CorpusSpecs (String commando,
                                       InFileDominatrix infile) {

        if (commando.startsWith("corpus_file_extension:")) {
            ///FileInfo.corpus_file_extension =
            //	ReadAux.Get_String(commando, infile);
            return true; }
	//if (commando.startsWith("input_format:")) {
	//  IoInfo.setInputFormat(ReadAux.Get_String(commando, infile));
	//  return true; }
       if (commando.startsWith("corpus_comment_begin:")) {
            begin = ReadAux.Get_String(commando, infile);
            return true; }
        if (commando.startsWith("corpus_comment_end:")) {
            end = ReadAux.Get_String(commando, infile);
	    (IoInfo.commie).addCommPair(begin, end);
	    return true; }
        if (commando.startsWith("corpus_line_comment:")) {
            begin = ReadAux.Get_String(commando, infile);
	    (IoInfo.linie).addLineComm(begin);
            return true; }
	if (commando.startsWith("corpus_encoding:")) {
	    char_encoding = ReadAux.Get_String(commando, infile);
	    IoInfo.setCorpusEncoding(char_encoding); 
	    return true; }
	if (commando.startsWith("errors_to_output:")) {
	    IoInfo.setErrorsToOutput(ReadAux.Get_boolean(commando, infile));
	    return true; }
        return false; }

    /*
      PrintSpecs --  handles commands related to printing output.
      input -- String commando -- command found in query file.
      output -- boolean -- true if command was found, 
      false otherwise.
      side-effect -- information from the command file is stored in
      class CommandInfo.
    */
    public static boolean PrintSpecs (String commando,
                                      InFileDominatrix infile) {

	if (commando.startsWith("append_to_CODING:")) {
	    append_to_CODING = "-" + ReadAux.Get_String(commando, infile);
	    return true; }
        if (commando.startsWith("set_margin:")) {
            Parameters.margin = ReadAux.Get_int(commando, infile);
            return true; }
        if (commando.startsWith("nodes_only:")) {
            Parameters.nodes_only = ReadAux.Get_boolean(commando, infile);
            if (!Parameters.nodes_only) {
                Parameters.remove_nodes = false; }
            return true; }
        if (commando.startsWith("remove_nodes:")) {
            Parameters.remove_nodes =
                ReadAux.Get_boolean(commando, infile);
            return true; }
        if (commando.startsWith("print_indices:")) {
            Parameters.print_indices =
                ReadAux.Get_boolean(commando, infile);
            return true; }
	if (commando.startsWith("print_complement:")) {
	    print_complement = ReadAux.Get_boolean(commando, infile);
	    return true; }
	if (commando.startsWith("copy_corpus:")) {
	    copy_corpus = ReadAux.Get_boolean(commando, infile);
	    Parameters.print_indices = false;
	    return true; }
	if (commando.startsWith("reformat_corpus:")) {
	    reformat_corpus = ReadAux.Get_boolean(commando, infile);
	    Parameters.print_indices = false;
	    return true; }
        if (commando.startsWith("print_only:")) {
            lonely = ReadAux.Get_String(commando, infile);
            print_only = true;
            return true; }
	if (commando.startsWith("add_IDs:")) {
	    print_only_add_IDs = ReadAux.Get_boolean(commando, infile);
	    return true; }
	if (commando.startsWith("ur_text_only:")) {
	    Parameters.ur_text_only = ReadAux.Get_boolean(commando, infile);
	    return true; }
	if (commando.startsWith("output_format:")) {
	    output_format = ReadAux.Get_String(commando, infile);
	    Parameters.setOutputFormat(output_format);
	    return true; }
	if (commando.startsWith("multi_output:")) {
	    multi_output = ReadAux.Get_boolean(commando, infile);
	    return true; }
        return false; }

    public static void setOutputFormat(String out_form) {
	output_format = out_form;
	Parameters.setOutputFormat(out_form); }

    public static boolean ReviseSpecs(String commando, 
				      InFileDominatrix infile) {
	OneTask one;
	String part, commo;
	
	if (commando.startsWith("replace_label") || 
	    commando.startsWith("append_label") ||
	    commando.startsWith("prepend_label") ||
	    commando.startsWith("post_crop_label") ||
	    commando.startsWith("pre_crop_label") ||
	    commando.startsWith("add_leaf") ||
	    commando.startsWith("move_up_node") ||
	    commando.startsWith("delete") ||
	    commando.startsWith("add_internal_node") || 
	    commando.startsWith("make_daughter") ||
	    commando.startsWith("trace") ||
	    commando.startsWith("co_index") ||
	    commando.startsWith("move_to") ||
	    commando.startsWith("concat") ||
	    commando.startsWith("extend_span")) {
  	    revise = true;
	    one = new OneTask(commando + infile.RestOfLine());
	    tasker.addTask(one);
	    return true; }
	return false; }

    /*
      Basics --  handles basic commands.
      input -- String commando -- command found in query file.
      output -- boolean -- true if command was found, 
      false otherwise.
      side-effect -- information from the command file is stored in
      class CommandInfo.
    */
    public static boolean Basics (String commando, InFileDominatrix infile) {

        if (commando.startsWith("ignore_nodes:")) {
            ignore = ReadAux.Get_String(commando, infile);
            if (ignore.equals("null")) {
                ignore = ""; }
            return true; }
        if (commando.startsWith("add_to_ignore:")) {
            ignore += "|" + ReadAux.Get_String(commando, infile);
            return true; }
        if (commando.startsWith("ignore_words:")) {
            word_ignore = ReadAux.Get_String(commando, infile);
            if (word_ignore.equals("null")) {
                word_ignore = ""; }
            return true; }
        if (commando.startsWith("add_to_ignore_words:")) {
            word_ignore += "|" + ReadAux.Get_String(commando, infile);
            return true; }
        return false; }

    public static boolean DrawStuff (String commando, 
				     InFileDominatrix infile) {
      if (commando.startsWith("show_only:")) {
            show_str = ReadAux.Get_String(commando, infile);
	    show_only = true;
            return true; }
      return false; }

    /*
      CommandNotFoundError -- deals with command not found.
      input -- String commando -- command found in query file,
      not found in list of basic commands.
      output -- void.
      side-effect -- search is aborted with message.
    */
    public static void CommandNotFoundError(String commando,
                                            InFileDominatrix infile) {

        try {
            BufferedReader in =
                new BufferedReader(new InputStreamReader(System.in));
            String response = "";
            System.err.println("ERROR! command not recognized:  " + commando);
	    // System.err.print("Would you like to see a list of legitimate ");
            //System.err.println("commands (y/n)?");
            //System.out.flush();
            //response = in.readLine();
            //if (response.equals("Y") || response.equals("y")) {
	    //  ReadAux.PrintList(); }
            System.err.print("Search aborted because of error ");
            System.err.println("in command file.");
            System.err.println("");
        } // end try
        catch (Exception e) {
            System.err.println("In BasicCommands.CommandNotFoundError:  ");
            System.err.println(e.getMessage()); }
        finally {
            Goodbye.SearchExit();
            return; } }

    public static void CodingQueryError(String commando, 
					InFileDominatrix infile) {
	Character charlie = new Character('Q');

	for (int i = 0; i < commando.length() - 1; i++) {
	    if (!(charlie.isDigit(commando.charAt(i)))) {
		return; }}
	System.err.print("ERROR!  Coding queries ");
	System.err.println("must now begin with this line:");
	System.err.println("    coding_query:    ");
	System.err.println("Search aborted.");
	Goodbye.SearchExit(); }

} 


