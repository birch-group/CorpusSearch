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

package command;

import java.io.*;
import java.util.*;

import basicinfo.*;
import revise.*; 

/**
 * stores variables holding information found in query file.
 */
public class CommandInfo {
    public static boolean use_prefs = false;
    public static String prefs_name;

    // start off variables with their default values.
    // these variables are given their final values by ReadIn.java.
    public static boolean multi_output = false;
    public static boolean do_frames = false;
    public static boolean make_lexicon = false;
    public static boolean make_label_lexicon = false;

    public static boolean has_command_file = true;
    public static String char_encoding = "ASCII";
    public static boolean copy_corpus = false;
    public static boolean reformat_corpus = false;
    public static boolean print_only = false;
    public static boolean print_complement = false;
    public static String output_format = "QINGTIAN";
    public static String recon_str = "";
    public static boolean reconstruct = false;
    public static ArgList recon_arg;
    public static String lonely = "QINGTIAN";
    public static ArgList lonely_list;
    public static boolean use_def_file = false;
    public static boolean revise = false;
    public static Vector curlies;
    public static TaskList tasker = new TaskList();

    public static boolean print_only_add_IDs = false;
    public static String append_to_CODING = "";
    public static String node = "NO_NODE_BOUNDARY_FOUND";
    public static String orig_coding_query = "NO_ORIG_CODING_QUERY_FOUND";
    public static String coding_query = "NO_CODING_QUERY_FOUND";
    public static boolean do_coding = false;
    public static String query = "NO_QUERY_FOUND";
    //public static String ignore = "CODE|LB|'|\"|,|E_S|/|RMV*|" + "\\" + "**";
    public static String ignore = "CODE|LB|'|E_S|COMMENT*|RMV*|" + "\\" + ".|" + ",|" + "\\" + "\"";
    public static String word_ignore = "CODE|0|LB|'|E_S|COMMENT*|RMV*|" + "\\" + "**|" + "\\" + ".|" + "\\" + "\"|" + "," ;
    public static String orig_query = "";
    public static String def_name = "";

    // for CorpusDraw, see drawtree package.
    public static boolean show_only = false;
    public static String show_str = "";
    public static boolean make_tag_list = false;

    public static boolean gotQuery() {
	if (query.equals("NO_QUERY_FOUND")) {
	    return false; }
	return true; }

} 




