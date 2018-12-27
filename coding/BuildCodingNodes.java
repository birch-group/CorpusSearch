/*
  Beth Randall: Jan. 2001 
  BuildCodingNodes.java
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
import search_result.*;

public class BuildCodingNodes {

    public static StatsPerFile file_info;

    public static ListCodes oneTree(SynTree sparse, CodingResult cod_res) {
        TreeBits tb = new TreeBits(sparse);
        Vector boundaries;
        Node bounder, sub_bound, daughter;
	CodeObj cobj;
	ListCodes clist = new ListCodes();
	ColumnResult col_res;
	SubColumnResult sub_col_res;
	SentenceResult sent_res;
	SubResult sub_res;
	int i, j, k, m, col_num;

	tb.SetBounds(sparse);
        boundaries = tb.NodesForBits(sparse);
	bound_loop:  for (i = 0; i < boundaries.size(); i++) {
	    bounder = (Node)boundaries.elementAt(i);
	    if (sparse.IsLeafPOS(bounder) || sparse.IsLeafText(bounder)) {
		continue bound_loop; }
	    daughter = sparse.FirstDaughter(bounder);
	    // deal with existing coding node, if any.
	    if ((daughter.getLabel()).startsWith("CODING")) {
		daughter = sparse.FirstDaughter(daughter);
		cobj = new CodeObj(daughter.getLabel());
		cobj.setBound(bounder); }
	    else {
		cobj = new CodeObj(bounder); }
	    for (j = 0; j < cod_res.size(); j++) {
		col_res = cod_res.getColumnResult(j);
		col_num = col_res.getColNum();
		// allow for user's indices to begin at 1 rather than 0.
		col_num -= 1;
		one_column: for (k = 0; k < col_res.size(); k++) {
		    sub_col_res = col_res.getSubColumnResult(k);
		    sent_res = sub_col_res.getSentenceResult();
		    for (m = 0; m < sent_res.size(); m++) {
			sub_res = sent_res.subResultAt(m);
			sub_bound = sub_res.getBoundary();
			if (bounder.equals(sub_bound) || 
			    sub_bound.IsMETAROOT()) {
			    cobj.setColumn(sub_col_res.getLabel(), col_num);
			    break one_column; } }
		    cobj.setDefaultColumn(col_num);
		}
	    }
	    clist.addCodeObj(cobj);
	}
	return (clist);
    } 

} 

