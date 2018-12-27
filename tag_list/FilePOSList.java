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

public class FilePOSList {

    private Vector list;
    private LexEntryBucket A_bucket, B_bucket, C_bucket, D_bucket;
    private LexEntryBucket E_bucket, F_bucket, G_bucket, H_bucket;
    private LexEntryBucket I_bucket, J_bucket, K_bucket, L_bucket;
    private LexEntryBucket M_bucket, N_bucket, O_bucket, P_bucket;
    private LexEntryBucket Q_bucket, R_bucket, S_bucket, T_bucket;
    private LexEntryBucket U_bucket, V_bucket, W_bucket, X_bucket;
    private LexEntryBucket Y_bucket, Z_bucket, other_bucket;

    public FilePOSList() {
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

    public boolean isEmpty() {
	LexEntryBucket paris;
	for (int i = 0; i < list.size(); i++) {
	    paris = (LexEntryBucket)list.elementAt(i);
	    if (!paris.isEmpty()) {
		return false; } }
	return true; }

    public LexEntryBucket bucketAt(int i) {
	return(LexEntryBucket)list.elementAt(i); }

    public void addSentenceList(SentenceList sent) {
	Vector posl = sent.getPOSList();
	for (int i = 0; i < posl.size(); i++) {
	    this.addLexEntry((LexEntry)posl.elementAt(i)); } }
					  
    public void addLexEntry(LexEntry lex) {
	int dex;
	LexEntryBucket bucket;
	dex = lex.getASCIInum();
	bucket = (LexEntryBucket)list.elementAt(dex);
	bucket.AddLexEntry(lex); 
	return; }

    public void PrintToSystemErr() {
	this.PrintToSystemErr(0, list.size()); }

    public void PrintToSystemErr(int start, int end) {
	LexEntryBucket bucket;
	LexEntry enter;
	int i;

	if (start < 0) { start = 0; }
	if (end > list.size()) { end = list.size(); }
	System.err.println("FilePOSList:  ");
	for (i = start; i < end; i++) {
	    System.err.println("bucket:  ") ;
	    bucket = (LexEntryBucket)list.elementAt(i);
	    bucket.PrintToSystemErr(); } } 

} 
