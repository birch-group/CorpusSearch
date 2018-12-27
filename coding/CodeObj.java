/*
  
 */
package coding;

import java.io.*;
import java.util.*;

import syntree.*;
import basicinfo.*;

public class CodeObj {

    private String code_str;
    private Vector code_vec;
    private Node bound;

    public CodeObj () {
        this.code_str = new String("");
        this.code_vec = new Vector(); }

    public CodeObj(String curr_str) {
	this.code_str = curr_str;
	this.code_vec = MakeCodList(curr_str); }

    public CodeObj(Node in_bound) {
	this.code_str = new String("");
	this.code_vec = new Vector();
	this.bound = in_bound; }

    public CodeObj(Node in_bound, String curr_str) {
	this.code_str = curr_str;
        this.code_vec = MakeCodList(curr_str);
	this.bound = in_bound; }

    public String getString () {
	this.code_str = this.MakeCodStr(this.code_vec);
	return this.code_str; }

    public void setBound(Node in_bound) {
	this.bound = in_bound; }

    public Node getBound() {
	return this.bound; }
    
    public String getColumn(int i) {
	return ((String)code_vec.elementAt(i)); }

    public void setColumn(String new_str, int i) {
        // ensure that code_vec has an element for every column index.
	while (i > (code_vec.size() - 1)) {
	    code_vec.addElement("_"); }
	giveWarning(new_str, i);
	code_vec.setElementAt(new_str, i);
	return; }

    public void setDefaultColumn(int i) {
	this.setColumn("_", i); 
	return; }

    private void giveWarning(String new_str, int i) {  // prints warning at first instance of column overwrite. 
	String prev; 
	int j, k;
	Integer done, j_Int;

	prev = (String)code_vec.elementAt(i);
	if (!prev.equals("_")) {
	    j = i + 1; // because the user numbers columns starting at 1 instead of 0.
	    j_Int = new Integer(j);
	    for (k = 0; k < CodingLoop.warned.size(); k++) {
		done = (Integer)CodingLoop.warned.elementAt(k);
		if (done.equals(j_Int)) { return; }}
	    System.err.println("");
	    System.err.print("(first instance) WARNING!  will overwrite CODING column " + j + ":  ");
	    System.err.println(prev + " with " + new_str);
	    System.err.println("");
	    getPerm(j);
	    CodingLoop.warned.addElement(j_Int); }
	return; }

    public void getPerm (int j) { // gets user's permission to overwrite a coding column.
	String response, prompt;

	prompt = "Continue coding with overwrite of column " + j + "?  (Y/N):  ";
	response = InFace.PromptUser(prompt);
	System.err.println("");
	if (response.startsWith("Y") || response.startsWith("y")) { return; }
	System.err.println("Search aborted at user request.");
	System.err.println("");
	Goodbye.SearchExit(); }

    /*
      makes coding string from coding vector.
    */
    public String MakeCodStr (Vector coding_vec) {
	int i;
	String codfish = "";

	for (i = 0; i < coding_vec.size(); i++) {
	    codfish += (String)coding_vec.elementAt(i);
	    if (i < coding_vec.size() - 1) {
		codfish += ":"; } }
	return codfish; }

    /*
        MakeCodList -- makes list of columns from coding string.
                Called by Coding.
                sample input:  m:r:s
                sample output:  Vector containing:
                        0.) m
                        1.) r
                        2.) s
        input -- list of columns in string form
        output -- same list of arguments in vector form.
    */
    public Vector MakeCodList(String Columns) {
        Vector NewList = new Vector();
        StringBuffer one_buff = new StringBuffer("");
        String one_item = "";
        Character chuff = new Character('Q');
        int i;

        for (i = 0; i < Columns.length(); i ++) {
	    if (!(Columns.charAt(i) == ':')) {
		one_buff.append(Columns.charAt(i)); }
	    if ((Columns.charAt(i) == ':') ||
                (i == (Columns.length() - 1))) {
		one_item = one_buff.toString();
		NewList.addElement(one_item);
		one_buff = new StringBuffer(""); }
        }
        return NewList; }

    public void PrintToSystemErr() {
	try {
	    bound.PrintToSystemErr();}
	catch (Exception e) {}
	finally {
	    System.err.println(":  " + this.getString());
	    System.err.println(""); }}

}






