
/*

	VectorToStoreMatrix -- Takes a tree in structured-list form, builds
	matrix form representing tree.

	The matrix built here is in storage form -- i.e., compressed, with 
	parentheses representing subnodes.  Use 
	matrix/syntree.CorrectForParens to uncompress. 
*/
package convert;

import java.io.*;
import java.util.*;

import syntree.*;

public class VectorToStoreMatrix {

    /*
    	VtoM -- Converts sentence vector to matrix vector.
    	input -- sentence vector.
    	output -- sentence vector described as matrix vector, as follows:
    		0.) list of node labels
    		1.) list of end anchors, corresponding to 0.)
    */
    public static SynTree VtoM (Vector sent_vec) {
        SynTree spare = new SynTree();

        return (subVtoM(spare, sent_vec));

    } // end method VtoM

    /*
    	subVtoM -- recursively called sub-method of VectorToMatrix.
    */
    public static SynTree subVtoM (SynTree spare, Vector phrase_vec) {
        Vector one_item;
        Vector sub_vec, daughter, curr_vec, sub_curr;
        int k, n, last;
        Integer start_dex, end_dex, leaf_dex;
        String old_text = "", text = "", text1 = "", text2 = "";

        //System.err.println("phrase_vec:  " + phrase_vec);
        one_item = (Vector)phrase_vec.elementAt(0);
        if (one_item.size() > 1) {
            text = (String)one_item.elementAt(1);
            start_dex = (Integer)one_item.elementAt(0);
            end_dex = GetEndDex(phrase_vec);
            last = spare.size() - 1;
	    if ((spare.size() > 0) && 
		(end_dex.equals(spare.EndDexAt(last)))) {
		old_text = spare.LabelAt(last);
		text = old_text + "(" + text;
		spare.SetLabelAt(text, last);
            }
            else {
                spare.AddItem(text, Less1(end_dex));
	    } // end else
        } // end if one_item.size()  > 1;
        for (n = 1; n < phrase_vec.size(); n++) {
            curr_vec = (Vector)phrase_vec.elementAt(n);
            if (curr_vec.size() > 1) {
                spare = subVtoM(spare, curr_vec);
            }
            else { // word vector.
                sub_curr = (Vector)curr_vec.elementAt(0);
                text1 = (String)sub_curr.elementAt(1);
                text2 = (String)sub_curr.elementAt(2);
                text = text1 + "(" + text2;
                last = spare.size() - 1;
                start_dex = (Integer)sub_curr.elementAt(0);
                if (spare.size() > 0 &&
                        (Less1(start_dex)).equals(spare.EndDexAt(last))) {
                    old_text = spare.LabelAt(last);
                    text = old_text + "(" + text;
                    spare.SetLabelAt(text, last);
                    spare.SetEndDexAt(Less1(start_dex), last);
                }
                else {
                    leaf_dex = new Integer(start_dex.intValue());
                    spare.AddItem(text, Less1(leaf_dex));
                } // end else
            } // end else (word vector).
        } // end for n = 1; n < phrase_vec.size(); n++
        return spare;
    } // end method subVtoM

    /*
    	GetEndDex -- given vector, finds the index of its last entry.
    	input -- Vector
    	output -- Integer -- index.
    */
    public static Integer GetEndDex (Vector vec) {
        int last_dex;

        last_dex = vec.size() - 1;
        if (vec.elementAt(last_dex) instanceof Vector) {
            return(GetEndDex((Vector)vec.elementAt(last_dex)));
        }
        else {
            return ((Integer)vec.elementAt(0));
        }
    } // end method GetEndDex


    public static Integer Less1 (Integer original) {
        Integer less1 = new Integer(original.intValue() - 1);
        return less1;
    }


} // end class VectorToMatrix






