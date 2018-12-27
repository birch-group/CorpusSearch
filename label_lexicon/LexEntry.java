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

public class LexEntry {

    private String canonical;
    private int total;

    public LexEntry() {
	total = 0;}

    public LexEntry(String canon) {
	this.canonical = canon;
        total = 1;}

    public boolean belongs(LexEntry lax) {
	if ((lax.getCanonical()).equals(canonical)) {
	    return true; }
	return false; }

    public void setCanonical(String canon) {
	canonical = canon.toLowerCase();}

    public String getCanonical() {
	return canonical; }

    public void incrementTotal() {
	total += 1; }

    public int getTotal() {
	return total; }

    public int getASCIInum() {
        Character charlie = new Character('Q');
        int num;
        char init;

	if (canonical.length() == 0) { return 0; }
        init = canonical.charAt(0);
        init = charlie.toUpperCase(init);
        num = (int)init;
        num -= 64;
        if (num < 1 || num > 26) {
	    num = 0; }
        return num; }

    /*
      returns true if this is alphabetically
      later than input LexEntry; false otherwise.
    */
    public boolean LaterEntry(LexEntry lurk) {
	if ((this.canonical).compareTo(lurk.getCanonical()) > 0) {
	    return true; }
	return false; }

    /*
      returns true if this is alphabetically
      later than input LexEntry; false otherwise.
    */
    public boolean MoreLaterEntry(LexEntry lurk) {
	if (this.total > lurk.getTotal()) {
	    return true; }
	if ((this.canonical).compareTo(lurk.getCanonical()) > 0) {
	    return true; }
	return false; }
				  
    public void PrintToSystemErr() {
	System.err.println(canonical + "  [" + total + "]"); }

} 
