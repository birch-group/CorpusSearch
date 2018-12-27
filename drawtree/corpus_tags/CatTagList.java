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

import java.util.*;
import basicinfo.*;

public class CatTagList {
    Vector argle;

    public CatTagList(String in_list) {
	argle =  PipeList.MakeList(in_list); }

    public int size() {
	return argle.size(); }

    public boolean legitTag(String taggle, Vector divide) {
	Vector tagstuff;
	String tagg, arg_tagg;
	Character charr = new Character('Q');
	boolean found;
	int i, j;

	tagstuff = PipeList.split(taggle, divide);
	if (tagstuff.size() > 2 || tagstuff.size() < 1) {
	    return false; }
	tagg = ((String)tagstuff.elementAt(0)).trim();
	found = false;
	argle_loop: for (j = 0; j < argle.size(); j++) {
	    arg_tagg = (String)argle.elementAt(j);
	    if (arg_tagg.equals(tagg)) {
		found = true;
		break argle_loop; } }
	if (!found) {
	    return false; }
	if (tagstuff.size() > 1) {
	    tagg = (String)tagstuff.elementAt(1);
	    for (i = 0; i < tagg.length(); i++) {
		if (!charr.isDigit(tagg.charAt(i))) {
		    return false; } } }
	return true; }

    public void PrintToSystemErr() {
	int i;
	String tagg;

	System.err.println("empty category list:  ");
	for (i = 0; i < argle.size(); i++) {
	    tagg = (String)argle.elementAt(i);
	    System.err.print(tagg);
	    if (i < argle.size() - 1) {
		System.err.print("|"); } }
	System.err.println("");
	return; }

} 






