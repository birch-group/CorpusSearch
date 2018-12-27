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

/**
 * handles tasks shared by search functions.
 * @author Beth Randall
 */

public class Matcher {

    /*
      StarMatch -- finds whether input looked-for-string and input 
      found-string match.  Implements * for wild-card matches.
      Input -- String looking -- string to be matched; may contain *
      -- String eureka -- string found in text; may match
      Output -- true if strings match (mod *); false otherwise.
    */
    /**
     * determines whether two strings match, mod * (wild card).
     * @param looking string to be matched; may contain *
     * @param eureka string found in text
     * @return true if strings match mod *; false otherwise
     */
    public static boolean StarMatch(String looking, String eureka) {
        int i, j = 0, k, look_len, reek_len;
        char i_look, i_look_plus_1;

        look_len = looking.length();
        reek_len = eureka.length();
	look_for:  for (i = 0; i < look_len; i++) {
            i_look = looking.charAt(i);
            if (j >= reek_len) {
                if ((i_look == '*') && (i == (look_len - 1))) {
                    return true; }
                return false; }
            if (i_look == '*') {
                if (i == (look_len - 1))
                    return true;
		star_loop:  for (k = j; k < reek_len; k++) {
                    String sub_look, sub_reek;
		    i_look_plus_1 = looking.charAt(i + 1);
		    if (i_look_plus_1 == '\\') {
			i += 1;
			i_look_plus_1 = looking.charAt(i + 1); }
                    if (eureka.charAt(k) == i_look_plus_1) {
                        if ((k == (reek_len - 1)) &&
			    (i == (look_len - 2)) ) {
                            return true;
                        }
                        sub_look = looking.substring(i+2);
                        sub_reek = eureka.substring(k+1);
                        if (StarMatch(sub_look, sub_reek))
                            return true;
                    } // end if eureka.charAt(k) == looking.charAt(i+1)
                } // end for k = j; k < eureka.length(); k++
            } // end if looking.charAt(i) == '*'
            if (i_look == '#') {
                char k_reek;
                for (k = j; k < reek_len; k++) {
                    k_reek = eureka.charAt(k);
                    if ((k_reek < '0') || (k_reek > '9'))
                        return false; }
                return true; }
            if (i_look == '\\') { // escape char
                i += 1;
                if (i == look_len)
                    return false;
                if (looking.charAt(i) == eureka.charAt(j)) {
                    j += 1;
                    if ((i == (look_len - 1)) && (j == reek_len))
                        return true;
                    continue look_for; }
                return false; }
            if (i_look == eureka.charAt(j)) {
                if ((i == (look_len - 1)) && (j == (reek_len - 1)))
                    return true;
                else {
                    j += 1;
                    continue look_for; } }
            return false;
        } // end for (i = 0; i < looking.size(); i++)
        return false;
    } // end method StarMatch


    /*
      MakeList -- makes list of node boundaries or arguments from string.
      Called by ReadIn, Commander.
      sample input:  MD|VB*|HV*
      sample output:  Vector containing:
      0.) MD
      1.) VB*
      2.) HV*
      input -- list of arguments in string form
      output -- same list of arguments in vector form.
    */
    public static Vector MakeList(String PipeList) {
        Vector NewList = new Vector();
        String one_item = "";
        StringBuffer wordbuff = new StringBuffer();
        int i;
        char prev_char = 'Q', curr_char = 'Z';

	char_by_char:  for (i = 0; i < PipeList.length(); i ++) {
            prev_char = curr_char;
            curr_char = PipeList.charAt(i);
            if (i+1 < PipeList.length()) {
                if ((curr_char == '\\') &&
		    (PipeList.charAt(i+1) == '|')) {
                    continue char_by_char;
                }
            }
            if (!(curr_char == '|')) {
                wordbuff.append(curr_char);
            }
            if (i > 0) {
                if ((curr_char == '|') && (prev_char == '\\')) {
                    wordbuff.append('|');
                    continue char_by_char;
                }
            } // end if i > 0
            if ((curr_char == '|') || (i == (PipeList.length() - 1))) {
                one_item = wordbuff.toString();
                wordbuff = new StringBuffer();
                NewList.addElement(one_item);
                continue char_by_char;
            }
        } // end for i=0; i < PipeList.length(); i++
        return NewList;
    } // end method MakeList

    public static void main (String[] args) {
	System.err.println("match *ICH* and slash*ICH*:  ");
	System.err.println(StarMatch("\\*ICH\\*", "*ICH*"));
	return;
    }
			   

} // end class Matcher






