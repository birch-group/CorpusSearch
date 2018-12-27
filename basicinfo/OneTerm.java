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
 * OneTerm represents one term in a regular expression.  For instance, the
 * regular expression 
 *    k[iy]+ng[e]*
 * has 5 terms, as follows:
 * k
 * [iy]+
 * n
 * g
 * [e]*
 */


public class OneTerm extends RegExpStr {

    boolean has_char_set, has_plus, has_period, last_term;
    boolean has_question_mark, plain_char, negate, has_star;
    boolean is_in_star, is_last_star;
    int start_dex, end_dex;
    String orig_term, orig_str;
    char my_char;
    BitSet my_char_set;
    Vector zero_list;

    public OneTerm() { Init(); }

    public OneTerm(String orig_string) {
	Init();
	doString(orig_string); }

    public OneTerm(String orig_string, int start) {
	Init();
	doString(orig_string, start); }

    private void Init() {
	has_char_set = false;
	has_star = false;
	has_plus = false;
	has_period = false;
	plain_char = true;
	is_in_star = false;
	is_last_star = false;
	negate = false;
	my_char = 'Q';
        orig_term = "";
        orig_str = ""; 
        last_term = false; }

    public int getEndDex() { return end_dex; }

    public boolean hasStar() { return has_star; }

    public boolean hasPlus() { return has_plus; }

    public boolean hasPeriod() { return has_period; }

    public void markAsLast() { 
	last_term = true;
        // also, mark is_last_star.
        if (has_star && !has_period && !plain_char && !has_char_set) {
	    is_last_star = true; } }

    public boolean isLastTerm() { return last_term; }

    public boolean isLastStar() { return is_last_star; }


    public void markInternalStar() {
	if (this.last_term) { return; }
	if (this.has_period && this.has_plus) {
	    is_in_star = true; 
	    return; }
	if (this.has_star && !this.has_char_set) {
	    is_in_star = true;
	    return; }
	return; }

    public boolean isInternalStar() { return is_in_star; }

    public BitSet getBitSet() {
	int i;
	BitSet my_bits;

	//this.markInternalStar();
	if (plain_char) {
	    my_bits = new BitSet(BIT_SET_SIZE);
	    i = getBucketDex(my_char);
	    my_bits.set(i);
	    return my_bits; }
	if (has_char_set) {
	    return my_char_set; }
	if (is_in_star || has_period) {
	    my_bits = new BitSet(BIT_SET_SIZE);
	    for (i = 0; i < BIT_SET_SIZE; i++) {
		my_bits.set(i); }
	    return my_bits; }
	// TEMPORARY HACK.
	System.out.println("WARNING:  in OneTerm.getBitSet:  temporary hack:");
	this.PrintToSystemErr();
	my_bits = new BitSet(BIT_SET_SIZE);
	return my_bits; }


    private void doString(String orig_string) {
	doString(orig_string, 0); }

    private void doString(String orig_string, int start) {

	start_dex = start;
	end_dex = start + 1;
	orig_str = orig_string;
 
	if (orig_string.equals("*")) {
	    System.err.print("WARNING!  Found \"*\" argument.  ");
	    System.err.println("Will interpret as \".*\".");
            has_period = true; }
	char one_char = orig_str.charAt(start);
	if (one_char == '[') {
	    readCharSet(orig_str, start); return; }
	if (one_char == '.') {
	    readPeriod(orig_str, start); return; }
	if (one_char == '*') {
	    readStar(orig_str, start); return; }
	if (one_char == '\\') {
	    readBackSlash(orig_str, start); return; }
	my_char = one_char; return; }

    private void readPeriod(String orig_string, int start) {
	has_period = true;
	plain_char = false;
	checkStarPlus(orig_string, start);
	return; }

    private void readStar(String orig_string, int start) {
	has_star = true;
	plain_char = false;
	return; }

    private void readBackSlash(String orig_string, int start) {
	if (start + 1 >= orig_string.length()) {
	    backSlashError(orig_string); }
	my_char = orig_string.charAt(start + 1);
	end_dex = start + 2;
	plain_char = true;
	return; }

    private void readCharSet(String orig_string, int start) {
	int i, dex;
	char next_char;
	boolean first_zero = true;

	has_char_set = true;
	plain_char = false;
	my_char_set = new BitSet(BIT_SET_SIZE);
	if (start + 1 > orig_string.length()) {
	    return; }
	if (orig_string.charAt(start + 1) == '^') {
	    readNegCharSet(orig_string, start + 2); 
	    return; }
	for (i = start + 1; i <= orig_string.length(); i++) {
	    if (i == orig_string.length()) { bracketError(orig_string); }
	    next_char = orig_string.charAt(i);
	    if (next_char == ']') {
		checkStarPlus(orig_string, i);
		return; }
	    dex = getBucketDex(next_char);
	    my_char_set.set(dex);
	    if (dex == 0) {
		if (first_zero) {
		    zero_list = new Vector(); 
		    first_zero = false; }
		zero_list.addElement(new Character(next_char)); }
	} 
	return; }

    private void readNegCharSet(String orig_string, int start) {
	int i, dex;
	char next_char;
	boolean first_zero = true;

	has_char_set = true;
	plain_char = false;
	my_char_set = new BitSet(BIT_SET_SIZE);
	// first, set entire BitSet to true.
	for (i = start + 1; i < BIT_SET_SIZE; i++) {
	    my_char_set.set(i); }
	for (i = start + 1; i <= orig_string.length(); i++) {
	    if (i == orig_string.length()) { bracketError(orig_string); }
	    next_char = orig_string.charAt(i);
	    if (next_char == ']') {
		checkStarPlus(orig_string, i);
		return; }
	    dex = getBucketDex(next_char);
	    my_char_set.clear(dex);
	    if (dex == 0) {
		if (first_zero) {
		    zero_list = new Vector(); 
		    first_zero = false; }
		zero_list.addElement(new Character(next_char)); }
	} 
	return; }

    private void checkStarPlus(String orig_string, int i) {
	char after_next_char;

	if (i + 1 >= orig_string.length()) { 
	    end_dex = i + 1; return; }
	after_next_char = orig_string.charAt(i + 1);
	if (after_next_char == '+') {
	    has_plus = true;
	    end_dex = i + 2; return; }
	if (after_next_char == '*') {
	    has_star = true;
	    end_dex = i + 2; return; }
	if (after_next_char == '?') {
	    has_question_mark = true;
	    end_dex = i + 2; return; }
	end_dex = i + 1; return; }

    /** input: String to match, int index to start attempted match
	output:  if current match was successful, 
	    int index of String to begin next attempted match.
	    otherwise, -1 to indicate no successful match.
    */
    public int match(String comp, int start) {
	int i, bucket_dex;
	char comp_char;

	if (plain_char) {
	    if (comp.charAt(start) == my_char) {
		return (start + 1); }
	    else { return (-1); } }
	if (has_char_set) {
	    if (has_star) {
		if (start >= comp.length()) {
		    return (start + 1); }
		for (i = start; i < comp.length(); i++) {
		    comp_char = comp.charAt(i);
		    if (!charInBitSet(comp_char)) {
			return(i); } }
		return (comp.length() + 1); }
	    if (has_plus) {
		if (start >= comp.length()) { return (-1); }
		for (i = start; i < comp.length(); i++) {
		    comp_char = comp.charAt(i);
		    if (!charInBitSet(comp_char)) {
			if (i == start) { return (-1); }
			return (i); } }
		return (comp.length() + 1); }
	    comp_char = comp.charAt(start);
	    if (charInBitSet(comp_char)) {
		return (start + 1); }
	    else { return (-1); } }
	if (has_star) {
	    if (last_term) { return (comp.length() + 1); }
	    else { return (-1); } }
	if (has_period) {
	    return (start + 1); }
	return (-1); 
    }

    private boolean charInBitSet (char comp_char) {
	int bucket_dex;

	bucket_dex = getBucketDex(comp_char);
	if (!my_char_set.get(bucket_dex)) {
	    return false; }
	if (bucket_dex == 0) {
	    return(charInZeroList(comp_char)); }
	return true; }

    private boolean charInZeroList (char comp_char) {
	int i;
	Character zero_char;

 	for (i = 0; i < zero_list.size(); i++) {
 	    zero_char = (Character)zero_list.elementAt(i);
 	    if (zero_char.charValue() == comp_char) {
		return true; } }
	return false; }

    private void bracketError(String orig_string) {

	String message = "ERROR! unclosed [ in string: " + orig_string;
	System.err.println(message);
	System.exit(1); }

    private void backSlashError(String orig_string) {
	String message = "ERROR! backslash at end of string:  " + orig_string;
	System.err.println(message); 
	System.exit(1); }

    public String toString() {
	int i;
	String str = "";
	
	if (plain_char) { 
	    if (my_char == '*') {
		str += '\\'; }
	    str += my_char; return str; }
	if (has_char_set) {
	    str += "[";
	    for (i = 0; i < my_char_set.size(); i++) {
		if (my_char_set.get(i)) {
		    str += getCharForDex(i); } }
	    str += "]";
	    if (has_star) {
		str += "*"; }
	    if (has_plus) {
		str += "+"; }
	    if (has_question_mark) {
	        str += "?"; }
	    return str; }
	if (has_period) {
	    str += "."; }
	if (has_star) {
	    if (is_in_star) {
		str += "in*"; }
	    else {
		if (is_last_star) {
		    str += "last*"; }
		else {
		    str += "*"; } } }
	if (has_question_mark) {
	    str += "?"; }
	return str; }

    public void PrintToSystemErr() {
	System.err.println("has_period:  " + this.has_period);
	System.err.println(this.toString()); }
		


}
