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
import basicinfo.*;
import command.*;

public class FilePrep extends CorpusSearch {

    public static StrCommand string_command = new StrCommand();
    public static boolean is_string_command = false;

    /** looks for preference file.
     */
    public static void GetPrefs() {
        File prefs_dir, user_dir;

	try {
	if (!CommandInfo.has_command_file) { return; }
        prefs_dir = Check.parent(command_file);
	//user_dir = new File(System.getProperty("user.dir"));
	if (perDir(prefs_dir)) { return; }}
	catch (Exception e) {
	    e.printStackTrace(); }
	finally { return; }}

    /** looks for preference file.
     */
    public static void seekPrefs(String file_name) {
        File first_source, source_dir;

	try {
	first_source = new File(file_name);
        source_dir = Check.parent(first_source);
	//user_dir = new File(System.getProperty("user.dir"));
	if (perDir(source_dir)) { 
	    ReadIn.CommandFile(CommandInfo.prefs_name);
	    ReadIn.AfterCommands(CommandInfo.prefs_name);
	    return; } }
	catch (Exception e) {
	    e.printStackTrace(); }
	finally { return; }}


    public static boolean perDir(File one_dir) {
        String[] dir_list;
        int j;
        String one_found = "";

        dir_list = one_dir.list();
	//System.out.println("one_dir:  " + one_dir.getName());
	//System.out.println(" dir_list.size():  " + dir_list.length);
        for (j = 0; j < dir_list.length; j++) {
            one_found = dir_list[j];
            if (one_found.endsWith(".prf")) {
		CommandInfo.use_prefs = true;
		CommandInfo.prefs_name = one_dir.toString() + "/" + one_found;
                return true; } }
	return false;
    }

    /*
      UnloadCommandLine -- if information was found on command line,
      unloads information into appropriate variables, 
      namely command_name, source_command, dest_name.
      input -- String args[0] -- arguments found on command line.
      output -- void.
    */
    public static void UnloadCommandLine (String args[]) {
        try {
            command_name = args[0];
	    if (string_command.initStrCommand(command_name)) {
		is_string_command = true;
		if (string_command.isFILEQ()) {
		    command_name = "fileq.q";
		    OutFileStuff(args); }
		return; }
            if (!(command_name.endsWith(".q")) &&
                !(command_name.endsWith(".c"))) {
                System.out.print("ERROR!  Name of command file ");
                System.out.println("must end with \".q\" or \".c\".");
                InFace.GetQueryFile(); }
            command_file = new File(command_name);
            Check.QueryFile(command_file);
	    OutFileStuff(args);
	}
        catch (Exception e) {
            System.out.println("In CorpusSearch.UnloadCommandLine:  ");
            System.out.println(e.getMessage());
        } 
        finally{ return; } 
    } 

    public static void OutFileStuff(String args[]) {

	if (args[args.length - 2].equals("-out")) {
	    // user has provided name of output file.
            dest_name = args[args.length - 1]; }
            // else, make automatically named output file.
        else { dest_name = AutoOutput(command_name); }
	return; }

    public static void MakeDestinationFile() {

	if (CommandInfo.output_format.equals("HTML") ||
	    CommandInfo.output_format.equals("STDOUT")) {
	    return; }
	try {
	    destination_file = new File(dest_name);
	    InFace.OutError();
	    destination = new FileWriter(dest_name); }
	catch (Exception e) {
	    System.out.println("in FilePrep.MakeDestinationFile:  ");
	    e.printStackTrace(); } 
	finally { return; }
    }  

    /*
      AutoOutput -- makes automatic name of output file.
      if query file is "koala.q", output file is "koala.out".
      input -- String query_name -- name of query file.
      output -- String dest_name -- name of output file.
    */
    public static String AutoOutput (String query_name) {
        StringBuffer dest_name_buff;
        String destiny = "";

        dest_name_buff = new StringBuffer(query_name);
        // remove ".q" from dest_name_buff.
        dest_name_buff.setLength(dest_name_buff.length() - 2);
        dest_name_buff.append(".out");
        destiny = dest_name_buff.toString();
        return destiny;
    } 

    /*
      FindFiles -- finds files that may have been described
      on the command line with "*".  Builds vector
      FileInfo.source_list.
      input -- String[] args -- arguments found on command line.
      output -- void.
      side-effect -- FileInfo.source_list contains names of
      appropriate input files.
    */
    public static void FindFiles (String[] args, String command_name) {
        File in_file;
        File in_dir;
        String[] dir_list;
        int i = -1, j = -1;
        String one_name = "", path_name = "", one_found = "";
        int last_dex;

        try {
            in_file = new File(command_name);
	    arg_loop:  for (i = 1; i < args.length; i++) {
                one_name = args[i];
                if (one_name.equals("-out")) {
                    break arg_loop; }
                if ((one_name.indexOf('*') > -1) ||
		    (one_name.indexOf('#') > -1)) {
                    if (one_name.indexOf(in_file.separatorChar) > -1) {
                        last_dex =
                            one_name.lastIndexOf(in_file.separatorChar);
                        path_name = one_name.substring(0, last_dex + 1);
                        one_name = one_name.substring(last_dex + 1);
                        in_dir = new File(path_name); }
                    else {
                        in_dir =
                            new File(System.getProperty("user.dir")); }
                    dir_list = in_dir.list();
                    for (j = 0; j < dir_list.length; j++) {
                        one_found = dir_list[j];
                        if (Matcher.StarMatch(one_name, one_found)) {
                            source_list.
				addElement(path_name + one_found); } }
                } 
                else { source_list.addElement(one_name); }
            } 
            if (source_list.isEmpty()) {
                System.out.println("");
                System.out.print("ERROR!  no source files ");
		System.out.println("were found here:  ");
                for (i = 1; i < args.length; i++) {
                    System.out.println(args[i]); }
                System.out.println("");
                InFace.GetSourceFiles();
                FindFiles(new_args, command_name);
            } 
        } // end try
        catch (Exception e) {
            if (source_list.isEmpty()) {
                System.out.println("");
                System.out.println("ERROR!  no source files ");
		System.out.println("were found here:  ");
                for (i = 1; i < args.length; i++) {
                    System.out.println(args[i]); }
                System.out.println("");
                InFace.GetSourceFiles();
                FindFiles(new_args, command_name); }
            else {
                System.out.println("in CorpusSearch.FindFiles:  ");
                System.out.println("source_list:  " + source_list);
                System.out.println(e.getMessage());
                Goodbye.SearchExit(); } }
        finally { return; } 
    } 

} 





