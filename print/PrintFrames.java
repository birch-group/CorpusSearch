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
  PrintFrames.java -- prints out frames_vec.
  called if subcats is true.
*/
package print;

import java.io.*;
import java.util.*;
import frames.*;

public class PrintFrames {

    /*
      List -- prints out contents of ListFrames object.
    */
    public static void List (ListFrames fist, PrintWriter OutStuff) {
	int i, j, k;
	SameKernelBucket bucko;
	Kernel kern;
	SingleFrame framed;

	for (j = 0; j < fist.size(); j++) {
	    bucko = fist.getBucket(j);
	    kern = bucko.getSynKernel();
	    OutStuff.println("");
	    OutStuff.println("/*");
	    OutStuff.println(kern.allLabels());
	    OutStuff.println("*/");
	    OutStuff.println("");
	    for (k = 0; k < bucko.size(); k++) {
		framed = bucko.getFrame(k);
		for (i = 0; i < framed.size(); i++) {
		    OutStuff.print(framed.sisLabelAt(i) + " "); }
		OutStuff.print(framed.getIDString());
		if (framed.getTotal() != 1) {
		    OutStuff.print(" [" + framed.getIndex() + ", ");
		    OutStuff.print(framed.getTotal() + "]"); }
		OutStuff.println("");
	    } // end for k = 0; k < bucko.size()
	} // end for j = 0; j < fist.size(); j++
	OutStuff.println("");
	return;
    } // end method List

} // end class PrintFrames
