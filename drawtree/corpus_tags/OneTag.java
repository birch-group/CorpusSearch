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
import syntree.*;

/**
 * OneTag 
 * 
 */

public class OneTag {
    private String canonical;
    private Vector list_tags; // list of ArgLists representing function tags.
    private boolean has_tags;

    public OneTag() {
	this.Init(); }

    /* input to OneTag looks like this:
       IP:MAT|INF|SUB:SPE|Q|X
    */
    public OneTag(String pipe_str) {
	this.Init();
	splitUp(pipe_str);
        return; }

    private void Init() {
	has_tags = false; }

    public String getCanonical() {
	return canonical; }

    public boolean hasTags() {
	return has_tags; }

    private void splitUp(String input_str) {
	int colon_dex, prev_dex;
	String tags_str, pipe_list;
	ArgList arg;

	// string up to first colon (if any) is canonical.
	colon_dex = input_str.indexOf(':');
	if (colon_dex < 0) {
	    canonical = input_str; 
	    has_tags = false;
	    return; }
	this.canonical = input_str.substring(0, colon_dex);
	tags_str = input_str.substring(colon_dex + 1);
	has_tags = true;
	list_tags = new Vector();
	prev_dex = 0;
	colon_dex = tags_str.indexOf(':');
	while (prev_dex < tags_str.length()) {
	    if (colon_dex < 0) {
		colon_dex = tags_str.length();
		pipe_list = tags_str.substring(prev_dex, colon_dex);
		arg = new ArgList(pipe_list);
		list_tags.addElement(arg);
		return; }
	    pipe_list = tags_str.substring(prev_dex, colon_dex);
	    arg = new ArgList(pipe_list);
	    list_tags.addElement(arg);
	    prev_dex = colon_dex + 1;
	    colon_dex = tags_str.indexOf(':', prev_dex); }
	return;
    }

    public int size() {
	return list_tags.size(); }

    public boolean isMatch (String in_str, Vector divide) {
	String root_tag, function_tag;
	int prev_dex, dash_dex, list_dex = 0, j;
	ArgList argle;
	Vector tagstuff;
	Character charr = new Character('Q');

	tagstuff = PipeList.split(in_str, divide);
	root_tag = (String)tagstuff.firstElement(); 	
	if (tagstuff.size() == 1) {
	    return ((this.canonical).equals(root_tag)); }
	if (!(this.canonical).equals(root_tag)) {
	    return false; }
	if (!this.hasTags()) { return false; }
	thru_tagstuff:  for (j = 1; j < tagstuff.size(); j++) {
	    function_tag = (String)tagstuff.elementAt(j);
	    if (charr.isDigit(function_tag.charAt(0))) {
		continue thru_tagstuff; }
	    argle = (ArgList)list_tags.elementAt(list_dex);
	    if (argle.hasMatch(function_tag)) {
		return true; } }
	return false; }

    public String toString() {
	ArgList arg;
	int i, j;
	String piper, return_me;
	

	//System.err.print(canonical);
	if (!has_tags) {
	    return_me = "no function tags";
	    return return_me; }
	return_me = "@" + canonical + "@";
	for (i = 0; i < this.size(); i++) {
	    arg = (ArgList)list_tags.elementAt(i);
	    piper = arg.toPipeList();
	    return_me += ":" + piper; }
	return return_me; }

    public void PrintToSystemErr() {
	ArgList arg;
	int i, j;
	String piper;

	System.err.print(canonical);
	if (!has_tags) {
	    System.err.println(":  no function tags");
	    return; }
	for (i = 0; i < this.size(); i++) {
	    arg = (ArgList)list_tags.elementAt(i);
	    piper = arg.toPipeList();
	    System.err.print(":" + piper); }
	System.err.println("");
	return; }

} 






