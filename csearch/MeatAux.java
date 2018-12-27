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
  Beth Randall:  August 2000 
  MeatAux.java
  Handles miscellaneous tasks for Meat.java.
*/
package csearch;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import command.*;
import print.*;
import syntree.*;
import basicinfo.*;
import io.*;


public class MeatAux extends Meat {

    public static Method which_vector_printer;

    /*
      ChooseVectorPrinter -- chooses appropriate vector printing
      method.
      input -- void.
      output -- void.
      side-effect -- variable which_vector_printer contains
      pointer to appropriate vector-printing method.
    */
    public static void ChooseVectorPrinter () {

        try {
            //if (CommandInfo.nodes_only) {
            //if (!CommandInfo.remove_nodes) {
            //		which_vector_printer = (Class.forName("PrintVec")).
            //  getMethod("Nodes", null);
            //	return;
            //    }
            //    if (CommandInfo.remove_nodes) {
            //	which_vector_printer = (Class.forName("Remove")).
            //	    getMethod("Nodes", null);
            //	return;
            //    }
            //} // end if CommandInfo.nodes_only
            which_vector_printer = (Class.forName("PrintTree")).
		getMethod("Sentence", null);
        } // end try
        catch (Exception e) {
            System.err.println("in MeatAux.ChooseVectorPrinter:  ");
            System.err.println(e.getMessage());
            Goodbye.SearchExit();
        } // end catch
        finally {
            return;
        }
    } // end method ChooseVectorPrinter

    /*
      CheckSource -- looks for source_file errors; 
      1.)  source-file same as destination file
      2.)  source_file doesn't exist or isn't file  
      3.)  source_file is unreadable.
      input -- void.
      output -- void.
      side-effect -- source_file has been checked; if it isn't OK,
      search is aborted.
    */
    public static void CheckSource () {

        try {
            if (file_name.equals(dest_name)) {
                System.err.println("in MeatAux.CheckSource:  ");
                System.err.print("ERROR!  " + dest_name);
                System.err.print(" being used as both output ");
                System.err.println("and input file.");
                System.err.println("Search aborted.");
                Goodbye.SearchExit();
            } // end if file_name.equals(dest_name)
            if (!source_file.exists() || !source_file.isFile()) {
                System.err.println("in MeatAux.CheckSource:  ");
                System.err.print("ERROR!  no such source file:  ");
                System.err.println(file_name);
                Goodbye.SearchExit();
            }
            if (!source_file.canRead()) {
                System.err.println("in MeatAux.CheckSource:  ");
                System.err.print("ERROR!  Source file is unreadable: ");
                System.err.println(file_name);
                Goodbye.SearchExit();
            }
        } // end try
        catch (Exception e) {
            System.err.print("ERROR! in MeatAux.CheckSource:  ");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("SearchAborted.");
            Goodbye.SearchExit();
        } // end catch
        finally{
            return;
        }
    } // end CheckSource

    /*
      EmptySourceError -- handles empty source error, that is, 
      a source file with no parsed sentences. 
      input -- void.
      output -- void.
      side-effect -- error messages printed to screen and output file.
    */
    public static void EmptySourceError () {
        System.err.println("");
        System.err.print("WARNING!  Input file " + file_name);
        System.err.println(" contains no parsed sentences.");
        System.err.println("");
        //	if (file_info.isEmpty())
        //  file_info.addElement(file_name);
        err_message = "    ";
        err_message += "WARNING!  Input file " + file_name;
        err_message += " contains no parsed sentences.";
        return;
    } // end method EmptySourceError

    /*
      Consolidate -- consolidates the file vector.  If the input to
      CorpusSearch consisted of multiple output files, the
      file vector might look like this: 
      AELR  3/1/10
      ...
      VICES 5/6/18
      AELR 2/4/12
      ...
      VICES 7/9/22	
      After Consolidate has been called, the file vector 
      would look like this:
      AELR 5/5/22
      ...
      VICES 12/15/40
      input -- Vector file-vec -- vector containing names of corpus
      files, number of hits, number of sentences, total
      number of sentences. 
      output -- Vector consol-vec -- consolidated version of input
      vector.
    */
    public static Vector Consolidate (Vector file_vec) {
        String file_name = "file not found";
        String consol_name = "consolidated file not found";
        Integer file_hits, file_sent, file_total;
        Integer consol_hits, consol_sent, consol_total;
        Integer sum_hits, sum_sent, sum_total;
        Vector one_file = new Vector(), one_consol;
        Vector consol_vec = new Vector();
        Vector sum_vec = new Vector();
        boolean found_file = false;
        int i, j, k;

        try {
	    file_vec_loop:  for (i = 0; i < file_vec.size();  i++) {
                one_file = (Vector)file_vec.elementAt(i);
                file_name = (String)one_file.elementAt(0);
                for (j = 0; j < consol_vec.size(); j++) {
                    one_consol = (Vector)consol_vec.elementAt(j);
                    consol_name = (String)one_consol.elementAt(0);
                    if (file_name.equals(consol_name)) {
                        sum_vec = new Vector();
                        sum_vec.addElement(file_name);
                        file_hits = (Integer)one_file.elementAt(1);
                        consol_hits = (Integer)one_consol.elementAt(1);
                        sum_hits  = new Integer(file_hits.intValue()
                                                + consol_hits.intValue());
                        file_sent = (Integer)one_file.elementAt(2);
                        consol_sent = (Integer)one_consol.elementAt(2);
                        sum_sent = new Integer(file_sent.intValue()
                                               + consol_sent.intValue());
                        file_total = (Integer)one_file.elementAt(3);
                        consol_total = (Integer)one_consol.elementAt(3);
                        sum_total = new Integer(file_total.intValue()
                                                + consol_total.intValue());
                        sum_vec.addElement(sum_hits);
			sum_vec.addElement(sum_sent);
                        sum_vec.addElement(sum_total);
                        consol_vec.setElementAt(sum_vec, j);
                        continue file_vec_loop;
                    } // end if file_name.equals(consol_name);
                } // end for j = 0; j < consol_vec.size(); j++
                // if you got this far, the entry in file_vec has no twin in consol_vec.
                // So put it straight into consol_vec.
                consol_vec.addElement(one_file);
            } // end file_vec_loop:  for i = 0; i < file_vec_size(); i++
        } // end try
        catch (Exception e) {
            System.err.println("in Consolidate: ");
            System.err.println("one_file:  " + one_file);
            System.err.println(e.getMessage());
            System.err.println(e.toString());
            //        e.printStackTrace();
            System.err.println("");
            System.err.println("You may need to add this line to your query file:");
            System.err.println("corpus_file_extension:  <extension>");
            System.err.println("");
        } // end catch
        finally {
            return consol_vec;
        } // end finally
    } // end method Consolidate


} // end class MeatAux.java


