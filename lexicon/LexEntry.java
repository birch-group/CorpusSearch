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
package lexicon;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class LexEntry {

    private String canonical;
    private Vector variations;
    private Vector pos_stats;
    private int total;

    public LexEntry() {
	variations = new Vector();
	pos_stats = new Vector();
	total = 0;}

    public LexEntry(String canon) {
	this.canonical = canon;
        variations = new Vector();
        pos_stats = new Vector();
        total = 0;}

    public LexEntry(POSPair paris) {
	POSStat poss;
	try {
	pos_stats = new Vector();
	variations = new Vector();
	canonical = (paris.GetTextStr()).toLowerCase();
	variations.addElement(canonical);
	if (canonical.startsWith("$")) {
	    canonical = canonical.substring(1, canonical.length()); }
	poss = new POSStat(paris.GetPOSStr());
	pos_stats.addElement(poss);
	total += 1;
	} 
	catch (Exception e) {
	    e.printStackTrace(); }
    }

    public boolean belongs(POSPair paris) {
	String text;
	text = paris.GetTextStr();
	if (text.startsWith("$")) {
	    text = text.substring(1, text.length()); }
	if (text.equalsIgnoreCase(canonical)) {
	    return true; }
	return false; }

    public void addPOSPair(POSPair paris) {
	String text;
	String pos, old_pos;
	POSStat old_stat, new_stat;
	int i;
	text = paris.GetTextStr();
	var_loop: for (i = 0; i < variations.size(); i++) {
	    if (text.equals((String)variations.elementAt(i))) {
		break var_loop; }
	    if (i == variations.size() - 1) {
		variations.addElement(text); 
		break var_loop;}
	}
	pos = paris.GetPOSStr();
	pos_loop: for (i = 0; i < pos_stats.size(); i++) {
	    old_stat = (POSStat)pos_stats.elementAt(i);
	    old_pos = old_stat.getString();
	    if  (old_pos.equals(pos)) {
		old_stat.incrementTotal();
		break pos_loop; }
	    if (i == pos_stats.size() - 1) {
		new_stat = new POSStat(pos);
		pos_stats.addElement(new_stat);
		break pos_loop; }
	}
	total += 1;
	return;
    }

    public void setCanonical(String canon) {
	canonical = canon.toLowerCase();}

    public String getCanonical() {
	return canonical; }

    public Vector getVariations() {
	return variations; }

    public void addToVariations(String var) {
	variations.addElement(var); }

    public void addToPOSStats(NumPOS numb) {
	pos_stats.addElement(numb); }

    public Vector getPOSStats() {
	return pos_stats; }

    public void incrementTotal() {
	total += 1; }

    public int getTotal() {
	return total; }

    /*
      returns true if this is alphabetically
      later than input LexEntry; false otherwise.
    */
    public boolean LaterEntry(LexEntry lurk) {
	if ((this.canonical).compareTo(lurk.getCanonical()) > 0) {
	    return true; }
	return false; }
				  
    public void PrintToSystemErr() {
	POSStat numb;
	System.err.println("LexEntry:  ");
	System.err.println("canonical:  " + canonical);
	System.err.println("variations:  " + variations);
	System.err.println("pos_stats:  ");
	for (int i = 0; i < pos_stats.size(); i++) {
	    numb = (POSStat)pos_stats.elementAt(i);
	    numb.PrintToSystemErr();
	}
	System.err.println("total:  " + this.total); }

} 
