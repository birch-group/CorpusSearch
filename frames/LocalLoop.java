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
  Beth Randall: Jan. 2001 
  LocalLoop.java
*/
package frames;

import java.io.*;
import java.util.*;

import syntree.*;
import io.*;
import print.*;
import stats.*;
import basicinfo.*;
import FramesParse.*;

public class LocalLoop {

    public static SynTree sparse;
    public static ListFrames fframes;
    public static StatsPerFile file_info;

    /*
      thruFile -- loops through an input file, finding all parsed 
      sentences and searching them. 
      input -- void.
      output -- void.
    */
    public static void thruFile (String file_name, PrintWriter OutStuff,
				 StatsPerSearch search_info) {
	TBToRix mytrees;
	SentenceFrame per_sentence;

	file_info = new StatsPerFile(file_name);
	fframes = new ListFrames();
	mytrees = new TBToRix(file_name, OutStuff);
	try {
            sent_loop:  do {
                sparse = mytrees.OneSentence();
                //sparse.PrintToSystemErr();
                if (sparse.isEmpty()) {
                    break sent_loop;
                }
		file_info.totalAdd1();
		per_sentence = ParseFrames.evaluable(sparse);
	        if (!per_sentence.isEmpty()) {
		    fframes.addSentenceFrame(per_sentence); 
		    file_info.tokensAdd1();
		    file_info.hitsAdd(per_sentence.size());
		}     
	    } while (!sparse.isEmpty());
	} // end try
	catch (Exception e) {
	    System.err.println("in frames/LocalLoop.thruFile:  ");
	    System.err.println(e.getMessage());
	    mytrees.CorpusError();
	    Goodbye.SearchExit();
	} // end catch
	finally {
	    if (fframes.isEmpty()) {
		EmptyFramesError();
	    }
	    fframes.Sort();
	    PrintFrames.List(fframes, OutStuff);
	    search_info.file_statsAdd(file_info);
	    PrintOut.Footer(file_info, OutStuff);
	    return;
	} // end finally
    } // end method thruFile

    /*
      EmptyFramesVecError -- deals with error of empty frames.
      input -- void
      output -- void.
      side-effect -- exits program.
    */
    public static void EmptyFramesError () {

	System.err.println("");
	System.err.println("WARNING! No frames were found.");
	System.err.println("");
	//CorpusSearch.SearchExit();
    } // end method EmptyFramesVecError

} // end class LocalLoop.java

