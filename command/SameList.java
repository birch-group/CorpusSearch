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
package command;

import java.util.*;

public class SameList {

    private Vector similar;

    public SameList () {
	similar = new Vector();
    }

    public void addCoincidence (boolean coinc, int one, int two) {
	Coincidence co = new Coincidence(coinc, one, two);
	this.addCoincidence(co);
    }

    public void addCoincidence (Coincidence co) {
	similar.addElement(co);
    }

    public Coincidence getCoincidence(int n) {
	return((Coincidence)similar.elementAt(n));
    }

    public int size() {
	return (similar.size());
    }

    public boolean isEmpty() {
	return (similar.isEmpty());
    }


    public void PrintToSystemErr() {
	int j;
	Coincidence conc;

	for (j = 0; j < similar.size(); j++) {
	    System.err.print(j + ".  ");
	    conc = (Coincidence)similar.elementAt(j);
	    conc.PrintToSystemErr();
	    System.err.println("");
	}
    }

}
