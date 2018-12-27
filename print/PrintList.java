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
  Beth Randall: Jan 2001
*/
package print;

import java.io.*;
import java.util.*;

public class PrintList {

    /*
      Vector -- prints out contents of input vector, 
      one item per line.  Used for "print_only".
    */
    public static void Vector(Vector vexed, PrintWriter OutStuff) {
	int i;

	for (i = 0; i < vexed.size(); i++) {
	    OutStuff.println((String)vexed.elementAt(i)); }
    }

} // end class PrintList
