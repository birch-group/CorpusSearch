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

public class POSTagList {

    private BitSet has_bucket;
    private Vector list;
    private Vector A_bucket, B_bucket, C_bucket, D_bucket;
    private Vector E_bucket, F_bucket, G_bucket, H_bucket;
    private Vector I_bucket, J_bucket, K_bucket, L_bucket;
    private Vector M_bucket, N_bucket, O_bucket, P_bucket;
    private Vector Q_bucket, R_bucket, S_bucket, T_bucket;
    private Vector U_bucket, V_bucket, W_bucket, X_bucket;
    private Vector Y_bucket, Z_bucket, other_bucket;

    public POSTagList() {
	this.Init(); }

    private void Init() {
	list = new Vector();
	list.addElement(other_bucket);
	list.addElement(A_bucket); list.addElement(B_bucket);
	list.addElement(C_bucket); list.addElement(D_bucket);
	list.addElement(E_bucket); list.addElement(F_bucket);
	list.addElement(G_bucket); list.addElement(H_bucket);
	list.addElement(I_bucket); list.addElement(J_bucket);
	list.addElement(K_bucket); list.addElement(L_bucket);
	list.addElement(M_bucket); list.addElement(N_bucket);
	list.addElement(O_bucket); list.addElement(P_bucket);
	list.addElement(Q_bucket); list.addElement(R_bucket);
	list.addElement(S_bucket); list.addElement(T_bucket);
	list.addElement(U_bucket); list.addElement(V_bucket);
	list.addElement(W_bucket); list.addElement(X_bucket);
	list.addElement(Y_bucket); list.addElement(Z_bucket);
	has_bucket = new BitSet(list.size()); }

    public int size() {
	return list.size(); }

    public boolean legitTag(String taggle, Vector divide) {
	return hasMatch(taggle, divide); }

    public Vector bucketAt(int i) {
	return (Vector)list.elementAt(i); }

    public void addOneTag(OneTag onet) {
	int num;
	boolean has;
	Vector new_buck;
	String canon;

	canon = onet.getCanonical();
	num = getASCIIforMatch(canon);
	if (has_bucket.get(num)) {
	    ((Vector)list.elementAt(num)).addElement(onet); 
	    return; }
	new_buck = new Vector();
	new_buck.addElement(onet);
	list.setElementAt(new_buck, num);
	has_bucket.set(num);
    }

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

    public boolean hasMatch(String str, Vector divide) {
	int num;

	if (str.equals("")) {
	    return false; }
	num = getASCIIforMatch(str);
	if (has_bucket.get(num)) {
	    return isInBucket(str, num, divide); }
	return false; }

    private boolean isInBucket(String str, int num, Vector divide) {
	OneTag onet;
	Vector buck = (Vector)list.elementAt(num); 

	for (int i = 0; i < buck.size(); i++) {
	    onet = (OneTag)buck.elementAt(i);
	    if (onet.isMatch(str, divide)) {
		return true; } }
	return false; }

    public void PrintToSystemErr() {
	Vector buck;
	int i, j;
	OneTag onet;
	String one_str;

	for (i = 0; i < has_bucket.size(); i++) {
	    if (has_bucket.get(i)) {
		buck = (Vector)list.elementAt(i);
		for (j = 0; j < buck.size(); j++) {
		    onet = (OneTag)buck.elementAt(j);
		    one_str = onet.toString();
		    System.err.println(one_str); } } } 
	return; }

} 






