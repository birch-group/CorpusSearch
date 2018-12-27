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

/*
 */
package tag_list;

import java.util.*;
import basicinfo.*;

public class LexEntry {

    private String canonical;
    private int total;
    private Vector dividers, dash_tags, all_tags, dash_tags_list;
    private boolean has_tags;

    public LexEntry() {
	total = 0;}

    public LexEntry(String canon, Vector divide_list) {
	has_tags = false;
	all_tags = PipeList.split(canon, divide_list);
	splitUp(all_tags);
        total = 1;}

    public boolean hasTags() {
	return has_tags; }

    private void splitUp(Vector all_tags) {
	int i;
	String one_tag;
	Character chara = new Character('Q');

	this.canonical = (String)all_tags.firstElement();
	if (all_tags.size() == 1) {
	    has_tags = false; 
	    return; }
	has_tags = true;
	dash_tags_list = new Vector();
	thru_tags: for (i = 1; i < all_tags.size(); i++) {
	    one_tag = (String)all_tags.elementAt(i);
	    // if one_tag begins with a digit, throw it out.
	    if (chara.isDigit(one_tag.charAt(0))) {
		continue thru_tags; }
	    dash_tags = new Vector();
	    dash_tags.addElement(one_tag);
	    dash_tags_list.addElement(dash_tags); }
	return; } 

    public boolean belongs(LexEntry lax) {
	if ((lax.getCanonical()).equals(canonical)) {
	    this.addTags(lax);
	    return true; }
	return false; }

    public int tagsSize() {
	return (dash_tags_list.size()); }

    public Vector dashTagsAt(int dex) {
	return ((Vector)dash_tags_list.elementAt(dex)); }

    public Vector getDashTagsList() {
	return (this.dash_tags_list); }

    private void addTags(LexEntry lax) {
	int i, j;
	Vector other_tags, this_tags;
	String other_tag;

	if (!lax.hasTags()) { return; } 
	if (!this.hasTags()) {
	    this.dash_tags_list = lax.getDashTagsList(); 
	    this.has_tags = true;
	    return; }
	get_tags: for (i = 0; i < lax.tagsSize(); i++) {
	    other_tags = lax.dashTagsAt(i);
	    if (i >= this.tagsSize()) {
		this.dash_tags_list.addElement(other_tags);
		continue get_tags; }
	    this_tags = this.dashTagsAt(i);
	    for (j = 0; j < other_tags.size(); j++) {
		other_tag = (String)other_tags.elementAt(j);
		if (!containsTag(this_tags, other_tag)) {
		    this_tags.addElement(other_tag); } } }
	return; }

    public boolean containsTag(Vector some_tags, String one_tag) {
	int i;
	String old_tag;

	for (i = 0; i < some_tags.size(); i++) {
	    old_tag = (String)some_tags.elementAt(i);
	    if (old_tag.equals(one_tag)) {
		return true; } }
	return false; }

    public void setCanonical(String canon) {
	canonical = canon.toLowerCase();}

    public String getCanonical() {
	return canonical; }

    public void incrementTotal() {
	total += 1; }

    public int getTotal() {
	return total; }

    public int getASCIInum() {
        Character charlie = new Character('Q');
        int num;
        char init;  

	if (canonical.length() == 0) { return 0; }
        init = canonical.charAt(0);
        init = charlie.toUpperCase(init);
        num = (int)init;
        num -= 64;
        if (num < 1 || num > 26) {
	    num = 0; }
        return num; }

    /*
      returns true if this is alphabetically
      later than input LexEntry; false otherwise.
    */
    public boolean LaterEntry(LexEntry lurk) {
	if ((this.canonical).compareTo(lurk.getCanonical()) > 0) {
	    return true; }
	return false; }

    /*
      returns true if this is alphabetically
      later than input LexEntry; false otherwise.
    */
    public boolean MoreLaterEntry(LexEntry lurk) {
	//  if (this.total > lurk.getTotal()) {
	//  return true; }
	if ((this.canonical).compareTo(lurk.getCanonical()) > 0) {
	    return true; }
	return false; }
	
    public void Sort() {
	int i;
	Vector dash_tags;

	if (!this.has_tags) { return; }
	for (i = 0; i < dash_tags_list.size(); i++) {
	    dash_tags = (Vector)dash_tags_list.elementAt(i);
	    QuickSortTags(0, dash_tags.size() - 1, dash_tags); } }

    /*
      SplitBucket -- called by QuickSortTags.  Splits list into
      two parts, those less than List[pivot_loc] on the
      left, those greater on the right.  
      input -- lower -- lower index
      -- upper -- upper index
      -- pivot_loc -- location of pivot
      output -- int -- pivot_loc  -- new location of pivot
    */
    private int SplitTags (int lower, int upper, 
			   int pivot_loc, Vector dash_tags) {
	String pivot, one_entry;
	int i;

	try {
	    pivot = (String)dash_tags.elementAt(pivot_loc);
	    Swap(lower, pivot_loc, dash_tags);
	    pivot_loc = lower;
	    for (i = lower + 1; i <= upper; i++) {
		one_entry = (String)dash_tags.elementAt(i);
		if (pivot.compareTo(one_entry) > 0) {
		    pivot_loc += 1;
		    Swap(pivot_loc, i, dash_tags); } }
	    Swap(lower, pivot_loc, dash_tags); }
	catch (Exception e) {
	    System.err.println("SplitTags:  ");
	    System.err.println(e.getMessage());
	    e.printStackTrace(); }
	    //CorpusSearch.SearchExit(); }
	finally { return (pivot_loc); } }

    private void Swap(int i, int j, Vector dash_tags) {
	String temp;

	temp = (String)dash_tags.elementAt(i);
	dash_tags.setElementAt((String)dash_tags.elementAt(j), i);
	dash_tags.setElementAt(temp, j);
	return; }

    /*
      QuickSortTags -- quick sorts dash tags Vector.
      input -- lower -- lower index
      -- upper -- upper index
      output -- void.
    */
    private void QuickSortTags (int lower, int upper, Vector dash_tags) {
	Random chance = new Random();
	int pivot_loc, index, random_dex;

	if (upper > lower) {
	    random_dex = chance.nextInt(upper - lower); 
	    index = lower + random_dex;
	    pivot_loc = SplitTags(lower, upper, index, dash_tags);
	    QuickSortTags(lower, pivot_loc - 1, dash_tags);
	    QuickSortTags(pivot_loc + 1, upper, dash_tags); }
	return; }
			  
    public void PrintToSystemErr() {
	this.PrintToSystemErr(0, dash_tags_list.size()); }

    public void PrintToSystemErr(int start, int end) {
	String tag;
	int i, j;

	System.err.print(canonical);
	if (!has_tags) {
	    System.err.println("");
	    return; }
	if (start < 0) { start = 0; }
	if (end > dash_tags_list.size()) { end = dash_tags_list.size(); }
	for (i = start; i < end; i++) {
	    dash_tags = (Vector)dash_tags_list.elementAt(i);
	    System.err.print(":");
	    for (j = 0; j < dash_tags.size(); j++) {
		tag = (String)dash_tags.elementAt(j);
		System.err.print(tag);
		if (j != dash_tags.size() - 1) {
		    System.err.print("|"); }
  	    } }
	System.err.println("");
	return; }

} 
