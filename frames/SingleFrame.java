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

package frames;

import java.util.*;
import syntree.*;
import command.*;
import basicinfo.*;

public class SingleFrame extends ListFrames{

    private Kernel my_kernel;
    private Vector sis_frame;
    private Node ID_node;
    private int index; // index within SentenceFrame.
    private int total; // number of SingleFrames in SentenceFrame.

    public SingleFrame() {
	my_kernel = new Kernel();
	sis_frame = new Vector(); 
	total = 0;
	index = 0;}

    public void setIndex(int i) {
	this.index = i;}

    public int getIndex() {
	return(this.index); }

    public void setTotal(int i) {
	this.total = i;}

    public int getTotal() {
	return this.total; }

    public void addToSynKernel(Node nodal) {
	my_kernel.addNode(nodal); }

    public int size() {
	return (sis_frame.size()); }

    public boolean isEmpty() {
	return (sis_frame.isEmpty()); }

    public int kernelSize() {
	return(my_kernel.size()); }

    public int compareKernels (SameKernelBucket bucko) {
	Kernel curr_ker, buck_ker;
	curr_ker = this.getSynKernel();
	buck_ker = bucko.getSynKernel();
	return (curr_ker.compareTo(buck_ker)); }
	    
    public void addToSisFrame(Node nodal) {
	sis_frame.addElement(nodal); }

    public Kernel getSynKernel() {
	return my_kernel; }

    public Vector getSisFrame() {
	return sis_frame; }

    public void purgeSister(SynTree sparse) {
	sis_frame = PipeList.PurgeNodeList(sparse, sis_frame,
					   Vitals.ignore_list); }


    public Node sisNodeAt(int i) {
	return((Node)sis_frame.elementAt(i)); }

    public String sisLabelAt(int i) {
	Node nodal;
	nodal = sisNodeAt(i);
	return (nodal.getLabel()); }

    public void setIDNode(Node nodal) {
	this.ID_node = nodal; }

    public String getIDString() {
	String printID = "(ID " + ID_node.getLabel() + ")";
	return printID; }

    /*
      LaterFrame -- determines whether input frame
      is alphabetically later than this.
      output -- boolean -- true if this is
      alphabetically later than input frame;
      false otherwise.
    */
     public boolean LaterFrame (SingleFrame frame2) {
        int frame_length1, frame_length2, i, less_length;
        String label1, label2;

        frame_length1 = this.size();
        frame_length2 = frame2.size();
        if (frame_length1 > frame_length2) {
            less_length = frame_length2;}
        else {
            less_length = frame_length1; }
        for (i = 0; i < less_length; i++) {
            label1 = this.sisLabelAt(i);
            label2 = frame2.sisLabelAt(i);
            if (label1.compareTo(label2) > 0) {
                return true; }
            if (label1.compareTo(label2) < 0) {
                return false; }
            }
        if (frame_length1 > frame_length2) {
            return true; }
        return false;
    } // end method LaterFrame

    public void PrintToSystemErr() {
	System.err.println("my_kernel:  ");
	my_kernel.PrintToSystemErr();
	System.err.println("sis_frame:  ");
	ID_node.PrintNodeVector(sis_frame);
	System.err.println("ID:  " + ID_node.getLabel());
	System.err.println("");
    }

}
