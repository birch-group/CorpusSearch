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
import io.*;
import basicinfo.*;
import print.*;

public class ReadIn extends CommandInfo {

    public static void PrefsAndQuery () {
	IoInfo.newComments();
	AfterCommands(); }

    public static void PrefsAndQuery (String commands_name) {
	IoInfo.newComments();
	if (use_prefs) {
	    //Parameters.has_prefs = true;
	    //Parameters.prefs_name = prefs_name;
	    CommandFile(prefs_name); }
	if (CommandInfo.has_command_file) {
	    CommandFile(commands_name); }
	AfterCommands(commands_name);
     }

    public static void CommandFile (String commands_name) {
        String commando = "";
        boolean found_command = false;
        InFileDominatrix infile;

        try {
            infile = new InFileDominatrix(commands_name);
	    read_loop: while (!infile.EOF) {
                commando = infile.NextString();
                if (infile.EOF) { break read_loop; }
                found_command = BasicCommands.Store(commando, infile);
                if (!found_command) {
                    BasicCommands.CommandNotFoundError(commando, infile); }
            }
	    infile.Close(); }
        catch (Exception e) {
            System.err.println("In ReadIn.PrefsFile:  ");
            System.err.println(e.getMessage());
            e.printStackTrace(); }
        finally { return; } 
    } 

    public static void AfterCommands () {
	Vitals.Init(node, ignore, word_ignore);
	Parameters.Init();
	return; }

    public static void AfterCommands (String commands_name) {

	CommandAux.CheckNodeBoundary();
	Vitals.Init(node, ignore, word_ignore);
	Parameters.Init();
	if (print_only) {
	    lonely_list = new ArgList(lonely); }
        if (use_def_file) {
	    if (do_coding) {
		coding_query = Definite.InstallDefs(orig_coding_query); }
	    else {
		query = Definite.InstallDefs(orig_query); } }
        return; }

} 



