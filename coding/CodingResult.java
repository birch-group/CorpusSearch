/*
A CodingResult is a list of ColumnResult objects.
 */
package coding;

import java.io.*;
import java.util.*;
import syntree.*;

public class CodingResult {

    private Vector result_vec;

    public CodingResult() {
        this.result_vec = new Vector(); }

    public int size() {
	return ((this.result_vec).size()); }

    public void addColumnResult(ColumnResult col_res) {
	result_vec.addElement(col_res); }

    public ColumnResult getColumnResult(int i) {
	return((ColumnResult)result_vec.elementAt(i)); }

    public void downDateBounds(ChangeTree changeable, SynTree sint) {
	ColumnResult col_res;

	for (int i = 0; i < result_vec.size(); i++) {
	    col_res = this.getColumnResult(i);
	    col_res.downDateBounds(changeable, sint); }}

    public void PrintToSystemErr() {
	ColumnResult col_res;
	System.err.println("");
	System.err.println("CodingResult:  ");
	for (int i = 0; i < result_vec.size(); i++) {
	    col_res = this.getColumnResult(i);
	    col_res.PrintToSystemErr(); }
    }

}






