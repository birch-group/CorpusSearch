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
  POSPair -- an object containing two nodes,
  a POS node and its associated text node.
 */
package lexicon;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class POSPair {

    private Node POS_node;
    private Node text_node;

    public POSPair() { }

    public POSPair(Node pos, Node text) {
	this.POS_node = pos;
	this.text_node = text; }

    public Node GetPOSNode() {
	return POS_node; }

    public Node GetTextNode() {
	return text_node; }

    public String GetPOSStr() {
	return (POS_node.getLabel()); }

    public String GetTextStr() {
	return (text_node.getLabel()); }

    public boolean SameText(POSPair other) {
	String this_text, other_text;

	this_text = this.GetPOSStr();
	other_text = other.GetPOSStr();
	if (this_text.startsWith("$+") && this_text.length() > 2) {
	    this_text = this_text.substring(2, this_text.length());}
	if (this_text.startsWith("$") && this_text.length() > 1) {
	    this_text = this_text.substring(1, this_text.length());}
	if (other_text.startsWith("$") && other_text.length() > 1) {
	    other_text = other_text.substring(1, other_text.length()); }
	if (this_text.equalsIgnoreCase(other_text)) {
	    return true; }
	return false;
    }

    /* returns number corresponding to ASCII number
       of initial letter of text.  Used to bucket-sort
       POSPairs.
    */
    public int getASCIInum() {
	String text_str;
	Character charlie = new Character('Q');
	int num;
	char init;


	text_str = this.GetTextStr();
        init = text_str.charAt(0);
	if (init == '$' || init == '+') {
	    if (text_str.length() > 1) {
		init = text_str.charAt(1); }
	    if (init == '+' && text_str.length() > 2) {
	        init = text_str.charAt(2); } }
	init = charlie.toUpperCase(init);
	num = (int)init;
	num -= 64;
	if (num < 1 || num > 26) {
	   num = 0; }
	return num; }

    public void PrintToSystemErr() {
	System.err.println("POSPair:  ");
	POS_node.PrintToSystemErr();
	text_node.PrintToSystemErr(); }

} 
