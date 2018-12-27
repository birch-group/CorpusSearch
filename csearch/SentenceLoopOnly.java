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
  Beth Randall:  August 2000
  SentenceLoopCopy.java
  this is "sentenceLoop.java", when print_only is true.
*/
package csearch;

import java.io.*;
import java.util.*;

import print.*;
import syntree.*;
import stats.*;
import search.*;
import io.*;
import basicinfo.*;

public class SentenceLoopOnly extends Meat {

    public SynTree sparse;
    private String dest_name = "BULLWINKLE";

    public SentenceLoopOnly() { }

    public String getDestName() {
	return dest_name; }

    /*
      thruFile -- loops through an input file, finding all parsed 
      sentences and searching them. 
      input -- void.
      output -- void.
    */
    public void thruFile (String file_name, ArgList lonely_list) {
	TBToRix mytrees = new TBToRix();
	Vector onlies;

        try {
	    dest_name = file_name + ".ooo";
	    destination = new FileWriter(dest_name);
	    out_dom = new OutFileDominatrix(dest_name);
	    OutStuff = out_dom.getPrintWriter();
	    mytrees = new TBToRix(file_name, OutStuff);
	    sent_loop:  do {
		sparse = mytrees.OneSentence();
		onlies = GetOnly.Plain(sparse, lonely_list);
		//sparse.PrintToSystemErr();
                if (sparse.isEmpty()) { break sent_loop; }
		PrintList.Vector(onlies, OutStuff);
            } while (!sparse.isEmpty()); // end sent_loop;
	    OutStuff.flush();
        } // end try
        catch (Exception e) {
            System.err.println("in SentenceLoop.thruFile:  ");
            System.err.println(e.getMessage());
            e.printStackTrace();
	    mytrees.CorpusError();
            Goodbye.SearchExit();
        } // end catch
        finally {
            return;
        } // end finally
    } // end method thruFile

} // end class SentenceLoopCopy.java




