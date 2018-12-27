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
  the final lexicon list.  A list of buckets, each
  containing a list of LexEntry objects.
*/
package label_lexicon;

import java.util.*;
import syntree.*;
import basicinfo.*;

public class UberList {

    private Vector list;
    private LexEntryBucket A_bucket, B_bucket, C_bucket, D_bucket;
    private LexEntryBucket E_bucket, F_bucket, G_bucket, H_bucket;
    private LexEntryBucket I_bucket, J_bucket, K_bucket, L_bucket;
    private LexEntryBucket M_bucket, N_bucket, O_bucket, P_bucket;
    private LexEntryBucket Q_bucket, R_bucket, S_bucket, T_bucket;
    private LexEntryBucket U_bucket, V_bucket, W_bucket, X_bucket;
    private LexEntryBucket Y_bucket, Z_bucket, other_bucket;

    public UberList() {
	list = new Vector();
	other_bucket = new LexEntryBucket(); list.addElement(other_bucket);
	A_bucket = new LexEntryBucket(); list.addElement(A_bucket);
	B_bucket = new LexEntryBucket(); list.addElement(B_bucket);
	C_bucket = new LexEntryBucket(); list.addElement(C_bucket);
	D_bucket = new LexEntryBucket(); list.addElement(D_bucket);
	E_bucket = new LexEntryBucket(); list.addElement(E_bucket);
	F_bucket = new LexEntryBucket(); list.addElement(F_bucket);
	G_bucket = new LexEntryBucket(); list.addElement(G_bucket);
	H_bucket = new LexEntryBucket(); list.addElement(H_bucket);
	I_bucket = new LexEntryBucket(); list.addElement(I_bucket);
	J_bucket = new LexEntryBucket(); list.addElement(J_bucket);
	K_bucket = new LexEntryBucket(); list.addElement(K_bucket);
	L_bucket = new LexEntryBucket(); list.addElement(L_bucket);
	M_bucket = new LexEntryBucket(); list.addElement(M_bucket);
	N_bucket = new LexEntryBucket(); list.addElement(N_bucket);
	O_bucket = new LexEntryBucket(); list.addElement(O_bucket);
	P_bucket = new LexEntryBucket(); list.addElement(P_bucket);
	Q_bucket = new LexEntryBucket(); list.addElement(Q_bucket);
	R_bucket = new LexEntryBucket(); list.addElement(R_bucket);
	S_bucket = new LexEntryBucket(); list.addElement(S_bucket);
	T_bucket = new LexEntryBucket(); list.addElement(T_bucket);
	U_bucket = new LexEntryBucket(); list.addElement(U_bucket);
	V_bucket = new LexEntryBucket(); list.addElement(V_bucket);
	W_bucket = new LexEntryBucket(); list.addElement(W_bucket);
	X_bucket = new LexEntryBucket(); list.addElement(X_bucket);
	Y_bucket = new LexEntryBucket(); list.addElement(Y_bucket);
	Z_bucket = new LexEntryBucket(); list.addElement(Z_bucket); }

    public int size() {
	return list.size(); }

    public LexEntryBucket bucketAt(int i) {
        return(LexEntryBucket)list.elementAt(i); }

    public void Sort() {
	LexEntryBucket pail;
	for (int i = 0; i < this.size(); i++) {
	    pail = this.bucketAt(i);
	    pail.Sort(); }
	return; }

    public void addFileList(FileList folly) {
	LexEntryBucket lexbucket, filebucket;
	LexEntry lurk, fuzz;
	int i = -3, j = -3, k = -3, start_here = 0;

	try {
	    folly_loop: for (i = 0; i < folly.size(); i++) {
		lexbucket = this.bucketAt(i);
		filebucket = folly.bucketAt(i);
		if (lexbucket.isEmpty()) {
		    if (filebucket.isEmpty()) {
			continue folly_loop; }
		    lexbucket.AddLexEntry(filebucket.LexEntryAt(0)); 
		    start_here = 1;}
		for (j = start_here; j < filebucket.size(); j++) {
		    fuzz = filebucket.LexEntryAt(j);
		    lexx_loop: for (k = 0; k < lexbucket.size(); k++) {
			lurk = lexbucket.LexEntryAt(k);
			if (lurk.belongs(fuzz)) {
			    lurk.incrementTotal();
			    break lexx_loop; }
			if (k == lexbucket.size() - 1) {
			    lexbucket.AddLexEntry(fuzz); 
			    break lexx_loop;} } } }
	} 
	catch (Exception e) {
	    System.err.println("in UberList.java: FileList:  ");
	    System.err.println("i, j, k:  " + i + ", " + j + ", " + k);
	    e.printStackTrace(); }
	finally { return; } }

    public void PrintToSystemErr() {
	LexEntryBucket bucket;
	LexEntry lurks;
	System.err.println("UberList:  ");
	for (int i = 0; i < list.size(); i ++) {
	    bucket = (LexEntryBucket)list.elementAt(i);
	    bucket.PrintToSystemErr(); }
	return; }
} 
