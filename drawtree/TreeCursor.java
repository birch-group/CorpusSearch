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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import syntree.*;
import basicinfo.*;
//import javax.swing.*;
import search_result.*;

public class TreeCursor extends TreeTextField {

    protected int where = 0;

    //    public TreeCursor() {
    //where = super.my_string.length(); }

    public TreeCursor(String stringy) { where = stringy.length(); }

    public int getWhere() { return where; }

    public void setWhere(int w) { where = w; }

    public void plusOne(String strung) { 
	where += 1;
	if (where > strung.length()) {
	    where = strung.length(); }}

    public void minusOne(String strung) { 
	where -= 1;
        if (where < 0) { where = 0; }}	

    public String getPre(String stringy) {
	String pre = "";
	int my_dex = where;

	if (my_dex > stringy.length()) {
	    my_dex = stringy.length(); }
	if (my_dex == 0) { return pre; }
	pre = stringy.substring(0, my_dex);
	return (pre); }

    public String getPost(String stringy) {
	String post = stringy;
	int my_dex = where;

	if (my_dex > stringy.length()) {
	    my_dex = stringy.length(); }
	if (my_dex == 0) { return post; }
	post = stringy.substring(my_dex, stringy.length());
	return (post); }

    public String deleteChar(String stringy) {
	String new_str;

	if (stringy.equals("")) {
	    where = 0; }
	if (where == 0) { return (stringy);  }
	if (where >= stringy.length()) {
	    where = stringy.length() - 1;
	    return (stringy.substring(0, stringy.length() - 1)); }
	new_str = stringy.substring(0, where - 1);
	new_str += stringy.substring(where, stringy.length()); 
	where -= 1;
	if (where < 0) { where = 0; }
        return new_str; }

    public String addChar(char c, String stringy) {
	String new_str;

	if (where == 0) { 
	    new_str = c + stringy;
	    where += 1;
	    return (new_str);  }
	if (where >= stringy.length()) { 
	    where = stringy.length() + 1;
	    return (stringy + c); }
	new_str = stringy.substring(0, where) + c;
	new_str += stringy.substring(where, stringy.length());
	where += 1;
	return new_str; }
	    


}

   
