/*
  Given a matrix representation of a tree, builds structured-list
  representation.
*/
package convert;

import java.io.*;
import java.util.*;

import syntree.*;

public class MatrixToVector {

    /*
      MtoV -- takes sentence in matrix form, converts to 
      sentence-vector form. 
      input -- Vector -- sentence in matrix form

      output -- Vector -- sentence in sentence-vector form.
    */
    public static Vector MtoV (SynTree spare) {
	Integer zero = new Integer(0);
	Vector sub_vec = new Vector();
	Vector sent_vec = new Vector();
 
	try {
	    //spare.PrintToSystemErr();
	    sub_vec.addElement(zero);
	    sent_vec = RecurseDaughters(spare, sent_vec, zero);
	    sent_vec.insertElementAt(sub_vec, 0);
	} // end try
	catch (RuntimeException e) {
	    System.err.println("Came to grief in MatrixToVector.");
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	    System.exit(1);
	} // end catch
	finally {
	    //System.err.println("MatrixToVector: sent_vec: " + sent_vec);
	    return sent_vec;
	} // end finally
    } // end method MtoV

    /*
      RecurseDaughters -- build sentence vector by recursively adding daughters.
      input -- mat_vec, sent_vec, mother_dex
      output -- sent_vec
    */
    public static Vector RecurseDaughters (SynTree spare, Vector sent_vec, 
					   Integer mother_dex) {
	Vector sub_vec = new Vector();
	Vector daughters, one_daughter, sub_sub_vec; 
	Vector sisters, sub_sister, sis_1, sis_2;
	Vector same_dex, leaf_vec;
	Integer start_dex, closures;
	String  label, daughter_label;
	int n, j, paren_dex;
	try { 

	    if (sub_vec.isEmpty()) {
		label = spare.LabelAt(mother_dex.intValue());
		if (spare.IsLeafPOS(mother_dex)) {
		    sub_vec = LeafStuff(spare, mother_dex);
		    sent_vec.addElement(sub_vec);
		}
		else {
		    sub_sub_vec = new Vector();
		    sub_sub_vec.addElement(mother_dex);
		    sub_sub_vec.addElement(label);
		    sub_vec.addElement(sub_sub_vec);
		    sent_vec.addElement(sub_vec);
		}
	    } // end if sub_vec.isEmpty()
	    if (!(spare.IsLeafPOS(mother_dex))) {
		daughters = spare.GetDaughters(mother_dex);
		//System.err.print("mother, daughters:  ");
		//System.err.println(spare.LabelAt(mother_dex) + ", " + daughters);
		for (n = 0; n < daughters.size(); n++) {
		    one_daughter = (Vector)daughters.elementAt(n);
		    start_dex = (Integer)one_daughter.elementAt(0);
		    if (!(spare.IsLeafPOS(start_dex))) { //  non-terminal node.
			RecurseDaughters(spare, sent_vec, start_dex);
		    }
		    else {
			sub_vec = LeafStuff(spare, start_dex);
			sent_vec.addElement(sub_vec);
			closures = NumClosures(spare, start_dex);
			// variable closures tells you how many times to nest object.
			for (j = 0; j < closures.intValue(); j++) {
			    if (sent_vec.size() > 1) {
				((Vector)sent_vec.elementAt(sent_vec.size() - 2)).
				    addElement((Vector)sent_vec.
					       elementAt(sent_vec.size() - 1));
				sent_vec.removeElementAt(sent_vec.size() - 1);
			    } // end if sent_vec.size() > 1
			} // end for (j = 0; j < closures.intValue(); j++
		    } // end else
		} // end for n = 0; n < daughters.size(); n++
	    } // end if !spare.IsLeafPOS(mother_dex)
	    //System.err.println("Sent_Vec:  " + sent_vec);
	    // add in sisters of the root (e.g. ID nodes.)
	    if (mother_dex.intValue() == 0) {
		sisters = spare.GetSisters(mother_dex);
		if (sisters.size() == 2) {
		    sub_sister = new Vector();
		    leaf_vec = new Vector();
		    sis_1 = (Vector)sisters.elementAt(0);
		    sis_2 = (Vector)sisters.elementAt(1);
		    sub_sister.addElement((Integer)sis_2.elementAt(0));
		    sub_sister.addElement((String)sis_1.elementAt(1));
		    sub_sister.addElement((String)sis_2.elementAt(1));
		    leaf_vec.addElement(sub_sister);
		    sent_vec.addElement(leaf_vec);    
		}
		if (sisters.size() > 2) {
		    System.err.println("WARNING! extra sisters:  " + sisters);
		}
	    } // end if mother_dex.intValue() == 0
	} // end try
	catch (Exception e) {
	    System.err.println("Came to grief in RecurseDaughters.");
	    e.printStackTrace();
	    System.err.println(e.getMessage());
	} // end catch
	finally {
	    return (sent_vec);
	} // end finally
    } // end method RecurseDaughters
 

    /*
      NumClosures -- 
      input -- mat_vec, start_dex
      output -- Integer -- number of phrases whose close coincides with the
      close of the node indicated by start_dex.  The node itself is counted.
    */
    public static Integer NumClosures (SynTree spare, Integer start_dex) {
	Integer orig_end_dex, end_dex, num_close;
	int n, close_int = 0;

	orig_end_dex = spare.EndDexAt(start_dex.intValue());
	for (n = 0; n < spare.size(); n++) {
	    end_dex = spare.EndDexAt(n);
	    if (end_dex.intValue() == orig_end_dex.intValue() && 
		!(spare.IsLeafPOS(n))) {
		close_int += 1;
	    } // end if end_dex.intValue() == orig_end_dex.intValue();
	} // end for n = 0; n < end_dex_vec.size(); n++
	num_close = new Integer(close_int);
	return num_close;
    } // end method NumClosures

    /*
      LeafStuff -- assembles leaf vector, given that start_dex is the
      index of a leaf POS.
      output -- sub-vec -- leaf vector, e.g. [[4, N, dog]]
    */
    public static Vector LeafStuff (SynTree spare, Integer start_dex) {

	Vector leaf_vec = new Vector(), sub_leaf = new Vector(); 
	String leaf_text, pos;
	Integer leaf_dex;
	Node leaf_node;
    
	pos = spare.LabelAt(start_dex);
	leaf_node = spare.NodeAt(start_dex.intValue() + 1);
	leaf_dex = leaf_node.getIndex();
	leaf_text = leaf_node.getLabel();
	sub_leaf.addElement(leaf_dex);
	sub_leaf.addElement(pos);
	sub_leaf.addElement(leaf_text);
	leaf_vec.addElement(sub_leaf);
	return leaf_vec;
    } // end method LeafStuff

} // end class MatrixToVector






