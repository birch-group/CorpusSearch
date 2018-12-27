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
import java.util.*;
import java.io.*;
import command.*;
import basicinfo.*;
import CSParse.*;
import syntree.*;
import print.*;
import io.*;

public class TreeBuffer {

    Vector master_buffer;
    BitSet fits_query;
    int showing_dex;

    public TreeBuffer() {
	master_buffer = new Vector();
	showing_dex = -1;
        fits_query = new BitSet(0); }

    public boolean isEmpty() {
	return (master_buffer.isEmpty()); }

    public int size() {
	return (master_buffer.size()); }

    public int getShowDex() {
	return (showing_dex); }

    public void setShowDex(int new_dex) {
	showing_dex = new_dex; }
					 

    public ChangeGraphicTree changeGraphicTreeAt(int i) {
	return ((ChangeGraphicTree)master_buffer.elementAt(i)); }

    public ChangeTree changeTreeAt(int i) {
	ChangeTree ct;
	ChangeGraphicTree cgt;

	cgt = this.changeGraphicTreeAt(i);
	ct = cgt.getChangeTree();
	return ct; }

    public ChangeGraphicTree getCurrTree() {
	ChangeGraphicTree curr = new ChangeGraphicTree();

	if (this.isEmpty()) {
	    return (new ChangeGraphicTree()); }
	try {
	    curr = this.changeGraphicTreeAt(showing_dex);
	}
	catch (Exception e) {
	    System.err.println("in TreeBuffer: getCurrTree:  ");
	    e.printStackTrace(); 
	    System.exit(1); }
	finally { return curr; }
    }

    public void rmPrev() {
	if (showing_dex < 1) { return; }
	this.master_buffer.removeElementAt(showing_dex - 1);
        showing_dex -= 1; }

    public void rmFoll() {
	if (showing_dex + 1 >= master_buffer.size()) { return; }
	this.master_buffer.removeElementAt(showing_dex + 1); }
 
    public void showLast () {
	showing_dex = master_buffer.size() - 1; }

    // add to tree buffer without changing showing_dex.
    // used by Save.
    public void blindAddToTreeBuffer(ChangeGraphicTree cgt) {
	ChangeGraphicTree store_tree;

	store_tree = cgt.copy();
	master_buffer.add(store_tree); }

    public void addToTreeBuffer(ChangeGraphicTree cgt) {
	ChangeGraphicTree store_tree;

	store_tree = cgt.copy();
	master_buffer.add(store_tree); 
        showing_dex += 1; }

    public void pushForward (ChangeGraphicTree cgt) {
	try {
	    master_buffer.insertElementAt(cgt, showing_dex + 1); }
	catch (Exception e) {
	    System.err.println("in TreeBuffer.pushForward:  ");
	    e.printStackTrace(); }
	finally { return; } }

    public boolean setNext() {
	if (CorpusDraw.hasQuery()) {
	    return (setNextHasQuery()); }
	return(setNextNoQuery()); }

    public boolean setNextNoQuery() {
	if (this.gotForward()) {
	    showing_dex += 1;
	    return true; }
	return false; }

    public boolean setNextHasQuery() {
	int next_set;

	next_set = fits_query.nextSetBit(showing_dex + 1);
	if (next_set == -1) {
	    return false; }
	showing_dex = next_set;
	return true; }

    public boolean setPrevious() {
	if (CorpusDraw.hasQuery()) {
	    return (setPreviousHasQuery()); }
	return (setPreviousNoQuery()); }

    public boolean setPreviousNoQuery() {
	if (this.gotPrevious()) {
	    showing_dex -= 1; 
	    return true; }
        return false; }

    public boolean setPreviousHasQuery() {
	int prev_set;

	//this.printToSystemErr();
	prev_set = this.prevSetBit(fits_query, showing_dex - 1);
	if (prev_set == -1) {
	    return false; }
	showing_dex = prev_set;
	return true; }

    public int prevSetBit(BitSet some_bits, int index) {

	for (int i = index; i >= 0; i--) {
	    if (fits_query.get(i)) {
		return i; } }
	return 0; }

    public ChangeGraphicTree previousTree() {
	//	this.printToSystemErr();
	if (this.gotPrevious()) {
	    return (this.changeGraphicTreeAt(showing_dex - 1)); }
	else {
	    return (new ChangeGraphicTree()); } }

    public ChangeGraphicTree followingTree() {
	if (this.gotForward()) {
	    return (this.changeGraphicTreeAt(showing_dex + 1)); }
	else {
	    return (new ChangeGraphicTree(DrawLoop.readNextTree())); }}

    public boolean gotPrevious() {
	if (showing_dex > 0) {
	    return true; }
	return false; }

    public boolean gotForward() {
	if (showing_dex < master_buffer.size() - 1) {
	    return true; }
	return false; }

    public boolean goToPrevious(int to_where) {
	ChangeGraphicTree cgt;
	
	if (to_where - 1 >= master_buffer.size() || to_where - 1 < 0) {
	    return false; }
	showing_dex = to_where - 1 ; 
        return true; }
	    
    
    public void blindAddToTreeBuffer(ChangeGraphicTree cgt,
				     boolean had_result) {
	this.blindAddToTreeBuffer(cgt);
	this.addToFitsQuery(had_result); }

    public void addToTreeBuffer(ChangeGraphicTree cgt, 
				boolean had_result) {
	this.addToTreeBuffer(cgt);
        this.addToFitsQuery(had_result); }

    public void addToFitsQuery(boolean had_result) {
	BitSet bigger_bits;

	bigger_bits = new BitSet(master_buffer.size());
	if (had_result) {
	    bigger_bits.set(master_buffer.size() - 1); }
	for (int i = 0; i < fits_query.size(); i++) {
	    if (fits_query.get(i)) {
	    bigger_bits.set(i); } }
	fits_query = bigger_bits; }


    public void printToSystemErr() {
	printToSystemErr(0, this.size()); }

    public void printToSystemErr(int start, int end) {
	ChangeGraphicTree my_tree;
	int i;

	if (this.isEmpty()) {
	    System.err.println("tree buffer is empty.");
	    return; }
	if (start < 0) { start = 0; }
	if (end > this.size()) { end = this.size(); }
	for (i = start; i < end; i++) {
	    System.err.print(i + ".)  ");
	    my_tree = this.changeGraphicTreeAt(i);
	    //my_tree.PrintToSystemErr();
	    System.err.println("sentence num:  " + my_tree.getNumID()); }
    }

}

    






