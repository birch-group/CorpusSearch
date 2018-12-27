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

public class NumPOS {

    private String pos_str;
    private int total;

    public NumPOS() {
	total = 0;
    }

    public NumPOS(String possum) {
	total = 0;
	pos_str = possum; }

    public void incrementTotal() {
	total += 1; }

    public int getTotal() {
	return total; }
			   
    public String getPOSstr() {
	return pos_str; }
			       
    public void PrintToSystemErr() {
	System.err.println("NumPOS:  ");
	System.err.println("pos_str: " + pos_str);
	System.err.println("total:  " + total);
    } // end method PrintToSystemErr()

} // end class POSPair.java
