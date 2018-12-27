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

/*
  Beth Randall:  Oct 2000
  UrText.java
*/
package print;

import java.io.*;
import java.util.*;
import syntree.*;
import basicinfo.*;

public class UrText extends Parameters{
    
    private int length;
    private boolean no_space_next;
    private boolean is_open_quote;
    private Vector ur_vec = new Vector();
    private ArgList ignore_for_ur;
    public String bad_ur_labels = "CODE|CODING*|COMMENT*";
    private ArgList punct_arg;
    public String punct_str = ",|;|:|\\.|?|!";
    private ArgList quote_arg;
    public String quote_str = "'|\"";
    private ArgList not_legit_arg;
    public String not_legit_str = "RMV*|" + "\\" + "**";

    public UrText() {
	UrInit(); }

    public UrText(int in_margin) {
	UrInit();
	Parameters.margin = in_margin; }
    
    private void UrInit() {
	length = 0;
	no_space_next = false;
	is_open_quote = true;
	ur_vec = new Vector();
	ignore_for_ur = new ArgList(bad_ur_labels);
	punct_arg = new ArgList(punct_str);
	quote_arg = new ArgList(quote_str);
	not_legit_arg = new ArgList(not_legit_str); }


    public String toString(SynTree sparse) {
        int n;
        Node curr_node, daughter, sis_node;
        String text = "", label = "", without_plus = "", ur_str = "";
        boolean prev_LB = false;

        length = 0;
	is_open_quote = true;
	no_space_next = false;
        try {
	    phrase_loop:  for (n = 1; n < sparse.size(); n++) {
                curr_node = sparse.NodeAt(n);
		if (curr_node.getLabel().equals("METADATA")) {
		    sis_node = sparse.nextSister(curr_node);
		    if (sis_node.IsNullNode()) { break phrase_loop; }
		    n = sis_node.getIndex_int() - 1; 
		    continue phrase_loop; }
                if (sparse.IsLeafPOS(curr_node)) {
                    label= curr_node.getLabel();
                    daughter = sparse.FirstDaughter(curr_node);
                    text = daughter.getLabel();
                    if (label.equals("ID")) {
                        //out_file.println("");
			ur_str += "  (" + text + ")";
                        continue phrase_loop; } 
                    // insert line break only if there are two "LB"s in a row.
                    if (label.equals("LB")) {
                        if (prev_LB) {
                            //out_file.println("");
                            length = 0;
                            prev_LB = false;
                            continue phrase_loop; }
                        if (!prev_LB) {
                            prev_LB = true;
                            continue phrase_loop; }
                    } // end if label.equals("LB")
		    if (ignore_for_ur.hasMatch(label)) {
			continue phrase_loop; }
                    if (LegitText(text, label, sparse.hasORTHO())) {
                        length += (text + " ").length();
			if (this.punct_arg.hasMatch(text)) {
                            ur_str += (text);
                            length -= 1;
                            continue phrase_loop; }
			if (length > 150) {
			    ur_str += "\n";
                            //out_file.println("");
			    length = (text + " ").length(); }
                        if (no_space_next && is_open_quote) {
                            ur_str += text;
                            length -= 1;
                            no_space_next = false;
                            is_open_quote = false;
                            continue phrase_loop; }
			if (this.quote_arg.hasMatch(text)) {
                            // print close quote with no prefix space.
                            if (!is_open_quote) {
                                ur_str += text;
                                length -= 1;
                                is_open_quote = true;
                                no_space_next = false;
                                continue phrase_loop; }
                            else { // is_open_quote is true.
                                no_space_next = true; }
                        } // end if (IsQuote(text))
                        // print first word in sentence without prefix space.
                        if (length == (text + " ").length()) {
                            ur_str += text;
                            length -= 1;
                            continue phrase_loop; }
			ur_str += " " + text; 
                    } //  end if LegitText(text)
                } // end if sparse.IsLeafPOS(n);
                prev_LB = false;
            } // end for n = 1; n < phrase_vec.size();
        } // end try
        catch (Exception e) {
            System.err.println("in UrText.SubPure:  sparse");
            sparse.PrintToSystemErr();
            e.printStackTrace(); }
        finally { return ur_str; } }

    // used by CorpusDraw scrollToWordDex.
    public Vector toScrollVec(SynTree sparse) {
        int n;
        Node curr_node, daughter, sis_node;
        String text = "", label = "", without_plus = "";
        boolean prev_LB = false;
	Vector scroll_vec = new Vector();

        try {
	    phrase_loop:  for (n = 1; n < sparse.size(); n++) {
                curr_node = sparse.NodeAt(n);
		if (curr_node.getLabel().equals("METADATA")) {
		    sis_node = sparse.nextSister(curr_node);
		    if (sis_node.IsNullNode()) { break phrase_loop; }
		    n = sis_node.getIndex_int() - 1; 
		    continue phrase_loop; }
                if (sparse.IsLeafPOS(curr_node)) {
                    label= curr_node.getLabel();
                    daughter = sparse.FirstDaughter(curr_node);
                    text = daughter.getLabel();
                    if (label.equals("ID")) {
			scroll_vec.addElement(daughter);
                        continue phrase_loop; } 
                    if (label.equals("LB")) {
			continue phrase_loop; }
		    if (ignore_for_ur.hasMatch(label)) {
			continue phrase_loop; }
                    if (LegitText(text, label, sparse.hasORTHO())) {
			if (this.punct_arg.hasMatch(text) ||
			    this.quote_arg.hasMatch(text)) {
                            continue phrase_loop; }
			scroll_vec.addElement(daughter); } } } } 
        catch (Exception e) {
            System.err.println("in UrText.toScrollVec: ");
            e.printStackTrace();
	    System.exit(1); }
        finally { return scroll_vec; } }

    /*
      PureText -- prints the contents of a sentence vector without
      parentheses and labels (the original text).
    */
    public void PureText (SynTree sparse) {

	System.out.println("");
        System.out.println("/~*");
        subPure (sparse);
        System.out.println("");
        System.out.println("*~/");
        return;
    } // end PureText

    /*
      subPure -- prints the contents of a sentence vector without
      parentheses and labels (the original text.) 
    */
    private void subPure (SynTree sparse) {
        int n;
        Node curr_node, daughter, sis_node;
        String text = "", label = "", without_plus = "";
        boolean prev_LB = false;

        length = 0;
	is_open_quote = true;
	no_space_next = false;
        try {
	    phrase_loop:  for (n = 1; n < sparse.size(); n++) {
                curr_node = sparse.NodeAt(n);
		if (curr_node.getLabel().equals("METADATA")) {
		    sis_node = sparse.nextSister(curr_node);
		    if (sis_node.IsNullNode()) { break phrase_loop; }
		    n = sis_node.getIndex_int() - 1; 
		    continue phrase_loop; }
                if (sparse.IsLeafPOS(curr_node)) {
                    label= curr_node.getLabel();
                    daughter = sparse.FirstDaughter(curr_node);
                    text = daughter.getLabel();
                    if (label.equals("ID")) {
                        System.out.println("");
                        System.out.print("(" + text + ")");
                        continue phrase_loop; } 
                    // insert line break only if there are two "LB"s in a row.
                    if (label.equals("LB")) {
                        if (prev_LB) {
                            System.out.println("");
                            length = 0;
                            prev_LB = false;
                            continue phrase_loop; }
                        if (!prev_LB) {
                            prev_LB = true;
                            continue phrase_loop; }
                    } // end if label.equals("LB")
		    if (ignore_for_ur.hasMatch(label)) {
			continue phrase_loop; }
                    if (LegitText(text, label, sparse.hasORTHO())) {
                        length += (text + " ").length();
			if (this.punct_arg.hasMatch(text)) {
                            System.out.print(text);
                            length -= 1;
                            continue phrase_loop; }
                        if (length > margin) {
                            System.out.println("");
                            length = (text + " ").length(); }
                        if (no_space_next && is_open_quote) {
                            System.out.print(text);
                            length -= 1;
                            no_space_next = false;
                            is_open_quote = false;
                            continue phrase_loop; }
			if (this.quote_arg.hasMatch(text)) {
                            // print close quote with no prefix space.
                            if (!is_open_quote) {
                                System.out.print(text);
                                length -= 1;
                                is_open_quote = true;
                                no_space_next = false;
                                continue phrase_loop; }
                            else { // is_open_quote is true.
                                no_space_next = true; }
                        } // end if (IsQuote(text))
                        // print first word in sentence without prefix space.
                        if (length == (text + " ").length()) {
                            System.out.print(text);
                            length -= 1;
                            continue phrase_loop; }
                        System.out.print(" " + text);
                    } //  end if LegitText(text)
                } // end if sparse.IsLeafPOS(n);
                prev_LB = false;
            } // end for n = 1; n < phrase_vec.size();
        } // end try
        catch (Exception e) {
            System.err.println("in UrText.SubPure:  sparse");
            sparse.PrintToSystemErr();
            e.printStackTrace(); }
        finally { return; } }


    /*
      PureText -- prints the contents of a sentence vector without
      parentheses and labels (the original text).
    */
    public void PureText (SynTree sparse, PrintWriter out_file) {

	out_file.println("");
        out_file.println("/~*");
        subPure (sparse, out_file);
        out_file.println("");
        out_file.println("*~/");
        return;
    } // end PureText

    /*
      subPure -- prints the contents of a sentence vector without
      parentheses and labels (the original text.) 
    */
    private void subPure (SynTree sparse, PrintWriter out_file) {
        int n = -5;
        Node curr_node, daughter, sis_node;
        String text = "", label = "", without_plus = "";
        boolean prev_LB = false;

        length = 0;
	is_open_quote = true;
	no_space_next = false;
        try {
	    phrase_loop:  for (n = 1; n < sparse.size(); n++) {
		curr_node = sparse.NodeAt(n);
		if (curr_node.getLabel().equals("METADATA")) {
		    sis_node = sparse.nextSister(curr_node);
		    if (sis_node.IsNullNode()) { break phrase_loop; }
		    n = sis_node.getIndex_int() - 1; 
		    continue phrase_loop; }
                if (sparse.IsLeafPOS(curr_node)) {
                    label= curr_node.getLabel();
                    daughter = sparse.FirstDaughter(curr_node);
                    text = daughter.getLabel();
                    if (label.equals("ID")) {
                        out_file.println("");
                        out_file.print("(" + text + ")");
                        continue phrase_loop; } 
                    // insert line break only if there are two "LB"s in a row.
                    if (label.equals("LB")) {
                        if (prev_LB) {
                            out_file.println("");
                            length = 0;
                            prev_LB = false;
                            continue phrase_loop; }
                        if (!prev_LB) {
                            prev_LB = true;
                            continue phrase_loop; }
                    } // end if label.equals("LB")
		    if (ignore_for_ur.hasMatch(label)) {
			continue phrase_loop; }
                    if (LegitText(text, label, sparse.hasORTHO())) {
                        length += (text + " ").length();
			if (this.punct_arg.hasMatch(text)) {
                            out_file.print(text);
                            length -= 1;
                            continue phrase_loop; }
                        if (length > margin) {
                            out_file.println("");
                            length = (text + " ").length(); }
                        if (no_space_next && is_open_quote) {
                            out_file.print(text);
                            length -= 1;
                            no_space_next = false;
                            is_open_quote = false;
                            continue phrase_loop; }
			if (this.quote_arg.hasMatch(text)) {
                            // print close quote with no prefix space.
                            if (!is_open_quote) {
                                out_file.print(text);
                                length -= 1;
                                is_open_quote = true;
                                no_space_next = false;
                                continue phrase_loop; }
                            else { // is_open_quote is true.
                                no_space_next = true; }
                        } // end if (IsQuote(text))
                        // print first word in sentence without prefix space.
                        if (length == (text + " ").length()) {
                            out_file.print(text);
                            length -= 1;
                            continue phrase_loop; }
                        out_file.print(" " + text);
                    } //  end if LegitText(text)
                } // end if sparse.IsLeafPOS(n);
                prev_LB = false;
            } // end for n = 1; n < phrase_vec.size();
        } // end try
        catch (Exception e) {
            System.err.println("in UrText.SubPure:  sparse");
	    if (n > 0 && n < sparse.size()) {
		curr_node = sparse.NodeAt(n);
		System.err.print("problem around node:  ");
		System.err.println(curr_node.toString()); }
            sparse.PrintToSystemErr();
            e.printStackTrace(); }
        finally { return; } }

    /*
      LegitText -- determines whether a string is a legitimate 
      piece of text.
      "label" is from preterminal node, "text" from terminal node.
    */
    private boolean LegitText (String text, String label, boolean hasORTHO) {

	//System.err.println("hasORTHO:  " + hasORTHO);
	if (hasORTHO) {
	    if (label.equals("ORTHO")) { return true; }
	    return false; }
        if ((text.equals("0")) && (!(label.equals("NUM"))))
            return false;
	if (not_legit_arg.hasMatch(text)) {
	    return false; }
	return true; }


}




