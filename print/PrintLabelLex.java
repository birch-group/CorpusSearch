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
  Beth Randall: Jan 2001
  PrintLex.java -- prints out lexicon.
*/
package print;

import java.io.*;
import java.util.*;
import label_lexicon.*;

public class PrintLabelLex {

    /*
      UberList -- prints out contents of UberList object.
    */
    public static void UberList (UberList ulist, PrintWriter OutStuff) {
	int i;
	LexEntryBucket lexbucket;
	int curr_char;
	
	try {
	    for (i = 1; i < ulist.size(); i++) {
		lexbucket = ulist.bucketAt(i);
		curr_char = i + 64;
		OutStuff.println("/*  ~" + (char)curr_char + "~  */");
		PrintLexBucket(lexbucket, OutStuff);
	    } // end for (i = 1; i < ulist.size(); i++)
	    OutStuff.println("/*  ~OTHER~  */");
	    lexbucket = ulist.bucketAt(0);
	    PrintLexBucket(lexbucket, OutStuff);
	} // end try
	catch (Exception e) {
	    System.err.println("in PrintLex.UberList:  ");
	    e.printStackTrace(); }
	finally {
	    OutStuff.println("");
	    return; }
    } // end method UberList


    private static void PrintLexBucket(LexEntryBucket lexbucket, 
				PrintWriter OutStuff) {
	int j, total;
        LexEntry lexx;
        String canon;

	for (j = 0; j < lexbucket.size(); j++) {
	    lexx = lexbucket.LexEntryAt(j);
	    canon = lexx.getCanonical();
	    total = lexx.getTotal();
	    //OutStuff.print(canon + " ");
	    OutStuff.println(canon + "  [" + total + "]"); }
    } 

} 
