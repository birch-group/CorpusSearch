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
  PosStat -- an object containing 
  a POS string, an int (number of times POS string was found).
 */
package lexicon;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class POSStat {

    private String pos;
    private int total;

    public POSStat() {
	pos = "";
	total = 0; }

    public POSStat(String post) {
	this.pos = post;
	total = 1; }

    public void incrementTotal() {
	this.total += 1; }

    public String getString () {
	return this.pos;}

    public int getTotal() {
	return this.total; }

    public void PrintToSystemErr() {
	System.err.println("pos:  " + pos);
	System.err.println("total: " + total);
    } 

} 
