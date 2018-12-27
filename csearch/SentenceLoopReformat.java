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
  this is "sentenceLoop.java", when reformat_corpus is true.
*/
package csearch;

import java.io.*;
import java.util.*;

import print.*;
import syntree.*;
import stats.*;
import search.*;
import search_result.*;
import CSParse.*;
import io.*;
import basicinfo.*;

public class SentenceLoopReformat extends Meat {

    public SynTree sparse;
    public ChangeTree changeable;
    private String dest_name = "BULLWINKLE";

    public SentenceLoopReformat() {
    }

    public String getDestName() {
	return dest_name; }

    /*
      thruFile -- loops through an input file, finding all parsed 
      sentences and searching them. 
      input -- void.
      output -- void.
    */
    public void thruFile (String file_name) {
        SentenceResult OneResult;
	TBToRix mytrees = new TBToRix();

        try {
	    dest_name = file_name + ".fmt";
	    destination = new FileWriter(dest_name);
	    out_dom = new OutFileDominatrix(dest_name);
	    OutStuff = out_dom.getPrintWriter();
	    mytrees = new TBToRix(file_name, OutStuff);
	    sent_loop:  do {
		sparse = mytrees.OneSentence();
		//sparse.PrintToSystemErr();
                if (sparse.isEmpty()) {
		    break sent_loop; }
		changeable = new ChangeTree(sparse);
		changeable.orderIndices();
		PrintTree.Sentence(sparse, OutStuff);
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




