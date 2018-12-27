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
import basicinfo.*;
import search_result.*;

public class RemoveSys extends PrintTreeSys {

    private static boolean removed;
    private static String label = "";
    private static Node one_item;

    /*
      RemoveNodeVec
    */
    public static void RemoveNodeVec (SynTree sparse, int indent,
				      int sparse_dex, 
				      int res_dex,
				      SentenceResult one_result,
				      PrintWriter out_file) {
	int i, j, k, next_dent;
	Vector daughters;
	Node daughter;
	boolean just_leaves;
	Integer daughter_dex;
	
	try {
	    one_item = sparse.NodeAt(sparse_dex);
	    label = one_item.getLabel();
	    next_dent = indent;
	    next_dent += 1 + label.length() + 1; // 1 for "(" + 1 for " ".
	    if (previous_leaf) {
                IndentIt(indent, out_file);
                previous_leaf = false; }
	    removed = Remove_or_not(sparse, one_item, res_dex, out_file);
	    if (removed) {
		previous_leaf = true;
		return; }
	    out_file.print("(");
	    next_dent = HandleIndices(one_item, next_dent, out_file);
	    PrintWithColor(one_item, one_result, true, out_file);
	    daughters = sparse.GetDaughters(sparse_dex);
	    just_leaves = HasJustLeaves(sparse, daughters);
	    for (j = 0; j < daughters.size(); j++) {
		daughter = (Node)daughters.elementAt(j);
		daughter_dex = daughter.getIndex();
		if (!(sparse.IsLeafPOS(daughter))) {
		    RemoveNodeVec(sparse, next_dent,
				  daughter_dex.intValue(), res_dex,
				  one_result, out_file); }
		else { 
		    if (j > 0 && !just_leaves) {
			IndentIt(next_dent, out_file); }
		    removed = Remove_or_not(sparse, daughter, res_dex, 
					    out_file);
		    if (!removed) {
			PrintLeafNode(sparse, daughter, one_result, out_file);
			if (just_leaves && (j < daughters.size() - 1)) {
			    out_file.print(" "); } } } }
	    out_file.print(")"); }
	catch (Exception e) {
	    System.err.println("");
	    e.printStackTrace();
	    System.err.println("in Remove.NodeVec:sparse_dex:  " + sparse_dex);
	    sparse.PrintToSystemErr(); }
	finally {  return; } }

    /*
      Nodes -- prints out nodes of sentence as indicated by result vector. 
      called if command file contained "nodes_only: true."
    */
    public static void Nodes (SynTree sparse, SentenceResult OneResult, 
			      PrintWriter out_file) {
	sparse.setConstantNodes();
	subNodes(sparse, 0, OneResult, out_file);
	bound_list.removeAllElements();
	return; }

    /*
      subNodes -- printout nodes of sentence as indicated by result vector.
    */
    public static void subNodes (SynTree sparse, int sparse_dex, 
				 SentenceResult sent_res,
				 PrintWriter out_file) { 
	Node one_item, res_bound, daughter;
	SubResult sub_res;
	Vector daughters;
	int i, j, res_dex, daughter_dex, indent = 0;

	try {
	    one_item = sparse.NodeAt(sparse_dex);
	    through_result: for (i = 0; i < sent_res.size(); i++) {
		sub_res = sent_res.subResultAt(i);
		res_bound = sub_res.getBoundary();
		if (res_bound.equals(one_item) && notDupe(res_bound)) {
		    res_dex = res_bound.getIndex_int();
		    previous_leaf = false;
		    if (res_bound.equals(sparse.METAROOT)) {
			PhraseVec(sparse, 0, sparse_dex, sent_res, out_file);
			out_file.println(""); return; }
		    out_file.print("( ");
		    CodingAndMetadata(sparse, res_bound, sent_res, out_file);
		    RemoveNodeVec(sparse, 2, sparse_dex, res_dex, sent_res, 
				  out_file);
		    PrintIDStuff(sparse, sent_res, out_file); } }
	    daughters = sparse.GetDaughters(sparse_dex);
	    for (j = 0; j < daughters.size(); j++) {
		daughter = (Node)daughters.elementAt(j);
		daughter_dex = daughter.getIndex_int();
		if (!(sparse.IsLeafPOS(daughter))) {
		    subNodes(sparse, daughter_dex, sent_res, out_file); }
		else { // leaf node
		    through_result2: for (i = 0; i < sent_res.size(); i++) {
			sub_res = sent_res.subResultAt(i);
			res_bound = sub_res.getBoundary();
			if (res_bound.equals(daughter) && notDupe(res_bound)) {
			    PrintLeafTree(sparse, daughter, sent_res, 
					  out_file); }
		    }}}}
	catch (Exception e) {
	    System.err.println("in Remove.java: Nodes:  ");
	    e.printStackTrace(); }
	finally { return; } }

    /*
      Remove_or_not:  determines whether or not
      to remove a node.
      input --
      output -- true if node was removed;
      false otherwise.
    */
    public static boolean Remove_or_not(SynTree sparse, Node one_item,
					int res_dex, PrintWriter out_file) {
	String text = "", place_holder = "", label = "";
	int sparse_dex;
	Node leaf_text;

	sparse_dex = one_item.getIndex_int();
	label = one_item.getLabel();
	if (sparse_dex != res_dex && 
	    species_list.hasMatch(label)) {
	    if (sparse.IsLeafPOS(one_item)) { 
		//&& (!remove_ignore_nodes)) {
		leaf_text = sparse.FirstDaughter(one_item);
		text = leaf_text.getLabel();
		//if (PipeList.IsOnList(text, PipeList.ignore_list)) {
		//  return false; }
	    }
	    place_holder = PlaceHolder(sparse, one_item);
	    PrintPlaceHolderNode(one_item, place_holder, out_file);
	    return true; }
	return false; }

    public static void PrintPlaceHolderNode (Node to_remove, 
					     String place_holder,
					     PrintWriter out_file) {
	int index;
	String label;
	
	label = to_remove.getLabel();
	if (print_indices) {
	    index = to_remove.getIndex_int();
	    out_file.print("(" + index + " " + label + " ");
	    out_file.print(place_holder + ")"); }
	else {
	    out_file.print("(" + label + " " + place_holder + ")"); }
	return; }

    /*
      PlaceHolder -- hold the place of a removed node.
      input --
      output -- place-holder string to be printed.
    */
    public static String PlaceHolder (SynTree sparse, Node one_item) { 

	String place_holder = "", str0 = "";
	Vector leaves_vec;
	int n, limit = 3;
	
	place_holder = "RMV:";
	leaves_vec = sparse.getText(one_item);
	str0 = ((Node)leaves_vec.elementAt(0)).getLabel();
	if (str0.startsWith("RMV:")) {
	    return str0; }
	if (leaves_vec.size() < limit)
	    limit = leaves_vec.size();
	for (n = 0; n < limit; n++) {
	    place_holder += ((Node)leaves_vec.elementAt(n)).getLabel();
	    if (n < limit - 1) {
		place_holder += "_"; }
	    else {
		place_holder += "..."; } }
	return place_holder; }

} 

