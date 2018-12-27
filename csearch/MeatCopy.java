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
	Meat.java when copy_corpus is true.
*/
package csearch;

import java.io.*;
import java.util.*;
import command.*;
import print.*;
import CSParse.*;
import basicinfo.*;

public class MeatCopy extends Meat{

    /*
      CrankThrough -- cranks through input file.
    */
    public static void CrankThrough () {
        int i;
	String first = "ALPHA", last = "OMEGA";
	SentenceLoopCopy slick = new SentenceLoopCopy();

        try {
            ReadAux.CheckNodeBoundary();
	    ParseQuery.makeQueryTree(new StringReader(CommandInfo.query));
	    ReviseStuff();
	    // Open output files, and print prefaces.

            System.err.println("Copying.  Please be patient.");

            // work through list of source files.

	    arg_loop:  for (i = 0; i < source_list.size(); i++) {
                file_name = (String)source_list.elementAt(i);
                //	    if (!file_name.endsWith(corpus_file_extension)) {
                //searching_output = true;
                ///}
                if (file_name.equals("-out"))
                    break arg_loop;
		if (file_name.endsWith(".out")) {
		    searching_output = true;
		}
                source_file = new File(file_name);
                MeatAux.CheckSource();

                //if (searching_output) {
                ///FileInit();
                //System.err.println("called FileInit:");
                //}

                empty_source = true;

                slick.thruFile(file_name);
		if (i == 0) { first = slick.getDestName(); }
		if (i == source_list.size() - 1) { last = slick.getDestName(); }
                // check for stack errors, due to fragments in input file.

                // check for empty source file (source file with no
                // complete parsed sentences).
                //if (empty_source) {
                //MeatAux.EmptySourceError();
                //} // end if empty_source

            } // end arg_loop

            //if (searching_output || source_list.size() > 1) {
            //    file_vec = MeatAux.Consolidate (file_vec);
            //} // end if searching_output && input_list.size() 1
        } // end try
        catch (Exception e) {
            System.err.println("ERROR!  In Meat.CrankThrough:  ");
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




