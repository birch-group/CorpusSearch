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
import basicinfo.*;

/**
 * contains data needed for printing output.
 */
public class Parameters {

    public static boolean remark_exists = false;
    public static Vector remark;
    public static int margin = 78;
    public static boolean nodes_only = false;
    public static boolean remove_nodes = false;
    public static boolean print_indices = false;
    //    public static boolean has_prefs = false;
    //public static String prefs_name = "GRAVEYARD_WORDS";
    public static String corpus_file_extension = ".psd";
    public static ArgList species_list;
    public static boolean remove_ignore_nodes = true;
    public static UrText urk;
    public static boolean ur_text_only = false;
    public static String output_format = "QINGTIAN";

    public static void Init() {
	if (remove_nodes) {
	    nodes_only = true;
	    species_list = MakeSpeciesList(Vitals.Node_List.toVector()); }
	urk = new UrText();
    }

    public static void PrintLines(PrintWriter out_file, int how_many) {
	for ( int i = 0; i < how_many; i++) {
	    out_file.println(""); }
	return; }

    public static void setOutputFormat (String form) {
	output_format = form; 
        output_format = output_format.toUpperCase(); }

    /**
     * makes list of node species.
     */
    public static ArgList MakeSpeciesList (Vector node_list) {
        String species = "";
        ArgList sparg = new ArgList();
        int i;

        for (i = 0; i < node_list.size(); i++) {
            species = GetSpecies(((RegExpStr)node_list.elementAt(i)).
				 getOrig());
            sparg.addToBucket(species); }
        return sparg; }

    /**
     * sets node_species variable, depending on
     * value of node variable.  e.g., if node variable
     * is IP-MAT*, node_species is IP*.
     */
    public static String GetSpecies (String node) {
        String node_species = "";
        int hyphen_dex = -1;

        node_species = node;
        hyphen_dex = node_species.indexOf('-');
        if (hyphen_dex > 0) {
            node_species = node_species.substring(0, hyphen_dex); }
        if (!node_species.endsWith("*")) {
            node_species += "*"; }
        return node_species; }

}




