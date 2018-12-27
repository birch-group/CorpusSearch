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

package syntree;

import java.io.*;
import java.util.*;

public class SparseMatrix {

    private Vector label_vec;
    private Vector end_vec;
    private Integer minus1 = new Integer(-1);
    //protected TreeBuckets buckt; 

    public SparseMatrix () {
	Init(); }

    //  to generate a sample matrix.  Called like this:
    //    sparse = new SynTree("EXAMPLE");
    public SparseMatrix (String Example) {
	Init();
	//        this.setSample();
        return; }

    public void setSparseMatrix(SynTree spaz) {
	label_vec = spaz.GetLabelVector();
	end_vec = spaz.GetEndVector();
	return; }

    private void Init() {
	label_vec = new Vector();
        end_vec = new Vector();
	//buckt = new TreeBuckets();
	return; }

    public Vector GetLabelVector() {
	return (this.label_vec); }

    public Vector GetEndVector() {
	return(end_vec); }

    public void setLabelVector(Vector new_label_vec) {
	this.label_vec = new_label_vec;
	return; }

    public void setEndVector(Vector new_end_vec) {
	this.end_vec = new_end_vec;
	return; }


    public boolean isEmpty() {
        return(label_vec.isEmpty()); }

    public void removeAllElements () {
        label_vec.removeAllElements();
        end_vec.removeAllElements();
	//buckt.removeAllElements();
        return; }

    public int size () {
        return(label_vec.size()); }

    public void addWrapper() {
	Integer new_end = new Integer(label_vec.size()), old_end;
	int i;

	label_vec.insertElementAt("", 0);
	end_vec.insertElementAt(new_end, 0);
	for (i = 1; i < end_vec.size(); i++) {
	    old_end = (Integer)end_vec.elementAt(i);
	    new_end = new Integer(old_end.intValue() + 1);
	    end_vec.setElementAt(new_end, i); }
	return; }
    /*
      returns e.g. [1, IP-MAT], where 1 is the start_index 
          and IP-MAT is the label.  
    */
    public Node NodeAt (Integer x) {
	    return(new Node(x, this.LabelAt(x))); }

    /*
      returns e.g. [1, IP-MAT], where 1 is the start_index 
          and IP-MAT is the label.  
    */
    public Node NodeAt (int x) {
        Integer index = new Integer(x);
        return (NodeAt(index)); }

    public void SetLabelAt(String label, int x) {
	setLabelAt(label, x); }
    
    public void setLabelAt(String label, int x) {
	while (x >= label_vec.size()) {
	    label_vec.addElement("_"); }
        label_vec.setElementAt(label, x); }

    public void SetLabelAt(String label, Integer x_Int) {
	SetLabelAt(label, x_Int.intValue()); }

    public void SetEndDexAt(Integer dex, int x) {
	while (x >= end_vec.size()) {
	    end_vec.addElement(minus1); }
        end_vec.setElementAt(dex, x); }

    public void SetEndDexAt (int dex, int x) {
	Integer dex_Int = new Integer(dex);
	SetEndDexAt(dex_Int, x); }

    public void SetEndDexAt (Integer dexInt, Integer xInt) {
	SetEndDexAt(dexInt, xInt.intValue()); }

    public String LabelAt(int x) {
	return(labelAt(x)); }

    public String labelAt (int x) {
        return ((String)label_vec.elementAt(x)); }

    public String LabelAt (Integer x) {
        return(this.LabelAt(x.intValue())); }

    public int intEndDexAt (int x) {
	return((this.EndDexAt(x)).intValue()); }

    public int intEndDexAt (Integer x) {
	return(this.intEndDexAt(x.intValue())); }

    public Integer EndDexAt (Integer x) {
        return(this.EndDexAt(x.intValue())); }

    public Integer EndDexAt (int x) {
        if (x >= end_vec.size() || x < 0) {
            return (minus1); }
        return((Integer)end_vec.elementAt(x)); }

    public void AddItem (String new_label, Integer new_end_dex) {
        label_vec.addElement(new_label);
        end_vec.addElement(new_end_dex);
        return; }

    public void AddItem(String new_label, int dex) {
	Integer dex_Int = new Integer(dex);

	AddItem(new_label, dex_Int); }

    public void InsertItem (String label, Integer end_dex, int where) {
        label_vec.insertElementAt(label, where);
        end_vec.insertElementAt(end_dex, where);
        return; }

    public void insertNode (String label, Integer end_dex, int where) {
        label_vec.insertElementAt(label, where);
        end_vec.insertElementAt(end_dex, where);
        return; }

    public void insertNode (String label, int end_dex, int where) {
	Integer endd = new Integer(end_dex);
	insertNode(label, endd, where); }

    public void AddLabel (String label) {
        label_vec.addElement(label);
	//buckt.addToBucket(label, label_vec.size() - 1);
        return; }

    public void AddDex (Integer x) {
        try {
            end_vec.addElement(x); }
        catch (Exception e) {
            System.err.println("in SparseMatrix: AddDex:  ");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(0); }
        finally { return; } }

    public void AddDex (int x) {
        Integer new_x = new Integer(x);
        this.AddDex(new_x);
        return; }

    public void AddDex (String dex_str) {
        try {
            Integer Int_str = new Integer(Integer.parseInt(dex_str));
            this.AddDex(Int_str); }
        catch (Exception e) {
            System.err.print("in SparseMatrix.AddDex:  ");
            System.err.print("could not parse to Int: ");
            System.err.println(dex_str);
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(0); }
        finally { return; } }

    public void deleteNode (int index) {
	end_vec.removeElementAt(index);
	label_vec.removeElementAt(index); }


    /* checks whether current sparse matrix is well formed.  */
    public boolean Verify () {
        if (end_vec.size() != label_vec.size()) {
            System.err.print("ERROR!  label_vec and end_vec ");
            System.err.println("not the same size:");
            System.err.print("label_vec.size, end_vec.size:  ");
            System.err.println(label_vec.size() + ", " + end_vec.size());
            return false; }
        else
            { return true; } }

    public void PrintToPrintWriter (PrintWriter outrage) {
        int j;

        for (j = 0; j < this.size(); j++) {
            outrage.print(this.LabelAt(j) + " "); }
        outrage.println("");
        for (j = 0; j < this.size(); j++) {
            outrage.print((this.EndDexAt(j)).toString() + " "); }
        outrage.println("");
        outrage.println("");
        outrage.flush();
        return; }


    public void PrintToSystemErr() {
	if (this.isEmpty()) {
	    System.err.println("sparse matrix is empty"); }
	PrintToSystemErr(0, this.size()); }

    public void PrintToSystemErr (int start, int end) {
	
	if (start < 0) { start = 0; }
	if (end > this.size()) { end = this.size(); }
        for (int j = start; j < end; j++) {
            System.err.print(j + ".  " + this.LabelAt(j) + " ");
	    System.err.println((this.EndDexAt(j)).toString());  }
        return; }

    public static void main (String[] args) {
        SparseMatrix sparse = new SparseMatrix("EXAMPLE");
        sparse.PrintToSystemErr();
        return; }

}






