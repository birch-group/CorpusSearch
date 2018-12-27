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

public class iDomsViaTrace extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse, ArgList x_list, 
					ArgList mod_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node forebear, current, offspr, grandspring, codexnode;
	Vector daughters, grandkids, codexvec;
        int n, i, j, k;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    if (IsOnList(sparse, current, x_list)) {
		daughters = sparse.GetDaughters(n);
		daughters_loop:  for (i = 0; i < daughters.size(); i++) {
		    offspr = (Node)daughters.elementAt(i);
		    if (offspr.hasLabelDex() && 
			IsOnList(sparse, offspr, mod_list)) {
			codexvec = sparse.getCoIndexed(offspr); 
			for (j = 0; j < codexvec.size(); j++) {
			    codexnode = (Node)codexvec.elementAt(j);
			    grandkids = sparse.GetDaughters(codexnode);
			    for (k = 0; k < grandkids.size(); k++) {
				grandspring = (Node)grandkids.elementAt(k);
				if (IsOnList(sparse, grandspring, y_list)) {
				    forebear = GetBoundaryNode(sparse, offspr);
				    if (!forebear.IsNullNode()) {
					Indices.addSubResult(forebear, 
							     current, 
							     grandspring);}}}
			}}}}}		
	    return Indices; }

    /*
      Not_x --
    */
    public static SentenceResult Not_x (SynTree sparse, ArgList x_list,
                                        ArgList mod_list, ArgList y_list) {
        SentenceResult Indices = new SentenceResult();
        Node forebear, current, mom, codexnode, codexmom;
        int n, j;
	Vector codexvec;

        for (n = 0; n < sparse.size(); n++) {
            current = sparse.NodeAt(n);
            if (IsOnList(sparse, current, y_list)) {
		mom = sparse.GetMother(current); 
		if (mom.hasLabelDex()) {
			codexvec = sparse.getCoIndexed(mom); 
			dexvecloop:  for (j = 0; j < codexvec.size(); j++) {
			    codexnode = (Node)codexvec.elementAt(j);
			    if (!IsOnList(sparse, codexnode, mod_list)) {
				continue dexvecloop; }
			    codexmom = sparse.GetMother(codexnode);
			    if (!IsOnList(sparse, codexnode, x_list)) {
				forebear = GetBoundaryNode(sparse, codexnode);
				if (!forebear.IsNullNode()) {
				    Indices.addSubResult(forebear, 
							 codexnode, current);}}}}}} 
	    return Indices; }

    /*
      Not-y --
    */
    public static SentenceResult Not_y (SynTree sparse, ArgList x_list, 
					ArgList mod_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node forebear, current, offspr, grandspring = new Node("NULL"), codexnode;
	Vector daughters, grandkids, codexvec;
        int n, i, j, k;

        for (n = 0; n < sparse.size(); n++) {
	    current = sparse.NodeAt(n);
	    if (IsOnList(sparse, current, x_list)) {
		daughters = sparse.GetDaughters(n);
		daughters_loop:  for (i = 0; i < daughters.size(); i++) {
		    offspr = (Node)daughters.elementAt(i);
		    if (offspr.hasLabelDex() && 
			IsOnList(sparse, offspr, mod_list)) {
			codexvec = sparse.getCoIndexed(offspr); 
			for (j = 0; j < codexvec.size(); j++) {
			    codexnode = (Node)codexvec.elementAt(j);
			    grandkids = sparse.GetDaughters(codexnode);
			    if (grandkids.isEmpty()) {
				continue daughters_loop; }
			    for (k = 0; k < grandkids.size(); k++) {
				grandspring = (Node)grandkids.elementAt(k);
				if (IsOnList(sparse, grandspring, y_list)) {
				    continue daughters_loop; } }
			    forebear = GetBoundaryNode(sparse, offspr);
			    if (!forebear.IsNullNode()) {
				Indices.addSubResult(forebear, 
						     current, 
						     grandspring);}}}}}}
    return Indices; }

} 



