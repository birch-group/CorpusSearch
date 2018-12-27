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
public class hasLabel extends Syntax {

    /*
      Plain --
    */
    public static SentenceResult Plain (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node forebear, current;
        int n;
	
	//System.err.print("in iDoms: x_list, y_list:  " + x_list.toString());
	//System.err.println(", "  + y_list.toString());
			   //	x_list.PrintToSystemErr();
			   //y_list.PrintToSystemErr();
        for (n = 0; n < sparse.size(); n++) {
	    current = (Node)sparse.NodeAt(n);
	    if (IsOnList(sparse, current,  x_list)) {
		if (IsOnList(sparse, current, y_list)) {
		    forebear = GetBoundaryNode(sparse, current);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, current, current); }}}}
	//if (!Indices.isEmpty()) {
	//System.err.println("in iDominates: ");
	//Indices.PrintToSystemErr();}
        return Indices; }

    public static SentenceResult Not_x (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node forebear, current;
        int n;
	
	//System.err.print("in iDoms: x_list, y_list:  " + x_list.toString());
	//System.err.println(", "  + y_list.toString());
			   //	x_list.PrintToSystemErr();
			   //y_list.PrintToSystemErr();
        for (n = 0; n < sparse.size(); n++) {
	    current = (Node)sparse.NodeAt(n);
	    if (IsOnList(sparse, current,  y_list)) {
		if (!IsOnList(sparse, current, x_list)) {
		    forebear = GetBoundaryNode(sparse, current);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, current, current); }}}}
	//if (!Indices.isEmpty()) {
	//System.err.println("in iDominates: ");
	//Indices.PrintToSystemErr();}
        return Indices; }


    public static SentenceResult Not_y (SynTree sparse,
					ArgList x_list, ArgList y_list) {
	SentenceResult Indices = new SentenceResult();
        Node forebear, current;
        int n;

	//System.err.print("in iDoms: x_list, y_list:  " + x_list.toString());
	//System.err.println(", "  + y_list.toString());
			   //	x_list.PrintToSystemErr();
			   //y_list.PrintToSystemErr();
        for (n = 0; n < sparse.size(); n++) {
	    current = (Node)sparse.NodeAt(n);
	    if (IsOnList(sparse, current,  x_list)) {
		if (!IsOnList(sparse, current, y_list)) {
		    forebear = GetBoundaryNode(sparse, current);
		    if (!forebear.IsNullNode()) {
			Indices.addSubResult(forebear, current, current); }}}}
	//if (!Indices.isEmpty()) {
	//System.err.println("in iDominates: ");
	//Indices.PrintToSystemErr();}
        return Indices; }

} 



