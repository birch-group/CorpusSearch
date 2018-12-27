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
	Beth Randall:  July 2001
	Timing.java
	contains methods to print out how much time CorpusSearch takes.
*/
package csearch;

import java.io.*;
import java.util.*;

/**
 * contains methods to time the running of CorpusSearch.
 */

public class Timing extends CorpusSearch {

    private static Date clock;
    private static long time1, time2, total_time;
    private static int minutes, seconds;

    public Timing () {
	clock = new Date();
        time1 = clock.getTime(); }

    /**
     * stops clock and prints time to screen.
     */
    public void StopClock () {
	
        clock = new Date();
        time2 = clock.getTime();
        total_time = time2 - time1;
        System.err.print("time taken:  " + total_time + " milliseconds.  ");
        minutes = Math.round(total_time/60000);
        if (minutes > 0) {
	    System.err.print(minutes + " minutes, "); 
	    seconds = Math.round((total_time%60000)/1000);    
	    System.err.println(seconds + " seconds."); }
        System.err.println("");
        System.err.println("");
        return; }

} 






