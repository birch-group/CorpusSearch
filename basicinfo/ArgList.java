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
 * ArgList is bucket-sorted by initial character to save time.  
 * There is a BitSet, the same size as the list, which records 
 * whether each bucket is empty or non-empty.
 * 
 */

public class ArgList {

    private boolean has_METAROOT, has_ROOT;
    private BitSet has_bucket;
    private Vector A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T;
    private Vector U, V, W, X, Y, Z, star, other, list;
    private Vector a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t;
    private Vector u, v, w, x, y, z;
    public static int STAR_INDEX = 53, BIT_SET_SIZE = 54;

    public ArgList() {
	this.Init(); }

    public ArgList(Vector pop) {
	this.Init();
	this.addPipeList(pop); 
	//this.PrintToSystemErr(); 
 	return; }    

     public ArgList(String pipe_str) {
 	Vector pop;
 	this.Init();
 	pop = PipeList.MakeList(pipe_str);
 	this.addPipeList(pop); 
	//this.PrintToSystemErr(); 
         return; }

     private void Init() {
 	list = new Vector();
 	list.addElement(other);
 	list.addElement(A); list.addElement(B); list.addElement(C);
 	list.addElement(D); list.addElement(E); list.addElement(F);
 	list.addElement(G); list.addElement(H); list.addElement(I);
 	list.addElement(J); list.addElement(K); list.addElement(L);
 	list.addElement(M); list.addElement(N); list.addElement(O);
 	list.addElement(P); list.addElement(Q); list.addElement(R);
 	list.addElement(S); list.addElement(T); list.addElement(U);
 	list.addElement(V); list.addElement(W); list.addElement(X);
 	list.addElement(Y); list.addElement(Z);
 	list.addElement(a); list.addElement(b); list.addElement(c);
 	list.addElement(d); list.addElement(e); list.addElement(f);
	list.addElement(g); list.addElement(h); list.addElement(i);
 	list.addElement(j); list.addElement(k); list.addElement(l);
 	list.addElement(m); list.addElement(n); list.addElement(o);
 	list.addElement(p); list.addElement(q); list.addElement(r);
 	list.addElement(s); list.addElement(t); list.addElement(u);
 	list.addElement(v); list.addElement(w); list.addElement(x);
 	list.addElement(y); list.addElement(z);
 	list.addElement(star); // must be at STAR_INDEX.
 	has_bucket = new BitSet(list.size()); 
 	has_METAROOT = false;
 	has_ROOT = false; }

     public int size() {
 	return list.size(); } 

     public boolean isEmpty() {
 	for (int i = 0; i < has_bucket.size(); i++) {
 	    if (has_bucket.get(i)) {
 		return false; } }
 	return true; }

     public Vector bucketAt(int i) {
 	return (Vector)list.elementAt(i); }

     public boolean fullBucket(int i) {
 	return (has_bucket.get(i)); }

     public Vector toVector() {
 	int i, j;
 	Vector argo = new Vector(), buck;
 	RegExpStr arse;

 	for (i = 0; i < list.size(); i++) {
 	    if (has_bucket.get(i)) {
 		buck = (Vector)list.elementAt(i);
 		for (j = 0; j < buck.size(); j++) {
 		    arse = (RegExpStr)buck.elementAt(j);
 		    argo.addElement(arse); }
 	    } }
 	return argo; }

     private void addPipeList(Vector pop) {
 	String str;
 	RegExpStr rex;

 	for (int i = 0; i < pop.size(); i++) {
 	    str = (String)pop.elementAt(i);
 	    rex = new RegExpStr(str);
 	    //rex.PrintToSystemErr();
 	    this.addToBucket(rex);
 	}
     }

     public void addToBucket (String str) {
 	RegExpStr rex = new RegExpStr(str);
 	addToBucket(rex);
 	return; }
    
     public void addToBucket(RegExpStr res) {
 	Vector new_buck;
 	BitSet init_bits;
 	int i, num;

 	if (res.isMetaRoot()) {
 	    has_METAROOT = true;
 	    return; }
 	if (res.isRoot()) {
 	    has_ROOT = true; 
 	    return; }
 	init_bits = res.getInitBits();
 	for (i = 0; i < init_bits.size(); i++) {
 	    if (init_bits.get(i)) {
 		if (has_bucket.get(i)) {
 		    ((Vector)list.elementAt(i)).addElement(res); 
 		    continue; }
 		else {
 		    new_buck = new Vector();
 		    new_buck.addElement(res);
 		    list.setElementAt(new_buck, i);
 		    has_bucket.set(i); } } }
 	return;
     }

     public boolean hasMetaroot() {
 	return has_METAROOT; }

     public boolean hasRoot() {
 	return has_ROOT; }

     public boolean hasMatch(Node nodorous) {
 	return (hasMatch(nodorous.getLabel())); }

     public boolean hasMatch(Node nodorous, int i) {
 	return (hasMatch(nodorous.getLabel(), i)); }
	
     public boolean hasMatch(String str) {
 	int num;

 	if (str.equals("")) {
 	    return false; }
 	num = getBucketDex(str);
 	// check star bucket.
 	if (has_bucket.get(STAR_INDEX)) {
 	    if (isInBucket(str, STAR_INDEX)) {
 		return true; }}
 	if (has_bucket.get(num)) {
 	    return isInBucket(str, num); }
 	return false; }

     public boolean hasMatch (String str, int i) {
	
 	if (str.equals("")) { return false; }
 	if (has_bucket.get(STAR_INDEX)) {
 	    if (isInBucket(str, STAR_INDEX)) {
 		return true; }}
 	if (has_bucket.get(i)) {
 	    return isInBucket(str, i); }
 	return false; }
	

     private boolean isInBucket(String str, int num) {
 	RegExpStr buck_res;
 	Vector buck = (Vector)list.elementAt(num); 

 	for (int i = 0; i < buck.size(); i++) {
 	    buck_res = (RegExpStr)buck.elementAt(i);
	    if (buck_res.match(str)) {
		return true; } }
	return false; }

    public int getBucketDex(String str) {
	return(getBucketDex(str.charAt(0))); }

    /** returns 1 through 26 for 'A' through 'Z'
               27 through 52 for 'a' through 'z'
	       53 for '*' or '.'
	       0 for other
    */
    public int getBucketDex(char one_char) {
	if (one_char == '*' || one_char == '.') { return STAR_INDEX; }
	if (one_char >= 'A' && one_char <= 'Z') {
	    return ((int)one_char - 64); }
	if (one_char >= 'a' && one_char <= 'z') {
	    return((int)one_char - 70); }
	return 0;
    }

    /** 
     */
    public char getCharForDex(int dex) {
	if (dex == 0) { return('%'); }
	if (dex == STAR_INDEX) { return('*'); }
	if (dex >= 1 && dex <= 26) {
	    return ((char)(dex + 64)); }
	if (dex >= 27 && dex <= 52) {
	    return ((char)(dex + 70)); }
	return('@'); 
    }

    /** returns number corresponding to ASCII number
     *  of initial letter of text.  
     *  Here, "*" is the wild card.
     *  Used to bucket-sort elements of PipeList.
    */
    public int getASCIInum(String text_str) {
        Character charlie = new Character('Q');
        int num;
        char init;

        init = text_str.charAt(0);
	if (init == '*' || init == '.') {
	    return (STAR_INDEX); }
        init = charlie.toUpperCase(init);
	if (init < 'A' || init > 'Z') {
	    num = 0; }
	else {
	    num = (int)init - 64; }
        return num; }


    /**
     *  There are two versions of getting the ASCII number 
     *  because the character "*" means different things in different 
     * places.  When found in the corpus, "*" is a character like any other.  
     * When found in a query, "*" is the wildcard.
     *  This version of "getASCII" is used for corpus elements, 
     * not query elements.
    */
    public int getASCIIforMatch(String text_str) {
        Character charlie = new Character('Q');
        int num;
        char init;

        init = text_str.charAt(0);
        init = charlie.toUpperCase(init);
	if (init < 'A' || init > 'Z') {
	    num = 0; }
	else {
	    num = (int)init - 64; }
        return num; }

    public String toPipeList() {
	Vector buck;
	String piper = "";
	int i, j;

	for (i = 0; i < has_bucket.size(); i++) {
	    if (has_bucket.get(i)) {
		buck = (Vector)list.elementAt(i);
		for (j = 0; j < buck.size(); j++) {
		    piper += ((RegExpStr)buck.elementAt(j)).toString();
		    if (j < buck.size() - 1) {
			piper += ("|"); } } } }
	return piper; }

    public String toString() {
	Vector buck;
	int i, j;
	String str = "";

	for (i = 0; i < has_bucket.size(); i++) {
	    if (has_bucket.get(i)) {
		buck = (Vector)list.elementAt(i);
		str += i + ".)";
		for (j = 0; j < buck.size(); j++) {
		    str += (((RegExpStr)buck.
				      elementAt(j)).toString());
		    if (j < buck.size() - 1) {
			str += "|"; } } } }
	str += "  ";
	return str; }

    public void PrintToSystemErr() {
	Vector buck;
	int i, j;

	System.err.println("has_Root:  " + has_ROOT);
	System.err.println("has_Metaroot:  " + has_METAROOT);
	for (i = 0; i < has_bucket.size(); i++) {
	    if (has_bucket.get(i)) {
		buck = (Vector)list.elementAt(i);
		System.err.print(i + ".)  ");
		for (j = 0; j < buck.size(); j++) {
		    System.err.print(((RegExpStr)buck.
				      elementAt(j)).toString());
		    if (j < buck.size() - 1) {
			System.err.print("|"); } }

		System.err.println(""); } }
	return; }

} 






