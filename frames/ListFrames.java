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
  ListFrames -- class for list of SameKernelBucket objects.
 */
package frames;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class ListFrames {

    private Vector list_o_buckets;
    private SameKernelBucket buck, newbuck;
    private String syn_str = "NP*";
    private Vector syn_labels_list;

    public ListFrames() {
	this.list_o_buckets = new Vector();
	this.syn_labels_list = PipeList.MakeList(syn_str);
    }

    public  void addSentenceFrame (SentenceFrame per_sentence) {
	SingleFrame framed;

	for (int i = 0; i < per_sentence.size(); i++) {
	    framed = per_sentence.getSingleFrame(i);
	    framed.setTotal(per_sentence.size());
	    if (per_sentence.size() > 1) {
		framed.setIndex(i + 1); }
	    this.addSingleFrame(framed);
	}
	return;
    }

    /*
      adds SingleFrame to appropriate SameKernelBucket.
    */
    public void addSingleFrame(SingleFrame framed) {
	int compare;
        // first frame.
	if (list_o_buckets.isEmpty()) {
	    newbuck = new SameKernelBucket(framed);
	    addBucket(newbuck);
	    return; }
	bucket_loop: for (int i = 0; i < list_o_buckets.size(); i++) {
	    buck = this.getBucket(i);
	    compare = framed.compareKernels(buck);
	    if (compare > 0) {
		if (i == list_o_buckets.size() - 1) {
		    newbuck = new SameKernelBucket(framed);
		    addBucket(newbuck);
		    return;
                }
		continue bucket_loop; }
	    if (compare == 0) {
		buck.addSingleFrame(framed);
		return; }
	    if (compare < 0) {
		newbuck = new SameKernelBucket(framed);
		insertBucket(newbuck, i);
		return; }
	} // end bucket_loop;
    } // end method addSingleFrame

    public int size() {
	return list_o_buckets.size(); }

    public boolean isEmpty() {
	return (list_o_buckets.isEmpty()); }

    public Vector getSynLabelsList() {
	return(syn_labels_list); }

    public void insertBucket(SameKernelBucket bucko, int dex) {
	list_o_buckets.insertElementAt(bucko, dex);
        return; }

    public void addBucket(SameKernelBucket bucko) {
	list_o_buckets.addElement(bucko);
    }

    public SameKernelBucket getBucket(int dex) {
	return((SameKernelBucket)list_o_buckets.elementAt(dex)); }

    public void Sort() {
	SameKernelBucket buck;
	for (int i = 0; i < this.size(); i++) {
	    buck = this.getBucket(i);
	    buck.Sort();
        }
    } // end method Sort

    public void PrintToSystemErr() {
	SameKernelBucket bucko; 
	for (int i = 0; i < list_o_buckets.size(); i++) {
	    bucko = getBucket(i);
	    bucko.PrintToSystemErr();
        }
    } // end method PrintToSystemErr()

} // end class ListFrames.java
