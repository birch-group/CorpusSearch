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
 * a RegExpStr represents one regular expression.  It is a list of 
 * OneTerm objects.
  */

public class RegExpStr extends ArgList {

    Vector res_list;
    int BIT_SET_SIZE = 54;
    boolean is_ROOT, is_METAROOT;
    String original;

    public RegExpStr() { Init(); }

    public RegExpStr(String rex) {
	Init();
	original = rex;
	buildRegExp(rex); }
    //	if (rex.equals("[tT][ou]*")) {
    //      this.PrintToSystemErr(); }}

    private void Init() {
	res_list = new Vector(); 
        is_ROOT = false;
	is_METAROOT = false; }

    public boolean isRoot() { return is_ROOT; }

    public boolean isMetaRoot() {return is_METAROOT; }

    private String chopRegExp(String rex) {
	int close_dex, j;
	Character chara = new Character('Q');

	// a hack to deal with incoming e.g. [1]CP-REL*
	if (rex.charAt(0) == '[') {
	    close_dex = rex.indexOf(']');
	    if (close_dex < 0) {
		return rex; }
	    for (j = 1; j < close_dex; j++) {
		if (!chara.isDigit(rex.charAt(j))) { return rex; } }
	    rex = rex.substring(close_dex + 1); }
	//  System.err.println("rex after chop:  " + rex);
        return rex; }
	
    private void buildRegExp(String rex) {
	int dex = 0;
	OneTerm one_term;

	if (rex.equals("$METAROOT")) {
	    is_METAROOT = true; return; }
	if (rex.equals("$ROOT")) {
	    is_ROOT = true; return; }
	rex = chopRegExp(rex);
	while (dex < rex.length()) {
	    one_term = new OneTerm(rex, dex);
	    res_list.addElement(one_term);
	    dex = one_term.getEndDex(); }
	one_term = (OneTerm)res_list.lastElement();
	one_term.markAsLast();
	for (dex = 0; dex < res_list.size() - 1; dex++) {
	    one_term = (OneTerm)res_list.elementAt(dex);
	    one_term.markInternalStar(); }
	return; 
    }
   
    /**
       returns a BitSet representing possible initial characters
       for this RegExp.  Can't use a single char because RegExp might
       begin with a character set.
    */
    public BitSet getInitBits() {
	OneTerm init_term = (OneTerm)res_list.firstElement();

	return(init_term.getBitSet()); }


    public boolean match(String comp) {
	return(match(comp, 0, 0)); }

    public boolean match(String comp, int comp_dex, int res_dex) {
	int i, j, so_far = comp_dex;
	OneTerm one_term;
	boolean got_match;

	for (i = res_dex; i < res_list.size(); i++) {
	    one_term = (OneTerm)res_list.elementAt(i);
	    if (one_term.isLastStar()) { return true; }
	    if (one_term.isInternalStar()) {
		if (one_term.hasPlus()) {
		    if (so_far + 1 >= comp.length()) {
			return false; }
		    if (one_term.isLastTerm()) { return true; }
		    so_far += 1; }
		// for (j = so_far + 1; j < comp.length(); j++) {
		for (j = so_far; j < comp.length(); j++) {  
		    got_match = match(comp, j, i + 1);
		    if (got_match) { return true; } }
		return false; }
	    // next, check for terminal .+
	    if (one_term.hasPlus() && one_term.hasPeriod()) {
		if (so_far <= comp.length() - 2) {
		    return true; }		    
		else { return false; }}
	    so_far = one_term.match(comp, so_far);
	    if (so_far >= comp.length()) {
		if (one_term.isLastTerm()) { return true; }
		if (i == res_list.size() - 2) {
		    one_term = (OneTerm)res_list.elementAt(i + 1);
		    if (one_term.isLastStar()) {
			return true; } }
		return false; }
	    if (so_far == -1) {
		return false; }
	}
	return false; 
    }

    public String toString() {
	OneTerm one_term;
	String str = "";

	for (int i = 0; i < res_list.size(); i++) {
	    one_term = (OneTerm)res_list.elementAt(i);
	    str += one_term.toString(); }
	return str; }

    public String getOrig() { return original; }


    public void PrintToSystemErr() {
	OneTerm one_term;

	System.err.println("");
	for (int i = 0; i < res_list.size(); i++) {
	    one_term = (OneTerm)res_list.elementAt(i);
	    one_term.PrintToSystemErr();
	    System.err.print(",  "); } }
	    
}
