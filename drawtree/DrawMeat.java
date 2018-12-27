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

package drawtree;

import java.io.*;
import java.util.*;
import io.*;
import print.*;
import command.*;
import stats.*;
import basicinfo.*;
import syntree.*;
import search_result.*;
import revise.*;

public class DrawMeat extends CorpusDraw {

    public static String file_name;
    public static StatsPerFile file_info, comp_file_info;
    public static StatsPerSearch search_info, comp_search_info;
    public static boolean empty_source = false;
    public static File source_file;
    protected static boolean use_tags = false;
    private static int source_dex = -1;

    /*
      CrankThrough -- cranks through input file.
    */
    public static void CrankThrough (Vector source_list, ToolView toole) {
        int i;

        try {
	    if (!CorpusDraw.hasQuery()) {
		ReadIn.PrefsAndQuery(); }
	    corpse_tags = new CorpusTags((String)source_list.firstElement());
	    //corpse_tags.PrintToSystemErr();
	    file_name = nextSourceFile();
	    if (file_name.equals("NO_FILE_FOUND"))  {
		System.err.println("ERROR! no source file:  ");
		System.exit(1); }
	    DrawLoop.thruFile(file_name, toole);
        } // end try
        catch (Exception e) {
            System.err.println("ERROR!  In DrawMeat.CrankThrough:  ");
            System.err.println("Exception:  " + e.getMessage());
            System.err.println(e.toString());
            e.printStackTrace();
            Goodbye.SearchExit(); } 
        finally {
	    return; } 
    } 


    public static String nextSourceFile() {

	source_dex += 1;
	if (source_dex >= source_list.size()) {
	    return "NO_FILE_FOUND"; }
        file_name = (String)source_list.elementAt(source_dex);
	return file_name; }


} 




