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

package print;

import java.io.*;
import java.util.*;
import syntree.*;
import search_result.*;

/**
 * prints tree to output, in standard Penn Treebank format.
 */

public class PrintTreeSys extends Parameters {

    protected static boolean previous_leaf = false;
    protected static Vector bound_list = new Vector();

    /**
     * recursively called function to print out tree.
     */
    protected static void PhraseVec (SynTree sparse, int indent, 
				     int sparse_dex, 
				     SentenceResult one_result,
				     PrintWriter out_file) {
	int j, next_dent;
	String label= "";
	Vector daughters;
	Node one_item, daughter;
	boolean just_leaves;
	Integer daughter_dex;

	try {
	    one_item = sparse.NodeAt(sparse_dex);
	    label = one_item.getLabel();
	    next_dent = indent;
	    next_dent += 1 + label.length() + 1;  // 1 for "(" + 1 for " ".
	    if (previous_leaf) {
		IndentIt(indent, out_file);
		previous_leaf = false; }
	    out_file.print("(");
	    next_dent = HandleIndices(one_item, next_dent, out_file);
	    PrintWithColor(one_item, one_result, true, out_file);
	    daughters = sparse.GetDaughters(sparse_dex);
	    just_leaves = HasJustLeaves(sparse, daughters);
	    for (j = 0; j < daughters.size(); j++) {
		daughter = (Node)daughters.elementAt(j);
		daughter_dex = daughter.getIndex();
		if (!(sparse.IsLeafPOS(daughter_dex))) {
		    PhraseVec(sparse, next_dent,  
			      daughter_dex.intValue(),
			      one_result, out_file); }
		else { 
		    if (j > 0 && !just_leaves) {
			IndentIt(next_dent, out_file); }
		    PrintLeafNode(sparse,  daughter, one_result, out_file);
		    if (just_leaves && (j < daughters.size() - 1)) {
			out_file.print(" "); } } }
	    out_file.print(")");
	} 
	catch (Exception e) {
	    e.printStackTrace();
	    System.err.println("in PrintTree:  sparse_dex:  " + sparse_dex);
	    sparse.PrintToSystemErr(); }
	finally { return; }
    } 

    /**
     * prints out nodes of sentence as indicated by result vector. 
     * Called if command file contained "nodes_only: true."
     */
    public static void Nodes (SynTree sparse, SentenceResult OneResult,
			      PrintWriter out_file) {
	sparse.setConstantNodes();
	subNodes(sparse, 0, OneResult, out_file);
	bound_list.removeAllElements();
	return; }

    /**
     * recursively called subfunction of Nodes.  
     * Prints out nodes of sentence as indicated by result vector.
     */
    protected static void subNodes (SynTree sparse, int sparse_dex, 
				    SentenceResult sent_res,
				    PrintWriter out_file) { 
	Node one_item, res_bound, daughter;
	SubResult sub_res;
	Vector daughters;
	int i, j, daughter_dex;

	one_item = sparse.NodeAt(sparse_dex);
	through_result: for (i = 0; i < sent_res.size(); i++) {
	    sub_res = sent_res.subResultAt(i);
	    res_bound = sub_res.getBoundary();
	    if (res_bound.equals(one_item) && notDupe(res_bound)) {
		previous_leaf = false;
		if (res_bound.equals(sparse.METAROOT)) {
		    PhraseVec(sparse, 0,  sparse_dex, sent_res, out_file); 
		    out_file.println("");
		    return; } 
		out_file.print("( ");
		CodingAndMetadata(sparse,  res_bound, sent_res, out_file);
		PhraseVec(sparse, 2,  sparse_dex, sent_res, out_file);
		PrintIDStuff(sparse, sent_res, out_file); } }
	daughters = sparse.GetDaughters(sparse_dex);
	for (j = 0; j < daughters.size(); j++) {
	    daughter = (Node)daughters.elementAt(j);
	    daughter_dex = daughter.getIndex_int();
	    if (!(sparse.IsLeafPOS(daughter))) {
		subNodes(sparse, daughter_dex, sent_res, out_file); }
	    else { // leaf node.
		through_result2: for (i = 0; i < sent_res.size(); i++) {
		    sub_res = sent_res.subResultAt(i);
		    res_bound = sub_res.getBoundary();
		    if (res_bound.equals(daughter) && notDupe(res_bound)) {
			PrintLeafTree(sparse,  daughter, sent_res, out_file); }
		} } } 
	return; }

    /**
     * Determines whether all elements of Vector daughters are leaves.
     * This affects indentation
     */
    protected static boolean HasJustLeaves (SynTree sparse, Vector daughters) {

	Node daughter, leaf_node;
	int j;
	String leaf;

	leaf_loop:  for (j = 0; j < daughters.size(); j++) {
	    daughter = (Node)daughters.elementAt(j);
	    if (!(sparse.IsLeafPOS(daughter))) {
		return false; }
	    else {
		previous_leaf = true;
		leaf_node = sparse.FirstDaughter(daughter);
		leaf = leaf_node.getLabel();
		if (leaf.startsWith("*") || leaf.startsWith("RMV") ||
		    leaf.indexOf(":") > 0) {
		    return false; } } }
	return true; }

    /**
     * prints tree that contains just a leaf.
     */
    protected static void PrintLeafTree(SynTree sparse, Node daughter,
					SentenceResult one_result,
					PrintWriter out_file) {
	out_file.println("");
	out_file.print("( ");
	PrintLeafNode(sparse,  daughter, one_result, out_file);
	PrintIDNode(sparse, one_result, out_file);
	out_file.println(" )"); 
	return; }

    protected static void CodingAndMetadata (SynTree sparse, 
					     Node res_bound,
					     SentenceResult one_result,
					     PrintWriter out_file) {
	boolean got_metadata = false;

	got_metadata = PrintMETADATA(sparse, one_result, out_file);
	PrintCODINGNode(sparse, res_bound, "( ",  got_metadata, one_result,
			out_file);
	return; }

    /**
     * prints CODING node closest to incoming node.
     */
    protected static boolean PrintCODINGNode (SynTree sparse, Node bound, 
					      String prev, 
					      boolean got_metadata,
					      SentenceResult one_result,
					      PrintWriter out_file) {
	Node code;

	code = GetAssocNode(sparse, bound, "CODING");
	if (!code.IsNullNode()) {
	    if (got_metadata) {
		out_file.println(""); 
		out_file.print("  "); }
	    PrintLeafNode(sparse,  code, one_result, out_file); 
	    //IndentIt(prev.length());
	    return true; }
	return false; }

    protected static boolean PrintMETADATA (SynTree sparse,
					    SentenceResult one_result,
					    PrintWriter out_file) {
	Node meta;
	Vector data;

	meta = sparse.getMETADATA();
	if (meta.IsNullNode()) { return false; }
        PhraseVec(sparse, 2,  meta.getIndex_int(), one_result, out_file);
        return true; }
	
    protected static Node GetAssocNode(SynTree sparse, 
				       Node current, String to_match) {

	int dex = current.getIndex_int();
	Node ancestor = current, daughter;

	while (!ancestor.IsNullNode()) {
	    ancestor = sparse.GetMother(ancestor);
	    daughter = sparse.FirstDaughter(ancestor);
	    if ((daughter.getLabel()).startsWith(to_match)) {
		return daughter; } }
	return (new Node("NULL")); }

    /**
     * prints ID node at end of subtree.
     */
    protected static void PrintIDNode (SynTree sparse, 
				       SentenceResult one_result,
				       PrintWriter out_file) {
	if (!(sparse.ID_POS).IsNullNode()) {
	    PrintLeafNode(sparse,  sparse.ID_POS, one_result, out_file); }
	return; }

    /**
     * prints this format (23 N dog)
     *
     */
    protected static void PrintLeafNode (SynTree sparse, Node leafPOS,
					 SentenceResult one_result,
					 PrintWriter out_file) {
	Node leafText;
	Integer index;

	out_file.print("(");
	if (print_indices) {
	    index = leafPOS.getIndex();
	    out_file.print(index + " "); }
	leafText = sparse.FirstDaughter(leafPOS);
	PrintWithColor(leafPOS, one_result, true, out_file);
	PrintWithColor(leafText, one_result, false, out_file);
	out_file.print(")");
	return; }


    protected static void PrintWithColor (Node to_print, 
					  SentenceResult one_result,
					  boolean space_after,
					  PrintWriter out_file) {
	boolean changed = false;

	if (one_result.containsNode(to_print)) {
	    out_file.print("<FONT COLOR=\"RED\">");
	    changed = true; }
	PrintWithEscape(to_print.getLabel(), out_file);
	if (changed) {
	    out_file.print("</FONT>"); }
	if (space_after) {
	    out_file.print("&nbsp;"); }
	}
	

    protected static void PrintWithEscape(String to_print, 
					  PrintWriter out_file) {

	if (to_print.startsWith("<")) {
	    out_file.print("&lt;" + to_print.substring(1)); }
	else {
	    out_file.print(to_print); } }

    /**
     * prints out sentence contained in SynTree.
     * Sets up recursive call to PhraseVec.
     */
    public static void Sentence(SynTree sparse, 
				SentenceResult one_result,
				PrintWriter out_file) {

	if (sparse.intEndDexAt(0) == 1) {
	    PrintLeafNode(sparse,  sparse.NodeAt(0), one_result, out_file); 
	    out_file.println("");
	    return; }
	PhraseVec(sparse, 0, 0, one_result, out_file);
	out_file.println("");
	return; }

    /**
     * prints out indices as required.
     * Returns updated value of next_dent.
     */
    protected static int HandleIndices(Node one_item, int next_dent,
				       PrintWriter out_file) {
	String str_dex;
	Integer index;

	if (print_indices) {  
	    index = one_item.getIndex();
	    str_dex = index.toString(index.intValue());
	    // number of digits in index + 1 for " ".
	    next_dent += str_dex.length() + 1;
	    out_file.print(str_dex + " "); }
	return next_dent; }
    
    /**
     * prints ID node with surrounding spaces and parens.
     */
    protected static void PrintIDStuff(SynTree sparse, 
				       SentenceResult one_result,
				       PrintWriter out_file) {
	out_file.println("");
	out_file.print("  "); // 2 spaces for "( ".
	PrintIDNode(sparse, one_result, out_file);
	out_file.println(")");
	out_file.print(""); }

    /**
     * causes spaces or tabs to be printed for indentation.
     * @param indent -- number of spaces to indent.
     * @param out_file -- file in which to print spaces.
     * @return -- void.
     */
    protected static void IndentIt (int indent, PrintWriter out_file) {
	int j;

	out_file.println("");
	for (j = 0; j < indent/8; j++) {
	    out_file.print("\t"); }
	for (j = 0; j < indent%8; j++) {
	    out_file.print(" "); }
	return; }


    /**
     */
    protected static boolean notDupe(Node res_bound) {
	Node a_bound;
	int m;

	for (m = 0; m < bound_list.size(); m++) {
	    a_bound = (Node)bound_list.elementAt(m);
	    if (a_bound.equals(res_bound)) {
		return false; } }
	bound_list.addElement(res_bound);
	return true; }

    public static void PrintToken (SynTree sparse, PrintWriter out_file) {
	if (sparse.isEmpty()) { return; }
	if (Parameters.ur_text_only) {  return; }
	Sentence(sparse, new SentenceResult(), out_file); }

    public static void PrintToken (SynTree sparse, SentenceResult one_result,
				   PrintWriter out_file) {
	if (sparse.isEmpty()) { return; }
	if (Parameters.ur_text_only) { return; }
	// deal with empty result vector, in case of complement file.
	if (one_result.isEmpty()) {
	    Sentence(sparse, one_result, out_file);
	    return; }
        if (!Parameters.nodes_only) {
            Sentence(sparse, one_result, out_file);
            return; }
        // if we're here, Parameters.nodes_only = true.
        if (!Parameters.remove_nodes) {
            Nodes(sparse, one_result, out_file);
            return; }
        // if we're here, Parameters.remove_nodes = true.
        RemoveSys.Nodes(sparse, one_result, out_file);
        return; }

} 



