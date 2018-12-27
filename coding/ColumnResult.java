package coding;

import java.io.*;
import java.util.*;
import search_result.*;
import syntree.*;

/**
 * A ColumnResult is a list of SubColumnResult objects.
 */
public class ColumnResult {

    private Vector sub_cols_vec;
    private int col_num = -3;

    public ColumnResult() {
	this.sub_cols_vec = new Vector();
	this.col_num = -1; }

    public ColumnResult(int col_num) {
        this.sub_cols_vec = new Vector();
        this.col_num = col_num; }

    public void addSubColumnResult(SubColumnResult sub_col_res) {
	this.sub_cols_vec.addElement(sub_col_res); }

    public void addSubColumnResult(String label, SentenceResult sent_res) {
	SubColumnResult sub_col_res = new SubColumnResult(label, sent_res);
	this.addSubColumnResult(sub_col_res); }

    public SubColumnResult getSubColumnResult(int i) {
	return((SubColumnResult)sub_cols_vec.elementAt(i)); }

    public void downDateBounds(ChangeTree changeable, SynTree sint) {
	SubColumnResult sub_col_res;

	for (int i = 0; i < this.size(); i++) {
	    sub_col_res = this.getSubColumnResult(i);
	    sub_col_res.downDateBounds(changeable, sint); }}
	
    public int getColNum() {
	return col_num; }

    public int size() {
	return (sub_cols_vec.size()); }

    public void PrintToSystemErr() {
	SubColumnResult sub_col_res;
	System.err.println("col_num:  " + col_num);

	for (int i = 0; i < sub_cols_vec.size(); i++) {
	    sub_col_res = (SubColumnResult)sub_cols_vec.elementAt(i);
	    System.err.print(i + ".)  ");
	    sub_col_res.PrintToSystemErr(); } }

}






