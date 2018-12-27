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
 * A iDominates B if B is a child of A.
 */
public class iDomsT extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node mother, forebear, current, ant, daughter;
	Vector daughters;
        int n, i;

        for (n = 0; n < sparse.size(); n++) {
	    current = (Node)sparse.NodeAt(n);
  	    if (IsOnList(sparse, current,  y_list)) {
		mother = sparse.GetMother(current);
		if (IsOnList(sparse, mother, x_list)) {
		    forebear = GetBoundaryNode(sparse, mother);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, mother, current);}}}
	    if (IsOnList(sparse, current, x_list)) { // look for antecedent.
		    ant = sparse.getAntecedent(current);
		    if (ant.IsNullNode()) { break; } 
		    daughters = sparse.GetDaughters(ant); 
		    for (i = 0 ; i < daughters.size(); i++) {
			daughter = (Node)daughters.elementAt(i);
			if (IsOnList(sparse, daughter, y_list)) {
			    forebear = GetBoundaryNode(sparse, current);
			    if (!forebear.IsNullNode()) {
				Indices.addSubResult(forebear, 
						     current, daughter);}}}}}
	//System.err.println("in iDomsT:  ");
	//Indices.PrintToSystemErr();
    return Indices; }

    public static SentenceResult Not_x (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node mother, forebear, current, trace;
        int n;

        sparse_loop: for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
            current_loop: if (IsOnList(sparse, current,  y_list)) {
                mother = sparse.GetMother(n);
                if (!IsOnList(sparse, mother, x_list)) {
		    forebear = GetBoundaryNode(sparse, mother);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, mother, current); }}
		trace = sparse.getTrace(current);
		if (trace.IsNullNode()) {
		    continue sparse_loop; }
                if (!IsOnList(sparse, trace, x_list)) {
		    forebear = GetBoundaryNode(sparse, trace);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, trace, current); }}}}
        return Indices; }

    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node mother, forebear, current, ant, daughter = new Node("NULL");
	Vector daughters;
        int n, i;
	boolean has_y = false;

        for (n = 0; n < sparse.size(); n++) {
	    current = (Node)sparse.NodeAt(n);
	    if (IsOnList(sparse, current, x_list)) {
		daughters = sparse.GetDaughters(current);
		has_y = false;
		offspring:  for (i = 0 ; i < daughters.size(); i++) {
		    daughter = (Node)daughters.elementAt(i);
		    if (IsOnList(sparse, daughter, y_list)) {
			has_y = true;
			break offspring; } 
		    if (!has_y) {
			forebear = GetBoundaryNode(sparse, current);
			if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, 
						 current, daughter); }}}
 
		if (sparse.IsLeafPOS(current)) { // has antecedent.
		    ant = sparse.getAntecedent(current);
		    if (ant.IsNullNode()) { break; }
		    daughters = sparse.GetDaughters(ant);
		    has_y = false;
		    offspring:  for (i = 0 ; i < daughters.size(); i++) {
			daughter = (Node)daughters.elementAt(i);
			if (IsOnList(sparse, daughter, y_list)) {
			    has_y = true;
			    break offspring; } }
		    if (!has_y) {
			forebear = GetBoundaryNode(sparse, current);
			if (!forebear.IsNullNode()) {
			    Indices.addSubResult(forebear, 
						 current, daughter); }}}}}
	    return Indices; }

} 



