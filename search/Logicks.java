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

package search;

import java.util.*;
import syntree.*;
import search_result.*;
import command.*;
import basicinfo.*;

/**
 * implements logical constructors AND, OR
 * @author Beth Randall	
 *
 */
public class Logicks {

    /**
     * directs V1 and V2 to AndSimple or AndSame, 
     * depending on same-instance issues.
     */
    public static SentenceResult And (SentenceResult V1, SentenceResult V2, 
				      SameList list_o_same) {
	//list_o_same.PrintToSystemErr();
	//V1.PrintToSystemErr();
	//V2.PrintToSystemErr();
	if (V1.isEmpty()) { return V1; }
	if (V2.isEmpty()) { return V2; }
	if (list_o_same.isEmpty())
	    return(AndSimple(V1, V2));
	else
	    return(AndSame(V1, V2, list_o_same)); }

    /**
     * AND with no same-instance issues.
     */
    public static SentenceResult AndSimple (SentenceResult V1, 
					    SentenceResult V2) {
	SentenceResult V1AndV2 = new SentenceResult();
	SubResult V1elt, V2elt, V1AndV2elt;
	Node bound1, bound2;
	int i, j, n, q; // loop indices

	for (i = 0; i < V1.size(); i++) {
	    for (j = 0; j < V2.size(); j++) {
		V1elt = V1.subResultAt(i);
		V2elt = V2.subResultAt(j);
		if (V1elt.sameBound(V2elt)) {
		    V1AndV2.addTwoSubs(V1elt, V2elt); } } }
	//V1AndV2.PrintToSystemErr();
	return V1AndV2; }

    /**
     * implements AND with same-instance issues. 
     */
    public static SentenceResult AndSame(SentenceResult V1, SentenceResult V2, 
					 SameList sames) {
	SentenceResult V1AndV2 = new SentenceResult();
	SubResult V1elt, V2elt, maybe;
	Coincidence coinc;
	Node sub1, sub2;
	int i, j, p;
	boolean coincide, good_sames;

	//sames.PrintToSystemErr();
	//V1.PrintToSystemErr();
	//V2.PrintToSystemErr();
	try {
	    V1_loop:  for (i = 0; i < V1.size(); i++) {
		V2_loop: for (j = 0; j < V2.size(); j++) {
		    V1elt = V1.subResultAt(i);
		    V2elt = V2.subResultAt(j);
		    maybe = new SubResult(V1elt, V2elt);
		    good_sames = false;
		    same_loop:  for (p = 0; p < sames.size(); p++) {
			coinc = sames.getCoincidence(p);
			coincide = coinc.getCoincide();
			if ((!V1elt.sameBound(V2elt)) && !coincide) {
			    continue V2_loop; }	
			if (V1elt.sameBound(V2elt) || coincide) {
			    good_sames = true;
			    sub1 = maybe.matchAt(coinc.getFirst());
			    sub2 = maybe.matchAt(coinc.getSecond());
			    if (sub1.IsNullNode() || sub2.IsNullNode()) {
				if (V1elt.sameBound(V2elt)) {
				    continue same_loop; }
				else {
				    continue V2_loop; }}
			    if ((sub1.equals(sub2)) != coincide) {
				continue V2_loop; } }
		    } // end same_loop
		    if (!good_sames) { 
			if (!V1elt.sameBound(V2elt)) { continue V2_loop; } }
		    V1AndV2.addSubResult(maybe); 
		} // end V2_loop
	    } // end V1_loop
	    //System.err.println("V1AndV2:  " + V1AndV2);
	} // end try
	catch (Exception e) {
	    System.err.println("in AndSame:  ");
	    e.printStackTrace();
	    sames.PrintToSystemErr();
	    Goodbye.SearchExit(); }
	finally {
	    //System.err.println("in AND:  result:  ");
	    //V1AndV2.PrintToSystemErr();
	    return V1AndV2; }
    } 

    /**
     * Or.
     */
    public static SentenceResult Or (SentenceResult V1, SentenceResult V2) {
	SentenceResult V1OrV2 = new SentenceResult();
	SubResult subb;
	int i;

	for (i = 0; i < V1.size(); i++) {
	    subb = V1.subResultAt(i);
	    V1OrV2.addSubResult(subb.getBoundary(), subb.getMatches(),
				subb.getMatches()); }
	for (i = 0; i < V2.size(); i++) {
	    subb = V2.subResultAt(i);
	    V1OrV2.addSubResult(subb.getBoundary(), subb.getMatches(),
				subb.getMatches()); }
	return (V1OrV2); }

    /**
     * Exclusive Or.
     */
    public static SentenceResult XOr (SentenceResult V1, SentenceResult V2) {
        SentenceResult V1OrV2 = new SentenceResult();
        SubResult subb1, subb2, subb3, or_subb;
        int i, j, k;

        // disallow AND. Remove.
        and_loop: for (i = 0; i < V1.size(); i++) {
            subb1 = V1.subResultAt(i);
            for (j = 0; j < V2.size(); j++) {
                subb2 = V2.subResultAt(j);
                if (subb1.sameBound(subb2)) {
                    V1.rmvSubResult(i);
                    V2.rmvSubResult(j);
                    i -= 1;
                    j -= 1;
		    if (i < 0 || j < 0) { break and_loop; }
		    // remove duplicate SubResults in V1.
		    for (k = i; k < V1.size(); k++) {
			subb3 = V1.subResultAt(k);
			if (subb1.sameBound(subb3)) {
			    V1.rmvSubResult(k); }
		    }
                }
            }
        }
        // whatever remains is exclusive OR.
        for (i = 0; i < V1.size(); i++) {
            subb1 = V1.subResultAt(i);
            or_subb = new SubResult(subb1.getBoundary());
            or_subb.addMatches(subb1.getMatches());
            or_subb.addNulls(2);
            V1OrV2.addSubResult(or_subb);
        }
        for (j = 0; j < V2.size(); j++) {
            subb2 = V2.subResultAt(j);
            or_subb = new SubResult(subb2.getBoundary());
	    or_subb.addNulls(2);
            or_subb.addMatches(subb2.getMatches());
            V1OrV2.addSubResult(or_subb);
        }
        return (V1OrV2);
    }


    /**
     * NOT.
     */
    public static SentenceResult Not (SynTree sparse, SentenceResult SR1, 
				      int num_nulls, History hist) {
	TreeBits bounds;
	SubResult sub;
	Node boundary, nullski = new Node("NULL");
	int i, bound_dex;
	SentenceResult nott = new SentenceResult();
	Vector not_bounds;

	//sparse.PrintToSystemErr();
	//hist.PrintToSystemErr();
	bounds = new TreeBits(sparse);
	bounds.SetBounds(sparse);
	// clear bits representing boundary nodes in SentenceResult SR1.
	for (i = 0; i < SR1.size(); i++) {
	    sub = SR1.subResultAt(i);
	    boundary = sub.getBoundary();
	    bounds.ClearNode(boundary); }
	// true values in TreeBits bounds now correspond to boundary nodes
	// not in SentenceResult SR1.
	not_bounds = bounds.NodesForBits(sparse);
	for (i = 0; i < not_bounds.size(); i++) {
	    boundary = (Node)not_bounds.elementAt(i);
	    nott = NotPerBoundary(nott, boundary, num_nulls, hist); }
	///nott.PrintToSystemErr();
	return nott;
    } 

    public static SentenceResult NotPerBoundary(SentenceResult nott, 
						Node boundary, 
						int num_nulls, History hist) {
	int start, j;
	SentenceResult oldsr1, oldsr2;
	SubResult newsubb;

	try {
	    start = hist.size() - num_nulls/2;
	    if (start < 0) { 
		return (NotBarf(nott, boundary, num_nulls)); }
	    if (start == hist.size() - 1) {
		newsubb = new SubResult(boundary, num_nulls);
		nott.addSubResult(newsubb);
		return nott; }
	    oldsr1 = hist.sentResPerBoundary(boundary, start);
	    oldsr2 = hist.sentResPerBoundary(boundary, start + 1);
	    nott = subNot(nott, oldsr1, oldsr2, boundary, num_nulls);
	    hist_loop: for (j = start + 2; j < hist.size(); j++) {
		oldsr1 = hist.sentResPerBoundary(boundary, j);
		oldsr2 = nott;
		nott = subNot(nott, oldsr2, oldsr1, boundary, num_nulls); }
	}
	catch (Exception e) {
	    System.err.println("");
	    hist.PrintToSystemErr();
	    System.err.println(""); }
	finally { return nott; }
    }

    private static SentenceResult subNot (SentenceResult nott,
					  SentenceResult sr1, 
					  SentenceResult sr2, 
					  Node boundary,
					  int num_nulls) {
	SubResult newsubb;
	SentenceResult orsr;

	if (sr1.isEmpty() && sr2.isEmpty()) {
	    newsubb = new SubResult(boundary, num_nulls); 
	    nott.addSubResult(newsubb); } 
	else {
	    orsr = Or(sr1, sr2);
	    nott.addSentenceResult(orsr); }
	nott.adjustSubSize();
	return nott; }

    private static SentenceResult NotBarf (SentenceResult nott, Node boundary, 
 					   int num_nulls) {
	SubResult subb;

	System.err.println("WARNING! NOT misbehaving.");
	subb = new SubResult(boundary, num_nulls); 
	nott.addSubResult(subb);
	return (nott); }
} 
