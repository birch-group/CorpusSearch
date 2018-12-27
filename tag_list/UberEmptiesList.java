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

package tag_list;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class UberEmptiesList {

    private Vector list;
    private LexEntryBucket other_bucket;

    public UberEmptiesList() {
	list = new Vector(); }

    public int size() {
	return list.size(); }

    public LexEntry lexEntryAt(int i) {
	return ((LexEntry)list.elementAt(i)); }

    public void addFileList(FileEmptiesList folly) {
	int i, j;
	String in_empty, old_empty;
	LexEntry old_lex, in_lex;

	in_lex = new LexEntry();
	for (i = 0; i < folly.size(); i++) {
	    in_lex = folly.lexEntryAt(i);
	    for (j = 0; j < this.size(); j++) {
		old_lex = this.lexEntryAt(j);
		if (in_lex.getCanonical().equals(old_lex.getCanonical())) {
		    return; }} 
	    this.addLexEntry(in_lex); }
    }

    public void addLexEntry(LexEntry lox) {
	this.list.addElement(lox); }

    public void PrintToSystemErr() {
	this.PrintToSystemErr(0, list.size()); }

    public void PrintToSystemErr(int start, int end) {
	LexEntry lurks;
	String canon;
	int i;

	System.err.println("UberEmptiesList:  ");
	if (start < 0) { start = 0; }
	if (end > list.size()) { end = list.size(); }
	for (i = start; i < end; i ++) {
	    lurks = this.lexEntryAt(i);
	    canon = lurks.getCanonical();
	    System.err.print(canon);
	    if (i < end - 1) {
		System.err.print("|"); } }
	System.err.println("");
	return; }
} 
