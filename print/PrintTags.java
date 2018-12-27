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

package print;

import java.io.*;
import java.util.*;
import tag_list.*;

public class PrintTags {


    public static void UberList(UberList ulist, PrintWriter OutStuff) {
	UberSyn(ulist.getSynList(), ulist.getSynDivideStr(), OutStuff); 
	UberPOS(ulist.getPOSList(), ulist.getPOSDivideStr(), OutStuff); 
	UberTrace(ulist.getEmptyList(), ulist.getTraceDivideStr(), OutStuff); }

     /*
      UberList -- prints out contents of UberList object.
    */
    public static void UberSyn (UberSynList usyn, String divide,
				PrintWriter OutStuff) {
	int i;
	LexEntryBucket lexbucket;
	int curr_char;
	
	try {
	    OutStuff.println("~SYNTACTIC TAGS~");
	    OutStuff.println("");
	    OutStuff.println("~SYN_DIVIDERS: " + divide); 
	    OutStuff.println("");
	    for (i = 1; i < usyn.size(); i++) {
		lexbucket = usyn.bucketAt(i);
		curr_char = i + 64;
		OutStuff.println("/*  ~" + (char)curr_char + "~  */");
		PrintLexBucket(lexbucket, OutStuff);
	    } 
	    OutStuff.println("/*  ~OTHER~  */");
	    lexbucket = usyn.bucketAt(0);
	    PrintLexBucket(lexbucket, OutStuff);
	} 
	catch (Exception e) {
	    System.err.println("in PrintTags.UberSynList:  ");
	    e.printStackTrace(); }
	finally {
	    OutStuff.println("");
	    return; }
    } 

    public static void UberPOS (UberPOSList upos, String divide, 
				PrintWriter OutStuff) {
	int i;
	LexEntryBucket lexbucket;
	int curr_char;
	
	try {
	    OutStuff.println("~POS TAGS~");
	    OutStuff.println("");
	    OutStuff.println("~POS_DIVIDERS:  " + divide);
	    OutStuff.println("");
	    for (i = 1; i < upos.size(); i++) {
		lexbucket = upos.bucketAt(i);
		curr_char = i + 64;
		OutStuff.println("/*  ~" + (char)curr_char + "~  */");
		PrintLexBucket(lexbucket, OutStuff);
	    } 
	    OutStuff.println("/*  ~OTHER~  */");
	    lexbucket = upos.bucketAt(0);
	    PrintLexBucket(lexbucket, OutStuff);
	} 
	catch (Exception e) {
	    System.err.println("in PrintLex.UberPOS:  ");
	    e.printStackTrace(); }
	finally {
	    OutStuff.println("");
	    return; }
    } 

    public static void UberTrace (UberEmptiesList uempty, String divide, 
				PrintWriter OutStuff) {
	int i;
	LexEntry lexx;
	String canon;
	
	//	uempty.PrintToSystemErr();
	try {
	    OutStuff.println("~EMPTY CATEGORY TAGS~");
	    OutStuff.println("");
	    OutStuff.println("~EMPTY_CAT_DIVIDERS:  " + divide);
	    OutStuff.println("");
	    for (i = 0; i < uempty.size(); i++) {
		lexx = uempty.lexEntryAt(i);
		canon = lexx.getCanonical();
		OutStuff.print(canon);
		if (i != uempty.size() - 1) {
		    OutStuff.print("|"); }
	    } 
	    OutStuff.println("");
	} 
	catch (Exception e) {
	    System.err.println("in PrintLex.UberTrace:  ");
	    e.printStackTrace(); }
	finally {
	    OutStuff.println("");
	    return; }
    } 


    private static void PrintLexBucket(LexEntryBucket lexbucket, 
				PrintWriter OutStuff) {
	int i, j, k, total;
        LexEntry lexx;
        String canon, one_tag;
	Vector dash_tags_list;
	Vector dash_tags;

	for (j = 0; j < lexbucket.size(); j++) {
	    lexx = lexbucket.LexEntryAt(j);
	    canon = lexx.getCanonical();
	    OutStuff.print(canon);
	    if (!lexx.hasTags()) {
		OutStuff.println("");
		continue; }
	    dash_tags_list = lexx.getDashTagsList();
	    for (i = 0; i < dash_tags_list.size(); i++) {
		OutStuff.print(":");
		dash_tags = (Vector)dash_tags_list.elementAt(i);
		for (k = 0; k < dash_tags.size(); k++) {
		    one_tag = (String)dash_tags.elementAt(k);
		    OutStuff.print(one_tag);
		    if (k != dash_tags.size() - 1) {
			OutStuff.print("|"); } } } 
	    OutStuff.println("");
	}
    } 

} 
