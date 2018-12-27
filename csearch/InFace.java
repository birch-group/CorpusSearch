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

public class InFace extends CorpusSearch {

    public static BufferedReader in =
        new BufferedReader(new InputStreamReader(System.in));
    public static String response;
    public static String prompt;

    /**
     * prints version number and bookmark to CorpusSearch Manual.
    */
    public static void PrintBookmark () {

	System.out.println("");
	System.out.println(Vitals.c_note);
	System.out.println("");
	System.out.println("CorpusSearch version " + Vitals.version_number);
        System.out.println("");
        System.out.print("CorpusSearch Manual:  ");
	System.out.println(Vitals.manual_address);
	System.out.println("");
        return; }

    public static String PromptUser(String prompt) {

        try {
            System.out.print(prompt);
            System.out.flush();
            response = in.readLine();
            MightQuit(response);
            if (response.length() == 0) {
                PromptUser(prompt); } }
        catch (Exception e) {
            System.out.println("In CorpusSearch:  PromptUser:   ");
            System.out.println(e.getMessage());
            Goodbye.SearchExit(); }
        finally { return (response); } }

    public static void GetQueryFile() {
        try {
            prompt = "Enter name of query file (q to quit, ? for help):  ";
            response = PromptUser(prompt);
            if (!response.endsWith(".q") && !response.endsWith(".c")) {
                System.out.print("ERROR!  Name of query file ");
                System.out.println("must end with \".q\" or \".c\".  ");
                GetQueryFile(); }
            command_name = response;
            command_file = new File(command_name);
            Check.QueryFile(command_file); }
        catch (Exception e) {
            System.out.println("In CorpusSearch.GetQueryFile:  " );
            System.out.println(e.getMessage());
            Goodbye.SearchExit(); }
        finally { return; } }

    public static void GetSourceFiles () {
        String null_str = "null";
        Vector files = new Vector();
        int i, last_dex = 0, space_dex = 0;

        try {
            prompt = "Enter name(s) of source file(s), q to quit:  ";
            response = PromptUser(prompt);
            // build Vector files, containing names of files found in response.
            space_count:  while (space_dex != -1) {
                space_dex = response.indexOf(' ', last_dex);
                if (space_dex == -1) {
                    files.addElement(response.substring(last_dex));
                    break space_count; }
                files.addElement(response.substring(last_dex, space_dex));
                last_dex = space_dex + 1; }
            new_args = new String[files.size() + 1];
            new_args[0] = command_name;
            for (i = 0; i < files.size(); i++) {
                new_args[i + 1] = (String)files.elementAt(i); }
        } 
        catch (Exception e) {
            System.out.println("In CorpusSearch.GetSourceFiles:  " );
            System.out.println(e.getMessage());
            Goodbye.SearchExit(); }
        finally { return; } }

    public static void GetOutFile () {
        String auto_name;
        try {
            auto_name = FilePrep.AutoOutput(command_name);
            prompt = "Would you like to name your output file ";
            prompt += auto_name + " (Y/N)?  ";
            response = PromptUser(prompt);
            if (response.startsWith("Y") || response.startsWith("y")) {
                dest_name = auto_name;
                destination_file = new File(dest_name);
                OutError();
                destination = new FileWriter(dest_name);
                return; }
            NewOutFile();
        } 
        catch (Exception e) {
            System.out.println("In CorpusSearch.GetOutFile:  " );
            System.out.println(e.getMessage());
            Goodbye.SearchExit(); }
        finally { return; } }

    public static void NewOutFile() {

            prompt = "Enter name of output file, or q to quit:  ";
            response = PromptUser(prompt);
            dest_name = response;
            destination_file = new File(dest_name);
            OutError();
            return; }

    public static void OutError () {
        try {
            // if destination file exists, get permission to overwrite, or
            // new name of output file.
            if (destination_file.exists()) {
                prompt = "File " + dest_name + " already exists.  ";
                prompt += "Overwrite?  (Y/N):  ";
                response = PromptUser(prompt);
                if (!response.equals("Y") && !response.equals("y")) {
                    NewOutFile(); } }
            // ensure that name of output file ends with ".out" or ".cod".
            if (!(dest_name.endsWith(".out")) && 
		!(dest_name.endsWith(".cod"))) {
                System.out.print("ERROR! name of output file (");
                System.out.print(dest_name +  ") must end in \".out\"");
		System.out.println(" or \".cod.\"");
                NewOutFile(); }
        } 
        catch (Exception e) {
            System.out.println("in OutError:  ");
            System.out.println(e.getMessage()); }
        finally{ return; } }

    public static void MightQuit (String response) {
        if (response.equals("Q") || response.equals("q")
                || response.equals("quit")
                || response.equals("Quit") || response.equals("QUIT")) {
            System.out.println("Search aborted at user request.");
            Goodbye.SearchExit(); }
        return; }

} 




