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


/**
 * contains information used by many different packages
 */

public class Vitals {

    public static ArgList Node_List;
    public static ArgList ignore_list;
    public static ArgList word_ignore_list;
    public static String version_number = "2.003.00";
    public static String manual_address = 
	"http://corpussearch.sourceforge.net/CS-manual/Contents.html";
    public static String CD_manual_address = 
	"http://corpussearch.sourceforge.net/CS-manual/Contents.html";
    public static String c_note = "Copyright 2010 Beth Randall";

    public static void Init(String node, String ignore, String word_ignore) {
	Node_List = new ArgList(node);
	ignore_list = new ArgList(ignore);
	word_ignore_list = new ArgList(word_ignore); }

} 






