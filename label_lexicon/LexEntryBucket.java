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
 */
package label_lexicon;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class LexEntryBucket {

    private Vector lexenter;

    public LexEntryBucket() {
	lexenter = new Vector(); }

    public int size() {
	return lexenter.size(); }

    public boolean isEmpty() {
	return (lexenter.isEmpty()); }

    public LexEntry LexEntryAt(int i) {
	return((LexEntry)lexenter.elementAt(i)); }

    public void AddLexEntry (LexEntry lexx) {
	lexenter.addElement(lexx); }

    public void SetLexEntry (LexEntry lexx, int i) {
	lexenter.setElementAt(lexx, i) ; }

    public void Sort() {
	QuickSortBucket(0, this.size() - 1); }

    /*
      SplitBucket -- called by QuickSortPOS.  Splits list into
      two parts, those less than List[pivot_loc] on the
      left, those greater on the right.  Sorts by POS;
      that is, for each word, sorts by its most used label.
      This will result in lex_vec having nouns together,
      prepositions together, etc.
      input -- lower -- lower index
      -- upper -- upper index
      -- pivot_loc -- location of pivot
      output -- int -- pivot_loc  -- new location of pivot
    */
    private int SplitBucket (int lower, int upper, int pivot_loc) {
	LexEntry pivot, one_entry;
	int i;

	try {
	    pivot = this.LexEntryAt(pivot_loc);
	    Swap(lower, pivot_loc);
	    pivot_loc = lower;
	    for (i = lower + 1; i <= upper; i++) {
		one_entry = this.LexEntryAt(i);
		if (pivot.MoreLaterEntry(one_entry)) {
		    pivot_loc += 1;
		    Swap(pivot_loc, i); }
	    } // end for (i = lower + 1; i <= upper; i++)
	    Swap(lower, pivot_loc);
	} // end try
	catch (Exception e) {
	    System.err.println("in LexEntryBucket.SplitBucket:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	    //CorpusSearch.SearchExit();
	} // end catch
	finally { return (pivot_loc); } 
    } // end method Split POS

    private void Swap(int i, int j) {
	LexEntry temp;
	temp = this.LexEntryAt(i);
	this.SetLexEntry(this.LexEntryAt(j), i);
	this.SetLexEntry(temp, j);
	return;
    }

    /*
      QuickSortBucket -- quick sorts bucket.
      input -- lower -- lower index
      -- upper -- upper index
      output -- void.
    */
    private void QuickSortBucket (int lower, int upper) {
	Random chance = new Random();
	int pivot_loc, index, random_dex;

	if (upper > lower) {
	    random_dex = chance.nextInt(upper - lower); 
	    index = lower + random_dex;
	    pivot_loc = SplitBucket(lower, upper, index);
	    QuickSortBucket(lower, pivot_loc - 1);
	    QuickSortBucket(pivot_loc + 1, upper);
	} // end if (upper > lower)
	return;
    } // end method QuickSortPOS 

    public void PrintToSystemErr() {
	LexEntry lexx;
	System.err.println("LexEntryBucket:  ");
	for (int i = 0; i < lexenter.size(); i++) {
	    lexx = LexEntryAt(i);
	    lexx.PrintToSystemErr(); }
	return; }
						   
} 
