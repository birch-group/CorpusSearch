/*

 */
package coding;

import java.io.*;
import java.util.*;
import search_result.*;
import syntree.*;

public class SubColumnResult {

    private String label;
    private SentenceResult sent_res;

    public SubColumnResult() {
	label = "BULLWINKLE_THE_MOOSE"; }

    public SubColumnResult(String label, SentenceResult res) {
	this.label = label;
	this.sent_res = res; }

    public String getLabel() {
	return this.label; }

    public SentenceResult getSentenceResult() {
	return this.sent_res; }

    public void downDateBounds(ChangeTree changeable, SynTree sint) {
	SubResult sub_res;
	Node change_bound, sint_bound;
	int i, sint_bound_dex;
	
	for (i = 0; i < this.sent_res.size(); i++) {
	    sub_res = this.sent_res.subResultAt(i);
	    change_bound = sub_res.getBoundary();
	    sint_bound_dex = changeable.getDowndate(change_bound.getIndex());
	    if (sint_bound_dex < 0) {
		System.err.print("WARNING!  in SubColumnResult.");
		System.err.print("downDateBounds:  ");
		System.err.print("change_bound:  "+ change_bound.toString());
		PrintLastNode(changeable);
	        sint_bound_dex = change_bound.getIndex_int(); }
	    sint_bound = sint.NodeAt(sint_bound_dex);
	    sub_res.setBoundary(sint_bound); }}

    public void PrintLastNode (ChangeTree changeable) {
	Node last_node;

	last_node = changeable.NodeAt(changeable.size() - 1);
	System.err.println(last_node.toString()); }

    public void PrintToSystemErr() {
	System.err.println("    label:  " + this.label);
	System.err.println("    SentenceResult:  ");
	(this.sent_res).PrintToSystemErr(); }

}






