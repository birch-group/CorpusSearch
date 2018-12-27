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
import basicinfo.*;

public class FileEmptiesList {

    private Vector list;
    private LexEntryBucket other_bucket;

    public FileEmptiesList() {
	list = new Vector(); }

    public int size() {
	return list.size(); }

    public boolean isEmpty() {
	return list.isEmpty(); }

    public void addSentenceList(SentenceList sent) {
	Vector posl = sent.getTraceList();
	for (int i = 0; i < posl.size(); i++) {
	    this.addLexEntry((LexEntry)posl.elementAt(i)); } }
					  
    public void addLexEntry(LexEntry lex) {
	int dex;
	LexEntry old_lex;
	String canonical, old_empty;

	canonical = lex.getCanonical();
	for (dex = 0; dex < list.size(); dex++) {
	    old_lex = (LexEntry)list.elementAt(dex);
	    old_empty = old_lex.getCanonical();
	    if (canonical.equals(old_empty)) { return; }}
	list.addElement(lex);
	return; }

    public LexEntry lexEntryAt(int dex) {
	return ((LexEntry)list.elementAt(dex)); }

    public void PrintToSystemErr() {
	this.PrintToSystemErr(0, list.size()); }

    public void PrintToSystemErr(int start, int end) {
	String trace;
	int i;

	if (start < 0) { start = 0; }
	if (end > list.size()) { end = list.size(); }
	System.err.println("FileEmptiesList:  ");
	for (i = start; i < end; i++) {
	    trace = (String)list.elementAt(i);
	    if (i == end - 1) {
		System.err.print(trace); }
	    else {
		System.err.print(trace + "|"); }
	}
	System.err.println("");
    } 

} 
