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
  Kernel.java -- class for single frame object.
 */
package frames;

import java.util.*;
import syntree.*;
import command.*;

public class Kernel extends ListFrames{

    private Vector syn_kernel;

    public Kernel() {
	syn_kernel = new Vector();
    }

    public void addNode(Node nodal) {
	syn_kernel.addElement(nodal); }

    public int size() {
	return(syn_kernel.size()); }

    public Vector getSynKernel() {
	return syn_kernel; }

    public Node nodeAt(int i) {
	return ((Node)syn_kernel.elementAt(i)); }

    public String labelAt(int i) {
	Node nodal;
	nodal = this.nodeAt(i);
	return (nodal.getLabel()); }

    public String allLabels() {
	Node nodal;
	String labels = "";
	for (int i = 0; i < this.size(); i++) {
	    labels += labelAt(i) + " ";
        }
	return labels;
    }

    public void PrintToSystemErr() {
	Node nodal = new Node("NULL");
	System.err.println("kernel:  ");
	nodal.PrintNodeVector(syn_kernel);
    }

    /*
      returns -1 if this < input kernel.
      returns 0 if this = input kernel.
      returns 1 if this > input kernel.
    */
    public int compareTo(Kernel ker2) {
	String this_str, ker2_str;
	int shorter;

	shorter = this.size();
	if (shorter > ker2.size()) {
	    shorter = ker2.size(); }
	for (int i = 0; i < shorter; i++) {
	    this_str = this.labelAt(i);
	    ker2_str = ker2.labelAt(i);
	    if (this_str.compareTo(ker2_str) < 0) {
		return -1; }
	    if (this_str.compareTo(ker2_str) > 0) {
		return 1; }
	}
	if (this.size() < ker2.size()) {
	    return -1; }
	if (ker2.size() < this.size()) {
	    return 1; }
	return 0;
    } // end method compareTo

} // end class Kernel
