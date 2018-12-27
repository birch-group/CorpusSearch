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
import syntree.*;

/**
 * handles tasks shared by search functions.
 * @author Beth Randall
 */

public class PipeList {

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
                    continue char_by_char; } }
            if (!(curr_char == '|')) {
                wordbuff.append(curr_char); }
            if (i > 0) {
                if ((curr_char == '|') && (prev_char == '\\')) {
                    wordbuff.append('|');
                    continue char_by_char; } }
            if ((curr_char == '|') || (i == (PipeList.length() - 1))) {
                one_item = wordbuff.toString();
                wordbuff = new StringBuffer();
                NewList.addElement(one_item);
                continue char_by_char; } }
        return NewList;
    } 

    public static Vector makeCharacterList(String PipeList) {
        Vector NewList = new Vector();
        Character one_char;
        int i;

	char_by_char:  for (i = 0; i < PipeList.length(); i ++) {
            one_char = new Character(PipeList.charAt(i));
            if (!(one_char.charValue() == '|')) {
                NewList.addElement(one_char); } }
	return NewList; 
    } 
    

    /**
       returns Vector of substrings, not including split_chars.
    */      
    public static Vector split(String to_split, Vector split_chars) {
        Vector NewList = new Vector();
        String one_item = "";
        StringBuffer wordbuff = new StringBuffer();
        int i;
        char curr_char = 'Z';
	boolean is_split = false;

	char_by_char:  for (i = 0; i < to_split.length(); i ++) {
            curr_char = to_split.charAt(i);
	    is_split = isSplitChar(curr_char, split_chars);
            if (!is_split) {
                wordbuff.append(curr_char); }
            if (is_split || i == to_split.length() - 1) {
                one_item = wordbuff.toString();
                wordbuff = new StringBuffer();
                NewList.addElement(one_item); } }
	return NewList; }

    public static boolean isSplitChar(char curr, Vector split_chars) {
	char one_split = 'Q';
	int i;

	for (i = 0; i < split_chars.size(); i++) {
	    one_split = ((Character)split_chars.elementAt(i)).charValue();
	    if (curr == one_split) {
		return true; } }
	return false; }
	

    public static boolean IsOnList(Node nodal, Vector List) {
	String label = nodal.getLabel();
	return (IsOnList(label, List)); }

    public static boolean IsOnList(String label, Vector List) {
	String list_item;
        for (int r = 0; r < List.size(); r++) {
            list_item = (String)List.elementAt(r);
	    if (Matcher.StarMatch(list_item, label)) {
                return (true); } }
        return (false); }
    /**
     * purges node list of items on input purge_list.
     */
    public static Vector PurgeNodeList (SynTree sparse,
                                        Vector in_list, Vector purge_list) {
        int n, k;
        Node nodal = new Node("NULL"), daughter, mom;
        String in_text1, in_text2, purge_text;

        try {
            in_list_loop: for (n = 0; n < in_list.size(); n++) {
                nodal = (Node)in_list.elementAt(n);
                in_text1 = nodal.getLabel();
                for (k = 0; k < purge_list.size(); k++) {
                    purge_text = (String)purge_list.elementAt(k);
                    if (Matcher.StarMatch(purge_text, in_text1)) {
                        in_list.remove(n);
                        n -= 1;
                        continue in_list_loop; } }
                if (sparse.IsLeafPOS(nodal)) {
                    daughter = sparse.FirstDaughter(nodal);
                    in_text2 = daughter.getLabel();
                    for (k = 0; k < purge_list.size(); k++) {
                        purge_text = (String)purge_list.elementAt(k);
                        if (Matcher.StarMatch(purge_text,in_text2)) {
                            in_list.remove(n);
                            n -= 1;
                            continue in_list_loop; } } }
                if (sparse.IsLeafText(nodal)) {
                    mom = sparse.GetMother(nodal);
                    in_text2 = mom.getLabel();
                    for (k = 0; k < purge_list.size(); k++) {
                        purge_text = (String)purge_list.elementAt(k);
                        if (Matcher.StarMatch(purge_text,in_text2)) {
                            in_list.remove(n);
                            n -= 1;
                            continue in_list_loop; } } } }
            return in_list; }
        catch (Exception e) { e.printStackTrace(); }
        finally { return in_list; } }

    /**
     * purges node list of items on input purge_list.
     */
    public static Vector PurgeNodeList (SynTree sparse,
                                        Vector in_list, ArgList purge_list) {
        int n;
        Node nodal, daughter, mom;
        String in_text;

        try {
            in_list_loop: for (n = 0; n < in_list.size(); n++) {
                nodal = (Node)in_list.elementAt(n);
                in_text = nodal.getLabel();
		if (purge_list.hasMatch(in_text)) {
		    in_list.remove(n);
		    n -= 1;
                    continue in_list_loop; }
                if (sparse.IsLeafPOS(nodal)) {
                    daughter = sparse.FirstDaughter(nodal);
                    in_text = daughter.getLabel();
		    if (purge_list.hasMatch(in_text)) {
			in_list.remove(n);
			n -= 1;
			continue in_list_loop; } } 
                if (sparse.IsLeafText(nodal)) {
                    mom = sparse.GetMother(nodal);
                    in_text = mom.getLabel();
		    if (purge_list.hasMatch(in_text)) {
			in_list.remove(n);
			n -= 1;
			continue in_list_loop; } } }
            return in_list; }
        catch (Exception e) { e.printStackTrace(); }
        finally { return in_list; } }

} 






