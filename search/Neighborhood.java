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

package search;
 
import java.util.*;
import syntree.*;
import search_result.*;
import basicinfo.*;

/**
 * 
 */
public class Neighborhood extends Syntax {

    public static SentenceResult Plain (SynTree sparse, ArgList x_List, 
					ArgList y_List, int how_close) {

        SentenceResult Indices = new SentenceResult();
        Node current;
        int n, so_far, next_dex;

	tree_loop:  for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current,  x_List)) {
		Indices = afterHood(Indices, sparse, y_List, 
				    how_close, current, n);
		Indices = beforeHood(Indices, sparse, y_List, 
				     how_close, current, n);
	    }}
	return Indices; }

    public static SentenceResult afterHood (SentenceResult Indices, 
					    SynTree sparse, ArgList y_List, 
					    int how_close, 
					    Node current, int from_where) {

        Node forebear, next_POS, next_Text;
        int n, so_far = 0, next_dex;

	so_far = 0;
	next_dex = from_where + 1;
	while (so_far < how_close) {
	    next_POS = nextLegitPOS(sparse, next_dex);
	    if (next_POS.IsNullNode()) { return Indices; }
	    so_far += 1;
	    if (IsOnList(sparse, next_POS, y_List)) {
		forebear = GetBoundaryNode(sparse, current, next_POS);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, next_POS); }} 
	    next_Text = sparse.FirstDaughter(next_POS);
	    if (IsOnList(sparse, next_Text, y_List)) {
		forebear = GetBoundaryNode(sparse, current, next_Text);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, next_Text); }}
	    next_dex = next_Text.getIndex_int(); }
	return Indices; }

    public static SentenceResult beforeHood (SentenceResult Indices, 
					     SynTree sparse, ArgList y_List, 
					     int how_close, 
					     Node current, int from_where) {

        Node forebear, prev_POS, prev_Text;
        int n, so_far = 0, prev_dex;

	so_far = 0;
	prev_dex = from_where - 1;
	while (so_far < how_close) {
	    prev_POS = prevLegitPOS(sparse, prev_dex);
	    if (prev_POS.IsNullNode()) { return Indices; }
	    so_far += 1;
	    if (IsOnList(sparse, prev_POS, y_List)) {
		forebear = GetBoundaryNode(sparse, current, prev_POS);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, prev_POS); }} 
	    prev_Text = sparse.FirstDaughter(prev_POS);
	    if (IsOnList(sparse, prev_Text, y_List)) {
		forebear = GetBoundaryNode(sparse, current, prev_Text);
		if (!forebear.IsNullNode()) {
		    Indices.addSubResult(forebear, current, prev_Text); }}
	    prev_dex = prev_POS.getIndex_int() - 1; }
	return Indices; }

    public static SentenceResult Not_x (SynTree sparse, ArgList x_List, 
					ArgList y_List, int how_close) {
        SentenceResult Indices = new SentenceResult();
        Node forebear, current, next_POS, next_Text;
        int n, so_far, next_dex;

	tree_loop:  for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current,  x_List)) {
		so_far = 0;
		next_dex = n + 1;
		while (so_far < how_close) {
		    next_POS = nextLegitPOS(sparse, next_dex);
		    if (next_POS.IsNullNode()) { continue tree_loop; }
		    so_far += 1;
		    if (IsOnList(sparse, next_POS, y_List)) {
			forebear = GetBoundaryNode(sparse, current, 
						   next_POS);
		        if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, current, 
						 next_POS); }} 
		    next_Text = sparse.FirstDaughter(next_POS);
		    if (IsOnList(sparse, next_Text, y_List)) {
			forebear = GetBoundaryNode(sparse, current, 
						   next_Text);
		        if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, current, 
						 next_Text); }}
		    next_dex = next_Text.getIndex_int(); }}}
	    return Indices; }


    public static SentenceResult Not_y (SynTree sparse, ArgList x_List, 
					ArgList y_List, int how_close) {
        SentenceResult Indices = new SentenceResult();
        Node forebear, current, next_POS, next_Text;
        int n, so_far, next_dex;

	tree_loop:  for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current,  x_List)) {
		so_far = 0;
		next_dex = n + 1;
		while (so_far < how_close) {
		    next_POS = nextLegitPOS(sparse, next_dex);
		    if (next_POS.IsNullNode()) { continue tree_loop; }
		    so_far += 1;
		    if (IsOnList(sparse, next_POS, y_List)) {
			continue tree_loop; }
		    next_Text = sparse.FirstDaughter(next_POS);
		    if (IsOnList(sparse, next_Text, y_List)) {
			continue tree_loop; }
		    next_dex = next_Text.getIndex_int(); }
		    forebear = GetBoundaryNode(sparse, current);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, current, current); }}} 
	    return Indices; }

    private static Node nextLegitPOS (SynTree sparse, int from_where) {
	int k;
	Node nodal;

	if (from_where>= sparse.size()) { return (new Node("NULL")); }
	looking:  for (k = from_where; k < sparse.size(); k++) {
	    nodal = sparse.NodeAt(k);
	    if (sparse.IsLeafPOS(nodal)) { 
		if (ignoreIt(sparse, nodal)) {
		    continue looking; }
		return nodal; } }
	return (new Node("NULL")); }

    private static Node prevLegitPOS (SynTree sparse, int from_where) {
	int k;
	Node nodal;

	if (from_where <= 0) { return (new Node("NULL")); }
	looking:  for (k = from_where; k > 0; k--) {
	    nodal = sparse.NodeAt(k);
	    if (sparse.IsLeafPOS(nodal)) { 
		if (ignoreIt(sparse, nodal)) {
		    continue looking; }
		return nodal; } }
	return (new Node("NULL")); }



} 

