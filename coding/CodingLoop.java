/*
  Beth Randall: Jan. 2001 
  CodingLoop.java
*/
package coding;

import java.io.*;
import java.util.*;

import syntree.*;
import io.*;
import print.*;
import stats.*;
import basicinfo.*;
import CodingParse.*;
import search.*;
import command.*;

public class CodingLoop {

    public static SynTree sparse;
    public static StatsPerFile file_info;
    public static Vector warned;
    private static String my_CODING = "CODING";

    /*
      thruFile -- loops through an input file, finding all parsed 
      sentences and searching them. 
      input -- void.
      output -- void.
    */
    public static void thruFile (String file_name, PrintWriter OutStuff,
				 StatsPerSearch search_info, 
				 String append_to_CODING) {
	TBToRix mytrees;
	CodingResult per_sentence;
	ListCodes clist;
	CodeObj cobj;
	ChangeTree changeable;
	Node bounder, daughter;
	int boundex, bound_up;
	boolean did_it;

	//my_CODING += append_to_CODING;
	file_info = new StatsPerFile(file_name);
	mytrees = new TBToRix(file_name, OutStuff);
	warned = new Vector();
	try {
            sent_loop:  do {
                sparse = mytrees.OneSentence();
                //sparse.PrintToSystemErr();
                if (sparse.isEmpty()) { break sent_loop; }
		file_info.totalAdd1();
		per_sentence = ParseCodingQuery.evaluable(sparse);
		if (CommandInfo.reconstruct) {
		    SynTree sparse_copy = sparse.synCopy();
		    ChangeTree rechangeable = new ChangeTree(sparse_copy);
		    did_it = rechangeable.reconstruct(CommandInfo.recon_arg); 
		    per_sentence = ParseCodingQuery.evaluable(rechangeable);
		    //per_sentence.PrintToSystemErr();
		    per_sentence.downDateBounds(rechangeable, sparse);
		}

		clist = BuildCodingNodes.oneTree(sparse, per_sentence);
		//clist.PrintToSystemErr();
		changeable = new ChangeTree(sparse);
		clist_loop:  for (int j = 0; j < clist.size(); j++) {
		    cobj = clist.getCodeObj(j);
		    bounder = cobj.getBound();
		    my_CODING = "CODING-" + bounder.getLabel();
		    boundex = bounder.getIndex_int();
		    bound_up = changeable.getUpdate(boundex);
		    daughter = sparse.FirstDaughter(bound_up);
		    if ((daughter.getLabel()).startsWith("CODING")) {
	changeable.ChangeLabel(cobj.getString(), 
					       daughter.getIndex_int() + 1,
					       true); }
		    else {
			changeable.InsertLeafBefore(my_CODING, 
						    cobj.getString(), 
						    boundex + 1, true); }
		}
		file_info.tokensAdd1();
		file_info.hitsAdd(clist.size());
		PrintOut.UrTextStuff(VectorAux.ur_vec, sparse, OutStuff);
		PrintTree.Sentence(sparse, OutStuff);
	    } while (!sparse.isEmpty());
	} // end try
	catch (Exception e) {
	    System.err.println("in CodingLoop.thruFile:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	    mytrees.CorpusError();
	    Goodbye.SearchExit(); }
	finally {
	    search_info.file_statsAdd(file_info);
	    PrintOut.Footer(file_info, OutStuff);
	    return; }
    } 

} 

