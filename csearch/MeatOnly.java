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
	MeatCopy.java
	This class is the "meat" of CorpusSearch; it runs the search.
	Meat.java when print_only is true.
*/
package csearch;

import java.io.*;
import java.util.*;
import command.*;
import print.*;
import CSParse.*;
import basicinfo.*;

public class MeatOnly extends Meat{

    /*
      CrankThrough -- cranks through input file.
    */
    public static void CrankThrough () {
        int i;
	String first= "INNOCENCE", last="EXPERIENCE";
	SentenceLoopOnly slurry = new SentenceLoopOnly();

        try {
            System.err.print("Printing only " + CommandInfo.lonely);
	    if (CommandInfo.print_only_add_IDs) {
		System.err.print(" plus IDs");}
	    System.err.println(".  Please be patient.");
	    if (CommandInfo.lonely.equals("CODING")) {
		System.err.print("WARNING!  For the output of ");
		System.err.print("CorpusSearch 2.7 and later, ");
		System.err.println("use this query:");
		System.err.println("    print_only: CODING*"); }
	    arg_loop:  for (i = 0; i < source_list.size(); i++) {
                file_name = (String)source_list.elementAt(i);
                if (file_name.equals("-out"))
                    break arg_loop;
		if (file_name.endsWith(".out")) {
		    searching_output = true; }
                source_file = new File(file_name);
                MeatAux.CheckSource();
                slurry.thruFile(file_name, CommandInfo.lonely_list);
		if (i == 0) { first = slurry.getDestName(); }
		if (i == source_list.size() - 1) { 
		    last = slurry.getDestName(); }
            } // end arg_loop
            OutStuff.flush();
        } // end try
        catch (Exception e) {
            System.err.println("ERROR!  In MeatOnly.CrankThrough:  ");
            System.err.println("Exception:  " + e.getMessage());
            System.err.println(e.getMessage());
            System.err.println(e.toString());
            e.printStackTrace();
            Goodbye.SearchExit();
        } // end catch
        finally {
	    OutMessage(source_list.size(), first, last);
            return;
        } // end finally
    } // end method CrankThrough

    private static void OutMessage(int num_files, String first, String last) {
	if (num_files < 2) {
	    System.err.println("");
	    System.err.println("Output file is ");
	    System.err.println("    " + first);
	    return; }
	else {
	    System.err.println("");
	    System.err.println("Output files are ");
	    System.err.println("    " + first);
	    System.err.println("through ");
	    System.err.println("    " + last); }
	return;
    }

} // end class MeatCopy.java




