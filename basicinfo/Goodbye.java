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

package basicinfo;

import java.util.*;
import java.io.*;

public class Goodbye {

    public Goodbye() {}

    private static boolean empty_command_line = false;

    public static void setEmptyCommandLine(boolean logic) {
	empty_command_line = logic; }

    /*
      SearchExit -- calls System.exit or DelayExit, depending
      on the value of empty_command_line.
      This is so you won't get a flashed
      error message in a disappearing window on a Mac.
      input -- void.
      output -- void.
      side-effect -- system is exited.
    */
    /**
     * exits program;  Calls DelayExit if needed.
     */
    public static void SearchExit() {
        if (empty_command_line) {
            DelayExit(); }
        else {
            System.exit(1); }
    } // end method SearchExit

    /*
      DelayExit -- used instead of System.exit when the command
      line is empty.  This is so you won't get a flashed
      error message in a disappearing window on a Mac.
      input -- void.
      output -- void.
      side-effect -- exit is delayed until user enters "return".
    */
    /**
     * delays program exit, to prevent disappearing window on a Mac.
     */
    public static void DelayExit() {
	BufferedReader in =
	    new BufferedReader(new InputStreamReader(System.in));
	String response;

        try {
            System.out.println("");
            System.out.println("");
            System.out.println("ERROR!  Search is aborted.");
            System.out.print("When you have finished reading ");
            System.err.println("the above error messages,");
            System.out.println("hit the return key, then restart program.");
            System.out.flush();
            response = in.readLine();
            System.exit(1);
        } // end try
        catch (Exception e) {
            System.err.println("In CorpusSearch:  DelayExit:  ");
            System.err.println(e.getMessage()); } // end catch
        finally {
            return; }
    } // end method DelayExit

} // end class Goodbye



