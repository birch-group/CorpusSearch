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
  Node -- handles nodes on the tree.  A node contains two objects:
  0.)  String label
  1.)  Integer index
  
 */
package syntree;

import java.io.*;
import java.util.*;

public class Node {

    private String label;
    private Integer index, null_index = new Integer(-3);
    private String null_label = new String("NULL");

    public Node () {
        this.label = new String("");
        this.index = new Integer(-1); }

    public Node(Integer index, String label) {
        this.label = new String(label);
        this.index = new Integer(index.intValue()); }

    public Node(int index, String label) {
        this.label = new String(label);
        this.index = new Integer(index); }

    /* call as follows:
       my_null = new Node("NULL");
    */
    public Node (String aragon) {
        if (aragon.equals("NULL")) {
            this.label = new String(this.null_label);
            this.index = new Integer((this.null_index).intValue()); } }

    public Node copy() {
	Node n_copy;

	n_copy = new Node(this.getIndex(), this.getLabel()); 
	return n_copy; }

    public boolean equals (Node node2) {
	if ((this.getIndex()).equals(node2.getIndex())) {
	    return true; }
	return false; }

    public boolean lessThan (Node node2) {
	if (this.getIndex_int() < node2.getIndex_int()) {
	    return true; }
	return false; }

    public boolean greaterThan (Node node2) {
	if (this.getIndex_int() > node2.getIndex_int()) {
	    return true; }
	return false; }

    public String getLabel() {
        return this.label; }

    public String minusTags() {
	int hyphen_dex;

	hyphen_dex = this.label.indexOf("-");
	if (hyphen_dex == -1) { return this.label; }
	return(this.label.substring(0, hyphen_dex)); }

    public Integer getIndex() {
        return this.index; }

    public int getIndex_int() {
	return((this.index).intValue()); }

    public void setLabel(String new_label) {
        this.label = new_label; }

    public void setIndex(Integer new_index) {
        this.index  = new_index; }

    public void setIndex(int new_index) {
        Integer henry = new Integer(new_index);
        this.setIndex(henry); }

    public boolean isEmpty() {
        if (index.intValue() == -1) { return true; }
        else { return false; } }

    public boolean IsNullNode () {
        if (this.getIndex().equals(this.null_index)) {
            if (this.getLabel().equals(this.null_label)) {
                return true; } }
        return false; }

    public boolean IsMETAROOT () {
	if (this.getIndex_int() == 0) { return true; }
	return false; }

    /* if label is "NP-SBJ-2", returns "2". */
    public String getLabelDex() {
        int i;
        String dex_str = "";
        Character charlotte = new Character('Q');
        char c;

	if (this.label.equals("")) {
	    return ""; }
        dex_loop: for (i = label.length() - 1; i >= 0; i--) {
            c = label.charAt(i);
            if (charlotte.isDigit(c)) {
                dex_str = c + dex_str; 
		continue dex_loop; }
	    if ((c == '-') || (c == '=')) {
		break dex_loop; }
            else { 
		dex_str = "";
		break dex_loop; } }
        return dex_str; }

    public boolean hasLabelDex() {
	String dex;

	dex = this.getLabelDex();
	if (dex.equals("")) { return false; }
	return true; }

    public boolean sameLabelDex(Node other) {
	String my_dex, other_dex;

	my_dex = this.getLabelDex();
	other_dex = other.getLabelDex();
	if (my_dex.equals(other_dex)) {
	    return true; }
	return false; }

    // returns e.g. "(12 NP)".
    public String toString() {
	String boleyn = "";
	boleyn += "(" + (this.index).toString() + " ";
	boleyn += this.label + ")";
	return boleyn; }

    public void PrintToPrintWriter (PrintWriter outrage) {
        outrage.print("[" + this.index + ", " + this.label + "]"); }

    public void PrintToSystemErr () {
        System.err.print("[" + this.index + ", " + this.label + "]"); }

    public void PrintNodeVector (Vector to_print) {
        int i, j;
        Node item;
        Vector sub_vec;

	if (to_print.isEmpty()) {
	    //System.err.println("vector is empty");
	    return; }
        if (to_print.elementAt(0) instanceof Vector) {
            for (i = 0; i < to_print.size(); i++) {
                sub_vec = (Vector)to_print.elementAt(i);
                System.err.print("[");
                for (j = 0; j < sub_vec.size(); j++) {
                    item = (Node)sub_vec.elementAt(j);
                    item.PrintToSystemErr();
                }
                System.err.println("]"); }}
        if (to_print.elementAt(0) instanceof Node) {
            for (i = 0; i < to_print.size(); i++) {
                item = (Node)to_print.elementAt(i);
                item.PrintToSystemErr(); }}
        return; }
}






