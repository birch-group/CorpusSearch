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

public class BasicStrCommands extends CommandInfo {

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
    public static boolean Store (String commando) {

        if (Fundamentals(commando)) {
            return true; }
        if (Basics(commando)) {
            return true; }
        if (CorpusSpecs(commando)) {
            return true; }
        if (PrintSpecs(commando)) {
            return true; }
        if (ReviseSpecs(commando)) {
	    return true; }
	if (LexStuff(commando)) {
	    return true; }
	if (DrawStuff(commando)) {
	    return true; }
	CodingQueryError(commando);
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
    public static boolean Fundamentals (String commando) {

	//  if (commando.startsWith("query:")) {
	//  query = ReadAuxStr.Get_Query(commando);
	//  orig_query = query;
	//  return true; }
        if (commando.startsWith("begin_remark:")){
            ReadAuxStr.Get_Remark(commando);
            return true; }
        if (commando.startsWith("node:")) {
            node = ReadAuxStr.Get_String(commando);
            return true; }
        if (commando.startsWith("define:" )) {
	    def_name = ReadAuxStr.Get_String(commando);
	    use_def_file = true;
	    Definite.MakeDefVector(def_name);
	    return true; }
	//  if (commando.startsWith("local_frames:")) {
	//  do_frames = true;
	//  query = ReadAuxStr.Get_Query(commando);
	//  orig_query = query;
	//  return true; }
	if (commando.startsWith("reconstruct:")) {
	    recon_str = ReadAuxStr.Get_String(commando);
	    reconstruct = true;
	    recon_arg = new ArgList(recon_str);
	    return true; }
	//if (commando.startsWith("coding_query:")) {
	//  coding_query = ReadAuxStr.Get_Query(commando);
	//  orig_coding_query = coding_query;
	//  do_coding = true;
	//  return true; }
        return false; }

    /*
      LexStuff
    */
    public static boolean LexStuff(String commando
				    ) {
	if (commando.startsWith("make_lexicon:")) {
            make_lexicon = ReadAuxStr.Get_boolean(commando);
            return true; }
	if (commando.startsWith("make_label_lexicon:")) {
	    make_label_lexicon = ReadAuxStr.Get_boolean(commando);
	    return true; }
	if (commando.startsWith("pos_labels:")) {
	    LocalLoop.SetPosLabels(ReadAuxStr.Get_String(commando)); 
	    return true; }
	if (commando.startsWith("text_labels:")) {
            LocalLoop.SetTextLabels(ReadAuxStr.Get_String(commando)); 
	    return true; }
	if (commando.startsWith("make_tag_list:")) {
	    ignore = "";
	    make_tag_list = ReadAuxStr.Get_boolean(commando); 
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
    public static boolean CorpusSpecs (String commando) {

        if (commando.startsWith("corpus_file_extension:")) {
            ///FileInfo.corpus_file_extension =
            //	ReadAuxStr.Get_String(commando);
            return true; }
	//if (commando.startsWith("input_format:")) {
	//  IoInfo.setInputFormat(ReadAuxStr.Get_String(commando));
	//  return true; }
       if (commando.startsWith("corpus_comment_begin:")) {
            begin = ReadAuxStr.Get_String(commando);
            return true; }
        if (commando.startsWith("corpus_comment_end:")) {
            end = ReadAuxStr.Get_String(commando);
	    (IoInfo.commie).addCommPair(begin, end);
	    return true; }
        if (commando.startsWith("corpus_line_comment:")) {
            begin = ReadAuxStr.Get_String(commando);
	    (IoInfo.linie).addLineComm(begin);
            return true; }
	if (commando.startsWith("corpus_encoding:")) {
	    char_encoding = ReadAuxStr.Get_String(commando);
	    IoInfo.setCorpusEncoding(char_encoding); 
	    return true; }
	if (commando.startsWith("errors_to_output:")) {
	    IoInfo.setErrorsToOutput(ReadAuxStr.Get_boolean(commando));
	    return true; }
        return false; }

    /*
      PrintSpecs --  handles commands related to printing output.
      input -- String commando -- command found in query file.
      output -- boolean -- true if command was found
      false otherwise.
      side-effect -- information from the command file is stored in
      class CommandInfo.
    */
    public static boolean PrintSpecs (String commando) {

        if (commando.startsWith("set_margin:")) {
            Parameters.margin = ReadAuxStr.Get_int(commando);
            return true; }
        if (commando.startsWith("nodes_only:")) {
            Parameters.nodes_only = ReadAuxStr.Get_boolean(commando);
            if (!Parameters.nodes_only) {
                Parameters.remove_nodes = false; }
            return true; }
        if (commando.startsWith("remove_nodes:")) {
            Parameters.remove_nodes =
                ReadAuxStr.Get_boolean(commando);
            return true; }
        if (commando.startsWith("print_indices:")) {
            Parameters.print_indices =
                ReadAuxStr.Get_boolean(commando);
            return true; }
	if (commando.startsWith("print_complement:")) {
	    print_complement = ReadAuxStr.Get_boolean(commando);
	    return true; }
	if (commando.startsWith("copy_corpus:")) {
	    copy_corpus = ReadAuxStr.Get_boolean(commando);
	    Parameters.print_indices = false;
	    return true; }
	if (commando.startsWith("reformat_corpus:")) {
	    reformat_corpus = ReadAuxStr.Get_boolean(commando);
	    Parameters.print_indices = false;
	    return true; }
        if (commando.startsWith("print_only:")) {
            lonely = ReadAuxStr.Get_String(commando);
            print_only = true;
            return true; }
	if (commando.startsWith("ur_text_only:")) {
	    Parameters.ur_text_only = ReadAuxStr.Get_boolean(commando);
	    return true; }
	if (commando.startsWith("output_format:")) {
	    output_format = ReadAuxStr.Get_String(commando);
	    Parameters.setOutputFormat(output_format);
	    return true; }
	if (commando.startsWith("multi_output:")) {
	    multi_output = ReadAuxStr.Get_boolean(commando);
	    return true; }
        return false; }

    public static void setOutputFormat(String out_form) {
	output_format = out_form;
	Parameters.setOutputFormat(out_form); }

    public static boolean ReviseSpecs(String commando) {
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
	    commando.startsWith("move_to")) {
  	    revise = true;
	    //one = new OneTask(commando + infile.RestOfLine());
	    //tasker.addTask(one);
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
    public static boolean Basics (String commando) {

        if (commando.startsWith("ignore_nodes:")) {
            ignore = ReadAuxStr.Get_String(commando);
            if (ignore.equals("null")) {
                ignore = ""; }
            return true; }
        if (commando.startsWith("add_to_ignore:")) {
            ignore += "|" + ReadAuxStr.Get_String(commando);
            return true; }
        if (commando.startsWith("ignore_words:")) {
            word_ignore = ReadAuxStr.Get_String(commando);
            if (word_ignore.equals("null")) {
                word_ignore = ""; }
            return true; }
        if (commando.startsWith("add_to_ignore_words:")) {
            word_ignore += "|" + ReadAuxStr.Get_String(commando);
            return true; }
        return false; }

    public static boolean DrawStuff (String commando
				      ) {
      if (commando.startsWith("show_only:")) {
            show_str = ReadAuxStr.Get_String(commando);
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
    public static void CommandNotFoundError(String commando) {

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
	    //  ReadAuxStr.PrintList(); }
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

    public static void CodingQueryError(String commando) {
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


