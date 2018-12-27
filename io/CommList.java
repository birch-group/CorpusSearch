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
package io;

import java.util.*;

public class CommList {
    private Vector list;

    public CommList () {
	list = new Vector(); }

    public int size() { return list.size(); }

    public CommPair pairAt(int i) { return ((CommPair)list.elementAt(i)); }

    public void addCommPair(String beginn, String endd) {
	CommPair pair;

	pair = new CommPair(beginn, endd);
	list.addElement(pair); }

    public void PrintToSystemErr() {
	CommPair pair;

	for (int j = 0; j < this.size(); j++) {
	    pair = this.pairAt(j);
	    pair.PrintToSystemErr();
	    System.err.println("");
	} }
}

