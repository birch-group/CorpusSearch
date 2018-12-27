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
  POSPairBucket -- a bucket (Vector) containing POSPairs 
  whose text begins with the same initial letter.
 */
package lexicon;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class POSPairBucket {

    private Vector POSPairList;

    public POSPairBucket() {
	POSPairList = new Vector(); }

    public int size() {
	return POSPairList.size(); }

    public boolean isEmpty() {
	return (POSPairList.isEmpty()); }

    public void AddPOSPair(POSPair paris) {
	POSPairList.addElement(paris); }

    public POSPair POSPairAt(int i) {
	return((POSPair)POSPairList.elementAt(i)); }

    public void PrintToSystemErr() {
	POSPair paris;
	System.err.println("POSPairBucket:  ");
	for (int i = 0; i < POSPairList.size(); i++) {
	    paris = (POSPair)POSPairList.elementAt(i);
	    paris.PrintToSystemErr();
	} }

} 
