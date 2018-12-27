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
  SameKernelBucket -- class for list of SingleFrame objects.
  a list of SingleFrame objects, all with the same SynKernel.
*/
package frames;

import java.util.*;
import syntree.*;
import command.*;

public class SameKernelBucket {

    private Vector list_o_frames;

    public SameKernelBucket() {
	this.list_o_frames = new Vector(); }

    public SameKernelBucket(SingleFrame framed) {
	this.list_o_frames = new Vector();
	this.addSingleFrame(framed); }

    public void addSingleFrame(SingleFrame framed) {
	list_o_frames.addElement(framed); }

    public int size() {
	return list_o_frames.size(); }

    public Vector getList() {
	return list_o_frames; }

    public Kernel getSynKernel() {
	SingleFrame framed = this.getFrame(0);
	return (framed.getSynKernel()); }

    public SingleFrame getFrame(int dex) {
	return((SingleFrame)list_o_frames.elementAt(dex)); }

    public void PrintToSystemErr() {
	SingleFrame framed; 
	for (int i = 0; i < list_o_frames.size(); i++) {
	    framed = (SingleFrame)list_o_frames.elementAt(i);
	    framed.PrintToSystemErr();
        }
    }

    public void Sort() {
	QuickSortLaterFrame(0, this.size() - 1);
    }

    /*
      QuickSortLaterFrame -- quick sorts frame_vec 
      within SameKernelBucket alphabetically by frame.
      input -- lower -- lower index
      -- upper -- upper index
      output -- void.
    */
    private void QuickSortLaterFrame (int lower, int upper) {
	Random chance = new Random();
	int pivot_loc, index, random_dex;

	if (upper > lower) {
	    random_dex = chance.nextInt(upper - lower); 
	    index = lower + random_dex;
	    pivot_loc = SplitLaterFrame(lower, upper, index);
	    QuickSortLaterFrame(lower, pivot_loc - 1);
	    QuickSortLaterFrame(pivot_loc + 1, upper);
	} // end if (upper > lower)
	return;
    } // end method QuickSortLaterFrame 

    /*
      SplitLaterFrame -- called by QuickSortLaterFrame.  Splits list into
      two parts, those less than List[pivot_loc] on the
      left, those greater on the right.  
      Alphabetizes by word entry.
      input -- lower -- lower index
      -- upper -- upper index
      -- pivot_loc -- location of pivot
      output -- int -- pivot_loc  -- new location of pivot
    */
    private int SplitLaterFrame(int lower, int upper, int pivot_loc) {
	SingleFrame frame1, frame2;
	int i;

	frame1 = this.getFrame(pivot_loc);
	Swap(list_o_frames, lower, pivot_loc);
	pivot_loc = lower;
	for (i = lower + 1; i <= upper; i++) {
	    frame2 = this.getFrame(i);	
	    if (frame1.LaterFrame(frame2)) {
		pivot_loc += 1;
		Swap(list_o_frames, pivot_loc, i);
	    } // end if (LaterFrame(frame1, frame2))
	} // end for (i = lower + 1; i <= upper; i++)
	Swap(list_o_frames, lower, pivot_loc);
	return (pivot_loc);
    } // end SplitLaterFrame

    /*
        Swap -- swaps the position of two sub-vectors in a host vector.
        input -- Vector host
              -- int one_dex
              -- int other_dex
        output -- void.
    */
    private void Swap (Vector host, int one_dex, int other_dex) {
        SingleFrame temp;

	try {
	    temp = (SingleFrame)host.elementAt(one_dex);
	    host.setElementAt((SingleFrame)host.elementAt(other_dex), one_dex);
	    host.setElementAt(temp, other_dex);
	} // end try
	catch (Exception e) {
	    System.err.println("in SameKernelBucket.Swap:  ");
	    System.err.println(e.getMessage());
	    //CorpusSearch.SearchExit();
	} // end catch
	finally {
	    return;
	}
    } // end method Swap

} // end class SameKernelBucket
